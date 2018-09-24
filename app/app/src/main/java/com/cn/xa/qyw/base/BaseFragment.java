package com.cn.xa.qyw.base;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;

/**
 * Created by Administrator on 2016/5/26.
 */
public class BaseFragment extends Fragment implements View.OnClickListener{

    public Activity myActivity;

    @Override
    public void onClick(View v) {
        myActivity = getActivity();
    }

}
