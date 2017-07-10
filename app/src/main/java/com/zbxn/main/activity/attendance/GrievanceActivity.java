package com.zbxn.main.activity.attendance;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pub.base.BaseActivity;
import com.pub.base.BaseApp;
import com.pub.http.HttpCallBack;
import com.pub.http.HttpRequest;
import com.pub.http.ResultData;
import com.pub.utils.MyToast;
import com.pub.utils.PreferencesUtils;
import com.pub.utils.StringUtils;
import com.pub.widget.slidedatetimepicker.NewSlideDateTimeDialogFragment;
import com.pub.widget.slidedatetimepicker.SlideDateTimeListener;
import com.pub.widget.slidedatetimepicker.SlideDateTimePicker;
import com.zbxn.R;
import com.zbxn.main.activity.login.LoginActivity;
import com.zbxn.main.entity.AttendanceRecordEntity;
import com.zbxn.main.entity.GrievanceTypeEntity;
import com.zbxn.main.listener.ICustomListener;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

/**
 * 考勤申诉
 * Created by Administrator on 2016/12/19.
 */
public class GrievanceActivity extends BaseActivity {
    private static final int Flag_Callback_ContactsPicker = 1001;//申诉类型回调
    @BindView(R.id.grievance_type)
    TextView grievanceType;
    @BindView(R.id.mType)
    LinearLayout mType;
    @BindView(R.id.grievance_edit)
    EditText grievanceEdit;
    @BindView(R.id.mState)
    TextView mState;
    @BindView(R.id.mTime)
    TextView mTime;
    @BindView(R.id.mAddr)
    TextView mAddr;
    @BindView(R.id.mAddressLayout)
    LinearLayout mAddressLayout;
    @BindView(R.id.mState2)
    TextView mState2;
    @BindView(R.id.mTime2)
    TextView mTime2;
    @BindView(R.id.mAddr2)
    TextView mAddr2;
    @BindView(R.id.mAddressLayout2)
    LinearLayout mAddressLayout2;
    @BindView(R.id.mLayout2)
    LinearLayout mLayout2;
    @BindView(R.id.text_new_time)
    TextView textNewTime;
    @BindView(R.id.mTimeLayout)
    LinearLayout mTimeLayout;
    @BindView(R.id.text_new_time_tip)
    TextView textNewTimeTip;
    @BindView(R.id.mLayout1)
    LinearLayout mLayout1;

    private MenuItem mCollect;
    //类型
    private String mAppealType = "1";//默认 补签

    private AttendanceRecordEntity entity;

    private String type = "";//1迟到  2早退

