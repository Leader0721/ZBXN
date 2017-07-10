package com.zbxn.crm.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pub.utils.StringUtils;
import com.zbxn.R;
import com.zbxn.crm.activity.custom.CustomFiltrateSave;
import com.zbxn.crm.entity.StaticTypeEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author: ysj
 * @date: 2017-01-11 13:06
 */
public class FiltratePopupAdapter extends BaseAdapter {

    private List<StaticTypeEntity> mList;
    private Context mContext;


    public FiltratePopupAdapter(List<StaticTypeEntity> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
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
            convertView = View.inflate(mContext, R.layout.list_item_creatform, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.creatForm.setText(mList.get(position).getValue());

        if (StringUtils.isEmpty(CustomFiltrateSave.itemSelect)) {//未选择时
            if (position == 0) {//默认选中第一个
                holder.mLinearLayout.setBackgroundColor(mContext.getResources().getColor(R.color.pub_backgroud_color));
            } else {//其余不选中
                holder.mLinearLayout.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            }
            //设置完后 把第一个item的id记录
            CustomFiltrateSave.itemSelect = mList.get(0).getKey();
        } else {
            if (mList.get(position).getKey().equals(CustomFiltrateSave.itemSelect)) {
                holder.mLinearLayout.setBackgroundColor(mContext.getResources().getColor(R.color.pub_backgroud_color));
            } else {
                holder.mLinearLayout.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            }
        }

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.creat_form)
        TextView creatForm;
        @BindView(R.id.ll_layout)
        LinearLayout mLinearLayout;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
