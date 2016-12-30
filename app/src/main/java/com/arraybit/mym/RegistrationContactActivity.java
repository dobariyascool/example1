package com.arraybit.mym;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
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

import java.util.ArrayList;

public class RegistrationContactActivity extends AppCompatActivity implements View.OnClickListener, MemberJSONParser.MemberRequestListener, MemberJSONParser.DetailRequestListener {

    LinearLayout llContactdetail;
    EditText etHomeArea, etHomeCity, etHomeCountry, etHomeNumberStreet, etHomeZipCode, etHomeNearby, etPhoneHome;
    //    etHomeState, etOfficeState,
    EditText etOfficeArea, etOfficeCity, etOfficeCountry, etOfficeNumberStreet, etOfficeNearby, etOfficeZipCode, etPhoneOffice;
    AppCompatAutoCompleteTextView actHomeState, actOfficeState;
    Button btnUpdateContact;
    ProgressDialog progressDialog;
    MemberMasterNew objMemberMaster;
    CheckBox checkBox;
    boolean isChecked = false, isUpdate = false;
    RegistrationContactFragment.UpdateResponseListener objUpdateResponseListener;
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
//            Globals.SetToolBarBackground(RegistrationContactActivity.this, app_bar, ContextCompat.getColor(RegistrationContactActivity.this, R.color.colorPrimary), ContextCompat.getColor(RegistrationContactActivity.this, android.R.color.white));
            actHomeState = (AppCompatAutoCompleteTextView) findViewById(R.id.actHomeState);
            actOfficeState = (AppCompatAutoCompleteTextView) findViewById(R.id.actOfficeState);
            RequestStates();
            llContactdetail = (LinearLayout) findViewById(R.id.llContactdetail);
            //EditText
            etHomeArea = (EditText) findViewById(R.id.etHomeArea);
            etHomeCity = (EditText) findViewById(R.id.etHomeCity);
//            etHomeCountry = (EditText) findViewById(R.id.etHomeCountry);
            etHomeNumberStreet = (EditText) findViewById(R.id.etHomeNumberStreet);
//            etHomeState = (EditText) findViewById(R.id.etHomeState);
            etHomeNearby = (EditText) findViewById(R.id.etHomeNearby);
            etHomeZipCode = (EditText) findViewById(R.id.etHomeZipCode);
            etPhoneHome = (EditText) findViewById(R.id.etPhoneHome);
            etOfficeArea = (EditText) findViewById(R.id.etOfficeArea);
            etOfficeCity = (EditText) findViewById(R.id.etOfficeCity);
//            etOfficeCountry = (EditText) findViewById(R.id.etOfficeCountry);
            etOfficeNumberStreet = (EditText) findViewById(R.id.etOfficeNumberStreet);
//            etOfficeState = (EditText) findViewById(R.id.etOfficeState);
            etOfficeNearby = (EditText) findViewById(R.id.etOfficeNearby);
            etOfficeZipCode = (EditText) findViewById(R.id.etOfficeZipCode);
            etPhoneOffice = (EditText) findViewById(R.id.etPhoneOffice);
            checkBox = (CheckBox) findViewById(R.id.checkBox);

            //button
            btnUpdateContact = (Button) findViewById(R.id.btnUpdateContact);
            //end

            //Spinner
            //

            //compound button
            //end

