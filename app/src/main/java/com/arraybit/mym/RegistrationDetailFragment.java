package com.arraybit.mym;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.AppCompatMultiAutoCompleteTextView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.arraybit.global.Globals;
import com.arraybit.global.MarshMallowPermission;
import com.arraybit.global.Service;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.modal.MemberMaster;
import com.arraybit.modal.MemberRelativesTran;
import com.arraybit.parser.MemberJSONParser;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.rey.material.widget.Button;
import com.rey.material.widget.EditText;
import com.rey.material.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class RegistrationDetailFragment extends Fragment implements View.OnClickListener, MemberJSONParser.MemberRequestListener, MemberJSONParser.DetailRequestListener {


    int childCount = 0;
    EditText etAnniversaryDate, etSpouseName, etSpouseDOB;
    EditText etChildName1, etChildDOB1, etChildName2, etChildDOB2, etChildName3, etChildDOB3, etChildName4, etChildDOB4, etChildName5, etChildDOB5;
    LinearLayout llChildDetail1, llChildDetail2, llChildDetail3, llChildDetail4, llChildDetail5;
    LinearLayout llAdd1, llAdd2, llAdd3, llAdd4;
    TextView txtAnniversaryDate, txtSpouseDetail, txtSpouseDOB, txtSpouseName, txtChildDetail;
    RadioGroup rgMain, rgMainChild1, rgMainChild2, rgMainChild3, rgMainChild4, rgMainChild5;
    CheckBox checkBox;
    RadioButton rbMarried, rbUnmarried, rbMale1, rbMale2, rbFemale1, rbFemale2;
    RadioButton rbMale3, rbMale4, rbMale5, rbFemale3, rbFemale4, rbFemale5;
    AppCompatAutoCompleteTextView actProfession;
    ArrayList<String> alString = new ArrayList<>();
    ArrayList<String> alStringFilter;
    AppCompatMultiAutoCompleteTextView actQualification;
    String[] selectedValue;
    Button btnUpdateDetail;
    ImageView ivSpouseImage, ivChildImage1, ivChildImage2, ivChildImage3, ivChildImage4, ivChildImage5;
    ImageView ivAdd1, ivAdd2, ivAdd3, ivAdd4, ivClose1, ivClose2, ivClose3, ivClose4, ivClose5;
    View ivTemparory;
    ProgressDialog progressDialog;
    Spinner spinnerBloodGroup;
    String imagePhysicalNameBytes, imageName, strImageName, picturePath = "";
    String imagePhysicalNameBytes1, imageName1, imagePhysicalNameBytes2, imageName2;
    String imagePhysicalNameBytes3, imageName3, imagePhysicalNameBytes4, imageName4, imagePhysicalNameBytes5, imageName5;
    MemberMaster objMemberMaster;
    boolean isUpdate = false, isDeleted;
    MarshMallowPermission marshMallowPermission;
    UpdateResponseListener objUpdateResponseListener;
    boolean isRemoveS = false, isRemove1 = false, isRemove2 = false, isRemove3 = false, isRemove4 = false, isRemove5 = false;
    Bitmap bitmap, bitmap1, bitmap2, bitmap3, bitmap4, bitmap5;
    ArrayAdapter<String> adapter;

    public RegistrationDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_registration_detail, container, false);
        try {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

            //app_bar
            Toolbar app_bar = (Toolbar) view.findViewById(R.id.app_bar);
            if (app_bar != null) {
                if (Build.VERSION.SDK_INT >= 21) {
                    app_bar.setElevation(getActivity().getResources().getDimension(R.dimen.app_bar_elevation));
                }
                ((AppCompatActivity) getActivity()).setSupportActionBar(app_bar);
                ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.title_fragment_registration));
            //end
//            Globals.SetToolBarBackground(getActivity(), app_bar, ContextCompat.getColor(getActivity(), R.color.colorPrimary), ContextCompat.getColor(getActivity(), android.R.color.white));

            //EditText

            marshMallowPermission = new MarshMallowPermission(getActivity());
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!marshMallowPermission.checkPermissionForCamera()) {
                    marshMallowPermission.requestPermissionForCamera();
                }
                if (!marshMallowPermission.checkPermissionForExternalStorage()) {
                    marshMallowPermission.requestPermissionForExternalStorage();
                }
            }
            actProfession = (AppCompatAutoCompleteTextView) view.findViewById(R.id.actDesignation);
            actQualification = (AppCompatMultiAutoCompleteTextView) view.findViewById(R.id.actQualification);

//            if (getArguments() != null) {
            Bundle bundle = getArguments();
            if (bundle != null) {
                objMemberMaster = bundle.getParcelable("MemberMaster");
                isUpdate = bundle.getBoolean("isUpdate", false);
            }
//            }

            if (Service.CheckNet(getActivity())) {
                RequestProfessions();
                RequestQualification();
            }
            etAnniversaryDate = (EditText) view.findViewById(R.id.etAnniversaryDate);
            etSpouseName = (EditText) view.findViewById(R.id.etSpouseName);
            etSpouseDOB = (EditText) view.findViewById(R.id.etSpouseDOB);
            etSpouseDOB.setInputType(InputType.TYPE_NULL);
            etChildName1 = (EditText) view.findViewById(R.id.etChildName1);
            etChildDOB1 = (EditText) view.findViewById(R.id.etChildDOB1);
            etChildDOB1.setInputType(InputType.TYPE_NULL);
            etChildName2 = (EditText) view.findViewById(R.id.etChildName2);
            etChildDOB2 = (EditText) view.findViewById(R.id.etChildDOB2);
            etChildDOB2.setInputType(InputType.TYPE_NULL);
            etChildName3 = (EditText) view.findViewById(R.id.etChildName3);
            etChildDOB3 = (EditText) view.findViewById(R.id.etChildDOB3);
            etChildDOB3.setInputType(InputType.TYPE_NULL);
            etChildName4 = (EditText) view.findViewById(R.id.etChildName4);
            etChildDOB4 = (EditText) view.findViewById(R.id.etChildDOB4);
            etChildDOB4.setInputType(InputType.TYPE_NULL);
            etChildName5 = (EditText) view.findViewById(R.id.etChildName5);
            etChildDOB5 = (EditText) view.findViewById(R.id.etChildDOB5);
            etChildDOB5.setInputType(InputType.TYPE_NULL);
            etAnniversaryDate.setInputType(InputType.TYPE_NULL);
            spinnerBloodGroup = (Spinner) view.findViewById(R.id.spinnerBloodGroup);

            txtAnniversaryDate = (TextView) view.findViewById(R.id.txtAnniversaryDate);
            txtSpouseDetail = (TextView) view.findViewById(R.id.txtSpouseDetail);
            txtChildDetail = (TextView) view.findViewById(R.id.txtChildDetail);
            txtSpouseDOB = (TextView) view.findViewById(R.id.txtSpouseDOB);
            txtSpouseName = (TextView) view.findViewById(R.id.txtSpouseName);
            ivSpouseImage = (ImageView) view.findViewById(R.id.ivSpouseImage);
            ivChildImage1 = (ImageView) view.findViewById(R.id.ivChildImage1);
            ivChildImage2 = (ImageView) view.findViewById(R.id.ivChildImage2);
            ivChildImage3 = (ImageView) view.findViewById(R.id.ivChildImage3);
            ivChildImage4 = (ImageView) view.findViewById(R.id.ivChildImage4);
            ivChildImage5 = (ImageView) view.findViewById(R.id.ivChildImage5);
            ivClose1 = (ImageView) view.findViewById(R.id.ivClose1);
            ivClose2 = (ImageView) view.findViewById(R.id.ivClose2);
            ivClose3 = (ImageView) view.findViewById(R.id.ivClose3);
            ivClose4 = (ImageView) view.findViewById(R.id.ivClose4);
            ivClose5 = (ImageView) view.findViewById(R.id.ivClose5);
            llChildDetail1 = (LinearLayout) view.findViewById(R.id.llChildDetail1);
            llChildDetail2 = (LinearLayout) view.findViewById(R.id.llChildDetail2);
            llChildDetail3 = (LinearLayout) view.findViewById(R.id.llChildDetail3);
            llChildDetail4 = (LinearLayout) view.findViewById(R.id.llChildDetail4);
            llChildDetail5 = (LinearLayout) view.findViewById(R.id.llChildDetail5);

            llAdd1 = (LinearLayout) view.findViewById(R.id.llAdd1);
            llAdd2 = (LinearLayout) view.findViewById(R.id.llAdd2);
            llAdd3 = (LinearLayout) view.findViewById(R.id.llAdd3);
            llAdd4 = (LinearLayout) view.findViewById(R.id.llAdd4);
            checkBox = (CheckBox) view.findViewById(R.id.checkBox);
            //end

            //Radiogroup
            rgMain = (RadioGroup) view.findViewById(R.id.rgMain);
            rgMainChild1 = (RadioGroup) view.findViewById(R.id.rgMainChild1);
            rgMainChild2 = (RadioGroup) view.findViewById(R.id.rgMainChild2);
            rgMainChild3 = (RadioGroup) view.findViewById(R.id.rgMainChild3);
            rgMainChild4 = (RadioGroup) view.findViewById(R.id.rgMainChild4);
            rgMainChild5 = (RadioGroup) view.findViewById(R.id.rgMainChild5);
            //

            //RadioButton
            rbMarried = (RadioButton) view.findViewById(R.id.rbMarried);
            rbUnmarried = (RadioButton) view.findViewById(R.id.rbUnmarried);
            rbMale1 = (RadioButton) view.findViewById(R.id.rbMale1);
            rbMale2 = (RadioButton) view.findViewById(R.id.rbMale2);
            rbMale3 = (RadioButton) view.findViewById(R.id.rbMale3);
            rbMale4 = (RadioButton) view.findViewById(R.id.rbMale4);
            rbMale5 = (RadioButton) view.findViewById(R.id.rbMale5);
            rbFemale1 = (RadioButton) view.findViewById(R.id.rbFemale1);
            rbFemale2 = (RadioButton) view.findViewById(R.id.rbFemale2);
            rbFemale3 = (RadioButton) view.findViewById(R.id.rbFemale3);
            rbFemale4 = (RadioButton) view.findViewById(R.id.rbFemale4);
            rbFemale5 = (RadioButton) view.findViewById(R.id.rbFemale5);
            //end

            //button
            btnUpdateDetail = (Button) view.findViewById(R.id.btnUpdateDetail);
            //end

            //Spinner
            //

            //compound button
            //end

            //event

            ivAdd1 = (ImageView) view.findViewById(R.id.ivAdd1);
            ivAdd2 = (ImageView) view.findViewById(R.id.ivAdd2);
            ivAdd3 = (ImageView) view.findViewById(R.id.ivAdd3);
            ivAdd4 = (ImageView) view.findViewById(R.id.ivAdd4);

            btnUpdateDetail.setOnClickListener(this);
            etAnniversaryDate.setOnClickListener(this);
            etSpouseDOB.setOnClickListener(this);
            etChildDOB1.setOnClickListener(this);
            etChildDOB2.setOnClickListener(this);
            etChildDOB3.setOnClickListener(this);
            etChildDOB4.setOnClickListener(this);
            etChildDOB5.setOnClickListener(this);
            ivSpouseImage.setOnClickListener(this);
            ivChildImage1.setOnClickListener(this);
            ivChildImage2.setOnClickListener(this);
            ivChildImage3.setOnClickListener(this);
            ivChildImage4.setOnClickListener(this);
            ivChildImage5.setOnClickListener(this);
            ivClose1.setOnClickListener(this);
            ivClose2.setOnClickListener(this);
            ivClose3.setOnClickListener(this);
            ivClose4.setOnClickListener(this);
            ivClose5.setOnClickListener(this);

            ivAdd1.setOnClickListener(this);
            ivAdd2.setOnClickListener(this);
            ivAdd3.setOnClickListener(this);
            ivAdd4.setOnClickListener(this);
            actProfession.setOnClickListener(this);
            actQualification.setOnClickListener(this);
            //end

            ArrayList<String> bloodGroups = new ArrayList<>();
            bloodGroups.add("- SELECT -");
            for (int i = 0; i < Globals.BloodGroup.values().length; i++) {
                bloodGroups.add(Globals.BloodGroup.getBlood(i));
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.row_blood, bloodGroups);
            spinnerBloodGroup.setAdapter(adapter);

            actProfession.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        actProfession.setSelection(actProfession.getText().toString().length());
                        actProfession.showDropDown();
                    }
                }
            });

            actQualification.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        actQualification.setSelection(actQualification.getText().toString().length());
                        actQualification.showDropDown();
                    }
                }
            });

            setHasOptionsMenu(true);

