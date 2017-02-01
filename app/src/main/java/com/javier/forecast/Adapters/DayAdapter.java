package com.javier.forecast.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.javier.forecast.R;
import com.javier.forecast.UI.MainActivity;
import com.javier.forecast.Wheather.Dayrly;

/**
 * Created by javi0 on 26/01/2017.
 */

public  class DayAdapter extends BaseAdapter{
    private Context context;
    private Dayrly[] days;
    public static final String TAG = MainActivity.class.getSimpleName();
    public DayAdapter(Context context, Dayrly[] days) {
        this.context = context;
        this.days = days;
    }

    @Override
    public int getCount() {
        return days.length;
    }

    @Override
    public Object getItem(int position) {
        return days[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.dayly_list_item,null);
            holder = new ViewHolder();
            holder.icon = (ImageView) convertView.findViewById(R.id.iconImageView);
            holder.day= (TextView) convertView.findViewById(R.id.dayNameLabel);
            holder.temp= (TextView) convertView.findViewById(R.id.temperatureLabel);


            convertView.setTag(holder);

        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.temp.setText(""+days[position].getCelsiustemp());
        if (position == 0) holder.day.setText("Hoy");
        else if (position == 1) holder.day.setText("Mañana");
        else holder.day.setText(days[position].getDay());
        holder.icon.setImageResource(days[position].getIconId());

        return convertView;
    }

    private class ViewHolder {
        ImageView icon;
        TextView temp,day;
    }
}
