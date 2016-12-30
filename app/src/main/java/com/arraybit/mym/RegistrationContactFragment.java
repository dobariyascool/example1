package com.arraybit.mym;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.modal.MemberMaster;
import com.arraybit.parser.MemberJSONParser;
import com.rey.material.widget.Button;
import com.rey.material.widget.EditText;

import java.util.ArrayList;

public class RegistrationContactFragment extends Fragment implements View.OnClickListener, MemberJSONParser.MemberRequestListener, MemberJSONParser.DetailRequestListener {

    LinearLayout llContactdetail;
    EditText etHomeArea, etHomeCity, etHomeCountry, etHomeNumberStreet, etHomeZipCode, etHomeNearby, etPhoneHome;
    //    etHomeState,, etOfficeState
    EditText etOfficeArea, etOfficeCity, etOfficeCountry, etOfficeNumberStreet, etOfficeNearby, etOfficeZipCode, etPhoneOffice;
    AppCompatAutoCompleteTextView actHomeState, actOfficeState;
    Button btnUpdateContact;
    ProgressDialog progressDialog;
    MemberMaster objMemberMaster;
    CheckBox checkBox;
    boolean isChecked = false, isUpdate = false;
    UpdateResponseListener objUpdateResponseListener;

    public RegistrationContactFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_registration_contact, container, false);
        try {
            //app_bar
            Toolbar app_bar = (Toolbar) view.findViewById(R.id.app_bar);
            if (app_bar != null) {
                if (Build.VERSION.SDK_INT >= 21) {
                    app_bar.setElevation(getActivity().getResources().getDimension(R.dimen.app_bar_elevation));
                }
                ((AppCompatActivity) getActivity()).setSupportActionBar(app_bar);
                ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.title_contact_fragment_registration));
            //end
//            Globals.SetToolBarBackground(getActivity(), app_bar, ContextCompat.getColor(getActivity(), R.color.colorPrimary), ContextCompat.getColor(getActivity(), android.R.color.white));
            actHomeState = (AppCompatAutoCompleteTextView) view.findViewById(R.id.actHomeState);
            actOfficeState= (AppCompatAutoCompleteTextView) view.findViewById(R.id.actOfficeState);
            RequestStates();

            llContactdetail = (LinearLayout) view.findViewById(R.id.llContactdetail);
            //EditText
            etHomeArea = (EditText) view.findViewById(R.id.etHomeArea);
            etHomeCity = (EditText) view.findViewById(R.id.etHomeCity);
//            etHomeCountry = (EditText) view.findViewById(R.id.etHomeCountry);
            etHomeNumberStreet = (EditText) view.findViewById(R.id.etHomeNumberStreet);
//            etHomeState = (EditText) view.findViewById(R.id.etHomeState);
            etHomeNearby = (EditText) view.findViewById(R.id.etHomeNearby);
            etHomeZipCode = (EditText) view.findViewById(R.id.etHomeZipCode);
            etPhoneHome = (EditText) view.findViewById(R.id.etPhoneHome);
            etOfficeArea = (EditText) view.findViewById(R.id.etOfficeArea);
            etOfficeCity = (EditText) view.findViewById(R.id.etOfficeCity);
//            etOfficeCountry = (EditText) view.findViewById(R.id.etOfficeCountry);
            etOfficeNumberStreet = (EditText) view.findViewById(R.id.etOfficeNumberStreet);
//            etOfficeState = (EditText) view.findViewById(R.id.etOfficeState);
            etOfficeNearby = (EditText) view.findViewById(R.id.etOfficeNearby);
            etOfficeZipCode = (EditText) view.findViewById(R.id.etOfficeZipCode);
            etPhoneOffice = (EditText) view.findViewById(R.id.etPhoneOffice);
            checkBox = (CheckBox) view.findViewById(R.id.checkBox);

            //button
            btnUpdateContact = (Button) view.findViewById(R.id.btnUpdateContact);
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

            setHasOptionsMenu(true);
