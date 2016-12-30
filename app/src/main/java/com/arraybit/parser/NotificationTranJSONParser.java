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
import com.arraybit.modal.NotificationTran;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class NotificationTranJSONParser {

    public String InsertNotificationTran = "InsertNotificationTran";
    NotificationInsertListener objNotificationInsertListener;

    public void InsertNotificationTran(NotificationTran objNotificationTran, Context context, final Fragment targetFragment) {
        try {
            JSONStringer stringer = new JSONStringer();
            stringer.object();

            stringer.key("notificationTran");
            stringer.object();

            stringer.key("linktoNotificationMasterId").value(objNotificationTran.getlinktoNotificationMasterId());
            stringer.key("linktoMemberMasterId").value(objNotificationTran.getlinktoMemberMasterId());

            stringer.endObject();

            stringer.endObject();
            Log.e("json", " " + stringer.toString());

            String url = Service.Url + this.InsertNotificationTran;

            RequestQueue queue = Volley.newRequestQueue(context);
            Log.e("url", " " + url);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(stringer.toString()), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    try {
                        Log.e("jsonObject", " " + jsonObject);
                        JSONObject jsonResponse = jsonObject.getJSONObject(InsertNotificationTran + "Result");
                        if (jsonResponse != null) {
                            objNotificationInsertListener = (NotificationInsertListener) targetFragment;
                            objNotificationInsertListener.NotificationResponse("0");
                        }
                    } catch (JSONException e) {
                        objNotificationInsertListener = (NotificationInsertListener) targetFragment;
                        objNotificationInsertListener.NotificationResponse("-1");
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    objNotificationInsertListener = (NotificationInsertListener) targetFragment;
                    objNotificationInsertListener.NotificationResponse("-1");
                }
            });

            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                    30000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            queue.add(jsonObjectRequest);
        } catch (Exception ex) {
            objNotificationInsertListener = (NotificationInsertListener) targetFragment;
            objNotificationInsertListener.NotificationResponse("-1");
        }
    }

    //region interface
    public interface NotificationInsertListener {
        void NotificationResponse(String errorCode);
    }
    //endregion
}
