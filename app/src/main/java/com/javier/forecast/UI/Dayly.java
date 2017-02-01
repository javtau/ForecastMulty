package com.javier.forecast.UI;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.ListView;

import com.javier.forecast.Adapters.DayAdapter;
import com.javier.forecast.R;
import com.javier.forecast.Wheather.Dayrly;

import java.util.Arrays;

public class Dayly extends ListActivity {
    private Dayrly[] days;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dayly);
        Intent intent = getIntent();
        Parcelable[] result =  intent.getParcelableArrayExtra(MainActivity.DAILY_FORECAST);

        days = Arrays.copyOf(result,result.length,Dayrly[].class);
        DayAdapter adapter = new DayAdapter(this,days);
        setListAdapter(adapter);
    }
}
