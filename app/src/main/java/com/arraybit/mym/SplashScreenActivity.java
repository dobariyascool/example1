package com.arraybit.mym;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.RelativeLayout;

import com.arraybit.global.Globals;
import com.arraybit.global.MarshMallowPermission;
import com.arraybit.global.Service;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.modal.MemberMaster;
import com.arraybit.parser.MemberJSONParser;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

public class SplashScreenActivity extends AppCompatActivity implements MemberJSONParser.MemberRequestListener {
    RelativeLayout splashScreenLayout;
    SharePreferenceManage objSharePreferenceManage;
//    ProgressDialog progressDialog;
    MemberMaster objMemberMaster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        splashScreenLayout = (RelativeLayout) findViewById(R.id.splashScreenLayout);
        objSharePreferenceManage = new SharePreferenceManage();
        if (objSharePreferenceManage.GetPreference("LoginPreference", "MemberName", SplashScreenActivity.this) != null && objSharePreferenceManage.GetPreference("LoginPreference", "MemberPassword", SplashScreenActivity.this) != null) {
            if (Service.CheckNet(SplashScreenActivity.this)) {
                FCMTokenGenerate();
            }
            if (Service.CheckNet(SplashScreenActivity.this)) {
                LoginRequest();
            }
        } else {
//            Globals.ClearUserPreference(SplashScreenActivity.this);
//            Globals.ChangeActivity(SplashScreenActivity.this, SignInActivity.class, true);
            HandlerMethod();
        }
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void MemberResponse(String errorCode, MemberMaster objMemberMaster) {
//        progressDialog.dismiss();
        this.objMemberMaster = objMemberMaster;
        if (errorCode.equals("0")) {
            if (objMemberMaster != null) {
                objSharePreferenceManage = new SharePreferenceManage();
                objSharePreferenceManage.CreatePreference("LoginPreference", "MemberMasterId", String.valueOf(objMemberMaster.getMemberMasterId()), this);
                objSharePreferenceManage.CreatePreference("LoginPreference", "MemberEmail", objMemberMaster.getEmail(), this);
                objSharePreferenceManage.CreatePreference("LoginPreference", "MemberName", objMemberMaster.getMemberName(), this);
                objSharePreferenceManage.CreatePreference("LoginPreference", "MemberPassword", objMemberMaster.getPassword(), this);
                objSharePreferenceManage.CreatePreference("LoginPreference", "MemberType", objMemberMaster.getMemberType(), this);
                objSharePreferenceManage.CreatePreference("LoginPreference", "IsApproved", String.valueOf(objMemberMaster.getIsApproved()), this);
                objSharePreferenceManage.CreatePreference("LoginPreference", "MemberImage", objMemberMaster.getImageName(), this);
                objSharePreferenceManage.CreatePreference("LoginPreference", "Gender", objMemberMaster.getGender(), this);
                Globals.memberMasterId = objMemberMaster.getMemberMasterId();
                Globals.memberType = objMemberMaster.getMemberType();
                Globals.isAdmin = objMemberMaster.getMemberType().equals("Admin") ? true : false;
                HandlerMethod();
            } else {
//            Globals.ShowSnackBar(splashScreenLayout, getResources().getString(R.string.siLoginFailedMsg), SignInActivity.this, 1000);
//                Globals.ClearUserPreference(SplashScreenActivity.this);
                Globals.ChangeActivity(SplashScreenActivity.this, SignInActivity.class, true);
//                HandlerMethod();
            }
        } else {
            RequestMemberMasterLogout();
        }

    }

    @Override
    public void MemberUpdate(String errorCode, MemberMaster objMemberMaster) {
//        progressDialog.dismiss();
//        if (errorCode.equals("0")) {
//            Globals.ClearUserPreference(SplashScreenActivity.this);
            Globals.ChangeActivity(SplashScreenActivity.this, SignInActivity.class, true);
//            HandlerMethod();
//        }else
//        {

//        }
    }

    private void RequestMemberMasterLogout() {
//        if (progressDialog.isAdded()) {
//            progressDialog.dismiss();
//        }
//        progressDialog.show(getSupportFragmentManager(), "");
        MemberJSONParser objMemeberJSONParser = new MemberJSONParser();
        objMemeberJSONParser.UpdateMemberMasterLogOut(SplashScreenActivity.this, Integer.parseInt(objSharePreferenceManage.GetPreference("LoginPreference", "MemberMasterId", SplashScreenActivity.this)));
    }

    private void FCMTokenGenerate() {

        //Checking play service is available or not
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());

