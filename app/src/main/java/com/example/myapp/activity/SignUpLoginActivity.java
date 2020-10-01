package com.example.myapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.model.ResponseToken;
import com.example.myapp.databinding.ActivitySignUpLoginBinding;
import com.example.myapp.network.GetDataService;
import com.example.myapp.network.RetrofitClientInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpLoginActivity extends AppCompatActivity {

    private ActivitySignUpLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        initViews();
        initListeners();
    }

    private void initViews() {
        binding.signUpBtn.setOnClickListener(v -> {
            String userName = binding.userNameET.getText().toString();
            String email = binding.emailET.getText().toString();
            String password = binding.passwordET.getText().toString();
            signIn(userName,email,password);

        });
    }

    private void signIn(String userName, String email, String password) {
        GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call <ResponseToken> call = getDataService.signUp(userName,email,password);
        call.enqueue(new Callback<ResponseToken>() {
            @Override
            public void onResponse(Call<ResponseToken> call, Response<ResponseToken> response) {
                        ResponseToken responseToken = response.body();
//                String t = responseToken.getToken().lastKey();
                int yy =88;
                int yry =88;
            }

            @Override
            public void onFailure(Call<ResponseToken> call, Throwable t) {

            }

        });

    }

    private void initListeners() {


    }
}