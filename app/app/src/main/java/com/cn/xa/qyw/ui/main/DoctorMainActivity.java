package com.cn.xa.qyw.ui.main;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.fastjson.JSONObject;
import com.cn.xa.qyw.DoctorApplication;
import com.cn.xa.qyw.R;
import com.cn.xa.qyw.adapter.MessageAdapter;
import com.cn.xa.qyw.base.DoctorBaseActivity;
import com.cn.xa.qyw.dialog.CameraDialog;
import com.cn.xa.qyw.dialog.DoctorBaseDialog;
import com.cn.xa.qyw.dialog.SelectCityDialog;
import com.cn.xa.qyw.entiy.CameraOperate;
import com.cn.xa.qyw.entiy.UserInfo;
import com.cn.xa.qyw.http.HttpAddress;
import com.cn.xa.qyw.http.HttpUtils;
import com.cn.xa.qyw.http.NetworkResponseHandler;
import com.cn.xa.qyw.preference.PreferenceKeys;
import com.cn.xa.qyw.preference.PreferenceUtils;
import com.cn.xa.qyw.redpackage.RedPacketActivity;
import com.cn.xa.qyw.ui.city.City;
import com.cn.xa.qyw.ui.city.DoctorSelectCityActivity;
import com.cn.xa.qyw.ui.login.LoginActivity;
import com.cn.xa.qyw.ui.main.fragment.DiscoverFragment;
import com.cn.xa.qyw.ui.main.fragment.HomeFragment;
import com.cn.xa.qyw.ui.main.fragment.MessageFragment;
import com.cn.xa.qyw.ui.main.fragment.ShopFragment;
import com.cn.xa.qyw.ui.slide.AboutDoctorAppActivity;
import com.cn.xa.qyw.ui.slide.AccountCenterActivity;
import com.cn.xa.qyw.ui.slide.DoctorSettingActivity;
import com.cn.xa.qyw.ui.slide.FeedBackActivity;
import com.cn.xa.qyw.ui.slide.MyGodActivity;
import com.cn.xa.qyw.ui.userinfo.AddUserInfoActivity;
import com.cn.xa.qyw.ui.userinfo.UserInfoUpdateActivity;
import com.cn.xa.qyw.update.WantUpdate;
import com.cn.xa.qyw.utils.DensityUtils;
import com.cn.xa.qyw.utils.IFileUtils;
import com.cn.xa.qyw.utils.Lg;
import com.cn.xa.qyw.utils.StringUtils;
import com.cn.xa.qyw.utils.ToastUtils;
import com.cn.xa.qyw.view.DoctorViewPager;
import com.facebook.drawee.view.SimpleDraweeView;
import com.loopj.android.http.RequestParams;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushManager;

import net.simonvt.menudrawer.MenuDrawer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import io.rong.imageloader.utils.L;
import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;

import static com.cn.xa.qyw.R.style.dialog;

public class DoctorMainActivity extends DoctorBaseActivity {

    public static final int SELECT_CITY_CODE = 10;
    private TextView[] mMenuTextView = new TextView[4];
    private ImageView[] mMenuImageView = new ImageView[4];
    private int[] titleResouceIds = new int[]{
            R.string.first_menu, R.string.second_menu, R.string.third_menu, R.string.four_menu
    };

    private MenuDrawer mDrawer;
    private TextView releaseTv, discoverTv, remindTv, mineTv, mTitle, mTvLocationCity, mTvUserName;
    private ListView mDrawerList;
    private ImageView releaseImage, discoverImage, remindImage, mineImage;
    private View releaseMenu, discoverMenu, remindMenu, mineMenu;
    private boolean isExitWant;
    private DoctorViewPager mViewPager;
    private ArrayList<Fragment> listFragment;
    private SimpleDraweeView userPhoto ,mOpenDraw;

    /**
     * 会话列表的fragment
     */
    private static MessageFragment mConversationListFragment = null;
    private AroundSlideAdapter adapter;
    private WantFragmentAdapter fragmentAdapter;
    private TextView mTipViewCount;
    public static UserInfo mUserInfo;
    private MaterialDialog mDialog;
    private Dialog mRedDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToolbar.setVisibility(View.GONE);
        initView();
        initListener();
        initData();
        WantUpdate.init(this);
        register();

