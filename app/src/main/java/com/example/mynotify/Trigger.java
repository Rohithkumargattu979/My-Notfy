package com.example.mynotify;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Trigger extends AppCompatActivity {
    Button todo, events, cal, timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trigger);
        todo = findViewById(R.id.todo_txt);
        events = findViewById(R.id.events);
        timer = findViewById(R.id.ttimer);
        cal = findViewById(R.id.Calendar_id);


    }
    ///clicking//

    public void open_todo(View view){
        MainActivity.redirectActivity(this,Todotask.class);
    }
    public void open_events(View view)
    {
        MainActivity.redirectActivity(this,Addingevents.class);
    }
    public void open_calendar(View view)
    {
        MainActivity.redirectActivity(this,Calendar.class);
    }
    public void open_timer(View view)
    {
        MainActivity.redirectActivity(this,TimerActivity.class);
    }

}
