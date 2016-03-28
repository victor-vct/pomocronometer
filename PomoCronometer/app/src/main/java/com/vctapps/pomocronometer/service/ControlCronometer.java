package com.vctapps.pomocronometer.service;

import android.widget.TextView;

/**
 * Created by victor vieira on 01/02/16.
 */
public interface ControlCronometer {

    /** Método para configurar o TextView do cronometro

     *   @return void*/
    void setClock(TextView clock);
    /** Método que inicia o cronometro

     *   @return void*/
    void start();
    /** Método que para o cronometro

     *   @return void*/
    void stop();
    /** Método que verifica se o cronometro esta´rodando

     *   @return boolean - true: rodando | false: parado*/
    boolean isStarted();
}
