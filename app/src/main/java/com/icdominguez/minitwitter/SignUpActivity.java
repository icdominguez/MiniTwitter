package com.icdominguez.minitwitter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnSigUp;
    private TextView tvGoLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        getSupportActionBar().hide();

        btnSigUp = findViewById(R.id.buttonSignUp);
        tvGoLogin = findViewById(R.id.textViewGoLogin);

        btnSigUp.setOnClickListener(this);
        tvGoLogin.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        int id = view.getId();

        switch (id) {
            case R.id.buttonSignUp:
                break;
            case R.id.textViewGoLogin:
                goToLogin();
                break;
        }
    }

    private void goToLogin() {
        Intent intentLogin = new Intent(SignUpActivity.this, MainActivity.class);
        startActivity(intentLogin);
    }
}