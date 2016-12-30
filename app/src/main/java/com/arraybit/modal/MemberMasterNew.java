package com.arraybit.modal;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/// <summary>
/// Model for MemberMaster
/// </summary>
public class MemberMasterNew implements Parcelable {

    //region Properties
    int MemberMasterId;
    String MemberName;
    String ImageNameBytes;
    String ImageName;
    String Phone1;
    String Phone2;
    String Email;
    String Password;
    String MemberType;
    String LastLoginDateTime;
    String Gender;
    String Qualification;
    String BloodGroup;
    String Profession;
    String BirthDate;
    String AnniversaryDate;
    String HomeCountry;
    String HomeState;
    String HomeCity;
    String HomeArea;
    String HomeNumberStreet;
    String HomeNearBy;
    String HomeZipCode;
    String HomePhone;
    String OfficeCountry;
    String OfficeState;
    String OfficeCity;
    String OfficeArea;
    String OfficeNumberStreet;
    String OfficeNearBy;
    String OfficeZipCode;
    String OfficePhone;
    boolean IsApproved;
    boolean IsAdminNotificationSent;
    boolean IsMemberNotificationSent;
    String FCMToken;
    int linktoMemberMasterIdApprovedBy;
    String ApprovedDateTime;
    int linktoMemberMasterIdUpdatedBy;
    String UpdateDateTime;
    //Extra
    boolean IsMarried;
//    ArrayList<MemberRelativesTran> lstMemberRelativeTran;

    public static final Creator<MemberMasterNew> CREATOR = new Creator<MemberMasterNew>() {
        public MemberMasterNew createFromParcel(Parcel source) {
            MemberMasterNew objMemberMaster = new MemberMasterNew();
            objMemberMaster.MemberMasterId = source.readInt();
            objMemberMaster.MemberName = source.readString();
            objMemberMaster.ImageNameBytes = source.readString();
            objMemberMaster.ImageName = source.readString();
            objMemberMaster.Phone1 = source.readString();
            objMemberMaster.Phone2 = source.readString();
            objMemberMaster.Email = source.readString();
            objMemberMaster.Password = source.readString();
            objMemberMaster.MemberType = source.readString();
            objMemberMaster.LastLoginDateTime = source.readString();
            objMemberMaster.Gender = source.readString();
            objMemberMaster.Qualification = source.readString();
            objMemberMaster.BloodGroup = source.readString();
            objMemberMaster.Profession = source.readString();
            objMemberMaster.BirthDate = source.readString();
            objMemberMaster.AnniversaryDate = source.readString();
            objMemberMaster.HomeCountry = source.readString();
            objMemberMaster.HomeState = source.readString();
            objMemberMaster.HomeCity = source.readString();
            objMemberMaster.HomeArea = source.readString();
            objMemberMaster.HomeNumberStreet = source.readString();
            objMemberMaster.HomeNearBy = source.readString();
            objMemberMaster.HomeZipCode = source.readString();
            objMemberMaster.HomePhone = source.readString();
            objMemberMaster.OfficeCountry = source.readString();
            objMemberMaster.OfficeState = source.readString();
            objMemberMaster.OfficeCity = source.readString();
            objMemberMaster.OfficeArea = source.readString();
            objMemberMaster.OfficeNumberStreet = source.readString();
            objMemberMaster.OfficeNearBy = source.readString();
            objMemberMaster.OfficeZipCode = source.readString();
            objMemberMaster.OfficePhone = source.readString();
            objMemberMaster.IsApproved = source.readByte() != 0;
            objMemberMaster.IsAdminNotificationSent = source.readByte() != 0;
            objMemberMaster.IsMemberNotificationSent = source.readByte() != 0;
            objMemberMaster.FCMToken = source.readString();
            objMemberMaster.linktoMemberMasterIdApprovedBy = source.readInt();
            objMemberMaster.ApprovedDateTime = source.readString();
            objMemberMaster.linktoMemberMasterIdUpdatedBy = source.readInt();
            objMemberMaster.UpdateDateTime = source.readString();
            objMemberMaster.IsMarried = source.readByte() != 0;
//            objMemberMaster.lstMemberRelativeTran = new ArrayList<MemberRelativesTran>();
//            in.readList(products,null);
//             source.readList(objMemberMaster.lstMemberRelativeTran,null);

            return objMemberMaster;
        }

        public MemberMasterNew[] newArray(int size) {
            return new MemberMasterNew[size];
        }
    };


    public int getMemberMasterId() {
        return this.MemberMasterId;
    }

