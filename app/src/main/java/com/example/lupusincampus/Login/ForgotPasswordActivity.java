package com.example.lupusincampus.Login;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lupusincampus.BaseActivity;
import com.example.lupusincampus.R;
import com.example.lupusincampus.API.PlayerAPI;


public class ForgotPasswordActivity extends BaseActivity {
    private EditText etEmail;
    private Button btnSubmit;
    private TextView bckButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password_activity);

        etEmail = findViewById(R.id.etEmail);
        btnSubmit = findViewById(R.id.btnSubmit);
        bckButton = findViewById(R.id.back_btn);

        bckButton.setOnClickListener(view -> getOnBackPressedDispatcher().onBackPressed());

        btnSubmit.setOnClickListener(v -> {
            String email = etEmail.getText().toString();

            if (email.isEmpty()) {
                Toast.makeText(ForgotPasswordActivity.this, "Inserisci una email valida!", Toast.LENGTH_SHORT).show();
            } else{
                PlayerAPI playerAPI = new PlayerAPI();
                playerAPI.doForgotPassword(email, getApplicationContext());
            }
        });
    }
}
