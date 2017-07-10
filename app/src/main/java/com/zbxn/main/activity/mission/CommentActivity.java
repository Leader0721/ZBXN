package com.zbxn.main.activity.mission;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.pub.base.BaseActivity;
import com.pub.http.HttpCallBack;
import com.pub.http.HttpRequest;
import com.pub.http.ResultData;
import com.pub.utils.MyToast;
import com.pub.utils.PreferencesUtils;
import com.pub.utils.StringUtils;
import com.zbxn.R;
import com.zbxn.main.activity.login.LoginActivity;
import com.zbxn.main.entity.MissionEntity;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;

/**
 * 项目名称：发表评论
 * 创建人：LiangHanXin
 * 创建时间：2016/11/14 13:46
 */
public class CommentActivity extends BaseActivity {
    @BindView(R.id.mComment)
    EditText mComment;

    private String TaskId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        ButterKnife.bind(this);
        setTitle("评论");


        TaskId = getIntent().getStringExtra("TaskId");

        initDate();

    }

    private void initDate() {
    }

    @Override
    public void initRight() {
        super.initRight();
        setRight1("完成");
        setRight1Icon(R.mipmap.complete1);
        setRight1Show(true);
    }

    @Override
    public void actionRight1(MenuItem menuItem) {
        super.actionRight1(menuItem);
        if (StringUtils.isEmpty(mComment)) {
            MyToast.showToast("请输入内容");
        } else {
            comment(TaskId, StringUtils.getEditText(mComment));
        }
    }

    /**
     * 发表评论
     *
     * @param taskIds
     * @param taskContent
     */
    public void comment(String taskIds, String taskContent) {
        String ssid = PreferencesUtils.getString(this, LoginActivity.FLAG_SSID);
        String currentCompanyId = PreferencesUtils.getString(this, LoginActivity.FLAG_ZMSCOMPANYID);
        Call call = HttpRequest.getIResourceOANetAction().postComment(ssid, currentCompanyId, taskIds, taskContent);
        callRequest(call, new HttpCallBack(MissionEntity.class, this, true) {
            @Override
            public void onSuccess(ResultData mResult) {
                if ("0".equals(mResult.getSuccess())) {//0成功
                    MyToast.showToast("评论成功");
                    setResult(RESULT_OK, getIntent());
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
