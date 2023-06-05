package com.example.sleepytimer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.stream.IntStream;

public class WakeUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //hiding the title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slp_wake);

        //arrays
        Integer[] hours = IntStream.range(1, 13).boxed().toArray(Integer[]::new);
        Integer[] minutes = IntStream.range(0, 60).filter(n -> n % 5 == 0).boxed().toArray(Integer[]::new);
        String[] amPm = {"AM","PM"};

        Spinner hourSpinner = findViewById(R.id.wakeSpinner);
        Spinner minSpinner = findViewById(R.id.wakeSpinner2);
        Spinner amPmSpinner = findViewById(R.id.wakeSpinner3);

        ArrayAdapter hourAdapter = new ArrayAdapter<Integer>(this, R.layout.spinner_layout, hours);
        hourAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hourSpinner.setAdapter(hourAdapter);

        ArrayAdapter minAdapter = new ArrayAdapter<Integer>(this, R.layout.spinner_layout, minutes);
        minAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        minSpinner.setAdapter(minAdapter);

        ArrayAdapter amPmAdapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, amPm);
        amPmAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        amPmSpinner.setAdapter(amPmAdapter);

        //submit
        Button submit = findViewById(R.id.wake_Calculate);
        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                int selectedHour = (int) hourSpinner.getSelectedItem();
                int selectedMinute = (int) minSpinner.getSelectedItem();
                String selectedAmPm = (String) amPmSpinner.getSelectedItem();
                loadShowPage(view, ShowTimesActivity.class,selectedHour,selectedMinute,selectedAmPm);
            }
        });

        //back button
        ImageView home = findViewById(R.id.gtbImage);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadPage(view, GoToBedActivity.class,0);
            }
        });


        //back to sleep now
        ImageView sleep = findViewById(R.id.sleepNow);
        sleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadPage(view, SleepNowActivity.class,0);
            }
        });

        ImageView gtb = findViewById(R.id.wakeupImg);
        gtb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadPage(view, GoToBedActivity.class,0);
            }
        });

        ImageView settings = findViewById(R.id.settingsImg);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadPage(view,SettingsActivity.class,1);
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
        Intent intent = new Intent(this, cls);
        if(prevScreen == 1)
            intent.putExtra("screen","wake");
        startActivity(intent);
    }

    public void loadShowPage(View v, Class cls, int selectedHour, int selectedMinute, String selectedAmPm){

        // Create an intent to start the second activity
        Intent intent = new Intent(this, ShowTimesActivity.class);

        // Put selected values as extras in the intent
        intent.putExtra("selectedHour", selectedHour);
        intent.putExtra("selectedMinute", selectedMinute);
        intent.putExtra("selectedAmPm",selectedAmPm);
        intent.putExtra("option","subtract");
        intent.putExtra("screen","wake");

        // Start the second activity
        startActivity(intent);
    }

}

