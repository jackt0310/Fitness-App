package com.buffboosterapp.buffbooster;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

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

    boolean usesReps = true;

    boolean spinnerFirst = true;

    void removeLayout() {
        Spinner mySpinner = (Spinner) findViewById(R.id.spinner_type);
        String type = mySpinner.getSelectedItem().toString();

        EditText exerciseNameForm = findViewById(R.id.exerciseNameForm);
        String exerciseName =  exerciseNameForm.getText().toString();

        EditText numSetsForm = findViewById(R.id.numSetsForm);
        int numSets = Integer.parseInt(numSetsForm.getText().toString());

        Spinner spinnerTimeReps = (Spinner) findViewById(R.id.spinnerTimeReps);
        String timeReps = spinnerTimeReps.getSelectedItem().toString();

        EditText notesForm = findViewById(R.id.notesForm);
        String notes =  notesForm.getText().toString();

        if(usesReps) {
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
            Entry entry = new Entry(type, exerciseName, numSets, true, setReps, null, notes);
            currentWorkout.add(entry);
        } else {
            ArrayList<TimeElement> setTime = new ArrayList<TimeElement>();

            for(int i = 0; i < numSets; i++) {
                EditText repsForm = findViewById(R.id.editRepsForm + i);
                int time = Integer.parseInt(repsForm.getText().toString());

                EditText weightForm = findViewById(R.id.editWeightForm + i);
                int weight = Integer.parseInt(weightForm.getText().toString());

                Spinner spinnerWeightUnits = (Spinner) findViewById(R.id.unitSpinner + i);
                String weightUnits = spinnerWeightUnits.getSelectedItem().toString();

                Spinner spinnerTimeUnits = (Spinner) findViewById(R.id.unitSpinner2 + i);
                String timeUnits = spinnerTimeUnits.getSelectedItem().toString();


                TimeElement timeEl = new TimeElement(time + "", timeUnits, true, weight, weightUnits);
                setTime.add(timeEl);
            }
            Entry entry = new Entry(type, exerciseName, numSets, false, null, setTime, notes);
            currentWorkout.add(entry);
        }




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

    void removeLayoutEdit(int position) {
        Spinner mySpinner = (Spinner) findViewById(R.id.spinner_type);
        String type = mySpinner.getSelectedItem().toString();

        EditText exerciseNameForm = findViewById(R.id.exerciseNameForm);
        String exerciseName =  exerciseNameForm.getText().toString();

        EditText numSetsForm = findViewById(R.id.numSetsForm);
        int numSets = Integer.parseInt(numSetsForm.getText().toString());

        Spinner spinnerTimeReps = (Spinner) findViewById(R.id.spinnerTimeReps);
        String timeReps = spinnerTimeReps.getSelectedItem().toString();

        EditText notesForm = findViewById(R.id.notesForm);
        String notes =  notesForm.getText().toString();

        if(usesReps) {
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
            Entry entry = new Entry(type, exerciseName, numSets, true, setReps, null, notes);
            currentWorkout.replace(position, entry);
        } else {
            ArrayList<TimeElement> setTime = new ArrayList<TimeElement>();

            for(int i = 0; i < numSets; i++) {
                EditText repsForm = findViewById(R.id.editRepsForm + i);
                int time = Integer.parseInt(repsForm.getText().toString());

                EditText weightForm = findViewById(R.id.editWeightForm + i);
                int weight = Integer.parseInt(weightForm.getText().toString());

                Spinner spinnerWeightUnits = (Spinner) findViewById(R.id.unitSpinner + i);
                String weightUnits = spinnerWeightUnits.getSelectedItem().toString();

                Spinner spinnerTimeUnits = (Spinner) findViewById(R.id.unitSpinner2 + i);
                String timeUnits = spinnerTimeUnits.getSelectedItem().toString();


                TimeElement timeEl = new TimeElement(time + "", timeUnits, true, weight, weightUnits);
                setTime.add(timeEl);
            }
            Entry entry = new Entry(type, exerciseName, numSets, false, null, setTime, notes);
            currentWorkout.replace(position, entry);
        }

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

                        EditText numSetsForm = (EditText) findViewById(R.id.numSetsForm);

                        numSetsForm.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {

                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                                if(!s.toString().equals("")) {
                                    if(Integer.parseInt(s.toString()) < 1) {
                                        numSetsForm.setText(1 + "");
                                    } else if(Integer.parseInt(s.toString()) > 10) {
                                        numSetsForm.setText(10 + "");
                                    }
                                    generateSetsView(Integer.parseInt(numSetsForm.getText().toString()));
                                }
                            }
                        });

                        Spinner spinnerTimeReps = (Spinner) findViewById(R.id.spinnerTimeReps);
                        spinnerTimeReps.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                            @Override
                            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                                if(spinnerTimeReps.getSelectedItem().toString().equals("Reps")) {
                                    usesReps = true;
                                } else {
                                    usesReps = false;
                                }
                                System.out.println("usesReps: " + usesReps);
                                EditText numSetsForm = (EditText) findViewById(R.id.numSetsForm);

                                if(!numSetsForm.getText().toString().equals("")) {
                                    generateSetsView(Integer.parseInt(numSetsForm.getText().toString()));
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parentView) {
                                // your code here
                            }
                        });
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

    void generateSetsView(int numSets) {
        System.out.println("generateSetsView");
        LinearLayout root = (LinearLayout)findViewById(R.id.root);
        if(findViewById(R.id.setsLayout) != null) {
            formLayout.removeView(findViewById(R.id.setsLayout));
        }
        LinearLayout setsLayout = new LinearLayout(EntryForm.this);
        setsLayout.setOrientation(LinearLayout.VERTICAL);
        setsLayout.setId(R.id.setsLayout);
        for(int i = 0; i < numSets; i++) {
            LayoutInflater inflater = getLayoutInflater();
            inflater = getLayoutInflater();
            if(usesReps) {
                ConstraintLayout layout = (ConstraintLayout) inflater.inflate(R.layout.set_reps, root, false);
                layout.getViewById(R.id.editRepsForm).setId(R.id.editRepsForm + i);
                layout.getViewById(R.id.editWeightForm).setId(R.id.editWeightForm + i);
                layout.getViewById(R.id.unitSpinner).setId(R.id.unitSpinner + i);
                setsLayout.addView(layout);
            } else {
                ConstraintLayout layout = (ConstraintLayout) inflater.inflate(R.layout.time_reps, root, false);
                layout.getViewById(R.id.editRepsForm).setId(R.id.editRepsForm + i);
                layout.getViewById(R.id.editWeightForm).setId(R.id.editWeightForm + i);
                layout.getViewById(R.id.unitSpinner).setId(R.id.unitSpinner + i);
                layout.getViewById(R.id.unitSpinner2).setId(R.id.unitSpinner2 + i);
                setsLayout.addView(layout);
            }
        }

        LayoutInflater inflater = getLayoutInflater();
        ConstraintLayout layout = (ConstraintLayout) inflater.inflate(R.layout.notes_form, root, false);
        setsLayout.addView(layout);

        Button addExerciseBtn = new Button(EntryForm.this);
        addExerciseBtn.setText("ADD EXERCISE");
        addExerciseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeLayout();
            }
        });
        setsLayout.addView(addExerciseBtn);

        formLayout.addView(setsLayout);
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
        currentWorkout = new EntryAdapter(workout, this);

        if(i.getParcelableExtra("editList") != null) {
            System.out.println("FJFIDJIJFISJ");
            workout = i.getParcelableExtra("editList");
            currentWorkout = new EntryAdapter(workout, this);
            i.removeExtra("editList");
            save();
        }


        list.setAdapter(currentWorkout);

