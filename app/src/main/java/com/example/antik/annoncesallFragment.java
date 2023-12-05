package com.example.antik;

import android.os.Bundle;
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

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class annoncesallFragment extends Fragment  {

    private RecyclerView recyclerView;
    private AnnonceAdapter annonceAdapter;
    private PopupWindow currentPopup;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_annoncesall, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_annonces);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Fetch data from API
        fetchAnnonces();

        return view;
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
                } else {
                    Log.e("API Error", "Unsuccessful response");
                }
            }

            @Override
            public void onFailure(Call<ApiService.ApiResponse> call, Throwable t) {
                Log.e("API Failure", t.getMessage(), t);
            }
        });
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
        View popupView = LayoutInflater.from(getContext()).inflate(R.layout.popup_layout, null);
        PopupWindow popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true

        );

        // Set popup content
        TextView titleTextView = popupView.findViewById(R.id.popupTitleTextView);
        TextView descriptionTextView = popupView.findViewById(R.id.popupDescriptionTextView);
        ImageView imageView = popupView.findViewById(R.id.popupImageView);
        titleTextView.setText(details.getTitre());
        descriptionTextView.setText(details.getDescription());
        Picasso.get().load(details.getImage().getUrl()).into(imageView);
        // Button to close the popup
        Button closeButton = popupView.findViewById(R.id.closeButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Dismiss the popup when the button is clicked
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