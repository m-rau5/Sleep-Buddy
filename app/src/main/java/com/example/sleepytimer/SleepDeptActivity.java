package com.example.sleepytimer;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.stream.IntStream;

public class SleepDeptActivity extends AppCompatActivity {
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    SleepDataDbHelper dbHelper = new SleepDataDbHelper(this);
    String screen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences sharedPreferences = getSharedPreferences("app_preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        screen = (getIntent().getStringExtra("screen"));


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_dept);

        Integer[] hoursDesired = IntStream.range(1, 13).boxed().toArray(Integer[]::new);
        Integer[] minutesDesired = IntStream.range(0, 60).filter(n -> n % 5 == 0).boxed().toArray(Integer[]::new);

        Spinner hourDesiredSpinner = findViewById(R.id.debt_hour_spinner);
        Spinner minDesiredSpinner = findViewById(R.id.debt_min_spinner);
        Spinner hourSpinner = findViewById(R.id.debt_selpt_hours);
        Spinner minSpinner = findViewById(R.id.debt_selpt_min);


        ArrayAdapter hourDesiredAdapter = new ArrayAdapter<Integer>(this, R.layout.spinner_layout, hoursDesired);
        hourDesiredAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hourDesiredSpinner.setAdapter(hourDesiredAdapter);

        ArrayAdapter minDesiredAdapter = new ArrayAdapter<Integer>(this, R.layout.spinner_layout, minutesDesired);
        minDesiredAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        minDesiredSpinner.setAdapter(minDesiredAdapter);

        ArrayAdapter hourAdapter = new ArrayAdapter<Integer>(this, R.layout.spinner_layout, hoursDesired);
        hourAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hourSpinner.setAdapter(hourAdapter);

        ArrayAdapter minAdapter = new ArrayAdapter<Integer>(this, R.layout.spinner_layout, minutesDesired);
        minAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        minSpinner.setAdapter(minAdapter);

        hourDesiredSpinner.setSelection(sharedPreferences.getInt("desiredHour",0));
        minDesiredSpinner.setSelection(sharedPreferences.getInt("desiredMinute",0));

        boolean wentFromEdit = getIntent().getBooleanExtra("edit",false);

        if(dbHelper.isDateExists(this,getCurrentDate())){
            if(!wentFromEdit) {
                loadShowPage(new View(this), ShowDebtHours.class,
                        (Integer) hourDesiredSpinner.getSelectedItem(), (Integer) minDesiredSpinner.getSelectedItem());
            }
        }

        //DUMMY TEST DATA
//        dbHelper.deleteAllData(this);
//        dbHelper.printAllData(this);
//        dbHelper.insert(this,"2023-05-27",920);
//        dbHelper.insert(this,"2023-06-02",490);
//        dbHelper.insert(this,"2023-06-03",430);
//        dbHelper.insert(this,"2023-06-04",520);
//        dbHelper.updateHoursSlept(this,"2023-05-27",99999);
        dbHelper.printAllData(this);

        Button calc = findViewById(R.id.debt_calc);
        calc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //put Desired Hour
                editor.putInt("desiredHour", hourDesiredSpinner.getSelectedItemPosition());
                editor.putInt("desiredMinute",minDesiredSpinner.getSelectedItemPosition());
                editor.apply();

                //Put slept Hour
                int h = (int) hourSpinner.getSelectedItem();
                int m = (int) minSpinner.getSelectedItem();
                int dh = (int) hourDesiredSpinner.getSelectedItem();
                int dm = (int) minDesiredSpinner.getSelectedItem();
                int totalMinutesSlept = h*60 + m;
                if(dbHelper.isDateExists(view.getContext(),getCurrentDate())) {
                    dbHelper.updateHoursSlept(view.getContext(),getCurrentDate(),totalMinutesSlept);
                }
                else
                    insert(totalMinutesSlept);
                loadShowPage(view, ShowDebtHours.class,dh,dm);
            }
        });

        TextView back = findViewById(R.id.debt_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(screen.equals("wake"))
                    loadPage(view, WakeUpActivity.class);
                else if (screen.equals("gotobed"))
                    loadPage(view,GoToBedActivity.class);
                else loadPage(view,SleepNowActivity.class);
            }
        });

    }

    public void insert(int totalMinutesSlept){
        dbHelper.insert(this, getCurrentDate(), totalMinutesSlept);
    }

    public void loadPage(View v, Class cls){
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }

    public void loadShowPage(View v, Class cls,int dh,int dm){
        Intent intent = new Intent(this, cls);
        intent.putExtra("desiredHour",dh);
        intent.putExtra("desireddMin",dm);
        intent.putExtra("screen",screen);
        startActivity(intent);
    }

    public static String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
        return dateFormat.format(new Date());
    }


}