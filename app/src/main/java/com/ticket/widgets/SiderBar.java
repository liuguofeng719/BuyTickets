package com.ticket.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.ticket.utils.DensityUtils;

//绘制对应的英文字母
public class SiderBar extends View {

    //XML文件创建控件对象时调用
    public SiderBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //new 对象时调用
    public SiderBar(Context context) {
        super(context);
    }

    private Paint paint = new Paint();//画笔
    // 26个字母
    public static String[] sideBar = {"热门", "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z"};

    private OnTouchingLetterChangedListener letterChangedListener;
    private int choose;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(Color.parseColor("#999999"));//设置画笔灰色
        paint.setTypeface(Typeface.DEFAULT_BOLD);//设置字体样式粗体
        paint.setTextSize(DensityUtils.dip2px(getContext(), 16));
        //获取自定义View的宽和高
        int height = getHeight();
        int width = getWidth();
        //设定每一个字母所在控件的高度
        int each_height = height / sideBar.length;
        //给每一个字母绘制出来
        for (int i = 0; i < sideBar.length; i++) {
            //字体所在区域在x轴的偏移量
            float x = width / 2 - paint.measureText(sideBar[i]) / 2;
            //字体所在区域在Y轴的偏移量
            float y = (1 + i) * each_height;
            canvas.drawText(sideBar[i], x, y, paint);
        }
    }

    //定义监听事件
    public interface OnTouchingLetterChangedListener {
        void onTouchingLetterChanged(String s);//根据滑动位置的索引做出处理
    }

    public void setOnTouchingLetterChangedListener(OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
        this.letterChangedListener = onTouchingLetterChangedListener;
    }

    //分发对应的touch监听
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();//获取对应的动作
        final float y = event.getY();//点击的y坐标
        final OnTouchingLetterChangedListener listener = letterChangedListener;
        final int c = (int) (y / getHeight() * sideBar.length);//获取点击y轴坐标所占总高度的比例*数组的长度就是等于数组中点击的字母索引
        switch (action) {
            case MotionEvent.ACTION_UP://抬起
//                setBackgroundResource(android.R.color.transparent);
//                invalidate();
                break;

            default:
//                setBackgroundResource(R.color.alpha_55_blue);
                if (c >= 0 && c < sideBar.length) {
                    if (listener != null) {
                        listener.onTouchingLetterChanged(sideBar[c]);
                    }
                    choose = c;
                    invalidate();
                }
                break;
        }
        return true;

    }
}
