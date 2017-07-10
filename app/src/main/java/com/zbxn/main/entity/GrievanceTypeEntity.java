package com.zbxn.main.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

/**
 * Created by Administrator on 2016/9/30.
 */
public class GrievanceTypeEntity implements Parcelable {

    @Expose
    private String AppealTypeName;
    @Expose
    private int AppealType;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.AppealTypeName);
        dest.writeInt(this.AppealType);
    }

    protected GrievanceTypeEntity(Parcel in) {
        this.AppealTypeName = in.readString();
        this.AppealType = in.readInt();
    }

    public static final Creator<GrievanceTypeEntity> CREATOR = new Creator<GrievanceTypeEntity>() {
        @Override
        public GrievanceTypeEntity createFromParcel(Parcel source) {
            return new GrievanceTypeEntity(source);
        }

        @Override
        public GrievanceTypeEntity[] newArray(int size) {
            return new GrievanceTypeEntity[size];
        }
    };

    public String getAppealTypeName() {
        return AppealTypeName;
    }

    public void setAppealTypeName(String AppealTypeName) {
        this.AppealTypeName = AppealTypeName;
    }

    public int getAppealtype() {
        return AppealType;
    }

    public void setAppealtype(int AppealType) {
        this.AppealType = AppealType;
    }
}
