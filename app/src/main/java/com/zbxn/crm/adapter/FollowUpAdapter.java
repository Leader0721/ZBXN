package com.zbxn.crm.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pub.utils.ConfigUtils;
import com.pub.utils.DateUtils;
import com.pub.utils.JsonUtil;
import com.pub.utils.PreferencesUtils;
import com.pub.utils.StringUtils;
import com.pub.widget.MyGridView;
import com.pub.widget.MyListView;
import com.zbxn.R;
import com.zbxn.crm.activity.custom.CustomActivity;
import com.zbxn.crm.entity.FollowUpEntity;
import com.zbxn.crm.entity.StaticTypeEntity;
import com.zbxn.main.activity.mission.PhotoDetailActivity;
import com.zbxn.main.adapter.PhotoListAdapter;
import com.zbxn.main.entity.PhotosEntity;
import com.zbxn.main.listener.ICustomListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author: ysj
 * @date: 2017-01-13 19:09
 */
public class FollowUpAdapter extends BaseAdapter {

    private Context mContext;
    private List<FollowUpEntity> mList;
    private List<StaticTypeEntity> typeList;
    private List<FollowUpEntity.AttachmentStrBean> list;
    private ArrayList<PhotosEntity> lists = new ArrayList<PhotosEntity>();

    public FollowUpAdapter(Context mContext, List<FollowUpEntity> mList) {
        this.mContext = mContext;
        this.mList = mList;
        String typeStr = PreferencesUtils.getString(mContext, CustomActivity.RECORDTYPELIST);
        typeList = JsonUtil.fromJsonList(typeStr, StaticTypeEntity.class);
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
            convertView = View.inflate(mContext, R.layout.list_item_followup, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        FollowUpEntity entity = mList.get(position);

        holder.tvContent.setText(entity.getRecordContent());
        holder.tvName.setText(entity.getCreateUserName());
        holder.tvNum.setText(entity.getReplyCount() + "");
        holder.tvTime.setText((DateUtils.fromTodaySimple(StringUtils.convertToDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"), entity.getCreateTimeStr()))));

        for (int i = 0; i < typeList.size(); i++) {
            if (entity.getRecordType() == Integer.decode(typeList.get(i).getKey())) {
                holder.tvLog.setText(typeList.get(i).getValue());
            }
        }
        list = entity.getAttachmentStr();
        initData(holder.gridview, holder.listview);
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_log)
        TextView tvLog;
        @BindView(R.id.tv_content)
        TextView tvContent;
        @BindView(R.id.tv_num)
        TextView tvNum;
        @BindView(R.id.gridview)
        MyGridView gridview;
        @BindView(R.id.listview)
        MyListView listview;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    private void initData(MyGridView gridview, MyListView listview) {
        if (StringUtils.isEmpty(list)) {
            list = new ArrayList<>();
        }
        List<FollowUpEntity.AttachmentStrBean> listTemp = new ArrayList<>();
        final List<FollowUpEntity.AttachmentStrBean> listTempFile = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (".jpg".equalsIgnoreCase(list.get(i).getType()) ||
                    ".png".equalsIgnoreCase(list.get(i).getType())) {
                listTemp.add(list.get(i));
            } else {
                listTempFile.add(list.get(i));
            }
        }
        final String webServer = ConfigUtils.getConfig(ConfigUtils.KEY.webServer);
        PhotosEntity entity = null;
        lists.clear();
        for (FollowUpEntity.AttachmentStrBean p : listTemp) {
            entity = new PhotosEntity();
            entity.setAppname("");
            entity.setId("");
            entity.setImgurl(webServer + p.getUrl());
            entity.setImgurlNet(webServer + p.getUrl());
            lists = updatePhotos(lists, entity);
        }
        PhotoListAdapter photoListAdapter = new PhotoListAdapter(mContext, lists, R.layout.list_item_select_photos, listener, false);
        gridview.setAdapter(photoListAdapter);

//        AttachmentListViewAdapter attachmentListViewAdapter = new AttachmentListViewAdapter(mContext, listTempFile);
//        listview.setAdapter(attachmentListViewAdapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(webServer + listTempFile.get(position).getUrl()));
                mContext.startActivity(intent);
            }
        });
    }

    /**
     * 更新照片list
     *
     * @param photoList
     * @param entity
     * @return
     */
    private ArrayList<PhotosEntity> updatePhotos(ArrayList<PhotosEntity> photoList, PhotosEntity entity) {
        List<PhotosEntity> listPhotosTemp = new ArrayList<PhotosEntity>();
        listPhotosTemp.addAll(photoList);
        photoList.clear();
        if (null != entity) {
            listPhotosTemp.add(entity);
        }

        photoList.addAll(listPhotosTemp);
        return photoList;
    }

    private ICustomListener listener = new ICustomListener() {

        @Override
        public void onCustomListener(int obj0, Object obj1, int position) {
            switch (obj0) {
                case 3://显示大图
                    ArrayList<String> list_Ads = new ArrayList<>();
                    for (int j = 0; j < lists.size(); j++) {
                        list_Ads.add(lists.get(j).getImgurl());
                    }
                    Intent intent = new Intent(mContext, PhotoDetailActivity.class);
                    intent.putExtra("list", list_Ads);
                    intent.putExtra("position", position);
                    mContext.startActivity(intent);
                    break;
                default:
                    break;
            }
        }
    };

}
