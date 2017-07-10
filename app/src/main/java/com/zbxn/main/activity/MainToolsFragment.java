package com.zbxn.main.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.pub.base.BaseFragment;
import com.pub.utils.JsonUtil;
import com.pub.utils.MyToast;
import com.pub.utils.ScreenUtils;
import com.pub.utils.StringUtils;
import com.pub.widget.MyGridViewLine;
import com.zbxn.R;
import com.zbxn.crm.activity.custom.CustomActivity;
import com.zbxn.main.activity.approvalmodule.ApplyActivity;
import com.zbxn.main.activity.attendance.AttendanceActivity;
import com.zbxn.main.activity.mission.MissionActivity;
import com.zbxn.main.activity.okr.OkrRankingActivity;
import com.zbxn.main.activity.schedule.ScheduleActivity;
import com.zbxn.main.activity.tools.TrailerActivity;
import com.zbxn.main.activity.workblog.WorkBlogCenterActivity;
import com.zbxn.main.adapter.ToolsAdapter;
import com.zbxn.main.entity.RecTool;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 主页-应用界面
 * Created by Administrator on 2016/12/26.
 */
public class MainToolsFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    @BindView(R.id.mGridView)
    MyGridViewLine mGridView;
    @BindView(R.id.mGridView_crm)
    MyGridViewLine mCrmGridView;

    //OA
    private ToolsAdapter mToolsAdapter;
    private List<RecTool> listTemp = new ArrayList<>();
