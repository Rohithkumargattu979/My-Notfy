package com.example.mynotify;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Profile extends AppCompatActivity {
    //Initialise variable
    DrawerLayout drawerLayout;
    TextView profile_user, profile_profession;
    EditText profile_email;
    String premail;
    private FirebaseAuth mAuth;
//    FirebaseUser currentUser = mAuth.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mAuth = FirebaseAuth.getInstance();

//        profile_email = findViewById(R.id.email_profile);

        //Assign variable
        drawerLayout = findViewById(R.id.drawer_layout);
        //premail = currentUser.getEmail();
    }




    public void ClickAddevent(View view){
        MainActivity.redirectActivity(this, Addingevents.class);
    }

    public void ClickMenu(View view){
        //Open drawer
        MainActivity.openDrawer(drawerLayout);
    }

    public void ClickLogo(View view){
        //Close drawer
        MainActivity.closeDrawer(drawerLayout);
    }

    public void ClickHome(View view){
        //Redirect activity to home
        MainActivity.redirectActivity(this,MainActivity.class);
    }

    public void ClickProfile(View view){
        //recreate activity
        recreate();
    }
    public void ClickStudent(View view){
        //redirect to student activity
        MainActivity.redirectActivity(this,Student.class);
    }
    public void ClickHousehold(View view){
        //redirect to household activity
        MainActivity.redirectActivity(this,Household.class);
    }
    public void ClickOffice(View view){
        //redirect to office activity
        MainActivity.redirectActivity(this,Office.class);
    }

    public void ClickTrigger(View view){
     MainActivity.redirectActivity(this,Trigger.class);
    }
    public void ClickRateus(View view){
        //redirect to rate us activity
        MainActivity.redirectActivity(this,Rateus.class);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Close drawer
        MainActivity.closeDrawer(drawerLayout);
    }

    @Override
    public void onBackPressed() {
        MainActivity.redirectActivity(this,MainActivity.class);
    }
}