        if (mUserInfo != null) {
            if (mUserInfo.getType() == 2 && StringUtils.isEmpty(mUserInfo.getBrithday())) {
                showCompleteUserInfoDialog();
            }
        }

        XGPushManager.registerPush(this, new XGIOperateCallback() {
            @Override
            public void onSuccess(Object data, int flag) {
                Lg.e("注册成功，设备token为：" + data);
            }
            @Override
            public void onFail(Object data, int errCode, String msg) {
                Lg.e("注册失败，错误码：" + errCode + ",错误信息：" + msg);
            }
        });


    }

    private void showJoinRedRain(){


        if(mRedDialog == null){
            View view = LayoutInflater.from(this).inflate(R.layout.layout_join_red_rain,null);
            mRedDialog = new Dialog(this, R.style.dialog);
            mRedDialog.setContentView(view);
            view.findViewById(R.id.jon_red_rain).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mRedDialog.dismiss();
                    startActivity(getNewIntent(RedPacketActivity.class));
                }
            });
        }

        if(!mRedDialog.isShowing()){
            mRedDialog.show();
        }
    }

    private void initData() {
        adapter = new AroundSlideAdapter(this);
        mDrawerList.setAdapter(adapter);
        updateMenu(0);
        initViewPager();
        mViewPager.setCurrentItem(0);
        String city = PreferenceUtils.getPrefString(PreferenceKeys.DEFAULT_CITY, "西安");
        mTvLocationCity.setText("[" + city + "]");
    }

    public void queryUserInfo() {
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

                        if (mUserInfo.getType() == 2 && StringUtils.isEmpty(mUserInfo.getBrithday())) {
                            showCompleteUserInfoDialog();
                            return;
                        }

                        if (mUserInfo.getUserSex() == 0) {
                            if(mUserInfo.getType() == 2||mUserInfo.getType() == 1){
                                showCompleteUserInfoDialog();
                                return;
                            }
                        }


                        io.rong.imlib.model.UserInfo userInfo = new io.rong.imlib.model.UserInfo(mUserInfo.getUserId(), StringUtils.getUserName(mUserInfo), Uri.parse(HttpAddress.PHOTO_URL + mUserInfo.getPhoto()));
                        RongIM.getInstance().setCurrentUserInfo(userInfo);
                        RongIM.getInstance().setMessageAttachedUserInfo(true);


                        if (StringUtils.isEmpty(mUserInfo.getNickName())) {

                            if (!StringUtils.isEmpty(mUserInfo.getTrueName())) {
                                mTvUserName.setText(mUserInfo.getTrueName());
                            } else {
                                mTvUserName.setText(DoctorApplication.mUser.getUserName());
                            }
                        } else {
                            mTvUserName.setText(mUserInfo.getNickName());
                        }

                        if (!StringUtils.isEmpty(mUserInfo.getPhoto())) {
                            userPhoto.setImageURI(HttpAddress.PHOTO_URL + mUserInfo.getPhoto());
                            mOpenDraw.setImageURI(HttpAddress.PHOTO_URL + mUserInfo.getPhoto());
                        }

                    } else {

                        showCompleteUserInfoDialog();

                    }

                }
            });
        }

    }

    private void showCompleteUserInfoDialog() {

        if (mDialog != null && mDialog.isShowing()) {
            return;
        }

        mDialog = new MaterialDialog.Builder(DoctorMainActivity.this)
                .title("温馨提示")
                .content("您的资料未完善，为给您带去更好的服务，请先完善您的个人信息！")
                .cancelable(false)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                        if (mUserInfo != null && (mUserInfo.getUserSex() == 0 || StringUtils.isEmpty(mUserInfo.getBrithday()))) {
                            Intent intent = getNewIntent(UserInfoUpdateActivity.class);
                            intent.putExtra("type", "modif");
                            startActivity(intent);
                            return;
                        }

                        startActivity(getNewIntent(AddUserInfoActivity.class));
                    }
                })
                .negativeText("下次完善")
                .positiveText("去完善")
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (DoctorApplication.mUser != null) {

            if (mUserInfo == null) {
                queryUserInfo();
            } else {
                mTvUserName.setText(StringUtils.getUserName(mUserInfo));
                if (!StringUtils.isEmpty(mUserInfo.getPhoto())) {
                    userPhoto.setImageURI(HttpAddress.PHOTO_URL + mUserInfo.getPhoto());
                    mOpenDraw.setImageURI(HttpAddress.PHOTO_URL + mUserInfo.getPhoto());
                }

            }
        } else {
            userPhoto.setImageURI("");
            mOpenDraw.setImageURI("");
            mTvUserName.setText("用户登录");
        }

        if (RongIM.getInstance() != null) {
            RongIM.getInstance().setOnReceiveUnreadCountChangedListener(new RongIM.OnReceiveUnreadCountChangedListener() {
                @Override
                public void onMessageIncreased(int i) {
                    Lg.e("未读条数==================" + i);

                    setCount(i);

                }
            }, new Conversation.ConversationType[]{
                    Conversation.ConversationType.PRIVATE, Conversation.ConversationType.PUBLIC_SERVICE
                    , Conversation.ConversationType.CUSTOMER_SERVICE, Conversation.ConversationType.APP_PUBLIC_SERVICE, Conversation.ConversationType.SYSTEM});
        }


    }

    private void initViewPager() {
        listFragment = new ArrayList<Fragment>();
        listFragment.add(new HomeFragment());
        listFragment.add(new DiscoverFragment());
        listFragment.add(initConversationList());
        listFragment.add(new ShopFragment());

        fragmentAdapter = new WantFragmentAdapter(getSupportFragmentManager(), listFragment);
        mViewPager.setAdapter(fragmentAdapter);

    }

    private Fragment initConversationList() {
        if (mConversationListFragment == null) {
            mConversationListFragment = MessageFragment.getInstance();
            mConversationListFragment.setOnClickListener(this);
            MessageAdapter mAdapter = new MessageAdapter(RongContext.getInstance());
            mConversationListFragment.setAdapter(mAdapter);

            Uri uri;
            if (true) {
                uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                        .appendPath("conversationlist")
                        .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话是否聚合显示
                        .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "false")//群组
                        .appendQueryParameter(Conversation.ConversationType.PUBLIC_SERVICE.getName(), "false")//公共服务号
                        .appendQueryParameter(Conversation.ConversationType.APP_PUBLIC_SERVICE.getName(), "false")//订阅号
                        .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "false")//系统
                        .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "false")
                        .build();
            } else {
                uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                        .appendPath("conversationlist")
                        .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话是否聚合显示
                        .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "false")//群组
                        .appendQueryParameter(Conversation.ConversationType.PUBLIC_SERVICE.getName(), "false")//公共服务号
                        .appendQueryParameter(Conversation.ConversationType.APP_PUBLIC_SERVICE.getName(), "false")//订阅号
                        .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "true")//系统
                        .build();
            }
            mConversationListFragment.setUri(uri);
            return mConversationListFragment;
        } else {
            return mConversationListFragment;
        }
    }

    private void initListener() {
        mOpenDraw.setOnClickListener(this);
        releaseMenu.setOnClickListener(this);
        discoverMenu.setOnClickListener(this);
        remindMenu.setOnClickListener(this);
        mineMenu.setOnClickListener(this);
        userPhoto.setOnClickListener(this);
        mTvLocationCity.setOnClickListener(this);


        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int resourceId = adapter.getItem(position);

                Intent intent = new Intent();
                intent.putExtra("title", resourceId);

                if (resourceId == R.string.user_ceter) {

                    if (!DoctorBaseDialog.isShowLoginDialog(DoctorMainActivity.this)) {
                        intent.setClass(DoctorMainActivity.this, MyGodActivity.class);
                    } else return;


                } else if (resourceId == R.string.shop_guide) {
                    intent.setClass(DoctorMainActivity.this, FeedBackActivity.class);
                } else if (resourceId == R.string.inviting_friends) {
                    intent.setClass(DoctorMainActivity.this, AboutDoctorAppActivity.class);
                } else if (resourceId == R.string.setting) {
                    intent.setClass(DoctorMainActivity.this, DoctorSettingActivity.class);
                } else if (resourceId == R.string.accout_ceter) {

                    if (!DoctorBaseDialog.isShowLoginDialog(DoctorMainActivity.this)) {
                        intent.setClass(DoctorMainActivity.this, AccountCenterActivity.class);
                    } else return;


                }

                startActivity(intent);

                if (mDrawer.isMenuVisible()) {
                    mDrawer.closeMenu();
                } else {
                    mDrawer.toggleMenu();
                }

            }
        });
    }

    private void setMenuImage(int wantResourceId, int discoverResourceId, int remindResourceId, int mineResourceId) {
        mMenuImageView[0].setImageResource(wantResourceId);
        mMenuImageView[1].setImageResource(discoverResourceId);
        mMenuImageView[2].setImageResource(remindResourceId);
        mMenuImageView[3].setImageResource(mineResourceId);
    }

    private void updateMenu(int position) {
        for (int i = 0; i < 4; i++) {
            if (i == position) {
                mViewPager.setCurrentItem(i);
                mMenuTextView[i].setTextColor(getResources().getColor(R.color.color_bg_green));
            } else {
                mMenuTextView[i].setTextColor(getResources().getColor(R.color.home_menu_text_normal_color));
            }
        }

        if (position == 0) {
            mTvLocationCity.setVisibility(View.VISIBLE);
            setMenuImage(R.mipmap.release_home_want_press, R.mipmap.release_home_discover_normal, R.mipmap.release_home_remind_normal, R.mipmap.release_home_mine_normal);
        } else if (position == 1) {
            mTvLocationCity.setVisibility(View.GONE);
            setMenuImage(R.mipmap.release_home_want_normal, R.mipmap.release_home_discover_press, R.mipmap.release_home_remind_normal, R.mipmap.release_home_mine_normal);
        } else if (position == 2) {
            mTvLocationCity.setVisibility(View.GONE);
            setMenuImage(R.mipmap.release_home_want_normal, R.mipmap.release_home_discover_normal, R.mipmap.release_home_remind_press, R.mipmap.release_home_mine_normal);
        } else if (position == 3) {
            mTvLocationCity.setVisibility(View.GONE);
            setMenuImage(R.mipmap.release_home_want_normal, R.mipmap.release_home_discover_normal, R.mipmap.release_home_remind_normal, R.mipmap.release_home_mine_press);
        }

        mTitle.setText(titleResouceIds[position]);

    }

    @Override
    public void initContentView() {
        mDrawer = MenuDrawer.attach(this, MenuDrawer.Type.OVERLAY);
        mDrawer.setContentView(R.layout.imkeli_base_activity);
        mDrawer.setMenuView(R.layout.main_menu_draw_view);
        mDrawer.setMenuSize(DensityUtils.dpToPx(270));
    }

    private void initView() {
        mTitle = (TextView) findViewById(R.id.main_toolbar_title);
        mTvUserName = (TextView) findViewById(R.id.user_name);
        mTvLocationCity = (TextView) findViewById(R.id.location_city);
        userPhoto = (SimpleDraweeView) findViewById(R.id.imageView);
        mDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_BEZEL);
        mDrawer.setDropShadowSize(2);

        mDrawerList = (ListView) findViewById(R.id.navdrawer);
        mOpenDraw = (SimpleDraweeView) findViewById(R.id.img_icon_draw);
        mViewPager = (DoctorViewPager) findViewById(R.id.viewpager_want_main);
        mViewPager.setNoScroll(true);
        mViewPager.setOffscreenPageLimit(6);
        initBottomMenu();
    }

    private void initBottomMenu() {
        releaseTv = (TextView) findViewById(R.id.release_menu_textview);
        discoverTv = (TextView) findViewById(R.id.discover_menu_textview);
        remindTv = (TextView) findViewById(R.id.remind_menu_textview);
        mineTv = (TextView) findViewById(R.id.mine_menu_textview);

        releaseImage = (ImageView) findViewById(R.id.release_menu_imageview);
        discoverImage = (ImageView) findViewById(R.id.discover_menu_imageview);
        remindImage = (ImageView) findViewById(R.id.remind_menu_imageview);
        mineImage = (ImageView) findViewById(R.id.mine_menu_imageview);

        mTipViewCount = (TextView) findViewById(R.id.tip_mine_item_count);

        releaseMenu = findViewById(R.id.release_menu);
        discoverMenu = findViewById(R.id.discover_menu);
        remindMenu = findViewById(R.id.remind_menu);
        mineMenu = findViewById(R.id.mine_menu);

        initMenuTextView();
        initMenuImageView();
    }

    private void initMenuImageView() {
        mMenuImageView[0] = releaseImage;
        mMenuImageView[1] = discoverImage;
        mMenuImageView[2] = remindImage;
        mMenuImageView[3] = mineImage;
    }

    private void initMenuTextView() {
        releaseTv.setText(R.string.first_menu);
        discoverTv.setText(R.string.second_menu);
        remindTv.setText(R.string.third_menu);
        mineTv.setText(R.string.four_menu);
        mMenuTextView[0] = releaseTv;
        mMenuTextView[1] = discoverTv;
        mMenuTextView[2] = remindTv;
        mMenuTextView[3] = mineTv;

    }

    @Override
    public int getChildLayoutId() {
        return R.layout.activity_main;
    }


    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        super.onClick(v);

        int id = v.getId();

        switch (id) {
            case R.id.release_menu:
                updateMenu(0);
                break;
            case R.id.discover_menu:
                updateMenu(1);
                break;
            case R.id.remind_menu:
                updateMenu(2);
                break;
            case R.id.mine_menu:
                updateMenu(3);
                break;
            case R.id.img_icon_draw:
                if (mDrawer.isMenuVisible()) {
                    mDrawer.closeMenu();
                } else {
                    mDrawer.toggleMenu();
                }

                break;
            case R.id.imageView:

                if (!DoctorBaseDialog.isShowLoginDialog(this)) {
                    startActivity(getNewIntent(AccountCenterActivity.class));
                }

                break;
            case R.id.location_city:
                Intent intent = getNewIntent(DoctorSelectCityActivity.class);
                startActivityForResult(intent, SELECT_CITY_CODE);
                break;
            case io.rong.imkit.R.id.rc_empty_tv:

                TextView tv = (TextView) v;
                String str = tv.getText().toString().trim();
                if (getString(io.rong.imkit.R.string.rc_conversation_list_not_connected).equals(str)) {
                    if (!DoctorBaseDialog.isShowLoginDialog(this)) {
                    }
                }

                break;
        }
    }


    @Override
    public void onBackPressed() {
        final int drawerState = mDrawer.getDrawerState();
        if (drawerState == MenuDrawer.STATE_OPEN || drawerState == MenuDrawer.STATE_OPENING) {
            mDrawer.closeMenu();
            return;
        }

        if (isExitWant) {
            DoctorApplication.getInstance().exist();
            return;
        }

        isExitWant = true;
        ToastUtils.showShortSnackbar(mToolbar, "再按一次退出求医网");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isExitWant = false;
            }
        }, 1000);

        if (isExitWant) {
            return;
        }

        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_CITY_CODE) {
                City city = (City) data.getSerializableExtra("city");
                mTvLocationCity.setText("[" + city.getName() + "]");
                PreferenceUtils.setPrefString(PreferenceKeys.DEFAULT_CITY, city.getName());

            }
        }
    }


    private void refreshMsgFragment() {
        Uri uri = Uri.parse("rong://com.cn.xa.qyw").buildUpon()
                .appendPath("conversationlist")
                .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话是否聚合显示
                .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "false")//群组
                .appendQueryParameter(Conversation.ConversationType.PUBLIC_SERVICE.getName(), "false")//公共服务号
                .appendQueryParameter(Conversation.ConversationType.APP_PUBLIC_SERVICE.getName(), "false")//订阅号
                .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "false")//系统
                .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "false")
                .build();

        if (mConversationListFragment != null) {
            mConversationListFragment.initFragment(uri);
        }
    }

    private BroadcastReceiver myReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("send_login".equals(action)) {
                refreshMsgFragment();
            } else if ("send_logout".equals(action)) {
                if (mConversationListFragment != null) {
                    mConversationListFragment.logout();
                }
            }
        }

    };

    private void register() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("send_login");
        filter.addAction("send_logout");
        registerReceiver(myReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myReceiver);
    }

    private void setCount(int count) {

        if (count == 0) {
            mTipViewCount.setVisibility(View.GONE);
        } else {
            mTipViewCount.setVisibility(View.VISIBLE);
        }

        mTipViewCount.setText("" + count);
    }

    /**
     * 检测SD卡是否可用
     */
    public static boolean isSDCardActive() {
        boolean isActive = false;

        if (Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            isActive = true;
        }

        return isActive;
    }
}
