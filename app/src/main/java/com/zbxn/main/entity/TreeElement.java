package com.zbxn.main.entity;

import java.util.ArrayList;

/**
 * 元素 可根据需要添加新的属性
 *
 * @author jrh
 */
public class TreeElement {
    /**
     * 各个元素的层级标识
     */
    private int parentLevel;
    /**
     * id
     */
    private int id;
    /**
     * parentId
     */
    private int parentId;
    /**
     * 节点显示标题
     */
    private String noteName;
    /**
     * 子节点元素集合
     */
    private ArrayList<TreeElement> dataList = new ArrayList<TreeElement>();
    /**
     * 是否已扩展
     */
    private boolean isExpandAble;
    /**
     * 是否有子节点元素
     */
    private boolean isHasChild;
    /**
     * 当前节点位置
     */
    private int position;
    /**
     * 是否选中
     */
    private boolean isCheck;

    /**
     * @param parentLevel  各个元素的层级标识
     * @param id
     * @param noteName     节点显示标题
     * @param isExpandAble 是否已扩展
     * @param isHasChild   是否有子节点元素
     * @param position     当前节点位置
     * @param isCheck      是否选中
     */

    public TreeElement(int parentLevel, int id,int parentId, String noteName, boolean isExpandAble,
                       boolean isHasChild, int position, boolean isCheck) {
        super();
        this.parentLevel = parentLevel;
        this.id = id;
        this.parentId = parentId;
        this.noteName = noteName;
        this.isExpandAble = isExpandAble;
        this.isHasChild = isHasChild;
        this.position = position;
        this.isCheck = isCheck;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isHasChild() {
        return isHasChild;
    }

    public void setHasChild(boolean isHasChild) {
        this.isHasChild = isHasChild;
    }


    public int getParentLevel() {
        return parentLevel;
    }

    public void setParentLevel(int parentLevel) {
        this.parentLevel = parentLevel;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getNoteName() {
        return noteName;
    }

    public void setNoteName(String noteName) {
        this.noteName = noteName;
    }


    public ArrayList<TreeElement> getDataList() {
        return dataList;
    }

    public void setDataList(ArrayList<TreeElement> dataList) {
        this.dataList = dataList;
    }

    public boolean isExpandAble() {
        return isExpandAble;
    }

    public void setExpandAble(boolean isExpandAble) {
        this.isExpandAble = isExpandAble;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}
