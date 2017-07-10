package com.zbxn.crm.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zbxn.R;
import com.zbxn.crm.entity.StaticTypeEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author: ysj
 * @date: 2017-01-16 15:45
 */
public class FollowTypeAdapter extends BaseAdapter {

    public static String typeId = "";

    private Context mContext;
    private List<StaticTypeEntity> mList;

    public FollowTypeAdapter(Context mContext, List<StaticTypeEntity> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList.size();
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
            convertView = View.inflate(mContext, R.layout.list_item_followtype, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        StaticTypeEntity entity = mList.get(position);
        holder.tvName.setText(entity.getValue());
        if (mList.get(position).getKey().equals(typeId)) {//已选择
            holder.imgFollowType.setImageResource(R.mipmap.checkbox_pressed);
        } else {
            holder.imgFollowType.setImageResource(R.mipmap.checkbox_normal);
        }
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.img_followType)
        ImageView imgFollowType;
        @BindView(R.id.tv_followHint)
        TextView tvFollowHint;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
