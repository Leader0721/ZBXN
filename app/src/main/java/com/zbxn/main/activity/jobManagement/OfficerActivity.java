package com.zbxn.main.activity.jobManagement;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.pub.base.BaseActivity;
import com.pub.common.EventCustom;
import com.pub.dialog.MessageDialog;
import com.pub.http.HttpCallBack;
import com.pub.http.HttpRequest;
import com.pub.http.ResultData;
import com.pub.utils.MyToast;
import com.pub.utils.PreferencesUtils;
import com.pub.utils.ScreenUtils;
import com.pub.utils.StringUtils;
import com.pub.widget.MyListView;
import com.pub.widget.pulltorefreshlv.PullRefreshListView;
import com.pub.widget.pulltozoomview.PullToZoomScrollViewEx;
import com.zbxn.R;
import com.zbxn.main.activity.contacts.ContactsChoseActivity;
import com.zbxn.main.activity.login.LoginActivity;
import com.zbxn.main.activity.org.ChooseJobActivity;
import com.zbxn.main.adapter.UserListAdapter;
import com.zbxn.main.entity.Contacts;
import com.zbxn.main.entity.JobEntity;
import com.zbxn.main.entity.UserEntity;

import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;

public class OfficerActivity extends BaseActivity {


    @BindView(R.id.mScrollView)
    PullToZoomScrollViewEx mScrollView;
    @BindView(R.id.lv_user)
    MyListView lvUser;
    @BindView(R.id.tv_name)
    TextView tvName;

