package cn.edu.pku.ss.houwy.popwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import cn.edu.pku.ss.houwy.shihou.R;

public class NewPopwindow extends PopupWindow {
    private View mview;

    public NewPopwindow(Activity context, View.OnClickListener itemsOnClick) {
        super(context);
        initView(context,itemsOnClick);
    }
    private void initView(final Activity context, View.OnClickListener itemsOnClick){
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mview = mInflater.inflate(R.layout.popwindow_share,null);
        LinearLayout weixinLL = (LinearLayout)mview.findViewById(R.id.weixin);
        LinearLayout weiboLL = (LinearLayout)mview.findViewById(R.id.weibo);
        LinearLayout qqLL = (LinearLayout)mview.findViewById(R.id.qq);
        TextView cancleTv = (TextView)mview.findViewById(R.id.share_cancle);
        cancleTv.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                //销毁弹出框
                dismiss();
                backgroundAlpha(context,1f);
            }
        });
        //设置监听按钮
        weixinLL.setOnClickListener(itemsOnClick);
        weiboLL.setOnClickListener(itemsOnClick);
        qqLL.setOnClickListener(itemsOnClick);
        //设置SelectPicPopupWindow的View
        this.setContentView(mview);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(WindowManager.LayoutParams.FILL_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置PopupWindow可触摸
        this.setTouchable(true);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        backgroundAlpha(context, 0.5f);//0.0-1.0
        this.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
                // TODO Auto-generated method stub
                backgroundAlpha(context, 1f);
            }
        });
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(Activity context, float bgAlpha){
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);
    }

}
