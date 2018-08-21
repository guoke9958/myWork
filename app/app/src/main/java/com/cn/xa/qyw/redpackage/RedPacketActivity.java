package com.cn.xa.qyw.redpackage;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.cn.xa.qyw.R;
import com.cn.xa.qyw.base.DoctorBaseActivity;

/**
 * Created by Administrator on 2017/7/24.
 */

public class RedPacketActivity extends DoctorBaseActivity implements View.OnClickListener {
    private RedPacketTest redRainView1;
    private int totalCount = 5;
    private int totalmoney = 0;
    AlertDialog.Builder ab;
    private Dialog mRedDialog;
    private TextView mTv;
    private int count = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToolbarTitle.setText("红包雨来了(剩余5次)");
        mToolbar.setBackgroundColor(getResources().getColor(R.color.transparent));
        mToolbarTitle.setTextColor(getResources().getColor(R.color.white));
        mBaseMain.setBackgroundColor(getResources().getColor(R.color.lsq_color_red));

        View view = LayoutInflater.from(this).inflate(R.layout.layout_get_red_bag_tip, null);
        mTv = (TextView) view.findViewById(R.id.tv_tip_red);
        mRedDialog = new Dialog(this, R.style.dialog);
        mRedDialog.setContentView(view);

        view.findViewById(R.id.continue_red).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (totalCount == 0) {
                    finish();
                }

                redRainView1.restartRain();
                mRedDialog.dismiss();
            }
        });

        redRainView1 = (RedPacketTest) findViewById(R.id.red_packets_view1);

        startRedRain();

    }

    @Override
    public int getChildLayoutId() {
        return R.layout.red_rain;
    }

    @Override
    public void onClick(View v) {
    }

    /**
     * 开始下红包雨
     */
    private void startRedRain() {
        redRainView1.startRain();
        redRainView1.setOnRedPacketClickListener(new RedPacketTest.OnRedPacketClickListener() {
            @Override
            public void onRedPacketClickListener(RedPacket redPacket) {
                count++;
                redRainView1.pauseRain();
                mRedDialog.setCancelable(false);

                if (redPacket.isRealRed) {
                    mTv.setText("恭喜你，抢到了" + redPacket.money + "元！\n已将零钱存入账户");
                    totalmoney += redPacket.money;
                } else {
                    mTv.setText("很遗憾，下次继续努力！");
                }
                redRainView1.post(new Runnable() {
                    @Override
                    public void run() {
                        mRedDialog.show();
                    }
                });

                mToolbarTitle.setText("红包雨来了(剩余" + (--totalCount) + "次)");

            }
        });
    }

    /**
     * 停止下红包雨
     */
    private void stopRedRain() {
        totalmoney = 0;//金额清零
        redRainView1.stopRainNow();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopRedRain();
    }
}