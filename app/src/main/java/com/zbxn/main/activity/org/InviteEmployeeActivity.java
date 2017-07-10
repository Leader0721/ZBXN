package com.zbxn.main.activity.org;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.pub.base.BaseActivity;
import com.pub.base.BaseApp;
import com.pub.http.HttpCallBack;
import com.pub.http.HttpRequest;
import com.pub.http.ResultData;
import com.pub.utils.MyToast;
import com.pub.utils.PreferencesUtils;
import com.pub.utils.StringUtils;
import com.pub.utils.ValidationUtil;
import com.zbxn.R;
import com.zbxn.main.activity.login.LoginActivity;
import com.zbxn.main.entity.InviteEmplEntity;
import com.zbxn.main.entity.JobEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

/**
 * Created by Administrator on 2017/2/6.
 */
public class InviteEmployeeActivity extends BaseActivity {


    @BindView(R.id.container_layout)
    LinearLayout containerLayout;
    List<InviteEmplEntity> mList = new ArrayList<>();

    private boolean IsOne = false;
    private boolean isEmpty = false;
    private String message = "请完善上述员工消息";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inviteemployee);
        ButterKnife.bind(this);
        addEmployee();
    }


    @Override
    public void initRight() {
        setTitle("邀请员工");
        setRight1("完成");
        setRight1Icon(R.mipmap.complete2);
        setRight1Show(true);
    }

    //菜单点击事件
    @Override
    public void actionRight1(MenuItem menuItem) {
        if (validate()){
            final Gson gson = new Gson();
            String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
            String CurrentCompanyId = PreferencesUtils.getString(BaseApp.getContext(), LoginActivity.FLAG_ZMSCOMPANYID, "");
            Call call = HttpRequest.getIResourceOANetAction().getInviteNewEmployee(ssid, CurrentCompanyId, gson.toJson(mList));
            Log.d("gsons", gson.toJson(mList));
            callRequest(call, new HttpCallBack(JobEntity.class, InviteEmployeeActivity.this, true) {
                @Override
                public void onSuccess(ResultData mResult) {
                    if ("0".equals(mResult.getSuccess())) {
//                        MyToast.showToast("1:"+phoneNum1);
                        MyToast.showToast("已发送");
//                        MyToast.showToast("添加成功");
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

    /**
     * 提交前验证
     *
     * @return
     */
    private boolean validate() {
        mList.clear();
        for (int i = 0; i < containerLayout.getChildCount(); i++) {
            InviteEmplEntity entity = new InviteEmplEntity();
            LinearLayout layout = (LinearLayout) containerLayout.getChildAt(i);// 获得子item的layout
            EditText et_Name = (EditText) layout.findViewById(R.id.et_name);// 从layout中获得控件,根据其id
            EditText et_phoneNum = (EditText) layout.findViewById(R.id.et_phoneNum);//
            entity.setEmployeeName(StringUtils.getEditText(et_Name));
            entity.setEmployeePhone(StringUtils.getEditText(et_phoneNum));

            mList.add(entity);

            if (StringUtils.isEmpty(et_Name)) {
                MyToast.showToast("请输入姓名");
                return false;
            }
            if (StringUtils.isEmpty(et_phoneNum)) {
                MyToast.showToast("请输入手机号");
                return false;
            }
            if (!ValidationUtil.isMobile(StringUtils.getEditText(et_phoneNum))) {
                MyToast.showToast("请输入正确的手机号");
                return false;
            }
        }

        return true;
    }

    private void addEmployee(){
        if (containerLayout.getChildCount() == 1) {
            IsOne = true;
            View view1 = LayoutInflater.from(this).inflate(R.layout.linearlayout_item_invite, null, false);
            ImageView deleteItem1 = (ImageView) view1.findViewById(R.id.delete_item3);
            deleteItem1.setVisibility(View.INVISIBLE);
        }
        isEmpty = true;
        final View view1 = LayoutInflater.from(this).inflate(R.layout.linearlayout_item_invite, null, false);
        ImageView deleteItem1 = (ImageView) view1.findViewById(R.id.delete_item3);
        deleteItem1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                containerLayout.removeView((View) v.getParent().getParent().getParent());
                if (containerLayout.getChildCount() == 1 || containerLayout.getChildCount() == 0) {
                    IsOne = false;
                }
            }
        });
        message = "请完善上述员工信息";
        for (int i = 0; i < containerLayout.getChildCount(); i++) {
            LinearLayout layout = (LinearLayout) containerLayout.getChildAt(i);// 获得子item的layout
            EditText et_Name = (EditText) layout.findViewById(R.id.et_name);// 从layout中获得控件,根据其id
            EditText et_phoneNum = (EditText) layout.findViewById(R.id.et_phoneNum);//
            System.out.println("the text of " + i + "'s EditText：----------->" + et_Name.getText() + "'eeee EditText：----------->" + et_phoneNum.getText());
            if (StringUtils.isEmpty(et_Name.getText().toString()) || StringUtils.isEmpty(et_phoneNum.getText().toString())) {
                isEmpty = true;
            } else {
                if (ValidationUtil.isMobile(et_phoneNum.getText().toString())) {
                    isEmpty = false;
                } else {
                    isEmpty = true;
                    message = "请输入正确的手机号";
                }
            }
        }
        if (IsOne) {
            if (isEmpty) {
                MyToast.showToast(message);
            } else {
                containerLayout.addView(view1);
            }
        } else {
            containerLayout.addView(view1);
            IsOne = true;
        }
    }
    @OnClick({R.id.createNewEmployee, R.id.linear_newcontact})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.createNewEmployee:
                addEmployee();
                break;
            case R.id.linear_newcontact:
                addEmployee();
                break;
        }
    }
}
