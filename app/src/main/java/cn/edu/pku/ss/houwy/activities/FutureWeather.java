package cn.edu.pku.ss.houwy.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cn.edu.pku.ss.houwy.chart.WeatherChartView;
import cn.edu.pku.ss.houwy.shihou.R;

public class FutureWeather extends AppCompatActivity {
    private int[] future_high = {0,0,0,0,0,0};
    private int[] future_low = {0,0,0,0,0,0};

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.future_weather);
        Intent intent = getIntent();
        future_low = (int[])intent.getBundleExtra("bundle").getSerializable("future_low");
        future_high = (int[])intent.getBundleExtra("bundle").getSerializable("future_high");
        WeatherChartView chartView = (WeatherChartView) findViewById(R.id.line_char);
        // set day
        chartView.setTempHigh(future_high);
        // set night
        chartView.setTempLow(future_low);
        chartView.invalidate();
    }
}
