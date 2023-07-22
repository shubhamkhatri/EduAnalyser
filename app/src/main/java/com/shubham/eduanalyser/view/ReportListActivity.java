package com.shubham.eduanalyser.view;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.shubham.eduanalyser.DocumentAdaptor;
import com.shubham.eduanalyser.R;
import com.shubham.eduanalyser.model.word;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.os.Environment.DIRECTORY_DOCUMENTS;

public class ReportListActivity extends AppCompatActivity {

    public List<String> listt = new ArrayList<>();
    private String check;
    private String categorySet;
    private String getCategorySet;
    private FloatingActionButton add;
    private FloatingActionButton graph;
    final ArrayList<word> fileList = new ArrayList<word>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_list);

        File root;

        if (Build.VERSION_CODES.R > Build.VERSION.SDK_INT) {
            root = new File(Environment.getExternalStorageDirectory() + "/" + "EduAnalyser");
        } else {
            root = new File(Environment.getExternalStoragePublicDirectory(DIRECTORY_DOCUMENTS) + "/" + "EduAnalyser");
        }


        ListDir(root);
        // defaultOcr = (TextView) v.findViewById(R.id.no_reports);
        graph = findViewById(R.id.graph_view);
        graph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Intent i = new Intent(ReportListActivity.this, GraphActivity.class);
                startActivity(i);
            }
        });

        add = findViewById(R.id.add_ocr);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ReportListActivity.this, ReportCreateActivity.class);
                startActivity(i);
            }
        });

    }


    void ListDir(File f) {
        File[] files = f.listFiles();
        fileList.clear();
        if (files != null) {
            for (File file : files) {
                listt.add(file.getPath());

                fileList.add(new word("Category", file.getName()));
            }
            if (fileList.isEmpty()) {
                //defaultOcr.setVisibility(View.VISIBLE);
            } else {
                //defaultOcr.setVisibility(View.GONE);
                final DocumentAdaptor directoryList = new DocumentAdaptor(ReportListActivity.this, fileList, R.color.purple_500);

                ListView listView = (ListView) findViewById(R.id.list);
                listView.setAdapter(directoryList);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                    @Override
                                                    public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
                                                        Intent appInfo = new Intent(ReportListActivity.this, pdfViewer.class);
                                                        appInfo.putExtra("name_path", listt.get(position));
                                                        startActivity(appInfo);
                                                    }
                                                }

                );
                listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ReportListActivity.this);

                        builder.setMessage("Do you really want to delete this PDF?").setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(final DialogInterface dialogInterface, int i) {
                                        File file = new File(listt.get(position));
                                        boolean deleted = file.delete();
                                        if (deleted) {
                                            directoryList.remove(directoryList.getItem(position));
                                            directoryList.notifyDataSetChanged();
                                        }
                                    }
                                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });

                        android.app.AlertDialog alert = builder.create();
                        alert.show();
                        return true;
                    }
                });
            }
        }
    }
}