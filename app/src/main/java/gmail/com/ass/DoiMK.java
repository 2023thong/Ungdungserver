package gmail.com.ass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import gmail.com.ass.Test.CanhanFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DoiMK extends AppCompatActivity {
    EditText Email, edMkcu, edMkmoi;
    Button btnDooi, btnthoat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doi_mk);
        Email = findViewById(R.id.edEmaildmk);
        edMkcu = findViewById(R.id.edMkdmk);
        edMkmoi = findViewById(R.id.edDmk1);
        btnDooi = findViewById(R.id.button4);
        btnDooi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerProcessdmk(Email.getText().toString(), edMkcu.getText().toString(), edMkmoi.getText().toString());
            }
        });
        btnthoat = findViewById(R.id.button7);
        btnthoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
    public void registerProcessdmk(String name, String old_password, String new_password) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RequestInterface requestInterface = retrofit.create(RequestInterface.class);
        User user = new User();
        user.setName(name);
        user.setOld_password(old_password);
        user.setNew_password(new_password);
        ServerRequest serverRequest = new ServerRequest();
        serverRequest.setOperation(Constants.CHANGE_PASSWORD_OPERATION);
        serverRequest.setUser(user);
        Call<ServerResponse> responseCall = requestInterface.operation(serverRequest);
        responseCall.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> responseCall) {
//                hideProgressBar(); // Ẩn ProgressBar khi nhận phản hồi từ máy chủ
                ServerResponse resp = responseCall.body();
                if (resp.getResult().equals(Constants.SUCCESS)) {
                    Toast.makeText(getApplicationContext(), resp.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), resp.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
//                hideProgressBar(); // Ẩn ProgressBar nếu có lỗi
                Log.d(Constants.TAG, "Lỗi"+t.getMessage());
            }
        });
    }
}