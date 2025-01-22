package com.example.lupusincampus.Login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lupusincampus.R;

public class ForgotPasswordActivity extends AppCompatActivity {
    private EditText etEmail;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        // Inizializza i componenti UI
        etEmail = findViewById(R.id.etEmail);
        btnSubmit = findViewById(R.id.btnSubmit);

        // Imposta il listener per il bottone di invio
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString().trim();

                if (email.isEmpty()) {
                    Toast.makeText(ForgotPasswordActivity.this, "Inserisci una email valida!", Toast.LENGTH_SHORT).show();
                } else if (isValidEmail(email)) {
                    // Simulazione del recupero della password
                    Toast.makeText(ForgotPasswordActivity.this, "Istruzioni inviate all'email: " + email, Toast.LENGTH_SHORT).show();

                    // Intent per tornare alla schermata di login
                    Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class); // Sostituisci LoginActivity con il nome della tua Activity di login
                    startActivity(intent);

                    finish();  // Chiudi la schermata di recupero password e torna alla login
                } else {
                    Toast.makeText(ForgotPasswordActivity.this, "Email non valida", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Verifica se l'email ha un formato valido (puoi personalizzarlo o usare un'espressione regolare)
    private boolean isValidEmail(String email) {
        return email.contains("@") && email.contains(".");
    }
}
