package com.example.lupusincampus.Login;

import static com.example.lupusincampus.Login.SHA256.hashSHA256;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lupusincampus.BaseActivity;
import com.example.lupusincampus.R;
import com.example.lupusincampus.API.PlayerAPI;
import com.example.lupusincampus.SharedActivity;

import me.pushy.sdk.Pushy;

public class RegisterActivity extends BaseActivity {

    private EditText etNickname, etEmail, etPassword;
    private Button btnRegister;
    private View bckButton;
    private SharedActivity sharedActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        sharedActivity = SharedActivity.getInstance(this);

        // Trova i componenti del layout
        etNickname = findViewById(R.id.etNickname);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);
        bckButton = findViewById(R.id.back_btn);


        bckButton.setOnClickListener(v->{
            getOnBackPressedDispatcher().onBackPressed();
            finish();
        });

        Pushy.listen(getApplicationContext());

        btnRegister.setOnClickListener(v -> {
            String nickname = etNickname.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String hashPass = hashSHA256(password);

            if (nickname.isEmpty() || email.isEmpty() || hashPass.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "Per favore, compila tutti i campi.", Toast.LENGTH_SHORT).show();
            } else {
                PlayerAPI playerAPI = new PlayerAPI();
                playerAPI.doRegister(email, hashPass,nickname, getApplicationContext(), sharedActivity);
            }
        });
    }
}
