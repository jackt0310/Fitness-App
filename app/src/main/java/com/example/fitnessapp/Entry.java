package com.example.fitnessapp;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Entry implements Parcelable, Serializable {
    public String exercise;
    public String type;
    public String date;
    public String sets;
    public String reps;
    public String weight;

    public Entry(String exercise, String type, String sets, String reps, String weight) {
        this.exercise = exercise;
        this.type = type;
        this.date = "";
        this.sets = sets;
        this.reps = reps;
        this.weight = weight;
    }


    /* everything below here is for implementing Parcelable */

    // 99.9% of the time you can just ignore this
    @Override
    public int describeContents() {
        return 0;
    }

    // write your object's data to the passed-in Parcel
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(exercise);
        out.writeString(type);
        out.writeString(date);
        out.writeString(sets);
        out.writeString(reps);
        out.writeString(weight);
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<Entry> CREATOR = new Parcelable.Creator<Entry>() {
        @Override
        public Entry createFromParcel(Parcel in) {
            return new Entry(in);
        }

        public Entry[] newArray(int size) {
            return new Entry[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private Entry(Parcel in) {
        exercise = in.readString();
        type = in.readString();
        date = in.readString();
        sets = in.readString();
        reps = in.readString();
        weight = in.readString();
    }

}
