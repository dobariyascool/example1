package com.arraybit.mym;


import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ToggleButton;

import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.global.SharePreferenceManage;
//import com.arraybit.parser.UserJSONParser;
import com.arraybit.modal.MemberMaster;
import com.arraybit.parser.MemberJSONParser;
import com.rey.material.widget.Button;
import com.rey.material.widget.EditText;


@SuppressWarnings("ConstantConditions")
public class ChangePasswordFragment extends Fragment implements MemberJSONParser.MemberRequestListener{


    EditText etOldPassword, etNewPassword, etConfirmPassword;
    Button btnChangePassword;
    ToggleButton tbPasswordShowOld, tbPasswordShowNew, tbPasswordShowConfirm;
    View view;
    ProgressDialog progressDialog = new ProgressDialog();
    SharePreferenceManage objSharePreferenceManage;

    public ChangePasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_change_password, container, false);

        Toolbar app_bar = (Toolbar) view.findViewById(R.id.app_bar);
        if (app_bar != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(app_bar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if (Build.VERSION.SDK_INT >= 21) {
                app_bar.setElevation(getActivity().getResources().getDimension(R.dimen.app_bar_elevation));
            }
        }
        app_bar.setTitle(getActivity().getResources().getString(R.string.title_fragment_change_password));

        setHasOptionsMenu(true);

        //edittext
        etOldPassword = (EditText) view.findViewById(R.id.etOldPassword);
        etNewPassword = (EditText) view.findViewById(R.id.etNewPassword);
        etConfirmPassword = (EditText) view.findViewById(R.id.etConfirmPassword);
        //end

        //button
        btnChangePassword = (Button) view.findViewById(R.id.btnChangePassword);
        //end

        //togglebutton
        tbPasswordShowOld = (ToggleButton) view.findViewById(R.id.tbPasswordShowOld);
        tbPasswordShowNew = (ToggleButton) view.findViewById(R.id.tbPasswordShowNew);
        tbPasswordShowConfirm = (ToggleButton) view.findViewById(R.id.tbPasswordShowConfirm);
        //end

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Globals.HideKeyBoard(getActivity(), getView());
                if (!Validation()) {
                    Globals.ShowSnackBar(v, getActivity().getResources().getString(R.string.MsgValidation), getActivity(), 2000);
                    return;
                }
                if (Service.CheckNet(getActivity())) {
                    UpdateUserPasswordRequest();
                } else {
                    Globals.ShowSnackBar(btnChangePassword, getResources().getString(R.string.MsgCheckConnection), getActivity(), 1000);
                }
            }
        });

        etConfirmPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    Globals.HideKeyBoard(getActivity(), v);
                }
                return false;
            }
        });

        return view;
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Globals.HideKeyBoard(getActivity(), getView());
            getActivity().getSupportFragmentManager().popBackStack();
        }
        return super.onOptionsItemSelected(item);
    }

    //region Private Method
    private void UpdateUserPasswordRequest() {
        progressDialog = new ProgressDialog();
        progressDialog.show(getActivity().getSupportFragmentManager(), "");

        try {
            MemberJSONParser objMemberJSONParser = new MemberJSONParser();
            objMemberJSONParser.UpdateMemberMasterPassword(getActivity(), ChangePasswordFragment.this, Globals.memberMasterId,
                    etOldPassword.getText().toString(), etNewPassword.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void SetError(String errorCode) {
        switch (errorCode) {
            case "-1":
                Globals.ShowSnackBar(view, getResources().getString(R.string.MsgServerNotResponding), getActivity(), 1000);
                break;
            case "-3":
                Globals.ShowSnackBar(view, "Password must be wrong.", getActivity(), 1000);
                ClearControls();
                break;
            default:
                Globals.ShowSnackBar(view, getResources().getString(R.string.MsgUpdatePassword), getActivity(), 1000);
                objSharePreferenceManage = new SharePreferenceManage();
                objSharePreferenceManage.CreatePreference("LoginPreference", "MemberPassword", etNewPassword.getText().toString(), getActivity());
                ClearControls();
                getActivity().getSupportFragmentManager().popBackStack();
                break;
        }

    }

    private boolean Validation() {
        boolean IsValid = true;
        if (etOldPassword.getText().toString().equals("") && etNewPassword.getText().toString().equals("") && etConfirmPassword.getText().toString().equals("")) {
            etOldPassword.setError("Enter " + getResources().getString(R.string.cpOldPassword));
            etNewPassword.setError("Enter " + getResources().getString(R.string.cpNewPasssword));
            etConfirmPassword.setError("Enter " + getResources().getString(R.string.cpConfirmPassword));
            IsValid = false;
        } else if (!etOldPassword.getText().toString().equals("") &&
                etNewPassword.getText().toString().equals("") && etConfirmPassword.getText().toString().equals("")) {
            etOldPassword.clearError();
            etNewPassword.setError("Enter " + getResources().getString(R.string.cpNewPasssword));
            etConfirmPassword.setError("Enter " + getResources().getString(R.string.cpConfirmPassword));
            IsValid = false;
        } else if (!etOldPassword.getText().toString().equals("") &&
                !etNewPassword.getText().toString().equals("") && etConfirmPassword.getText().toString().equals("")) {
            etOldPassword.clearError();
            etNewPassword.clearError();
            etConfirmPassword.setError("Enter " + getResources().getString(R.string.cpConfirmPassword));
            IsValid = false;
        } else if (!etNewPassword.getText().toString().equals("") &&
                etOldPassword.getText().toString().equals("") && etConfirmPassword.getText().toString().equals("")) {
            etNewPassword.clearError();
            etOldPassword.setError("Enter " + getResources().getString(R.string.cpOldPassword));
            etConfirmPassword.setError("Enter " + getResources().getString(R.string.cpConfirmPassword));
            IsValid = false;
        } else if (!etNewPassword.getText().toString().equals("") && !etConfirmPassword.getText().toString().equals("")
                && etOldPassword.getText().toString().equals("")) {
            etNewPassword.clearError();
            etConfirmPassword.clearError();
            etOldPassword.setError("Enter " + getResources().getString(R.string.cpOldPassword));
            if (!etNewPassword.getText().toString().trim().equals(etConfirmPassword.getText().toString().trim())) {
                etConfirmPassword.setError(getActivity().getResources().getString(R.string.cpPasswordMisMatch));
            } else {
                etConfirmPassword.clearError();
            }
            IsValid = false;
        } else if (!etConfirmPassword.getText().toString().equals("")
                && etOldPassword.getText().toString().equals("") && etNewPassword.getText().toString().equals("")) {
            etConfirmPassword.clearError();
            etOldPassword.setError("Enter " + getResources().getString(R.string.cpOldPassword));
            etNewPassword.setError("Enter " + getResources().getString(R.string.cpNewPasssword));
            IsValid = false;
        } else if (!etNewPassword.getText().toString().equals("") &&
                !etConfirmPassword.getText().toString().equals("") &&
                !etNewPassword.getText().toString().trim().equals(etConfirmPassword.getText().toString().trim())) {
            etNewPassword.clearError();
            etOldPassword.clearError();
            etConfirmPassword.setError(getActivity().getResources().getString(R.string.cpPasswordMisMatch));
            IsValid = false;
        }
        if (IsValid) {
            etConfirmPassword.clearError();
            etOldPassword.clearError();
            etNewPassword.clearError();
        }
        return IsValid;
    }

    private void ClearControls() {
        etOldPassword.setText("");
        etConfirmPassword.setText("");
        etNewPassword.setText("");
    }

    @Override
    public void MemberResponse(String errorCode, MemberMaster objMemberMaster) {

    }

    @Override
    public void MemberUpdate(String errorCode, MemberMaster objMemberMaster) {
        progressDialog.dismiss();
        SetError(errorCode);
    }

    //endregion

}
