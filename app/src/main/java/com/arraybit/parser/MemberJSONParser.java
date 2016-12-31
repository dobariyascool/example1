package com.arraybit.parser;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.modal.MemberMaster;
import com.arraybit.modal.MemberMasterNew;
import com.arraybit.modal.MemberRelativesTran;
import com.arraybit.mym.RegistrationActivity;
import com.arraybit.mym.RegistrationDetailActivity;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MemberJSONParser {

    public String InsertMemberMasterDetail = "InsertMemberMasterDetail";
    public String UpdateMemberMasterAdmin = "UpdateMemberMasterAdmin";
    public String UpdateMemberMasterPassword = "UpdateMemberMasterPassword";
    public String UpdateMemberMasterLogOut = "UpdateMemberMasterLogOut";
    public String UpdateMemberMasterPersonalDetail = "UpdateMemberMasterPersonalDetail";
    public String UpdateMemberMasterContactDetail = "UpdateMemberMasterContactDetail";
    public String UpdateMemberMasterIsApproved = "UpdateMemberMasterIsApproved";
    public String UpdateMemberMasterByMemberMasterId = "UpdateMemberMasterByMemberMasterId";
    public String UpdateMemberMasterImageByMemberMasterId = "UpdateMemberMasterImageByMemberMasterId";
    public String SelectMemberMaster = "SelectMemberMaster";
    public String SelectMemberByMemberMasterId = "SelectMemberByMemberMasterId";
    public String DeleteMemberMasterByMemberMasterId = "DeleteMemberMasterByMemberMasterId";

    SimpleDateFormat sdfControlDateFormat = new SimpleDateFormat(Globals.DateFormat, Locale.US);
    Date dt = null;
    SimpleDateFormat sdfDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
    SimpleDateFormat sdfDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    MemberRequestListener objMemberRequestListener;
    MembersListRequestListener objMemberListRequestListener;

    private String SelectAllMemberMasterPageWise = "SelectAllMemberMasterPageWise";
    private String SelectAllMemberMasterFilterPageWise = "SelectAllMemberMasterFilterPageWise";
    private String SelectAllNewMemberMasterPageWise = "SelectAllNewMemberMasterPageWise";
    private String ForgotPasswordMemberMaster = "ForgotPasswordMemberMaster";

    //region class Methods
    private MemberMaster SetClassPropertiesFromJSONObject(JSONObject jsonObject) {
        MemberMaster objMemberMaster = null;
        try {
            if (jsonObject != null) {
                objMemberMaster = new MemberMaster();
                objMemberMaster.setMemberMasterId(jsonObject.getInt("MemberMasterId"));
                objMemberMaster.setMemberName(jsonObject.getString("MemberName"));
                objMemberMaster.setPhone1(jsonObject.getString("Phone1"));
                if (jsonObject.getString("Phone2") != null && !jsonObject.getString("Phone2").equals("")) {
                    objMemberMaster.setPhone2(jsonObject.getString("Phone2"));
                }
                if (jsonObject.getString("ImageName") != null && !jsonObject.getString("ImageName").equals("") && !jsonObject.getString("ImageName").equals("null")) {
                    objMemberMaster.setImageName(jsonObject.getString("ImageName"));
                }
                objMemberMaster.setEmail(jsonObject.getString("Email"));
                objMemberMaster.setPassword(jsonObject.getString("Password"));
                objMemberMaster.setMemberType(jsonObject.getString("MemberType"));
                objMemberMaster.setGender(jsonObject.getString("Gender"));
                if (jsonObject.getString("Qualification") != null && !jsonObject.getString("Qualification").equals("")) {
                    objMemberMaster.setQualification(jsonObject.getString("Qualification"));
                }
                if (jsonObject.getString("BloodGroup") != null && !jsonObject.getString("BloodGroup").equals("")) {
                    objMemberMaster.setBloodGroup(jsonObject.getString("BloodGroup"));
                }
                if (jsonObject.getString("Profession") != null && !jsonObject.getString("Profession").equals("")) {
                    objMemberMaster.setProfession(jsonObject.getString("Profession"));
                }
                if (jsonObject.getString("BirthDate") != null && !jsonObject.getString("BirthDate").equals("null")) {
                    dt = sdfDateFormat.parse(jsonObject.getString("BirthDate"));
                    objMemberMaster.setBirthDate(sdfControlDateFormat.format(dt));
                }
                if (jsonObject.getString("AnniversaryDate") != null && !jsonObject.getString("AnniversaryDate").equals("null")) {
                    dt = sdfDateFormat.parse(jsonObject.getString("AnniversaryDate"));
                    objMemberMaster.setAnniversaryDate(sdfControlDateFormat.format(dt));
                }
                if (jsonObject.getString("HomeCountry") != null && !jsonObject.getString("HomeCountry").equals("")) {
                    objMemberMaster.setHomeCountry(jsonObject.getString("HomeCountry"));
                }
                if (jsonObject.getString("HomeState") != null && !jsonObject.getString("HomeState").equals("")) {
                    objMemberMaster.setHomeState(jsonObject.getString("HomeState"));
                }
                if (jsonObject.getString("HomeCity") != null && !jsonObject.getString("HomeCity").equals("")) {
                    objMemberMaster.setHomeCity(jsonObject.getString("HomeCity"));
                }
                if (jsonObject.getString("HomeArea") != null && !jsonObject.getString("HomeArea").equals("")) {
                    objMemberMaster.setHomeArea(jsonObject.getString("HomeArea"));
                }
                if (jsonObject.getString("HomeNumberStreet") != null && !jsonObject.getString("HomeNumberStreet").equals("")) {
                    objMemberMaster.setHomeNumberStreet(jsonObject.getString("HomeNumberStreet"));
                }
                if (jsonObject.getString("HomeZipCode") != null && !jsonObject.getString("HomeZipCode").equals("")) {
                    objMemberMaster.setHomeZipCode(jsonObject.getString("HomeZipCode"));
                }
                if (jsonObject.getString("HomeNearBy") != null && !jsonObject.getString("HomeNearBy").equals("")) {
                    objMemberMaster.setHomeNearBy(jsonObject.getString("HomeNearBy"));
                }
                if (jsonObject.getString("HomePhone") != null && !jsonObject.getString("HomePhone").equals("")) {
                    objMemberMaster.setHomePhone(jsonObject.getString("HomePhone"));
                }
                if (jsonObject.getString("OfficeCountry") != null && !jsonObject.getString("OfficeCountry").equals("")) {
                    objMemberMaster.setOfficeCountry(jsonObject.getString("OfficeCountry"));
                }
                if (jsonObject.getString("OfficeState") != null && !jsonObject.getString("OfficeState").equals("")) {
                    objMemberMaster.setOfficeState(jsonObject.getString("OfficeState"));
                }
                if (jsonObject.getString("OfficeCity") != null && !jsonObject.getString("OfficeCity").equals("")) {
                    objMemberMaster.setOfficeCity(jsonObject.getString("OfficeCity"));
                }
                if (jsonObject.getString("OfficeArea") != null && !jsonObject.getString("OfficeArea").equals("")) {
                    objMemberMaster.setOfficeArea(jsonObject.getString("OfficeArea"));
                }
                if (jsonObject.getString("OfficeNumberStreet") != null && !jsonObject.getString("OfficeNumberStreet").equals("")) {
                    objMemberMaster.setOfficeNumberStreet(jsonObject.getString("OfficeNumberStreet"));
                }
                if (jsonObject.getString("OfficeNearBy") != null && !jsonObject.getString("OfficeNearBy").equals("")) {
                    objMemberMaster.setOfficeNearBy(jsonObject.getString("OfficeNearBy"));
                }
                if (jsonObject.getString("OfficeZipCode") != null && !jsonObject.getString("OfficeZipCode").equals("")) {
                    objMemberMaster.setOfficeZipCode(jsonObject.getString("OfficeZipCode"));
                }
                if (jsonObject.getString("OfficePhone") != null && !jsonObject.getString("OfficePhone").equals("")) {
                    objMemberMaster.setOfficePhone(jsonObject.getString("OfficePhone"));
                }
                if (!jsonObject.getString("IsApproved").equals("null")) {
                    objMemberMaster.setIsApproved(jsonObject.getBoolean("IsApproved"));
                }
                if (!jsonObject.getString("IsAdminNotificationSent").equals("null")) {
                    objMemberMaster.setIsAdminNotificationSent(jsonObject.getBoolean("IsAdminNotificationSent"));
                }
                if (!jsonObject.getString("IsMemberNotificationSent").equals("null")) {
                    objMemberMaster.setIsMemberNotificationSent(jsonObject.getBoolean("IsMemberNotificationSent"));
                }
                if (jsonObject.getString("FCMToken") != null && !jsonObject.getString("FCMToken").equals("")) {
                    objMemberMaster.setFCMToken(jsonObject.getString("FCMToken"));
                }
                if (!jsonObject.getString("linktoMemberMasterIdApprovedBy").equals("null")) {
                    objMemberMaster.setlinktoMemberMasterIdApprovedBy(jsonObject.getInt("linktoMemberMasterIdApprovedBy"));
                }
                if (jsonObject.getString("ApprovedDateTime") != null && !jsonObject.getString("ApprovedDateTime").equals("null")) {
                    dt = sdfDateTimeFormat.parse(jsonObject.getString("ApprovedDateTime"));
                    objMemberMaster.setApprovedDateTime(sdfControlDateFormat.format(dt));
                }
                if (!jsonObject.getString("linktoMemberMasterIdUpdatedBy").equals("null")) {
                    objMemberMaster.setlinktoMemberMasterIdUpdatedBy(jsonObject.getInt("linktoMemberMasterIdUpdatedBy"));
                }
                if (jsonObject.getString("UpdateDateTime") != null && !jsonObject.getString("UpdateDateTime").equals("null")) {
                    dt = sdfDateTimeFormat.parse(jsonObject.getString("UpdateDateTime"));
                    objMemberMaster.setUpdateDateTime(sdfControlDateFormat.format(dt));
                }
                if (jsonObject.getString("lstMemberRelativeTran") != null && !jsonObject.getString("lstMemberRelativeTran").equals("null")) {
                    objMemberMaster.setLstMemberRelativeTran(new MemberRelativesJSONParser().SetListPropertiesFromJSONArray(jsonObject.getJSONArray("lstMemberRelativeTran")));
                }

            }
            return objMemberMaster;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private ArrayList<MemberMaster> SetListPropertiesFromJSONArray(JSONArray jsonArray) {
        ArrayList<MemberMaster> lstMemberMaster = new ArrayList<>();
        MemberMaster objMemberMaster;
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                objMemberMaster = new MemberMaster();
                objMemberMaster.setMemberMasterId(jsonArray.getJSONObject(i).getInt("MemberMasterId"));
                objMemberMaster.setMemberName(jsonArray.getJSONObject(i).getString("MemberName"));
                objMemberMaster.setPhone1(jsonArray.getJSONObject(i).getString("Phone1"));
                if (jsonArray.getJSONObject(i).getString("Phone2") != null && !jsonArray.getJSONObject(i).getString("Phone2").equals("")) {
                    objMemberMaster.setPhone2(jsonArray.getJSONObject(i).getString("Phone2"));
                }
                if (jsonArray.getJSONObject(i).getString("ImageName") != null && !jsonArray.getJSONObject(i).getString("ImageName").equals("") && !jsonArray.getJSONObject(i).getString("ImageName").equals("null")) {
                    objMemberMaster.setImageName(jsonArray.getJSONObject(i).getString("ImageName"));
                }
                objMemberMaster.setEmail(jsonArray.getJSONObject(i).getString("Email"));
                objMemberMaster.setPassword(jsonArray.getJSONObject(i).getString("Password"));
                objMemberMaster.setMemberType(jsonArray.getJSONObject(i).getString("MemberType"));
                objMemberMaster.setGender(jsonArray.getJSONObject(i).getString("Gender"));
                if (jsonArray.getJSONObject(i).getString("Qualification") != null && !jsonArray.getJSONObject(i).getString("Qualification").equals("")) {
                    objMemberMaster.setQualification(jsonArray.getJSONObject(i).getString("Qualification"));
                }
                if (jsonArray.getJSONObject(i).getString("BloodGroup") != null && !jsonArray.getJSONObject(i).getString("BloodGroup").equals("")) {
                    objMemberMaster.setBloodGroup(jsonArray.getJSONObject(i).getString("BloodGroup"));
                }
                if (jsonArray.getJSONObject(i).getString("Profession") != null && !jsonArray.getJSONObject(i).getString("Profession").equals("")) {
                    objMemberMaster.setProfession(jsonArray.getJSONObject(i).getString("Profession"));
                }
                if (jsonArray.getJSONObject(i).getString("BirthDate") != null && !jsonArray.getJSONObject(i).getString("BirthDate").equals("null")) {
                    dt = sdfDateFormat.parse(jsonArray.getJSONObject(i).getString("BirthDate"));
                    objMemberMaster.setBirthDate(sdfControlDateFormat.format(dt));
                }
                if (jsonArray.getJSONObject(i).getString("AnniversaryDate") != null && !jsonArray.getJSONObject(i).getString("AnniversaryDate").equals("null")) {
                    dt = sdfDateFormat.parse(jsonArray.getJSONObject(i).getString("AnniversaryDate"));
                    objMemberMaster.setAnniversaryDate(sdfControlDateFormat.format(dt));
                }
                if (jsonArray.getJSONObject(i).getString("HomeCountry") != null && !jsonArray.getJSONObject(i).getString("HomeCountry").equals("")) {
                    objMemberMaster.setHomeCountry(jsonArray.getJSONObject(i).getString("HomeCountry"));
                }
                if (jsonArray.getJSONObject(i).getString("HomeState") != null && !jsonArray.getJSONObject(i).getString("HomeState").equals("")) {
                    objMemberMaster.setHomeState(jsonArray.getJSONObject(i).getString("HomeState"));
                }
                if (jsonArray.getJSONObject(i).getString("HomeCity") != null && !jsonArray.getJSONObject(i).getString("HomeCity").equals("")) {
                    objMemberMaster.setHomeCity(jsonArray.getJSONObject(i).getString("HomeCity"));
                }
                if (jsonArray.getJSONObject(i).getString("HomeArea") != null && !jsonArray.getJSONObject(i).getString("HomeArea").equals("")) {
                    objMemberMaster.setHomeArea(jsonArray.getJSONObject(i).getString("HomeArea"));
                }
                if (jsonArray.getJSONObject(i).getString("HomeNumberStreet") != null && !jsonArray.getJSONObject(i).getString("HomeNumberStreet").equals("")) {
                    objMemberMaster.setHomeNumberStreet(jsonArray.getJSONObject(i).getString("HomeNumberStreet"));
                }
                if (jsonArray.getJSONObject(i).getString("HomeNearBy") != null && !jsonArray.getJSONObject(i).getString("HomeNearBy").equals("")) {
                    objMemberMaster.setHomeNearBy(jsonArray.getJSONObject(i).getString("HomeNearBy"));
                }
                if (jsonArray.getJSONObject(i).getString("HomeZipCode") != null && !jsonArray.getJSONObject(i).getString("HomeZipCode").equals("")) {
                    objMemberMaster.setHomeZipCode(jsonArray.getJSONObject(i).getString("HomeZipCode"));
                }
                if (jsonArray.getJSONObject(i).getString("HomePhone") != null && !jsonArray.getJSONObject(i).getString("HomePhone").equals("")) {
                    objMemberMaster.setHomePhone(jsonArray.getJSONObject(i).getString("HomePhone"));
                }
                if (jsonArray.getJSONObject(i).getString("OfficeCountry") != null && !jsonArray.getJSONObject(i).getString("OfficeCountry").equals("")) {
                    objMemberMaster.setOfficeCountry(jsonArray.getJSONObject(i).getString("OfficeCountry"));
                }
                if (jsonArray.getJSONObject(i).getString("OfficeState") != null && !jsonArray.getJSONObject(i).getString("OfficeState").equals("")) {
                    objMemberMaster.setOfficeState(jsonArray.getJSONObject(i).getString("OfficeState"));
                }
                if (jsonArray.getJSONObject(i).getString("OfficeCity") != null && !jsonArray.getJSONObject(i).getString("OfficeCity").equals("")) {
                    objMemberMaster.setOfficeCity(jsonArray.getJSONObject(i).getString("OfficeCity"));
                }
                if (jsonArray.getJSONObject(i).getString("OfficeArea") != null && !jsonArray.getJSONObject(i).getString("OfficeArea").equals("")) {
                    objMemberMaster.setOfficeArea(jsonArray.getJSONObject(i).getString("OfficeArea"));
                }
                if (jsonArray.getJSONObject(i).getString("OfficeNumberStreet") != null && !jsonArray.getJSONObject(i).getString("OfficeNumberStreet").equals("")) {
                    objMemberMaster.setOfficeNumberStreet(jsonArray.getJSONObject(i).getString("OfficeNumberStreet"));
                }
                if (jsonArray.getJSONObject(i).getString("OfficeNearBy") != null && !jsonArray.getJSONObject(i).getString("OfficeNearBy").equals("")) {
                    objMemberMaster.setOfficeNearBy(jsonArray.getJSONObject(i).getString("OfficeNearBy"));
                }
                if (jsonArray.getJSONObject(i).getString("OfficeZipCode") != null && !jsonArray.getJSONObject(i).getString("OfficeZipCode").equals("")) {
                    objMemberMaster.setOfficeZipCode(jsonArray.getJSONObject(i).getString("OfficeZipCode"));
                }
                if (jsonArray.getJSONObject(i).getString("OfficePhone") != null && !jsonArray.getJSONObject(i).getString("OfficePhone").equals("")) {
                    objMemberMaster.setOfficePhone(jsonArray.getJSONObject(i).getString("OfficePhone"));
                }
                if (!jsonArray.getJSONObject(i).getString("IsApproved").equals("null")) {
                    objMemberMaster.setIsApproved(jsonArray.getJSONObject(i).getBoolean("IsApproved"));
                }
                objMemberMaster.setIsAdminNotificationSent(jsonArray.getJSONObject(i).getBoolean("IsAdminNotificationSent"));
                objMemberMaster.setIsMemberNotificationSent(jsonArray.getJSONObject(i).getBoolean("IsMemberNotificationSent"));
                if (jsonArray.getJSONObject(i).getString("FCMToken") != null && !jsonArray.getJSONObject(i).getString("FCMToken").equals("")) {
                    objMemberMaster.setFCMToken(jsonArray.getJSONObject(i).getString("FCMToken"));
                }
                if (!jsonArray.getJSONObject(i).getString("linktoMemberMasterIdApprovedBy").equals("null")) {
                    objMemberMaster.setlinktoMemberMasterIdApprovedBy(jsonArray.getJSONObject(i).getInt("linktoMemberMasterIdApprovedBy"));
                }
                if (jsonArray.getJSONObject(i).getString("ApprovedDateTime") != null && !jsonArray.getJSONObject(i).getString("ApprovedDateTime").equals("null")) {
                    dt = sdfDateTimeFormat.parse(jsonArray.getJSONObject(i).getString("ApprovedDateTime"));
                    objMemberMaster.setApprovedDateTime(sdfControlDateFormat.format(dt));
                }
                if (!jsonArray.getJSONObject(i).getString("linktoMemberMasterIdUpdatedBy").equals("null")) {
                    objMemberMaster.setlinktoMemberMasterIdUpdatedBy(jsonArray.getJSONObject(i).getInt("linktoMemberMasterIdUpdatedBy"));
                }
                if (jsonArray.getJSONObject(i).getString("UpdateDateTime") != null && !jsonArray.getJSONObject(i).getString("UpdateDateTime").equals("null")) {
                    dt = sdfDateTimeFormat.parse(jsonArray.getJSONObject(i).getString("UpdateDateTime"));
                    objMemberMaster.setUpdateDateTime(sdfControlDateFormat.format(dt));
                }
                lstMemberMaster.add(objMemberMaster);
            }
            return lstMemberMaster;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //endregion

    //region Insert

    public void InsertMemberMasterDetail(final Context context, final Fragment fragment, final MemberMasterNew objMemberMaster, ArrayList<MemberRelativesTran> objMemberRelativesTren) {
        try {
            JSONStringer stringer = new JSONStringer();
            stringer.object();

            stringer.key("memberMaster");
            stringer.object();

            stringer.key("MemberName").value(objMemberMaster.getMemberName());
            stringer.key("Phone1").value(objMemberMaster.getPhone1());
            stringer.key("Phone2").value(objMemberMaster.getPhone2());
            stringer.key("Email").value(objMemberMaster.getEmail());
            stringer.key("Password").value(objMemberMaster.getPassword());
            stringer.key("MemberType").value(objMemberMaster.getMemberType());
            stringer.key("Gender").value(objMemberMaster.getGender());
            if (objMemberMaster.getBirthDate() != null) {
                dt = sdfControlDateFormat.parse(objMemberMaster.getBirthDate());
                stringer.key("BirthDate").value(sdfDateFormat.format(dt));
            }
            String token1 = null;
            if (objMemberMaster.getFCMToken() != null) {
                token1 = Globals.UrlEncode(objMemberMaster.getFCMToken());
            }
            stringer.key("FCMToken").value(token1);
            if (objMemberMaster.getImageName() != null) {
                stringer.key("ImageName").value(objMemberMaster.getImageName());
                stringer.key("ImageNameBytes").value(RegistrationActivity.imagePhysicalNameBytes);
            }

//            Personal Detail
            stringer.key("Qualification").value(objMemberMaster.getQualification());
            stringer.key("Profession").value(objMemberMaster.getProfession());
            stringer.key("BloodGroup").value(objMemberMaster.getBloodGroup());
            stringer.key("IsMarried").value(objMemberMaster.getIsMarried());
            if (objMemberMaster.getIsMarried()) {
                dt = sdfControlDateFormat.parse(objMemberMaster.getAnniversaryDate());
                stringer.key("AnniversaryDate").value(sdfDateFormat.format(dt));
            }

            if (objMemberRelativesTren != null && objMemberRelativesTren.size() > 0) {
                stringer.key("lstMemberRelativeTran");
                stringer.array();
                for (int i = 0; i < objMemberRelativesTren.size(); i++) {
                    stringer.object();
                    stringer.key("linktoMemberMasterId").value(objMemberRelativesTren.get(i).getlinktoMemberMasterId());
                    stringer.key("RelativeName").value(objMemberRelativesTren.get(i).getRelativeName());
                    if (objMemberRelativesTren.get(i).getImageName() != null) {
                        stringer.key("ImageName").value(objMemberRelativesTren.get(i).getImageName());
                        if (i == 0) {
                            stringer.key("ImageNameBytes").value(RegistrationDetailActivity.imagePhysicalNameBytes);
                        } else if (i == 1) {
                            stringer.key("ImageNameBytes").value(RegistrationDetailActivity.imagePhysicalNameBytes1);
                        } else if (i == 2) {
                            stringer.key("ImageNameBytes").value(RegistrationDetailActivity.imagePhysicalNameBytes2);
                        } else if (i == 3) {
                            stringer.key("ImageNameBytes").value(RegistrationDetailActivity.imagePhysicalNameBytes3);
                        } else if (i == 4) {
                            stringer.key("ImageNameBytes").value(RegistrationDetailActivity.imagePhysicalNameBytes4);
                        } else if (i == 5) {
                            stringer.key("ImageNameBytes").value(RegistrationDetailActivity.imagePhysicalNameBytes5);
                        }
                    }
                    stringer.key("Gender").value(objMemberRelativesTren.get(i).getGender());
                    dt = sdfControlDateFormat.parse(objMemberRelativesTren.get(i).getBirthDate());
                    stringer.key("BirthDate").value(sdfDateFormat.format(dt));
                    stringer.key("Relation").value(objMemberRelativesTren.get(i).getRelation());
                    stringer.endObject();
                }
                stringer.endArray();
            }

            //Contact Detail
            stringer.key("HomeCountry").value(objMemberMaster.getHomeCountry());
            stringer.key("HomeState").value(objMemberMaster.getHomeState());
            stringer.key("HomeCity").value(objMemberMaster.getHomeCity());
            stringer.key("HomeArea").value(objMemberMaster.getHomeArea());
            stringer.key("HomeNumberStreet").value(objMemberMaster.getHomeNumberStreet());
            stringer.key("HomeNearBy").value(objMemberMaster.getHomeNearBy());
            stringer.key("HomeZipCode").value(objMemberMaster.getHomeZipCode());
            stringer.key("HomePhone").value(objMemberMaster.getHomePhone());
            stringer.key("OfficeCountry").value(objMemberMaster.getOfficeCountry());
            stringer.key("OfficeState").value(objMemberMaster.getOfficeState());
            stringer.key("OfficeCity").value(objMemberMaster.getOfficeCity());
            stringer.key("OfficeArea").value(objMemberMaster.getOfficeArea());
            stringer.key("OfficeNumberStreet").value(objMemberMaster.getOfficeNumberStreet());
            stringer.key("OfficeNearBy").value(objMemberMaster.getOfficeNearBy());
            stringer.key("OfficeZipCode").value(objMemberMaster.getOfficeZipCode());
            stringer.key("OfficePhone").value(objMemberMaster.getOfficePhone());

            stringer.endObject();

            stringer.endObject();

            String url = Service.Url + this.InsertMemberMasterDetail;

            RequestQueue queue = Volley.newRequestQueue(context);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(stringer.toString()), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    try {
                        JSONObject jsonResponse = jsonObject.getJSONObject(InsertMemberMasterDetail + "Result");
                        if (jsonResponse != null) {
                            if (fragment != null) {
                                objMemberRequestListener = (MemberRequestListener) fragment;
                            } else {
                                objMemberRequestListener = (MemberRequestListener) context;
                            }
                            MemberMaster objMemberMaster = new MemberMaster();
                            if (jsonResponse.getString("errorCode") != null && !jsonResponse.getString("errorCode").equals("")) {
                                if (jsonResponse.getString("errorCode").equals("-2")) {
                                    objMemberRequestListener.MemberUpdate("-2", null);
                                    return;
                                }
                            }
                            objMemberMaster.setMemberMasterId(jsonResponse.getInt("MemberMasterId"));
                            objMemberMaster.setMemberName(jsonResponse.getString("MemberName"));
                            objMemberMaster.setPhone1(jsonResponse.getString("Phone1"));
                            objMemberMaster.setEmail(jsonResponse.getString("Email"));
                            objMemberMaster.setPassword(jsonResponse.getString("Password"));
                            objMemberMaster.setMemberType(jsonResponse.getString("MemberType"));
                            objMemberMaster.setGender(jsonResponse.getString("Gender"));
                            if (jsonResponse.getString("BirthDate") != null && !jsonResponse.getString("BirthDate").equals("null")) {
                                dt = sdfDateFormat.parse(jsonResponse.getString("BirthDate"));
                                objMemberMaster.setBirthDate(sdfControlDateFormat.format(dt));
                            }
                            objMemberRequestListener.MemberUpdate("0", objMemberMaster);
                        } else {
                            if (fragment != null) {
                                objMemberRequestListener = (MemberRequestListener) fragment;
                            } else {
                                objMemberRequestListener = (MemberRequestListener) context;
                            }
                            objMemberRequestListener.MemberUpdate("-1", null);
                        }
                    } catch (Exception e) {
                        if (fragment != null) {
                            objMemberRequestListener = (MemberRequestListener) fragment;
                        } else {
                            objMemberRequestListener = (MemberRequestListener) context;
                        }
                        objMemberRequestListener.MemberUpdate("-1", null);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    if (fragment != null) {
                        objMemberRequestListener = (MemberRequestListener) fragment;
                    } else {
                        objMemberRequestListener = (MemberRequestListener) context;
                    }
                    objMemberRequestListener.MemberUpdate("-1", null);
                }
            });

            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                    30000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            queue.add(jsonObjectRequest);
        } catch (Exception ex) {
            if (fragment != null) {
                objMemberRequestListener = (MemberRequestListener) fragment;
            } else {
                objMemberRequestListener = (MemberRequestListener) context;
            }
            objMemberRequestListener.MemberResponse("-1", null);
        }
    }

    //endregion

    //region Update

    public void UpdateMemberMasterByMemberMasterId(final Context context, final Fragment fragment, final MemberMaster objMemberMaster) {
        try {
            JSONStringer stringer = new JSONStringer();
            stringer.object();

            stringer.key("memberMaster");
            stringer.object();

            stringer.key("MemberMasterId").value(objMemberMaster.getMemberMasterId());
            stringer.key("MemberName").value(objMemberMaster.getMemberName());
            stringer.key("Phone1").value(objMemberMaster.getPhone1());
            stringer.key("Phone2").value(objMemberMaster.getPhone2());
            stringer.key("Gender").value(objMemberMaster.getGender());
            if (objMemberMaster.getBirthDate() != null) {
                dt = sdfControlDateFormat.parse(objMemberMaster.getBirthDate());
                stringer.key("BirthDate").value(sdfDateFormat.format(dt));
            }

            stringer.endObject();

            stringer.endObject();

            String url = Service.Url + this.UpdateMemberMasterByMemberMasterId;

            RequestQueue queue = Volley.newRequestQueue(context);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(stringer.toString()), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    try {
                        JSONObject jsonResponse = jsonObject.getJSONObject(UpdateMemberMasterByMemberMasterId + "Result");
                        if (jsonResponse != null) {
                            objMemberRequestListener = (MemberRequestListener) fragment;
                            objMemberRequestListener.MemberResponse(String.valueOf(jsonResponse.getInt("ErrorCode")), null);
                        } else {
                            objMemberRequestListener = (MemberRequestListener) fragment;
                            objMemberRequestListener.MemberResponse("-1", null);
                        }
                    } catch (Exception e) {
                        objMemberRequestListener = (MemberRequestListener) fragment;
                        objMemberRequestListener.MemberResponse("-1", null);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    objMemberRequestListener = (MemberRequestListener) fragment;
                    objMemberRequestListener.MemberResponse("-1", null);
                }
            });

            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                    30000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            queue.add(jsonObjectRequest);
        } catch (Exception ex) {
            objMemberRequestListener = (MemberRequestListener) fragment;
            objMemberRequestListener.MemberResponse("-1", null);
        }

    }

    public void UpdateMemberMasterImageByMemberMasterId(final Context context, final MemberMaster objMemberMaster) {
        try {
            final JSONStringer stringer = new JSONStringer();
            stringer.object();
            stringer.key("memberMaster");
            stringer.object();
            stringer.key("MemberMasterId").value(objMemberMaster.getMemberMasterId());
            if (objMemberMaster.getImageName() != null) {
                stringer.key("ImageName").value(objMemberMaster.getImageName());
            }
            if (objMemberMaster.getImageNameBytes() != null) {
                stringer.key("ImageNameBytes").value(objMemberMaster.getImageNameBytes());
            }
            stringer.endObject();
            stringer.endObject();

            String url = Service.Url + this.UpdateMemberMasterImageByMemberMasterId;

            RequestQueue queue = Volley.newRequestQueue(context);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(stringer.toString()), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    try {
                        String stringImage = jsonObject.getString(UpdateMemberMasterImageByMemberMasterId + "Result");
                        MemberMaster objMemberMaster = new MemberMaster();
                        if (stringImage != null && !stringImage.equals("") && !stringImage.equals("null")) {
                            objMemberRequestListener = (MemberRequestListener) context;
                            objMemberMaster.setImageName(stringImage);
                            objMemberRequestListener.MemberUpdate("0", objMemberMaster);
                        } else {
                            objMemberRequestListener = (MemberRequestListener) context;
                            objMemberRequestListener.MemberUpdate("-1", objMemberMaster);
                        }
                    } catch (Exception e) {
                        objMemberRequestListener = (MemberRequestListener) context;
                        objMemberRequestListener.MemberUpdate("-1", null);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    objMemberRequestListener = (MemberRequestListener) context;
                    objMemberRequestListener.MemberUpdate("-1", null);
                }
            });

            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                    30000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            queue.add(jsonObjectRequest);
        } catch (Exception ex) {
            objMemberRequestListener = (MemberRequestListener) context;
            objMemberRequestListener.MemberResponse("-1", null);
        }

    }

    public void UpdateMemberMasterContactDetail(final Context context, final Fragment fragment, final MemberMaster objMemberMaster) {
        try {
            JSONStringer stringer = new JSONStringer();
            stringer.object();

            stringer.key("memberMaster");
            stringer.object();

            stringer.key("MemberMasterId").value(objMemberMaster.getMemberMasterId());
            stringer.key("HomeCountry").value(objMemberMaster.getHomeCountry());
            stringer.key("HomeState").value(objMemberMaster.getHomeState());
            stringer.key("HomeCity").value(objMemberMaster.getHomeCity());
            stringer.key("HomeArea").value(objMemberMaster.getHomeArea());
            stringer.key("HomeNumberStreet").value(objMemberMaster.getHomeNumberStreet());
            stringer.key("HomeNearBy").value(objMemberMaster.getHomeNearBy());
            stringer.key("HomeZipCode").value(objMemberMaster.getHomeZipCode());
            stringer.key("HomePhone").value(objMemberMaster.getHomePhone());
            stringer.key("OfficeCountry").value(objMemberMaster.getOfficeCountry());
            stringer.key("OfficeState").value(objMemberMaster.getOfficeState());
            stringer.key("OfficeCity").value(objMemberMaster.getOfficeCity());
            stringer.key("OfficeArea").value(objMemberMaster.getOfficeArea());
            stringer.key("OfficeNumberStreet").value(objMemberMaster.getOfficeNumberStreet());
            stringer.key("OfficeNearBy").value(objMemberMaster.getOfficeNearBy());
            stringer.key("OfficeZipCode").value(objMemberMaster.getOfficeZipCode());
            stringer.key("OfficePhone").value(objMemberMaster.getOfficePhone());

            stringer.endObject();

            stringer.endObject();

            String url = Service.Url + this.UpdateMemberMasterContactDetail;

            RequestQueue queue = Volley.newRequestQueue(context);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(stringer.toString()), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    try {
                        JSONObject jsonResponse = jsonObject.getJSONObject(UpdateMemberMasterContactDetail + "Result");
                        if (jsonResponse != null) {
                            objMemberRequestListener = (MemberRequestListener) fragment;
                            objMemberRequestListener.MemberResponse(String.valueOf(jsonResponse.getInt("ErrorCode")), null);
                        } else {
                            objMemberRequestListener = (MemberRequestListener) fragment;
                            objMemberRequestListener.MemberResponse("-1", null);
                        }
                    } catch (Exception e) {
                        objMemberRequestListener = (MemberRequestListener) fragment;
                        objMemberRequestListener.MemberResponse("-1", null);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    objMemberRequestListener = (MemberRequestListener) fragment;
                    objMemberRequestListener.MemberResponse("-1", null);
                }
            });

            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                    30000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            queue.add(jsonObjectRequest);
        } catch (Exception ex) {
            objMemberRequestListener = (MemberRequestListener) fragment;
            objMemberRequestListener.MemberResponse("-1", null);
        }

    }

    public void UpdateMemberMasterIsApproved(final Context context, final MemberMaster objMemberMaster) {
        try {
            JSONStringer stringer = new JSONStringer();
            stringer.object();

            stringer.key("memberMaster");
            stringer.object();

            stringer.key("MemberMasterId").value(objMemberMaster.getMemberMasterId());
            stringer.key("IsApproved").value(objMemberMaster.getIsApproved());
            stringer.key("linktoMemberMasterIdApprovedBy").value(objMemberMaster.getlinktoMemberMasterIdApprovedBy());
            stringer.key("linktoMemberMasterIdUpdatedBy").value(objMemberMaster.getlinktoMemberMasterIdUpdatedBy());

            stringer.endObject();

            stringer.endObject();

            String url = Service.Url + this.UpdateMemberMasterIsApproved;

            RequestQueue queue = Volley.newRequestQueue(context);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(stringer.toString()), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    try {
                        JSONObject jsonResponse = jsonObject.getJSONObject(UpdateMemberMasterIsApproved + "Result");
                        if (jsonResponse != null) {
                            objMemberRequestListener = (MemberRequestListener) context;
                            objMemberRequestListener.MemberUpdate(String.valueOf(jsonResponse.getInt("ErrorCode")), null);
                        } else {
                            objMemberRequestListener = (MemberRequestListener) context;
                            objMemberRequestListener.MemberUpdate("-1", null);
                        }
                    } catch (Exception e) {
                        objMemberRequestListener = (MemberRequestListener) context;
                        objMemberRequestListener.MemberUpdate("-1", null);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    objMemberRequestListener = (MemberRequestListener) context;
                    objMemberRequestListener.MemberUpdate("-1", null);
                }
            });

            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                    30000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            queue.add(jsonObjectRequest);
        } catch (Exception ex) {
            objMemberRequestListener = (MemberRequestListener) context;
            objMemberRequestListener.MemberUpdate("-1", null);
        }

    }

    public void DeleteMemberMasterByMemberMasterId(final Context context, int memberMasterId, boolean isDelete) {
        try {
            String url = Service.Url + this.DeleteMemberMasterByMemberMasterId + "/" + memberMasterId + "/" + String.valueOf(isDelete) + "/" + Globals.memberMasterId;

            RequestQueue queue = Volley.newRequestQueue(context);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, new JSONObject(), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    try {
                        JSONObject jsonResponse = jsonObject.getJSONObject(DeleteMemberMasterByMemberMasterId + "Result");
                        if (jsonResponse != null) {
                            objMemberRequestListener = (MemberRequestListener) context;
                            objMemberRequestListener.MemberUpdate(String.valueOf(jsonResponse.getInt("ErrorCode")), null);
                        } else {
                            objMemberRequestListener = (MemberRequestListener) context;
                            objMemberRequestListener.MemberUpdate("-1", null);
                        }
                    } catch (Exception e) {
                        objMemberRequestListener = (MemberRequestListener) context;
                        objMemberRequestListener.MemberUpdate("-1", null);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    objMemberRequestListener = (MemberRequestListener) context;
                    objMemberRequestListener.MemberUpdate("-1", null);
                }
            });

            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                    30000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            queue.add(jsonObjectRequest);
        } catch (Exception ex) {
            objMemberRequestListener = (MemberRequestListener) context;
            objMemberRequestListener.MemberUpdate("-1", null);
        }

    }

    public void UpdateMemberMasterAdmin(final Context context, int memberMasterId, String memberType) {
        try {
            String url = Service.Url + this.UpdateMemberMasterAdmin + "/" + memberMasterId + "/" + memberType + "/" + Globals.memberMasterId;

            RequestQueue queue = Volley.newRequestQueue(context);
            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, new JSONObject(), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    try {
                        JSONObject jsonResponse = jsonObject.getJSONObject(UpdateMemberMasterAdmin + "Result");
                        if (jsonResponse != null) {
                            objMemberRequestListener = (MemberRequestListener) context;
                            objMemberRequestListener.MemberUpdate(String.valueOf(jsonResponse.getInt("ErrorCode")), null);
                        } else {
                            objMemberRequestListener = (MemberRequestListener) context;
                            objMemberRequestListener.MemberUpdate("-1", null);
                        }
                    } catch (Exception e) {
                        objMemberRequestListener = (MemberRequestListener) context;
                        objMemberRequestListener.MemberUpdate("-1", null);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    objMemberRequestListener = (MemberRequestListener) context;
                    objMemberRequestListener.MemberUpdate("-1", null);
                }
            });

            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                    30000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            queue.add(jsonObjectRequest);
        } catch (Exception ex) {
            objMemberRequestListener = (MemberRequestListener) context;
            objMemberRequestListener.MemberUpdate("-1", null);
        }

    }

    public void UpdateMemberMasterPassword(final Context context, final Fragment fragment, int memberMasterId, String password, String confirmPassword) {
        try {
            String url = Service.Url + this.UpdateMemberMasterPassword + "/" + memberMasterId + "/" + password + "/" + confirmPassword;

            RequestQueue queue = Volley.newRequestQueue(context);
            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, new JSONObject(), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    try {
                        JSONObject jsonResponse = jsonObject.getJSONObject(UpdateMemberMasterPassword + "Result");
                        if (jsonResponse != null) {
                            objMemberRequestListener = (MemberRequestListener) fragment;
                            objMemberRequestListener.MemberUpdate(String.valueOf(jsonResponse.getInt("ErrorCode")), null);
                        } else {
                            objMemberRequestListener = (MemberRequestListener) fragment;
                            objMemberRequestListener.MemberUpdate("-1", null);
                        }
                    } catch (Exception e) {
                        objMemberRequestListener = (MemberRequestListener) fragment;
                        objMemberRequestListener.MemberUpdate("-1", null);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    objMemberRequestListener = (MemberRequestListener) fragment;
                    objMemberRequestListener.MemberUpdate("-1", null);
                }
            });

            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                    30000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            queue.add(jsonObjectRequest);
        } catch (Exception ex) {
            objMemberRequestListener = (MemberRequestListener) fragment;
            objMemberRequestListener.MemberUpdate("-1", null);
        }

    }

    public void UpdateMemberMasterLogOut(final Context context, int memberMasterId) {
        try {
            String url = Service.Url + this.UpdateMemberMasterLogOut + "/" + memberMasterId;

            RequestQueue queue = Volley.newRequestQueue(context);
            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, new JSONObject(), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    try {
                        JSONObject jsonResponse = jsonObject.getJSONObject(UpdateMemberMasterLogOut + "Result");
                        if (jsonResponse != null) {
                            objMemberRequestListener = (MemberRequestListener) context;
                            objMemberRequestListener.MemberUpdate(String.valueOf(jsonResponse.getInt("ErrorCode")), null);
                        } else {
                            objMemberRequestListener = (MemberRequestListener) context;
                            objMemberRequestListener.MemberUpdate("-1", null);
                        }
                    } catch (Exception e) {
                        objMemberRequestListener = (MemberRequestListener) context;
                        objMemberRequestListener.MemberUpdate("-1", null);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    objMemberRequestListener = (MemberRequestListener) context;
                    objMemberRequestListener.MemberUpdate("-1", null);
                }
            });

            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                    30000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            queue.add(jsonObjectRequest);
        } catch (Exception ex) {
            objMemberRequestListener = (MemberRequestListener) context;
            objMemberRequestListener.MemberUpdate("-1", null);
        }

    }

    public void UpdateMemberMasterPersonalDetail(final Context context, final Fragment fragment, final MemberMaster objMemberMaster) {
        try {
            JSONStringer stringer = new JSONStringer();
            stringer.object();

            stringer.key("memberMaster");
            stringer.object();

            stringer.key("MemberMasterId").value(objMemberMaster.getMemberMasterId());
            stringer.key("Qualification").value(objMemberMaster.getQualification());
            stringer.key("Profession").value(objMemberMaster.getProfession());
            stringer.key("BloodGroup").value(objMemberMaster.getBloodGroup());
            stringer.key("IsMarried").value(objMemberMaster.getIsMarried());
            if (objMemberMaster.getIsMarried()) {
                dt = sdfControlDateFormat.parse(objMemberMaster.getAnniversaryDate());
                stringer.key("AnniversaryDate").value(sdfDateFormat.format(dt));
            }

            if (objMemberMaster.getLstMemberRelativeTran() != null && objMemberMaster.getLstMemberRelativeTran().size() > 0) {
                stringer.key("lstMemberRelativeTran");
                stringer.array();
                for (int i = 0; i < objMemberMaster.getLstMemberRelativeTran().size(); i++) {
                    stringer.object();
                    stringer.key("linktoMemberMasterId").value(objMemberMaster.getLstMemberRelativeTran().get(i).getlinktoMemberMasterId());
                    stringer.key("RelativeName").value(objMemberMaster.getLstMemberRelativeTran().get(i).getRelativeName());
                    stringer.key("ImageName").value(objMemberMaster.getLstMemberRelativeTran().get(i).getImageName());
                    stringer.key("Gender").value(objMemberMaster.getLstMemberRelativeTran().get(i).getGender());
                    dt = sdfControlDateFormat.parse(objMemberMaster.getLstMemberRelativeTran().get(i).getBirthDate());
                    stringer.key("BirthDate").value(sdfDateFormat.format(dt));
                    stringer.key("Relation").value(objMemberMaster.getLstMemberRelativeTran().get(i).getRelation());
                    stringer.key("ImageNameBytes").value(objMemberMaster.getLstMemberRelativeTran().get(i).getImageNameBytes());
                    stringer.endObject();
                }
                stringer.endArray();
            }
            stringer.endObject();

            stringer.endObject();

            String url = Service.Url + this.UpdateMemberMasterPersonalDetail;

            RequestQueue queue = Volley.newRequestQueue(context);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(stringer.toString()), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    try {
                        JSONObject jsonResponse = jsonObject.getJSONObject(UpdateMemberMasterPersonalDetail + "Result");
                        if (jsonResponse != null) {
                            objMemberRequestListener = (MemberRequestListener) fragment;
                            objMemberRequestListener.MemberResponse(String.valueOf(jsonResponse.getInt("ErrorCode")), null);
                        } else {
                            objMemberRequestListener = (MemberRequestListener) fragment;
                            objMemberRequestListener.MemberResponse("-1", null);
                        }
                    } catch (Exception e) {
                        objMemberRequestListener = (MemberRequestListener) fragment;
                        objMemberRequestListener.MemberResponse("-1", null);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    objMemberRequestListener = (MemberRequestListener) fragment;
                    objMemberRequestListener.MemberResponse("-1", null);
                }
            });

            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                    30000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            queue.add(jsonObjectRequest);
        } catch (Exception ex) {
            objMemberRequestListener = (MemberRequestListener) fragment;
            objMemberRequestListener.MemberResponse("-1", null);
        }
    }

    //endregion

    //region Select

    public void SelectMemberByMemberMasterId(final Context context, int memberMasterId) {
        String url;
        try {
            url = Service.Url + this.SelectMemberByMemberMasterId + "/" + memberMasterId;
            final RequestQueue queue = Volley.newRequestQueue(context);
            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, new JSONObject(), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    try {
                        if (jsonObject != null) {
                            JSONObject jsonResponse = jsonObject.getJSONObject(SelectMemberByMemberMasterId + "Result");
                            if (jsonResponse != null) {
                                objMemberRequestListener = (MemberRequestListener) context;
                                objMemberRequestListener.MemberResponse(null, SetClassPropertiesFromJSONObject(jsonResponse));
                            }
                        }
                    } catch (Exception e) {
                        objMemberRequestListener = (MemberRequestListener) context;
                        objMemberRequestListener.MemberResponse(null, null);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    objMemberRequestListener = (MemberRequestListener) context;
                    objMemberRequestListener.MemberResponse(null, null);
                }
            });
            queue.add(jsonObjectRequest);
        } catch (Exception e) {
            objMemberRequestListener = (MemberRequestListener) context;
            objMemberRequestListener.MemberResponse(null, null);
        }
    }

    public void SelectMemberMaster(final Context context, String email, String password, String token) {
        String url;
        try {
            if (email != null && password != null) {
                url = Service.Url + this.SelectMemberMaster + "/" + Globals.UrlEncode(email) + "/" + Globals.UrlEncode(password) + "/" + Globals.UrlEncode(token);
            } else {
                url = Service.Url + this.SelectMemberMaster + "/" + null + "/" + null + "/" + null;
            }
            final RequestQueue queue = Volley.newRequestQueue(context);
            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, new JSONObject(), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    try {
                        if (jsonObject != null) {
                            JSONObject jsonResponse = jsonObject.getJSONObject(SelectMemberMaster + "Result");
                            if (jsonResponse != null) {
                                objMemberRequestListener = (MemberRequestListener) context;
                                objMemberRequestListener.MemberResponse("0", SetClassPropertiesFromJSONObject(jsonResponse));
                            }
                        }
                    } catch (Exception e) {
                        objMemberRequestListener = (MemberRequestListener) context;
                        objMemberRequestListener.MemberResponse("-1", null);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    objMemberRequestListener = (MemberRequestListener) context;
                    objMemberRequestListener.MemberResponse("-1", null);
                }
            });
            queue.add(jsonObjectRequest);
        } catch (Exception e) {
            objMemberRequestListener = (MemberRequestListener) context;
            objMemberRequestListener.MemberResponse("-1", null);
        }
    }
    //endregion

    //region SelectAll

    public void SelectAllMemberMasterPageWise(final Context context, int currentPage, int pagesize) {
        String url;
        try {
            url = Service.Url + this.SelectAllMemberMasterPageWise + "/" + Globals.memberMasterId + "/" + currentPage + "/" + pagesize;
            final RequestQueue queue = Volley.newRequestQueue(context);
            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, new JSONObject(), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    try {
                        if (jsonObject != null) {
                            JSONArray jsonArray = jsonObject.getJSONArray(SelectAllMemberMasterPageWise + "Result");
                            if (jsonArray != null) {
                                objMemberListRequestListener = (MembersListRequestListener) context;
                                objMemberListRequestListener.MemberListResponse(SetListPropertiesFromJSONArray(jsonArray), null, "0");
                            }
                        }
                    } catch (Exception e) {
                        objMemberListRequestListener = (MembersListRequestListener) context;
                        objMemberListRequestListener.MemberListResponse(null, null, "-1");
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    objMemberListRequestListener = (MembersListRequestListener) context;
                    objMemberListRequestListener.MemberListResponse(null, null, "-1");
                }
            });
            queue.add(jsonObjectRequest);
        } catch (Exception e) {
            objMemberListRequestListener = (MembersListRequestListener) context;
            objMemberListRequestListener.MemberListResponse(null, null, "-1");
        }
    }

    public void SelectAllMemberMasterFilterPageWise(final Context context, int currentPage, String memberName, String profession, String qualification, String bloodGroup) {
        String url;
        try {
            url = Service.Url + this.SelectAllMemberMasterFilterPageWise + "/" + Globals.memberMasterId + "/" + currentPage + "/" + memberName + "/" + Globals.UrlEncode(profession) + "/" + Globals.UrlEncode(qualification) + "/" + Globals.UrlEncode(bloodGroup);
            final RequestQueue queue = Volley.newRequestQueue(context);
            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, new JSONObject(), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    try {
                        if (jsonObject != null) {
                            JSONArray jsonArray = jsonObject.getJSONArray(SelectAllMemberMasterFilterPageWise + "Result");
                            if (jsonArray != null) {
                                objMemberListRequestListener = (MembersListRequestListener) context;
                                objMemberListRequestListener.MemberListResponse(SetListPropertiesFromJSONArray(jsonArray), null, "0");
                            }
                        }
                    } catch (Exception e) {
                        objMemberListRequestListener = (MembersListRequestListener) context;
                        objMemberListRequestListener.MemberListResponse(null, null, "-1");
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    objMemberListRequestListener = (MembersListRequestListener) context;
                    objMemberListRequestListener.MemberListResponse(null, null, "-1");
                }
            });
            queue.add(jsonObjectRequest);
        } catch (Exception e) {
            objMemberListRequestListener = (MembersListRequestListener) context;
            objMemberListRequestListener.MemberListResponse(null, null, "-1");
        }
    }

    public void SelectAllNewMemberMasterPageWise(final Context context, int currentPage) {
        String url;
        try {
            url = Service.Url + this.SelectAllNewMemberMasterPageWise + "/" + currentPage;
            final RequestQueue queue = Volley.newRequestQueue(context);
            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, new JSONObject(), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    try {
                        if (jsonObject != null) {
                            JSONArray jsonArray = jsonObject.getJSONArray(SelectAllNewMemberMasterPageWise + "Result");
                            if (jsonArray != null) {
                                objMemberListRequestListener = (MembersListRequestListener) context;
                                objMemberListRequestListener.MemberListResponse(SetListPropertiesFromJSONArray(jsonArray), null, "0");
                            }
                        }
                    } catch (Exception e) {
                        objMemberListRequestListener = (MembersListRequestListener) context;
                        objMemberListRequestListener.MemberListResponse(null, null, "0");
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    objMemberListRequestListener = (MembersListRequestListener) context;
                    objMemberListRequestListener.MemberListResponse(null, null, "-1");
                }
            });
            queue.add(jsonObjectRequest);
        } catch (Exception e) {
            objMemberListRequestListener = (MembersListRequestListener) context;
            objMemberListRequestListener.MemberListResponse(null, null, "-1");
        }

    }

    //endregion

    public void ForgotPasswordMemberMaster(final Context context, final Fragment fragment, String email) {
        try {
            String url = Service.Url + this.ForgotPasswordMemberMaster + "/" + Globals.UrlEncode(email);

            RequestQueue queue = Volley.newRequestQueue(context);
            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, new JSONObject(), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    try {
                        JSONObject jsonResponse = jsonObject.getJSONObject(ForgotPasswordMemberMaster + "Result");
                        if (jsonResponse != null) {
                            objMemberRequestListener = (MemberRequestListener) fragment;
                            objMemberRequestListener.MemberUpdate(String.valueOf(jsonResponse.getInt("ErrorCode")), null);
                        } else {
                            objMemberRequestListener = (MemberRequestListener) fragment;
                            objMemberRequestListener.MemberUpdate("-1", null);
                        }
                    } catch (Exception e) {
                        objMemberRequestListener = (MemberRequestListener) fragment;
                        objMemberRequestListener.MemberUpdate("-1", null);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    objMemberRequestListener = (MemberRequestListener) fragment;
                    objMemberRequestListener.MemberUpdate("-1", null);
                }
            });

            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                    30000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            queue.add(jsonObjectRequest);
        } catch (Exception ex) {
            objMemberRequestListener = (MemberRequestListener) fragment;
            objMemberRequestListener.MemberUpdate("-1", null);
        }

    }

    public interface MemberRequestListener {
        void MemberResponse(String errorCode, MemberMaster objMemberMaster);

        void MemberUpdate(String errorCode, MemberMaster objMemberMaster);
    }

    public interface MembersListRequestListener {
        void MemberListResponse(ArrayList<MemberMaster> alMemberMasters, MemberMaster objMemberMaster, String errorCode);
    }

}
