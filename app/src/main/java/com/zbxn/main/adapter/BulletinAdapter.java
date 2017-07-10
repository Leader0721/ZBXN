package com.zbxn.main.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.pub.bean.Bulletin;
import com.pub.utils.ConfigUtils;
import com.pub.utils.DateUtils;
import com.zbxn.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 通知公告适配器
 *
 * @author GISirFive
 * @since 2016-7-13 下午9:03:13
 */
public class BulletinAdapter extends BaseAdapter {

    private List<Bulletin> mList;
    private Context mContext;

    public BulletinAdapter(List<Bulletin> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mList.size() != 0 ? mList.size() : 0;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Bulletin getItem(int position) {
        return mList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_mesagecenter, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Bulletin entity = mList.get(position);

        holder.tvTitle.setText(entity.getTitle());
        holder.tvContent.setText(entity.getContent());
        holder.tvTime.setText(DateUtils.fromTodaySimple(entity.getCreatetime()));
        if (entity.isRead() == 1) {
            holder.tvCount.setVisibility(View.GONE);
            holder.tvTitle.setTextColor(mContext.getResources().getColor(R.color.tvc9));
        } else {
            holder.tvCount.setVisibility(View.VISIBLE);
            holder.tvTitle.setTextColor(mContext.getResources().getColor(R.color.tvc3));
        }
        String server = ConfigUtils.getConfig(ConfigUtils.KEY.webServer);
        ImageLoader.getInstance().displayImage(server + entity.getImg(), holder.imgIcon);

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.img_icon)
        CircleImageView imgIcon;
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
