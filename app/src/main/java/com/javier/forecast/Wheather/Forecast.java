package com.javier.forecast.Wheather;

/**
 * Created by javi0 on 02/01/2017.
 */

public class Forecast {
    private Current current;
    private Hourly[] hourly;
    private Dayrly[] dayrly;

    public Forecast() {
    }



    public Current getCurrent() {
        return current;
    }

    public void setCurrent(Current current) {
        this.current = current;
    }

    public Hourly[] getHourly() {
        return hourly;
    }

    public void setHourly(Hourly[] hourly) {
        this.hourly = hourly;
    }

    public Dayrly[] getDayrly() {
        return dayrly;
    }

    public void setDayrly(Dayrly[] dayrly) {
        this.dayrly = dayrly;
    }
}
