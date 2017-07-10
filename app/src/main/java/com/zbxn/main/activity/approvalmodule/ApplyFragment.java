package com.zbxn.main.activity.approvalmodule;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pub.base.BaseFragment;
import com.pub.dialog.MessageDialog;
import com.pub.http.HttpCallBack;
import com.pub.http.HttpRequest;
import com.pub.http.ResultData;
import com.pub.utils.MyToast;
import com.pub.utils.PreferencesUtils;
import com.pub.widget.pulltorefreshlv.PullRefreshListView;
import com.zbxn.R;
import com.zbxn.main.activity.login.LoginActivity;
import com.zbxn.main.adapter.ApplyAdapter;
import com.zbxn.main.adapter.PopupwindowTypeListAdapter;
import com.zbxn.main.entity.ApplyEntity;
import com.zbxn.main.entity.ApprovalEntity;
import com.zbxn.main.popupwindow.PopupWindowType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

/**
 * @author: ysj
 * @date: 2016-11-04 09:40
 */
public class ApplyFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    private static final int Type_Apply = 101;      // 我的申请
    private static final int Type_Approval = 102;   // 我的审批
    private static final int Type_Inquire = 103;    // 审批查询

    /**
     * 回调处理
     */
    private static final int Flag_Callback_Record = 1001;

    @BindView(R.id.mWhole)
    TextView mWhole;
    @BindView(R.id.layout_whole)
    RelativeLayout layoutWhole;
    @BindView(R.id.mType)
    TextView mType;
    @BindView(R.id.layout_type)
    RelativeLayout layoutType;
    @BindView(R.id.mListView)
    PullRefreshListView mListView;

    //一页加载10条
    private int pageSize = 10;
    private List<ApplyEntity> mList;
    private ApplyAdapter mAdapter;

    //第几页
    private int mPage = 1;
    //审批状态
    private String mStateStr = "0";
    //申请类型
    private String mTypeStr = "-1";

    //根据 type 判断当前页面类型
    private int type = 101;
    private boolean isVis = false;

    private MessageDialog messageDialog;

    private List<ApprovalEntity> listStateLeft = new ArrayList<>();
    private List<ApprovalEntity> listTypeRight = new ArrayList<>();

    public void setFragmentTitle(String title) {
        mTitle = title;
    }

    @Override
    protected View onCreateView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_apply, container, false);
        ButterKnife.bind(this, view);
        type = getArguments().getInt("types");


        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            isVis = true;
        } else {
            isVis = false;
        }
    }

    @Override
    protected void initialize(View root, @Nullable Bundle savedInstanceState) {
        mListView.startFirst();
        messageDialog = new MessageDialog(getContext());
        getSelectState();
        getSelectType();
        mList = new ArrayList<>();
        mAdapter = new ApplyAdapter(getContext(), mList, type + "");
        mListView.setAdapter(mAdapter);
        mListView.setOnPullListener(new PullRefreshListView.OnPullListener() {
            @Override
            public void onRefresh() {
                setRefresh();
            }

            @Override
            public void onLoad() {
                getListData(type);
            }
        });
        //刷新
        setRefresh();

        mListView.setOnItemClickListener(this);
    }

    //列表的点击事件
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        view.findViewById(R.id.mTitle);//当前状态
        Intent intent = new Intent(getContext(), ApplyDetailActivity.class);
        if (type == Type_Apply) {
            intent.putExtra("flag", 1000);
        } else if (type == Type_Approval) {

        } else if (type == Type_Inquire) {
            intent.putExtra("flag", 1001);
        }
        intent.putExtra("approvalID", mList.get(position).getID() + "");
        startActivityForResult(intent, Flag_Callback_Record);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Flag_Callback_Record) {
            if (resultCode == getActivity().RESULT_OK) {
                setRefresh();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onResume() {
        super.onResume();
        isVis = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        isVis = false;
    }

    /**
     * 刷新
     */
    public void setRefresh() {
        mPage = 1;
        getListData(type);
    }

    //请求数据
    public void getListData(int types) {
        getApplyList(types);
    }

    @OnClick({R.id.layout_whole, R.id.layout_type})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_whole:
                setPopupWindow(mWhole, mWhole.getWidth(), listStateLeft, "全部", 0, mWhole, 1);
                break;
            case R.id.layout_type:
                setPopupWindow(mType, mType.getWidth(), listTypeRight, "全部", 1, mWhole, 2);
                break;
        }
    }

    //类型item的点击事件
    private AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (id == 0) {
                ApprovalEntity entity = listStateLeft.get(position);
                mWhole.setText(entity.getName());
                if (entity.getTypeid() == -1) {
                    mStateStr = "-1";
                } else {
                    mStateStr = entity.getTypeid() + "";
                }
                if (type == Type_Apply) {
                    PopupwindowTypeListAdapter.savePosionLeft = position;
                } else if (type == Type_Approval) {
                    PopupwindowTypeListAdapter.savePosionLeft2 = position;
                } else if (type == Type_Inquire) {
                    PopupwindowTypeListAdapter.savePosionLeft3 = position;
                }

                mListView.startFirst();
                setRefresh();
            } else if (id == 1) {
                ApprovalEntity entity = listTypeRight.get(position);
                mType.setText(entity.getName());
                if (entity.getTypeid() == -1) {
                    mType.setText("类型");
                }
                if (type == Type_Apply) {
                    PopupwindowTypeListAdapter.savePosionRight = position;
                } else if (type == Type_Approval) {
                    PopupwindowTypeListAdapter.savePosionRight2 = position;
                } else if (type == Type_Inquire) {
                    PopupwindowTypeListAdapter.savePosionRight3 = position;
                }

                mTypeStr = entity.getTypeid() + "";
                mListView.startFirst();
                setRefresh();
            }
        }
    };

    //设置PopupWindow
    public void setPopupWindow(View view, float width, List<ApprovalEntity> list, String type, long flag, View location, int types) {
        PopupWindowType mpopupWindowMeetingType = new PopupWindowType(getActivity(), view, width, listener, list, type, flag, types);
        ColorDrawable dw1 = new ColorDrawable(0xffffffff);
        //设置SelectPicPopupWindow弹出窗体的背景
        mpopupWindowMeetingType.setBackgroundDrawable(dw1);
        mpopupWindowMeetingType.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                mWhole.setTextColor(R.color.app_primary_text);
                mType.setTextColor(R.color.app_primary_text);
            }
        });
        //设置layout在PopupWindow中显示的位置
        mpopupWindowMeetingType.showAsDropDown(location, 1, 1);
        if (types == 1) {
            mWhole.setTextColor(getResources().getColor(R.color.app_listview_slidemenu_1));
        } else if (types == 2) {
            mType.setTextColor(getResources().getColor(R.color.app_listview_slidemenu_1));
        }
    }

    /**
     * 获取列表
     */
    public void getApplyList(int types) {
        String ssid = PreferencesUtils.getString(getContext(), LoginActivity.FLAG_SSID);
        Call call = null;
        switch (types) {
            case Type_Apply: // 我的申请
                call = HttpRequest.getIResourceOA().getApplyForList(ssid, mPage, mStateStr, mTypeStr);
                break;
            case Type_Approval:// 我的审批
                call = HttpRequest.getIResourceOA().getMyApprovalList(ssid, mPage, mStateStr, mTypeStr);
                break;
            case Type_Inquire: // 审批查询
                call = HttpRequest.getIResourceOA().getInquireList(ssid, mPage, mStateStr, mTypeStr);
                break;
        }
        if (call == null) {
            return;
        }
        callRequest(call, new HttpCallBack(ApplyEntity.class, getContext(), false) {
            @Override
            public void onSuccess(ResultData mResult) {
                if (mResult.getSuccess().equals("0")) {
                    List<ApplyEntity> list = mResult.getRows();
                    if (mPage == 1) {
                        mList.clear();
                    }
                    mList.addAll(list);
                    mAdapter.notifyDataSetChanged();
                    mPage++;
                    setMore(list);
                    mListView.onRefreshFinish();
                } else {
                    Toast.makeText(getContext(), mResult.getMsg(), Toast.LENGTH_SHORT).show();
                    MyToast.showToast(mResult.getMsg());
                    mListView.onRefreshFinish();
                }
            }

            @Override
            public void onFailure(String string) {
                MyToast.showToast(R.string.NETWORKERROR);
                mListView.onRefreshFinish();
            }
        });
    }

    /**
     * 获取审批状态
     */
    public void getSelectState() {
        String ssid = PreferencesUtils.getString(getContext(), LoginActivity.FLAG_SSID);
        Call call = HttpRequest.getIResourceOA().getSelectFrame(ssid);
        callRequest(call, new HttpCallBack(ApprovalEntity.class, getContext(), false) {
            @Override
            public void onSuccess(ResultData mResult) {
                if (mResult.getSuccess().equals("0")) {
                    listStateLeft = mResult.getRows();
                }
            }

            @Override
            public void onFailure(String string) {
                MyToast.showToast(R.string.NETWORKERROR);
            }
        });
    }

    /**
     * 获取申请类型
     */
    public void getSelectType() {
        Object o;
        String ssid = PreferencesUtils.getString(getContext(), LoginActivity.FLAG_SSID);
        Call call = HttpRequest.getIResourceOA().getSelectType(ssid);
        callRequest(call, new HttpCallBack(ApprovalEntity.class, getContext(), false) {
            @Override
            public void onSuccess(ResultData mResult) {
                if (mResult.getSuccess().equals("0")) {
                    listTypeRight = mResult.getRows();
                }
            }

            @Override
            public void onFailure(String string) {
                MyToast.showToast(R.string.NETWORKERROR);
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
            mListView.setHasMoreData(true);
            return;
        }
        int pageTotal = mResult.size();
        if (pageTotal >= pageSize) {
            mListView.setHasMoreData(true);
            mListView.setPullLoadEnabled(true);
        } else {
            mListView.setHasMoreData(false);
            mListView.setPullLoadEnabled(false);
        }
    }

}
