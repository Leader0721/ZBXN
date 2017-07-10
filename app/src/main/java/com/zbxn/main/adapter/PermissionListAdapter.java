package com.zbxn.main.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zbxn.R;
import com.zbxn.main.activity.jobManagement.CreateJobActivity;
import com.zbxn.main.activity.jobManagement.PermissionSelectedFragment;
import com.zbxn.main.entity.PermissionEntity;
import com.zbxn.main.entity.TreeElement;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 项目名称：ZBXMobile
 * 创建人：U
 * 创建时间：2017-01-10 2017-01-11 11:53:07
 */
public class PermissionListAdapter extends BaseAdapter {


    private Context mCxontext;
    private List<PermissionEntity> mList;
    private ArrayList<TreeElement> dataList;

    public PermissionListAdapter(Context mContext, List<PermissionEntity> mList) {
        this.mCxontext = mContext;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mCxontext, R.layout.createjob_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final PermissionEntity entity = mList.get(position);

        //获取到的数据显示上去
        holder.tvName.setText(entity.getPermissionName());
        if (entity.isCheck()) {
            holder.tvNumber.setText("有");
            holder.imgCheck.setSelected(true);
        } else {
            holder.imgCheck.setSelected(false);
            holder.tvNumber.setText("无");
        }
        holder.imgCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (entity.isCheck()){
                    holder.imgCheck.setSelected(false);
                    holder.tvNumber.setText("无");
                    entity.setCheck(false);
                    ChangeData(entity.getID(),false);

                }else {
                    holder.imgCheck.setSelected(true);
                    holder.tvNumber.setText("有");
                    entity.setCheck(true);
                    ChangeData(entity.getID(),true);

                }
            }
        });
        return convertView;
    }

    protected void ChangeData(int id,boolean mBoolean) {
        dataList = new ArrayList<>();
        List<PermissionEntity> listTemp = CreateJobActivity.mList;
        int position = 0;
        for (int i = 0; i < listTemp.size(); i++) {
            if (listTemp.get(i).getParentID() == id) {
                TreeElement   element = new TreeElement(0, listTemp.get(i).getID(), listTemp.get(i).getParentID(), listTemp.get(i).getPermissionName(),
                        false, true, position,mBoolean);

                dataList.add(element);
                listTemp.get(i).setCheck(mBoolean);
                position++;
            }
        }

        for (int i = 0; i < dataList.size(); i++) {
            position = 0;
            for (int j = 0; j < listTemp.size(); j++) {
                if (dataList.get(i).getId() == listTemp.get(j).getParentID()) {

                    TreeElement  element = new TreeElement(1, listTemp.get(j).getID(), listTemp.get(j).getParentID(), listTemp.get(j).getPermissionName(),
                            false, true, position, mBoolean);

                    dataList.get(i).getDataList().add(element);
                    listTemp.get(j).setCheck(mBoolean);
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
                                false, true, position, mBoolean);

                        dataList.get(i).getDataList().get(j).getDataList().add(element);
                        listTemp.get(k).setCheck(mBoolean);
                        position++;
                    }
                }
            }
        }


    }
    static class ViewHolder {
        @BindView(R.id.img_check)
        ImageView imgCheck;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_number)
        TextView tvNumber;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
