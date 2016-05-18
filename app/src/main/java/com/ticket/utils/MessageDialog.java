package com.ticket.utils;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ticket.R;
import com.ticket.TicketsApplication;


/**
 * 消息确认框
 *
 * @author ipangy
 */
public class MessageDialog {
    private Dialog mDialog;
    private Context context;
    private String message;
    private boolean isShowing;
    private View view;
    private boolean isPicture = false;
    private ProgressBar pb;

    public MessageDialog(Context context) {
        this.context = context;
        view = LayoutInflater.from(context).inflate(R.layout.activity_seller_dialog, null);
        init();
    }

    public MessageDialog(Context context, View view) {
        this.context = context;
        this.view = view;
        init();
    }

    /**
     * Used when need to show the big picture of a thumbnail
     *
     * @param context
     * @param view
     * @param isPicture
     */
    public MessageDialog(Context context, View view, boolean isPicture) {
        this.context = context;
        this.view = view;
        this.isPicture = isPicture;
        init();
    }

    public static DisplayMetrics getDisplayMetrices() {
        WindowManager wm = (WindowManager) TicketsApplication.getTicketApplication()
                .getSystemService(Context.WINDOW_SERVICE);

        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm;
    }

    /**
     * 初始化
     *
     * @author lilw
     * @CrateTime 2012-11-27
     */
    private void init() {
        mDialog = new Dialog(context, R.style.seller_dialogStyle);
        WindowManager.LayoutParams params = mDialog.getWindow().getAttributes();
        mDialog.addContentView(view, params);
        int margin = (int) context.getResources().getDimension(R.dimen.common_padding);
        params.width = getDisplayMetrices().widthPixels - margin * 2;
        mDialog.getWindow().setAttributes(params);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setCancelable(false);
        isShowing = false;
    }

    public View getView() {
        return view;
    }

    /**
     * 获取弹框实例
     */
    public Dialog getDialog() {
        return mDialog;
    }

    /**
     * 获取消息内容
     */
    public String getMessage() {
        return message;
    }

    /**
     * 设置消息内容-- 居中
     */
    public void setMessage(String message) {
        this.message = message + "";
        TextView tv_message = (TextView) view.findViewById(R.id.tv_dialog_message);
        tv_message.setText(message);
        tv_message.setGravity(Gravity.CENTER);
    }

    public void setMessageLeft(String message) {
        this.message = message + "";
        TextView tv_message = (TextView) view.findViewById(R.id.tv_dialog_message);
        tv_message.setText(message);
    }

    /**
     * 设置消息内容
     *
     * @param resId
     */
    public void setMessage(int resId) {
        TextView tv_message = (TextView) view.findViewById(R.id.tv_dialog_message);
        tv_message.setText(resId);
    }

    /**
     * 设置标题
     *
     * @param title
     */
    public void setTitle(String title) {
        TextView tv_title = (TextView) view.findViewById(R.id.tv_dialog_title);
        tv_title.setText(title);
        if (TextUtils.isEmpty(title)) {
            view.findViewById(R.id.ll_title_container).setVisibility(View.GONE);
        } else {
            view.findViewById(R.id.ll_title_container).setVisibility(View.VISIBLE);
        }
    }


    public ProgressBar getPb() {
        if (pb == null) {
            pb = (ProgressBar) view.findViewById(R.id.pb_download_progress);
        }
        return pb;
    }

    /**
     * 显示弹框
     *
     * @author lilw
     * @CrateTime 2013-1-7
     */
    public void show() {
        if (isShowing) {
            isShowing = true;
            return;
        }
        getDialog().show();

        isShowing = true;
    }

    /**
     * 取消弹框
     *
     * @author lilw
     * @CrateTime 2013-1-7
     */
    public void dismiss() {
        getDialog().dismiss();
        isShowing = false;
    }

    /**
     * 判断弹框是否正在显示
     *
     * @return
     * @author lilw
     * @CrateTime 2013-1-7
     */
    public boolean isShowing() {
        return isShowing;
    }

    /**
     * 设置弹框是否可以取消（点击弹框外区域和按back按钮取消）
     *
     * @param flag true为可以取消 false为不可取消
     * @author lilw
     * @CrateTime 2013-1-7
     */
    public void setCancelable(boolean flag) {
        getDialog().setCancelable(flag);
    }

    public void setCanceledOnTouchOutside(boolean flag) {
        getDialog().setCanceledOnTouchOutside(flag);
    }
}
