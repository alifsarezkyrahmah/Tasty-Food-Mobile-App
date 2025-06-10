//package com.example.project2.Fragment;
//
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Looper;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//
//import androidx.appcompat.widget.SearchView;
//import androidx.constraintlayout.widget.ConstraintLayout;
//import androidx.fragment.app.Fragment;
//import androidx.lifecycle.ViewModelProvider;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.project2.Adapter.RecipeAdapter;
//import com.example.project2.Model.HomeViewModel;
//import com.example.project2.Model.Recipe;
//import com.example.project2.R;
//
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
//public class HomeFragment extends Fragment {
//
//    private HomeViewModel homeViewModel;
//
//    private RecyclerView recyclerViewFeaturedRecipes;
//    private RecyclerView recyclerViewAllRecipes;
//    private RecipeAdapter featuredAdapter;
//    private RecipeAdapter allAdapter;
//    private ProgressBar progressBar;
//    private ConstraintLayout contentLayout;
//    private View viewback;
//    private SearchView searchView;
//
//    private TextView tvErrorMessage, tvFeaturedTitle, tvAllRecipesTitle, tvNoRecipes;
//    private Button btnRefresh;
//    private ExecutorService executorService = Executors.newSingleThreadExecutor();
//    private Handler mainHandler = new Handler(Looper.getMainLooper());
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
//        tvErrorMessage = view.findViewById(R.id.tvErrorMessage);
//        btnRefresh = view.findViewById(R.id.btnRefresh);
//        searchView = view.findViewById(R.id.searchView);
//        tvFeaturedTitle = view.findViewById(R.id.tvFeaturedRecipes);
//        tvAllRecipesTitle = view.findViewById(R.id.tvAllRecipes);
//        tvNoRecipes = view.findViewById(R.id.tvNoRecipes);
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
//        tvErrorMessage.setVisibility(View.GONE);
//        btnRefresh.setVisibility(View.GONE);
//        tvNoRecipes.setVisibility(View.GONE); // Initially hide
//
//        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
//
//        btnRefresh.setOnClickListener(v -> {
//            homeViewModel.clearError();
//            homeViewModel.loadRecipes();
//        });
//
//        homeViewModel.getLoading().observe(getViewLifecycleOwner(), isLoading -> {
//            if (isLoading) {
//                progressBar.setVisibility(View.VISIBLE);
//                contentLayout.setVisibility(View.GONE);
//                viewback.setVisibility(View.GONE);
//                tvErrorMessage.setVisibility(View.GONE);
//                btnRefresh.setVisibility(View.GONE);
//                tvNoRecipes.setVisibility(View.GONE);
//            } else {
//                progressBar.setVisibility(View.GONE);
//            }
//        });
//
//        homeViewModel.getFeaturedRecipes().observe(getViewLifecycleOwner(), featuredRecipes -> {
//            if (featuredRecipes != null) {
//                featuredAdapter = new RecipeAdapter(getContext(), featuredRecipes, true);
//                recyclerViewFeaturedRecipes.setAdapter(featuredAdapter);
//            }
//        });
//
//        homeViewModel.getAllRecipes().observe(getViewLifecycleOwner(), allRecipes -> {
//            if (allRecipes != null) {
//                allAdapter = new RecipeAdapter(getContext(), allRecipes, false);
//
//                // Set filter result listener to handle "no recipes" message
//                allAdapter.setOnFilterResultListener(new RecipeAdapter.OnFilterResultListener() {
//                    @Override
//                    public void onResultCount(int count) {
//                        // Show tvNoRecipes if no results found during search
//                        String currentQuery = searchView.getQuery().toString();
//                        if (currentQuery != null && !currentQuery.trim().isEmpty() && count == 0) {
//                            tvNoRecipes.setVisibility(View.VISIBLE);
//                            recyclerViewAllRecipes.setVisibility(View.GONE);
//                        } else if (currentQuery != null && !currentQuery.trim().isEmpty() && count > 0) {
//                            tvNoRecipes.setVisibility(View.GONE);
//                            recyclerViewAllRecipes.setVisibility(View.VISIBLE);
//                        } else {
//                            // Not searching, show normal view
//                            tvNoRecipes.setVisibility(View.GONE);
//                            recyclerViewAllRecipes.setVisibility(View.VISIBLE);
//                        }
//                    }
//                });
//
//                recyclerViewAllRecipes.setAdapter(allAdapter);
//
//                contentLayout.setVisibility(View.VISIBLE);
//                viewback.setVisibility(View.VISIBLE);
//                tvErrorMessage.setVisibility(View.GONE);
//                btnRefresh.setVisibility(View.GONE);
//            }
//        });
//
//        homeViewModel.getError().observe(getViewLifecycleOwner(), errorMsg -> {
//            if (errorMsg != null) {
//                progressBar.setVisibility(View.GONE);
//                contentLayout.setVisibility(View.GONE);
//                viewback.setVisibility(View.GONE);
//                tvNoRecipes.setVisibility(View.GONE);
//
//                tvErrorMessage.setText(errorMsg);
//                tvErrorMessage.setVisibility(View.VISIBLE);
//                btnRefresh.setVisibility(View.VISIBLE);
//            }
//        });
//
//        if (homeViewModel.getAllRecipes().getValue() == null) {
//            homeViewModel.loadRecipes();
//        }
//
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false; // Tidak butuh submit
//            }
//
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                // Show progress indicator
//                progressBar.setVisibility(View.VISIBLE);
//                recyclerViewAllRecipes.setVisibility(View.GONE);
//                // Execute search on background thread
//                executorService.execute(() -> {
//                    try {
//                        // Update UI on main thread
//                        mainHandler.post(() -> {
//                            if (allAdapter != null) {
//                                // Call the adapter's filter method on the main thread
//                                allAdapter.filterList(newText);
//
//                                // UI visibility logic
//                                if (newText != null && !newText.trim().isEmpty()) {
//                                    recyclerViewFeaturedRecipes.setVisibility(View.GONE);
//                                    tvAllRecipesTitle.setVisibility(View.GONE);
//                                    tvFeaturedTitle.setVisibility(View.GONE);
//                                } else {
//                                    recyclerViewFeaturedRecipes.setVisibility(View.VISIBLE);
//                                    tvAllRecipesTitle.setVisibility(View.VISIBLE);
//                                    tvFeaturedTitle.setVisibility(View.VISIBLE);
//                                    tvNoRecipes.setVisibility(View.GONE);
//                                }
//                            }
//                            // Hide progress indicator
//                            progressBar.setVisibility(View.GONE);
//
//                        });
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        mainHandler.post(() -> {
//                            // Handle errors
//                            progressBar.setVisibility(View.GONE);
//                            // Show error message if needed
//                        });
//                    }
//                });
//
//                return true;
//            }
//        });
//
//        return view;
//    }
//}


package com.example.project2.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project2.Adapter.RecipeAdapter;
import com.example.project2.Model.HomeViewModel;
import com.example.project2.Model.Recipe;
import com.example.project2.R;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    private RecyclerView recyclerViewFeaturedRecipes;
    private RecyclerView recyclerViewAllRecipes;
    private RecipeAdapter featuredAdapter;
    private RecipeAdapter allAdapter;
    private ProgressBar progressBar;
    private ConstraintLayout contentLayout;
    private View viewback;
    private SearchView searchView;

    private TextView tvErrorMessage, tvFeaturedTitle, tvAllRecipesTitle, tvNoRecipes;
    private Button btnRefresh;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private Handler mainHandler = new Handler(Looper.getMainLooper());

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerViewFeaturedRecipes = view.findViewById(R.id.recyclerViewFeaturedRecipes);
        recyclerViewAllRecipes = view.findViewById(R.id.recyclerViewAllRecipes);
        progressBar = view.findViewById(R.id.progressBar);
        contentLayout = view.findViewById(R.id.constraintLayoutContent);
        viewback = view.findViewById(R.id.viewBack);
        tvErrorMessage = view.findViewById(R.id.tvErrorMessage);
        btnRefresh = view.findViewById(R.id.btnRefresh);
        searchView = view.findViewById(R.id.searchView);
        tvFeaturedTitle = view.findViewById(R.id.tvFeaturedRecipes);
        tvAllRecipesTitle = view.findViewById(R.id.tvAllRecipes);
        tvNoRecipes = view.findViewById(R.id.tvNoRecipes);

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
        tvNoRecipes.setVisibility(View.GONE); // Initially hide

        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);

        btnRefresh.setOnClickListener(v -> {
            homeViewModel.clearError();
            homeViewModel.loadRecipes();
        });

        homeViewModel.getLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading) {
                progressBar.setVisibility(View.VISIBLE);
                contentLayout.setVisibility(View.GONE);
                viewback.setVisibility(View.GONE);
                tvErrorMessage.setVisibility(View.GONE);
                btnRefresh.setVisibility(View.GONE);
                tvNoRecipes.setVisibility(View.GONE);
            } else {
                progressBar.setVisibility(View.GONE);
            }
        });

        homeViewModel.getFeaturedRecipes().observe(getViewLifecycleOwner(), featuredRecipes -> {
            if (featuredRecipes != null) {
                featuredAdapter = new RecipeAdapter(getContext(), featuredRecipes, true);
                recyclerViewFeaturedRecipes.setAdapter(featuredAdapter);
            }
        });

        homeViewModel.getAllRecipes().observe(getViewLifecycleOwner(), allRecipes -> {
            if (allRecipes != null) {
                allAdapter = new RecipeAdapter(getContext(), allRecipes, false);

                // Set filter result listener to handle "no recipes" message
                allAdapter.setOnFilterResultListener(new RecipeAdapter.OnFilterResultListener() {
                    @Override
                    public void onResultCount(int count) {
                        // Show tvNoRecipes if no results found during search
                        String currentQuery = searchView.getQuery().toString();
                        if (currentQuery != null && !currentQuery.trim().isEmpty() && count == 0) {
                            tvNoRecipes.setVisibility(View.VISIBLE);
                            recyclerViewAllRecipes.setVisibility(View.GONE);
                        } else if (currentQuery != null && !currentQuery.trim().isEmpty() && count > 0) {
                            tvNoRecipes.setVisibility(View.GONE);
                            recyclerViewAllRecipes.setVisibility(View.VISIBLE);
                        } else {
                            // Not searching, show normal view
                            tvNoRecipes.setVisibility(View.GONE);
                            recyclerViewAllRecipes.setVisibility(View.VISIBLE);
                        }
                    }
                });

                recyclerViewAllRecipes.setAdapter(allAdapter);

                contentLayout.setVisibility(View.VISIBLE);
                viewback.setVisibility(View.VISIBLE);
                tvErrorMessage.setVisibility(View.GONE);
                btnRefresh.setVisibility(View.GONE);
            }
        });

        homeViewModel.getError().observe(getViewLifecycleOwner(), errorMsg -> {
            if (errorMsg != null) {
                progressBar.setVisibility(View.GONE);
                contentLayout.setVisibility(View.GONE);
                viewback.setVisibility(View.GONE);
                tvNoRecipes.setVisibility(View.GONE);

                tvErrorMessage.setText(errorMsg);
                tvErrorMessage.setVisibility(View.VISIBLE);
                btnRefresh.setVisibility(View.VISIBLE);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false; // Tidak butuh submit
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Show progress indicator
                progressBar.setVisibility(View.VISIBLE);
                recyclerViewAllRecipes.setVisibility(View.GONE);
                // Execute search on background thread
                executorService.execute(() -> {
                    try {
                        // Update UI on main thread
                        mainHandler.post(() -> {
                            if (allAdapter != null) {
                                // Call the adapter's filter method on the main thread
                                allAdapter.filterList(newText);

                                // UI visibility logic
                                if (newText != null && !newText.trim().isEmpty()) {
                                    recyclerViewFeaturedRecipes.setVisibility(View.GONE);
                                    tvAllRecipesTitle.setVisibility(View.GONE);
                                    tvFeaturedTitle.setVisibility(View.GONE);
                                } else {
                                    recyclerViewFeaturedRecipes.setVisibility(View.VISIBLE);
                                    tvAllRecipesTitle.setVisibility(View.VISIBLE);
                                    tvFeaturedTitle.setVisibility(View.VISIBLE);
                                    tvNoRecipes.setVisibility(View.GONE);
                                }
                            }
                            // Hide progress indicator
                            progressBar.setVisibility(View.GONE);

                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        mainHandler.post(() -> {
                            // Handle errors
                            progressBar.setVisibility(View.GONE);
                            // Show error message if needed
                        });
                    }
                });

                return true;
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Load data setiap kali fragment ditampilkan
        if (homeViewModel != null) {
            homeViewModel.clearError();
            homeViewModel.loadRecipes();
        }
    }
}