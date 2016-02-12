package com.vctapps.pomocronometer.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by Victor on 11/02/2016.
 */
public class Pomodoro implements ControlCronometer {

    private Intent intent_service;
    private Context context;
    private TextView clock;
    private static final String LOG = "Pomodoro";
    private ControlCronometer controlCronometer;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            PomoClock.CronoBinder conexao = (PomoClock.CronoBinder) service;
            controlCronometer = conexao.getInterface();
            if(clock != null){
                controlCronometer.setClock(clock);
            }
            Log.d(LOG, "Conectado ao service");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            controlCronometer = null;
        }
    };

    public Pomodoro(Context context, TextView clock){
        this.context = context;
        this.clock = clock;
        intent_service = new Intent(context, PomoClock.class);
        serviceStart();
        serviceBind();
    }

    private void serviceStart(){
        context.startService(intent_service);
        Log.d(LOG, "Started service.");
    }

    private void serviceBind(){
        context.bindService(intent_service, connection, Context.BIND_AUTO_CREATE);
        Log.d(LOG, "Bind on service.");
    }

    private void serviceUnbind(){
        context.unbindService(connection);
        Log.d(LOG, "unbind service.");
    }

    @Override
    public void setClock(TextView clock) {

    }

    @Override
    public void start() {
        controlCronometer.start();
    }

    @Override
    public void stop() {
        controlCronometer.stop();
    }

    @Override
    public boolean isStarted() {
        return controlCronometer != null ? controlCronometer.isStarted() : false;
    }

    public void onDestroy(){
        serviceUnbind();
        context.stopService(intent_service);
    }
}
