package com.cn.xa.qyw.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cn.xa.qyw.R;
import com.cn.xa.qyw.entiy.SimpleDoctor;
import com.cn.xa.qyw.entiy.VideoDetail;
import com.cn.xa.qyw.http.HttpAddress;
import com.cn.xa.qyw.utils.StringUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/18.
 */
public class VideoDetailAdapter extends BaseAdapter {
    private final Context mContext;
    private final LayoutInflater mInflater;
    private List<VideoDetail> mData;


    public VideoDetailAdapter(Context context) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        mData = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public VideoDetail getItem(int position) {
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
            view = mInflater.inflate(R.layout.item_video_grid_view, null);
            holder.userPhoto = (SimpleDraweeView) view.findViewById(R.id.doctor_photo);
            holder.userName = (TextView) view.findViewById(R.id.doctor_name);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        VideoDetail video = getItem(position);

        if(!StringUtils.isEmpty(video.getVideoImage())){
            if(video.getVideoImage().contains("http://")){
                holder.userPhoto.setImageURI(video.getVideoImage());
            }else{
                holder.userPhoto.setImageURI(Uri.parse(HttpAddress.PHOTO_URL + video.getVideoImage()));
            }
        }
            holder.userName.setText(video.getVideoName());

        return view;
    }

    public void setData(List<VideoDetail> addDepartmentses) {
        mData.clear();
        mData.addAll(addDepartmentses);
        notifyDataSetChanged();
    }

    class ViewHolder {
        SimpleDraweeView userPhoto;
        TextView userName;
    }

}
