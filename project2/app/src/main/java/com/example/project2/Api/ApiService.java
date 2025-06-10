package com.example.project2.Api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("recipes/list")
    Call<TastyResponse> getRecipes(
            @Query("from") int from,
            @Query("size") int size
    );


}
