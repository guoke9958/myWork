package com.cn.xa.qyw.ui.order;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.cn.xa.qyw.R;
import com.cn.xa.qyw.base.DoctorBaseActivity;
import com.cn.xa.qyw.entiy.CommodityOrderData;
import com.cn.xa.qyw.entiy.SimpleDoctor;
import com.cn.xa.qyw.entiy.UserInfo;
import com.cn.xa.qyw.http.HttpAddress;
import com.cn.xa.qyw.http.HttpUtils;
import com.cn.xa.qyw.http.NetworkResponseHandler;
import com.cn.xa.qyw.ui.news.wrapRecyclerview.base.ViewHolder;
import com.cn.xa.qyw.utils.DateUtils;
import com.cn.xa.qyw.utils.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.loopj.android.http.RequestParams;


/**
 * 商品详情
 */
public class CommodityDetailsActivity extends DoctorBaseActivity {

    private SimpleDoctor mDoctor;
    private String mGrade;
    private String mGradeId;
    private View mParent;
    private TextView productName;
    private TextView productPrice;
    private TextView productNum;
    private TextView productTypeName;
    private TextView productOrigin;
    private TextView productAddress;
    private TextView productUpdateTime;
    private TextView productDisc;
    private SimpleDraweeView doctorPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDoctor = (SimpleDoctor) getIntent().getSerializableExtra("doctor");
        mGrade = getIntent().getStringExtra("grade");
        mGradeId = getIntent().getStringExtra("grade_id");
        mToolbarTitle.setText(mDoctor.getDoctorName() + "详情");

        initData();
        initView();

    }

    private void initData() {
        showDialog();
        RequestParams params = new RequestParams();
        params.put("productId",mDoctor.getId());
        HttpUtils.getDataFromServer(HttpAddress.GET_NEW_COLUMN_PRODUCTDETAIL, params, new NetworkResponseHandler() {
            @Override
            public void onFail(String message) {
                dismissDialog();
                mParent.setVisibility(View.VISIBLE);
                ToastUtils.showShortSnackbar(mToolbar, message);
            }

            @Override
            public void onSuccess(String data) {
                dismissDialog();
                CommodityOrderData commodityOrderData = JSONObject.parseObject(data, CommodityOrderData.class);
                initViewData(commodityOrderData);
                mParent.setVisibility(View.VISIBLE);
            }
        });
    }

    private void initViewData(CommodityOrderData commodityOrderData) {
        doctorPhoto.setImageURI(commodityOrderData.getProductPic());

        productName.setText("商品名称：" + commodityOrderData.getProductName());
        productPrice.setText("商品价格：" + commodityOrderData.getProductPrice() + "元");
        productNum.setText("商品数量：" + commodityOrderData.getProductNum() +" "+ commodityOrderData.getSpecifications());
        productTypeName.setText("商品分类：" + commodityOrderData.getTypeName());

        productOrigin.setText("来源：" + commodityOrderData.getAreaName() + "--" + commodityOrderData.getSellerName());
        productAddress.setText("地址：" +  commodityOrderData.getSellerAddress());

        productDisc.setText("描述：" +  commodityOrderData.getProductDisc());
        productUpdateTime.setText("更新时间：" + DateUtils.convertToTime(commodityOrderData.getUpdateTime(),"yyyy-MM-dd"));
    }

    private void initView() {
        mParent = findViewById(R.id.parent);
        mParent.setVisibility(View.GONE);
        doctorPhoto = (SimpleDraweeView)findViewById(R.id.doctor_photo);
        productName = (TextView)findViewById(R.id.product_name);
        productPrice = (TextView)findViewById(R.id.product_price);
        productNum = (TextView)findViewById(R.id.product_num);
        productTypeName = (TextView)findViewById(R.id.product_type_name);

        productOrigin = (TextView)findViewById(R.id.product_origin);
        productAddress = (TextView)findViewById(R.id.product_address);

        productDisc = (TextView)findViewById(R.id.product_disc);
        productUpdateTime = (TextView)findViewById(R.id.product_update_time);

    }


    @Override
    public int getChildLayoutId() {
        return R.layout.activity_commodity_detail;
    }
}
