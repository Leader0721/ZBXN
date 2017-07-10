package com.zbxn.main.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zbxn.R;
import com.zbxn.main.entity.RecTool;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author: ysj
 * @date: 2016-12-15 18:42
 */
public class ToolsAdapter extends BaseAdapter {

    private Context mContext;
    private List<RecTool> mList;
    private LayoutInflater mInflater;

    public ToolsAdapter(Context context, List<RecTool> list) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mList = new ArrayList<>();
        if (list != null)
            mList.addAll(list);
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
            convertView = View.inflate(mContext, R.layout.grideview_item_toolsview, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        RecTool tool = mList.get(position);
        holder.mName.setText(tool.getTitle());
        //动态选择图片的加载
        int drawableId = mContext.getResources().getIdentifier(tool.getImg(), "mipmap", mContext.getPackageName());
        holder.mIcon.setImageResource(drawableId);

        if ("202".equals(tool.getMenuid()) || "203".equals(tool.getMenuid()) || "204".equals(tool.getMenuid())) {
            holder.mTip.setVisibility(View.VISIBLE);
        } else {
            holder.mTip.setVisibility(View.GONE);
        }

        return convertView;
    }


    static class ViewHolder {
        @BindView(R.id.mTip)
        TextView mTip;
        @BindView(R.id.mIcon)
        ImageView mIcon;
        @BindView(R.id.mName)
        TextView mName;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
