package com.cn.xa.qyw.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.telephony.TelephonyManager;

/**
 * @author lyj
 * 
 */
public class NetUtils {

	public static final String TAG = "NetUtil";

	private volatile static NetUtils sInstance;

	private static ConnectivityManager CM;

	private static TelephonyManager TM;

	public static NetUtils getInstance(Context context) {
		if (sInstance == null) {

			sInstance = new NetUtils(context.getApplicationContext());

		}
		return sInstance;
	}

	private NetUtils(Context context) {
		init(context);
	}

	private void init(Context context) {
		
			CM = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);


	
			TM = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
		
	}

	/**
	 * @Function: Determine whether the WIFI is connected
	 * @Description:
	 * @return boolean
	 */
	public static boolean isWifiConnected() {
		NetworkInfo info = CM.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		return info.isConnected();
	}

	/**
	 * @Function: Determine whether the Mobile net is connected
	 * @Description:
	 * @return boolean
	 */
	public static boolean isMobileNetConnected() {
		NetworkInfo info = CM.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		return info.isConnected();
	}

	/**
	 * @Function: Determine whether the NET is connected
	 * @Description:
	 * @return boolean
	 */
	public static boolean isNetConnected() {
		NetworkInfo info = CM.getActiveNetworkInfo();
		if (info != null) {
			return info.isAvailable();
		}
		return false;
	}

	
	public static void setWifiNeverSleep(Context context){
		int wifiSleepPolicy=0;
		wifiSleepPolicy=Settings.System.getInt(context.getContentResolver(),
				                               Settings.System.WIFI_SLEEP_POLICY,
				                               Settings.System.WIFI_SLEEP_POLICY_DEFAULT);
		System.out.println("---> 修改前的Wifi休眠策略值 WIFI_SLEEP_POLICY="+wifiSleepPolicy);
		
		
		Settings.System.putInt(context.getContentResolver(),
				               Settings.System.WIFI_SLEEP_POLICY,
				               Settings.System.WIFI_SLEEP_POLICY_NEVER);
		
		
		wifiSleepPolicy=Settings.System.getInt(context.getContentResolver(),
                Settings.System.WIFI_SLEEP_POLICY,
                Settings.System.WIFI_SLEEP_POLICY_DEFAULT);
        System.out.println("---> 修改后的Wifi休眠策略值 WIFI_SLEEP_POLICY="+wifiSleepPolicy);
	}
	
}
