package com.example.antik;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class signinn extends AppCompatActivity {
    private EditText loginemail, loginPassword;
    private Button loginButton;
    private TextView signupRedirectText, forgot_password;
    private String token ,id,name,email;

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
                loginUser(email, password);
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

    private void loginUser(String email, String password) {
        ApiService apiService = ApiClient.getApiServicein();
// Create a Map to hold the POST parameters
        Map<String, String> parameters = new HashMap<>();
        parameters.put("email", email);
        parameters.put("password", password);

        // Convert the Map into a JSON object
        Gson gson = new Gson();
        String jsonParameters = gson.toJson(parameters);

        // Pass the JSON object as the @Body parameter
        Call<JsonObject> call = apiService.loginUser(parameters);

        // Add the token to the headers
        apiService.loginUser(parameters).enqueue(new Callback<JsonObject>() {

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

                // Extract the token from the JSON object
                String token = null;
                String id = null;
                String name = null;
                String email = null;
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    JSONObject successObject = jsonObject.getJSONObject("success");
                    token = successObject.getString("token");
                    id = successObject.getString("id");
                    name = successObject.getString("name");
                    email = successObject.getString("email");
                    Log.i("API token", token);
                    Log.i("API id", id);
                } catch (JSONException e) {
                    Log.e("API Call Error", "Error 3, JSONException: " + e.getMessage());
                }

                // Save the token in Shared Preferences
                if (token != null) {
                    saveTokenInSharedPreferences(token,id,name,email);
                }
                Intent intent = new Intent(signinn.this, MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(signinn.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }


        });


    }
    private void saveTokenInSharedPreferences(String token,String id,String name,String email) {
        SharedPreferences sharedPreferences = getSharedPreferences("AppSettings", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("token", token);
        editor.putString("id", id);
        editor.putString("name", name);
        editor.putString("email", email);
        Log.e("shared preferences", "enregistre " +sharedPreferences.getString("token", token));
        Log.e("shared preferences", "enregistre " +sharedPreferences.getString("id", id));
        editor.apply();
    }
}
