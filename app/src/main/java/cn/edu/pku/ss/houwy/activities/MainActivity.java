package cn.edu.pku.ss.houwy.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

import cn.edu.pku.ss.houwy.app.MyApplication;
import cn.edu.pku.ss.houwy.bean.TodayWeather;
import cn.edu.pku.ss.houwy.popwindow.NewPopwindow;
import cn.edu.pku.ss.houwy.shihou.R;
import cn.edu.pku.ss.houwy.util.NetUtil;
import cn.edu.pku.ss.houwy.solarterms._24SolarTerms;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static final int UPDATE_TODAY_WEATHER = 1;

    private ImageView mUpdateBtn;
    //定义相关控件对象
    private TextView cityTv, humidityTv, windTv,climateTv,qualityTv,pm2_5Tv,temperatureTv;
    private ImageView climateImg,cityImg;

    //定义搜索按钮
    private ImageView mSearchBtn;
    //定义关注城市按钮
    private ImageView mLikesBtn;
    //定义未来天气按钮
    private ImageView mFutureBtn;
    //定义分享按钮
    private ImageView mShareBtn;
    private NewPopwindow mPopwindow;

    //定义relativelayout以实现背景切换
    private RelativeLayout RLbackground;

    //未来天气：最高最低温度
    private int[] future_high = {0,0,0,0,0,0};
    private int[] future_low = {0,0,0,0,0,0};

    //未来天气：日期，星期
    private String[] future_day = {"","","","","",""};
    private String[] future_weekday = {"","","","","",""};

    //未来天气：天气类型
    private String[] future_type = {"","","","","",""};
    //未来天气：城市
    private String future_city;

    private static MyApplication myApplication;
    //日历
    Calendar calendar = Calendar.getInstance();

    private Handler mHandler = new Handler() {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case UPDATE_TODAY_WEATHER:
                    updateTodayWeather((TodayWeather) msg.obj);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_info);
        mUpdateBtn = (ImageView) findViewById(R.id.navigation_update_btn);
        mUpdateBtn.setOnClickListener(this);
        mSearchBtn = (ImageView) findViewById(R.id.bottom_navigation_search);
        mSearchBtn.setOnClickListener(this);
        mLikesBtn = (ImageView) findViewById(R.id.bottom_navigation_likes);
        mLikesBtn.setOnClickListener(this);
        mFutureBtn = (ImageView) findViewById(R.id.bottom_navigation_future);
        mFutureBtn.setOnClickListener(this);
        mSearchBtn = (ImageView) findViewById(R.id.navigation_share_btn);
        mSearchBtn.setOnClickListener(this);
        initView();
