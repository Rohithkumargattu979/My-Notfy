package com.example.mynotify;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.BreakIterator;

import static androidx.core.content.ContextCompat.startActivity;

public class Addingevents extends AppCompatActivity {
    EditText title;
    EditText location;
    EditText description;
    EditText email;
    Button addEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addingevents);
        title = findViewById(R.id.etTitle);
        location = findViewById(R.id.etLocation);
        description= findViewById(R.id.etDescription);
        addEvent = findViewById(R.id.btnadd);
        email = findViewById(R.id.etEmail);
        addEvent.setOnClickListener(v -> {
            if(!title.getText().toString().isEmpty() && !location.getText().toString().isEmpty()
                    && !description.getText().toString().isEmpty() && !email.getText().toString().isEmpty()){
                Intent intent = new Intent (Intent.ACTION_INSERT);
                intent.setData(CalendarContract.Events.CONTENT_URI);
                intent.putExtra(CalendarContract.Events.TITLE,title.getText().toString());
                intent.putExtra(CalendarContract.Events.EVENT_LOCATION,location.getText().toString());
                intent.putExtra(CalendarContract.Events.DESCRIPTION,description.getText().toString());

                if(intent.resolveActivity(getPackageManager())!=null){
                    startActivity(intent);
                }else{
                    Toast.makeText(Addingevents.this, "There is no app that supports this action", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(Addingevents.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            }
        });
    }
}