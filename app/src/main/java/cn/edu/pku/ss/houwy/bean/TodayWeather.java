package cn.edu.pku.ss.houwy.bean;

public class TodayWeather {
    private String city;
    private String humidity;
    private String wind;
    private String climate;
    private String quality;
    private String pm2_5;
    private String temperature;
    private String tem_low;
    private String tem_high;
    private String cityCode;
    private String day;
    private String weekday;

    //六天（昨天，今天，未来五天）天气类型
    private String[] future_type;
    //六天最高温度
    private int[] future_high;
    //六天最低温度
    private int[] future_low;
    //六天（星期几）
    private String[] future_weekday;
    //六天（几号）
    private String[] future_day;

    public TodayWeather(){
        //初始化长度
        future_day = new String[6];
        future_high = new int[6];
        future_low = new int[6];
        future_type = new String[6];
        future_weekday = new String[6];
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getWeekday() {
        return weekday;
    }

    public void setWeekday(String weekday) {
        this.weekday = weekday;
    }

    public String[] getFuture_type() {
        return future_type;
    }

    public void setFuture_type(String[] future_type) {
        this.future_type = future_type;
    }

    public int[] getFuture_high() {
        return future_high;
    }

    public void setFuture_high(int[] future_high) {
        this.future_high = future_high;
    }

    public int[] getFuture_low() {
        return future_low;
    }

    public void setFuture_low(int[] future_low) {
        this.future_low = future_low;
    }

    public String[] getFuture_weekday() {
        return future_weekday;
    }

    public void setFuture_weekday(String[] future_weekday) {
        this.future_weekday = future_weekday;
    }

    public String[] getFuture_day() {
        return future_day;
    }

    public void setFuture_day(String[] future_day) {
        this.future_day = future_day;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    public String getClimate() {
        return climate;
    }

    public void setClimate(String climate) {
        this.climate = climate;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public String getPm2_5() {
        return pm2_5;
    }

    public void setPm2_5(String pm2_5) {
        this.pm2_5 = pm2_5;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getTem_low() {
        return tem_low;
    }

    public void setTem_low(String tem_low) {
        this.tem_low = tem_low;
    }

    public String getTem_high() {
        return tem_high;
    }

    public void setTem_high(String tem_high) {
        this.tem_high = tem_high;
    }

}
