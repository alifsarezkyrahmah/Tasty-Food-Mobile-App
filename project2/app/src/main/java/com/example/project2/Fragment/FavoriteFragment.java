package com.example.project2.Fragment;

import android.database.Cursor;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.project2.Adapter.FavoriteAdapter;
import com.example.project2.Database.DatabaseHelper;
import com.example.project2.Database.MappingHelper;
import com.example.project2.Database.RecipeHelper;
import com.example.project2.Model.Recipe;
import com.example.project2.R;

import java.util.List;

public class FavoriteFragment extends Fragment {

    private RecyclerView rvFavorite;
    private FavoriteAdapter adapter;
    private RecipeHelper recipeHelper;
    private TextView tvFavoriteCount;
    private View layoutEmptyState;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);

        rvFavorite = view.findViewById(R.id.rvFav);
        rvFavorite.setLayoutManager(new LinearLayoutManager(getContext()));

        tvFavoriteCount = view.findViewById(R.id.tv_favorite_count);
        layoutEmptyState = view.findViewById(R.id.layout_empty_state);

        recipeHelper = new RecipeHelper(getContext());

        adapter = new FavoriteAdapter(getContext(), null, recipe -> {
            recipeHelper.removeFavorite(recipe.getName());
            loadFavorites();
        });

        rvFavorite.setAdapter(adapter);

        loadFavorites();

        return view;
    }


    private void loadFavorites() {
        List<Recipe> favoriteList = recipeHelper.getAllFavorites();
        adapter.updateData(favoriteList);

        int count = favoriteList.size();
        tvFavoriteCount.setText(count + " resep tersimpan");

        if (count == 0) {
            layoutEmptyState.setVisibility(View.VISIBLE);
            rvFavorite.setVisibility(View.GONE);
        } else {
            layoutEmptyState.setVisibility(View.GONE);
            rvFavorite.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadFavorites(); // Refresh data saat fragment kembali aktif
    }

}
