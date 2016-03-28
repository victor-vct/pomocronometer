package com.vctapps.pomocronometer.service;

/**
 * Created by Victor on 15/02/2016.
 */
public interface OnChangeTime{
    /** Método para ser chamado quando iniciar um ciclo pomo
     *   @return void*/
    void onPomo();
    /** Método para ser chamado quando iniciar um ciclo de pausa
     *   @return void*/
    void onBreak();
}
