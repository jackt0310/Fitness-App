package com.buffboosterapp.buffbooster;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class WeightLog extends AppCompatActivity {
    private WeightAdapter entryList;
    LineChart mChart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight_log);

        Button btn = (Button) findViewById(R.id.button2);

        btn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                newEntry(v);
            }
        });

        ListView list = (ListView) findViewById(R.id.listArea);
        //instantiate custom adapter
        ArrayList<WeightWeek> entries = load();
        entryList = new WeightAdapter(entries, this);

        list.setAdapter(entryList);

        mChart = findViewById(R.id.lineChart);
        setData();


        LimitLine ll2 = new LimitLine(155f, "Goal");
        ll2.setLineWidth(4f);
        ll2.enableDashedLine(10f, 10f, 0f);
        ll2.setLineColor(Color.GREEN);

        XAxis xAxis = mChart.getXAxis();
        YAxis leftAxis = mChart.getAxisLeft();
        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);

        XAxis.XAxisPosition position = XAxis.XAxisPosition.BOTTOM;
        xAxis.setPosition(position);

        mChart.getDescription().setEnabled(true);
        Description description = new Description();

        description.setText("Week");
        description.setTextSize(15f);

        mChart.setDescription(description);

        leftAxis.addLimitLine(ll2);

        leftAxis.setTextSize(15f);
        xAxis.setTextSize(15f);

        mChart.setExtraBottomOffset(5f);

    }

    private void setData() {
        ArrayList<com.github.mikephil.charting.data.Entry> values = new ArrayList<>();
        values.add(new com.github.mikephil.charting.data.Entry(1, 150));
        values.add(new com.github.mikephil.charting.data.Entry(2, 155));
        values.add(new com.github.mikephil.charting.data.Entry(3, 160));
        LineDataSet set1;
        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            set1 = new LineDataSet(values, "Sample Data");
            mChart.getLegend().setEnabled(false);
            set1.setDrawIcons(false);
            set1.enableDashedLine(10f, 5f, 0f);
            set1.enableDashedHighlightLine(10f, 5f, 0f);
            set1.setColor(Color.DKGRAY);
            set1.setCircleColor(Color.DKGRAY);
            set1.setLineWidth(1f);
            set1.setCircleRadius(3f);
            set1.setDrawCircleHole(false);
            set1.setValueTextSize(15f);
            set1.setDrawFilled(true);
            set1.setFormLineWidth(1f);
            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            set1.setFormSize(15.f);
            set1.setFillColor(Color.DKGRAY);

            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);
            LineData data = new LineData(dataSets);
            mChart.setData(data);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void newEntry(View view) {
        System.out.println(entryList.getCount());
        EditText weightEdit = (EditText) findViewById(R.id.editTextNumber);
        double weight = Double.parseDouble(weightEdit.getText().toString());
        WeightEntry entry = new WeightEntry(weight, LocalDateTime.now());
        entryList.addWeightEntry(entry);
        entryList.notifyDataSetChanged();
    }

    public void save() {
        /*
        try
        {
            File file = new File(getFilesDir() + FILE_NAME);
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
        }*/
    }

    public ArrayList<WeightWeek> load() {
        ArrayList<WeightWeek> loadList = new ArrayList<WeightWeek>();
        /*
        try {
            FileInputStream fos = new FileInputStream(getFilesDir() + FILE_NAME);
            ObjectInputStream oos = new ObjectInputStream(fos);
            loadList = (ArrayList<Workout>) oos.readObject();
            replacePos = (int) oos.readObject();
            oos.close();
            fos.close();
        } catch(Exception ex) {
            ex.printStackTrace();
        }*/
        return loadList;
    }
}