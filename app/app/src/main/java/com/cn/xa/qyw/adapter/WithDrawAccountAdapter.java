package com.cn.xa.qyw.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.cn.xa.qyw.R;
import com.cn.xa.qyw.entiy.UserAliAccount;
import com.cn.xa.qyw.http.HttpAddress;
import com.cn.xa.qyw.http.HttpUtils;
import com.cn.xa.qyw.http.NetworkResponseHandler;
import com.cn.xa.qyw.ui.slide.WithDrawAccountActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/21.
 */
public class WithDrawAccountAdapter extends BaseAdapter implements View.OnClickListener{

    private final LayoutInflater mInflater;
    private final WithDrawAccountActivity mContext;
    private List<UserAliAccount> mList = new ArrayList<>();

    public WithDrawAccountAdapter(WithDrawAccountActivity context){
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public UserAliAccount getItem(int position) {
        return mList.get(position);
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        view = mInflater.inflate(R.layout.list_item_withdrawals_style,null);
        TextView name = (TextView)view.findViewById(R.id.name);
        TextView number = (TextView)view.findViewById(R.id.number);
        ImageView delete = (ImageView)view.findViewById(R.id.error_image);

        delete.setOnClickListener(this);
        delete.setTag(position);

        UserAliAccount item = mList.get(position);
        String nameStr = item.getAliName().substring(1, item.getAliName().length());
        name.setText("*"+nameStr);
        number.setText(item.getAliAccount());

        if(mContext.isSelected()){
           delete.setVisibility(View.VISIBLE);
        }else{
            delete.setVisibility(View.GONE);
        }


        return view;
    }

    @Override
    public void onClick(View v) {

        int position = (int) v.getTag();
        final UserAliAccount item = mList.get(position);

        String nameStr = item.getAliName().substring(1, item.getAliName().length());

        new MaterialDialog.Builder(mContext)
                .title("温馨提示")
                .content("确定要删除支付宝账号*"+nameStr)
                .positiveText(R.string.jiebang_sure)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        deleteUserAliAccount(item);
                    }
                })
                .negativeText("取消")
                .show();


    }

    private void deleteUserAliAccount(final UserAliAccount item) {
        mContext.showDialog();
        HttpUtils.postDataFromServer(HttpAddress.DELETE_USER_ALI_ACCOUNT, item.getId()+"", new NetworkResponseHandler() {
            @Override
            public void onFail(String message) {
                mContext.dismissDialog();
                mContext.showToast("删除失败");
            }

            @Override
            public void onSuccess(String data) {
                mContext.dismissDialog();
                mContext.showToast("删除成功");

                mList.remove(item);
                notifyDataSetChanged();
                mContext.refreshTitleRight();
            }
        });

    }

    public void setData(List<UserAliAccount> list) {
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }
}
