package com.cn.xa.qyw.ui.search;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.cn.xa.qyw.R;
import com.cn.xa.qyw.adapter.SearchResultAdapter;
import com.cn.xa.qyw.base.DoctorBaseActivity;
import com.cn.xa.qyw.datasource.SearchResultDataSource;
import com.cn.xa.qyw.entiy.SearchHot;
import com.cn.xa.qyw.entiy.SimpleDoctor;
import com.cn.xa.qyw.entiy.UserInfo;
import com.cn.xa.qyw.http.HttpAddress;
import com.cn.xa.qyw.http.HttpUtils;
import com.cn.xa.qyw.http.NetworkResponseHandler;
import com.cn.xa.qyw.ui.doctor.DoctorDetailActivity;
import com.cn.xa.qyw.utils.InputUtil;
import com.cn.xa.qyw.utils.StringUtils;
import com.shizhefei.mvc.IDataAdapter;
import com.shizhefei.mvc.MVCNormalHelper;
import com.shizhefei.mvc.MVCUltraHelper;
import com.shizhefei.mvc.OnStateChangeListener;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.header.MaterialHeader;
import me.next.tagview.TagCloudView;

/**
 * Created by Administrator on 2017/1/10.
 */
public class YhtSearchActivity extends DoctorBaseActivity {

    private MVCNormalHelper<List<UserInfo>> mvcHelper;
    private SearchResultDataSource mDataSource;
    private SearchResultAdapter mAdapter;
    private EditText search;
    private ImageView searchImage;
    private ListView mSearchResultList;
    private View mSeachTip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initListener();
        initData();
        getHotData();
    }

    private void getHotData() {
        showDialog();
        HttpUtils.getDataFromServer(HttpAddress.SEARCH_HOT, new NetworkResponseHandler() {
            @Override
            public void onFail(String message) {
                dismissDialog();
            }

            @Override
            public void onSuccess(String data) {
                dismissDialog();
                List<SearchHot> list = JSONObject.parseArray(data,SearchHot.class);

                final List<String> mList = new ArrayList<String>();
                for(SearchHot hot:list){
                    mList.add(hot.getSearchText());
                }

                TagCloudView tagCloudView8 = (TagCloudView) findViewById(R.id.tag_cloud_view);
                tagCloudView8.setTags(mList);
                tagCloudView8.setOnTagClickListener(new TagCloudView.OnTagClickListener() {
                    @Override
                    public void onTagClick(int position) {
                        if (position != -1) {
                            search.setText(mList.get(position));
                            getData();
                        }
                    }
                });

            }
        });
    }

    private void initView() {

        search = (EditText) findViewById(R.id.search);
        search.setVisibility(View.VISIBLE);
        searchImage = (ImageView) findViewById(R.id.search_img);
        searchImage.setVisibility(View.VISIBLE);
        search.setHint("搜索 医生/科室/医院 关键字");
        mSeachTip = findViewById(R.id.search_tip);

        mSearchResultList = (ListView) findViewById(R.id.rotate_header_list_view);
        mvcHelper = new MVCNormalHelper<List<UserInfo>>(mSearchResultList);
        mDataSource = new SearchResultDataSource();
        mvcHelper.setDataSource(mDataSource);
        mAdapter = new SearchResultAdapter(this,mSearchResultList,mSeachTip);
        mvcHelper.setAdapter(mAdapter);

        searchImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (search.getVisibility() == View.VISIBLE) {
                    getData();
                } else {
                    search.setVisibility(View.VISIBLE);
                    search.requestFocus();
                    InputUtil.openInput(YhtSearchActivity.this, search);
                    mToolbarTitle.setVisibility(View.GONE);
                }
            }
        });

    }

    private void getData() {
        String searchText = search.getText().toString().trim();
        if (StringUtils.isEmpty(searchText)) {
            showToast("请输入要搜的医院关键字");
        } else {
            InputUtil.closeInput(YhtSearchActivity.this);
            mSearchResultList.setVisibility(View.VISIBLE);
            mSeachTip.setVisibility(View.GONE);

            mDataSource.setKey(searchText);
            mAdapter.setKey(searchText);
            mvcHelper.refresh();
        }
    }

    private void initListener() {
        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    InputUtil.closeInput(YhtSearchActivity.this);
                    getData();
                    return true;
                }
                return false;
            }
        });

        mSearchResultList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserInfo info = mAdapter.getItem(position);
                SimpleDoctor doctor = new SimpleDoctor();
                doctor.setDcotorProfessionalTitle(info.getProfessionalTitle());
                doctor.setDoctorId(info.getUserId());
                doctor.setDoctorName(info.getTrueName());
                doctor.setDoctorPhoto(info.getPhoto());
                doctor.setHospitalId(info.getHospitalId());
                doctor.setHospitalName(info.getHospitalName());
                Intent intent = getNewIntent(DoctorDetailActivity.class);
                intent.putExtra("doctor",doctor);
                startActivity(intent);
            }
        });

    }

    private void initData() {

    }

    @Override
    public void onBackPressed() {

        if (mSearchResultList.getVisibility() == View.VISIBLE) {
            searchImage.setVisibility(View.VISIBLE);
            mSearchResultList.setVisibility(View.GONE);
            search.setText("");
            mSeachTip.setVisibility(View.VISIBLE);
            return;
        }

        super.onBackPressed();
    }

    @Override
    public int getChildLayoutId() {
        return R.layout.activity_yht_search;
    }

}
