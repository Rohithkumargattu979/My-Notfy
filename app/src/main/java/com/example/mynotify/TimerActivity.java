package com.example.mynotify;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Timer;
import java.util.TimerTask;

public class TimerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
    }

    public void start(View view) {

        Timer t = new Timer();
        final Button btn = (Button)findViewById(R.id.btn_timer);
        TimerTask tt = new TimerTask() {
            @Override
            public void run()
            {
                btn.setRotation(btn.getRotation()+10);
            }
        };

        t.schedule(tt, 0, 1000);
    }
}