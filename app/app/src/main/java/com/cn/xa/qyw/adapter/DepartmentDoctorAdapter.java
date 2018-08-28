package com.cn.xa.qyw.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cn.xa.qyw.R;
import com.cn.xa.qyw.entiy.SimpleDoctor;
import com.cn.xa.qyw.http.HttpAddress;
import com.cn.xa.qyw.utils.StringUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.shizhefei.mvc.IDataAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/18.
 */
public class DepartmentDoctorAdapter extends BaseAdapter {
    private final Context mContext;
    private final LayoutInflater mInflater;
    private final String mGrade;
    private List<SimpleDoctor> mData;


    public DepartmentDoctorAdapter(Context context,String mGrade) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        mData = new ArrayList<>();
        this.mGrade = mGrade;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public SimpleDoctor getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mData.get(position).getId();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder = null;
        if (view == null) {
            holder = new ViewHolder();
            view = mInflater.inflate(R.layout.item_department_grid_view, null);
            holder.userPhoto = (SimpleDraweeView) view.findViewById(R.id.doctor_photo);
            holder.userName = (TextView) view.findViewById(R.id.doctor_name);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        SimpleDoctor doctor = getItem(position);

        if(!StringUtils.isEmpty(doctor.getDoctorPhoto())){
            if(doctor.getDoctorPhoto().contains("http://")){
                holder.userPhoto.setImageURI(doctor.getDoctorPhoto());
            }else{
                holder.userPhoto.setImageURI(Uri.parse(HttpAddress.PHOTO_URL + doctor.getDoctorPhoto()));
            }
        }



        if(mGrade.contains("学校")){
            holder.userName.setText(doctor.getNickName());
        }else{
            holder.userName.setText(doctor.getDoctorName());
        }

        return view;
    }

    public void setData(List<SimpleDoctor> addDepartmentses) {
        mData.clear();
        mData.addAll(addDepartmentses);
        notifyDataSetChanged();
    }

    class ViewHolder {
        SimpleDraweeView userPhoto;
        TextView userName;
    }

}
