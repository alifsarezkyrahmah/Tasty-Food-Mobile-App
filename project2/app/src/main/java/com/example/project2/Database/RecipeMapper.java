//package com.example.project2.Database;
//
//import android.content.ContentValues;
//import android.database.Cursor;
//
//import com.example.project2.Model.Recipe;
//
//public class RecipeMapper {
//
//    // Mapping dari Cursor ke Recipe object
//    public static Recipe mapCursorToRecipe(Cursor cursor) {
//        Recipe recipe = new Recipe();
//
//        recipe.setName(cursor.getString(cursor.getColumnIndexOrThrow(RecipeContract.RecipeColumns.COLUMN_NAME)));
//        recipe.setThumbnailUrl(cursor.getString(cursor.getColumnIndexOrThrow(RecipeContract.RecipeColumns.COLUMN_THUMBNAIL)));
//        recipe.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(RecipeContract.RecipeColumns.COLUMN_DESCRIPTION)));
//
//        return recipe;
//    }
//
//    // Mapping dari Recipe ke ContentValues (untuk insert/update ke DB)
//    public static ContentValues mapRecipeToContentValues(Recipe recipe) {
//        ContentValues values = new ContentValues();
//
//        values.put(RecipeContract.RecipeColumns.COLUMN_NAME, recipe.getName());
//        values.put(RecipeContract.RecipeColumns.COLUMN_THUMBNAIL, recipe.getThumbnailUrl());
//        values.put(RecipeContract.RecipeColumns.COLUMN_DESCRIPTION, recipe.getDescription());
//
//        return values;
//    }
//}
