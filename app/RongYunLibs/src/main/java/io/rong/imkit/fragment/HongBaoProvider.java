package io.rong.imkit.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Toast;

import io.rong.imkit.R;
import io.rong.imkit.RongContext;
import io.rong.imkit.widget.provider.InputProvider;

/**
 * Created by Administrator on 2016/9/1.
 */
public class HongBaoProvider extends InputProvider.ExtendProvider {

    private final RongContext mContext;

    public HongBaoProvider(RongContext context) {
        super(context);
        mContext = context;
    }

    @Override
    public Drawable obtainPluginDrawable(Context context) {
        return context.getResources().getDrawable(R.drawable.chat_bottom_function_help_bg_pressed);
    }

    @Override
    public CharSequence obtainPluginTitle(Context context) {
        return "打赏";
    }

    @Override
    public void onPluginClick(View view) {

        Intent intent = new Intent();
        intent.setAction("start_hongbao");
        mContext.sendBroadcast(intent);

    }
}
