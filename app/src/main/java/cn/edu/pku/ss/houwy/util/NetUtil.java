package cn.edu.pku.ss.houwy.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Message;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;

import cn.edu.pku.ss.houwy.activities.MainActivity;
import cn.edu.pku.ss.houwy.bean.TodayWeather;

/**
 * Created by Houwy
 */


public class NetUtil {
    public static final int NETWORK_NONE = 0;
    public static final int NETWORK_WIFI = 1;
    public static final int NETWORK_MOBILE = 2;

    public static int getNetworkState(Context context) {

        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
        if (networkInfo == null)
            return NETWORK_NONE;

        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_MOBILE)
            return NETWORK_MOBILE;
        else if (nType == ConnectivityManager.TYPE_WIFI)
            return NETWORK_WIFI;

        return NETWORK_NONE;
    }


    public static TodayWeather parseXML(String xmldata) {
        TodayWeather todayWeather = null;

        int dateCount = 1;
        int highCount = 1;
        int lowCount = 1;
        int typeCount = 1;
        int typeCount1 = 1;
        int flag = 1;
        String[] temp_weekday = new String[6];
        String[] temp_day = new String[6];
        int[] temp_high = new int[6];
        int[] temp_low = new int[6];
        String[] temp_type = new String[6];
        try {
            XmlPullParserFactory fac = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = fac.newPullParser();
            xmlPullParser.setInput(new StringReader(xmldata));
            int eventType = xmlPullParser.getEventType();
            Log.d("myWeather", "parseXML");
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    // 判断当前事件是否为文档开始事件
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    // 判断当前事件是否为标签元素开始事件
                    case XmlPullParser.START_TAG:
                        if (xmlPullParser.getName().equals("resp")) {
                            todayWeather = new TodayWeather();
                        }
                        if (todayWeather != null) {
                            if (xmlPullParser.getName().equals("city")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setCity(xmlPullParser.getText());
                                //Log.d("myWeather", "city: " + xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("shidu")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setHumidity(xmlPullParser.getText());
                                //Log.d("myWeather", "shidu: " + xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("wendu")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setTemperature(xmlPullParser.getText());
                                //Log.d("myWeather", "wendu: " + xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("pm25")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setPm2_5(xmlPullParser.getText());
                                //Log.d("myWeather", "pm25: " + xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("quality")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setQuality(xmlPullParser.getText());
                                //Log.d("myWeather", "quality: " + xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("fengxiang")) {
                                eventType = xmlPullParser.next();
                                //Log.d("myWeather", "fengxiang: " + xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("fengli")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setWind(xmlPullParser.getText());
                                //Log.d("myWeather", "fengli: " + xmlPullParser.getText());
                            }else if(xmlPullParser.getName().equals("date_1")){
                                eventType = xmlPullParser.next();
                                //存入昨天的日期信息
                                String temp_date;
                                int length;
                                temp_date = xmlPullParser.getText();
                                length = temp_date.length();
                                temp_day[0] = temp_date.substring(0,length-3);
                                temp_weekday[0] = temp_date.substring(length-3,length);
                            }
                            else if (xmlPullParser.getName().equals("date")) {
                                eventType = xmlPullParser.next();
                                String temp_date;
                                int length;
                                temp_date = xmlPullParser.getText();
                                length = temp_date.length();
                                temp_day[dateCount] = temp_date.substring(0,length-3);
                                temp_weekday[dateCount] = temp_date.substring(length-3,length);
                                //如果是今天的信息，则存入今日信息的属性中
                                if(dateCount == 1){
                                    todayWeather.setDay(temp_day[1]);
                                    todayWeather.setWeekday(temp_weekday[1]);
                                }
                                dateCount++;
                            } else if(xmlPullParser.getName().equals("high_1")){
                                eventType = xmlPullParser.next();
                                //存入昨天的最高温度
                                String temp = xmlPullParser.getText().split(" ")[1];
                                temp_high[0] = Integer.parseInt(temp.substring(0,temp.length()-1));
                            } else if (xmlPullParser.getName().equals("high")) {
                                eventType = xmlPullParser.next();
                                //如果是今天的信息，则存入今日信息的属性中
                                if(highCount == 1){
                                    todayWeather.setTem_high(xmlPullParser.getText().split(" ")[1]);
                                }
                                String temp = xmlPullParser.getText().split(" ")[1];
                                temp_high[highCount] = Integer.parseInt(temp.substring(0,temp.length()-1));
                                highCount++;
                            }else if(xmlPullParser.getName().equals("low_1")){
                                eventType = xmlPullParser.next();
                                //存入昨天的最高温度
                                String temp = xmlPullParser.getText().split(" ")[1];
                                temp_low[0] = Integer.parseInt(temp.substring(0,temp.length()-1));
                            } else if (xmlPullParser.getName().equals("low")) {
                                eventType = xmlPullParser.next();
                                //如果是今天的信息，则存入今日信息的属性中
                                if(lowCount == 1){
                                    todayWeather.setTem_low(xmlPullParser.getText().split(" ")[1]);
                                }
                                String temp = xmlPullParser.getText().split(" ")[1];
                                temp_low[lowCount] = Integer.parseInt(temp.substring(0,temp.length()-1));
                                lowCount++;
                            } else if(xmlPullParser.getName().equals("type_1")){
                                eventType = xmlPullParser.next();
                                //存入昨天的天气类型
                                temp_type[0] = xmlPullParser.getText();
                            } else if (xmlPullParser.getName().equals("type")) {
                                eventType = xmlPullParser.next();
                                if(flag == 1){
                                    //如果是今天的信息，则存入今日信息的属性中
                                    if(typeCount == 1){
                                        todayWeather.setClimate(xmlPullParser.getText());
                                    }
                                    temp_type[typeCount] = xmlPullParser.getText();
                                }
                                typeCount1++;
                                if(typeCount1%2 == 0){
                                    flag = 0;
                                }else{
                                    flag = 1;
                                    typeCount++;
                                }
                            }
                            todayWeather.setFuture_day(temp_day);
                            todayWeather.setFuture_high(temp_high);
                            todayWeather.setFuture_low(temp_low);
                            todayWeather.setFuture_type(temp_type);
                            todayWeather.setFuture_weekday(temp_weekday);
                        }
                        break;
                    // 判断当前事件是否为标签元素结束事件
                    case XmlPullParser.END_TAG:
                        break;
                }
                // 进入下一个元素并触发相应事件
                eventType = xmlPullParser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return todayWeather;
    }




}
