package com.example.shc_notice;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class access extends AppCompatActivity {
    String[] items = {"user login", "admin login", "Principal", "COE", "Golden Jubilee", "Silver Jubilee", "John Med","MCA",
            "MBA","Msc CS","Msc Mathematics","Msc MicroBiology"};
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterItems;

    TextInputLayout staffname, staffID, staffpassword, conpass;
    AutoCompleteTextView selectAccess;

    Button savebtn;

    FirebaseDatabase rootNode;
    DatabaseReference reference;
    String AccSelectItem = "";

    //Validation
    private Boolean validateStaffname() {
        String val = staffname.getEditText().getText().toString();
        // String namePattern=".*\\d.*";
        if (val.isEmpty()) {
            staffname.setError("Name cannot be empty");
            staffname.requestFocus();
            return false;
        } else if (!val.matches("^[A-Za-z]+$")) {

            staffname.setError("Username must be Alphabets!");
            staffname.findFocus();
            return false;
        } else {
            staffname.setError(null);
            staffname.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateStaffID() {
        String val = staffID.getEditText().getText().toString();
        if (val.isEmpty()) {
            staffID.setError("Register Number cannot be empty");
            staffID.requestFocus();
            return false;
        } else {
            staffID.setError(null);
            staffID.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateStaffpassword() {
        String val = staffpassword.getEditText().getText().toString();
        String val1 = conpass.getEditText().getText().toString();
        if (!val.equals(val1)) {
            staffpassword.setError("password cannot match");
            staffpassword.requestFocus();
            return false;

        } else if (val.isEmpty()) {
            staffpassword.setError("Password cannot Empty");
            staffpassword.requestFocus();
            return false;
        } else {
            staffpassword.setError(null);
            return true;
        }
    }

    private Boolean validateAccess() {
        String val = selectAccess.getText().toString();

        if (val.isEmpty()) {
            selectAccess.setError("Register Number cannot be empty");
            selectAccess.requestFocus();
            return false;
        } else {
            selectAccess.setError(null);
            return true;
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        String Acctype = "user login";
        Intent intent = new Intent(access.this, admin_profile.class);
        intent.putExtra("acctype", Acctype);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access);

        staffname = findViewById(R.id.staff_Name);
        staffID = findViewById(R.id.staff_ID);
        staffpassword = findViewById(R.id.staff_pass);
        conpass = findViewById(R.id.staff_conpass);
        selectAccess = findViewById(R.id.spinner_Options);
        savebtn = findViewById(R.id.save);

        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rootNode = FirebaseDatabase.getInstance();
                reference = rootNode.getReference("" + AccSelectItem);

                if (!validateStaffname() | !validateStaffID() | !validateStaffpassword() | !validateAccess()) {
                    return;
                    //staffname.setText("");
                }

                //Get Values
                String staffName = staffname.getEditText().getText().toString();
                String username = staffID.getEditText().getText().toString();
                String password = staffpassword.getEditText().getText().toString();
                String accessType = selectAccess.getText().toString();

                //pass value into accessHelperDB
                accessHelperDB helper = new accessHelperDB(staffName, username, password, accessType);
                reference.child(username).setValue(helper);
                Toast.makeText(access.this, "Saved Successfully", Toast.LENGTH_SHORT).show();
            // Clear the form values
                staffname.getEditText().setText("");
                staffID.getEditText().setText("");
                staffpassword.getEditText().setText("");
                conpass.getEditText().setText("");
                selectAccess.setText("");

            }
        });


        autoCompleteTextView = findViewById(R.id.spinner_Options);
        adapterItems = new ArrayAdapter<String>(this, R.layout.spinner_item, items);
        autoCompleteTextView.setAdapter(adapterItems);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                AccSelectItem = item;
                Toast.makeText(access.this, ""+AccSelectItem, Toast.LENGTH_SHORT).show();
            }
        });
    }

}