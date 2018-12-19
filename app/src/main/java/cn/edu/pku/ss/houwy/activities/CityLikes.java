package cn.edu.pku.ss.houwy.activities;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
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

public class CityLikes extends AppCompatActivity implements View.OnClickListener{

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
    }
    public void initViews(){
        inflater = LayoutInflater.from(this);
        lin = (LinearLayout)findViewById(R.id.LL_city_likes);
        cityCardCount = 0;
        mAddCityBtn = (ImageView) findViewById(R.id.add_like_city);
        mAddCityBtn.setOnClickListener(this);

        //加载已关注的城市
//        List<String> city = myApplication.getInstance().getCityLike();
//        for(String item:city){
//            addLikeCity(item);
//        }
    }

    public void addLikeCity(String cityName){
        cityCardCount++;
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.city_card,null).findViewById(R.id.add_city);
        lin.addView(layout);
        TextView textView = (TextView) findViewById(R.id.city_card_text);
        switch (cityCardCount){
            case 1:
                textView.setId(R.id.like_city_1);
                textView.setText(cityName);
                textView.setOnClickListener(this);
                break;
            case 2:
                textView.setId(R.id.like_city_2);
                textView.setText(cityName);
                textView.setOnClickListener(this);
                break;
            case 3:
                textView.setId(R.id.like_city_3);
                textView.setText(cityName);
                textView.setOnClickListener(this);
                break;
            case 4:
                textView.setId(R.id.like_city_4);
                textView.setText(cityName);
                textView.setOnClickListener(this);
                break;
            case 5:
                textView.setId(R.id.like_city_5);
                textView.setText(cityName);
                textView.setOnClickListener(this);
                break;
        }

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.like_city_1 || v.getId() == R.id.like_city_2 ||v.getId() == R.id.like_city_3
                || v.getId() == R.id.like_city_4 || v.getId() == R.id.like_city_5){
            TextView t = (TextView) findViewById(v.getId());
            Log.d("cityName",t.getText().toString());
            Intent intent = new Intent(CityLikes.this,MainActivity.class).putExtra("like_city_name",t.getText().toString());
            setResult(RESULT_OK, intent);
            finish();
        }
        else if(v.getId() == R.id.add_like_city){
            Intent intent = new Intent(this,SearchFavouriteCity.class);
            startActivityForResult(intent,1);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 1 && resultCode == RESULT_OK){
            String cityName = data.getStringExtra("add_city_name");
            addLikeCity(cityName);
        }
    }
}
