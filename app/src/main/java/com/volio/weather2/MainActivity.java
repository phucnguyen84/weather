package com.volio.weather2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.volio.model.WeatherResponse;
import com.volio.model.WeatherService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    public static String BaseUrl = "http://api.openweathermap.org/";
    public static String AppId = "eec3c6835e90cc84f98892e4f5f07739";
    public static String lat = "35";
    public static String lon = "139";
    private TextView txtWeather,txtLocation,txtClouds,txtWind,txtHumidity,txtTempMax,txtTempMin,txtC;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addControls();
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCurrentData();
            }
        });
    }

    private void addControls() {
        txtWeather = findViewById(R.id.txtWeather);
        txtLocation=findViewById(R.id.txtLocation);
        txtClouds=findViewById(R.id.txtClouds);
        txtWind=findViewById(R.id.txtWind);
        txtHumidity=findViewById(R.id.txtHumidity);
        txtTempMin=findViewById(R.id.txtTempMin);
        txtTempMax=findViewById(R.id.txtTempMax);
        txtC=findViewById(R.id.txtC);
    }

    void getCurrentData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        WeatherService service = retrofit.create(WeatherService.class);
        Call<WeatherResponse> call = service.getCurrentWeatherData(lat, lon, AppId);
        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(@NonNull Call<WeatherResponse> call, @NonNull Response<WeatherResponse> response) {
                if (response.code() == 200) {
                    WeatherResponse weatherResponse = response.body();
                    assert weatherResponse != null;
                    txtWeather.setText(weatherResponse.getMain().getTemp().toString());
                    txtLocation.setText(weatherResponse.getName()+", "+weatherResponse.getSys().getCountry());
                    txtClouds.setText(weatherResponse.getClouds().getAll()+"%");
                    txtWind.setText(weatherResponse.getWind().getSpeed()+"m/s");
                    txtHumidity.setText(weatherResponse.getMain().getHumidity()+"%");
                    txtTempMin.setText(weatherResponse.getMain().getTempMin().toString());
                    txtTempMax.setText(weatherResponse.getMain().getTempMax().toString());
                    txtC.setText("°C");
                }
            }

            @Override
            public void onFailure(@NonNull Call<WeatherResponse> call, @NonNull Throwable t) {
                txtLocation.setText(t.getMessage());
            }
        });
    }

}