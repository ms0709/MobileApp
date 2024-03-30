package com.example.shc_notice;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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

public class admin_view_notice extends AppCompatActivity {

    String[] items = {"Principal Notice", "COE Notice", "Golden Jubilee Notice", "Silver Jubilee Notice",
            "John Med Notice","MCA Notice",
            "MBA Notice","Msc CS Notice","Msc Mathematics Notice","Msc Micro Biology Notice"};
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterItems;

    AutoCompleteTextView selectAccess;
   // TextView princtxt, coetxt, goldtxt, silvertxt, jmtxt;
   // CardView princnotice, coeNotice, goldNotice, silverNotice, JmNotice;

    RecyclerView noticeRecyclerView;
    DatabaseReference pRef;
    Query query;
    String AccSelectItem = "";
    private static String NoticeType="Principal Notice";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_notice);

        autoCompleteTextView = findViewById(R.id.NoticeTypeSpinner);
        adapterItems = new ArrayAdapter<String>(this, R.layout.spinner_item, items);
        autoCompleteTextView.setAdapter(adapterItems);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                NoticeType = item;
               // NoticeType="COE Notice";
                Intent intent = new Intent(admin_view_notice.this, admin_view_notice.class);
                intent.putExtra("acctype", NoticeType);
                startActivityForResult(intent, 1);


                Toast.makeText(admin_view_notice.this, "COE Notice", Toast.LENGTH_SHORT).show();
            }
        });

        displayNotice();

    }

    private void displayNotice() {

        Intent intent = getIntent();
//        Acctype = intent.getStringExtra("acctype");
       // NoticeType = intent.getExtra("user");

        pRef = FirebaseDatabase.getInstance().getReference().child(NoticeType);//receivedCatNotice
        noticeRecyclerView = findViewById(R.id.recyclerView);
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
                    Toast.makeText(admin_view_notice.this, ":", Toast.LENGTH_SHORT).show();

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

                holder.dlt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(holder.NoticeTitle.getContext());
                        builder.setTitle("Are You Sure?");
                        builder.setMessage("This data will be Delete.");

                        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                FirebaseDatabase.getInstance().getReference().child("" + NoticeType)
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


    @Override
    public void onBackPressed() {

        super.onBackPressed();
        String Acctype = "user login";
        Intent intent = new Intent(admin_view_notice.this, admin_profile.class);
        intent.putExtra("acctype", Acctype);
        startActivityForResult(intent, 1);
    }

}