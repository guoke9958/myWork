package com.cn.xa.qyw.base;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;

/**
 * Created by Administrator on 2016/5/26.
 */
public class BaseFragment extends Fragment implements View.OnClickListener{

    private Activity mActivity;

    @Override
    public void onClick(View v) {
      mActivity = getActivity();
    }
}
