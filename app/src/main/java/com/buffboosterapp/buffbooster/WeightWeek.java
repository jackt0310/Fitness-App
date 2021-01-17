package com.buffboosterapp.buffbooster;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class WeightWeek implements Serializable {
    LocalDate startDate;
    LocalDate endDate;
    ArrayList<WeightEntry> weightEntries;

    WeightWeek(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
        weightEntries = new ArrayList<WeightEntry>();
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    String getDateRange() {
        return startDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")) + " - " + endDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    boolean isInRange(LocalDate date) {
        boolean isInRange = true;
        if(date.isAfter(endDate) || date.isBefore(startDate)) {
            isInRange = false;
        }
        return isInRange;
    }

    double getAvgWeight() {
        double total = 0.0;

        for(int i = 0; i < weightEntries.size(); i++) {
            total += weightEntries.get(i).weight;
        }

        return total / weightEntries.size();
    }
}
