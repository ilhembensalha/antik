package com.example.antik;



import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import org.apache.commons.io.FileUtils;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.Intent;

public class AddFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    //private static final int STORAGE_PERMISSION_CODE = 123;
    private static final int PERMISSION_REQUEST_CODE = 1001;

    private EditText titreEditText, descriptionEditText, prixEditText, locationEditText;
    private Spinner categorieSpinner;
    private Spinner livraisonSpinner;
    private static final String[] LIVRAISON_OPTIONS = {"choisir ","Oui", "Non"};

    private Button chooseImageButton, confirmButton;
    private List<Categorie> categories;

    private Uri selectedImageUri; // To store the selected image URI

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        titreEditText = view.findViewById(R.id.titre);
        descriptionEditText = view.findViewById(R.id.description);
        prixEditText = view.findViewById(R.id.prix);
        locationEditText = view.findViewById(R.id.localisation);
        categorieSpinner = view.findViewById(R.id.categorie);
        livraisonSpinner = view.findViewById(R.id.livraison);

        chooseImageButton = view.findViewById(R.id.chooseImageButton);
        chooseImageButton.setOnClickListener(v ->
                    chooseProfileImage());

        confirmButton = view.findViewById(R.id.Confirmer);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    sendAnnouncementToServer();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        // Nouvelle logique pour récupérer la liste des catégories
        ApiService apiService = ApiClient.getApiService();
        Call<List<Categorie>> callCategories = apiService.getCategories();
        callCategories.enqueue(new Callback<List<Categorie>>() {
            @Override
            public void onResponse(Call<List<Categorie>> call, Response<List<Categorie>> response) {
                if (response.isSuccessful()) {
                    // Stocker les catégories dans la liste
                    categories = response.body();

                    // Initialiser le Spinner avec les catégories
                    initCategorySpinner(categories, view);
                } else {
                    Log.e("API Call", "Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Categorie>> call, Throwable t) {
                Log.e("API Call", "Failure: " + t.getMessage());
            }
        });

        ArrayAdapter<String> livraisonAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, LIVRAISON_OPTIONS);
        livraisonAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        livraisonSpinner.setAdapter(livraisonAdapter);

        return view;
    }



    private void chooseProfileImage() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        resultLauncher.launch(intent);
    }
    private void handleActivityResult(Intent data) {
        Uri imageUri = data.getData();
        if (imageUri == null) {
            Log.e("uploadImageToServer", "Selected image URI is null");
            return;
        }
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), imageUri);
            chooseImageButton.setBackground(new BitmapDrawable(requireContext().getResources(), bitmap));
            selectedImageUri = imageUri;
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

            private void sendAnnouncementToServer() throws IOException {
                // Retrieve user-selected category ID
                String selectedCategoryId = getCategoryIdFromSpinner(categorieSpinner);

                // Retrieve other input values
                String titre = titreEditText.getText().toString().trim();
                String description = descriptionEditText.getText().toString().trim();
                String prix = prixEditText.getText().toString().trim();
                String location = locationEditText.getText().toString().trim();
                String livraison = livraisonSpinner.getSelectedItem().toString().trim();

                // Get the SharedPreferences using the fragment's context
                SharedPreferences preferences = requireContext().getSharedPreferences("AppSettings", Context.MODE_PRIVATE);

                // Retrieve user details from SharedPreferences
                String id = preferences.getString("id", "");

                // Perform input validation
                if (titre.isEmpty() || description.isEmpty() || prix.isEmpty() || location.isEmpty() || selectedCategoryId.isEmpty() || id.isEmpty()) {
                    // Display an error message to the user or handle it accordingly
                    Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (livraison.equals("choisir")) {
                    Toast.makeText(requireContext(), "Please fill in  livraison", Toast.LENGTH_SHORT).show();
                    return;
                }


        // Check if an image is selected
        if (selectedImageUri == null) {
            // Display an error message to the user or handle it accordingly
            Toast.makeText(requireContext(), "Please select an image", Toast.LENGTH_SHORT).show();
            return;
        }

        // Prepare the image file for uploading
        File imageFile = new File(requireContext().getCacheDir(), "image.jpg");
        InputStream inputStream = requireContext().getContentResolver().openInputStream(selectedImageUri);
        FileUtils.copyInputStreamToFile(inputStream, imageFile);

        // Get the SharedPreferences using the fragment's context
        SharedPreferences pref = requireContext().getSharedPreferences("AppSettings", Context.MODE_PRIVATE);

        // Retrieve user details from SharedPreferences
        String idd = pref.getString("id", "");

        // Create a request body with the image file
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpeg"), imageFile);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("image", imageFile.getName(), requestBody);

        // Create other RequestBody instances for text fields
        RequestBody titreRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), titre);
        RequestBody descriptionRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), description);
        RequestBody prixRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), prix);
        RequestBody livraisonRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), livraison);
        RequestBody locationRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), location);
        RequestBody catIdRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), selectedCategoryId);
        RequestBody userIdRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), idd);

        // TODO: Implement the Retrofit code to send data to the server

        // For example, assuming you have a Retrofit service instance named "apiService":
        // Call<Void> call = apiService.uploadAnnouncement(imagePart, titreRequestBody, descriptionRequestBody, prixRequestBody, locationRequestBody, catIdRequestBody, userIdRequestBody);
        // call.enqueue(new Callback<Void>() { ... });
        // Assuming you have a Retrofit service instance named "apiService":
        ApiService apiService = ApiClient.getApiService();
        Call<ResponseBody> call = apiService.addAnnouncement(
                titreRequestBody,
                descriptionRequestBody,
                filePart,
                prixRequestBody,
                livraisonRequestBody,
                locationRequestBody,
                catIdRequestBody,
                userIdRequestBody
        );

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    annoncesallFragment annoncesall = new annoncesallFragment();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, annoncesall); // Replace R.id.fragment_container with the ID of your fragment container
                    transaction.addToBackStack(null); // Optional: Add the transaction to the back stack
                    transaction.commit();
                } else {
                    // Handle the case where the response is not successful
                }            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Handle failure
            }
        });
    }

    private String getCategoryIdFromSpinner(Spinner categorySpinner) {
        String selectedCategoryName = categorySpinner.getSelectedItem().toString().trim();

        // Find the corresponding category ID from the list
        if (categories != null && !categories.isEmpty()) {
            for (Categorie category : categories) {
                if (category.getName().equalsIgnoreCase(selectedCategoryName)) {
                    // Convert the category ID to String
                    return String.valueOf(category.getId());
                }
            }
        }

        // Return an empty string if the category is not found (you may handle this case differently)
        return "";
    }

    private void initCategorySpinner(List<Categorie> categories, View view) {
        if (categories != null && categories.size() > 0) {
            // Extract category names from the list
            Spinner categorySpinner = view.findViewById(R.id.categorie);

            // Create a list of category names
            List<String> categoryNames = new ArrayList<>();
            categoryNames.add(0, "Select a category");
            for (Categorie category : categories) {
                categoryNames.add(category.getName());
            }

            // Set up Spinner for categories
            ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, categoryNames);
            categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            categorySpinner.setAdapter(categoryAdapter);
        }
    }
}
