package com.zbxn.crm.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pub.utils.ConfigUtils;
import com.zbxn.R;
import com.zbxn.crm.entity.CustomDetailEntity;
import com.zbxn.main.entity.Contacts;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2017/1/17.
 */
public class FollowAdapter extends BaseAdapter {
    private List<CustomDetailEntity> mList;
    private Context mContext;

    public FollowAdapter(List<CustomDetailEntity> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mList != null ? mList.size() : 0;
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
            convertView = View.inflate(mContext, R.layout.grid_item_follow, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        CustomDetailEntity entity = mList.get(position);

        holder.name.setText(mList.get(position).getUserName());
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showStubImage(R.mipmap.userhead_img)          // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.mipmap.userhead_img)  // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.mipmap.userhead_img)       // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)                          // 设置下载的图片是否缓存在SD卡中
                //.displayer(new RoundedBitmapDisplayer(20))  // 设置成圆角图片
                .build();                                   // 创建配置过得DisplayImageOption对象
        String server = ConfigUtils.getConfig(ConfigUtils.KEY.webServer);
//        if (entity.getUserIcon().contains("http")) {
            ImageLoader.getInstance().displayImage(server + mList.get(position).getUserIcon(), holder.icon, options);
//        } else {
//            ImageLoader.getInstance().displayImage("file:///" + mList.get(position).getUserIcon(), holder.icon, options);
//        }
//        if (mList.get(position).getID().equals("-1")) {
//            holder.name.setVisibility(View.GONE);
//            holder.icon.setImageResource(R.mipmap.add_circle);
//        }
        return convertView;
    }


    class ViewHolder {
        @BindView(R.id.iv_icon)
        CircleImageView icon;
        @BindView(R.id.tt_name)
        TextView name;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
