package com.buffboosterapp.buffbooster;

import android.content.Intent;
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

public class WorkoutAdapter extends BaseAdapter implements ListAdapter {
    ArrayList<ArrayList<Entry>> entry;
    static MainActivity main;
    int replacePos;

    public WorkoutAdapter() {
        entry = new ArrayList<ArrayList<Entry>>();
        main = null;
        replacePos = -1;
    }

    public WorkoutAdapter(ArrayList<ArrayList<Entry>> entry, MainActivity main, int replacePos) {
        this.entry = entry;
        this.main = main;
        this.replacePos = replacePos;
    }

    public ArrayList<ArrayList<Entry>> getList() {
        return entry;
    }

    @Override
    public int getCount() {
        return entry.size();
    }

    @Override
    public Object getItem(int position) {
        return entry.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    void add(ArrayList<Entry> element) {
        entry.add(0, element);
    }

    void add(int pos, ArrayList<Entry> element) {
        entry.add(pos, element);
    }

    public void save() {
        try
        {
            File file = new File(main.getFilesDir() + "/main.txt");
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
        Intent intent = new Intent(main, WorkoutList.class);
        intent.putParcelableArrayListExtra("editList", entry.get(position));
        entry.remove(position);
        save();
        main.startActivity(intent);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        RelativeLayout horizon = new RelativeLayout(parent.getContext());
        //horizon.setOrientation(LinearLayout.HORIZONTAL);
        horizon.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

        LinearLayout layout = new LinearLayout(parent.getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        TextView dateText = new TextView(parent.getContext());
        dateText.setLayoutParams(lparams);
        if(entry.get(position).size() > 0) {
            dateText.setText(entry.get(position).get(0).date);
        } else {
            dateText.setText("N/A");
        }
        dateText.setTextSize(24);
        layout.addView(dateText);


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
                entry.remove(position);
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