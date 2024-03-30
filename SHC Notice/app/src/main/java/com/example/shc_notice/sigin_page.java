package com.example.shc_notice;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class sigin_page extends AppCompatActivity {
    TextInputLayout regFullname, regusername, regPassword, regconpass;
    Button  gotoLoginbtn;

    FirebaseDatabase rootNode;
    DatabaseReference reference;

    //Validation
    private Boolean validateFullname() {
        String val = regFullname.getEditText().getText().toString();
        // String namePattern=".*\\d.*";
        if (val.isEmpty()) {
            regFullname.setError("Name cannot be empty");
            regFullname.requestFocus();
            return false;
        } else if (!val.matches("^[A-Za-z]+$")) {

            regFullname.setError("Username must be Alphabets!");
            regFullname.findFocus();
            return false;
        } else {
            regFullname.setError(null);
            regFullname.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateRegnum() {
        String val = regusername.getEditText().getText().toString();
        if (val.isEmpty()) {
            regusername.setError("Register Number cannot be empty");
            regusername.requestFocus();
            return false;
        } else {
            regusername.setError(null);
            regusername.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateConPass() {
        String val = regPassword.getEditText().getText().toString();
        String val1 = regconpass.getEditText().getText().toString();
        if (!val.equals(val1)) {
            regconpass.setError("password cannot match");
            regconpass.requestFocus();
            return false;

        } else if (val.isEmpty()) {
            regPassword.setError("Password cannot Empty");
            regPassword.requestFocus();
            return false;
        } else {
            regPassword.setError(null);
            return true;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sigin_page);
        try {
            this.getSupportActionBar().hide();
        }
       catch (NullPointerException e) {
        }//        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#EB4C37")));

        regFullname = findViewById(R.id.fullname);
        regusername = findViewById(R.id.RegNum);
        regPassword = findViewById(R.id.pass);
        regconpass = findViewById(R.id.conpass);
//        go_btn = findViewById(R.id.go_btn);
        gotoLoginbtn = findViewById(R.id.gotloginbtn);

        gotoLoginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rootNode = FirebaseDatabase.getInstance();
                reference = rootNode.getReference("user login");
                if (!validateFullname() | !validateRegnum() | !validateConPass()) {
                    return;
                }
                //Get Values
                String fullname = regFullname.getEditText().getText().toString();
                String username = regusername.getEditText().getText().toString();
                String password = regconpass.getEditText().getText().toString();
                String acctype="user login";
                //pass value into dbHelper
                dbHelper helper = new dbHelper(fullname, username, password,acctype);
                reference.child(username).setValue(helper);
                Intent intent = new Intent(sigin_page.this, login_page.class);
                startActivity(intent);
                Toast.makeText(sigin_page.this, "Sigin Successfully", Toast.LENGTH_SHORT).show();
                // Clear the form values
                regFullname.getEditText().setText("");
                regusername.getEditText().setText("");
                regPassword.getEditText().setText("");
                regconpass.getEditText().setText("");
            }
        });
//        gologPagebtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(sigin_page.this, login_page.class);
//                startActivity(intent);
//            }
//        });
    }
}