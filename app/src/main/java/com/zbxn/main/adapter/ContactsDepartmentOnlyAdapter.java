package com.zbxn.main.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zbxn.R;
import com.zbxn.main.entity.ContactsDepartmentEntity;
import com.zbxn.main.listener.ICustomListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 项目名称：申请列表的adapter
 * 创建人：Wuzy
 * 创建时间：2016/10/10 10:05
 */
public class ContactsDepartmentOnlyAdapter extends BaseAdapter {

    private Context mCxontext;
    private List<ContactsDepartmentEntity> mList;
    private ICustomListener listener;

    public ContactsDepartmentOnlyAdapter(Context mContext, List<ContactsDepartmentEntity> mList, ICustomListener listener) {
        this.mCxontext = mContext;
        this.mList = mList;
        this.listener = listener;
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

        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mCxontext, R.layout.list_item_contacts_department_only, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ContactsDepartmentEntity entity = mList.get(position);


        //姓名
        holder.mRemarkName.setText(entity.getDepartmentName() + "");
        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.mPortrait)
        CircleImageView mPortrait;
        @BindView(R.id.mRemarkName)
        TextView mRemarkName;
//        @BindView(R.id.mMobileNumber)
//        TextView mMobileNumber;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
