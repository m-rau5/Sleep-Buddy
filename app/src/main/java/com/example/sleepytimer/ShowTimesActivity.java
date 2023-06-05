package com.example.sleepytimer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import java.util.ArrayList;

public class ShowTimesActivity extends AppCompatActivity {
    int userAge;
    boolean screenBeforeBed;
    Calculator calc;
    int hour,minute,amPm, timeToSleep = 15;
    String screen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //hiding the title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slp_show_times);

        setPreferences();

        TextView show1 = findViewById(R.id.show_1);
        TextView show2 = findViewById(R.id.show_2);
        TextView show3 = findViewById(R.id.show_3);

        TextView c1 = findViewById(R.id.cycle_1);
        TextView c2 = findViewById(R.id.cycle_2);
        TextView c3 = findViewById(R.id.cycle_3);

        String option = (getIntent().getStringExtra("option"));
        screen = (getIntent().getStringExtra("screen"));

        hour = getIntent().getIntExtra("selectedHour", 4);
        minute = getIntent().getIntExtra("selectedMinute", 0);
        amPm = (getIntent().getStringExtra("selectedAmPm").equals("AM") ? 0 : 1); // 0 AM 1 PM

        if(screen.equals("sleep")) { //means we are in advanced options
            timeToSleep = getIntent().getIntExtra("timeToSleep", 15);

        }


        calc = new Calculator(hour,screenBeforeBed ? minute+10 : minute,
                amPm,option,timeToSleep);

        if(screen.equals("sleep")){
            int extraH = getIntent().getIntExtra("hourToAdd", 4);
            int extraM  = getIntent().getIntExtra("minToAdd", 0);
            calc.addExtraTime(extraH,extraM);

            ArrayList<Integer[]> times = new ArrayList<>();
            times.add(new Integer[]{9,0});
            times.add(new Integer[]{7,30});
            times.add(new Integer[]{6,0});

            for(Integer[] time : times){
                time[1] += extraM;
                if(time[1] > 60)
                    time[0] += 1;
                time[0] += extraH;
            }

            c1.setText("For " + (times.get(0)[0] == 0 ? 12 : times.get(0)[0]) +":"+
                    (times.get(0)[1]<10 ? ("0"+times.get(0)[1]) : times.get(0)[1])
                    + " hours of sleep:");
            c2.setText("For " + (times.get(1)[0] == 0 ? 12 : times.get(1)[0]) +":"+
                    (times.get(1)[1]<10 ? ("0"+times.get(1)[1]) : times.get(1)[1])
                    + " hours of sleep:");
            c3.setText("For " + (times.get(2)[0] == 0 ? 12 : times.get(2)[0]) +":"+
                    (times.get(2)[1]<10 ? ("0"+times.get(2)[1]) : times.get(2)[1])
                    + " hours of sleep:");


            show1.setText(calc.getTimeText(6));
            show2.setText(calc.getTimeText(5));
            show3.setText(calc.getTimeText(4));

        }
        if(!screen.equals("sleep")) {
            int[] cycles = getCycles();
            show1.setText(calc.getTimeText(cycles[0]));
            c1.setText("For " + cycles[0] + " cycles of sleep:");
            show2.setText(calc.getTimeText(cycles[0]-1));
            c2.setText("For " + (cycles[0]-1) + " cycles of sleep:");
            show3.setText(calc.getTimeText(cycles[0]-2));
            c3.setText("For " + (cycles[0]-2) + " cycles of sleep:");
        }

        show1.setVisibility(View.VISIBLE);
        findViewById(R.id.cycle_1).setVisibility(View.VISIBLE);

        show2.setVisibility(View.VISIBLE);
        findViewById(R.id.cycle_2).setVisibility(View.VISIBLE);

        show3.setVisibility(View.VISIBLE);
        findViewById(R.id.cycle_3).setVisibility(View.VISIBLE);

        findViewById(R.id.show_back).setOnClickListener(new View.OnClickListener() {
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
    public void loadPage(View v,Class cls){
        Intent i = new Intent(this, cls);
        startActivity(i);
    }

    public void setPreferences(){
        SharedPreferences sharedPreferences = getSharedPreferences("app_preferences", Context.MODE_PRIVATE);
        userAge = sharedPreferences.getInt("age",0);
        screenBeforeBed = sharedPreferences.getBoolean("screenBeforeBed",false);
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

}