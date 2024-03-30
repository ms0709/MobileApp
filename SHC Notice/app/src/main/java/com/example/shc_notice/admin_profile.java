package com.example.shc_notice;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class admin_profile extends AppCompatActivity {
    TextView userTypetxt, uploadtxt, viewtxt, accesstxt;

    ImageView menu;
    RecyclerView recyclerView;
    ArrayList<dbHelper> userList;
    UsersAdapter usersAdapter;
    DatabaseReference databaseReference;
    userAdapter adapter;
    CardView cardView;
    LinearLayout toolbarLayout;
    RelativeLayout uploadNotice, viewNotice, allowAccess;
    boolean isToolbarVisible = true;
    boolean isCardViewVisible = true;
    // Button btn;

    private static String Acctype = "user login", user = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_profile);

        accesstxt = findViewById(R.id.accesstxt);
        uploadtxt = findViewById(R.id.uploadtxt);
        viewtxt = findViewById(R.id.viewtxt);

        uploadNotice = findViewById(R.id.uploadLayout);
        viewNotice = findViewById(R.id.viewLayout);
        allowAccess = findViewById(R.id.accessLayout);
        cardView = findViewById(R.id.navigationCard);
        recyclerView = findViewById(R.id.userRecyclerView);
        userTypetxt = findViewById(R.id.userTypeTitle);
        //  btn=findViewById(R.id.log);

        menu = findViewById(R.id.imageMenu);
        toolbarLayout = findViewById(R.id.toolbarlayout);
        //--Hide tool bar whenever have scroll
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        Intent intent = getIntent();
//        Acctype = intent.getStringExtra("acctype");
        user = intent.getStringExtra("user");
