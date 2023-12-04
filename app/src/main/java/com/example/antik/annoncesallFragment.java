package com.example.antik;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.antik.ApiClient;
import com.example.antik.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class annoncesallFragment extends Fragment {

    private RecyclerView recyclerView;
    private AnnonceAdapter annonceAdapter;

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


}
