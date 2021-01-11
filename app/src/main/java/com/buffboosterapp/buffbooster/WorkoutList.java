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

        replacePos = i.getIntExtra("editPos", -1);
        entryList = new EntryAdapter(entries, this, replacePos);

        if(i.getParcelableArrayListExtra("editList") != null) {
            entries = i.getParcelableArrayListExtra("editList");
            entryList = new EntryAdapter(entries, this, replacePos);
            i.removeExtra("editList");
            save();
        }


        list.setAdapter(entryList);

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

        // back button
        Button backBtn = (Button) findViewById(R.id.button4);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WorkoutList.this, MainActivity.class);
                entryList.replacePos = -1;
                save();
                startActivity(intent);
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
        if(!inputIsValid()) {
            Context context = getApplicationContext();
            CharSequence text = "Must have at least one exercise per workout.";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

            return;
        }
        Intent intent = new Intent(this, MainActivity.class);
        TextView date = (TextView) findViewById(R.id.textView3);
        entryList.setDate(date.getText().toString());
        intent.putParcelableArrayListExtra("finalEntry", entryList.getList());
        startActivity(intent);
    }

    public boolean inputIsValid() {
        return entryList.getCount() > 0;
    }
}