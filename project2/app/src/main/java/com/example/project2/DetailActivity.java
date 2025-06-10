package com.example.project2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.project2.Database.RecipeHelper;
import com.example.project2.Model.Instruction;
import com.example.project2.Model.Recipe;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;

import java.util.Locale;

public class DetailActivity extends AppCompatActivity {

    private CollapsingToolbarLayout collapsingToolbar;
    private Toolbar toolbar;
    private ImageView backdrop, ivFavorite;
    private TextView description, foodName;
    private TextView ratingText;
    private MaterialButton videoButton;
    private TextView tvCalories, tvFat, tvProtein, tvCarbs, tvSugar, tvFiber;
    private LinearLayout instructionContainer;
    private Recipe recipe;
    private RecipeHelper recipeHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Ambil data recipe terlebih dahulu
        recipe = getIntent().getParcelableExtra("SEND_RECIPE");

        if (recipe == null) {
            Toast.makeText(this, "Data resep tidak tersedia", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        recipeHelper = new RecipeHelper(this);

        initViews();
        setupToolbar();
        updateUI(recipe);
    }

    private void initViews() {
        collapsingToolbar = findViewById(R.id.toolbar_layout);
        toolbar = findViewById(R.id.toolbar);
        backdrop = findViewById(R.id.backdrop);
        description = findViewById(R.id.description);
        ratingText = findViewById(R.id.tv_rating_score);
        videoButton = findViewById(R.id.video_button);

        foodName = findViewById(R.id.tvNameFood);
        tvCalories = findViewById(R.id.tv_calories);
        tvFat = findViewById(R.id.tv_fat);
        tvProtein = findViewById(R.id.tv_protein);
        tvCarbs = findViewById(R.id.tv_carbs);
        tvSugar = findViewById(R.id.tv_sugar);
        tvFiber = findViewById(R.id.tv_fiber);

        instructionContainer = findViewById(R.id.instruction_container);
        ivFavorite = findViewById(R.id.iv_favorite);

        // Set status ikon favorit berdasarkan database
        updateFavoriteIcon();

        ivFavorite.setOnClickListener(v -> {
            if (recipeHelper.isFavorited(recipe.getName())) {
                recipeHelper.removeFavorite(recipe.getName());
                Toast.makeText(this, "Dihapus dari favorit", Toast.LENGTH_SHORT).show();
            } else {
                recipeHelper.addFavorite(recipe);
                Toast.makeText(this, "Ditambahkan ke favorit", Toast.LENGTH_SHORT).show();
            }
            updateFavoriteIcon();
        });
    }

    private void updateFavoriteIcon() {
        if (recipeHelper.isFavorited(recipe.getName())) {
            ivFavorite.setImageResource(R.drawable.ic_fav_filled);
        } else {
            ivFavorite.setImageResource(R.drawable.ic_fav);
        }
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void updateUI(Recipe recipe) {
        collapsingToolbar.setTitle(recipe.getName());

        if (recipe.getThumbnailUrl() != null && !recipe.getThumbnailUrl().isEmpty()) {
            Picasso.get()
                    .load(recipe.getThumbnailUrl())
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)
                    .fit()
                    .centerCrop()
                    .into(backdrop);
        }

        foodName.setText(recipe.getName());

        description.setText(recipe.getDescription() != null ? recipe.getDescription() : "No description available");

        if (recipe.getUser_ratings() != null) {
            double score = recipe.getUser_ratings().getScore();
            double ratingOutOfFive = score * 5;
            ratingText.setText(String.format(Locale.getDefault(), "%.1f", ratingOutOfFive));
        }

        if (recipe.getVideo_url() != null && !recipe.getVideo_url().isEmpty()) {
            videoButton.setEnabled(true);
            videoButton.setOnClickListener(v -> {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(recipe.getVideo_url()));
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(this, "Tidak dapat membuka video", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            videoButton.setEnabled(false);
            videoButton.setText("Video Tidak Tersedia");
        }

        displayNutritionInfo(recipe);
        displayInstructions(recipe);
    }

    private void displayNutritionInfo(Recipe recipe) {
        if (recipe.getNutrition() != null) {
            tvCalories.setText(String.valueOf(recipe.getNutrition().getCalories()));
            tvFat.setText(String.valueOf(recipe.getNutrition().getFat()));
            tvProtein.setText(String.valueOf(recipe.getNutrition().getProtein()));
            tvCarbs.setText(String.valueOf(recipe.getNutrition().getCarbohydrates()));
            tvSugar.setText(String.valueOf(recipe.getNutrition().getSugar()));
            tvFiber.setText(String.valueOf(recipe.getNutrition().getFiber()));
        }
    }

    private void displayInstructions(Recipe recipe) {
        if (recipe.getInstructions() != null && !recipe.getInstructions().isEmpty()) {
            instructionContainer.removeAllViews();
            for (int i = 0; i < recipe.getInstructions().size(); i++) {
                Instruction instruction = recipe.getInstructions().get(i);
                TextView stepView = new TextView(this);
                stepView.setText(String.format("%d. %s", (i + 1), instruction.getDisplay_text()));
                stepView.setTextSize(16f);
                stepView.setPadding(0, 16, 0, 16);
                stepView.setLineSpacing(4f, 1.2f);
                instructionContainer.addView(stepView);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateFavoriteIcon(); // Refresh icon saat balik ke detail
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
