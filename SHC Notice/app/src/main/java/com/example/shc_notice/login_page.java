package com.example.shc_notice;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class login_page extends AppCompatActivity {



    Button sigin_pagebtn, login_btn;
    TextInputLayout username, password;
    ProgressBar progressBar;
    String User="";
    String Fullname="";
    String userType="";
    boolean userExists = false;
    public static final String PRFES="Mypreference";
    private SharedPreferences sharedPreferences;



    //String userExists = "false";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
        }

        PreferenceManager.getDefaultSharedPreferences(this);


        login_btn = findViewById(R.id.loginbtn);
        sigin_pagebtn = findViewById(R.id.siginbtn);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        progressBar = findViewById(R.id.prograssBar);
//        User=username.getEditText().getText().toString();

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);

                if (!validateUsername() | !validatePassword()) {
                    progressBar.setVisibility(View.GONE);
                    return;
                } else {
                    progressBar.setVisibility(View.GONE);
                    isUser();




                }


            }
        });


        sigin_pagebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login_page.this, sigin_page.class);
                startActivity(intent);
            }
        });

    }

    private Boolean validateUsername() {
        String val = username.getEditText().getText().toString();
        if (val.isEmpty()) {
            username.setError("Username cannot empty");
            username.findFocus();

            return false;
        } else {
            username.setError(null);
            username.setErrorEnabled(false);

            return true;
        }
    }

    private Boolean validatePassword() {
        String val = password.getEditText().getText().toString();
        if (val.isEmpty()) {
            password.setError("password cannot empty");
            password.findFocus();

            return false;
        } else {
            password.setError(null);
            password.setErrorEnabled(false);

            return true;
        }
    }

    private void isUser() {
        String userEnteredUsername = username.getEditText().getText().toString();
        String userEnteredPassword = password.getEditText().getText().toString();
//        Toast.makeText(this, ""+userEnteredUsername, Toast.LENGTH_SHORT).show();
        User=userEnteredUsername;

        DatabaseReference rootReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userInfoReference = rootReference.child("user login");
        DatabaseReference adminInfoReference = rootReference.child("admin login");
        DatabaseReference principalReference = rootReference.child("Principal");
        DatabaseReference GJStaffReference = rootReference.child("Golden Jubilee");
        DatabaseReference SJStaffReference = rootReference.child("Silver Jubilee");
        DatabaseReference JMStaffReference = rootReference.child("John Med");
        DatabaseReference coeStaffReference = rootReference.child("COE");
        DatabaseReference MCAStaffReference = rootReference.child("MCA");
        DatabaseReference MBAStaffReference = rootReference.child("MBA");
        DatabaseReference Msc_csStaffReference = rootReference.child("Msc CS");
        DatabaseReference Msc_MathsReference = rootReference.child("Msc Mathematics");
        DatabaseReference Msc_MicroBioReference = rootReference.child("Msc MicroBiology");
        // Add more references as needed for your specific use case

        // Create a list of references to check
        List<DatabaseReference> referencesToCheck = new ArrayList<>();
        referencesToCheck.add(userInfoReference);
        referencesToCheck.add(adminInfoReference);
        referencesToCheck.add(principalReference);
        referencesToCheck.add(GJStaffReference);
        referencesToCheck.add(SJStaffReference);
        referencesToCheck.add(JMStaffReference);
        referencesToCheck.add(coeStaffReference);
        referencesToCheck.add(MCAStaffReference);
        referencesToCheck.add(MBAStaffReference);
        referencesToCheck.add(Msc_csStaffReference);
        referencesToCheck.add(Msc_MathsReference);
        referencesToCheck.add(Msc_MicroBioReference);
        // Add more references to the list as needed


        for (DatabaseReference reference : referencesToCheck) {
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                            String usernameFromDB = childSnapshot.child("username").getValue(String.class);
                            String passwordFromDB = childSnapshot.child("password").getValue(String.class);

                            if (usernameFromDB != null && usernameFromDB.equals(userEnteredUsername)
                                    && passwordFromDB != null && passwordFromDB.equals(userEnteredPassword)) {
                                // User found in this reference
                                userExists = true;
                                // User found in this reference
                                userType = childSnapshot.child("acctype").getValue(String.class);
                                Fullname = childSnapshot.child("fullname").getValue(String.class);

                                // Redirect to different activities based on userType
                                if (userType != null) {
                                    switch (userType) {
                                        case "admin login":
                                            startAdminActivity();
                                            break;
                                        case "user login":
                                            startUserActivity();
                                            break;
                                        case "Principal":
                                            startStaffActivity();
                                            break;
                                        case "Golden Jubilee":
                                            startStaffActivity();
                                            break;
                                        case "Silver Jubilee":
                                            startStaffActivity();
                                            break;
                                        case "John Med":
                                            startStaffActivity();
                                            break;
                                        case "COE":
                                            startStaffActivity();
                                            break;
                                        case "MCA":
                                            startStaffActivity();
                                            break;
                                        case "MBA":
                                            startStaffActivity();
                                            break;
                                        case "Msc CS":
                                            startStaffActivity();
                                            break;
                                        case "Msc Mathematics":
                                            startStaffActivity();
                                            break;
                                        case "Msc MicroBiology":
                                            startStaffActivity();
                                            break;
                                        default:

                                            break;
                                    }
                                    if (!userExists) {
                                        username.setError("No Such user exist");
                                        username.requestFocus();
                                    }
                                }
                                break;
                                // No need to continue checking other references
                            }

                        }

                       // Toast.makeText(login_page.this, ""+i, Toast.LENGTH_SHORT).show();
                    }
                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle error
                }
            });

            if (userExists) {
                break; // User found, exit the loop
            }

        }


    }

    private void startStaffActivity() {
//        SharedPreferences sharedPreferences = getSharedPreferences(login_page.PRFES, 0);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putBoolean("login_user", true);
//        editor.putString("fullName",Fullname);
//
//        editor.commit();
        Intent intent = new Intent(getApplicationContext(), notice_staff.class);
        intent.putExtra("fullname",Fullname);// Pass any necessary data to the AdminProfileActivity
        intent.putExtra("type",userType+" Notice" );// Pass any necessary data to the AdminProfileActivity
        startActivity(intent);
        finish();
    }

    private void startUserActivity() {
        SharedPreferences sharedPreferences = getSharedPreferences(login_page.PRFES, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("login_user", true);
        editor.putString("fullName",Fullname);

        editor.commit();
        Intent intent = new Intent(getApplicationContext(), user_profile.class);
        intent.putExtra("fullname",Fullname);// Pass any necessary data to the AdminProfileActivity
        startActivity(intent);
        finish();
    }

    private void startAdminActivity() {
        SharedPreferences sharedPreferences = getSharedPreferences(login_page.PRFES, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("hasloggedin", true);
      //  editor.putString("user",User);
        editor.commit();
        Intent i = new Intent(login_page.this,admin_profile.class);
        i.putExtra("user", User);
        startActivity(i);
//        finish();
        Intent intent = new Intent(getApplicationContext(), admin_profile.class);
        intent.putExtra("acctype", "user login");
        startActivity(intent);
        finish();
    }


}


