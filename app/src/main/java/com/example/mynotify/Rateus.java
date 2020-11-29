package com.example.mynotify;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

public class Rateus extends AppCompatActivity {
    RatingBar ratingBar;
    Button btnsubmit;
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rateus);

        ratingBar = findViewById(R.id.rating_bar);
        btnsubmit = findViewById(R.id.btn_submit);

        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = String.valueOf(ratingBar.getRating());
                Toast.makeText(getApplicationContext(), s+"Star", Toast.LENGTH_SHORT).show();
            }
        });
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
        recreate();
    }
}