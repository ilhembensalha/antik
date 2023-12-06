package com.example.antik;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.gson.JsonObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShareFragment extends Fragment {

    private EditText nameEditText, emailEditText, passwordEditText,confirmpassword;
    private Button updateButton;
    private String  id, name, email,confirpassword;
    private ImageView imageViewProfile;
    private Uri selectedImageUri;
    private Bitmap bitmap;

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    public ShareFragment() {
        // Required empty public constructor
    }

    public static ShareFragment newInstance() {
        ShareFragment fragment = new ShareFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_share, container, false);

        nameEditText = view.findViewById(R.id.nameEditText);
        emailEditText = view.findViewById(R.id.emailEditText);
        passwordEditText = view.findViewById(R.id.passwordEditText);
        confirmpassword = view.findViewById(R.id.confirmpassword);
        updateButton = view.findViewById(R.id.updateButton);
        SharedPreferences preferences = requireContext().getSharedPreferences("AppSettings", Context.MODE_PRIVATE);

        // Retrieve user details from SharedPreferences
        id = preferences.getString("id", "");

        getUserDetails();

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                updateUserDetails(name, email, password);
            }
        });

        imageViewProfile = view.findViewById(R.id.imageViewProfile);
        // Créez une instance de votre ApiService
        ApiService apiService = ApiClient.getApiService();

        String apiUrl = "getUserImage/" + id;

// Effectuez l'appel à l'API
        Call<ResponseBody> call = apiService.getUserImage(apiUrl);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        // Convertissez les données binaires en bitmap
                        byte[] imageBytes = response.body().bytes();
                        if (imageBytes != null && imageBytes.length > 0) {
                            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

                            // Affichez l'image dans votre ImageView
                            imageViewProfile.setImageBitmap(bitmap);
                        } else {
                            Log.e("API Call", "Empty response body");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e("API Call", "Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("API Call", "Failure: " + t.getMessage());
            }
        });







        imageViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseProfileImage();
            }
        });


        return view;
    }



        private void getUserDetails() {
            // Get the SharedPreferences using the fragment's context
            SharedPreferences preferences = requireContext().getSharedPreferences("AppSettings", Context.MODE_PRIVATE);

            // Retrieve user details from SharedPreferences
            id = preferences.getString("id", "");
            name = preferences.getString("name", "");
            email = preferences.getString("email", "");


            // Set the retrieved values to the respective views
            nameEditText.setText(name);
            emailEditText.setText(email);
        }

        private void updateUserDetails(String name, String email, String password) {
            User user = new User(name, email, password);
            ApiService apiService = ApiClient.getedit();
            SharedPreferences preferences = requireContext().getSharedPreferences("AppSettings", Context.MODE_PRIVATE);
            id = preferences.getString("id", "");
            confirpassword = confirmpassword.getText().toString().trim();
            if (!password.isEmpty()|| !confirpassword.equals(password) ) {
                Toast.makeText(getContext(), " password must match confirm password", Toast.LENGTH_SHORT).show();
                return;
            }
            // Assurez-vous d'ajuster l'URL en fonction de votre route Laravel
            Call<JsonObject> call = apiService.editProfile(Integer.parseInt(id), user);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.isSuccessful()) {
                        // Mise à jour réussie, mettez à jour la base de données locale et affichez un message
                        Log.e("onResponse Code", "success");
                    } else {
                        Log.e("onResponse Code", response.message());
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.e("onFailure Code", "failure");
                }
            });
        }

    private void uploadImageToServer(Bitmap bitmap) {
        SharedPreferences preferences = requireContext().getSharedPreferences("AppSettings", Context.MODE_PRIVATE);
        id = preferences.getString("id", "");

        File imageFile = new File(requireContext().getCacheDir(), "image.jpg");

        try {
            // Copy the selected image to the cache directory
            InputStream inputStream = requireContext().getContentResolver().openInputStream(selectedImageUri);
            FileUtils.copyInputStreamToFile(inputStream, imageFile);

            // Create a request body with the image file

            RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpeg"), imageFile);
            MultipartBody.Part filePart = MultipartBody.Part.createFormData("avatar",  imageFile.getName(), requestBody);

            // Call the API service to update the profile image
            ApiService apiService = ApiClient.getApiService();
            Call<ResponseBody> call = apiService.updateProfileImage(Integer.parseInt(id), filePart);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Log.i("goog id", "ok");
                        Log.d("Upload", "User ID: " + id);
                        Log.d("Upload", "File path: " + imageFile.getPath());
                    } else {
                        Log.i("else id", "else");
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.i("jemla id", "dewiw");
                    Log.e("Upload Failure", "Error: " + t.getMessage());
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void chooseProfileImage() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        resultLauncher.launch(intent);
    }
    private void handleActivityResult(Intent data) {
        selectedImageUri = data.getData();
        if (selectedImageUri == null) {
            Log.e("uploadImageToServer", "Selected image URI is null");
            return;
        }
        try {
            InputStream inputStream = requireContext().getContentResolver().openInputStream(selectedImageUri);
            bitmap = BitmapFactory.decodeStream(inputStream);
            imageViewProfile.setImageBitmap(bitmap);
            uploadImageToServer(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private final ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        handleActivityResult(data);
                    }
                }
            });


}
