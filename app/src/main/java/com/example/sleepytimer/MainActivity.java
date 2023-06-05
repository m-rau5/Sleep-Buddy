package com.example.sleepytimer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences sharedPreferences = getSharedPreferences("app_preferences", Context.MODE_PRIVATE);

        //DEBUG RESET ALL USER DATA:

//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.clear();
//        editor.apply();
//
//        SleepDataDbHelper dbhelper = new SleepDataDbHelper(this);
//        dbhelper.deleteAllData(this);


        boolean isSetupCompleted = sharedPreferences.getBoolean("isSetupCompleted", false);

        //hiding the title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button slpBud = findViewById(R.id.enterApp);
        slpBud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isSetupCompleted)
                    loadPage(view,InitialSetup.class);
                else loadPage(view, SleepNowActivity.class);
            }
        });

    }
    public void loadPage(View v,Class cls){
        Intent i = new Intent(this, cls);
        startActivity(i);
    }

}