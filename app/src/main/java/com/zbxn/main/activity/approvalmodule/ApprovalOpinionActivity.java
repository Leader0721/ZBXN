package com.zbxn.main.activity.approvalmodule;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pub.base.BaseActivity;
import com.pub.http.HttpCallBack;
import com.pub.http.HttpRequest;
import com.pub.http.ResultData;
import com.pub.utils.MyToast;
import com.pub.utils.PreferencesUtils;
import com.pub.utils.StringUtils;
import com.zbxn.R;
import com.zbxn.main.activity.contacts.ContactsChoseActivity;
import com.zbxn.main.activity.login.LoginActivity;
import com.zbxn.main.entity.ApplyEntity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

/**
 * 审批意见
 *
 * @author: ysj
 * @date: 2016-10-11 16:04
 */
public class ApprovalOpinionActivity extends BaseActivity {

    private static final int Flag_ApplyForm_Next = 1000; //同意并转审
    private static final int Flag_ApplyForm_End = 1001;  //同意并结束
    private static final int Flag_CallBack_Select = 1002; //选择转审人
    private static final int Flag_ApplyForm_Rejected = 1003;// 驳回
    private static final int Flag_ApplyForm_Stop = 1004;// 终止

    private static final int Flag_State_0 = 0; //审批中
    private static final int Flag_State_1 = 1; //同意
    private static final int Flag_State_2 = 2; //驳回
    private static final int Flag_State_3 = 3; //终止

    @BindView(R.id.opinion_text)
    TextView opinionText;
    @BindView(R.id.opinion_edit)
    EditText opinionEdit;
    @BindView(R.id.opinion_name)
    TextView opinionName;
    @BindView(R.id.opinion_layout)
    LinearLayout opinionLayout;

    private int flag = -1;//页面标记
    private MenuItem mCollect;

    private String mApprovalName;//审批人name
    private int mApprovalId;//审批人id
    private String approvalID;

    private boolean isAgree; // 是否同意

    /**
     * 创建按钮显示
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_schedule_edit, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * 创建按钮监听
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        mCollect = menu.findItem(R.id.mScheduleEdit);
        mCollect.setTitle("确定");
        mCollect.setEnabled(true);
        return true;
    }

    /**
     * 创建按钮的点击事件
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String content = opinionEdit.getText().toString();
        switch (item.getItemId()) {
            case R.id.mScheduleEdit:
                if (StringUtils.isEmpty(content) && !isAgree) {
                    MyToast.showToast("内容不能为空");
                } else {
                    switch (flag) {
                        case Flag_ApplyForm_Next://同意并转审
                            postApprovalOpinion(Flag_State_0);
                            break;
                        case Flag_ApplyForm_End://同意并结束
                            postApprovalOpinion(Flag_State_1);
                            break;
                        case Flag_ApplyForm_Rejected://驳回
                            postApprovalOpinion(Flag_State_2);
                            break;
                        case Flag_ApplyForm_Stop://终止
                            postApprovalOpinion(Flag_State_3);
                            break;
                    }
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opinion);
        ButterKnife.bind(this);
        setTitle("审批意见");
        initView();

    }

    private void initView() {
        Intent intent = getIntent();
        flag = intent.getIntExtra("flag", -1);
        approvalID = intent.getStringExtra("approvalID");
        isAgree = intent.getBooleanExtra("isAgree", false);
        if (flag == Flag_ApplyForm_Next) {
            getToolbarHelper().setTitle("审批意见-同意转审批");
            opinionLayout.setVisibility(View.VISIBLE);
        } else if (flag == Flag_ApplyForm_End) {
            getToolbarHelper().setTitle("审批意见-同意并结束");
        } else if (flag == Flag_ApplyForm_Rejected) {
            getToolbarHelper().setTitle("审批意见-驳回");
        } else if (flag == Flag_ApplyForm_Stop) {
            getToolbarHelper().setTitle("审批意见-终止");
        }
    }


    @OnClick({R.id.opinion_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            //选择审批人
            case R.id.opinion_layout:
                Intent intent = new Intent(this, ContactsChoseActivity.class);
                intent.putExtra("type", 2);//0-查看 1-多选 2-单选
                startActivityForResult(intent, Flag_CallBack_Select);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Flag_CallBack_Select) {
            if (resultCode == RESULT_OK) {
                mApprovalId = data.getIntExtra("id", -1);
                mApprovalName = data.getStringExtra("name");
                opinionName.setText(mApprovalName);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 修改审批状态
     *
     * @param state
     */
    public void postApprovalOpinion(int state) {
        String ssid = PreferencesUtils.getString(this, LoginActivity.FLAG_SSID);
        Call call = HttpRequest.getIResourceOA().postApprovalOpinion(ssid, approvalID, state, StringUtils.getEditText(opinionEdit), mApprovalId + "");
        callRequest(call, new HttpCallBack(ApplyEntity.class, this, true) {
            @Override
            public void onSuccess(ResultData mResult) {
                if (mResult.getSuccess().equals("0")) {
                    MyToast.showToast("修改成功");
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
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