//    private String oaToolsList = "[{\"id\":1,\"title\":\"日志\",\"menuid\":112,\"img\":\"temp112\",\"isvisible\":true}" +
//            ",{\"id\":2,\"title\":\"考勤\",\"menuid\":117,\"img\":\"temp117\",\"isvisible\":true}" +
//            ",{\"id\":7,\"title\":\"日程\",\"menuid\":116,\"img\":\"temp116\",\"isvisible\":true}" +
//            ",{\"id\":10,\"title\":\"审批\",\"menuid\":120,\"img\":\"temp120\",\"isvisible\":true}" +
//            ",{\"id\":3,\"title\":\"OKR\",\"menuid\":113,\"img\":\"temp113\",\"isvisible\":true}" +
//            ",{\"id\":9,\"title\":\"任务\",\"menuid\":119,\"img\":\"temp119\",\"isvisible\":true}" +
//            ",{\"id\":16,\"title\":\"预告\",\"menuid\":126,\"img\":\"temp126\",\"isvisible\":true}" +
//            ",{\"id\":1622,\"title\":\"\",\"menuid\":12633,\"img\":\"\",\"isvisible\":true}]";

    //CRM
    private ToolsAdapter mCrmToolsAdapter;
    private List<RecTool> crmListTemp = new ArrayList<>();
    private String crmToolsList = "[{\"id\":1,\"title\":\"客户\",\"menuid\":201,\"img\":\"custom\",\"isvisible\":true}" +
            ",{\"id\":2,\"title\":\"联系人\",\"menuid\":202,\"img\":\"contacts\",\"isvisible\":true}" +
            ",{\"id\":3,\"title\":\"销售流\",\"menuid\":203,\"img\":\"sales_leads\",\"isvisible\":true}" +
            ",{\"id\":4,\"title\":\"合同管理\",\"menuid\":204,\"img\":\"contract\",\"isvisible\":true}]";

    private String oaToolsList = "[{\"id\":1,\"title\":\"日志\",\"menuid\":112,\"img\":\"temp112\",\"isvisible\":true}" +
            ",{\"id\":2,\"title\":\"考勤\",\"menuid\":117,\"img\":\"temp117\",\"isvisible\":true}" +
            ",{\"id\":7,\"title\":\"日程\",\"menuid\":116,\"img\":\"temp116\",\"isvisible\":true}" +
            ",{\"id\":10,\"title\":\"审批\",\"menuid\":120,\"img\":\"temp120\",\"isvisible\":true}" +
            ",{\"id\":3,\"title\":\"OKR\",\"menuid\":113,\"img\":\"temp113\",\"isvisible\":true}" +
            ",{\"id\":9,\"title\":\"任务\",\"menuid\":119,\"img\":\"temp119\",\"isvisible\":true}" +
            ",{\"id\":1,\"title\":\"客户\",\"menuid\":201,\"img\":\"custom\",\"isvisible\":true}" +
            ",{\"id\":2,\"title\":\"联系人\",\"menuid\":202,\"img\":\"contacts\",\"isvisible\":true}" +
            ",{\"id\":3,\"title\":\"销售流\",\"menuid\":203,\"img\":\"sales_leads\",\"isvisible\":true}" +
            ",{\"id\":4,\"title\":\"合同管理\",\"menuid\":204,\"img\":\"contract\",\"isvisible\":true}" +
            ",{\"id\":1622,\"title\":\"\",\"menuid\":12633,\"img\":\"\",\"isvisible\":true}" +
            ",{\"id\":1622,\"title\":\"\",\"menuid\":12633,\"img\":\"\",\"isvisible\":true}]";

    @Override
    protected View onCreateView(LayoutInflater inflater, ViewGroup container) {
        View root = inflater.inflate(R.layout.main_toolscenter, container, false);
        ButterKnife.bind(this, root);
//        initView();
        return root;
    }

    @Override
    protected void initialize(View root, @Nullable Bundle savedInstanceState) {
        try {
            listTemp = JsonUtil.fromJsonList(oaToolsList, RecTool.class);
            crmListTemp = JsonUtil.fromJsonList(crmToolsList, RecTool.class);
        } catch (Exception e) {

        }
        //OA
        mToolsAdapter = new ToolsAdapter(getActivity(), listTemp);
        mGridView.setAdapter(mToolsAdapter);
        mGridView.setOnItemClickListener(this);
        mGridView.setFocusable(false);
        if (!StringUtils.isEmpty(listTemp)) {
            resetData(listTemp);
        }

        //CRM
        mCrmToolsAdapter = new ToolsAdapter(getActivity(), crmListTemp);
        mCrmGridView.setAdapter(mCrmToolsAdapter);
        mCrmGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    String menuId = crmListTemp.get(position).getMenuid();
                    switch (menuId) {
                        case "201"://客户
                            startActivity(new Intent(getContext(), CustomActivity.class));
                            break;
                        case "202"://联系人
                        case "203"://销售流
                        case "204"://合同管理
                            MyToast.showToast("正在开发中，精彩稍后");
                            break;
                        default:
                            break;
                    }
                } catch (Exception e) {

                }
            }
        });
        mCrmGridView.setFocusable(false);
        if (!StringUtils.isEmpty(crmListTemp)) {
            resetCrmData(crmListTemp);
        }

    }

    //OA
    public void resetData(List<RecTool> list) {
        listTemp = list;
        int size = list.size() + 1;
        int line = size / 4;//一共有几行
        if (size % 4 > 0)
            line += 1;

        int height = ScreenUtils.dipToPx(getContext(), line * 96);
        ViewGroup.LayoutParams params = mGridView.getLayoutParams();
        params.height = height;
        mGridView.setLayoutParams(params);
    }

    //CRM
    public void resetCrmData(List<RecTool> list) {
        crmListTemp = list;
        int size = list.size() + 1;
        int line = size / 4;//一共有几行
        if (size % 4 > 0)
            line += 1;

        int height = ScreenUtils.dipToPx(getContext(), line * 96);
        ViewGroup.LayoutParams params = mCrmGridView.getLayoutParams();
        params.height = height;
        mCrmGridView.setLayoutParams(params);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            String menuId = listTemp.get(position).getMenuid();
            switch (menuId) {
                case "112"://日志
                    startActivity(new Intent(getActivity(), WorkBlogCenterActivity.class));
                    break;
                case "113"://OKR
                    startActivity(new Intent(getActivity(), OkrRankingActivity.class));
                    break;
                case "115"://收藏
//                    startActivity(new Intent(getActivity(), CollectCenter.class));
                    break;
                case "116"://日程
                    startActivity(new Intent(getActivity(), ScheduleActivity.class));
                    break;
                case "117"://考勤
                    startActivity(new Intent(getActivity(), AttendanceActivity.class));
                    break;
                case "119"://任务
                    startActivity(new Intent(getActivity(), MissionActivity.class));
                    break;
                case "120"://审批
                    startActivity(new Intent(getActivity(), ApplyActivity.class));
                    break;
                case "126"://预告
                    startActivity(new Intent(getActivity(), TrailerActivity.class));
                    break;
                case "201"://客户
                    startActivity(new Intent(getContext(), CustomActivity.class));
                    break;
                case "202"://联系人
                case "203"://销售流
                case "204"://合同管理
                    MyToast.showToast("正在开发中，精彩稍后");
                    break;
                default:
                    break;
            }
        } catch (Exception e) {

        }
    }
}
