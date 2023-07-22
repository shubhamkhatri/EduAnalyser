package com.shubham.eduanalyser.view;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import com.shubham.eduanalyser.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;


import java.io.File;
import java.util.ArrayList;

import static android.os.Environment.DIRECTORY_DOCUMENTS;

public class GraphActivity extends AppCompatActivity {

    private String name;
    private String intyr, month;
    int initialyr = 2021, yr, monthh, i, j;
    int[] ar = new int[12];
    private BarChart barChart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        File root;

        if (Build.VERSION_CODES.R > Build.VERSION.SDK_INT) {
            root = new File(Environment.getExternalStorageDirectory() + "/" + "EduAnalyser");
        } else {
            root = new File(Environment.getExternalStoragePublicDirectory(DIRECTORY_DOCUMENTS) + "/" + "EduAnalyser");
        }

        ListDir(root);
        barChart = (BarChart) findViewById(R.id.barGraph);

        ArrayList<BarEntry> barEntries = new ArrayList<>();
        for (j = 0; j < 12; j++) {
            barEntries.add(new BarEntry((float) ar[j], j));

        }
        BarDataSet barDataSet = new BarDataSet(barEntries, "No of Files");

        ArrayList<String> theDates = new ArrayList<>();
        theDates.add("JAN");
        theDates.add("FEB");
        theDates.add("MAR");
        theDates.add("APR");
        theDates.add("MAY");
        theDates.add("JUN");
        theDates.add("JUL");
        theDates.add("AUG");
        theDates.add("SPT");
        theDates.add("OCT");
        theDates.add("NOV");
        theDates.add("DEC");

        BarDataSet dataa = new BarDataSet(barEntries, "hello");
        barDataSet.setColor(Color.parseColor("#FFB74D"));

        BarData theData = new BarData(theDates, barDataSet);
        barChart.setData(theData);
        barChart.setTouchEnabled(true);
        barChart.setDragEnabled(true);
        barChart.setScaleEnabled(true);
    }

    void ListDir(File f) {
        File[] files = f.listFiles();
        if (files == null) {
            Toast.makeText(GraphActivity.this, "No files added by EduAnalyser", Toast.LENGTH_LONG).show();
        } else {
            for (File file : files) {

                name = file.getName();
                intyr = name.substring(0, 4);

                month = name.substring(4, 6);
                System.out.println(" month extraction in string= " + month);

                yr = Integer.parseInt(intyr);
                monthh = Integer.parseInt(month);

                i = monthh - 1;
                if (yr == initialyr) {
                    ar[i]++;
                } else {
                    for (j = 0; j < 12; j++)
                        ar[j] = 0;
                }
            }
        }


    }
}