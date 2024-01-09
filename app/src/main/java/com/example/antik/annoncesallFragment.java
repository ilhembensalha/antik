package com.example.antik;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import androidx.appcompat.widget.SearchView;
public class annoncesallFragment extends Fragment  {

    private RecyclerView recyclerView;
    private AnnonceAdapter annonceAdapter;
    private PopupWindow currentPopup;
    private Bitmap bitmap;

    private ApiService apiService;
    public String username;
    private List<Annonce.Annoncee> annoncesList;
    private List<Annonce.Annoncee> filteredList;
    private SearchView searchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_annoncesall, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_annonces);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        // Add this code for search functionality
        searchView = view.findViewById(R.id.searchView);
        // Add this code for search functionality
        setupSearch();
        // Fetch data from API
        fetchAnnonces();

        return view;
    }
    private void setupSearch() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });
    }

    private void filter(String query) {
        if (annoncesList == null) {
            return;
        }

        filteredList.clear();

        if (TextUtils.isEmpty(query)) {
            filteredList.addAll(annoncesList);
        } else {
            query = query.toLowerCase().trim();

            for (Annonce.Annoncee annonce : annoncesList) {
                if (annonce.getTitre().toLowerCase().contains(query) ||
                        annonce.getDescription().toLowerCase().contains(query)) {
                    filteredList.add(annonce);
                }
            }
        }

        annonceAdapter.notifyDataSetChanged();
    }

    private void fetchAnnonces() {
        ApiService apiService = ApiClient.getApiService();
        Call<ApiService.ApiResponse> call = apiService.getAnnonces();

        call.enqueue(new Callback<ApiService.ApiResponse>() {
            @Override
            public void onResponse(Call<ApiService.ApiResponse> call, Response<ApiService.ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiService.ApiResponse apiResponse = response.body();
                    List<Annonce.Annoncee> annoncesList = apiResponse.getAnnonces();
                    if (annoncesList != null) {
                        filteredList = new ArrayList<>();
                        filteredList.addAll(annoncesList);

                        annonceAdapter = new AnnonceAdapter(annoncesList);

                    // Add these lines to set the OnItemClickListener
                    annonceAdapter.setOnItemClickListener(new AnnonceAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(Annonce.Annoncee annonce) {
                            // Show details in a popup window
                            dismissCurrentPopup();
                            showDetailsPopup(annonce.getId());
                        }
                    });
                    recyclerView.setAdapter(annonceAdapter);
                    }
                } else {
                    Log.e("API Error", "Unsuccessful response");
                }
            }

            @Override
            public void onFailure(Call<ApiService.ApiResponse> call, Throwable t) {
                Log.e("API Failure", t.getMessage(), t);
            }
        });
        if (annonceAdapter != null) {
            annonceAdapter.notifyDataSetChanged();
        }
    }

    private void showDetailsPopup(int annonceId) {
        ApiService apiService = ApiClient.getApiService();
        Call<AnnonceDetails> call = apiService.getAnnonceDetails(annonceId);

        call.enqueue(new Callback<AnnonceDetails>() {
            @Override
            public void onResponse(Call<AnnonceDetails> call, Response<AnnonceDetails> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AnnonceDetails details = response.body();
                    showPopupWindow(details);
                } else {
                    Log.e("API Error", "Unsuccessful response");
                }
            }

            @Override
            public void onFailure(Call<AnnonceDetails> call, Throwable t) {
                Log.e("API Failure", t.getMessage(), t);
            }
        });
    }

    private void showPopupWindow(AnnonceDetails details) {
        apiService = ApiClient.getApiService();
        View popupView = LayoutInflater.from(getContext()).inflate(R.layout.popup_layout, null);
        PopupWindow popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true

        );

        // Set popup content
        TextView titleTextView = popupView.findViewById(R.id.popupTitleTextView);
        TextView prix = popupView.findViewById(R.id.prixx);
        TextView descriptionTextView = popupView.findViewById(R.id.popupDescriptionTextView);
        TextView statutTextView = popupView.findViewById(R.id.statutTextView);
        TextView userTextView = popupView.findViewById(R.id.userTextView);
        TextView categoryTextView = popupView.findViewById(R.id.categoryTextView);
        TextView DATE = popupView.findViewById(R.id.date);
        TextView livraisonTextView = popupView.findViewById(R.id.livraisonTextView);
        TextView locationTextView = popupView.findViewById(R.id.locationTextView);
        ImageView imageView = popupView.findViewById(R.id.popupImageView);
        titleTextView.setText(details.getTitre());
        int idcat = details.getCategoryId();
        Log.e("API Response", "cat Name: " +idcat);
        Call<Categorie> call3 = apiService.getCat(idcat);

        call3.enqueue(new Callback<Categorie>() {
            @Override
            public void onResponse(Call<Categorie> call, Response<Categorie> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Categorie cat = response.body();
                    Log.e("API Response", "cat Name: " +cat);
                    Log.e("API Response", "cat Name: " + cat.getName());
                    categoryTextView.setText(cat.getName());
                    //  Log.e("API Error", userProfile.getName());
                    Log.e("API Error", "successful response");
                } else {
                    Log.e("API Error", "Unsuccessful response");
                }
            }

            @Override
            public void onFailure(Call<Categorie> call, Throwable t) {
                Log.e("API Failure", t.getMessage(), t);
            }
        });


        int userId = details.getUserId();


        Call<User> call = apiService.getUserProfile(userId);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User userProfile = response.body();
                    Log.e("API Response", "User Name: " +userProfile);
                    Log.e("API Response", "User Name: " + userProfile.getName());
                    userTextView.setText(userProfile.getName());
                  //  Log.e("API Error", userProfile.getName());
                    Log.e("API Error", "successful response");
                } else {
                    Log.e("API Error", "Unsuccessful response");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("API Failure", t.getMessage(), t);
            }
        });
       ImageView imageViewProfile = popupView.findViewById(R.id.imageViewProfile);
        // Créez une instance de votre ApiService
        ApiService apiService = ApiClient.getApiService();