    private SimpleDateFormat selectFormat = new SimpleDateFormat("HH:mm");
    private String dayTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grievance);
        ButterKnife.bind(this);
        setTitle("申诉");
        entity = (AttendanceRecordEntity) getIntent().getParcelableExtra("item");
        type = getIntent().getStringExtra("type");
        if (entity != null) {
            dayTime = entity.getAttendanceDate();
        } else {
            dayTime = selectFormat.format(new Date());
        }
        initView();
        initData();

    }

    private void initView() {
        //1迟到  2早退
        if ("1".equals(type)) {
            textNewTimeTip.setText("签到时间");
            mLayout1.setVisibility(View.VISIBLE);
            mLayout2.setVisibility(View.GONE);
        } else {
            textNewTimeTip.setText("签退时间");
            mLayout1.setVisibility(View.GONE);
            mLayout2.setVisibility(View.VISIBLE);
        }
    }

    private void initData() {
        grievanceType.setText("补签");
        if (null != entity) {
            mTime.setText(StringUtils.convertDateMin(entity.getCheckInTime()));
            mState.setText(entity.getCheckInStateName());
            mAddr.setText("地点:" + entity.getCheckInAddress());
            if (StringUtils.isEmpty(entity.getCheckInAddress())) {
                mAddressLayout.setVisibility(View.GONE);
            } else {
                mAddressLayout.setVisibility(View.VISIBLE);
            }
            if (StringUtils.isEmpty(entity.getCheckOutAddress())) {
                mAddressLayout2.setVisibility(View.GONE);
            } else {
                mAddressLayout2.setVisibility(View.VISIBLE);
            }

            if (entity.getCheckInStateName().equals("正常")) {
                mState.setTextColor(getResources().getColor(R.color.tvc6));
            } else {
                mState.setTextColor(getResources().getColor(R.color.orange));
            }
            if (entity.getCheckOutStateName().equals("正常")) {
                mState2.setTextColor(getResources().getColor(R.color.tvc6));
            } else {
                mState2.setTextColor(getResources().getColor(R.color.orange));
            }

            if (!"未签到".equals(entity.getCheckOutStateName())) {
                mTime2.setText(StringUtils.convertDateMin(entity.getCheckOutTime()));
                mState2.setText(entity.getCheckOutStateName());
                mAddr2.setText("地点:" + entity.getCheckOutAddress());
            }
        }
    }


    @Override
    public void initRight() {
        setRight1Show(true);
        setRight1("确定");
        setRight1Icon(R.mipmap.complete2);
        setRight2Show(false);

    }

    @Override
    public void actionRight1(MenuItem menuItem) {

        if (!validate()) {
            return;
        }
        String appealtime = entity.getAttendanceDate() + " " + StringUtils.getEditText(textNewTime) + ":00";
        //完成提交
        save(this, entity.getID() + "", type, mAppealType, appealtime, StringUtils.getEditText(grievanceEdit), mICustomListener);
    }








 /*   @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        mCollect = menu.findItem(R.id.mEstablish);
        mCollect.setEnabled(true);
        mCollect.setTitle("确定");
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mEstablish:
                if (!validate()) {
                    break;
                }
                String appealtime = entity.getCheckTime() + " " + StringUtils.getEditText(textNewTime) + ":00";
                //完成提交
                save(this, entity.getId() + "", type, mAppealType, appealtime, StringUtils.getEditText(grievanceEdit), mICustomListener);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

*/

    /**
     * 提交前验证
     *
     * @return
     */
    private boolean validate() {
        if (StringUtils.isEmpty(grievanceType)) {
            MyToast.showToast("请选择类型");
            return false;
        }

        if (StringUtils.isEmpty(textNewTime)) {
            MyToast.showToast("请选择时间");
            return false;
        }

        if (StringUtils.getEditText(grievanceEdit).length() < 10) {
            MyToast.showToast("申诉理由不能小于10个字");
            return false;
        }
        return true;
    }


    @OnClick({R.id.mType, R.id.mTimeLayout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mType:
                startActivityForResult(new Intent(this, GrievanceTypeActivity.class), Flag_Callback_ContactsPicker);
                break;
            case R.id.mTimeLayout:
                /**
                 * 日期选择器
                 */
                String time = "";
                if (!StringUtils.isEmpty(textNewTime)) {
                    time = StringUtils.getEditText(textNewTime);
                }
                new SlideDateTimePicker.Builder(getSupportFragmentManager())
                        .setListener(new SlideDateTimeListener() {
                            @Override
                            public void onDateTimeSet(Date date) {
                                textNewTime.setText(selectFormat.format(date));
                                dayTime = StringUtils.getEditText(textNewTime);
                            }
                        })
                        .setInitialDate(StringUtils.convertToDate(selectFormat, time))
                        .setIs24HourTime(true)
                        .setIsHaveTime(NewSlideDateTimeDialogFragment.Have_Time)
                        .build()
                        .show();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Flag_Callback_ContactsPicker:
                if (data != null) {
                    if (resultCode == RESULT_OK) {
                        String typeName = data.getStringExtra("typeName"); // 类型名称
                        mAppealType = data.getStringExtra("appealType"); // 需要提交的字段
                        if (!typeName.equals(null)) {
                            grievanceType.setText(typeName);
                        }
                    }
                }
                break;
        }
    }

    private ICustomListener mICustomListener = new ICustomListener() {
        @Override
        public void onCustomListener(int obj0, Object obj1, int position) {
            switch (obj0) {
                case 0:
//                    message().show("提交成功");
                    MyToast.showToast("提交成功");
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    finish();
                    break;
            }
        }
    };


    //网络数据请求的重新封装

    /**
     * 考勤申诉
     *
     * @param context
     * @param attendanceid
     * @param appealsource
     * @param appealType
     * @param appealtime
     * @param appealreason
     * @param listener
     */
    public void save(Context context, String attendanceid, String appealsource
            , String appealType, String appealtime, String appealreason, final ICustomListener listener) {
        String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        String currentCompanyId = PreferencesUtils.getString(BaseApp.getContext(), LoginActivity.FLAG_ZMSCOMPANYID);
        Call call = HttpRequest.getIResourceOANetAction().SaveDataList(ssid, currentCompanyId, attendanceid, appealsource, appealType, appealtime, appealreason);
        callRequest(call, new HttpCallBack(GrievanceTypeEntity.class, this, true) {
            @Override
            public void onSuccess(ResultData mResult) {
                if ("0".equals(mResult.getSuccess())) {//0成功
                    listener.onCustomListener(0, null, 0);
                } else {
                    String message = mResult.getMsg();
                    MyToast.showToast(message);
                }
            }

            @Override
            public void onFailure(String string) {
                MyToast.showToast("获取网络数据失败");
            }
        });
    }

}
