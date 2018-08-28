package com.cn.xa.qyw.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.cn.xa.qyw.DoctorApplication;
import com.cn.xa.qyw.R;
import com.cn.xa.qyw.entiy.User;
import com.cn.xa.qyw.http.HttpAddress;
import com.cn.xa.qyw.http.HttpUtils;
import com.cn.xa.qyw.http.NetworkResponseHandler;
import com.cn.xa.qyw.preference.PreferenceKeys;
import com.cn.xa.qyw.preference.PreferenceUtils;
import com.cn.xa.qyw.ui.main.DoctorMainActivity;
import com.cn.xa.qyw.ui.userinfo.ModifyUserIdActivity;
import com.cn.xa.qyw.ui.welcome.WelcomeActivity;
import com.cn.xa.qyw.utils.Lg;
import com.cn.xa.qyw.utils.StringUtils;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushManager;

import java.util.ArrayList;

import io.rong.imkit.MainActivity;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserInfo;

/**
 * Created by Administrator on 2016/9/1.
 */
public class GuideActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    ArrayList<View> viewContainter = new ArrayList<View>();
    private TextView tvShow;
    int count = 6;
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        initView();
        initListener();
        initData();
    }

    private void startThread() {
        new Thread() {

            @Override
            public void run() {
                try {

                    while (count >= 0) {
                        setTvShow();
                        sleep(1000);

                        if (count == 0) {
                            startWelcomActivity();
                        }
                        count--;

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }.start();
    }

    private void setTvShow() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvShow.setText("跳过(" + count + ")");
            }
        });
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.guide_view_pager);
        tvShow = (TextView) findViewById(R.id.tiaoguo);
        tvShow.setVisibility(View.GONE);
        tvShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count = -1;
                startWelcomActivity();
            }
        });
    }

    private void startWelcomActivity() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                count = -1;
                startActivity(new Intent(GuideActivity.this, DoctorMainActivity.class));
                finish();
            }
        });

    }

    private void initListener() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initData() {
        View view1 = LayoutInflater.from(this).inflate(R.layout.guide_tab1, null);
        View view2 = LayoutInflater.from(this).inflate(R.layout.guide_tab2, null);
        View view3 = LayoutInflater.from(this).inflate(R.layout.guide_tab3, null);
        View view0 = LayoutInflater.from(this).inflate(R.layout.guide_tab0, null);

        View mBtnStart = view3.findViewById(R.id.btn_start_app);

        mBtnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    startWelcomActivity();
            }
        });

        viewContainter.add(view0);
        viewContainter.add(view1);
        viewContainter.add(view2);
        viewContainter.add(view3);

        mViewPager.setAdapter(new PagerAdapter() {

            //viewpager中的组件数量
            @Override
            public int getCount() {
                return viewContainter.size();
            }

            //滑动切换的时候销毁当前的组件
            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
                ((ViewPager) container).removeView(viewContainter.get(position));
            }

            //每次滑动的时候生成的组件
            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ((ViewPager) container).addView(viewContainter.get(position));
                return viewContainter.get(position);
            }

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }

            @Override
            public int getItemPosition(Object object) {
                return super.getItemPosition(object);
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return "";
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        initLogin();
    }

    private void initLogin() {
        String token = PreferenceUtils.getPrefString(PreferenceKeys.RONG_YUN_TOKEN, "");

        if (!StringUtils.isEmpty(token)) {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    login();
                }
            }, 2000);

        } else if (DoctorApplication.mUser != null) {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startWelcomActivity();
                }
            },2000);

        }else{
            showTvTiaoGuo();
        }
    }

    private void login() {
        String userName = PreferenceUtils.getPrefString(PreferenceKeys.USER_NAME, "");
        String password = PreferenceUtils.getPrefString(PreferenceKeys.USER_PASSWORD, "");

        User user = new User();
        user.setUserName(userName);
        user.setPassword(password);

        HttpUtils.postDataFromServer(HttpAddress.USER_LOGIN, JSONObject.toJSONString(user), new NetworkResponseHandler() {
            @Override
            public void onFail(String error) {
                Lg.e("登陆失败");
                Intent intent = new Intent(GuideActivity.this, DoctorMainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onSuccess(String data) {
                mUser = JSONObject.parseObject(data, User.class);

                try {
                    long phone = Long.parseLong(mUser.getUserName());
                    PreferenceUtils.setPrefString(PreferenceKeys.USER_ID, mUser.getUserId());
                    connectRongYun(mUser.getToken());
                    Lg.e("登陆成功" + data);
                } catch (Exception e) {
                    Lg.e(e);

                    Intent intent = new Intent(GuideActivity.this,ModifyUserIdActivity.class);
                    intent.putExtra("user", mUser);
                    intent.putExtra("type","guide");
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private void connectRongYun(final String token) {
        if (getApplicationInfo().packageName.equals(DoctorApplication.getCurProcessName(this))) {

            /**
             * IMKit SDK调用第二步,建立与服务器的连接
             */
            RongIM.connect(token, new RongIMClient.ConnectCallback() {

                /**
                 * Token 错误，在线上环境下主要是因为 Token 已经过期，您需要向 App Server 重新请求一个新的 Token
                 */
                @Override
                public void onTokenIncorrect() {
                    Lg.e("token===过期");
                }

                /**
                 * 连接融云成功
                 * @param userid 当前 token
                 */
                @Override
                public void onSuccess(String userid) {

                    String token = PreferenceUtils.getPrefString(PreferenceKeys.RONG_YUN_TOKEN, "");

                        DoctorApplication.mUser = mUser;
                        PreferenceUtils.setPrefString(PreferenceKeys.RONG_YUN_TOKEN, token);

//                        UserInfo userInfo = new UserInfo(mUser.getUserId(), mUser.getNickName(), Uri.parse(HttpAddress.UPLOAD_USER_PHOTO + mUser.getUserPhoto()));
//                        RongIM.getInstance().setCurrentUserInfo(userInfo);
//                        RongIM.getInstance().setMessageAttachedUserInfo(true);

                        showTvTiaoGuo();

                }

                /**
                 * 连接融云失败
                 * @param errorCode 错误码，可到官网 查看错误码对应的注释
                 */
                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    Toast.makeText(GuideActivity.this, "连接服务器失败", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void showTvTiaoGuo() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvShow.setVisibility(View.VISIBLE);
                startThread();
            }
        });
    }

}
