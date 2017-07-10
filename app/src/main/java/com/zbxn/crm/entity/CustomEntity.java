package com.zbxn.crm.entity;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: ysj
 * @date: 2017-01-13 09:37
 */
public class CustomEntity {
    @Expose
    private String CustName;
    @Expose
    private String Name;
    @Expose
    private String Mobile;
    @Expose
    private String CustState;
    @Expose
    private String CustStateName;
    @Expose
    private String Province;
    @Expose
    private String City;
    @Expose
    private String Area;
    @Expose
    private String ProvinceName;
    @Expose
    private String CityName;
    @Expose
    private String AreaName;
    @Expose
    private String Address;
    @Expose
    private String Telephone;
    @Expose
    private String Fax;
    @Expose
    private String WebSite;
    @Expose
    private String Email;
    @Expose
    private String CustPublicPool;
    @Expose
    private String CustPublicPoolName;
    @Expose
    private String Source;
    @Expose
    private String SourceName;
    @Expose
    private String Industry;
    @Expose
    private String IndustryName;
    @Expose
    private int FollowUser;
    @Expose
    private String FollowUserName;
    @Expose
    private String FollowUserIcon;
    @Expose
    private String CreateUserID;
    @Expose
    private String CreateUserName;
    @Expose
    private String CreateTimeStr;
    @Expose
    private String UpdateTimeStr;
    @Expose
    private String ID;//客户ID
    @Expose
    private String Remark;

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    @Expose
    private String CloseDealFee;//成交金额
    @Expose
    private String CostFee;//花费金额
    @Expose
    private List<CustomDetailEntity> Contacts= new ArrayList<>();
    @Expose
    private List<CustomDetailEntity> CommonFollowUser= new ArrayList<>();

    public List<CustomDetailEntity> getContacts() {
        return Contacts;
    }

    public List<CustomDetailEntity> getCommonFollowUser() {
        return CommonFollowUser;
    }

    public void setCommonFollowUser(ArrayList<CustomDetailEntity> commonFollowUser) {
        CommonFollowUser = commonFollowUser;
    }

    public String getCustName() {
        return CustName;
    }

    public void setCustName(String custName) {
        CustName = custName;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getCustState() {
        return CustState;
    }

    public void setCustState(String custState) {
        CustState = custState;
    }

    public String getCustStateName() {
        return CustStateName;
    }

    public void setCustStateName(String custStateName) {
        CustStateName = custStateName;
    }

    public String getProvince() {
        return Province;
    }

    public void setProvince(String province) {
        Province = province;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getArea() {
        return Area;
    }

    public void setArea(String area) {
        Area = area;
    }

    public String getProvinceName() {
        return ProvinceName;
    }

    public void setProvinceName(String provinceName) {
        ProvinceName = provinceName;
    }

    public String getCityName() {
        return CityName;
    }

    public void setCityName(String cityName) {
        CityName = cityName;
    }

    public String getAreaName() {
        return AreaName;
    }

    public void setAreaName(String areaName) {
        AreaName = areaName;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getTelephone() {
        return Telephone;
    }

    public void setTelephone(String telephone) {
        Telephone = telephone;
    }

    public String getFax() {
        return Fax;
    }

    public void setFax(String fax) {
        Fax = fax;
    }

    public String getWebSite() {
        return WebSite;
    }

    public void setWebSite(String webSite) {
        WebSite = webSite;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getCustPublicPool() {
        return CustPublicPool;
    }

    public void setCustPublicPool(String custPublicPool) {
        CustPublicPool = custPublicPool;
    }

    public String getCustPublicPoolName() {
        return CustPublicPoolName;
    }

    public void setCustPublicPoolName(String custPublicPoolName) {
        CustPublicPoolName = custPublicPoolName;
    }

    public String getSource() {
        return Source;
    }

    public void setSource(String source) {
        Source = source;
    }

    public String getSourceName() {
        return SourceName;
    }

    public void setSourceName(String sourceName) {
        SourceName = sourceName;
    }

    public String getIndustry() {
        return Industry;
    }

    public void setIndustry(String industry) {
        Industry = industry;
    }

    public String getIndustryName() {
        return IndustryName;
    }

    public void setIndustryName(String industryName) {
        IndustryName = industryName;
    }

    public int getFollowUser() {
        return FollowUser;
    }

    public void setFollowUser(int followUser) {
        FollowUser = followUser;
    }

    public String getFollowUserName() {
        return FollowUserName;
    }

    public void setFollowUserName(String followUserName) {
        FollowUserName = followUserName;
    }

    public String getFollowUserIcon() {
        return FollowUserIcon;
    }

    public void setFollowUserIcon(String followUserIcon) {
        FollowUserIcon = followUserIcon;
    }

    public String getCreateUserID() {
        return CreateUserID;
    }

    public void setCreateUserID(String createUserID) {
        CreateUserID = createUserID;
    }

    public String getCreateUserName() {
        return CreateUserName;
    }

    public void setCreateUserName(String createUserName) {
        CreateUserName = createUserName;
    }

    public String getCreateTimeStr() {
        return CreateTimeStr;
    }

    public void setCreateTimeStr(String createTimeStr) {
        CreateTimeStr = createTimeStr;
    }

    public String getUpdateTimeStr() {
        return UpdateTimeStr;
    }

    public void setUpdateTimeStr(String updateTimeStr) {
        UpdateTimeStr = updateTimeStr;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }




    public String getCloseDealFee() {
        return CloseDealFee;
    }

    public void setCloseDealFee(String closeDealFee) {
        CloseDealFee = closeDealFee;
    }

    public String getCostFee() {
        return CostFee;
    }

    public void setCostFee(String costFee) {
        CostFee = costFee;
    }

    public void setContacts(ArrayList<CustomDetailEntity> contacts) {
        Contacts = contacts;
    }
}
