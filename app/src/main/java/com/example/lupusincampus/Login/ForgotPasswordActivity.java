package com.example.lupusincampus.Login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lupusincampus.R;
import com.example.lupusincampus.ServerConnector;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class ForgotPasswordActivity extends AppCompatActivity {
    private EditText etEmail;
    private Button btnSubmit;
    View bckButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        etEmail = findViewById(R.id.etEmail);
        btnSubmit = findViewById(R.id.btnSubmit);
        bckButton = findViewById(R.id.back_btn);

        bckButton.setOnClickListener(v->{
            getOnBackPressedDispatcher().onBackPressed();
        });

        btnSubmit.setOnClickListener(v -> {
            String email = etEmail.getText().toString();

            if (email.isEmpty()) {
                Toast.makeText(ForgotPasswordActivity.this, "Inserisci una email valida!", Toast.LENGTH_SHORT).show();
            } else if (isValidEmail(email)) {
                // Invia la richiesta al server per il recupero password
                JSONObject requestBody = new JSONObject();
                try {

                    ;
                    /*

                    requestBody.put("email", email);
                    new ServerConnector().recoverPasswordRequest(this,email, new ServerConnector.CallbackInterface() {
                        @Override
                        public void onSuccess(String jsonResponse) {
                            try {
                                JSONObject response = new JSONObject(jsonResponse);
                                if (response != null) {
                                    Toast.makeText(ForgotPasswordActivity.this, "Istruzioni inviate all'email: " + email, Toast.LENGTH_SHORT).show();
                                    // Reindirizza alla schermata di login
                                    Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(ForgotPasswordActivity.this, "Email non trovata nel nostro sistema!", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                Toast.makeText(ForgotPasswordActivity.this, "Errore nella risposta del server!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError(String e) {
                            Toast.makeText(ForgotPasswordActivity.this, "Errore informazioni da cambiare : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onServerOffline(Exception e) {
                            Toast.makeText(ForgotPasswordActivity.this, "Errore nella connessione al server: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    */
                } catch (Exception ex) {
                    Toast.makeText(ForgotPasswordActivity.this, "Errore nella richiesta", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(ForgotPasswordActivity.this, "Email non valida", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Verifica se l'email ha un formato valido
    private boolean isValidEmail(String email) {
        String emailPattern = "[a-z0-9._%+\\-]+@[a-z0-9.\\-]+\\.[a-z]{2,}$\n";
        return email.matches(emailPattern);
    }
}