//        Toast.makeText(this, ""+user, Toast.LENGTH_SHORT).show();

        userTypetxt.setText(Acctype);
        //Toast.makeText(this, ""+Acctype, Toast.LENGTH_SHORT).show();
        FirebaseRecyclerOptions<dbHelper> options =
                new FirebaseRecyclerOptions.Builder<dbHelper>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("" + Acctype), dbHelper.class)
                        .build();

        usersAdapter = new UsersAdapter(options);
        recyclerView.setAdapter(usersAdapter);


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0 && isToolbarVisible) {
                    // Scrolling down, hide the toolbar
                    animateToolbar(false);
                    isToolbarVisible = false;
                } else if (dy < 0 && !isToolbarVisible) {
                    // Scrolling up or not scrolling, show the toolbar
                    animateToolbar(true);
                    isToolbarVisible = true;
                }
            }
        });//**

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0 && isCardViewVisible) {
                    // Scrolling down, hide the CardView
                    animateView(cardView, false);
                    isCardViewVisible = false;
                } else if (dy < 0 && !isCardViewVisible) {
                    // Scrolling up, show the CardView
                    animateView(cardView, true);
                    isCardViewVisible = true;
                }
            }
        });


        //Bottom CardView Navigation
        uploadNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadtxt.setTextColor(getResources().getColor(R.color.red_orange));
                viewtxt.setTextColor(getResources().getColor(R.color.white));
                accesstxt.setTextColor(getResources().getColor(R.color.white));

                Intent intent = new Intent(admin_profile.this, admin_upload_notice.class);
                startActivity(intent);

            }
        });

        viewNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadtxt.setTextColor(getResources().getColor(R.color.white));
                viewtxt.setTextColor(getResources().getColor(R.color.red_orange));
                accesstxt.setTextColor(getResources().getColor(R.color.white));
                Intent intent = new Intent(admin_profile.this, admin_view_notice.class);
                startActivity(intent);
            }
        });

        allowAccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadtxt.setTextColor(getResources().getColor(R.color.white));
                viewtxt.setTextColor(getResources().getColor(R.color.white));
                accesstxt.setTextColor(getResources().getColor(R.color.red_orange));
                Intent intent = new Intent(admin_profile.this, access.class);
                startActivity(intent);
                finish();
            }
        });


        //Navigation menu Btn
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // showBottomDialog();
                showPopupMenu(v);
            }


        });

    }


    @Override
    protected void onStart() {
        super.onStart();
        usersAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        usersAdapter.stopListening();
    }


    private void animateToolbar(boolean show) {
        float fromY, toY;

        if (show) {
            fromY = -toolbarLayout.getHeight();
            toY = 0;
        } else {
            fromY = 0;
            toY = -toolbarLayout.getHeight();
        }


        ObjectAnimator animation = ObjectAnimator.ofFloat(toolbarLayout, "translationY", fromY, toY);
        animation.setDuration(300); // Adjust the duration for your preferred animation speed
        animation.start();
    }


    private void animateView(View view, boolean show) {
        float fromY, toY;

        if (show) {
            fromY = view.getTranslationY();
            toY = 0;
        } else {
            fromY = view.getTranslationY();
            toY = view.getHeight(); // Hide the CardView by moving it down
        }

        ObjectAnimator animation = ObjectAnimator.ofFloat(view, "translationY", fromY, toY);
        animation.setDuration(300); // Adjust the duration for your preferred animation speed
        animation.start();
    }

    //Choose Users Menu
    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.main_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.logout:
                        SharedPreferences sharedPreferences = getSharedPreferences(login_page.PRFES, 0);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("hasloggedin", false);
                        editor.commit();
                        Intent in = new Intent(admin_profile.this, login_page.class);
                        startActivity(in);
                        finish();
                        return true;

                    case R.id.userLogin:

                        Acctype = "user login";
                        Intent intent = new Intent(admin_profile.this, admin_profile.class);
                        intent.putExtra("acctype", Acctype);
                        startActivityForResult(intent, 1);

                        finish();
                        return true;
                    case R.id.COElogin:

                        Acctype = "COE";
                        Intent i = new Intent(admin_profile.this, admin_profile.class);
                        i.putExtra("acctype", Acctype);
                        startActivityForResult(i, 2);
                        finish();
                        return true;
                    case R.id.GoldenJubileeLogin:

                        Acctype = "Golden Jubilee";
                        Intent g = new Intent(admin_profile.this, admin_profile.class);
                        g.putExtra("acctype", Acctype);
                        startActivityForResult(g, 3);
                        finish();
                        return true;
                    case R.id.silverJubileeLogin:

                        Acctype = "Silver Jubilee";
                        Intent s = new Intent(admin_profile.this, admin_profile.class);
                        s.putExtra("acctype", Acctype);
                        startActivityForResult(s, 4);
                        finish();
                        return true;
                    case R.id.johnMadLogin:

                        Acctype = "John Med";
                        Intent j = new Intent(admin_profile.this, admin_profile.class);
                        j.putExtra("acctype", Acctype);
                        startActivityForResult(j, 5);
                        finish();
                        return true;
                    case R.id.principalLogin:

                        Acctype = "Principal";
                        Intent p = new Intent(admin_profile.this, admin_profile.class);
                        p.putExtra("acctype", Acctype);
                        startActivityForResult(p, 6);
                        finish();
                        return true;
                    case R.id.adminLogin:
                        Acctype = "admin login";
                        Intent a = new Intent(admin_profile.this, admin_profile.class);
                        a.putExtra("acctype", Acctype);
                        startActivityForResult(a, 7);
                        finish();

                        return true;

                    case R.id.mcaLogin:
                        Acctype = "MCA";
                        Intent n = new Intent(admin_profile.this, admin_profile.class);
                        n.putExtra("acctype", Acctype);
                        startActivityForResult(n, 8);
                        finish();
                        return true;

                    case R.id.mbaLogin:

                        Acctype = "MBA";
                        Intent m = new Intent(admin_profile.this, admin_profile.class);
                        m.putExtra("acctype", Acctype);
                        startActivityForResult(m, 9);
                        finish();
                        return true;

                    case R.id.msc_csLogin:

                        Acctype = "Msc CS";
                        Intent cs = new Intent(admin_profile.this, admin_profile.class);
                        cs.putExtra("acctype", Acctype);
                        startActivityForResult(cs, 10);
                        finish();
                        return true;
                    default:

                        return false;
                }
            }
        });

        popupMenu.show();
    }




    //onClick for bottom navigation
    private void showBottomDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheetlayout);

        LinearLayout upload_access = dialog.findViewById(R.id.uploadAccess);
        LinearLayout upload_Notice = dialog.findViewById(R.id.uploadNotice);
        LinearLayout view_Notice = dialog.findViewById(R.id.viewNotice);
        // ImageView cancelButton = dialog.findViewById(R.id.cancelButton);

        upload_access.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
//                Toast.makeText(MainActivity.this,"Upload a Video is clicked",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(admin_profile.this, access.class);
                startActivity(intent);
                finish();


            }
        });

        upload_Notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                Intent intent = new Intent(admin_profile.this, admin_upload_notice.class);
                startActivity(intent);
            }
        });

        view_Notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                Intent intent = new Intent(admin_profile.this, admin_view_notice.class);
                startActivity(intent);
            }
        });


        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }


}
