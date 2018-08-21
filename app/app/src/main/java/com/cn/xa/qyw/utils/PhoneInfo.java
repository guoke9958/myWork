/******************************************************************************
 * File: PhoneInfo.java
 * Author: mengp
 * Create Date : 2014-4-9
 * JDK version used: <JDK1.6> 
 * Version : V1.0
 * Description : 
 * 
 * 
 * 
 * History :
 * 1. mengp add for the first release , 2014-4-9 
 *
 * 
 * Copyright (C), 2001-2013, Xi'an TCL Software Development Co.,Ltd
 * All rights reserved
 ******************************************************************************/
package com.cn.xa.qyw.utils;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

/**
 * @description 手机信息获取
 * @author mengp
 * @date 2014-4-9 上午10:33:21
 */
public class PhoneInfo {

	private TelephonyManager tm;
	private Context mContext;
	private static PhoneInfo sPhoneInfo;

	private PhoneInfo(Context context) {
		this.mContext = context;
		tm = (TelephonyManager) context
				.getSystemService(context.TELEPHONY_SERVICE);
	}

	public static PhoneInfo instance(Context context) {
		synchronized (PhoneInfo.class) {
			if (sPhoneInfo == null) {
				sPhoneInfo = new PhoneInfo(context);
			}
		}
		return sPhoneInfo;
	}

	public static PhoneInfo getInstance() {
		return sPhoneInfo;
	}

	/**
	 * @description 手机sn号
	 * @return
	 */
	public String getPhoneSN() {
		String sn = tm.getSimSerialNumber();
		return sn;
	}

	/**
	 * @description 手机名称
	 * @return
	 */
	public String getPhoneName() {
		String model = android.os.Build.MODEL;
		return model;
	}
	
	public String getAndroidVersion(){
		return android.os.Build.VERSION.RELEASE;
	}
	

	/**
	 * @description 屏幕密度
	 * @return
	 */
	public String getPhoneDensity() {
		DisplayMetrics dm = new DisplayMetrics();
		dm = mContext.getResources().getDisplayMetrics();
		int densityDpi = dm.densityDpi; // 屏幕密度DPI（120 / 160 / 240）
		switch (densityDpi) {
		case 120:
			densityDpi = 1;
			break;
		case 160:
			densityDpi = 2;
			break;
		case 240:
			densityDpi = 3;
			break;
		case 320:
			densityDpi = 4;
			break;
		default:
			break;
		}
		return densityDpi + "";
	}

	/**
	 * @description 获取屏幕宽高
	 * @return
	 */
	public String getPhoneHeightAndWigth() {
		DisplayMetrics dm = new DisplayMetrics();
		dm = mContext.getResources().getDisplayMetrics();
		int width = dm.widthPixels; // 屏幕密度DPI（120 / 160 / 240）
		int height = dm.heightPixels;
		return height + "*" + width;
	}
	
	/**
	 * @description 获取屏幕高
	 * @return
	 */
	public int getPhoneHeight() {
		DisplayMetrics dm = new DisplayMetrics();
		dm = mContext.getResources().getDisplayMetrics();
		// 屏幕密度DPI（120 / 160 / 240）
		int height = dm.heightPixels;
		return height;
	}
	
	/**
	 * @description 获取屏幕宽
	 * @return
	 */
	public int getPhoneWigth() {
		DisplayMetrics dm = new DisplayMetrics();
		dm = mContext.getResources().getDisplayMetrics();
		int width = dm.widthPixels; // 屏幕密度DPI（120 / 160 / 240）
		return  width;
	}
	
	
	
}
