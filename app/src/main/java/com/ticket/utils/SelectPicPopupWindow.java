package com.ticket.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ticket.R;

/**
 * 图片选择窗口
 *
 * @author ipangy
 */
@SuppressLint("InflateParams")
public class SelectPicPopupWindow extends PopupWindow {
    private TextView btn_cancel, btn_ok;
    private View mMenuView, videoLine;
    private SelectPicPWInterface selectPicPWInterface = null;

    private Activity mContext;

    public SelectPicPopupWindow(Activity context) {
        super(context);
        mContext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.passenger_dialog, null);
        btn_cancel = (TextView) mMenuView.findViewById(R.id.btn_cancel);
        btn_ok = (TextView) mMenuView.findViewById(R.id.btn_ok);
        // 设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.dialog_animation);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
    }

    /**
     * 全按钮监听定义
     *
     * @param parent
     * @param gravity
     * @param x
     * @param y
     * @param cancelListener
     * @param showType
     */
    public void showAtLocationXAll(View parent, int showType, int gravity, int x, int y,

                                   OnClickListener cancelListener) {
    	WindowManager.LayoutParams lp=mContext.getWindow().getAttributes();
        lp.alpha=0.5f;
        mContext.getWindow().setAttributes(lp);
        btn_cancel.setOnClickListener(cancelListener);
        showAtLocation(parent, gravity, x, y);
    }

    public void setSelectPicPWInterface(SelectPicPWInterface selectPicPWInterface) {
        this.selectPicPWInterface = selectPicPWInterface;
    }

    public interface SelectPicPWInterface{
        public void clickCancel();
    }
    
    @Override 
    public void dismiss(){
    	super.dismiss();
		WindowManager.LayoutParams lp=mContext.getWindow().getAttributes();
        lp.alpha=1.0f;
        mContext.getWindow().setAttributes(lp);
    }
}
