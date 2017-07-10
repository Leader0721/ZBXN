package com.zbxn.main.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pub.utils.DateUtils;
import com.zbxn.R;
import com.zbxn.main.entity.WorkBlog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WorkBlogAdapter extends BaseAdapter {

    private Context mContext;
    private List<WorkBlog> mList;

    public WorkBlogAdapter(Context mContext, List<WorkBlog> mList) {
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
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.list_item_blogcenter, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        WorkBlog item = mList.get(position);

        if (item.isFromMobile()) {// 来自移动端
            holder.mOrigin.setText("来自移动端");
            holder.mOriginImg.setImageResource(R.mipmap.bg_mobile);
        } else {
            holder.mOrigin.setText("来自PC端");
            holder.mOriginImg.setImageResource(R.mipmap.bg_pc);
        }
        holder.mCommentCount.setText("评论:" + item.getCommentNum());
        holder.mCreateWeek.setText(item.getCreateWeek());
        String content = item.getWorkblogcontent();
        content = content.replace("\n", "");
        holder.mContent.setText(content);
        holder.mCreateTime.setText(DateUtils.fromTodaySimple(item.getCreatetime()));

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.mContent)
        TextView mContent;
        @BindView(R.id.mTitle)
        TextView mTitle;
        @BindView(R.id.mCreateTime)
        TextView mCreateTime;
        @BindView(R.id.mCreateWeek)
        TextView mCreateWeek;
        @BindView(R.id.mCommentCount)
        TextView mCommentCount;
        @BindView(R.id.mOriginImg)
        ImageView mOriginImg;
        @BindView(R.id.mOrigin)
        TextView mOrigin;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
