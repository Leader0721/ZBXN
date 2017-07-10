package com.zbxn.main.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zbxn.R;
import com.zbxn.main.entity.UserEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 项目名称：ZBXMobile
 * 创建人：U
 * 创建时间：2017-01-10 13:20:21
 */
public class UserListAdapter extends BaseAdapter {


    private Context mCxontext;
    private List<UserEntity> mList;

    public UserListAdapter(Context mContext, List<UserEntity> mList) {
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
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mCxontext, R.layout.list_item_contacts_people, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        //获取到的数据显示上去
        holder.mRemarkName.setText(mList.get(position).getUsername());
        Picasso.with(mCxontext).load(mList.get(position).getPhoto()).placeholder(R.mipmap.temp110).error(R.mipmap.temp110).into(holder.mPortrait);
        return convertView;
    }


    static class ViewHolder {
        @BindView(R.id.mCaptialChar)
        TextView mCaptialChar;
        @BindView(R.id.mCheck)
        ImageView mCheck;
        @BindView(R.id.mPortrait)
        CircleImageView mPortrait;
        @BindView(R.id.mRemarkName)
        TextView mRemarkName;
        @BindView(R.id.mMobileNumber)
        TextView mMobileNumber;
        @BindView(R.id.mLayout)
        LinearLayout mLayout;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
