package com.example.sleepytimer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
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

public class InitialSetup extends AppCompatActivity {

    String userName;
    int userAge = 0;
    boolean screenBeforeBed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_setup);

        SharedPreferences sharedPreferences = getSharedPreferences("app_preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Button submit = findViewById(R.id.setup_submit);
        TextView skip = findViewById(R.id.setup_skip);

        EditText name = findViewById(R.id.setup_enterName);
        RadioGroup radioGroup = findViewById(R.id.setup_radioGroup);
        CheckBox screens = findViewById(R.id.setup_checkBox);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton checkedRadioButton = group.findViewById(checkedId);
                if (checkedRadioButton != null) {
                    submit.setClickable(true);
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

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userName = name.getText().toString();
                if(!userName.isEmpty() && userAge!=0) {
                    if(userName.length() > 12 || userName.length() < 3){
                        AlertDialog.Builder builder = new AlertDialog.Builder(InitialSetup.this);
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
                        editor.putBoolean("isSetupCompleted", true);
                        editor.apply();
                        loadPage(view, SleepNowActivity.class);
                    }
                }
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(InitialSetup.this);
                    builder.setTitle("Input incomplete");
                    builder.setMessage("Please input all details before submitting.");
                    builder.setPositiveButton("OK", null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                userName = "User";
//                editor.putString("name", userName);
//                editor.putInt("age", userAge);
//                editor.putBoolean("screenBeforeBed", screenBeforeBed);
                editor.putBoolean("isSetupCompleted",true);
                editor.apply();
                loadPage(view,SleepNowActivity.class);
            }
        });
    }

    public void loadPage(View v,Class cls){
        Intent i = new Intent(this, cls);
        startActivity(i);
    }
}