//            if (getActivity() instanceof DetailActivity) {
            if (getArguments() != null) {
                Bundle bundle = getArguments();
                if (bundle != null) {
                    objMemberMaster = bundle.getParcelable("MemberMaster");
                    isUpdate = bundle.getBoolean("isUpdate", false);
                }
                if (isUpdate) {
                    btnUpdateContact.setText("Update");
                } else {
                    btnUpdateContact.setText("Next");
                }
                if (objMemberMaster != null) {
                    if (isUpdate) {
                        SetData();
                    }
                }
            }
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnUpdateContact) {
            if (!ValidateControls()) {
                Globals.ShowSnackBar(v, getResources().getString(R.string.MsgValidation), getActivity(), 1000);
                return;
            }
            if (Service.CheckNet(getActivity())) {
                UpdateDetailRequest();
            } else {
                Globals.ShowSnackBar(v, getResources().getString(R.string.MsgCheckConnection), getActivity(), 1000);
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
    public void onPrepareOptionsMenu(Menu menu) {
        if (getActivity() instanceof DetailActivity) {
            menu.findItem(R.id.share).setVisible(false);
            menu.findItem(R.id.changePassword).setVisible(false);
            menu.findItem(R.id.saveContact).setVisible(false);
            menu.findItem(R.id.accept).setVisible(false);
            menu.findItem(R.id.cancle).setVisible(false);
        }
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        if (getActivity() instanceof RegistartionFragmentActivity) {
//            if (item.getItemId() == android.R.id.home) {
//                getActivity().finish();
//            }
//        } else {
        if (item.getItemId() == android.R.id.home) {
            if (getActivity().getSupportFragmentManager().getBackStackEntryCount() != 0) {
                if (getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null && getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals("ContactDetail")) {
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            } else {
                getActivity().finish();
            }
        }
//        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void MemberResponse(String errorCode, MemberMaster objMemberMaster) {
        progressDialog.dismiss();
        if (errorCode.equals("0")) {
            if (Globals.startPage == 2) {
                new RequestSendTask().execute();
                Globals.startPage = 0;
                SharePreferenceManage objSharePreferenceManage = new SharePreferenceManage();
                objSharePreferenceManage.CreatePreference("LoginPreference", "startPage", "0", getActivity());
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                startActivity(intent);
                getActivity().finish();
//            }else
//            if (objMemberMaster != null) {
//                Log.e("sign in", " ");
//                SharePreferenceManage objSharePreferenceManage = new SharePreferenceManage();
//                objSharePreferenceManage.CreatePreference("LoginPreference", "MemberMasterId", String.valueOf(objMemberMaster.getMemberMasterId()), getActivity());
//                objSharePreferenceManage.CreatePreference("LoginPreference", "MemberEmail", objMemberMaster.getEmail(), getActivity());
//                objSharePreferenceManage.CreatePreference("LoginPreference", "MemberName", objMemberMaster.getMemberName(), getActivity());
//                objSharePreferenceManage.CreatePreference("LoginPreference", "MemberPassword", objMemberMaster.getPassword(), getActivity());
//                objSharePreferenceManage.CreatePreference("LoginPreference", "MemberType", objMemberMaster.getMemberType(), getActivity());
//                objSharePreferenceManage.CreatePreference("LoginPreference", "IsApproved", String.valueOf(objMemberMaster.getIsApproved()), getActivity());
//                objSharePreferenceManage.CreatePreference("LoginPreference", "MemberImage", objMemberMaster.getImageName(), getActivity());
//                objSharePreferenceManage.CreatePreference("LoginPreference", "Gender", objMemberMaster.getGender(), getActivity());
//                Globals.memberMasterId = objMemberMaster.getMemberMasterId();
//                Globals.memberType = objMemberMaster.getMemberType();
//                Globals.isAdmin = objMemberMaster.getMemberType().equals("Admin") ? true : false;
//                if (!isUpdate) {
//                    new RequestSendTask().execute();
//                }
//                Intent intent = new Intent(getActivity(), HomeActivity.class);
//                intent.putExtra("memberMaster", objMemberMaster);
//                startActivity(intent);
//                getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
//                getActivity().finish();
            } else {
                if (getActivity().getSupportFragmentManager().getBackStackEntryCount() != 0) {
                    if (getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null && getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals("ContactDetail")) {
                        if (getActivity() instanceof DetailActivity) {
                            objUpdateResponseListener = (RegistrationContactFragment.UpdateResponseListener) getActivity();
                            if (objUpdateResponseListener != null) {
                                objUpdateResponseListener.UpdateResponse();
                            }
                        } else {
                            getActivity().getSupportFragmentManager().popBackStack();
//                    getActivity().onBackPressed();
                        }
//                        getActivity().getSupportFragmentManager().popBackStack();
                    }
                }
            }
        } else {
            Globals.ShowSnackBar(llContactdetail, getResources().getString(R.string.MsgServerNotResponding), getActivity(), 1000);
        }
    }

    @Override
    public void MemberUpdate(String errorCode, MemberMaster objMemberMaster) {
        progressDialog.dismiss();
        if (errorCode.equals("0")) {
            if (objMemberMaster != null) {
                Log.e("sign in", " ");
                SharePreferenceManage objSharePreferenceManage = new SharePreferenceManage();
                objSharePreferenceManage.CreatePreference("LoginPreference", "MemberMasterId", String.valueOf(objMemberMaster.getMemberMasterId()), getActivity());
                objSharePreferenceManage.CreatePreference("LoginPreference", "MemberEmail", objMemberMaster.getEmail(), getActivity());
                objSharePreferenceManage.CreatePreference("LoginPreference", "MemberName", objMemberMaster.getMemberName(), getActivity());
                objSharePreferenceManage.CreatePreference("LoginPreference", "MemberPassword", objMemberMaster.getPassword(), getActivity());
                objSharePreferenceManage.CreatePreference("LoginPreference", "MemberType", objMemberMaster.getMemberType(), getActivity());
                objSharePreferenceManage.CreatePreference("LoginPreference", "IsApproved", String.valueOf(objMemberMaster.getIsApproved()), getActivity());
                objSharePreferenceManage.CreatePreference("LoginPreference", "MemberImage", objMemberMaster.getImageName(), getActivity());
                objSharePreferenceManage.CreatePreference("LoginPreference", "Gender", objMemberMaster.getGender(), getActivity());
                Globals.memberMasterId = objMemberMaster.getMemberMasterId();
                Globals.memberType = objMemberMaster.getMemberType();
                Globals.isAdmin = objMemberMaster.getMemberType().equals("Admin") ? true : false;

                RegistrationActivity.imagePhysicalNameBytes = null;
                RegistrationDetailActivity.imagePhysicalNameBytes = null;
                if (!isUpdate) {
                    new RequestSendTask().execute();
                }
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                intent.putExtra("memberMaster", objMemberMaster);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
                getActivity().finish();
            } else {
                Globals.ShowSnackBar(llContactdetail, getResources().getString(R.string.MsgServerNotResponding), getActivity(), 1000);
            }
        }
    }

    @Override
    public void QualificationResponse(ArrayList<String> lstStrings) {
        if (lstStrings != null && lstStrings.size() > 0) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.row_spinner, lstStrings);
            actHomeState.setAdapter(adapter);
            actOfficeState.setAdapter(adapter);
        }
    }

    @Override
    public void ProfessionResponse(ArrayList<String> lstStrings) {

    }


    // region Private Methods and Interface

    private void UpdateDetailRequest() {
//        if(isUpdate) {
        progressDialog = new ProgressDialog();
        progressDialog.show(getActivity().getSupportFragmentManager(), "");
//        }

        try {
            MemberJSONParser objMemberJSONParser = new MemberJSONParser();
            MemberMaster objMemberMaster = new MemberMaster();
//            if (!isUpdate) {
//                objMemberMaster = this.objMemberMaster;
//            } else {
            objMemberMaster.setMemberMasterId(Globals.memberMasterId);
//            }
            objMemberMaster.setHomeArea(etHomeArea.getText().toString());
            objMemberMaster.setHomeCity(etHomeCity.getText().toString());
//            objMemberMaster.setHomeCountry(etHomeCountry.getText().toString());
            objMemberMaster.setHomeNearBy(etHomeNearby.getText().toString());
            objMemberMaster.setHomeZipCode(etHomeZipCode.getText().toString());
            objMemberMaster.setHomePhone(etPhoneHome.getText().toString());
//            if (spinnerHomeState.getSelectedItemPosition() > 0) {
//                objMemberMaster.setHomeState(spinnerHomeState.getSelectedItem().toString());
//            }
            objMemberMaster.setHomeState(actHomeState.getText().toString());
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
            objMemberJSONParser.UpdateMemberMasterContactDetail(getActivity(), RegistrationContactFragment.this, objMemberMaster);
//            } else {
//                objMemberJSONParser.InsertMemberMasterDetail(getActivity(), RegistrationContactFragment.this, objMemberMaster);
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
            actHomeState.setText(objMemberMaster.getHomeState());
//            for (int i = 0; i < Globals.StateList.values().length; i++) {
//                if (Globals.StateList.getState(i).equals(objMemberMaster.getHomeState())) {
//                    spinnerHomeState.setSelection(i + 1);
//                }
//            }
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
//        } else {
//            etPhoneOffice.setVisibility(View.GONE);
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
        objMemeberJSONParser.SelectAllStates(getActivity(), this);
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
