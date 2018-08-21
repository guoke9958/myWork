package com.cn.xa.qyw.adapter;

import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.internal.MDAdapter;
import com.cn.xa.qyw.R;
import com.cn.xa.qyw.entiy.MaterialSimpleListItem;

import java.util.ArrayList;
import java.util.List;

/**
 * See the sample project to understand how this is used. Mimics the Simple List dialog style
 * displayed on Google's guidelines site: https://www.google.com/design/spec/components/dialogs.html#dialogs-simple-dialogs
 *
 * @author Aidan Follestad (afollestad)
 */
public class MaterialSimpleListAdapter extends BaseAdapter implements MDAdapter {

    public interface Callback {
        void onMaterialListItemSelected(MaterialDialog dialog, int index);
    }

    private MaterialDialog dialog;
    private List<MaterialSimpleListItem> mItems;
    private Callback mCallback;

    public MaterialSimpleListAdapter(Callback callback) {
        mItems = new ArrayList<>(4);
        mCallback = callback;
    }

    public void add(MaterialSimpleListItem item) {
        mItems.add(item);
        notifyDataSetChanged();
    }

    public void clear() {
        mItems.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    public MaterialSimpleListItem getItem(int index) {
        return mItems.get(index);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public void setDialog(MaterialDialog dialog) {
        this.dialog = dialog;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
         View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.md_simplelist_item, parent, false);
        SimpleListVH holder = new SimpleListVH(view, this);

        if (dialog != null) {
            final MaterialSimpleListItem item = mItems.get(position);
            if (item.getIcon() != null) {
                holder.icon.setImageDrawable(item.getIcon());
                holder.icon.setPadding(item.getIconPadding(), item.getIconPadding(),
                        item.getIconPadding(), item.getIconPadding());
                holder.icon.getBackground().setColorFilter(item.getBackgroundColor(),
                        PorterDuff.Mode.SRC_ATOP);
            } else {
                holder.icon.setVisibility(View.GONE);
            }
            holder.title.setTextColor(dialog.getBuilder().getItemColor());
            holder.title.setText(item.getContent());
            dialog.setTypeface(holder.title, dialog.getBuilder().getRegularFont());

            view.setTag(position);
        }

        return view;
    }

    public static class SimpleListVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        final ImageView icon;
        final TextView title;
        final MaterialSimpleListAdapter adapter;

        public SimpleListVH(View itemView, MaterialSimpleListAdapter adapter) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(android.R.id.icon);
            title = (TextView) itemView.findViewById(android.R.id.title);
            this.adapter = adapter;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = (Integer) view.getTag();
            if (adapter.mCallback != null)
                adapter.mCallback.onMaterialListItemSelected(adapter.dialog,position);
        }
    }
}