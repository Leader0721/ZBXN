package com.zbxn.main.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.pub.common.EventCustom;
import com.pub.common.KeyEvent;
import com.pub.utils.ConfigUtils;
import com.zbxn.R;
import com.zbxn.main.activity.contacts.ContactsChoseActivity;
import com.zbxn.main.entity.Contacts;
import com.zbxn.main.entity.ContactsDepartmentEntity;
import com.zbxn.main.listener.ICustomListener;

import org.simple.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2017/2/13.
 */
public class PartmanAdapter extends BaseExpandableListAdapter {

    private Context mContext;//
    private final List<ContactsDepartmentEntity> listGroup;// 数据集合
    private LayoutInflater listContainer;// 视图容器
    private ICustomListener mListener;// 视图容器
    private int mType;//

    private HashMap<String, Integer> mAlphaIndexer;// 存放存在的汉语拼音首字母和与之对应的列表位置
    private String[] mSections;// 存放存在的汉语拼音首字母

    public PartmanAdapter(Activity context, final List<ContactsDepartmentEntity> data, ICustomListener listener
            , final HashMap<String, Integer> alphaIndexer, final String[] sections, int type) {
        this.listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
        this.mContext = context;
        this.listGroup = data;
        this.mListener = listener;
        this.mType = type;

        mAlphaIndexer = alphaIndexer;
        mSections = sections;

        for (int i = 0; i < listGroup.size(); i++) {
            // 当前汉语拼音首字母
            // getAlpha(list.get(i));
            String currentStr = listGroup.get(i).getCaptialChar();
            // 上一个汉语拼音首字母，如果不存在为“ ”
            String previewStr = (i - 1) >= 0 ? listGroup.get(i - 1).getCaptialChar() : " ";
            if (!previewStr.equals(currentStr)) {
                String name = listGroup.get(i).getCaptialChar();
                mAlphaIndexer.put(name, i);
                mSections[i] = name;
            }
        }
    }

    @Override
    public int getGroupCount() {
        return listGroup.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return listGroup.get(groupPosition).getListContacts().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return listGroup.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return listGroup.get(groupPosition).getListContacts().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        // 这个的意思就是你的每个item中元素的id是否稳定，如果你用position当id ，那就是不稳定的，如果有自己特定的id那就是稳定的
        // 就是干这个用的
        return true;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ViewHolderHeader holder = null;
        if (convertView == null) {
            convertView = listContainer.inflate(R.layout.list_item_contacts_department_group, parent, false);
            holder = new ViewHolderHeader(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolderHeader) convertView.getTag();
        }

        final ContactsDepartmentEntity entity = listGroup.get(groupPosition);

        String currentStr = entity.getCaptialChar();
        String previewStr = (groupPosition - 1) >= 0 ? listGroup.get(groupPosition - 1).getCaptialChar() : " ";

        if (!previewStr.equals(currentStr)) {
            holder.mCaptialChar.setVisibility(View.VISIBLE);
            holder.mCaptialChar.setText(currentStr);
        } else {
            holder.mCaptialChar.setVisibility(View.GONE);
        }

        holder.mDepartmentName.setText(entity.getDepartmentName());
        if (isExpanded) {
            holder.mDepartmentImage.setImageResource(R.mipmap.bg_banner_up);
        } else {
            holder.mDepartmentImage.setImageResource(R.mipmap.bg_banner_down);
        }

        if (entity.isSelected()) {
            holder.mCheck.setSelected(true);
        } else {
            holder.mCheck.setSelected(false);
        }
        //可选人
        if (mType == 1) {
            holder.mCheck.setVisibility(View.VISIBLE);

            holder.mCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v.isSelected()) {
                        //v.setSelected(false);
                        entity.setSelected(false);
                        for (int i = 0; i < entity.getListContacts().size(); i++) {
                            entity.getListContacts().get(i).setSelected(false);
                            ContactsChoseActivity.mHashMap.remove(entity.getListContacts().get(i).getId() + "");
                        }
                    } else {
                        //v.setSelected(true);
                        entity.setSelected(true);
                        for (int i = 0; i < entity.getListContacts().size(); i++) {
                            entity.getListContacts().get(i).setSelected(true);
                            ContactsChoseActivity.mHashMap.put(entity.getListContacts().get(i).getId() + "", entity.getListContacts().get(i));
                        }
                    }
                    notifyDataSetChanged();
                    //通知更新选择人
                    EventCustom eventCustom = new EventCustom();
                    eventCustom.setTag(KeyEvent.UPDATECONTACTSSELECT);
                    eventCustom.setObj("");
                    EventBus.getDefault().post(eventCustom);
                }
            });
        }
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = listContainer.inflate(R.layout.list_item_contacts_people, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final ContactsDepartmentEntity entity = listGroup.get(groupPosition);
        final Contacts entitySub = entity.getListContacts().get(childPosition);

        holder.mCaptialChar.setVisibility(View.GONE);

        //姓名
        holder.mRemarkName.setText(entitySub.getUserName() + "");
        String mBaseUrl = ConfigUtils.getConfig(ConfigUtils.KEY.webServer);
        ImageLoader.getInstance().displayImage(mBaseUrl + entitySub.getPortrait(), holder.mPortrait);
        holder.mMobileNumber.setText("ID:" + entitySub.getNumber());

        if (entitySub.isSelected()) {
            holder.mCheck.setSelected(true);
        } else {
            holder.mCheck.setSelected(false);
        }
//        holder.mCheck.setEnabled(false);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    static class ViewHolderHeader {
        @BindView(R.id.mCaptialChar)
        TextView mCaptialChar;
        @BindView(R.id.mCheck)
        ImageView mCheck;
        @BindView(R.id.mDepartmentName)
        TextView mDepartmentName;
        @BindView(R.id.mDepartmentImage)
        ImageView mDepartmentImage;
        @BindView(R.id.mDepartmentImageLayout)
        LinearLayout mDepartmentImageLayout;

        ViewHolderHeader(View view) {
            ButterKnife.bind(this, view);
        }
    }

    static class ViewHolder {
        @BindView(R.id.mLayout)
        LinearLayout mLayout;
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
        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
