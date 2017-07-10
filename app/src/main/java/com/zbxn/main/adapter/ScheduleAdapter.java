package com.zbxn.main.adapter;


import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pub.utils.StringUtils;
import com.zbxn.R;
import com.zbxn.main.entity.ScheduleListEntity;
import com.zbxn.main.entity.ScheduleRuleEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2016/9/27.
 */
public class ScheduleAdapter extends BaseAdapter {

    private Context mCxontext;
    private List<ScheduleListEntity> mList;
    private boolean isWorkmate;
    public ScheduleAdapter(Context mCxontext, List<ScheduleListEntity> mList, boolean isWorkmate) {
        this.mCxontext = mCxontext;
        this.mList = mList;
        this.isWorkmate = isWorkmate;
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
            convertView = View.inflate(mCxontext, R.layout.list_item_schedule, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        ScheduleListEntity entity = mList.get(position);



        if (entity.getIsCrossday() == 1) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
            SimpleDateFormat sdf1 = new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA);
            try {
                Date endDate = sdf.parse(entity.getStartTime());
                holder.schedStartTime.setText(sdf1.format(endDate));
                holder.schedEndTime.setVisibility(View.GONE);
                holder.mPotr.setVisibility(View.GONE);
            }catch (ParseException e){
                e.printStackTrace();
            }
        }else {
            holder.mPotr.setVisibility(View.VISIBLE);
            holder.schedEndTime.setVisibility(View.VISIBLE);
            String startTime = entity.getStartTime();
            holder.schedStartTime.setText(StringUtils.convertDateMin(startTime));
            String enTime = entity.getEndTime();
            holder.schedEndTime.setText(StringUtils.convertDateMin(enTime));
            if (entity.isIsAllday()) {
                holder.schedStartTime.setText("全天");
                holder.mPotr.setVisibility(View.GONE);
                holder.schedEndTime.setVisibility(View.GONE);
            }else {
                holder.mPotr.setVisibility(View.VISIBLE);
                holder.schedEndTime.setVisibility(View.VISIBLE);
            }
        }

        holder.schedName.setText(entity.getTitle());
        holder.createName.setText(entity.getCreateUserName());
        holder.schedContent.setText(entity.getTitle());
        holder.myContent.setVisibility(View.GONE);

        // (0:创建的日程 1：分享的日程 2：参与的日程)
//        if ("100".equals(entity.getPermission())) {
//            holder.schedCircle.setImageResource(R.mipmap.schedule_circle_type1);
//        } else if ("110".equals(entity.getPermission())) {
//            holder.schedCircle.setImageResource(R.mipmap.schedule_circle_type2);
//        } else if ("111".equals(entity.getPermission())) {
//            holder.schedCircle.setImageResource(R.mipmap.schedule_circle_type3);
//        }
//        if (isWorkmate){
//            holder.schedCircle.setImageResource(R.mipmap.schedule_circle_type4);
//        }
        //现在的参数没用，这个以后再改


        holder.schedCircle.setImageResource(R.mipmap.schedule_circle_type4);
        return convertView;
    }


    static class ViewHolder {
        @BindView(R.id.sched_start_time)
        TextView schedStartTime;
        @BindView(R.id.sched_end_time)
        TextView schedEndTime;
        @BindView(R.id.sched_circle)
        ImageView schedCircle;
        @BindView(R.id.sched_image)
        CircleImageView schedImage;
        @BindView(R.id.sched_name)
        TextView schedName;
        @BindView(R.id.sched_content)
        TextView schedContent;
        @BindView(R.id.sched_location)
        TextView schedLocation;
        @BindView(R.id.myName)
        LinearLayout myName;
        @BindView(R.id.myContent)
        LinearLayout myContent;
        @BindView(R.id.create_name)
        TextView createName;
        @BindView(R.id.mPort)
        TextView mPotr;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


}
