package com.zbxn.crm.activity.custom;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.lidroid.xutils.db.sqlite.Selector;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pub.base.BaseApp;
import com.pub.base.BaseFragment;
import com.pub.common.EventCustom;
import com.pub.http.HttpCallBack;
import com.pub.http.HttpRequest;
import com.pub.http.ResultData;
import com.pub.utils.ConfigUtils;
import com.pub.utils.JsonUtil;
import com.pub.utils.MyToast;
import com.pub.utils.PreferencesUtils;
import com.pub.utils.StringUtils;
import com.pub.widget.MyGridView;
import com.zbxn.R;
import com.zbxn.crm.adapter.FollowAdapter;
import com.zbxn.crm.entity.CustomDetailEntity;
import com.zbxn.crm.entity.CustomEntity;
import com.zbxn.main.activity.contacts.ContactsDetailActivity;
import com.zbxn.main.activity.login.LoginActivity;
import com.zbxn.main.activity.schedule.ScheduleActivity;
import com.zbxn.main.entity.Contacts;

import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;

/**
 * 客户基本信息
 *
 * @author: ysj
 * @date: 2017-01-13 10:33
 */
public class BasicInfoFragment extends BaseFragment {
    @BindView(R.id.tt_name)
    TextView ttName;
    @BindView(R.id.tt_state)
    TextView ttState;
    @BindView(R.id.tt_Updatetime)
    TextView ttUpdateTime;
    @BindView(R.id.tt_follower)
    TextView ttFollower;
    @BindView(R.id.moreinfo_rela)
    RelativeLayout moreinfoRela;
    @BindView(R.id.listview_contact)
    LinearLayout listviewContact;
    @BindView(R.id.listview_samefollower)
    LinearLayout listviewSamefollower;
    @BindView(R.id.addcost_iv)
    ImageView addcostIv;
    @BindView(R.id.fordetail_rela)
    RelativeLayout fordetailRela;
    @BindView(R.id.mGridView)
    MyGridView mGridView;
    @BindView(R.id.image_follower)
    CircleImageView imageFollower;
    @BindView(R.id.tt_name_follower)
    TextView ttNameFollower;
    @BindView(R.id.tt_contactCount)
    TextView ttContactCount;
    @BindView(R.id.tt_costcount)
    TextView ttCostcount;
    @BindView(R.id.tt_dealcount)
    TextView ttDealcount;
    @BindView(R.id.scrollView_basic)
    ScrollView scrollViewBasic;
    @BindView(R.id.linear_contacts)
    LinearLayout linearContacts;
    private FollowAdapter mAdapter;
    final int FLAG_ADDUSER = 1000;
    final int Type_CallBack = 1001;
    final int Info_CallBack = 1002;
    final int Cost_CallBack = 1002;
    private String ID = "";
    private List<CustomDetailEntity> contact = new ArrayList<>();
    private List<CustomDetailEntity> sameFollowerCustom = new ArrayList<>();
    private String followerName;
    private String followerIcon;
    private CustomDetailsActivity activity;
    private List<Contacts> listAll;

