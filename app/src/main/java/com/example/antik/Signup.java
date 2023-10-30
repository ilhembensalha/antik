package com.example.antik;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.antik.R;
import com.example.antik.signinn;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Signup extends AppCompatActivity {

    private EditText signupEmail, signupPassword;
    private Button signupButton;
    private TextView signinButton;
    private String email, password;
    private String serverUrl = "http://10.0.2.2:3000"; // Remplacez par l'URL de votre serveur

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signupEmail = findViewById(R.id.signup_email);
        signupPassword = findViewById(R.id.signup_password);
        signupButton = findViewById(R.id.signup_button);
        signinButton = findViewById(R.id.loginRedirectText);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = signupEmail.getText().toString().trim();
                password = signupPassword.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(Signup.this, "Email and password cannot be empty", Toast.LENGTH_SHORT).show();
                } else {
                    // Effectuez une demande HTTP vers le serveur pour l'inscription
                    registerUser(email, password);
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

    private void registerUser(String email, String password) {
        try {
            JSONObject jsonRequest = new JSONObject();
            jsonRequest.put("username", email);
            jsonRequest.put("password", password);

            // Créez une demande HTTP (POST) vers le serveur
            StringRequest request = new StringRequest(Request.Method.POST, serverUrl + "/signup",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Gérez la réponse du serveur ici (inscription réussie ou échec)
                            if (response.equals("Inscription réussie")) {
                                Toast.makeText(Signup.this, "Inscription réussie", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Signup.this, signinn.class));
                            } else {
                                Toast.makeText(Signup.this, "Erreur d'inscription", Toast.LENGTH_LONG).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(Signup.this, "Erreur de connexion au serveur "+error.getMessage()+"", Toast.LENGTH_LONG).show();
                        }
                    }
            ) {
                @Override
                public byte[] getBody() {
                    return jsonRequest.toString().getBytes();
                }

                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }
            };

            // Ajoutez la demande à la file d'attente de Volley
            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
