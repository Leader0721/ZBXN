package com.zbxn.main.activity.mission;

import android.app.ActionBar;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.pub.base.BaseActivity;
import com.pub.base.BaseApp;
import com.pub.common.EventCustom;
import com.pub.dialog.InputDialog;
import com.pub.dialog.MessageDialog;
import com.pub.http.HttpCallBack;
import com.pub.http.HttpRequest;
import com.pub.http.ResultData;
import com.pub.utils.FragmentAdapter;
import com.pub.utils.MyToast;
import com.pub.utils.PreferencesUtils;
import com.pub.utils.StringUtils;
import com.pub.widget.MyListView;
import com.zbxn.R;
import com.zbxn.main.activity.login.LoginActivity;
import com.zbxn.main.adapter.CheckListViewAdapter;
import com.zbxn.main.adapter.MissionCommentListViewAdapter;
import com.zbxn.main.adapter.MissionDetailsAdpter;
import com.zbxn.main.adapter.OperateListViewAdapter;
import com.zbxn.main.adapter.PopupwindowMoreAdapter;
import com.zbxn.main.entity.MissionCommendListEntity;
import com.zbxn.main.entity.MissionDetailEntity;
import com.zbxn.main.entity.MissionEntity;
import com.zbxn.main.entity.MissionGetOkrEntity;
import com.zbxn.main.entity.MissionOperateLogListEntity;
import com.zbxn.main.entity.MissionReadListEntity;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

/**
 * 项目名称：任务详情
 * 创建人：LiangHanXin
 * 创建时间：2016/11/9 12:01
 */
public class MissionDetailsActivity extends BaseActivity implements MissionDetailsAdpter.DeleteChildCallback {
    private static final int Flag_Callback_ContactsPicker1 = 1002;//进度
    //修改
    private static final int Flag_Callback_MissionUpdateActivity = 1002;
    //打开新建子任务
    private static final int REQUEST_MISSION_ACTIVITY = 1004;
    //子任务
    private static final int REQUEST_MISSION_CHILDDETAIL = 1005;
    //打开评论页
    private static final int REQUEST_COMMENT_ACTIVITY = 1006;

    @BindView(R.id.mTitle)
    TextView mTitle;
    @BindView(R.id.mTime)
    TextView mTime;
    @BindView(R.id.mState)
    TextView mState;
    @BindView(R.id.imageView2)
    ImageView imageView2;
    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.ll_attachment)
    RelativeLayout llAttachment;
    @BindView(R.id.mContent)
    TextView mContent;
    @BindView(R.id.ll_time)
    LinearLayout llTime;
    @BindView(R.id.ll_work_hours)
    LinearLayout llWorkHours;
    @BindView(R.id.etScore)
    TextView etScore;
    @BindView(R.id.ll_score)
    LinearLayout llScore;
    @BindView(R.id.mEject)
    LinearLayout mEject;
    @BindView(R.id.tv_create_people)
    TextView tvCreatePeople;
    @BindView(R.id.ll_create_people)
    LinearLayout llCreatePeople;
    @BindView(R.id.tv_charge_people)
    TextView tvChargePeople;
    @BindView(R.id.ll_charge_people)
    LinearLayout llChargePeople;
    @BindView(R.id.tv_executor_people)
    TextView tvExecutorPeople;
    @BindView(R.id.ll_executor_people)
    LinearLayout llExecutorPeople;
    @BindView(R.id.tv_checker_people)
    TextView tvCheckerPeople;
    @BindView(R.id.ll_checker_people)
    LinearLayout llCheckerPeople;
    @BindView(R.id.tv_copy_people)
    TextView tvCopyPeople;
    @BindView(R.id.ll_copy_people)
    LinearLayout llCopyPeople;
    @BindView(R.id.mDisplay)
    LinearLayout mDisplay;
    @BindView(R.id.tv_difficulty)
    TextView tvDifficulty;
    @BindView(R.id.ll_difficulty)
    LinearLayout llDifficulty;
    @BindView(R.id.tv_level)
    TextView tvLevel;
    @BindView(R.id.ll_level)
    LinearLayout llLevel;
    @BindView(R.id.tvSubtask)
    TextView tvSubtask;
    @BindView(R.id.mSubTask)
    LinearLayout mSubTask;
    @BindView(R.id.textView2)
    TextView textView2;
    @BindView(R.id.tvParenttask)
    TextView tvParenttask;
    @BindView(R.id.mParentTask)
    LinearLayout mParentTask;
    @BindView(R.id.mission_scrollView)
    ScrollView missionScrollView;
    @BindView(R.id.mAccept)
    TextView mAccept;
    @BindView(R.id.mComplete)
    TextView mComplete;
    @BindView(R.id.mRefuse)
    TextView mRefuse;
    @BindView(R.id.mComment)
    TextView mComment;
    @BindView(R.id.mSeekBar)
    SeekBar mSeekBar;
    @BindView(R.id.mLine)
    ImageView mLine;
    @BindView(R.id.mPercentage)
    TextView mPercentage;
    @BindView(R.id.tv_end_time)
    TextView tvEndTime;
    @BindView(R.id.mLayout)
    LinearLayout mLayout;
    @BindView(R.id.tvComments)
    TextView tvComments;
    @BindView(R.id.mlvComment)
    MyListView mlvComment;
    @BindView(R.id.mlvCheck)
    MyListView mlvCheck;
    @BindView(R.id.mlvOperate)
    MyListView mlvOperate;
    @BindView(R.id.tvCheck)
    TextView tvCheck;
    @BindView(R.id.tvOperate)
    TextView tvOperate;
    @BindView(R.id.ivComments)
    ImageView ivComments;
    @BindView(R.id.ivCheck)
    ImageView ivCheck;
    @BindView(R.id.ivOperate)
    ImageView ivOperate;
    @BindView(R.id.tvTime)
    TextView tvTime;
    @BindView(R.id.et_work_hours)
    TextView etWorkHours;


    private int minPaddingTop;
    private boolean isExtend = false;//是否是伸展 ，默认是收缩的

    private String leadId;//负责人id
    private String executeIds;//执行人id（多个）
    private String checkId;//审核人id
    private String mTaskId;//当前任务ID
    private int mPlan;//记录任务进度
    private int taskState;//记录主任务状态
    private int taskPersonState;//个人状态

    //按钮标识
    private boolean isAgree;//true:同意 false:通过
    private boolean isStop;//true:拒绝 false:驳回
    private boolean isCommit;//true:提交审核 false:已完成
    private boolean isChildTask;//是否是子任务
    private int types;

    private InputDialog mDialog;
    private MessageDialog messageDialog;

    //任务详情
    private MissionDetailEntity entity;

    //子任务列表
    private List<MissionEntity> missionList;
    private MissionDetailsAdpter missionDetailsAdapter;
    //评论列表
    private List<MissionCommendListEntity> commendList;
    private MissionCommentListViewAdapter commendListViewAdapter;

    private String loginId;
    private String chargePeopleName;
    private String checkerPeopleName;
    private String chargePeopleIds;
    private String checkerPeopleId;

    private FragmentAdapter mAdapter;
    protected static int index = 0;

    private LayoutInflater mInflater;
    private List<View> mListV = new ArrayList<>();
    private List<MissionOperateLogListEntity> operateList;
    private List<MissionReadListEntity> checkList;
    private CheckListViewAdapter checkListViewAdapter;
    private OperateListViewAdapter operateListViewAdapter;
    public static final String  SUCCESS="SeekBarChangeSuccess";
    //选中的难易程度(角标)
    private int choosedDiffNum = 0;
    //选择的时间   分钟
    private String choosedTime;
    //选择的时间  小时
    String hours = "0";
