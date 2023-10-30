package com.example.antik;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.security.AccessControlContext;
import java.util.HashMap;
import java.util.Map;

public class signinn extends AppCompatActivity {
        private EditText loginUsername, loginPassword;
        private Button loginButton;
        private TextView signupRedirectText,forgot_password;
        private Map<String, String> users; // Une simulation de base de données locale

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_signinn);

            loginUsername = findViewById(R.id.login_email);
            loginPassword = findViewById(R.id.login_password);
            loginButton = findViewById(R.id.login_button);
            signupRedirectText = findViewById(R.id.signUpRedirectText);
            forgot_password = findViewById(R.id.forgot_password);
            // Initialisez votre base de données locale d'utilisateurs.
            users = new HashMap<>();
            users.put("john@example.com", "password123");
            users.put("jane@example.com", "securePass");

            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String username = loginUsername.getText().toString();
                    String password = loginPassword.getText().toString();

                    if (users.containsKey(username) && users.get(username).equals(password)) {
                        // Authentification réussie
                        Toast.makeText(signinn.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(signinn.this, MainActivity.class));
                        finish();
                    } else {
                        // Authentification échouée
                        Toast.makeText(signinn.this, "Login Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            signupRedirectText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(signinn.this, Signup.class);
                    startActivity(intent);

                }
            });
            forgot_password.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();

                    // Créez un dialogue
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);

                    // Inflater le layout XML du dialogue
                    View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_forgot, null);
                    builder.setView(dialogView);

                    // Créez le dialogue
                    AlertDialog dialog = builder.create();

                    // Affichez le dialogue
                    dialog.show();

                }
            });
        }
    }
