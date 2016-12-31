package com.arraybit.mym;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
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

public class SignInActivity extends AppCompatActivity implements View.OnClickListener, MemberJSONParser.MemberRequestListener {

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
        try {
            if (Service.CheckNet(this)) {
                FCMTokenGenerate();
            }

            //Sign in layout
            etUserName = (EditText) findViewById(R.id.etUserName);
            etPassword = (EditText) findViewById(R.id.etPassword);
            cbSignUp = (CompoundButton) findViewById(R.id.cbSignUp);
            cbForgotPassword = (CompoundButton) findViewById(R.id.cbForgotPassword);
            signInLayout = (LinearLayout) findViewById(R.id.signInLayout);
            tbPasswordShow = (ToggleButton) findViewById(R.id.tbPasswordShow);
            ibClear = (ImageView) findViewById(R.id.ibClear);

            //No Internet layout
            internetLayout = (LinearLayout) findViewById(R.id.internetLayout);
            Button btnRetry = (Button) internetLayout.findViewById(R.id.btnRetry);

            //Not approved user layout
            Button btnSignIn = (Button) findViewById(R.id.btnSignIn);

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
                    tbPasswordShow.setVisibility(View.VISIBLE);
                    if (etPassword.getText().toString().equals("")) {
                        tbPasswordShow.setVisibility(View.GONE);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            //Check internet Connection
            if (Service.CheckNet(this)) {
                internetLayout.setVisibility(View.GONE);
                signInLayout.setVisibility(View.VISIBLE);
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
                //Check internet Connection
                if (Service.CheckNet(this)) {
                    //Login Request
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
            Intent intent = new Intent(SignInActivity.this, RegistrationActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.tbPasswordShow) {
            if (tbPasswordShow.isChecked()) {
                etPassword.setInputType(InputType.TYPE_CLASS_TEXT);
            } else {
                etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
        } else if (v.getId() == R.id.btnRetry) {
            //Try for check internet
            if (Service.CheckNet(SignInActivity.this)) {
                FCMTokenGenerate();
                internetLayout.setVisibility(View.GONE);
                signInLayout.setVisibility(View.VISIBLE);
            }
        } else if (v.getId() == R.id.cbForgotPassword) {
            //Forgot password
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out, 0, R.anim.right_exit);
            fragmentTransaction.add(android.R.id.content, new ForgotPasswordFragment(), "ForgotPassword");
            fragmentTransaction.addToBackStack("ForgotPassword");
            fragmentTransaction.commit();
        }
    }

    @Override
    public void MemberResponse(String errorCode, MemberMaster objMemberMaster) {
        progressDialog.dismiss();
        this.objMemberMaster = objMemberMaster;
        // if Login successful
        if (objMemberMaster != null) {

            //Set data to preference
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

            //Redirect to Home Activity
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
                    && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals("ForgotPassword")) {
                getSupportFragmentManager().popBackStack("ForgotPassword", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        } else {
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
            IsValid = false;
        } else if (etPassword.getText().toString().equals("") && !etUserName.getText().toString().equals("")) {
            etPassword.setError("Enter " + getResources().getString(R.string.siPassword));
            IsValid = false;
            if (!Globals.IsValidEmail(etUserName.getText().toString())) {
                IsValid = false;
                etUserName.setError("Enter Valid Email Address");
            }
        } else {
            etUserName.setError(null);
            etPassword.setError(null);
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

            } else {
                Toast.makeText(getApplicationContext(), "This device does not support for Google Play Service!", Toast.LENGTH_LONG).show();
            }
        } else {
            FirebaseMessaging.getInstance().subscribeToTopic("news");

            token = FirebaseInstanceId.getInstance().getToken();
        }
    }
    //endregion
}
