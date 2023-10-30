package com.example.antik;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class Signup extends AppCompatActivity {

    private EditText signupEmail, signupPassword;
    private Button signupButton;
    private String email, password;
    private TextView signinButton ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signupEmail = findViewById(R.id.signup_email);
        signupPassword = findViewById(R.id.signup_password);
        signupButton = findViewById(R.id.signup_button);
        signinButton = findViewById(R.id.loginRedirectText);
       // userDatabase = new UserDatabase(); // Simule une base de données utilisateur

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = signupEmail.getText().toString().trim();
                password = signupPassword.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(Signup.this, "Email and password cannot be empty", Toast.LENGTH_SHORT).show();
                }else{
                    startActivity(new Intent(Signup.this, signinn.class));
                }

            }

        });

        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                    startActivity(new Intent(Signup.this, signinn.class));


            }
        });
    }
}
