package com.cn.xa.qyw.ui.main;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cn.xa.qyw.R;

public class AroundSlideAdapter extends BaseAdapter {

	public static int[][] listSlide ;

	private LayoutInflater inflater;
	private Context context;

	public AroundSlideAdapter(Context context) {
		this.context = context;
		inflater = LayoutInflater.from(context);

		listSlide = new int[][]{
				{R.mipmap.icon_my_account, R.string.accout_ceter,R.mipmap.icon_my_account_left},
				{R.mipmap.icon_my_account, R.string.user_ceter,R.mipmap.icon_my_account_left},
				{R.mipmap.icon_feedback, R.string.shop_guide,R.mipmap.icon_feedback_left},
				{R.mipmap.icon_about_me, R.string.inviting_friends,R.mipmap.icon_about_left},
				{R.mipmap.icon_setting, R.string.setting,R.mipmap.icon_setting_left}
		};
	}

	@Override
	public int getCount() {
		return listSlide.length;
	}

	@Override
	public Integer getItem(int position) {
		return listSlide[position][1];
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View view, ViewGroup viewGroup) {
		if (view == null) {
			view = inflater.inflate(R.layout.slide_listview_item, null);
			ImageView slide_img = (ImageView) view
					.findViewById(R.id.slide_item_img);
			TextView slide_tv = (TextView) view
					.findViewById(R.id.slide_item_tv);
			ImageView left = (ImageView)view.findViewById(R.id.slide_item_left);
			slide_img.setImageResource(listSlide[position][0]);
			slide_tv.setText(listSlide[position][1]);
			left.setImageResource(listSlide[position][2]);
		}

		return view;
	}

}
