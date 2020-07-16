package com.icdominguez.minitwitter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnLogin;
    TextView tvGoSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        btnLogin = findViewById(R.id.buttonLogin);
        btnLogin.setOnClickListener(this);

        tvGoSignUp = findViewById(R.id.textViewGoSignUp);
        tvGoSignUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.buttonLogin:
                break;
            case R.id.textViewGoSignUp:
                goToSignUp();
                break;
        }
    }

    private void goToSignUp() {
        Intent intentSignUp = new Intent(MainActivity.this, SignUpActivity.class);
        startActivity(intentSignUp);
        finish();
    }
}