    @Override
    protected View onCreateView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_basic_info, null);
        ButterKnife.bind(this, view);
        ID = CustomActivity.CUSTOMID;
        initData();
        activity = (CustomDetailsActivity) getActivity();
        scrollViewBasic.smoothScrollTo(0, 0);
        return view;
    }



    @Subscriber
    public void onEventMainThread(EventCustom eventCustom) {

        if (eventCustom.getTag().equals(EditDetailActivity.SUCCESS2)) {
            initData();
        }
    }

    private void initData() {
        String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        String CurrentCompanyId = PreferencesUtils.getString(BaseApp.getContext(), LoginActivity.FLAG_ZMSCOMPANYID, "");
        final Call call = HttpRequest.getIResourceOANetAction().getCustomerSummary(ssid, CurrentCompanyId, ID);
        callRequest(call, new HttpCallBack(CustomEntity.class, getContext(), true) {
            @Override
            public void onSuccess(ResultData mResult) {
                if ("0".equals(mResult.getSuccess())) {
                    CustomEntity entity = (CustomEntity) mResult.getData();
                    if (entity != null) {
                        ttName.setText(entity.getCustName());
                        if (StringUtils.isEmpty(entity.getFollowUserName())) {
                            ttFollower.setText("跟进人:");
                        } else {
                            ttFollower.setText("跟进人:" + entity.getFollowUserName());
                        }
                        if (StringUtils.isEmpty(entity.getUpdateTimeStr())) {
                            ttUpdateTime.setText("更新时间:");
                        } else {
                            ttUpdateTime.setText("更新时间:" + entity.getUpdateTimeStr());
                        }
                        if (entity.getCustState().contains("0")) {
                            ttState.setText("成交状态:暂无");
                        } else if (entity.getCustState().contains("5")) {
                            ttState.setText("成交状态:流失客户");
                        } else if (entity.getCustState().contains("3")) {
                            ttState.setText("成交状态:成交客户");
                        } else if (entity.getCustState().contains("1")) {
                            ttState.setText("成交状态:潜在客户");
                        } else if (entity.getCustState().contains("2")) {
                            ttState.setText("成交状态:普通客户");
                        } else if (entity.getCustState().contains("4")) {
                            ttState.setText("成交状态:长期合作");
                        }

                        contact = entity.getContacts();
                        //对联系人列表进行添加
                        String list = JsonUtil.toJsonString(contact);

                        contact.clear();
                        contact = JsonUtil.fromJsonList(list, CustomDetailEntity.class);
                        //清空listviewContact，避免重复添加
                        listviewContact.removeAllViews();
                        for (int i = 0; i < contact.size(); i++) {
                            if (StringUtils.isEmpty(contact.get(i).getName()) && StringUtils.isEmpty(contact.get(i).getMobile())) {
                                contact.remove(i);
                            }
                            if (contact.size() != 0) {
                                View view = LayoutInflater.from(getContext()).inflate(R.layout.linearlayout_item_contact, null);
                                final TextView name = (TextView) view.findViewById(R.id.tt_name);
                                TextView position = (TextView) view.findViewById(R.id.tt_position);
                                final TextView phoneNum = (TextView) view.findViewById(R.id.tt_phonenum);
                                phoneNum.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (!StringUtils.isEmpty(phoneNum)){
                                            showCallServiceDialog(phoneNum.getText().toString(), name.getText().toString());
                                        }
                                    }
                                });
                                name.setText(contact.get(i).getName());
                                position.setText(contact.get(i).getPosition());
                                phoneNum.setText(contact.get(i).getMobile());
                                listviewContact.addView(view);
                            }
                        }


                        if (contact.size() == 0) {
                            ttContactCount.setText("暂无");
                        } else {
                            ttContactCount.setText(contact.size() + " 人");
                        }

                        sameFollowerCustom = entity.getCommonFollowUser();
                        String list1 = JsonUtil.toJsonString(sameFollowerCustom);
                        sameFollowerCustom.clear();

                        sameFollowerCustom = JsonUtil.fromJsonList(list1, CustomDetailEntity.class);
                        DisplayImageOptions options = new DisplayImageOptions.Builder()
                                .showStubImage(R.mipmap.userhead_img)          // 设置图片下载期间显示的图片
                                .showImageForEmptyUri(R.mipmap.userhead_img)  // 设置图片Uri为空或是错误的时候显示的图片
                                .showImageOnFail(R.mipmap.userhead_img)       // 设置图片加载或解码过程中发生错误显示的图片
                                .cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中
                                .cacheOnDisc(true)                          // 设置下载的图片是否缓存在SD卡中
                                //.displayer(new RoundedBitmapDisplayer(20))  // 设置成圆角图片
                                .build();                                   // 创建配置过得DisplayImageOption对象

                        //对共同跟进人模块进行数据填充和修改
                        mAdapter = new FollowAdapter(sameFollowerCustom, getContext());
                        mGridView.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();
                        scrollViewBasic.smoothScrollTo(0, 0);
                        try {
                            listAll = BaseApp.DBLoader.findAll(Selector.from(Contacts.class).where("isDepartment", "=", null).orderBy("captialChar"));//
                        } catch (Exception e) {
                            listAll = new ArrayList<>();
                            e.printStackTrace();
                        }


                        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Contacts contacts = new Contacts();
                                CustomDetailEntity entity1 = (CustomDetailEntity) mAdapter.getItem(position);
                                contacts.setId(Integer.parseInt(entity1.getUserID()));
                                contacts.setUserName(entity1.getUserName());
                                contacts.setPortrait(entity1.getUserIcon());
                                for (int i = 0; i < listAll.size(); i++) {
                                    if (contacts.getId() == listAll.get(i).getId()) {
                                        contacts = listAll.get(i);
                                    }
                                }
                                Intent intent = new Intent(getContext(), ContactsDetailActivity.class);
                                intent.putExtra(ContactsDetailActivity.Flag_Input_Contactor, contacts);
                                startActivity(intent);
                            }
                        });

                        ttCostcount.setText(entity.getCostFee());
                        ttDealcount.setText(entity.getCloseDealFee());

                        ttNameFollower.setText(entity.getFollowUserName());
                        followerName = entity.getFollowUserName();

                        linearContacts.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Contacts contacts = new Contacts();
                                for (int i = 0; i < listAll.size(); i++) {
                                    if (followerName.equals(listAll.get(i).getUserName())) {
                                        contacts = listAll.get(i);
                                    }
                                }
                                Intent intent = new Intent(getContext(), ContactsDetailActivity.class);
                                intent.putExtra(ContactsDetailActivity.Flag_Input_Contactor, contacts);
                                startActivity(intent);
                            }
                        });

                                followerIcon = entity.getFollowUserIcon();
                        String server = ConfigUtils.getConfig(ConfigUtils.KEY.webServer);
                        ImageLoader.getInstance().displayImage(server + entity.getFollowUserIcon(), imageFollower, options);

                    } else {
                        return;
                    }

                } else {
                    MyToast.showToast("获取数据失败，请重试");
                }
            }

            @Override
            public void onFailure(String string) {
                MyToast.showToast(R.string.NETWORKERROR);
            }
        });
    }

    private void showCallServiceDialog(final String phoneNum, String customName) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        startActivity(intent);
    }


    @Override
    protected void initialize(View root, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != -1) {
            return;
        }
        if (requestCode == FLAG_ADDUSER) {
            sameFollowerCustom = (List<CustomDetailEntity>) data.getExtras().getSerializable("list");
            mAdapter = new FollowAdapter(sameFollowerCustom, getContext());
            mGridView.setAdapter(mAdapter);
            initData();
        }
        if (requestCode == Type_CallBack) {
            initData();
        }
        if (requestCode == Info_CallBack) {
            initData();
            activity.finish();
//            MyToast.showToast("fasdfasdfasdfas");
//            activity.setResult(Activity.RESULT_OK);
//            activity.setResult(activity.RESULT_OK);
        }
        if (requestCode == Cost_CallBack) {
            initData();
        }
    }

    @OnClick({R.id.moreinfo_rela, R.id.listview_samefollower, R.id.addcost_iv, R.id.fordetail_rela, R.id.linear_adjustfollower})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.moreinfo_rela:
                intent = new Intent(getContext(), CustomerInfoActivity.class);
                intent.putExtra("ID", ID);
                startActivityForResult(intent, Info_CallBack);
                break;
            case R.id.addcost_iv:
                intent = new Intent(getContext(), SellingTypeActivity.class);
                intent.putExtra("ID", ID);
                startActivityForResult(intent, Type_CallBack);
                break;
            case R.id.fordetail_rela:
                intent = new Intent(getContext(), CostDetailActivity.class);
                intent.putExtra("ID", ID);
                startActivityForResult(intent, Cost_CallBack);
                break;
            case R.id.linear_adjustfollower:
                intent = new Intent(getContext(), AdjustFollowerActivity.class);
                intent.putExtra("List", JsonUtil.toJsonString(sameFollowerCustom));
                intent.putExtra("ID", ID);
                intent.putExtra("followerName", followerName);
                intent.putExtra("followerIcon", followerIcon);
                startActivityForResult(intent, FLAG_ADDUSER);
                break;
        }
    }
}
