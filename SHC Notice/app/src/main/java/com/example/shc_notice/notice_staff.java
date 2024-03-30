package com.example.shc_notice;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
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
import androidx.appcompat.app.AlertDialog;
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

public class notice_staff extends AppCompatActivity {

    String[] items = {"Principal Notice", "COE Notice", "Golden Jubilee Notice", "Silver Jubilee Notice",
            "John Med Notice", "MCA Notice",
            "MBA Notice", "Msc CS Notice","Msc Mathematics Notice","Msc MicroBiology Notice"};
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterItems;

    ImageView logoutbtn;

    RecyclerView noticeRecyclerView;
    DatabaseReference pRef;
    Query query;

    private FirebaseRecyclerAdapter<adminUploadPDF, AdapterPDF> adapter;

    public static String UserType = "";
    private static String NoticeType = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_staff);


        Intent intent = getIntent();
        UserType = intent.getStringExtra("type");
        NoticeType = UserType;

     //   Toast.makeText(this, "N_" + NoticeType, Toast.LENGTH_SHORT).show();
       // Toast.makeText(this, "U_" + UserType, Toast.LENGTH_SHORT).show();
//        NoticeType=intent.getStringExtra("acctype");
        //  Toast.makeText(this, UserType, Toast.LENGTH_SHORT).show();

        if (NoticeType.equals(UserType)) {

            staffAllowUpload();
        } else {
            displayNotice();
        }

//Spinner
        autoCompleteTextView = findViewById(R.id.NoticeTypeSpinner);
        adapterItems = new ArrayAdapter<String>(this, R.layout.spinner_item, items);
        autoCompleteTextView.setAdapter(adapterItems);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String items = parent.getItemAtPosition(position).toString();
                NoticeType = items;
                // NoticeType="COE Notice";

                if (NoticeType.equals(UserType)) {

                    staffAllowUpload();
                } else {
                    displayNotice();
                }
                Toast.makeText(notice_staff.this, "" + NoticeType, Toast.LENGTH_SHORT).show();
            }
        });



        logoutbtn = findViewById(R.id.logoutStaff);

        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }


        });

    }

    private void staffAllowUpload() {


        pRef = FirebaseDatabase.getInstance().getReference().child(NoticeType);//receivedCatNotice
        noticeRecyclerView = findViewById(R.id.recyclerviewStaff);
        noticeRecyclerView.setHasFixedSize(true);
        noticeRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        query = pRef.orderByChild("dateTime").limitToLast(10);
        ;

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    //progressBar.setVisibility(View.GONE);
                    showStaffAccessNotice();
                } else {

                    // progressBar.setVisibility(View.GONE);
                    Toast.makeText(notice_staff.this, ":", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void showStaffAccessNotice() {


        FirebaseRecyclerOptions<adminUploadPDF> options = new FirebaseRecyclerOptions.Builder<adminUploadPDF>()
                .setQuery(query, adminUploadPDF.class)
                .build();
        adapter = new FirebaseRecyclerAdapter<adminUploadPDF, AdapterPDF>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AdapterPDF holder, @SuppressLint("RecyclerView") int position, @NonNull adminUploadPDF model) {
                holder.NoticeTitle.setText(model.getTitle());
                holder.dtime.setText(model.getDateTime());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setType("application/pdf");
                        intent.setData(Uri.parse(model.getUrl()));
                        startActivity(intent);

                        Intent i = new Intent(holder.NoticeTitle.getContext(), view_pdf.class);
                        i.putExtra("pdfUrl", model.getUrl());
                        startActivity(i);


                    }
                });

                holder.dlt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(holder.NoticeTitle.getContext());
                        builder.setTitle("Are You Sure?");
                        builder.setMessage("This data will be Delete.");

                        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                FirebaseDatabase.getInstance().getReference().child("" + UserType)
                                        .child(getRef(position).getKey()).removeValue();
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(holder.NoticeTitle.getContext(), "Cancelled", Toast.LENGTH_SHORT).show();
                            }
                        });
                        builder.show();

                    }
                });

            }

            @NonNull
            @Override
            public AdapterPDF onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pdf_item, parent, false);
                AdapterPDF holder = new AdapterPDF(view);

                return holder;
            }
        };
        noticeRecyclerView.setAdapter(adapter);
        adapter.startListening();

    }


    private void displayNotice() {

//        if (adapter != null) {
//            adapter.stopListening();
//        }
        pRef = FirebaseDatabase.getInstance().getReference().child(NoticeType);//receivedCatNotice
        noticeRecyclerView = findViewById(R.id.recyclerviewStaff);
        noticeRecyclerView.setHasFixedSize(true);
        noticeRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
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
                    Toast.makeText(notice_staff.this, ":", Toast.LENGTH_SHORT).show();

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
            protected void onBindViewHolder(@NonNull AdapterPDF holder, @SuppressLint("RecyclerView") int position, @NonNull adminUploadPDF model) {
                holder.NoticeTitle.setText(model.getTitle());
                holder.dtime.setText(model.getDateTime());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setType("application/pdf");
                        intent.setData(Uri.parse(model.getUrl()));
                        startActivity(intent);

                        Intent i = new Intent(holder.NoticeTitle.getContext(), view_pdf.class);
                        i.putExtra("pdfUrl", model.getUrl());
                        startActivity(i);


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
        noticeRecyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    private void showPopupMenu(View view) {
        if (NoticeType.equals(UserType)) {
            PopupMenu popupMenu = new PopupMenu(this, view);
            popupMenu.getMenuInflater().inflate(R.menu.staff_menu, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.stafflogoutmenu:
//                        SharedPreferences sharedPreferences = getSharedPreferences(login_page.PRFES, 0);
//                        SharedPreferences.Editor editor = sharedPreferences.edit();
//                        editor.putBoolean("hasloggedin1", false);
//                        editor.commit();
                            Intent in = new Intent(notice_staff.this, login_page.class);
                            startActivity(in);
                            finish();
                            return true;

                        case R.id.staffuploadmenu:

                            Intent i = new Intent(notice_staff.this, staff_upload_notice.class);
                            i.putExtra("upload_type", UserType);
                            startActivity(i);

                            return true;

                        default:
                            return false;
                    }
                }
            });
            popupMenu.show();
        } else {
            PopupMenu popupMenu = new PopupMenu(this, view);
            popupMenu.getMenuInflater().inflate(R.menu.user_menu, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.userlogoutmenu:

                            Intent i = new Intent(notice_staff.this, login_page.class);
                            startActivity(i);

                            return true;

                        default:
                            return false;
                    }
                }
            });
            popupMenu.show();

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}