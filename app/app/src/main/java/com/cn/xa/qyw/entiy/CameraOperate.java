package com.cn.xa.qyw.entiy;
/**
 * Created by 409176 on 2016/7/23.
 * 头像生成 请求标记
 */
public interface CameraOperate {
	/**
	 * 拍照
	 */
	public final int REQ_TAKE_PHOTO = 10003;
	/**
	 * 从相册中选择
	 */
	public final int REQ_GALLERY = 10004;
	/**
	 * 结果
	 */
	public final int REQ_CUT = 10005;
}
