package com.example.antik;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_Home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Home<AutoScrollPagerAdapter> extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private TextView viewall;
    private LinearLayout productsContainer;
    private ApiService apiService;
    private LinearLayout productsContainerr;
    private ViewPager viewPager;
    private autoscrollpageradapter pagerAdapter;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Fragment_Home() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_Home.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_Home newInstance(String param1, String param2) {
        Fragment_Home fragment = new Fragment_Home();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment__home, container, false);

        // Initialize your UI components
        productsContainer = view.findViewById(R.id.productsContainer);

        viewPager = view.findViewById(R.id.viewPager);
        // Initialize your ApiServices
        apiService = ApiClient.getApiService();

        // Fetch data from your API
        fetchDataFromApi();

        viewall = view.findViewById(R.id.viewall);
        viewall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                annoncesallFragment annoncesall = new annoncesallFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, annoncesall); // Replace R.id.fragment_container with the ID of your fragment container
                transaction.addToBackStack(null); // Optional: Add the transaction to the back stack
                transaction.commit();
            }
        });
        return view;

    }

    private void fetchDataFromApi() {
        // Make a Retrofit API call
        Call<ApiService.ApiResponse> call = apiService.getAnnonces();
        call.enqueue(new Callback<ApiService.ApiResponse>() {
            @Override
            public void onResponse(Call<ApiService.ApiResponse> call, Response<ApiService.ApiResponse> response) {
                if (response.isSuccessful()) {
                    // Parse the response and populate UI

                        List<Annonce.Annoncee> annonces = response.body().getAnnonces();
                    if (annonces != null) {
                        populateUII(annonces);
                        populateUI(annonces);
                    }
                } else {
                    // Handle unsuccessful response
                }
            }

            @Override
            public void onFailure(Call<ApiService.ApiResponse> call, Throwable t) {
                // Handle failure
            }
        });
    }
    private void populateUII(List<Annonce.Annoncee> annonces) {

        // Create adapter for ViewPager
        pagerAdapter = new autoscrollpageradapter(requireContext(), annonces);

        if (annonces != null) {
            viewPager.setAdapter((PagerAdapter) pagerAdapter);
        }

        // Set a timer for automatic scrolling
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new AutoScrollTask(), 5000, 5000);
    }

    private class AutoScrollTask extends TimerTask {
        @Override
        public void run() {
            if (getActivity() != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int currentItem = viewPager.getCurrentItem();
                        int nextItem = (currentItem + 1) % pagerAdapter.getCount();
                        viewPager.setCurrentItem(nextItem);
                    }
                });
            }
        }
    }
    private void populateUI(List<Annonce.Annoncee> annonces) {
        // Clear existing views
        productsContainer.removeAllViews();

        // Define spacing between items (adjust as needed)
        int itemSpacing = dpToPx(8);

        // Iterate through annonces and create UI elements
        for (Annonce.Annoncee annoncee : annonces) {
            if (annonces != null) {
                // Create a layout for each item
                LinearLayout itemLayout = new LinearLayout(requireContext());
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        dpToPx(160), // Set width as needed
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                layoutParams.setMargins(itemSpacing, 0, itemSpacing, 0); // Add spacing
                itemLayout.setLayoutParams(layoutParams);
                itemLayout.setOrientation(LinearLayout.VERTICAL);
                itemLayout.setPadding(12, 12, 12, 12);
                itemLayout.setBackgroundResource(R.drawable.white_circular_border);

                // Create ImageView for the image
                ImageView imageView = new ImageView(requireContext());
                imageView.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dpToPx(120) // Set height as needed
                ));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

                // Load image from URL using Picasso
                Picasso.get().load(annoncee.getImage().getUrl()).into(imageView);

                itemLayout.addView(imageView);
                // Create TextView for the title
                TextView titleTextView = new TextView(requireContext());
                titleTextView.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                ));
                titleTextView.setText(annoncee.getTitre());
                titleTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.textColor));
                titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                itemLayout.addView(titleTextView);

                // Create TextView for the price
                TextView priceTextView = new TextView(requireContext());
                priceTextView.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                ));
                priceTextView.setText("" + annoncee.getPrix() + " DT");
                priceTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.textColor));
                priceTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                itemLayout.addView(priceTextView);

                // Add the item layout to the container
                productsContainer.addView(itemLayout);
            }
        }
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }
}