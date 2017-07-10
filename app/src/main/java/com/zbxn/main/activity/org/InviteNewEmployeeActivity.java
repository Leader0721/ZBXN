package com.zbxn.main.activity.org;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
 * Created by Administrator on 2016/12/29.
 */
public class InviteNewEmployeeActivity extends BaseActivity {


    @BindView(R.id.delete_item1)//删除item
            ImageView deleteItem1;
    @BindView(R.id.num_inviteEmpl1)//排序号
            TextView numInviteEmpl1;
    @BindView(R.id.invite_name1)//第一个员工姓名
            EditText inviteName1;
    @BindView(R.id.invite_phoneNum1)//第二个员工电话
            EditText invitePhoneNum1;
    @BindView(R.id.delete_item2)
    ImageView deleteItem2;
    @BindView(R.id.num_inviteEmpl2)
    TextView numInviteEmpl2;
    @BindView(R.id.invite_name2)
    EditText inviteName2;
    @BindView(R.id.invite_phoneNum2)
    EditText invitePhoneNum2;
    @BindView(R.id.linearlayout_invite2)
    LinearLayout linearlayoutInvite2;


    @BindView(R.id.delete_item3)
    ImageView deleteItem3;
    @BindView(R.id.num_inviteEmpl3)
    TextView numInviteEmpl3;
    @BindView(R.id.invite_name3)
    EditText inviteName3;
    @BindView(R.id.invite_phoneNum3)
    EditText invitePhoneNum3;
    @BindView(R.id.linearlayout_invite3)
    LinearLayout linearlayoutInvite3;
    @BindView(R.id.footview_textInvite)
    TextView footviewTextInvite;
    @BindView(R.id.footView_invite)
    LinearLayout footViewInvite;

    private int NUM = 1;
    boolean firstIsEmpty = true;
    boolean secondIsEmpty = true;
    List<InviteEmplEntity> mList = new ArrayList<>();

