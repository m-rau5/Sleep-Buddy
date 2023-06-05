package com.example.sleepytimer;

import java.util.ArrayList;
import java.util.List;
public class Calculator {
    int hour, min, amPm;
    String option;
    boolean passedTwelve = false;

    int fallAsleepTime;

    List<List<Integer>> GlobalHourList = new ArrayList<>();
    public Calculator(int hour, int min, int amPm,String option, Integer fallAsleepTime) {
        this.hour = hour;
        this.min = min;
        this.amPm = amPm;
        this.option = option;
        this.fallAsleepTime = fallAsleepTime;

        if(option.equals("add"))
            setHoursAdding();
        else
            setHoursSubtracting();
    }

    public void setHoursAdding() {
        //In GlobalHourList: 0 -> cycle 1, 1-> cycle 2 etc.

        min += fallAsleepTime;

        if(min > 60){
            checkTimes(false);
        }

        for (int i = 0; i < 7; i++) {
            min = (min + 30);
            hour++;
            if(i==0)
                checkTimes(true);
            else checkTimes(false);

            GlobalHourList.add(List.of(hour,min,amPm));
        }

    }

    public void checkTimes(boolean firstCycle){
        int baseHour = hour;
        if(min >= 60){
            hour++;
            min -= 60;
        }
        if(hour > 12){
            hour -= 12;
            if(!passedTwelve && (!firstCycle || (firstCycle && baseHour != 12))) {
                amPm = (amPm == 0) ? 1 : 0;
                passedTwelve = true;
            }
        }
        else if(hour == 12 && !firstCycle && !passedTwelve){   ///WHEN DOING 3 AM =>6'th cycle is 00:15 AM instead of 12:15 PM //3 pm works though => 00:15 AM as it should
            passedTwelve = true;
            if (amPm == 1) { //aka PM
                    amPm = 0;
                    hour = 0;
                } else {
                    amPm = 1;
                }
        }


    }

    public void addExtraTime(int extraH,int extraM){
        for (int i = 0; i < 7; i++) {
            hour = GlobalHourList.get(i).get(0); //hour,min,amPm
            min = GlobalHourList.get(i).get(1);
            amPm = GlobalHourList.get(i).get(2);
            min += extraM;
            hour += extraH;
            if(i==0)
                checkTimes(true);
            else checkTimes(false);
            GlobalHourList.set(i,List.of(hour,min,amPm));

        }
    }

    public void setHoursSubtracting(){

        for (int i = 0; i < 7; i++) {
            if(i==0)
                SubtractTimes(true);
            else SubtractTimes(false);

            GlobalHourList.add(List.of(hour,min,amPm));
        }
    }

    public void SubtractTimes(boolean first){
        min -= 30;
        hour -= 1;

        if (min < 0 ){
            hour--;
            min = 60 + min;
        }
        if (hour < 0)
        {
            hour = 12 + hour;
            amPm = (amPm==0) ? 1 : 0;
        }
    }

    public String getTimeText(int cycleNb){
        cycleNb = cycleNb-1;
        String time = "";
        List<Integer> selectedTime = GlobalHourList.get(cycleNb);
        String h = (selectedTime.get(0) == 0) ? (amPm == 0 ? "00" : "12") : String.valueOf(selectedTime.get(0));
        String m = (selectedTime.get(1) < 10) ? ("0" + String.valueOf(selectedTime.get(1))) : String.valueOf(selectedTime.get(1));

        time = (h +":" + m + " " + (selectedTime.get(2) == 0 ? "AM" : "PM"));

        return time;
    }

}
