
package com.shubham.eduanalyser.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;

import com.shubham.eduanalyser.R;

import java.io.File;
import java.io.FileOutputStream;

import com.itextpdf.text.Document;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import static android.os.Environment.DIRECTORY_DOCUMENTS;


public class ReportCreateActivity extends AppCompatActivity {

    private EditText mResltEt, name;
    private TextView noWords, creativity, criticalThinking;
    private SeekBar creat, cThinking;
    private LinearLayout analyses;
    private Button mSaveBtn;
    private String analysedText;


    private static final int STORAGE_CODE = 600;
    String storagePermission[];


    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_create);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setSubtitle("Click add button to Speak ->");

        mResltEt = findViewById(R.id.resultEt);
        noWords = (TextView) findViewById(R.id.no_of_words_tv);
        analyses = (LinearLayout) findViewById(R.id.analysis_ll);
        creat = (SeekBar) findViewById(R.id.creativity_seekBar);
        cThinking = (SeekBar) findViewById(R.id.critical_thinking_seekBar);
        creativity = (TextView) findViewById(R.id.creativity_tv);
        criticalThinking = (TextView) findViewById(R.id.critical_thinking_tv);
        name=(EditText)findViewById(R.id.name_editText);
        mSaveBtn = findViewById(R.id.saveBtn);

        mSaveBtn.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                //we need to handle runtime permission for devices with marshmallow and above
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                    //system OS >= Marshmallow(6.0), check if permission is enabled or not
                    if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                            PackageManager.PERMISSION_DENIED) {
                        //permission was not granted, request it
                        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permissions, STORAGE_CODE);
                    } else {
                        //permission already granted, call save pdf method
                        if (mResltEt.getText().toString().trim().isEmpty()) {
                            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ReportCreateActivity.this);

                            builder.setMessage("There is no text to save in PDF file, Do you still want to create file?").setCancelable(false)
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(final DialogInterface dialogInterface, int i) {
                                            checkFolder();
                                        }
                                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });

                            android.app.AlertDialog alert = builder.create();
                            alert.show();
                        }
                        //system OS < Marshmallow, call save pdf method
                        else
                            checkFolder();
                    }
                } else {
                    if (mResltEt.getText().toString().trim().isEmpty()) {
                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ReportCreateActivity.this);

                        builder.setMessage("There is no text to save in PDF file, Do you still want to create file?").setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(final DialogInterface dialogInterface, int i) {
                                        checkFolder();
                                    }
                                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });

                        android.app.AlertDialog alert = builder.create();
                        alert.show();
                    }
                    //system OS < Marshmallow, call save pdf method
                    else
                        checkFolder();
                }
            }
        });

        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.menu_analyse, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@androidx.annotation.NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.addText) {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                    "Hi Speak Something");
            try {

                startActivityForResult(intent, 1001);
            } catch (ActivityNotFoundException e) {
                Log.e("TestActivityError", e.getMessage());
            }
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @androidx.annotation.NonNull String[] permissions, @androidx.annotation.NonNull int[] grantResults) {
        switch (requestCode) {


            case STORAGE_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permission was granted from popup, call savepdf method
                    checkFolder();
                } else {
                    //permission was denied from popup, show error message
                    Toast.makeText(this, "Permission denied...!", Toast.LENGTH_SHORT).show();
                }

            }
            break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 1001:
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    analysedText = result.get(0);
                    setResultData();
                }
                break;
        }
    }

    private void setResultData() {
        analyses.setVisibility(View.VISIBLE);
        mResltEt.setText(analysedText);
        noWords.setText("No of Words: " + countWords());

        creat.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                creativity.setText("Creativity: " + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        int progress = creat.getProgress();
        creativity.setText("Creativity: " + progress);

        cThinking.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                criticalThinking.setText("Critical Thinking: " + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        int progress2 = creat.getProgress();
        criticalThinking.setText("Critical Thinking: " + progress2);

    }

    private int countWords() {
        String ar[] = analysedText.split(" ");
        return ar.length;
    }


    private void savePdf() {

        //create object of Document class
        Document mDoc = new Document();
        //pdf file name
        String mFileName = new SimpleDateFormat("yyyyMMdd_HHmm",
                java.util.Locale.getDefault()).format(System.currentTimeMillis());
        if(!name.getText().toString().trim().isEmpty())
            mFileName=mFileName.concat("_"+name.getText().toString().trim());
        //pdf file path
        String path;

        if (Build.VERSION_CODES.R > Build.VERSION.SDK_INT) {
            path = Environment.getExternalStorageDirectory() + "/" + "EduAnalyser";
        } else {
            path = Environment.getExternalStoragePublicDirectory(DIRECTORY_DOCUMENTS) + "/" + "EduAnalyser";
        }
        String mFilePath = path + "/" + mFileName + ".pdf";

        try {
            //create instance of PdfWriter class
            PdfWriter.getInstance(mDoc, new FileOutputStream(mFilePath));
            //open the document for writing
            mDoc.open();

            Rectangle rect = new Rectangle(36, 108);
            rect.enableBorderSide(1);
            rect.enableBorderSide(2);
            rect.enableBorderSide(4);
            rect.enableBorderSide(8);
            rect.setBorder(2);
            rect.setBorderColor(BaseColor.BLACK);
            rect.setBorderWidth(3);
            rect.setUseVariableBorders(true);
            mDoc.add(rect);
            //get text from EditText i.e. mTextEt

            String mText = mResltEt.getText().toString().trim();
            mText = mText.concat("\n\n" + noWords.getText().toString().trim());
            mText = mText.concat("\n\n" + creativity.getText().toString().trim());
            mText = mText.concat("\n\n" + criticalThinking.getText().toString().trim());
            if(!name.getText().toString().trim().isEmpty())
            {
                String temp="Name : ";
                temp=temp.concat(name.getText().toString().trim()+"\n\n");
                mText=temp.concat(mText);
            }

            //add author of the document (optional)
            mDoc.addAuthor("shubham");

            //add paragraph to the document
            mDoc.add(new Paragraph(mText));

            //close the document
            mDoc.close();
            //show message that file is saved, it will show file name and file path too
            Toast.makeText(this, mFileName + ".pdf\nis saved to\n" + mFilePath, Toast.LENGTH_SHORT).show();
            Intent activity = new Intent(ReportCreateActivity.this, ReportListActivity.class);
            activity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(activity);
            finish();
        } catch (Exception e) {
            //if any thing goes wrong causing exception, get and show exception message
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    public void checkFolder() {
        File file;

        if (Build.VERSION_CODES.R > Build.VERSION.SDK_INT) {
            file = new File(Environment.getExternalStorageDirectory() + "/" + "EduAnalyser");
        } else {
            file = new File(Environment.getExternalStoragePublicDirectory(DIRECTORY_DOCUMENTS) + "/" + "EduAnalyser");
        }
        if (file.exists())
            savePdf();
        else {
            if (file.mkdirs())
                savePdf();
            else
                Toast.makeText(ReportCreateActivity.this,"Failed to create folder",Toast.LENGTH_SHORT).show();

        }
    }


}
