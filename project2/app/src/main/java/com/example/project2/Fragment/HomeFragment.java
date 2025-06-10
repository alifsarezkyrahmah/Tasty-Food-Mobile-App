//package com.example.project2.Fragment;
//
//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ProgressBar;
//import android.widget.Toast;
//
//import androidx.constraintlayout.widget.ConstraintLayout;
//import androidx.fragment.app.Fragment;
//import androidx.lifecycle.ViewModelProvider;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.project2.Adapter.RecipeAdapter;
//import com.example.project2.Api.ApiService;
//import com.example.project2.Api.RetrofitClient;
//import com.example.project2.Api.TastyResponse;
//import com.example.project2.Model.HomeViewModel;
//import com.example.project2.Model.Recipe;
//import com.example.project2.R;
//
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.List;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//import androidx.lifecycle.Observer;
//import androidx.lifecycle.ViewModelProvider;
//import com.example.project2.Model.HomeViewModel;
//
//// ...
//
//public class HomeFragment extends Fragment {
//
//    private HomeViewModel homeViewModel;
//
//    // View references
//    private RecyclerView recyclerViewFeaturedRecipes;
//    private RecyclerView recyclerViewAllRecipes;
//    private RecipeAdapter featuredAdapter;
//    private RecipeAdapter allAdapter;
//    private ProgressBar progressBar;
//    private ConstraintLayout contentLayout;
//    private View viewback;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_home, container, false);
//
//        recyclerViewFeaturedRecipes = view.findViewById(R.id.recyclerViewFeaturedRecipes);
//        recyclerViewAllRecipes = view.findViewById(R.id.recyclerViewAllRecipes);
//        progressBar = view.findViewById(R.id.progressBar);
//        contentLayout = view.findViewById(R.id.constraintLayoutContent);
//        viewback = view.findViewById(R.id.viewBack);
//
//        recyclerViewFeaturedRecipes.setLayoutManager(
//                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false)
//        );
//        recyclerViewAllRecipes.setLayoutManager(
//                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false)
//        );
//
//        contentLayout.setVisibility(View.GONE);
//        viewback.setVisibility(View.GONE);
//        progressBar.setVisibility(View.VISIBLE);
//
//
//        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
//
//        // Observe loading
//        homeViewModel.getLoading().observe(getViewLifecycleOwner(), isLoading -> {
//            if (isLoading) {
//                progressBar.setVisibility(View.VISIBLE);
//                contentLayout.setVisibility(View.GONE);
//                viewback.setVisibility(View.GONE);
//            } else {
//                progressBar.setVisibility(View.GONE);
//                contentLayout.setVisibility(View.VISIBLE);
//                viewback.setVisibility(View.VISIBLE);
//            }
//        });
//
//        // Observe featured recipes
//        homeViewModel.getFeaturedRecipes().observe(getViewLifecycleOwner(), featuredRecipes -> {
//            if (featuredRecipes != null) {
//                featuredAdapter = new RecipeAdapter(getContext(), featuredRecipes, true);
//                recyclerViewFeaturedRecipes.setAdapter(featuredAdapter);
//            }
//        });
//
//        // Observe all recipes
//        homeViewModel.getAllRecipes().observe(getViewLifecycleOwner(), allRecipes -> {
//            if (allRecipes != null) {
//                allAdapter = new RecipeAdapter(getContext(), allRecipes, false);
//                recyclerViewAllRecipes.setAdapter(allAdapter);
//            }
//        });
//
//        // Observe errors
//        homeViewModel.getError().observe(getViewLifecycleOwner(), errorMsg -> {
//            if (errorMsg != null) {
//                Toast.makeText(getContext(), errorMsg, Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        if (homeViewModel.getAllRecipes().getValue() == null) {
//            homeViewModel.loadRecipes();
//        }
//
//        return view;
//    }
//}



package com.example.project2.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project2.Adapter.RecipeAdapter;
import com.example.project2.Model.HomeViewModel;
import com.example.project2.Model.Recipe;
import com.example.project2.R;

import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    private RecyclerView recyclerViewFeaturedRecipes;
    private RecyclerView recyclerViewAllRecipes;
    private RecipeAdapter featuredAdapter;
    private RecipeAdapter allAdapter;
    private ProgressBar progressBar;
    private ConstraintLayout contentLayout;
    private View viewback;

    private TextView tvErrorMessage;
    private Button btnRefresh;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Inisialisasi view
        recyclerViewFeaturedRecipes = view.findViewById(R.id.recyclerViewFeaturedRecipes);
        recyclerViewAllRecipes = view.findViewById(R.id.recyclerViewAllRecipes);
        progressBar = view.findViewById(R.id.progressBar);
        contentLayout = view.findViewById(R.id.constraintLayoutContent);
        viewback = view.findViewById(R.id.viewBack);
        tvErrorMessage = view.findViewById(R.id.tvErrorMessage);
        btnRefresh = view.findViewById(R.id.btnRefresh);

        recyclerViewFeaturedRecipes.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false)
        );
        recyclerViewAllRecipes.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false)
        );

        contentLayout.setVisibility(View.GONE);
        viewback.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        tvErrorMessage.setVisibility(View.GONE);
        btnRefresh.setVisibility(View.GONE);

        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);

        // Refresh button listener
        btnRefresh.setOnClickListener(v -> {
            homeViewModel.clearError();
            homeViewModel.loadRecipes();
        });

        // Observe loading state
        homeViewModel.getLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading) {
                progressBar.setVisibility(View.VISIBLE);
                contentLayout.setVisibility(View.GONE);
                viewback.setVisibility(View.GONE);
                tvErrorMessage.setVisibility(View.GONE);
                btnRefresh.setVisibility(View.GONE);
            } else {
                progressBar.setVisibility(View.GONE);
            }
        });

        // Featured recipes
        homeViewModel.getFeaturedRecipes().observe(getViewLifecycleOwner(), featuredRecipes -> {
            if (featuredRecipes != null) {
                featuredAdapter = new RecipeAdapter(getContext(), featuredRecipes, true);
                recyclerViewFeaturedRecipes.setAdapter(featuredAdapter);
            }
        });

        // All recipes
        homeViewModel.getAllRecipes().observe(getViewLifecycleOwner(), allRecipes -> {
            if (allRecipes != null) {
                allAdapter = new RecipeAdapter(getContext(), allRecipes, false);
                recyclerViewAllRecipes.setAdapter(allAdapter);

                contentLayout.setVisibility(View.VISIBLE);
                viewback.setVisibility(View.VISIBLE);
                tvErrorMessage.setVisibility(View.GONE);
                btnRefresh.setVisibility(View.GONE);
            }
        });

        // Error handling
        homeViewModel.getError().observe(getViewLifecycleOwner(), errorMsg -> {
            if (errorMsg != null) {
                progressBar.setVisibility(View.GONE);
                contentLayout.setVisibility(View.GONE);
                viewback.setVisibility(View.GONE);

                tvErrorMessage.setText(errorMsg);
                tvErrorMessage.setVisibility(View.VISIBLE);
                btnRefresh.setVisibility(View.VISIBLE);
            }
        });

        // Load data if not yet loaded
        if (homeViewModel.getAllRecipes().getValue() == null) {
            homeViewModel.loadRecipes();
        }

        return view;
    }
}
