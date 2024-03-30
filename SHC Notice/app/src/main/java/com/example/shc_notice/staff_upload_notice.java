package com.example.shc_notice;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class staff_upload_notice extends AppCompatActivity {

    TextView NoticeName, filename;
    AutoCompleteTextView NoticeSpinner;
    TextInputLayout NoticeTitle;
    ImageView uploadImg;
    Button NoticeUploadbtn;

    String Notice_Type = "";

    TimeZone timeZone = TimeZone.getTimeZone("Asia/Kolkata");
    Calendar calendar = Calendar.getInstance(timeZone);
    Date currentTime = calendar.getTime();

    FirebaseDatabase rootNode;
    StorageReference storageReference;
    DatabaseReference databaseReference;


    ArrayAdapter<String> adapterItems;
   // String[] items = {"Golden Jubilee Notice"};

    //Validation
    private Boolean validateTitle() {
        String val = NoticeTitle.getEditText().getText().toString();
        // String namePattern=".*\\d.*";
        if (val.isEmpty()) {
            NoticeTitle.setError("Title cannot be empty");
            NoticeTitle.requestFocus();
            return false;
        } else {
            NoticeTitle.setError(null);
            NoticeTitle.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateCatagory() {
        String val = NoticeSpinner.getText().toString();

        if (val.isEmpty()) {
            NoticeSpinner.setError("Type cannot be empty");
            NoticeSpinner.requestFocus();
            return false;
        } else {
            NoticeSpinner.setError(null);
            return true;
        }

    }

    private Boolean validatefile() {
        String val = filename.getText().toString();

        if (val.equals("Click to Upload PDF !")) {
            filename.setText("Select Your Notice");
            filename.setTextColor(Color.RED);
            filename.requestFocus();
            return false;
        } else {
            filename.setError(null);
            return true;
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_upload_notice);

        NoticeName = findViewById(R.id.uplodeNotice_name);
        filename = findViewById(R.id.S_fileName);
        NoticeTitle = findViewById(R.id.StaffUploadTitle);

        NoticeSpinner = findViewById(R.id.NoticeTypeSpinner);
        storageReference = FirebaseStorage.getInstance().getReference();

        uploadImg = findViewById(R.id.Staffuploadimage);
        NoticeUploadbtn = findViewById(R.id.StaffUploadbtn);

        uploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPDF();
            }
        });

        NoticeUploadbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!validateTitle() | !validateCatagory() | !validatefile()) {
                    return;

                }
            }
        });

        Intent i = getIntent();
        String[] items = {i.getStringExtra("upload_type")};
        NoticeName.setText("upload your "+i.getStringExtra("upload_type")+" Here");

        adapterItems = new ArrayAdapter<String>(this, R.layout.spinner_item, items);
        NoticeSpinner.setAdapter(adapterItems);
        NoticeSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                Notice_Type = item;
                 Toast.makeText(staff_upload_notice.this, "" + Notice_Type, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void selectPDF() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select pdf files"), 101);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            //--- To change TextView Text into fileName
            Uri pdfUri = data.getData();
            String pdfFileName = getFileNameFromUri(pdfUri);
            filename.setText(pdfFileName);
            //---
            NoticeUploadbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    uploadPDFFileFirebase(data.getData());

                }

            });

        }
    }

    private void uploadPDFFileFirebase(Uri data) {


        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("File is Loading...");
        //Validation again
        if (!validateTitle() | !validateCatagory() | !validatefile()) {
            return;
        }
        progressDialog.show();

        rootNode = FirebaseDatabase.getInstance();
        databaseReference = rootNode.getReference(Notice_Type);
        String file_name = filename.getText().toString();
        String rootFileName = NoticeSpinner.getText().toString();
        final  StorageReference reference = storageReference.child(rootFileName + "/" +NoticeTitle.getEditText().getText().toString()+ ".pdf");//System.currentTimeMillis()
        reference.putFile(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                // Get the current date and time as a formatted string
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm a", new Locale("en", "IN"));
                String dateTime = dateFormat.format(currentTime);

                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete()) ;
                Uri uri = uriTask.getResult();
                String title = NoticeTitle.getEditText().getText().toString();

                adminUploadPDF uploadPDF = new adminUploadPDF(title, uri.toString(), dateTime);
                databaseReference.child(databaseReference.push().getKey()).setValue(uploadPDF);//databaseReference.push().getKey()
                Toast.makeText(staff_upload_notice.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

                //clear the values
                NoticeTitle.getEditText().setText("");
                filename.setText("Click to Upload PDF !");
                NoticeSpinner.setText("");
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                progressDialog.setMessage("File Uploaded.." + (int) progress + "%");

            }
        });
    }

    private String getFileNameFromUri(Uri uri) {
        String fileName = null;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (nameIndex != -1) {
                        fileName = cursor.getString(nameIndex);
                    }
                }
            }
        }
        if (fileName == null) {
            fileName = uri.getLastPathSegment();
        }
        return fileName;
    }


}