package com.zbxn.main.activity.jobManagement;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.zbxn.R;
import com.zbxn.main.adapter.DetaiTreeAdapter;
import com.zbxn.main.entity.PermissionEntity;
import com.zbxn.main.entity.TreeElement;
import com.zbxn.main.listener.ICustomListener;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class PermissionSelectedFragment extends Fragment {

    @BindView(R.id.m_listview)
    ListView mListview;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.tv_ok)
    TextView tvOk;

    CallBackValue mCvb;

    private DetaiTreeAdapter mAdapter;
    private ArrayList<TreeElement> dataList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_permission_selected, container, false);
        ButterKnife.bind(this, view);
        initView();

        return view;
    }

    private void initView() {
        mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                mAdapter.onExpandClick(arg2);
            }
        });
    }

    protected void initData(int id, CallBackValue cvb) {
        dataList = new ArrayList<>();
        mCvb = cvb;
        List<PermissionEntity> listTemp = CreateJobActivity.mList;
        int position = 0;
        for (int i = 0; i < listTemp.size(); i++) {
            if (listTemp.get(i).getParentID() == id) {

                TreeElement   element = new TreeElement(0, listTemp.get(i).getID(), listTemp.get(i).getParentID(), listTemp.get(i).getPermissionName(),
                        false, true, position, listTemp.get(i).isCheck());
                dataList.add(element);
                position++;
            }
        }

        for (int i = 0; i < dataList.size(); i++) {
            position = 0;
            for (int j = 0; j < listTemp.size(); j++) {
                if (dataList.get(i).getId() == listTemp.get(j).getParentID()) {

                    TreeElement  element = new TreeElement(1, listTemp.get(j).getID(), listTemp.get(j).getParentID(), listTemp.get(j).getPermissionName(),
                            false, true, position, listTemp.get(j).isCheck());

//
//                    element = new TreeElement(1, listTemp.get(j).getID(), listTemp.get(j).getParentID(), listTemp.get(j).getPermissionName(),
//                            false, true, position, listTemp.get(j).isCheck());
                    dataList.get(i).getDataList().add(element);
                    position++;
                }
            }
        }

        for (int i = 0; i < dataList.size(); i++) {
            position = 0;
            ArrayList<TreeElement> subList = dataList.get(i).getDataList();
            for (int j = 0; j < subList.size(); j++) {
                for (int k = 0; k < listTemp.size(); k++) {
                    if (subList.get(j).getId() == listTemp.get(k).getParentID()) {

                        TreeElement  element = new TreeElement(2, listTemp.get(k).getID(), listTemp.get(k).getParentID(), listTemp.get(k).getPermissionName(),
                                false, true, position, listTemp.get(k).isCheck());


//                        element = new TreeElement(2, listTemp.get(k).getID(), listTemp.get(k).getParentID(), listTemp.get(k).getPermissionName(),
//                                false, true, position, listTemp.get(k).isCheck());
//
                        dataList.get(i).getDataList().get(j).getDataList().add(element);
                        position++;
                    }
                }
            }
        }

        mAdapter = new DetaiTreeAdapter(dataList, getActivity(), mListener);
        mListview.setAdapter(mAdapter);

        for (int i = 0; i < dataList.size(); i++) {
            mAdapter.onExpandClick(i);
        }

    }

    private ICustomListener mListener = new ICustomListener() {
        @Override
        public void onCustomListener(int obj0, Object obj1, int position) {
            TreeElement entity = (TreeElement) obj1;
            switch (obj0) {
                case 0:
                    refreshData(entity, dataList);
                    TreeElement treeElement = getParentList(entity, dataList);
                    if (treeElement != null) {
                        refreshDataParent(entity, treeElement);
                    }
                    mAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    private TreeElement getParentList(TreeElement entity, final ArrayList<TreeElement> listTemp) {
        for (int i = 0; i < listTemp.size(); i++) {
            if (listTemp.get(i).getId() == entity.getParentId()) {
                return listTemp.get(i);
            }
            if (listTemp.get(i).getDataList().size() > 0) {
                TreeElement entityTemp = getParentList(entity, listTemp.get(i).getDataList());
                if (null != entityTemp) {
                    return entityTemp;
                } else {
                    continue;
                }
            }
        }
        return null;
    }

    private void refreshData(TreeElement entity, final ArrayList<TreeElement> listTemp) {
        for (int i = 0; i < listTemp.size(); i++) {
            if (listTemp.get(i).getParentId() == entity.getId()) {
                listTemp.get(i).setCheck(entity.isCheck());
                if (listTemp.get(i).getDataList().size() > 0) {
                    refreshData(entity, listTemp.get(i).getDataList());
                }
            }
        }
    }

    private void refreshDataParent(TreeElement entity, final TreeElement entityParent) {
        for (int i = 0; i < entityParent.getDataList().size(); i++) {
            if (entity.isCheck()) {
                entityParent.setCheck(true);

                TreeElement treeElement = getParentList(entityParent, dataList);
                if (treeElement != null) {
                    refreshDataParent(entityParent, treeElement);
                }
            } else {
                int count = 0;
                for (int j = 0; j < entityParent.getDataList().size(); j++) {
                    if (entityParent.getDataList().get(j).isCheck()) {
                        count++;
                    }
                }
                if (count <= 0) {
                    entityParent.setCheck(false);

                    TreeElement treeElement = getParentList(entityParent, dataList);
                    if (treeElement != null) {
                        refreshDataParent(entityParent, treeElement);
                    }
                    return;
                }
            }
        }
    }

    /**
     * 深复制
     *
     * @param src
     * @param <T>
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static <T> List<T> deepCopy(List<T> src) {
        try {
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(byteOut);
            out.writeObject(src);

            ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
            ObjectInputStream in = new ObjectInputStream(byteIn);
            @SuppressWarnings("unchecked")
            List<T> dest = (List<T>) in.readObject();
            return dest;
        } catch (Exception e) {
            return src;
        }
    }

    @OnClick({R.id.tv_ok, R.id.tv_cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_ok:
                List<Integer> listAll = new ArrayList<>();
                List<Integer> listCheck = new ArrayList<>();
                for (int i = 0; i < dataList.size(); i++) {
                    listAll.add(dataList.get(i).getId());
                    if (dataList.get(i).isCheck()) {
                        listCheck.add(dataList.get(i).getId());
                    }

                    /*ArrayList<TreeElement> subList = dataList.get(i).getDataList();
                    for (int j = 0; j < subList.size(); j++) {
                        listAll.add(subList.get(j).getId());
                        if (subList.get(j).isCheck()) {
                            listCheck.add(subList.get(j).getId());
                        }

                        ArrayList<TreeElement> subList1 = subList.get(j).getDataList();
                        for (int k = 0; k < subList1.size(); k++) {
                            listAll.add(subList1.get(k).getId());
                            if (subList1.get(k).isCheck()) {
                                listCheck.add(subList1.get(k).getId());
                            }
                        }
                    }*/
                }

                for (int i = 0; i < CreateJobActivity.mList.size(); i++) {
                    //先置为未选中
                    for (int j = 0; j < listAll.size(); j++) {
                        if (listAll.get(j) == CreateJobActivity.mList.get(i).getID()) {
                            CreateJobActivity.mList.get(i).setCheck(false);
                        }
                    }
                    for (int j = 0; j < listCheck.size(); j++) {
                        if (listCheck.get(j) == CreateJobActivity.mList.get(i).getID()) {
                            CreateJobActivity.mList.get(i).setCheck(true);
                        }
                    }
                }

                mCvb.SendMessageValue("");
                break;
            case R.id.tv_cancel:
                mCvb.SendMessageValue("CANCEL");
                break;
        }
    }

    //确定回调
    public interface CallBackValue {
        void SendMessageValue(String str);
    }
}
