package com.example.lupusincampus.Login;

import static com.example.lupusincampus.Login.SHA256.hashSHA256;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONObject;
import androidx.appcompat.app.AppCompatActivity;
import com.example.lupusincampus.MainActivity;
import com.example.lupusincampus.R;
import com.example.lupusincampus.SharedActivity;
import com.example.lupusincampus.ServerConnector;

public class LoginActivity extends AppCompatActivity {
    private EditText etEmailorNicK;
    private EditText etPassword;
    private Button btnLogin;
    private Button btnRegister;
    private TextView tvForgotPassword;
    private SharedActivity sharedActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        ServerConnector serverConnector = new ServerConnector();
        sharedActivity = SharedActivity.getInstance(this);

        boolean isLoggedIn = sharedActivity.isLoggedIn();
        Log.d("LoginActivity", "isLoggedIn: " + isLoggedIn);

        if (isLoggedIn) {
            navigateToMainActivity();
            return;
        }

        etEmailorNicK = findViewById(R.id.etEmailorNicK);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);


        btnLogin.setOnClickListener(v -> {
            String email = etEmailorNicK.getText().toString();
            String password = etPassword.getText().toString();

            String hashPass = hashSHA256(password);
            Log.d("PASSWORD", "onCreate: hasspass " + hashPass);

            // Funzione che simula una risposta dal server (per test locali)
            // simulateServerResponse(email, password);

            if (email.isEmpty() || hashPass.isEmpty()) {
                Toast.makeText(this, "Compila tutti i campi!", Toast.LENGTH_SHORT).show();
            } else {
                // Invia la richiesta di login
                serverConnector.loginRequest(email, hashPass,  new ServerConnector.FetchDataCallback() {
                    @Override
                    public void onSuccess(String jsonResponse) {
                        try {
                            // Analizza la risposta del server (JSON)
                            JSONObject jsonObject = new JSONObject(jsonResponse);
                            JSONObject value = jsonObject.getJSONObject("body");
                            String storedHash = value.getString("password"); // Password hashata

                            // Confronta la password immessa con quella hashata (BCrypt)
                            if (hashPass.equals(storedHash)) {
                                sharedActivity.setEmail(value.getString("email")); // Salva email
                                sharedActivity.setLoggedIn(true);
                                Log.d("LoginActivity", "Login success, navigating to MainActivity");
                                navigateToMainActivity();
                            } else {
                                Toast.makeText(getApplicationContext(), "Credenziali errate!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "Errore nella risposta del server!", Toast.LENGTH_SHORT).show();
                            Log.e("LoginActivity", "Errore: " + e.getMessage(), e);
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e("LoginActivity", "Errore di comunicazione con il server", e);
                        Toast.makeText(getApplicationContext(), "Errore di connessione!", Toast.LENGTH_SHORT).show();
                    }
                });
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
