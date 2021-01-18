package com.buffboosterapp.buffbooster;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;

import java.io.Serializable;

public class TimeElement implements Parcelable, Serializable {
    /* Time of current set - ex: 30 */
    public String time;

    /* Units for time - ex: seconds */
    public String timeUnits;

    /* If false, no weight is being used */
    public boolean usesWeight;

    /* Weight - ex: 35 */
    int weight;

    /* Units for weight - ex: lbs */
    String weightUnits;

    TimeElement(String time, String timeUnits, boolean usesWeight, int weight, String weightUnits) {
        this.time = time;
        this.timeUnits = timeUnits;
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
        out.writeString(time);
        out.writeString(timeUnits);
        out.writeBoolean(usesWeight);
        out.writeInt(weight);
        out.writeString(weightUnits);
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<TimeElement> CREATOR = new Parcelable.Creator<TimeElement>() {
        @RequiresApi(api = Build.VERSION_CODES.Q)
        @Override
        public TimeElement createFromParcel(Parcel in) {
            return new TimeElement(in);
        }

        public TimeElement[] newArray(int size) {
            return new TimeElement[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    @RequiresApi(api = Build.VERSION_CODES.Q)
    private TimeElement(Parcel in) {
        time = in.readString();
        timeUnits = in.readString();
        usesWeight = in.readBoolean();
        weight = in.readInt();
        weightUnits = in.readString();
    }
}
