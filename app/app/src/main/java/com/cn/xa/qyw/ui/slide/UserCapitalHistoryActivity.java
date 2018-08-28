package com.cn.xa.qyw.ui.slide;

import android.os.Bundle;
import android.widget.ListView;

import com.alibaba.fastjson.JSONObject;
import com.cn.xa.qyw.DoctorApplication;
import com.cn.xa.qyw.R;
import com.cn.xa.qyw.adapter.GodHistoryAdapter;
import com.cn.xa.qyw.base.DoctorBaseActivity;
import com.cn.xa.qyw.entiy.CapitalHistory;
import com.cn.xa.qyw.entiy.MyGodItem;
import com.cn.xa.qyw.entiy.UserCapital;
import com.cn.xa.qyw.http.HttpAddress;
import com.cn.xa.qyw.http.HttpUtils;
import com.cn.xa.qyw.http.NetworkResponseHandler;
import com.cn.xa.qyw.utils.DateUtils;

import java.util.List;

/**
 * Created by Administrator on 2016/8/29.
 */
public class UserCapitalHistoryActivity extends DoctorBaseActivity {

    private ListView mListView;
    private GodHistoryAdapter mAdapter;
    private int mType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mType = getIntent().getIntExtra("type",1);

        if(MyGodItem.GOD_TYPE_YU_E == mType){
        }else if(MyGodItem.GOD_TYPE_CHONG_ZHI ==mType){
            mToolbarTitle.setText("充值详情");
        }else if(MyGodItem.GOD_TYPE_SHOU_RU == mType){
            mToolbarTitle.setText("收入详情");
        }else if(MyGodItem.GOD_TYPE_XIAO_FEI == mType){
            mToolbarTitle.setText("消费详情");
        }

        initView();
        initListener();
        initData();
    }

    private void initView() {
        mListView = (ListView)findViewById(R.id.my_god_list_view);
        mAdapter = new GodHistoryAdapter(this,mType);
        mListView.setAdapter(mAdapter);
    }

    private void initListener() {

    }

    private void initData() {

        showDialog();

        CapitalHistory history = new CapitalHistory();
        history.setUpdateTime(DateUtils.getCurrentTimestamp());
        history.setUserId(DoctorApplication.mUser.getUserId());
        history.setCapitalType(mType);

        String url = HttpAddress.GET_CAPITAL_HISTORY;

        if(mType==6){
            url = HttpAddress.GET_CAPITAL_HISTORY_INCOME;
            history.setCapitalType(2);
        }


        HttpUtils.postDataFromServer(url, JSONObject.toJSONString(history), new NetworkResponseHandler() {
            @Override
            public void onFail(String error) {
                dismissDialog();
                showToast("获取信息失败");
            }

            @Override
            public void onSuccess(String data) {
                dismissDialog();
                List<CapitalHistory> list = JSONObject.parseArray(data, CapitalHistory.class);
                mAdapter.setData(list);
            }
        });

    }

    @Override
    public int getChildLayoutId() {
        return R.layout.activity_my_god;
    }
}
