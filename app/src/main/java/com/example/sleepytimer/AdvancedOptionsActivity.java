package com.example.sleepytimer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdvancedOptionsActivity extends AppCompatActivity {

    int userAge;
    int timeToSleep = 15;
    int hour,hourToAdd;
    int min,minToAdd;

    String amPm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();

        SharedPreferences sharedPreferences = getSharedPreferences("app_preferences", Context.MODE_PRIVATE);
        userAge = sharedPreferences.getInt("age", 0);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_options);

        RadioGroup activityRad = findViewById(R.id.adv_radGroup1);
        RadioGroup stressRad = findViewById(R.id.adv_radGroup2);

        TextView coffe = findViewById(R.id.adv_coffe);
        TextView screens = findViewById(R.id.adv_devices);

        CheckBox coffeCheck = findViewById(R.id.adv_checkBoxCoffe);
        CheckBox screensCheck = findViewById(R.id.adv_checkBoxDevices);

        coffe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                coffeCheck.toggle();
            }
        });
        screens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                screensCheck.toggle();
            }
        });

        activityRad.check(activityRad.getChildAt(0).getId());
        stressRad.check(stressRad.getChildAt(0).getId());


        TextView back = findViewById(R.id.adv_back);

        Button calc = findViewById(R.id.adv_calc);
        calc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTimes();
                getModifiers(coffeCheck,screensCheck,activityRad,stressRad);

                loadPage(view,ShowTimesActivity.class,1);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadPage(view,SleepNowActivity.class,0);
            }
        });

    }
    public void loadPage(View v,Class cls,int goToShowScreen){
        Intent i = new Intent(this, cls);
        if(goToShowScreen == 1) {
            i.putExtra("screen", "sleep");
            i.putExtra("option","add");
            i.putExtra("selectedHour", hour);
            i.putExtra("selectedMinute", min);
            i.putExtra("selectedAmPm", amPm);
            i.putExtra("hourToAdd", hourToAdd);
            i.putExtra("minToAdd", minToAdd);
            i.putExtra("timeToSleep", timeToSleep);
            startActivity(i);
        }
        else
            startActivity(i);
    }

    public void getTimes(){
        //get time details
        Calendar calendar = Calendar.getInstance();
        hour = calendar.get(Calendar.HOUR);
        min = calendar.get(Calendar.MINUTE);
        int tempAmPm = calendar.get(Calendar.AM_PM); //AM == 0 and PM == 1
        amPm = tempAmPm == 0 ? "AM" : "PM";
    }

    public void getModifiers(CheckBox coffeCheck, CheckBox screensCheck, RadioGroup activityRad,RadioGroup stressRad){
        if (coffeCheck.isChecked()) timeToSleep += 15;
        if(screensCheck.isChecked()){
            SharedPreferences sharedPreferences = getSharedPreferences("app_preferences", Context.MODE_PRIVATE);
            boolean screenBeforeBed = sharedPreferences.getBoolean("screenBeforeBed", false);
            if(!screenBeforeBed)
                timeToSleep += 10;
        }

        RadioButton checkedActivityButton = findViewById(activityRad.getCheckedRadioButtonId());
        String checkedActivityText = (String) checkedActivityButton.getText();
        if (checkedActivityText.equals("Moderate")) {
            timeToSleep -= 5;
            if(userAge > 25 || userAge == 0) minToAdd += 40;
            else minToAdd += 20;
        }
        else if (checkedActivityText.equals("High")) {
            timeToSleep -= 10;
            if(userAge > 25 || userAge == 0) {
                hourToAdd += 1;
                minToAdd += 30;
            }
            else minToAdd += 45;
        }

        RadioButton checkedStressButton = findViewById(stressRad.getCheckedRadioButtonId());
        String checkedStressText = (String) checkedStressButton.getText();
        if (checkedStressText.equals("Moderate")) timeToSleep += 5;
        else if (checkedStressText.equals("High")) {
            timeToSleep += 10;
            minToAdd += 20;
        }

        if(minToAdd > 60) {
            minToAdd -= 60;
            hourToAdd++;
        }
    }

}