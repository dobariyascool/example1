package com.arraybit.modal;

import android.os.Parcel;
import android.os.Parcelable;

/// <summary>
/// Model for MemberRelativesTran
/// </summary>
public class MemberRelativesTran implements Parcelable {
    //region Properties

    int MemberRelativesTranId;
    int linktoMemberMasterId;
    String RelativeName;
    String ImageNameBytes;
    String ImageName;
    String Gender;
    String BirthDate;
    String Relation;

    //endregion

    public static final Creator<MemberRelativesTran> CREATOR = new Creator<MemberRelativesTran>() {
        public MemberRelativesTran createFromParcel(Parcel source) {
            MemberRelativesTran objMemberRelativesTran = new MemberRelativesTran();
            objMemberRelativesTran.MemberRelativesTranId = source.readInt();
            objMemberRelativesTran.linktoMemberMasterId = source.readInt();
            objMemberRelativesTran.RelativeName = source.readString();
            objMemberRelativesTran.ImageNameBytes = source.readString();
            objMemberRelativesTran.ImageName = source.readString();
            objMemberRelativesTran.Gender = source.readString();
            objMemberRelativesTran.BirthDate = source.readString();
            objMemberRelativesTran.Relation = source.readString();

            return objMemberRelativesTran;
        }

        public MemberRelativesTran[] newArray(int size) {
            return new MemberRelativesTran[size];
        }
    };

    public int getMemberRelativesTranId() {
        return this.MemberRelativesTranId;
    }

    public void setMemberRelativesTranId(int memberRelativesTranId) {
        this.MemberRelativesTranId = memberRelativesTranId;
    }

    public int getlinktoMemberMasterId() {
        return this.linktoMemberMasterId;
    }

    public void setlinktoMemberMasterId(int linktoMemberMasterId) {
        this.linktoMemberMasterId = linktoMemberMasterId;
    }

    public String getRelativeName() {
        return this.RelativeName;
    }

    public void setRelativeName(String relativeName) {
        this.RelativeName = relativeName;
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

    public String getGender() {
        return this.Gender;
    }

    public void setGender(String gender) {
        this.Gender = gender;
    }

    public String getBirthDate() {
        return this.BirthDate;
    }

    public void setBirthDate(String birthDate) {
        this.BirthDate = birthDate;
    }

    public String getRelation() {
        return this.Relation;
    }

    public void setRelation(String relation) {
        this.Relation = relation;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(MemberRelativesTranId);
        parcel.writeInt(linktoMemberMasterId);
        parcel.writeString(RelativeName);
        parcel.writeString(ImageNameBytes);
        parcel.writeString(ImageName);
        parcel.writeString(Gender);
        parcel.writeString(BirthDate);
        parcel.writeString(Relation);

    }
}
