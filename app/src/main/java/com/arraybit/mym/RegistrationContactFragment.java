package com.arraybit.mym;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.Toolbar;
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
import com.arraybit.modal.MemberMaster;
import com.arraybit.parser.MemberJSONParser;
import com.rey.material.widget.Button;
import com.rey.material.widget.EditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class RegistrationContactFragment extends Fragment implements View.OnClickListener, MemberJSONParser.MemberRequestListener {

    LinearLayout llContactdetail;
    EditText etHomeArea, etHomeCity, etHomeNumberStreet, etHomeZipCode, etHomeNearby, etPhoneHome;
    EditText etOfficeArea, etOfficeCity, etOfficeNumberStreet, etOfficeNearby, etOfficeZipCode, etPhoneOffice;
    AppCompatAutoCompleteTextView actHomeState, actOfficeState;
    Button btnUpdateContact;
    ProgressDialog progressDialog;
    MemberMaster objMemberMaster;
    CheckBox checkBox;

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

            //layout
            actHomeState = (AppCompatAutoCompleteTextView) view.findViewById(R.id.actHomeState);
            actOfficeState = (AppCompatAutoCompleteTextView) view.findViewById(R.id.actOfficeState);
            RequestStates();

            llContactdetail = (LinearLayout) view.findViewById(R.id.llContactdetail);
            etHomeArea = (EditText) view.findViewById(R.id.etHomeArea);
            etHomeCity = (EditText) view.findViewById(R.id.etHomeCity);
            etHomeNumberStreet = (EditText) view.findViewById(R.id.etHomeNumberStreet);
            etHomeNearby = (EditText) view.findViewById(R.id.etHomeNearby);
            etHomeZipCode = (EditText) view.findViewById(R.id.etHomeZipCode);
            etPhoneHome = (EditText) view.findViewById(R.id.etPhoneHome);
            etOfficeArea = (EditText) view.findViewById(R.id.etOfficeArea);
            etOfficeCity = (EditText) view.findViewById(R.id.etOfficeCity);
            etOfficeNumberStreet = (EditText) view.findViewById(R.id.etOfficeNumberStreet);
            etOfficeNearby = (EditText) view.findViewById(R.id.etOfficeNearby);
            etOfficeZipCode = (EditText) view.findViewById(R.id.etOfficeZipCode);
            etPhoneOffice = (EditText) view.findViewById(R.id.etPhoneOffice);
            checkBox = (CheckBox) view.findViewById(R.id.checkBox);
            btnUpdateContact = (Button) view.findViewById(R.id.btnUpdateContact);

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

            setHasOptionsMenu(true);
            if (getArguments() != null) {
                Bundle bundle = getArguments();
                if (bundle != null) {
                    objMemberMaster = bundle.getParcelable("MemberMaster");
                }
                if (objMemberMaster != null) {
                    SetData();
                }
            }
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
            // update conatct detail
            if (Service.CheckNet(getActivity())) {
                UpdateDetailRequest();
            } else {
                Globals.ShowSnackBar(v, getResources().getString(R.string.MsgCheckConnection), getActivity(), 1000);
            }
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
        if (item.getItemId() == android.R.id.home) {
            if (getActivity().getSupportFragmentManager().getBackStackEntryCount() != 0) {
                if (getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null && getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals("ContactDetail")) {
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            } else {
                getActivity().finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void MemberResponse(String errorCode, MemberMaster objMemberMaster) {
        progressDialog.dismiss();
        if (errorCode.equals("0")) {
            if (getActivity().getSupportFragmentManager().getBackStackEntryCount() != 0) {
                if (getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null && getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals("ContactDetail")) {
                    if (getActivity() instanceof DetailActivity) {
                        UpdateResponseListener objUpdateResponseListener = (UpdateResponseListener) getActivity();
                        if (objUpdateResponseListener != null) {
                            objUpdateResponseListener.UpdateResponse();
                        }
                    }
                }
            }
        } else {
            Globals.ShowSnackBar(llContactdetail, getResources().getString(R.string.MsgServerNotResponding), getActivity(), 1000);
        }
    }

    @Override
    public void MemberUpdate(String errorCode, MemberMaster objMemberMaster) {

    }

    // region Private Methods and Interface

    private void UpdateDetailRequest() {
        progressDialog = new ProgressDialog();
        progressDialog.show(getActivity().getSupportFragmentManager(), "");
        try {
            MemberJSONParser objMemberJSONParser = new MemberJSONParser();
            MemberMaster objMemberMaster = new MemberMaster();
            objMemberMaster.setMemberMasterId(Globals.memberMasterId);
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
            objMemberJSONParser.UpdateMemberMasterContactDetail(getActivity(), RegistrationContactFragment.this, objMemberMaster);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void SetData() {
        if (objMemberMaster.getHomeNumberStreet() != null) {
            etHomeNearby.setText(objMemberMaster.getHomeNearBy());
            etHomeArea.setText(objMemberMaster.getHomeArea());
            etHomeCity.setText(objMemberMaster.getHomeCity());
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
            etOfficeCity.setVisibility(View.GONE);
        }
        if (objMemberMaster.getOfficePhone() != null) {
            etPhoneOffice.setText(objMemberMaster.getOfficePhone());
        }
    }

    private boolean ValidateControls() {
        boolean IsValid = true;
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
        if (etHomeNumberStreet.getText().toString().equals("")) {
            etHomeNumberStreet.setError("Enter street name");
            IsValid = false;
        }
        if (etHomeNearby.getText().toString().equals("")) {
            etHomeNearby.setError("Enter nearby area");
            IsValid = false;
        }
        if (!checkBox.isChecked()) {
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
            if (etOfficeNumberStreet.getText().toString().equals("")) {
                etOfficeNumberStreet.setError("Enter street name");
                IsValid = false;
            }
            if (etOfficeNearby.getText().toString().equals("")) {
                etOfficeNearby.setError("Enter nearby area");
                IsValid = false;
            }
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
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.row_spinner, lstStrings);
                    actHomeState.setAdapter(adapter);
                    actOfficeState.setAdapter(adapter);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    interface UpdateResponseListener {
        void UpdateResponse();
    }

    //endregion

}
