package gmail.com.ass.Test;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import gmail.com.ass.Constants;
import gmail.com.ass.Nhanvien;
import gmail.com.ass.R;
import gmail.com.ass.RequestInterface;
import gmail.com.ass.ServerRequest;
import gmail.com.ass.ServerResponse;
import gmail.com.ass.User1;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TRangchuFragment extends Fragment {
    private ListView lvUser;
    private Nhanvien adapter;
    private List<User1> lsuList = new ArrayList<>();
    private List<User1> filteredList = new ArrayList<>();
    private ProgressDialog pd;
    private String urllink = "http://192.168.1.9:8080/learn-login-register/get_all_product.php";
    private EditText etSearch;
    private Button btnSearch ,btnsua, btnxoa;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_t_rangchu, container, false);


        btnsua  = view.findViewById(R.id.btnSua);
        btnsua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Sua(Gravity.CENTER);
            }
        });
        btnxoa = view.findViewById(R.id.btnXoa);
        btnxoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Xoa(Gravity.CENTER);

            }
        });

        lvUser = view.findViewById(R.id.lvhienthi);

        adapter = new Nhanvien(getContext(), lsuList);
        lvUser.setAdapter(adapter);

        etSearch = view.findViewById(R.id.etSearch);
        btnSearch = view.findViewById(R.id.btnSearch);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchQuery = etSearch.getText().toString();
                if(searchQuery.isEmpty()) {
                    Toast.makeText(getContext(), "Bạn chưa nhập thông tin", Toast.LENGTH_SHORT).show();
                }else{
                    filterData(searchQuery);
                }

            }
        });

        // Load data from the server
        new MyAsyncTask().execute(urllink);

        return view;
    }

    private void filterData(String searchQuery) {
        filteredList.clear();
        for (User1 user : lsuList) {
            if (user.getManv().equalsIgnoreCase(searchQuery)) {
                filteredList.add(user);
            }

        }
        if (filteredList.isEmpty()) {
            Toast.makeText(getContext(), "Không tìm thấy mã nhân viên.", Toast.LENGTH_SHORT).show();
        }

        adapter = new Nhanvien(getContext(), filteredList);
        lvUser.setAdapter(adapter);
    }


    @SuppressLint("MissingInflatedId")
    public void Sua(int gravity) {
        final Dialog dialog = new Dialog(this.getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.sua_item);
        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams window1 = window.getAttributes();

        window1.gravity = gravity;
        window.setAttributes(window1);

        if (Gravity.BOTTOM == gravity) {
            dialog.setCancelable(true);
        } else {
            dialog.setCancelable(false);
        }
        EditText edTen = dialog.findViewById(R.id.edManvs);
        EditText edMk = dialog.findViewById(R.id.edTennvS);
        EditText edEmail = dialog.findViewById(R.id.edSdtS);
        EditText edhoten = dialog.findViewById(R.id.edDiachis);
        Button btnthem = dialog.findViewById(R.id.btnthoatsua);
        Button btnthoat = dialog.findViewById(R.id.btnSuas);

        btnthem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btnthoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String manv = edTen.getText().toString();
                String tennv = edMk.getText().toString();
                String sdt = edEmail.getText().toString();
                String diachi = edhoten.getText().toString();
                registerProcess2(manv, tennv, sdt, diachi);
            }
        });
        dialog.show();
    }

    public void registerProcess2(String manv, String tennv, String sdt, String diachi) {
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
        serverRequest.setOperation(Constants.SUA_OPERATION);
        serverRequest.setUser1(user1);
        Call<ServerResponse> responseCall = requestInterface.operation(serverRequest);
        responseCall.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> responseCall) {
                ServerResponse resp1 = responseCall.body();
                if (resp1.getResult().equals(Constants.SUCCESS)) {
                    Toast.makeText(getContext(), resp1.getMessage(), Toast.LENGTH_SHORT).show();
                    replaceFragment(new TRangchuFragment());
                } else {
                    Toast.makeText(getContext(), resp1.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Log.d(Constants.TAG, "Lỗi");
            }
        });
    }
    @SuppressLint("MissingInflatedId")
    public void Xoa(int gravity) {
        final Dialog dialog = new Dialog(this.getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.xoa_item);
        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams window1 = window.getAttributes();

        window1.gravity = gravity;
        window.setAttributes(window1);

        if (Gravity.BOTTOM == gravity) {
            dialog.setCancelable(true);
        } else {
            dialog.setCancelable(false);
        }
        EditText edTen = dialog.findViewById(R.id.edXoa);

        Button btnthem = dialog.findViewById(R.id.btnthoatx);
        Button btnthoat = dialog.findViewById(R.id.btnxoax);

        btnthem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btnthoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String manv = edTen.getText().toString();
//                String tennv = edMk.getText().toString();
//                String sdt = edEmail.getText().toString();
//                String diachi = edhoten.getText().toString();
                registerProcess1(manv);
            }
        });
        dialog.show();
    }
    public void registerProcess1(String manv) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RequestInterface requestInterface = retrofit.create(RequestInterface.class);
        User1 user1 = new User1();
        user1.setManv(manv);

        ServerRequest serverRequest = new ServerRequest();
        serverRequest.setOperation(Constants.XOA_OPERATION);
        serverRequest.setUser1(user1);
        Call<ServerResponse> responseCall = requestInterface.operation(serverRequest);
        responseCall.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> responseCall) {
                ServerResponse resp1 = responseCall.body();
                if (resp1.getResult().equals(Constants.SUCCESS)) {
                    Toast.makeText(getContext(), resp1.getMessage(), Toast.LENGTH_SHORT).show();
                    replaceFragment(new TRangchuFragment());
                } else {
                    Toast.makeText(getContext(), resp1.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Log.d(Constants.TAG, "Lỗi");
            }
        });
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fagme, fragment);
        fragmentTransaction.commit();
    }

    private class MyAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(getContext());
            pd.setMessage("Đang tải dữ liệu...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                String strJson = readJsonOnline(strings[0]);
                Log.d("//====", strJson);

                JSONObject jsonObject = new JSONObject(strJson);
                int success = jsonObject.getInt("success");
                if (success == 1) {
                    JSONArray jsonArrayNhanvien = jsonObject.getJSONArray("nhanvien");
                    Log.d("//=====size===", jsonArrayNhanvien.length() + "");

                    for (int i = 0; i < jsonArrayNhanvien.length(); i++) {
                        JSONObject nhanvienObject = jsonArrayNhanvien.getJSONObject(i);
                        Log.d("Manv", nhanvienObject.getString("manv"));
                        Log.d("Tennv", nhanvienObject.getString("tennv"));
                        Log.d("Sdt", nhanvienObject.getString("sdt"));
                        Log.d("Diachi", nhanvienObject.getString("diachi"));

                        String Manv = nhanvienObject.getString("manv");
                        String Tennv = nhanvienObject.getString("tennv");
                        String Sdt = nhanvienObject.getString("sdt");
                        String Diachi = nhanvienObject.getString("diachi");

                        User1 user1 = new User1();
                        user1.setManv(Manv);
                        user1.setTennv(Tennv);
                        user1.setSdt(Sdt);
                        user1.setDiachi(Diachi);
                        lsuList.add(user1);
                    }
                } else {
                    Log.d("Error: ", "Failed to fetch data. Success is not 1.");
                }
            } catch (JSONException e) {
                Log.d("Error: ", e.toString());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (pd.isShowing()) {
                pd.dismiss();
            }
            adapter.notifyDataSetChanged();
        }
    }

    public String readJsonOnline(String linkUrl) {
        HttpURLConnection connection = null;
        BufferedReader bufferedReader = null;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            URL url = new URL(linkUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }
            return stringBuilder.toString();
        } catch (Exception ex) {
            Log.d("Error: ", ex.toString());
        }
        return null;
    }

}
