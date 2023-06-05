package com.example.sleepytimer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ShowDebtHours extends AppCompatActivity {
    String screen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        screen = (getIntent().getStringExtra("screen"));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_debt_hours);

        SleepDataDbHelper dbHelper = new SleepDataDbHelper(this);


        int minDesiredToSleep = getIntent().getIntExtra("desiredHour",0)*60+getIntent().getIntExtra("desireddMin",0);

        int timeAmounts = dbHelper.countEntries(this,getCurrentDate(),getSevenDaysAgoDate());

        int minSlept = (dbHelper.getMinSlept(this,getCurrentDate(),getSevenDaysAgoDate()));
        minDesiredToSleep *= timeAmounts;


        TextView show = findViewById(R.id.debtShow_Hours);

        float timeDiff = minSlept-minDesiredToSleep;

        if(timeDiff <= -120){
            show.setTextColor(getResources().getColor(R.color.red2));
            show.setText(timeDiff/60 + " Hours");
        }
        else if(timeDiff > -120 && timeDiff <= -60){
            show.setTextColor(getResources().getColor(R.color.red1));
            show.setText(timeDiff/60 + " Hours");
        }
        else if (timeDiff > -60 && timeDiff < 0)
        {
            show.setTextColor(getResources().getColor(R.color.orange1));
            show.setText(timeDiff/60 + " Hours");
        }
        else if(timeDiff >= 0 && timeDiff < 2){
            show.setTextColor(getResources().getColor(R.color.green2));
            show.setText("+" + timeDiff/60 + " Hours");
        }
        else{
            show.setTextColor(getResources().getColor(R.color.green1));
            show.setText("+" + timeDiff/60 + " Hours");
        }

        TextView back = findViewById(R.id.debtShow_back);
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

        TextView edit = findViewById(R.id.debtShow_edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), SleepDeptActivity.class);
                intent.putExtra("edit",true);
                String screen = getIntent().getStringExtra("screen");
                intent.putExtra("screen",screen);
                startActivity(intent);
            }
        });
    }
    public void loadPage(View v, Class cls){
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }

    private static final String DATE_FORMAT = "yyyy-MM-dd";

    public static String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
        return dateFormat.format(new Date());
    }

    public static String getSevenDaysAgoDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -7);
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }

}