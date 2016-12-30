package com.arraybit.mym;

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
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.arraybit.global.Globals;
import com.arraybit.global.MarshMallowPermission;
import com.arraybit.global.Service;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.modal.MemberMaster;
import com.arraybit.modal.MemberMasterNew;
import com.arraybit.parser.MemberJSONParser;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.rey.material.widget.Button;
import com.rey.material.widget.EditText;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener, MemberJSONParser.MemberRequestListener {


    public static String imagePhysicalNameBytes;
    final int PIC_CROP = 1;
    EditText etFirstName, etLastName, etEmail, etPassword, etConfirmPassword, etPhone, etPhone1, etBirthDate;
    RadioGroup rgMain;
    RadioButton rbMale, rbFemale;
    Button btnSignUp;
    ImageView ivImage;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss.SSS", Locale.US);
    SharePreferenceManage objSharePreferenceManage;
    ProgressDialog progressDialog;
    String imageName, strImageName, picturePath = "";
    MarshMallowPermission marshMallowPermission;
    LinearLayout signUpActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

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
            getSupportActionBar().setTitle(getResources().getString(R.string.title_fragment_signup));
            //end
            Globals.SetToolBarBackground(RegistrationActivity.this, app_bar, ContextCompat.getColor(RegistrationActivity.this, R.color.colorPrimary), ContextCompat.getColor(RegistrationActivity.this, android.R.color.white));

            marshMallowPermission = new MarshMallowPermission(RegistrationActivity.this);
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!marshMallowPermission.checkPermissionForCamera()) {
                    marshMallowPermission.requestPermissionForCamera();
                }
                if (!marshMallowPermission.checkPermissionForExternalStorage()) {
                    marshMallowPermission.requestPermissionForExternalStorage();
                }
            }

            if (Service.CheckNet(this)) {
                FCMTokenGenerate();
            }

            //EditText
            signUpActivity = (LinearLayout) findViewById(R.id.signUpActivity);

            etFirstName = (EditText) findViewById(R.id.etFirstName);
            etLastName = (EditText) findViewById(R.id.etLastName);
            etPassword = (EditText) findViewById(R.id.etPassword);
            etConfirmPassword = (EditText) findViewById(R.id.etConfirmPassword);
            etEmail = (EditText) findViewById(R.id.etEmail);
            etPhone = (EditText) findViewById(R.id.etPhone);
            etPhone1 = (EditText) findViewById(R.id.etPhone1);
            etBirthDate = (EditText) findViewById(R.id.etDateOfBirth);
            etBirthDate.setInputType(InputType.TYPE_NULL);
            ivImage = (ImageView) findViewById(R.id.ivImage);

            //end

            //Radiogroup
            rgMain = (RadioGroup) findViewById(R.id.rgMain);
            //

            //RadioButton
            rbMale = (RadioButton) findViewById(R.id.rbMale);
            rbFemale = (RadioButton) findViewById(R.id.rbFemale);
            //end

            //button
            btnSignUp = (Button) findViewById(R.id.btnSignUp);
            //end

            //Spinner
            //

            //compound button
            CompoundButton cbSignIn = (CompoundButton) findViewById(R.id.cbSignIn);
            //end

            //event
            cbSignIn.setOnClickListener(this);
            btnSignUp.setOnClickListener(this);
            ivImage.setOnClickListener(this);
            etBirthDate.setOnClickListener(this);
            //end

