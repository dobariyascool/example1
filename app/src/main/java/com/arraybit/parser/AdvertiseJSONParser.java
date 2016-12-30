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
import com.arraybit.modal.AdvertiseMaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/// <summary>
/// JSONParser for AdvertiseMaster
/// </summary>
public class AdvertiseJSONParser {
    public String InsertAdvertiseMaster = "InsertAdvertiseMaster";
    public String UpdateAdvertiseMasterDisable = "UpdateAdvertiseMasterDisable";
    public String UpdateAdvertiseMasterDelete = "UpdateAdvertiseMasterDelete";
    public String UpdateAdvertiseMaster = "UpdateAdvertiseMaster";

    SimpleDateFormat sdfControlDateFormat = new SimpleDateFormat(Globals.DateFormat, Locale.US);
    Date dt = null;
    SimpleDateFormat sdfDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
    AdvretiseRequestListener objAdvretiseRequestListener;
    AdvertiseAddListener objAdvertiseAddListener;
    AdvertiseUpdateListener objAdvertiseUpdateListener;
    private String SelectAllAdvertiseMasterPageWise = "SelectAllAdvertiseMasterPageWise";
    String url = "";
    RequestQueue queue ;
    JsonObjectRequest jsonObjectRequest;
    JSONArray jsonArray;
    private AdvertiseMaster SetClassPropertiesFromJSONObject(JSONObject jsonObject) {
        AdvertiseMaster objAdvertiseMaster = null;
        try {
            if (jsonObject != null) {
                objAdvertiseMaster = new AdvertiseMaster();
                objAdvertiseMaster.setAdvertiseMasterId(jsonObject.getInt("AdvertiseMasterId"));
                if (jsonObject.getString("AdvertiseText") != null && !jsonObject.getString("AdvertiseText").equals("")&& !jsonObject.getString("AdvertiseText").equals("null")) {
                    objAdvertiseMaster.setAdvertiseText(jsonObject.getString("AdvertiseText"));
                }
                if (jsonObject.getString("AdvertiseImageName") != null && !jsonObject.getString("AdvertiseImageName").equals("")&& !jsonObject.getString("AdvertiseImageName").equals("null")) {
                    objAdvertiseMaster.setAdvertiseImageName(jsonObject.getString("AdvertiseImageName"));
                }
                objAdvertiseMaster.setWebsiteURL(jsonObject.getString("WebsiteURL"));
                objAdvertiseMaster.setAdvertisementType(jsonObject.getString("AdvertisementType"));
                objAdvertiseMaster.setlinktoMemberMasterIdCreatedBy(jsonObject.getInt("linktoMemberMasterIdCreatedBy"));
                dt = sdfDateTimeFormat.parse(jsonObject.getString("CreateDateTime"));
                objAdvertiseMaster.setCreateDateTime(sdfControlDateFormat.format(dt));
                if (!jsonObject.getString("linktoMemberMasterIdUpdatedBy").equals("null")) {
                    objAdvertiseMaster.setlinktoMemberMasterIdUpdatedBy(jsonObject.getInt("linktoMemberMasterIdUpdatedBy"));
                }
                objAdvertiseMaster.setIsEnabled(jsonObject.getBoolean("IsEnabled"));
                objAdvertiseMaster.setIsDeleted(jsonObject.getBoolean("IsDeleted"));
            }
            return objAdvertiseMaster;
        } catch (JSONException e) {
            return null;
        } catch (ParseException e) {
            return null;
        }
    }

