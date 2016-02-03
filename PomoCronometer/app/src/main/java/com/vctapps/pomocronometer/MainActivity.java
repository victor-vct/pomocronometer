package com.vctapps.pomocronometer;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.vctapps.pomocronometer.service.ControlCronometer;
import com.vctapps.pomocronometer.service.Cronometer;
import com.vctapps.pomocronometer.service.PomoClock;

public class MainActivity extends AppCompatActivity {

    private static final String LOG = "Activity_Pomo";
    private ControlCronometer controlCronometer;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            PomoClock.CronoBinder conexao = (PomoClock.CronoBinder) service;
            controlCronometer = conexao.getInterface();
            Log.d(LOG, "Conectado ao service");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            controlCronometer = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button bt = (Button) findViewById(R.id.start_stop);

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(controlCronometer == null) {
                    Intent it = new Intent(v.getContext(), Cronometer.class);
                    startService(it);
                    bindService(it, connection, Context.BIND_AUTO_CREATE);
                }else{
                    if(controlCronometer.isStarted()){
                        controlCronometer.stop();
                    }else{
                        controlCronometer.start();
                    }
                }
            }
        });
    }
}
