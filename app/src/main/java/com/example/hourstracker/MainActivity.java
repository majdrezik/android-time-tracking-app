package com.example.hourstracker;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Chronometer chronometer;
    private long pauseOffSet = 0;
    private boolean isPlaying = false;
    private ToggleButton toggleButton;
    private Button reset_btn;
    private List<Long> times;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        chronometer = findViewById(R.id.chronometer);
        toggleButton = findViewById(R.id.Toggle);
        reset_btn = findViewById(R.id.reset_btn);
        toggleButton.setText(null);
        toggleButton.setTextOn(null);
        toggleButton.setTextOff(null);
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean buttonState) {
                //buttonState: true if button is pressed, otherwise false
                if(buttonState){
                    // SystemClock.elapsedRealtime() is the number of milliseconds since the device was turned on.
                    // without the following line, when we stop the chronometer for a specific time, it will start again with the specific time that passed in the background.
                    // i.e. if we stopped the timer at 5:00 for 30 seconds, and start again, it'll start from 5:30 instead of 5:00
                    chronometer.setBase(SystemClock.elapsedRealtime()- pauseOffSet);
                    chronometer.start();
                    isPlaying = true;
                }else{
                    chronometer.stop();
                    pauseOffSet = SystemClock.elapsedRealtime()- chronometer.getBase();
                    isPlaying = false;
//                    updateTimesList(pauseOffSet);
//                    printList();
                }
            }
        });

        // in our project, we decided that the restart could be done only when the timer is running. i.e, isPlaying == true
        reset_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isPlaying){
                    chronometer.setBase(SystemClock.elapsedRealtime());
                    pauseOffSet = 0;
                    chronometer.start();
                    isPlaying = true;
                }
            }
        });
    }

    private void updateTimesList(long chunk){
        times.add(chunk);
    }

    private void printList(){
        for(int i=0;i<times.size();i++){
            System.out.println(times.get(i));
        }
    }

}