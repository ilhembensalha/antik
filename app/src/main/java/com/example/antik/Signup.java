package com.example.antik;

import retrofit2.Response;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

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
                    // Log to verify the click event
                    Log.d("Signup", "Signup button clicked");
                    registerUser(email, password);
                }
            }
        });
        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(Signup.this, signinn.class));
            }
        });
    }

    // Inside your Signup activity

    private void registerUser(String email, String password) {
        ApiService apiService = ApiClient.getApiService();

        // Create a Map to hold the POST parameters
        Map<String, String> parameters = new HashMap<>();
        parameters.put("name", "ilhem");
        parameters.put("email", email);
        parameters.put("password", password);

        Call<ApiResponse> call = apiService.registerUser(parameters);

        // Add the token to the headers
        call.enqueue(new retrofit2.Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    ApiResponse apiResponse = response.body();
                    if (apiResponse != null && apiResponse.getSuccess() != null) {
                        Toast.makeText(Signup.this, "Inscription réussie", Toast.LENGTH_SHORT).show();

                    } else if (apiResponse.getError() != null) {
                        Toast.makeText(Signup.this, apiResponse.getError(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    int responseCode = response.code();
                    Log.e("Response Code", String.valueOf(responseCode));
                    Toast.makeText(Signup.this, "Erreur 2, Response Code: " + responseCode, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                // Try to parse the exception message as a JSON object
                String jsonResponse = t.getMessage();
                try {
                    JSONObject jsonObject = new JSONObject(jsonResponse);
                    String errorMessage = jsonObject.getString("message");

                    // Display a Toast message with the extracted error message
                    Toast.makeText(Signup.this, "Erreur 2: " + errorMessage, Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    // If there's a JSONException, check if the exception message starts with "{"
                    if (jsonResponse != null && jsonResponse.startsWith("{")) {
                        // Try to parse the exception message again as a JSON object
                        try {
                            JSONObject jsonObject = new JSONObject(jsonResponse);
                            String errorMessage = jsonObject.getString("message");

                            // Display a Toast message with the extracted error message
                            Toast.makeText(Signup.this, "Erreur 2: " + errorMessage, Toast.LENGTH_LONG).show();
                        } catch (JSONException e2) {
                            // Display a Toast message with the default error message
                            Toast.makeText(Signup.this, "Erreur 2, JSONException: " + e2.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        // Display a Toast message with the default error message
                        Toast.makeText(Signup.this, "Erreur 2, JSONException: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });


    }


}