//            if (Service.CheckNet(getActivity())) {
//                RequestProfessions();
//                RequestQualification();
//            }

//            if (getActivity() instanceof DetailActivity) {

//            }

//            if (isUpdate) {
            btnUpdateDetail.setText("Update");
//            } else {
//                btnUpdateDetail.setText("Next");
//            }

            rgMain.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if (checkedId == R.id.rbMarried) {
                        etAnniversaryDate.setVisibility(View.VISIBLE);
                        etSpouseName.setVisibility(View.VISIBLE);
                        etSpouseDOB.setVisibility(View.VISIBLE);
                        txtAnniversaryDate.setVisibility(View.VISIBLE);
                        txtSpouseName.setVisibility(View.VISIBLE);
                        txtSpouseDOB.setVisibility(View.VISIBLE);
                        txtSpouseDetail.setVisibility(View.VISIBLE);
                        ivSpouseImage.setVisibility(View.VISIBLE);
                        txtChildDetail.setVisibility(View.VISIBLE);
                        checkBox.setVisibility(View.VISIBLE);
                        checkBox.setChecked(true);
                    } else if (checkedId == R.id.rbUnmarried) {
                        etAnniversaryDate.setVisibility(View.GONE);
                        etSpouseName.setVisibility(View.GONE);
                        etSpouseDOB.setVisibility(View.GONE);
                        txtAnniversaryDate.setVisibility(View.GONE);
                        txtSpouseName.setVisibility(View.GONE);
                        txtSpouseDOB.setVisibility(View.GONE);
                        txtSpouseDetail.setVisibility(View.GONE);
                        ivSpouseImage.setVisibility(View.GONE);
                        txtChildDetail.setVisibility(View.GONE);
                        checkBox.setVisibility(View.GONE);
                        llChildDetail1.setVisibility(View.GONE);
                        llChildDetail2.setVisibility(View.GONE);
                        llChildDetail3.setVisibility(View.GONE);
                        llChildDetail4.setVisibility(View.GONE);
                        llChildDetail5.setVisibility(View.GONE);
                        llAdd1.setVisibility(View.GONE);
                        llAdd2.setVisibility(View.GONE);
                        llAdd3.setVisibility(View.GONE);
                        llAdd4.setVisibility(View.GONE);


                    }
                }
            });

            etAnniversaryDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        Globals.ShowDatePickerDialog(etAnniversaryDate, getActivity(), false);
                    }
                }
            });

            etSpouseDOB.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        Globals.ShowDatePickerDialog(etSpouseDOB, getActivity(), false);
                    }
                }
            });
            etChildDOB1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        Globals.ShowDatePickerDialog(etChildDOB1, getActivity(), false);
                    }
                }
            });
            etChildDOB2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        Globals.ShowDatePickerDialog(etChildDOB2, getActivity(), false);
                    }
                }
            });
            etChildDOB3.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        Globals.ShowDatePickerDialog(etChildDOB3, getActivity(), false);
                    }
                }
            });
            etChildDOB4.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        Globals.ShowDatePickerDialog(etChildDOB4, getActivity(), false);
                    }
                }
            });
            etChildDOB5.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        Globals.ShowDatePickerDialog(etChildDOB5, getActivity(), false);
                    }
                }
            });

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        llChildDetail1.setVisibility(View.GONE);
                        llChildDetail2.setVisibility(View.GONE);
                        llChildDetail3.setVisibility(View.GONE);
                        llChildDetail4.setVisibility(View.GONE);
                        llChildDetail5.setVisibility(View.GONE);
                        llAdd1.setVisibility(View.GONE);
                        llAdd2.setVisibility(View.GONE);
                        llAdd3.setVisibility(View.GONE);
                        llAdd4.setVisibility(View.GONE);
                    } else {
                        if (childCount == 0) {
                            childCount = 1;
                        }
//                        if (objMemberMaster != null) {
//                            SetData();
//                        } else {
                        llChildDetail1.setVisibility(View.VISIBLE);
                        llAdd1.setVisibility(View.VISIBLE);
                        if (!etChildName1.getText().toString().equals("")) {
                            llChildDetail1.setVisibility(View.VISIBLE);
                            llAdd1.setVisibility(View.VISIBLE);
                        }
                        if (!etChildName2.getText().toString().equals("")) {
                            llChildDetail2.setVisibility(View.VISIBLE);
                            llAdd2.setVisibility(View.VISIBLE);
                            llAdd1.setVisibility(View.GONE);
                        }
                        if (!etChildName3.getText().toString().equals("")) {
                            llChildDetail3.setVisibility(View.VISIBLE);
                            llAdd3.setVisibility(View.VISIBLE);
                            llAdd2.setVisibility(View.GONE);
                        }
                        if (!etChildName4.getText().toString().equals("")) {
                            llChildDetail4.setVisibility(View.VISIBLE);
                            llAdd4.setVisibility(View.VISIBLE);
                            llAdd3.setVisibility(View.GONE);
                        }
                        if (!etChildName5.getText().toString().equals("")) {
                            llChildDetail5.setVisibility(View.VISIBLE);
                            llAdd4.setVisibility(View.GONE);
                        }

//                        }
                    }
                }
            });

            actQualification.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