    public void setMemberMasterId(int memberMasterId) {
        this.MemberMasterId = memberMasterId;
    }

    public String getMemberName() {
        return this.MemberName;
    }

    public void setMemberName(String memberName) {
        this.MemberName = memberName;
    }

    public String getImageNameBytes() {
        return this.ImageNameBytes;
    }

    public void setImageNameBytes(String imageNameBytes) {
        this.ImageNameBytes = imageNameBytes;
    }

    public String getImageName() {
        return this.ImageName;
    }

    public void setImageName(String imageName) {
        this.ImageName = imageName;
    }

    public String getPhone1() {
        return this.Phone1;
    }

    public void setPhone1(String phone1) {
        this.Phone1 = phone1;
    }

    public String getPhone2() {
        return this.Phone2;
    }

    public void setPhone2(String phone2) {
        this.Phone2 = phone2;
    }

    public String getEmail() {
        return this.Email;
    }

    public void setEmail(String email) {
        this.Email = email;
    }

    public String getPassword() {
        return this.Password;
    }

    public void setPassword(String password) {
        this.Password = password;
    }

    public String getMemberType() {
        return this.MemberType;
    }

    public void setMemberType(String memberType) {
        this.MemberType = memberType;
    }

    public String getLastLoginDateTime() {
        return this.LastLoginDateTime;
    }

    public void setLastLoginDateTime(String lastLoginDateTime) {
        this.LastLoginDateTime = lastLoginDateTime;
    }

    public String getGender() {
        return this.Gender;
    }

    public void setGender(String gender) {
        this.Gender = gender;
    }

    public String getQualification() {
        return this.Qualification;
    }

    public void setQualification(String qualification) {
        this.Qualification = qualification;
    }

    public String getBloodGroup() {
        return this.BloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.BloodGroup = bloodGroup;
    }

    public String getProfession() {
        return this.Profession;
    }

    public void setProfession(String profession) {
        this.Profession = profession;
    }

    public String getBirthDate() {
        return this.BirthDate;
    }

    public void setBirthDate(String birthDate) {
        this.BirthDate = birthDate;
    }

    public String getAnniversaryDate() {
        return this.AnniversaryDate;
    }

    public void setAnniversaryDate(String anniversaryDate) {
        this.AnniversaryDate = anniversaryDate;
    }

    public String getHomeCountry() {
        return this.HomeCountry;
    }

    public void setHomeCountry(String homeCountry) {
        this.HomeCountry = homeCountry;
    }

    public String getHomeState() {
        return this.HomeState;
    }

    public void setHomeState(String homeState) {
        this.HomeState = homeState;
    }

    public String getHomeCity() {
        return this.HomeCity;
    }

    public void setHomeCity(String homeCity) {
        this.HomeCity = homeCity;
    }

    public String getHomeArea() {
        return this.HomeArea;
    }

    public void setHomeArea(String homeArea) {
        this.HomeArea = homeArea;
    }

    public String getHomeNumberStreet() {
        return this.HomeNumberStreet;
    }

    public void setHomeNumberStreet(String homeNumberStreet) {
        this.HomeNumberStreet = homeNumberStreet;
    }

    public String getHomeNearBy() {
        return this.HomeNearBy;
    }

    public void setHomeNearBy(String homeNearBy) {
        this.HomeNearBy = homeNearBy;
    }

    public String getHomePhone() {
        return this.HomePhone;
    }

    public void setHomePhone(String homePhone) {
        this.HomePhone = homePhone;
    }

    public String getOfficeCountry() {
        return this.OfficeCountry;
    }

    public void setOfficeCountry(String officeCountry) {
        this.OfficeCountry = officeCountry;
    }

    public String getOfficeState() {
        return this.OfficeState;
    }

    public void setOfficeState(String officeState) {
        this.OfficeState = officeState;
    }

    public String getOfficeCity() {
        return this.OfficeCity;
    }

    public void setOfficeCity(String officeCity) {
        this.OfficeCity = officeCity;
    }

    public String getOfficeArea() {
        return this.OfficeArea;
    }

    public void setOfficeArea(String officeArea) {
        this.OfficeArea = officeArea;
    }

    public String getOfficeNumberStreet() {
        return this.OfficeNumberStreet;
    }

    public void setOfficeNumberStreet(String officeNumberStreet) {
        this.OfficeNumberStreet = officeNumberStreet;
    }

    public String getOfficeNearBy() {
        return this.OfficeNearBy;
    }

