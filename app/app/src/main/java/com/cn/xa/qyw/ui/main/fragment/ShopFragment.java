package com.cn.xa.qyw.ui.main.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.cn.xa.qyw.R;
import com.cn.xa.qyw.adapter.ShowTypeAdapter;
import com.cn.xa.qyw.base.BaseFragment;
import com.cn.xa.qyw.entiy.Shop;
import com.cn.xa.qyw.entiy.ShopType;
import com.cn.xa.qyw.utils.Lg;
import com.shizhefei.mvc.MVCNormalHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016/5/26.
 */
public class ShopFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Lg.e("ShopFragment + onCreateView");
        return inflater.inflate(R.layout.fragment_shop_view, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initView(view);
        initListener();
        initData();
    }

    private void initData() {

    }

    private void initListener() {
    }

    private void initView(View view) {
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

    }
}
