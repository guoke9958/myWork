package com.cn.xa.qyw.adapter;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.cn.xa.qyw.R;
import com.cn.xa.qyw.entiy.UserInfo;
import com.cn.xa.qyw.http.HttpAddress;
import com.cn.xa.qyw.ui.search.YhtSearchActivity;
import com.cn.xa.qyw.utils.InputUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.shizhefei.mvc.IDataAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 409160 on 2017/1/16.
 */
public class SearchResultAdapter extends BaseAdapter implements IDataAdapter<List<UserInfo>> {

    private final YhtSearchActivity mActivity;
    private final LayoutInflater mInflater;
    private final ListView mListView;
    private final View mTip;
    private List<UserInfo> mList = new ArrayList<>();
    private String mKey;

    public SearchResultAdapter(YhtSearchActivity activity,ListView listView,View tip){
        mActivity = activity;
        mInflater = LayoutInflater.from(activity);
        mListView = listView;
        mTip = tip;
    }

    public void setKey(String key){
        mKey = key;
    }
    
    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public UserInfo getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder = null;
        if(view == null){
            holder = new ViewHolder();
            view = mInflater.inflate(R.layout.item_listview_search_result,null);

            holder.departmentName = (TextView)view.findViewById(R.id.doctor_department_name);
            holder.hospitalName = (TextView)view.findViewById(R.id.doctor_hospital_name);
            holder.doctorName = (TextView)view.findViewById(R.id.doctor_name);
            holder.doctorPhoto = (SimpleDraweeView)view.findViewById(R.id.doctor_photo);

            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }

        UserInfo info = getItem(position);

        holder.doctorPhoto.setImageURI(HttpAddress.PHOTO_URL + info.getPhoto());

        String key = "<font color=\"#F63B34\">" + mKey + "</font>";

        String userName = info.getTrueName().replace(mKey, key);
        holder.doctorName.setText(Html.fromHtml("姓名："+userName));

        String departmentName = info.getDepartmentName().replace(mKey, key);
        holder.departmentName.setText(Html.fromHtml("科室："+departmentName));

        String hospitalName = info.getHospitalName().replace(mKey, key);
        holder.hospitalName.setText(Html.fromHtml("医院："+hospitalName));

        return view;
    }

    class ViewHolder {
        TextView doctorName;
        TextView hospitalName;
        TextView departmentName;
        SimpleDraweeView doctorPhoto;
    }


    @Override
    public void notifyDataChanged(List<UserInfo> userInfos, boolean isRefresh) {
        mListView.setVisibility(View.VISIBLE);
        InputUtil.closeInput(mActivity);
        if (isRefresh) {
            mList.clear();
        }
        mList.addAll(userInfos);
        notifyDataSetChanged();

        if (isRefresh) {
            mListView.setSelection(0);
        }
    }

    @Override
    public List<UserInfo> getData() {
        return mList;
    }
}
