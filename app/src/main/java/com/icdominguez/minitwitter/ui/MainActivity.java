package com.icdominguez.minitwitter.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.icdominguez.minitwitter.retrofit.request.RequestLogin;
import com.icdominguez.minitwitter.retrofit.response.ResponseAuth;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnLogin;
    private TextView tvGoSignUp;
    private EditText etEmail, etPassword;
    private MiniTwitterService miniTwitterService;
    private MiniTwitterClient miniTwitterClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        findViews();
        events();
        retrofitInit();
    }



    private void findViews() {
        btnLogin = findViewById(R.id.buttonLogin);
        etEmail = findViewById(R.id.editTextEmail);
        etPassword = findViewById(R.id.editTextPassword);
        tvGoSignUp = findViewById(R.id.textViewGoSignUp);
    }

    private void events() {
        btnLogin.setOnClickListener(this);
        tvGoSignUp.setOnClickListener(this);
    }

    private void retrofitInit() {
        miniTwitterClient = MiniTwitterClient.getInstance();
        miniTwitterService = miniTwitterClient.getMiniTwitterService();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.buttonLogin:
                goToLogin();
                break;
            case R.id.textViewGoSignUp:
                goToSignUp();
                break;
        }
    }

    private void goToLogin() {
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        if(email.isEmpty()) {
            etEmail.setError("El email es requerido");
        } else if (password.isEmpty()) {
            etPassword.setError("La contraseña es requerida");
        } else {
            RequestLogin requestLogin = new RequestLogin(email, password);
            Call<ResponseAuth> call = miniTwitterService.doLogin(requestLogin);

            call.enqueue(new Callback<ResponseAuth>() {
                @Override
                public void onResponse(Call<ResponseAuth> call, Response<ResponseAuth> response) {
                    if(response.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Sesion iniciada correctamente", Toast.LENGTH_SHORT).show();

                        SharedPreferencesManager.setSomeStringValue(Constants.PREF_TOKEN, response.body().getToken());
                        SharedPreferencesManager.setSomeStringValue(Constants.PREF_USERNAME, response.body().getUsername());
                        SharedPreferencesManager.setSomeStringValue(Constants.PREF_EMAIL, response.body().getEmail());
                        SharedPreferencesManager.setSomeStringValue(Constants.PREF_PHOTOURL, response.body().getPhotoUrl());
                        SharedPreferencesManager.setSomeStringValue(Constants.PREF_CREATED, response.body().getCreated());
                        SharedPreferencesManager.setSomeBooleanValue(Constants.PREF_TOKEN, response.body().getActive());

                        Intent intentDasboard = new Intent(MainActivity.this, DashboardActivity.class);
                        startActivity(intentDasboard);

                        finish();
                    } else {
                        Toast.makeText(MainActivity.this, "Usuario o contraseña incorrecto, revise sus datos", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseAuth> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "Se ha producido un error. Inténtelo de nuevo mas tarde", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void goToSignUp() {
        Intent intentSignUp = new Intent(MainActivity.this, SignUpActivity.class);
        startActivity(intentSignUp);
        finish();
    }
}