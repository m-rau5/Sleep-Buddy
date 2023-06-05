package com.example.sleepytimer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;


public class SleepNowActivity extends AppCompatActivity {
    int userAge;
    boolean screenBeforeBed;
    Calculator calc;

    protected void onCreate(Bundle savedInstanceState) {

        //hiding the title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_bud);

        setPreferences();


        ImageView home = findViewById(R.id.gtbImage);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadPage(view,GoToBedActivity.class,0);
            }
        });

        ImageView wake = findViewById(R.id.wakeupImg);
        wake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadPage(view, WakeUpActivity.class,0);
            }
        });

        ImageView settings = findViewById(R.id.settingsImg);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadPage(view,SettingsActivity.class,1);
            }
        });

        TextView debt = findViewById(R.id.SlpDebtButton);
        debt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loadPage(view,SleepDeptActivity.class,1);
            }
        });

        Button calc = findViewById(R.id.slp_Calculate);
        calc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getWakeUp();
                calc.setText("Recalculate");
            }
        });

        TextView advancedFilters = findViewById(R.id.advanced_text);
        advancedFilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadPage(view,AdvancedOptionsActivity.class,0);
            }
        });
    }

    public void loadPage(View v,Class cls,int prevScreen){
        Intent i = new Intent(this, cls);
        if(prevScreen == 1)
            i.putExtra("screen","sleepNow");
        startActivity(i);
    }

    public void getWakeUp(){
        TextView wakeUpTime1 = findViewById(R.id.slp_currTime1);
        TextView wakeUpTime2 = findViewById(R.id.slp_currTime2);
        TextView desc1 = findViewById(R.id.slp_should_text1);
        TextView desc2 = findViewById(R.id.slp_should_text2);

        //get time details
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR);
        int min = calendar.get(Calendar.MINUTE);
        int amPm = calendar.get(Calendar.AM_PM); //AM == 0 and PM == 1


        calc = new Calculator(hour,screenBeforeBed ? min+10 : min,amPm,"add",15);

        desc1.setText("For "+ getTimes()[0] + " of sleep:");
        desc1.setVisibility(View.VISIBLE);
        wakeUpTime1.setVisibility(View.VISIBLE);

        desc2.setText("For " + getTimes()[1] + " of sleep:");
        desc2.setVisibility(View.VISIBLE);
        wakeUpTime2.setVisibility(View.VISIBLE);

        wakeUpTime1.setText(calc.getTimeText(getCycles()[0]));
        wakeUpTime2.setText(calc.getTimeText(getCycles()[1]));
    }

    public void setPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("app_preferences", Context.MODE_PRIVATE);
        userAge = sharedPreferences.getInt("age", 0);
        screenBeforeBed = sharedPreferences.getBoolean("screenBeforeBed", false);
    }

    public int[] getCycles(){
        int[] cycles;
        if(userAge == 14){
            cycles = new int[]{7,6};
        }
        else if (userAge == 19){
            cycles = new int[]{6,5};
        }
        else {
            cycles = new int[]{5,4};
        }
        return cycles;
    }

    public String[] getTimes(){
        String[] time;
        if(userAge == 14){
            time = new String[]{"10.5 Hrs", "9 Hrs"};
        }
        else if (userAge == 19){
            time = new String[]{"9 Hrs", "7.5 Hrs"};
        }
        else {
            time = new String[]{"7.5 Hrs", "6 Hrs"};
        }
        return time;
    }

}
