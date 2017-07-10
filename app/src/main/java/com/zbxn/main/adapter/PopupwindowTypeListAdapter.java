package com.zbxn.main.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zbxn.R;
import com.zbxn.main.entity.ApprovalEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 项目名称：弹出框的Adapter
 * 创建人：LiangHanXin
 * 创建时间：2016/9/27 16:28
 */
public class PopupwindowTypeListAdapter extends BaseAdapter {

    public static int savePosionMiddle = -1;
    public static int savePosionLeft = -1;
    public static int savePosionRight = -1;
    public static int savePosionLeft2 = -1;
    public static int savePosionRight2 = -1;
    public static int savePosionLeft3 = -1;
    public static int savePosionRight3 = -1;
    public static int TYPE_APPLY = 101;

    private Context mCxontext;
    private List<ApprovalEntity> mList;
    private int type = 1;

    public PopupwindowTypeListAdapter(Context mContext, List<ApprovalEntity> mList, int type) {
        this.mCxontext = mContext;
        this.mList = mList;
        this.type = type;
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
            convertView = View.inflate(mCxontext, R.layout.list_item_popupwind_type, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //获取到的数据显示上去
        holder.mName.setText(mList.get(position).getName());
        if (TYPE_APPLY == 101) {
            setSelect(holder.imageSel, type, savePosionLeft, savePosionMiddle, savePosionRight, position);
        }
        if (TYPE_APPLY == 102) {
            setSelect(holder.imageSel, type, savePosionLeft2, 0, savePosionRight2, position);
        }
        if (TYPE_APPLY == 103) {
            setSelect(holder.imageSel, type, savePosionLeft3, 0, savePosionRight3, position);
        }

        return convertView;
    }

    public void setSelect(ImageView imageView, int type, int left, int middle, int right, int position) {
        if (type == 1) {
            if (left == -1) {
                if (position == 0) {
                    imageView.setVisibility(View.VISIBLE);
                } else {
                    imageView.setVisibility(View.INVISIBLE);
                }
            } else {
                if (left == position) {
                    imageView.setVisibility(View.VISIBLE);
                } else {
                    imageView.setVisibility(View.INVISIBLE);
                }
            }
        }
        if (type == 2) {
            if (right == -1) {
                if (position == 0) {
                    imageView.setVisibility(View.VISIBLE);
                } else {
                    imageView.setVisibility(View.INVISIBLE);
                }
            } else {
                if (right == position) {
                    imageView.setVisibility(View.VISIBLE);
                } else {
                    imageView.setVisibility(View.INVISIBLE);
                }
            }
        }
        if (type == 3) {
            if (middle == -1) {
                if (position == 0) {
                    imageView.setVisibility(View.VISIBLE);
                } else {
                    imageView.setVisibility(View.INVISIBLE);
                }
            } else {
                if (middle == position) {
                    imageView.setVisibility(View.VISIBLE);
                } else {
                    imageView.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    static class ViewHolder {
        @BindView(R.id.m_name)
        TextView mName;
        @BindView(R.id.image_sel)
        ImageView imageSel;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
