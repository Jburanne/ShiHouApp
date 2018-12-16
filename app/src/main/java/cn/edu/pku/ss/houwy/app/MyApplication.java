package cn.edu.pku.ss.houwy.app;

import android.app.Application;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cn.edu.pku.ss.houwy.bean.City;
import cn.edu.pku.ss.houwy.db.CityDB;
import cn.edu.pku.ss.houwy.db.FoodDB;

public class MyApplication extends Application {
    private static final String TAG = "MyAPP";
    private static MyApplication mApplication;
    private CityDB mCityDB;
    private FoodDB mFoodDB;
    private List<City> mCityList;
    private List<City> mResultList;
    private String cityCode;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
        mCityDB = openCityDB();
        mFoodDB = openFoodDB();
        initCityList();
    }
    private void initCityList(){
        mCityList = new ArrayList<City>();
        new Thread(new Runnable() {
            @Override
            public void run() {
// TODO Auto-generated method stub
                prepareCityList();
               // prepareResultList();
            }
        }).start();
    }

    //获取所有的城市列表
    private boolean prepareCityList(){
        mCityList = mCityDB.getAllCity();
        int i = 0;
        for(City city:mCityList){
            i++;
            String cityName = city.getCity();
            String cityCode = city.getNumber();
            Log.d(TAG,cityCode+":"+cityName);
        }
        Log.d(TAG,"i="+i);
        return true;
    }

    //获取str模糊匹配后得到的城市列表
    public List<City> getResultList(String str){
        mResultList = mCityDB.getSearchResult(str);
        int i = 0;
        for(City city:mResultList){
            i++;
            String cityName = city.getCity();
            String cityCode = city.getNumber();
            Log.d(TAG,cityCode+":"+cityName);
        }
        return mResultList;
    }

    //获取城市cName的cityCode
    public String getCityCode(String cName){
        cityCode = mCityDB.getCitycodeByCityname(cName);
        return cityCode;
    }

    //根据温度在搜索到的食物里随机挑选一个进行展示
    public String getBackgroundByTemperature(String t){
        //得到所有匹配的背景图
        List<String> paths = mFoodDB.getFoodByTemperature(t);
        if(paths.size()!=0){
            //随机产生一个数
            int num = (int)(Math.random()*(paths.size()-1));
            Log.d("randomNum",String.valueOf(num));
            Log.d("size",String.valueOf(paths.size()));
            //返回对应的图片名称
            String selectedPath = paths.get(num);
            Log.d("src:",selectedPath);
            return selectedPath;
        }
        return "bg3";
    }


    public List<City> getCityList(){
        return mCityList;
    }
    public static MyApplication getInstance(){
        return mApplication;
    }

    private CityDB openCityDB() {
        String path = "/data"
                + Environment.getDataDirectory().getAbsolutePath
                ()
                + File.separator + getPackageName()
                + File.separator + "databases1"
                + File.separator
                + CityDB.CITY_DB_NAME;
        File db = new File(path);
        Log.d(TAG, path);
        if (!db.exists()) {
            String pathfolder = "/data"
                    + Environment.getDataDirectory().getAbsolutePath()
                    + File.separator + getPackageName()
                    + File.separator + "databases1"
                    + File.separator;
            File dirFirstFolder = new File(pathfolder);
            if (!dirFirstFolder.exists()) {
                dirFirstFolder.mkdirs();
                Log.i("MyApp", "mkdirs");
            }
            Log.i("MyApp", "db is not exists");
            try {
                InputStream is = getAssets().open("city.db");
                FileOutputStream fos = new FileOutputStream(db);
                int len = -1;
                byte[] buffer = new byte[1024];
                while ((len = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                    fos.flush();
                }
                fos.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(0);
            }

        }
        return new CityDB(this, path);
    }

    private FoodDB openFoodDB() {
        String path = "/data"
                + Environment.getDataDirectory().getAbsolutePath
                ()
                + File.separator + getPackageName()
                + File.separator + "databases1"
                + File.separator
                + FoodDB.FOOD_DB_NAME;
        File db = new File(path);
        Log.d(TAG, path);
        if (!db.exists()) {
            String pathfolder = "/data"
                    + Environment.getDataDirectory().getAbsolutePath()
                    + File.separator + getPackageName()
                    + File.separator + "databases1"
                    + File.separator;
            File dirFirstFolder = new File(pathfolder);
            if (!dirFirstFolder.exists()) {
                dirFirstFolder.mkdirs();
                Log.i("MyApp", "mkdirs");
            }
            Log.i("MyApp", "db is not exists");
            try {
                InputStream is = getAssets().open("chooseFood.db");
                FileOutputStream fos = new FileOutputStream(db);
                int len = -1;
                byte[] buffer = new byte[1024];
                while ((len = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                    fos.flush();
                }
                fos.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(0);
            }
        }
        return new FoodDB(this, path);
    }
}
