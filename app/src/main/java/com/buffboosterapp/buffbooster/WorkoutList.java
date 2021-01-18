package com.buffboosterapp.buffbooster;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class WorkoutList extends AppCompatActivity {
    private WorkoutAdapter entryList;

    private static final String FILE_NAME = "workoutList.txt";

    public int replacePos = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_list);

        Button btn = (Button) findViewById(R.id.button);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newEntry(v);
            }
        });

        ListView list = (ListView) findViewById(R.id.listArea);
        //instantiate custom adapter
        ArrayList<Workout> entries = load();
        entryList = new WorkoutAdapter(entries, this, replacePos);

        list.setAdapter(entryList);

        Intent i = getIntent();

        // Takes information from last page (if applicable)
        if(i.getParcelableExtra("finalWorkout") != null) {
            Workout entry = i.getParcelableExtra("finalWorkout");
            if(entryList.replacePos != -1) {
                entryList.replace(entryList.replacePos, entry);
                entryList.replacePos = -1;
            } else {
                entryList.add(entry);
            }
            entryList.notifyDataSetChanged();
            save();
        }
    }

    public void newEntry(View view) {
        Intent intent = new Intent(this, EntryForm.class);
        save();
        startActivity(intent);
    }

    public void save() {
        try
        {
            File file = new File(getFilesDir() + FILE_NAME);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(entryList.getList());
            oos.writeObject(entryList.replacePos);
            oos.close();
            fos.close();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public ArrayList<Workout> load() {
        ArrayList<Workout> loadList = new ArrayList<Workout>();
        try {
            FileInputStream fos = new FileInputStream(getFilesDir() + FILE_NAME);
            ObjectInputStream oos = new ObjectInputStream(fos);
            loadList = (ArrayList<Workout>) oos.readObject();
            replacePos = (int) oos.readObject();
            oos.close();
            fos.close();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        return loadList;
    }
}