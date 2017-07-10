/**
 * @author Leestar54
 * http://www.cnblogs.com/leestar54
 */
package com.zbxn.main.service;

import com.google.gson.annotations.Expose;

public class AppVersion {

//    private String name;//版本名称
//    private String note;//版本更新说明
//    private String URL;//版本 APK 的下载地址
//    private int code;//版本代码

    /**
     * createtime : 2016-11-17 15:02:20
     * id : 5
     * downloadurl : http://zms.zbzbx.com:8088/ZBXMobile/upload/version_mobile/ZMS_1.0.21.apk
     * flag : Android
     * versionname : ZMS-1.0.21
     * description : <br>1.新增OKR积分统计功能</br>
     <br>2.优化N币、审批、考勤模块</br>

     * versioncode : 10021
     * zmscompanyid : 267
     */

    @Expose
    private String createtime;
    @Expose
    private int id;
    @Expose
    private String downloadurl;
    @Expose
    private String flag;
    @Expose
    private String versionname;
    @Expose
    private String description;
    @Expose
    private int versioncode;
    @Expose
    private int zmscompanyid;

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDownloadurl() {
        return downloadurl;
    }

    public void setDownloadurl(String downloadurl) {
        this.downloadurl = downloadurl;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getVersionname() {
        return versionname;
    }

    public void setVersionname(String versionname) {
        this.versionname = versionname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getVersioncode() {
        return versioncode;
    }

    public void setVersioncode(int versioncode) {
        this.versioncode = versioncode;
    }

    public int getZmscompanyid() {
        return zmscompanyid;
    }

    public void setZmscompanyid(int zmscompanyid) {
        this.zmscompanyid = zmscompanyid;
    }
}