//        Globals.CustomView(btnSignUp, ContextCompat.getColor(getActivity(),R.color.accent), ContextCompat.getColor(getActivity(),android.R.color.white));

            etPhone.setSelection(etPhone.getText().toString().length());
            etPhone1.setSelection(etPhone1.getText().toString().length());

            etPhone.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_ENTER) {
                        Globals.HideKeyBoard(RegistrationActivity.this, v);
                    }
                    return false;
                }
            });

            etPhone1.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_ENTER) {
                        Globals.HideKeyBoard(RegistrationActivity.this, v);
                    }
                    return false;
                }
            });

            etPhone1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(hasFocus)
                    {
                        etPhone1.setSelection(etPhone1.getText().toString().length());
                    }
                }
            });

            etPhone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(hasFocus)
                    {
                        etPhone.setSelection(etPhone.getText().toString().length());
                    }
                }
            });

            etPhone.addTextChangedListener(new TextWatcher() {
                String mobile;
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    mobile= etPhone.getText().toString();
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (etPhone.length() < 4) {
                        etPhone.setText("+91 ");
                        etPhone.setSelection(etPhone.getText().toString().length());
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(!s.toString().startsWith("+91",0)){
                        etPhone.setText(mobile);
                        Selection.setSelection(etPhone.getText(), 4);
                    }
                }
            });

            etPhone1.addTextChangedListener(new TextWatcher() {
                String mobile;
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    mobile= etPhone1.getText().toString();
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (etPhone1.length() < 4) {
                        etPhone1.setText("+91 ");
                        etPhone1.setSelection(etPhone1.getText().toString().length());
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(!s.toString().startsWith("+91",0)){
                        etPhone.setText(mobile);
                        Selection.setSelection(etPhone1.getText(), 4);
                    }
                }
            });

            etBirthDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        Globals.ShowDatePickerDialog(etBirthDate, RegistrationActivity.this, false);
                    }
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == RESULT_OK) {
                SelectImage(requestCode, data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        Globals.HideKeyBoard(RegistrationActivity.this, v);
        if (v.getId() == R.id.btnSignUp) {
            if (!ValidateControls()) {
                Globals.ShowSnackBar(v, getResources().getString(R.string.MsgValidation), RegistrationActivity.this, 1000);
                return;
            }
            if (Service.CheckNet(this)) {
//                new SignUpLoadingTask().execute();
                RegistrationRequest();
            } else {
                Globals.ShowSnackBar(v, getResources().getString(R.string.MsgCheckConnection), RegistrationActivity.this, 1000);
            }
        } else if (v.getId() == R.id.cbSignIn) {
            getSupportFragmentManager().popBackStack();
        } else if (v.getId() == R.id.ivImage) {
            Globals.HideKeyBoard(this, v);
            SelectImageProfile(this, 100, 101);
        } else if (v.getId() == R.id.etDateOfBirth) {
            EditTextOnClick(v);
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
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void EditTextOnClick(View v) {
        Globals.ShowDatePickerDialog(etBirthDate, RegistrationActivity.this, false);
    }

    @Override
    public void MemberResponse(String errorCode, MemberMaster objMemberMaster) {
        progressDialog.dismiss();
        SetError(errorCode, objMemberMaster);
    }

    @Override
    public void MemberUpdate(String errorCode, MemberMaster objMemberMaster) {

    }

    public void SelectImage(int requestCode, Intent data) {
        if (requestCode == 100) {
//            strImageName = "CameraImage_" + simpleDateFormat.format(new Date()) + imageName.substring(imageName.lastIndexOf("."), imageName.length()) + ".jpg";
//            File file = new File(android.os.Environment.getExternalStorageDirectory(), strImageName);
//            picturePath = file.getAbsolutePath();
//            imageName = "Member_" + Globals.memberMasterId + "." + MimeTypeMap.getFileExtensionFromUrl(picturePath);
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            long millis = System.currentTimeMillis();
            imageName = String.valueOf(millis) + ".jpg";
            ivImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ivImage.setImageBitmap(bitmap);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 110, bos);
            byte[] byteData = bos.toByteArray();
            imagePhysicalNameBytes = Base64.encodeToString(byteData, Base64.DEFAULT);
//            UpdateUserProfileImageRequest();
            return;
        } else if (requestCode == 101 && data != null && data.getData() != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            String extension = MimeTypeMap.getFileExtensionFromUrl(picturePath);
            long millis = System.currentTimeMillis();
//            if (extension != null) {
//                imageName = String.valueOf(millis) + "." + MimeTypeMap.getFileExtensionFromUrl(picturePath);
//            } else {
            imageName = String.valueOf(millis) + ".jpg";
//            }
            cursor.close();
        }
        if (!picturePath.equals("")) {
            try {
                File file = new File(picturePath);
                Bitmap bitmap = decodeFile(file);
                ivImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                ivImage.setImageBitmap(bitmap);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                byte[] byteData = bos.toByteArray();
                imagePhysicalNameBytes = Base64.encodeToString(byteData, Base64.DEFAULT);
                return;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
    }

    //region Private Methods and Interface

    private void RegistrationRequest() {
//        progressDialog = new ProgressDialog();
//        progressDialog.show(RegistrationActivity.this.getSupportFragmentManager(), "");

        try {
//            MemberJSONParser objMemberJSONParser = new MemberJSONParser();
            MemberMasterNew objMemberMaster = new MemberMasterNew();
            objMemberMaster.setMemberName(etFirstName.getText().toString().trim() + " " + etLastName.getText().toString().trim());
            objMemberMaster.setEmail(etEmail.getText().toString().trim());
            objMemberMaster.setPassword(etPassword.getText().toString().trim());
            objMemberMaster.setPhone1(etPhone.getText().toString().substring(4));
            if (!etPhone1.getText().toString().isEmpty()) {
                objMemberMaster.setPhone2(etPhone1.getText().toString().substring(4));
            }
            if (imageName != null && !imageName.equals("")) {
//                strImageName = imageName.substring(0, imageName.lastIndexOf(".")) + "_" + simpleDateFormat.format(new Date()) + imageName.substring(imageName.lastIndexOf("."), imageName.length());
                strImageName = imageName;
                objMemberMaster.setImageName(strImageName);
//                objMemberMaster.setImageNameBytes(imagePhysicalNameBytes);
            }
            if (rbMale.isChecked()) {
                objMemberMaster.setGender(rbMale.getText().toString());
            }
            if (rbFemale.isChecked()) {
                objMemberMaster.setGender(rbFemale.getText().toString());
            }
            if (!etBirthDate.getText().toString().isEmpty()) {
                objMemberMaster.setBirthDate(etBirthDate.getText().toString());
            }
            objMemberMaster.setMemberType(Globals.MemberType.User.getMemberType());
//            if (imageName != null && !imageName.equals("")) {
//                strImageName = imageName.substring(0, imageName.lastIndexOf(".")) + "_" + simpleDateFormat.format(new Date()) + imageName.substring(imageName.lastIndexOf("."), imageName.length());
//                objMemberMaster.setImageName(strImageName);
//                objMemberMaster.setImageNamePhysicalNameBytes(imagePhysicalNameBytes);
//            }
//            if (SignInActivity.token != null) {
//                Log.e("Registrartion", " encoded token:" + SignInActivity.token.replace(":", "2E2").replace("-", "3E3").replace("_", "4E4"));
            objMemberMaster.setFCMToken(SignInActivity.token);
//            }
//            objMemberJSONParser.InsertMemberMaster(RegistrationActivity.this, RegistrationFragment.this, objMemberMaster);

//            RegistrationDetailFragment objRegistrationDetailFragment = new RegistrationDetailFragment();
//            Bundle bundle = new Bundle();
//            bundle.putParcelable("MemberMaster", objMemberMaster);
//            objRegistrationDetailFragment.setArguments(bundle);
//            FragmentTransaction fragmentTransaction = RegistrationActivity.this.getSupportFragmentManager().beginTransaction();
//            fragmentTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out, 0, R.anim.right_exit);
//            fragmentTransaction.replace(R.id.signUpFragment, objRegistrationDetailFragment, "PersonalDetail");
//            fragmentTransaction.addToBackStack("PersonalDetail");
//            fragmentTransaction.commit();
//            RegistrationFragment.onNext objOnNext =(RegistrationFragment.onNext) ((SignInActivity)getActivity());
//            objOnNext.OnNext(objMemberMaster);

            Intent intent = new Intent(RegistrationActivity.this, RegistrationDetailActivity.class);
            intent.putExtra("MemberMaster", objMemberMaster);
            startActivity(intent);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean ValidateControls() {
        boolean IsValid = true;

//        if (etFirstName.getText().toString().equals("")
//                && !etEmail.getText().toString().equals("")
//                && !etPassword.getText().toString().equals("")) {
//            if (Globals.IsValidEmail(etEmail.getText().toString())) {
//                etEmail.clearError();
//            } else {
//                etEmail.setError("Enter " + getResources().getString(R.string.suValidEmail));
//            }
//            etFirstName.setError("Enter " + getResources().getString(R.string.suFirstName));
//            etPassword.clearError();
//            IsValid = false;
//
//        } else if (etEmail.getText().toString().equals("")
//                && !etFirstName.getText().toString().equals("")
//                && !etPassword.getText().toString().equals("")) {
//            etEmail.setError("Enter " + getResources().getString(R.string.suEmail));
//            etPassword.clearError();
//            etFirstName.clearError();
//            IsValid = false;
//        } else if (etPassword.getText().toString().equals("")
//                && !etFirstName.getText().toString().equals("")
//                && !etEmail.getText().toString().equals("")) {
//            if (Globals.IsValidEmail(etEmail.getText().toString())) {
//                etEmail.clearError();
//            } else {
//                etEmail.setError("Enter " + getResources().getString(R.string.suValidEmail));
//            }
//            etPassword.setError("Enter " + getResources().getString(R.string.suPassword));
//            etFirstName.clearError();
//            IsValid = false;
//        } else if (etFirstName.getText().toString().equals("")
//                && etEmail.getText().toString().equals("")
//                && !etPassword.getText().toString().equals("")) {
//            etPassword.clearError();
//            etFirstName.setError("Enter " + getResources().getString(R.string.suFirstName));
//            etEmail.setError("Enter " + getResources().getString(R.string.suEmail));
//            IsValid = false;
//        } else if (etFirstName.getText().toString().equals("")
//                && !etEmail.getText().toString().equals("")
//                && etPassword.getText().toString().equals("")) {
//            if (Globals.IsValidEmail(etEmail.getText().toString())) {
//                etEmail.clearError();
//            } else {
//                etEmail.setError("Enter " + getResources().getString(R.string.suValidEmail));
//            }
//            etFirstName.setError("Enter " + getResources().getString(R.string.suFirstName));
//            etPassword.setError("Enter " + getResources().getString(R.string.suPassword));
//            IsValid = false;
//        } else if (etEmail.getText().toString().equals("")
//                && !etFirstName.getText().toString().equals("")
//                && etPassword.getText().toString().equals("")) {
//            etPassword.setError("Enter " + getResources().getString(R.string.suPassword));
//            etEmail.setError("Enter " + getResources().getString(R.string.suEmail));
//            etFirstName.clearError();
//            IsValid = false;
//        } else if (etFirstName.getText().toString().equals("")
//                && etEmail.getText().toString().equals("")
//                && etPassword.getText().toString().equals("")) {
//            etFirstName.setError("Enter " + getResources().getString(R.string.suFirstName));
//            etEmail.setError("Enter " + getResources().getString(R.string.suEmail));
//            etPassword.setError("Enter " + getResources().getString(R.string.suPassword));
//            IsValid = false;
//        } else if (!etFirstName.getText().toString().equals("") && !etEmail.getText().toString().equals("") && !etPassword.getText().toString().equals("")) {
//            if (Globals.IsValidEmail(etEmail.getText().toString())) {
//                etEmail.clearError();
//            } else {
//                etEmail.setError("Enter " + getResources().getString(R.string.suValidEmail));
//                IsValid = false;
//            }
//            etFirstName.clearError();
//            etPassword.clearError();
//        }
        if (etPhone.getText().toString().substring(4).equals("") || etPhone.getText().toString().substring(4).length() != 10) {
            etPhone.setError("Enter 10 digit Mobile No");
            IsValid = false;
        } else {
            etPhone.clearError();
        }
        if (!etPhone1.getText().toString().substring(4).equals("") && etPhone1.getText().toString().substring(4).length() != 10) {
            etPhone1.setError("Enter 10 digit Mobile No");
            IsValid = false;
        } else {
            etPhone1.clearError();
        }

        if (etFirstName.getText().toString().trim().equals("")) {
            etFirstName.setError("Enter First Name");
            IsValid = false;
        } else {
            etFirstName.clearError();
        }
        if (etBirthDate.getText().toString().trim().equals("")) {
            etBirthDate.setError("Select Birthdate");
            IsValid = false;
        } else {
            etBirthDate.clearError();
        }
        if (etLastName.getText().toString().trim().equals("")) {
            etLastName.setError("Enter Last Name");
            IsValid = false;
        } else {
            etLastName.clearError();
        }
        if (etEmail.getText().toString().trim().equals("")) {
            etEmail.setError("Enter Email");
            IsValid = false;
        } else {
            if (Globals.IsValidEmail(etEmail.getText().toString())) {
                etEmail.clearError();
            } else {
                IsValid = false;
                etEmail.setError("Enter Valid Email");
            }
        }
        if (etPassword.getText().toString().trim().equals("")) {
            etPassword.setError("Enter Password");
            IsValid = false;
        } else {
            etPassword.clearError();
        }
        if (etConfirmPassword.getText().toString().trim().equals("")) {
            etConfirmPassword.setError("Enter Confirm password");
            IsValid = false;
        } else {
            etConfirmPassword.clearError();
        }
        if (!etConfirmPassword.getText().toString().trim().equals("")) {
            if (!etPassword.getText().toString().equals(etConfirmPassword.getText().toString())) {
                etConfirmPassword.setError("Password mismatch");
                IsValid = false;
            } else {
                etConfirmPassword.clearError();
            }
        }

        return IsValid;
    }

    private void ClearControls() {
        etFirstName.setText("");
        etLastName.setText("");
        etPassword.setText("");
        etEmail.setText("");
        etPhone.setText("");
        etPhone1.setText("");
    }

    private void SetError(String errorCode, MemberMaster objMemberMaster) {
        switch (errorCode) {
            case "-1":
                Globals.ShowSnackBar(signUpActivity, getResources().getString(R.string.MsgServerNotResponding), RegistrationActivity.this, 1000);
                break;
//            case "-2":
//                if (isIntegrationLogin) {
//                    if (objMemberMaster != null) {
//                        if (isIntegrationLogin) {
//                            if (objMemberMaster.getGooglePlusUserId() != null && !objMemberMaster.getGooglePlusUserId().equals("")) {
//                                objSharePreferenceManage.CreatePreference("LoginPreference", "IntegrationId", objMemberMaster.getGooglePlusUserId(), this);
//                            } else if (objMemberMaster.getFacebookUserId() != null && !objMemberMaster.getFacebookUserId().equals("")) {
//                                objSharePreferenceManage.CreatePreference("LoginPreference", "IntegrationId", objMemberMaster.getFacebookUserId(), this);
//                            }
//                        }
//                        if (isLoginWithFb) {
//                            objSharePreferenceManage.CreatePreference("LoginPreference", "isLoginWithFb", "true", this);
//                        }
//                        objSharePreferenceManage.CreatePreference("LoginPreference", "CustomerMasterId", String.valueOf(objMemberMaster.getCustomerMasterId()), this);
//                        objSharePreferenceManage.CreatePreference("LoginPreference", "UserName", objMemberMaster.getEmail1(), this);
//                        objSharePreferenceManage.CreatePreference("LoginPreference", "UserPassword", objMemberMaster.getPassword(), this);
//                        objSharePreferenceManage.CreatePreference("LoginPreference", "CustomerName", objMemberMaster.getCustomerName(), this);
//                        if (objMemberMaster.getXs_ImagePhysicalName() != null && !objMemberMaster.getXs_ImagePhysicalName().equals("")) {
//                            objSharePreferenceManage.CreatePreference("LoginPreference", "CustomerProfileUrl", objMemberMaster.getXs_ImagePhysicalName(), this);
//                        }
//                        if (objMemberMaster.getPhone1() != null && !objMemberMaster.getPhone1().equals("")) {
//                            objSharePreferenceManage.CreatePreference("LoginPreference", "Phone", objMemberMaster.getPhone1(), this);
//                        }
//                        objSharePreferenceManage.CreatePreference("LoginPreference", "BusinessMasterId", String.valueOf(objMemberMaster.getlinktoBusinessMasterId()), this);
//                    }
//                    ClearControls();
//                    if (getIntent().getStringExtra("Booking") != null && getIntent().getStringExtra("Booking").equals("Booking")) {
//                        Intent returnIntent = new Intent();
//                        returnIntent.putExtra("IsRedirect", true);
//                        returnIntent.putExtra("TargetActivity", "Booking");
//                        setResult(Activity.RESULT_OK, returnIntent);
//                        finish();
//                    } else if (getIntent().getStringExtra("Order") != null && getIntent().getStringExtra("Order").equals("Order")) {
//                        Intent returnIntent = new Intent();
//                        returnIntent.putExtra("IsRedirect", true);
//                        returnIntent.putExtra("TargetActivity", "Order");
//                        setResult(Activity.RESULT_OK, returnIntent);
//                        finish();
//                    } else {
//                        Intent returnIntent = new Intent();
//                        returnIntent.putExtra("IsLogin", true);
//                        returnIntent.putExtra("IsShowMessage", true);
//                        setResult(Activity.RESULT_OK, returnIntent);
//                        finish();
//                    }
//                } else {
//                    Globals.ShowSnackBar(view, getResources().getString(R.string.MsgAlreadyExist), RegistrationActivity.this, 1000);
//                    ClearControls();
//                }
//                break;
            default:
                if (objMemberMaster != null) {
//                    if (isIntegrationLogin) {
//                        if (objMemberMaster.getGooglePlusUserId() != null && !objMemberMaster.getGooglePlusUserId().equals("")) {
//                            objSharePreferenceManage.CreatePreference("LoginPreference", "IntegrationId", objMemberMaster.getGooglePlusUserId(), this);
//                        } else if (objMemberMaster.getFacebookUserId() != null && !objMemberMaster.getFacebookUserId().equals("")) {
//                            objSharePreferenceManage.CreatePreference("LoginPreference", "IntegrationId", objMemberMaster.getFacebookUserId(), this);
//                        }
//                    }
//                    if (isLoginWithFb) {
//                        objSharePreferenceManage.CreatePreference("LoginPreference", "isLoginWithFb", "true", this);
//                    }

//                    objSharePreferenceManage = new SharePreferenceManage();
//                    objSharePreferenceManage.CreatePreference("LoginPreference", "MemberMasterId", String.valueOf(objMemberMaster.getMemberMasterId()), RegistrationActivity.this);
//                    objSharePreferenceManage.CreatePreference("LoginPreference", "MemberEmail", objMemberMaster.getEmail(), RegistrationActivity.this);
//                    objSharePreferenceManage.CreatePreference("LoginPreference", "MemberPassword", objMemberMaster.getPassword(), RegistrationActivity.this);
//                    objSharePreferenceManage.CreatePreference("LoginPreference", "MemberName", objMemberMaster.getMemberName(), RegistrationActivity.this);
//                    objSharePreferenceManage.CreatePreference("LoginPreference", "MemberType", objMemberMaster.getMemberType(), RegistrationActivity.this);
//                    objSharePreferenceManage.CreatePreference("LoginPreference", "Gender", objMemberMaster.getGender(), RegistrationActivity.this);
//                    objSharePreferenceManage.CreatePreference("LoginPreference", "startPage", "1", RegistrationActivity.this);
//                    if (objMemberMaster.getPhone1() != null && !objMemberMaster.getPhone1().equals("")) {
//                        objSharePreferenceManage.CreatePreference("LoginPreference", "Phone", objMemberMaster.getPhone1(), RegistrationActivity.this);
//                    }
                    Globals.memberMasterId = objMemberMaster.getMemberMasterId();
                    Globals.memberType = objMemberMaster.getMemberType();
//                    Globals.startPage = 1;
                    ClearControls();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out, 0, R.anim.right_exit);
                    fragmentTransaction.replace(android.R.id.content, new RegistrationDetailFragment(), "PersonalDetail");
                    fragmentTransaction.addToBackStack("PersonalDetail");
                    fragmentTransaction.commit();
                }
//                if (getIntent().getStringExtra("Booking") != null && getIntent().getStringExtra("Booking").equals("Booking")) {
//                    Intent returnIntent = new Intent();
//                    returnIntent.putExtra("IsRedirect", true);
//                    returnIntent.putExtra("TargetActivity", "Booking");
//                    setResult(Activity.RESULT_OK, returnIntent);
//                    finish();
//                } else if (getIntent().getStringExtra("Order") != null && getIntent().getStringExtra("Order").equals("Order")) {
//                    Intent returnIntent = new Intent();
//                    returnIntent.putExtra("IsRedirect", true);
//                    returnIntent.putExtra("TargetActivity", "Order");
//                    setResult(Activity.RESULT_OK, returnIntent);
//                    finish();
//                } else {
//                    Intent returnIntent = new Intent();
//                    returnIntent.putExtra("IsLogin", true);
//                    returnIntent.putExtra("IsShowMessage", true);
//                    setResult(Activity.RESULT_OK, returnIntent);
//                    finish();
//                }
//                Globals.ChangeActivity(getActivity(), HomeActivity.class, true);

                break;
        }
    }

    private void FCMTokenGenerate() {

        //Checking play service is available or not
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(RegistrationActivity.this.getApplicationContext());

        //if play service is not available
        if (ConnectionResult.SUCCESS != resultCode) {
            //If play service is supported but not installed
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                //Displaying message that play service is not installed
                Toast.makeText(getApplicationContext(), "Google Play Service is not install/enabled in this device!", Toast.LENGTH_LONG).show();
                GooglePlayServicesUtil.showErrorNotification(resultCode, getApplicationContext());

                //If play service is not supported
                //Displaying an error message
            } else {
                Toast.makeText(getApplicationContext(), "This device does not support for Google Play Service!", Toast.LENGTH_LONG).show();
            }

            //If play service is available
        } else {
            FirebaseMessaging.getInstance().subscribeToTopic("news");

            SignInActivity.token = FirebaseInstanceId.getInstance().getToken();
        }

    }

    private Bitmap decodeFile(File f) {
        try {
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);

            // The new size we want to scale to
            final int REQUIRED_SIZE = 250;

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
                    startActivityForResult(intent, requestCodeCamera);
                } else if (items[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(Intent.createChooser(intent, "Select File"), requestCodeGallery);
                } else if (items[item].equals("Remove Image")) {
                    ivImage.setImageResource(R.drawable.no_image);
                    imageName = null;
                    imagePhysicalNameBytes = null;
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    //endregion


}
