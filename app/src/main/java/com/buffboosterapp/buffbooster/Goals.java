package com.buffboosterapp.buffbooster;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class Goals extends AppCompatActivity {
    private static final String FILE_NAME = "goals.txt";
    double goalWeight = -1;

    public void save() {
        try
        {
            File file = new File(getFilesDir() + FILE_NAME);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeDouble(goalWeight);
            oos.close();
            fos.close();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public void load() {
        try {
            FileInputStream fos = new FileInputStream(getFilesDir() + FILE_NAME);
            ObjectInputStream oos = new ObjectInputStream(fos);
            goalWeight = oos.readDouble();
            oos.close();
            fos.close();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goals);
        load();

        System.out.println(goalWeight);
        EditText editGoal = (EditText) findViewById(R.id.editGoal);
        if(goalWeight >= 0) {
            editGoal.setText(goalWeight + "");
        }

        editGoal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().equals("")) {
                    if(Double.parseDouble(s.toString()) < 0) {
                        editGoal.setText(0 + "");
                    } else if(Double.parseDouble(s.toString()) > 1000) {
                        editGoal.setText(1000 + "");
                    }
                    goalWeight = Double.parseDouble(s.toString());
                    save();
                    System.out.println("save");
                }
            }
        });
    }
}
