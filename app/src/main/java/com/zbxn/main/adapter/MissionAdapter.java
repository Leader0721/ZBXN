package com.zbxn.main.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pub.common.EventCustom;
import com.pub.utils.PreferencesUtils;
import com.pub.utils.StringUtils;
import com.zbxn.R;
import com.zbxn.main.activity.login.LoginActivity;
import com.zbxn.main.activity.mission.MissionDetailsActivity;
import com.zbxn.main.activity.mission.MissionUpdateActivity;
import com.zbxn.main.entity.MissionEntity;

import org.simple.eventbus.Subscriber;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ysj on 2016/11/8.
 */
public class MissionAdapter extends BaseAdapter {

    private Context mCxontext;
    private List<MissionEntity> mList;
    private ItemCallBack callBack;
    private LayoutInflater mInflater;
    private List<View> mListV = new ArrayList<>();

    //按钮标识
    private boolean isAgree;//true:同意 false:通过
    private boolean isStop;//true:拒绝 false:驳回
    private boolean isCommit;//true:提交审核 false:已完成
    private boolean isTrunDownPerson;//是否为监督人

    private String leadId;//负责人id
    private String checkId;//审核人id
    private String executeIds;//执行人id???

    private String loginId;
    private int mState;
    private int mStatePerson;

