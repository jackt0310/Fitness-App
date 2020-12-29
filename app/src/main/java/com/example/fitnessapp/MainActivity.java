package com.example.fitnessapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private WorkoutAdapter entryList;

    private static final String FILE_NAME = "example.txt";

    public int replacePos = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn = (Button) findViewById(R.id.button);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newEntry(v);
            }
        });

        ListView list = (ListView) findViewById(R.id.listArea);
        //instantiate custom adapter
        ArrayList<ArrayList<Entry>> entries = load();
        entryList = new WorkoutAdapter(entries, this, replacePos);

        list.setAdapter(entryList);

        Intent i = getIntent();

        if(i.getParcelableArrayListExtra("finalEntry") != null) {
            ArrayList<Entry> entry = i.getParcelableArrayListExtra("finalEntry");
            if(entryList.replacePos != -1) {
                entryList.add(entryList.replacePos, entry);
                entryList.replacePos = -1;
            } else {
                entryList.add(entry);
            }
            entryList.notifyDataSetChanged();
            save();
        }
    }

    public void newEntry(View view) {
        Intent intent = new Intent(this, WorkoutList.class);
        save();
        startActivity(intent);
    }

    public void save() {
        try
        {
            File file = new File(getFilesDir() + "/main.txt");
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

    public ArrayList<ArrayList<Entry>> load() {
        ArrayList<ArrayList<Entry>> loadList = new ArrayList<ArrayList<Entry>>();
        try {
            FileInputStream fos = new FileInputStream(getFilesDir() + "/main.txt");
            ObjectInputStream oos = new ObjectInputStream(fos);
            loadList = (ArrayList<ArrayList<Entry>>) oos.readObject();
            replacePos = (int) oos.readObject();
            oos.close();
            fos.close();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        return loadList;
    }
}