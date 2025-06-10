package com.example.project2.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Instruction implements Parcelable {
    private int id;
    private int position;
    private String display_text;
    private int start_time;
    private int end_time;
    private String appliance;
    private Integer temperature;

    public int getId() { return id; }
    public int getPosition() { return position; }
    public String getDisplay_text() { return display_text; }
    public int getStart_time() { return start_time; }
    public int getEnd_time() { return end_time; }
    public String getAppliance() { return appliance; }
    public Integer getTemperature() { return temperature; }

    protected Instruction(Parcel in) {
        id = in.readInt();
        position = in.readInt();
        display_text = in.readString();
        start_time = in.readInt();
        end_time = in.readInt();
        appliance = in.readString();
        if (in.readByte() == 0) {
            temperature = null;
        } else {
            temperature = in.readInt();
        }
    }

    public static final Creator<Instruction> CREATOR = new Creator<Instruction>() {
        @Override
        public Instruction createFromParcel(Parcel in) {
            return new Instruction(in);
        }

        @Override
        public Instruction[] newArray(int size) {
            return new Instruction[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(position);
        dest.writeString(display_text);
        dest.writeInt(start_time);
        dest.writeInt(end_time);
        dest.writeString(appliance);
        if (temperature == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(temperature);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
