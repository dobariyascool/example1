package com.arraybit.mym;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.arraybit.global.Globals;
import com.arraybit.global.MarshMallowPermission;
import com.arraybit.global.Service;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.modal.MemberMaster;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class RegistrationFragment extends Fragment implements View.OnClickListener, MemberJSONParser.MemberRequestListener {

    final int PIC_CROP = 1;
    EditText etFirstName, etLastName, etEmail, etPassword, etConfirmPassword, etPhone, etBirthDate;
    RadioGroup rgMain;
    RadioButton rbMale, rbFemale;
    Button btnSignUp;
    ImageView ivImage;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss.SSS", Locale.US);
    SharePreferenceManage objSharePreferenceManage;
    ProgressDialog progressDialog;
    View view;
    String imagePhysicalNameBytes, imageName, strImageName, picturePath = "";
    MarshMallowPermission marshMallowPermission;

    public RegistrationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setHasOptionsMenu(true);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_registration, container, false);
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
            app_bar.setTitle(getResources().getString(R.string.title_fragment_signup));
            //end
            Globals.SetToolBarBackground(getActivity(), app_bar, ContextCompat.getColor(getActivity(), R.color.colorPrimary), ContextCompat.getColor(getActivity(), android.R.color.white));

            marshMallowPermission = new MarshMallowPermission(getActivity());
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!marshMallowPermission.checkPermissionForCamera()) {
                    marshMallowPermission.requestPermissionForCamera();
                }
                if (!marshMallowPermission.checkPermissionForExternalStorage()) {
                    marshMallowPermission.requestPermissionForExternalStorage();
                }
            }

            if (Service.CheckNet(getActivity())) {
                FCMTokenGenerate();
            }

            //EditText
            etFirstName = (EditText) view.findViewById(R.id.etFirstName);
            etLastName = (EditText) view.findViewById(R.id.etLastName);
            etPassword = (EditText) view.findViewById(R.id.etPassword);
            etConfirmPassword = (EditText) view.findViewById(R.id.etConfirmPassword);
            etEmail = (EditText) view.findViewById(R.id.etEmail);
            etPhone = (EditText) view.findViewById(R.id.etPhone);
            etBirthDate = (EditText) view.findViewById(R.id.etDateOfBirth);
            etBirthDate.setInputType(InputType.TYPE_NULL);
            ivImage = (ImageView) view.findViewById(R.id.ivImage);

            //end

            //Radiogroup
            rgMain = (RadioGroup) view.findViewById(R.id.rgMain);
            //

            //RadioButton
            rbMale = (RadioButton) view.findViewById(R.id.rbMale);
            rbFemale = (RadioButton) view.findViewById(R.id.rbFemale);
            //end

            //button
            btnSignUp = (Button) view.findViewById(R.id.btnSignUp);
            //end

            //Spinner
            //

            //compound button
            CompoundButton cbSignIn = (CompoundButton) view.findViewById(R.id.cbSignIn);
            //end

            //event
            cbSignIn.setOnClickListener(this);
            btnSignUp.setOnClickListener(this);
            ivImage.setOnClickListener(this);
            etBirthDate.setOnClickListener(this);
            //end

