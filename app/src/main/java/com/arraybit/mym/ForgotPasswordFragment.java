package com.arraybit.mym;


import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.modal.MemberMaster;
import com.arraybit.parser.MemberJSONParser;
import com.rey.material.widget.Button;
import com.rey.material.widget.EditText;
import com.rey.material.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ForgotPasswordFragment extends Fragment implements MemberJSONParser.MemberRequestListener {

    EditText etEmail;
    Button btnVerification;
    ProgressDialog progressDialog = new ProgressDialog();
    FrameLayout flForgotPasswrod;
    TextView txtWrong;

    public ForgotPasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);
        Toolbar app_bar = (Toolbar) view.findViewById(R.id.app_bar);
        if (app_bar != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(app_bar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if (Build.VERSION.SDK_INT >= 21) {
                app_bar.setElevation(getActivity().getResources().getDimension(R.dimen.app_bar_elevation));
            }
        }
        app_bar.setTitle("Forgot Password");

        setHasOptionsMenu(true);

        //edittext
        etEmail = (EditText) view.findViewById(R.id.etEmail);
        txtWrong = (TextView) view.findViewById(R.id.txtWrong);

        //end

        //button
        btnVerification = (Button) view.findViewById(R.id.btnVerification);
        flForgotPasswrod = (FrameLayout) view.findViewById(R.id.flForgotPasswrod);
        //end

        btnVerification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Globals.HideKeyBoard(getActivity(), getView());
                if (!ValidateControls()) {
                    Globals.ShowSnackBar(flForgotPasswrod, getActivity().getResources().getString(R.string.MsgValidation), getActivity(), 2000);
                    return;
                }
                if (Service.CheckNet(getActivity())) {
                    ForgotPasswordRequest();
                } else {
                    Globals.ShowSnackBar(flForgotPasswrod, getResources().getString(R.string.MsgCheckConnection), getActivity(), 1000);
                }
            }
        });

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Globals.HideKeyBoard(getActivity(), getView());
            getActivity().getSupportFragmentManager().popBackStack();
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
            txtWrong.setVisibility(View.GONE);
            Toast.makeText(getActivity(), "Mail Sent on " + etEmail.getText().toString(), Toast.LENGTH_LONG).show();
            getActivity().getSupportFragmentManager().popBackStack();
        }else if (errorCode.equals("-2")) {
//            Globals.ShowSnackBar(flForgotPasswrod, "The email address " + etEmail.getText().toString() + " is not registered. Please try again.", getActivity(), Snackbar.LENGTH_SHORT);
            txtWrong.setVisibility(View.VISIBLE);
            txtWrong.setText("The email address " + etEmail.getText().toString() + " is not registered. Please try again.");
        } else {
            txtWrong.setVisibility(View.GONE);
            Toast.makeText(getActivity(), "Mail not send. Please try again after sometime.", Toast.LENGTH_LONG).show();
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }

    private void ForgotPasswordRequest() {
        progressDialog = new ProgressDialog();
        progressDialog.show(getActivity().getSupportFragmentManager(), "");
        try {
            MemberJSONParser objMemberJSONParser = new MemberJSONParser();
            objMemberJSONParser.ForgotPasswordMemberMaster(getActivity(), this, etEmail.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean ValidateControls() {
        boolean IsValid = true;

        if (!Globals.IsValidEmail(etEmail.getText().toString())) {
            IsValid = false;
            etEmail.setError("Enter Valid Email Address");
        } else {
            etEmail.setError(null);
        }

        return IsValid;
    }

}
