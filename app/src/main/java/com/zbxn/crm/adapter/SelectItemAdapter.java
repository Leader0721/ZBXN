package com.zbxn.crm.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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
 * @date: 2017-01-12 09:41
 */
public class SelectItemAdapter extends BaseAdapter {

    private Context mContext;
    private List<StaticTypeEntity> mList;

    public SelectItemAdapter(Context mContext, List<StaticTypeEntity> mList) {
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
            convertView = View.inflate(mContext, R.layout.list_item_select, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvSelect.setText(mList.get(position).getValue());

        switch (CustomFiltrateSave.itemSelect) {
            case "CustState"://客户状态
                setState(CustomFiltrateSave.mStateSelect, holder.imgSelect, position);
                if (StringUtils.isEmpty(CustomFiltrateSave.mStateSelect)) {
                    CustomFiltrateSave.mStateSelect = mList.get(0).getKey();
                }
                break;
            case "Source"://来源
                setState(CustomFiltrateSave.mSourceSelect, holder.imgSelect, position);
                if (StringUtils.isEmpty(CustomFiltrateSave.mSourceSelect)) {
                    CustomFiltrateSave.mSourceSelect = mList.get(0).getKey();
                }
                break;
            case "Industry"://行业
                setState(CustomFiltrateSave.mTradeSelect, holder.imgSelect, position);
                if (StringUtils.isEmpty(CustomFiltrateSave.mTradeSelect)) {
                    CustomFiltrateSave.mTradeSelect = mList.get(0).getKey();
                }
                break;
            default:
                break;
        }

        return convertView;
    }

    /**
     * 设置选中状态
     *
     * @param stateId
     * @param imgView
     * @param position
     */
    private void setState(String stateId, ImageView imgView, int position) {
        if (StringUtils.isEmpty(stateId)) {//未选择时
            if (position == 0) {//默认选中第一个
                imgView.setImageResource(R.mipmap.checkbox_pressed);
            } else {//其余不选中
                imgView.setImageResource(R.mipmap.checkbox_normal);
            }
        } else {
            if (mList.get(position).getKey().equals(stateId)) {//已选择
                imgView.setImageResource(R.mipmap.checkbox_pressed);
            } else {
                imgView.setImageResource(R.mipmap.checkbox_normal);
            }
        }
    }

    static class ViewHolder {
        @BindView(R.id.img_select)
        ImageView imgSelect;
        @BindView(R.id.tv_select)
        TextView tvSelect;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
