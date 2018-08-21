package com.cn.xa.qyw.dialog;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.cn.xa.qyw.R;
import com.cn.xa.qyw.adapter.SelectCityAdapter;
import com.cn.xa.qyw.city.SelectCity;
import com.cn.xa.qyw.utils.Lg;

import java.util.HashMap;

/**
 * Created by 409160 on 2016/12/6.
 */
public class SelectCityDialog {

    private final ListView mListView;
    private final MaterialDialog dialog;
    private int mType;
    private SelectCityAdapter mAdapter;
    private HashMap<Integer, String> mMap = new HashMap<>();

    public SelectCityDialog(Activity context, int type, final String key, final boolean isShow,final boolean isArea,
                            final OnSelectListener listener,int index) {
        mType = type;
        mListView = (ListView) LayoutInflater.from(context).inflate(R.layout.custom_list_view, null);

        if (mAdapter == null) {
            mAdapter = new SelectCityAdapter(context);
        }

        if (type == 0) {

            if(index==1){
                mAdapter.initData(SelectCity.getInstance().getAllmProvinceDatas(), type,isShow);
            }else{
                mAdapter.initData(SelectCity.getInstance().getmProvinceDatas(), type,isShow);
            }

        } else if (type == 1) {
            if(index==1){
                mAdapter.initData(SelectCity.getInstance().getAllmCitisDatas(key), type,isShow);
            }else{
                mAdapter.initData(SelectCity.getInstance().getmCitisDatas(key), type,isShow);
            }

        } else if (type == 2) {
            if(index==1){
                mAdapter.initData(SelectCity.getInstance().getAllDistrictDatas(key), type,isShow);
            }else{
                mAdapter.initData(SelectCity.getInstance().getDistrictDatas(key), type,isShow);
            }

        }
        mListView.setSelected(true);
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String first = mAdapter.getItem(position);
                Lg.e(first);

                if(isShow){
                    if (mType == 0) {
                        mType = 1;
                        mMap.put(1,first);

                        if(first.contains("北京")){
                            mAdapter.initData(SelectCity.getInstance().getDistrictDatas(first), mType,isShow);
                        }else if(first.contains("上海")){
                            mAdapter.initData(SelectCity.getInstance().getDistrictDatas(first), mType,isShow);
                        }else if(first.contains("天津")){
                            mAdapter.initData(SelectCity.getInstance().getDistrictDatas(first), mType,isShow);
                        }else if(first.contains("重庆")){
                            mAdapter.initData(SelectCity.getInstance().getDistrictDatas(first), mType,isShow);
                        }else{
                            mAdapter.initData(SelectCity.getInstance().getmCitisDatas(first), mType,isShow);
                        }

                    } else if (mType == 1) {
                        if (position == 0) {
                            mType = 0;
                            mAdapter.initData(SelectCity.getInstance().getmProvinceDatas(), mType,isShow);
                        } else {

                            if(!isArea){
                                dialog.dismiss();
                                listener.onSelect(first);
                                return;
                            }

                            mType = 2;
                            mAdapter.initData(SelectCity.getInstance().getDistrictDatas(first), mType,isShow);
                        }
                    }else if(mType==2){
                        if (position == 0) {
                            first = mMap.get(1);
                            mMap.remove(1);
                            mType = 1;
                            mAdapter.initData(SelectCity.getInstance().getmCitisDatas(first), mType,isShow);
                        }else{
                            dialog.dismiss();
                            listener.onSelect(first);
                            return;
                        }
                    }

                    if (android.os.Build.VERSION.SDK_INT >= 8) {
                        mListView.smoothScrollToPosition(0);
                    } else {
                        mListView.setSelection(0);
                    }

                }else{
                    dialog.dismiss();
                    listener.onSelect(first);
                    return;
                }

            }
        });


        dialog = new MaterialDialog.Builder(context)
                .title("城市选择")
                .customView(mListView, false)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                    }
                })
                .positiveText("取消")
                .show();
    }

    public interface OnSelectListener{
        public void onSelect(String city);
    }

}
