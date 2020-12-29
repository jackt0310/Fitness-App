package com.example.fitnessapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class WorkoutList extends AppCompatActivity {
    private EntryAdapter entryList;

    private static final String FILE_NAME = "example.txt";

    public int replacePos = -1;

    @RequiresApi(api = Build.VERSION_CODES.O)
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

        Intent i = getIntent();
        ArrayList<Entry> entries = new ArrayList<Entry>();
        System.out.println(getIntent().getIntExtra("editPos", -1));
        replacePos = i.getIntExtra("editPos", -1);
        entryList = new EntryAdapter(entries, this, replacePos);



        Log.d("myTag", "This is my parcel message");
        System.out.println(i.getParcelableArrayListExtra("editList") != null);

        if(i.getParcelableArrayListExtra("editList") != null) {
            entries = i.getParcelableArrayListExtra("editList");
            entryList = new EntryAdapter(entries, this, replacePos);
            i.removeExtra("editList");
            save();
        }


        list.setAdapter(entryList);
        Log.d("myTag", "Parcel extra found");
        System.out.println(i.getParcelableExtra("entry") != null);

        if(i.getParcelableExtra("entry") != null) {
            Entry entry = (Entry) i.getParcelableExtra("entry");
            if(entryList.replacePos != -1) {
                System.out.println("replacePos");
                entryList.add(entryList.replacePos, entry);
                entryList.replacePos = -1;
            } else {
                entryList.add(entry);
            }
            entryList.notifyDataSetChanged();
            save();
        }

        Button subBtn = (Button) findViewById(R.id.button3);

        subBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit(v);
            }
        });

        TextView date = (TextView) findViewById(R.id.textView3);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDateTime now = LocalDateTime.now();
        date.setText(dtf.format(now));
    }

    public void newEntry(View view) {
        Intent intent = new Intent(this, EntryForm.class);
        save();
        intent.putParcelableArrayListExtra("editList", entryList.getList());
        startActivity(intent);
    }

    /*
    @Override
    public void onRestoreInstanceState(Bundle savedInstance){
        onCreate(savedInstance);
    }*/

    public void save() {
        try
        {
            File file = new File(getFilesDir() + "/output.txt");
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

    public ArrayList<Entry> load() {
        ArrayList<Entry> loadList = new ArrayList<Entry>();
        try {
            FileInputStream fos = new FileInputStream(getFilesDir() + "/output.txt");
            ObjectInputStream oos = new ObjectInputStream(fos);
            loadList = (ArrayList<Entry>) oos.readObject();
            replacePos = (int) oos.readObject();
            oos.close();
            fos.close();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        return loadList;
    }

    public void submit(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        TextView date = (TextView) findViewById(R.id.textView3);
        entryList.setDate(date.getText().toString());
        intent.putParcelableArrayListExtra("finalEntry", entryList.getList());
        startActivity(intent);
    }
}