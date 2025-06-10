package com.example.project2.Api;

import com.example.project2.Model.Recipe;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TastyResponse {


    private List<Recipe> results;

    public List<Recipe> getResults() {
        return results;
    }
}
