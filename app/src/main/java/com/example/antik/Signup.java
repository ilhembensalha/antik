package com.example.antik;

import retrofit2.Callback;
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

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

public class Signup extends AppCompatActivity {

    private EditText signupEmail, signupPassword,confirmpassword;
    private Button signupButton;
    private TextView signinButton;
    private String email, password,confpassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signupEmail = findViewById(R.id.signup_email);
        signupPassword = findViewById(R.id.signup_password);
        signupButton = findViewById(R.id.signup_button);
        signinButton = findViewById(R.id.loginRedirectText);
        confirmpassword =findViewById(R.id.confirmpassword);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = signupEmail.getText().toString().trim();
                password = signupPassword.getText().toString().trim();
                confpassword = confirmpassword.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()|| !confpassword.equals(password) ) {
                    Toast.makeText(Signup.this, "Email and password cannot be empty and password equals confirm password", Toast.LENGTH_SHORT).show();
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
                startActivity(new Intent(Signup.this, signinn.class));
            }
        });
    }

    private void registerUser(String email, String password) {
        ApiService apiService = ApiClient.getApiService();

        // Create a Map to hold the POST parameters
        Map<String, String> parameters = new HashMap<>();
        parameters.put("name", "ilhem");
        parameters.put("email", email);
        parameters.put("password", password);

        // Convert the Map into a JSON object
        Gson gson = new Gson();
        String jsonParameters = gson.toJson(parameters);

        // Pass the JSON object as the @Body parameter
        Call<JsonObject> call = apiService.registerUser(parameters);

        // Add the token to the headers
        apiService.registerUser(parameters).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (!response.isSuccessful()) {
                    try {
                        Log.e("API Call Error", "Error 2, JSONException: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return;
                }

                String responseString = response.body().toString();
                Log.i("API Response", responseString);
                Intent intent = new Intent(Signup.this, signinn.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("API Call Error", "Error 1, Call failed: " + t.getMessage());
            }
        });


    }
    private void checkResponse(Response<ApiResponse> response) {
        if (response.isSuccessful()) {
            ApiResponse apiResponse = response.body();
            if (apiResponse != null && apiResponse.getSuccess() != null) {
                Log.e("apiResponse.getSuccess() ", String.valueOf(apiResponse.getSuccess() ));
                Toast.makeText(Signup.this, "Inscription réussie", Toast.LENGTH_SHORT).show();
            } else if (apiResponse.getError() != null) {
                Log.e("apiResponse.getError()", String.valueOf(apiResponse.getError()));
                Toast.makeText(Signup.this, apiResponse.getError(), Toast.LENGTH_LONG).show();
            }
        } else {
            int responseCode = response.code();
            Log.e("Response Code", String.valueOf(responseCode));
            Toast.makeText(Signup.this, "Erreur 2, Response Code: " + responseCode, Toast.LENGTH_LONG).show();
        }
    }

}
