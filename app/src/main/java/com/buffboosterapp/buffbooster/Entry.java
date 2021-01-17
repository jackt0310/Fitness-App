package com.buffboosterapp.buffbooster;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

public class Entry implements Parcelable, Serializable {
    /* Type of exercise - ex: Custom */
    public String type;

    /* Name of exercise - ex: Dumbbell Curls */
    public String exerciseName;

    /* Number of sets */
    public int numSets;

    /* If true, uses reps; otherwise, uses time */
    public boolean usesReps;

    /* List of each set - using reps as a measurement */
    public ArrayList<RepElement> setReps;

    /* List of each set - using time as a measurement */
    public ArrayList<TimeElement> setTimes;

    /* Notes about current exercise */
    public String notes;

    public Entry(String type, String exerciseName, int numSets, boolean usesReps, ArrayList<RepElement> setReps, ArrayList<TimeElement> setTimes, String notes) {
        this.type = type;
        this.exerciseName = exerciseName;
        this.numSets = numSets;
        this.usesReps = usesReps;
        this.setReps = setReps;
        this.setTimes = setTimes;
        this.notes = notes;
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
    }

}
