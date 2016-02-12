package com.vctapps.pomocronometer.service;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.vctapps.pomocronometer.cons.StatusCronometer;

/**
 * Created by formadoresdosaber on 01/02/16.
 */
public class Cronometer implements ControlCronometer {

    private TextView clock;
    private long lastTime = 0;
    private long cronometer = 0;
    private StatusCronometer STATUS = StatusCronometer.Stoped;
    private static final String CRONO = "Crono_service";
    private OnFinish onFinish;

    public interface OnFinish{
        void onFinishTime();
    }

    public Cronometer(OnFinish onFinish){
        this.onFinish = onFinish;
    }

    @Override
    public void start() {
        STATUS = StatusCronometer.Started;
        Log.d(CRONO, "Cronometer started");
        if(STATUS == StatusCronometer.Started) {
            runClock();
        }
    }

    @Override
    public void stop() {
        if(STATUS == StatusCronometer.Started){
            STATUS = StatusCronometer.Stoped;
        }
    }

    @Override
    public void setClock(TextView clock) {
        this.clock = clock;
    }

    @Override
    public boolean isStarted() {
        if(STATUS == StatusCronometer.Started){
            return true;
        }
        return false;
    }

    public void setTime(long timeInMillis){
        this.cronometer = timeInMillis;
    }

    private void runClock(){
        new AsyncTask<Void, String, Void>(){

            @Override
            protected Void doInBackground(Void... params) {
                lastTime = System.currentTimeMillis();
                publishProgress(clockFormat(getMinutes(), getSeconds()));
                while (cronometer > 0) {
                    if(STATUS == StatusCronometer.Stoped){
                        Log.d(CRONO, "Cronometer stoped");
                        break;
                    }else {
                        if (System.currentTimeMillis() - lastTime >= 1000) {
                            cronometer -= 1000;

                            if(clock != null){
                                publishProgress(clockFormat(getMinutes(), getSeconds()));
                            }
                            lastTime = System.currentTimeMillis();
                            Log.d(CRONO, "Cronometer: " + cronometer);
                        }
                    }
                }
                Log.d(CRONO, "Cronometer finished time.");

                return null;
            }

            @Override
            protected void onProgressUpdate(String... time){
                clock.setText(time[0]);
            }

            @Override
            protected void onPostExecute(Void result){
                if(STATUS == StatusCronometer.Started){
                    onFinish.onFinishTime();
                }
            }
        }.execute();
    }

    private int getSeconds(){
        return (int) (cronometer / 1000) % 60 ;
    }

    private int getMinutes(){
        return (int) (cronometer / (1000*60) % 60);
    }

    private String clockFormat(int min, int seg){
        String minFormat, segFormat, timeFormat;

        if(min < 10){
            minFormat = "0" + min;
        }else{
            minFormat = String.valueOf(min);
        }

        if(seg < 10){
            segFormat = "0" + seg;
        }else{
            segFormat = String.valueOf(seg);
        }

        timeFormat = minFormat + ":" + segFormat;

        return timeFormat;
    }

    /*
    private void runClock(){
        Log.d(CRONO, "Cronometer run()");
        new Thread(new Runnable() {
            @Override
            public void run() {
                lastTime = System.currentTimeMillis();
                while (cronometer > 0) {
                    if(STATUS == StatusCronometer.Stoped){
                        Log.d(CRONO, "Cronometer stoped");
                        break;
                    }else {
                        if (System.currentTimeMillis() - lastTime >= 1000) {
                            cronometer -= 1000;
                            if(clock != null){

                                clock.setText(cronometer + "");
                            }
                            lastTime = System.currentTimeMillis();
                            Log.d(CRONO, "Cronometer: " + cronometer);
                        }
                    }
                }
                if(STATUS == StatusCronometer.Started){
                    onFinish.onFinishTime();
                }
                Log.d(CRONO, "Cronometer finished time.");
            }
        }).start();
    }*/
}
