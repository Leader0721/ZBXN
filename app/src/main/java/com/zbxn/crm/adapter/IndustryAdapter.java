package com.zbxn.crm.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zbxn.R;
import com.zbxn.crm.entity.StaticTypeEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/1/11.
 */
public class IndustryAdapter extends BaseAdapter {
    private List<StaticTypeEntity>mList;
    private Context mContext;

    public IndustryAdapter(List<StaticTypeEntity>mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
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
            convertView = View.inflate(mContext, R.layout.list_item_industry, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.industry.setText(mList.get(position).getValue());
        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.industry_item)
        TextView industry;
        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
