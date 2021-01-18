package com.buffboosterapp.buffbooster;

import android.content.Intent;
import android.os.Parcelable;
import android.view.Gravity;
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

import androidx.cardview.widget.CardView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class EntryAdapter extends BaseAdapter implements ListAdapter {
    Workout workout;
    static EntryForm main;

    public EntryAdapter() {
        workout = new Workout();
        main = null;
    }

    public EntryAdapter(Workout workout, EntryForm main) {
        this.workout = workout;
        this.main = main;
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
        workout.add(element);
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
    */
    void replace(int pos, Entry element) {
        System.out.println(pos + " replace");
        workout.remove(pos);
        workout.add(pos, element);
    }

    public void edit(int position) {
        main.setEditView(position);
                /*
        Intent intent = new Intent(main, EntryForm.class);
        intent.putExtra("editEntry", (Parcelable) entry.get(position));
        entry.remove(position);
        save();
        intent.putExtra("editPos", position);
        intent.putParcelableArrayListExtra("editList", entry);
        main.startActivity(intent);*/
    }

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

        TextView detailsText = new TextView(parent.getContext());
        detailsText.setLayoutParams(lparams);

        detailsText.setText(workout.get(position).numSets + " Sets");
        detailsText.setTextSize(19);
        layout.addView(detailsText);

        for(int i = 0; i < workout.get(position).numSets; i++) {
            TextView setText = new TextView(parent.getContext());
            setText.setLayoutParams(lparams);

            if(workout.get(position).usesReps) {
                setText.setText("     " + workout.get(position).setReps.get(i).reps + " Reps @ " + workout.get(position).setReps.get(i).weight + workout.get(position).setReps.get(i).weightUnits);
            } else {
                setText.setText("     " + workout.get(position).setTimes.get(i).time + " " + workout.get(position).setTimes.get(i).timeUnits + " @ " + workout.get(position).setTimes.get(i).weight + workout.get(position).setTimes.get(i).weightUnits);
            }

            setText.setTextSize(19);
            layout.addView(setText);
        }

        if(!workout.get(position).notes.equals("")) {
            TextView notesText = new TextView(parent.getContext());
            notesText.setLayoutParams(lparams);

            notesText.setText("Notes: " + workout.get(position).notes);
            notesText.setTextSize(19);
            layout.addView(notesText);
        }

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);

        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        LinearLayout buttons = new LinearLayout(parent.getContext());
        buttons.setOrientation(LinearLayout.VERTICAL);
        buttons.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

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

        ImageButton deleteButton = new ImageButton(parent.getContext());
        deleteButton.setImageResource(R.drawable.deleteicon);
        deleteButton.setScaleType(ImageView.ScaleType.FIT_CENTER);

        deleteButton.setLayoutParams(new LinearLayout.LayoutParams(150, 150));
        deleteButton.setBackground(null);

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