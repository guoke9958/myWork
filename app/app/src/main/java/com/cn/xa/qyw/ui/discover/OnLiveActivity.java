package com.cn.xa.qyw.ui.discover;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.baidu.cloud.media.player.BDCloudMediaPlayer;
import com.baidu.cloud.media.player.IMediaPlayer;
import com.baidu.cloud.videoplayer.widget.BDCloudVideoView;
import com.cn.xa.qyw.R;
import com.cn.xa.qyw.http.HttpAddress;
import com.cn.xa.qyw.utils.Lg;
import com.cn.xa.qyw.utils.StringUtils;

import java.util.Timer;
import java.util.TimerTask;

import io.rong.imageloader.utils.L;

/**
 * Created by 409160 on 2016/7/11.
 */
public class OnLiveActivity extends DiscoverBaseActivity implements IMediaPlayer.OnPreparedListener,
        IMediaPlayer.OnCompletionListener, IMediaPlayer.OnErrorListener,
        IMediaPlayer.OnInfoListener, IMediaPlayer.OnBufferingUpdateListener,
        BDCloudVideoView.OnPlayerStateListener{
    private String mUrl;

    @Override
    public int getChildLayoutId() {
        return R.layout.activity_simple_video_playing;
    }

    @Override
    public void initContentView() {
        setContentView(R.layout.imkeli_base_activity_full);
    }

    private static final String TAG = "SimplePlayActivity";

    /**
     * 您的AK 请到http://console.bce.baidu.com/iam/#/iam/accesslist获取
     */
    private String ak = "474bcb4299f24a58b72057bd4c56de0a"; // 请录入您的AK !!!

    private BDCloudVideoView mVV = null;
    private SimpleMediaController mediaController = null;
    private RelativeLayout mViewHolder = null;

    private Timer barTimer;

    /**
     * 记录播放位置
     */
    private int mLastPos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);
        mUrl = getIntent().getStringExtra("playUrl");
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);

        mToolbar.setVisibility(View.GONE);
        initUI();
    }

    /**
     * 初始化界面
     */
    private void initUI() {
        mViewHolder = (RelativeLayout) findViewById(R.id.view_holder);
        mediaController = (SimpleMediaController) findViewById(R.id.media_controller_bar);
        /**
         * 设置ak
         */
        BDCloudVideoView.setAK(ak);

        mVV = new BDCloudVideoView(this);
        mVV.setLooping(true);

        if(!StringUtils.isEmpty(mUrl)){
            if(mUrl.contains("rtmp://")||mUrl.contains("http://")){
                mVV.setVideoPath(mUrl);
            }else{
                mVV.setVideoPath(HttpAddress.PHOTO_URL + mUrl);
            }
        }

        mVV.setVideoScalingMode(BDCloudVideoView.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
        RelativeLayout.LayoutParams rllp = new RelativeLayout.LayoutParams(-1, -1);
        mViewHolder.addView(mVV, rllp);

        /**
         * 注册listener
         */
        mVV.setOnPreparedListener(this);
        mVV.setOnCompletionListener(this);
        mVV.setOnErrorListener(this);
        mVV.setOnInfoListener(this);
        mVV.setOnBufferingUpdateListener(this);
        mVV.setOnPlayerStateListener(this);

        mediaController.setMediaPlayerControl(mVV);

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v(TAG, "onPause");
        mVV.pause();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v(TAG, "onResume");
        mVV.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.v(TAG, "onStop");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mVV != null) {
            mVV.stopPlayback();
        }
        Log.v(TAG, "onDestroy");
    }

    /**
     * 检测'点击'空白区的事件，若播放控制控件未显示，设置为显示，否则隐藏。
     *
     * @param v
     */
    public void onClickEmptyArea(View v) {
        if (barTimer != null) {
            barTimer.cancel();
            barTimer = null;
        }
        if (this.mediaController != null) {
            if (mediaController.getVisibility() == View.VISIBLE) {
                mediaController.hide();
            } else {
                mediaController.show();
                hideOuterAfterFiveSeconds();
            }
        }
    }

    private void hideOuterAfterFiveSeconds() {
        if (barTimer != null) {
            barTimer.cancel();
            barTimer = null;
        }
        barTimer = new Timer();
        barTimer.schedule(new TimerTask() {

            @Override
            public void run() {
                if (mediaController != null) {
                    mediaController.getMainThreadHandler().post(new Runnable() {

                        @Override
                        public void run() {
                            mediaController.hide();
                        }

                    });
                }
            }

        }, 5 * 1000);

    }

    @Override
    public boolean onInfo(IMediaPlayer mp, int what, int extra) {
        // TODO Auto-generated method stub
        Lg.e("onInfo");
        return false;
    }

    @Override
    public boolean onError(IMediaPlayer mp, int what, int extra) {
        // restart player?
        Lg.e("onError");
        return false;
    }

    @Override
    public void onCompletion(IMediaPlayer mp) {
        Lg.e("onCompletion");
    }

    @Override
    public void onPrepared(IMediaPlayer mp) {
        Lg.e("onPrepared");
    }

    @Override
    public void onBufferingUpdate(IMediaPlayer mp, int percent) {
        if (mediaController != null && mVV != null) {
            mediaController.onTotalCacheUpdate(percent * mVV.getDuration() / 100);
        }
    }

    @Override
    public void onPlayerStateChanged(BDCloudVideoView.PlayerState nowState) {
        if (mediaController != null) {
            mediaController.changeState();
        }
        Lg.e("onPlayerStateChanged   " + nowState);

    }
}
