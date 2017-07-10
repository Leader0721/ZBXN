package com.zbxn.crm.activity.custom;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.lidroid.xutils.db.table.Id;
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
import com.zbxn.main.entity.JobEntity;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

/**
 * Created by Administrator on 2017/1/13.
 */
public class NewCostActivity extends BaseActivity {

    @BindView(R.id.et_project)
    EditText etProject;
    @BindView(R.id.et_count)
    EditText etCount;
    @BindView(R.id.et_time)
    TextView etTime;
    @BindView(R.id.et_content)
    EditText etContent;
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private String ID;
    private String CustomID;
    private String First;
    private String Second;
    private String Third;
    private String Forth;
    private String title = "新建花费费用";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cost);
        ButterKnife.bind(this);
        etCount.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        Intent intent = getIntent();
        CustomID = CustomActivity.CUSTOMID;
        ID = intent.getStringExtra("CustomID");
        First = intent.getStringExtra("First");
        Second = intent.getStringExtra("Second");
        Third = intent.getStringExtra("Third");
        Forth = intent.getStringExtra("Forth");
        if (!StringUtils.isEmpty(intent.getStringExtra("Title"))){
            title = intent.getStringExtra("Title");
        }

        etProject.setText(First);
        etCount.setText(Second);
        etTime.setText(Third);
        etContent.setText(Forth);
        setTitle(title);
    }

    @Override
    public void initRight() {
        setRight2Show(false);
        setRight1Show(true);
        setRight1Icon(0);
        setRight1("确定");
        super.initRight();
    }

    @Override
    public void actionRight1(MenuItem menuItem) {
        super.actionRight1(menuItem);
        if (isRight()){
            String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
            String CurrentCompanyId = PreferencesUtils.getString(BaseApp.getContext(), LoginActivity.FLAG_ZMSCOMPANYID, "");
            Call call = HttpRequest.getIResourceOANetAction().createPay(ssid, CurrentCompanyId,
                    ID,etProject.getText().toString(),
                    etCount.getText().toString(),etTime.getText().toString(),etContent.getText().toString(),CustomID);
            callRequest(call, new HttpCallBack(JobEntity.class, NewCostActivity.this, false) {
                @Override
                public void onSuccess(ResultData mResult) {
                    if ("0".equals(mResult.getSuccess())) {
                        setResult(RESULT_OK);
                        finish();
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

    }

    private boolean isRight() {
        boolean isRight = false;
        if (StringUtils.isEmpty(etProject)){
            MyToast.showToast("请输入花费项目名称");
        }else if(StringUtils.isEmpty(etCount)){
            MyToast.showToast("请输入金额");
        }else if(Double.parseDouble(etCount.getText().toString()) == 0){
            MyToast.showToast("输入的金额不能为零");
        }else if(StringUtils.isEmpty(etTime)){
            MyToast.showToast("请输入花费时间");
        }else {
            isRight = true;
        }
        return isRight;
    }


    @OnClick(R.id.et_time)
    public void onClick() {
        String time = StringUtils.getEditText(etTime);
        new SlideDateTimePicker.Builder(getSupportFragmentManager())
                .setListener(new SlideDateTimeListener() {
                    @Override
                    public void onDateTimeSet(Date date) {
                        etTime.setText(format.format(date));
                        etTime.setTextColor(getResources().getColor(R.color.tvc6));
                    }
                })
                .setInitialDate(StringUtils.convertToDate(format, time))
                .setIs24HourTime(true)
                .setIsHaveTime(NewSlideDateTimeDialogFragment.Have_Date_Time)
                .build()
                .show();
    }
}
