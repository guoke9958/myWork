package com.cn.xa.qyw.utils;

import android.os.CountDownTimer;

/**
 * 
 * @description
 * @author muyf
 * @date 2015年6月29日 下午4:59:48
 */
public class TimeCount extends CountDownTimer {

	/**
	 * 
	 * @description
	 * @author muyf
	 * @date 2015年7月1日 下午3:06:52
	 */
	public interface ITimerCount {
		public void finish(boolean isEnable);

		public void onTck(boolean isEnable, String strResult);
	}

	private ITimerCount miTimerCount;

	public TimeCount(long millisInFuture, long countDownInterval, ITimerCount iTimerCount) {
		super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔

		this.miTimerCount = iTimerCount;
	}

	/**
	 * 
	 * 
	 * @see CountDownTimer#onFinish()
	 */
	@Override
	public void onFinish() {// 计时完毕时触发
		miTimerCount.finish(true);
	}

	/**
	 * 
	 * @param millisUntilFinished
	 * @see CountDownTimer#onTick(long)
	 */
	@Override
	public void onTick(long millisUntilFinished) {// 计时过程显示
		miTimerCount.onTck(false, millisUntilFinished / 1000 + "秒");
	}

}
