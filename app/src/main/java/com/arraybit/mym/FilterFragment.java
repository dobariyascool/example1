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
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;

import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.parser.MemberJSONParser;
import com.rey.material.widget.Button;
import com.rey.material.widget.EditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class FilterFragment extends Fragment implements View.OnClickListener, MemberJSONParser.DetailRequestListener {

    public static boolean isFilter = false;
    Spinner spinnerBloodGroup;
    EditText etName;
    AppCompatAutoCompleteTextView actDesignation, actQualification;
    Button btnClear, btnfilter;
    SelectFilterListerner objSelectFilterListerner;

    public FilterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_filter, container, false);
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
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Filter");
            setHasOptionsMenu(true);

            spinnerBloodGroup = (Spinner) view.findViewById(R.id.spinnerBloodGroup);
            etName = (EditText) view.findViewById(R.id.etName);
            actDesignation = (AppCompatAutoCompleteTextView) view.findViewById(R.id.actDesignation);
            actQualification = (AppCompatAutoCompleteTextView) view.findViewById(R.id.actQualification);
            btnClear = (Button) view.findViewById(R.id.btnClear);
            btnfilter = (Button) view.findViewById(R.id.btnfilter);

            ArrayList<String> bloodGroups = new ArrayList<>();
            bloodGroups.add("- SELECT -");
            for (int i = 0; i < Globals.BloodGroup.values().length; i++) {
                bloodGroups.add(Globals.BloodGroup.getBlood(i));
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.row_blood, bloodGroups);
            spinnerBloodGroup.setAdapter(adapter);

            btnClear.setOnClickListener(this);
            btnfilter.setOnClickListener(this);
            actDesignation.setOnClickListener(this);
            actQualification.setOnClickListener(this);

            if (Service.CheckNet(getActivity())) {
                RequestProfessions();
                RequestQualification();
            }

            Bundle bundle = getArguments();
            if (bundle != null) {
                if (bundle.getString("name") != null && !bundle.getString("name").equals("")) {
                    etName.setText(bundle.getString("name"));
                }
                if (bundle.getString("designation") != null && !bundle.getString("designation").equals("")) {
                    actDesignation.setText(bundle.getString("designation"));
                }
                if (bundle.getString("qualification") != null && !bundle.getString("qualification").equals("")) {
                    actQualification.setText(bundle.getString("qualification"));
                }
                if (bundle.getString("bloodGroup") != null && !bundle.getString("bloodGroup").equals("")) {
                    spinnerBloodGroup.setSelection(Globals.BloodGroup.getBloodPosition(bundle.getString("bloodGroup")) + 1);
                }
            }


            actDesignation.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        actDesignation.showDropDown();
                    }
                }
            });

            actQualification.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        actQualification.showDropDown();
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Globals.HideKeyBoard(getActivity(), getView());
            if (getActivity().getSupportFragmentManager().getBackStackEntryCount() != 0) {
                if (getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null && getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals("Filter")) {
//                    getActivity().getSupportFragmentManager().popBackStack();
                    getActivity().onBackPressed();
                }
//            } else {
//                getActivity().finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
//        menu.findItem(R.id.action_search).setVisible(false);
        menu.findItem(R.id.memberRequest).setVisible(false);
        menu.findItem(R.id.notification).setVisible(false);
        menu.findItem(R.id.memberFilter).setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.actDesignation) {
            actDesignation.showDropDown();
        } else if (v.getId() == R.id.actQualification) {
            actQualification.showDropDown();
        } else if (v.getId() == R.id.btnClear) {
//            isFilter = false;
            actQualification.setText("");
            actDesignation.setText("");
            etName.setText("");
            spinnerBloodGroup.setSelection(0);

//            objSelectFilterListerner = (SelectFilterListerner) getActivity();
//            objSelectFilterListerner.SelectFilter(null, null, null, null, false);
        } else if (v.getId() == R.id.btnfilter) {
            if (getActivity() instanceof SelectFilterListerner) {
                objSelectFilterListerner = (SelectFilterListerner) getActivity();
                String designation = null, qualification = null, bloodGroup = null, name = null;
                if (etName.getText().toString() != null && !etName.getText().toString().equals("")) {
                    name = etName.getText().toString();
                }
                if (actDesignation.getText().toString() != null && !actDesignation.getText().toString().equals("")) {
                    designation = actDesignation.getText().toString();
                }
                if (actQualification.getText().toString() != null && !actQualification.getText().toString().equals("")) {
                    qualification = actQualification.getText().toString();
                }
                if (spinnerBloodGroup.getSelectedItem().toString() != null && spinnerBloodGroup.getSelectedItemPosition() > 0) {
                    bloodGroup = spinnerBloodGroup.getSelectedItem().toString();
                }
                if (getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null && getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals("Filter")) {
                    getActivity().getSupportFragmentManager().popBackStack();
                }
                if (name != null || designation != null || qualification != null || bloodGroup != null) {
                    isFilter = true;
                    objSelectFilterListerner.SelectFilter(name, designation, qualification, bloodGroup, false);
                }else {
                    objSelectFilterListerner.SelectFilter(name, designation, qualification, bloodGroup, true);
                }

            }
        }
    }

    @Override
    public void QualificationResponse(ArrayList<String> lstStrings) {
        if (lstStrings != null && lstStrings.size() > 0) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.row_spinner, lstStrings);
            actQualification.setAdapter(adapter);
        }
    }

    @Override
    public void ProfessionResponse(ArrayList<String> lstStrings) {
        if (lstStrings != null && lstStrings.size() > 0) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.row_spinner, lstStrings);
            actDesignation.setAdapter(adapter);
        }
    }

    private void RequestProfessions() {
//        MemberJSONParser objMemeberJSONParser = new MemberJSONParser();
//        objMemeberJSONParser.SelectAllProfession(getActivity(), this);
        ArrayList<String> lstStrings = new ArrayList<>();

        try {
            JSONObject jsonObject = Service.HttpGetService(Service.Url + "SelectAllProfession");
            if (jsonObject != null) {
                JSONArray jsonArray = jsonObject.getJSONArray("SelectAllProfessionResult");
                if (jsonArray != null) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        lstStrings.add(jsonArray.getString(i));
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.row_spinner, lstStrings);
                    actDesignation.setAdapter(adapter);
                }
            }
        } catch (Exception e) {

        }
    }

    private void RequestQualification() {
//        MemberJSONParser objMemeberJSONParser = new MemberJSONParser();
//        objMemeberJSONParser.SelectAllQualification(getActivity(), this);
        ArrayList<String> lstStrings = new ArrayList<>();

        try {
            JSONObject jsonObject = Service.HttpGetService(Service.Url + "SelectAllQualification");
            if (jsonObject != null) {
                JSONArray jsonArray = jsonObject.getJSONArray("SelectAllQualificationResult");
                if (jsonArray != null) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        lstStrings.add(jsonArray.getString(i));
                    }
                    if (lstStrings != null && lstStrings.size() > 0) {
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.row_spinner, lstStrings);
//                        alString = lstStrings;
//                        alStringFilter = new ArrayList<>();
                        actQualification.setAdapter(adapter);
//                        actQualification.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
                    }
                }
            }
        } catch (Exception e) {
            // Log exception

        }
    }

    public interface SelectFilterListerner {
        void SelectFilter(String name, String designation, String qualification, String bloodGroup, boolean isClear);
    }
}
