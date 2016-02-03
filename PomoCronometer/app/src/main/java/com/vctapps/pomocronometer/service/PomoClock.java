package com.vctapps.pomocronometer.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by Victor on 02/02/2016.
 */
public class PomoClock extends Service implements ControlCronometer {

    private Cronometer cronometer;

    public PomoClock(){
        cronometer = new Cronometer();
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
                cronometer.start();
            }
        }
    }

    @Override
    public void stop() {
        if(cronometer != null){
            if(!cronometer.isStarted()){
                cronometer.stop();
            }
        }
    }

    @Override
    public boolean isStarted() {
        return cronometer != null ? cronometer.isStarted() : false;
    }
}
