package com.example.lupusincampus.Login;

import static com.example.lupusincampus.Login.SHA256.hashSHA256;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.lupusincampus.Server.PlayerAPI;
import androidx.appcompat.app.AppCompatActivity;
import com.example.lupusincampus.MainActivity;
import com.example.lupusincampus.R;
import com.example.lupusincampus.SharedActivity;

public class LoginActivity extends AppCompatActivity {
    private EditText etEmail;
    private EditText etPassword;
    private Button btnLogin;
    private Button btnRegister;
    private TextView tvForgotPassword;
    private SharedActivity sharedActivity;
    private static final String TAG = "LOGIN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        sharedActivity = SharedActivity.getInstance(this);

       if (sharedActivity.isLoggedIn()) {
            Log.i(TAG, "onCreate: User logged");
            navigateToMainActivity();
            finish();
       }

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);


        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();

            String hashPass = hashSHA256(password);
            Log.d("PASSWORD", "onCreate: hasspass " + hashPass);

            PlayerAPI playerAPI = new PlayerAPI();
            if (playerAPI.doLogin(email, hashPass, getApplicationContext(), sharedActivity)) {
                sharedActivity.setLoggedIn(true);
                navigateToMainActivity();
            }

        });

        btnRegister.setOnClickListener(v -> navigateToRegisterActivity());
        tvForgotPassword.setOnClickListener(v -> navigateToForgotPasswordActivity());
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void navigateToRegisterActivity() {
        Log.d("LoginActivity", "Navigating to RegisterActivity");
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private void navigateToForgotPasswordActivity() {
        Log.d("LoginActivity", "Navigating to ForgotPasswordActivity");
        Intent intent = new Intent(this, ForgotPasswordActivity.class);
        startActivity(intent);
    }

}
