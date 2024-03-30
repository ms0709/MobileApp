package com.example.shc_notice;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

public class view_pdf extends AppCompatActivity {
  //--  private PDFView pdfView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pdf);





      //--      pdfView = findViewById(R.id.pdfView);

            // Get the PDF file URL from the intent
            String pdfUrl = getIntent().getStringExtra("pdfUrl");

            // Load and display the PDF file
            displayPDF(pdfUrl);
        }

        private void displayPDF(String pdfUrl) {
            File pdfFile = new File(pdfUrl);

            if (pdfFile.exists()) {
           //--     pdfView.fromFile(pdfFile)
//                        .enableSwipe(true)
//                        .swipeHorizontal(false)
//                        .enableDoubletap(true)
//                        .defaultPage(0)
//                        .pageFitPolicy(FitPolicy.BOTH)
//                        .load();
            } else {
                // Handle the case where the PDF file doesn't exist
            }
        }

    }
