package gmail.com.ass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class Quenmk extends AppCompatActivity {
    FirebaseAuth auth;
    EditText edqmk;
    Button button;
    String stremail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quenmk);
        edqmk = findViewById(R.id.edqmk);
        button = findViewById(R.id.button3);
        auth = FirebaseAuth.getInstance();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stremail = edqmk.getText().toString().trim();
                if (!TextUtils.isEmpty(stremail)){
                    ResetPass();

                }else{
                    edqmk.setError("Email ko hợp lệ");
                }

            }
        });
    }
    private void ResetPass(){

        auth.sendPasswordResetEmail(stremail).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(Quenmk.this, "Gửi xác thực thành công", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Quenmk.this, Dangnhap.class);
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Quenmk.this, "Error"+e.getMessage(), Toast.LENGTH_SHORT).show();


                    }
                });

    }
}