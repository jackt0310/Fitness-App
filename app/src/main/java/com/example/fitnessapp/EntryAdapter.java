package com.example.fitnessapp;

import android.content.Intent;
import android.os.Build;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.Gravity;
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

public class EntryAdapter extends BaseAdapter implements ListAdapter {
    ArrayList<Entry> entry;
    static MainActivity main;
    int replacePos;

    public EntryAdapter() {
        entry = new ArrayList<Entry>();
        main = null;
        replacePos = -1;
    }

    public EntryAdapter(ArrayList<Entry> entry, MainActivity main, int replacePos) {
        this.entry = entry;
        this.main = main;
        this.replacePos = replacePos;
    }

    public ArrayList<Entry> getList() {
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

    void add(Entry element) {
        entry.add(0, element);
    }

    void add(int pos, Entry element) {
        entry.add(pos, element);
    }

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
        entry.remove(position);
        replacePos = position;
        save();
        Intent intent = new Intent(main, EntryForm.class);
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
        dateText.setTextSize(28);
        SpannableString content = new SpannableString(entry.get(position).date);
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        dateText.setText(content);

        layout.addView(dateText);

        TextView exerciseText = new TextView(parent.getContext());
        exerciseText.setLayoutParams(lparams);
        exerciseText.setText(entry.get(position).exercise + " (" + entry.get(position).type + ")");
        exerciseText.setTextSize(24);
        layout.addView(exerciseText);

        TextView detailsText = new TextView(parent.getContext());
        detailsText.setLayoutParams(lparams);
        detailsText.setText(entry.get(position).sets + "x" + entry.get(position).reps + " @ " + entry.get(position).weight + "lb");
        detailsText.setTextSize(24);
        layout.addView(detailsText);


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