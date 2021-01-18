package com.buffboosterapp.buffbooster;

import android.content.Intent;
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

import androidx.cardview.widget.CardView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class WorkoutAdapter extends BaseAdapter implements ListAdapter {
    ArrayList<Workout> workouts;
    static WorkoutList main;
    int replacePos;

    public WorkoutAdapter() {
        workouts = new ArrayList<Workout>();
        main = null;
        replacePos = -1;
    }

    public WorkoutAdapter(ArrayList<Workout> workouts, WorkoutList main, int replacePos) {
        this.workouts = workouts;
        this.main = main;
        this.replacePos = replacePos;
    }

    public ArrayList<Workout> getList() {
        return workouts;
    }

    @Override
    public int getCount() {
        return workouts.size();
    }

    @Override
    public Object getItem(int position) {
        return workouts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    void add(Workout element) {
        workouts.add(0, element);
    }

    void add(int pos, Workout element) {
        workouts.add(pos, element);
    }

    void replace(int pos, Workout element) {
        workouts.set(pos, element);
    }

    public void save() {
        try
        {
            File file = new File(main.getFilesDir() + "workoutList.txt");
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
        }
    }

    public void edit(int position) {
        replacePos = position;
        Intent intent = new Intent(main, EntryForm.class);
        intent.putExtra("editList", (Parcelable) workouts.get(position));
        //workouts.remove(position);
        save();
        main.startActivity(intent);
    }

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
        dateText.setText(workouts.get(position).date);
        dateText.setTextSize(24);
        layout.addView(dateText);




        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);

        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        /* Vertical layout containing buttons */
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
                workouts.remove(position);
                notifyDataSetChanged();
                save();
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