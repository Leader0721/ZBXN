package com.zbxn.crm.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pub.utils.DateUtils;
import com.zbxn.R;
import com.zbxn.crm.entity.OperationLogEntity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author: ysj
 * @date: 2017-01-17 14:50
 */
public class OperationLogAdapter extends BaseAdapter {

    private Context mContext;
    private List<OperationLogEntity> mList;
    private String tagTime = "";
    private SimpleDateFormat sdfDay = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");
    private Date date;
    private List<Boolean> booleanList;

    public OperationLogAdapter(Context mContext, List<OperationLogEntity> mList) {
        this.mContext = mContext;
        this.mList = mList;
        booleanList = new ArrayList<>();
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
            convertView = View.inflate(mContext, R.layout.list_item_child_log, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        OperationLogEntity entity = mList.get(position);
        holder.tvName.setText(entity.getCreateUserName());
        holder.tvContent.setText(entity.getOperateRecord());
        String time = entity.getCreateTimeStr();
        date = DateUtils.toDate(time);
        if (position == 0) {
            tagTime = "";
        }
        if (booleanList.size() <= position) {
            if (tagTime.equals(sdfDay.format(date))) {
                holder.tvTimeDay.setVisibility(View.GONE);
                booleanList.add(position, false);
            } else {
                holder.tvTimeDay.setVisibility(View.VISIBLE);
                booleanList.add(position, true);
            }
        }
        if (booleanList.get(position)) {
            holder.tvTimeDay.setVisibility(View.VISIBLE);
        } else {
            holder.tvTimeDay.setVisibility(View.GONE);
        }
        holder.tvTime.setText(sdfTime.format(date));
        holder.tvTimeDay.setText(sdfDay.format(date));
        tagTime = sdfDay.format(date);
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.tv_time_day)
        TextView tvTimeDay;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_content)
        TextView tvContent;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
