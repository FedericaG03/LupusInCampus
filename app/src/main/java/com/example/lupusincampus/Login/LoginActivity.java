package com.example.lupusincampus.Login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lupusincampus.MainActivity;
import com.example.lupusincampus.R;

public class LoginActivity extends AppCompatActivity {
    private EditText etEmail;
    private EditText etPassword;
    private Button btnLogin;
    private Button btnRegister;
    private TextView tvForgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        // Controllo se l'utente è già loggato
        SharedPreferences preferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        boolean isLoggedIn = preferences.getBoolean("isLoggedIn", false);
        Log.d("LoginActivity", "isLoggedIn: " + isLoggedIn);  // Aggiungi per il debug

        if (isLoggedIn) {
            navigateToMainActivity();
            return;
        }


        // Inizializza i componenti UI
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Compila tutti i campi!", Toast.LENGTH_SHORT).show();
                } else if (isValidCredentials(email, password)) {
                    preferences.edit()
                            .putBoolean("isLoggedIn", true)
                            .putString("username", email)
                            .apply();
                    Log.d("LoginActivity", "Login success, navigating to MainActivity");
                    navigateToMainActivity();
                } else {
                    Toast.makeText(LoginActivity.this, "Credenziali errate!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToRegisterActivity();
            }
        });

        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToForgotPasswordActivity();
            }
        });
    }

    private boolean isValidCredentials(String email, String password) {
        // TODO
        return email.equals("user@example.com") && password.equals("password");
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void navigateToRegisterActivity() {
        // TODO
        Log.d("LoginActivity", "Navigating to RegisterActivity");
        Intent intent = new Intent(LoginActivity.this, com.example.lupusincampus.Login.RegisterActivity.class);
        startActivity(intent);
    }

    private void navigateToForgotPasswordActivity() {
        // TODO
        Log.d("LoginActivity", "Navigating to RegisterActivity");
        Intent intent = new Intent(LoginActivity.this, com.example.lupusincampus.Login.ForgotPasswordActivity.class);
        startActivity(intent);
    }

}
