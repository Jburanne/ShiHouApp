package cn.edu.pku.ss.houwy.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class FoodDB {
    public static final String FOOD_DB_NAME = "chooseFood.db";
    //private static final String FOOD_TABLE_NAME = "temp_food";
    private SQLiteDatabase db;

    public FoodDB(Context context, String path){
        db = context.openOrCreateDatabase(path,Context.MODE_PRIVATE,null);
    }

    //根据当前城市的温度匹配食物
    public List<String> getFoodByTemperature(String t){
        List<String> paths = new ArrayList<>();
        int temp = Integer.parseInt(t);
        Cursor c = db.rawQuery("SELECT path from temp_food WHERE high>"+temp+" AND low<"+temp,null);
        while(c.moveToNext()){
            paths.add(c.getString(c.getColumnIndex("path")));
        }
        return paths;
    }


}
