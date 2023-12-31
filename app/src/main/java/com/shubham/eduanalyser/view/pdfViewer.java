package com.shubham.eduanalyser.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.text.TextUtils;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.util.FitPolicy;
import com.shubham.eduanalyser.R;

import java.io.File;

public class pdfViewer extends AppCompatActivity {
    private String changeText;

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_viewer);


        Intent appInfo = getIntent();
        if (null != appInfo &&
                !TextUtils.isEmpty(appInfo.getStringExtra("name_path"))) {


            changeText = appInfo.getStringExtra("name_path");
            // Extracting sent text from intent


        }


        PDFView pdfView = findViewById(R.id.pdfView);
        pdfView.fromFile(new File(changeText))
                .enableSwipe(true) // allows to block changing pages using swipe
                .swipeHorizontal(true)
                .enableDoubletap(true)
                .defaultPage(0)
                .enableAnnotationRendering(false) // render annotations (such as comments, colors or forms)
                .password(null)
                .scrollHandle(null)
                .enableAntialiasing(true) // improve rendering a little bit on low-res screens
                // spacing between pages in dp. To define spacing color, set view background
                .spacing(0)
                .pageFitPolicy(FitPolicy.WIDTH)
                .load();


    }
}