package com.ticket.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;

import com.ticket.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class UpdateManager {

	/* 下载中 */
	private static final int DOWNLOAD = 1;

	/* 下载结束 */
	private static final int DOWNLOAD_FINISH = 2;

	private String download_url;
	private String apkName = "troycd.apk";

	/* 下载保存路径 */
	private String mSavePath;

	/* 记录进度条数量 */
	private int progress;

	/* 是否取消更新 */
	private boolean cancelUpdate = false;

	private Context mContext;

	/* 更新进度条 */
	private ProgressBar mProgress;

	private MessageDialog mDialog = null;

	private Handler mHandler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			// 正在下载
			case DOWNLOAD:
				// 设置进度条位置
				mProgress.setProgress(progress);
				break;
			case DOWNLOAD_FINISH:
				// 安装文件
				((Activity) mContext).finish();
				installApk();
				break;
			default:
				break;
			}
		};
	};

	private Handler xHandler;

	public UpdateManager(Context context,String downloadUrl, Handler xHandler) {
		this.mContext = context;
		this.download_url = downloadUrl;
		this.xHandler = xHandler;
	}

	/**
	 * 显示软件下载对话框
	 */
	public void showDownloadDialog() {
		// 构造软件下载对话框
		mDialog = new MessageDialog(mContext);
		// 给下载对话框增加进度条
		mProgress = mDialog.getPb();
		mProgress.setVisibility(View.VISIBLE);
		mDialog.setMessage(R.string.common_load_message);
		if (mContext != null)
			mDialog.show();
		// 下载文件
		downloadApk();
	}

	/**
	 * 下载apk文件
	 */
	private void downloadApk() {
		// 启动新线程下载软件
		new downloadApkThread().start();
	}

	/**
	 * 下载文件线程
	 * 
	 * @author coolszy
	 * @date 2012-4-26
	 * @blog http://blog.92coding.com
	 */
	private class downloadApkThread extends Thread {

		@Override
		public void run() {
			try {
				if (StorageUtil.hasExternalStorage()) {
					mSavePath = Environment.getExternalStorageDirectory() + "/download";
				} else {
					if (mContext.getFilesDir() == null
							|| TextUtils.isEmpty(mContext.getFilesDir().toString())) {
						throw new IOException("permission exception");
					}
					mSavePath = mContext.getFilesDir().toString() + "/";
				}
				URL url = new URL(download_url);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.connect();
				// 创建输入流
				InputStream is = conn.getInputStream();
				// 获取文件大小
				int length = conn.getContentLength();
				File file = new File(mSavePath);
				// 判断文件目录是否存在
				if (!file.exists()) {
					file.mkdir();
				}
				File apkFile = new File(mSavePath, apkName);
				FileOutputStream fos = new FileOutputStream(apkFile);
				int count = 0;
				// 缓存
				byte buf[] = new byte[1024];
				// 写入到文件中
				do {
					int numread = is.read(buf);
					count += numread;
					// 计算进度条位置
					progress = (int) (((float) count / length) * 100);
					// 更新进度
					mHandler.sendEmptyMessage(DOWNLOAD);
					if (numread <= 0) {
						// 下载完成
						mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
						break;
					}
					// 写入文件
					fos.write(buf, 0, numread);
				} while (!cancelUpdate);// 点击取消就停止下载.
				fos.close();
				is.close();
			} catch (MalformedURLException e) {
				xHandler.sendMessage(xHandler.obtainMessage(13));
			} catch (IOException e) {
				xHandler.sendMessage(xHandler.obtainMessage(14));
			} catch (Exception e) {
				xHandler.sendMessage(xHandler.obtainMessage(13));
			}
			
			// 取消下载对话框显示
			if (mContext != null)
				mDialog.dismiss();
		}
	};

	/**
	 * 安装APK文件
	 */
	private void installApk() {
		File apkfile = new File(mSavePath, apkName);
		if (!apkfile.exists()) {
			return;
		}
		// 通过Intent安装APK文件
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
		mContext.startActivity(i);
	}
}
