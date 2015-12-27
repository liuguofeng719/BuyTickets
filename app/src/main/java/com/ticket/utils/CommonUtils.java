package com.ticket.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.ticket.R;

import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class CommonUtils {

    public static final String REGEXP_MOBILE = "^1[3-8]\\d{9}$";
    public static final String REGEXP_CHINESE_STR = "^[\u4E00-\u9FA5]+$";
    public static final Pattern REG_CHINESE_STR = Pattern.compile(REGEXP_CHINESE_STR);
    public static final String REGEXP_IDCARD_15 = "^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$";
    public static final String REGEXP_IDCARD_18 = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X|x)$";
    public static final Pattern REG_MOBILE = Pattern.compile(REGEXP_MOBILE);
    public static final Pattern REG_IDCARD_15 = Pattern.compile(REGEXP_IDCARD_15);
    public static final Pattern REG_IDCARD_18 = Pattern.compile(REGEXP_IDCARD_18);

    public static boolean isMatch(Pattern patt, String value) {
        if (isEmpty(value)) {
            return false;
        }
        return patt.matcher(value).matches();
    }

    /**
     * return if str is empty
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0 || str.equalsIgnoreCase("null") || str.isEmpty() || str.equals("");
    }

    /**
     * 是否为中文字符串
     *
     * @param value 值对象
     * @return true: 为中文字符串, false: 为null或不为中文字符串
     */
    public static boolean isChineseString(String value) {
        if (isEmpty(value)) {
            return false;
        }
        return isMatch(REG_CHINESE_STR, value);
    }

    /**
     * 验证是否是手机号
     *
     * @param mobile
     * @return
     */
    public static boolean isMobile(String mobile) {
        return isMatch(REG_MOBILE, mobile);
    }

    /**
     * 判断字符串长度是否为期望值
     *
     * @param value 字符串长度
     * @param size  长度期望值
     * @return true: 相同, false: 字符串为null,""或长度不匹配
     * @createTime Jun 17, 2014 3:35:51 PM
     * @author wujianjun
     */
    public static boolean isEqualsLength(String value, int size) {
        if (size <= 0) {
            throw new RuntimeException("期望值小于等于0");
        }
        if (isEmpty(value)) {
            return false;
        }
        return value.trim().length() == size;
    }

    /**
     * 是否为身份证号码
     *
     * @param value 值对象
     * @return true:身份证号码, false: 为空串(null,"")或不为身份证号码
     */
    public static boolean isIDcard(String value) {
        if (isEqualsLength(value, 15) || isEqualsLength(value, 18)) {
            return isMatch(REG_IDCARD_15, value) || isMatch(REG_IDCARD_18, value);
        }
        return false;
    }

    /**
     * get format date
     *
     * @param timemillis
     * @return
     */
    public static String getFormatDate(long timemillis) {
        return new SimpleDateFormat("yyyy年MM月dd日").format(new Date(timemillis));
    }

    /**
     * decode Unicode string
     *
     * @param s
     * @return
     */
    public static String decodeUnicodeStr(String s) {
        StringBuilder sb = new StringBuilder(s.length());
        char[] chars = s.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (c == '\\' && chars[i + 1] == 'u') {
                char cc = 0;
                for (int j = 0; j < 4; j++) {
                    char ch = Character.toLowerCase(chars[i + 2 + j]);
                    if ('0' <= ch && ch <= '9' || 'a' <= ch && ch <= 'f') {
                        cc |= (Character.digit(ch, 16) << (3 - j) * 4);
                    } else {
                        cc = 0;
                        break;
                    }
                }
                if (cc > 0) {
                    i += 5;
                    sb.append(cc);
                    continue;
                }
            }
            sb.append(c);
        }
        return sb.toString();
    }

    /**
     * encode Unicode string
     *
     * @param s
     * @return
     */
    public static String encodeUnicodeStr(String s) {
        StringBuilder sb = new StringBuilder(s.length() * 3);
        for (char c : s.toCharArray()) {
            if (c < 256) {
                sb.append(c);
            } else {
                sb.append("\\u");
                sb.append(Character.forDigit((c >>> 12) & 0xf, 16));
                sb.append(Character.forDigit((c >>> 8) & 0xf, 16));
                sb.append(Character.forDigit((c >>> 4) & 0xf, 16));
                sb.append(Character.forDigit((c) & 0xf, 16));
            }
        }
        return sb.toString();
    }

    /**
     * convert time str
     *
     * @param time
     * @return
     */
    public static String convertTime(int time) {

        time /= 1000;
        int minute = time / 60;
        int second = time % 60;
        minute %= 60;
        return String.format("%02d:%02d", minute, second);
    }

    /**
     * url is usable
     *
     * @param url
     * @return
     */
    public static boolean isUrlUsable(String url) {
        if (CommonUtils.isEmpty(url)) {
            return false;
        }

        URL urlTemp = null;
        HttpURLConnection connt = null;
        try {
            urlTemp = new URL(url);
            connt = (HttpURLConnection) urlTemp.openConnection();
            connt.setRequestMethod("HEAD");
            int returnCode = connt.getResponseCode();
            if (returnCode == HttpURLConnection.HTTP_OK) {
                return true;
            }
        } catch (Exception e) {
            return false;
        } finally {
            connt.disconnect();
        }
        return false;
    }

    /**
     * is url
     *
     * @param url
     * @return
     */
    public static boolean isUrl(String url) {
        Pattern pattern = Pattern.compile("^([hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\/])+$");
        return pattern.matcher(url).matches();
    }

    /**
     * get toolbar height
     *
     * @param context
     * @return
     */
    public static int getToolbarHeight(Context context) {
        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(
                new int[]{android.R.attr.actionBarSize});
        int toolbarHeight = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();
        return toolbarHeight;
    }

    public static void make(Context context, String str) {
        Toast toast = Toast.makeText(context, str, Toast.LENGTH_SHORT);
        TextView view = (TextView) LayoutInflater.from(context).inflate(R.layout.publish_toast, null);
        view.setText(str);
        view.setPadding(30, 40, 30, 40);
        toast.setView(view);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static String parseDate(String createTime) {
        String res = "";
        try {
            Calendar today = Calendar.getInstance();
            Calendar old = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date dt = sdf.parse(createTime);
            old.setTime(dt);
            //此处好像是去除0
            today.set(Calendar.HOUR, 0);
            today.set(Calendar.MINUTE, 0);
            today.set(Calendar.SECOND, 0);
            old.set(Calendar.HOUR, 0);
            old.set(Calendar.MINUTE, 0);
            old.set(Calendar.SECOND, 0);
            //老的时间减去今天的时间
            long intervalMilli = old.getTimeInMillis() - today.getTimeInMillis();
            int xcts = (int) (intervalMilli / (24 * 60 * 60 * 1000));
            // -2:前天 -1：昨天 0：今天 1：明天 2：后天， out：显示日期

            String str[] = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};//字符串数组
            Calendar rightNow = Calendar.getInstance();
            int day = rightNow.get(Calendar.DAY_OF_WEEK);//获取时间
            rightNow.setTime(dt);
            if (xcts >= -2 && xcts <= 2) {
                String rest = String.valueOf(xcts);
//                if (rest.equals("-2")) {
//                    res = "前天";
//                }
//                if (rest.equals("-1")) {
//                    res = "昨天";
//                }
                if (rest.equals("0")) {
                    res = "今天";
                }
                if (rest.equals("1")) {
                    res = "明天";
                }
                if (rest.equals("2")) {
                    res = str[day - 1];
                }
            } else {
                res = str[day - 1];
            }


        } catch (ParseException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static String getCodeToStr(int code) {
        switch (code) {
            case 408:
                return "网络请求超时";
            case 500:
            case 503:
            case 504:
                return "服务器异常";
            default:
                return "";
        }
    }

    public static Dialog createDialog(Context context) {
        Dialog mDialog = new Dialog(context, R.style.loadingDialog);
        Window window = mDialog.getWindow();
        window.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);
        mDialog.setCancelable(false);
        mDialog.setCanceledOnTouchOutside(false);
        return mDialog;
    }
    public static Dialog showDialog(Context context){
       return showDialog(context,"");
    }

    public static Dialog showDialog(Context context, String message) {
        Dialog dialog = createDialog(context);
        dialog.setContentView(R.layout.loading);
        if (!TextUtils.isEmpty(message)) {
            TextView textView = (TextView) dialog.findViewById(R.id.loading_msg);
            textView.setText(message);
        }
        return dialog;
    }


    public static void dismiss(Dialog mdialog) {
        if (mdialog != null) {
            mdialog.dismiss();
            mdialog = null;
        }
    }

    // 删除ArrayList中重复元素，保持顺序 vo 必须是现在equals 和hashCode 方法
    public static List removeDuplicate(List list) {
        return new ArrayList(new LinkedHashSet(list));
    }

    // 删除ArrayList中重复元素，保持顺序
    public static void removeDuplicateWithOrder(List list) {
        Set set = new HashSet();
        List newList = new ArrayList();
        for (Iterator iter = list.iterator(); iter.hasNext(); ) {
            Object element = iter.next();
            if (set.add(element))
                newList.add(element);
        }
        list.clear();
        list.addAll(newList);
    }

}
