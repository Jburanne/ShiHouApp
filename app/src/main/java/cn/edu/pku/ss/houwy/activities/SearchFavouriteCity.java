package cn.edu.pku.ss.houwy.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;

import cn.edu.pku.ss.houwy.app.MyApplication;
import cn.edu.pku.ss.houwy.bean.City;
import cn.edu.pku.ss.houwy.shihou.R;

public class SearchFavouriteCity extends AppCompatActivity implements SearchView.OnQueryTextListener,AdapterView.OnItemClickListener{

    //定义搜索框
    private SearchView searchSv;
    //定义ListView
    private ListView searchLv;

    private ArrayAdapter mAdapter;
    private ArrayList<City> cityList;
    private ArrayList<String> data;
    private static MyApplication myApplication;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_city_search);
        initViews();
    }

    public void initViews(){
        searchSv = (SearchView) findViewById(R.id.add_city_Sv);
        searchLv = (ListView) findViewById(R.id.add_city_Lv);
        searchSv.setOnQueryTextListener(this);
        searchLv.setOnItemClickListener(this);
    }

    @Override
    public boolean onQueryTextSubmit(String newText) {
        if(!TextUtils.isEmpty(newText)){
            showListView(newText); //如果搜索框内有文字，显示ListView
        }
        return false;
    }

    public void showListView(String str){
        //根据搜索框内已输入的文字模糊匹配数据库中的城市名
        cityList = new ArrayList<City>(myApplication.getInstance().getResultList(str));
        //构建城市名的一个列表
        data = new ArrayList<String>();
        for(City s:cityList){
            data.add(s.getCity());
            Log.d("test",s.getCity());
        }
        //将数据绑定到ListView
        mAdapter = new ArrayAdapter<String>(SearchFavouriteCity.this,android.R.layout.simple_list_item_1,data);
        searchLv.setAdapter(mAdapter);

    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if(!TextUtils.isEmpty(newText)){
            showListView(newText); //如果搜索框内有文字，显示ListView
        }
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //设置提醒框
        AlertDialog.Builder builder = new AlertDialog.Builder(SearchFavouriteCity.this);
        builder.setTitle("提醒");

        final String cityName;

        //获取点击位置的城市名
        cityName = String.valueOf(mAdapter.getItem(position));
        Log.d("cityName",cityName);
        //跳出提示语句
        builder.setMessage("你确定要添加"+cityName+"吗？");
        //如果点击的是取消键
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //直接消掉dialog
            }
        });
        //如果点击的是确定键
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(SearchFavouriteCity.this,CityLikes.class).putExtra("add_city_name",cityName);
                setResult(RESULT_OK, i);
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }
}
