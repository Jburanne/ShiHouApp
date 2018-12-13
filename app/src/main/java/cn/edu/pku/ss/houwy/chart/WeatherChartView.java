package cn.edu.pku.ss.houwy.chart;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import java.util.jar.Attributes;

import cn.edu.pku.ss.houwy.shihou.R;

public class WeatherChartView extends View {

    //x轴集合
    private float mXAxis[] = new float[6];
    //最高温度y集合
    private float mYAxisHigh[] = new float[6];
    //最低温度y集合
    private float mYAxisLow[] = new float[6];
    //x,y轴的集合数
    private static final int LENGTH = 6;
    //最高温度集合
    private int mTempHigh[] = new int[6];
    //最低温度集合
    private int mTempLow[] = new int[6];
    //控件高
    private int mHeight;
    //字体大小
    private float mTextSize;
    //圆半价
    private float mRadius;
    //（今天的）圆半径
    private float mRadiusToday;
    //文字移动位置距离
    private float mTextSpace;
    //线的大小
    private float mStokeWidth;
    //最高温度折线颜色
    private int mColorHigh;
    //最低温度折线颜色
    private int mColorLow;
    //字体颜色
    private int mTextColor;
    //屏幕密度
    private float mDensity;
    //控件边的空白空间
    private float mSpace;

    @SuppressWarnings("deprecation")
    public WeatherChartView(Context context, AttributeSet attrs){
        super(context,attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.WeatherChartView);
        float densityText = getResources().getDisplayMetrics().scaledDensity;
        mTextSize = a.getDimensionPixelSize(R.styleable.WeatherChartView_textSize,
                (int) (14 * densityText));
        mColorHigh = a.getColor(R.styleable.WeatherChartView_dayColor,
                getResources().getColor(R.color.colorAccent));
        mColorLow = a.getColor(R.styleable.WeatherChartView_nightColor,
                getResources().getColor(R.color.colorPrimary));
        mTextColor = a.getColor(R.styleable.WeatherChartView_textColor, Color.WHITE);
        //回收TypedArray
        a.recycle();
        mDensity = getResources().getDisplayMetrics().density;
        mRadius = 3 * mDensity;
        mRadiusToday = 5 * mDensity;
        mSpace = 3 * mDensity;
        mTextSpace = 10 * mDensity;
        mStokeWidth = 2 * mDensity;
    }

