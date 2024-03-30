package com.example.shc_notice;
import static com.example.shc_notice.login_page.PRFES;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    Animation topAnim,bottomAnim;
    ImageView image;
    TextView logo,slogan;
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    private static int main_animi=2000;
    boolean userExists = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            this.getSupportActionBar().hide();
        }
       catch (NullPointerException e) {
        }


        //Animation
        topAnim= AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim= AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        //image and text
        image=findViewById(R.id.imageView);
        logo=findViewById(R.id.textView);
        slogan=findViewById(R.id.textView2);

        image.setAnimation(topAnim);
        logo.setAnimation(bottomAnim);
        slogan.setAnimation(bottomAnim);




        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPreferences= getSharedPreferences(PRFES,0);
                boolean hasloggedin=sharedPreferences.getBoolean("hasloggedin",false);
                boolean hasloggedin1=sharedPreferences.getBoolean("login_user",false);
               String name1=sharedPreferences.getString("fullname","sample1");
                if(hasloggedin){
                    Intent intent = new Intent(MainActivity.this,admin_profile.class);
                   // intent.putExtra("user",name);
                    startActivity(intent);
                    finish();
                }else if(hasloggedin1){
                    Intent intent = new Intent(MainActivity.this,user_profile.class);
                   intent.putExtra("fullname",name1);
                    startActivity(intent);
                    finish();
                }
                else{
                    Intent intent= new Intent(MainActivity.this,login_page.class);
                    startActivity(intent);
                    finish();
                }
//                checkIfUserIsLoggedIn();
//                FirebaseUser currentUser =FirebaseAuth.getInstance().getCurrentUser();
//                if (currentUser == null) {
//                   // Intent intent = new Intent(MainActivity.this, login_page.class);
//                    startActivity(new Intent(MainActivity.this, login_page.class));
//
//                } else {
////                    Intent mainIntent = new Intent(MainActivity.this, admin_profile.class);
////                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    startActivity(new Intent(MainActivity.this, admin_profile.class));
//
////                    Intent intent = new Intent(MainActivity.this, login_page.class);
////                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
////                    startActivity(intent);
////                    finish();
//                }
//                finish();



            }
//
//
//                Intent intent=new Intent(MainActivity.this,login_page.class);
//
//                Pair[] pairs=new Pair[2];
//                pairs[0]=new Pair<View,String>(image,"logo_image");
//                pairs[1]=new Pair<View,String>(logo,"logo_text");
//
//               if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.LOLLIPOP) {
//                   ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, pairs);
//                   startActivity(intent,options.toBundle());
//
//                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
//                startActivity(intent);
//                finish();
//
//               }

        },main_animi);

    }

    private void checkIfUserIsLoggedIn() {

        SharedPreferences sharedPreferences = getSharedPreferences("username", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

        if (isLoggedIn) {
            startActivity(new Intent(MainActivity.this, admin_upload_notice.class));
            // User is logged in; start the appropriate activity
            // You can replace these with the actual activities you want to start
            // Example:
            // startActivity(new Intent(MainActivity.this, admin_profile.class));
        } else {
            // User is not logged in; start the login page
            startActivity(new Intent(MainActivity.this, login_page.class));
            finish();
        }

//        List<DatabaseReference> referencesToCheck = new ArrayList<>();
//
//        // Add your child references to the list
//        referencesToCheck.add(FirebaseDatabase.getInstance().getReference("user login"));
//        referencesToCheck.add(FirebaseDatabase.getInstance().getReference("admin login"));
//        referencesToCheck.add(FirebaseDatabase.getInstance().getReference("notice Staff"));
//        // Add more references as needed
//
//        String loggedInUserIdentifier = "ms54"; // Replace with the user identifier
//
//        for (DatabaseReference reference : referencesToCheck) {
//            reference.child(loggedInUserIdentifier).addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    if (dataSnapshot.exists()) {
//                        // User is logged in
//                        String userType = dataSnapshot.child("userType").getValue(String.class);
//                        String usernamedb = dataSnapshot.child("username").getValue(String.class);
//
//                        // Start the appropriate activity based on userType
//                        if ("admin login".equals(userType)) {
//                            startActivity(new Intent(MainActivity.this, admin_profile.class));
//                            SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.PRFES, 0);
//                            SharedPreferences.Editor editor = sharedPreferences.edit();
//                            editor.putBoolean("hasloggedin", true);
//                            editor.putString("staffcode", userType);
//                            editor.commit();
//                        } else if ("user login".equals(userType)) {
//                            startActivity(new Intent(MainActivity.this, admin_upload_notice.class));
//                        } else if ("notice Staff".equals(userType)) {
//                            startActivity(new Intent(MainActivity.this, access.class));
//                        }
//                        userExists = true;
//                        finish();
//                    }
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//                    // Handle any errors
//                }
//            });
//
//            // Exit the loop if the user is found in any reference
//            if (userExists) {
//                break;
//            }
//        }
//
//        // If the loop completes and userExists is still false, the user doesn't exist
//        if (!userExists) {
//            // User is not logged in; start the login page
//            startActivity(new Intent(MainActivity.this, login_page.class));
//            finish();
//        }
   }

}