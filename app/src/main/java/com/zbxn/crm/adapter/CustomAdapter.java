package com.zbxn.crm.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zbxn.R;
import com.zbxn.crm.entity.CustomListEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author: ysj
 * @date: 2017-01-13 09:33
 */
public class CustomAdapter extends BaseAdapter {

    private Context mContext;
    private List<CustomListEntity> mList;

    public CustomAdapter(Context mContext, List<CustomListEntity> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList != null?mList.size():0;
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.list_item_custom, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        CustomListEntity entity = mList.get(position);
        holder.tvName.setText(entity.getFollowUserName());
        holder.tvCustomName.setText(entity.getCustName());
        holder.tvTime.setText(entity.getUpdateTimeStr());

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.tv_custom_name)
        TextView tvCustomName;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_time)
        TextView tvTime;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
