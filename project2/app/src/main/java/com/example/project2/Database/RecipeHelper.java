package com.example.project2.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.project2.Model.Instruction;
import com.example.project2.Model.Nutrition;
import com.example.project2.Model.Recipe;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class RecipeHelper {

    private final SQLiteDatabase db;

    public RecipeHelper(Context context) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    // Tambah resep ke favorit
    public void addFavorite(Recipe recipe) {
        ContentValues values = new ContentValues();
        values.put(RecipeContract.RecipeColumns.COLUMN_NAME, recipe.getName());
        values.put(RecipeContract.RecipeColumns.COLUMN_THUMBNAIL, recipe.getThumbnailUrl());
        values.put(RecipeContract.RecipeColumns.COLUMN_VIDEO, recipe.getVideo_url());
        values.put(RecipeContract.RecipeColumns.COLUMN_DESCRIPTION, recipe.getDescription());

        Gson gson = new Gson();

        String nutritionJson = gson.toJson(recipe.getNutrition());
        String instructionsJson = gson.toJson(recipe.getInstructions());
        String ratingsJson = gson.toJson(recipe.getUser_ratings());

        values.put(RecipeContract.RecipeColumns.COLUMN_NUTRITION, nutritionJson);
        values.put(RecipeContract.RecipeColumns.COLUMN_INSTRUCTIONS, instructionsJson);
        values.put(RecipeContract.RecipeColumns.COLUMN_RATINGS, ratingsJson);

        db.insert(RecipeContract.TABLE_NAME, null, values);
    }

    // Hapus resep dari favorit berdasarkan nama
    public void removeFavorite(String name) {
        db.delete(
                RecipeContract.TABLE_NAME,
                RecipeContract.RecipeColumns.COLUMN_NAME + " = ?",
                new String[]{name}
        );
    }

    // Cek apakah sebuah resep sudah difavoritkan (berdasarkan nama)
    public boolean isFavorited(String name) {
        if (db == null || name == null) return false;

        Cursor cursor = db.query(
                RecipeContract.TABLE_NAME,
                null,
                RecipeContract.RecipeColumns.COLUMN_NAME + " = ?",
                new String[]{name},
                null, null, null
        );

        boolean exists = (cursor != null && cursor.moveToFirst());
        if (cursor != null) cursor.close();
        return exists;
    }


    // Ambil semua resep favorit
    public List<Recipe> getAllFavorites() {
        List<Recipe> list = new ArrayList<>();
        Cursor cursor = db.query(
                RecipeContract.TABLE_NAME,
                null,
                null, null, null, null, null
        );

        if (cursor != null && cursor.moveToFirst()) {
            Gson gson = new Gson();
            Type instructionListType = new TypeToken<List<Instruction>>() {}.getType();

            do {
                Recipe recipe = new Recipe();
                recipe.setName(cursor.getString(cursor.getColumnIndexOrThrow(RecipeContract.RecipeColumns.COLUMN_NAME)));
                recipe.setThumbnailUrl(cursor.getString(cursor.getColumnIndexOrThrow(RecipeContract.RecipeColumns.COLUMN_THUMBNAIL)));
                recipe.setVideo_url(cursor.getString(cursor.getColumnIndexOrThrow(RecipeContract.RecipeColumns.COLUMN_VIDEO)));
                recipe.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(RecipeContract.RecipeColumns.COLUMN_DESCRIPTION)));

                // Parse JSON
                String nutritionJson = cursor.getString(cursor.getColumnIndexOrThrow(RecipeContract.RecipeColumns.COLUMN_NUTRITION));
                String instructionsJson = cursor.getString(cursor.getColumnIndexOrThrow(RecipeContract.RecipeColumns.COLUMN_INSTRUCTIONS));
                String ratingsJson = cursor.getString(cursor.getColumnIndexOrThrow(RecipeContract.RecipeColumns.COLUMN_RATINGS));

                recipe.setNutrition(gson.fromJson(nutritionJson, Nutrition.class));
                recipe.setInstructions(gson.fromJson(instructionsJson, instructionListType));
                recipe.setUser_ratings(gson.fromJson(ratingsJson, Recipe.UserRatings.class));

                list.add(recipe);
            } while (cursor.moveToNext());
            cursor.close();
        }

        return list;
    }

}
