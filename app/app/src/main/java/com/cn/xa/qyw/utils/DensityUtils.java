package com.cn.xa.qyw.utils;

import android.app.Activity;
import android.content.Context;
import android.view.Display;

public class DensityUtils {

	private static Context mContext;
	public static int SCREEN_WIDTH_PIXELS;
	public static int SCREEN_HEIGHT_PIXELS;

	public static void init(Activity context){
			mContext = context;

			Display display = context.getWindowManager().getDefaultDisplay();
			SCREEN_WIDTH_PIXELS = display.getWidth();
			SCREEN_HEIGHT_PIXELS = display.getHeight();
		}
	
		/**
		 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
		 */
		public static int dip2px(Context context, float dpValue) {
			final float scale = context.getResources().getDisplayMetrics().density;
			return (int) (dpValue * scale + 0.5f);
		}

		/**
		 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
		 */
		public static int px2dip(Context context, float pxValue) {
			final float scale = context.getResources().getDisplayMetrics().density;
			return (int) (pxValue / scale + 0.5f);
		}
		
		
		/**
		 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
		 */
		public static int dpToPx(float dpValue) {
			final float scale = mContext.getResources().getDisplayMetrics().density;
			return (int) (dpValue * scale + 0.5f);
		}

}
