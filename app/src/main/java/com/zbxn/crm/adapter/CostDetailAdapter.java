package com.zbxn.crm.adapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Path;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zbxn.R;
import com.zbxn.crm.activity.custom.NewCostActivity;
import com.zbxn.crm.entity.CostDetailEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/1/13.
 */
public class CostDetailAdapter extends BaseAdapter{
    private List<CostDetailEntity> mList;
    private Context mContext;

    public CostDetailAdapter(List<CostDetailEntity> mList, Context mContext) {
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
            convertView = View.inflate(mContext, R.layout.list_item_costdetail, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tt_project.setText(mList.get(position).getFeeProject());
        holder.tt_count.setText(mList.get(position).getFee());
        holder.tt_time.setText(mList.get(position).getFeeTimeStr());
        holder.tt_remark.setText(mList.get(position).getRemark());
        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.tt_project)
        TextView tt_project;
        @BindView(R.id.tt_count)
        TextView tt_count;
        @BindView(R.id.tt_time)
        TextView tt_time;
        @BindView(R.id.tt_remark)
        TextView tt_remark;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
