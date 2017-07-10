package com.zbxn.main.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pub.utils.ConfigUtils;
import com.zbxn.R;
import com.zbxn.main.entity.MissionOperateLogListEntity;
import com.zbxn.main.entity.MissionReadListEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by wj on 2016/11/15.
 * 评论列表的适配器
 */
public class CheckListViewAdapter extends BaseAdapter {
    private Context mCxontext;
    private List<MissionReadListEntity> mList;


    public CheckListViewAdapter(Context mContext, List<MissionReadListEntity> mList) {
        this.mCxontext = mContext;
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
            convertView = View.inflate(mCxontext, R.layout.list_item_commend1, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //每一个评论对象
        MissionReadListEntity dataBean = mList.get(position);

        String mBaseUrl = ConfigUtils.getConfig(ConfigUtils.KEY.webServer);
        //头像
//        ImageLoader.getInstance().displayImage(mBaseUrl + dataBean.getPersonHeadUrl(), holder.mPortrait, DisplayImageOptions.createSimple());
        //评论人
        holder.tvCommendName.setText(dataBean.getUserName());
        //评论时间
        String time = dataBean.getReadTime();
//        holder.tvCommendTime.setText(time.substring(0, time.length() - 3));
        String userName = dataBean.getUserName();
        boolean isRead = dataBean.isIsRead();

        //评论状态
        holder.tvCommendContent.setText(dataBean.isIsRead()?"已阅读":"未阅读");
        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.mPortrait)
        CircleImageView mPortrait;
        @BindView(R.id.tv_commend_name)
        TextView tvCommendName;
        @BindView(R.id.tv_commend_time)
        TextView tvCommendTime;
        @BindView(R.id.tv_commend_content)
        TextView tvCommendContent;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