            //event
            btnUpdateContact.setOnClickListener(this);
            checkBox.setOnClickListener(this);
            actHomeState.setOnClickListener(this);
            actOfficeState.setOnClickListener(this);
            //end
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

//            etPhoneHome.addTextChangedListener(new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                }
//
//                @Override
//                public void onTextChanged(CharSequence s, int start, int before, int count) {
//                    if (etPhoneHome.getText().toString().length() < 4) {
//                        etPhoneHome.setText("+91 ");
//                        etPhoneHome.setSelection(etPhoneHome.getText().toString().length());
//                    }
//                }
//
//                @Override
//                public void afterTextChanged(Editable s) {
//
//                }
//            });
//
//            etPhoneOffice.addTextChangedListener(new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                }
//
//                @Override
//                public void onTextChanged(CharSequence s, int start, int before, int count) {
//                    if (etPhoneOffice.getText().toString().length() < 4) {
//                        etPhoneOffice.setText("+91 ");
//                        etPhoneOffice.setSelection(etPhoneOffice.getText().toString().length());
//                    }
//                }
//
//                @Override
//                public void afterTextChanged(Editable s) {
//
//                }
//            });

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
//                        checkBox.setChecked(true);
//                        isChecked = true;
                        etOfficeArea.setVisibility(View.GONE);
                        etPhoneOffice.setVisibility(View.GONE);
                        etOfficeNearby.setVisibility(View.GONE);
                        etOfficeZipCode.setVisibility(View.GONE);
                        etOfficeNumberStreet.setVisibility(View.GONE);
                        actOfficeState.setVisibility(View.GONE);
//                        etOfficeState.setVisibility(View.GONE);
//                        etOfficeCountry.setVisibility(View.GONE);
                        etOfficeCity.setVisibility(View.GONE);
                    } else {
//                        checkBox.setChecked(false);
//                        isChecked = false;
                        etOfficeArea.setVisibility(View.VISIBLE);
                        etPhoneOffice.setVisibility(View.VISIBLE);
                        etOfficeNearby.setVisibility(View.VISIBLE);
                        etOfficeZipCode.setVisibility(View.VISIBLE);
                        actOfficeState.setVisibility(View.VISIBLE);
//                        etOfficeState.setVisibility(View.VISIBLE);
                        etOfficeNumberStreet.setVisibility(View.VISIBLE);
//                        etOfficeCountry.setVisibility(View.VISIBLE);
                        etOfficeCity.setVisibility(View.VISIBLE);
                    }
                }
            });

//            if (RegistrationContactActivity.this instanceof DetailActivity) {
            if (getIntent() != null) {
                objMemberMaster = getIntent().getParcelableExtra("MemberMaster");
                isUpdate = getIntent().getBooleanExtra("isUpdate", false);
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


//                if (isUpdate) {
//                    btnUpdateContact.setText("Update");
//                } else {
                btnUpdateContact.setText("Finish");
//                }

//                if (objMemberMaster != null) {
//                    if (isUpdate) {
//                        SetData();
//                    }
//                }
            }
//            }
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
            if (Service.CheckNet(RegistrationContactActivity.this)) {
                UpdateDetailRequest();
            } else {
                Globals.ShowSnackBar(v, getResources().getString(R.string.MsgCheckConnection), RegistrationContactActivity.this, 1000);
            }
