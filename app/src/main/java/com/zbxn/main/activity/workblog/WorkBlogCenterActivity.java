package com.zbxn.main.activity.workblog;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;

import com.pub.base.BaseActivity;
import com.pub.common.EventCustom;
import com.pub.common.KeyEvent;
import com.pub.common.ToolbarParams;
import com.pub.dialog.MessageDialog;
import com.pub.http.HttpCallBack;
import com.pub.http.HttpRequest;
import com.pub.http.ResultData;
import com.pub.utils.MyToast;
import com.pub.utils.PreferencesUtils;
import com.pub.widget.ProgressLayout;
import com.pub.widget.pulltorefreshlv.PullRefreshListView;
import com.zbxn.R;
import com.zbxn.main.activity.contacts.ContactsDepartmentFragment;
import com.zbxn.main.activity.login.LoginActivity;
import com.zbxn.main.activity.schedule.ScheduleActivity;
import com.zbxn.main.adapter.WorkBlogAdapter;
import com.zbxn.main.entity.Contacts;
import com.zbxn.main.entity.Member;
import com.zbxn.main.entity.WorkBlog;
import com.zbxn.main.entity.WorkmateEntity;

import org.simple.eventbus.Subscriber;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;

/**
 * 日志中心
 */
public class WorkBlogCenterActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    /**
     * 回调_创建日志
     */
    private static final int Flag_Callback_CreateWorkBlog = 1001;
    private static final int Flag_Callback_ColleagueWorkBlog = 1002;

    @BindView(R.id.mDrawerLayout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.listView_workBlog)
    PullRefreshListView listViewWorkBlog;
    @BindView(R.id.layout_progress)
    ProgressLayout layoutProgress;
    @BindView(R.id.mContactsFragment)
    FrameLayout mContactsFragments;

    private ActionBarDrawerToggle mDrawerToggle;
    private WorkBlogAdapter mAdapter;
    private List<WorkBlog> mList;

    private int mPage = 1;
    private int mRows = 10;
    private WorkBlog mWorkBlog;
    private MessageDialog mMessageDialog;

    private ContactsDepartmentFragment mContactsFragment;
    private FragmentManager manager;
    private FragmentTransaction transaction;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workblog_center);
        ButterKnife.bind(this);
        initData();
        init();
    }

    /**
     * 初始化列表数据
     */
    private void initData() {
        userId = PreferencesUtils.getString(this, LoginActivity.FLAG_INPUT_ID);
        mList = new ArrayList<>();
        mAdapter = new WorkBlogAdapter(this, mList);
        listViewWorkBlog.setAdapter(mAdapter);
        listViewWorkBlog.setOnPullListener(new PullRefreshListView.OnPullListener() {
            @Override
            public void onRefresh() {
                refreshUI();
            }

            @Override
            public void onLoad() {
                getWorkBlogList(getApplicationContext());
            }
        });
        listViewWorkBlog.setOnItemClickListener(this);
        listViewWorkBlog.startFirst();
        refreshUI();
    }

    public void refreshUI() {
        mPage = 1;
        getIsBlogToday();
        getWorkBlogList(this);
    }

    @Override
    public void initRight() {
        super.initRight();
        setTitle("我的日志");
        setRight1("同事");
        setRight1Icon(R.mipmap.menu_blog_switch2);
        setRight2("创建");
        setRight2Icon(R.mipmap.menu_creat_blog);
        setRight1Show(true);
        setRight2Show(true);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    //同事
    @Override
    public void actionRight1(MenuItem menuItem) {
        super.actionRight1(menuItem);
        Intent intent = new Intent(this, ColleagueWorkBlogActivity.class);
        startActivityForResult(intent, Flag_Callback_ColleagueWorkBlog);
    }

    //创建
    @Override
    public void actionRight2(MenuItem menuItem) {
        super.actionRight2(menuItem);
        switch (Member.get().getBlogStateToday()) {
            case -1://未查询
                openCreateWorkBlog(null);
                break;
            case 0://未写
                openCreateWorkBlog(null);
                break;
            case 1://已写
                mMessageDialog = new MessageDialog(this);
                mMessageDialog.setTitle("提示");
                mMessageDialog.setMessage(getResources().getString(
                        R.string.app_main_blogcenter_message));
                mMessageDialog.setNegativeButton("取消");
                mMessageDialog.setPositiveButton("编辑",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                openCreateWorkBlog(mWorkBlog);
                            }
                        });
                mMessageDialog.show();
                break;
        }
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.blog_switch: //切换他人日志
////                if (mDrawerLayout.isDrawerOpen(mContactsFragments)) {
////                    mDrawerLayout.closeDrawer(mContactsFragments);
////                } else {
////                    mDrawerLayout.openDrawer(mContactsFragments);
////                }
//                Intent intent = new Intent(this, ColleagueWorkBlogActivity.class);
//                startActivityForResult(intent,Flag_Callback_ColleagueWorkBlog);
//                break;
//            case R.id.blog_creat: //创建日志
//                switch (Member.get().getBlogStateToday()) {
//                    case -1://未查询
//                        openCreateWorkBlog(null);
//                        break;
//                    case 0://未写
//                        openCreateWorkBlog(null);
//                        break;
//                    case 1://已写
//                        mMessageDialog = new MessageDialog(this);
//                        mMessageDialog.setTitle("提示");
//                        mMessageDialog.setMessage(getResources().getString(
//                                R.string.app_main_blogcenter_message));
//                        mMessageDialog.setNegativeButton("取消");
//                        mMessageDialog.setPositiveButton("编辑",
//                                new DialogInterface.OnClickListener() {
//
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        openCreateWorkBlog(mWorkBlog);
//                                    }
//
//                                });
//                        mMessageDialog.show();
//                        break;
//                }
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    // 打开创建日志页面
    private void openCreateWorkBlog(WorkBlog workBlog) {
        Intent intent = new Intent(this, CreatWorkBlogActivity.class);
        // 将当天日志传入
        if (workBlog != null) {
            intent.putExtra(CreatWorkBlogActivity.Flag_Input_Blog, workBlog);
        }
        startActivityForResult(intent, Flag_Callback_CreateWorkBlog);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Flag_Callback_CreateWorkBlog:
                // 保存/更新日志成功
                if (resultCode == Activity.RESULT_OK) {
                    refreshUI();
                }
                break;
            case Flag_Callback_ColleagueWorkBlog:
                // 保存/更新日志成功
                if (resultCode == Activity.RESULT_OK) {
                    WorkmateEntity entity = (WorkmateEntity) data.getSerializableExtra("entity");
                    userId = entity.getUserId();
                    if (userId.equals(PreferencesUtils.getString(this, LoginActivity.FLAG_INPUT_ID))) {
                        setTitle("我的日志");
                        setRight2Show(true);
                    } else {
                        setTitle(entity.getUserName() + "的日志");
                        setRight2Show(false);
                    }
                    listViewWorkBlog.startFirst();
                    refreshUI();
                }
                break;


            default:
                break;
        }
    }

    //初始化侧滑
    private void init() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.app_name, R.string.app_name);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        manager = getSupportFragmentManager();
        mContactsFragment = new ContactsDepartmentFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", 3);
        mContactsFragment.setArguments(bundle);
        transaction = manager.beginTransaction();
        transaction.replace(R.id.mContactsFragment, mContactsFragment);
        transaction.commit();
    }

    @Subscriber
    public void onEventMainThread(EventCustom eventCustom) {
        if (eventCustom.getTag().equals(KeyEvent.UPDATEWORKBLOG)) {
            getWorkBlogList(getApplicationContext());
            mAdapter.notifyDataSetChanged();

            listViewWorkBlog.startFirst();
            refreshUI();

        }



        Contacts contacts = (Contacts) eventCustom.getObj();
        userId = contacts.getId() + "";
        if (userId.equals(PreferencesUtils.getString(this, LoginActivity.FLAG_INPUT_ID))) {
            setTitle("我的日志");
        } else {
            setTitle(contacts.getUserName() + "的日志");
        }
        mDrawerLayout.closeDrawer(mContactsFragments);
        listViewWorkBlog.startFirst();
        refreshUI();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, WorkBlogDetailActivity.class);
        intent.putExtra("id", mList.get(position).getId());
        startActivity(intent);
    }

    /**
     * 获取日志列表
     *
     * @param mContext
     */
    public void getWorkBlogList(Context mContext) {
        String ssid = PreferencesUtils.getString(this, LoginActivity.FLAG_SSID);
        Call call = HttpRequest.getIResourceOA().getWorkBlogCenter(ssid, userId, mPage, mRows);
        callRequest(call, new HttpCallBack(WorkBlog.class, mContext, false) {
            @Override
            public void onSuccess(ResultData mResult) {
                if (mResult.getSuccess().equals("0")) {
                    layoutProgress.showContent();
                    List<WorkBlog> list = mResult.getRows();
                    if (mPage == 1) {
                        mList.clear();
                    }
                    setMore(list);
                    mPage++;
                    mList.addAll(list);
                    mAdapter.notifyDataSetChanged();
                    listViewWorkBlog.onRefreshFinish();
                } else {
                    MyToast.showToast(mResult.getMsg());
                    listViewWorkBlog.onRefreshFinish();
                    layoutProgress.showError(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            refreshUI();
                        }
                    });
                }
            }

            @Override
            public void onFailure(String string) {
                MyToast.showToast(R.string.NETWORKERROR);
                listViewWorkBlog.onRefreshFinish();
                layoutProgress.showError(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        refreshUI();
                    }
                });
            }
        });
    }

    /**
     * 检查用户当天是否写了日志
     */
    public void getIsBlogToday() {
        String ssid = PreferencesUtils.getString(this, LoginActivity.FLAG_SSID);
        Call call = HttpRequest.getIResourceOA().getIsBlogToday(ssid);
        callRequest(call, new HttpCallBack(WorkBlog.class, this, false) {
            @Override
            public void onSuccess(ResultData mResult) {
                mWorkBlog = (WorkBlog) mResult.getData();
                Member.get().setBlogStateToday(mWorkBlog == null ? 0 : 1);
                if (mWorkBlog != null) {
                    PreferencesUtils.putString(getBaseContext(), "blog", mWorkBlog.getWorkblogcontent());
                }
            }

            @Override
            public void onFailure(String string) {
                Member.get().setBlogStateToday(mWorkBlog == null ? 0 : 1);
            }
        });
    }

    /**
     * 显示加载更多
     *
     * @param mResult
     */
    private void setMore(List mResult) {
        if (mResult == null) {
            listViewWorkBlog.setHasMoreData(true);
            return;
        }
        int pageTotal = mResult.size();
        if (pageTotal >= mRows) {
            listViewWorkBlog.setHasMoreData(true);
            listViewWorkBlog.setPullLoadEnabled(true);
        } else {
            listViewWorkBlog.setHasMoreData(false);
            listViewWorkBlog.setPullLoadEnabled(false);
        }
    }


}
