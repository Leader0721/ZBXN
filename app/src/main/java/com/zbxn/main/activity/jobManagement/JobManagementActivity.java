package com.zbxn.main.activity.jobManagement;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.pub.base.BaseActivity;
import com.pub.common.EventCustom;
import com.pub.dialog.MessageDialog;
import com.pub.http.HttpCallBack;
import com.pub.http.HttpRequest;
import com.pub.http.ResultData;
import com.pub.utils.MyToast;
import com.pub.utils.PreferencesUtils;
import com.pub.utils.StringUtils;
import com.pub.utils.Utils;
import com.pub.widget.PullToRefreshSwipeListView;
import com.pub.widget.swiplistview.BaseSwipeListViewListener;
import com.pub.widget.swiplistview.SwipeListView;
import com.zbxn.R;
import com.zbxn.main.activity.login.LoginActivity;
import com.zbxn.main.activity.org.AddDepartmentActivity;
import com.zbxn.main.adapter.ManagerJobAdapter;
import com.zbxn.main.entity.JobEntity;
import com.zbxn.main.listener.ICustomListener;

import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;


public class JobManagementActivity extends BaseActivity {

    @BindView(R.id.m_listview)
    PullToRefreshSwipeListView mListview;

    private List<JobEntity> mListTemp;
    private ArrayList<JobEntity> mList;
    private ManagerJobAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_management);
        ButterKnife.bind(this);
        initView();
        loadData();

    }

    private void initView() {
        mList = new ArrayList<>();
        mAdapter = new ManagerJobAdapter(getApplicationContext(), mList, mListener);
        mListview.setAdapter(mAdapter);
        mListview.addHeaderView(createView());
        mListview.startFirst();
        mListview.setPullRefreshEnable(false);
        mListview.setPullLoadEnabled(false);//禁用上拉加载更多
        mListview.getListView().setSwipeMode(SwipeListView.SWIPE_MODE_LEFT);//左滑出现
        mListview.getListView().setOffsetLeft(Utils.getScreenWidth(this) - Utils.Dp2Px(this, 100));//左滑宽度
        mListview.getListView().setSwipeCloseAllItemsWhenMoveList(true);//当滑动列表时关闭所有
        mListview.setOnPullListener(new PullToRefreshSwipeListView.OnPullListener() {
            @Override
            public void onRefresh() {
                loadData();
            }

            @Override
            public void onLoad() {

            }
        });
        mListview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                mListview.getListView().closeOpenedItems();
                return true;
            }
        });
        mListview.setSwipeListViewListener(new BaseSwipeListViewListener() {
            @Override
            public void onClickFrontView(int position) {
                //相当于普通ListView的OnItemClickListener事件
                super.onClickFrontView(position);

                JobEntity entity = mList.get(position - 1);//由于添加headerView，所以-1

                Intent intent = new Intent(JobManagementActivity.this, OfficerActivity.class);
                int positionID = entity.getPositionID();
                intent.putExtra("id", positionID);
                String positionName = entity.getPositionName();
                intent.putExtra("name", positionName);
                String desc = entity.getPositionDesc();
                intent.putExtra("Desc", desc);
                startActivity(intent);
            }

            @Override
            public void onStartOpen(int position, int action, boolean right) {
                mListview.getListView().closeOpenedItems();
            }
        });
    }

    private void initData() {

    }

    private void loadData() {
        String ssid = PreferencesUtils.getString(this, LoginActivity.FLAG_SSID);
        String currentCompanyId = PreferencesUtils.getString(this, LoginActivity.FLAG_ZMSCOMPANYID);

        Call call = HttpRequest.getIResourceOANetAction().getChooseJob(ssid, currentCompanyId);
        callRequest(call, new HttpCallBack(JobEntity.class, JobManagementActivity.this, false) {
            @Override
            public void onSuccess(ResultData mResult) {
                if ("0".equals(mResult.getSuccess())) {
                    mListTemp = mResult.getRows();
                    mList.clear();
                    mList.addAll(mListTemp);
                    mAdapter.notifyDataSetChanged();
                    mListview.onRefreshFinish();
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

    /**
     * 设置listview 头部
     */
    private View createView() {
        View mContainer = LayoutInflater.from(this).inflate(R.layout.view_search, null);

        final EditText etSearch = (EditText) mContainer.findViewById(R.id.et_search);
        etSearch.setFocusableInTouchMode(false);

        etSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JobManagementActivity.this, JobSearchActivity.class);
                String keyWord = StringUtils.getEditText(etSearch);
                intent.putExtra("keyWord", keyWord);
                Bundle bundle = new Bundle();
                bundle.putSerializable("list", mList);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });

        return mContainer;
    }

    @Override
    public void initRight() {
        setTitle("职位管理");
        setRight1Icon(R.mipmap.menu_creat_blog);
        setRight1Show(true);
    }

    //    菜单按钮的点击事件
    @Override
    public void actionRight1(MenuItem menuItem) {
        Intent intent = new Intent(this, CreateJobActivity.class);
        intent.putExtra("PostionID", 0);
        startActivityForResult(intent, 1000);
    }

    private ICustomListener mListener = new ICustomListener() {
        @Override
        public void onCustomListener(int obj0, Object obj1, final int position) {
            JobEntity entity = (JobEntity) obj1;
            switch (obj0) {
                case 0:
                    MessageDialog dialog = new MessageDialog(JobManagementActivity.this);
                    dialog.setMessage("确认删除职位吗？");
                    dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    dialog.setPositiveButton("确定",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deletePosition(position);
                        dialog.dismiss();
                    }
                });

                    dialog.show();
                    break;
                default:
                    break;
            }
        }
    };


    //    删除部门
    private void deletePosition(final int position) {

        String ssid = PreferencesUtils.getString(this, LoginActivity.FLAG_SSID);
        String currentCompanyId = PreferencesUtils.getString(this, LoginActivity.FLAG_ZMSCOMPANYID);

        Call call = HttpRequest.getIResourceOANetAction().getdeletePosition(ssid, currentCompanyId, mList.get(position).getPositionID());
        callRequest(call, new HttpCallBack(JobEntity.class, JobManagementActivity.this, true) {
            @Override
            public void onSuccess(ResultData mResult) {
                if ("0".equals(mResult.getSuccess())) {
                    //更新UI
                    mList.remove(position);
                    mAdapter.notifyDataSetChanged();
                    mListview.onRefreshFinish();
                    MyToast.showToast("删除成功");
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

    @Subscriber
    public void onEventMainThread(EventCustom eventCustom) {
        if (CreateJobActivity.SUCCESS.equals(eventCustom.getTag())){
            loadData();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        //新增职位之后刷新界面
        if (requestCode == 1000) {
            loadData();
        }
    }
}
