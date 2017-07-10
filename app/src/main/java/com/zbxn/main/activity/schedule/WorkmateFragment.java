package com.zbxn.main.activity.schedule;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.pub.base.BaseFragment;
import com.pub.common.EventCustom;
import com.pub.http.HttpCallBack;
import com.pub.http.HttpRequest;
import com.pub.http.ResultData;
import com.pub.utils.MyToast;
import com.pub.utils.PreferencesUtils;
import com.pub.widget.pulltorefreshlv.PullRefreshListView;
import com.zbxn.R;
import com.zbxn.main.activity.jobManagement.CreateJobActivity;
import com.zbxn.main.activity.login.LoginActivity;
import com.zbxn.main.adapter.WorkmateAdapter;
import com.zbxn.main.entity.Contacts;
import com.zbxn.main.entity.PermissionIDEntity;
import com.zbxn.main.entity.WorkmateEntity;

import org.simple.eventbus.EventBus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;

import static com.zbxn.main.activity.jobManagement.CreateJobActivity.mList;

/**
 * Created by Administrator on 2017/2/10.
 */
public class WorkmateFragment extends BaseFragment {

    private WorkmateListFragment fragment;
    private FragmentTransaction ft;

    @Override
    protected View onCreateView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_workmate, null);
        ButterKnife.bind(this, view);
        getScheduleUserList();
        FragmentManager manager = getFragmentManager();
        ft = manager.beginTransaction();
        fragment = new WorkmateListFragment();
        return view;
    }

    //获取有权限的同事列表
    private void getScheduleUserList() {
        String ssid = PreferencesUtils.getString(getContext(), LoginActivity.FLAG_SSID);
        String currentCompanyId = PreferencesUtils.getString(getContext(), LoginActivity.FLAG_ZMSCOMPANYID);
        String UserID = PreferencesUtils.getString(getContext(), LoginActivity.FLAG_INPUT_ID);
        Call call = HttpRequest.getIResourceOANetAction().getScheduleUserList(ssid, currentCompanyId, UserID);
        callRequest(call, new HttpCallBack(WorkmateEntity.class, getContext(), true) {

            @Override
            public void onSuccess(ResultData mResult) {
                if ("0".equals(mResult.getSuccess())) {
                    ScheduleActivity.list = mResult.getRows();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("mList", (Serializable) ScheduleActivity.list);
                    fragment.setArguments(bundle);
                    ft.add(R.id.mShortCutContainer, fragment);
                    ft.commit();
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

    @Override
    protected void initialize(View root, @Nullable Bundle savedInstanceState) {

    }

}
