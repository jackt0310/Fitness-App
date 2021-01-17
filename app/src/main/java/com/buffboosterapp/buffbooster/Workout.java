package com.buffboosterapp.buffbooster;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Workout implements Parcelable {
    ArrayList<Entry> exercises;
    String date;

    public Workout() {
        exercises = new ArrayList<Entry>();
        this.date = "Current Date";
    }

    public int size() {
        if(exercises != null) {
            return exercises.size();
        }
        return -1;
    }

    public Entry get(int position) {
        return exercises.get(position);
    }

    public void add(Entry entry) {
        exercises.add(entry);
    }
    public void add(int position, Entry entry) {
        exercises.add(position, entry);
    }

    public void remove(int position) {
        exercises.remove(position);
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
            out.writeString(date);
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<Workout> CREATOR = new Parcelable.Creator<Workout>() {
        @Override
        public Workout createFromParcel(Parcel in) {
            return new Workout(in);
        }

        public Workout[] newArray(int size) {
            return new Workout[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private Workout(Parcel in) {
        date = in.readString();
    }
}
