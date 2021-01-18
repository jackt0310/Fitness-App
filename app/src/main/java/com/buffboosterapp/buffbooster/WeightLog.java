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
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

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
    double goalWeight = -1;

    private static final String FILE_NAME = "weightLog.txt";


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

        setData();

    }

    private void setData() {
        loadGoal();

        mChart = findViewById(R.id.lineChart);
        YAxis leftAxis = mChart.getAxisLeft();

        if(goalWeight >= 0) {
            LimitLine ll2 = new LimitLine((float) goalWeight, "Goal");
            ll2.setTextSize(15f);
            ll2.setLineWidth(4f);
            ll2.enableDashedLine(10f, 10f, 0f);
            ll2.setLineColor(Color.GREEN);
            leftAxis.addLimitLine(ll2);
        }


        XAxis xAxis = mChart.getXAxis();

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);

        XAxis.XAxisPosition position = XAxis.XAxisPosition.BOTTOM;
        xAxis.setPosition(position);

        mChart.getDescription().setEnabled(true);
        Description description = new Description();

        description.setText("Week");
        description.setTextSize(15f);

        mChart.setDescription(description);


        leftAxis.setTextSize(15f);
        xAxis.setTextSize(15f);

        mChart.setExtraBottomOffset(5f);
        xAxis.setGranularity(1f);

        leftAxis.setGranularity(1f);
        //mChart.getXAxis().setAxisMaximum(1f);
        mChart.invalidate();

        mChart.getAxisLeft().setDrawGridLines(false);
        mChart.getXAxis().setDrawGridLines(false);
        ArrayList<com.github.mikephil.charting.data.Entry> values = new ArrayList<>();
        float min = 999999f;
        float max = 0f;

        for(int i = 0; i < entryList.getCount(); i++) {
            values.add(new com.github.mikephil.charting.data.Entry(i + 1, (float) entryList.weightWeeks.get(i).getAvgWeight()));
            if((float) entryList.weightWeeks.get(i).getAvgWeight() < min) {
                min = (float) entryList.weightWeeks.get(i).getAvgWeight();
            } else if((float) entryList.weightWeeks.get(i).getAvgWeight() > max) {
                max = (float) entryList.weightWeeks.get(i).getAvgWeight();
            }
        }

        System.out.println(goalWeight);
        if(max < goalWeight) {
            max = (float) goalWeight;
        }


        leftAxis = mChart.getAxisLeft();
        leftAxis.setAxisMinimum(min - 15);
        leftAxis.setAxisMaximum(max + 15);
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
            mChart.getXAxis().setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value) {
                    if (value>0){
                        return Math.round(value) + "";
                    }else{
                        return "";
                    }
                }
            });

            leftAxis.setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value) {
                    return Math.round(value) + "";
                }
            });

            data.setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value) {
                    return String.format("%.2f", value) + " lb";
                }
            });
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
        setData();
        save();
    }

    public void save() {
        try
        {
            File file = new File(getFilesDir() + FILE_NAME);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(entryList.getList());
            oos.close();
            fos.close();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public ArrayList<WeightWeek> load() {
        ArrayList<WeightWeek> loadList = new ArrayList<WeightWeek>();
        try {
            FileInputStream fos = new FileInputStream(getFilesDir() + FILE_NAME);
            ObjectInputStream oos = new ObjectInputStream(fos);
            loadList = (ArrayList<WeightWeek>) oos.readObject();
            oos.close();
            fos.close();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        return loadList;
    }


    public void loadGoal() {
        try {
            FileInputStream fos = new FileInputStream(getFilesDir() + "goals.txt");
            ObjectInputStream oos = new ObjectInputStream(fos);
            goalWeight = oos.readDouble();
            oos.close();
            fos.close();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
}