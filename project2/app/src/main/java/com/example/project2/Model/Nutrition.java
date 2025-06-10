package com.example.project2.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Nutrition implements Parcelable {
    private int calories;
    private int carbohydrates;
    private int fat;
    private int fiber;
    private int protein;
    private int sugar;
    private String updated_at;

    public int getCalories() { return calories; }
    public int getCarbohydrates() { return carbohydrates; }
    public int getFat() { return fat; }
    public int getFiber() { return fiber; }
    public int getProtein() { return protein; }
    public int getSugar() { return sugar; }
    public String getUpdated_at() { return updated_at; }

    protected Nutrition(Parcel in) {
        calories = in.readInt();
        carbohydrates = in.readInt();
        fat = in.readInt();
        fiber = in.readInt();
        protein = in.readInt();
        sugar = in.readInt();
        updated_at = in.readString();
    }

    public static final Creator<Nutrition> CREATOR = new Creator<Nutrition>() {
        @Override
        public Nutrition createFromParcel(Parcel in) {
            return new Nutrition(in);
        }

        @Override
        public Nutrition[] newArray(int size) {
            return new Nutrition[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(calories);
        dest.writeInt(carbohydrates);
        dest.writeInt(fat);
        dest.writeInt(fiber);
        dest.writeInt(protein);
        dest.writeInt(sugar);
        dest.writeString(updated_at);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
