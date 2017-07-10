package com.zbxn.main.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pub.utils.ConfigUtils;
import com.squareup.picasso.Picasso;
import com.zbxn.R;
import com.zbxn.main.entity.Contacts;
import com.zbxn.main.entity.WorkmateEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 项目名称：ZBXMobile
 * 创建人：U
 * 创建时间：2017-02-13 13:50:12
 */
public class WorkmateAdapter extends BaseAdapter {


    private Context mCxontext;
    private List<WorkmateEntity> mList;

    public WorkmateAdapter(Context mCxontext, List<WorkmateEntity> mList) {
        this.mCxontext = mCxontext;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mCxontext, R.layout.fragment_workmate_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final WorkmateEntity entity = mList.get(position);
        holder.mNumber.setVisibility(View.GONE);
        //获取到的数据显示上去
        holder.mName.setText(entity.getUserName());
//        if ("0".equals(entity.getReadCount())){
//            holder.mNumber.setVisibility(View.GONE);
//        }else {
//            if (Integer.parseInt(entity.getReadCount()) >= 99){
//                holder.mNumber.setText("99");
//            }else {
//                holder.mNumber.setText(entity.getReadCount()+"");
//            }
//        }




        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showStubImage(R.mipmap.userhead_img)          // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.mipmap.userhead_img)  // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.mipmap.userhead_img)       // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)                          // 设置下载的图片是否缓存在SD卡中
                //.displayer(new RoundedBitmapDisplayer(20))  // 设置成圆角图片
                .build();                                   // 创建配置过得DisplayImageOption对象
        String server = ConfigUtils.getConfig(ConfigUtils.KEY.webServer);
        ImageLoader.getInstance().displayImage(server+entity.getPhotoUrl(), holder.mIamge, options);
        String permissionIds = entity.getPermission();

        if ("100".equals(permissionIds)){
            holder.mState.setImageResource(R.mipmap.colleague_browse);
        }else if ("110".equals(permissionIds)){
            holder.mState.setImageResource(R.mipmap.colleague_establish1);
        }else if ("111".equals(permissionIds)){
            holder.mState.setImageResource(R.mipmap.colleague_xietong1);
        }else {
            holder.mState.setVisibility(View.GONE);
        }
        return convertView;
    }


    static class ViewHolder {
        @BindView(R.id.mIamge)
        CircleImageView mIamge;
        @BindView(R.id.mNumber)
        TextView mNumber;
        @BindView(R.id.mName)
        TextView mName;
        @BindView(R.id.mState)
        ImageView mState;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
