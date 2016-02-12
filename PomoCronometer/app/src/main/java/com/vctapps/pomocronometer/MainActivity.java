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
import android.widget.TextView;

import com.vctapps.pomocronometer.service.ControlCronometer;
import com.vctapps.pomocronometer.service.PomoClock;
import com.vctapps.pomocronometer.service.Pomodoro;

public class MainActivity extends AppCompatActivity {

    private static final String LOG = "Activity_Pomo";
    private TextView clock;
    private Pomodoro pomodoro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        clock = (TextView) findViewById(R.id.clock);

        pomodoro = new Pomodoro(this, clock);

        Button bt = (Button) findViewById(R.id.start_stop);

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pomodoro == null) {
                    pomodoro = new Pomodoro(v.getContext(), clock);
                }else{
                    if(pomodoro.isStarted()){
                        pomodoro.stop();
                        Log.d(LOG, "Method stop()");
                    }else{
                        pomodoro.start();
                        Log.d(LOG, "Method start()");
                    }
                }
            }
        });
    }

    @Override
    protected void onDestroy(){
        pomodoro.onDestroy();
        super.onDestroy();
    }
}
