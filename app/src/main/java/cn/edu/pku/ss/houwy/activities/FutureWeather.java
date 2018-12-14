package cn.edu.pku.ss.houwy.activities;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import cn.edu.pku.ss.houwy.chart.WeatherChartView;
import cn.edu.pku.ss.houwy.shihou.R;

public class FutureWeather extends AppCompatActivity {
    //最高温度，最低温度集合
    private int[] future_high;
    private int[] future_low;
    //日期集合
    private String[] future_day;
    //星期集合
    private String[] future_weekday;
    //天气类型集合
    private String[] future_type;

    //定义星期控件
    private TextView weekday_forthTv,weekday_fifthTv,weekday_sixthTv;
    //定义日期控件
    private TextView day_yesterdayTv,day_todayTv,day_tomorrowTv,day_forthTv,day_fifthTv,day_sixthTv;
    //定义天气类型控件
    private TextView type_yesterdayTv,type_todayTv,type_tomorrowTv,type_forthTv,type_fifthTv,type_sixthTv;
    //定义天气图标
    private ImageView type_yesterdayImg,type_todayImg,type_tomorrowImg,type_forthImg,type_fifthTmg,type_sixthImg;
    //定义城市控件
    private TextView cityTv;

    //城市名
    private String cityName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.future_weather);
        initViews();
        Intent intent = getIntent();
        future_low = (int[])intent.getBundleExtra("bundle").getSerializable("future_low");
        future_high = (int[])intent.getBundleExtra("bundle").getSerializable("future_high");
        WeatherChartView chartView = (WeatherChartView) findViewById(R.id.line_char);
        // set day
        chartView.setTempHigh(future_high);
        // set night
        chartView.setTempLow(future_low);
        chartView.invalidate();
        future_type = (String[])intent.getBundleExtra("bundle").getSerializable("future_type");
        future_day = (String[])intent.getBundleExtra("bundle").getSerializable("future_day");
        future_weekday = (String[])intent.getBundleExtra("bundle").getSerializable("future_weekday");
        cityName = (String)intent.getBundleExtra("bundle").getSerializable("city");

        updateContent();

    }

    public void updateContent(){
        cityTv.setText(cityName);
        if(future_weekday[3] != "")
            weekday_forthTv.setText("周"+future_weekday[3].substring(2,3));
        if(future_weekday[4] != "")
            weekday_fifthTv.setText("周"+future_weekday[4].substring(2,3));
        if(future_weekday[5] != "")
            weekday_sixthTv.setText("周"+future_weekday[5].substring(2,3));

        day_yesterdayTv.setText(future_day[0]);
        day_todayTv.setText(future_day[1]);
        day_tomorrowTv.setText(future_day[2]);
        day_forthTv.setText(future_day[3]);
        day_fifthTv.setText(future_day[4]);
        day_sixthTv.setText(future_day[5]);

        type_yesterdayTv.setText(future_type[0]);
        type_todayTv.setText(future_type[1]);
        type_tomorrowTv.setText(future_type[2]);
        type_forthTv.setText(future_type[3]);
        type_fifthTv.setText(future_type[4]);
        type_sixthTv.setText(future_type[5]);

        setWeatherImg(future_type[0],type_yesterdayImg);
        setWeatherImg(future_type[1],type_todayImg);
        setWeatherImg(future_type[2],type_tomorrowImg);
        setWeatherImg(future_type[3],type_forthImg);
        setWeatherImg(future_type[4],type_fifthTmg);
        setWeatherImg(future_type[5],type_sixthImg);

    }

    public void initViews(){
        weekday_forthTv = (TextView)findViewById(R.id.weekday_forthday);
        weekday_fifthTv = (TextView)findViewById(R.id.weekday_fifthday);
        weekday_sixthTv = (TextView)findViewById(R.id.weekday_sixthday);

        weekday_forthTv.setText("N/A");
        weekday_fifthTv.setText("N/A");
        weekday_sixthTv.setText("N/A");

        day_yesterdayTv = (TextView)findViewById(R.id.day_yesterday);
        day_todayTv = (TextView)findViewById(R.id.day_today);
        day_tomorrowTv = (TextView)findViewById(R.id.day_tomorrow);
        day_forthTv = (TextView)findViewById(R.id.day_forthday);
        day_fifthTv = (TextView)findViewById(R.id.day_fifthday);
        day_sixthTv = (TextView)findViewById(R.id.day_sixthday);

        day_yesterdayTv.setText("N/A");
        day_todayTv.setText("N/A");
        day_tomorrowTv.setText("N/A");
        day_forthTv.setText("N/A");
        day_fifthTv.setText("N/A");
        day_sixthTv.setText("N/A");

        type_yesterdayTv = (TextView)findViewById(R.id.type_yesterday);
        type_todayTv = (TextView)findViewById(R.id.type_today);
        type_tomorrowTv = (TextView)findViewById(R.id.type_tomorrow);
        type_forthTv = (TextView)findViewById(R.id.type_forthday);
        type_fifthTv = (TextView)findViewById(R.id.type_fifthday);
        type_sixthTv = (TextView)findViewById(R.id.type_sixthday);

        type_yesterdayTv.setText("N/A");
        type_todayTv.setText("N/A");
        type_tomorrowTv.setText("N/A");
        type_forthTv.setText("N/A");
        type_fifthTv.setText("N/A");
        type_sixthTv.setText("N/A");

        type_yesterdayImg = (ImageView) findViewById(R.id.typeImg_yesterday);
        type_todayImg = (ImageView)findViewById(R.id.typeImg_today);
        type_tomorrowImg = (ImageView)findViewById(R.id.typeImg_tomorrow);
        type_forthImg = (ImageView)findViewById(R.id.typeImg_forthday);
        type_fifthTmg = (ImageView)findViewById(R.id.typeImg_fifthday);
        type_sixthImg = (ImageView)findViewById(R.id.typeImg_sixthday);

        cityTv = (TextView)findViewById(R.id.future_weather_city);
        cityTv.setText("N/A");
    }

    public void setWeatherImg(String type,ImageView imgView){
        switch(type){
            case "暴雪":
                imgView.setImageResource(R.drawable.weather_baoxue);
                break;
            case "暴雨":
                imgView.setImageResource(R.drawable.weather_baoyu);
                break;
            case "大暴雨":
                imgView.setImageResource(R.drawable.weather_dabaoyu);
                break;
            case "大雪":
                imgView.setImageResource(R.drawable.weather_daxue);
                break;
            case "大雨":
                imgView.setImageResource(R.drawable.weather_dayu);
                break;
            case "多云":
                imgView.setImageResource(R.drawable.weather_duoyun);
                break;
            case "雷阵雨":
                imgView.setImageResource(R.drawable.weather_leizhenyu);
                break;
            case "雷阵雨冰雹":
                imgView.setImageResource(R.drawable.weather_leizhenyubingbao);
                break;
            case "沙尘暴":
                imgView.setImageResource(R.drawable.weather_shachenbao);
                break;
            case "特大暴雨":
                imgView.setImageResource(R.drawable.weather_tedabaoyu);
                break;
            case "雾":
                imgView.setImageResource(R.drawable.weather_wu);
                break;
            case "小雪":
                imgView.setImageResource(R.drawable.weather_xiaoxue);
                break;
            case "小雨":
                imgView.setImageResource(R.drawable.weather_xiaoyu);
                break;
            case "阴":
                imgView.setImageResource(R.drawable.weather_yin);
                break;
            case "雨加雪":
                imgView.setImageResource(R.drawable.weather_yujiaxue);
                break;
            case "阵雪":
                imgView.setImageResource(R.drawable.weather_zhenxue);
                break;
            case "阵雨":
                imgView.setImageResource(R.drawable.weather_zhenyu);
                break;
            case "中雪":
                imgView.setImageResource(R.drawable.weather_zhongxue);
                break;
            case "中雨":
                imgView.setImageResource(R.drawable.weather_zhongyu);
                break;
            case "晴":
                imgView.setImageResource(R.drawable.weather_qing);
                break;
            default:
                imgView.setImageResource(R.drawable.weather_null);
                break;

        }
    }
}
