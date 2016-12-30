package com.arraybit.parser;

import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.modal.MemberRelativesTran;

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
/// JSONParser for MemberRelativesTran
/// </summary>
public class MemberRelativesJSONParser {

    SimpleDateFormat sdfControlDateFormat = new SimpleDateFormat(Globals.DateFormat, Locale.US);
    Date dt = null;
    SimpleDateFormat sdfDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

    public ArrayList<MemberRelativesTran> SetListPropertiesFromJSONArray(JSONArray jsonArray) {
        ArrayList<MemberRelativesTran> lstMemberRelativesTran = new ArrayList<>();
        MemberRelativesTran objMemberRelativesTran;
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                objMemberRelativesTran = new MemberRelativesTran();
                objMemberRelativesTran.setMemberRelativesTranId(jsonArray.getJSONObject(i).getInt("MemberRelativesTranId"));
                objMemberRelativesTran.setlinktoMemberMasterId(jsonArray.getJSONObject(i).getInt("linktoMemberMasterId"));
                if (jsonArray.getJSONObject(i).getString("ImageName") != null && !jsonArray.getJSONObject(i).getString("ImageName").equals("") && !jsonArray.getJSONObject(i).getString("ImageName").equals("null")) {
                    objMemberRelativesTran.setImageName(jsonArray.getJSONObject(i).getString("ImageName"));
                }
                objMemberRelativesTran.setRelativeName(jsonArray.getJSONObject(i).getString("RelativeName"));
                objMemberRelativesTran.setGender(jsonArray.getJSONObject(i).getString("Gender"));
                dt = sdfDateFormat.parse(jsonArray.getJSONObject(i).getString("BirthDate"));
                objMemberRelativesTran.setBirthDate(sdfControlDateFormat.format(dt));
                objMemberRelativesTran.setRelation(jsonArray.getJSONObject(i).getString("Relation"));

                lstMemberRelativesTran.add(objMemberRelativesTran);
            }
            return lstMemberRelativesTran;
        } catch (JSONException e) {
            return null;
        } catch (ParseException e) {
            return null;
        }
    }

}