//        } else if (v.getId() == R.id.checkBox) {
//            if (!isChecked) {
//                checkBox.setChecked(true);
//                isChecked = true;
//                etOfficeArea.setVisibility(View.GONE);
//                etPhoneOffice.setVisibility(View.GONE);
//                etOfficeStreet.setVisibility(View.GONE);
//                etOfficeState.setVisibility(View.GONE);
//                etOfficeNo.setVisibility(View.GONE);
//                etOfficeCountry.setVisibility(View.GONE);
//                etOfficeCity.setVisibility(View.GONE);
//            } else {
//                checkBox.setChecked(false);
//                isChecked = false;
//                etOfficeArea.setVisibility(View.VISIBLE);
//                etPhoneOffice.setVisibility(View.VISIBLE);
//                etOfficeStreet.setVisibility(View.VISIBLE);
//                etOfficeState.setVisibility(View.VISIBLE);
//                etOfficeNo.setVisibility(View.VISIBLE);
//                etOfficeCountry.setVisibility(View.VISIBLE);
//                etOfficeCity.setVisibility(View.VISIBLE);
//            }
        } else if (v.getId() == R.id.actHomeState) {
            actHomeState.showDropDown();
        } else if (v.getId() == R.id.actOfficeState) {
            actOfficeState.showDropDown();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        if (RegistrationContactActivity.this instanceof RegistartionFragmentActivity) {
//            if (item.getItemId() == android.R.id.home) {
//                finish();
//            }
//        } else {
        if (item.getItemId() == android.R.id.home) {
//            if (getSupportFragmentManager().getBackStackEntryCount() != 0) {
//                if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals("ContactDetail")) {
//                    getSupportFragmentManager().popBackStack();
//                }
//            } else {
            finish();
//            }
        }
//        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void MemberResponse(String errorCode, MemberMaster objMemberMaster) {
        progressDialog.dismiss();
        if (errorCode.equals("0")) {
//            if (Globals.startPage == 2) {
            new RequestSendTask().execute();
            Globals.startPage = 0;
            SharePreferenceManage objSharePreferenceManage = new SharePreferenceManage();
            objSharePreferenceManage.CreatePreference("LoginPreference", "startPage", "0", RegistrationContactActivity.this);
            Intent intent = new Intent(RegistrationContactActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
//            }else
//            if (objMemberMaster != null) {
//                Log.e("sign in", " ");
//                SharePreferenceManage objSharePreferenceManage = new SharePreferenceManage();
//                objSharePreferenceManage.CreatePreference("LoginPreference", "MemberMasterId", String.valueOf(objMemberMaster.getMemberMasterId()), RegistrationContactActivity.this);
//                objSharePreferenceManage.CreatePreference("LoginPreference", "MemberEmail", objMemberMaster.getEmail(), RegistrationContactActivity.this);
//                objSharePreferenceManage.CreatePreference("LoginPreference", "MemberName", objMemberMaster.getMemberName(), RegistrationContactActivity.this);
//                objSharePreferenceManage.CreatePreference("LoginPreference", "MemberPassword", objMemberMaster.getPassword(), RegistrationContactActivity.this);
//                objSharePreferenceManage.CreatePreference("LoginPreference", "MemberType", objMemberMaster.getMemberType(), RegistrationContactActivity.this);
//                objSharePreferenceManage.CreatePreference("LoginPreference", "IsApproved", String.valueOf(objMemberMaster.getIsApproved()), RegistrationContactActivity.this);
//                objSharePreferenceManage.CreatePreference("LoginPreference", "MemberImage", objMemberMaster.getImageName(), RegistrationContactActivity.this);
//                objSharePreferenceManage.CreatePreference("LoginPreference", "Gender", objMemberMaster.getGender(), RegistrationContactActivity.this);
//                Globals.memberMasterId = objMemberMaster.getMemberMasterId();
//                Globals.memberType = objMemberMaster.getMemberType();
//                Globals.isAdmin = objMemberMaster.getMemberType().equals("Admin") ? true : false;
//                if (!isUpdate) {
//                    new RequestSendTask().execute();
//                }
//                Intent intent = new Intent(RegistrationContactActivity.this, HomeActivity.class);
//                intent.putExtra("memberMaster", objMemberMaster);
//                startActivity(intent);
//                overridePendingTransition(R.anim.right_in, R.anim.left_out);
////                finish();
//            } else {
//                if (getSupportFragmentManager().getBackStackEntryCount() != 0) {
//                    if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals("ContactDetail")) {
//                        if (RegistrationContactActivity.this instanceof DetailActivity) {
//                            objUpdateResponseListener = (RegistrationContactFragment.UpdateResponseListener) RegistrationContactActivity.this;
//                            if (objUpdateResponseListener != null) {
//                                objUpdateResponseListener.UpdateResponse();
//                            }
//                        } else {
//                            getSupportFragmentManager().popBackStack();
////                    onBackPressed();
//                        }
////                        getSupportFragmentManager().popBackStack();
//                    }
//                }
//            }
        } else {
            Globals.ShowSnackBar(llContactdetail, getResources().getString(R.string.MsgServerNotResponding), RegistrationContactActivity.this, 1000);
        }
    }

    @Override
    public void MemberUpdate(String errorCode, MemberMaster objMemberMaster) {
        progressDialog.dismiss();
        if (errorCode.equals("0")) {
            if (objMemberMaster != null) {
                Log.e("sign in", " ");
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
//                if (!isUpdate) {
                new RequestSendTask().execute();
//                }
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

    @Override
    public void QualificationResponse(ArrayList<String> lstStrings) {
        if (lstStrings != null && lstStrings.size() > 0) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(RegistrationContactActivity.this, R.layout.row_spinner, lstStrings);
            actHomeState.setAdapter(adapter);
            actOfficeState.setAdapter(adapter);
        }
    }

    @Override
    public void ProfessionResponse(ArrayList<String> lstStrings) {

    }

    // region Private Methods

    private void UpdateDetailRequest() {
//        if(isUpdate) {
        progressDialog = new ProgressDialog();
        progressDialog.show(getSupportFragmentManager(), "");
//        }

        try {
            MemberJSONParser objMemberJSONParser = new MemberJSONParser();
            MemberMasterNew objMemberMaster = new MemberMasterNew();
//            if (!isUpdate) {
            objMemberMaster = this.objMemberMaster;
//            } else {
//                objMemberMaster.setMemberMasterId(Globals.memberMasterId);
//            }

            objMemberMaster.setHomeArea(etHomeArea.getText().toString());
            objMemberMaster.setHomeCity(etHomeCity.getText().toString());
//            objMemberMaster.setHomeCountry(etHomeCountry.getText().toString());
            objMemberMaster.setHomeNearBy(etHomeNearby.getText().toString());
            objMemberMaster.setHomeZipCode(etHomeZipCode.getText().toString());
            objMemberMaster.setHomePhone(etPhoneHome.getText().toString());
            objMemberMaster.setHomeState(actHomeState.getText().toString());
//            objMemberMaster.setHomeState(etHomeState.getText().toString());
            objMemberMaster.setHomeNumberStreet(etHomeNumberStreet.getText().toString());
            if (!checkBox.isChecked()) {
                objMemberMaster.setOfficeArea(etOfficeArea.getText().toString());
                objMemberMaster.setOfficeCity(etOfficeCity.getText().toString());
//                objMemberMaster.setOfficeCountry(etOfficeCountry.getText().toString());
                objMemberMaster.setOfficeNearBy(etOfficeNearby.getText().toString());
                objMemberMaster.setOfficeZipCode(etOfficeZipCode.getText().toString());
//                objMemberMaster.setOfficeState(etOfficeState.getText().toString());
                objMemberMaster.setOfficeState(actOfficeState.getText().toString());
                objMemberMaster.setOfficeNumberStreet(etOfficeNumberStreet.getText().toString());
                objMemberMaster.setOfficePhone(etPhoneOffice.getText().toString());
            }
//            if (isUpdate) {
//                objMemberJSONParser.UpdateMemberMasterContactDetail(RegistrationContactFragment.this, objMemberMaster);
//            } else {
            objMemberJSONParser.InsertMemberMasterDetail(RegistrationContactActivity.this, null, objMemberMaster, lstMemberRelativesTren);
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void SetData() {
        if (objMemberMaster.getHomeNumberStreet() != null) {
            etHomeNearby.setText(objMemberMaster.getHomeNearBy());
            etHomeArea.setText(objMemberMaster.getHomeArea());
            etHomeCity.setText(objMemberMaster.getHomeCity());
//            etHomeCountry.setText(objMemberMaster.getHomeCountry());
//            etHomeState.setText(objMemberMaster.getHomeState());
            actHomeState.setText(objMemberMaster.getHomeState());
            etHomeNumberStreet.setText(objMemberMaster.getHomeNumberStreet());
            etHomeZipCode.setText(objMemberMaster.getHomeZipCode());
        }
        if (objMemberMaster.getHomePhone() != null) {
            etPhoneHome.setText(objMemberMaster.getHomePhone());
        }
        if (objMemberMaster.getOfficeNumberStreet() != null) {
            checkBox.setChecked(false);
            etOfficeNearby.setText(objMemberMaster.getOfficeNearBy());
            etOfficeArea.setText(objMemberMaster.getOfficeArea());
            etOfficeCity.setText(objMemberMaster.getOfficeCity());
//            etOfficeCountry.setText(objMemberMaster.getOfficeCountry());
//            etOfficeState.setText(objMemberMaster.getOfficeState());
            actOfficeState.setText(objMemberMaster.getOfficeState());
            etOfficeNumberStreet.setText(objMemberMaster.getOfficeNumberStreet());
            etOfficeZipCode.setText(objMemberMaster.getOfficeZipCode());
        } else {
            checkBox.setChecked(true);
            etOfficeArea.setVisibility(View.GONE);
            etPhoneOffice.setVisibility(View.GONE);
            etOfficeNearby.setVisibility(View.GONE);
            etOfficeZipCode.setVisibility(View.GONE);
            etOfficeNumberStreet.setVisibility(View.GONE);
            actOfficeState.setVisibility(View.GONE);
//            etOfficeState.setVisibility(View.GONE);
//            etOfficeCountry.setVisibility(View.GONE);
            etOfficeCity.setVisibility(View.GONE);
        }
        if (objMemberMaster.getOfficePhone() != null) {
            etPhoneOffice.setText(objMemberMaster.getOfficePhone());
        } else {
            etPhoneOffice.setVisibility(View.GONE);
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
        MemberJSONParser objMemeberJSONParser = new MemberJSONParser();
        objMemeberJSONParser.SelectAllStates(RegistrationContactActivity.this, null);
    }

    interface UpdateResponseListener {
        void UpdateResponse();
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
