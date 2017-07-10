package com.zbxn.crm.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zbxn.R;
import com.zbxn.crm.entity.StaticTypeEntity;
import com.zbxn.main.listener.ICustomListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author: ysj
 * @date: 2017-01-11 10:04
 */
public class AddFiltrateAdapter extends BaseAdapter {

    private Context mContext;
    private List<StaticTypeEntity> mList = new ArrayList<>();
    private int mType;
    private ICustomListener listener;

    public AddFiltrateAdapter(Context mContext, List<StaticTypeEntity> mList, int mType, ICustomListener listener) {
        this.mContext = mContext;
        this.mList = mList;
        this.mType = mType;
        this.listener = listener;
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
            convertView = View.inflate(mContext, R.layout.list_item_addfiltrate, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvAddFiltrate.setText(mList.get(position).getValue());
        if (mType == 0) {//未添加
            holder.imgAddDelete.setImageResource(R.mipmap.custom_add);
            holder.imgAddDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onCustomListener(0, null, position);
                }
            });
        } else if (mType == 1) {//已添加
            holder.imgAddDelete.setImageResource(R.mipmap.delete);
            holder.imgAddDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onCustomListener(1, null, position);
                }
            });
        }
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.img_add_delete)
        ImageView imgAddDelete;
        @BindView(R.id.tv_addFiltrate)
        TextView tvAddFiltrate;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
