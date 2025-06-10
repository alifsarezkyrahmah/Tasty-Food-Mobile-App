package com.example.project2.Database;

import android.database.Cursor;

import com.example.project2.Model.Recipe;

import java.util.ArrayList;

public class MappingHelper {
    public static ArrayList<Recipe> mapCursorToArrayList(Cursor cursor) {
        ArrayList<Recipe> recipes = new ArrayList<>();
        while (cursor.moveToNext()) {
            Recipe recipe = new Recipe();
            recipe.setName(cursor.getString(cursor.getColumnIndexOrThrow(RecipeContract.RecipeColumns.COLUMN_NAME)));
            recipe.setThumbnailUrl(cursor.getString(cursor.getColumnIndexOrThrow(RecipeContract.RecipeColumns.COLUMN_THUMBNAIL)));
            recipe.setVideo_url(cursor.getString(cursor.getColumnIndexOrThrow(RecipeContract.RecipeColumns.COLUMN_VIDEO)));
            recipe.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(RecipeContract.RecipeColumns.COLUMN_DESCRIPTION)));
            recipes.add(recipe);
        }
        return recipes;
    }

}
