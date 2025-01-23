package com.example.lupusincampus.Login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lupusincampus.MainActivity;
import com.example.lupusincampus.R;
import com.example.lupusincampus.SalvataggioPassword.SharedActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    private EditText etNickname, etEmail, etPassword;
    private Button btnRegister;
    private SharedActivity sharedActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        sharedActivity = new SharedActivity(this);

        // Trova i componenti del layout
        etNickname = findViewById(R.id.etNickname);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(v -> {
            String nickname = etNickname.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (nickname.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "Per favore, compila tutti i campi.", Toast.LENGTH_SHORT).show();
            } else if (!email.contains("@")) {
                Toast.makeText(RegisterActivity.this, "Inserisci un'email valida.", Toast.LENGTH_SHORT).show();
            } else {
                String serverUrl = "http://your-server-url/register"; // Cambia con il tuo URL
                new ServerConnector().registerRequest(nickname, email, password, serverUrl, new ServerConnector.RegisterCallback() {
                    @Override
                    public void onResponse(String jsonResponse) {
                        runOnUiThread(() -> {
                            try {
                                JSONObject response = new JSONObject(jsonResponse);
                                if (response.getBoolean("success")) {
                                    sharedActivity.saveUserDetails(nickname, email, password, "email");

                                    Toast.makeText(RegisterActivity.this, "Registrazione avvenuta con successo!", Toast.LENGTH_SHORT).show();
                                    navigateToMainActivity();
                                } else {
                                    String errorMessage = response.optString("error", "Registrazione fallita. Riprova.");
                                    Toast.makeText(RegisterActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                Toast.makeText(RegisterActivity.this, "Errore nella risposta del server!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onError(Exception e) {
                        runOnUiThread(() -> {
                            Toast.makeText(RegisterActivity.this, "Errore di connessione: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
                    }
                });
            }
        });
    }

        private void navigateToMainActivity() {
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
