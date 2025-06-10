package com.example.project2.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.List;

public class Recipe implements Parcelable {

    private String canonical_id;
    private String name;
    private String thumbnail_url;
    private String country;
    private UserRatings user_ratings;
    private String video_url;
    private String description;
    private Nutrition nutrition;
    private List<Instruction> instructions;

    // Default constructor
    public Recipe() {}

    // Constructor from Parcel
    protected Recipe(Parcel in) {
        canonical_id = in.readString();
        name = in.readString();
        thumbnail_url = in.readString();
        country = in.readString();
        user_ratings = in.readParcelable(UserRatings.class.getClassLoader()); // Fixed: was missing
        video_url = in.readString();
        description = in.readString();
        nutrition = in.readParcelable(Nutrition.class.getClassLoader());
        instructions = in.createTypedArrayList(Instruction.CREATOR);
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    // Getters
    public String getCanonical_id() {
        return canonical_id;
    }

    public String getName() {
        return name;
    }

    public String getThumbnailUrl() {
        return thumbnail_url;
    }

    public String getCountry() {
        return country;
    }

    public String getVideo_url() {
        return video_url;
    }

    public String getDescription() {
        return description;
    }

    public UserRatings getUser_ratings() {
        return user_ratings;
    }

    public Nutrition getNutrition() {
        return nutrition;
    }

    public List<Instruction> getInstructions() {
        return instructions;
    }

    // Setters
    public void setCanonical_id(String canonical_id) {
        this.canonical_id = canonical_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setThumbnailUrl(String thumbnail_url) {
        this.thumbnail_url = thumbnail_url;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUser_ratings(UserRatings user_ratings) {
        this.user_ratings = user_ratings;
    }

    public void setNutrition(Nutrition nutrition) {
        this.nutrition = nutrition;
    }

    public void setInstructions(List<Instruction> instructions) {
        this.instructions = instructions;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(canonical_id);
        dest.writeString(name);
        dest.writeString(thumbnail_url);
        dest.writeString(country);
        dest.writeParcelable(user_ratings, flags); // Fixed: was missing
        dest.writeString(video_url);
        dest.writeString(description);
        dest.writeParcelable(nutrition, flags);
        dest.writeTypedList(instructions);
    }

    // Inner class for user_ratings - now implements Parcelable
    public static class UserRatings implements Parcelable {
        private int count_negative;
        private int count_positive;
        private double score;

        public UserRatings() {}

        protected UserRatings(Parcel in) {
            count_negative = in.readInt();
            count_positive = in.readInt();
            score = in.readDouble();
        }

        public static final Creator<UserRatings> CREATOR = new Creator<UserRatings>() {
            @Override
            public UserRatings createFromParcel(Parcel in) {
                return new UserRatings(in);
            }

            @Override
            public UserRatings[] newArray(int size) {
                return new UserRatings[size];
            }
        };

        public int getCount_negative() {
            return count_negative;
        }

        public void setCount_negative(int count_negative) {
            this.count_negative = count_negative;
        }

        public int getCount_positive() {
            return count_positive;
        }

        public void setCount_positive(int count_positive) {
            this.count_positive = count_positive;
        }

        public double getScore() {
            return score;
        }

        public void setScore(double score) {
            this.score = score;
        }

        @Override
        public void writeToParcel(@NonNull Parcel dest, int flags) {
            dest.writeInt(count_negative);
            dest.writeInt(count_positive);
            dest.writeDouble(score);
        }

        @Override
        public int describeContents() {
            return 0;
        }
    }
}