    private ArrayList<AdvertiseMaster> SetListPropertiesFromJSONArray(JSONArray jsonArray) {
        ArrayList<AdvertiseMaster> lstAdvertiseMaster = new ArrayList<>();
        AdvertiseMaster objAdvertiseMaster;
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                objAdvertiseMaster = new AdvertiseMaster();
                objAdvertiseMaster.setAdvertiseMasterId(jsonArray.getJSONObject(i).getInt("AdvertiseMasterId"));
                if (jsonArray.getJSONObject(i).getString("AdvertiseText") != null && !jsonArray.getJSONObject(i).getString("AdvertiseText").equals("")&& !jsonArray.getJSONObject(i).getString("AdvertiseText").equals("null")) {
                    objAdvertiseMaster.setAdvertiseText(jsonArray.getJSONObject(i).getString("AdvertiseText"));
                }
                if (jsonArray.getJSONObject(i).getString("AdvertiseImageName") != null && !jsonArray.getJSONObject(i).getString("AdvertiseImageName").equals("")&& !jsonArray.getJSONObject(i).getString("AdvertiseImageName").equals("null")) {
                    objAdvertiseMaster.setAdvertiseImageName(jsonArray.getJSONObject(i).getString("AdvertiseImageName"));
                }
                objAdvertiseMaster.setWebsiteURL(jsonArray.getJSONObject(i).getString("WebsiteURL"));
                objAdvertiseMaster.setAdvertisementType(jsonArray.getJSONObject(i).getString("AdvertisementType"));
                objAdvertiseMaster.setlinktoMemberMasterIdCreatedBy(jsonArray.getJSONObject(i).getInt("linktoMemberMasterIdCreatedBy"));
                dt = sdfDateTimeFormat.parse(jsonArray.getJSONObject(i).getString("CreateDateTime"));
                objAdvertiseMaster.setCreateDateTime(sdfControlDateFormat.format(dt));
                if (!jsonArray.getJSONObject(i).getString("linktoMemberMasterIdUpdatedBy").equals("null")) {
                    objAdvertiseMaster.setlinktoMemberMasterIdUpdatedBy(jsonArray.getJSONObject(i).getInt("linktoMemberMasterIdUpdatedBy"));
                }
                objAdvertiseMaster.setIsEnabled(jsonArray.getJSONObject(i).getBoolean("IsEnabled"));
                objAdvertiseMaster.setIsDeleted(jsonArray.getJSONObject(i).getBoolean("IsDeleted"));

                lstAdvertiseMaster.add(objAdvertiseMaster);
            }
            return lstAdvertiseMaster;
        } catch (JSONException e) {
            return null;
        } catch (ParseException e) {
            return null;
        }
    }

    public void InsertAdvertiseMaster(AdvertiseMaster objAdvertiseMaster, Context context, final Fragment targetFragment) {
        try {
            JSONStringer stringer = new JSONStringer();
            stringer.object();

            stringer.key("advertiseMaster");
            stringer.object();

            stringer.key("AdvertiseText").value(objAdvertiseMaster.getAdvertiseText());
            stringer.key("AdvertiseImageName").value(objAdvertiseMaster.getAdvertiseImageName());
            stringer.key("WebsiteURL").value(objAdvertiseMaster.getWebsiteURL());
            stringer.key("AdvertisementType").value(objAdvertiseMaster.getAdvertisementType());
            stringer.key("linktoMemberMasterIdCreatedBy").value(objAdvertiseMaster.getlinktoMemberMasterIdCreatedBy());
            stringer.key("IsEnabled").value(objAdvertiseMaster.getIsEnabled());
            stringer.key("IsDeleted").value(objAdvertiseMaster.getIsDeleted());
            stringer.key("AdvertiseImageNameBytes").value(objAdvertiseMaster.getAdvertiseImageNameBytes());

            stringer.endObject();

            stringer.endObject();
            Log.e("json", " " + stringer.toString());

            String url = Service.Url + this.InsertAdvertiseMaster;

            RequestQueue queue = Volley.newRequestQueue(context);
            Log.e("url", " " + url);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(stringer.toString()), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    try {
                        Log.e("jsonObject", " " + jsonObject);
                        JSONObject jsonResponse = jsonObject.getJSONObject(InsertAdvertiseMaster + "Result");
                        if (jsonResponse != null) {
                            objAdvertiseAddListener = (AdvertiseAddListener) targetFragment;
                            objAdvertiseAddListener.AdvertiseAddResponse("0", SetClassPropertiesFromJSONObject(jsonResponse));
                        }
                    } catch (JSONException e) {
                        objAdvertiseAddListener = (AdvertiseAddListener) targetFragment;
                        objAdvertiseAddListener.AdvertiseAddResponse("-1", null);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    objAdvertiseAddListener = (AdvertiseAddListener) targetFragment;
                    objAdvertiseAddListener.AdvertiseAddResponse("-1", null);
                }
            });

            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                    30000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            queue.add(jsonObjectRequest);
        } catch (Exception ex) {
            objAdvertiseAddListener = (AdvertiseAddListener) targetFragment;
            objAdvertiseAddListener.AdvertiseAddResponse("-1", null);
        }
    }

    public void UpdateAdvertiseMaster(AdvertiseMaster objAdvertiseMaster, Context context, final Fragment targetFragment) {
        try {
            JSONStringer stringer = new JSONStringer();
            stringer.object();

            stringer.key("advertiseMaster");
            stringer.object();

            stringer.key("AdvertiseMasterId").value(objAdvertiseMaster.getAdvertiseMasterId());
            stringer.key("AdvertiseText").value(objAdvertiseMaster.getAdvertiseText());
            stringer.key("AdvertiseImageName").value(objAdvertiseMaster.getAdvertiseImageName());
            stringer.key("WebsiteURL").value(objAdvertiseMaster.getWebsiteURL());
            stringer.key("AdvertisementType").value(objAdvertiseMaster.getAdvertisementType());
            stringer.key("linktoMemberMasterIdUpdatedBy").value(objAdvertiseMaster.getlinktoMemberMasterIdUpdatedBy());
            stringer.key("AdvertiseImageNameBytes").value(objAdvertiseMaster.getAdvertiseImageNameBytes());

            stringer.endObject();

            stringer.endObject();
            Log.e("json", " " + stringer.toString());

            String url = Service.Url + this.UpdateAdvertiseMaster;

            RequestQueue queue = Volley.newRequestQueue(context);
            Log.e("url", " " + url);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(stringer.toString()), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    try {
                        Log.e("jsonObject", " " + jsonObject);
                        JSONObject jsonResponse = jsonObject.getJSONObject(UpdateAdvertiseMaster + "Result");
                        if (jsonResponse != null) {
                            objAdvertiseAddListener = (AdvertiseAddListener) targetFragment;
                            objAdvertiseAddListener.AdvertiseAddResponse("0", SetClassPropertiesFromJSONObject(jsonResponse));
                        }
                    } catch (JSONException e) {
                        objAdvertiseAddListener = (AdvertiseAddListener) targetFragment;
                        objAdvertiseAddListener.AdvertiseAddResponse("-1", null);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    objAdvertiseAddListener = (AdvertiseAddListener) targetFragment;
                    objAdvertiseAddListener.AdvertiseAddResponse("-1", null);
                }
            });

            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                    30000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            queue.add(jsonObjectRequest);
        } catch (Exception ex) {
            objAdvertiseAddListener = (AdvertiseAddListener) targetFragment;
            objAdvertiseAddListener.AdvertiseAddResponse("-1", null);
        }
    }

    public void UpdateAdvertiseMasterDisable(AdvertiseMaster objAdvertiseMaster, Context context, final Fragment targetFragment) {
        try {
            JSONStringer stringer = new JSONStringer();
            stringer.object();

            stringer.key("advertiseMaster");
            stringer.object();

            stringer.key("AdvertiseMasterId").value(objAdvertiseMaster.getAdvertiseMasterId());
            stringer.key("linktoMemberMasterIdUpdatedBy").value(objAdvertiseMaster.getlinktoMemberMasterIdUpdatedBy());
            stringer.key("IsEnabled").value(objAdvertiseMaster.getIsEnabled());

            stringer.endObject();

            stringer.endObject();

            Log.e("json", " " + stringer.toString());

            String url = Service.Url + this.UpdateAdvertiseMasterDisable;

            RequestQueue queue = Volley.newRequestQueue(context);
            Log.e("url", " " + url);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(stringer.toString()), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    try {
                        Log.e("jsonObject", " " + jsonObject);
                        JSONObject jsonResponse = jsonObject.getJSONObject(UpdateAdvertiseMasterDisable + "Result");
                        if (jsonResponse != null) {
                            objAdvertiseUpdateListener = (AdvertiseUpdateListener) targetFragment;
                            objAdvertiseUpdateListener.AdvertiseDisableResponse("0");
                        }
                    } catch (JSONException e) {
                        objAdvertiseUpdateListener = (AdvertiseUpdateListener) targetFragment;
                        objAdvertiseUpdateListener.AdvertiseDisableResponse("-1");
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    objAdvertiseUpdateListener = (AdvertiseUpdateListener) targetFragment;
                    objAdvertiseUpdateListener.AdvertiseDisableResponse("-1");
                }
            });

            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                    30000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            queue.add(jsonObjectRequest);
        } catch (Exception ex) {
            objAdvertiseUpdateListener = (AdvertiseUpdateListener) targetFragment;
            objAdvertiseUpdateListener.AdvertiseDisableResponse("-1");
        }
    }

    public void UpdateAdvertiseMasterDelete(AdvertiseMaster objAdvertiseMaster, Context context, final Fragment targetFragment) {
        try {
            JSONStringer stringer = new JSONStringer();
            stringer.object();

            stringer.key("advertiseMaster");
            stringer.object();

            stringer.key("AdvertiseMasterId").value(objAdvertiseMaster.getAdvertiseMasterId());
            stringer.key("linktoMemberMasterIdUpdatedBy").value(objAdvertiseMaster.getlinktoMemberMasterIdUpdatedBy());
            stringer.key("IsDeleted").value(objAdvertiseMaster.getIsDeleted());

            stringer.endObject();

            stringer.endObject();

            Log.e("json", " " + stringer.toString());

            String url = Service.Url + this.UpdateAdvertiseMasterDelete;

            RequestQueue queue = Volley.newRequestQueue(context);
            Log.e("url", " " + url);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(stringer.toString()), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    try {
                        Log.e("jsonObject", " " + jsonObject);
                        JSONObject jsonResponse = jsonObject.getJSONObject(UpdateAdvertiseMasterDelete + "Result");
                        if (jsonResponse != null) {
                            objAdvertiseUpdateListener = (AdvertiseUpdateListener) targetFragment;
                            objAdvertiseUpdateListener.AdvertiseDeleteResponse("0");
                        }
                    } catch (JSONException e) {
                        objAdvertiseUpdateListener = (AdvertiseUpdateListener) targetFragment;
                        objAdvertiseUpdateListener.AdvertiseDeleteResponse("-1");
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    objAdvertiseUpdateListener = (AdvertiseUpdateListener) targetFragment;
                    objAdvertiseUpdateListener.AdvertiseDeleteResponse("-1");
                }
            });

            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                    30000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            queue.add(jsonObjectRequest);
        } catch (Exception ex) {
            objAdvertiseUpdateListener = (AdvertiseUpdateListener) targetFragment;
            objAdvertiseUpdateListener.AdvertiseDeleteResponse("-1");
        }
    }

    public void SelectAllAdvertiseMasterPageWise(String currentPage, String pageSize, final Context context, final Fragment targetFragment) {
        url = Service.Url + this.SelectAllAdvertiseMasterPageWise + "/" + currentPage + "/" + pageSize;
        Log.e("url", " " + url);
        queue = Volley.newRequestQueue(context);
         jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, new JSONObject(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    Log.e("json", " " + jsonObject);
                    jsonArray = jsonObject.getJSONArray(SelectAllAdvertiseMasterPageWise + "Result");
                    if (jsonArray != null) {
                        if (targetFragment != null) {
                            objAdvretiseRequestListener = (AdvretiseRequestListener) targetFragment;
                        } else {
                            objAdvretiseRequestListener = (AdvretiseRequestListener) context;
                        }
                        objAdvretiseRequestListener.AdvretiseResponse(SetListPropertiesFromJSONArray(jsonArray), null);
                    }
                } catch (Exception e) {
                    if (targetFragment != null) {
                        objAdvretiseRequestListener = (AdvretiseRequestListener) targetFragment;
                    } else {
                        objAdvretiseRequestListener = (AdvretiseRequestListener) context;
                    }
                    objAdvretiseRequestListener.AdvretiseResponse(null, null);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (targetFragment != null) {
                    objAdvretiseRequestListener = (AdvretiseRequestListener) targetFragment;
                } else {
                    objAdvretiseRequestListener = (AdvretiseRequestListener) context;
                }
                objAdvretiseRequestListener.AdvretiseResponse(null, null);
            }

        });

        queue.add(jsonObjectRequest);
    }

    //region interface
    public interface AdvretiseRequestListener {
        void AdvretiseResponse(ArrayList<AdvertiseMaster> alAdvertiseMasters, AdvertiseMaster objAdvertiseMaster);
    }

    public interface AdvertiseAddListener {
        void AdvertiseAddResponse(String errorCode, AdvertiseMaster objAdvertiseMaster);
    }

    public interface AdvertiseUpdateListener {
        void AdvertiseDisableResponse(String errorCode);

        void AdvertiseDeleteResponse(String errorCode);
    }
    //endregion

}
