 package com.example.know_your_weather;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {

    //Creating my variables
    EditText city;
    Button  getWeather;
    TextView weather,weatherPre,temp,feels,max,min,actual,sunrise,sunset,riseTime,setTime;
    ProgressBar bar;

    //create dowW function which runs in the background thread to fetch the JSONObject data from the weather API and process it accordingly
    public class downW extends AsyncTask<String,Void,ArrayList<String>>
    {

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected ArrayList<String> doInBackground(String... url1) {
            String res  = "";
            URL url;
            HttpURLConnection httpURLConnection;
            try
            {
                url  =  new URL(url1[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(inputStream);
                int data  = reader.read();
                while(data != -1)
                {
                    char k = (char)data;
                    res+=k;
                    data = reader.read();
                }
                JSONObject goodData = new JSONObject(res);
                String weather  = goodData.getString("weather");
                JSONObject goodData2  = goodData.getJSONObject("sys");
                JSONObject gon = goodData.getJSONObject("main");
                Log.i("Weather",weather);
                ArrayList<String> strings = new ArrayList<String>();
                JSONArray goodArr = new JSONArray(weather);
                String a1 = "vishal ",a2 = "charan";
                for(int i = 0;i<goodArr.length();i++)
                {
                    Log.i("arr size", String.valueOf(goodArr.length()));
                     a1 =  goodArr.getJSONObject(i).getString("main");
                     a2 = goodArr.getJSONObject(i).getString("description");
                     strings.add(a1);
                     strings.add(a2);
                }

                //getting the time for sunrise, set and the timeZone too
                int sunrise  = goodData2.getInt("sunrise");
                int sunset   = goodData2.getInt("sunset");
                int timezone = goodData.getInt("timezone")*1000;

                //setting  the timeZone
                TimeZone tz = TimeZone.getDefault();
                String[] availableIDs = tz.getAvailableIDs(timezone);
                for(int i = 0; i < availableIDs.length; i++)
                { Log.i("Timezone"+String.valueOf(i),availableIDs[i]); }

                //working on the sunset, rise and on timezone too;
                Long sec1 = Long.valueOf(sunrise);
                Long sec2 = Long.valueOf(sunset);
                Date fox = new Date(sec1*1000L);
                Date wolf = new Date(sec2*1000L);
                SimpleDateFormat jdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
                jdf.setTimeZone(TimeZone.getTimeZone(availableIDs[0]));
                String sunriseDate = jdf.format(fox);
                String sunsetDate  = jdf.format(wolf);

                //round-off our double values to add beauty
                DecimalFormat numberFormat = new DecimalFormat("#.00");
                String realFeel = numberFormat.format(gon.getDouble("temp")-273.15)+"°C";
                String feel     = numberFormat.format(gon.getDouble("feels_like")-273.15)+"°C";
                String max      = numberFormat.format(gon.getDouble("temp_max")-273.15)+"°C";
                String min      = numberFormat.format(gon.getDouble("temp_min")-273.15)+"°C";

                //adding all the processed value into the arrayList
                strings.add(realFeel);
                strings.add(feel);
                strings.add(max);
                strings.add(min);
                strings.add(sunriseDate);
                strings.add(sunsetDate);
                for(int i = 0; i < strings.size(); i++) {
                    Log.i("strings"+String.valueOf(i),strings.get(i));
                }

                return strings;

            }catch (Exception e)
            {
                e.printStackTrace();
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                String exceptionAsString = sw.toString();
                ArrayList<String> error  = new ArrayList<String>();
                error.add(exceptionAsString);
                return error;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<String> s) {
            super.onPostExecute(s);



        }
    }

    public void appear()
    {
        bar.setVisibility(View.INVISIBLE);
        weather.setVisibility(View.VISIBLE);
        temp.setVisibility(View.VISIBLE);
        sunrise.setVisibility(View.VISIBLE);
        sunset.setVisibility(View.VISIBLE);
        weatherPre.setVisibility(View.VISIBLE);
        actual.setVisibility(View.VISIBLE);
        feels.setVisibility(View.VISIBLE);
        max.setVisibility(View.VISIBLE);
        min.setVisibility(View.VISIBLE);
        riseTime.setVisibility(View.VISIBLE);
        setTime.setVisibility(View.VISIBLE);

    }

    public void disappear()
    {
        bar.setVisibility(View.VISIBLE);
        weather.setVisibility(View.INVISIBLE);
        temp.setVisibility(View.INVISIBLE);
        sunrise.setVisibility(View.INVISIBLE);
        sunset.setVisibility(View.INVISIBLE);
        weatherPre.setVisibility(View.INVISIBLE);
        actual.setVisibility(View.INVISIBLE);
        feels.setVisibility(View.INVISIBLE);
        max.setVisibility(View.INVISIBLE);
        min.setVisibility(View.INVISIBLE);
        riseTime.setVisibility(View.INVISIBLE);
        setTime.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        city        = findViewById(R.id.edTxtCountry);

        weather     = findViewById(R.id.tVweather);
        weatherPre  = findViewById(R.id.tVWeatherPre);
        temp        = findViewById(R.id.tVTemp);
        feels       = findViewById(R.id.tVFeels);
        max         = findViewById(R.id.tvMax);
        min         = findViewById(R.id.tVMin);
        actual      = findViewById(R.id.tvActual);
        sunrise     = findViewById(R.id.tVSunrise);
        sunset      = findViewById(R.id.tVSunset);
        riseTime    = findViewById(R.id.tVRise);
        setTime     = findViewById(R.id.tVSet);
        bar         = findViewById(R.id.progressBar);

        getWeather  = findViewById(R.id.btnGetCity);

        getWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                disappear();
                downW wea  = new downW();
                String b = city.getText().toString().trim();
                if(!b.isEmpty()){
                try{
                    ArrayList<String> a =  wea.execute("https://api.openweathermap.org/data/2.5/weather?q="+b+"&appid=746848a01c94b9029e9f5fb9567b0f15").get();
                    if(a.size()<8)
                    {
                        bar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(),a.get(0),Toast.LENGTH_LONG).show();
                    }

                    else
                        {
                            appear();
                            weatherPre.setText(a.get(0)+"---> "+a.get(1));
                            actual.setText("Actual Temperature:"+" "+a.get(2));
                            feels.setText("Feels Like:"+" "+a.get(3));
                            max.setText("Max:"+" "+a.get(4));
                            min.setText("Min:"+" "+a.get(5));
                            riseTime.setText(a.get(6));
                            setTime.setText(a.get(7));
                        }




                }catch (Exception e )
                {
                    e.printStackTrace();}}
                else
                    {

                        city.requestFocus();
                        city.setError("Please Provide us With a City");
                    }

            }


        });




    }
}