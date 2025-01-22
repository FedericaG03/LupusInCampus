package com.example.lupusincampus;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private EditText etNickname, etEmail, etRegione, etPassword;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);  // Nome del tuo layout XML

        // Trova i componenti del layout
        etNickname = findViewById(R.id.etNickname);
        etEmail = findViewById(R.id.etEmail);
        etRegione = findViewById(R.id.etRegione);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);

        // Imposta il click listener sul bottone di registrazione
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ottieni i valori inseriti dagli utenti
                String nickname = etNickname.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String regione = etRegione.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                // Verifica che i campi non siano vuoti
                if (nickname.isEmpty() || email.isEmpty() || regione.isEmpty() || password.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Per favore, compila tutti i campi.", Toast.LENGTH_SHORT).show();
                } else {
                    // Puoi processare qui la registrazione, ad esempio inviarli a un server o salvarli localmente.
                    Toast.makeText(RegisterActivity.this, "Registrazione avvenuta con successo!", Toast.LENGTH_SHORT).show();

                    // Intent per tornare alla schermata di login
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class); // Sostituisci LoginActivity con il nome della tua Activity di login
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
