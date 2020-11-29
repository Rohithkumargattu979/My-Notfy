package com.example.mynotify;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    GoogleSignInClient mGoogleSignInClient;
    DrawerLayout drawerLayout;
    Button resendmail;
    TextView emailnot;
    String userId;

    //Creating member variable for FirebaseAuth
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout=findViewById(R.id.drawer_layout);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        //Building Google sign-in and sign-up option.

// Configuring Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
// for the requestIdToken, use getString(R.string.default_web_client_id), this is in the values.xml file that
                // is generated from your google-services.json file (data from your firebase project), uses the google-sign-in method
                // web api key
                .requestIdToken(getString(R.string.default_web_client_id))//Default_web_client_id will be matched with the
                .requestEmail()
                .build();

// Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        //////////////mail verification/////////////
        resendmail = findViewById(R.id.resendmail);
        emailnot = findViewById(R.id.emailnot);
        userId = mAuth.getCurrentUser().getUid();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(!currentUser.isEmailVerified())
        {
            resendmail.setVisibility(View.VISIBLE);
            emailnot.setVisibility(View.VISIBLE);
            resendmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentUser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(v.getContext(), "Verification Email has been sent" , Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("Tag", "onFailure: Email not sent" + e.getMessage());
                        }
                    });

                }
            });
        }
        /////////////////////mail ver end/////////////

    }
    ///////////////////////////////////layout code begins/////////////////////////////////
    public static void redirectActivity(Activity activity, Class aClass) {
        //Initialise intent
        Intent intent = new Intent(activity,aClass);
        //Set flag
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //Start activity
        activity.startActivity(intent);
    }
    public static void closeDrawer(DrawerLayout drawerLayout) {
        //close drawer layout
        //check condition
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            //when drawer is open
            //close drawer
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }
    public static void openDrawer(DrawerLayout drawerLayout) {
        //Open drawer layout
        drawerLayout.openDrawer(GravityCompat.START);
    }
    public void ClickMenu(View view){
        //Open drawer
        openDrawer(drawerLayout);
    }
    public void ClickHome(View view){
        //Redirect activity to home
       recreate();
    }
    public void ClickStudent(View view){
        //redirect activity to student
        redirectActivity(this,Student.class);
    }
    public void ClickHousehold(View view){
        //Redirect activity to household
        redirectActivity(this,Household.class);
    }
    public void ClickOffice(View view){
        //Redirect activity to office
        redirectActivity(this,Office.class);
    }
    public void ClickProfile(View view){
        //Redirect activity to Profile
        redirectActivity(this,Profile.class);
    }
    public void ClickTrigger(View view){
        redirectActivity(this,Trigger.class);
    }
    public void ClickRateus(View view){
        //Redirect activity to Rate us
        redirectActivity(this,Rateus.class);
    }
    public void ClickShare(View view){
        redirectActivity(this,Share.class);
    }
    //////////////////////////////////layout code ends///////////////////////////////////
    public void logout(View view)
    {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();
        Toast.makeText(MainActivity.this, "You are logged out", Toast.LENGTH_SHORT).show();
    }
    public void onStart() {
        super.onStart();

// Checking if the user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

      /*  if (currentUser != null) {
            Toast.makeText(this, "Currently Logged in: " + currentUser.getEmail(), Toast.LENGTH_LONG).show();
        }*/
    }
    private void signOut() {
// Firebase sign out
        FirebaseAuth.getInstance().signOut();

// Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
// Google Sign In failed, update UI appropriately
                        Toast.makeText(getApplicationContext(),"Signed out of google",Toast.LENGTH_SHORT).show();
                        Intent i=new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(i);
                    }
                });
    }

    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.logout1) {
            signOut();
        }
    }
}