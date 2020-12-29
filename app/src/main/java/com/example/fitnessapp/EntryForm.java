package com.example.fitnessapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class EntryForm extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_form);

        Button btn = (Button) findViewById(R.id.button2);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit(v);
            }
        });
    }

    public void submit(View view) {
        String editExercise = ((TextView) findViewById(R.id.editExercise)).getText().toString();
        String editType = ((TextView) findViewById(R.id.editType)).getText().toString();
        String editDate = ((TextView) findViewById(R.id.editDate)).getText().toString();
        String editSets = ((TextView) findViewById(R.id.editSets)).getText().toString();
        String editReps = ((TextView) findViewById(R.id.editReps)).getText().toString();
        String editWeight = ((TextView) findViewById(R.id.editWeight)).getText().toString();

        Entry entry = new Entry(editExercise, editType, editDate, editSets, editReps, editWeight);

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("entry", (Parcelable) entry);
        startActivity(intent);
    }
}