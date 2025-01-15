package com.example.lupusincampus;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        Log.d(TAG, "onCreate: Activity creata");

        // Collega i componenti del layout
        EditText emailInput = findViewById(R.id.email_input);
        EditText passwordInput = findViewById(R.id.password_input);
        Button registerButton = findViewById(R.id.register_button);
        Button loginButton = findViewById(R.id.login_button);
        TextView forgotPassword = findViewById(R.id.forgot_password);

        // Collega il ViewModel
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        // Ripristina i dati salvati nel ViewModel
        emailInput.setText(viewModel.getEmail());
        passwordInput.setText(viewModel.getPassword());

        // Aggiorna i dati del ViewModel durante l'interazione
        registerButton.setOnClickListener(v -> {
            viewModel.setEmail(emailInput.getText().toString());
            viewModel.setPassword(passwordInput.getText().toString());
            Toast.makeText(LoginActivity.this, "Registrazione cliccata", Toast.LENGTH_SHORT).show();
        });

        loginButton.setOnClickListener(v -> {
            viewModel.setEmail(emailInput.getText().toString());
            viewModel.setPassword(passwordInput.getText().toString());
            Toast.makeText(LoginActivity.this, "Login cliccato", Toast.LENGTH_SHORT).show();
        });

        forgotPassword.setOnClickListener(v ->
                Toast.makeText(LoginActivity.this, "Password dimenticata!", Toast.LENGTH_SHORT).show()
        );
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Salva i dati del form
        EditText emailInput = findViewById(R.id.email_input);
        EditText passwordInput = findViewById(R.id.password_input);

        outState.putString("email", emailInput.getText().toString());
        outState.putString("password", passwordInput.getText().toString());

        Log.d(TAG, "onSaveInstanceState: Dati salvati");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // Ripristina i dati del form
        EditText emailInput = findViewById(R.id.email_input);
        EditText passwordInput = findViewById(R.id.password_input);

        emailInput.setText(savedInstanceState.getString("email"));
        passwordInput.setText(savedInstanceState.getString("password"));

        Log.d(TAG, "onRestoreInstanceState: Dati ripristinati");
    }

}
