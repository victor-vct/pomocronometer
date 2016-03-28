package com.vctapps.pomocronometer.service;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.vctapps.pomocronometer.cons.StatusCronometer;

/**
 * Created by formadoresdosaber on 01/02/16.
 */
public class Cronometer implements ControlCronometer {

    private TextView clock; //relógio do cronometro
    private long lastTime = 0; //tempo que o cronometro deve rodar (25, 5 ou 30 min) em milliseconds
    private long cronometer = 0; //tempo atual do cronometro
    private StatusCronometer STATUS = StatusCronometer.Stoped; //enum para identificar status do cronometro
    private static final String CRONO = "Crono"; //String para debug
    private OnFinish onFinish; //interface callback
    private Async async; //Async onde roda o cronometro

    /** Interface callback para mudança de tempo ("mantenha o foco" para "faça uma pausa")*/
    public interface OnFinish{
        void onFinishTime();
    }

    /** Método construtor
     * params: OnFinish onFinish - callback que é chamando quando existe troca de tempo no cronometro
     * de "Mantenha foco" para "faça uma pausa"
     */
    public Cronometer(OnFinish onFinish){
        this.onFinish = onFinish;
    }

    /** Método que inicia o cronometro

     *   @return void*/
    @Override
    public void start() {
        STATUS = StatusCronometer.Started;
        Log.d(CRONO, "Cronometer started");
        runClock();
    }

    /** Método que para o cronometro

     *   @return void*/
    @Override
    public void stop() {
        STATUS = StatusCronometer.Stoped;
        Log.d(CRONO, "Cronometer stoped called");
        if(async != null) {
            async.cancel(true);
        }
    }

    /** Método para colocar o tempo no TextView que representar o cronometro

     *   @return void*/
    @Override
    public void setClock(TextView clock) {
        this.clock = clock;
    }

    /** Método para Verificar se o cronometro está rodando

     *   @return int - minutos atual*/
    @Override
    public boolean isStarted() {
        if(STATUS.equals(StatusCronometer.Started)){
            Log.d(CRONO, "isStarted == TRUE");
            return true;
        }
        Log.d(CRONO, "isStarted == FALSE");
        return false;
    }

    /** Método para configurar o tempo do cronometro
     * 25 min - Foco no trabalho
     * 5 min - Descanso
     * 30 min - Descanso
     *   @return void*/
    public void setTime(long timeInMillis){
        this.cronometer = timeInMillis;
    }

    /** Método para iniciar o AsyncTask do relógio

     *   @return void*/
    private void runClock(){
        if(STATUS == StatusCronometer.Started) {
            async = new Async();
            async.execute();
        }
    }

    /** Método para retornar os segundos
     *
     *   @return int - segundos atual*/
    private int getSeconds(){
        return (int) (cronometer / 1000) % 60 ;
    }

    /** Método para retornar os minutos
     *
     *   @return int - minutos atual*/
    private int getMinutes(){
        return (int) (cronometer / (1000*60) % 60);
    }

    /** Método para formatar o relógio: mm:ss

     *   @return String - Hora formatada*/
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

    private class Async extends AsyncTask<Void, String, Void>{

        @Override
        protected Void doInBackground(Void... params) {
            lastTime = System.currentTimeMillis();
            publishProgress(clockFormat(getMinutes(), getSeconds()));
            while (cronometer > 0) {
                if(STATUS == StatusCronometer.Stoped){
                    this.cancel(true);
                    Log.d(CRONO, "Cronometer stoped in while");
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
    }
}
