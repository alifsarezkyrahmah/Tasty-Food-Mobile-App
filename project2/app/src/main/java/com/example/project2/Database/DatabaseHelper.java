package com.example.project2.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "favoriterecipe.db";
    private static final int DATABASE_VERSION = 3;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // CREATE TABLE favorite
        String CREATE_TABLE = "CREATE TABLE " + RecipeContract.TABLE_NAME + " (" +
                RecipeContract.RecipeColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                RecipeContract.RecipeColumns.COLUMN_NAME + " TEXT, " +
                RecipeContract.RecipeColumns.COLUMN_THUMBNAIL + " TEXT, " +
                RecipeContract.RecipeColumns.COLUMN_VIDEO + " TEXT, " +
                RecipeContract.RecipeColumns.COLUMN_DESCRIPTION + " TEXT, " +
                RecipeContract.RecipeColumns.COLUMN_NUTRITION + " TEXT, " +
                RecipeContract.RecipeColumns.COLUMN_INSTRUCTIONS + " TEXT, " +
                RecipeContract.RecipeColumns.COLUMN_RATINGS + " TEXT)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + RecipeContract.TABLE_NAME);
        onCreate(db);
    }
}
