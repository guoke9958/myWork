package com.cn.xa.qyw.ui.slide;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alibaba.fastjson.JSONObject;
import com.cn.xa.qyw.DoctorApplication;
import com.cn.xa.qyw.R;
import com.cn.xa.qyw.adapter.WithDrawAccountAdapter;
import com.cn.xa.qyw.base.DoctorBaseActivity;
import com.cn.xa.qyw.entiy.UserAliAccount;
import com.cn.xa.qyw.http.HttpAddress;
import com.cn.xa.qyw.http.HttpUtils;
import com.cn.xa.qyw.http.NetworkResponseHandler;
import com.cn.xa.qyw.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/21.
 */
public class WithDrawAccountActivity extends DoctorBaseActivity {

    private ListView mListView;
    private View mFootView;
    private WithDrawAccountAdapter mAdapter;
    private boolean isSelected;
    private View mBtnAddd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToolbarTitle.setText("提现方式");
        mToolbarRight.setVisibility(View.VISIBLE);
        initView();
        initListener();
        initData();
        getData();
    }

    private void initListener() {
        mToolbarRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isSelected) {
                    isSelected = true;
                } else {
                    isSelected = false;
                }
                mAdapter.notifyDataSetChanged();
                refreshTitleRight();
            }
        });

        mBtnAddd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getNewIntent(AddAlipayAccountActivity.class);
                startActivityForResult(intent, 17);
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserAliAccount item = mAdapter.getItem(position);
                Intent intent = getNewIntent(WithDrawDetailsActivity.class);
                intent.putExtra("item", item);
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 17) {
                getData();
            }
        }
    }

    private void getData() {
        showDialog();
        String userId = DoctorApplication.mUser.getUserId();
        HttpUtils.postDataFromServer(HttpAddress.GET_USER_ALI_ACCOUNT, userId, new NetworkResponseHandler() {
            @Override
            public void onFail(String message) {
                dismissDialog();
                showToast("获取列表失败");
            }

            @Override
            public void onSuccess(String data) {
                dismissDialog();

                isSelected = false;


                if (!StringUtils.isEmpty(data)) {
                    List<UserAliAccount> list = JSONObject.parseArray(data, UserAliAccount.class);
                    mAdapter.setData(list);
                } else {
                    mToolbarRight.setVisibility(View.GONE);
                    List<UserAliAccount> list = new ArrayList<UserAliAccount>();
                    mAdapter.setData(list);
                }

                refreshTitleRight();

            }
        });
    }

    public void refreshTitleRight() {
        if (!isSelected) {
            mToolbarRight.setText("管理");
        } else {
            mToolbarRight.setText("完成");
        }

        if (mAdapter.getCount() == 0) {
            mToolbarRight.setVisibility(View.GONE);
        } else {
            mToolbarRight.setVisibility(View.VISIBLE);
        }

    }


    private void initView() {
        mListView = (ListView) findViewById(R.id.listview);
        mFootView = getLayoutInflater().inflate(R.layout.list_item_footer_withdrawals_style, null);
        mBtnAddd = mFootView.findViewById(R.id.add_alipay);
        mListView.addFooterView(mFootView);
    }

    private void initData() {
        mAdapter = new WithDrawAccountAdapter(this);
        mListView.setAdapter(mAdapter);
    }

    @Override
    public int getChildLayoutId() {
        return R.layout.activity_withdrawals_style;
    }

    public boolean isSelected() {
        return isSelected;
    }
}
