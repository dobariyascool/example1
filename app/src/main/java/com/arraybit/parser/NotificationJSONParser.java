package com.arraybit.parser;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.modal.NotificationMaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class NotificationJSONParser {
    public String InsertNotificationMaster = "InsertNotificationMaster";
    public String UpdateNotificationMaster = "UpdateNotificationMaster";
    public String DeleteNotificationMaster = "DeleteNotificationMaster";
    public String SelectNotificationMaster = "SelectNotificationMaster";
    public String SelectAllNotificationMaster = "SelectAllNotificationMasterPageWise";

    public  String DateFormat = "dd/MM/yyyy HH:mm";
    SimpleDateFormat sdfControlDateFormat = new SimpleDateFormat(DateFormat, Locale.US);
    Date dt = null;
    SimpleDateFormat sdfDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
    NotificationRequestListener objNotificationRequestListener;
    NotificationAddListener objNotificationAddListener;

    private NotificationMaster SetClassPropertiesFromJSONObject(JSONObject jsonObject) {
        NotificationMaster objNotificationMaster = null;
        try {
            if (jsonObject != null) {
                objNotificationMaster = new NotificationMaster();
                objNotificationMaster.setNotificationMasterId(jsonObject.getInt("NotificationMasterId"));
                objNotificationMaster.setNotificationTitle(jsonObject.getString("NotificationTitle"));
                objNotificationMaster.setNotificationText(jsonObject.getString("NotificationText"));
                if (jsonObject.getString("NotificationImageName") != null && !jsonObject.getString("NotificationImageName").equals("") && !jsonObject.getString("NotificationImageName").equals("null")) {
                    objNotificationMaster.setNotificationImageName(jsonObject.getString("NotificationImageName"));
                }
                if(jsonObject.getString("NotificationDateTime") != null && !jsonObject.getString("NotificationDateTime").equals("") && !jsonObject.getString("NotificationDateTime").equals("null")) {
                    dt = sdfDateTimeFormat.parse(jsonObject.getString("NotificationDateTime"));
                    objNotificationMaster.setNotificationDateTime(sdfControlDateFormat.format(dt));
                }
            }
            return objNotificationMaster;
        } catch (Exception e) {
            return null;
        }
    }

    private ArrayList<NotificationMaster> SetListPropertiesFromJSONArray(JSONArray jsonArray) {
        ArrayList<NotificationMaster> lstNotificationMaster = new ArrayList<>();
        NotificationMaster objNotificationMaster;
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                objNotificationMaster = new NotificationMaster();
                objNotificationMaster.setNotificationMasterId(jsonArray.getJSONObject(i).getInt("NotificationMasterId"));
                objNotificationMaster.setNotificationTitle(jsonArray.getJSONObject(i).getString("NotificationTitle"));
                objNotificationMaster.setNotificationText(jsonArray.getJSONObject(i).getString("NotificationText"));
                if (jsonArray.getJSONObject(i).getString("NotificationImageName") != null && !jsonArray.getJSONObject(i).getString("NotificationImageName").equals("") && !jsonArray.getJSONObject(i).getString("NotificationImageName").equals("null")) {
                    objNotificationMaster.setNotificationImageName(jsonArray.getJSONObject(i).getString("NotificationImageName"));
                }
                dt = sdfDateTimeFormat.parse(jsonArray.getJSONObject(i).getString("NotificationDateTime"));
                objNotificationMaster.setNotificationDateTime(sdfControlDateFormat.format(dt));
                lstNotificationMaster.add(objNotificationMaster);
            }
            return lstNotificationMaster;
        } catch (Exception e) {
            return null;
        }
    }

    public void InsertNotificationMaster(NotificationMaster objNotificationMaster, Context context, final Fragment targetFragment) {
        dt = new Date();
        try {
            JSONStringer stringer = new JSONStringer();
            stringer.object();

            stringer.key("notificationMaster");
            stringer.object();

            stringer.key("NotificationTitle").value(objNotificationMaster.getNotificationTitle());
            stringer.key("NotificationText").value(objNotificationMaster.getNotificationText());
            stringer.key("NotificationImageNameBytes").value(objNotificationMaster.getNotificationImageNameBytes());
            stringer.key("NotificationImageName").value(objNotificationMaster.getNotificationImageName());
            stringer.key("linktoMemberMasterIdCreatedBy").value(objNotificationMaster.getlinktoMemberMasterIdCreatedBy());

            stringer.endObject();
            stringer.endObject();

            String url = Service.Url + this.InsertNotificationMaster;

            RequestQueue queue = Volley.newRequestQueue(context);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(stringer.toString()), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    try {
                        Log.e("jsonObject", " " + jsonObject);
                        JSONObject jsonResponse = jsonObject.getJSONObject(InsertNotificationMaster + "Result");
                        if (jsonResponse != null) {
                            objNotificationAddListener = (NotificationAddListener) targetFragment;
                            objNotificationAddListener.NotificationAddResponse("0", SetClassPropertiesFromJSONObject(jsonResponse));
                        }
                    } catch (JSONException e) {
                        objNotificationAddListener = (NotificationAddListener) targetFragment;
                        objNotificationAddListener.NotificationAddResponse("-1", null);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    objNotificationAddListener = (NotificationAddListener) targetFragment;
                    objNotificationAddListener.NotificationAddResponse("-1", null);
                }
            });

            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                    30000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            queue.add(jsonObjectRequest);
        } catch (Exception ex) {
            objNotificationAddListener = (NotificationAddListener) targetFragment;
            objNotificationAddListener.NotificationAddResponse("-1", null);
        }
    }

    public void SelectAllNotificationMasterPageWise(String currentPage, Context context, final Fragment targetFragment) {
        String url = Service.Url + this.SelectAllNotificationMaster + "/" + currentPage +"/"+ Globals.memberMasterId ;
        Log.e("url", " " + url);
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, new JSONObject(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    Log.e("json", " " + jsonObject);
                    JSONArray jsonArray = jsonObject.getJSONArray(SelectAllNotificationMaster + "Result");
                    if (jsonArray != null) {
                        ArrayList<NotificationMaster> notificationMasters = SetListPropertiesFromJSONArray(jsonArray);
                        objNotificationRequestListener = (NotificationRequestListener) targetFragment;
                        objNotificationRequestListener.NotificationResponse(notificationMasters, null);
                    }
                } catch (Exception e) {
                    objNotificationRequestListener = (NotificationRequestListener) targetFragment;
                    objNotificationRequestListener.NotificationResponse(null, null);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                objNotificationRequestListener = (NotificationRequestListener) targetFragment;
                objNotificationRequestListener.NotificationResponse(null, null);
            }

        });
        queue.add(jsonObjectRequest);

    }

    //region interface
    public interface NotificationRequestListener {
        void NotificationResponse(ArrayList<NotificationMaster> alNotificationMasters, NotificationMaster objNotificationMaster);
    }

    public interface NotificationAddListener {
        void NotificationAddResponse(String errorCode, NotificationMaster objNotificationMaster);
    }
    //endregion
}
