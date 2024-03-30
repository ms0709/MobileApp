package com.example.shc_notice;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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




public class RetrivePDF extends AppCompatActivity {

    RecyclerView noticeRecyclerView;
    private DatabaseReference pRef;
    Query query;
    ProgressBar progressBar;
    String receivedCatNotice="";

//    FirebaseRecyclerOption FirebaseRecyclerOptions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrive_pdf);

        displayNotice();

    }

    private void displayNotice() {
        //Geting value from admin_view_notice.java
        Intent intent = getIntent();
        receivedCatNotice = intent.getStringExtra("CatNotice");
        Toast.makeText(this, ""+receivedCatNotice, Toast.LENGTH_SHORT).show();

        pRef= FirebaseDatabase.getInstance().getReference().child(receivedCatNotice);//receivedCatNotice
        noticeRecyclerView=findViewById(R.id.recyclerView);
        noticeRecyclerView.setHasFixedSize(true);
        noticeRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        progressBar=findViewById(R.id.prograssBar);
        progressBar.setVisibility(View.VISIBLE);

        query=pRef.orderByChild("title");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    progressBar.setVisibility(View.GONE);
                    showNotice();
                }else{

                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(RetrivePDF.this, ":", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void showNotice() {
       // try {


            FirebaseRecyclerOptions<adminUploadPDF> options = new FirebaseRecyclerOptions.Builder<adminUploadPDF>()
                    .setQuery(query, adminUploadPDF.class)
                    .build();
            FirebaseRecyclerAdapter<adminUploadPDF, Adapter> adapter = new FirebaseRecyclerAdapter<adminUploadPDF, Adapter>(options) {
                @Override
                protected void onBindViewHolder(@NonNull Adapter holder, int position, @NonNull adminUploadPDF model) {

                    progressBar.setVisibility(View.GONE);
                    holder.NoticeTitle.setText(model.getTitle());
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setType("application/pdf");
                            intent.setData(Uri.parse(model.getUrl()));
                            startActivity(intent);
                        }
                    });



                }

                @NonNull
                @Override
                public Adapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pdf_item, parent, false);
                    Adapter holder = new Adapter(view);
                    return holder;
                }
            };

            noticeRecyclerView.setAdapter(adapter);
            adapter.startListening();

        }
//        catch (Exception e) {
//            System.err.println("An error occurred: " + e.getMessage());
//        }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
