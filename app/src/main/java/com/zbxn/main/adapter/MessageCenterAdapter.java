package com.zbxn.main.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.pub.utils.ConfigUtils;
import com.pub.utils.DateUtils;
import com.zbxn.R;
import com.zbxn.main.entity.OaAlertEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/12/19.
 */
public class MessageCenterAdapter extends BaseAdapter {
    private List<OaAlertEntity> mLists;
    private Context mContext;

    public MessageCenterAdapter(List<OaAlertEntity> mLists, Context mContext) {
        this.mLists = mLists;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mLists.size();
    }

    @Override
    public Object getItem(int position) {
        return mLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.list_item_newmessagecenter, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        OaAlertEntity entity = mLists.get(position);
        holder.tvTitle.setText(entity.getTitle());
        holder.tvContent.setText(entity.getContent());
        holder.tvTime.setText(DateUtils.fromTodaySimple(entity.getCreatetime()));

        String server = ConfigUtils.getConfig(ConfigUtils.KEY.server);
        ImageLoader.getInstance().displayImage(server + entity.getImg(), holder.imgIcon);

        if (entity.getUnReadCount() > 0) {
            holder.tvCount.setVisibility(View.VISIBLE);
            if (entity.getUnReadCount() > 99) {
                holder.tvCount.setText("99+");
            } else {
                holder.tvCount.setText(entity.getUnReadCount() + "");
            }
        } else {
            holder.tvCount.setVisibility(View.INVISIBLE);
        }
        return convertView;
    }


    static class ViewHolder {
        @BindView(R.id.img_icon)
        ImageView imgIcon;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_content)
        TextView tvContent;
        @BindView(R.id.tv_count)
        TextView tvCount;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