//        Globals.CustomView(btnSignUp, ContextCompat.getColor(getActivity(),R.color.accent), ContextCompat.getColor(getActivity(),android.R.color.white));

            setHasOptionsMenu(true);

            etBirthDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        Globals.ShowDatePickerDialog(etBirthDate, getActivity(), false);
                    }
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    @Override
    public void onClick(View v) {
        view = v;
        Globals.HideKeyBoard(getActivity(), v);
        if (v.getId() == R.id.btnSignUp) {
            if (!ValidateControls()) {
                Globals.ShowSnackBar(v, getResources().getString(R.string.MsgValidation), getActivity(), 1000);
                return;
            }
            if (Service.CheckNet(getActivity())) {
//                new SignUpLoadingTask().execute();
                RegistrationRequest();
            } else {
                Globals.ShowSnackBar(v, getResources().getString(R.string.MsgCheckConnection), getActivity(), 1000);
            }
        } else if (v.getId() == R.id.cbSignIn) {
            getActivity().getSupportFragmentManager().popBackStack();
        } else if (v.getId() == R.id.ivImage) {
            Globals.HideKeyBoard(getActivity(), v);
            Globals.SelectImage(getActivity(), 100, 101);
        } else if (v.getId() == R.id.etDateOfBirth) {
            EditTextOnClick(v);
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            getActivity().getSupportFragmentManager().popBackStack();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void EditTextOnClick(View v) {
        Globals.ShowDatePickerDialog(etBirthDate, getActivity(), false);
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
            imageName = "Member_" + Globals.memberMasterId + ".jpg";
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

            Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            imageName = "Member_" + Globals.memberMasterId + "." + MimeTypeMap.getFileExtensionFromUrl(picturePath);
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
//        progressDialog.show(getActivity().getSupportFragmentManager(), "");

        try {

            MemberJSONParser objMemberJSONParser = new MemberJSONParser();
            MemberMaster objMemberMaster = new MemberMaster();
            objMemberMaster.setMemberName(etFirstName.getText().toString().trim() + " " + etLastName.getText().toString().trim());
            objMemberMaster.setEmail(etEmail.getText().toString().trim());
            objMemberMaster.setPassword(etPassword.getText().toString().trim());
            objMemberMaster.setPhone1(etPhone.getText().toString().trim());
            if (imageName != null && !imageName.equals("")) {
//                strImageName = imageName.substring(0, imageName.lastIndexOf(".")) + "_" + simpleDateFormat.format(new Date()) + imageName.substring(imageName.lastIndexOf("."), imageName.length());
                strImageName = imageName;
                objMemberMaster.setImageName(strImageName);
                objMemberMaster.setImageNameBytes(imagePhysicalNameBytes);
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
//            objMemberJSONParser.InsertMemberMaster(getActivity(), RegistrationFragment.this, objMemberMaster);

//            RegistrationDetailFragment objRegistrationDetailFragment = new RegistrationDetailFragment();
//            Bundle bundle = new Bundle();
//            bundle.putParcelable("MemberMaster", objMemberMaster);
//            objRegistrationDetailFragment.setArguments(bundle);
//            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
//            fragmentTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out, 0, R.anim.right_exit);
//            fragmentTransaction.replace(R.id.signUpFragment, objRegistrationDetailFragment, "PersonalDetail");
//            fragmentTransaction.addToBackStack("PersonalDetail");
//            fragmentTransaction.commit();
            onNext objOnNext =(onNext) ((SignInActivity)getActivity());
            objOnNext.OnNext(objMemberMaster);

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
        if (etPhone.getText().toString().equals("") && etPhone.getText().length() != 10) {
            etPhone.setError("Enter Mobile No");
            IsValid = false;
        } else {
            etPhone.clearError();
        }

        if (etFirstName.getText().toString().trim().equals("")) {
            etFirstName.setError("Enter First Name");
            IsValid = false;
        } else {
            etFirstName.clearError();
        } if (etBirthDate.getText().toString().trim().equals("")) {
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
            etEmail.clearError();
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
        if(!etConfirmPassword.getText().toString().trim().equals("")) {
            if (!etPassword.getText().toString().equals(etConfirmPassword.getText().toString())) {
                etConfirmPassword.setError("Mismatch the password");
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
    }

    private void SetError(String errorCode, MemberMaster objMemberMaster) {
        switch (errorCode) {
            case "-1":
                Globals.ShowSnackBar(view, getResources().getString(R.string.MsgServerNotResponding), getActivity(), 1000);
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
//                    objSharePreferenceManage.CreatePreference("LoginPreference", "MemberMasterId", String.valueOf(objMemberMaster.getMemberMasterId()), getActivity());
//                    objSharePreferenceManage.CreatePreference("LoginPreference", "MemberEmail", objMemberMaster.getEmail(), getActivity());
//                    objSharePreferenceManage.CreatePreference("LoginPreference", "MemberPassword", objMemberMaster.getPassword(), getActivity());
//                    objSharePreferenceManage.CreatePreference("LoginPreference", "MemberName", objMemberMaster.getMemberName(), getActivity());
//                    objSharePreferenceManage.CreatePreference("LoginPreference", "MemberType", objMemberMaster.getMemberType(), getActivity());
//                    objSharePreferenceManage.CreatePreference("LoginPreference", "Gender", objMemberMaster.getGender(), getActivity());
//                    objSharePreferenceManage.CreatePreference("LoginPreference", "startPage", "1", getActivity());
//                    if (objMemberMaster.getPhone1() != null && !objMemberMaster.getPhone1().equals("")) {
//                        objSharePreferenceManage.CreatePreference("LoginPreference", "Phone", objMemberMaster.getPhone1(), getActivity());
//                    }
                    Globals.memberMasterId = objMemberMaster.getMemberMasterId();
                    Globals.memberType = objMemberMaster.getMemberType();
//                    Globals.startPage = 1;
                    ClearControls();
                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
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
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity().getApplicationContext());

        //if play service is not available
        if (ConnectionResult.SUCCESS != resultCode) {
            //If play service is supported but not installed
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                //Displaying message that play service is not installed
                Toast.makeText(getActivity().getApplicationContext(), "Google Play Service is not install/enabled in this device!", Toast.LENGTH_LONG).show();
                GooglePlayServicesUtil.showErrorNotification(resultCode, getActivity().getApplicationContext());

                //If play service is not supported
                //Displaying an error message
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "This device does not support for Google Play Service!", Toast.LENGTH_LONG).show();
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

    public interface onNext
    {
        void OnNext(MemberMaster objMemberMaster);
    }
    //endregion
}
