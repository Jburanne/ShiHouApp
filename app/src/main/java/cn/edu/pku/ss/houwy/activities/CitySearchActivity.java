package cn.edu.pku.ss.houwy.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.edu.pku.ss.houwy.app.MyApplication;
import cn.edu.pku.ss.houwy.bean.City;
import cn.edu.pku.ss.houwy.shihou.R;

public class CitySearchActivity extends AppCompatActivity implements View.OnClickListener,SearchView.OnQueryTextListener,AdapterView.OnItemClickListener{
    //定义返回按钮
    private ImageView mbackBtn;
    //定义热门城市按钮
    private Button bjBtn,shBtn,gzBtn,szBtn,njBtn,whBtn,suzBtn,cqBtn,srBtn;

    //定义搜索框
    private SearchView searchSv;
    //定义ListView
    private ListView searchLv;

    private ArrayAdapter mAdapter;
    private ArrayList<City> cityList;
    private ArrayList<String> data;
    private static MyApplication myApplication;

    //定义ListView中被点中的城市对应的编码
    String selectedCityCode;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.city_search);
        initViews();
        setBtnOnclick();
        searchSv.setOnQueryTextListener(this);
        searchLv.setOnItemClickListener(this);
    }

    //初始化
    public void initViews(){
        mbackBtn = (ImageView) findViewById(R.id.city_search_back);
        bjBtn = (Button) findViewById(R.id.beijing_btn);
        shBtn = (Button) findViewById(R.id.shanghai_btn);
        gzBtn = (Button) findViewById(R.id.guangzhou_btn);
        szBtn = (Button) findViewById(R.id.shenzhen_btn);
        njBtn = (Button) findViewById(R.id.nanjing_btn);
        whBtn = (Button) findViewById(R.id.wuhan_btn);
        suzBtn = (Button) findViewById(R.id.suzhou_btn);
        cqBtn = (Button) findViewById(R.id.chongqing_btn);
        srBtn = (Button) findViewById(R.id.shangrao_btn);
        searchSv = (SearchView) findViewById(R.id.city_searchView);
        searchLv = (ListView) findViewById(R.id.city_searchLv);
    }

    //为按钮们设置点击事件
    public void setBtnOnclick(){
        mbackBtn.setOnClickListener(this);
        bjBtn.setOnClickListener(this);
        shBtn.setOnClickListener(this);
        gzBtn.setOnClickListener(this);
        szBtn.setOnClickListener(this);
        njBtn.setOnClickListener(this);
        whBtn.setOnClickListener(this);
        suzBtn.setOnClickListener(this);
        cqBtn.setOnClickListener(this);
        srBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String newCityCode = "101010100";
        switch(v.getId()){
            case(R.id.city_search_back):
                finish();
                break;
            //如果点击了热门城市，传回热门城市的编码
            case (R.id.beijing_btn):
                newCityCode = "101010100";
                break;
            case(R.id.shanghai_btn):
                newCityCode = "101020100";
                break;
            case(R.id.guangzhou_btn):
                newCityCode = "101280101";
                break;
            case(R.id.shenzhen_btn):
                newCityCode = "101280601";
                break;
            case(R.id.nanjing_btn):
                newCityCode = "101190101";
                break;
            case(R.id.wuhan_btn):
                newCityCode = "101200101";
                break;
            case(R.id.suzhou_btn):
                newCityCode = "101190401";
                break;
            case(R.id.chongqing_btn):
                newCityCode = "101040100";
                break;
            case (R.id.shangrao_btn):
                newCityCode = "101240301";
                break;
            default:
                break;
        }
        Intent intent = new Intent();
        intent.putExtra("new_city_code", newCityCode);
        setResult(RESULT_OK, intent);
        finish();

//        更新主页面的默认城市
//        SharedPreferences.Editor editor = getSharedPreferences("config", MODE_PRIVATE).edit();
//        editor.putString("main_city_code", "101010100");
//        editor.apply();
    }
    @Override
    public boolean onQueryTextChange(String newText) {
        if(!TextUtils.isEmpty(newText)){
            showListView(newText); //如果搜索框内有文字，显示ListView
        }
        else{
            searchLv.setVisibility(View.GONE); // 如果搜索框内为空，ListView消失
        }
        return false;
    }
    private void showListView(String str){
        //把ListView设为可见
        searchLv.setVisibility(View.VISIBLE);
        //根据搜索框内已输入的文字模糊匹配数据库中的城市名
        cityList = new ArrayList<City>(myApplication.getInstance().getResultList(str));
        //构建城市名的一个列表
        data = new ArrayList<String>();
        for(City s:cityList){
            data.add(s.getCity());
            Log.d("test",s.getCity());
        }
        //将数据绑定到ListView
        mAdapter = new ArrayAdapter<String>(CitySearchActivity.this,android.R.layout.simple_list_item_1,data);
        searchLv.setAdapter(mAdapter);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }


    //设置listView点击事件
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //设置提醒框
        AlertDialog.Builder builder = new AlertDialog.Builder(CitySearchActivity.this);
        builder.setTitle("提醒");

        String cityName;

        //获取点击位置的城市名
        cityName = String.valueOf(mAdapter.getItem(position));
        Log.d("cityName",cityName);
        //获取城市名对应的城市编码
        selectedCityCode = myApplication.getInstance().getCityCode(cityName);
        Log.d("test",selectedCityCode);
        //跳出提示语句
        builder.setMessage("你确定要将城市切换至"+cityName+"吗？");
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
                Intent i = new Intent(CitySearchActivity.this,MainActivity.class).putExtra("new_city_code",selectedCityCode);
                setResult(RESULT_OK, i);
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