/*      if(i.getParcelableExtra("entry") != null) {
            Entry entry = (Entry) i.getParcelableExtra("entry");
            if(currentWorkout.replacePos != -1) {
                currentWorkout.add(currentWorkout.replacePos, entry);
                currentWorkout.replacePos = -1;
            } else {
                currentWorkout.add(entry);
            }
            currentWorkout.notifyDataSetChanged();
            save();
        }*/

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
                save();
                startActivity(intent);
            }
        });

        TextView date = (TextView) findViewById(R.id.textView3);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDateTime now = LocalDateTime.now();
        date.setText(dtf.format(now));

    }

    public void save() {
        /*        try
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
        currentWorkout.workout.date = date.getText().toString();
        intent.putExtra("finalWorkout", (Parcelable) currentWorkout.workout);
        startActivity(intent);
    }

    public boolean inputIsValid() {
        return currentWorkout.getCount() > 0;
    }

    public void setEditView(int position) {
        LinearLayout root = (LinearLayout)findViewById(R.id.root);
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

                EditText numSetsForm = (EditText) findViewById(R.id.numSetsForm);
                EditText exerciseNameForm = (EditText) findViewById(R.id.exerciseNameForm);
                exerciseNameForm.setText(currentWorkout.workout.get(position).exerciseName);

                numSetsForm.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if(!s.toString().equals("")) {
                            if(Integer.parseInt(s.toString()) < 1) {
                                numSetsForm.setText(1 + "");
                            } else if(Integer.parseInt(s.toString()) > 10) {
                                numSetsForm.setText(10 + "");
                            }
                            generateSetsView(Integer.parseInt(numSetsForm.getText().toString()));
                        }
                    }
                });

                Spinner spinnerTimeReps = (Spinner) findViewById(R.id.spinnerTimeReps);

                spinnerTimeReps.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                        if(spinnerTimeReps.getSelectedItem().toString().equals("Reps")) {
                            usesReps = true;
                        } else {
                            usesReps = false;
                        }

                        System.out.println("usesReps: " + usesReps);

                        if(spinnerFirst) {
                            spinnerFirst = false;
                            return;
                        }
                        EditText numSetsForm = (EditText) findViewById(R.id.numSetsForm);

                        if(!numSetsForm.getText().toString().equals("")) {
                            generateSetsView(Integer.parseInt(numSetsForm.getText().toString()));
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {
                        // your code here
                    }
                });

                if(currentWorkout.workout.get(position).usesReps) {
                    spinnerTimeReps.setSelection(0);
                } else {
                    spinnerTimeReps.setSelection(1);
                }

                numSetsForm.setText(currentWorkout.workout.get(position).numSets + "");
                generateSetsViewEdit(Integer.parseInt(numSetsForm.getText().toString()), position);
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

    void generateSetsViewEdit(int numSets, int position) {
        LinearLayout root = (LinearLayout)findViewById(R.id.root);
        if(findViewById(R.id.setsLayout) != null) {
            formLayout.removeView(findViewById(R.id.setsLayout));
        }
        LinearLayout setsLayout = new LinearLayout(EntryForm.this);
        setsLayout.setOrientation(LinearLayout.VERTICAL);
        setsLayout.setId(R.id.setsLayout);
        for(int i = 0; i < numSets; i++) {
            LayoutInflater inflater = getLayoutInflater();
            inflater = getLayoutInflater();
            if(currentWorkout.workout.get(position).usesReps) {
                ConstraintLayout layout = (ConstraintLayout) inflater.inflate(R.layout.set_reps, root, false);
                layout.getViewById(R.id.editRepsForm).setId(R.id.editRepsForm + i);
                layout.getViewById(R.id.editWeightForm).setId(R.id.editWeightForm + i);
                layout.getViewById(R.id.unitSpinner).setId(R.id.unitSpinner + i);

                System.out.println(currentWorkout.workout.get(position).setReps.get(i).reps + "");
                ((EditText) layout.getViewById(R.id.editRepsForm)).setText(currentWorkout.workout.get(position).setReps.get(i).reps + "");
                ((EditText) layout.getViewById(R.id.editWeightForm)).setText(currentWorkout.workout.get(position).setReps.get(i).weight + "");
                //layout.getViewById(R.id.unitSpinner).setId(R.id.unitSpinner + i);
                setsLayout.addView(layout);
            } else {
                ConstraintLayout layout = (ConstraintLayout) inflater.inflate(R.layout.time_reps, root, false);
                layout.getViewById(R.id.editRepsForm).setId(R.id.editRepsForm + i);
                layout.getViewById(R.id.editWeightForm).setId(R.id.editWeightForm + i);
                layout.getViewById(R.id.unitSpinner).setId(R.id.unitSpinner + i);
                layout.getViewById(R.id.unitSpinner2).setId(R.id.unitSpinner2 + i);

                ((EditText) layout.getViewById(R.id.editRepsForm)).setText(currentWorkout.workout.get(position).setTimes.get(i).time + "");
                ((EditText) layout.getViewById(R.id.editWeightForm)).setText(currentWorkout.workout.get(position).setTimes.get(i).weight + "");
                //layout.getViewById(R.id.unitSpinner).setId(R.id.unitSpinner + i);
                setsLayout.addView(layout);
            }
        }

        LayoutInflater inflater = getLayoutInflater();
        ConstraintLayout layout = (ConstraintLayout) inflater.inflate(R.layout.notes_form, root, false);
        ((EditText) layout.getViewById(R.id.notesForm)).setText(currentWorkout.workout.get(position).notes);

        setsLayout.addView(layout);

        Button addExerciseBtn = new Button(EntryForm.this);
        addExerciseBtn.setText("ADD EXERCISE");
        addExerciseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeLayoutEdit(position);
            }
        });
        setsLayout.addView(addExerciseBtn);

        formLayout.addView(setsLayout);
    }
}