//    private boolean flag = false;


    private List<String> listMore = new ArrayList<>();//右上角更多里面的内容

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission_details);
        ButterKnife.bind(this);
        mInflater = LayoutInflater.from(this);

        setTitle("任务详情");
        initData();
        //获取任务详情
        getMissionDetails();
        initTabLayout();
    }

    @Override
    public void initRight() {
        setRight1("更多");
        setRight1Icon(R.mipmap.my_more);
        setRight1Show(true);
        setRight2("创建子任务");
        setRight2Icon(R.mipmap.menu_creat_blog);
        setRight2Show(true);
        super.initRight();
    }

    //评论
    private void initTabLayout() {
        setPageSelected(0);
        //隐藏父任务
        mParentTask.setVisibility(View.GONE);
        mSeekBar.setEnabled(false);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {//该方法拖动进度条进度改变的时候调用
//                if (fromUser) {
//                    progress(mTaskId, progress + "");//到达100时是否自己算完成
//                    mPercentage.setText(progress + "" + "%");//进度百分比
//                }
                /*else {//第一次不设置进度
                    flag = true;
                }*/
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {//该方法拖动进度条开始拖动的时候调用

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {//该方法拖动进度条停止拖动的时候调用
                progress(mTaskId, mSeekBar.getProgress() + "");//到达100时是否自己算完成
                mPercentage.setText(mSeekBar.getProgress() + "" + "%");//进度百分比
//                if (mSeekBar.getProgress() >= 100) {
//                    getMissionDetails();
//                }
                EventCustom eventCustom = new EventCustom();
                eventCustom.setTag(MissionDetailsActivity.SUCCESS);
                eventCustom.setObj(mSeekBar.getProgress());
                EventBus.getDefault().post(eventCustom);
            }
        });
    }


    //点击显示listview
    private void setPageSelected(int arg0) {

        tvComments.setSelected(false);
        tvComments.setTextColor(getResources().getColor(R.color.tvc6));
        ivComments.setVisibility(View.INVISIBLE);
        ivComments.setBackgroundColor(getResources().getColor(R.color.cpb_blue));
        mlvComment.setVisibility(View.GONE);

        tvCheck.setSelected(false);
        tvCheck.setTextColor(getResources().getColor(R.color.tvc6));
        ivCheck.setVisibility(View.INVISIBLE);
        ivCheck.setBackgroundColor(getResources().getColor(R.color.cpb_blue));
        mlvCheck.setVisibility(View.GONE);

        tvOperate.setSelected(false);
        tvOperate.setTextColor(getResources().getColor(R.color.tvc6));
        ivOperate.setVisibility(View.INVISIBLE);
        ivOperate.setBackgroundColor(getResources().getColor(R.color.cpb_blue));
        mlvOperate.setVisibility(View.GONE);

        if (arg0 == 0) {
            tvComments.setSelected(true);
            tvComments.setTextColor(getResources().getColor(R.color.cpb_blue));
            ivComments.setVisibility(View.VISIBLE);
            ivComments.setBackgroundColor(getResources().getColor(R.color.cpb_blue));
            mlvComment.setVisibility(View.VISIBLE);
        } else if (arg0 == 1) {
            tvCheck.setSelected(true);
            tvCheck.setTextColor(getResources().getColor(R.color.cpb_blue));
            ivCheck.setVisibility(View.VISIBLE);
            ivCheck.setBackgroundColor(getResources().getColor(R.color.cpb_blue));
            mlvCheck.setVisibility(View.VISIBLE);
        } else if (arg0 == 2) {
            tvOperate.setSelected(true);
            tvOperate.setTextColor(getResources().getColor(R.color.cpb_blue));
            ivOperate.setVisibility(View.VISIBLE);
            ivOperate.setBackgroundColor(getResources().getColor(R.color.cpb_blue));
            mlvOperate.setVisibility(View.VISIBLE);
        }
    }

    private void initData() {
        loginId = PreferencesUtils.getString(this, LoginActivity.FLAG_INPUT_ID);
        Intent intent = getIntent();
        mTaskId = intent.getStringExtra("id");
        types = intent.getIntExtra("types", 0);
        if (types == 1) {
            isChildTask = true;
        } else {
            isChildTask = false;
        }
//        mListViewP.setFocusable(false);
        missionScrollView.smoothScrollTo(0, 20);
    }

    @Override
    public void actionRight1(MenuItem menuItem) {
        super.actionRight1(menuItem);
        ShowPopupWindow(listMore, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (listMore.get(position)) {
                    case "修改":
                        Intent intent = new Intent(MissionDetailsActivity.this, MissionUpdateActivity.class);
                        intent.putExtra("type", 1);
                        intent.putExtra("mission", entity);
                        startActivityForResult(intent, Flag_Callback_MissionUpdateActivity);
                        break;
                    case "删除":
                        final String CurrentCompanyId = PreferencesUtils.getString(BaseApp.getContext(), LoginActivity.FLAG_ZMSCOMPANYID, "");
                        MessageDialog dialog = new MessageDialog(MissionDetailsActivity.this);
                        dialog.setTitle("提示");
                        dialog.setMessage("确认删除?");
                        dialog.setNegativeButton("取消");
                        dialog.setPositiveButton("删除",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
//                                        if (isChildTask) {
//                                            deleteTask(mTaskId, CurrentCompanyId, 3,);
//                                        } else {
                                        deleteTask(mTaskId, CurrentCompanyId, 1, 18);
//                                        }
                                    }

                                });
                        dialog.show();
                        break;
                    //当点击锁定时，状态应该改编成解锁
                    case "锁定":
                        postTaskState("", 15 + "");
                        break;
                    case "解锁":
                        postTaskState("", 16 + "");
                        break;
                }
            }
        });
    }

    @Override
    public void actionRight2(MenuItem menuItem) {
        super.actionRight2(menuItem);
        addNewMission();//新建子任务
    }

    /**
     * 回调需要接收的人员
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == Flag_Callback_ContactsPicker1) {//
            getMissionDetails();
        } else if (requestCode == REQUEST_COMMENT_ACTIVITY) { //评论
            MyToast.showToast("评论成功");
            getMissionDetails();
        } else if (requestCode == REQUEST_MISSION_ACTIVITY) { //新建子任务
            getMissionDetails();
            MyToast.showToast("新建子任务成功");
            getMissionDetails();
        } else if (requestCode == REQUEST_MISSION_CHILDDETAIL) { // 子任务
            getMissionDetails();
        } else if (requestCode == Flag_Callback_MissionUpdateActivity) { // 修改任务
            getMissionDetails();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick({R.id.mEject, R.id.mAccept, R.id.mComplete, R.id.mRefuse, R.id.mComment,
            R.id.ll_attachment, R.id.layout_org, R.id.layout_Check, R.id.layout_Operate, R.id.mSubTask})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mEject:
                PaddingTopAnim anim = null;
                if (isExtend) {
                    //执行收缩动画
                    anim = new PaddingTopAnim(mDisplay, 0, minPaddingTop);
                } else {
                    //执行伸展动画
                    anim = new PaddingTopAnim(mDisplay, minPaddingTop, 0);
                }
                anim.start(350);

                //将标记置为反值
                isExtend = !isExtend;
                break;
//            case R.id.mProgress://更新进度
//                Intent intent = new Intent(MissionDetailsActivity.this, ProgressActivity.class);
//                intent.putExtra("id", ID);
//                intent.putExtra("progress", mPlan);
//                startActivityForResult(intent, Flag_Callback_ContactsPicker1);
//                break;
            case R.id.mAccept:
                if (isAgree) { //同意(审核通过)
                    postTaskState("", 3 + "");
                } else { //接受
                    postTaskState("", 11 + "");
                }
                break;
            case R.id.mComplete:
                if (isCommit) { //提交审核
                    if (isLeadPerson() && isExecutePerson() && isCheckPerson() && isExecuteOne()) {
                        postTaskState("", 2 + "");
                    } else {
                        if (mPlan < 100) {
                            MyToast.showToast("进度还未到达100%");
                        } else {
                            postTaskState("", 2 + "");
                        }
                    }
                } else {//已完成
                    messageDialog = new MessageDialog(this);
                    messageDialog.setTitle("任务提示");
                    messageDialog.setMessage("确认任务是否已完成？");
                    messageDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            if (isChildListNull()) {
                                postTaskState("", 13 + "");
                            } else {
                                if (mPlan < 100) {
                                    MyToast.showToast("进度还未到达100%");
                                } else {
                                    postTaskState("", 13 + "");
                                }
                            }
                        }
                    });
                    messageDialog.setNegativeButton("取消");
                    messageDialog.show();
                }
                break;
            case R.id.mRefuse:
                if (isStop) {//拒绝
                    mDialog = new InputDialog(this);
                    mDialog.setTitle("拒绝理由");
                    mDialog.setContentHint("请输入拒绝理由");
                    mDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String content = mDialog.getEditText().getText().toString();
                            if (!StringUtils.isEmpty(content.trim())) {
//                                if (taskState == 0) {
//                                    if (taskPersonState == 10) {
//                                        postTaskState(content, 14 + "");
//                                    }
//                                } else {
//                                    postTaskState(content, 4 + "");
//                                }
                            } else {
                                MyToast.showToast("拒绝理由不能为空");
                            }
                        }
                    });
                    mDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    mDialog.show();
                } else {//驳回
                    mDialog = new InputDialog(this);
                    mDialog.setTitle("驳回意见");
                    mDialog.setContentHint("请输入驳回意见");
                    mDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String content = mDialog.getEditText().getText().toString();
                            if (!StringUtils.isEmpty(content.trim())) {
//                                if (taskState == 3) {
//                                    if (isTrunDownPerson) {
//                                        postTaskState(content, 5 + "");
//                                    }
//                                } else {
//                                    postTaskState(content, 1 + "");
//                                }
                            } else {
                                MyToast.showToast("驳回意见不能为空");
                            }
                        }
                    });
                    mDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    mDialog.show();
                }
                break;
            case R.id.mComment://评论
                Intent intents = new Intent(MissionDetailsActivity.this, CommentActivity.class);
                intents.putExtra("TaskId", mTaskId);
                MissionDetailsActivity.this.startActivityForResult(intents, REQUEST_COMMENT_ACTIVITY);
                break;
            case R.id.ll_attachment: //查看附件
                if (null == entity && StringUtils.isEmpty(entity.getAttachmentList())) {
                    MyToast.showToast("暂无附件");
                    break;
                }
                Intent intent2 = new Intent(MissionDetailsActivity.this, AttachmentActivity.class);
                intent2.putExtra("list", entity.getAttachmentList());
                MissionDetailsActivity.this.startActivity(intent2);
                break;
            case R.id.layout_org:
                setPageSelected(0);
                break;
            case R.id.layout_Check:
                setPageSelected(1);
                break;
            case R.id.layout_Operate:
                setPageSelected(2);
                break;
            case R.id.mSubTask:
                Intent intent = new Intent(MissionDetailsActivity.this, MissionActivity.class);
                intent.putExtra("id", mTaskId);
                startActivity(intent);
                break;
        }


    }

    private void anim() {
        mDisplay.measure(0, 0);
        mDisplay.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mDisplay.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                //最小值
                minPaddingTop = -1 * mDisplay.getMeasuredHeight();
                mDisplay.setPadding(mDisplay.getPaddingLeft(), minPaddingTop, mDisplay.getPaddingRight()
                        , mDisplay.getPaddingBottom());
            }
        });
    }
//获取业务积分
    public void getPrewOKR() {
        String ssid = PreferencesUtils.getString(this, LoginActivity.FLAG_SSID);
        String currentCompanyId = PreferencesUtils.getString(this, LoginActivity.FLAG_ZMSCOMPANYID, "");
        Call call = HttpRequest.getIResourceOANetAction().getPrewOKR(ssid, currentCompanyId, choosedDiffNum + "", hours, choosedTime);
        callRequest(call, new HttpCallBack(MissionGetOkrEntity.class, this, false) {
            @Override
            public void onSuccess(ResultData mResult) {
                if ("0".equals(mResult.getSuccess())) {
                    MissionGetOkrEntity entity = (MissionGetOkrEntity) mResult.getData();
                    etScore.setText(entity.getOkr() + "");
                } else {
//                    String message = mResult.getMsg();
//                    MyToast.showToast(message);
                }
            }

            @Override
            public void onFailure(String string) {
                MyToast.showToast(R.string.NETWORKERROR);
            }
        });
    }

    /**
     * 获取任务详情
     */
    public void getMissionDetails() {
        String ssid = PreferencesUtils.getString(this, LoginActivity.FLAG_SSID);
        String CurrentCompanyId = PreferencesUtils.getString(this, LoginActivity.FLAG_ZMSCOMPANYID, "");
        Call call = HttpRequest.getIResourceOANetAction().getMissionDetails(ssid, CurrentCompanyId, mTaskId);
        callRequest(call, new HttpCallBack(MissionDetailEntity.class, this, true) {
            @Override
            public void onSuccess(ResultData mResult) {
                if ("0".equals(mResult.getSuccess())) {//0成功
                    try {
                        setRight2Show(true);//加载成功显示创建子任务加号
                    }catch (Exception e){
                        Log.d("====","打断点时initright(）没有初始化完成，正常现象");
                    }
                    entity = (MissionDetailEntity) mResult.getData();

                    if (entity == null) {
                        return;
                    }
//                    flag = false;//获取详情时不调用setpress
                    listMore.clear();
                    mListV.clear();
                    for (int i = 0; i < entity.getButtons().size(); i++) {
                        if ("修改".equals(entity.getButtons().get(i).getOperateName())) {
                            setRight1("更多");
                            setRight1Icon(R.mipmap.my_more);
                            setRight1Show(true);
                            listMore.add("修改");
                            continue;
                        }
                        if ("进度条".equals(entity.getButtons().get(i).getOperateName())) {
                            mSeekBar.setEnabled(true);
                            continue;
                        }
                        if ("创建子任务".equals(entity.getButtons().get(i).getOperateName())) {
                            setRight2("创建子任务");
                            setRight2Icon(R.mipmap.menu_creat_blog);
                            setRight2Show(true);
                            continue;
                        }
                        //表示可以删除子任务
                        /*if ("删除子任务".equals(entity.getButtons().get(i).getOperateName())) {
                            setRight1("更多");
                            setRight1Icon(R.mipmap.my_more);
                            setRight1Show(true);
                            listMore.add("删除");
                            continue;
                        }*/
                        if ("删除子任务".equals(entity.getButtons().get(i).getOperateName())) {
                            continue;
                        }
                        if (entity.getButtons().get(i).getActionType()==15) {
                            setRight1("更多");
                            setRight1Icon(R.mipmap.my_more);
                            setRight1Show(true);
                            listMore.add("锁定");
                            continue;
                        }
                        if (entity.getButtons().get(i).getActionType()==16) {
                            setRight1("更多");
                            setRight1Icon(R.mipmap.my_more);
                            setRight1Show(true);
                            listMore.add("解锁");
                            continue;
                        }
                        addView(entity.getButtons().get(i).getOperateName(), entity.getButtons().get(i).getActionType() + "");
                    }
                    addView("评论", "-1");
                    if (StringUtils.isEmpty(listMore)) {
                        setRight1("更多");
                        listMore.add("修改");
                        setRight1Icon(R.mipmap.my_more);
                        setRight1Show(true);
                    }

//                    etScore.setText();//设置业务积分
                    choosedDiffNum=entity.getDifferentlyLevel();
                    choosedTime=entity.getWorkMins()+"";
                    hours=entity.getWorkHours()+"";
                    getPrewOKR();
                    mTime.setText(StringUtils.convertDateWithMin(entity.getCreateTime()));//开始时间
                    tvEndTime.setText(StringUtils.convertDateWithMin(entity.getEndTime()));//截止时间
                    mPercentage.setText(entity.getProgress() + "" + "%");//进度百分比
//                    firstBar.setProgress(entity.getDoneProgress());//进度条
                    mSeekBar.setProgress(entity.getProgress());

                    mTitle.setText(entity.getTitle());//标题
                    mContent.setText(entity.getInfo());//内容
                    if (StringUtils.isEmpty(entity.getInfo())) {//内容
                        mContent.setVisibility(View.GONE);
                    } else {
                        mContent.setVisibility(View.VISIBLE);
                    }
                    if (StringUtils.isEmpty(entity.getAttachmentList())) {//附件
//                        attachment.setVisibility(View.GONE);
                    }
                    etWorkHours.setText(entity.getWorkHours() + "");//工作时间
                    tvTime.setText(entity.getWorkMins() + "");//工作时间

                    if (StringUtils.isEmpty(entity.getCreatorName())) {
                        tvCreatePeople.setHint(null);
                    } else {
                        tvCreatePeople.setText(entity.getCreatorName());//负责人
                    }

                    chargePeopleName = entity.getChargerName();

                    chargePeopleIds = entity.getChargerId() + "";//负责人id
                    if (StringUtils.isEmpty(entity.getChargerName())) {
                        tvChargePeople.setHint(null);
                    } else {
                        tvChargePeople.setText(chargePeopleName);//负责人
                    }
                    //执行人
                    executeIds = "";
                    String sb = "";
                    for (int i = 0; i < entity.getTransactors().size(); i++) {
                        sb += entity.getTransactors().get(i).getName() + "、";
                        executeIds += entity.getTransactors().get(i).getId() + ",";
                    }
                    if (sb.length() > 0) {
                        sb = sb.substring(0, sb.length() - 1);
                    }
                    if (sb.length() > 0) {
                        executeIds = executeIds.substring(0, executeIds.length() - 1);
                    }
                    if (isExecutePerson()){
                        setRight2Show(false);
                    }
                    tvExecutorPeople.setText(sb);
                    if (StringUtils.isEmpty(sb)) {
                        tvExecutorPeople.setText(" ");
                    }
                    checkerPeopleName = entity.getReviewerName();
                    checkerPeopleId = entity.getReviewerId() + "";
                    if (StringUtils.isEmpty(entity.getReviewerName())) {
                        tvCheckerPeople.setHint(null);
                    } else {
                        tvCheckerPeople.setText(checkerPeopleName);//审核人
                    }
                    mPlan = entity.getProgress();
                    leadId = entity.getChargerId() + "";//负责人id
                    checkId = entity.getReviewerId() + "";//审核人id
//                    isTrunDownPerson = entity.isTaskTrunDownPerson();//是否为监督人
                    mTaskId = entity.getTaskId() + "";

                    //获取评论列表
                    initCommentList();
                    taskState = entity.getTaskState();
//                    mProgress.setVisibility(View.GONE);
//                    initView();
                    anim();
                    if (entity.getDifferentlyLevel() == 0) {//难易程度
                        tvDifficulty.setText("普通");
                    } else if (entity.getDifferentlyLevel() == 1) {
                        tvDifficulty.setText("困难");
                    }
                    if (entity.getTaskLevel() == 0) {//级别
                        tvLevel.setText("正常");
                    } else if (entity.getTaskLevel() == 1) {
                        tvLevel.setText("紧急");
                    }

                    if (isCanDelete()) {
                    }

                    //设置是否显示子任务
                    if (entity.getChildTaskCount() <= 0) {
                        mSubTask.setVisibility(View.GONE);
                    } else {
                        mSubTask.setVisibility(View.VISIBLE);
                        tvSubtask.setText(entity.getFinshChildTaskCount() + "/" + entity.getChildTaskCount());
                    }

                } else {
                    //mCreatChild.setVisible(false);//加载失败隐藏创建子任务加号
                    setRight1Show(false);//加载失败隐藏创建子任务加号
                    MyToast.showToast(mResult.getMsg());
                }
            }

            @Override
            public void onFailure(String string) {
                MyToast.showToast(R.string.NETWORKERROR);
            }
        });
    }

    /**
     * 加载任务评论列表
     */
    public void initCommentList() {
        //评论列表
        commendList = new ArrayList<>();
        checkList = new ArrayList<>();
        operateList = new ArrayList<>();
        //先设置适配器
        commendListViewAdapter = new MissionCommentListViewAdapter(MissionDetailsActivity.this, commendList);
        checkListViewAdapter = new CheckListViewAdapter(MissionDetailsActivity.this, checkList);
        operateListViewAdapter = new OperateListViewAdapter(MissionDetailsActivity.this, operateList);

        //不聚焦
        mlvComment.setFocusable(false);
        mlvCheck.setFocusable(false);
        mlvOperate.setFocusable(false);

        mlvComment.setAdapter(commendListViewAdapter);
        mlvCheck.setAdapter(checkListViewAdapter);
        mlvOperate.setAdapter(operateListViewAdapter);

        checkList.clear();

        List<MissionReadListEntity> readList = entity.getReadList();
        checkList.addAll(readList);
        checkListViewAdapter.notifyDataSetChanged();

        operateList.clear();
        List<MissionOperateLogListEntity> operList = entity.getOperateLog();
        operateList.addAll(operList);
        operateListViewAdapter.notifyDataSetChanged();

        commendList.clear();
        List<MissionCommendListEntity> commments = entity.getCommments();
        commendList.addAll(commments);
        commendListViewAdapter.notifyDataSetChanged();
    }

    //初始化下方的按钮
    public void initView() {
        hideView();

        //待接受的任务
        if (taskState == 0) {

            //如果是负责人
            if (isLeadPerson()) {
                hideView();
                mComplete.setVisibility(View.VISIBLE);
                mComplete.setText("提交审核");
                isCommit = true;
            }

        }

        //进行中的任务
        if (taskState == 1) {

            //如果是负责人
            if (isLeadPerson()) {
                hideView();
                mComplete.setVisibility(View.VISIBLE);
                mComplete.setText("提交审核");
                isCommit = true;
            }

        }

        //待审核的任务
        if (taskState == 2) {
            //如果是审核人
            if (isCheckPerson()) {
                mAccept.setVisibility(View.VISIBLE);
                mAccept.setText("同意");
                mRefuse.setVisibility(View.VISIBLE);
                mRefuse.setText("驳回");
                isAgree = true;
                isStop = false;
            }
        }

//        //已完成的任务
//        if (taskState == 3) {
//            mProgress.setVisibility(View.GONE);
//        }

        //执行人已完成
        if (taskState == 8) {

            //如果是负责人
            if (isLeadPerson()) {
                hideView();
                mComplete.setVisibility(View.VISIBLE);
                mComplete.setText("提交审核");
                isCommit = true;
            }
        }

        //如果主任务是待接收或进行中
        if (taskState == 0 || taskState == 1) {
            if (isExecutePerson()) {
                //个人待接受
                if (taskPersonState == 10) {
                    hideView();
                    mAccept.setVisibility(View.VISIBLE);
                    mAccept.setText("接受");
                    mRefuse.setVisibility(View.VISIBLE);
                    mRefuse.setText("拒绝");
                    isAgree = false;
                    isStop = true;
                }

                //个人已接受
                if (taskPersonState == 11) {
                    hideView();
                    if (taskState == 1) {
                        mComplete.setVisibility(View.VISIBLE);
                        mComplete.setText("已完成");
                        isCommit = false;
                    }
                }

                //个人进行中
                if (taskPersonState == 12) {
                    //只有是执行人的时候，显示更改进度
                    if (isExecutePerson() && isChildListNull()) {
//                        mProgress.setVisibility(View.VISIBLE);
                        mSeekBar.setEnabled(true);
                    }
                    hideView();
                    mComplete.setVisibility(View.VISIBLE);
                    mComplete.setText("已完成");
                    isCommit = false;
                }

                //个人已完成
                if (taskPersonState == 13) {
                    hideView();
                }

                //个人已拒绝
                if (taskPersonState == 14) {
                    hideView();
                    mAccept.setVisibility(View.VISIBLE);
                    mAccept.setText("接受");
                    isAgree = false;
                }
            }
        }

        if (taskState == 3) {
//            if (isTrunDownPerson) {
//                hideView();
//                mRefuse.setVisibility(View.VISIBLE);
//                mRefuse.setText("驳回");
//                isStop = false;
//            }
        }

    }

    /**
     * 修改任务进度
     *
     * @param taskId
     * @param taskProgress
     */
    public void progress(String taskId, String taskProgress) {
        String ssid = PreferencesUtils.getString(this, LoginActivity.FLAG_SSID);
        String currentCompanyId = PreferencesUtils.getString(this, LoginActivity.FLAG_ZMSCOMPANYID);
        Call call = HttpRequest.getIResourceOANetAction().postProgress(ssid, currentCompanyId, taskId, taskProgress);
        callRequest(call, new HttpCallBack(MissionEntity.class, this, true) {
            @Override
            public void onSuccess(ResultData mResult) {
                if ("0".equals(mResult.getSuccess())) {//0成功
                    MyToast.showToast("修改成功");
//                    setResult(RESULT_OK);
//                    finish();
                } else {
                    MyToast.showToast(mResult.getMsg());
                }
            }

            @Override
            public void onFailure(String string) {
                MyToast.showToast(R.string.NETWORKERROR);
            }
        });
    }

    /**
     * 修改任务状态
     *
     * @param content //     * @param TaskState
     */
    public void postTaskState(String content, String actionType) {
        String ssid = PreferencesUtils.getString(this, LoginActivity.FLAG_SSID);
        String CurrentCompanyId = PreferencesUtils.getString(this, LoginActivity.FLAG_ZMSCOMPANYID, "");
        Call call = HttpRequest.getIResourceOANetAction().postTaskState(ssid, CurrentCompanyId, mTaskId, content, actionType);
        callRequest(call, new HttpCallBack(MissionDetailEntity.class, this, true) {
            @Override
            public void onSuccess(ResultData mResult) {
                if ("0".equals(mResult.getSuccess())) {//0成功
                    switch (taskState) {//状态
                        case 0://待接受
                            break;
                        case 1://进行中
                            break;
                        case 2://待审核
                            setResult(RESULT_OK);
                            finish();
                            break;
                        case 3://已完成
                            setResult(RESULT_OK);
                            finish();
                            break;
                        case 4://已拒绝
                            break;
                        case 5://已驳回
                            break;
                        case 6://已作废
                            break;
                    }
                    getMissionDetails();
                    MyToast.showToast("提交成功");
                } else {
                    String message = mResult.getMsg();
                    MyToast.showToast(message);
                }
            }

            @Override
            public void onFailure(String string) {
                MyToast.showToast(R.string.NETWORKERROR);
            }
        });
    }


    /**
     * 打开子任务详情
     *
     * @param position
     */
    private void openChildDetail(int position) {
        Intent intent = new Intent(MissionDetailsActivity.this, MissionDetailsActivity.class);
        String missionID = missionList.get(position).getID() + "";
        intent.putExtra("id", missionID);
        intent.putExtra("types", 1);
        MissionDetailsActivity.this.startActivity(intent);
    }

    /**
     * 隐藏按钮
     */
    public void hideView() {
        mAccept.setVisibility(View.GONE);
        mComplete.setVisibility(View.GONE);
        mRefuse.setVisibility(View.GONE);
    }

    /**
     * 是否为创建人
     *
     * @return
     */
    public boolean isCreatPerson() {
//        if (!StringUtils.isEmpty(creatId)) {
//            if (creatId.equals(loginId)) {
//                return true;
//            }
//        }
        return false;
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
        if (!StringUtils.isEmpty(executeIds)) {
            String[] array = executeIds.split(",");
            for (int i = 0; i < array.length; i++) {
                if (array[i].equals(loginId)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 执行人是否只有一个
     *
     * @return
     */
    public boolean isExecuteOne() {
        if (!StringUtils.isEmpty(executeIds)) {
            String[] array = executeIds.split(",");
            List<String> list = new ArrayList<>();
            for (int i = 0; i < array.length; i++) {
                if (!StringUtils.isEmpty(array[i])) {
                    list.add(array[i]);
                }
            }
            if (list.size() == 1) {
                return true;
            }
        }
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

    /**
     * 负责人与执行人是否相同
     *
     * @return
     */
    public boolean isAlike() {
        if (!StringUtils.isEmpty(leadId) && !StringUtils.isEmpty(executeIds)) {
            if (leadId.equals(executeIds)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 子任务列表是否为空
     * true null
     * false 不为空
     *
     * @return
     */
    public boolean isChildListNull() {
        if (StringUtils.isEmpty(missionList)) {
            return true;
        }
        return false;
    }

    /**
     * 新建子任务
     */
    public void addNewMission() {
        Intent intent = new Intent(MissionDetailsActivity.this, MissionCreatActivity.class);
        intent.putExtra("parentId", mTaskId);
        intent.putExtra("chargeId", chargePeopleIds);
        intent.putExtra("chargeName", chargePeopleName);
        intent.putExtra("checkerId", checkerPeopleId);
        intent.putExtra("checkerName", checkerPeopleName);
        intent.putExtra("endTime", tvEndTime.getText().toString());
        startActivityForResult(intent, REQUEST_MISSION_ACTIVITY);
    }

    /**
     * 删除任务
     *
     * @param taskId
     * @param currentCompanyId
     * @param type
     */
    public void deleteTask(String taskId, String currentCompanyId, final int type, int ActionType) {
        String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        Call call = HttpRequest.getIResourceOANetAction().postTaskState(ssid, currentCompanyId, taskId, "", ActionType + "");//作废
        callRequest(call, new HttpCallBack(MissionEntity.class, this, true) {
            @Override
            public void onSuccess(ResultData mResult) {
                if ("0".equals(mResult.getSuccess())) {//0成功
                    MyToast.showToast("删除成功");
                    switch (type) {
                        case 1://删除主任务
                            setResult(RESULT_OK);
                            finish();
                            break;
                        case 2://子任务列表删除
                            getMissionDetails();
                            break;
                        case 3://删除子任务
                            setResult(RESULT_OK);
                            finish();
                            break;
                    }
                } else {
                    String message = mResult.getMsg();
                    MyToast.showToast(message);
                }
            }

            @Override
            public void onFailure(String string) {

            }
        });
    }


    //子任务列表的删除
    @Override
    public void deleteChildTask(final View view, final String childId, final String childCurrentCompanyId) {

        if (isCanDelete()) {
            MessageDialog dialog = new MessageDialog(this);
            dialog.setTitle("提示");
            dialog.setMessage("确认删除?");
            dialog.setNegativeButton("取消");
            dialog.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    deleteTask(childId, childCurrentCompanyId, 2, 18);
                }
            });
            dialog.show();
        } else {
            view.setVisibility(View.GONE);
        }
    }

    /**
     * 是否可以删除
     *
     * @return
     */
    public boolean isCanDelete() {
        if (taskState != 0 && taskState != 6) {//任务状态是已完成或已驳回
            return false;
        }
        if (isExecutePerson() && !isLeadPerson() && !isCreatPerson()) {//如果只是执行人
            return false;
        }
        if (isCheckPerson() && !isLeadPerson() && !isCreatPerson()) {//如果只是审核人
            return false;
        }
        if (!isLeadPerson() && !isCreatPerson()) {
            return false;
        }
        return true;
    }

    /**
     * 是否可以创建子任务
     *
     * @return
     */
    public boolean isCanCreatChildTask() {
        if (isExecutePerson() && !isLeadPerson() && !isCreatPerson()) {//如果只是执行人
            return false;
        }
        if (isCheckPerson() && !isLeadPerson() && !isCreatPerson()) {//如果只是审核人
            return false;
        }
        if (!isLeadPerson() && !isCreatPerson()) {
            return false;
        }
        return true;
    }

    public void ShowPopupWindow(List<String> list, final AdapterView
            .OnItemClickListener listener) {
        LinearLayout layout = (LinearLayout) LayoutInflater.from(this).inflate(
                R.layout.base_popupwindow_more, null);

        ListView listView = (ListView) layout.findViewById(R.id.list_popup);

        layout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);//强制绘制layout

        PopupwindowMoreAdapter adapter = new PopupwindowMoreAdapter(this, list);
        listView.setAdapter(adapter);

        WindowManager manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        int xoff = manager.getDefaultDisplay().getWidth() / 2;
        final PopupWindow m_popupWindow = new PopupWindow(layout, xoff, ActionBar.LayoutParams.WRAP_CONTENT);
        m_popupWindow.setFocusable(true);
        //m_popupWindow.setBackgroundDrawable(new BitmapDrawable(null, ""));
        ColorDrawable dw = new ColorDrawable(0x33000000);
        m_popupWindow.setBackgroundDrawable(dw);
        //设置SelectPicPopupWindow弹出窗体的宽
        m_popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        m_popupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        m_popupWindow.showAsDropDown(mLine, xoff, 0);

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_popupWindow.dismiss();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                m_popupWindow.dismiss();
                listener.onItemClick(parent, view, position, id);
            }
        });

    }

    private void addView(String operateName, String actionType) {
        View view = mInflater.inflate(R.layout.view_mission_text, mLayout, false);
        view.setTag(actionType);
        TextView textView = (TextView) view.findViewById(R.id.tv_submit);
        textView.setText(operateName);
        textView.setTag(actionType);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String actionType = v.getTag().toString();
                /// 负责人拒绝 5 执行人拒绝7 审核人驳回10
                if ("5".equals(actionType) || "7".equals(actionType) || "10".equals(actionType)) {
                    mDialog = new InputDialog(MissionDetailsActivity.this);
                    mDialog.setTitle("理由");
                    mDialog.setContentHint("请输入理由");
                    mDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String content = mDialog.getEditText().getText().toString();
                            if (!StringUtils.isEmpty(content)) {
                                postTaskState(content, actionType);
                            } else {
                                MyToast.showToast("理由不能为空");
                            }
                        }
                    });
                    mDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    mDialog.show();
                } else if ("-1".equals(actionType)) {
                    Intent intents = new Intent(MissionDetailsActivity.this, CommentActivity.class);
                    intents.putExtra("TaskId", mTaskId);
                    startActivityForResult(intents, REQUEST_COMMENT_ACTIVITY);
                } else if ("1".equals(actionType)){
                    updateMission();
                } else {
                    postTaskState("", actionType);
                }
            }
        });
        mListV.add(view);
        refresh();
    }

    /**
     * 修改任务
     */
    public void updateMission() {
        String ssid = PreferencesUtils.getString(this, LoginActivity.FLAG_SSID);
        String currentCompanyId = PreferencesUtils.getString(this, LoginActivity.FLAG_ZMSCOMPANYID, "");
        Call call = HttpRequest.getIResourceOANetAction().postUpdateMission(ssid, entity.getTaskId()+"","0", entity.getTitle(), entity.getEndTime(), entity.getWorkHours()+""
                , entity.getWorkMins()+"", entity.getChargerId()+"", executeIds, entity.getReviewerId()+"", entity.getDifferentlyLevel() + "", entity.getTaskLevel() + "", entity.getInfo(), entity.getAttachmentGUID(), currentCompanyId
                , (entity.getTaskState()==15?true:false) + "", "1");
        callRequest(call, new HttpCallBack(MissionEntity.class, this, true) {
            @Override
            public void onSuccess(ResultData mResult) {
                if ("0".equals(mResult.getSuccess())) {
                    MyToast.showToast("修改成功");
                    setResult(RESULT_OK);
                    finish();
                } else {
                    String message = mResult.getMsg();
                    MyToast.showToast(message);
                }
            }

            @Override
            public void onFailure(String string) {
                MyToast.showToast(R.string.NETWORKERROR);
            }
        });
    }
    /**
     * 刷新顶部视图
     */
    private void refresh() {
        mLayout.removeAllViews();
        for (int i = 0; i < mListV.size(); i++) {
            /*if (mListV.size() - 1 == i) {
                ((TextView) mListV.get(i).findViewById(R.id.tv_submit)).setTextColor(getResources().getColor(R.color.tvc9));
            } else {
                ((TextView) mListV.get(i).findViewById(R.id.tv_submit)).setTextColor(getResources().getColor(R.color.cpb_blue));
            }*/
            mLayout.addView(mListV.get(i));
        }
        if (StringUtils.isEmpty(mListV)) {
            mLayout.setVisibility(View.GONE);
        } else {
            mLayout.setVisibility(View.VISIBLE);
        }
    }


}