        //if play service is not available
        if (ConnectionResult.SUCCESS != resultCode) {
            //If play service is supported but not installed
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                //Displaying message that play service is not installed
//                Toast.makeText(getApplicationContext(), "Google Play Service is not install/enabled in this device!", Toast.LENGTH_LONG).show();
                GooglePlayServicesUtil.showErrorNotification(resultCode, getApplicationContext());

                //If play service is not supported
                //Displaying an error message
            } else {
//                Toast.makeText(getApplicationContext(), "This device does not support for Google Play Service!", Toast.LENGTH_LONG).show();
            }

            //If play service is available
        } else {
            FirebaseMessaging.getInstance().subscribeToTopic("news");

            SignInActivity.token = FirebaseInstanceId.getInstance().getToken();
            Log.e("token", " " + SignInActivity.token);
        }

    }

    private void LoginRequest() {
//        progressDialog = new ProgressDialog();
//        progressDialog.show(getSupportFragmentManager(), "");
        MemberJSONParser objMemeberJSONParser = new MemberJSONParser();
        objMemeberJSONParser.SelectMemberMaster(SplashScreenActivity.this, objSharePreferenceManage.GetPreference("LoginPreference", "MemberEmail", SplashScreenActivity.this),
                objSharePreferenceManage.GetPreference("LoginPreference", "MemberPassword", SplashScreenActivity.this), SignInActivity.token);
    }

    private void HandlerMethod() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    MarshMallowPermission marshMallowPermission = new MarshMallowPermission(SplashScreenActivity.this);
                    if (!marshMallowPermission.checkPermissionForCamera()) {
                        marshMallowPermission.requestPermissionForCamera();
                    }
                    if (!marshMallowPermission.checkPermissionForExternalStorage()) {
                        marshMallowPermission.requestPermissionForExternalStorage();
                    }
                }

                objSharePreferenceManage = new SharePreferenceManage();
                if (objSharePreferenceManage.GetPreference("LoginPreference", "MemberName", SplashScreenActivity.this) != null && objSharePreferenceManage.GetPreference("LoginPreference", "MemberPassword", SplashScreenActivity.this) != null) {
                    String userName = objSharePreferenceManage.GetPreference("LoginPreference", "MemberName", SplashScreenActivity.this);
                    String userPassword = objSharePreferenceManage.GetPreference("LoginPreference", "MemberPassword", SplashScreenActivity.this);
                    if ((!userName.isEmpty() && !userPassword.isEmpty())) {
                        Globals.memberMasterId = Integer.parseInt(objSharePreferenceManage.GetPreference("LoginPreference", "MemberMasterId", SplashScreenActivity.this));
                        Globals.memberType = objSharePreferenceManage.GetPreference("LoginPreference", "MemberType", SplashScreenActivity.this);
                        Globals.isAdmin = objSharePreferenceManage.GetPreference("LoginPreference", "MemberType", SplashScreenActivity.this).equals("Admin");
//                        if (objSharePreferenceManage.GetPreference("LoginPreference", "startPage", SplashScreenActivity.this) != null) {
//                            Globals.startPage = Integer.parseInt(objSharePreferenceManage.GetPreference("LoginPreference", "startPage", SplashScreenActivity.this));
//                        }
//                        if (Globals.startPage == 0) {
                        Intent intent = new Intent(SplashScreenActivity.this, HomeActivity.class);
                        MemberMaster objMemberMaster = new MemberMaster();
                        objMemberMaster.setMemberName(userName);
                        objMemberMaster.setMemberMasterId(Globals.memberMasterId);
                        objMemberMaster.setMemberType(Globals.memberType);
                        objMemberMaster.setEmail(objSharePreferenceManage.GetPreference("LoginPreference", "MemberEmail", SplashScreenActivity.this));
                        objMemberMaster.setImageName(objSharePreferenceManage.GetPreference("LoginPreference", "MemberImage", SplashScreenActivity.this));
                        objMemberMaster.setIsApproved(Boolean.parseBoolean(objSharePreferenceManage.GetPreference("LoginPreference", "IsApproved", SplashScreenActivity.this)));
                        intent.putExtra("memberMaster", objMemberMaster);
                        startActivity(intent);
                        overridePendingTransition(R.anim.right_in, R.anim.left_out);
                        finish();
//                        Globals.ChangeActivity(SplashScreenActivity.this, HomeActivity.class, true);
//                        } else {
//                            Globals.ChangeActivity(SplashScreenActivity.this, RegistartionFragmentActivity.class, true);
//                        }
                    } else {
                        Globals.ChangeActivity(SplashScreenActivity.this, SignInActivity.class, true);
                    }
                } else {
                    Globals.ChangeActivity(SplashScreenActivity.this, SignInActivity.class, true);
                }
            }
        }, 3000);
    }

}