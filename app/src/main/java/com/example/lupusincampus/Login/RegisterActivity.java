package com.example.lupusincampus.Login;

import static com.example.lupusincampus.Login.SHA256.hashSHA256;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.lupusincampus.R;
import com.example.lupusincampus.API.PlayerAPI;
import com.example.lupusincampus.SharedActivity;

import me.pushy.sdk.Pushy;

public class RegisterActivity extends AppCompatActivity {

    private EditText etNickname, etEmail, etPassword;
    private Button btnRegister;
    private View bckButton;
    private SharedActivity sharedActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        sharedActivity = SharedActivity.getInstance(this);

        // Trova i componenti del layout
        etNickname = findViewById(R.id.etNickname);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);
        bckButton = findViewById(R.id.back_btn);


//        bckButton.setOnClickListener(v->{
  //          getOnBackPressedDispatcher().onBackPressed();

    //    });

        Pushy.listen(getApplicationContext());

        btnRegister.setOnClickListener(v -> {
            String nickname = etNickname.getText().toString();
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();
            String hashPass = hashSHA256(password);

            if (nickname.isEmpty() || email.isEmpty() || hashPass.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "Per favore, compila tutti i campi.", Toast.LENGTH_SHORT).show();
            //} //else if (!email.contains("@")) {
              //  Toast.makeText(RegisterActivity.this, "Inserisci un'email valida.", Toast.LENGTH_SHORT).show();
            } else {
                PlayerAPI playerAPI = new PlayerAPI();
                playerAPI.doRegister(email, hashPass,nickname, getApplicationContext(), sharedActivity);
            }
        });
    }
}
