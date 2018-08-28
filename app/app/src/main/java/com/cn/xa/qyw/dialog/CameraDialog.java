package com.cn.xa.qyw.dialog;


import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.cn.xa.qyw.R;


public class CameraDialog extends BaseDialog {
	private static int default_width = -1;
	private static int default_height = -2;

	private TextView tvCancel;
	private TextView tvCamera;
	private TextView tvPhoto;

	private CameraListener cameralistener;
	private PhotoListener photolistener;

	public CameraDialog(Context context) {
		super(context, default_width, default_height, R.layout.v_camera_dialog, R.style.CameraDialog, Gravity.BOTTOM);
		this.setCancelable(true);
		init();
	}

	@Override
	public void onDetachedFromWindow() {
		super.onDetachedFromWindow();
	}

	private void init() {
		tvCamera = (TextView) findViewById(R.id.tv_camera);
		tvCamera.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (cameralistener != null) {
					cameralistener.onCamera();
				}
				CameraDialog.this.dismiss();
			}
		});

		tvPhoto = (TextView) findViewById(R.id.tv_xiangce);
		tvPhoto.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (photolistener != null) {
					photolistener.onPhoto();
				}
				CameraDialog.this.dismiss();
			}
		});

		tvCancel = (TextView) findViewById(R.id.tvCancel);
		tvCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				CameraDialog.this.dismiss();
			}
		});
	}

	public void setCameraListener(CameraListener cameralistener) {
		this.cameralistener = cameralistener;
	}

	public interface CameraListener {
		public void onCamera();
	}

	public void setPhotoListener(PhotoListener photolistener) {
		this.photolistener = photolistener;
	}

	public interface PhotoListener {
		public void onPhoto();
	}

}
