package gmail.com.ass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Dangki extends AppCompatActivity {
    EditText edTen, edMk, edEmail, edhoten;
    Button btnDangki, button , button2;
    ProgressBar progressBar;
    FirebaseAuth auth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dangki);
        edTen = findViewById(R.id.edTen);
        edMk = findViewById(R.id.edMk);
        edEmail = findViewById(R.id.edEmail);
        btnDangki = findViewById(R.id.btnDangki);
        auth = FirebaseAuth.getInstance();

        progressBar = findViewById(R.id.progressBar);
        button = findViewById(R.id.btndangki1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                Intent intent = new Intent(getApplicationContext(), Dangki.class);
                startActivity(intent);
            }
        });
        button2 = findViewById(R.id.btndangnhap);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                Intent intent = new Intent(getApplicationContext(), Dangnhap.class);
                startActivity(intent);

            }
        });

        btnDangki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edTen.getText().toString();
                String password = edMk.getText().toString();
                String email = edEmail.getText().toString();



                progressBar.setVisibility(View.VISIBLE);


                registerProcess(name, email, password);
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isComplete()){
//                            Toast.makeText(getApplicationContext(), "ok", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    }
    public void hideProgressBar() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    public void registerProcess(String name, String email, String password ) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RequestInterface requestInterface = retrofit.create(RequestInterface.class);
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        ServerRequest serverRequest = new ServerRequest();
        serverRequest.setOperation(Constants.REGISTER_OPERATION);
        serverRequest.setUser(user);
        Call<ServerResponse> responseCall = requestInterface.operation(serverRequest);
        responseCall.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> responseCall) {
                hideProgressBar(); // Ẩn ProgressBar khi nhận phản hồi từ máy chủ
                ServerResponse resp = responseCall.body();
                if (resp.getResult().equals(Constants.SUCCESS)) {
                    Toast.makeText(getApplicationContext(), resp.getMessage(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Dangki.this, Dangnhap.class);
                    startActivity(intent);

                } else {
                    Toast.makeText(getApplicationContext(), resp.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                hideProgressBar(); // Ẩn ProgressBar nếu có lỗi
                Log.d(Constants.TAG, "Lỗi");
            }
        });
    }
}
