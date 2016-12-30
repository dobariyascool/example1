package com.arraybit.modal;

import android.os.Parcel;
import android.os.Parcelable;

/// <summary>
/// Model for AdvertiseMaster
/// </summary>
public class AdvertiseMaster implements Parcelable {
    //region Properties
    int AdvertiseMasterId;
    String AdvertiseText;
    String AdvertiseImageNameBytes;
    String AdvertiseImageName;
    String WebsiteURL;
    String AdvertisementType;
    int linktoMemberMasterIdCreatedBy;
    String CreateDateTime;
    int linktoMemberMasterIdUpdatedBy;
    String UpdateDateTime;
    boolean IsEnabled;
    boolean IsDeleted;
    //endregion

    public static final Creator<AdvertiseMaster> CREATOR = new Creator<AdvertiseMaster>() {
        public AdvertiseMaster createFromParcel(Parcel source) {
            AdvertiseMaster objAdvertiseMaster = new AdvertiseMaster();
            objAdvertiseMaster.AdvertiseMasterId = source.readInt();
            objAdvertiseMaster.AdvertiseText = source.readString();
            objAdvertiseMaster.AdvertiseImageNameBytes = source.readString();
            objAdvertiseMaster.AdvertiseImageName = source.readString();
            objAdvertiseMaster.WebsiteURL = source.readString();
            objAdvertiseMaster.AdvertisementType = source.readString();
            objAdvertiseMaster.linktoMemberMasterIdCreatedBy = source.readInt();
            objAdvertiseMaster.CreateDateTime = source.readString();
            objAdvertiseMaster.linktoMemberMasterIdUpdatedBy = source.readInt();
            objAdvertiseMaster.UpdateDateTime = source.readString();
            objAdvertiseMaster.IsEnabled = source.readByte() != 0;
            objAdvertiseMaster.IsDeleted = source.readByte() != 0;

            return objAdvertiseMaster;
        }

        public AdvertiseMaster[] newArray(int size) {
            return new AdvertiseMaster[size];
        }
    };

    public int getAdvertiseMasterId() {
        return this.AdvertiseMasterId;
    }

    public void setAdvertiseMasterId(int advertiseMasterId) {
        this.AdvertiseMasterId = advertiseMasterId;
    }

    public String getAdvertiseText() {
        return this.AdvertiseText;
    }

    public void setAdvertiseText(String advertiseText) {
        this.AdvertiseText = advertiseText;
    }

    public String getAdvertiseImageNameBytes() {
        return this.AdvertiseImageNameBytes;
    }

    public void setAdvertiseImageNameBytes(String advertiseImageNameBytes) {
        this.AdvertiseImageNameBytes = advertiseImageNameBytes;
    }

    public String getAdvertiseImageName() {
        return this.AdvertiseImageName;
    }

    public void setAdvertiseImageName(String advertiseImageName) {
        this.AdvertiseImageName = advertiseImageName;
    }

    public String getWebsiteURL() {
        return this.WebsiteURL;
    }

    public void setWebsiteURL(String websiteURL) {
        this.WebsiteURL = websiteURL;
    }

    public String getAdvertisementType() {
        return this.AdvertisementType;
    }

    public void setAdvertisementType(String advertisementType) {
        this.AdvertisementType = advertisementType;
    }

    public int getlinktoMemberMasterIdCreatedBy() {
        return this.linktoMemberMasterIdCreatedBy;
    }

    public void setlinktoMemberMasterIdCreatedBy(int linktoMemberMasterIdCreatedBy) {
        this.linktoMemberMasterIdCreatedBy = linktoMemberMasterIdCreatedBy;
    }

    public String getCreateDateTime() {
        return this.CreateDateTime;
    }

    public void setCreateDateTime(String createDateTime) {
        this.CreateDateTime = createDateTime;
    }

    public int getlinktoMemberMasterIdUpdatedBy() {
        return this.linktoMemberMasterIdUpdatedBy;
    }

    public void setlinktoMemberMasterIdUpdatedBy(int linktoMemberMasterIdUpdatedBy) {
        this.linktoMemberMasterIdUpdatedBy = linktoMemberMasterIdUpdatedBy;
    }

    public String getUpdateDateTime() {
        return this.UpdateDateTime;
    }

    public void setUpdateDateTime(String updateDateTime) {
        this.UpdateDateTime = updateDateTime;
    }

    public boolean getIsEnabled() {
        return this.IsEnabled;
    }

    public void setIsEnabled(boolean isEnabled) {
        this.IsEnabled = isEnabled;
    }

    public boolean getIsDeleted() {
        return this.IsDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.IsDeleted = isDeleted;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(AdvertiseMasterId);
        parcel.writeString(AdvertiseText);
        parcel.writeString(AdvertiseImageNameBytes);
        parcel.writeString(AdvertiseImageName);
        parcel.writeString(WebsiteURL);
        parcel.writeString(AdvertisementType);
        parcel.writeInt(linktoMemberMasterIdCreatedBy);
        parcel.writeString(CreateDateTime);
        parcel.writeInt(linktoMemberMasterIdUpdatedBy);
        parcel.writeString(UpdateDateTime);
        parcel.writeByte((byte) (IsEnabled ? 1 : 0));
        parcel.writeByte((byte) (IsDeleted ? 1 : 0));
    }
}
