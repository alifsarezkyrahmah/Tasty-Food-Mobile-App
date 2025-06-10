package com.example.project2.Fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.project2.Adapter.FavoriteAdapter;
import com.example.project2.Database.RecipeHelper;
import com.example.project2.Model.Recipe;
import com.example.project2.R;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FavoriteFragment extends Fragment {

    private RecyclerView rvFavorite;
    private FavoriteAdapter adapter;
    private RecipeHelper recipeHelper;
    private TextView tvFavoriteCount, tvNofound;
    private View layoutEmptyState;
    private ProgressBar progressBar;

    private ExecutorService executorService;
    private Handler mainHandler;
    private String currentSearchQuery = "";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);

        rvFavorite = view.findViewById(R.id.rvFav);
        rvFavorite.setLayoutManager(new LinearLayoutManager(getContext()));

        tvFavoriteCount = view.findViewById(R.id.tv_favorite_count);
        layoutEmptyState = view.findViewById(R.id.layout_empty_state);
        tvNofound = view.findViewById(R.id.tvNoFound);
        progressBar = view.findViewById(R.id.progressBarSearch);

        recipeHelper = new RecipeHelper(getContext());
        executorService = Executors.newSingleThreadExecutor();
        mainHandler = new Handler(Looper.getMainLooper());

        adapter = new FavoriteAdapter(getContext(), null, recipe -> {
            recipeHelper.removeFavorite(recipe.getName());
            loadFavorites();
        });

        rvFavorite.setAdapter(adapter);
        loadFavorites();

        SearchView searchView = view.findViewById(R.id.searchViewFavorite);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

    @Override
    public boolean onQueryTextChange(final String newText) {
        // Save the current query
        currentSearchQuery = newText;

        // Apply the search
        applySearch(currentSearchQuery);
        return true;
    }
        });

        return view;
    }

    private void applySearch(String searchText) {
        progressBar.setVisibility(View.VISIBLE);
        rvFavorite.setVisibility(View.GONE);
        layoutEmptyState.setVisibility(View.GONE);

        executorService.execute(() -> {
            try {
                List<Recipe> filteredList;

                if (searchText == null || searchText.trim().isEmpty()) {
                    filteredList = recipeHelper.getAllFavorites();
                } else {
                    filteredList = recipeHelper.searchFavoriteRecipes(searchText);
                }

                mainHandler.post(() -> {
                    adapter.updateData(filteredList);
                    progressBar.setVisibility(View.GONE);

                    if (filteredList.isEmpty()) {
                        layoutEmptyState.setVisibility(View.VISIBLE);
                        tvNofound.setText("Resep tidak ditemukan");
                        rvFavorite.setVisibility(View.GONE);
                    } else {
                        layoutEmptyState.setVisibility(View.GONE);
                        rvFavorite.setVisibility(View.VISIBLE);
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
                mainHandler.post(() -> {
                    progressBar.setVisibility(View.GONE);
                    layoutEmptyState.setVisibility(View.VISIBLE);
                    tvNofound.setText("Terjadi kesalahan saat pencarian");
                    rvFavorite.setVisibility(View.GONE);
                });
            }
        });
    }
    private void loadFavorites() {
        List<Recipe> favoriteList = recipeHelper.getAllFavorites();
        adapter.updateData(favoriteList);

        int count = favoriteList.size();
        tvFavoriteCount.setText(count + " resep tersimpan");

        if (count == 0) {
            layoutEmptyState.setVisibility(View.VISIBLE);
            tvNofound.setText("Tidak ada resep favorit yang ditemukan");
            rvFavorite.setVisibility(View.GONE);
        } else {
            layoutEmptyState.setVisibility(View.GONE);
            rvFavorite.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Reapply the current search query when returning to this screen
        if (!currentSearchQuery.isEmpty()) {
            applySearch(currentSearchQuery);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (executorService != null) executorService.shutdown();
    }
}
