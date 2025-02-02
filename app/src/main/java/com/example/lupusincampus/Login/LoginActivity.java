package com.example.lupusincampus.Login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONObject;
import org.mindrot.jbcrypt.BCrypt;
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
        sharedActivity = new SharedActivity(this);

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
            String emailOrNickname = etEmailorNicK.getText().toString();
            String password = etPassword.getText().toString();

            // Funzione che simula una risposta dal server (per test locali)
            simulateServerResponse(emailOrNickname, password);

            if (emailOrNickname.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Compila tutti i campi!", Toast.LENGTH_SHORT).show();
            } else {
                boolean isEmail = isEmail(emailOrNickname);
                if (isEmail) {
                    Log.d("LoginActivity", "Email: " + emailOrNickname);
                } else {
                    Log.d("LoginActivity", "Nickname: " + emailOrNickname);
                }

                // Invia la richiesta di login
                serverConnector.loginRequest(emailOrNickname, password, isEmail, new ServerConnector.FetchDataCallback() {
                    @Override
                    public void onSuccess(String jsonResponse) {
                        try {
                            // Analizza la risposta del server (JSON)
                            JSONObject jsonObject = new JSONObject(jsonResponse);
                            String storedHash = jsonObject.getString("password"); // Password hashata

                            // Confronta la password immessa con quella hashata (BCrypt)
                            if (BCrypt.checkpw(password, storedHash)) {
                                sharedActivity.setLoggedIn(true);
                                sharedActivity.setEmail(jsonObject.getString("email")); // Salva email
                                sharedActivity.setNickname(jsonObject.getString("nickname")); // Salva nickname
                                Log.d("LoginActivity", "Login success, navigating to MainActivity");
                                navigateToMainActivity();
                            } else {
                                Toast.makeText(LoginActivity.this, "Credenziali errate!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(LoginActivity.this, "Errore nella risposta del server!", Toast.LENGTH_SHORT).show();
                            Log.e("LoginActivity", "Errore: " + e.getMessage(), e);
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e("LoginActivity", "Errore di comunicazione con il server", e);
                        Toast.makeText(LoginActivity.this, "Errore di connessione!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        btnRegister.setOnClickListener(v -> navigateToRegisterActivity());
        tvForgotPassword.setOnClickListener(v -> navigateToForgotPasswordActivity());
    }
    private boolean isEmail(String email){
        String emailPattern = "[a-z0-9._%+\\-]+@[a-z0-9.\\-]+\\.[a-z]{2,}$\n";
        return email.matches(emailPattern);
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void navigateToRegisterActivity() {
        Log.d("LoginActivity", "Navigating to RegisterActivity");
        Intent intent = new Intent(LoginActivity.this, com.example.lupusincampus.Login.RegisterActivity.class);
        startActivity(intent);
    }

    private void navigateToForgotPasswordActivity() {
        Log.d("LoginActivity", "Navigating to ForgotPasswordActivity");
        Intent intent = new Intent(LoginActivity.this, com.example.lupusincampus.Login.ForgotPasswordActivity.class);
        startActivity(intent);
    }

    /*private void testServerConn() {
        try {
            URL serverUrl = new URL("https://172.19.188.97:8080/");
            HttpURLConnection connection = (HttpURLConnection) serverUrl.openConnection();
            try (InputStream inputStream = new BufferedInputStream(connection.getInputStream())) {
                Log.d("Inzio LoginActivity", "OK ho letto " + inputStream.read());
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ServerConnector connector = new ServerConnector();
        connector.testServerConn("http://172.19.188.97:8080/");
    }*/

    // Funzione che simula una risposta dal server (per test locali)
    private void simulateServerResponse(String emailOrNickname, String password) {
        // Definiamo un esempio di email e password valide
        String validEmail = "user@example.com"; // Email di esempio
        String validNickname = "userNickname"; // Nickname di esempio
        String validPassword = "password";// Password di esempio

        if (emailOrNickname.equals(validEmail) || emailOrNickname.equals(validNickname)) {
            if (password.equals(validPassword)) {
                sharedActivity.setLoggedIn(true);
                sharedActivity.setEmail(validEmail);
                sharedActivity.setNickname(validNickname);
                Log.d("LoginActivity", "Login simulato riuscito, navigazione su MainActivity");
                navigateToMainActivity();
            } else {
                Toast.makeText(LoginActivity.this, "Credenziali errate!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(LoginActivity.this, "Email o nickname non trovati!", Toast.LENGTH_SHORT).show();
        }
    }

}
