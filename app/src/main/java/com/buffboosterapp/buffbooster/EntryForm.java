package com.buffboosterapp.buffbooster;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class EntryForm extends AppCompatActivity {
    private EntryAdapter currentWorkout;

    private static final String FILE_NAME = "example.txt";

    public int replacePos = -1;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_form);

        LinearLayout root = (LinearLayout)findViewById(R.id.root);
        Button btn = (Button) findViewById(R.id.button);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout layout = new LinearLayout(EntryForm.this);
                ArrayList<String> spinnerArray = new ArrayList<String>();
                spinnerArray.add("Custom");

                Spinner spinner = new Spinner(EntryForm.this);
                spinner.setPrompt("Choose an exercise...");
                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(EntryForm.this, android.R.layout.simple_spinner_dropdown_item, spinnerArray);
                spinner.setAdapter(spinnerArrayAdapter);

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                        // your code here
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {
                        // your code here
                    }

                });

                root.removeView(findViewById(R.id.button));
                root.addView(spinner);

            }
        });

        ListView list = (ListView) findViewById(R.id.listArea);

        //instantiate custom adapter

        Intent i = getIntent();
        Workout workout = new Workout();

        replacePos = i.getIntExtra("editPos", -1);
        currentWorkout = new EntryAdapter(workout, this, replacePos);

        if(i.getParcelableArrayListExtra("editList") != null) {
            workout = i.getParcelableExtra("editList");
            currentWorkout = new EntryAdapter(workout, this, replacePos);
            i.removeExtra("editList");
            save();
        }


        list.setAdapter(currentWorkout);

        if(i.getParcelableExtra("entry") != null) {
            Entry entry = (Entry) i.getParcelableExtra("entry");
            if(currentWorkout.replacePos != -1) {
                currentWorkout.add(currentWorkout.replacePos, entry);
                currentWorkout.replacePos = -1;
            } else {
                currentWorkout.add(entry);
            }
            currentWorkout.notifyDataSetChanged();
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
                Intent intent = new Intent(EntryForm.this, WorkoutList.class);
                currentWorkout.replacePos = -1;
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

    }

    /*
    @Override
    public void onRestoreInstanceState(Bundle savedInstance){
        onCreate(savedInstance);
    }*/

    public void save() {
        /*
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
        */
    }

    public ArrayList<Entry> load() {
        ArrayList<Entry> loadList = new ArrayList<Entry>();
        /*
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
         */
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
        Intent intent = new Intent(this, WorkoutList.class);
        TextView date = (TextView) findViewById(R.id.textView3);
        currentWorkout.setDate(date.getText().toString());
        //intent.putParcelableArrayListExtra("finalEntry", currentWorkout.getList());
        startActivity(intent);
    }

    public boolean inputIsValid() {
        return currentWorkout.getCount() > 0;
    }

    /*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_form);

        Button btn = (Button) findViewById(R.id.button2);

        Intent i = getIntent();

        if(i.getParcelableExtra("editEntry") != null) {
            Entry entry = (Entry) i.getParcelableExtra("editEntry");
            ((TextView) findViewById(R.id.editExercise)).setText(entry.exercise);
            ((TextView) findViewById(R.id.editType)).setText(entry.type);
            ((TextView) findViewById(R.id.editSets)).setText(entry.sets);
            ((TextView) findViewById(R.id.editReps)).setText(entry.reps);
            ((TextView) findViewById(R.id.editWeight)).setText(entry.weight);
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!inputIsValid()) {
                    Context context = getApplicationContext();
                    CharSequence text = "Must enter information for all fields.";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
                else {
                    submit(v);
                }
            }
        });

        //back button

        Button backBtn = (Button) findViewById(R.id.button5);

        backBtn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) { submit(v); }
        });

    }

    public void submit(View view) {

        String editExercise = ((TextView) findViewById(R.id.editExercise)).getText().toString();
        String editType = ((TextView) findViewById(R.id.editType)).getText().toString();
        String editSets = ((TextView) findViewById(R.id.editSets)).getText().toString();
        String editReps = ((TextView) findViewById(R.id.editReps)).getText().toString();
        String editWeight = ((TextView) findViewById(R.id.editWeight)).getText().toString();

        Entry entry = new Entry(editExercise, editType, editSets, editReps, editWeight);

        Intent intent = new Intent(this, WorkoutList.class);
        intent.putParcelableArrayListExtra("editList", getIntent().getParcelableArrayListExtra("editList"));
        intent.putExtra("editPos", getIntent().getIntExtra("editPos", -1));
        intent.putExtra("entry", (Parcelable) entry);
        startActivity(intent);
    }

    public boolean inputIsValid() {
        boolean isValid = true;

        if(((TextView) findViewById(R.id.editExercise)).getText().toString().equals("")) {
            isValid = false;
        }
        if(((TextView) findViewById(R.id.editType)).getText().toString().equals("")) {
            isValid = false;
        }
        if(((TextView) findViewById(R.id.editSets)).getText().toString().equals("")) {
            isValid = false;
        }
        if(((TextView) findViewById(R.id.editReps)).getText().toString().equals("")) {
            isValid = false;
        }
        if(((TextView) findViewById(R.id.editWeight)).getText().toString().equals("")) {
            isValid = false;
        }

        return isValid;
    }

*/
}