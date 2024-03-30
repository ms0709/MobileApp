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


public class admin_upload_notice extends AppCompatActivity {
    String[] items = {"Principal Notice", "COE Notice", "Golden Jubilee Notice", "Silver Jubilee Notice", "John Med Notice",
    "MCA Notice","MBA Notice","Msc CS Notice","Msc Mathematics Notice","Msc MicroBiology Notice"};
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterItems;
    TextInputLayout notice_title;
    AutoCompleteTextView selectCategory;
    Button upload_btn;
    TextView file_name;
    ImageView upload_image;
    String CatSelectItem = "";

    TimeZone timeZone = TimeZone.getTimeZone("Asia/Kolkata");
    Calendar calendar = Calendar.getInstance(timeZone);
    Date currentTime = calendar.getTime();


    FirebaseDatabase rootNode;
    StorageReference storageReference;
    DatabaseReference databaseReference;

    //Validation
    private Boolean validateTitle() {
        String val = notice_title.getEditText().getText().toString();
        // String namePattern=".*\\d.*";
        if (val.isEmpty()) {
            notice_title.setError("Title cannot be empty");
            notice_title.requestFocus();
            return false;
        } else {
            notice_title.setError(null);
            notice_title.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateCatagory() {
        String val = selectCategory.getText().toString();

        if (val.isEmpty()) {
            selectCategory.setError("Category cannot be empty");
            selectCategory.requestFocus();
            return false;
        } else {
            selectCategory.setError(null);
            return true;
        }

    }

    private Boolean validatefile() {
        String val = file_name.getText().toString();

        if (val.equals("Click to Upload PDF !")) {
            file_name.setText("Select Your Notice");
            file_name.setTextColor(Color.RED);
            file_name.requestFocus();
            return false;
        } else {
            file_name.setError(null);
            return true;
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_upload_notice);

        file_name = findViewById(R.id.fileName);
        notice_title = findViewById(R.id.noticeTitle);

        selectCategory = findViewById(R.id.spinner_Options);
        upload_btn = findViewById(R.id.uploadbtn);
        upload_image = findViewById(R.id.uploadimage);

        storageReference = FirebaseStorage.getInstance().getReference();


        upload_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPDF();
            }
        });

        upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!validateTitle() | !validateCatagory() | !validatefile()) {
                    return;

                }
            }
        });


        autoCompleteTextView = findViewById(R.id.spinner_Options);
        adapterItems = new ArrayAdapter<String>(this, R.layout.spinner_item, items);
        autoCompleteTextView.setAdapter(adapterItems);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                CatSelectItem = item;
                // Toast.makeText(admin_upload_notice.this, "" + CatSelectItem, Toast.LENGTH_SHORT).show();
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
            file_name.setText(pdfFileName);
            //---
            upload_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    uploadPDFFileFirebase(data.getData());

                }

            });

        }
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


    private void uploadPDFFileFirebase(Uri data) {


        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("File is Loading...");
        //Validation again
        if (!validateTitle() | !validateCatagory() | !validatefile()) {
            return;
        }
        progressDialog.show();

        rootNode = FirebaseDatabase.getInstance();
        databaseReference = rootNode.getReference(CatSelectItem);
        String filename = file_name.getText().toString();
        String rootFileName = selectCategory.getText().toString();
        final StorageReference reference = storageReference.child(rootFileName + "/" + notice_title.getEditText().getText().toString() + ".pdf");//System.currentTimeMillis()
        reference.putFile(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                // Get the current date and time as a formatted string
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm a", new Locale("en", "IN"));
                String dateTime = dateFormat.format(currentTime);

                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete()) ;
                Uri uri = uriTask.getResult();
                String title = notice_title.getEditText().getText().toString();

                adminUploadPDF uploadPDF = new adminUploadPDF(title, uri.toString(), dateTime);
                databaseReference.child(databaseReference.push().getKey()).setValue(uploadPDF);//databaseReference.push().getKey()
                Toast.makeText(admin_upload_notice.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

                //clear the values
                notice_title.getEditText().setText("");
                file_name.setText("Click to Upload PDF !");
                autoCompleteTextView.setText("");


            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                progressDialog.setMessage("File Uploaded.." + (int) progress + "%");

            }
        });
    }

    @Override
    public void onBackPressed() {
        String Acctype = "user login";
        Intent intent = new Intent(admin_upload_notice.this, admin_profile.class);
        intent.putExtra("acctype", Acctype);
        startActivityForResult(intent, 1);
    }
}
