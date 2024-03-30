package com.example.shc_notice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;


public class temp extends AppCompatActivity {
    TextView name,reg_no;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp);
name=findViewById(R.id.Full_Name);
reg_no=findViewById(R.id.Regno);

showUser();

    }
    private void showUser(){
        Intent intent=getIntent();
        String full_name=intent.getStringExtra("Name");
        String username=intent.getStringExtra("Reg_no");
        name.setText(full_name);
        reg_no.setText(username);
    }
}