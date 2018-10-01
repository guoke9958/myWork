package com.cn.xa.qyw.view;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.holder.Holder;
import com.cn.xa.qyw.utils.StringUtils;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by Administrator on 2017/4/23.
 */
public class NetworkImageHolderView implements Holder<String> {
    private SimpleDraweeView imageView;

    @Override
    public View createView(Context context) {
        imageView = new SimpleDraweeView(context);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        return imageView;
    }

    @Override
    public void UpdateUI(Context context, int position, String data) {
        if (StringUtils.isEmpty(data)){

        }else{
            imageView.setImageURI(data);
        }
    }
}
