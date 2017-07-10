package com.zbxn.crm.activity.custom;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.pub.base.BaseActivity;
import com.pub.dialog.MessageDialog;
import com.pub.http.HttpCallBack;
import com.pub.http.HttpRequest;
import com.pub.http.ResultData;
import com.pub.utils.MyToast;
import com.pub.utils.PreferencesUtils;
import com.pub.widget.MyListView;
import com.pub.widget.PopupWindowHelper;
import com.pub.widget.smarttablayout.SmartTabLayout;
import com.zbxn.R;
import com.zbxn.crm.adapter.PopupListAdapter;
import com.zbxn.crm.entity.CustomEntity;
import com.zbxn.main.activity.login.LoginActivity;
import com.zbxn.main.adapter.FragmentAdapter;
import com.zbxn.main.adapter.PopupwindowMoreAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

/**
 * 客户详情
 *
 * @author: ysj
 * @date: 2017-01-13 10:05
 */
public class CustomDetailsActivity extends BaseActivity {

    private static final int FLAG_FOLLOWUP_CREAT = 10001;

    @BindView(R.id.mSmartTabLayout)
    SmartTabLayout mSmartTabLayout;
    @BindView(R.id.mViewPager)
    ViewPager mViewPager;
    @BindView(R.id.ll_follow_up)
    LinearLayout mFollowUp;
    @BindView(R.id.ll_operation_log)
    LinearLayout mOperationLog;
    @BindView(R.id.ll_more)
    LinearLayout mMore;
    @BindView(R.id.tv_more)
    TextView tvMore;

    private FragmentAdapter mAdapter;
    private String customId;
    private String customName;
    private FollowUpFragment followUpFragment;
    private PopupWindowHelper mHelper;
    private View popView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_details);
        ButterKnife.bind(this);
        setTitle("客户");
        customId = getIntent().getStringExtra("id");
        customName = getIntent().getStringExtra("name");
        initView();
    }

    private void initView() {
        mAdapter = new FragmentAdapter(getSupportFragmentManager());
        Bundle bundle;
        BasicInfoFragment infoFragment = new BasicInfoFragment();
        infoFragment.setmTitle("基本信息");
        followUpFragment = new FollowUpFragment();
        bundle = new Bundle();
        bundle.putString("id", customId);
        bundle.putString("name", customName);
        followUpFragment.setArguments(bundle);
        PreferencesUtils.putString(this, "customID", customId);


        followUpFragment.setmTitle("跟进记录");
        mAdapter.addFragment(infoFragment, followUpFragment);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(mAdapter);
        mSmartTabLayout.setViewPager(mViewPager);
    }

    public void ShowPopupWindow(List<String> list, final AdapterView
            .OnItemClickListener listener) {
        LinearLayout layout = (LinearLayout) LayoutInflater.from(this).inflate(
                R.layout.bottom_popupwindow_more, null);

        ListView listView = (ListView) layout.findViewById(R.id.popupListView);
        int[] locations = new int[2];
        mMore.getLocationOnScreen(locations);
        PopupwindowMoreAdapter adapter = new PopupwindowMoreAdapter(this, list);
        listView.setAdapter(adapter);

        WindowManager manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        int xoff = manager.getDefaultDisplay().getWidth();
        listView.setLayoutParams(new LinearLayout.LayoutParams(xoff - locations[0], ViewGroup.LayoutParams.WRAP_CONTENT));
        PopupWindow m_popupWindow = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int statusBarHeight = -1;
            //获取status_bar_height资源的ID
            int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                //根据资源ID获取响应的尺寸值
                statusBarHeight = getResources().getDimensionPixelSize(resourceId);
            }
            m_popupWindow = new PopupWindow(layout, xoff, locations[1] - statusBarHeight);
        } else {
            m_popupWindow = new PopupWindow(layout, xoff, locations[1]);
        }
        m_popupWindow.setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0x33000000);
        m_popupWindow.setBackgroundDrawable(dw);

        m_popupWindow.showAtLocation(mMore, Gravity.TOP, 0, 0);

        final PopupWindow finalM_popupWindow = m_popupWindow;
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalM_popupWindow.dismiss();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                finalM_popupWindow.dismiss();
                listener.onItemClick(parent, view, position, id);
            }
        });

    }

    @OnClick({R.id.ll_follow_up, R.id.ll_operation_log, R.id.ll_more})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_follow_up://添加跟进记录
                Intent intent = new Intent(this, CreatFollowUpActivity.class);
                intent.putExtra("id", customId);
                startActivityForResult(intent, FLAG_FOLLOWUP_CREAT);
                break;
            case R.id.ll_operation_log://操作记录
                Intent logIntent = new Intent(this, OperationLogActivity.class);
                logIntent.putExtra("id", customId);
                startActivity(logIntent);
                break;
            case R.id.ll_more://更多
                //加载PopupWindow布局
                popView = LayoutInflater.from(this).inflate(R.layout.popupview, null);
                //写在PopupWindowHelper实例化之前，避免出现listview的高度为第一个item的高度的问题
                List<String> list = new ArrayList<>();
                list.add("转移他人");
//                list.add("退回公海");
                list.add("无效");
                PopupListAdapter adapter = new PopupListAdapter(this, list);
                MyListView myListView = (MyListView) popView.findViewById(R.id.mListView);
                myListView.setAdapter(adapter);
                myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        switch (position) {
                            case 0://转移他人
                                Intent intent1 = new Intent(CustomDetailsActivity.this, TransferCustomActivity.class);
                                intent1.putExtra("name", customName);
                                intent1.putExtra("id", customId);
                                startActivity(intent1);
                                //隐藏popupwindow
                                if (mHelper != null) {
                                    mHelper.dismiss();
                                }
                                break;
//                            case 1://退回公海
//                                break;
                            case 1://无效
                                MessageDialog messageDialog = new MessageDialog(CustomDetailsActivity.this);
                                messageDialog.setMessage("确定要把" + customName + "作为无效客户吗？");
                                messageDialog.setNegativeButton("取消");
                                messageDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        postCustomerInvalid();
                                    }
                                });
                                messageDialog.show();
                                if (mHelper != null) {
                                    mHelper.dismiss();
                                }
                                break;
                            default:
                                break;
                        }
                    }
                });
                mHelper = new PopupWindowHelper(this, popView, getWindow());
                mHelper.showAsPopUp(tvMore, -50, 0);
                break;
        }
    }

    /**
     * 标记为无效客户
     */
    public void postCustomerInvalid() {
        String ssid = PreferencesUtils.getString(this, LoginActivity.FLAG_SSID);
        String currentCompanyId = PreferencesUtils.getString(this, LoginActivity.FLAG_ZMSCOMPANYID, "");
        Call call = HttpRequest.getIResourceOANetAction().postCustomerInvalid(ssid, currentCompanyId, customId);
        callRequest(call, new HttpCallBack(CustomEntity.class, this, true) {
            @Override
            public void onSuccess(ResultData mResult) {
                if (mResult.getSuccess().equals("0")) {
                    MyToast.showToast("标记为无效客户成功");
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
