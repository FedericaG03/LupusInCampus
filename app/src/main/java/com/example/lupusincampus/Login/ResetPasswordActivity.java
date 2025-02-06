package com.example.lupusincampus.Login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lupusincampus.R;
import com.example.lupusincampus.ServerConnector;

import org.json.JSONException;
import org.json.JSONObject;
import org.mindrot.jbcrypt.BCrypt;

public class ResetPasswordActivity extends AppCompatActivity {
    private EditText etNewPwd;
    private EditText etConPwd;
    private Button btnRest;
    private TextView btnBack;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_password);

        etNewPwd = findViewById(R.id.new_pwd);
        etConPwd = findViewById(R.id.confirm_pwd);
        btnRest = findViewById(R.id.btnResetPassword);
        btnBack =findViewById(R.id.back_btn);

        email = String.valueOf(getIntent().getExtras());


        btnRest.setOnClickListener(v -> {
            String newPassword = etNewPwd.getText().toString();
            if (newPassword.isEmpty() || newPassword.length() < 6) {
                Toast.makeText(this, "La password deve avere almeno 6 caratteri!", Toast.LENGTH_SHORT).show();
                return;
            }
            String hashPass = BCrypt.hashpw(newPassword, BCrypt.gensalt());
            updatePassword(email, hashPass);
        });
    }

    private void updatePassword(String email, String hashPass) {
        JSONObject requestBody = new JSONObject();
        try{
            requestBody.put("email", email);
            requestBody.put("new_password",hashPass);


            new ServerConnector().updatePasswordRequest(this,requestBody, new ServerConnector.FetchDataCallback() {
                @Override
                public void onSuccess(String jsonResponse) {
                    Toast.makeText(ResetPasswordActivity.this, "Password aggiornata con successo!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ResetPasswordActivity.this, LoginActivity.class));
                    finish();
                }

                @Override
                public void onError(Exception e) {
                    Toast.makeText(ResetPasswordActivity.this, "Errore nell'aggiornamento della password.", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (JSONException e) {
            Toast.makeText(this, "Errore nella richiesta.", Toast.LENGTH_SHORT).show();
        }
    }
}
