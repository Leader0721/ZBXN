package com.zbxn.main.activity.bulletin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.pub.base.BaseActivity;
import com.pub.http.HttpCallBack;
import com.pub.http.HttpRequest;
import com.pub.http.ResultData;
import com.pub.utils.DateUtils;
import com.pub.utils.MyToast;
import com.pub.utils.PreferencesUtils;
import com.pub.utils.StringUtils;
import com.pub.widget.slidedatetimepicker.NewSlideDateTimeDialogFragment;
import com.pub.widget.slidedatetimepicker.SlideDateTimeListener;
import com.pub.widget.slidedatetimepicker.SlideDateTimePicker;
import com.zbxn.R;
import com.zbxn.main.activity.contacts.ContactsChoseActivity;
import com.zbxn.main.activity.login.LoginActivity;
import com.zbxn.main.entity.Contacts;
import com.zbxn.main.entity.WorkBlog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

/**
 * 发布公告
 *
 * @author: ysj
 * @date: 2016-12-15 09:52
 */
public class CreatBulletinActivity extends BaseActivity {

    /**
     * 选择接收人回调
     */
    private static final int Flag_Callback_ContactsPicker = 1001;

    @BindView(R.id.mReceiveUsers)
    TextView mReceiveUsers;
    @BindView(R.id.mSelectReceiveUser)
    LinearLayout mSelectReceiveUser;
    @BindView(R.id.bulletin_comment_true)
    RadioButton bulletinCommentTrue;
    @BindView(R.id.bulletin_comment_false)
    RadioButton bulletinCommentFalse;
    @BindView(R.id.comment_group)
    RadioGroup commentGroup;
    @BindView(R.id.bulletin_stick_true)
    RadioButton bulletinStickTrue;
    @BindView(R.id.bulletin_stick_false)
    RadioButton bulletinStickFalse;
    @BindView(R.id.bulletin_group)
    RadioGroup mBulletinGroup;
    @BindView(R.id.stick_time)
    TextView stickTime;
    @BindView(R.id.stick_layout)
    LinearLayout mStickLayout;
    @BindView(R.id.mTitle)
    EditText mTitleEdit;
    @BindView(R.id.mContent)
    EditText mContentEdit;
    //选择人员
    private ArrayList<Contacts> mListContacts = new ArrayList<>();
    private String[] mReceiveArray;//接收人Id数组
    private String mTitle;//标题
    private String mContent;//内容
    private int isTop = 0;//是否置顶 1--是 0--否(默认)
    private int isComment = 1;//是否可评论
    private String mDate;//时间
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createbulletin);
        ButterKnife.bind(this);
        setTitle("发布公告");
        mReceiveArray = new String[]{""};
        init();
    }

    @Override
    public void initRight() {
        super.initRight();
        setRight1Icon(0);
        setRight1("完成");
        setRight1Show(true);
    }

    @Override
    public void actionRight1(MenuItem menuItem) {
        super.actionRight1(menuItem);
        if (verifyValue()) {
            postCreatBulletin();
        }
    }

    @OnClick({R.id.mSelectReceiveUser, R.id.stick_time})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mSelectReceiveUser:
                Intent intent = new Intent(this, ContactsChoseActivity.class);
                intent.putExtra("list", mListContacts);
                intent.putExtra("type", 1);
                startActivityForResult(intent, Flag_Callback_ContactsPicker);
                break;
            case R.id.stick_time:
                String time = StringUtils.getEditText(stickTime);
                new SlideDateTimePicker.Builder(getSupportFragmentManager())
                        .setListener(new SlideDateTimeListener() {
                            @Override
                            public void onDateTimeSet(Date date) {
                                stickTime.setText(format.format(date));
                            }
                        })
                        .setInitialDate(StringUtils.convertToDate(format, time))
                        .setIs24HourTime(true)
                        .setIsHaveTime(NewSlideDateTimeDialogFragment.Have_Date)
                        .build()
                        .show();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Flag_Callback_ContactsPicker) {
            if (resultCode == RESULT_OK) {
                mListContacts = (ArrayList<Contacts>) data.getExtras().getSerializable(ContactsChoseActivity.Flag_Output_Checked);
                mReceiveArray = new String[mListContacts.size()];
                String content = "";
                for (int i = 0; i < mListContacts.size(); i++) {
                    mReceiveArray[i] = mListContacts.get(i).getId() + "";
                    content += mListContacts.get(i).getUserName() + ",";
                }
                content = content.substring(0, content.length() - 1);
                mReceiveUsers.setText(content);
            } else {

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private boolean verifyValue() {
        if (StringUtils.isEmpty(mTitleEdit.getText().toString().trim())) {
            MyToast.showToast("标题不能为空");
            return false;
        }
        mTitle = mTitleEdit.getText().toString().trim();
        if (StringUtils.isEmpty(mContentEdit.getText().toString().trim())) {
            MyToast.showToast("内容不能为空");
            return false;
        }
        mContent = mContentEdit.getText().toString().trim();
        if (mReceiveArray == null || mReceiveArray.length == 0) {
            MyToast.showToast("接收人不能为空");
            return false;
        }
        if (isTop == 1) {
            mDate = StringUtils.getEditText(stickTime);
        }
        return true;
    }

    private void init() {
        String date = DateUtils.getDate("yyyy-MM-dd");
        stickTime.setText(date);
        //是否置顶
        mBulletinGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.bulletin_stick_true:
                        isTop = 1;
                        mStickLayout.setVisibility(View.VISIBLE);
                        break;
                    case R.id.bulletin_stick_false:
                        isTop = 0;
                        mStickLayout.setVisibility(View.GONE);
                        break;
                }
            }
        });
        //是否可以评论
        commentGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.bulletin_comment_true:
                        isComment = 1;
                        break;
                    case R.id.bulletin_comment_false:
                        isComment = 0;
                        break;
                }
            }
        });

    }

    /**
     * 发布公告
     */
    public void postCreatBulletin() {
        String ssid = PreferencesUtils.getString(this, LoginActivity.FLAG_SSID);
        Call call = HttpRequest.getIResourceOA().postCreatBulletin(ssid, mTitle, mContent, mReceiveArray, isTop, mDate, isComment);
        callRequest(call, new HttpCallBack(WorkBlog.class, this, true) {
            @Override
            public void onSuccess(ResultData mResult) {
                if (mResult.getSuccess().equals("0")) {
                    Toast.makeText(getBaseContext(), "发布成功", Toast.LENGTH_SHORT).show();
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
