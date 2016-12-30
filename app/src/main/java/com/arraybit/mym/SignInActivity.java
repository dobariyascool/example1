package com.arraybit.mym;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.modal.MemberMaster;
import com.arraybit.parser.MemberJSONParser;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.rey.material.widget.Button;
import com.rey.material.widget.CompoundButton;

import java.util.ArrayList;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener, MemberJSONParser.MemberRequestListener{

//    public static String token = "hjhjghj:guh-vgug_bhj:6576HJGjjhghjbj-";
    public static String token = "12";
    EditText etUserName, etPassword;
    CompoundButton cbSignUp, cbForgotPassword;
    ImageView ibClear;
    ToggleButton tbPasswordShow;
    View view;
    ProgressDialog progressDialog;
    SharePreferenceManage objSharePreferenceManage;
    MemberJSONParser objMemeberJSONParser;
    MemberMaster objMemberMaster;
    LinearLayout internetLayout, signInLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        try {
            if (Service.CheckNet(this)) {
                FCMTokenGenerate();
            }

            etUserName = (EditText) findViewById(R.id.etUserName);
            etPassword = (EditText) findViewById(R.id.etPassword);
            cbSignUp = (CompoundButton) findViewById(R.id.cbSignUp);
            cbForgotPassword= (CompoundButton) findViewById(R.id.cbForgotPassword);
            signInLayout = (LinearLayout) findViewById(R.id.signInLayout);

            internetLayout = (LinearLayout) findViewById(R.id.internetLayout);
            Button btnRetry = (Button) internetLayout.findViewById(R.id.btnRetry);

            Button btnSignIn = (Button) findViewById(R.id.btnSignIn);
            ibClear = (ImageView) findViewById(R.id.ibClear);
            tbPasswordShow = (ToggleButton) findViewById(R.id.tbPasswordShow);
            btnSignIn.setOnClickListener(this);
            btnRetry.setOnClickListener(this);

            ibClear.setOnClickListener(this);
            cbSignUp.setOnClickListener(this);
            cbForgotPassword.setOnClickListener(this);
            tbPasswordShow.setOnClickListener(this);
            Globals.HideKeyBoard(SignInActivity.this, signInLayout);

            etUserName.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
//                    etUserName.clearError();
                    ibClear.setVisibility(View.VISIBLE);
                    if (etUserName.getText().toString().equals("")) {
                        ibClear.setVisibility(View.GONE);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            etPassword.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
//                    etPassword.clearError();
                    tbPasswordShow.setVisibility(View.VISIBLE);
                    if (etPassword.getText().toString().equals("")) {
                        tbPasswordShow.setVisibility(View.GONE);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            if (Service.CheckNet(this)) {
                internetLayout.setVisibility(View.GONE);
                signInLayout.setVisibility(View.VISIBLE);
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            } else {
                internetLayout.setVisibility(View.VISIBLE);
                Globals.SetErrorLayout(internetLayout, true, getResources().getString(R.string.MsgCheckConnection), null, R.drawable.wifi_off);
                signInLayout.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        view = v;
        if (v.getId() == R.id.btnSignIn) {
            if (!ValidateControls()) {
                Globals.ShowSnackBar(v, getResources().getString(R.string.MsgValidation), SignInActivity.this, 1000);
            } else {
                if (Service.CheckNet(this)) {
                    LoginRequest();
                } else {
                    Globals.ShowSnackBar(v, getResources().getString(R.string.MsgCheckConnection), this, 1000);
                }
            }
            Globals.HideKeyBoard(this, view);
        } else if (v.getId() == R.id.ibClear) {
            etUserName.setText("");
            ibClear.setVisibility(View.GONE);
        } else if (v.getId() == R.id.cbSignUp) {
            etUserName.setError(null);
            etPassword.setError(null);
//            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//            fragmentTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out, 0, R.anim.right_exit);
//            fragmentTransaction.add(R.id.llSignin, new RegistrationFragment(), "Registration");
//            fragmentTransaction.addToBackStack("Registration");
//            fragmentTransaction.commit();
            Intent intent = new Intent(SignInActivity.this, RegistrationActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.tbPasswordShow) {
            if (tbPasswordShow.isChecked()) {
                etPassword.setInputType(InputType.TYPE_CLASS_TEXT);
            } else {
                etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
        } else if (v.getId() == R.id.btnRetry) {
            if (Service.CheckNet(SignInActivity.this)) {
//                CheckUserNamePassword();
                FCMTokenGenerate();
                internetLayout.setVisibility(View.GONE);
                signInLayout.setVisibility(View.VISIBLE);
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            }
        }else if (v.getId() == R.id.cbForgotPassword) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out, 0, R.anim.right_exit);
            fragmentTransaction.add(android.R.id.content, new ForgotPasswordFragment(), "ForgotPassword");
            fragmentTransaction.addToBackStack("ForgotPassword");
            fragmentTransaction.commit();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == RESULT_OK) {
                if (getSupportFragmentManager().getBackStackEntryCount() != 0) {
//                    if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
                    if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals("Registration")) {
                        RegistrationFragment registrationFragment = (RegistrationFragment) getSupportFragmentManager().findFragmentByTag("Registration");
                        registrationFragment.SelectImage(requestCode, data);
                    } else if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals("PersonalDetail")) {
                        RegistrationDetailFragment registrationDetailFragment = (RegistrationDetailFragment) getSupportFragmentManager().findFragmentByTag("PersonalDetail");
                        registrationDetailFragment.SelectImage(requestCode, data);
                    }
//                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void MemberResponse(String errorCode, MemberMaster objMemberMaster) {
        progressDialog.dismiss();
//        if (isIntegrationLogin) {
        this.objMemberMaster = objMemberMaster;
        if (objMemberMaster != null) {
//            if (objMemberMaster.getProfession() != null) {
//                if (objMemberMaster.getHomeNumberStreet() != null) {
                    objSharePreferenceManage = new SharePreferenceManage();
                    objSharePreferenceManage.CreatePreference("LoginPreference", "MemberMasterId", String.valueOf(objMemberMaster.getMemberMasterId()), this);
                    objSharePreferenceManage.CreatePreference("LoginPreference", "MemberEmail", objMemberMaster.getEmail(), this);
                    objSharePreferenceManage.CreatePreference("LoginPreference", "MemberName", objMemberMaster.getMemberName(), this);
                    objSharePreferenceManage.CreatePreference("LoginPreference", "MemberPassword", objMemberMaster.getPassword(), this);
                    objSharePreferenceManage.CreatePreference("LoginPreference", "MemberType", objMemberMaster.getMemberType(), this);
                    objSharePreferenceManage.CreatePreference("LoginPreference", "IsApproved", String.valueOf(objMemberMaster.getIsApproved()), this);
                    objSharePreferenceManage.CreatePreference("LoginPreference", "MemberImage", objMemberMaster.getImageName(), this);
                    objSharePreferenceManage.CreatePreference("LoginPreference", "Gender", objMemberMaster.getGender(), this);
//                } else {
//                    Globals.startPage = 2;
//                }
//            } else {
//                Globals.startPage = 1;
//            }
            Globals.memberMasterId = objMemberMaster.getMemberMasterId();
            Globals.memberType = objMemberMaster.getMemberType();
            Globals.isAdmin = objMemberMaster.getMemberType().equals("Admin") ? true : false;
            Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
            intent.putExtra("memberMaster", objMemberMaster);
            startActivity(intent);
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
            finish();
        } else {
            Globals.ShowSnackBar(view, getResources().getString(R.string.siLoginFailedMsg), SignInActivity.this, 1000);
        }
    }

    @Override
    public void MemberUpdate(String errorCode, MemberMaster objMemberMaster) {

    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() != 0) {
            if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                    && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals("Registration")) {
                getSupportFragmentManager().popBackStack("Registration", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            } else if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                    && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals("PersonalDetail")) {
                getSupportFragmentManager().popBackStack("PersonalDetail", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }else if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                    && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals("ContactDetail")) {
                getSupportFragmentManager().popBackStack("ContactDetail", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }else if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                    && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals("ForgotPassword")) {
                getSupportFragmentManager().popBackStack("ForgotPassword", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        }else
        {
            finish();
        }
    }

    //region Private Methods
    private void LoginRequest() {
        progressDialog = new ProgressDialog();
        progressDialog.show(getSupportFragmentManager(), "");
        objMemeberJSONParser = new MemberJSONParser();
        objMemeberJSONParser.SelectMemberMaster(SignInActivity.this, etUserName.getText().toString().trim(), etPassword.getText().toString().trim(), token);
    }

    private boolean ValidateControls() {
        boolean IsValid = true;

        if (etUserName.getText().toString().equals("") && etPassword.getText().toString().equals("")) {
            etUserName.setError("Enter " + getResources().getString(R.string.siEmail));
            etPassword.setError("Enter " + getResources().getString(R.string.siPassword));
            IsValid = false;
        } else if (etUserName.getText().toString().equals("") && !etPassword.getText().toString().equals("")) {
            etUserName.setError("Enter " + getResources().getString(R.string.siEmail));
//            etPassword.clearError();
            IsValid = false;
        } else if (etPassword.getText().toString().equals("") && !etUserName.getText().toString().equals("")) {
            etPassword.setError("Enter " + getResources().getString(R.string.siPassword));
//            etUserName.clearError();
            IsValid = false;
            if(!Globals.IsValidEmail(etUserName.getText().toString())) {
                IsValid = false;
                etUserName.setError("Enter Valid Email Address");
            }

        } else {
//            etUserName.clearError();
//            etPassword.clearError();
        }

        return IsValid;
    }

    private void FCMTokenGenerate() {

        //Checking play service is available or not
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());

        //if play service is not available
        if (ConnectionResult.SUCCESS != resultCode) {
            //If play service is supported but not installed
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                //Displaying message that play service is not installed
                Toast.makeText(getApplicationContext(), "Google Play Service is not install/enabled in this device!", Toast.LENGTH_LONG).show();
                GooglePlayServicesUtil.showErrorNotification(resultCode, getApplicationContext());

                //If play service is not supported
                //Displaying an error message
            } else {
                Toast.makeText(getApplicationContext(), "This device does not support for Google Play Service!", Toast.LENGTH_LONG).show();
            }

            //If play service is available
        } else {
            FirebaseMessaging.getInstance().subscribeToTopic("news");

            token = FirebaseInstanceId.getInstance().getToken();
            Log.e("token1"," "+token);

        }

    }

    //endregion
}
