package com.arraybit.mym;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.modal.MemberMaster;
import com.arraybit.parser.MemberJSONParser;
import com.rey.material.widget.Button;
import com.rey.material.widget.EditText;


public class UserProfileFragment extends Fragment implements View.OnClickListener, MemberJSONParser.MemberRequestListener {

    EditText etFirstName, etMobile, etMobile1, etBirthDate, etLastName;
    RadioButton rbMale, rbFemale;
    Button btnUpdate;
    ProgressDialog progressDialog;
    View view;
    UpdateResponseListener objUpdateResponseListener;
    SharePreferenceManage objSharePreferenceManage = new SharePreferenceManage();
    MemberMaster objMemberMaster;
    LinearLayout myprofile;

    public UserProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        Toolbar app_bar = (Toolbar) view.findViewById(R.id.app_bar);
        if (app_bar != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(app_bar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        app_bar.setTitle(getActivity().getResources().getString(R.string.title_fragment_your_profile));
        setHasOptionsMenu(true);

        if (objSharePreferenceManage.GetPreference("LoginPreference", "MemberName", getActivity()) != null) {
            app_bar.setTitle(objSharePreferenceManage.GetPreference("LoginPreference", "MemberName", getActivity()));
        }

        Bundle bundle = getArguments();
        objMemberMaster = bundle.getParcelable("memberMaster");

        myprofile = (LinearLayout) view.findViewById(R.id.myprofile);

        etFirstName = (EditText) view.findViewById(R.id.etFirstNameUser);
        etLastName = (EditText) view.findViewById(R.id.etLastNameUser);
        etMobile = (EditText) view.findViewById(R.id.etMobileUser);
        etMobile1 = (EditText) view.findViewById(R.id.etMobileUser1);
        etBirthDate = (EditText) view.findViewById(R.id.etDateOfBirthUser);
        etBirthDate.setInputType(InputType.TYPE_NULL);

        etMobile.setSelection(etMobile.getText().toString().length());
        etMobile1.setSelection(etMobile1.getText().toString().length());

        rbMale = (RadioButton) view.findViewById(R.id.rbMaleUser);
        rbFemale = (RadioButton) view.findViewById(R.id.rbFemaleUser);

        btnUpdate = (Button) view.findViewById(R.id.btnUpdate);

        btnUpdate.setOnClickListener(this);
        etBirthDate.setOnClickListener(this);
        setHasOptionsMenu(true);

        if (objMemberMaster != null) {
            app_bar.setTitle(objMemberMaster.getMemberName());
            if (objMemberMaster.getMemberName() != null && !objMemberMaster.getMemberName().equals("")) {
                String str = objMemberMaster.getMemberName();
                String[] splited = str.split("\\s+");
                etFirstName.setText(splited[0]);
                if (splited.length > 1) {
                    etLastName.setText(splited[1]);
                }
            }
            if (objMemberMaster.getPhone1() != null && !objMemberMaster.getPhone1().equals("")) {
                etMobile.setText(etMobile.getText().toString() + objMemberMaster.getPhone1());
            }
            if (objMemberMaster.getPhone2() != null && !objMemberMaster.getPhone2().equals("")) {
                etMobile1.setText(etMobile1.getText().toString() + objMemberMaster.getPhone2());
            }
            if (objMemberMaster.getBirthDate() != null && !objMemberMaster.getBirthDate().equals("")) {
                etBirthDate.setText(objMemberMaster.getBirthDate());
            }
            if (objMemberMaster.getGender() != null && !objMemberMaster.getGender().equals("")) {
                if (objMemberMaster.getGender().equals("Male")) {
                    rbMale.setChecked(true);
                    rbFemale.setChecked(false);
                } else if (objMemberMaster.getGender().equals("Female")) {
                    rbMale.setChecked(false);
                    rbFemale.setChecked(true);
                }
            }
        }

        etMobile.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    Globals.HideKeyBoard(getActivity(), v);
                }
                return false;
            }
        });

        etMobile1.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    Globals.HideKeyBoard(getActivity(), v);
                }
                return false;
            }
        });

        etMobile.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                return false;
            }
        });

        etMobile.addTextChangedListener(new TextWatcher() {
            String mobile;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                 mobile= etMobile.getText().toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                 mobile = etMobile.getText().toString();
                if (etMobile.length() < 4) {
                    etMobile.setText("+91 ");
                    etMobile.setSelection(etMobile.getText().toString().length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().startsWith("+91 ",0)){
                    etMobile.setText(mobile);
                    Selection.setSelection(etMobile.getText(), 4);
                }
            }
        });

        etMobile1.addTextChangedListener(new TextWatcher() {
            String mobile;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                mobile= etMobile1.getText().toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etMobile1.getText().toString().length() < 4) {
                    etMobile1.setText("+91 ");
                    etMobile1.setSelection(etMobile1.getText().toString().length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().startsWith("+91 ",0)){
                    etMobile1.setText(mobile);
                    Selection.setSelection(etMobile1.getText(), 4);
                }
            }
        });

        etBirthDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Globals.ShowDatePickerDialog(etBirthDate, getActivity(), false);
                }
            }
        });

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Globals.HideKeyBoard(getActivity(), getView());
//                getActivity().onBackPressed();
                if (getActivity() instanceof DetailActivity) {
                    objUpdateResponseListener = (UpdateResponseListener) getActivity();
                    if (objUpdateResponseListener != null) {
                        objUpdateResponseListener.UpdateResponse();
                    }
                } else {
                    getActivity().getSupportFragmentManager().popBackStack();
//                    getActivity().onBackPressed();
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.share).setVisible(false);
        menu.findItem(R.id.changePassword).setVisible(false);
        menu.findItem(R.id.saveContact).setVisible(false);
        menu.findItem(R.id.accept).setVisible(false);
        menu.findItem(R.id.cancle).setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }

    public void EditTextOnClick(View view) {
        Globals.ShowDatePickerDialog(etBirthDate, getActivity(), false);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnUpdate) {
            Globals.HideKeyBoard(getActivity(), v);
            if (!ValidateControls()) {
                Globals.ShowSnackBar(v, getResources().getString(R.string.MsgValidation), getActivity(), 1000);
                return;
            }
            if (Service.CheckNet(getActivity())) {
                UpdateUserProfileRequest();
            } else {
                Globals.ShowSnackBar(btnUpdate, getResources().getString(R.string.MsgCheckConnection), getActivity(), 1000);
            }
        } else if (v.getId() == R.id.etDateOfBirth) {
            EditTextOnClick(v);
        }
    }

    @Override
    public void MemberResponse(String errorCode, MemberMaster objMemberMaster) {
        progressDialog.dismiss();
        SetError(errorCode, objMemberMaster);
    }

    @Override
    public void MemberUpdate(String errorCode, MemberMaster objMemberMaster) {
        progressDialog.dismiss();
    }

    // region Private Methods

    private void UpdateUserProfileRequest() {
        progressDialog = new ProgressDialog();
        progressDialog.show(getActivity().getSupportFragmentManager(), "");

        try {
            MemberJSONParser objMemberJSONParser = new MemberJSONParser();
            MemberMaster objMemberMaster = new MemberMaster();
            objMemberMaster.setMemberMasterId(Globals.memberMasterId);
            objMemberMaster.setMemberName(etFirstName.getText().toString().trim() + " " + etLastName.getText().toString().trim());
            objMemberMaster.setPhone1(etMobile.getText().toString().substring(4));
            objMemberMaster.setPhone2(etMobile1.getText().toString().substring(4));
//            if (imageName != null && !imageName.equals("")) {
////                strImageName = imageName.substring(0, imageName.lastIndexOf(".")) + "_" + simpleDateFormat.format(new Date()) + imageName.substring(imageName.lastIndexOf("."), imageName.length());
//                strImageName = imageName;
//                objMemberMaster.setImageName(strImageName);
//                objMemberMaster.setImageNameBytes(imagePhysicalNameBytes);
//            }
            if (rbMale.isChecked()) {
                objMemberMaster.setGender(rbMale.getText().toString());
            }
            if (rbFemale.isChecked()) {
                objMemberMaster.setGender(rbFemale.getText().toString());
            }
            if (!etBirthDate.getText().toString().isEmpty()) {
                objMemberMaster.setBirthDate(etBirthDate.getText().toString());
            }
            objMemberJSONParser.UpdateMemberMasterByMemberMasterId(getActivity(), UserProfileFragment.this, objMemberMaster);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean ValidateControls() {
        boolean IsValid = true;
        if (etMobile.getText().toString().substring(4).equals("") || etMobile.getText().toString().substring(4).length() != 10) {
            etMobile.setError("Enter 10 digit Mobile No");
            IsValid = false;
        } else {
            etMobile.clearError();
        }

        if (!etMobile1.getText().toString().substring(4).equals("") && etMobile1.getText().toString().substring(4).length() != 10) {
            etMobile1.setError("Enter 10 digit Mobile No");
            IsValid = false;
        } else {
            etMobile1.clearError();
        }
        return IsValid;
    }

    private void ClearControls() {
        etFirstName.setText("");
        etMobile.setText("");
        etBirthDate.setText("");
    }

    private void SetError(String errorCode, MemberMaster objMemberMaster) {
        switch (errorCode) {
            case "-1":
                Globals.ShowSnackBar(myprofile, getResources().getString(R.string.MsgServerNotResponding), getActivity(), 1000);
                break;
            default:
                objSharePreferenceManage = new SharePreferenceManage();
                objSharePreferenceManage.CreatePreference("LoginPreference", "MemberName", etFirstName.getText().toString().trim() + " " + etLastName.getText().toString().trim(), getActivity());
                if (rbMale.isChecked()) {
                    objSharePreferenceManage.CreatePreference("LoginPreference", "Gender", rbMale.getText().toString(), getActivity());
                }
                if (rbFemale.isChecked()) {
                    objSharePreferenceManage.CreatePreference("LoginPreference", "Gender", rbFemale.getText().toString(), getActivity());
                }
                objSharePreferenceManage.CreatePreference("LoginPreference", "Phone", etMobile.getText().toString().trim(), getActivity());
                objSharePreferenceManage.CreatePreference("LoginPreference", "Birthdate", etBirthDate.getText().toString().trim(), getActivity());
                ClearControls();
                if (getActivity() instanceof DetailActivity) {
                    objUpdateResponseListener = (UpdateResponseListener) getActivity();
                    if (objUpdateResponseListener != null) {
                        objUpdateResponseListener.UpdateResponse();
                    }
                } else {
                    getActivity().getSupportFragmentManager().popBackStack();
//                    getActivity().onBackPressed();
                }
                break;
        }

    }

    interface UpdateResponseListener {
        void UpdateResponse();
    }

    //endregion
}