    private Intent intent;
    private int mPositionID;
    private ArrayList<UserEntity> mList;
    private UserListAdapter mAdapter;
    private ArrayList<Contacts> mListContacts = new ArrayList<>();
    final int FLAG_ADDUSER = 1000;
    final int FLAG_UPDATEJOB = 1001;
    private List<UserEntity> listTemp;
    private String mPositionName;
    private int mUserID;
    private int position;
    private String mDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_officer);
        ButterKnife.bind(this);
        mPositionID = getIntent().getIntExtra("id", 0);

        initLength();
        initView();
        initData();
    }


    //  获取员工列表
    private void initData() {
        String ssid = PreferencesUtils.getString(this, LoginActivity.FLAG_SSID);
        String currentCompanyId = PreferencesUtils.getString(this, LoginActivity.FLAG_ZMSCOMPANYID);
        Call call = HttpRequest.getIResourceOANetAction().getUserList(ssid, currentCompanyId, mPositionID);
        callRequest(call, new HttpCallBack(UserEntity.class, OfficerActivity.this, true) {

            @Override
            public void onSuccess(ResultData mResult) {
                if ("0".equals(mResult.getSuccess())) {
                    listTemp = mResult.getRows();
                    mList.clear();
                    mList.addAll(listTemp);
//                    mAdapter.notifyDataSetChanged();
                    mAdapter = new UserListAdapter(OfficerActivity.this, mList);
                    lvUser.setAdapter(mAdapter);
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

    private void initView() {
        intent = new Intent();
        mPositionID = getIntent().getIntExtra("id", 0);
        mPositionName = getIntent().getStringExtra("name");
        mDesc = getIntent().getStringExtra("Desc");
        String firstName = mPositionName.substring(0, 1);//圆图上的字
        tvName.setText(firstName);
        mList = new ArrayList<>();
        mAdapter = new UserListAdapter(this, mList);
        lvUser.setAdapter(mAdapter);
        //员工列表点击事件
//        lvUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
//                MessageDialog dialog = new MessageDialog(OfficerActivity.this);
//                dialog.setTitle("请选择操作");
//                dialog.setNegativeButton("删除", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
////                        mList.remove(position);
//                        dialog.dismiss();
//                    }
//                });
//                dialog.setPositiveButton("修改职位", new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        intent.setClass(OfficerActivity.this, ChooseJobActivity.class);
//                        mUserID = mList.get(position).getID();
//                        startActivityForResult(intent, FLAG_UPDATEJOB);
//                        dialog.dismiss();
//                    }
//                });
//                dialog.show();
//            }
//        });

    }

    private void initLength() {
        // 调整ZoomView的高度
        int screenWidth = ScreenUtils.getScreenWidth(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                screenWidth, (int) (screenWidth * 9 / 16));
        mScrollView.setHeaderLayoutParams(params);

    }

    @Override
    public void initRight() {
        setRight1("添加人员");
        setRight1Icon(0);
        setRight1Show(true);
        setRight2("编辑职位");
        setRight2Icon(R.mipmap.management_compile);
        setRight2Show(true);

    }

    //添加
    @Override
    public void actionRight1(MenuItem menuItem) {
        intent = new Intent(this, ContactsChoseActivity.class);
        intent.putExtra("type", 1);//0-查看 1-多选 2-单选
        startActivityForResult(intent, FLAG_ADDUSER);
    }

    //编辑
    @Override
    public void actionRight2(MenuItem menuItem) {
        intent = new Intent(this, CreateJobActivity.class);
        intent.putExtra("PostionID", mPositionID);
        intent.putExtra("PositionName", mPositionName);
        intent.putExtra("Desc", mDesc);
        startActivityForResult(intent, 2000);
    }

    @Subscriber
    public void onEventMainThread(EventCustom eventCustom) {
        if (eventCustom.getTag().equals(CreateJobActivity.SUCCESS)) {
            JobEntity entity = (JobEntity) eventCustom.getObj();
            mPositionName=entity.getPositionName();
            mDesc = entity.getPositionDesc();
            String firstName = mPositionName.substring(0, 1);//圆图上的字
            tvName.setText(firstName);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        //新增员工
        String ssid = PreferencesUtils.getString(this, LoginActivity.FLAG_SSID);
        String currentCompanyId = PreferencesUtils.getString(this, LoginActivity.FLAG_ZMSCOMPANYID);
        if (requestCode == FLAG_ADDUSER) {
            mListContacts = (ArrayList<Contacts>) data.getExtras().getSerializable(ContactsChoseActivity.Flag_Output_Checked);
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < mListContacts.size(); i++) {
                sb.append(mListContacts.get(i).getId()).append(",");
            }
            if (sb.length() > 0) {
                sb.deleteCharAt(sb.length() - 1);
            }
            String userList = sb.toString();
            Call call = HttpRequest.getIResourceOANetAction().getaddEmployee(ssid, currentCompanyId, mPositionID, userList);
            callRequest(call, new HttpCallBack(UserEntity.class, OfficerActivity.this, true) {
                @Override
                public void onSuccess(ResultData mResult) {
                    if ("0".equals(mResult.getSuccess())) {

                        for (int i = 0; i < mListContacts.size(); i++) {
                            UserEntity userEntity = new UserEntity();
                            userEntity.setUsername(mListContacts.get(i).getUserName());
                            userEntity.setID(mListContacts.get(i).getId());
                            userEntity.setPhoto(mListContacts.get(i).getPhoto());
                            mList.add(userEntity);
                        }
                        mAdapter.notifyDataSetChanged();
                        MyToast.showToast("添加成功");

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

        //编辑部门


        //修改人员职位
        if (requestCode == FLAG_UPDATEJOB) {
            String positionId = data.getStringExtra("positionId");
            mPositionID = Integer.parseInt(positionId);
            Call call = HttpRequest.getIResourceOANetAction().getaddEmployee(ssid, currentCompanyId, mPositionID, mUserID + "");
            callRequest(call, new HttpCallBack(UserEntity.class, OfficerActivity.this, true) {
                @Override
                public void onSuccess(ResultData mResult) {
                    if ("0".equals(mResult.getSuccess())) {
                        MyToast.showToast("修改成功");

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
}
