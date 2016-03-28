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
    public void setStatus(boolean status){
        if(status){
            this.status = Started.getStatus();
        }else{
            this.status = Stoped.getStatus();
        }
    }
}
