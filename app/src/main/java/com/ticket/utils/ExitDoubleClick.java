package com.ticket.utils;

import android.app.Activity;
import android.content.Context;

import com.ticket.ui.base.BaseAppManager;


/**
 * 双击退出Activity
 *
 * @author wanglin
 */
public class ExitDoubleClick extends DoubleClick {

    private static ExitDoubleClick exit;

    private ExitDoubleClick(Context context) {
        super(context);
    }

    /**
     * 返回一个双击退出的实例。
     *
     * @param context
     * @return ExitDoubleClick
     */
    public static synchronized ExitDoubleClick getInstance(Context context) {
        if (exit == null) {
            exit = new ExitDoubleClick(context);
        }
        return exit;
    }

    /**
     * 双击之后退出。
     */
    @Override
    protected void afterDoubleClick() {
        ((Activity) mContext).finish();
        System.exit(0);
        BaseAppManager.getInstance().clear();
        destroy();
    }

    /**
     * 双击退出Activity，如果msg为null，而默认显示的提示语为"再按一次退出"。
     */
    @Override
    public void doDoubleClick(int delayTime, String msg) {
        if (msg == null || msg.equals("")) {
            msg = "再按一次退出";
        }
        super.doDoubleClick(delayTime, msg);
    }

    private static void destroy() {
        exit = null;
    }
}
