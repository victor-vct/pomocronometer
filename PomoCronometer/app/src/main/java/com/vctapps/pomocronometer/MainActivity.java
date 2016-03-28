package com.vctapps.pomocronometer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.vctapps.pomocronometer.service.OnChangeTime;
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

        pomodoro = new Pomodoro(this, clock, onChangeTime());

        Button bt = (Button) findViewById(R.id.start_stop);

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pomodoro == null) {
                    pomodoro = new Pomodoro(v.getContext(), clock, onChangeTime());
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
    protected void onPause(){
        pomodoro.onPause();
        super.onPause();
    }

    @Override
    protected void onResume(){
        pomodoro.onResume();
        super.onResume();
    }

    @Override
    protected void onStop(){
        pomodoro.onStop();
        super.onStop();
    }

    private OnChangeTime onChangeTime(){
        return new OnChangeTime() {
            @Override
            public void onPomo() {
                Log.d(LOG, "OnPomo() at activity was called.");
            }

            @Override
            public void onBreak() {
                Log.d(LOG, "OnBreak() at activity was called.");
            }
        };
    }
}
