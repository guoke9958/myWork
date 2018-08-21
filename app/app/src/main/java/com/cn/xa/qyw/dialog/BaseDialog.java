package com.cn.xa.qyw.dialog;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

public class BaseDialog extends Dialog {

	/**
	 * Screen Size Info
	 */
	protected static DisplayMetrics mDisplayMetrics;

	protected BaseDialog(Context context, int width, int height, int layout, int style, int gravity) {
		super(context, style);

		//
		mDisplayMetrics = context.getResources().getDisplayMetrics();

		setContentView(layout);
		Window window = getWindow();
		WindowManager.LayoutParams params = window.getAttributes();
		if (width == -1) {
			params.width = WindowManager.LayoutParams.MATCH_PARENT;
		} else if (width == -2) {
			params.width = WindowManager.LayoutParams.WRAP_CONTENT;
		} else {
			params.width = (int) (width * mDisplayMetrics.density);
		}
		if (height == -1) {
			params.height = WindowManager.LayoutParams.MATCH_PARENT;
		} else if (height == -2) {
			params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		} else {
			params.height = (int) (height * mDisplayMetrics.density);
		}
		params.gravity = gravity;
		window.setAttributes(params);
	}

	@Override
	public void onDetachedFromWindow() {
		super.onDetachedFromWindow();
	}
}