// Remplacez 'YOUR_API_ENDPOINT' par l'URL réelle de votre API Laravel
// et 'userId' par l'ID réel de l'utilisateur dont vous souhaitez récupérer l'image
        String apiUrl = "getUserImage/" + userId;

// Effectuez l'appel à l'API
        Call<ResponseBody> call2 = apiService.getUserImage(apiUrl);
        call2.enqueue(new Callback<ResponseBody>() {
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

        statutTextView.setText(details.getStatut());
        livraisonTextView.setText(details.getLivraison());
        locationTextView.setText(details.getLocation());
        prix.setText(String.valueOf(details.getPrix())+" DT");
        descriptionTextView.setText(details.getDescription());
        DATE.setText(String.valueOf(details.getCreated_at()));
        Picasso.get().load(details.getImage().getUrl()).into(imageView);
        // Button to close the popup
        Button closeButton = popupView.findViewById(R.id.closeButton);
        Button send = popupView.findViewById(R.id.send);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Dismiss the popup when the button is clicked
                popupWindow.dismiss();
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = requireContext().getSharedPreferences("AppSettings", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("iduser", String.valueOf(userId));
                editor.apply();

                messagerie messagerie = new messagerie();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, messagerie); // Replace R.id.fragment_container with the ID of your fragment container
                transaction.addToBackStack(null); // Optional: Add the transaction to the back stack
                transaction.commit();
                popupWindow.dismiss();
            }

        });


        // Set dismiss listener
        popupWindow.setOnDismissListener(() -> {
            currentPopup = null;
        });

        // Show the popup
        popupWindow.showAtLocation(getView(), Gravity.CENTER, 0, 0);
        currentPopup = popupWindow;
    }
    private void dismissCurrentPopup() {
        if (currentPopup != null && currentPopup.isShowing()) {
            currentPopup.dismiss();
        }
    }

}