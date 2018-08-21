package com.cn.xa.qyw.ui.slide;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.fastjson.JSONObject;
import com.cn.xa.qyw.DoctorApplication;
import com.cn.xa.qyw.R;
import com.cn.xa.qyw.entiy.FeedBack;
import com.cn.xa.qyw.http.HttpAddress;
import com.cn.xa.qyw.http.HttpUtils;
import com.cn.xa.qyw.http.NetworkResponseHandler;
import com.cn.xa.qyw.utils.DateUtils;
import com.cn.xa.qyw.utils.StringUtils;

/**
 * Created by Administrator on 2016/7/26.
 */
public class FeedBackActivity extends SlideBaseActivity {

    private TextView mTextView;
    private EditText mEditContent;
    private EditText mEditeContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToolbarRight.setVisibility(View.VISIBLE);
        mToolbarRight.setText("提 交");
        initView();
        initListener();
    }

    private void initView() {
        mTextView = (TextView)findViewById(R.id.tv_feedback_name);
        mEditContent = (EditText)findViewById(R.id.et_content);
        mEditeContact = (EditText)findViewById(R.id.et_contact);

    }

    private void initListener() {
        mTextView.setOnClickListener(this);

        mToolbarRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendServerFeedBack();
            }
        });
    }

    private void sendServerFeedBack() {
        String questionClass = mTextView.getText().toString().trim();
        String content = mEditContent.getText().toString().trim();
        if(StringUtils.isEmpty(content)){
            showToast("反馈内容不能为空");
        }else{
            String name = "";
            if(DoctorApplication.mUser!=null){
                name = DoctorApplication.mUser.getUserName();
            }else{
                name = mEditeContact.getText().toString().trim();
            }

            if(StringUtils.isEmpty(name)){
                showToast("请您留下联系方式，我们会尽快处理您的反馈");
            }else{
                if(StringUtils.isTelActive(name)||StringUtils.isEmailActive(name)){

                    FeedBack fb = new FeedBack();
                    fb.setContent(content);
                    fb.setQuestion(questionClass);
                    fb.setName(name);
                    fb.setCreateTime(DateUtils.getCurrentTimestamp());

                    showDialog();
                    HttpUtils.postDataFromServer(HttpAddress.ADD_FEED_BACK, JSONObject.toJSONString(fb), new NetworkResponseHandler() {
                        @Override
                        public void onFail(String message) {
                            dismissDialog();
                            showToast("反馈提交失败");
                        }

                        @Override
                        public void onSuccess(String data) {
                            dismissDialog();
                            showTipDialog();
                        }
                    });

                }else{
                    showToast("您的联系方式不正确，请确认联系方式是否正确");
                }
            }

        }


    }

    private void showTipDialog() {
        new MaterialDialog.Builder(this)
                .title("选择问题类型")
                .cancelable(false)
                .content("您好！\n您的反馈我们已经收到，很高兴您为我们提供宝贵的意见，稍后会有我们的工作人员和您联系。")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        finish();
                    }
                })
                .positiveText("确定")
                .show();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if(id == R.id.tv_feedback_name){
            selectQuestionClass();
        }
    }

    private void selectQuestionClass(){

        new MaterialDialog.Builder(this)
                .title("选择问题类型")
                .items(R.array.item_question)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        mTextView.setText(text);
                    }
                })
                .show();
    }

    @Override
    public int getChildLayoutId() {
        return R.layout.activity_feedback;
    }
}
