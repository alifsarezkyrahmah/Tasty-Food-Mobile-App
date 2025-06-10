//package com.example.project2.Adapter;
//
//import android.content.Context;
//import android.content.Intent;
//import android.view.LayoutInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.PopupMenu;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.project2.DetailActivity;
//import com.example.project2.Model.Recipe;
//import com.example.project2.R;
//import com.squareup.picasso.Picasso;
//
//import java.util.List;
//
//public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavViewHolder> {
//
//    private Context context;
//    private List<Recipe> favoriteList;
//    private OnFavoriteActionListener actionListener;
//
//    public interface OnFavoriteActionListener {
//        void onRemoveFavorite(Recipe recipe);
//    }
//
//    public FavoriteAdapter(Context context, List<Recipe> favoriteList, OnFavoriteActionListener actionListener) {
//        this.context = context;
//        this.favoriteList = favoriteList;
//        this.actionListener = actionListener;
//    }
//
//    public void updateData(List<Recipe> newData) {
//        this.favoriteList = newData;
//        notifyDataSetChanged();
//    }
//
//    @NonNull
//    @Override
//    public FavViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(context).inflate(R.layout.item_favorite, parent, false);
//        return new FavViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull FavViewHolder holder, int position) {
//        Recipe recipe = favoriteList.get(position);
//
//        holder.tvName.setText(recipe.getName());
//        holder.tvDescription.setText(recipe.getDescription());
//        holder.tvSavedDate.setText("Disimpan"); // Kamu bisa ganti jadi tanggal jika ada
//
//        // Load image
//        if (recipe.getThumbnailUrl() != null && !recipe.getThumbnailUrl().isEmpty()) {
//            Picasso.get()
//                    .load(recipe.getThumbnailUrl())
//                    .placeholder(R.drawable.ic_launcher_background)
//                    .into(holder.ivImage);
//        } else {
//            holder.ivImage.setImageResource(R.drawable.ic_launcher_background);
//        }
//
//        // Item click to detail
//        holder.itemView.setOnClickListener(v -> {
//            Intent intent = new Intent(context, DetailActivity.class);
//            intent.putExtra("SEND_RECIPE", recipe);
//            context.startActivity(intent);
//        });
//
//        // More options (misal hapus)
//        holder.ivMore.setOnClickListener(v -> {
//            PopupMenu popupMenu = new PopupMenu(context, holder.ivMore);
//            popupMenu.getMenuInflater().inflate(R.menu.menu_fav_item, popupMenu.getMenu());
//            popupMenu.setOnMenuItemClickListener(item -> {
//                if (item.getItemId() == R.id.action_remove_favorite) {
//                    if (actionListener != null) {
//                        actionListener.onRemoveFavorite(recipe);
//                    }
//                    return true;
//                }
//                return false;
//            });
//            popupMenu.show();
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return favoriteList != null ? favoriteList.size() : 0;
//    }
//
//    static class FavViewHolder extends RecyclerView.ViewHolder {
//
//        ImageView ivImage, ivMore;
//        TextView tvName, tvDescription, tvSavedDate;
//
//        public FavViewHolder(@NonNull View itemView) {
//            super(itemView);
//            ivImage = itemView.findViewById(R.id.iv_recipe_image);
//            ivMore = itemView.findViewById(R.id.iv_more_options);
//            tvName = itemView.findViewById(R.id.tv_recipe_name);
//            tvDescription = itemView.findViewById(R.id.tv_recipe_description);
//            tvSavedDate = itemView.findViewById(R.id.tv_saved_date);
//        }
//    }
//}


package com.example.project2.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project2.DetailActivity;
import com.example.project2.Model.Recipe;
import com.example.project2.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavViewHolder> {

    private Context context;
    private List<Recipe> favoriteList;
    private List<Recipe> fullList;
    private OnFavoriteActionListener actionListener;

    public interface OnFavoriteActionListener {
        void onRemoveFavorite(Recipe recipe);
    }

    public FavoriteAdapter(Context context, List<Recipe> favoriteList, OnFavoriteActionListener actionListener) {
        this.context = context;
        this.favoriteList = favoriteList != null ? favoriteList : new ArrayList<>();
        this.fullList = new ArrayList<>(this.favoriteList);
        this.actionListener = actionListener;
    }

    public void updateData(List<Recipe> newData) {
        this.fullList = newData != null ? new ArrayList<>(newData) : new ArrayList<>();
        this.favoriteList = new ArrayList<>(this.fullList);
        notifyDataSetChanged();
    }



    @NonNull
    @Override
    public FavViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_favorite, parent, false);
        return new FavViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavViewHolder holder, int position) {
        Recipe recipe = favoriteList.get(position);

        holder.tvName.setText(recipe.getName());
        holder.tvDescription.setText(recipe.getDescription());
        holder.tvSavedDate.setText("Disimpan"); // Bisa diganti tanggal jika tersedia

        // Load image
        if (recipe.getThumbnailUrl() != null && !recipe.getThumbnailUrl().isEmpty()) {
            Picasso.get()
                    .load(recipe.getThumbnailUrl())
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(holder.ivImage);
        } else {
            holder.ivImage.setImageResource(R.drawable.ic_launcher_background);
        }

        // Item click to detail
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("SEND_RECIPE", recipe);
            context.startActivity(intent);
        });

        // More options (hapus)
        holder.ivMore.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(context, holder.ivMore);
            popupMenu.getMenuInflater().inflate(R.menu.menu_fav_item, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.action_remove_favorite) {
                    if (actionListener != null) {
                        actionListener.onRemoveFavorite(recipe);
                    }
                    return true;
                }
                return false;
            });
            popupMenu.show();
        });
    }

    @Override
    public int getItemCount() {
        return favoriteList != null ? favoriteList.size() : 0;
    }

    static class FavViewHolder extends RecyclerView.ViewHolder {

        ImageView ivImage, ivMore;
        TextView tvName, tvDescription, tvSavedDate;

        public FavViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.iv_recipe_image);
            ivMore = itemView.findViewById(R.id.iv_more_options);
            tvName = itemView.findViewById(R.id.tv_recipe_name);
            tvDescription = itemView.findViewById(R.id.tv_recipe_description);
            tvSavedDate = itemView.findViewById(R.id.tv_saved_date);
        }
    }
}

