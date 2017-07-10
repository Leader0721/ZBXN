package com.zbxn.crm.activity.custom;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.pub.base.BaseActivity;
import com.pub.base.BaseApp;
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
import com.zbxn.crm.adapter.FollowerAdapter;
import com.zbxn.crm.entity.CustomDetailEntity;
import com.zbxn.main.activity.contacts.ContactsChoseActivity;
import com.zbxn.main.activity.login.LoginActivity;
import com.zbxn.main.entity.Contacts;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;

/**
 * Created by Administrator on 2017/1/17.
 */
public class AdjustFollowerActivity extends BaseActivity {
    @BindView(R.id.iv_icon)
    CircleImageView ivIcon;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.mGridView)
    MyGridView mGridView;
    private FollowerAdapter mAdapter;
    final int FLAG_ADDUSER = 1000;
    private ArrayList<Contacts> mListContacts = new ArrayList<>();
    private List<CustomDetailEntity> mList = new ArrayList<>();
    private String ID;
    private String UserID;
    private String followerName;
    private String followerIcon;

    //空的加号
    private Contacts entityEmpty = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adjustfollower);
        ButterKnife.bind(this);
        setTitle("跟进成员");
        Intent intent = getIntent();
        ID = CustomActivity.CUSTOMID;
        mList = JsonUtil.fromJsonList(intent.getStringExtra("List"), CustomDetailEntity.class);
        followerName = intent.getStringExtra("followerName");
        followerIcon = intent.getStringExtra("followerIcon");

        tvName.setText(followerName);
        String server = ConfigUtils.getConfig(ConfigUtils.KEY.webServer);
        ImageLoader.getInstance().displayImage(server + followerIcon, ivIcon);

        entityEmpty = new Contacts();
        entityEmpty.setUserName("");
        entityEmpty.setId(-1);
        entityEmpty.setPortrait("");

        mListContacts.clear();
        if (!StringUtils.isEmpty(mList)) {
            for (int i = 0; i < mList.size(); i++) {
                Contacts entity = new Contacts();
                entity.setUserName(mList.get(i).getUserName());
                entity.setId(Integer.parseInt(mList.get(i).getUserID()));
                entity.setPortrait(mList.get(i).getUserIcon());
                mListContacts.add(entity);
            }
        }
        mListContacts = updatePhotos(mListContacts, null);
        mAdapter = new FollowerAdapter(mListContacts, this);
        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (StringUtils.isEmpty(mListContacts)) {
                    mListContacts.add(entityEmpty);
                }
                if (position == mListContacts.size() - 1) {
                    mListContacts.remove(mListContacts.size() - 1);
                    Intent intent = new Intent(AdjustFollowerActivity.this, ContactsChoseActivity.class);
                    intent.putExtra("type", 1);//0-查看 1-多选 2-单选
                    intent.putExtra("list", mListContacts);
                    startActivityForResult(intent, FLAG_ADDUSER);
                }
            }
        });

    }

    @Override
    public void initRight() {
        setRight1("保存");
        setRight1Icon(0);
        setRight1Show(true);
        setRight2Show(false);
        super.initRight();
    }

    //编辑的时候必须传进去id
    @Override
    public void actionRight1(MenuItem menuItem) {
        mList.clear();
        if (!StringUtils.isEmpty(mListContacts)) {
            mListContacts.remove(mListContacts.size() - 1);
        }
        for (int i = 0; i < mListContacts.size(); i++) {
            CustomDetailEntity entity = new CustomDetailEntity();
            entity.setUserID(mListContacts.get(i).getId() + "");
            entity.setUserName(mListContacts.get(i).getUserName());
            entity.setUserIcon(mListContacts.get(i).getPortrait());
            mList.add(entity);
        }


        final List<CustomDetailEntity> list = mList;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i).getUserID() + "");
            if (i != list.size() - 1) {
                sb.append(",");
            }
        }
        UserID = sb.toString();

        if (mList.size() == 0){
            MyToast.showToast("至少有一个共同跟进人");
            mListContacts = updatePhotos(mListContacts, null);
            mAdapter = new FollowerAdapter(mListContacts, this);
            mGridView.setAdapter(mAdapter);
        }else {
            String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
            String CurrentCompanyId = PreferencesUtils.getString(BaseApp.getContext(), LoginActivity.FLAG_ZMSCOMPANYID, "");
            Call call = HttpRequest.getIResourceOANetAction().createCommonFollow(ssid, CurrentCompanyId, ID, sb.toString());
            callRequest(call, new HttpCallBack(Contacts.class, this, false) {
                @Override
                public void onSuccess(ResultData mResult) {
                    if ("0".equals(mResult.getSuccess())) {
                        Intent data = new Intent();
                        data.putExtra("list", (Serializable) list);
                        setResult(Activity.RESULT_OK, data);
                        finish();
                    } else {
                        if (list.size() == 1){
                            MyToast.showToast("至少选择一个共同跟进人");
                        }else {
                            MyToast.showToast(mResult.getMsg());
                        }

                    }
                }

                @Override
                public void onFailure(String string) {

                }
            });
        }


        super.actionRight1(menuItem);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        mListContacts = (ArrayList<Contacts>) data.getExtras().getSerializable(ContactsChoseActivity.Flag_Output_Checked);
        mListContacts.add(entityEmpty);

        mAdapter = new FollowerAdapter(mListContacts, this);
        mGridView.setAdapter(mAdapter);
    }


    /**
     * 更新共同跟进人list
     *
     * @param contactList
     * @param entity
     * @return
     */
    private ArrayList<Contacts> updatePhotos(ArrayList<Contacts> contactList, Contacts entity) {
        if (!StringUtils.isEmpty(contactList)) {
            mListContacts.remove(entity);
        } else {
            contactList = new ArrayList<Contacts>();
        }
        List<Contacts> listContactTemp = new ArrayList<Contacts>();
        listContactTemp.addAll(contactList);
        contactList.clear();
        if (null != entity) {
            listContactTemp.add(entityEmpty);
        }

        contactList.addAll(listContactTemp);
        contactList.add(entityEmpty);
        return contactList;
    }


}
