package com.example.project2.Model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.project2.Api.ApiService;
import com.example.project2.Api.RetrofitClient;
import com.example.project2.Api.TastyResponse;
import com.example.project2.Model.Recipe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<List<Recipe>> featuredRecipesLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Recipe>> allRecipesLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> loadingLiveData = new MutableLiveData<>();
    private MutableLiveData<String> errorLiveData = new MutableLiveData<>();

    public LiveData<List<Recipe>> getFeaturedRecipes() {
        return featuredRecipesLiveData;
    }

    public LiveData<List<Recipe>> getAllRecipes() {
        return allRecipesLiveData;
    }

    public LiveData<Boolean> getLoading() {
        return loadingLiveData;
    }

    public LiveData<String> getError() {
        return errorLiveData;
    }



//    public void loadRecipes() {
//        if (featuredRecipesLiveData.getValue() != null && allRecipesLiveData.getValue() != null) {
//            // Data sudah ada, tidak perlu reload
//            return;
//        }
//
//        loadingLiveData.setValue(true);
//
//        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
//        Call<TastyResponse> call = apiService.getRecipes(0, 20);
//
//        call.enqueue(new Callback<TastyResponse>() {
//            @Override
//            public void onResponse(Call<TastyResponse> call, Response<TastyResponse> response) {
//                loadingLiveData.setValue(false);
//                if (response.isSuccessful() && response.body() != null) {
//                    List<Recipe> recipes = response.body().getResults();
//
//                    List<Recipe> featuredRecipes = new ArrayList<>();
//                    for (Recipe recipe : recipes) {
//                        if (recipe.getUser_ratings() != null && recipe.getUser_ratings().getScore() > 0.92) {
//                            featuredRecipes.add(recipe);
//                        }
//                    }
//
//                    Collections.sort(featuredRecipes, new Comparator<Recipe>() {
//                        @Override
//                        public int compare(Recipe r1, Recipe r2) {
//                            double score1 = r1.getUser_ratings() != null ? r1.getUser_ratings().getScore() : 0;
//                            double score2 = r2.getUser_ratings() != null ? r2.getUser_ratings().getScore() : 0;
//                            return Double.compare(score2, score1);
//                        }
//                    });
//
//                    if (featuredRecipes.size() > 5) {
//                        featuredRecipes = featuredRecipes.subList(0, 5);
//                    }
//
//                    featuredRecipesLiveData.setValue(featuredRecipes);
//                    allRecipesLiveData.setValue(recipes);
//
//                } else {
//                    errorLiveData.setValue("Data tidak ditemukan");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<TastyResponse> call, Throwable t) {
//                loadingLiveData.setValue(false);
//                errorLiveData.setValue("Gagal memuat data: " + t.getMessage());
//            }
//        });
//
//    }

    public void loadRecipes() {
        // Tampilkan loading
        loadingLiveData.setValue(true);
        errorLiveData.setValue(null); // Reset error saat mulai request baru

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<TastyResponse> call = apiService.getRecipes(0, 20);

        call.enqueue(new Callback<TastyResponse>() {
            @Override
            public void onResponse(Call<TastyResponse> call, Response<TastyResponse> response) {
                loadingLiveData.setValue(false);

                if (response.isSuccessful() && response.body() != null) {
                    List<Recipe> recipes = response.body().getResults();

                    List<Recipe> featuredRecipes = new ArrayList<>();
                    for (Recipe recipe : recipes) {
                        if (recipe.getUser_ratings() != null && recipe.getUser_ratings().getScore() > 0.92) {
                            featuredRecipes.add(recipe);
                        }
                    }

                    Collections.sort(featuredRecipes, (r1, r2) -> {
                        double score1 = r1.getUser_ratings() != null ? r1.getUser_ratings().getScore() : 0;
                        double score2 = r2.getUser_ratings() != null ? r2.getUser_ratings().getScore() : 0;
                        return Double.compare(score2, score1);
                    });

                    if (featuredRecipes.size() > 5) {
                        featuredRecipes = featuredRecipes.subList(0, 5);
                    }

                    featuredRecipesLiveData.setValue(featuredRecipes);
                    allRecipesLiveData.setValue(recipes);

                    errorLiveData.setValue(null); // Clear error jika berhasil
                } else {
                    errorLiveData.setValue("Data tidak ditemukan");
                }
            }

            @Override
            public void onFailure(Call<TastyResponse> call, Throwable t) {
                loadingLiveData.setValue(false);
                errorLiveData.setValue("Tidak dapat terhubung. Cek koneksi internet Anda.");
            }
        });
    }

    public void clearError() {
        errorLiveData.setValue(null);
    }

}
