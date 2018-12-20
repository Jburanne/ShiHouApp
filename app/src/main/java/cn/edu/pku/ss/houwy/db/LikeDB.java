package cn.edu.pku.ss.houwy.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class LikeDB {
    public static final String Like_DB_NAME = "LIKE.db";
    private SQLiteDatabase db;

    public LikeDB(Context context, String path){
        db = context.openOrCreateDatabase(path,Context.MODE_PRIVATE,null);
    }

    //搜索当前已关注的城市
    public List<String> getFavouriteCity(){
        List<String> city = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT city from city_likes",null);
        while(c.moveToNext()){
            city.add(c.getString(c.getColumnIndex("city")));
        }
        return city;
    }

    //不再关注某个城市，从city_likes表中删除数据
    public void deleteFavouriteCity(String cityname){
        db.execSQL("delete from city_likes where city = ?",new Object[]{cityname});
    }

    //查询城市是否已被关注
    public boolean cityInList(String name){
        Cursor c = db.rawQuery("SELECT * from city_likes where city='"+name+"'",null);
        if(c.moveToNext()){
            return true;
        }else{
            return false;
        }
    }

    //关注的城市数是否已达上限
    public boolean cityNum(){
        Cursor c = db.rawQuery("SELECT count(*) count from city_likes",null);
        int num = 0;
        while(c.moveToNext()){
            num = Integer.parseInt(c.getString(c.getColumnIndex("count")));
        }
        if(num < 5){
            return true;
        }else{
            return false;
        }
    }

    //关注某个城市
    public void addFavouriteCity(String cityname){
        db.execSQL("insert into city_likes(city,tmp) values(?,null)",new Object[]{cityname});
    }


}
