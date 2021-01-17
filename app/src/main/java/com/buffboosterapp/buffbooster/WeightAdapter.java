package com.buffboosterapp.buffbooster;

import android.content.Intent;
import android.os.Build;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
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
        /*
        try
        {
            File file = new File(main.getFilesDir() + "/main.txt");
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(workouts);
            oos.writeObject(replacePos);
            oos.close();
            fos.close();
        } catch(Exception ex) {
            ex.printStackTrace();
        }*/
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
        Button editButton = new Button(parent.getContext());
        editButton.setLayoutParams(lparams);
        editButton.setText("Edit");
        editButton.setTextSize(20);
        editButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                edit(position);
            }
        });
        buttons.addView(editButton);

        Button deleteButton = new Button(parent.getContext());
        deleteButton.setLayoutParams(lparams);
        deleteButton.setText("Delete");
        deleteButton.setTextSize(20);

        deleteButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                workouts.remove(position);
                notifyDataSetChanged();
                save();
            }
        });

        buttons.addView(deleteButton);

        buttons.setLayoutParams(layoutParams);


         */
        horizon.addView(layout);
        //horizon.addView(buttons);

        CardView cardView = new CardView(parent.getContext());
        cardView.addView(horizon);
        return (cardView);
    }
}