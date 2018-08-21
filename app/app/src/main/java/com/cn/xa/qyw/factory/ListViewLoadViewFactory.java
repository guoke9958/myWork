/*
Copyright 2015 shizhefei（LuckyJayce）

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package com.cn.xa.qyw.factory;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.widget.TextView;
import android.widget.Toast;

import com.cn.xa.qyw.R;
import com.shizhefei.mvc.ILoadViewFactory;
import com.shizhefei.view.vary.VaryViewHelper;

public class ListViewLoadViewFactory implements ILoadViewFactory {


	@Override
	public ILoadMoreView madeLoadMoreView() {
		return new LoadMoreHelper();
	}

	@Override
	public ILoadView madeLoadView() {
		return new LoadViewHelper();
	}

	private class LoadMoreHelper implements ILoadMoreView {

		protected TextView footView;

		protected OnClickListener onClickRefreshListener;

		@Override
		public void init(FootViewAdder footViewHolder, OnClickListener onClickRefreshListener) {
			footView = (TextView) footViewHolder.addFootView(R.layout.base_layout_listview_foot);
			this.onClickRefreshListener = onClickRefreshListener;
			showNormal();
		}

		@Override
		public void showNormal() {
			footView.setVisibility(View.GONE);
			footView.setOnClickListener(onClickRefreshListener);
		}

		@Override
		public void showLoading() {
			footView.setVisibility(View.VISIBLE);
			footView.setText("正在加载中..");
			footView.setOnClickListener(null);
		}

		@Override
		public void showFail(Exception exception) {
			footView.setVisibility(View.VISIBLE);
			footView.setText("加载失败，点击重新加载");
			footView.setOnClickListener(onClickRefreshListener);
		}

		@Override
		public void showNomore() {
			footView.setVisibility(View.VISIBLE);
			footView.setText("已经加载完毕");
			footView.setOnClickListener(null);
		}

	}

	private class LoadViewHelper implements ILoadView {
		private VaryViewHelper helper;
		private OnClickListener onClickRefreshListener;
		private Context context;

		@Override
		public void init(View switchView, OnClickListener onClickRefreshListener) {
			this.context = switchView.getContext().getApplicationContext();
			this.onClickRefreshListener = onClickRefreshListener;
			helper = new VaryViewHelper(switchView);
		}

		@Override
		public void restore() {
			helper.restoreView();
		}

		@Override
		public void showLoading() {
			View layout = helper.inflate(R.layout.base_progress_dialog);
//			initView(layout);
//			startAnim();
			helper.showLayout(layout);
		}

		@Override
		public void tipFail(Exception exception) {
//			stopAnim();
			Toast.makeText(context, "网络加载失败", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void showFail(Exception exception) {
//			startAnim();
			View layout = helper.inflate(R.layout.base_load_error);
			layout.setOnClickListener(onClickRefreshListener);
			helper.showLayout(layout);
		}

		@Override
		public void showEmpty() {
//			stopAnim();
			View layout = helper.inflate(R.layout.base_load_empty);
			layout.setOnClickListener(onClickRefreshListener);
			helper.showLayout(layout);
		}

	}
}

