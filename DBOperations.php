<?php

class DBOperations{

	 private $host = '127.0.0.1';
	 private $user = 'root';
	 private $db = 'learn-login-register';
	 private $pass = '';
	 private $conn;

public function __construct() {

	$this -> conn = new PDO("mysql:host=".$this -> host.";dbname=".$this -> db, $this -> user, $this -> pass);

}


 public function insertData($name,$email,$password){

 	$unique_id = uniqid('', true);
    $hash = $this->getHash($password);
    $encrypted_password = $hash["encrypted"];
	$salt = $hash["salt"];

 	$sql = 'INSERT INTO users SET unique_id =:unique_id,name =:name,
    email =:email,encrypted_password =:encrypted_password,salt =:salt,created_at = NOW()';

 	$query = $this ->conn ->prepare($sql);
 	$query->execute(array('unique_id' => $unique_id, ':name' => $name, ':email' => $email,
     ':encrypted_password' => $encrypted_password, ':salt' => $salt));

    if ($query) {

        return true;

    } else {

        return false;

    }
 }
 public function insertThongtin($manv, $tennv, $sdt, $diachi) {
    $unique_id = uniqid('', true);

    $sql = 'INSERT INTO nhanvien (unique_id, manv, tennv, sdt, diachi) VALUES (:unique_id, :manv, :tennv, :sdt, :diachi)';

    $query = $this->conn->prepare($sql);
    $result = $query->execute(array(
        ':unique_id' => $unique_id,
        ':manv' => $manv,
        ':tennv' => $tennv,
        ':sdt' => $sdt,
        ':diachi' => $diachi
    ));

    return $result; 
}




 public function checkLogin($name, $password) {

    $sql = 'SELECT * FROM users WHERE name = :name';
    $query = $this -> conn -> prepare($sql);
    $query -> execute(array(':name' => $name));
    $data = $query -> fetchObject();
    $salt = $data -> salt;
    $db_encrypted_password = $data -> encrypted_password;

    if ($this -> verifyHash($password.$salt,$db_encrypted_password) ) {


        $user["name"] = $data -> name;
        $user["email"] = $data -> email;
        $user["unique_id"] = $data -> unique_id;
        return $user;

    } else {

        return false;
    }

 }
 //tim
 public function timnhanvien($manv) {
    $sql = 'SELECT * FROM nhanvien WHERE manv = :manv';
    $query = $this->conn->prepare($sql);
    $query->execute(array(':manv' => $manv));
    $data = $query->fetch(PDO::FETCH_ASSOC);

    return $data;
}
//sua
 public function updateThongtin($manv, $tennv, $sdt, $diachi)
{
    $sql = 'UPDATE nhanvien
            SET tennv = :tennv, sdt = :sdt, diachi = :diachi
            WHERE manv = :manv';

    $query = $this->conn->prepare($sql);
    $query->execute(array(
        ':manv' => $manv,
        ':tennv' => $tennv,
        ':sdt' => $sdt,
        ':diachi' => $diachi
    ));

    return $query->rowCount() > 0;
}
//xoa
public function deleteThongtin($manv)
{
    $sql = 'DELETE FROM nhanvien WHERE manv = :manv';

    $query = $this->conn->prepare($sql);
    $query->execute(array(':manv' => $manv));

    return $query->rowCount() > 0;
}


 public function changePassword($name, $password){


    $hash = $this -> getHash($password);
    $encrypted_password = $hash["encrypted"];
    $salt = $hash["salt"];

    $sql = 'UPDATE users SET encrypted_password = :encrypted_password, salt = :salt WHERE name = :name';
    $query = $this -> conn -> prepare($sql);
    $query -> execute(array(':name' => $name, ':encrypted_password' => $encrypted_password, ':salt' => $salt));

    if ($query) {

        return true;

    } else {

        return false;

    }

 }

 public function passwordResetRequest($email){

    $random_string = substr(str_shuffle(str_repeat("0123456789abcdefghijklmnopqrstuvwxyz", 6)), 0, 6);
    $hash = $this->getHash($random_string);
    $encrypted_temp_password = $hash["encrypted"];
    $salt = $hash["salt"];

    $sql = 'SELECT COUNT(*) from password_reset_request WHERE email =:email';
    $query = $this -> conn -> prepare($sql);
    $query -> execute(array('email' => $email));

    if($query){

        $row_count = $query -> fetchColumn();

        if ($row_count == 0){


            $insert_sql = 'INSERT INTO password_reset_request SET email =:email,encrypted_temp_password =:encrypted_temp_password,
                    salt =:salt,created_at = :created_at';
            $insert_query = $this ->conn ->prepare($insert_sql);
            $insert_query->execute(array(':email' => $email, ':encrypted_temp_password' => $encrypted_temp_password,
            ':salt' => $salt, ':created_at' => date("Y-m-d H:i:s")));

            if ($insert_query) {

                $user["email"] = $email;
                $user["temp_password"] = $random_string;

                return $user;

            } else {

                return false;

            }


        } else {

            $update_sql = 'UPDATE password_reset_request SET email =:email,encrypted_temp_password =:encrypted_temp_password,
                    salt =:salt,created_at = :created_at';
            $update_query = $this -> conn -> prepare($update_sql);
            $update_query -> execute(array(':email' => $email, ':encrypted_temp_password' => $encrypted_temp_password,
            ':salt' => $salt, ':created_at' => date("Y-m-d H:i:s")));

            if ($update_query) {

                $user["email"] = $email;
                $user["temp_password"] = $random_string;
                return $user;

            } else {

                return false;

            }

        }
    } else {

        return false;
    }


 }

 public function resetPassword($email,$code,$password){


    $sql = 'SELECT * FROM password_reset_request WHERE email = :email';
    $query = $this -> conn -> prepare($sql);
    $query -> execute(array(':email' => $email));
    $data = $query -> fetchObject();
    $salt = $data -> salt;
    $db_encrypted_temp_password = $data -> encrypted_temp_password;

    if ($this -> verifyHash($code.$salt,$db_encrypted_temp_password) ) {

        $old = new DateTime($data -> created_at);
        $now = new DateTime(date("Y-m-d H:i:s"));
        $diff = $now->getTimestamp() - $old->getTimestamp();

        if($diff < 120) {

            return $this -> changePassword($email, $password);

        } else {

            false;
        }


    } else {

        return false;
    }

 }


 public function checkUserExist($name){

    $sql = 'SELECT COUNT(*) from users WHERE name =:name';
    $query = $this -> conn -> prepare($sql);
    $query -> execute(array('name' => $name));

    if($query){

        $row_count = $query -> fetchColumn();

        if ($row_count == 0){

            return false;

        } else {

            return true;

        }
    } else {

        return false;
    }
 }
 public function checkManv($manv){

    $sql = 'SELECT COUNT(*) from nhanvien WHERE manv =:manv';
    $query = $this -> conn -> prepare($sql);
    $query -> execute(array('manv' => $manv));

    if($query){

        $row_count = $query -> fetchColumn();

        if ($row_count == 0){

            return false;

        } else {

            return true;

        }
    } else {

        return false;
    }
 }

 public function getHash($password) {

     $salt = sha1(rand());
     $salt = substr($salt, 0, 10);
     $encrypted = password_hash($password.$salt, PASSWORD_DEFAULT);
     $hash = array("salt" => $salt, "encrypted" => $encrypted);

     return $hash;

}



public function verifyHash($password, $hash) {

    return password_verify ($password, $hash);
}
}
