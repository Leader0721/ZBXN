package com.zbxn.main.activity.memberCenter;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.pub.base.BaseActivity;
import com.pub.base.BaseApp;
import com.pub.dialog.ProgressDialog;
import com.pub.http.HttpCallBack;
import com.pub.http.HttpRequest;
import com.pub.http.ResultData;
import com.pub.http.ResultNetData;
import com.pub.utils.MyToast;
import com.pub.utils.PreferencesUtils;
import com.pub.utils.StringUtils;
import com.pub.utils.ValidationUtil;
import com.zbxn.R;
import com.zbxn.main.entity.Member;
import com.zbxn.main.entity.MissionEntity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

/**
 * Created by Administrator on 2016/12/27.
 */
public class ModifyPasswordActivity extends BaseActivity implements View.OnClickListener{
    /**
     * 登录回调
     */
    private static final int Flag_Callback_Login = 1000;
    //输入的密码
    public static final String FLAG_INPUT_PASSWORD = "password";
    // private TopMessageController mMessageController;
    private ProgressDialog mProgressDialog;
    @BindView(R.id.mOriginalPassword)//原始密码
            EditText mOriginalPassword;
    @BindView(R.id.mNewPassword)//修改密码
            EditText mNewPassword;
    @BindView(R.id.mSecondPassword)//第二次输入
            EditText mSecondPassword;
    @BindView(R.id.ConfirmModification)//确认修改
            Button ConfirmModification;

//    private Handler mHandler = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what){
//                case 100:
//                    finish();
//                    break;
//            }
//        }
//    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        setTitle("修改密码");
        ButterKnife.bind(this);
    }


    /**
     * 创建按钮显示
     *
     * @return
     */

    @Override
    public void initRight() {
        setRight1("确定");
        setRight1Icon(R.mipmap.complete2);
        setRight1Show(true);
        super.initRight();
    }

    @Override
    public void actionRight1(MenuItem menuItem) {
        onclickl();
        super.actionRight1(menuItem);
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

        switch (item.getItemId()) {
            case R.id.itemOk:
                onclickl();
                break;
        }
        return super.onOptionsItemSelected(item);
    }



    @OnClick({R.id.ConfirmModification})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.ConfirmModification://按钮
                onclickl();

                break;

        }
    }

    /**
     * 判断输入的格式是否正确
     */

    public void onclickl() {


        String origin = mOriginalPassword.getText().toString();
        String newpass = mNewPassword.getText().toString();
        String secondp = mSecondPassword.getText().toString();


        // 输入是否合法
        boolean isValidate = false;

        if (origin.length() != 0) {
            if (newpass != null && newpass.length() >= 8 && newpass.length() <= 20) {
                isValidate = ValidationUtil.isABCNumber(newpass);
                if (!StringUtils.isEmpty(secondp)) {
                    if (isValidate) {
                        if (secondp.equals(newpass)) {
                            login();
                        } else {
                            MyToast.showToast("您输入的两次密码不相同");
                        }
                    } else {
                        MyToast.showToast("您输入的新密码格式有误");
                    }
                } else {
                    MyToast.showToast("请再次输入新密码");
                }
            } else if (StringUtils.isEmpty(newpass)) {
                MyToast.showToast("新密码不能为空");
            } else {
                MyToast.showToast("您输入的新密码位数错误");
            }
        } else {
            MyToast.showToast("原始密码不能为空");
        }

    }

//
//    @Override
//    public String getOriginalPassword() {
//        return mOriginalPassword.getText().toString();
//    }
//
//    @Override
//    public String getNewPassword() {
//        return mNewPassword.getText().toString();
//    }
//
//    @Override
//    public String getSecondPassword() {
//        return mSecondPassword.getText().toString();
//    }

//    @Override
//    public void finishForResult(boolean b) {
//        setResult(b ? RESULT_OK : RESULT_CANCELED);
//        Message message = new Message();
//        message.what = 100;
//        mHandler.sendMessageDelayed(message,1500);
//    }


    /**
     * 更改密码
     */
    public void login() {
        String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        String secondPassword = mNewPassword.getText().toString();
        String OriginalPassword = mOriginalPassword.getText().toString();
        Call call = HttpRequest.getIResourceOA().MemberCenterModifyPassword(ssid,OriginalPassword,secondPassword);
        callRequest(call, new HttpCallBack(MissionEntity.class,this,true) {
            @Override
            public void onSuccess(ResultData mResult) {
                if ("0".equals(mResult.getSuccess())) {//0成功
                    String data = (String) mResult.getData();
                    PreferencesUtils.putString(getApplicationContext(), ModifyPasswordActivity.FLAG_INPUT_PASSWORD,
                            mSecondPassword.getText().toString());
                    MyToast.showToast("密码修改成功");
                } else {
                    Member.clear();
                    String message = mResult.getMsg();
                    MyToast.showToast( message);
                }
            }

            @Override
            public void onFailure(String string) {
                MyToast.showToast( "获取网络数据失败");
            }
        });
    }

}
