package com.vctapps.pomocronometer.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.vctapps.pomocronometer.cons.CurrentPomo;

/**
 * Created by Victor on 02/02/2016.
 */
public class PomoClock extends Service implements ControlCronometer {

    private Cronometer cronometer;
    private CurrentPomo currentPomo = CurrentPomo.First;
    private boolean nextBreak = false;
    private final long longBreakTime = 1000 * 60 * 30;
    private final long breakTime = 1000 * 60 * 5;
    private final long pomoTime = 1000 * 60 * 25;

    public PomoClock(){
        cronometer = new Cronometer(onFinish());
    }

    public class CronoBinder extends Binder {
        public ControlCronometer getInterface(){
            return PomoClock.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new CronoBinder();
    }

    @Override
    public void start() {
        if(cronometer != null){
            if(!cronometer.isStarted()) {
                nextPomo();
                cronometer.start();
            }
        }
    }

    @Override
    public void setClock(TextView clock) {
        cronometer.setClock(clock);
    }

    @Override
    public void stop() {
        if(cronometer != null){
            if(cronometer.isStarted()){
                cronometer.stop();
                reset();
            }
        }
    }

    @Override
    public boolean isStarted() {
        return cronometer != null ? cronometer.isStarted() : false;
    }

    private void nextPomo(){
        switch (currentPomo){
            case First:
                if(nextBreak){
                    cronometer.setTime(breakTime);
                }else {
                    cronometer.setTime(pomoTime);
                    currentPomo = CurrentPomo.Second;
                }
                cronometer.start();
                nextBreak = !nextBreak;
                break;
            case Second:
                if(nextBreak){
                    cronometer.setTime(breakTime);
                }else {
                    cronometer.setTime(pomoTime);
                    currentPomo = CurrentPomo.Third;
                }
                cronometer.start();
                nextBreak = !nextBreak;
                break;
            case Third:
                if(nextBreak){
                    cronometer.setTime(breakTime);
                }else {
                    cronometer.setTime(pomoTime);
                    currentPomo = CurrentPomo.Fourt;
                }
                cronometer.start();
                nextBreak = !nextBreak;
                break;
            case Fourt:
                if(nextBreak){
                    cronometer.setTime(longBreakTime);
                }else {
                    cronometer.setTime(pomoTime);
                    currentPomo = CurrentPomo.First;
                }
                cronometer.start();
                nextBreak = !nextBreak;
                break;
        }
    }

    private Cronometer.OnFinish onFinish(){
        return new Cronometer.OnFinish() {
            @Override
            public void onFinishTime() {
                nextPomo();
            }
        };
    }

    private void reset(){
        currentPomo = CurrentPomo.First;
        nextBreak = false;
    }
}
