package com.cn.xa.qyw.ui.slide;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.cn.xa.qyw.DoctorApplication;
import com.cn.xa.qyw.R;
import com.cn.xa.qyw.entiy.UserCapital;
import com.cn.xa.qyw.entiy.UserInfo;
import com.cn.xa.qyw.http.HttpAddress;
import com.cn.xa.qyw.http.HttpUtils;
import com.cn.xa.qyw.http.NetworkResponseHandler;
import com.cn.xa.qyw.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class PocketAccountActivity extends SlideBaseActivity{


    private LinearLayout addCardView;
    private TextView addCardNumber;
    private EditText cardNumber;

    private LinearLayout changeView;
    private TextView changeCardNumber;
    private TextView changeLines;

    private UserInfo mUserInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToolbarTitle.setText("通惠账户");
        initView();
        getData();
    }

    private void initView() {
        addCardView = (LinearLayout)findViewById(R.id.add_card_view);
        addCardNumber =  (TextView)findViewById(R.id.add_card_number);
        cardNumber = (EditText)findViewById(R.id.card_number);

        changeView = (LinearLayout)findViewById(R.id.change_view);
        changeCardNumber =  (TextView)findViewById(R.id.change_card_number);
        changeLines =  (TextView)findViewById(R.id.change_lines);

        addCardNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(addCardNumber.getText())){
                    showToast("通惠卡号不能为空");
                }else {
                    if (addCardNumber.getText().toString().trim().length() < 8){
                        showToast("请输入正确的通惠卡号");
                    }else{
                        setAddCardNumber();
                    }
                }
            }
        });
    }

    /**
     * 获取初始化数据
     */
    public void getData() {
        if (DoctorApplication.mUser != null) {
            String url = HttpAddress.GET_USER_INFO + "?data=" + DoctorApplication.mUser.getUserId();
            HttpUtils.postDataFromServer(url, new NetworkResponseHandler() {
                @Override
                public void onFail(String messsage) {
                }

                @Override
                public void onSuccess(String data) {
                    if (!StringUtils.isEmpty(data)) {
                        mUserInfo = JSONObject.parseObject(data, UserInfo.class);
//                        if (){
//                            changeView.setVisibility(View.VISIBLE);
//                        }else{
//                            addCardView.setVisibility(View.VISIBLE);
//                        }
                    } else {
                        addCardView.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
    }


    /**
     * 绑定通慧卡
     */
    public void setAddCardNumber(){
        Map<String,String> mapData = new HashMap<>();
        mapData.put("userId",DoctorApplication.mUser.getUserId());
        mapData.put("cardId",DoctorApplication.mUser.getUserId());
        HttpUtils.postDataFromServer(HttpAddress.GET_NEW_SAVE_USER_CARD,JSONObject.toJSONString(mapData), new NetworkResponseHandler() {
            @Override
            public void onFail(String messsage) {
            }

            @Override
            public void onSuccess(String data) {
                addCardView.setVisibility(View.GONE);
                changeView.setVisibility(View.VISIBLE);
                changeCardNumber.setText("");
                changeLines.setText(StringUtils.isEmpty(data)?"0元":"" + "元");
            }
        });
    }


    @Override
    public int getChildLayoutId() {
        return R.layout.activity_pocket_account;
    }


}
