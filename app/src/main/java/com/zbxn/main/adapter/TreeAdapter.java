package com.zbxn.main.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.zbxn.main.entity.TreeElement;

import java.util.ArrayList;
import java.util.List;

/**
 * 处理点击 数据更新 通用
 *
 * @author jrh
 */
public class TreeAdapter extends BaseAdapter {

    private List<TreeElement> mParentList;
    private Context context;

    public TreeAdapter(List<TreeElement> parentList, Context context) {
        super();
        this.mParentList = parentList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return mParentList == null ? 0 : mParentList.size();
    }

    @Override
    public Object getItem(int arg0) {
        return mParentList == null ? null : mParentList.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(int arg0, View arg1, ViewGroup arg2) {
        return null;
    }

    public void onExpandClick(int position) {
        // 父节点没有子元素直接返回
        if (!mParentList.get(position).isHasChild()) {
            return;
        }
        // 父节点已经扩展
        if (mParentList.get(position).isExpandAble()) {
            mParentList.get(position).setExpandAble(false);
            TreeElement element = mParentList.get(position);
            // 遍历已扩展的元素，删除
            ArrayList<TreeElement> temp = new ArrayList<TreeElement>();
            // 从当前点的下一个元素开始隐藏
            for (int i = position + 1; i < mParentList.size(); i++) {

                if (element.getParentLevel() >= mParentList.get(i)
                        .getParentLevel()) {
                    break;
                }
                temp.add(mParentList.get(i));
            }

            mParentList.removeAll(temp);

            for (int i = position + 1; i < mParentList.size(); i++) {
                mParentList.get(i).setPosition(i);
            }

            notifyDataSetChanged();
        } else {
            // 父节点为点开

            TreeElement objElement = mParentList.get(position);
            objElement.setExpandAble(true);
            int level = objElement.getParentLevel();
            int nextLevel = level + 1;

            ArrayList<TreeElement> tempList = objElement.getDataList();
            //扩充父类集合
            for (int i = 0; i < tempList.size(); i++) {
                TreeElement element = tempList.get(i);
                element.setParentLevel(nextLevel);
                element.setExpandAble(false);
                mParentList.add(position + 1, element);
            }

            for (int i = position + 1; i < mParentList.size(); i++) {
                mParentList.get(i).setPosition(i);
            }

            notifyDataSetChanged();

        }

    }

}
