package cn.edu.pku.ss.houwy.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.edu.pku.ss.houwy.app.MyApplication;
import cn.edu.pku.ss.houwy.shihou.R;

public class CityLikes extends AppCompatActivity implements View.OnClickListener,View.OnLongClickListener{

    private LayoutInflater inflater;
    private LinearLayout lin;
    //记录已添加的城市个数
    private int cityCardCount;
    //定义添加城市按钮
    private ImageView mAddCityBtn;

    private MyApplication myApplication;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.city_likes);
        initViews();
        initCities();
    }
    public void initViews(){
        inflater = LayoutInflater.from(this);
        lin = (LinearLayout)findViewById(R.id.LL_city_likes);
        cityCardCount = 0;
        mAddCityBtn = (ImageView) findViewById(R.id.add_like_city);
        mAddCityBtn.setOnClickListener(this);
    }

    public void initCities(){
        //加载已关注的城市
        List<String> city = new ArrayList<>(myApplication.getInstance().getCityLike());
        if(city.size() != 0){
            for(String item:city){
                addLikeCity(item);
            }
        }
    }

    //动态增加布局：为每个新增的城市增加一个textview，加入到当前layout中
    public void addLikeCity(String cityName){
        cityCardCount++;
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.city_card,null).findViewById(R.id.add_city);
        lin.addView(layout);
        TextView textView = (TextView) findViewById(R.id.city_card_text);
        //最多能关注5个城市，设置textview中的城市名，设置点击事件和长按事件
        switch (cityCardCount){
            case 1:
                textView.setId(R.id.like_city_1);
                textView.setText(cityName);
                textView.setOnClickListener(this);
                textView.setOnLongClickListener(this);
                break;
            case 2:
                textView.setId(R.id.like_city_2);
                textView.setText(cityName);
                textView.setOnClickListener(this);
                textView.setOnLongClickListener(this);
                break;
            case 3:
                textView.setId(R.id.like_city_3);
                textView.setText(cityName);
                textView.setOnClickListener(this);
                textView.setOnLongClickListener(this);
                break;
            case 4:
                textView.setId(R.id.like_city_4);
                textView.setText(cityName);
                textView.setOnClickListener(this);
                textView.setOnLongClickListener(this);
                break;
            case 5:
                textView.setId(R.id.like_city_5);
                textView.setText(cityName);
                textView.setOnClickListener(this);
                textView.setOnLongClickListener(this);
                break;
        }

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.like_city_1 || v.getId() == R.id.like_city_2 ||v.getId() == R.id.like_city_3
                || v.getId() == R.id.like_city_4 || v.getId() == R.id.like_city_5){
            TextView t = (TextView) findViewById(v.getId());
            //Log.d("cityName",t.getText().toString());
            //将点击的城市名传回主界面
            Intent intent = new Intent(CityLikes.this,MainActivity.class).putExtra("like_city_name",t.getText().toString());
            setResult(RESULT_OK, intent);
            finish();
        }
        else if(v.getId() == R.id.add_like_city){
            Intent intent = new Intent(this,SearchFavouriteCity.class);
            startActivity(intent);
            //startActivityForResult(intent,1);
        }

    }

    //设置长按事件，当长按某个城市时，可以选择不再关注这个城市
    @Override
    public boolean onLongClick(View v) {
        if(v.getId() == R.id.like_city_1 || v.getId() == R.id.like_city_2 ||v.getId() == R.id.like_city_3
                || v.getId() == R.id.like_city_4 || v.getId() == R.id.like_city_5){
            //获取点击的城市名
            TextView t = (TextView) findViewById(v.getId());
            final String city = t.getText().toString();
            //设置提醒框
            AlertDialog.Builder builder = new AlertDialog.Builder(CityLikes.this);
            builder.setTitle("提醒");
            //跳出提示语句
            builder.setMessage("你确定不再关注"+city+"吗？");
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
                    //在数据库表中删除该城市
                    myApplication.getInstance().deleteCityLike(city);
                    //重新加载内容
                    setContentView(R.layout.city_likes);
                    initViews();
                    initCities();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();

        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 1 && resultCode == RESULT_OK){
            String cityName = data.getStringExtra("add_city_name");
            addLikeCity(cityName);
        }
    }

    //返回该活动时重新加载内容
    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.city_likes);
        initViews();
        initCities();
    }
}
