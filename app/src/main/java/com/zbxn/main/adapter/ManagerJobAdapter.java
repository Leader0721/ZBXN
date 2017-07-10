package com.zbxn.main.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zbxn.R;
import com.zbxn.main.entity.JobEntity;
import com.zbxn.main.listener.ICustomListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 项目名称：ZBXMobile
 * 创建人：U
 * 创建时间：2017-01-10 11:37:12
 */
public class ManagerJobAdapter extends BaseAdapter {


    private Context mCxontext;
    private List<JobEntity> mList;
    private ICustomListener mListener;

    private int[] src = {R.mipmap.position_total, R.mipmap.position_project, R.mipmap.position_product, R.mipmap.position_personnel, R.mipmap.position_employees};

    public ManagerJobAdapter(Context mContext, List<JobEntity> mList, ICustomListener listener) {
        this.mCxontext = mContext;
        this.mList = mList;
        this.mListener = listener;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mCxontext, R.layout.list_item_jobmanager, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final JobEntity entity = mList.get(position);

        //获取到的数据显示上去
        holder.tvName.setText(entity.getPositionName());
//        holder.tvCount.setText(mList.get(position).getCount());
        holder.ivHead.setImageResource(src[position % 5]);

        holder.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onCustomListener(0, entity, position);
            }
        });

        return convertView;
    }


    static class ViewHolder {
        @BindView(R.id.iv_head)
        ImageView ivHead;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_count)
        TextView tvCount;
        @BindView(R.id.tv_delete)
        TextView tvDelete;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
