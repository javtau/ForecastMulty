package com.javier.forecast.UI;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.javier.forecast.Cities.Cities;
import com.javier.forecast.Cities.City;
import com.javier.forecast.Wheather.Current;
import com.javier.forecast.R;
import com.javier.forecast.Wheather.Dayrly;
import com.javier.forecast.Wheather.Forecast;
import com.javier.forecast.Wheather.Hourly;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    final static String KEY = "7b69e457fa16a2aada4b55c08a241a6c";
    final static String DAILY_FORECAST = "dayly_forecast";
    //final static String LONGITUD = "40.5358";
    //final static String LATITUD = "-3.61661";
    public static final String TAG = MainActivity.class.getSimpleName();
    private TextView txt_timezone;
    private TextView txt_time;
    private TextView txt_temperature;
    private TextView txt_humidity;
    private TextView txt_precipity;
    private TextView txt_summary;
    private ImageView img_icon;
    private ImageView img_refresh;
    private Spinner sp_cities;
    private ProgressBar pg_refresco;
    private int selected = 0;
    Forecast forecast = new Forecast();

    Cities cities = new Cities();
    String[] names;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txt_timezone = (TextView) findViewById(R.id.txt_timezone);
        txt_time = (TextView) findViewById(R.id.txt_time);
        txt_temperature = (TextView) findViewById(R.id.txt_temperature);
        txt_humidity = (TextView) findViewById(R.id.txt_humidity);
        txt_precipity = (TextView) findViewById(R.id.txt_precipity);
        txt_summary = (TextView) findViewById(R.id.txt_summary);
        img_icon = (ImageView) findViewById(R.id.img_icon);
        img_refresh = (ImageView) findViewById(R.id.img_refresh);
        sp_cities = (Spinner) findViewById(R.id.sp_cities);
        pg_refresco = (ProgressBar) findViewById(R.id.pg_refresco);
        pg_refresco.setVisibility(View.INVISIBLE);
        names = new String[cities.getCities().length];
        for (int i = 0; i<names.length;i++){
            names[i] = cities.getCities()[i].getName();
        }
        ArrayAdapter <String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,names);
        sp_cities.setAdapter(adapter);

        consult(cities.getCities()[selected]);
        sp_cities.setOnItemSelectedListener(this);
    }

    private void consult(City ciudad) {
        String forecastURL = "https://api.darksky.net/forecast/" + KEY + "/" + ciudad.getLongitud() + "," + ciudad.getLatitud();
        if (isNetworkAvailable()) {
            showProgresBar();
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(forecastURL).build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dontshowProgresBar();
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        //Response response = call.execute();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dontshowProgresBar();
                            }
                        });
                        String result = response.body().string();
                        Log.v(TAG, result);

                        if (response.isSuccessful()) {


                            forecast = parseForecastdetails(result);
                            mostrarDatos();
                            String timezone = forecast.getCurrent().getTimeZone();
                            Log.i(TAG, "Timezone: "+ timezone);
                            Log.i(TAG, "time: "+ forecast.getCurrent().getStringTime());
                            Log.i(TAG, "temperature: "+ forecast.getCurrent().getCelsiustemp());
                            Log.i(TAG, "humidity: "+ forecast.getCurrent().getHumidity());
                            Log.i(TAG, "lluvias: "+ forecast.getCurrent().getPrecipChance());
                            Log.i(TAG, "sumario: "+ forecast.getCurrent().getSummary());
                            Log.i(TAG, "icono: "+ forecast.getCurrent().getIcon());
                        } else {
                            alerUserAboutError();
                        }

                    } catch (IOException | JSONException ex ) {
                        Log.e(TAG, "Excepcion: ", ex);

                    }
                }
            });

        } else {
            Toast.makeText(this,"Error de conexion",Toast.LENGTH_SHORT).show();
        }
    }




    private void alerUserAboutError() {
        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.show(getFragmentManager(), "Error dialog:");
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }
        return isAvailable;
    }

    private Forecast parseForecastdetails(String jsondata) throws JSONException{
        Forecast forecast = new Forecast();
        forecast.setCurrent(getCurrentDetails(jsondata));
        forecast.setDayrly(getDailyForecast(jsondata));
        forecast.setHourly(getHourlyForecast(jsondata));


        return forecast;
    }

    private Hourly[] getHourlyForecast(String jsondata) throws JSONException {
        JSONObject json = new JSONObject(jsondata);
        String timezone = json.getString("timezone");
        JSONObject forecast = json.getJSONObject("hourly");
        JSONArray data = forecast.getJSONArray("data");
        Hourly hour[] = new Hourly[data.length()];
        for (int i = 0; i < data.length(); i++) {
            JSONObject hours = data.getJSONObject(i);
            //obtemos del json los datos deseados y los almacenamos que las diferentes variables que
            //posteriormente se mostraran en forma de log
            hour[i] = new Hourly();
            hour[i].cargarIconos();
            //extraccion de los datos solicitados
            hour[i] .setTime(hours.getLong("time"));
            hour[i].setTimeZone(timezone);
            hour[i] .setTemperature(hours.getDouble("temperature"));
            hour[i] .setHumidity(hours.getDouble("humidity")*100);
            hour[i] .setPrecipChance(hours.getDouble("precipProbability")*100);
            hour[i] .setSummary(hours.getString("summary"));
            hour[i] .setIcon(hours.getString("icon"));
        }
        return hour;
    }


    private Dayrly[] getDailyForecast(String jsondata) throws JSONException {
        JSONObject json = new JSONObject(jsondata);
        String timezone = json.getString("timezone");
        JSONObject forecast = json.getJSONObject("daily");
        JSONArray data = forecast.getJSONArray("data");
        Dayrly[] day = new Dayrly[data.length()];

        for (int i = 0; i < data.length(); i++) {
            JSONObject days = data.getJSONObject(i);
            //obtemos del json los datos deseados y los almacenamos que las diferentes variables que
            //posteriormente se mostraran en forma de log
            day[i] = new Dayrly();
            day[i].cargarIconos();
            //extraccion de los datos solicitados
            day[i] .setTime(days.getLong("time"));
            day[i].setTimeZone(timezone);
            day[i] .setTemperature(days.getDouble("temperatureMax"));
            day[i] .setHumidity(days.getDouble("humidity")*100);
            day[i] .setPrecipChance(days.getDouble("precipProbability")*100);
            day[i] .setSummary(days.getString("summary"));
            day[i] .setIcon(days.getString("icon"));
        }
        return day;
    }

    private Current getCurrentDetails(String result) throws JSONException {
        //Creación de un objeto Current donde vayamos almacendo uno por uno los valores que nos interesan
        Current c = new Current();
        c.cargarIconos();
        //creación del objeto JSON que almacene objeto  raíz.
        JSONObject forecast = new JSONObject(result);

        c.setTimeZone(forecast.getString("timezone"));
        Log.i(TAG, "Obtenido desde JSON: "+ forecast.getString("timezone"));
        //creación del objeto JSON que almacene el objeto currently definido dentro de la raíz.
        JSONObject actual = forecast.getJSONObject("currently");
        //extraccion de los datos solicitados
        c.setTime(actual.getLong("time"));

        c.setTemperature(actual.getDouble("temperature"));
        c.setHumidity(actual.getDouble("humidity")*100);
        c.setPrecipChance(actual.getDouble("precipProbability")*100);
        c.setSummary(actual.getString("summary"));
        c.setIcon(actual.getString("icon"));

        return c;
    }
    public void mostrarDatos(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Current current = forecast.getCurrent();
                String timezone = current.getTimeZone();
                txt_timezone.setText("Zona horaria: "+timezone);
                txt_time.setText("Hora actual: "+ current.getStringTime());
                txt_temperature.setText("Temperatura: "+ current.getCelsiustemp()+"ºC");
                txt_humidity.setText("humedad: "+(int) current.getHumidity()+" %");
                txt_precipity.setText("Probabilidad de lluvias: "+(int) current.getPrecipChance()+"%");
                txt_summary.setText("Sumario: "+ current.getSummary());
                img_icon.setImageResource(current.getIconId());
            }
        });

    }

    public void refresh(View v){
        consult(cities.getCities()[selected]);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selected = position;
        consult(cities.getCities()[position]);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    public void showProgresBar(){
        img_refresh.setVisibility(View.INVISIBLE);
        pg_refresco.setVisibility(View.VISIBLE);
    }
    public void dontshowProgresBar(){
        img_refresh.setVisibility(View.VISIBLE);
        pg_refresco.setVisibility(View.INVISIBLE);
    }

    public void ShowDayly(View v){
        Intent intent = new Intent(this, Dayly.class);
        intent.putExtra(DAILY_FORECAST,forecast.getDayrly());
        startActivity(intent);
    }
}
