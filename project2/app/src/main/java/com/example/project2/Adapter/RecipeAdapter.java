//package com.example.project2.Adapter;
//
//
//import android.content.Context;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.recyclerview.widget.RecyclerView;
//import com.squareup.picasso.Picasso;
//import java.util.List;
//import com.example.project2.R;
//import com.example.project2.Model.Recipe;
//
//public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {
//
//    private Context context;
//    private List<Recipe> recipeList;
//
//    public RecipeAdapter(Context context, List<Recipe> recipeList) {
//        this.context = context;
//        this.recipeList = recipeList;
//    }
//
//    @Override
//    public RecipeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(context).inflate(R.layout.item_trend_recipe, parent, false);
//        return new ViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(RecipeAdapter.ViewHolder holder, int position) {
//        Recipe recipe = recipeList.get(position);
//        holder.tvNameFood.setText(recipe.getName());
//        holder.tvCountryFood.setText(recipe.getCountry());
//
//        // Cek URL gambar
//        Log.d("RecipeAdapter", "Binding image: " + recipe.getThumbnailUrl());
//
//        Picasso.get()
//                .load(recipe.getThumbnailUrl())
//                .placeholder(R.drawable.ic_launcher_background)
//                .error(R.drawable.ic_launcher_background)
//                .fit()
//                .centerCrop()
//                .into(holder.imageFood);
//    }
//
//
//    @Override
//    public int getItemCount() {
//        return recipeList.size();
//    }
//
//    public static class ViewHolder extends RecyclerView.ViewHolder {
//        ImageView imageFood;
//        TextView tvNameFood, tvCountryFood;
//
//        public ViewHolder(View itemView) {
//            super(itemView);
//            imageFood = itemView.findViewById(R.id.ivImageFood);
//            tvNameFood = itemView.findViewById(R.id.tvNameFood);
//            tvCountryFood = itemView.findViewById(R.id.tvCountryFood);
//        }
//    }
//}
//
package com.example.project2.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.project2.DetailActivity;
import com.example.project2.Model.Recipe;
import com.example.project2.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RecipeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_FEATURED = 0;
    private static final int TYPE_REGULAR = 1;

    private Context context;
    private List<Recipe> recipeList;
    private List<Recipe> fullList; // full copy for filtering
    private boolean isFeaturedList;

    private OnFilterResultListener filterResultListener;

    public RecipeAdapter(Context context, List<Recipe> recipeList, boolean isFeaturedList) {
        this.context = context;
        this.recipeList = new ArrayList<>(recipeList);
        this.fullList = new ArrayList<>(recipeList);
        this.isFeaturedList = isFeaturedList;
    }

    // Callback Interface
    public interface OnFilterResultListener {
        void onResultCount(int count);
    }

    public void setOnFilterResultListener(OnFilterResultListener listener) {
        this.filterResultListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        return isFeaturedList ? TYPE_FEATURED : TYPE_REGULAR;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_FEATURED) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_trend_recipe, parent, false);
            return new FeaturedViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_recipe, parent, false);
            return new RegularViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Recipe recipe = recipeList.get(position);

        double score = 0.0;
        if (recipe.getUser_ratings() != null) {
            score = recipe.getUser_ratings().getScore();
        }
        double ratingOutOfFive = score * 5;
        String ratingText = String.format(Locale.getDefault(), "%.1f", ratingOutOfFive);

        View.OnClickListener imageClickListener = v -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("SEND_RECIPE", recipe);
            Log.d("RecipeAdapter", "Sending RECIPE_ID = " + recipe.getCanonical_id());
            context.startActivity(intent);
        };

        if (holder instanceof FeaturedViewHolder) {
            FeaturedViewHolder featuredHolder = (FeaturedViewHolder) holder;
            featuredHolder.tvNameFood.setText(recipe.getName());
            featuredHolder.tvCountryFood.setText(recipe.getCountry());
            featuredHolder.tvRating.setText(ratingText);
            Picasso.get()
                    .load(recipe.getThumbnailUrl())
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)
                    .fit()
                    .centerCrop()
                    .into(featuredHolder.imageFood);

            featuredHolder.imageFood.setOnClickListener(imageClickListener);

        } else if (holder instanceof RegularViewHolder) {
            RegularViewHolder regularHolder = (RegularViewHolder) holder;
            regularHolder.tvNameFood.setText(recipe.getName());
            regularHolder.tvCountryFood.setText(recipe.getCountry());
            Picasso.get()
                    .load(recipe.getThumbnailUrl())
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)
                    .fit()
                    .centerCrop()
                    .into(regularHolder.imageFood);

            regularHolder.imageFood.setOnClickListener(imageClickListener);
        }
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    // 🔍 Filter Method
    public void filterList(String query) {
        if (query == null || query.trim().isEmpty()) {
            recipeList = new ArrayList<>(fullList);
        } else {
            List<Recipe> filtered = new ArrayList<>();
            for (Recipe recipe : fullList) {
                if (recipe.getName() != null &&
                        recipe.getName().toLowerCase().contains(query.toLowerCase())) {
                    filtered.add(recipe);
                }
            }
            recipeList = filtered;
        }

        // Callback untuk mengirim hasil filter
        if (filterResultListener != null) {
            filterResultListener.onResultCount(recipeList.size());
        }

        notifyDataSetChanged();
    }

    // ViewHolder Featured
    public static class FeaturedViewHolder extends RecyclerView.ViewHolder {
        ImageView imageFood;
        TextView tvNameFood, tvCountryFood, tvRating;

        public FeaturedViewHolder(View itemView) {
            super(itemView);
            imageFood = itemView.findViewById(R.id.ivImageFood);
            tvNameFood = itemView.findViewById(R.id.tvNameFood);
            tvCountryFood = itemView.findViewById(R.id.tvCountryFood);
            tvRating = itemView.findViewById(R.id.tv_rating_score);
        }
    }

    // ViewHolder Regular
    public static class RegularViewHolder extends RecyclerView.ViewHolder {
        ImageView imageFood;
        TextView tvNameFood, tvCountryFood;

        public RegularViewHolder(View itemView) {
            super(itemView);
            imageFood = itemView.findViewById(R.id.ivImageFood);
            tvNameFood = itemView.findViewById(R.id.tvNameFood);
            tvCountryFood = itemView.findViewById(R.id.tvCountryFood);
        }
    }
}