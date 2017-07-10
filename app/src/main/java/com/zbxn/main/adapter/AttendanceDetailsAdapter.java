package com.zbxn.main.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pub.utils.PreferencesUtils;
import com.zbxn.R;
import com.zbxn.main.activity.login.LoginActivity;
import com.zbxn.main.entity.AttendanceRecordEntity;
import com.zbxn.main.listener.ICustomListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author: ysj
 * @date: 2017-02-15 13:20
 */
public class AttendanceDetailsAdapter extends BaseAdapter {

    private Context mContext;
    private List<AttendanceRecordEntity> mList;
    private ICustomListener mListener;

    private List<Boolean> inLineList = new ArrayList<>();
    private List<Boolean> outLineList = new ArrayList<>();
    private SimpleDateFormat sfDay = new SimpleDateFormat("MM-dd");
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private String mUserId;

    public AttendanceDetailsAdapter(Context mContext, List<AttendanceRecordEntity> mList, ICustomListener mListener) {
        this.mContext = mContext;
        this.mList = mList;
        this.mListener = mListener;
        mUserId = PreferencesUtils.getString(mContext, LoginActivity.FLAG_INPUT_ID);
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
            convertView = View.inflate(mContext, R.layout.list_item_attendance, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final AttendanceRecordEntity entity = mList.get(position);
        //如果是第一个item隐藏上边的线
        if (inLineList.size() <= position) {
            if (position == 0) {
                inLineList.add(true);
            } else {
                inLineList.add(false);
            }
        }
        if (inLineList.get(position)) {
            holder.imgInLineTop.setVisibility(View.INVISIBLE);
            holder.imgInHead.setVisibility(View.VISIBLE);
            holder.tvInHead.setVisibility(View.GONE);
        } else {
            holder.imgInLineTop.setVisibility(View.VISIBLE);
            holder.imgInHead.setVisibility(View.GONE);
            holder.tvInHead.setVisibility(View.VISIBLE);
        }
        //如果是最后一个item隐藏下边的线
        if (outLineList.size() <= position) {
            if (position == mList.size() - 1) {
                outLineList.add(true);
            } else {
                outLineList.add(false);
            }
        }
        if (outLineList.get(position)) {
            holder.imgOutLineBottom.setVisibility(View.INVISIBLE);
        } else {
            holder.imgOutLineBottom.setVisibility(View.VISIBLE);
        }

        holder.tvInAddress.setText("地点:" + entity.getCheckInAddress());
        holder.tvOutAddress.setText("地点:" + entity.getCheckOutAddress());


        if (entity.getCheckInApprealState().equals("-2")) {
            holder.tvInType.setText(entity.getCheckInStateName());
        } else {
            holder.tvInType.setTextColor(mContext.getResources().getColor(R.color.red));
            if (entity.getCheckInApprealState().equals("-1")) {
                holder.tvInButton.setText("未处理");
                holder.tvInButton.setEnabled(false);
            } else if (entity.getCheckInApprealState().equals("0")) {
                holder.tvInType.setText("正常");
                holder.tvInType.setTextColor(mContext.getResources().getColor(R.color.bg_textview_blue));
            } else if (entity.getCheckInApprealState().equals("1")) {
                holder.tvInType.setText("迟到");
            } else if (entity.getCheckInApprealState().equals("2")) {
                holder.tvInType.setText("早退");
            } else if (entity.getCheckInApprealState().equals("3")) {
                holder.tvInType.setText("异常考勤");
            }
        }

        if (entity.getCheckOutApprealState().equals("-2")) {
            holder.tvOutType.setText(entity.getCheckOutStateName());
        } else {
            holder.tvOutType.setTextColor(mContext.getResources().getColor(R.color.red));
            if (entity.getCheckOutApprealState().equals("-1")) {
                holder.tvOutButton.setEnabled(false);
                holder.tvOutButton.setText("未处理");
            } else if (entity.getCheckOutApprealState().equals("0")) {
                holder.tvOutType.setText("正常");
                holder.tvOutType.setTextColor(mContext.getResources().getColor(R.color.bg_textview_blue));
            } else if (entity.getCheckOutApprealState().equals("1")) {
                holder.tvOutType.setText("迟到");
            } else if (entity.getCheckOutApprealState().equals("2")) {
                holder.tvOutType.setText("早退");
            } else if (entity.getCheckOutApprealState().equals("3")) {
                holder.tvOutType.setText("异常考勤");
            }
        }

        //正常
        holder.tvInType.setText(entity.getCheckInStateName());
        if (entity.getCheckInState().equals("1")) {
            holder.tvInButton.setVisibility(View.GONE);
            holder.tvInType.setTextColor(mContext.getResources().getColor(R.color.bg_textview_blue));
            holder.tvInTime.setTextColor(mContext.getResources().getColor(R.color.tvc3));
        } else {
            holder.tvInType.setTextColor(mContext.getResources().getColor(R.color.red));
            holder.tvInTime.setTextColor(mContext.getResources().getColor(R.color.red));
            holder.tvInButton.setVisibility(View.VISIBLE);
            if (entity.getCheckInApprealState().equals("-2")) {

            } else {
                holder.tvInButton.setEnabled(false);
            }
        }
        holder.tvOutType.setText(entity.getCheckOutStateName());
        if (entity.getCheckOutState().equals("1")) {
            holder.tvOutButton.setVisibility(View.GONE);
            holder.tvOutType.setTextColor(mContext.getResources().getColor(R.color.bg_textview_blue));
            holder.tvOutTime.setTextColor(mContext.getResources().getColor(R.color.tvc3));
        } else {
            holder.tvOutType.setTextColor(mContext.getResources().getColor(R.color.red));
            holder.tvOutTime.setTextColor(mContext.getResources().getColor(R.color.red));
            holder.tvOutButton.setVisibility(View.VISIBLE);
            if (entity.getCheckOutApprealState().equals("-2")) {

            } else {
                holder.tvOutButton.setEnabled(false);
            }
        }

        //未签到
        if (entity.getCheckInState().equals("-1")) {
            try {
                holder.tvInTime.setText(sfDay.format(sdf.parse(entity.getAttendanceDate())));
                holder.tvInTime.setTextColor(mContext.getResources().getColor(R.color.red));
                holder.tvInType.setTextColor(mContext.getResources().getColor(R.color.red));
                holder.tvInAddress.setVisibility(View.GONE);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            holder.tvInTime.setText(entity.getCheckInTime());
            holder.tvInAddress.setVisibility(View.VISIBLE);
        }
        //未签退
        if (entity.getCheckOutState().equals("-1")) {
            try {
                holder.tvOutTime.setText(sfDay.format(sdf.parse(entity.getAttendanceDate())));
                holder.tvOutTime.setTextColor(mContext.getResources().getColor(R.color.red));
                holder.tvOutType.setTextColor(mContext.getResources().getColor(R.color.red));
                holder.tvOutAddress.setVisibility(View.GONE);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            holder.tvOutTime.setText(entity.getCheckOutTime());
            holder.tvOutAddress.setVisibility(View.VISIBLE);
        }

        if (!(entity.getUserID() + "").equals(mUserId)) {
            holder.tvInButton.setVisibility(View.GONE);
            holder.tvOutButton.setVisibility(View.GONE);
        }

        //item中的上面的申诉按钮
        holder.tvInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onCustomListener(0, entity, position);
            }
        });
        //item中的下面的申诉按钮
        holder.tvOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onCustomListener(1, entity, position);
            }
        });

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.img_in_line_top)
        ImageView imgInLineTop;
        @BindView(R.id.img_in_head)
        ImageView imgInHead;
        @BindView(R.id.tv_in_head)
        TextView tvInHead;
        @BindView(R.id.img_in_line_bottom)
        ImageView imgInLineBottom;
        @BindView(R.id.layout_in_line)
        LinearLayout layoutInLine;
        @BindView(R.id.tv_in_check)
        TextView tvInCheck;
        @BindView(R.id.tv_in_time)
        TextView tvInTime;
        @BindView(R.id.tv_in_address)
        TextView tvInAddress;
        @BindView(R.id.tv_in_type)
        TextView tvInType;
        @BindView(R.id.tv_in_button)
        TextView tvInButton;
        @BindView(R.id.img_out_line_top)
        ImageView imgOutLineTop;
        @BindView(R.id.tv_out_head)
        TextView tvOutHead;
        @BindView(R.id.img_out_line_bottom)
        ImageView imgOutLineBottom;
        @BindView(R.id.layout_out_line)
        LinearLayout layoutOutLine;
        @BindView(R.id.tv_out_check)
        TextView tvOutCheck;
        @BindView(R.id.tv_out_time)
        TextView tvOutTime;
        @BindView(R.id.tv_out_address)
        TextView tvOutAddress;
        @BindView(R.id.tv_out_type)
        TextView tvOutType;
        @BindView(R.id.tv_out_button)
        TextView tvOutButton;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