    private String name1;
    private String phoneNum1;
    private String name2;
    private String phoneNum2;
    private String name3;
    private String phoneNum3;
    private InviteEmplEntity entity1, entity2, entity3;
    private boolean isRight = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitenewemployee);
        ButterKnife.bind(this);
        entity1 = new InviteEmplEntity();
        entity2 = new InviteEmplEntity();
        entity3 = new InviteEmplEntity();
        initData1();

    }

    private void initData1() {

        inviteName1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString();
                name1 = input;

            }
        });

        invitePhoneNum1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString();
                phoneNum1 = input;
            }
        });
    }


    private void initData2() {
        inviteName2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString();
                name2 = input;

            }
        });

        invitePhoneNum2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString();
                phoneNum2 = input;
            }
        });
    }


    private void initData3() {
        inviteName3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString();
                name3 = input;

            }
        });

        invitePhoneNum3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString();
                phoneNum3 = input;
            }
        });

        if (TextUtils.isEmpty(name3) && TextUtils.isEmpty(phoneNum3)) {
            footViewInvite.setVisibility(View.GONE);
        } else {
            footViewInvite.setVisibility(View.GONE);
        }
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

        name1 = inviteName1.getText().toString();
        name2 = inviteName2.getText().toString();
        name3 = inviteName3.getText().toString();
        phoneNum1 = invitePhoneNum1.getText().toString();
        phoneNum2 = invitePhoneNum2.getText().toString();
        phoneNum3 = invitePhoneNum3.getText().toString();
        entity1.setEmployeeName(name1);
        entity2.setEmployeeName(name2);
        entity3.setEmployeeName(name3);
        entity1.setEmployeePhone(phoneNum1);
        entity2.setEmployeePhone(phoneNum2);
        entity3.setEmployeePhone(phoneNum3);
        mList.clear();
        mList.add(0, entity1);




        if (StringUtils.isEmpty(inviteName1)) {
            MyToast.showToast("请输入姓名");
            isRight = false;
        }
        if (!ValidationUtil.isMobile(StringUtils.getEditText(invitePhoneNum1)) && !StringUtils.isEmpty(inviteName1)) {
            MyToast.showToast("请输入正确的手机号");
            isRight = false;
        }

        if (linearlayoutInvite2.getVisibility() == View.VISIBLE) {
            mList.add(1, entity2);
            if (StringUtils.isEmpty(inviteName2)) {
                MyToast.showToast("请输入姓名");
                isRight = false;
            }
            if (!ValidationUtil.isMobile(StringUtils.getEditText(invitePhoneNum2))) {
                MyToast.showToast("请输入正确的手机号");
                isRight = false;
            }
        }
        if (linearlayoutInvite3.getVisibility() == View.VISIBLE) {
            mList.add(2, entity3);
            if (StringUtils.isEmpty(inviteName3)) {
                MyToast.showToast("请输入姓名");
                isRight = false;
            }
            if (!ValidationUtil.isMobile(StringUtils.getEditText(invitePhoneNum3))) {
                MyToast.showToast("请输入正确的手机号");
                isRight = false;
            }
        }

        if (TextUtils.isEmpty(name3) && TextUtils.isEmpty(phoneNum3)) {

        } else {
//            entity3.setName(name3);
//            entity3.setPhoneNum(phoneNum3);
//            mList.add(2, entity3);
        }

        if (isRight) {
            final Gson gson = new Gson();
            String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
            String CurrentCompanyId = PreferencesUtils.getString(BaseApp.getContext(), LoginActivity.FLAG_ZMSCOMPANYID, "");
            Call call = HttpRequest.getIResourceOANetAction().getInviteNewEmployee(ssid, CurrentCompanyId, gson.toJson(mList));
            Log.d("gsons",gson.toJson(mList));
            callRequest(call, new HttpCallBack(JobEntity.class, InviteNewEmployeeActivity.this, true) {
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
        isRight = true;
    }


    @OnClick({R.id.delete_item2, R.id.delete_item3, R.id.footview_textInvite, R.id.footView_invite})
    public void onClick(View view) {
        if (mList.size() == 1) {
            firstIsEmpty = TextUtils.isEmpty(name1) && TextUtils.isEmpty(phoneNum1);
        } else if (mList.size() == 2) {
            secondIsEmpty = TextUtils.isEmpty(name2) && TextUtils.isEmpty(phoneNum2);
        }

        switch (view.getId()) {
            case R.id.delete_item2:

                linearlayoutInvite2.setVisibility(View.GONE);
                mList.clear();
                mList.add(0, entity1);
                NUM = 1;
                name2 = "";
                phoneNum2 = "";
                inviteName2.setText("");
                invitePhoneNum2.setText("");

                break;
            case R.id.delete_item3:
                deleteItem2.setVisibility(View.VISIBLE);
                linearlayoutInvite3.setVisibility(View.GONE);
                mList.clear();
                mList.add(0, entity1);
                mList.add(1, entity2);
                footViewInvite.setVisibility(View.VISIBLE);
                NUM = 2;
                name3 = "";
                phoneNum3 = "";
                inviteName3.setText("");
                invitePhoneNum3.setText("");
                secondIsEmpty = true;
                break;
            case R.id.footview_textInvite:
                if (NUM == 1) {
                    if (!ValidationUtil.isMobile(phoneNum1)) {
                        MyToast.showToast("请把信息补充完整");
                        break;
                    }
                    if (TextUtils.isEmpty(name1) && TextUtils.isEmpty(phoneNum1)) {
                        MyToast.showToast("请把信息补充完整");
                    } else {
                        entity1.setEmployeeName(name1);
                        entity1.setEmployeePhone(phoneNum1);
                        linearlayoutInvite2.setVisibility(View.VISIBLE);
                        NUM = 2;
                        initData2();
                        mList.add(0, entity1);
                    }
                } else if (NUM == 2) {
                    deleteItem2.setVisibility(View.GONE);
                    if (!ValidationUtil.isMobile(phoneNum2)) {
                        MyToast.showToast("请把信息补充完整");
                        break;
                    }
                    if (TextUtils.isEmpty(name2) && TextUtils.isEmpty(phoneNum2)) {
                        MyToast.showToast("请把信息补充完整");
                    } else {
                        entity2.setEmployeeName(name2);
                        entity2.setEmployeePhone(phoneNum2);
                        linearlayoutInvite3.setVisibility(View.VISIBLE);
                        NUM = 3;
                        initData3();
                        mList.add(1, entity2);
                    }
                }
                break;
            case R.id.footView_invite:
                if (NUM == 1) {
                    if (!ValidationUtil.isMobile(phoneNum1)) {
                        MyToast.showToast("请把信息补充完整");
                        break;
                    }
                    if (TextUtils.isEmpty(name1) && TextUtils.isEmpty(phoneNum1)) {
                        MyToast.showToast("请完善第一个员工信息");
                    } else {
                        entity1.setEmployeeName(name1);
                        entity1.setEmployeePhone(phoneNum1);
                        linearlayoutInvite2.setVisibility(View.VISIBLE);
                        NUM = 2;
                        initData2();
                        mList.add(0, entity1);
                    }
                } else if (NUM == 2) {
//                    deleteItem2.setVisibility(View.GONE);
                    if (!ValidationUtil.isMobile(phoneNum2)) {
                        MyToast.showToast("请填写正确的手机号");
                        break;
                    }
                    if (TextUtils.isEmpty(name2) && TextUtils.isEmpty(phoneNum2)) {
                        MyToast.showToast("请完善第二个员工信息");
                    } else {
                        deleteItem2.setVisibility(View.GONE);
                        linearlayoutInvite3.setVisibility(View.VISIBLE);
                        deleteItem2.setVisibility(View.GONE);
                        entity2.setEmployeeName(name2);
                        entity2.setEmployeePhone(phoneNum2);
                        NUM = 3;
                        initData3();
                        mList.add(1, entity2);
                    }
                }
                break;
        }
    }
}
