package com.arraybit.mym;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.modal.MemberMaster;
import com.arraybit.modal.MemberMasterNew;
import com.arraybit.modal.MemberRelativesTran;
import com.arraybit.parser.MemberJSONParser;
import com.rey.material.widget.Button;
import com.rey.material.widget.EditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class RegistrationContactActivity extends AppCompatActivity implements View.OnClickListener, MemberJSONParser.MemberRequestListener {

    LinearLayout llContactdetail;
    EditText etHomeArea, etHomeCity, etHomeNumberStreet, etHomeZipCode, etHomeNearby, etPhoneHome;
    EditText etOfficeArea, etOfficeCity, etOfficeNumberStreet, etOfficeNearby, etOfficeZipCode, etPhoneOffice;
    AppCompatAutoCompleteTextView actHomeState, actOfficeState;
    Button btnUpdateContact;
    ProgressDialog progressDialog;
    MemberMasterNew objMemberMaster;
    CheckBox checkBox;
    ArrayList<MemberRelativesTran> lstMemberRelativesTren = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_contact);

        try {
            //app_bar
            Toolbar app_bar = (Toolbar) findViewById(R.id.app_bar);
            if (app_bar != null) {
                if (Build.VERSION.SDK_INT >= 21) {
                    app_bar.setElevation(getResources().getDimension(R.dimen.app_bar_elevation));
                }
                setSupportActionBar(app_bar);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
            getSupportActionBar().setTitle(getResources().getString(R.string.title_contact_fragment_registration));
            //end

            actHomeState = (AppCompatAutoCompleteTextView) findViewById(R.id.actHomeState);
            actOfficeState = (AppCompatAutoCompleteTextView) findViewById(R.id.actOfficeState);
            RequestStates();
            llContactdetail = (LinearLayout) findViewById(R.id.llContactdetail);
            etHomeArea = (EditText) findViewById(R.id.etHomeArea);
            etHomeCity = (EditText) findViewById(R.id.etHomeCity);
            etHomeNumberStreet = (EditText) findViewById(R.id.etHomeNumberStreet);
            etHomeNearby = (EditText) findViewById(R.id.etHomeNearby);
            etHomeZipCode = (EditText) findViewById(R.id.etHomeZipCode);
            etPhoneHome = (EditText) findViewById(R.id.etPhoneHome);
            etOfficeArea = (EditText) findViewById(R.id.etOfficeArea);
            etOfficeCity = (EditText) findViewById(R.id.etOfficeCity);
            etOfficeNumberStreet = (EditText) findViewById(R.id.etOfficeNumberStreet);
            etOfficeNearby = (EditText) findViewById(R.id.etOfficeNearby);
            etOfficeZipCode = (EditText) findViewById(R.id.etOfficeZipCode);
            etPhoneOffice = (EditText) findViewById(R.id.etPhoneOffice);
            checkBox = (CheckBox) findViewById(R.id.checkBox);

            btnUpdateContact = (Button) findViewById(R.id.btnUpdateContact);

            btnUpdateContact.setOnClickListener(this);
            checkBox.setOnClickListener(this);
            actHomeState.setOnClickListener(this);
            actOfficeState.setOnClickListener(this);
            checkBox.setChecked(true);

            etPhoneHome.setSelection(etPhoneHome.getText().toString().length());
            etPhoneOffice.setSelection(etPhoneOffice.getText().toString().length());

            actHomeState.setText("Maharashtra");
            actOfficeState.setText("Maharashtra");

            actHomeState.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        actHomeState.showDropDown();
                    }
                }
            });

            actOfficeState.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        actOfficeState.showDropDown();
                    }
                }
            });


            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        etOfficeArea.setVisibility(View.GONE);
                        etPhoneOffice.setVisibility(View.GONE);
                        etOfficeNearby.setVisibility(View.GONE);
                        etOfficeZipCode.setVisibility(View.GONE);
                        etOfficeNumberStreet.setVisibility(View.GONE);
                        actOfficeState.setVisibility(View.GONE);
                        etOfficeCity.setVisibility(View.GONE);
                    } else {
                        etOfficeArea.setVisibility(View.VISIBLE);
                        etPhoneOffice.setVisibility(View.VISIBLE);
                        etOfficeNearby.setVisibility(View.VISIBLE);
                        etOfficeZipCode.setVisibility(View.VISIBLE);
                        actOfficeState.setVisibility(View.VISIBLE);
                        etOfficeNumberStreet.setVisibility(View.VISIBLE);
                        etOfficeCity.setVisibility(View.VISIBLE);
                    }
                }
            });

            if (getIntent() != null) {
                objMemberMaster = getIntent().getParcelableExtra("MemberMaster");
                MemberRelativesTran objMemberRelativesTran;
                if (getIntent().getParcelableExtra("MemberRelative0") != null) {
                    objMemberRelativesTran = getIntent().getParcelableExtra("MemberRelative0");
                    lstMemberRelativesTren.add(objMemberRelativesTran);
                }
                if (getIntent().getParcelableExtra("MemberRelative1") != null) {
                    objMemberRelativesTran = getIntent().getParcelableExtra("MemberRelative1");
                    lstMemberRelativesTren.add(objMemberRelativesTran);
                }
                if (getIntent().getParcelableExtra("MemberRelative2") != null) {
                    objMemberRelativesTran = getIntent().getParcelableExtra("MemberRelative2");
                    lstMemberRelativesTren.add(objMemberRelativesTran);
                }
                if (getIntent().getParcelableExtra("MemberRelative3") != null) {
                    objMemberRelativesTran = getIntent().getParcelableExtra("MemberRelative3");
                    lstMemberRelativesTren.add(objMemberRelativesTran);
                }
                if (getIntent().getParcelableExtra("MemberRelative4") != null) {
                    objMemberRelativesTran = getIntent().getParcelableExtra("MemberRelative4");
                    lstMemberRelativesTren.add(objMemberRelativesTran);
                }
                if (getIntent().getParcelableExtra("MemberRelative5") != null) {
                    objMemberRelativesTran = getIntent().getParcelableExtra("MemberRelative5");
                    lstMemberRelativesTren.add(objMemberRelativesTran);
                }
                btnUpdateContact.setText("Finish");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnUpdateContact) {
            if (!ValidateControls()) {
                Globals.ShowSnackBar(v, getResources().getString(R.string.MsgValidation), RegistrationContactActivity.this, 1000);
                return;
            }

            // Insert new request to database
            if (Service.CheckNet(RegistrationContactActivity.this)) {
                UpdateDetailRequest();
            } else {
                Globals.ShowSnackBar(v, getResources().getString(R.string.MsgCheckConnection), RegistrationContactActivity.this, 1000);
            }
        } else if (v.getId() == R.id.actHomeState) {
            actHomeState.showDropDown();
        } else if (v.getId() == R.id.actOfficeState) {
            actOfficeState.showDropDown();
        }
    }

    //Option menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void MemberResponse(String errorCode, MemberMaster objMemberMaster) {

    }

    @Override
    public void MemberUpdate(String errorCode, MemberMaster objMemberMaster) {
        progressDialog.dismiss();
        if (errorCode.equals("0")) {
            if (objMemberMaster != null) {
                SharePreferenceManage objSharePreferenceManage = new SharePreferenceManage();
                objSharePreferenceManage.CreatePreference("LoginPreference", "MemberMasterId", String.valueOf(objMemberMaster.getMemberMasterId()), RegistrationContactActivity.this);
                objSharePreferenceManage.CreatePreference("LoginPreference", "MemberEmail", objMemberMaster.getEmail(), RegistrationContactActivity.this);
                objSharePreferenceManage.CreatePreference("LoginPreference", "MemberName", objMemberMaster.getMemberName(), RegistrationContactActivity.this);
                objSharePreferenceManage.CreatePreference("LoginPreference", "MemberPassword", objMemberMaster.getPassword(), RegistrationContactActivity.this);
                objSharePreferenceManage.CreatePreference("LoginPreference", "MemberType", objMemberMaster.getMemberType(), RegistrationContactActivity.this);
                objSharePreferenceManage.CreatePreference("LoginPreference", "IsApproved", String.valueOf(objMemberMaster.getIsApproved()), RegistrationContactActivity.this);
                objSharePreferenceManage.CreatePreference("LoginPreference", "MemberImage", objMemberMaster.getImageName(), RegistrationContactActivity.this);
                objSharePreferenceManage.CreatePreference("LoginPreference", "Gender", objMemberMaster.getGender(), RegistrationContactActivity.this);
                Globals.memberMasterId = objMemberMaster.getMemberMasterId();
                Globals.memberType = objMemberMaster.getMemberType();
                Globals.isAdmin = objMemberMaster.getMemberType().equals("Admin") ? true : false;

                //send notification to admin
                new RequestSendTask().execute();

                //redirect to home activity
                Intent intent = new Intent(RegistrationContactActivity.this, HomeActivity.class);
                intent.putExtra("memberMaster", objMemberMaster);
                intent.setAction(Intent.ACTION_MAIN);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                finish();
            } else {
                Globals.ShowSnackBar(llContactdetail, getResources().getString(R.string.MsgServerNotResponding), RegistrationContactActivity.this, 1000);
            }
        } else if (errorCode.equals("-2")) {
            Toast.makeText(RegistrationContactActivity.this, "Email is already registered.", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(RegistrationContactActivity.this, "Signup not completed. Please Try again.", Toast.LENGTH_LONG).show();
        }
    }

    // region Private Methods

    private void UpdateDetailRequest() {
        progressDialog = new ProgressDialog();
        progressDialog.show(getSupportFragmentManager(), "");

        try {
            MemberJSONParser objMemberJSONParser = new MemberJSONParser();
            MemberMasterNew objMemberMaster = new MemberMasterNew();
            objMemberMaster = this.objMemberMaster;

            objMemberMaster.setHomeArea(etHomeArea.getText().toString());
            objMemberMaster.setHomeCity(etHomeCity.getText().toString());
            objMemberMaster.setHomeNearBy(etHomeNearby.getText().toString());
            objMemberMaster.setHomeZipCode(etHomeZipCode.getText().toString());
            objMemberMaster.setHomePhone(etPhoneHome.getText().toString());
            objMemberMaster.setHomeState(actHomeState.getText().toString());
            objMemberMaster.setHomeNumberStreet(etHomeNumberStreet.getText().toString());
            if (!checkBox.isChecked()) {
                objMemberMaster.setOfficeArea(etOfficeArea.getText().toString());
                objMemberMaster.setOfficeCity(etOfficeCity.getText().toString());
                objMemberMaster.setOfficeNearBy(etOfficeNearby.getText().toString());
                objMemberMaster.setOfficeZipCode(etOfficeZipCode.getText().toString());
                objMemberMaster.setOfficeState(actOfficeState.getText().toString());
                objMemberMaster.setOfficeNumberStreet(etOfficeNumberStreet.getText().toString());
                objMemberMaster.setOfficePhone(etPhoneOffice.getText().toString());
            }
            objMemberJSONParser.InsertMemberMasterDetail(RegistrationContactActivity.this, null, objMemberMaster, lstMemberRelativesTren);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean ValidateControls() {
        boolean IsValid = true;

//        if (etHomeState.getText().toString().equals("")) {
//            etHomeState.setError("Enter state");
//            IsValid = false;
//        }
        if (actHomeState.getText().toString().equals("")) {
            actHomeState.setError("Enter State");
            IsValid = false;
        }
        if (etHomeCity.getText().toString().equals("")) {
            etHomeCity.setError("Enter city");
            IsValid = false;
        }
        if (etHomeArea.getText().toString().equals("")) {
            etHomeArea.setError("Enter area");
            IsValid = false;
        }
        if (etHomeZipCode.getText().toString().equals("")) {
            etHomeZipCode.setError("Enter zipcode");
            IsValid = false;
        }
//        if (etHomeCountry.getText().toString().equals("")) {
//            etHomeCountry.setError("Enter country");
//            IsValid = false;
//        }
        if (etHomeNumberStreet.getText().toString().equals("")) {
            etHomeNumberStreet.setError("Enter street name");
            IsValid = false;
        }
        if (etHomeNearby.getText().toString().equals("")) {
            etHomeNearby.setError("Enter nearby area");
            IsValid = false;
//        } else if (etPhoneHome.getText().toString().equals("")) {
//            etPhoneHome.setError("Enter phone no");
//            IsValid = false;
        }
//        if (!etPhoneHome.getText().toString().equals("") && etPhoneHome.getText().toString().substring(4).length() != 10) {
//            etPhoneHome.setError("Enter 10 digit phone no");
//            IsValid = false;
//        } else {
//            etPhoneHome.clearError();
//        }
        if (!checkBox.isChecked()) {
//            if (etOfficeState.getText().toString().equals("")) {
//                etOfficeState.setError("Enter state");
//                IsValid = false;
//            }
            if (actOfficeState.getText().toString().equals("")) {
                actOfficeState.setError("Enter State");
                IsValid = false;
            }
            if (etOfficeCity.getText().toString().equals("")) {
                etOfficeCity.setError("Enter city");
                IsValid = false;
            }
            if (etOfficeArea.getText().toString().equals("")) {
                etOfficeArea.setError("Enter area");
                IsValid = false;
            }
            if (etOfficeZipCode.getText().toString().equals("")) {
                etOfficeZipCode.setError("Enter zipcode");
                IsValid = false;
            }
//            if (etOfficeCountry.getText().toString().equals("")) {
//                etOfficeCountry.setError("Enter country");
//                IsValid = false;
//            }
            if (etOfficeNumberStreet.getText().toString().equals("")) {
                etOfficeNumberStreet.setError("Enter street name");
                IsValid = false;
            }
            if (etOfficeNearby.getText().toString().equals("")) {
                etOfficeNearby.setError("Enter nearby area");
                IsValid = false;
//            } else if (etPhoneOffice.getText().toString().equals("")) {
//                etPhoneOffice.setError("Enter phone no");
//                IsValid = false;
            }
//            if (!etPhoneOffice.getText().toString().equals("") && etPhoneOffice.getText().toString().substring(4).length() != 10) {
//                etPhoneOffice.setError("Enter 10 digit phone no");
//                IsValid = false;
//            } else {
//                etPhoneOffice.clearError();
//            }
        }

        return IsValid;
    }

    private void RequestStates() {
        ArrayList<String> lstStrings = new ArrayList<>();
        try {
            JSONObject jsonObject = Service.HttpGetService(Service.Url + "SelectAllStates");
            if (jsonObject != null) {
                JSONArray jsonArray = jsonObject.getJSONArray("SelectAllStatesResult");
                if (jsonArray != null) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        lstStrings.add(jsonArray.getString(i));
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(RegistrationContactActivity.this, R.layout.row_spinner, lstStrings);
                    actHomeState.setAdapter(adapter);
                    actOfficeState.setAdapter(adapter);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //endregion

    //region LoadingTask

    class RequestSendTask extends AsyncTask {
        public String SendNotificationsNewRequest = "SendNotificationsNewRequest";

        @Override
        protected Object doInBackground(Object[] objects) {
            Service.HttpGetService(Service.Url + this.SendNotificationsNewRequest + "/" + Globals.memberMasterId);
            return null;
        }

    }

    //endregion
}
