package com.cn.xa.qyw.base;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cn.xa.qyw.DoctorApplication;
import com.cn.xa.qyw.R;
import com.cn.xa.qyw.utils.DensityUtils;
import com.cn.xa.qyw.utils.Lg;
import com.cn.xa.qyw.utils.ToastUtils;
import com.lib.PinWheelDialog;
import com.loopj.android.http.AsyncHttpResponseHandler;

import cz.msebera.android.httpclient.Header;
import io.rong.imkit.MainActivity;

/**
 * Created by Administrator on 2016/5/11.
 */
public abstract class DoctorBaseActivity extends AppCompatActivity implements View.OnClickListener {

    public LinearLayout mBaseLayout;
    public View mToolbar;
    public TextView mToolbarTitle;
    public ImageView btnBack;
    private PinWheelDialog mDialog;
    public TextView mToolbarRight;
    public LinearLayout mBaseMain;
    public static DoctorBaseActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        DoctorApplication.getInstance().listActivity.add(this);
        DensityUtils.init(this);
        initContentView();
        initBaseView();
        initBaseListener();
        initBaseViewData();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

    }

    public static Activity getInstance(){
        return instance;
    }

    public void initContentView() {
        setContentView(R.layout.imkeli_base_activity);
    }

    public void initBaseView() {
        mBaseLayout = (LinearLayout) findViewById(R.id.base_layout_view);
        mBaseMain = (LinearLayout) findViewById(R.id.base_main);
        mToolbar = findViewById(R.id.toolbar);
        mToolbarRight = (TextView)findViewById(R.id.toolbar_text_right);
        mToolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        btnBack = (ImageView) findViewById(R.id.img_btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        int resourceId = getChildLayoutId();
        if (resourceId != 0 && resourceId != -1) {
            View view = getLayoutInflater().inflate(resourceId, null);
            mBaseLayout.addView(view, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
    }

    public Intent getNewIntent(Class cls) {
        Intent intent = new Intent(this, cls);
        return intent;
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, Bundle options) {
        super.startActivityForResult(intent, requestCode, options);
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.activity_push_right_in,
                R.anim.activity_push_left_out);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_push_left_in,
                R.anim.activity_push_right_out);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public abstract int getChildLayoutId();

    private void initBaseListener() {

    }

    public void initBaseViewData() {

    }

    @Override
    public void onClick(View v) {

    }


    public void showDialog() {
        mDialog = new PinWheelDialog(this);
        mDialog.show();
    }

    public void dismissDialog() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mDialog != null) {
                    mDialog.dismiss();
                }
            }
        });
    }

    public void showToast(final String str){
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtils.showShortToast(DoctorBaseActivity.this,str);
            }
        });
    }

    public void showLongToast(final String str){
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(DoctorBaseActivity.this,str,Toast.LENGTH_LONG).show();
            }
        });
    }

}
