package com.buffboosterapp.buffbooster;

import android.content.Intent;
import android.os.Parcelable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class EntryAdapter extends BaseAdapter implements ListAdapter {
    Workout workout;
    static EntryForm main;
    int replacePos;

    public EntryAdapter() {
        workout = new Workout();
        main = null;
        replacePos = -1;
    }

    public EntryAdapter(Workout workout, EntryForm main, int replacePos) {
        this.workout = workout;
        this.main = main;
        this.replacePos = replacePos;
    }

    public Workout getWorkout() {
        return workout;
    }

    @Override
    public int getCount() {
        return workout.size();
    }

    @Override
    public Object getItem(int position) {
        return workout.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    void add(Entry element) {
        workout.add(0, element);
    }

    void add(int pos, Entry element) {
        workout.add(pos, element);
    }

    public void setDate(String date) {
        workout.date = date;
    }

    /*
    public void save() {
        try
        {
            File file = new File(main.getFilesDir() + "/output.txt");
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(entry);
            oos.writeObject(replacePos);
            oos.close();
            fos.close();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public void edit(int position) {

        replacePos = position;
        Intent intent = new Intent(main, EntryForm.class);
        intent.putExtra("editEntry", (Parcelable) entry.get(position));
        entry.remove(position);
        save();
        intent.putExtra("editPos", position);
        intent.putParcelableArrayListExtra("editList", entry);
        main.startActivity(intent);
    }*/

    public View getView(int position, View convertView, ViewGroup parent) {
        /* Horizontal layout containing everything within card */
        RelativeLayout horizon = new RelativeLayout(parent.getContext());
        horizon.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

        /* Vertical layout containing the information within card */
        LinearLayout layout = new LinearLayout(parent.getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lparams.gravity = Gravity.CENTER_VERTICAL;


        TextView exerciseText = new TextView(parent.getContext());
        exerciseText.setLayoutParams(lparams);
        exerciseText.setText(workout.get(position).exerciseName);
        exerciseText.setTextSize(19);
        layout.addView(exerciseText);

        /*
        TextView detailsText = new TextView(parent.getContext());
        detailsText.setLayoutParams(lparams);
        detailsText.setText(entry.get(position).sets + "x" + entry.get(position).reps + " @ " + entry.get(position).weight + "lb");
        detailsText.setTextSize(19);
        layout.addView(detailsText);
        */



        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);

        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        LinearLayout buttons = new LinearLayout(parent.getContext());
        buttons.setOrientation(LinearLayout.VERTICAL);
        buttons.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        Button editButton = new Button(parent.getContext());
        editButton.setLayoutParams(lparams);
        editButton.setText("Edit");
        editButton.setTextSize(20);
        editButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //edit(position);
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
                workout.remove(position);
                notifyDataSetChanged();
                //save();
            }
        });

        buttons.addView(deleteButton);

        buttons.setLayoutParams(layoutParams);
        horizon.addView(layout);
        horizon.addView(buttons);

        CardView cardView = new CardView(parent.getContext());
        cardView.addView(horizon);
        return (cardView);
    }
}