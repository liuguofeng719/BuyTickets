package com.ticket.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Debug;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.Runnable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 将崩溃异常记录在文件中，方便我们查看异常
 *
 * @author luocan
 */
@SuppressLint("SimpleDateFormat")
public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private static final String EXCEPTION_LOG_FILE_PATH = "crash.txt";
    public static final int MAX_CRASHLOG_FILE_LENGTH = 1024 * 500;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private Thread.UncaughtExceptionHandler mDefaultHandler;
    private Context mContext;

    public CrashHandler(Context context) {
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        mContext = context.getApplicationContext();
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        ByteArrayOutputStream baos = null;
        PrintStream printStream = null;
        StringBuilder sb = new StringBuilder();
        try {
            baos = new ByteArrayOutputStream();
            printStream = new PrintStream(baos);
            ex.printStackTrace(printStream);
            byte[] data = baos.toByteArray();
            Throwable cause = ex.getCause();
            while (cause != null) {
                cause.printStackTrace(printStream);
                cause = cause.getCause();
            }
            TLog.e("Debug", "getNativeHeapAllocatedSize:" + Debug.getNativeHeapAllocatedSize());
            TLog.e("Debug", "getNativeHeapFreeSize:" + Debug.getNativeHeapFreeSize());
            TLog.e("Debug", "getNativeHeapSize:" + Debug.getNativeHeapSize());
            sb.append("Exception time:" + dateFormat.format(new Date()) + " NativeHeapAllocatedSize:" + Debug.getNativeHeapAllocatedSize() + " NativeNativeHeapFreeSize:" + Debug.getNativeHeapFreeSize() + " NativeHeapSize:" + Debug.getNativeHeapSize() + " MaxMemory:" + Runtime.getRuntime().maxMemory() + " freeMemory:" + Runtime.getRuntime().freeMemory() + " totalMemory:" + Runtime.getRuntime().totalMemory() + " Thread Name:" + thread.getName() + " Thread id:" + thread.getId() + "\n");
            sb.append(collectCrashDeviceInfo(mContext) + "\n");
            sb.append(new String(data) + "\n");
            writeExceptionInfoToFile(sb.toString());
            data = null;
            TLog.e("Debug", sb.toString());
        } catch (Exception e) {
            TLog.e("Debug", e.getMessage());
        } finally {
            try {
                if (printStream != null) {
                    printStream.close();
                }
                if (baos != null) {
                    baos.close();
                }
            } catch (Exception e) {
                TLog.e("Debug", e.getMessage());
            }
        }

        // 弹出程序crash的对话框
        mDefaultHandler.uncaughtException(thread, ex);
    }

    private void writeExceptionInfoToFile(final String traceStackInfo) {
        if (StorageUtil.isSDCardExist()) {
            final File file = new File(StorageUtil.getDirByType(StorageUtil.DIR_TYPE_LOG), EXCEPTION_LOG_FILE_PATH);
            new Thread(new Runnable() {

                @Override
                public void run() {
                    FileWriter fw = null;
                    try {
                        if (file.exists()) {
                            if (file.length() > MAX_CRASHLOG_FILE_LENGTH) {
                                file.delete();
                            }
                        }
                        if (!file.exists()) {
                            file.getParentFile().mkdirs();
                            file.createNewFile();
                        }
                        fw = new FileWriter(file, true);
                        fw.write(traceStackInfo);
                        fw.flush();
                    } catch (IOException e) {
                        TLog.e("Debug", e.getMessage());
                    } finally {
                        if (fw != null) {
                            try {
                                fw.close();
                            } catch (IOException e) {
                                TLog.e("Debug", e.getMessage());
                            }
                        }
                    }

                }
            }).start();
        }
    }

    /**
     * 收集程序崩溃的设备信息
     *
     * @param context
     */
    public String collectCrashDeviceInfo(Context context) {
        StringBuilder sb = new StringBuilder();
        try {
            final PackageManager pm = context.getPackageManager();
            final PackageInfo info = pm.getPackageInfo(context.getPackageName(), 0);
            sb.append("版本").append(info.versionName).append(",").append(info.versionCode).append(",");
            sb.append("型号").append(Build.MODEL).append(",");
            sb.append("系统").append(Build.VERSION.RELEASE).append(",");
            // sb.append(getNetworkType());
        } catch (final Exception e) {
            // 忽略异常
        }
        return sb.toString();
    }

    // private String getNetworkType() {
    // if (HttpUtil.getNetworkState(mContext) == HttpUtil.NetworkState.WIFI) {
    // return "wifi";
    // } else {
    // String apn = HttpUtil.getAPN(mContext).apn;
    // return apn;
    // }
    // }
}