//
//        if(NetUtil.getNetworkState(this) != NetUtil.NETWORK_NONE){
//            Log.d("myWeather","网络ok");
//            Toast.makeText(MainActivity.this,"网络Ok",Toast.LENGTH_LONG).show();
//        }
//        else{
//            Log.d("myWeather","网络挂了");
//            Toast.makeText(MainActivity.this,"网络挂了",Toast.LENGTH_LONG).show();
//        }


    }

    @Override
    public void onClick(View view){
        if(view.getId() == R.id.navigation_update_btn){
            SharedPreferences sharedPreferences = getSharedPreferences("config",MODE_PRIVATE);
            String cityCode = sharedPreferences.getString("main_city_code","101010100");
            Log.d("myWeather",cityCode);
            if(NetUtil.getNetworkState(this) != NetUtil.NETWORK_NONE){
                Log.d("myWeather","网络ok");
                Toast.makeText(MainActivity.this,"网络Ok",Toast.LENGTH_LONG).show();
                queryWeatherCode(cityCode);
            }
            else{
                Log.d("myWeather","网络挂了");
                Toast.makeText(MainActivity.this,"网络挂了",Toast.LENGTH_LONG).show();
            }
        }
        else if(view.getId() == R.id.bottom_navigation_search){
            Intent intent = new Intent(this,CitySearchActivity.class);
            startActivityForResult(intent,1);
        }
        else if(view.getId() == R.id.bottom_navigation_future){
            //向未来天气页面传入当前城市数据
            Intent intent = new Intent(this,FutureWeather.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("future_high",future_high);
            bundle.putSerializable("future_low",future_low);
            bundle.putSerializable("future_type",future_type);
            bundle.putSerializable("future_day",future_day);
            bundle.putSerializable("future_weekday",future_weekday);
            bundle.putSerializable("city",future_city);
            intent.putExtra("bundle",bundle);
            startActivity(intent);
        }
        else if(view.getId() == R.id.bottom_navigation_likes){
            Intent intent = new Intent(this,CityLikes.class);
            startActivityForResult(intent,2);
        }
        else if(view.getId() == R.id.navigation_share_btn){
            mPopwindow = new NewPopwindow(MainActivity.this,itemsOnClick);
            mPopwindow.showAtLocation(view,Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        }
    }


    //（接收intent的Activity销毁后，会回调这个方法）
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){

        //获取CitySearchActivity页面传来的citycode
        if(requestCode == 1 && resultCode == RESULT_OK){
            String newCityCode = data.getStringExtra("new_city_code");
            if(NetUtil.getNetworkState(this) != NetUtil.NETWORK_NONE){
                Log.d("myWeather","网络ok");
                Toast.makeText(MainActivity.this,"网络Ok",Toast.LENGTH_LONG).show();
                queryWeatherCode(newCityCode);
            }
            else{
                Log.d("myWeather","网络挂了");
                Toast.makeText(MainActivity.this,"网络挂了",Toast.LENGTH_LONG).show();
            }
        }
        //如果是从CityLikes传来的intent，加载被点击的城市的信息
        if(requestCode == 2 && resultCode == RESULT_OK){
            String newCityName = data.getStringExtra("like_city_name");
            String newCityCode = myApplication.getInstance().getCityCode(newCityName);
            queryWeatherCode(newCityCode);
        }
    }

    public void initView(){
        cityTv = (TextView) findViewById(R.id.city_tv);
        humidityTv = (TextView) findViewById(R.id.humidity_tv);
        windTv = (TextView) findViewById(R.id.wind_tv);
        climateTv = (TextView) findViewById(R.id.climate_tv);
        climateImg = (ImageView) findViewById(R.id.climate_img);
        pm2_5Tv = (TextView) findViewById(R.id.pm2_5_tv);
        qualityTv = (TextView) findViewById(R.id.quality_tv);
        temperatureTv = (TextView) findViewById(R.id.temperature_tv);
        cityImg = (ImageView) findViewById(R.id.location_img);
        RLbackground = (RelativeLayout)findViewById(R.id.main_activity_xml);
        RLbackground.setBackgroundResource(R.drawable.food_bg_paomian);

        cityTv.setText("N/A");
        humidityTv.setText("N/A");
        windTv.setText("N/A");
        climateTv.setText("N/A");
        pm2_5Tv.setText("N/A");
        qualityTv.setText("N/A");
        temperatureTv.setText("N/A");
    }

    //根据天气类型，设置天气图标
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
    //根据节气匹配背景图片
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void setFoodBg(TodayWeather todayWeather){
        String[] solarterm = {"立春","雨水,","惊蛰","春分","清明","谷雨","立夏","小满","芒种","夏至",
        "小暑","大暑","立秋","处暑","白露","秋分","寒露","霜降","立冬","小雪","大雪","冬至","小寒","大寒"};
        //获取今日日期
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        //日期的格式：月份是两位数，月份可以是两位或一位，如051（5月1日），0511（5月11日）
        String date;
        if(month < 10){
            date = "0"+String.valueOf(month)+String.valueOf(day);
        }else
        {
            date = String.valueOf(month)+String.valueOf(day);
        }

        String solarName = _24SolarTerms.getSolatName(year,date); //今日节气
        int index = 0;
        Class aClass = R.drawable.class;
        String foodSrc = "";//背景图片路径

        //设置背景图片，若今天不是某个节气，则按温度匹配图片，若今天是某个节气，则匹配该节气的传统食物
        if(solarName == null){
            foodSrc = myApplication.getInstance().getBackgroundByTemperature(todayWeather.getTemperature());
        }else{
            for(String str:solarterm){
                if(solarName.equals(str)){
                    //属于该节气，设置背景图片，图片标号为index
                    foodSrc = "food_bg_"+index;
                    break;
                }
                index++;
            }
        }
        //设置背景图片
        int foodId = -1;
        try {
            Field fField = aClass.getField(foodSrc);
            Object mValue = fField.get(new Integer(0));
            foodId = (int)mValue;
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            Drawable drawable = getResources().getDrawable(foodId);
            RLbackground.setBackground(drawable);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void updateTodayWeather(TodayWeather todayWeather){
        cityTv.setText(todayWeather.getCity());
        humidityTv.setText(todayWeather.getHumidity());
        windTv.setText(todayWeather.getWind());
        climateTv.setText(todayWeather.getClimate());
        pm2_5Tv.setText(todayWeather.getPm2_5());
        qualityTv.setText(todayWeather.getQuality());
        temperatureTv.setText(todayWeather.getTemperature());
        setWeatherImg(todayWeather.getClimate(),climateImg);
        //更新省份地图
        String provinceSrc = "p_" + todayWeather.getCityCode().substring(0,5);
        Class aClass = R.drawable.class;
        int provinceId = -1;
        try {
            Field field = aClass.getField(provinceSrc);
            Object value = field.get(new Integer(0));
            provinceId = (int)value;

        } catch (Exception e) {
            if(provinceId == -1){
               cityImg.setImageResource(R.drawable.p_10101);
            }
            //e.printStackTrace();

        }finally {
            Drawable drawable = getResources().getDrawable(provinceId);
            cityImg.setImageDrawable(drawable);
        }
        //根据温度更新背景食物图片
        setFoodBg(todayWeather);
        Toast.makeText(MainActivity.this,"更新成功！",Toast.LENGTH_SHORT).show();
    }

    public void queryWeatherCode(final String cityCode) {
        final String address = "http://wthrcdn.etouch.cn/WeatherApi?citykey=" + cityCode;
        Log.d("myWeather", address);
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection con = null;
                TodayWeather todayWeather = null;
                try {
                    URL url = new URL(address);
                    con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("GET");
                    con.setConnectTimeout(8000);
                    con.setReadTimeout(8000);
                    InputStream in = con.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String str;
                    while ((str = reader.readLine()) != null) {
                        response.append(str);
                        Log.d("myWeather", str);
                    }
                    String responseStr = response.toString();
                    Log.d("myWeather", responseStr);
                    todayWeather = NetUtil.parseXML(responseStr);
                    //向todayWeather中传入citycode
                    todayWeather.setCityCode(cityCode);
                    //从todayWeather中得到未来天气信息
                    future_high = todayWeather.getFuture_high();
                    future_low = todayWeather.getFuture_low();
                    future_type = todayWeather.getFuture_type();
                    future_weekday = todayWeather.getFuture_weekday();
                    future_day = todayWeather.getFuture_day();
                    future_city = todayWeather.getCity();
                    if(todayWeather != null){
                        Message msg = new Message();
                        msg.what = MainActivity.UPDATE_TODAY_WEATHER;
                        msg.obj = todayWeather;
                        mHandler.sendMessage(msg);//成功后数据传给UI线程
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (con != null) {
                        con.disconnect();
                    }
                }
            }
        }).start();
    }

    //为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {

        public void onClick(View v) {
            mPopwindow.dismiss();
            mPopwindow.backgroundAlpha(MainActivity.this, 1f);
            switch (v.getId()) {
                case R.id.weixin:
                    Toast.makeText(MainActivity.this, "微信", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.weibo:
                    Toast.makeText(MainActivity.this, "微博", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.qq:
                    Toast.makeText(MainActivity.this, "QQ", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }

    };

}
