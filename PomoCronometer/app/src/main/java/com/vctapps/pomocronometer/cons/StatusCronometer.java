package com.vctapps.pomocronometer.cons;

/**
 * Created by formadoresdosaber on 01/02/16.
 */
public enum StatusCronometer {

    Started(1),
    Stoped(2);

    private int status;

    StatusCronometer(int status){
        this.status = status;
    }

    public int getStatus(){
        return this.status;
    }
}