//                    actQualification.setSelection(actQualification.getText().toString().length());
                    if (keyCode == KeyEvent.KEYCODE_DEL) {
                        if (actQualification.getText().toString().isEmpty()) {
                            SetArrayListAdapter(alString);
                            isDeleted = false;
                        } else {
                            isDeleted = true;
                        }
                    }
                    return false;
                }
            });

            actQualification.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Globals.HideKeyBoard(getActivity(), view);
                    UpdateArrayListAdapter((String) parent.getAdapter().getItem(position));
                }
            });

            if (objMemberMaster != null) {
//                if (isUpdate) {
                SetData();
//                } else {
//                    checkBox.setChecked(true);
//                }
            } else {
                checkBox.setChecked(true);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
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
                if (getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null && getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals("PersonalDetail")) {
                    Globals.HideKeyBoard(getActivity(), actProfession);
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
    public void onClick(View v) {
        if (v.getId() == R.id.btnUpdateDetail) {
            if (!ValidateControls()) {
                Globals.ShowSnackBar(v, getResources().getString(R.string.MsgValidation), getActivity(), 1000);
                return;
            }
            if (Service.CheckNet(getActivity())) {
//                new SignUpLoadingTask().execute();
                UpdateDetailRequest();
            } else {
                Globals.ShowSnackBar(v, getResources().getString(R.string.MsgCheckConnection), getActivity(), 1000);
            }
        } else if (v.getId() == R.id.ivSpouseImage) {
            ivTemparory = v;
            Globals.HideKeyBoard(getActivity(), v);
            SelectImageProfile(getActivity(), 100, 101);
        } else if (v.getId() == R.id.ivChildImage1) {
            ivTemparory = v;
            Globals.HideKeyBoard(getActivity(), v);
            SelectImageProfile(getActivity(), 100, 101);
        } else if (v.getId() == R.id.ivChildImage2) {
            ivTemparory = v;
            Globals.HideKeyBoard(getActivity(), v);
            SelectImageProfile(getActivity(), 100, 101);
        } else if (v.getId() == R.id.ivChildImage3) {
            ivTemparory = v;
            Globals.HideKeyBoard(getActivity(), v);
            SelectImageProfile(getActivity(), 100, 101);
        } else if (v.getId() == R.id.ivChildImage4) {
            ivTemparory = v;
            Globals.HideKeyBoard(getActivity(), v);
            SelectImageProfile(getActivity(), 100, 101);
        } else if (v.getId() == R.id.ivChildImage5) {
            ivTemparory = v;
            Globals.HideKeyBoard(getActivity(), v);
            SelectImageProfile(getActivity(), 100, 101);
        } else if (v.getId() == R.id.etAnniversaryDate) {
            Globals.ShowDatePickerDialog(etAnniversaryDate, getActivity(), false);
        } else if (v.getId() == R.id.etSpouseDOB) {
            Globals.ShowDatePickerDialog(etSpouseDOB, getActivity(), false);
        } else if (v.getId() == R.id.etChildDOB1) {
            Globals.ShowDatePickerDialog(etChildDOB1, getActivity(), false);
        } else if (v.getId() == R.id.etChildDOB2) {
            Globals.ShowDatePickerDialog(etChildDOB2, getActivity(), false);
        } else if (v.getId() == R.id.etChildDOB3) {
            Globals.ShowDatePickerDialog(etChildDOB3, getActivity(), false);
        } else if (v.getId() == R.id.etChildDOB4) {
            Globals.ShowDatePickerDialog(etChildDOB4, getActivity(), false);
        } else if (v.getId() == R.id.etChildDOB5) {
            Globals.ShowDatePickerDialog(etChildDOB5, getActivity(), false);
        } else if (v.getId() == R.id.actDesignation) {
            actProfession.setSelection(actProfession.getText().toString().length());
            actProfession.showDropDown();
        } else if (v.getId() == R.id.actQualification) {
            actQualification.setSelection(actQualification.getText().toString().length());
            if (actQualification.getText().toString().isEmpty()) {
                SetArrayListAdapter(alString);
            } else {
                if (isDeleted) {
                    if (actQualification.getText().subSequence(actQualification.length() - 1, actQualification.length()).toString().equals(",")) {
                        selectedValue = String.valueOf(actQualification.getText().subSequence(0, actQualification.length()) + " ").split(", ");
                    } else if (actQualification.getText().subSequence(actQualification.length() - 1, actQualification.length()).toString().equals(" ")) {
                        selectedValue = actQualification.getText().subSequence(0, actQualification.length()).toString().split(", ");
                    } else {
                        selectedValue = actQualification.getText().subSequence(0, actQualification.length()).toString().split(", ");
                        actQualification.setText(actQualification.getText() + ", ");
                    }
                    UpdateArrayListAdapter(null);
                    isDeleted = false;
                }

            }
            actQualification.showDropDown();
        } else if (v.getId() == R.id.ivAdd1) {
            childCount += 1;
            llAdd1.setVisibility(View.GONE);
            llChildDetail2.setVisibility(View.VISIBLE);
            llAdd2.setVisibility(View.VISIBLE);
        } else if (v.getId() == R.id.ivAdd2) {
            childCount += 1;
            llAdd2.setVisibility(View.GONE);
            llChildDetail3.setVisibility(View.VISIBLE);
            llAdd3.setVisibility(View.VISIBLE);
        } else if (v.getId() == R.id.ivAdd3) {
            childCount += 1;
            llAdd3.setVisibility(View.GONE);
            llChildDetail4.setVisibility(View.VISIBLE);
            llAdd4.setVisibility(View.VISIBLE);
        } else if (v.getId() == R.id.ivAdd4) {
            childCount += 1;
            llAdd4.setVisibility(View.GONE);
            llChildDetail5.setVisibility(View.VISIBLE);
        } else if (v.getId() == R.id.ivClose1) {
            childCount -= 1;
            llChildDetail1.setVisibility(View.GONE);
            llAdd1.setVisibility(View.GONE);
            if (childCount == 0) {
                checkBox.setChecked(true);
            }
            Globals.HideKeyBoard(getActivity(), v);
        } else if (v.getId() == R.id.ivClose2) {
            childCount -= 1;
            llChildDetail2.setVisibility(View.GONE);
            llAdd1.setVisibility(View.GONE);
            if (childCount == 0) {
                checkBox.setChecked(true);
            }
            Globals.HideKeyBoard(getActivity(), v);
        } else if (v.getId() == R.id.ivClose3) {
            childCount -= 1;
            llChildDetail3.setVisibility(View.GONE);
            llAdd1.setVisibility(View.GONE);
            if (childCount == 0) {
                checkBox.setChecked(true);
            }
            Globals.HideKeyBoard(getActivity(), v);
        } else if (v.getId() == R.id.ivClose4) {
            childCount -= 1;
            llChildDetail4.setVisibility(View.GONE);
            llAdd1.setVisibility(View.GONE);
            if (childCount == 0) {
                checkBox.setChecked(true);
            }
            Globals.HideKeyBoard(getActivity(), v);
        } else if (v.getId() == R.id.ivClose5) {
            childCount -= 1;
            llChildDetail5.setVisibility(View.GONE);
            llAdd1.setVisibility(View.GONE);
            if (childCount == 0) {
                checkBox.setChecked(true);
            }
            Globals.HideKeyBoard(getActivity(), v);
        }
    }

    @Override
    public void MemberResponse(String errorCode, MemberMaster objMemberMaster) {
        progressDialog.dismiss();
        if (errorCode.equals("0")) {
//            if (Globals.startPage == 1) {
//                Globals.startPage = 2;
//                SharePreferenceManage objSharePreferenceManage = new SharePreferenceManage();
//                objSharePreferenceManage.CreatePreference("LoginPreference", "startPage", "2", getActivity());
//                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
//                fragmentTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out, 0, R.anim.right_exit);
//                fragmentTransaction.replace(android.R.id.content, new RegistrationContactFragment(), "ContactDetail");
//                fragmentTransaction.addToBackStack("ContactDetail");
//                fragmentTransaction.commit();
//            } else {
            if (getActivity().getSupportFragmentManager().getBackStackEntryCount() != 0) {
                if (getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null && getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals("PersonalDetail")) {
                    if (getActivity() instanceof DetailActivity) {
                        objUpdateResponseListener = (RegistrationDetailFragment.UpdateResponseListener) getActivity();
                        if (objUpdateResponseListener != null) {
                            objUpdateResponseListener.UpdateResponse();
                        }
                    } else {
                        getActivity().getSupportFragmentManager().popBackStack();
//                    getActivity().onBackPressed();
                    }
                }
            }
//            }
        } else {
            Globals.ShowSnackBar(getView(), getResources().getString(R.string.MsgServerNotResponding), getActivity(), 1000);
        }
    }

    @Override
    public void MemberUpdate(String errorCode, MemberMaster objMemberMaster) {

    }

    @Override
    public void QualificationResponse(final ArrayList<String> lstStrings) {
        if (lstStrings != null && lstStrings.size() > 0) {
//            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.row_spinner, lstStrings);
            alString = lstStrings;
            alStringFilter = new ArrayList<>();
//            actQualification.setAdapter(adapter);
            actQualification.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
            if (actQualification.getText().toString().isEmpty()) {
                SetArrayListAdapter(alString);
            } else {
                if (isDeleted) {
                    if (actQualification.getText().subSequence(actQualification.length() - 1, actQualification.length()).toString().equals(",")) {
                        selectedValue = String.valueOf(actQualification.getText().subSequence(0, actQualification.length()) + " ").split(", ");
                    } else if (actQualification.getText().subSequence(actQualification.length() - 1, actQualification.length()).toString().equals(" ")) {
                        selectedValue = actQualification.getText().subSequence(0, actQualification.length()).toString().split(", ");
                    } else {
                        selectedValue = actQualification.getText().subSequence(0, actQualification.length()).toString().split(", ");
                        actQualification.setText(actQualification.getText() + ", ");
                    }
                    if (selectedValue != null) {
                        for (int i = 0; i <= selectedValue.length; i++) {
                            UpdateArrayListAdapter(selectedValue[i]);
                        }
                    } else {
                        UpdateArrayListAdapter(null);
                    }
                    isDeleted = false;
                }

            }

        }
    }

    @Override
    public void ProfessionResponse(ArrayList<String> lstStrings) {
        if (lstStrings != null && lstStrings.size() > 0) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.row_spinner, lstStrings);
            actProfession.setAdapter(adapter);
        }
    }

    public void SelectImage(int requestCode, Intent data) {
        if (requestCode == 100) {
//            strImageName = "CameraImage_" + simpleDateFormat.format(new Date()) + imageName.substring(imageName.lastIndexOf("."), imageName.length()) + ".jpg";
//            File file = new File(android.os.Environment.getExternalStorageDirectory(), strImageName);
//            picturePath = file.getAbsolutePath();
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
//            imageName = "Member_" + Globals.memberMasterId + ".jpg" ;
            long millis = System.currentTimeMillis();
            if (ivTemparory == ivSpouseImage) {
//                String extension = MimeTypeMap.getFileExtensionFromUrl(picturePath);
//                if (extension != null) {
//                    imageName = String.valueOf(millis)+ "_0." + MimeTypeMap.getFileExtensionFromUrl(picturePath);
//                } else {
                imageName = String.valueOf(millis) + "_0.jpg";
//                }
            } else if (ivTemparory == ivChildImage1) {
//                String extension = MimeTypeMap.getFileExtensionFromUrl(picturePath);
//                if (extension != null) {
//                    imageName1 =  String.valueOf(millis) + "_1." + MimeTypeMap.getFileExtensionFromUrl(picturePath);
//                } else {
                imageName1 = String.valueOf(millis) + "_1.jpg";
//                }
            } else if (ivTemparory == ivChildImage2) {
//                String extension = MimeTypeMap.getFileExtensionFromUrl(picturePath);
//                if (extension != null) {
//                    imageName2 =  String.valueOf(millis)+ "_2." + MimeTypeMap.getFileExtensionFromUrl(picturePath);
//                } else {
                imageName2 = String.valueOf(millis) + "_2.jpg";
//                }
            } else if (ivTemparory == ivChildImage3) {
//                String extension = MimeTypeMap.getFileExtensionFromUrl(picturePath);
//                if (extension != null) {
//                    imageName3 =  String.valueOf(millis) + "_3." + MimeTypeMap.getFileExtensionFromUrl(picturePath);
//                } else {
                imageName3 = String.valueOf(millis) + "_3.jpg";
//                }
            } else if (ivTemparory == ivChildImage4) {
//                String extension = MimeTypeMap.getFileExtensionFromUrl(picturePath);
//                if (extension != null) {
//                    imageName4 =  String.valueOf(millis) + "_4." + MimeTypeMap.getFileExtensionFromUrl(picturePath);
//                } else {
                imageName4 = String.valueOf(millis) + "_4.jpg";
//                }
            } else if (ivTemparory == ivChildImage5) {
//                String extension = MimeTypeMap.getFileExtensionFromUrl(picturePath);
//                if (extension != null) {
//                    imageName5 =  String.valueOf(millis) + "_5." + MimeTypeMap.getFileExtensionFromUrl(picturePath);
//                } else {
                imageName5 = String.valueOf(millis) + "_5.jpg";
//                }
            }
//            ivTemparory.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            ivTemparory.setImageBitmap(bitmap);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 110, bos);
            byte[] byteData = bos.toByteArray();
            imagePhysicalNameBytes = Base64.encodeToString(byteData, Base64.DEFAULT);
            return;
        } else if (requestCode == 101 && data != null && data.getData() != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            File file = new File(picturePath);
            long millis = System.currentTimeMillis();
            if (ivTemparory.getId() == ivSpouseImage.getId()) {
//                String extension = MimeTypeMap.getFileExtensionFromUrl(picturePath);
//                if (extension != null) {
//                    imageName =  String.valueOf(millis) + "_0." + MimeTypeMap.getFileExtensionFromUrl(picturePath);
//                } else {
                imageName = String.valueOf(millis) + "_0.jpg";
//                }
            }
            if (ivTemparory.getId() == ivChildImage1.getId()) {
//                String extension = MimeTypeMap.getFileExtensionFromUrl(picturePath);
//                if (extension != null) {
//                    imageName1 =  String.valueOf(millis) + "_1." + MimeTypeMap.getFileExtensionFromUrl(picturePath);
//                } else {
                imageName1 = String.valueOf(millis) + "_1.jpg";
//                }
            }
            if (ivTemparory.getId() == ivChildImage2.getId()) {
//                String extension = MimeTypeMap.getFileExtensionFromUrl(picturePath);
//                if (extension != null) {
//                    imageName2 =  String.valueOf(millis) + "_2." + MimeTypeMap.getFileExtensionFromUrl(picturePath);
//                } else {
                imageName2 = String.valueOf(millis) + "_2.jpg";
//                }
            }
            if (ivTemparory.getId() == ivChildImage3.getId()) {
//                String extension = MimeTypeMap.getFileExtensionFromUrl(picturePath);
//                if (extension != null) {
//                    imageName3 =  String.valueOf(millis) + "_3." + MimeTypeMap.getFileExtensionFromUrl(picturePath);
//                } else {
                imageName3 = String.valueOf(millis) + "_3.jpg";
//                }
            }
            if (ivTemparory.getId() == ivChildImage4.getId()) {
//                String extension = MimeTypeMap.getFileExtensionFromUrl(picturePath);
//                if (extension != null) {
//                    imageName4 =  String.valueOf(millis) + "_4." + MimeTypeMap.getFileExtensionFromUrl(picturePath);
//                } else {
                imageName4 = String.valueOf(millis) + "_4.jpg";
//                }
            }
            if (ivTemparory.getId() == ivChildImage5.getId()) {
//                String extension = MimeTypeMap.getFileExtensionFromUrl(picturePath);
//                if (extension != null) {
//                    imageName5 =  String.valueOf(millis) + "_5." + MimeTypeMap.getFileExtensionFromUrl(picturePath);
//                } else {
                imageName5 = String.valueOf(millis) + "_5.jpg";
//                }
            }
            cursor.close();
        }
        if (!picturePath.equals("")) {
            try {
                File file = new File(picturePath);
                Bitmap bitmap = decodeFile(file);

                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                byte[] bytedata = bos.toByteArray();
                if (ivTemparory.getId() == ivSpouseImage.getId()) {
                    ivSpouseImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    ivSpouseImage.setImageBitmap(bitmap);
                    imagePhysicalNameBytes = Base64.encodeToString(bytedata, Base64.DEFAULT);
                }
                if (ivTemparory.getId() == ivChildImage1.getId()) {
                    ivChildImage1.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    ivChildImage1.setImageBitmap(bitmap);
                    imagePhysicalNameBytes1 = Base64.encodeToString(bytedata, Base64.DEFAULT);
                }
                if (ivTemparory.getId() == ivChildImage2.getId()) {
                    ivChildImage2.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    ivChildImage2.setImageBitmap(bitmap);
                    imagePhysicalNameBytes2 = Base64.encodeToString(bytedata, Base64.DEFAULT);
                }
                if (ivTemparory.getId() == ivChildImage3.getId()) {
                    ivChildImage3.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    ivChildImage3.setImageBitmap(bitmap);
                    imagePhysicalNameBytes3 = Base64.encodeToString(bytedata, Base64.DEFAULT);
                }
                if (ivTemparory.getId() == ivChildImage4.getId()) {
                    ivChildImage4.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    ivChildImage4.setImageBitmap(bitmap);
                    imagePhysicalNameBytes4 = Base64.encodeToString(bytedata, Base64.DEFAULT);
                }
                if (ivTemparory.getId() == ivChildImage5.getId()) {
                    ivChildImage5.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    ivChildImage5.setImageBitmap(bitmap);
                    imagePhysicalNameBytes5 = Base64.encodeToString(bytedata, Base64.DEFAULT);
                }
                return;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
    }

    // region Private Methods

    public void EditTextOnClick(View v) {
        if (v.getId() == R.id.etAnniversaryDate) {
            Globals.ShowDatePickerDialog(etAnniversaryDate, getActivity(), false);
        } else if (v.getId() == R.id.etSpouseDOB) {
            Globals.ShowDatePickerDialog(etSpouseDOB, getActivity(), false);
        } else if (v.getId() == R.id.etChildDOB1) {
            Globals.ShowDatePickerDialog(etChildDOB1, getActivity(), false);
        } else if (v.getId() == R.id.etChildDOB2) {
            Globals.ShowDatePickerDialog(etChildDOB2, getActivity(), false);
        } else if (v.getId() == R.id.etChildDOB3) {
            Globals.ShowDatePickerDialog(etChildDOB3, getActivity(), false);
        } else if (v.getId() == R.id.etChildDOB4) {
            Globals.ShowDatePickerDialog(etChildDOB4, getActivity(), false);
        } else if (v.getId() == R.id.etChildDOB5) {
            Globals.ShowDatePickerDialog(etChildDOB5, getActivity(), false);
        }
    }

    private void UpdateDetailRequest() {
        progressDialog = new ProgressDialog();
        progressDialog.show(getActivity().getSupportFragmentManager(), "");
        try {
            MemberJSONParser objMemberJSONParser = new MemberJSONParser();
            MemberMaster objMemberMaster = new MemberMaster();
            MemberRelativesTran objMemberRelativesTran = new MemberRelativesTran();
            ArrayList<MemberRelativesTran> lstMemberRelativesTren = new ArrayList<>();
            objMemberMaster.setMemberMasterId(Globals.memberMasterId);
            objMemberMaster.setProfession(actProfession.getText().toString());

//            if (actQualification.getText().toString().substring(actQualification.getText().toString().length() - 1).equals(",")) {
//                objMemberMaster.setQualification(actQualification.getText().toString().substring(0, actQualification.getText().toString().length() - 2));
////            }if(actQualification.getText().toString().substring(actQualification.getText().toString().length()-2).equals(","))
////            {
////                objMemberMaster.setQualification(actQualification.getText().toString().substring(0,actQualification.getText().toString().length()-3));
//            } else {
//                objMemberMaster.setQualification(actQualification.getText().toString());
//            }

            if (actQualification.getText().subSequence(actQualification.length() - 1, actQualification.length()).toString().equals(",")) {
                objMemberMaster.setQualification(actQualification.getText().toString().substring(0, actQualification.length() - 1));
            } else if (actQualification.getText().subSequence(actQualification.length() - 1, actQualification.length()).toString().equals(" ")) {
                if (actQualification.getText().subSequence(actQualification.length() - 2, actQualification.length() - 1).toString().equals(",")) {
                    objMemberMaster.setQualification(actQualification.getText().toString().substring(0, actQualification.length() - 2));
                } else {
                    objMemberMaster.setQualification(actQualification.getText().toString().substring(0, actQualification.length() - 1));
                }
            } else {
                objMemberMaster.setQualification(actQualification.getText().toString());
            }


            if (spinnerBloodGroup.getSelectedItemPosition() > 0) {
                objMemberMaster.setBloodGroup(spinnerBloodGroup.getSelectedItem().toString());
            }
            if (rbMarried.isChecked()) {
                objMemberMaster.setIsMarried(true);
                if (!etAnniversaryDate.getText().toString().isEmpty()) {
                    objMemberMaster.setAnniversaryDate(etAnniversaryDate.getText().toString());
                }
                objMemberRelativesTran.setlinktoMemberMasterId(Globals.memberMasterId);
                if (this.objMemberMaster != null && this.objMemberMaster.getLstMemberRelativeTran() != null && this.objMemberMaster.getLstMemberRelativeTran().size() > 0) {
                    objMemberRelativesTran.setMemberRelativesTranId(this.objMemberMaster.getLstMemberRelativeTran().get(0).getMemberRelativesTranId());
                }
                objMemberRelativesTran.setRelativeName(etSpouseName.getText().toString());
                if (!etSpouseDOB.getText().toString().isEmpty()) {
                    objMemberRelativesTran.setBirthDate(etSpouseDOB.getText().toString());
                }
                if (imageName != null && !imageName.equals("")) {
//                strImageName = imageName.substring(0, imageName.lastIndexOf(".")) + "_" + simpleDateFormat.format(new Date()) + imageName.substring(imageName.lastIndexOf("."), imageName.length());
                    strImageName = imageName;
                    objMemberRelativesTran.setImageName(strImageName);
                    objMemberRelativesTran.setImageNameBytes(imagePhysicalNameBytes);
                }
                if (objMemberRelativesTran.getImageName() == null && !isRemoveS && bitmap != null) {
                    if (this.objMemberMaster != null && this.objMemberMaster.getLstMemberRelativeTran() != null && this.objMemberMaster.getLstMemberRelativeTran().size() > 0) {
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                        byte[] bytedata = bos.toByteArray();
                        objMemberRelativesTran.setImageNameBytes(Base64.encodeToString(bytedata, Base64.DEFAULT));
                        long millis = System.currentTimeMillis();
                        objMemberRelativesTran.setImageName(String.valueOf(millis) + "_0.jpg");
                    }
                }
                SharePreferenceManage objSharePreferenceManage = new SharePreferenceManage();
//                if(isUpdate) {
                if (objSharePreferenceManage.GetPreference("LoginPreference", "Gender", getActivity()) != null) {
                    if (objSharePreferenceManage.GetPreference("LoginPreference", "Gender", getActivity()).equals("Male")) {
                        objMemberRelativesTran.setGender("Female");
                        objMemberRelativesTran.setRelation("Wife");
                    } else if (objSharePreferenceManage.GetPreference("LoginPreference", "Gender", getActivity()).equals("Female")) {
                        objMemberRelativesTran.setGender("Male");
                        objMemberRelativesTran.setRelation("Husbund");
                    }
                }
//                }
//                else
//                {
//                    if (objMemberMaster.getGender().equals("Male")) {
//                        objMemberRelativesTran.setGender("Female");
//                        objMemberRelativesTran.setRelation("Wife");
//                    } else if (objMemberMaster.getGender().equals("Female")) {
//                        objMemberRelativesTran.setGender("Male");
//                        objMemberRelativesTran.setRelation("Husbund");
//                    }
//                }

                lstMemberRelativesTren.add(objMemberRelativesTran);

                if (!checkBox.isChecked()) {
                    if (llChildDetail1.getVisibility() == View.VISIBLE && etChildName1.getText().toString() != null && !etChildName1.getText().toString().trim().equals("")) {
                        objMemberRelativesTran = new MemberRelativesTran();
                        objMemberRelativesTran.setlinktoMemberMasterId(Globals.memberMasterId);
                        if (this.objMemberMaster != null && this.objMemberMaster.getLstMemberRelativeTran() != null && this.objMemberMaster.getLstMemberRelativeTran().size() > 1) {
                            objMemberRelativesTran.setMemberRelativesTranId(this.objMemberMaster.getLstMemberRelativeTran().get(1).getMemberRelativesTranId());
                        }
                        objMemberRelativesTran.setRelativeName(etChildName1.getText().toString());
                        if (!etChildDOB1.getText().toString().isEmpty()) {
                            objMemberRelativesTran.setBirthDate(etChildDOB1.getText().toString());
                        }
                        if (imageName1 != null && !imageName1.equals("")) {
//                strImageName = imageName.substring(0, imageName.lastIndexOf(".")) + "_" + simpleDateFormat.format(new Date()) + imageName.substring(imageName.lastIndexOf("."), imageName.length());
                            strImageName = imageName1;
                            objMemberRelativesTran.setImageName(strImageName);
                            objMemberRelativesTran.setImageNameBytes(imagePhysicalNameBytes1);
                        }
                        if (objMemberRelativesTran.getImageName() == null && !isRemove1 && bitmap1 != null) {
                            if (this.objMemberMaster != null && this.objMemberMaster.getLstMemberRelativeTran() != null && this.objMemberMaster.getLstMemberRelativeTran().size() > 0) {
                                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                                bitmap1.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                                byte[] bytedata = bos.toByteArray();
                                objMemberRelativesTran.setImageNameBytes(Base64.encodeToString(bytedata, Base64.DEFAULT));
                                long millis = System.currentTimeMillis();
                                objMemberRelativesTran.setImageName(String.valueOf(millis) + "_1.jpg");
                            }
                        }
                        if (rbMale1.isChecked()) {
                            objMemberRelativesTran.setGender(rbMale1.getText().toString());
                            objMemberRelativesTran.setRelation("Son");
                        }
                        if (rbFemale1.isChecked()) {
                            objMemberRelativesTran.setGender(rbFemale1.getText().toString());
                            objMemberRelativesTran.setRelation("Daughter");
                        }
                        lstMemberRelativesTren.add(objMemberRelativesTran);
                    }

                    if (llChildDetail2.getVisibility() == View.VISIBLE && etChildName2.getText().toString() != null && !etChildName2.getText().toString().trim().equals("")) {
                        objMemberRelativesTran = new MemberRelativesTran();
                        if (this.objMemberMaster != null && this.objMemberMaster.getLstMemberRelativeTran() != null && this.objMemberMaster.getLstMemberRelativeTran().size() > 2) {
                            objMemberRelativesTran.setMemberRelativesTranId(this.objMemberMaster.getLstMemberRelativeTran().get(2).getMemberRelativesTranId());
                        }
                        objMemberRelativesTran.setlinktoMemberMasterId(Globals.memberMasterId);
                        objMemberRelativesTran.setRelativeName(etChildName2.getText().toString());
                        if (!etChildDOB2.getText().toString().isEmpty()) {
                            objMemberRelativesTran.setBirthDate(etChildDOB2.getText().toString());
                        }
                        if (imageName2 != null && !imageName2.equals("")) {
//                strImageName = imageName.substring(0, imageName.lastIndexOf(".")) + "_" + simpleDateFormat.format(new Date()) + imageName.substring(imageName.lastIndexOf("."), imageName.length());
                            strImageName = imageName2;
                            objMemberRelativesTran.setImageName(strImageName);
                            objMemberRelativesTran.setImageNameBytes(imagePhysicalNameBytes2);
                        }
                        if (objMemberRelativesTran.getImageName() == null && !isRemove2 && bitmap2 != null) {
                            if (this.objMemberMaster != null && this.objMemberMaster.getLstMemberRelativeTran() != null && this.objMemberMaster.getLstMemberRelativeTran().size() > 0) {
                                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                                bitmap2.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                                byte[] bytedata = bos.toByteArray();
                                objMemberRelativesTran.setImageNameBytes(Base64.encodeToString(bytedata, Base64.DEFAULT));
                                long millis = System.currentTimeMillis();
                                objMemberRelativesTran.setImageName(String.valueOf(millis) + "_2.jpg");
                            }
                        }
                        if (rbMale2.isChecked()) {
                            objMemberRelativesTran.setGender(rbMale2.getText().toString());
                            objMemberRelativesTran.setRelation("Son");
                        }
                        if (rbFemale2.isChecked()) {
                            objMemberRelativesTran.setGender(rbFemale2.getText().toString());
                            objMemberRelativesTran.setRelation("Daughter");
                        }
                        lstMemberRelativesTren.add(objMemberRelativesTran);
                    }

                    if (llChildDetail3.getVisibility() == View.VISIBLE && etChildName3.getText().toString() != null && !etChildName3.getText().toString().trim().equals("")) {
                        objMemberRelativesTran = new MemberRelativesTran();
                        if (this.objMemberMaster != null && this.objMemberMaster.getLstMemberRelativeTran() != null && this.objMemberMaster.getLstMemberRelativeTran().size() > 3) {
                            objMemberRelativesTran.setMemberRelativesTranId(this.objMemberMaster.getLstMemberRelativeTran().get(3).getMemberRelativesTranId());
                        }
                        objMemberRelativesTran.setlinktoMemberMasterId(Globals.memberMasterId);
                        objMemberRelativesTran.setRelativeName(etChildName3.getText().toString());
                        if (!etChildDOB3.getText().toString().isEmpty()) {
                            objMemberRelativesTran.setBirthDate(etChildDOB3.getText().toString());
                        }
                        if (imageName3 != null && !imageName3.equals("")) {
//                strImageName = imageName.substring(0, imageName.lastIndexOf(".")) + "_" + simpleDateFormat.format(new Date()) + imageName.substring(imageName.lastIndexOf("."), imageName.length());
                            strImageName = imageName3;
                            objMemberRelativesTran.setImageName(strImageName);
                            objMemberRelativesTran.setImageNameBytes(imagePhysicalNameBytes3);
                        }
                        if (objMemberRelativesTran.getImageName() == null && !isRemove3 && bitmap3 != null) {
                            if (this.objMemberMaster != null && this.objMemberMaster.getLstMemberRelativeTran() != null && this.objMemberMaster.getLstMemberRelativeTran().size() > 0) {

                                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                                bitmap3.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                                byte[] bytedata = bos.toByteArray();
                                objMemberRelativesTran.setImageNameBytes(Base64.encodeToString(bytedata, Base64.DEFAULT));
                                long millis = System.currentTimeMillis();
                                objMemberRelativesTran.setImageName(String.valueOf(millis) + "_3.jpg");
                            }
                        }
                        if (rbMale3.isChecked()) {
                            objMemberRelativesTran.setGender(rbMale3.getText().toString());
                            objMemberRelativesTran.setRelation("Son");
                        }
                        if (rbFemale3.isChecked()) {
                            objMemberRelativesTran.setGender(rbFemale3.getText().toString());
                            objMemberRelativesTran.setRelation("Daughter");
                        }
                        lstMemberRelativesTren.add(objMemberRelativesTran);
                    }

                    if (llChildDetail4.getVisibility() == View.VISIBLE && etChildName4.getText().toString() != null && !etChildName4.getText().toString().trim().equals("")) {
                        objMemberRelativesTran = new MemberRelativesTran();
                        if (this.objMemberMaster != null && this.objMemberMaster.getLstMemberRelativeTran() != null && this.objMemberMaster.getLstMemberRelativeTran().size() > 4) {
                            objMemberRelativesTran.setMemberRelativesTranId(this.objMemberMaster.getLstMemberRelativeTran().get(4).getMemberRelativesTranId());
                        }
                        objMemberRelativesTran.setlinktoMemberMasterId(Globals.memberMasterId);
                        objMemberRelativesTran.setRelativeName(etChildName4.getText().toString());
                        if (!etChildDOB4.getText().toString().isEmpty()) {
                            objMemberRelativesTran.setBirthDate(etChildDOB4.getText().toString());
                        }
                        if (imageName4 != null && !imageName4.equals("")) {
//                strImageName = imageName.substring(0, imageName.lastIndexOf(".")) + "_" + simpleDateFormat.format(new Date()) + imageName.substring(imageName.lastIndexOf("."), imageName.length());
                            strImageName = imageName4;
                            objMemberRelativesTran.setImageName(strImageName);
                            objMemberRelativesTran.setImageNameBytes(imagePhysicalNameBytes4);
                        }
                        if (objMemberRelativesTran.getImageName() == null && !isRemove4 && bitmap4 != null) {
                            if (this.objMemberMaster != null && this.objMemberMaster.getLstMemberRelativeTran() != null && this.objMemberMaster.getLstMemberRelativeTran().size() > 0) {

                                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                                bitmap4.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                                byte[] bytedata = bos.toByteArray();
                                objMemberRelativesTran.setImageNameBytes(Base64.encodeToString(bytedata, Base64.DEFAULT));
                                long millis = System.currentTimeMillis();
                                objMemberRelativesTran.setImageName(String.valueOf(millis) + "_4.jpg");
                            }
                        }
                        if (rbMale4.isChecked()) {
                            objMemberRelativesTran.setGender(rbMale4.getText().toString());
                            objMemberRelativesTran.setRelation("Son");
                        }
                        if (rbFemale4.isChecked()) {
                            objMemberRelativesTran.setGender(rbFemale4.getText().toString());
                            objMemberRelativesTran.setRelation("Daughter");
                        }
                        lstMemberRelativesTren.add(objMemberRelativesTran);
                    }
                    if (llChildDetail5.getVisibility() == View.VISIBLE && etChildName5.getText().toString() != null && !etChildName5.getText().toString().trim().equals("")) {
                        objMemberRelativesTran = new MemberRelativesTran();
                        if (this.objMemberMaster != null && this.objMemberMaster.getLstMemberRelativeTran() != null && this.objMemberMaster.getLstMemberRelativeTran().size() > 5) {
                            objMemberRelativesTran.setMemberRelativesTranId(this.objMemberMaster.getLstMemberRelativeTran().get(5).getMemberRelativesTranId());
                        }
                        objMemberRelativesTran.setlinktoMemberMasterId(Globals.memberMasterId);
                        objMemberRelativesTran.setRelativeName(etChildName5.getText().toString());
                        if (!etChildDOB5.getText().toString().isEmpty()) {
                            objMemberRelativesTran.setBirthDate(etChildDOB5.getText().toString());
                        }
                        if (imageName5 != null && !imageName5.equals("")) {
//                strImageName = imageName.substring(0, imageName.lastIndexOf(".")) + "_" + simpleDateFormat.format(new Date()) + imageName.substring(imageName.lastIndexOf("."), imageName.length());
                            strImageName = imageName5;
                            objMemberRelativesTran.setImageName(strImageName);
                            objMemberRelativesTran.setImageNameBytes(imagePhysicalNameBytes5);
                        }
                        if (objMemberRelativesTran.getImageName() == null && !isRemove5 && bitmap5 != null) {
                            if (this.objMemberMaster != null && this.objMemberMaster.getLstMemberRelativeTran() != null && this.objMemberMaster.getLstMemberRelativeTran().size() > 0) {

                                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                                bitmap5.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                                byte[] bytedata = bos.toByteArray();
                                objMemberRelativesTran.setImageNameBytes(Base64.encodeToString(bytedata, Base64.DEFAULT));
                                long millis = System.currentTimeMillis();
                                objMemberRelativesTran.setImageName(String.valueOf(millis) + "_5.jpg");
                            }
                        }
                        if (rbMale5.isChecked()) {
                            objMemberRelativesTran.setGender(rbMale5.getText().toString());
                            objMemberRelativesTran.setRelation("Son");
                        }
                        if (rbFemale5.isChecked()) {
                            objMemberRelativesTran.setGender(rbFemale5.getText().toString());
                            objMemberRelativesTran.setRelation("Daughter");
                        }
                        lstMemberRelativesTren.add(objMemberRelativesTran);
                    }
                    objMemberMaster.setLstMemberRelativeTran(lstMemberRelativesTren);
                }
            } else {
                objMemberMaster.setIsMarried(false);
            }
//            if (isUpdate) {
            objMemberMaster.setLstMemberRelativeTran(lstMemberRelativesTren);

            objMemberJSONParser.UpdateMemberMasterPersonalDetail(getActivity(), RegistrationDetailFragment.this, objMemberMaster);
//            } else {
////                RegistrationContactFragment objRegistrationContactFragment = new RegistrationContactFragment();
////                Bundle bundle = new Bundle();
////                bundle.putParcelable("MemberMaster", objMemberMaster);
////                objRegistrationContactFragment.setArguments(bundle);
////                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
////                fragmentTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out, 0, R.anim.right_exit);
////                fragmentTransaction.replace(R.id.llPersonal, objRegistrationContactFragment, "ContactDetail");
////                fragmentTransaction.addToBackStack("ContactDetail");
////                fragmentTransaction.commit();
//                onNextClick objOnNextClick =(onNextClick) ((SignInActivity)getActivity());
//                objOnNextClick.OnNextClick(objMemberMaster);
//            }
        } catch (Exception e) {
            e.printStackTrace();
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
                    actProfession.setAdapter(adapter);
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
//            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.row_spinner, lstStrings);
                        alString = lstStrings;
                        alStringFilter = new ArrayList<>();
//            actQualification.setAdapter(adapter);
                        actQualification.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
                        if (actQualification.getText().toString().isEmpty()) {
                            SetArrayListAdapter(alString);
                        } else {
                            if (isDeleted) {
                                if (actQualification.getText().subSequence(actQualification.length() - 1, actQualification.length()).toString().equals(",")) {
                                    selectedValue = String.valueOf(actQualification.getText().subSequence(0, actQualification.length()) + " ").split(", ");
                                } else if (actQualification.getText().subSequence(actQualification.length() - 1, actQualification.length()).toString().equals(" ")) {
                                    selectedValue = actQualification.getText().subSequence(0, actQualification.length()).toString().split(", ");
                                } else {
                                    selectedValue = actQualification.getText().subSequence(0, actQualification.length()).toString().split(", ");
                                    actQualification.setText(actQualification.getText() + ", ");
                                }
                                if (selectedValue != null) {
                                    for (int i = 0; i <= selectedValue.length; i++) {
                                        UpdateArrayListAdapter(selectedValue[i]);
                                    }
                                } else {
                                    UpdateArrayListAdapter(null);
                                }
                                isDeleted = false;
                            }

                        }

                    }
                }
            }
        } catch (Exception e) {
            // Log exception

        }
    }

    private void SetData() {

//        if (isUpdate) {
        if (objMemberMaster.getLstMemberRelativeTran() != null && objMemberMaster.getLstMemberRelativeTran().size() > 1) {
            checkBox.setChecked(false);
            llChildDetail1.setVisibility(View.VISIBLE);
            if (objMemberMaster.getLstMemberRelativeTran().size() > 2) {
                llChildDetail2.setVisibility(View.VISIBLE);
                if (objMemberMaster.getLstMemberRelativeTran().size() > 3) {
                    llChildDetail3.setVisibility(View.VISIBLE);
                    if (objMemberMaster.getLstMemberRelativeTran().size() > 4) {
                        llChildDetail4.setVisibility(View.VISIBLE);
                        if (objMemberMaster.getLstMemberRelativeTran().size() > 5) {
                            llChildDetail5.setVisibility(View.VISIBLE);
                        } else {
                            llAdd4.setVisibility(View.VISIBLE);
                        }
                    } else {
                        llAdd3.setVisibility(View.VISIBLE);
                    }
                } else {
                    llAdd2.setVisibility(View.VISIBLE);
                }
            } else {
                llAdd1.setVisibility(View.VISIBLE);
            }
        } else {
            checkBox.setChecked(true);
        }
//            llChildDetail1.setVisibility(View.VISIBLE);
//            llChildDetail2.setVisibility(View.VISIBLE);
        txtChildDetail.setVisibility(View.VISIBLE);
        checkBox.setVisibility(View.VISIBLE);
//        } else {
//            llChildDetail1.setVisibility(View.GONE);
//            llChildDetail2.setVisibility(View.GONE);
//            llChildDetail3.setVisibility(View.GONE);
//            llChildDetail4.setVisibility(View.GONE);
//            llChildDetail5.setVisibility(View.GONE);
//            llAdd1.setVisibility(View.GONE);
//            llAdd2.setVisibility(View.GONE);
//            llAdd3.setVisibility(View.GONE);
//            llAdd4.setVisibility(View.GONE);
//            txtChildDetail.setVisibility(View.GONE);
//            checkBox.setVisibility(View.GONE);
//        }

        actProfession.setText(objMemberMaster.getProfession());
        actQualification.setText(objMemberMaster.getQualification());
        if (objMemberMaster.getQualification() != null && !objMemberMaster.getQualification().equals("")) {
            isDeleted = true;
        }

        for (int i = 0; i < Globals.BloodGroup.values().length; i++) {
            if (Globals.BloodGroup.getBlood(i).equals(objMemberMaster.getBloodGroup())) {
                spinnerBloodGroup.setSelection(i + 1);
            }
        }

        if (objMemberMaster.getAnniversaryDate() != null) {
            rbMarried.setChecked(true);
            rbUnmarried.setChecked(false);
            etAnniversaryDate.setText(objMemberMaster.getAnniversaryDate());
            if (objMemberMaster.getLstMemberRelativeTran() != null && objMemberMaster.getLstMemberRelativeTran().size() > 0) {
                etSpouseName.setText(objMemberMaster.getLstMemberRelativeTran().get(0).getRelativeName());
                etSpouseDOB.setText(objMemberMaster.getLstMemberRelativeTran().get(0).getBirthDate());
                if (objMemberMaster.getLstMemberRelativeTran().get(0).getImageName() != null) {
                    Glide.with(getActivity()).load(objMemberMaster.getLstMemberRelativeTran().get(0).getImageName()).asBitmap()
                            .diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(ivSpouseImage);
                    bitmap = Globals.getBitmapFromURL(objMemberMaster.getLstMemberRelativeTran().get(0).getImageName());
                }
                if (ivSpouseImage.getDrawable() == null) {
//                    ivProfile1.setVisibility(View.GONE);
//                    ivProfile.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                    ivSpouseImage.setImageResource(R.drawable.no_image);
                }
                if (objMemberMaster.getLstMemberRelativeTran() != null && objMemberMaster.getLstMemberRelativeTran().size() > 1) {
                    childCount = objMemberMaster.getLstMemberRelativeTran().size() - 1;
                    checkBox.setChecked(false);
                    etChildName1.setText(objMemberMaster.getLstMemberRelativeTran().get(1).getRelativeName());
                    etChildDOB1.setText(objMemberMaster.getLstMemberRelativeTran().get(1).getBirthDate());
                    if (objMemberMaster.getLstMemberRelativeTran().get(1).getImageName() != null) {
                        Glide.with(getActivity()).load(objMemberMaster.getLstMemberRelativeTran().get(1).getImageName()).asBitmap()
                                .diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(ivChildImage1);
                        bitmap1 = Globals.getBitmapFromURL(objMemberMaster.getLstMemberRelativeTran().get(1).getImageName());

                    }

                    if (objMemberMaster.getLstMemberRelativeTran().get(1).getGender().equals("Male")) {
                        rbMale1.setChecked(true);
                    }
                    if (objMemberMaster.getLstMemberRelativeTran().get(1).getGender().equals("Female")) {
                        rbFemale1.setChecked(true);
                    }

                    if (objMemberMaster.getLstMemberRelativeTran().size() > 2) {
                        etChildName2.setText(objMemberMaster.getLstMemberRelativeTran().get(2).getRelativeName());
                        etChildDOB2.setText(objMemberMaster.getLstMemberRelativeTran().get(2).getBirthDate());
                        if (objMemberMaster.getLstMemberRelativeTran().get(2).getImageName() != null) {
                            Glide.with(getActivity()).load(objMemberMaster.getLstMemberRelativeTran().get(2).getImageName()).asBitmap()
                                    .diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(ivChildImage2);
                            bitmap2 = Globals.getBitmapFromURL(objMemberMaster.getLstMemberRelativeTran().get(2).getImageName());

                        }
                        if (objMemberMaster.getLstMemberRelativeTran().get(2).getGender().equals("Male")) {
                            rbMale2.setChecked(true);
                        }
                        if (objMemberMaster.getLstMemberRelativeTran().get(2).getGender().equals("Female")) {
                            rbFemale2.setChecked(true);
                        }

                        if (objMemberMaster.getLstMemberRelativeTran().size() > 3) {
                            etChildName3.setText(objMemberMaster.getLstMemberRelativeTran().get(3).getRelativeName());
                            etChildDOB3.setText(objMemberMaster.getLstMemberRelativeTran().get(3).getBirthDate());
                            if (objMemberMaster.getLstMemberRelativeTran().get(3).getImageName() != null) {
                                Glide.with(getActivity()).load(objMemberMaster.getLstMemberRelativeTran().get(3).getImageName()).asBitmap()
                                        .diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(ivChildImage3);
                                bitmap3 = Globals.getBitmapFromURL(objMemberMaster.getLstMemberRelativeTran().get(3).getImageName());

                            }
                            if (objMemberMaster.getLstMemberRelativeTran().get(3).getGender().equals("Male")) {
                                rbMale3.setChecked(true);
                            }
                            if (objMemberMaster.getLstMemberRelativeTran().get(3).getGender().equals("Female")) {
                                rbFemale3.setChecked(true);
                            }

                            if (objMemberMaster.getLstMemberRelativeTran().size() > 4) {
                                etChildName4.setText(objMemberMaster.getLstMemberRelativeTran().get(4).getRelativeName());
                                etChildDOB4.setText(objMemberMaster.getLstMemberRelativeTran().get(4).getBirthDate());
                                if (objMemberMaster.getLstMemberRelativeTran().get(4).getImageName() != null) {
                                    Glide.with(getActivity()).load(objMemberMaster.getLstMemberRelativeTran().get(4).getImageName()).asBitmap()
                                            .diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(ivChildImage4);
                                    bitmap4 = Globals.getBitmapFromURL(objMemberMaster.getLstMemberRelativeTran().get(4).getImageName());

                                }
                                if (objMemberMaster.getLstMemberRelativeTran().get(4).getGender().equals("Male")) {
                                    rbMale4.setChecked(true);
                                }
                                if (objMemberMaster.getLstMemberRelativeTran().get(4).getGender().equals("Female")) {
                                    rbFemale4.setChecked(true);
                                }

                                if (objMemberMaster.getLstMemberRelativeTran().size() > 5) {
                                    etChildName5.setText(objMemberMaster.getLstMemberRelativeTran().get(5).getRelativeName());
                                    etChildDOB5.setText(objMemberMaster.getLstMemberRelativeTran().get(5).getBirthDate());
                                    if (objMemberMaster.getLstMemberRelativeTran().get(5).getImageName() != null) {
                                        Glide.with(getActivity()).load(objMemberMaster.getLstMemberRelativeTran().get(5).getImageName()).asBitmap()
                                                .diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(ivChildImage5);
                                        bitmap5 = Globals.getBitmapFromURL(objMemberMaster.getLstMemberRelativeTran().get(5).getImageName());

                                    }
                                    if (objMemberMaster.getLstMemberRelativeTran().get(5).getGender().equals("Male")) {
                                        rbMale5.setChecked(true);
                                    }
                                    if (objMemberMaster.getLstMemberRelativeTran().get(5).getGender().equals("Female")) {
                                        rbFemale5.setChecked(true);
                                    }
                                }
                            }
                        }
                    }
                } else {
                    checkBox.setChecked(true);
                }
            }
            rbMarried.setChecked(true);
            rbUnmarried.setChecked(false);
        } else {
            rbMarried.setChecked(false);
            rbUnmarried.setChecked(true);
            etAnniversaryDate.setVisibility(View.GONE);
            etSpouseName.setVisibility(View.GONE);
            etSpouseDOB.setVisibility(View.GONE);
            txtAnniversaryDate.setVisibility(View.GONE);
            txtSpouseName.setVisibility(View.GONE);
            txtSpouseDOB.setVisibility(View.GONE);
            txtSpouseDetail.setVisibility(View.GONE);
            ivSpouseImage.setVisibility(View.GONE);
            txtChildDetail.setVisibility(View.GONE);
            checkBox.setVisibility(View.GONE);
            llChildDetail1.setVisibility(View.GONE);
            llChildDetail2.setVisibility(View.GONE);
            llChildDetail3.setVisibility(View.GONE);
            llChildDetail4.setVisibility(View.GONE);
            llChildDetail5.setVisibility(View.GONE);
            llAdd1.setVisibility(View.GONE);
            llAdd2.setVisibility(View.GONE);
            llAdd3.setVisibility(View.GONE);
            llAdd4.setVisibility(View.GONE);
        }

        if (actQualification.getText().toString().isEmpty()) {
            SetArrayListAdapter(alString);
        } else {
            if (isDeleted) {
                if (actQualification.getText().subSequence(actQualification.length() - 1, actQualification.length()).toString().equals(",")) {
                    selectedValue = String.valueOf(actQualification.getText().subSequence(0, actQualification.length()) + " ").split(", ");
                } else if (actQualification.getText().subSequence(actQualification.length() - 1, actQualification.length()).toString().equals(" ")) {
                    selectedValue = actQualification.getText().subSequence(0, actQualification.length()).toString().split(", ");
                } else {
                    selectedValue = actQualification.getText().subSequence(0, actQualification.length()).toString().split(", ");
                    actQualification.setText(actQualification.getText() + ", ");
                }
                if (selectedValue != null) {
                    for (int i = 0; i < selectedValue.length; i++) {
                        UpdateArrayListAdapter(selectedValue[i]);
                    }
                } else {
                    UpdateArrayListAdapter(null);
                }
                isDeleted = false;
            }

        }

    }

    private Bitmap decodeFile(File f) {
        try {
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);

            // The new size we want to scale to
            final int REQUIRED_SIZE = 300;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {
        }
        return null;
    }

    private void SelectImageProfile(final Context context, final int requestCodeCamera, final int requestCodeGallery) {
//        final CharSequence[] items = {"Take Photo", "Choose from Gallery", "Remove Image"};
        final CharSequence[] items = {"Choose from Gallery", "Remove Image"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialog);
        builder.setTitle("ADD PHOTO");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment
                            .getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    ((Activity) context).startActivityForResult(intent, requestCodeCamera);
                } else if (items[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    ((Activity) context).startActivityForResult(Intent.createChooser(intent, "Select File"), requestCodeGallery);
                } else if (items[item].equals("Remove Image")) {

                    if (ivTemparory.getId() == ivSpouseImage.getId()) {
                        isRemoveS = true;
                        ivSpouseImage.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                        ivSpouseImage.setImageResource(R.drawable.no_image);
                        imageName = null;
                        imagePhysicalNameBytes = null;
                    } else if (ivTemparory.getId() == ivChildImage1.getId()) {
                        isRemove1 = true;
                        ivChildImage1.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                        ivChildImage1.setImageResource(R.drawable.no_image);
                        imageName1 = null;
                        imagePhysicalNameBytes1 = null;
                    } else if (ivTemparory.getId() == ivChildImage2.getId()) {
                        isRemove2 = true;
                        ivChildImage2.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                        ivChildImage2.setImageResource(R.drawable.no_image);
                        imageName2 = null;
                        imagePhysicalNameBytes2 = null;
                    } else if (ivTemparory.getId() == ivChildImage3.getId()) {
                        isRemove3 = true;
                        ivChildImage3.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                        ivChildImage3.setImageResource(R.drawable.no_image);
                        imageName3 = null;
                        imagePhysicalNameBytes3 = null;
                    } else if (ivTemparory.getId() == ivChildImage4.getId()) {
                        isRemove4 = true;
                        ivChildImage4.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                        ivChildImage4.setImageResource(R.drawable.no_image);
                        imageName4 = null;
                        imagePhysicalNameBytes4 = null;
                    } else if (ivTemparory.getId() == ivChildImage5.getId()) {
                        isRemove5 = true;
                        ivChildImage5.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                        ivChildImage5.setImageResource(R.drawable.no_image);
                        imageName5 = null;
                        imagePhysicalNameBytes5 = null;
                    }
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private boolean ValidateControls() {
        boolean IsValid = true;

        if (actProfession.getText().toString().equals("")) {
            actProfession.setError("Enter profession");
            IsValid = false;
        }
        if (actQualification.getText().toString().equals("")) {
            actQualification.setError("Enter qualification");
            IsValid = false;
        }
        if (rbMarried.isChecked()) {
            if (etAnniversaryDate.getText().toString().equals("")) {
                etAnniversaryDate.setError("Enter anniversary date");
                IsValid = false;
            }
            if (etSpouseName.getText().toString().equals("")) {
                etSpouseName.setError("Enter spouse name");
                IsValid = false;
            }
            if (etSpouseDOB.getText().toString().equals("")) {
                etSpouseDOB.setError("Enter spouse birthdate");
                IsValid = false;
            }
            if (!checkBox.isChecked()) {
                if (llChildDetail1.getVisibility() == View.VISIBLE) {
                    if (etChildName1.getText().toString().equals("")) {
                        etChildName1.setError("Enter child name");
                        IsValid = false;
                    }
                    if (etChildDOB1.getText().toString().equals("")) {
                        etChildDOB1.setError("Enter child birthdate");
                        IsValid = false;
                    }
                }
                if (llChildDetail2.getVisibility() == View.VISIBLE) {
                    if (etChildName2.getText().toString().equals("")) {
                        etChildName2.setError("Enter child name");
                        IsValid = false;
                    }
                    if (etChildDOB2.getText().toString().equals("")) {
                        etChildDOB2.setError("Enter child birthdate");
                        IsValid = false;
                    }
                }
                if (llChildDetail3.getVisibility() == View.VISIBLE) {
                    if (etChildName3.getText().toString().equals("")) {
                        etChildName3.setError("Enter child name");
                        IsValid = false;
                    }
                    if (etChildDOB3.getText().toString().equals("")) {
                        etChildDOB3.setError("Enter child birthdate");
                        IsValid = false;
                    }
                }
                if (llChildDetail4.getVisibility() == View.VISIBLE) {
                    if (etChildName4.getText().toString().equals("")) {
                        etChildName4.setError("Enter child name");
                        IsValid = false;
                    }
                    if (etChildDOB4.getText().toString().equals("")) {
                        etChildDOB4.setError("Enter child birthdate");
                        IsValid = false;
                    }
                }
                if (llChildDetail5.getVisibility() == View.VISIBLE) {
                    if (etChildName5.getText().toString().equals("")) {
                        etChildName5.setError("Enter child name");
                        IsValid = false;
                    }
                    if (etChildDOB5.getText().toString().equals("")) {
                        etChildDOB5.setError("Enter child birthdate");
                        IsValid = false;
                    }
                }
            }
        }
        return IsValid;
    }

    private void UpdateArrayListAdapter(String name) {
        int isRemove = -1;
        String str;
        if (name == null && isDeleted) {
            alStringFilter = new ArrayList<>();
            for (String strSelectedValue : selectedValue) {
                if (!strSelectedValue.equals("")) {
                    if (alStringFilter.size() == 0) {
                        for (int j = 0; j < alString.size(); j++) {
                            if (!strSelectedValue.equals(alString.get(j))) {
                                alStringFilter.add(alString.get(j));
                            }
                        }
                    } else {
                        for (int j = 0; j < alStringFilter.size(); j++) {
                            str = strSelectedValue;
                            if (alStringFilter.get(j).equals(str)) {
                                alStringFilter.remove(j);
                            }
                        }
                    }
                }
            }
            adapter = new ArrayAdapter<>(getActivity(), R.layout.row_spinner, alStringFilter);
            actQualification.setAdapter(adapter);
        } else {
            if (alStringFilter.size() == 0) {
                for (int i = 0; i < alString.size(); i++) {
                    if (!alString.get(i).equals(name)) {
                        alStringFilter.add(alString.get(i));
                    }
                }
                adapter = new ArrayAdapter<>(getActivity(), R.layout.row_spinner, alStringFilter);
                actQualification.setAdapter(adapter);

            } else {
                for (int j = 0; j < alStringFilter.size(); j++) {
                    if (alStringFilter.get(j).equals(name)) {
                        isRemove = j;
                    }
                }
                if (isRemove != -1) {
                    alStringFilter.remove(isRemove);
                }
            }
        }
        adapter = new ArrayAdapter<>(getActivity(), R.layout.row_spinner, alStringFilter);
        actQualification.setAdapter(adapter);
    }

    private void SetArrayListAdapter(ArrayList<String> alString) {
        alStringFilter = new ArrayList<>();
        if (alString != null) {
            adapter = new ArrayAdapter<>(getActivity(), R.layout.row_spinner, alString);
            actQualification.setAdapter(adapter);
        }
    }

    interface UpdateResponseListener {
        void UpdateResponse();
    }


    //endregion

}
