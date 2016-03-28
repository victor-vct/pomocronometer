package com.vctapps.pomocronometer.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.TextView;

import com.vctapps.pomocronometer.cons.CurrentPomo;

/**
 * Created by Victor on 02/02/2016.
 */
public class PomoClock extends Service implements ControlCronometer {

    private Cronometer cronometer; //cronometro
    private CurrentPomo currentPomo = CurrentPomo.First; //Ciclo atual
    private static final String LOG = "Cycle"; //String para debug
    private boolean nextBreak = false; //Boolean para verificar se está em ciclo de trabalho ou de pausa
    private final long longBreakTime = 1000 * 60 * 30; //pausa longa
    private final long breakTime = 1000 * 60 * 5; //pausa curta
    private final long pomoTime = 1000 * 60 * 25; //tempo de trabalho
    private static OnChangeTime onChangeTime; //callback para quando acabar um ciclo

    /** Método construtor*/
    public PomoClock(){
        cronometer = new Cronometer(onFinish());
    }

    /** Inner class responsável por fazer o binder ao service caso já esteja rodando*/
    public class CronoBinder extends Binder {
        public ControlCronometer getInterface(){
            return PomoClock.this;
        }

        public void setOnChangeTime(OnChangeTime onChangeTime){
            PomoClock.onChangeTime = onChangeTime;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new CronoBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        return START_STICKY;
    }

    /** Método que inicia o cronometro

     *   @return void*/
    @Override
    public void start() {
        if(cronometer != null){
            if(!cronometer.isStarted()) {
                nextPomo();
            }
        }
    }

    /** Método para configurar o TextView do cronometro

     *   @return void*/
    @Override
    public void setClock(TextView clock) {
        cronometer.setClock(clock);
    }

    /** Método que para o cronometro

     *   @return void*/
    @Override
    public void stop() {
        if(cronometer != null){
            if(cronometer.isStarted()){
                cronometer.stop();
                Log.d(LOG, "stop was called");
                reset();
            }
        }
    }

    /** Método que verifica se o cronometro esta´rodando

     *   @return boolean - true: rodando | false: parado*/
    @Override
    public boolean isStarted() {
        return cronometer.isStarted();
    }

    /** Método chamado quando acaba um ciclo e responsável por verificar qual o próximo, configurar o cronometro e iniciar

     *   @return void*/
    private void nextPomo(){
        Log.d(LOG, "NextPomo called, current pomo is: " + currentPomo);
        cronometer.stop();
        switch (currentPomo){
            case First:
                if(nextBreak){
                    cronometer.setTime(breakTime);
                    onChangeTime.onBreak();
                    currentPomo = CurrentPomo.Second;
                    Log.d(LOG, "First break start");
                }else {
                    cronometer.setTime(pomoTime);
                    onChangeTime.onPomo();
                    Log.d(LOG, "First pomo start");
                }
                nextBreak = !nextBreak;
                cronometer.start();
                break;
            case Second:
                if(nextBreak){
                    cronometer.setTime(breakTime);
                    onChangeTime.onBreak();
                    Log.d(LOG, "Second break start");
                    currentPomo = CurrentPomo.Third;
                }else {
                    cronometer.setTime(pomoTime);
                    onChangeTime.onPomo();
                    Log.d(LOG, "Second pomo start");
                }
                nextBreak = !nextBreak;
                cronometer.start();
                break;
            case Third:
                if(nextBreak){
                    cronometer.setTime(breakTime);
                    onChangeTime.onBreak();
                    currentPomo = CurrentPomo.Fourt;
                    Log.d(LOG, "Third break start");
                }else {
                    cronometer.setTime(pomoTime);
                    onChangeTime.onPomo();
                    Log.d(LOG, "Third pomo start");
                }
                nextBreak = !nextBreak;
                cronometer.start();
                break;
            case Fourt:
                if(nextBreak){
                    cronometer.setTime(longBreakTime);
                    onChangeTime.onBreak();
                    currentPomo = CurrentPomo.First;
                    Log.d(LOG, "Fourt break start");
                }else {
                    cronometer.setTime(pomoTime);
                    onChangeTime.onPomo();
                    Log.d(LOG, "Fourt pomo start");
                }
                nextBreak = !nextBreak;
                cronometer.start();
                break;
        }
    }

    /** Método que retorna um callback chamar o proximo ciclo

     *   @return Cronometer.Onfinish*/
    private Cronometer.OnFinish onFinish(){
        return new Cronometer.OnFinish() {
            @Override
            public void onFinishTime() {
                nextPomo();
            }
        };
    }

    /** Método para zerar as configurações

     *   @return void*/
    private void reset(){
        currentPomo = CurrentPomo.First;
        nextBreak = false;
    }


}
