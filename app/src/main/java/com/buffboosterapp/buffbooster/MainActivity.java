package com.buffboosterapp.buffbooster;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button workout = (Button) findViewById(R.id.buttonWorkout);

        workout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WorkoutList.class);
                startActivity(intent);
            }
        });

        Button weightLog = (Button) findViewById(R.id.buttonWeightLog);

        weightLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WeightLog.class);
                startActivity(intent);
            }
        });

        Button progressPics = (Button) findViewById(R.id.buttonProgressPics);

        progressPics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProgressPictures.class);
                startActivity(intent);
            }
        });

        Button goals = (Button) findViewById(R.id.buttonGoals);

        goals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Goals.class);
                startActivity(intent);
            }
        });

        Button exerciseInfo = (Button) findViewById(R.id.buttonExerciseInfo);

        exerciseInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ExerciseInfo.class);
                startActivity(intent);
            }
        });

        Button recRoutines = (Button) findViewById(R.id.buttonRecRoutines);

        recRoutines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RecRoutines.class);
                startActivity(intent);
            }
        });

        Button community = (Button) findViewById(R.id.buttonCommunity);

        community.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Community.class);
                startActivity(intent);
            }
        });

        Button about = (Button) findViewById(R.id.buttonAbout);

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, About.class);
                startActivity(intent);
            }
        });
    }
}