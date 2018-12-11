package cn.edu.pku.ss.houwy.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.edu.pku.ss.houwy.app.MyApplication;
import cn.edu.pku.ss.houwy.bean.City;
import cn.edu.pku.ss.houwy.shihou.R;

public class CitySearchActivity extends AppCompatActivity implements View.OnClickListener,SearchView.OnQueryTextListener{
    //定义返回按钮
    private ImageView mbackBtn;
    //定义热门城市按钮
    private Button bjBtn,shBtn,gzBtn,szBtn,njBtn,whBtn,suzBtn,cqBtn,srBtn;

    //定义搜索框
    private SearchView searchSv;
    //定义ListView
    private ListView searchLv;

    //定义搜索结果，只放城市名
    private ArrayList<String> mSearchResult = new ArrayList<>();
    //定义城市名到编码的映射
    private Map<String,String> nameToCode = new HashMap<>();
    //定义城市名到拼音的映射
    private Map<String,String> nameToPinyin = new HashMap<>();

    //private ArrayAdapter arrayAdapter = new ArrayAdapter();

    Context context;

    private ArrayAdapter mAdapter;
    private ArrayList<City> cityList;
    private ArrayList<String> data;
    private static MyApplication myApplication;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.city_search);
        context = this;
        initViews();
        setBtnOnclick();
        searchSv.setOnQueryTextListener(this);
    }

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
            case(R.id.beijing_btn):
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

            //更新主页面的默认城市
//            SharedPreferences.Editor editor = getSharedPreferences("config",MODE_PRIVATE).edit();
//            editor.putString("main_city_code","101010100");
//            editor.apply();
    }



    @Override
    public boolean onQueryTextChange(String newText) {
        if(!TextUtils.isEmpty(newText)){
            showListView(newText);
            Log.d("test","test");
//            if(mSearchResult != null){
//                mSearchResult.clear();
//            }
//            for(String str : nameToPinyin.keySet()){
//                if(str.contains(newText)||nameToPinyin.get(str).contains(newText)){
//                    mSearchResult.add(str);
//                }
//            }
            //arrayAdapter.notifyDataSetChanged();
        }
        return false;
    }
    private void showListView(String str){
        searchLv.setVisibility(View.VISIBLE);
        cityList = new ArrayList<City>(myApplication.getInstance().getResultList(str));
        data = new ArrayList<String>();//构建城市名的一个列表
        for(City s:cityList){
            data.add(s.getCity());//通过citylist获取数据
            Log.d("test",s.getCity());
        }
        //绑定
        mAdapter = new ArrayAdapter<String>(CitySearchActivity.this,android.R.layout.simple_list_item_1,data);
        searchLv.setAdapter(mAdapter);//绑定
        //searchLv.setTextFilterEnabled(true);//开启过滤功能
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }


}