    public void setOfficeNearBy(String officeNearBy) {
        this.OfficeNearBy = officeNearBy;
    }

    public String getOfficePhone() {
        return this.OfficePhone;
    }

    public void setOfficePhone(String officePhone) {
        this.OfficePhone = officePhone;
    }

    public boolean getIsApproved() {
        return this.IsApproved;
    }

    public void setIsApproved(boolean isApproved) {
        this.IsApproved = isApproved;
    }

    public boolean getIsAdminNotificationSent() {
        return this.IsAdminNotificationSent;
    }

    public void setIsAdminNotificationSent(boolean isAdminNotificationSent) {
        this.IsAdminNotificationSent = isAdminNotificationSent;
    }

    public boolean getIsMemberNotificationSent() {
        return this.IsMemberNotificationSent;
    }

    public void setIsMemberNotificationSent(boolean isMemberNotificationSent) {
        this.IsMemberNotificationSent = isMemberNotificationSent;
    }

    public String getFCMToken() {
        return this.FCMToken;
    }

    public void setFCMToken(String fCMToken) {
        this.FCMToken = fCMToken;
    }

    public int getlinktoMemberMasterIdApprovedBy() {
        return this.linktoMemberMasterIdApprovedBy;
    }

    public void setlinktoMemberMasterIdApprovedBy(int linktoMemberMasterIdApprovedBy) {
        this.linktoMemberMasterIdApprovedBy = linktoMemberMasterIdApprovedBy;
    }

    public String getApprovedDateTime() {
        return this.ApprovedDateTime;
    }

    public void setApprovedDateTime(String approvedDateTime) {
        this.ApprovedDateTime = approvedDateTime;
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

    public boolean getIsMarried() {
        return IsMarried;
    }

    public void setIsMarried(boolean married) {
        IsMarried = married;
    }

//    public ArrayList<MemberRelativesTran> getLstMemberRelativeTran() {
//        return lstMemberRelativeTran;
//    }
//
//    public void setLstMemberRelativeTran(ArrayList<MemberRelativesTran> lstMemberRelativeTran) {
//        this.lstMemberRelativeTran = lstMemberRelativeTran;
//    }

    public String getHomeZipCode() {
        return HomeZipCode;
    }

    public void setHomeZipCode(String homeZipCode) {
        HomeZipCode = homeZipCode;
    }

    public String getOfficeZipCode() {
        return OfficeZipCode;
    }

    public void setOfficeZipCode(String officeZipCode) {
        OfficeZipCode = officeZipCode;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(MemberMasterId);
        parcel.writeString(MemberName);
        parcel.writeString(ImageNameBytes);
        parcel.writeString(ImageName);
        parcel.writeString(Phone1);
        parcel.writeString(Phone2);
        parcel.writeString(Email);
        parcel.writeString(Password);
        parcel.writeString(MemberType);
        parcel.writeString(LastLoginDateTime);
        parcel.writeString(Gender);
        parcel.writeString(Qualification);
        parcel.writeString(BloodGroup);
        parcel.writeString(Profession);
        parcel.writeString(BirthDate);
        parcel.writeString(AnniversaryDate);
        parcel.writeString(HomeCountry);
        parcel.writeString(HomeState);
        parcel.writeString(HomeCity);
        parcel.writeString(HomeArea);
        parcel.writeString(HomeNumberStreet);
        parcel.writeString(HomeNearBy);
        parcel.writeString(HomeZipCode);
        parcel.writeString(HomePhone);
        parcel.writeString(OfficeCountry);
        parcel.writeString(OfficeState);
        parcel.writeString(OfficeCity);
        parcel.writeString(OfficeArea);
        parcel.writeString(OfficeNumberStreet);
        parcel.writeString(OfficeNearBy);
        parcel.writeString(OfficeZipCode);
        parcel.writeString(OfficePhone);
        parcel.writeByte((byte) (IsApproved ? 1 : 0));
        parcel.writeByte((byte) (IsAdminNotificationSent ? 1 : 0));
        parcel.writeByte((byte) (IsMemberNotificationSent ? 1 : 0));
        parcel.writeString(FCMToken);
        parcel.writeInt(linktoMemberMasterIdApprovedBy);
        parcel.writeString(ApprovedDateTime);
        parcel.writeInt(linktoMemberMasterIdUpdatedBy);
        parcel.writeString(UpdateDateTime);
        parcel.writeByte((byte) (IsMarried ? 1 : 0));
//        parcel.writeList(lstMemberRelativeTran);

    }
}
