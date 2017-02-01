package com.javier.forecast.Wheather;

import android.os.Parcel;
import android.os.Parcelable;

import com.javier.forecast.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

/**
 * Created by javi0 on 15/12/2016.
 */

public class Dayrly implements Parcelable{
    private String icon;
    private long time;
    private double temperature;
    private double humidity;
    private double precipChance;
    private String summary;
    private String timeZone;
    private HashMap<String, Integer> iconos = new HashMap<String, Integer>();


    public Dayrly() {
        cargarIconos();
    }




    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getPrecipChance() {
        return precipChance;
    }

    public void setPrecipChance(double precipChance) {
        this.precipChance = precipChance;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getDay() {
        SimpleDateFormat format = new SimpleDateFormat("EEEE");
        format.setTimeZone(TimeZone.getTimeZone(getTimeZone()));
        Date time = new java.util.Date(getTime() * 1000);
        return format.format(time);
    }


    public void cargarIconos() {
        iconos.put("clear-day", R.drawable.clear_day);
        iconos.put("clear-night", R.drawable.clear_night);
        iconos.put("rain", R.drawable.rain);
        iconos.put("snow", R.drawable.snow);
        iconos.put("sleet", R.drawable.sleet);
        iconos.put("wind", R.drawable.wind);
        iconos.put("fog", R.drawable.fog);
        iconos.put("cloudy", R.drawable.cloudy);
        iconos.put("partly-cloudy-day", R.drawable.partly_cloudy);
        iconos.put("partly-cloudy-night", R.drawable.cloudy_night);

    }

    public int getIconId() {
        int id;
        cargarIconos();
        String icon = getIcon();
        if (iconos.containsKey(icon)) {
            id = iconos.get(icon);
        } else {
            id = iconos.get("clear-day");
        }
        return id;
    }

    public int getCelsiustemp(){
        return (int) ((getTemperature()-32)*5/9);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(time);
        dest.writeString(summary);
        dest.writeString(icon);
        dest.writeString(timeZone);
        dest.writeDouble(temperature);
    }
    private Dayrly (Parcel in){
        time = in.readLong();
        summary = in.readString();
        icon = in.readString();
        timeZone = in.readString();
        temperature = in.readDouble();
    }

    public static final Creator<Dayrly> CREATOR = new Creator<Dayrly>() {
        @Override
        public Dayrly createFromParcel(Parcel in) {

            return new Dayrly(in);
        }

        @Override
        public Dayrly[] newArray(int size) {
            return new Dayrly[size];
        }
    };
}
