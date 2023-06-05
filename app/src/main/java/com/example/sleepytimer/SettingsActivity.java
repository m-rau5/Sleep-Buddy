package com.example.sleepytimer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {

    String userName;
    int userAge;
    boolean screenBeforeBed = false;
    String prevScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        SharedPreferences sharedPreferences = getSharedPreferences("app_preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        prevScreen = getIntent().getStringExtra("screen");

        EditText name = findViewById(R.id.settings_enterName);
        RadioGroup radioGroup = findViewById(R.id.settings_radioGroup);
        CheckBox screens = findViewById(R.id.settings_checkBox);

        TextView welcome = findViewById(R.id.settings_title);
        welcome.setText("Hello " + sharedPreferences.getString("name","User") + "!");

        Button apply = findViewById(R.id.settings_apply);

        TextView back = findViewById(R.id.settings_back);

        //get Default User Variables

        userName = sharedPreferences.getString("name","User");
        userAge = sharedPreferences.getInt("age",0);
        screenBeforeBed = sharedPreferences.getBoolean("screenBeforeBed",false);

        name.setText(userName);

        if (userAge == 14) {
            radioGroup.check(R.id.set_radioButton);
        } else if (userAge == 19 ) {
            radioGroup.check(R.id.set_radioButton2);
        } else if(userAge == 26){
            radioGroup.check(R.id.set_radioButton3);
        }

        screens.setChecked(screenBeforeBed);

        //Set button listeners

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton checkedRadioButton = group.findViewById(checkedId);
                if (checkedRadioButton != null) {
                    String checkedText = (String) checkedRadioButton.getText();
                    if (checkedText.equals("14-18")) {
                        userAge = 14;
                    } else if (checkedText.equals("19-25")) {
                        userAge = 19;
                    } else if (checkedText.equals("25+")) {
                        userAge = 26;
                    }
                }
            }
        });

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userName = name.getText().toString();
                if(!userName.isEmpty() && userAge!=0) {
                    if(userName.length() > 12 || userName.length() < 3){
                        AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                        builder.setTitle(userName.length() > 12 ? "Name is too long" : "Name is too short");
                        builder.setMessage("Please input a valid name (between 3 and 12 characters)");
                        builder.setPositiveButton("OK", null);
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                    else {
                        screenBeforeBed = screens.isChecked() ? true : false;
                        editor.putString("name", userName);
                        editor.putInt("age", userAge);
                        editor.putBoolean("screenBeforeBed", screenBeforeBed);
                        editor.apply();
                        AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                        builder.setTitle("Done!");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                back.performClick();
                            }
                        });
                        builder.setCancelable(false);
                        AlertDialog dialog = builder.create();
                        dialog.show();

                    }
                }
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                    builder.setTitle("Input incomplete");
                    builder.setMessage("Please input all details before submitting.");
                    builder.setPositiveButton("OK", null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(prevScreen.equals("sleepNow"))
                    loadPage(view,SleepNowActivity.class,1);
                else if (prevScreen.equals("gotobed"))
                    loadPage(view,GoToBedActivity.class,1);
                else if (prevScreen.equals("wake"))
                    loadPage(view,WakeUpActivity.class,1);
            }
        });
    }

    public void loadPage(View v,Class cls,int needPrevScreen){
        Intent i = new Intent(this, cls);
        if(needPrevScreen == 1)
            i.putExtra("screen",prevScreen);
        startActivity(i);
    }
}