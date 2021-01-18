package com.buffboosterapp.buffbooster;

import android.content.Intent;
import android.os.Build;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class WeightAdapter extends BaseAdapter implements ListAdapter {
    ArrayList<WeightWeek> weightWeeks;
    static WeightLog main;

    public WeightAdapter() {
        weightWeeks = new ArrayList<WeightWeek>();
        main = null;
    }

    public WeightAdapter(ArrayList<WeightWeek> weightWeeks, WeightLog main) {
        this.weightWeeks = weightWeeks;
        this.main = main;
    }

    public ArrayList<WeightWeek> getList() {
        return weightWeeks;
    }

    @Override
    public int getCount() {
        return weightWeeks.size();
    }

    @Override
    public Object getItem(int position) {
        return weightWeeks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    void add(WeightWeek element) {
        weightWeeks.add(0, element);
    }

    void add(int pos, WeightWeek element) {
        weightWeeks.add(pos, element);
    }

    void replace(int pos, WeightWeek element) {
        weightWeeks.set(pos, element);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    void addWeightEntry(WeightEntry entry) {
        if(weightWeeks.size() <= 0) {
            WeightWeek week = new WeightWeek(entry.getWeekStart(), entry.getWeekEnd());
            week.weightEntries.add(entry);
            weightWeeks.add(week);
        } else {
            boolean done = false;
            for(int i = 0; i < weightWeeks.size() && !done; i++) {
                if(weightWeeks.get(i).isInRange(entry.date.toLocalDate())) {
                    weightWeeks.get(i).weightEntries.add(entry);
                    done = true;
                }
            }
            if(!done) {
                WeightWeek week = new WeightWeek(entry.getWeekStart(), entry.getWeekEnd());
                week.weightEntries.add(entry);
                weightWeeks.add(week);
            }
        }
    }

    public void save() {
        main.save();
        main.setData();
    }

    public void edit(int position) {
        /*replacePos = position;
        Intent intent = new Intent(main, WorkoutList.class);
        intent.putExtra("editList", (Parcelable) workouts.get(position));
        //entry.remove(position);
        save();
        main.startActivity(intent);*/
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public View getView(int position, View convertView, ViewGroup parent) {
        /* Horizontal layout containing everything else */
        RelativeLayout horizon = new RelativeLayout(parent.getContext());
        horizon.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

        /* Vertical layout containing information on card */
        LinearLayout layout = new LinearLayout(parent.getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        TextView dateText = new TextView(parent.getContext());
        dateText.setLayoutParams(lparams);
        dateText.setText("Week " + (position + 1) + " (" + weightWeeks.get(position).getDateRange() + ")");
        dateText.setTextSize(20);
        layout.addView(dateText);

        for(int i = 0; i < weightWeeks.get(position).weightEntries.size(); i++) {
            RelativeLayout horizonWeight = new RelativeLayout(parent.getContext());
            horizon.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

            TextView singleWeight = new TextView(parent.getContext());
            singleWeight.setLayoutParams(lparams);
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy");

            singleWeight.setText(dtf.format(weightWeeks.get(position).weightEntries.get(i).date) + ": " + weightWeeks.get(position).weightEntries.get(i).weight + "lbs");

            horizonWeight.addView(singleWeight);

            ImageButton deleteButton = new ImageButton(parent.getContext());
            deleteButton.setImageResource(R.drawable.deleteicon);
            deleteButton.setScaleType(ImageView.ScaleType.FIT_CENTER);

            //deleteButton.setLayoutParams(new LinearLayout.LayoutParams(30, 30));
            deleteButton.setBackground(null);

            int finalI = i;
            deleteButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    weightWeeks.get(position).weightEntries.remove(finalI);
                    notifyDataSetChanged();
                    save();
                }
            });

            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                    120,120);

            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            deleteButton.setLayoutParams(layoutParams);
            horizonWeight.addView(deleteButton);

            layout.addView(horizonWeight);
        }
        TextView countText = new TextView(parent.getContext());
        countText.setLayoutParams(lparams);
        countText.setText("Entries: " + weightWeeks.get(position).weightEntries.size());
        countText.setTextSize(20);
        layout.addView(countText);

        TextView avgText = new TextView(parent.getContext());
        avgText.setLayoutParams(lparams);
        avgText.setText("Average weight: " + String.format("%.2f", weightWeeks.get(position).getAvgWeight()) + "lb");
        avgText.setTextSize(20);
        layout.addView(avgText);


        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);

        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        /* Vertical layout containing buttons */
        LinearLayout buttons = new LinearLayout(parent.getContext());
        buttons.setOrientation(LinearLayout.VERTICAL);
        buttons.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        /*
        ImageButton editButton = new ImageButton(parent.getContext());
        editButton.setImageResource(R.drawable.pencil);
        editButton.setScaleType(ImageView.ScaleType.FIT_CENTER);
        editButton.setLayoutParams(new LinearLayout.LayoutParams(150, 150));
        editButton.setBackground(null);

        editButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                edit(position);
            }
        });
        buttons.addView(editButton);


        buttons.setLayoutParams(layoutParams);*/
        horizon.addView(layout);
        //horizon.addView(buttons);

        CardView cardView = new CardView(parent.getContext());
        cardView.addView(horizon);
        return (cardView);
    }
}