    /**
     * @param context
     * @param list
     */
    public MissionAdapter(Context context, List list, ItemCallBack callBack) {
        this.mCxontext = context;
        this.mList = list;
        this.loginId = PreferencesUtils.getString(mCxontext, LoginActivity.FLAG_INPUT_ID);
        this.callBack = callBack;
        mInflater = LayoutInflater.from(context);
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
        final ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mCxontext, R.layout.list_item_mission, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final MissionEntity entity = mList.get(position);

        leadId = entity.getChargerId() + "";//负责人id
        executeIds=entity.getID();
        checkId = entity.getReviewerId() + "";//审核人id
//        isTrunDownPerson = entity.isTaskTrunDownPerson();//是否为监督人

        holder.mLeadName.setText(entity.getChargerName());
        holder.mCheckName.setText(entity.getReviewerName());

        holder.tvProgress.setText(Html.fromHtml("子任务完成度：<font color='#FF8C00'>"
                + entity.getFinshChildTaskCount() + "</font>/" + entity.getChildTaskCount()));

        holder.mExecuteName.setText(entity.getTransactors());

        holder.mName.setText(entity.getCreatorName());//创建人姓名
        holder.missionProgress.setText("完成进度:" + entity.getProgress() + "%");//完成进度s


//        holder.creatTime.setText(entity.getCreateTime().substring(0, 10));//创建时间
        if (!StringUtils.isEmpty(entity.getEndTime())) {
            holder.endTime.setText(getWeekData(entity.getEndTime()));//限期
        } else {
            holder.endTime.setText("");//限期
        }
        holder.mContent.setText(entity.getTitle());//内容
        if (entity.getDifferentlyLevel() == 0) { //普通任务
            holder.missionLevel.setText("普通");//任务级别
        } else if (entity.getDifferentlyLevel() == 1) { // 困难任务
            holder.missionLevel.setText("困难");//任务级别
        }
        mState = entity.getTaskStatus();
        mStatePerson = entity.getPersonTaskState();

        holder.mState.setTextColor(Color.WHITE);
        holder.mState.setBackgroundDrawable(mCxontext.getResources().getDrawable(R.drawable.bg_mission_shape));
        GradientDrawable myGrad = (GradientDrawable) holder.mState.getBackground();
        //任务状态
        String taskState = "";

        if (entity.isVerfiy()) {
            taskState = "待审核";
            myGrad.setColor(Color.parseColor("#f7c326"));
        }
        if (entity.isOverTime()) {
            taskState = "已超期";
            myGrad.setColor(Color.parseColor("#f34a58"));
        }
        if (entity.isRebut()) {
            taskState = "被驳回";
            myGrad.setColor(Color.parseColor("#f6873d"));
        }
        if (entity.isAccepted()) {
            taskState = "待接受";
            myGrad.setColor(Color.parseColor("#2aa9ef"));
        }
        if (entity.isRefused()) {
            taskState = "已拒绝";
            myGrad.setColor(Color.parseColor("#9ebdc5"));
        }
        if (entity.isNew()) {
            taskState = "新任务";
            myGrad.setColor(Color.parseColor("#20d19a"));
        }

        if (entity.isInvalid()) {
            taskState = "已作废";
            myGrad.setColor(Color.parseColor("#cfcccc"));
        }
        if (entity.isAbnormal()) {
            taskState = "异常";
            myGrad.setColor(Color.parseColor("#d28ce1"));
        }
        if (entity.isUnFinish()) {
            taskState = "未完成";
            myGrad.setColor(Color.parseColor("#f35ca1"));
        }
        if (entity.isVerfiy()) {
            taskState = "待审核";
            myGrad.setColor(Color.parseColor("#f7c326"));
        }
        if (entity.isOverTime()) {
            taskState = "已超期";
            myGrad.setColor(Color.parseColor("#f34a58"));
        }
        if (entity.isRebut()) {
            taskState = "被驳回";
            myGrad.setColor(Color.parseColor("#f6873d"));
        }
        if (entity.isInvalid()) {
            taskState = "已作废";
            myGrad.setColor(Color.parseColor("#cfcccc"));
        }
        if (entity.isAbnormal()) {
            taskState = "异常";
            myGrad.setColor(Color.parseColor("#d28ce1"));
        }
        if (entity.isAccepted()) {
            taskState = "待接受";
            myGrad.setColor(Color.parseColor("#2aa9ef"));
        }
        if (entity.isRefused()) {
            taskState = "已拒绝";
            myGrad.setColor(Color.parseColor("#9ebdc5"));
        }
        if (entity.isUrgent()) {
            taskState = "任务加急";
            myGrad.setColor(Color.parseColor("#ff0000"));
        }
        if (entity.getTaskStatus()==1){
            taskState = "待接受";
            myGrad.setColor(Color.parseColor("#2aa9ef"));
        }
        if (entity.getTaskStatus()==6){
            holder.mState.setVisibility(View.GONE);
        }
        if (entity.getTaskStatus()==8){
            taskState = "已拒绝";
            myGrad.setColor(Color.parseColor("#9ebdc5"));
        }

//
//        if (entity.getTaskStatus() == 2) {
//            if (entity.isVerfiy()) {
//                taskState = "待审核";
//                myGrad.setColor(Color.parseColor("#f7c326"));
//            }
//            if (entity.isOverTime()) {
//                taskState = "已超期";
//                myGrad.setColor(Color.parseColor("#f34a58"));
//            }
//            if (entity.isRebut()) {
//                taskState = "被驳回";
//                myGrad.setColor(Color.parseColor("#f6873d"));
//            }
//            if (entity.isAccepted()) {
//                taskState = "待接受";
//                myGrad.setColor(Color.parseColor("#2aa9ef"));
//            }
//            if (entity.isRefused()) {
//                taskState = "已拒绝";
//                myGrad.setColor(Color.parseColor("#9ebdc5"));
//            }
//        }  else if (entity.getTaskStatus() == 1) {
//            taskState = "待接受";
//            myGrad.setColor(Color.parseColor("#2aa9ef"));
//            if (entity.isNew()) {
//                taskState = "新任务";
//                myGrad.setColor(Color.parseColor("#20d19a"));
//            }
//        } else if (entity.getTaskStatus() == 6) {
//            if (entity.isInvalid()) {
//                taskState = "已作废";
//                myGrad.setColor(Color.parseColor("#cfcccc"));
//            }
//            if (entity.isAbnormal()) {
//                taskState = "异常";
//                myGrad.setColor(Color.parseColor("#d28ce1"));
//            }
//            if (entity.isUnFinish()) {
//                taskState = "未完成";
//                myGrad.setColor(Color.parseColor("#f35ca1"));
//            }
//        } else if (entity.getTaskStatus() == 16) {
//            if (entity.isVerfiy()) {
//                taskState = "待审核";
//                myGrad.setColor(Color.parseColor("#f7c326"));
//            }
//            if (entity.isOverTime()) {
//                taskState = "已超期";
//                myGrad.setColor(Color.parseColor("#f34a58"));
//            }
//            if (entity.isRebut()) {
//                taskState = "被驳回";
//                myGrad.setColor(Color.parseColor("#f6873d"));
//            }
//            if (entity.isInvalid()) {
//                taskState = "已作废";
//                myGrad.setColor(Color.parseColor("#cfcccc"));
//            }
//            if (entity.isAbnormal()) {
//                taskState = "异常";
//                myGrad.setColor(Color.parseColor("#d28ce1"));
//            }
//            if (entity.isAccepted()) {
//                taskState = "待接受";
//                myGrad.setColor(Color.parseColor("#2aa9ef"));
//            }
//            if (entity.isRefused()) {
//                taskState = "已拒绝";
//                myGrad.setColor(Color.parseColor("#9ebdc5"));
//            }
//        } else if (entity.getTaskStatus() == 11) {
//            if (entity.isVerfiy()) {
//                taskState = "待审核";
//                myGrad.setColor(Color.parseColor("#f7c326"));
//            }
//            if (entity.isOverTime()) {
//                taskState = "已超期";
//                myGrad.setColor(Color.parseColor("#f34a58"));
//            }
//            if (entity.isRebut()) {
//                taskState = "被驳回";
//                myGrad.setColor(Color.parseColor("#f6873d"));
//            }
//            if (entity.isNew()) {
//                taskState = "新任务";
//                myGrad.setColor(Color.parseColor("#20d19a"));
//            }
//            if (entity.isAccepted()) {
//                taskState = "待接受";
//                myGrad.setColor(Color.parseColor("#2aa9ef"));
//            }
//            if (entity.isRefused()) {
//                taskState = "已拒绝";
//                myGrad.setColor(Color.parseColor("#9ebdc5"));
//            }
//        }
//        if (entity.isUrgent()) {
//            taskState = "任务加急";
//            myGrad.setColor(Color.parseColor("#ff0000"));
//        }
        if (StringUtils.isEmpty(taskState)) {
            holder.mState.setVisibility(View.GONE);
        } else {
            holder.mState.setVisibility(View.VISIBLE);
            holder.mState.setText(taskState);//状态
        }
//        //超期数量
//        if (!StringUtils.isEmpty(entity.getEndTimeLength())){
//            holder.mState.setVisibility(View.VISIBLE);
//            myGrad.setColor(Color.parseColor("#f34a58"));
//            holder.mState.setText(entity.getEndTimeLength());
//        }
        initView(holder);

        mListV.clear();
        for (int i = 0; i < entity.getButtons().size(); i++) {
            if ("创建子任务".equals(entity.getButtons().get(i).getOperateName())
                    || "删除子任务".equals(entity.getButtons().get(i).getOperateName())
                    || "锁定".equals(entity.getButtons().get(i).getOperateName())
                    || "进度条".equals(entity.getButtons().get(i).getOperateName())
                    || "解锁".equals(entity.getButtons().get(i).getOperateName())
                    || "修改".equals(entity.getButtons().get(i).getOperateName())) {
                continue;
            }
            if (entity.getButtons().get(i).getActionType()==0||entity.getButtons().get(i).getActionType()==1){
                continue;
            }
            addView(entity.getButtons().get(i).getOperateName(), entity.getButtons().get(i).getActionType() + "", holder.mLayout, position);
        }
        addView("评论", "-1", holder.mLayout, position);

        //按钮点击事件回调
        //评论
        holder.mComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                callBack.onTextViewClick(v, position, entity.getTaskStatus(), entity.getPersonTaskState(), false);
            }
        });
        //已完成 or 提交审核
        holder.mComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.getEditText(holder.mComplete).equals("提交审核")) {
                    isCommit = true;
                } else {
                    isCommit = false;
                }
