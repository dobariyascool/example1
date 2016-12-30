package com.arraybit.modal;

import android.os.Parcel;
import android.os.Parcelable;

/// <summary>
/// Model for NotificationMaster
/// </summary>
public class NotificationMaster implements Parcelable {
    //region Properties
    int NotificationMasterId;
    String NotificationTitle;
    String NotificationText;
    String NotificationImageNameBytes;
    String NotificationImageName;
    String NotificationDateTime;
    String CreateDateTime;
    int linktoMemberMasterIdCreatedBy;
    String UpdateDateTime;
    int linktoMemberMasterIdUpdatedBy;
    boolean IsDeleted;

    //endregion

    public static final Creator<NotificationMaster> CREATOR = new Creator<NotificationMaster>() {
        public NotificationMaster createFromParcel(Parcel source) {
            NotificationMaster objNotificationMaster = new NotificationMaster();
            objNotificationMaster.NotificationMasterId = source.readInt();
            objNotificationMaster.NotificationTitle = source.readString();
            objNotificationMaster.NotificationText = source.readString();
            objNotificationMaster.NotificationImageNameBytes = source.readString();
            objNotificationMaster.NotificationImageName = source.readString();
            objNotificationMaster.NotificationDateTime = source.readString();
            objNotificationMaster.CreateDateTime = source.readString();
            objNotificationMaster.linktoMemberMasterIdCreatedBy = source.readInt();
            objNotificationMaster.UpdateDateTime = source.readString();
            objNotificationMaster.linktoMemberMasterIdUpdatedBy = source.readInt();
            objNotificationMaster.IsDeleted = source.readByte() != 0;

            return objNotificationMaster;
        }

        public NotificationMaster[] newArray(int size) {
            return new NotificationMaster[size];
        }
    };


    public int getNotificationMasterId() {
        return this.NotificationMasterId;
    }

    public void setNotificationMasterId(int notificationMasterId) {
        this.NotificationMasterId = notificationMasterId;
    }

    public String getNotificationTitle() {
        return this.NotificationTitle;
    }

    public void setNotificationTitle(String notificationTitle) {
        this.NotificationTitle = notificationTitle;
    }

    public String getNotificationText() {
        return this.NotificationText;
    }

    public void setNotificationText(String notificationText) {
        this.NotificationText = notificationText;
    }

    public String getNotificationImageNameBytes() {
        return this.NotificationImageNameBytes;
    }

    public void setNotificationImageNameBytes(String notificationImageNameBytes) {
        this.NotificationImageNameBytes = notificationImageNameBytes;
    }

    public String getNotificationImageName() {
        return this.NotificationImageName;
    }

    public void setNotificationImageName(String notificationImageName) {
        this.NotificationImageName = notificationImageName;
    }

    public String getNotificationDateTime() {
        return this.NotificationDateTime;
    }

    public void setNotificationDateTime(String notificationDateTime) {
        this.NotificationDateTime = notificationDateTime;
    }

    public String getCreateDateTime() {
        return this.CreateDateTime;
    }

    public void setCreateDateTime(String createDateTime) {
        this.CreateDateTime = createDateTime;
    }

    public int getlinktoMemberMasterIdCreatedBy() {
        return this.linktoMemberMasterIdCreatedBy;
    }

    public void setlinktoMemberMasterIdCreatedBy(int linktoMemberMasterIdCreatedBy) {
        this.linktoMemberMasterIdCreatedBy = linktoMemberMasterIdCreatedBy;
    }

    public String getUpdateDateTime() {
        return this.UpdateDateTime;
    }

    public void setUpdateDateTime(String updateDateTime) {
        this.UpdateDateTime = updateDateTime;
    }

    public int getlinktoMemberMasterIdUpdatedBy() {
        return this.linktoMemberMasterIdUpdatedBy;
    }

    public void setlinktoMemberMasterIdUpdatedBy(int linktoMemberMasterIdUpdatedBy) {
        this.linktoMemberMasterIdUpdatedBy = linktoMemberMasterIdUpdatedBy;
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
        parcel.writeInt(NotificationMasterId);
        parcel.writeString(NotificationTitle);
        parcel.writeString(NotificationText);
        parcel.writeString(NotificationImageNameBytes);
        parcel.writeString(NotificationImageName);
        parcel.writeString(NotificationDateTime);
        parcel.writeString(CreateDateTime);
        parcel.writeInt(linktoMemberMasterIdCreatedBy);
        parcel.writeString(UpdateDateTime);
        parcel.writeInt(linktoMemberMasterIdUpdatedBy);
        parcel.writeByte((byte) (IsDeleted ? 1 : 0));

    }
}
