package com.zbxn.main.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zbxn.R;
import com.zbxn.main.entity.TreeElement;
import com.zbxn.main.listener.ICustomListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 具体子adapter 可根据不同的要求自定义
 *
 * @author jrh
 */
public class DetaiTreeAdapter extends TreeAdapter {

    private List<TreeElement> mParentList;
    private Context mContext;
    private  ICustomListener mListener;

    public DetaiTreeAdapter(List<TreeElement> parentList, Context context, ICustomListener listener) {
        super(parentList, context);
        mParentList = parentList;
        mContext = context;
        mListener = listener;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public Object getItem(int arg0) {
        return super.getItem(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return super.getItemId(arg0);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.list_item_position, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final TreeElement treeElement = mParentList.get(position);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins((treeElement.getParentLevel() + 1) * 40, 0, 0, 0);  //设置标题左边距
        holder.imgCheck.setLayoutParams(layoutParams);
        holder.imgCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                treeElement.setCheck(!treeElement.isCheck());

                mListener.onCustomListener(0,treeElement,position);

                notifyDataSetChanged();
            }
        });
        if (treeElement.isCheck()) {
            holder.imgCheck.setSelected(true);
        } else {
            holder.imgCheck.setSelected(false);
        }

        holder.tvName.setText(treeElement.getNoteName());

        return convertView;
    }

    @Override
    public void onExpandClick(int position) {
        super.onExpandClick(position);
    }

    static class ViewHolder {
        @BindView(R.id.img_check)
        ImageView imgCheck;
        @BindView(R.id.tv_name)
        TextView tvName;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
