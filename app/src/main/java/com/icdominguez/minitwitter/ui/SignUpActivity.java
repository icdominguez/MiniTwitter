package com.icdominguez.minitwitter.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.icdominguez.minitwitter.R;
import com.icdominguez.minitwitter.common.Constants;
import com.icdominguez.minitwitter.common.SharedPreferencesManager;
import com.icdominguez.minitwitter.retrofit.MiniTwitterClient;
import com.icdominguez.minitwitter.retrofit.MiniTwitterService;
import com.icdominguez.minitwitter.retrofit.request.RequestSignUp;
import com.icdominguez.minitwitter.retrofit.response.ResponseAuth;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnSigUp;
    private TextView tvGoLogin;
    private EditText etUsername, etEmail, etPassword;

    private MiniTwitterClient miniTwitterClient;
    private MiniTwitterService miniTwitterService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        getSupportActionBar().hide();

        retrofitInit();
        findViews();
        events();
    }

    private void findViews() {
        btnSigUp = findViewById(R.id.buttonSignUp);
        tvGoLogin = findViewById(R.id.textViewGoLogin);
        etUsername = findViewById(R.id.editTextUsername);
        etEmail = findViewById(R.id.editTextEmail);
        etPassword = findViewById(R.id.editTextPassword);
    }

    private void events() {
        btnSigUp.setOnClickListener(this);
        tvGoLogin.setOnClickListener(this);
    }

    private void retrofitInit() {
        miniTwitterClient = MiniTwitterClient.getInstance();
        miniTwitterService = miniTwitterClient.getMiniTwitterService();
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();

        switch (id) {
            case R.id.buttonSignUp:
                goToSignUp();
                break;
            case R.id.textViewGoLogin:
                goToLogin();
                break;
        }
    }

    private void goToSignUp() {
        String username = etUsername.getText().toString();
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        if(username.isEmpty()) {
            etUsername.setError("El username es requerido");
        } else if(email.isEmpty()) {
            etEmail.setError("El email es requerido");
        } else if(password.isEmpty() || password.length() < 4) {
            etPassword.setError("La password es requerida y debe tener al menos 4 caracteres");
        } else {
            String code = "UDEMYANDROID";
            RequestSignUp requestSignUp = new RequestSignUp(username, email, password, code);
            miniTwitterService.doSignUp(requestSignUp);

            Call<ResponseAuth> call = miniTwitterService.doSignUp(requestSignUp);

            call.enqueue(new Callback<ResponseAuth>() {
                @Override
                public void onResponse(Call<ResponseAuth> call, Response<ResponseAuth> response) {
                    if(response.isSuccessful()) {

                        Toast.makeText(SignUpActivity.this, "Registro realizado", Toast.LENGTH_SHORT).show();

                        SharedPreferencesManager.setSomeStringValue(Constants.PREF_TOKEN, response.body().getToken());
                        SharedPreferencesManager.setSomeStringValue(Constants.PREF_USERNAME, response.body().getUsername());
                        SharedPreferencesManager.setSomeStringValue(Constants.PREF_EMAIL, response.body().getEmail());
                        SharedPreferencesManager.setSomeStringValue(Constants.PREF_PHOTOURL, response.body().getPhotoUrl());
                        SharedPreferencesManager.setSomeStringValue(Constants.PREF_CREATED, response.body().getCreated());
                        SharedPreferencesManager.setSomeBooleanValue(Constants.PREF_TOKEN, response.body().getActive());

                        Intent intentDashboard = new Intent(SignUpActivity.this, DashboardActivity.class);
                        startActivity(intentDashboard);
                        finish();

                    } else {
                        Toast.makeText(SignUpActivity.this, "Ups ... algo ha ido mal", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseAuth> call, Throwable t) {
                    Toast.makeText(SignUpActivity.this, "Se ha producido un error. Inténtelo de nuevo mas tarde", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    private void goToLogin() {
        Intent intentLogin = new Intent(SignUpActivity.this, MainActivity.class);
        startActivity(intentLogin);
    }
}