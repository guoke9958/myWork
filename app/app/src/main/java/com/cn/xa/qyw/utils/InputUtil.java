package com.cn.xa.qyw.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class InputUtil {

	public static void openInput(final Context context, View view) {

		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				InputMethodManager input = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
				input.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
				
			}
		}).start();
	}

	public static void closeInput(Activity context) {
		InputMethodManager inputMethodManager = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);

		inputMethodManager.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(),

		InputMethodManager.HIDE_NOT_ALWAYS);
	}
}
