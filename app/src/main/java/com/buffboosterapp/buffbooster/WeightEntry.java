package com.buffboosterapp.buffbooster;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class WeightEntry implements Serializable {
    double weight;
    LocalDateTime date;

    WeightEntry(double weight, LocalDateTime date) {
        this.weight = weight;
        this.date = date;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    LocalDate getWeekStart() {
        LocalDate start = date.toLocalDate();

        while(start.getDayOfWeek() != DayOfWeek.SUNDAY) {
            start = start.minusDays(1);
        }
        return start;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    LocalDate getWeekEnd() {
        LocalDate end = date.toLocalDate();
        while(end.getDayOfWeek() != DayOfWeek.SATURDAY) {
            end = end.plusDays(1);
        }
        return end;
    }

}
