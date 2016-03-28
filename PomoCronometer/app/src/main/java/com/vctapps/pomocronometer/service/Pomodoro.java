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

    private Intent intent_service; //intent para iniciar o service do cronometro
    private Context context; //contexto da app
    private TextView clock; //TextView que representa o relógio
    private static final String LOG = "Pomodoro"; //String para debug
    private ControlCronometer controlCronometer; //Interface para verificar o ciclo pomo
    private OnChangeTime onChangeTime; //callback chamado quando acaba um ciclo
    private boolean onBind = false; //verifica se está conectado ao service
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            PomoClock.CronoBinder conexao = (PomoClock.CronoBinder) service;
            controlCronometer = conexao.getInterface();
            if(clock != null){
                controlCronometer.setClock(clock);
            }
            if(onChangeTime != null){
                conexao.setOnChangeTime(onChangeTime);
            }
            Log.d(LOG, "Conectado ao service");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            controlCronometer = null;
        }
    };

    /** Método construtor
     * Params:
     * context - contexto da aplicação
     * clock - TextView que representa o relógio
     * onChangeTime - Callback chamado quando há alteração de ciclo
     */
    public Pomodoro(Context context, TextView clock, OnChangeTime onChangeTime){
        this.context = context;
        this.clock = clock;
        intent_service = new Intent(context, PomoClock.class);
        this.onChangeTime = onChangeTime;
        serviceStart();
        serviceBind();
    }

    /** Método que inicia o service do cronometro

     *   @return void*/
    private void serviceStart() {
        context.startService(intent_service);
        Log.d(LOG, "Started service.");
    }

    /** Método que faz o bind ao service

     *   @return void*/
    private void serviceBind() {
        if(!onBind) {
            context.bindService(intent_service, connection, Context.BIND_AUTO_CREATE);
            onBind = true;
            Log.d(LOG, "Bind on service.");
        }
    }

    /** Método que faz o unbind no service
     *
     *   @return void*/
    private void serviceUnbind(){
        if(onBind) {
            context.unbindService(connection);
            onBind = false;
            Log.d(LOG, "unbind service.");
        }
    }

    /** Método para configurar o TextView do cronometro

     *   @return void*/
    @Override
    public void setClock(TextView clock) {

    }

    /** Método que inicia o cronometro

     *   @return void*/
    @Override
    public void start() {
        serviceBind();
        controlCronometer.start();
    }

    /** Método que para o cronometro

     *   @return void*/
    @Override
    public void stop() {
        controlCronometer.stop();
    }

    /** Método verifica se está iniciado ou não

     *   @return boolean - true: iniciado | false: parado*/
    @Override
    public boolean isStarted() {
        return controlCronometer.isStarted();
    }

    /** Método que chama o unbind.
     * Deve ser implementado no método onPause da activity/fragment

     *   @return void*/
    public void onPause(){
        serviceUnbind();
    }

    /** Método que chama o bind.
     * Deve ser implementado no método onResume da activity/fragment

     *   @return void*/
    public void onResume(){
        serviceBind();
    }

    /** Método que chama o unbind.
     * Deve ser implementado no método onStop da activity/fragment

     *   @return void*/
    public void onStop(){
        serviceUnbind();
    }
}
