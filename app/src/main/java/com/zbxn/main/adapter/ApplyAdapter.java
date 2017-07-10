package com.zbxn.main.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zbxn.R;
import com.zbxn.main.entity.ApplyEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 项目名称：申请列表的adapter
 * 创建人：LiangHanXin
 * 创建时间：2016/10/10 10:05
 */
public class ApplyAdapter extends BaseAdapter {

    private Context mCxontext;
    private List<ApplyEntity> mList;
    private String mType;

    public ApplyAdapter(Context mContext, List<ApplyEntity> mList, String type) {
        this.mCxontext = mContext;
        this.mList = mList;
        this.mType = type;
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
            convertView = View.inflate(mCxontext, R.layout.list_item_myapproval, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final ApplyEntity entity = mList.get(position);
        /**
         * 把获取到的数据显示出来
         */
        holder.myType.setText(entity.getTitle());
        holder.mTime.setText(entity.getCreateTime());
        holder.mTitle.setText(entity.getStatetext());
        holder.mTvReason.setText(entity.getDescription());
        if ("101".equals(mType)) {
            holder.mName.setText("处理人:" + entity.getCurApproveUserName());
        } else if ("102".equals(mType)) {
            holder.mName.setText("申请人:" + entity.getUserName());
        } else if ("103".equals(mType)) {
            holder.mName.setText("申请人:" + entity.getUserName());
        }

        switch (entity.getType()) {
            case 1://请假
                holder.mImgApply.setImageResource(R.mipmap.apply_leave);
                break;
            case 2://报销
                holder.mImgApply.setImageResource(R.mipmap.apply_reimbursement);
                break;
            case 3://物品
                holder.mImgApply.setImageResource(R.mipmap.apply_items);
                break;
            case 4://工作请示
                holder.mImgApply.setImageResource(R.mipmap.apply_works);
                break;
            case 5://出差
                holder.mImgApply.setImageResource(R.mipmap.apply_evection);
                break;
            case 6://外出
                holder.mImgApply.setImageResource(R.mipmap.apply_go_out);
                break;
            case 7://采购
                holder.mImgApply.setImageResource(R.mipmap.apply_procurement);
                break;
            case 8://付款
                holder.mImgApply.setImageResource(R.mipmap.apply_payment);
                break;
            case 9://用印
                holder.mImgApply.setImageResource(R.mipmap.apply_positive2);
                break;
            case 10://转正
                holder.mImgApply.setImageResource(R.mipmap.apply_positive);
                break;
            case 11://离职
                holder.mImgApply.setImageResource(R.mipmap.apply_workovertime);
                break;
            case 12://加班
                holder.mImgApply.setImageResource(R.mipmap.apply_overtime);
                break;
            default:
                holder.mImgApply.setImageResource(R.mipmap.apply_leave);
                break;
        }

        return convertView;
    }


    static class ViewHolder {
        @BindView(R.id.img_apply)
        ImageView mImgApply;
        @BindView(R.id.myType)
        TextView myType;
        @BindView(R.id.mTime)
        TextView mTime;
        @BindView(R.id.mName)
        TextView mName;
        @BindView(R.id.mTitle)
        TextView mTitle;
        @BindView(R.id.tv_reason)
        TextView mTvReason;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
