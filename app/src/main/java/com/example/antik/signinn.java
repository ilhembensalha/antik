package com.example.antik;

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
import org.json.JSONException;
import org.json.JSONObject;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class signinn extends AppCompatActivity {
    private EditText loginemail, loginPassword;
    private Button loginButton;
    private TextView signupRedirectText, forgot_password;
    private String serverUrl = "https://192.168.1.113:8000/"; // Remplacez par l'URL de votre API distante

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signinn);

        loginemail = findViewById(R.id.login_email);
        loginPassword = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_button);
        signupRedirectText = findViewById(R.id.signUpRedirectText);
        forgot_password = findViewById(R.id.forgot_password);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = loginemail.getText().toString();
                String password = loginPassword.getText().toString();

                // Effectuez une demande HTTP vers votre API distante pour l'authentification
                authenticateUser(email, password);
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

    private void authenticateUser(String email, String password) {
        try {
            JSONObject jsonRequest = new JSONObject();
            jsonRequest.put("email", email);
            jsonRequest.put("password", password);

            // Créez une demande HTTP (POST) vers votre API distante pour l'authentification
            StringRequest request = new StringRequest(Request.Method.POST, serverUrl + "api/login", // Utilisez la route d'authentification
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                if (jsonResponse.has("success")) {
                                    // Authentification réussie
                                    Toast.makeText(signinn.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(signinn.this, MainActivity.class));
                                    finish();
                                } else if (jsonResponse.has("error")) {
                                    // Authentification échouée
                                    Toast.makeText(signinn.this, jsonResponse.getString("error"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(signinn.this, "Erreur de connexion au serveur: " + error.getMessage(), Toast.LENGTH_SHORT).show();
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
