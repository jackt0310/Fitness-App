package com.buffboosterapp.buffbooster;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;

import java.io.Serializable;

public class RepElement implements Parcelable, Serializable {
    /* Number of reps */
    public int reps;

    /* If false, no weight is being used */
    public boolean usesWeight;

    /* Weight - ex: 35 */
    int weight;

    /* Units for weight - ex: lbs */
    String weightUnits;

    public RepElement(int reps, boolean usesWeight, int weight, String weightUnits) {
        this.reps = reps;
        this.usesWeight = usesWeight;
        this.weight = weight;
        this.weightUnits = weightUnits;
    }

    /* everything below here is for implementing Parcelable */

    // 99.9% of the time you can just ignore this
    @Override
    public int describeContents() {
        return 0;
    }

    // write your object's data to the passed-in Parcel
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(reps);
        out.writeBoolean(usesWeight);
        out.writeInt(weight);
        out.writeString(weightUnits);
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<RepElement> CREATOR = new Parcelable.Creator<RepElement>() {
        @RequiresApi(api = Build.VERSION_CODES.Q)
        @Override
        public RepElement createFromParcel(Parcel in) {
            return new RepElement(in);
        }

        public RepElement[] newArray(int size) {
            return new RepElement[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    @RequiresApi(api = Build.VERSION_CODES.Q)
    private RepElement(Parcel in) {
        reps = in.readInt();
        usesWeight = in.readBoolean();
        weight = in.readInt();
        weightUnits = in.readString();
    }
}
