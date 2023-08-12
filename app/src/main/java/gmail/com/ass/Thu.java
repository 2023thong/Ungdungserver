package gmail.com.ass;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

import gmail.com.ass.Test.CanhanFragment;
import gmail.com.ass.Test.TRangchuFragment;
import gmail.com.ass.databinding.ActivityThuBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Thu extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView1;
    private FloatingActionButton buttontt;
    EditText ed1, ed2, ed3, ed4, ed5;
    Button btn1, btn2, btn3, btn4;

    ActivityThuBinding binding;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityThuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new TRangchuFragment());

        binding.bottomNavigation.setBackground(null);

        buttontt = findViewById(R.id.floatingActionButton);
        buttontt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                hienthi(Gravity.CENTER);
            }
        });



        binding.bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.trangchu){
                    replaceFragment(new TRangchuFragment());

                }
                else if(item.getItemId() == R.id.canhan){
                    replaceFragment(new CanhanFragment());
                }
                return true;
            }
        });


    }



    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fagme, fragment);
        fragmentTransaction.commit();
    }

    public void hienthi( int gravity) {
        final Dialog dialog  = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.themnn_item);
        Window window = dialog.getWindow();
        if(window ==null){
            return;
        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams window1 = window.getAttributes();
        window1.gravity = gravity;
        window.setAttributes(window1);

        if (Gravity.BOTTOM == gravity){
            dialog.setCancelable(true);
        }
        else{
            dialog.setCancelable(false);
        }

        ed1 = dialog.findViewById(R.id.edManv);
        ed2 = dialog.findViewById(R.id.edTennv);
        ed3 = dialog.findViewById(R.id.edSdt1);
        ed4 = dialog.findViewById(R.id.edDiachi1);
        btn1 = dialog.findViewById(R.id.button6);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
            }
        });
        btn3 = dialog.findViewById(R.id.button5);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String manv = ed1.getText().toString();
                String tennv = ed2.getText().toString();
                String sdt = ed3.getText().toString();
                String diachi = ed4.getText().toString();
                registerProcess1(manv, tennv, sdt, diachi);

            }
        });
        dialog.show();


    }
    public void registerProcess1(String manv, String tennv, String sdt, String diachi ) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RequestInterface requestInterface = retrofit.create(RequestInterface.class);
        User1 user1 = new User1();
        user1.setManv(manv);
        user1.setTennv(tennv);
        user1.setSdt(sdt);
        user1.setDiachi(diachi);
        ServerRequest serverRequest = new ServerRequest();
        serverRequest.setOperation(Constants.NHANVIEN_OPERATION);
        serverRequest.setUser1(user1);
        Call<ServerResponse> responseCall = requestInterface.operation(serverRequest);
        responseCall.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> responseCall) {
                ServerResponse resp1 = responseCall.body();
                if (resp1.getResult().equals(Constants.SUCCESS)) {
                    Toast.makeText(getApplicationContext(), resp1.getMessage(), Toast.LENGTH_SHORT).show();
                    replaceFragment(new TRangchuFragment());
                } else {
                    Toast.makeText(getApplicationContext(), resp1.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {

                Log.d(Constants.TAG, "Lỗi");
            }
        });
    }



}