    public WeatherChartView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(mHeight == 0){
            //设置控件高度，x集合
            setHeightAndXAxis();
        }
        //计算Y轴集合的数值
        computeYAxisValues();
        //画最高温度折线图
        drawChart(canvas,mColorHigh,mTempHigh,mYAxisHigh,0);
        //画最低温度折线图
        drawChart(canvas,mColorLow,mTempLow,mYAxisLow,1);

    }

    private void setHeightAndXAxis(){
        mHeight = getHeight();
        // 控件宽
        int width = getWidth();
        // 每一份宽
        float w = width / 12;
        mXAxis[0] = w;
        mXAxis[1] = w * 3;
        mXAxis[2] = w * 5;
        mXAxis[3] = w * 7;
        mXAxis[4] = w * 9;
        mXAxis[5] = w * 11;

    }

    /**
     * 设置白天温度
     *
     * @param tempHigh 温度数组集合
     */
    public void setTempHigh(int[] tempHigh) {
        mTempHigh = tempHigh;
    }

    /**
     * 设置夜间温度
     *
     * @param tempLow 温度数组集合
     */
    public void setTempLow(int[] tempLow) {
        mTempLow = tempLow;
    }
    /**
     * 计算y轴集合数值
     */
    private void computeYAxisValues() {
        // 存放六天最高温度集合中的最小值
        int minTempHigh = mTempHigh[0];
        // 存放六天最高温度集合中的最大值
        int maxTempHigh = mTempHigh[0];
        for (int item : mTempHigh) {
            if (item < minTempHigh) {
                minTempHigh = item;
            }
            if (item > maxTempHigh) {
                maxTempHigh = item;
            }
        }

        // 存放六天最低温度集合中的最小值
        int minTempLow = mTempLow[0];
        // 存放夜间最高温度
        int maxTempLow = mTempLow[0];
        for (int item : mTempLow) {
            if (item < minTempLow) {
                minTempLow = item;
            }
            if (item > maxTempLow) {
                maxTempLow = item;
            }
        }

        // 最高温度、最低温度集合中的最小值
        int minTemp = minTempLow < minTempHigh ? minTempLow : minTempHigh;
        // 最高温度、最低温度集合中的最大值
        int maxTemp = maxTempHigh > maxTempLow ? maxTempHigh : maxTempLow;

        // 份数（综合温差）
        float parts = maxTemp - minTemp;
        // y轴一端到控件一端的距离
        float length = mSpace + mTextSize + mTextSpace + mRadius;
        // y轴高度
        float yAxisHeight = mHeight - length * 2;

        // 当温度都相同时（被除数不能为0）
        if (parts == 0) {
            for (int i = 0; i < LENGTH; i++) {
                mYAxisHigh[i] = yAxisHeight / 2 + length;
                mYAxisLow[i] = yAxisHeight / 2 + length;
            }
        } else {
            float partValue = yAxisHeight / parts;
            for (int i = 0; i < LENGTH; i++) {
                mYAxisHigh[i] = mHeight - partValue * (mTempHigh[i] - minTemp) - length;
                mYAxisLow[i] = mHeight - partValue * (mTempLow[i] - minTemp) - length;
            }
        }
    }

    /**
     * 画折线图
     *
     * @param canvas 画布
     * @param color  画图颜色
     * @param temp   温度集合
     * @param yAxis  y轴集合
     * @param type   折线种类：0，白天；1，夜间
     */
    private void drawChart(Canvas canvas, int color, int temp[], float[] yAxis, int type) {
        //线化笔
        Paint linePaint = new Paint();
        //抗锯齿
        linePaint.setAntiAlias(true);
        //线宽
        linePaint.setStrokeWidth(mStokeWidth);
        linePaint.setColor(color);
        //空心
        linePaint.setStyle(Paint.Style.STROKE);
        //点画笔
        Paint pointPaint = new Paint();
        pointPaint.setAntiAlias(true);
        pointPaint.setColor(color);
        //字体画笔
        Paint textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(mTextColor);
        textPaint.setTextSize(mTextSize);
        //文字居中
        textPaint.setTextAlign(Paint.Align.CENTER);
        int alpha1 = 102;
        int alpha2 = 255;
        for (int i = 0; i < LENGTH; i++) {
            // 画线
            if (i < LENGTH - 1) {
                // 昨天
                if (i == 0) {
                    linePaint.setAlpha(alpha1);
                    // 设置虚线效果
                    linePaint.setPathEffect(new DashPathEffect(new float[]{2 * mDensity, 2 * mDensity}, 0));
                    // 路径
                    Path path = new Path();
                    // 路径起点
                    path.moveTo(mXAxis[i], yAxis[i]);
                    // 路径连接到
                    path.lineTo(mXAxis[i + 1], yAxis[i + 1]);
                    canvas.drawPath(path, linePaint);
                } else {
                    linePaint.setAlpha(alpha2);
                    linePaint.setPathEffect(null);
                    canvas.drawLine(mXAxis[i], yAxis[i], mXAxis[i + 1], yAxis[i + 1], linePaint);
                }
            }

            // 画点
            if (i != 1) {
                // 昨天
                if (i == 0) {
                    pointPaint.setAlpha(alpha1);
                    canvas.drawCircle(mXAxis[i], yAxis[i], mRadius, pointPaint);
                } else {
                    pointPaint.setAlpha(alpha2);
                    canvas.drawCircle(mXAxis[i], yAxis[i], mRadius, pointPaint);
                }
                // 今天
            } else {
                pointPaint.setAlpha(alpha2);
                canvas.drawCircle(mXAxis[i], yAxis[i], mRadiusToday, pointPaint);
            }

            // 画字
            // 昨天
            if (i == 0) {
                textPaint.setAlpha(alpha1);
                drawText(canvas, textPaint, i, temp, yAxis, type);
            } else {
                textPaint.setAlpha(alpha2);
                drawText(canvas, textPaint, i, temp, yAxis, type);
            }
        }
    }

    /**
     * 绘制文字
     *
     * @param canvas    画布
     * @param textPaint 画笔
     * @param i         索引
     * @param temp      温度集合
     * @param yAxis     y轴集合
     * @param type      折线种类：0，白天；1，夜间
     */
    private void drawText(Canvas canvas, Paint textPaint, int i, int[] temp, float[] yAxis, int type) {
        switch (type) {
            case 0:
                // 显示最高气温
                canvas.drawText(temp[i] + "°", mXAxis[i], yAxis[i] - mRadius - mTextSpace, textPaint);
                break;
            case 1:
                // 显示最低气温
                canvas.drawText(temp[i] + "°", mXAxis[i], yAxis[i] + mTextSpace + mTextSize, textPaint);
                break;
        }
    }

}
