package com.javier.forecast.Cities;

/**
 * Created by javi0 on 20/12/2016.
 */

public class City {
    private String name;
    private String longitud;
    private String latitud;

    public City(String name, String longitud, String latitud) {
        this.name = name;
        this.longitud = longitud;
        this.latitud = latitud;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }
}
