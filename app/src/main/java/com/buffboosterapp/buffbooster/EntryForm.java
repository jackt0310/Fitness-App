package com.buffboosterapp.buffbooster;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;

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

    private boolean initialSpinner = true;

    private LinearLayout formLayout;

    private Button addButton;

    void removeLayout() {
        Spinner mySpinner = (Spinner) findViewById(R.id.spinner_type);
        String type = mySpinner.getSelectedItem().toString();

        EditText exerciseNameForm = findViewById(R.id.exerciseNameForm);
        String exerciseName =  exerciseNameForm.getText().toString();

        EditText numSetsForm = findViewById(R.id.numSetsForm);
        int numSets = Integer.parseInt(numSetsForm.getText().toString());

        Spinner spinnerTimeReps = (Spinner) findViewById(R.id.spinnerTimeReps);
        String timeReps = spinnerTimeReps.getSelectedItem().toString();

        boolean usesReps = false;
        if(timeReps == "Reps") {
            usesReps = true;
        }

        EditText notesForm = findViewById(R.id.notesForm);
        String notes =  notesForm.getText().toString();

        ArrayList<RepElement> setReps = new ArrayList<RepElement>();

        for(int i = 0; i < numSets; i++) {
            EditText repsForm = findViewById(R.id.editRepsForm + i);
            int reps = Integer.parseInt(repsForm.getText().toString());

            EditText weightForm = findViewById(R.id.editWeightForm + i);
            int weight = Integer.parseInt(weightForm.getText().toString());

            Spinner spinnerWeightUnits = (Spinner) findViewById(R.id.unitSpinner + i);
            String weightUnits = spinnerWeightUnits.getSelectedItem().toString();

            RepElement rep = new RepElement(reps, true, weight, weightUnits);
            setReps.add(rep);
        }

        Entry entry = new Entry(type, exerciseName, numSets, usesReps, setReps, null, notes);
        currentWorkout.add(entry);

        currentWorkout.notifyDataSetChanged();
        LinearLayout root = (LinearLayout)findViewById(R.id.root);
        root.removeView(formLayout);
        Button btn = new Button(EntryForm.this);
        btn.setBackgroundColor(ContextCompat.getColor(EntryForm.this, R.color.purple_500));
        btn.setTextColor(Color.WHITE);
        btn.setText("ADD NEW EXERCISE");
        btn.setId(R.id.exerciseButton);
        setOnClick(btn);
        root.addView(btn);
        addButton = btn;
    }

    void setOnClick(Button btn) {
        LinearLayout root = (LinearLayout)findViewById(R.id.root);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                formLayout = new LinearLayout(EntryForm.this);
                formLayout.setOrientation(formLayout.VERTICAL);
                ArrayList<String> spinnerArray = new ArrayList<String>();
                spinnerArray.add("Custom");

                Spinner spinner = new Spinner(EntryForm.this);
                spinner.setPrompt("Choose an exercise...");
                spinner.setId(R.id.spinner_type);
                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(EntryForm.this, android.R.layout.simple_spinner_dropdown_item, spinnerArray);
                spinner.setAdapter(spinnerArrayAdapter);

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                        LayoutInflater inflater = getLayoutInflater();
                        ConstraintLayout layout = (ConstraintLayout) inflater.inflate(R.layout.exercise_custom, root, false);
                        formLayout.addView(layout);

                        for(int i = 0; i < 3; i++) {
                            inflater = getLayoutInflater();
                            layout = (ConstraintLayout) inflater.inflate(R.layout.set_reps, root, false);
                            layout.getViewById(R.id.editRepsForm).setId(R.id.editRepsForm + i);
                            layout.getViewById(R.id.editWeightForm).setId(R.id.editWeightForm + i);
                            layout.getViewById(R.id.unitSpinner).setId(R.id.unitSpinner + i);
                            formLayout.addView(layout);
                        }

                        inflater = getLayoutInflater();
                        layout = (ConstraintLayout) inflater.inflate(R.layout.notes_form, root, false);
                        formLayout.addView(layout);

                        Button addExerciseBtn = new Button(EntryForm.this);
                        addExerciseBtn.setText("ADD EXERCISE");
                        addExerciseBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                removeLayout();
                            }
                        });
                        formLayout.addView(addExerciseBtn);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {
                        // your code here
                    }

                });

                root.removeView(addButton);
                formLayout.addView(spinner);
                root.addView(formLayout);

            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_form);

        addButton = (Button) findViewById(R.id.button);
        setOnClick(addButton);



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