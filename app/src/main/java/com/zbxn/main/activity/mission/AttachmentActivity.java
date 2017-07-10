package com.zbxn.main.activity.mission;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.pub.base.BaseActivity;
import com.pub.utils.ConfigUtils;
import com.pub.widget.MyGridView;
import com.pub.widget.MyListView;
import com.zbxn.R;
import com.zbxn.main.adapter.AttachmentListViewAdapter;
import com.zbxn.main.adapter.PhotoListAdapter;
import com.zbxn.main.entity.MissionAttachmentEntity;
import com.zbxn.main.entity.PhotosEntity;
import com.zbxn.main.listener.ICustomListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * wj
 * 附件查看页
 */
public class AttachmentActivity extends BaseActivity {

    @BindView(R.id.gridview)
    MyGridView gridview;
    @BindView(R.id.listview)
    MyListView listview;

    private List<MissionAttachmentEntity> list;
    private ArrayList<PhotosEntity> lists = new ArrayList<PhotosEntity>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attachment);
        ButterKnife.bind(this);
        setTitle("查看附件");

        list = (List<MissionAttachmentEntity>) getIntent().getSerializableExtra("list");

        initData();
    }

    private void initData() {
        List<MissionAttachmentEntity> listTemp = new ArrayList<>();
        final List<MissionAttachmentEntity> listTempFile = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (".jpg".equalsIgnoreCase(list.get(i).getAttachmentSuffix()) ||
                    ".png".equalsIgnoreCase(list.get(i).getAttachmentSuffix())) {
                listTemp.add(list.get(i));
            } else {
                listTempFile.add(list.get(i));
            }
        }
        final String webServer = ConfigUtils.getConfig(ConfigUtils.KEY.webServer);
        PhotosEntity entity = null;
        lists.clear();
        for (MissionAttachmentEntity p : listTemp) {
            entity = new PhotosEntity();
            entity.setAppname("");
            entity.setId("");
            entity.setImgurl(webServer + p.getAttachmentUrl());
            entity.setImgurlNet(webServer + p.getAttachmentUrl());
            lists = updatePhotos(lists, entity);
        }
        PhotoListAdapter photoListAdapter = new PhotoListAdapter(this, lists, R.layout.list_item_select_photos, listener, false);
        gridview.setAdapter(photoListAdapter);

        AttachmentListViewAdapter attachmentListViewAdapter = new AttachmentListViewAdapter(this, listTempFile);
        listview.setAdapter(attachmentListViewAdapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(webServer + listTempFile.get(position).getAttachmentUrl()));
                startActivity(intent);
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
                    Intent intent = new Intent(AttachmentActivity.this, PhotoDetailActivity.class);
                    intent.putExtra("list", list_Ads);
                    intent.putExtra("position", position);
                    startActivity(intent);
                    break;


                default:
                    break;
            }
        }
    };

}
