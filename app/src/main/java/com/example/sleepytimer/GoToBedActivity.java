package com.example.sleepytimer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.stream.IntStream;

public class GoToBedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go_to_bed);

        ImageView sleep = findViewById(R.id.sleepNow);
        ImageView wake = findViewById(R.id.wakeupImg);
        ImageView settings = findViewById(R.id.settingsImg);

        Integer[] hours = IntStream.range(1, 13).boxed().toArray(Integer[]::new);
        Integer[] minutes = IntStream.range(0, 60).filter(n -> n % 5 == 0).boxed().toArray(Integer[]::new);
        String[] amPm = {"AM", "PM"};

        Spinner hourSpinner = findViewById(R.id.gtb_spinner1);
        Spinner minSpinner = findViewById(R.id.gtb_spinner2);
        Spinner amPmSpinner = findViewById(R.id.gtb_spinner3);

        ArrayAdapter hourAdapter = new ArrayAdapter<Integer>(this, R.layout.spinner_layout, hours);
        hourAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hourSpinner.setAdapter(hourAdapter);

        ArrayAdapter minAdapter = new ArrayAdapter<Integer>(this, R.layout.spinner_layout, minutes);
        minAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        minSpinner.setAdapter(minAdapter);

        ArrayAdapter amPmAdapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, amPm);
        amPmAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        amPmSpinner.setAdapter(amPmAdapter);

        sleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadPage(view,SleepNowActivity.class,0);
            }
        });

        wake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadPage(view,WakeUpActivity.class,0);
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadPage(view,SettingsActivity.class,1);
            }
        });

        Button submit = findViewById(R.id.gtb_Calculate);
        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                int selectedHour = (int) hourSpinner.getSelectedItem();
                int selectedMinute = (int) minSpinner.getSelectedItem();
                String selectedAmPm = (String) amPmSpinner.getSelectedItem();
                loadShowPage(view, ShowTimesActivity.class,selectedHour,selectedMinute,selectedAmPm);
            }
        });

        TextView debt = findViewById(R.id.slpDebtButton);
        debt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadPage(view, SleepDeptActivity.class,1);
            }
        });

    }

    public void loadPage(View v,Class cls,int prevScreen){
        Intent i = new Intent(this, cls);
        if(prevScreen == 1)
            i.putExtra("screen","gotobed");
        startActivity(i);
    }

    public void loadShowPage(View v, Class cls, int selectedHour, int selectedMinute, String selectedAmPm){

        // Create an intent to start the second activity
        Intent intent = new Intent(this, ShowTimesActivity.class);

        // Put selected values as extras in the intent
        intent.putExtra("selectedHour", selectedHour);
        intent.putExtra("selectedMinute", selectedMinute);
        intent.putExtra("selectedAmPm",selectedAmPm);
        intent.putExtra("option","add");
        intent.putExtra("screen","gotobed");

        // Start the second activity
        startActivity(intent);
    }
}