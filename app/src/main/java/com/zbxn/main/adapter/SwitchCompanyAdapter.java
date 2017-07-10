package com.zbxn.main.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pub.utils.PreferencesUtils;
import com.zbxn.R;
import com.zbxn.main.activity.login.LoginActivity;
import com.zbxn.main.entity.ZmsCompanyListEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/9/30.
 */
public class SwitchCompanyAdapter extends BaseAdapter {

    private Context mContext;
    private List<ZmsCompanyListEntity> mList;

    public SwitchCompanyAdapter(Context mContext, List<ZmsCompanyListEntity> mList) {
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
            convertView = View.inflate(mContext, R.layout.list_item_switchcompany, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ZmsCompanyListEntity item = mList.get(position);

        holder.myRemind.setText(item.getCompanyname());

        String zmsCompanyId = PreferencesUtils.getString(mContext, LoginActivity.FLAG_ZMSCOMPANYID);
        if (zmsCompanyId.equals(item.getId())) {
            holder.mImageview.setVisibility(View.VISIBLE);
        } else {
            holder.mImageview.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.myRemind)
        TextView myRemind;
        @BindView(R.id.mImageview)
        ImageView mImageview;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
