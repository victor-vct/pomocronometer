package com.vctapps.pomocronometer.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

/**
 * Created by formadoresdosaber on 01/02/16.
 */
public class Cronometer implements ControlCronometer {

    private long timeInMillis = 1000 * 60;
    private long currentTime = 0;
    private long cronometer = 0;
    private StatusCronometer STATUS = StatusCronometer.Started;
    private static final String CRONO = "Crono_service";

    @Override
    public void start() {
        STATUS = StatusCronometer.Started;
        if(STATUS == StatusCronometer.Started) {
            currentTime = System.currentTimeMillis();
            while (cronometer < timeInMillis) {
                if(STATUS == StatusCronometer.Stoped){
                    break;
                }else {
                    if (System.currentTimeMillis() - currentTime >= 1000) {
                        cronometer += 1000;
                        currentTime = System.currentTimeMillis();
                        Log.d(CRONO, "Cronometer: " + cronometer);
                    }
                }
            }
            Log.d(CRONO, "Cronometer finished.");
        }
    }

    @Override
    public void stop() {
        if(STATUS == StatusCronometer.Started){
            STATUS = StatusCronometer.Stoped;
        }
    }

    @Override
    public boolean isStarted() {
        if(STATUS == StatusCronometer.Started){
            return true;
        }
        return false;
    }

    public void setTime(long timeInMillis){
        this.timeInMillis = timeInMillis;
    }
}
