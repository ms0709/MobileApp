package com.example.shc_notice;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class user_profile extends AppCompatActivity {

    String[] items = {"Principal Notice", "COE Notice", "Golden Jubilee Notice", "Silver Jubilee Notice",
            "John Med Notice","MCA Notice",
            "MBA Notice","Msc CS Notice","Msc Mathematics Notice","Msc MicroBiology Notice"};
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterItems;

    ImageView logoutbtn;
  TextView fullname;// username, princtxt, coetxt, goldtxt, silvertxt, jmtxt;
//    CardView princnotice, coeNotice, goldNotice, silverNotice, JmNotice;

    RecyclerView noticeRecyclerViewUser;
    DatabaseReference pRef;
    Query query;

    private static String NoticeType = "Principal Notice";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);


        autoCompleteTextView = findViewById(R.id.NoticeTypeSpinner);
        adapterItems = new ArrayAdapter<String>(this, R.layout.spinner_item, items);
        autoCompleteTextView.setAdapter(adapterItems);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                NoticeType = item;
                // NoticeType="COE Notice";
                Intent intent = new Intent(user_profile.this, user_profile.class);
                intent.putExtra("acctype", NoticeType);
                startActivityForResult(intent, 1);

            }
        });
       fullname = findViewById(R.id.user_username);

        logoutbtn = findViewById(R.id.logoutuser);

        Intent intent = getIntent();
        String name = intent.getStringExtra("fullname");
        fullname.setText(name);



        displayNotice();


        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }


        });

    }

    private void displayNotice() {

        Intent intent = getIntent();
        //  Acctype = intent.getStringExtra("acctype");
        // NoticeType = intent.getExtra("user");
        Toast.makeText(this, ""+NoticeType, Toast.LENGTH_SHORT).show();
        pRef = FirebaseDatabase.getInstance().getReference().child(NoticeType);//receivedCatNotice
        noticeRecyclerViewUser = findViewById(R.id.recyclerviewuser);
        noticeRecyclerViewUser.setHasFixedSize(true);
        noticeRecyclerViewUser.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        // progressBar=findViewById(R.id.prograssBar);
        // progressBar.setVisibility(View.VISIBLE);

        query = pRef.orderByChild("dateTime");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    //progressBar.setVisibility(View.GONE);
                    showNotice();
                } else {

                    // progressBar.setVisibility(View.GONE);
                    Toast.makeText(user_profile.this, ":", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void showNotice() {

        FirebaseRecyclerOptions<adminUploadPDF> options = new FirebaseRecyclerOptions.Builder<adminUploadPDF>()
                .setQuery(query, adminUploadPDF.class)
                .build();
        FirebaseRecyclerAdapter<adminUploadPDF, AdapterPDF> adapter = new FirebaseRecyclerAdapter<adminUploadPDF, AdapterPDF>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AdapterPDF holder,int position, @NonNull adminUploadPDF model) {
                holder.NoticeTitle.setText(model.getTitle());
                holder.dtime.setText(model.getDateTime());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setType("application/pdf");
                        intent.setData(Uri.parse(model.getUrl()));
                        startActivity(intent);

//                        Intent intent = new Intent(holder.NoticeTitle.getContext(), view_pdf.class);
//                        intent.putExtra("pdfUrl", model.getUrl());
//                        startActivity(intent);
                    }
                });


            }

            @NonNull
            @Override
            public AdapterPDF onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_siede_pdf_item, parent, false);
                AdapterPDF holder = new AdapterPDF(view);

                return holder;
            }
        };
        noticeRecyclerViewUser.setAdapter(adapter);
        adapter.startListening();

    }

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.user_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.userlogoutmenu:
                        SharedPreferences sharedPreferences = getSharedPreferences(login_page.PRFES, 0);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("hasloggedin1", false);
                        editor.commit();
                        Intent in = new Intent(user_profile.this, login_page.class);
                        startActivity(in);
                        finish();
                        return true;

                    default:
                        return false;
                }
            }
        });
        popupMenu.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}