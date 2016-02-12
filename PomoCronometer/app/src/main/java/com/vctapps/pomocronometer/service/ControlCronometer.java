package com.vctapps.pomocronometer.service;

import android.widget.TextView;

/**
 * Created by formadoresdosaber on 01/02/16.
 */
public interface ControlCronometer {

    void setClock(TextView clock);
    void start();
    void stop();
    boolean isStarted();
}
