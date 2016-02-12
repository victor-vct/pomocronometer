package com.vctapps.pomocronometer;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.vctapps.pomocronometer.service.Cronometer;

/**
 * Created by Victor on 04/02/2016.
 */

public class CronometerTest extends ApplicationTestCase<Application> {

    Cronometer cronometer;

    public CronometerTest() {
        super(Application.class);
        cronometer = new Cronometer(onFinish());
    }

    public void testCriaCronometer(){
        cronometer = new Cronometer(onFinish());

        assertNotNull(cronometer);
    }

    private Cronometer.OnFinish onFinish(){
        return new Cronometer.OnFinish() {
            @Override
            public void onFinishTime() {

            }
        };
    }

    public void testMethodStart(){
        cronometer.start();

        assertEquals(true, cronometer.isStarted());
    }

    public void testMethodStop(){
        cronometer.stop();

        assertEquals(false, cronometer.isStarted());
    }
}
