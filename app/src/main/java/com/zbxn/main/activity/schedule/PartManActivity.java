package com.zbxn.main.activity.schedule;

import android.os.Bundle;
import android.widget.ListView;

import com.pub.base.BaseActivity;
import com.pub.widget.pulltorefreshlv.PullRefreshListView;
import com.zbxn.R;
import com.zbxn.main.adapter.PartmanAdapter;
import com.zbxn.main.adapter.UserListAdapter;
import com.zbxn.main.entity.ContactsDepartmentEntity;
import com.zbxn.main.entity.JobEntity;
import com.zbxn.main.entity.ScheduleDetailEntity;
import com.zbxn.main.entity.UserEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/2/13.
 */
public class PartManActivity extends BaseActivity {

    @BindView(R.id.mListView)
    ListView mListView;
    private List<ContactsDepartmentEntity> lists;
    private PartmanAdapter adapter;
    private List<UserEntity> mList=new ArrayList<>();
    private UserListAdapter adapter1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partman);
        setTitle("参与人");
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        ScheduleDetailEntity entity = (ScheduleDetailEntity) getIntent().getSerializableExtra("entity");
        List<ScheduleDetailEntity.SelectUserListBean> childList = entity.getSelectUserList();
        for (int i = 0; i < childList.size(); i++) {
            UserEntity userEntity = new UserEntity();
            userEntity.setPhoto(childList.get(i).getHeadUrl());
            userEntity.setUsername(childList.get(i).getUserName());
            userEntity.setID(childList.get(i).getUserID());
            mList.add(userEntity);
        }
        adapter1 = new UserListAdapter(this,mList);
        mListView.setAdapter(adapter1);
        adapter1.notifyDataSetChanged();
    }


//    private void initView() {
//        try {
//            List<Contacts> listTemp = BaseApp.DBLoader.findAll(Selector.from(Contacts.class).where("isDepartment", "=", null));
//            //例：分组聚合查询出  Parent表中 非重复的name和它的对应数量
//            List<DbModel> dbModels = BaseApp.DBLoader.findDbModelAll(Selector.from(Contacts.class).select("distinct departmentId", "departmentName")
//                    .where("isDepartment", "=", null).orderBy("departmentName"));
//            lists = new ArrayList<>();
//            ContactsDepartmentEntity contactsDepartment = null;
//            for (int i = 0; i < dbModels.size(); i++) {
//                contactsDepartment = new ContactsDepartmentEntity();
//                contactsDepartment.setCaptialChar(StringUtils.getPinYinHeadChar(dbModels.get(i).getString("departmentName")));
//                contactsDepartment.setDepartmentId(dbModels.get(i).getString("departmentId"));
//                contactsDepartment.setDepartmentName(dbModels.get(i).getString("departmentName"));
//                int countSelect = 0;
//                int countAll = 0;
//                for (int j = 0; j < listTemp.size(); j++) {
//                    if (listTemp.get(j).getDepartmentId().equals(contactsDepartment.getDepartmentId())) {
//                        countAll++;
//                        if (ContactsChoseActivity.mHashMap.containsKey(listTemp.get(j).getId() + "")) {
//                            listTemp.get(j).setSelected(true);
//                            countSelect++;
//                        } else {
//                            listTemp.get(j).setSelected(false);
//                        }
//                        contactsDepartment.getListContacts().add(listTemp.get(j));
//
//                        //设置是否选中group check
//                        if (countSelect >= countAll) {
//                            contactsDepartment.setSelected(true);
//                        } else {
//                            contactsDepartment.setSelected(false);
//                        }
//                    }
//                }
//                lists.add(contactsDepartment);
//            }
//            //JDK7中的Collections.Sort方法实现中，如果两个值是相等的，那么compare方法需要返回0，否则可能会在排序时抛错，而JDK6是没有这个限制的
//            Collections.sort(lists, new Comparator<ContactsDepartmentEntity>() {
//                @Override
//                public int compare(ContactsDepartmentEntity a, ContactsDepartmentEntity b) {
//                    String idA = a.getCaptialChar();
//                    String idB = b.getCaptialChar();
//                    if (idA.compareTo(idB) == 0) {
//                        return 0;
//                    } else if (idA.compareTo(idB) > 0)
//                        return 1;
//                    else
//                        return -1;
//                }
//            });
////            adapter = new PartmanAdapter(this,listTemp);
//            mListView.setAdapter(adapter);
//
//        } catch (DbException e) {
//            e.printStackTrace();
//        }
//
//
//    }

}
