package com.example.lupusincampus.Login;

import static com.example.lupusincampus.Login.SHA256.hashSHA256;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lupusincampus.MainActivity;
import com.example.lupusincampus.R;
import com.example.lupusincampus.SharedActivity;
import com.example.lupusincampus.ServerConnector;

import org.json.JSONException;
import org.json.JSONObject;

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
            } else if (!email.contains("@")) {
                Toast.makeText(RegisterActivity.this, "Inserisci un'email valida.", Toast.LENGTH_SHORT).show();
            } else {
                new ServerConnector().registerRequest(this,nickname, email, hashPass, new ServerConnector.FetchDataCallback() {
                    @Override
                    public void onSuccess(String jsonResponse) {
                        runOnUiThread(() -> {
                            try {
                                JSONObject response = new JSONObject(jsonResponse);
                                if (response != null) {
                                    JSONObject value = response.getJSONObject("body");
                                    sharedActivity.saveUserDetails(String.valueOf(value.getInt("id")), value.getString("nickname"), value.getString("email"));
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

                    @Override
                    public void onServerOffline(Exception e) {
                        Toast.makeText(RegisterActivity.this, "Errore di connessione: " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
