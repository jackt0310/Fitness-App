package com.buffboosterapp.buffbooster;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class About extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        TextView versionText = (TextView) findViewById(R.id.versionText);
        versionText.setText("Version number: " + BuildConfig.VERSION_NAME);
    }
}
