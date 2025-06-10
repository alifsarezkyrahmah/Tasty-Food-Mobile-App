package com.example.project2.Database;

import android.provider.BaseColumns;

public class RecipeContract {
    public static String TABLE_NAME = "favorite";

    public static final class RecipeColumns implements BaseColumns {
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_THUMBNAIL = "thumbnail_url";
        public static final String COLUMN_VIDEO = "video_url";
        public static final String COLUMN_DESCRIPTION = "description";

        // Tambahan:
        public static final String COLUMN_NUTRITION = "nutrition_json";
        public static final String COLUMN_INSTRUCTIONS = "instructions_json";
        public static final String COLUMN_RATINGS = "ratings_json";
    }

}
