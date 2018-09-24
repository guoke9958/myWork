package com.cn.xa.qyw.ui.slide;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.cn.xa.qyw.R;

public class PocketAccountActivity extends SlideBaseActivity{


    private TextView addCardNumber;
    private EditText cardNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToolbarTitle.setText("通惠账户");
        initView();
        getData();
    }

    private void initView() {
        addCardNumber =  (TextView)findViewById(R.id.add_card_number);
        cardNumber = (EditText)findViewById(R.id.card_number);

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

    }


    public void setAddCardNumber(){

    }


    @Override
    public int getChildLayoutId() {
        return R.layout.activity_pocket_account;
    }


}