//                callBack.onTextViewClick(v, position, entity.getTaskStatus(), entity.getPersonTaskState(), isCommit);
            }
        });
        //接收 or 同意
        holder.mAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.getEditText(holder.mAccept).equals("接受")) {
                    isAgree = false;
                } else {
                    isAgree = true;
                }
//                callBack.onTextViewClick(v, position, entity.getTaskStatus(), entity.getPersonTaskState(), isAgree);
            }
        });
        //拒绝 or 驳回
        holder.mRefuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.getEditText(holder.mRefuse).equals("拒绝")) {
                    isStop = true;
                } else {
                    isStop = false;
                }
//                callBack.onTextViewClick(v, position, entity.getTaskStatus(), entity.getPersonTaskState(), isStop);
            }
        });

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.mName)
        TextView mName;
        @BindView(R.id.mission_level)
        TextView missionLevel;
        @BindView(R.id.mission_progress)
        TextView missionProgress;
        @BindView(R.id.creat_time)
        TextView creatTime;
        @BindView(R.id.end_time)
        TextView endTime;
        @BindView(R.id.mContent)
        TextView mContent;
        @BindView(R.id.mState)
        TextView mState;
        @BindView(R.id.mExecuteName)
        TextView mExecuteName;
        @BindView(R.id.mLeadName)
        TextView mLeadName;
        @BindView(R.id.mCheckName)
        TextView mCheckName;
        @BindView(R.id.mComment)
        TextView mComment;
        @BindView(R.id.mRefuse_view)
        View mRefuseView;
        @BindView(R.id.mRefuse)
        TextView mRefuse;
        @BindView(R.id.mComplete_view)
        View mCompleteView;
        @BindView(R.id.mComplete)
        TextView mComplete;
        @BindView(R.id.mAccept_view)
        View mAcceptView;
        @BindView(R.id.mAccept)
        TextView mAccept;
        @BindView(R.id.mLayout)
        LinearLayout mLayout;
        @BindView(R.id.tv_progress)
        TextView tvProgress;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    /**
     * 是否为负责人
     *
     * @return
     */
    public boolean isLeadPerson() {
        if (!StringUtils.isEmpty(leadId)) {
            if (leadId.equals(loginId)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否为执行人
     *
     * @return
     */
    public boolean isExecutePerson() {
//        if (!StringUtils.isEmpty(executeIds)) {
//            String[] array = executeIds.split(",");
//            for (int i = 0; i < array.length; i++) {
//                if (array[i].equals(loginId)) {
//                    return true;
//                }
//            }
//        }
        return false;
    }

    /**
     * 是否为审核人
     *
     * @return
     */
    public boolean isCheckPerson() {
        if (!StringUtils.isEmpty(checkId)) {
            if (checkId.equals(loginId)) {
                return true;
            }
        }
        return false;
    }


    //初始化下方的按钮
    public void initView(ViewHolder holder) {
        hideView(holder);

        //待接受的任务
        if (mState == 0) {
            //如果是负责人
            if (isLeadPerson()) {
                hideView(holder);
                holder.mComplete.setVisibility(View.VISIBLE);
                holder.mCompleteView.setVisibility(View.VISIBLE);
                holder.mComplete.setText("提交审核");
                isCommit = true;
            }

            if (isExecutePerson()) {
                //个人待接受
                if (mStatePerson == 10) {
                    hideView(holder);
                    holder.mAccept.setVisibility(View.VISIBLE);
                    holder.mAcceptView.setVisibility(View.VISIBLE);
                    holder.mAccept.setText("接受");
                    holder.mRefuse.setVisibility(View.VISIBLE);
                    holder.mRefuseView.setVisibility(View.VISIBLE);
                    holder.mRefuse.setText("拒绝");
                    isAgree = false;
                    isStop = true;
                }

                //个人已接受
                if (mStatePerson == 11) {
                    hideView(holder);
                    if (mState == 1) {
                        holder.mComplete.setVisibility(View.VISIBLE);
                        holder.mCompleteView.setVisibility(View.VISIBLE);
                        holder.mComplete.setText("已完成");
                        isCommit = false;
                    }
                }

                //个人进行中
                if (mStatePerson == 12) {
                    hideView(holder);
                    holder.mComplete.setVisibility(View.VISIBLE);
                    holder.mCompleteView.setVisibility(View.VISIBLE);
                    holder.mComplete.setText("已完成");
                    isCommit = false;
                }

                //个人已完成
                if (mStatePerson == 13) {
                    hideView(holder);
                }

                //个人已拒绝
                if (mStatePerson == 14) {
                    hideView(holder);
                    holder.mAccept.setVisibility(View.VISIBLE);
                    holder.mAcceptView.setVisibility(View.VISIBLE);
                    holder.mAccept.setText("接受");
                    isAgree = false;
                }
            }
        } else if (mState == 1) {//进行中的任务

            //如果是负责人
            if (isLeadPerson()) {
                holder.mComplete.setVisibility(View.VISIBLE);
                holder.mCompleteView.setVisibility(View.VISIBLE);
                holder.mComplete.setText("提交审核");
                isCommit = true;
            }

            if (isExecutePerson()) {
                //个人待接受
                if (mStatePerson == 10) {
                    hideView(holder);
                    holder.mAccept.setVisibility(View.VISIBLE);
                    holder.mAcceptView.setVisibility(View.VISIBLE);
                    holder.mAccept.setText("接受");
                    holder.mRefuse.setVisibility(View.VISIBLE);
                    holder.mRefuseView.setVisibility(View.VISIBLE);
                    holder.mRefuse.setText("拒绝");
                    isAgree = false;
                    isStop = true;
                }

                //个人已接受
                if (mStatePerson == 11) {
                    hideView(holder);
                    holder.mComplete.setVisibility(View.VISIBLE);
                    holder.mCompleteView.setVisibility(View.VISIBLE);
                    holder.mComplete.setText("已完成");
                    isCommit = false;
                }

                //个人进行中
                if (mStatePerson == 12) {
                    hideView(holder);
                    holder.mComplete.setVisibility(View.VISIBLE);
                    holder.mCompleteView.setVisibility(View.VISIBLE);
                    holder.mComplete.setText("已完成");
                    isCommit = false;
                }

                //个人已完成
                if (mStatePerson == 13) {
                    hideView(holder);
                }

                //个人已拒绝
                if (mStatePerson == 14) {
                    hideView(holder);
                    holder.mAccept.setVisibility(View.VISIBLE);
                    holder.mAcceptView.setVisibility(View.VISIBLE);
                    holder.mAccept.setText("接受");
                    isAgree = false;
                }
            }
        } else if (mState == 2) {//待审核的任务
            //如果是审核人
            if (isCheckPerson()) {
                holder.mAccept.setVisibility(View.VISIBLE);
                holder.mAcceptView.setVisibility(View.VISIBLE);
                holder.mAccept.setText("同意");
                holder.mRefuse.setVisibility(View.VISIBLE);
                holder.mRefuseView.setVisibility(View.VISIBLE);
                holder.mRefuse.setText("驳回");
                isAgree = true;
                isStop = false;
            }
        } else if (mState == 8) {//执行人已完成

            //如果是负责人
            if (isLeadPerson()) {
                hideView(holder);
                holder.mComplete.setVisibility(View.VISIBLE);
                holder.mCompleteView.setVisibility(View.VISIBLE);
                holder.mComplete.setText("提交审核");
                isCommit = true;
            }
        } else if (mState == 3) {
            if (isTrunDownPerson) {
                hideView(holder);
                holder.mRefuse.setVisibility(View.VISIBLE);
                holder.mRefuseView.setVisibility(View.VISIBLE);
                holder.mRefuse.setText("驳回");
                isStop = false;
            }
        } else {
            hideView(holder);
        }
    }

    /**
     * 隐藏按钮
     */
    public void hideView(ViewHolder holder) {
        holder.mAccept.setVisibility(View.GONE);
        holder.mAcceptView.setVisibility(View.GONE);
        holder.mComplete.setVisibility(View.GONE);
        holder.mCompleteView.setVisibility(View.GONE);
        holder.mRefuse.setVisibility(View.GONE);
        holder.mRefuseView.setVisibility(View.GONE);
    }

    //按钮点击回调
    public interface ItemCallBack {
        void onTextViewClick(View view, int position, String actionType, boolean isType);
    }

    /**
     * 加了星期的时间
     *
     * @param time 格式：yyyy-MM-dd HH:mm:ss
     * @return 格式：yyyy-MM-dd 星期 HH:mm:ss
     */
    public String getWeekData(String time) {
//        2016-11-29 11:44:12
        String weekDay = null;
        String day = null;
        String[] days = new String[]{"日", "一", "二", "三", "四", "五", "六"};
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");// 输入的日期格式必须是这种
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
        Date date = null;// 把字符串转化成日期
        try {
            date = df.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        weekDay = "星期" + days[date.getDay()];
        String yearMonthDay = sdf1.format(date);
        String hourMinute = sdf2.format(date);
        day = yearMonthDay + " " + weekDay + " " + hourMinute;
        return day;
    }

    private void addView(String operateName, String actionType, LinearLayout mLayout, final int position) {
        View view = mInflater.inflate(R.layout.view_mission_text_list, mLayout, false);
        view.setTag(actionType);
        TextView textView = (TextView) view.findViewById(R.id.tv_submit);
        textView.setText(operateName);
        textView.setTag(actionType);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String actionType = v.getTag().toString();

//                    postTaskState("", actionType);
                callBack.onTextViewClick(v, position, actionType, isCommit);
            }
        });
        mListV.add(view);
        refresh(mLayout);
    }

    /**
     * 刷新顶部视图
     */
    private void refresh(LinearLayout mLayout) {
        mLayout.removeAllViews();
        for (int i = 0; i < mListV.size(); i++) {
            /*if (mListV.size() - 1 == i) {
                ((TextView) mListV.get(i).findViewById(R.id.tv_submit)).setTextColor(mCxontext.getResources().getColor(R.color.tvc9));
            } else {
                ((TextView) mListV.get(i).findViewById(R.id.tv_submit)).setTextColor(mCxontext.getResources().getColor(R.color.cpb_blue));
            }*/
            mLayout.addView(mListV.get(i));
        }
    }
}
