package com.zbxn.main.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pub.utils.ConfigUtils;
import com.pub.utils.StringUtils;
import com.zbxn.R;
import com.zbxn.main.entity.MissionCommendListEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by wj on 2016/11/15.
 * 评论列表的适配器
 */
public class MissionCommentListViewAdapter extends BaseAdapter {
    private Context mCxontext;
    private List<MissionCommendListEntity> mList;


    public MissionCommentListViewAdapter(Context mContext, List<MissionCommendListEntity> mList) {
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
            convertView = View.inflate(mCxontext, R.layout.list_item_commend, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //每一个评论对象
        MissionCommendListEntity dataBean = mList.get(position);

        String mBaseUrl = ConfigUtils.getConfig(ConfigUtils.KEY.webServer);
        //头像
//        ImageLoader.getInstance().displayImage(mBaseUrl + dataBean.getPersonHeadUrl(), holder.mPortrait, DisplayImageOptions.createSimple());
        //评论人
        holder.tvCommendName.setText(dataBean.getUserName());
        //评论时间
        String time = dataBean.getCommentDate();
        holder.tvCommendTime.setText(StringUtils.convertDateWithMin(time));
        //评论状态
        holder.tvCommendContent.setText(dataBean.getContent());
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
