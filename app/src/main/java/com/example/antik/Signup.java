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

    // ...

    private void registerUser(String email, String password) {
        // Create a HashMap for the parameters
        final HashMap<String, String> params = new HashMap<>();
        params.put("name", "ilhem");
        params.put("email", email);
        params.put("password", password);

        // Create an HTTP request (POST) to the server for registration
        StringRequest request = new StringRequest(Request.Method.POST, "https://192.168.1.113:8000/api/register",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle the server response here (successful registration or failure)
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            if (jsonResponse.has("success")) {
                                Toast.makeText(Signup.this, "Inscription réussie", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Signup.this, signinn.class));
                            } else if (jsonResponse.has("error")) {
                                Toast.makeText(Signup.this, jsonResponse.getString("error"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Signup.this, "Erreur de connexion au serveur: " + error.getMessage(), Toast.LENGTH_LONG).show();
                        signinButton.setText(error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                return params; // Pass the HashMap as the request parameters
            }
        };

        // Add the request to the Volley request queue
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

// ...


}
