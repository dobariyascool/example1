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
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.arraybit.global.Globals;
import com.arraybit.global.MarshMallowPermission;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.modal.AdvertiseMaster;
import com.arraybit.modal.MemberMaster;
import com.arraybit.parser.AdvertiseJSONParser;
import com.arraybit.parser.MemberJSONParser;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.rey.material.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener, AdvertiseJSONParser.AdvretiseRequestListener,
        MemberJSONParser.MemberRequestListener, UserProfileFragment.UpdateResponseListener, RegistrationDetailFragment.UpdateResponseListener, RegistrationContactFragment.UpdateResponseListener, ConfirmDialog.ConfirmationResponseListener {

    public static boolean isNotification = false;
    public static boolean isDetail = false;
    TextView txtAddress, txtBloodGroup, txtAnniversaryDate, txtSpouseName, txtSpouseDOB;
    TextView txtQualification, txtEmail, txtMobile, txtMobile1, txtDOB, txtDesignation;
    TextView txtOfficeAddress, txtHomeNo, txtOfficeNo, txtAdvertise;
    TextView txtChildGender1, txtChildGender2, txtChildGender3, txtChildGender4, txtChildGender5;
    TextView txtChildName1, txtChildDOB1, txtChildName2, txtChildDOB2, txtChildName3, txtChildDOB3, txtChildName4, txtChildDOB4, txtChildName5, txtChildDOB5;
    LinearLayout llDetail, llAnniversaryDate, llSpouseName, llSpouseDOB, llOfficeAddress, llOfficeNo, llHomeNo, llAddress;
    LinearLayout llPersonal, llPersonalDetail, llContact, llContactdetail;
    LinearLayout llMobile, llMobile1, llEmail, llDOB, llDesignation, llQualification, llBloodGroup;
    LinearLayout llChildDetail1, llChildDetail2, llChildDetail3, llChildDetail4, llChildDetail5;
    ImageView ivProfile, ivProfile1, ivAdvertise, ivAddress1, ivCall, ivCall1, ivMessage, ivMessage1, ivEmail, ivAddress, ibVisible, ivContact;
    ImageView ivSpouse, ivChild1, ivChild2, ivChild3, ivChild4, ivChild5;
    SharePreferenceManage objSharePreferenceManage = new SharePreferenceManage();
    Toolbar app_bar;
    boolean isAdvertise = false, isPersonal = true, isContact = true, isSave = false;
    FloatingActionButton fabEdit;
    String name, imagePhysicalNameBytes, imageName, strImageName;
    MemberMaster objMemberMaster;
    int CurrentPageAdvertise = 1, memberMasterId, position = -1;
    Timer timer = new Timer();
    boolean isStart = false, isApproved;
    ProgressDialog progressDialog = new ProgressDialog();
    AdvertiseMaster objAdvertiseMaster;
    AdvertiseJSONParser objAdvertiseJSONParser = new AdvertiseJSONParser();
    MarshMallowPermission marshMallowPermission = new MarshMallowPermission(DetailActivity.this);
    private Uri fileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        }
        setContentView(R.layout.activity_detail);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);
        app_bar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(app_bar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("My Profile");
        }

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!marshMallowPermission.checkPermissionForCamera()) {
                marshMallowPermission.requestPermissionForCamera();
            }
            if (!marshMallowPermission.checkPermissionForExternalStorage()) {
                marshMallowPermission.requestPermissionForExternalStorage();
            }
        }

        Intent intent = getIntent();
        isStart = intent.getBooleanExtra("isStart", false);
        memberMasterId = intent.getIntExtra("memberMasterId", 0);
        isDetail = intent.getBooleanExtra("isDetail", false);
        isNotification = intent.getBooleanExtra("isNotification", false);
        position = intent.getIntExtra("position", -1);

        if (intent.getStringExtra("memberName") != null) {
            getSupportActionBar().setTitle(intent.getStringExtra("memberName"));
        }

        llDetail = (LinearLayout) findViewById(R.id.llDetail);
        ivAdvertise = (ImageView) findViewById(R.id.ivAdvertise);
        ivAddress = (ImageView) findViewById(R.id.ivAddress);
        ivAddress1 = (ImageView) findViewById(R.id.ivAddress1);
        ivCall = (ImageView) findViewById(R.id.ivCall);
        ivCall1 = (ImageView) findViewById(R.id.ivCall1);
        ivMessage = (ImageView) findViewById(R.id.ivMessage);
        ivMessage1 = (ImageView) findViewById(R.id.ivMessage1);
        ivEmail = (ImageView) findViewById(R.id.ivEmail);
        ivProfile = (ImageView) findViewById(R.id.ivProfile);
        ivProfile1 = (ImageView) findViewById(R.id.ivProfile1);
        ibVisible = (ImageView) findViewById(R.id.ibVisible);
        ivContact = (ImageView) findViewById(R.id.ivContact);

        ivSpouse = (ImageView) findViewById(R.id.ivSpouse);
        ivChild1 = (ImageView) findViewById(R.id.ivChild1);
        ivChild2 = (ImageView) findViewById(R.id.ivChild2);
        ivChild3 = (ImageView) findViewById(R.id.ivChild3);
        ivChild4 = (ImageView) findViewById(R.id.ivChild4);
        ivChild5 = (ImageView) findViewById(R.id.ivChild5);

        txtAddress = (TextView) findViewById(R.id.txtAddress);
        txtMobile = (TextView) findViewById(R.id.txtMobile);
        txtMobile1 = (TextView) findViewById(R.id.txtMobile1);
        txtEmail = (TextView) findViewById(R.id.txtEmail);
        txtDOB = (TextView) findViewById(R.id.txtDOB);
        txtDesignation = (TextView) findViewById(R.id.txtDesignation);
        txtQualification = (TextView) findViewById(R.id.txtQualification);
        txtBloodGroup = (TextView) findViewById(R.id.txtBloodGroup);
        txtAnniversaryDate = (TextView) findViewById(R.id.txtAnniversaryDate);
        txtSpouseName = (TextView) findViewById(R.id.txtSpouseName);
        txtSpouseDOB = (TextView) findViewById(R.id.txtSpouseDOB);
        txtOfficeAddress = (TextView) findViewById(R.id.txtOfficeAddress);
        txtHomeNo = (TextView) findViewById(R.id.txtHomeNo);
        txtOfficeNo = (TextView) findViewById(R.id.txtOfficeNo);
        txtAdvertise = (TextView) findViewById(R.id.txtAdvertise);

        txtChildName1 = (TextView) findViewById(R.id.txtChildName1);
        txtChildDOB1 = (TextView) findViewById(R.id.txtChildDOB1);
        txtChildName2 = (TextView) findViewById(R.id.txtChildName2);
        txtChildDOB2 = (TextView) findViewById(R.id.txtChildDOB2);
        txtChildName3 = (TextView) findViewById(R.id.txtChildName3);
        txtChildDOB3 = (TextView) findViewById(R.id.txtChildDOB3);
        txtChildName4 = (TextView) findViewById(R.id.txtChildName4);
        txtChildDOB4 = (TextView) findViewById(R.id.txtChildDOB4);
        txtChildName5 = (TextView) findViewById(R.id.txtChildName5);
        txtChildDOB5 = (TextView) findViewById(R.id.txtChildDOB5);
        txtChildGender1 = (TextView) findViewById(R.id.txtChildGender1);
        txtChildGender2 = (TextView) findViewById(R.id.txtChildGender2);
        txtChildGender3 = (TextView) findViewById(R.id.txtChildGender3);
        txtChildGender4 = (TextView) findViewById(R.id.txtChildGender4);
        txtChildGender5 = (TextView) findViewById(R.id.txtChildGender5);


        llAnniversaryDate = (LinearLayout) findViewById(R.id.llAnniversaryDate);
        llSpouseName = (LinearLayout) findViewById(R.id.llSpouseName);
        llSpouseDOB = (LinearLayout) findViewById(R.id.llSpouseDOB);
        llAddress = (LinearLayout) findViewById(R.id.llAddress);
        llOfficeAddress = (LinearLayout) findViewById(R.id.llOfficeAddress);
        llOfficeNo = (LinearLayout) findViewById(R.id.llOfficeNo);
        llHomeNo = (LinearLayout) findViewById(R.id.llHomeNo);
        llMobile = (LinearLayout) findViewById(R.id.llMobile);
        llMobile1 = (LinearLayout) findViewById(R.id.llMobile1);
        llEmail = (LinearLayout) findViewById(R.id.llEmail);
        llDOB = (LinearLayout) findViewById(R.id.llDOB);
        llDesignation = (LinearLayout) findViewById(R.id.llDesignation);
        llQualification = (LinearLayout) findViewById(R.id.llQualification);
        llBloodGroup = (LinearLayout) findViewById(R.id.llBloodGroup);

        llPersonal = (LinearLayout) findViewById(R.id.llPersonal);
        llContact = (LinearLayout) findViewById(R.id.llContact);
        llPersonalDetail = (LinearLayout) findViewById(R.id.llPersonalDetail);
        llContactdetail = (LinearLayout) findViewById(R.id.llContactDetail);
        llChildDetail1 = (LinearLayout) findViewById(R.id.llChildDetail1);
        llChildDetail2 = (LinearLayout) findViewById(R.id.llChildDetail2);
        llChildDetail3 = (LinearLayout) findViewById(R.id.llChildDetail3);
        llChildDetail4 = (LinearLayout) findViewById(R.id.llChildDetail4);
        llChildDetail5 = (LinearLayout) findViewById(R.id.llChildDetail5);

        fabEdit = (FloatingActionButton) findViewById(R.id.fabEdit);
//        ibVisible.setImageResource(R.drawable.expand);
//        ivContact.setImageResource(R.drawable.expand);
//        ibVisible.setImageDrawable(getResources().getDrawable(R.drawable.edit_drawable));

//        sharePreferenceManage = new SharePreferenceManage();
        ivAdvertise.setOnClickListener(this);
        txtAdvertise.setOnClickListener(this);
        ivCall.setOnClickListener(this);
        ivCall1.setOnClickListener(this);
        ivAddress.setOnClickListener(this);
        ivAddress1.setOnClickListener(this);
        ivMessage.setOnClickListener(this);
        ivMessage1.setOnClickListener(this);
        ivEmail.setOnClickListener(this);
        ibVisible.setOnClickListener(this);
        fabEdit.setOnClickListener(this);
        ivContact.setOnClickListener(this);
        llContact.setOnClickListener(this);
        ivProfile.setOnClickListener(this);
        llPersonal.setOnClickListener(this);
//        if (objMemberMaster != null) {
//            if (isDetail || isNotification) {
//                fabEdit.setVisibility(View.GONE);
//                name = objMemberMaster.getMemberName();
//                if (name != null && !name.equals("")) {
//                    getSupportActionBar().setTitle(name);
//                }
//                SetAdvertise();
//            } else if (!isDetail && !isNotification) {
//                getSupportActionBar().setTitle("My Profile");
//                fabEdit.setVisibility(View.VISIBLE);
//                ivAdvertise.setVisibility(View.GONE);
//                ivAddress.setVisibility(View.GONE);
//                ivEmail.setVisibility(View.GONE);
//                ivMessage.setVisibility(View.GONE);
//                ivCall.setVisibility(View.GONE);
//                ivProfile.setOnClickListener(this);
////                SetUserName();
//            }
//            SetData();
//        } else {
        RequestMemberByMemberMasterId();
//        }

        if (!isDetail && !isNotification) {
            ibVisible.setImageResource(R.drawable.edit);
            ivContact.setImageResource(R.drawable.edit);
            ivProfile.setClickable(true);
        } else {
            ivProfile.setClickable(false);
        }

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (appBarLayout.getHeight() / 2 < -verticalOffset) {
                    fabEdit.setVisibility(View.GONE);
                } else {
                    if (!isDetail && !isNotification) {
                        fabEdit.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail_activity, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        if (isNotification) {
            menu.findItem(R.id.share).setVisible(false);
            menu.findItem(R.id.changePassword).setVisible(false);
            menu.findItem(R.id.saveContact).setVisible(false);
            menu.findItem(R.id.close).setVisible(false);
            menu.findItem(R.id.accept).setVisible(true);
            menu.findItem(R.id.cancle).setVisible(true);
        } else if (isDetail) {
            menu.findItem(R.id.share).setVisible(true);
            menu.findItem(R.id.changePassword).setVisible(false);
            menu.findItem(R.id.saveContact).setVisible(true);
            menu.findItem(R.id.close).setVisible(false);
            menu.findItem(R.id.accept).setVisible(false);
            menu.findItem(R.id.cancle).setVisible(false);
        } else if (!isNotification && !isDetail) {
            menu.findItem(R.id.share).setVisible(false);
            menu.findItem(R.id.changePassword).setVisible(true);
            menu.findItem(R.id.saveContact).setVisible(false);
            menu.findItem(R.id.close).setVisible(false);
            menu.findItem(R.id.accept).setVisible(false);
            menu.findItem(R.id.cancle).setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                if (isStart && isNotification) {
//                    Intent intent = new Intent(DetailActivity.this, HomeActivity.class);
//                    startActivity(intent);
//                    finish();
//                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                    OnApprvedFromNotification();
                } else {
                    setResult(RESULT_OK);
                    finish();
                    overridePendingTransition(0, R.anim.right_exit);
                }
            }
        } else if (item.getItemId() == R.id.accept) {
            isApproved = true;
            ConfirmDialog confirmDialog = new ConfirmDialog(true, "Approve this member?");
            confirmDialog.show(getSupportFragmentManager(), "");
        } else if (item.getItemId() == R.id.saveContact) {
            isSave = true;
            ConfirmDialog confirmDialog = new ConfirmDialog(true, "Save to contacts?");
            confirmDialog.show(getSupportFragmentManager(), "");
//           Globals.ContactSave(DetailActivity.this, objMemberMaster);
        } else if (item.getItemId() == R.id.cancle) {
            isApproved = false;
            ConfirmDialog confirmDialog = new ConfirmDialog(true, "Decline this member?");
            confirmDialog.show(getSupportFragmentManager(), "");
        } else if (item.getItemId() == R.id.share) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Name: " + objMemberMaster.getMemberName() +
                    "\nMobile: " + objMemberMaster.getPhone1() +
                    "\nEmail: " + objMemberMaster.getEmail());
            sendIntent.setType("text/plain");
            startActivity(Intent.createChooser(sendIntent, "Share Contact"));
        } else if (item.getItemId() == R.id.changePassword) {
            ReplaceFragment(new ChangePasswordFragment(), getResources().getString(R.string.title_fragment_change_password));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == RESULT_OK) {
                if (requestCode == 1) {
                    Toast.makeText(DetailActivity.this, "Contact saved successfully.", Toast.LENGTH_SHORT).show();
                }
                if (getSupportFragmentManager().getBackStackEntryCount() != 0) {
//                    if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
                    if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals("Registration")) {
                        RegistrationFragment registrationFragment = (RegistrationFragment) getSupportFragmentManager().findFragmentByTag("Registration");
                        registrationFragment.SelectImage(requestCode, data);
                    } else if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals("PersonalDetail")) {
                        RegistrationDetailFragment registrationDetailFragment = (RegistrationDetailFragment) getSupportFragmentManager().findFragmentByTag("PersonalDetail");
                        registrationDetailFragment.SelectImage(requestCode, data);
//                    } else if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getResources().getString(R.string.title_fragment_your_profile))) {
//                        UserProfileFragment userProfileFragment = (UserProfileFragment) getSupportFragmentManager().findFragmentByTag(getResources().getString(R.string.title_fragment_your_profile));
//                        userProfileFragment.SelectImage(requestCode, data);
                    }
//                    }
                } else {
                    SelectImage(requestCode, data);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ivAdvertise || v.getId() == R.id.txtAdvertise) {
            if (objAdvertiseMaster != null) {
                AdvertiseWebViewFragment objAdvertiseWebViewFragment = new AdvertiseWebViewFragment();
                Bundle bundle = new Bundle();
                bundle.putString("url", objAdvertiseMaster.getWebsiteURL());
                objAdvertiseWebViewFragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out, 0, R.anim.right_exit);
                fragmentTransaction.replace(android.R.id.content, objAdvertiseWebViewFragment, "Advertisement");
                fragmentTransaction.addToBackStack("Advertisement");
                fragmentTransaction.commit();
            }
        } else if (v.getId() == R.id.ivProfile) {
            SelectImageProfile(DetailActivity.this, 100, 101);
        } else if (v.getId() == R.id.ivCall) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:+91" + objMemberMaster.getPhone1()));
            startActivity(intent);
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
        } else if (v.getId() == R.id.ivCall1) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:+91" + objMemberMaster.getPhone2()));
            startActivity(intent);
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
        } else if (v.getId() == R.id.ivAddress) {
//            String str = txtAddress.getText().toString();
            String str = objMemberMaster.getHomeNearBy() + ", " + objMemberMaster.getHomeArea() + ", " + objMemberMaster.getHomeCity() + ", " + objMemberMaster.getHomeZipCode();
            Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + str);
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
            startActivity(mapIntent);
        } else if (v.getId() == R.id.ivAddress1) {
            String str = objMemberMaster.getOfficeNearBy() + ", " + objMemberMaster.getOfficeArea() + ", " + objMemberMaster.getOfficeCity() + ", " + objMemberMaster.getOfficeZipCode();
            Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + str);
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
            startActivity(mapIntent);
        } else if (v.getId() == R.id.ivMessage) {
            Uri uri = Uri.parse("smsto:+91" + objMemberMaster.getPhone1());
            Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
            startActivity(intent);
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
        } else if (v.getId() == R.id.ivMessage1) {
            Uri uri = Uri.parse("smsto:+91" + objMemberMaster.getPhone2());
            Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
            startActivity(intent);
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
        } else if (v.getId() == R.id.ivEmail) {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_EMAIL, objMemberMaster.getEmail());
            startActivity(Intent.createChooser(intent, "Send Email"));
        } else if (v.getId() == R.id.fabEdit) {
            UserProfileFragment userProfileFragment = new UserProfileFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable("memberMaster", objMemberMaster);
            userProfileFragment.setArguments(bundle);
            ReplaceFragment(userProfileFragment, getResources().getString(R.string.title_fragment_your_profile));
        } else if (v.getId() == R.id.llPersonal) {
            if (isPersonal) {
                isPersonal = false;
                ibVisible.setImageResource(R.drawable.expand);
                llPersonalDetail.setVisibility(View.GONE);
            } else {
                isPersonal = true;
                if (isDetail || isNotification) {
                    ibVisible.setImageResource(R.drawable.collapse);
                } else {
                    ibVisible.setImageResource(R.drawable.edit);
                }
                llPersonalDetail.setVisibility(View.VISIBLE);
            }
        } else if (v.getId() == R.id.ibVisible) {
            if (isPersonal) {
                isPersonal = false;
                if (isDetail || isNotification) {
                    ibVisible.setImageResource(R.drawable.expand);
                    llPersonalDetail.setVisibility(View.GONE);
                } else {
                    RegistrationDetailFragment registrationDetailFragment = new RegistrationDetailFragment();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("MemberMaster", objMemberMaster);
                    bundle.putBoolean("isUpdate", true);
                    registrationDetailFragment.setArguments(bundle);
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out, 0, R.anim.right_exit);
                    fragmentTransaction.replace(android.R.id.content, registrationDetailFragment, "PersonalDetail");
                    fragmentTransaction.addToBackStack("PersonalDetail");
                    fragmentTransaction.commit();
                }
            } else {
                isPersonal = true;
                if (isDetail || isNotification) {
                    ibVisible.setImageResource(R.drawable.collapse);
                } else {
                    ibVisible.setImageResource(R.drawable.edit);
                }
                llPersonalDetail.setVisibility(View.VISIBLE);
            }
        } else if (v.getId() == R.id.llContact) {
            if (isContact) {
                isContact = false;
                ivContact.setImageResource(R.drawable.expand);
                llContactdetail.setVisibility(View.GONE);
            } else {
                isContact = true;
                if (isDetail || isNotification) {
                    ivContact.setImageResource(R.drawable.collapse);
                } else {
                    ivContact.setImageResource(R.drawable.edit);
                }
                llContactdetail.setVisibility(View.VISIBLE);
            }
        } else if (v.getId() == R.id.ivContact) {
            if (isContact) {
                isContact = false;
                if (isDetail || isNotification) {
                    ivContact.setImageResource(R.drawable.expand);
                    llContactdetail.setVisibility(View.GONE);
                } else {
                    RegistrationContactFragment registrationContactFragment = new RegistrationContactFragment();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("MemberMaster", objMemberMaster);
                    bundle.putBoolean("isUpdate", true);
                    registrationContactFragment.setArguments(bundle);
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out, 0, R.anim.right_exit);
                    fragmentTransaction.replace(android.R.id.content, registrationContactFragment, "ContactDetail");
                    fragmentTransaction.addToBackStack("ContactDetail");
                    fragmentTransaction.commit();
                }
            } else {
                isContact = true;
                if (isDetail || isNotification) {
                    ivContact.setImageResource(R.drawable.collapse);
                } else {
                    ivContact.setImageResource(R.drawable.edit);
                }
                llContactdetail.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() != 0) {
            if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                    && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getResources().getString(R.string.title_fragment_your_profile))) {
                RequestMemberByMemberMasterId();
                getSupportFragmentManager().popBackStack(getResources().getString(R.string.title_fragment_your_profile), FragmentManager.POP_BACK_STACK_INCLUSIVE);
            } else if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                    && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getResources().getString(R.string.title_fragment_change_password))) {
                getSupportFragmentManager().popBackStack(getResources().getString(R.string.title_fragment_change_password), FragmentManager.POP_BACK_STACK_INCLUSIVE);
            } else if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                    && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals("ContactDetail")) {
                RequestMemberByMemberMasterId();
                getSupportFragmentManager().popBackStack("ContactDetail", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            } else if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                    && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals("PersonalDetail")) {
                RequestMemberByMemberMasterId();
                getSupportFragmentManager().popBackStack("PersonalDetail", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            } else if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                    && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals("Advertisement")) {
                getSupportFragmentManager().popBackStack("Advertisement", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        } else {
            if (isStart && isNotification) {
//                Intent intent = new Intent(DetailActivity.this, HomeActivity.class);
//                startActivity(intent);
//                finish();
//                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                OnApprvedFromNotification();
            } else {
                setResult(RESULT_OK);
                finish();
                overridePendingTransition(0, R.anim.right_exit);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("pause", " ");

//        if (isDetail || isNotification) {
//            try {
//                isAdvertise = false;
//                timer.wait();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
    }

    @Override
    protected void onStop() {
        super.onStop();
//        if (isDetail || isNotification) {
//            try {
//                isAdvertise = false;
//                timer.wait();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("resume", " ");
//        if (isDetail || isNotification) {
//            if (!isAdvertise) {
//                isAdvertise = true;
//                timer.notify();
//            }
//        }
    }

    @Override
    public void AdvretiseResponse(ArrayList<AdvertiseMaster> alAdvertiseMasters, AdvertiseMaster objAdvertiseMaster) {
        if (alAdvertiseMasters != null && alAdvertiseMasters.size() > 0) {
            this.objAdvertiseMaster = alAdvertiseMasters.get(0);
            CurrentPageAdvertise += 1;
            if (alAdvertiseMasters.get(0).getAdvertiseImageName() != null && !alAdvertiseMasters.get(0).getAdvertiseImageName().equals("")) {
                ivAdvertise.setVisibility(View.VISIBLE);
                txtAdvertise.setVisibility(View.GONE);
                Glide.with(DetailActivity.this).load(alAdvertiseMasters.get(0).getAdvertiseImageName()).asBitmap().diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true).into(ivAdvertise);
            } else if (alAdvertiseMasters.get(0).getAdvertiseText() != null && !alAdvertiseMasters.get(0).getAdvertiseText().equals("")) {
                ivAdvertise.setVisibility(View.GONE);
                txtAdvertise.setVisibility(View.VISIBLE);
                txtAdvertise.setText(alAdvertiseMasters.get(0).getAdvertiseText());
            }
        } else {
            ivAdvertise.setVisibility(View.GONE);
            txtAdvertise.setVisibility(View.GONE);
            CurrentPageAdvertise = 1;
        }
    }

    @Override
    public void MemberResponse(String errorCode, MemberMaster objMemberMaster) {
        this.objMemberMaster = objMemberMaster;
        if (objMemberMaster != null) {
            if (isDetail) {
                fabEdit.setVisibility(View.GONE);
                name = objMemberMaster.getMemberName();
                SetAdvertise();
            } else if (isNotification) {
                fabEdit.setVisibility(View.GONE);
                name = objMemberMaster.getMemberName();
                if (objMemberMaster.getMemberName() != null && !objMemberMaster.getMemberName().equals("")) {
                    getSupportActionBar().setTitle(name);
                }
            } else if (!isDetail && !isNotification) {
//                getSupportActionBar().setTitle("My Profile");
                fabEdit.setVisibility(View.VISIBLE);
                ivAdvertise.setVisibility(View.GONE);
                ivAddress.setVisibility(View.GONE);
                ivAddress1.setVisibility(View.GONE);
                ivEmail.setVisibility(View.GONE);
                ivMessage.setVisibility(View.GONE);
                ivMessage1.setVisibility(View.GONE);
                ivCall.setVisibility(View.GONE);
                ivCall1.setVisibility(View.GONE);
//                ivProfile.setOnClickListener(this);
//                SetUserName();
            }
            SetData();
        }
    }

    @Override
    public void MemberUpdate(String errorCode, MemberMaster objMemberMaster) {
        progressDialog.dismiss();
        if (isNotification) {
            if (errorCode.equals("0")) {
                if (isStart) {
//                    Intent intent = new Intent(DetailActivity.this, HomeActivity.class);
//                    startActivity(intent);
//                    finish();
                    OnApprvedFromNotification();
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("position", position);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            } else {
                Globals.ShowSnackBar(llContactdetail, getResources().getString(R.string.MsgServerNotResponding), DetailActivity.this, 1000);
            }
        } else if (objMemberMaster != null) {
            if (errorCode.equals("0")) {
                if (objMemberMaster.getImageName() != null && !objMemberMaster.getImageName().equals("")) {
                    Glide.with(DetailActivity.this).load(objMemberMaster.getImageName()).asBitmap().centerCrop().diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true).into(ivProfile);
                    SharePreferenceManage objSharePreferenceManage = new SharePreferenceManage();
                    objSharePreferenceManage.CreatePreference("LoginPreference", "MemberImage", objMemberMaster.getImageName(), DetailActivity.this);
                    ivProfile1.setVisibility(View.VISIBLE);
                }
            } else if (errorCode.equals("-1")) {
                ivProfile.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                ivProfile.setImageResource(R.drawable.accountprofile);
                ivProfile1.setVisibility(View.GONE);
                SharePreferenceManage objSharePreferenceManage = new SharePreferenceManage();
                objSharePreferenceManage.CreatePreference("LoginPreference", "MemberImage", objMemberMaster.getImageName(), DetailActivity.this);
            }
        }
    }

    @Override
    public void UpdateResponse() {
        this.objMemberMaster.setMemberName(objSharePreferenceManage.GetPreference("LoginPreference", "MemberName", DetailActivity.this));
        this.objMemberMaster.setGender(objSharePreferenceManage.GetPreference("LoginPreference", "Gender", DetailActivity.this));
        this.objMemberMaster.setBirthDate(objSharePreferenceManage.GetPreference("LoginPreference", "Birthdate", DetailActivity.this));
        this.objMemberMaster.setPhone1(objSharePreferenceManage.GetPreference("LoginPreference", "Phone", DetailActivity.this));
        if (getSupportFragmentManager().getBackStackEntryCount() != 0) {
            if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                    && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getResources().getString(R.string.title_fragment_your_profile))) {
                RequestMemberByMemberMasterId();
                getSupportFragmentManager().popBackStack(getResources().getString(R.string.title_fragment_your_profile), FragmentManager.POP_BACK_STACK_INCLUSIVE);
            } else if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                    && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals("ContactDetail")) {
                RequestMemberByMemberMasterId();
                getSupportFragmentManager().popBackStack("ContactDetail", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            } else if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                    && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals("PersonalDetail")) {
                RequestMemberByMemberMasterId();
                getSupportFragmentManager().popBackStack("PersonalDetail", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        }
        Log.e("update:", " ");
//        SetData();
    }

    @Override
    public void ConfirmResponse() {
        if (isSave) {
            isSave = false;


            Globals.ContactSave(DetailActivity.this, objMemberMaster);
        } else if (isApproved) {
            RequestIsApproved(objMemberMaster, isApproved);
        } else {
            if (isStart) {
//                Intent intent = new Intent(DetailActivity.this, HomeActivity.class);
//                startActivity(intent);
//                finish();
                RequestIsApproved(objMemberMaster, isApproved);

            } else {
                Intent intent = new Intent();
                intent.putExtra("position", position);
                if (isNotification && !isStart) {
                    intent.putExtra("isApproved", isApproved);
                    intent.putExtra("isStart", true);
                }
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }

    public void SelectImage(int requestCode, Intent data) {
        String picturePath = "";
        if (requestCode == 100) {
//            strImageName = "CameraImage_" + simpleDateFormat.format(new Date()) + imageName.substring(imageName.lastIndexOf("."), imageName.length()) + ".jpg";
//            File file = new File(mediaStorageDir, strImageName);
//            picturePath = file.getAbsolutePath();
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            long millis = System.currentTimeMillis();
            imageName = String.valueOf(millis) + ".jpg";
            ivProfile.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ivProfile.setImageBitmap(bitmap);
            ivProfile1.setVisibility(View.VISIBLE);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 110, bos);
            byte[] byteData = bos.toByteArray();
            imagePhysicalNameBytes = Base64.encodeToString(byteData, Base64.DEFAULT);
            UpdateUserProfileImageRequest();
            return;

        } else if (requestCode == 101 && data != null && data.getData() != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            Log.e("path", " " + filePathColumn[0]);

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            long millis = System.currentTimeMillis();
            String extension = MimeTypeMap.getFileExtensionFromUrl(picturePath);
            if (extension != null) {
                imageName = String.valueOf(millis) + "." + MimeTypeMap.getFileExtensionFromUrl(picturePath);
            } else {
                imageName = String.valueOf(millis) + ".jpg";
            }
            cursor.close();
            if (!picturePath.equals("")) {
                try {
                    File file = new File(picturePath);
                    Bitmap bitmap = decodeFile(file);
                    ivProfile.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    ivProfile.setImageBitmap(bitmap);
                    ivProfile1.setVisibility(View.VISIBLE);
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                    byte[] byteData = bos.toByteArray();
                    imagePhysicalNameBytes = Base64.encodeToString(byteData, Base64.DEFAULT);
                    UpdateUserProfileImageRequest();
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return;
            }
        }

    }


    private void SetData() {
        if (objMemberMaster != null) {
            if (objMemberMaster.getHomeNumberStreet() != null) {
                llAddress.setVisibility(View.VISIBLE);
                txtAddress.setText(objMemberMaster.getHomeNumberStreet() + ", " + objMemberMaster.getHomeNearBy() + ", " + objMemberMaster.getHomeArea() + ", " + objMemberMaster.getHomeCity()
                        + ", " + objMemberMaster.getHomeState() + " - " + objMemberMaster.getHomeZipCode());
            } else {
                llAddress.setVisibility(View.GONE);
            }
            if (isDetail || isNotification) {
                if (objMemberMaster.getImageName() != null) {
                    ivProfile.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    Glide.with(DetailActivity.this).load(objMemberMaster.getImageName()).asBitmap().diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true).into(ivProfile);
//                ivProfile.setBackground(getResources().getDrawable(R.drawable.profile_gradient));
//                app_bar.setBackgroundColor(getResources().getColor(R.color.card_enable));
                    ivProfile1.setVisibility(View.VISIBLE);
                } else {
                    ivProfile1.setVisibility(View.GONE);
                    ivProfile.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                    ivProfile.setImageResource(R.drawable.accountprofile);
                }
            } else {
                if (objSharePreferenceManage.GetPreference("LoginPreference", "MemberImage", DetailActivity.this) != null &&
                        !objSharePreferenceManage.GetPreference("LoginPreference", "MemberImage", DetailActivity.this).equals("")) {
                    Log.e("image", " " + objSharePreferenceManage.GetPreference("LoginPreference", "MemberImage", DetailActivity.this));
                    ivProfile.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    Glide.with(DetailActivity.this).load(objSharePreferenceManage.GetPreference("LoginPreference", "MemberImage", DetailActivity.this)).asBitmap()
                            .diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(ivProfile);
                    ivProfile1.setVisibility(View.VISIBLE);
                } else {
                    ivProfile1.setVisibility(View.GONE);
                    ivProfile.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                    ivProfile.setImageResource(R.drawable.accountprofile);
                }
            }

            if (ivProfile.getDrawable() == null) {
                ivProfile1.setVisibility(View.GONE);
                ivProfile.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                ivProfile.setImageResource(R.drawable.accountprofile);
            }
            if (objMemberMaster.getPhone1() != null) {
                llMobile.setVisibility(View.VISIBLE);
                txtMobile.setText("+91" + objMemberMaster.getPhone1());
            } else {
                llMobile.setVisibility(View.GONE);
            }
            if (objMemberMaster.getPhone2() != null) {
                llMobile1.setVisibility(View.VISIBLE);
                txtMobile1.setText("+91" + objMemberMaster.getPhone2());
            } else {
                llMobile1.setVisibility(View.GONE);
            }
            if (objMemberMaster.getEmail() != null) {
                llEmail.setVisibility(View.VISIBLE);
                txtEmail.setText(objMemberMaster.getEmail());
            } else {
                llEmail.setVisibility(View.GONE);
            }
            if (objMemberMaster.getBirthDate() != null) {
                llDOB.setVisibility(View.VISIBLE);
                txtDOB.setText(objMemberMaster.getBirthDate());
            } else {
                llDOB.setVisibility(View.GONE);
            }
            if (objMemberMaster.getProfession() != null) {
                llDesignation.setVisibility(View.VISIBLE);
                txtDesignation.setText(objMemberMaster.getProfession());
            } else {
                llDesignation.setVisibility(View.GONE);
            }
            if (objMemberMaster.getQualification() != null) {
                llQualification.setVisibility(View.VISIBLE);
                txtQualification.setText(objMemberMaster.getQualification());
            } else {
                llQualification.setVisibility(View.GONE);
            }
            if (objMemberMaster.getBloodGroup() != null) {
                llBloodGroup.setVisibility(View.VISIBLE);
                txtBloodGroup.setText(objMemberMaster.getBloodGroup());
            } else {
                llBloodGroup.setVisibility(View.GONE);
            }
//            txtMobile.setText(objMemberMaster.getPhone1());
//            txtEmail.setText(objMemberMaster.getEmail());
//            txtDOB.setText(objMemberMaster.getBirthDate());
//            txtDesignation.setText(objMemberMaster.getProfession());
//            txtQualification.setText(objMemberMaster.getQualification());
//            txtBloodGroup.setText(objMemberMaster.getBloodGroup());
            if (objMemberMaster.getAnniversaryDate() != null) {
                llAnniversaryDate.setVisibility(View.VISIBLE);
                txtAnniversaryDate.setText(objMemberMaster.getAnniversaryDate());
                if (objMemberMaster.getLstMemberRelativeTran() != null && objMemberMaster.getLstMemberRelativeTran().size() > 0) {
                    if (objMemberMaster.getLstMemberRelativeTran().get(0).getRelativeName() != null) {
                        llSpouseName.setVisibility(View.VISIBLE);
                        txtSpouseName.setText(objMemberMaster.getLstMemberRelativeTran().get(0).getRelativeName());
                    } else {
                        llSpouseName.setVisibility(View.GONE);
                    }
                    if (objMemberMaster.getLstMemberRelativeTran().get(0).getBirthDate() != null) {
                        llSpouseDOB.setVisibility(View.VISIBLE);
                        txtSpouseDOB.setText(objMemberMaster.getLstMemberRelativeTran().get(0).getBirthDate());
                    } else {
                        llSpouseDOB.setVisibility(View.GONE);
                    }
                    if (objMemberMaster.getLstMemberRelativeTran().get(0).getImageName() != null) {
                        Glide.with(DetailActivity.this).load(objMemberMaster.getLstMemberRelativeTran().get(0).getImageName()).asBitmap().centerCrop()
                                .diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(new BitmapImageViewTarget(ivSpouse) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                if (resource != null) {
                                    ivSpouse.setVisibility(View.VISIBLE);
                                    RoundedBitmapDrawable circularBitmapDrawable =
                                            RoundedBitmapDrawableFactory.create(getResources(), resource);
                                    circularBitmapDrawable.setCircular(true);
                                    ivSpouse.setImageDrawable(circularBitmapDrawable);
                                } else {
                                    ivSpouse.setVisibility(View.GONE);
                                }
                            }
                        });
                    }

                    if (objMemberMaster.getLstMemberRelativeTran().size() > 1) {
                        llChildDetail1.setVisibility(View.VISIBLE);
                        if (objMemberMaster.getLstMemberRelativeTran().get(1).getRelativeName() != null) {
                            txtChildName1.setText(objMemberMaster.getLstMemberRelativeTran().get(1).getRelativeName());
                        } else {
                            llChildDetail1.setVisibility(View.GONE);
                        }
                        if (objMemberMaster.getLstMemberRelativeTran().get(1).getBirthDate() != null) {
                            txtChildDOB1.setText(objMemberMaster.getLstMemberRelativeTran().get(1).getBirthDate());
                        } else {
                            llChildDetail1.setVisibility(View.GONE);
                        }
                        if (objMemberMaster.getLstMemberRelativeTran().get(1).getGender().equals("Male")) {
                            txtChildGender1.setText("Male");
                        } else if (objMemberMaster.getLstMemberRelativeTran().get(1).getGender().equals("Female")) {
                            txtChildGender1.setText("Female");
                        }
                        if (objMemberMaster.getLstMemberRelativeTran().get(1).getImageName() != null) {
                            Glide.with(DetailActivity.this).load(objMemberMaster.getLstMemberRelativeTran().get(1).getImageName()).asBitmap().centerCrop()
                                    .diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(new BitmapImageViewTarget(ivChild1) {
                                @Override
                                protected void setResource(Bitmap resource) {
                                    if (resource != null) {
                                        ivChild1.setVisibility(View.VISIBLE);
                                        RoundedBitmapDrawable circularBitmapDrawable =
                                                RoundedBitmapDrawableFactory.create(getResources(), resource);
                                        circularBitmapDrawable.setCircular(true);
                                        ivChild1.setImageDrawable(circularBitmapDrawable);
                                    } else {
                                        ivChild1.setVisibility(View.GONE);
                                    }
                                }
                            });
                        } else {
                            ivChild1.setVisibility(View.GONE);
                        }

                        if (objMemberMaster.getLstMemberRelativeTran().size() > 2) {
                            llChildDetail2.setVisibility(View.VISIBLE);
                            if (objMemberMaster.getLstMemberRelativeTran().get(2).getRelativeName() != null) {
                                txtChildName2.setText(objMemberMaster.getLstMemberRelativeTran().get(2).getRelativeName());
                            } else {
                                llChildDetail2.setVisibility(View.GONE);
                            }
                            if (objMemberMaster.getLstMemberRelativeTran().get(2).getBirthDate() != null) {
                                txtChildDOB2.setText(objMemberMaster.getLstMemberRelativeTran().get(2).getBirthDate());
                            } else {
                                llChildDetail2.setVisibility(View.GONE);
                            }
                            if (objMemberMaster.getLstMemberRelativeTran().get(2).getGender().equals("Male")) {
                                txtChildGender2.setText("Male");
                            } else if (objMemberMaster.getLstMemberRelativeTran().get(2).getGender().equals("Female")) {
                                txtChildGender2.setText("Female");
                            }
                            if (objMemberMaster.getLstMemberRelativeTran().get(2).getImageName() != null) {
                                Glide.with(DetailActivity.this).load(objMemberMaster.getLstMemberRelativeTran().get(2).getImageName()).asBitmap().centerCrop()
                                        .diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(new BitmapImageViewTarget(ivChild2) {
                                    @Override
                                    protected void setResource(Bitmap resource) {
                                        if (resource != null) {
                                            ivChild2.setVisibility(View.VISIBLE);
                                            RoundedBitmapDrawable circularBitmapDrawable =
                                                    RoundedBitmapDrawableFactory.create(getResources(), resource);
                                            circularBitmapDrawable.setCircular(true);
                                            ivChild2.setImageDrawable(circularBitmapDrawable);
                                        } else {
                                            ivChild2.setVisibility(View.GONE);
                                        }
                                    }
                                });
                            } else {
                                ivChild2.setVisibility(View.GONE);
                            }

                            if (objMemberMaster.getLstMemberRelativeTran().size() > 3) {
                                llChildDetail3.setVisibility(View.VISIBLE);
                                if (objMemberMaster.getLstMemberRelativeTran().get(3).getRelativeName() != null) {
                                    txtChildName3.setText(objMemberMaster.getLstMemberRelativeTran().get(3).getRelativeName());
                                } else {
                                    llChildDetail3.setVisibility(View.GONE);
                                }
                                if (objMemberMaster.getLstMemberRelativeTran().get(3).getBirthDate() != null) {
                                    txtChildDOB3.setText(objMemberMaster.getLstMemberRelativeTran().get(3).getBirthDate());
                                } else {
                                    llChildDetail3.setVisibility(View.GONE);
                                }
                                if (objMemberMaster.getLstMemberRelativeTran().get(3).getGender().equals("Male")) {
                                    txtChildGender3.setText("Male");
                                } else if (objMemberMaster.getLstMemberRelativeTran().get(3).getGender().equals("Female")) {
                                    txtChildGender3.setText("Female");
                                }
                                if (objMemberMaster.getLstMemberRelativeTran().get(3).getImageName() != null) {
                                    Glide.with(DetailActivity.this).load(objMemberMaster.getLstMemberRelativeTran().get(3).getImageName()).asBitmap().centerCrop()
                                            .diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(new BitmapImageViewTarget(ivChild3) {
                                        @Override
                                        protected void setResource(Bitmap resource) {
                                            if (resource != null) {
                                                ivChild3.setVisibility(View.VISIBLE);
                                                RoundedBitmapDrawable circularBitmapDrawable =
                                                        RoundedBitmapDrawableFactory.create(getResources(), resource);
                                                circularBitmapDrawable.setCircular(true);
                                                ivChild3.setImageDrawable(circularBitmapDrawable);
                                            } else {
                                                ivChild3.setVisibility(View.GONE);
                                            }
                                        }
                                    });
                                } else {
                                    ivChild3.setVisibility(View.GONE);
                                }

                                if (objMemberMaster.getLstMemberRelativeTran().size() > 4) {
                                    llChildDetail4.setVisibility(View.VISIBLE);
                                    if (objMemberMaster.getLstMemberRelativeTran().get(4).getRelativeName() != null) {
                                        txtChildName4.setText(objMemberMaster.getLstMemberRelativeTran().get(4).getRelativeName());
                                    } else {
                                        llChildDetail4.setVisibility(View.GONE);
                                    }
                                    if (objMemberMaster.getLstMemberRelativeTran().get(4).getBirthDate() != null) {
                                        txtChildDOB4.setText(objMemberMaster.getLstMemberRelativeTran().get(4).getBirthDate());
                                    } else {
                                        llChildDetail4.setVisibility(View.GONE);
                                    }
                                    if (objMemberMaster.getLstMemberRelativeTran().get(4).getGender().equals("Male")) {
                                        txtChildGender4.setText("Male");
                                    } else if (objMemberMaster.getLstMemberRelativeTran().get(4).getGender().equals("Female")) {
                                        txtChildGender4.setText("Female");
                                    }
                                    if (objMemberMaster.getLstMemberRelativeTran().get(4).getImageName() != null) {
                                        Glide.with(DetailActivity.this).load(objMemberMaster.getLstMemberRelativeTran().get(4).getImageName()).asBitmap().centerCrop()
                                                .diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(new BitmapImageViewTarget(ivChild4) {
                                            @Override
                                            protected void setResource(Bitmap resource) {
                                                if (resource != null) {
                                                    ivChild4.setVisibility(View.VISIBLE);
                                                    RoundedBitmapDrawable circularBitmapDrawable =
                                                            RoundedBitmapDrawableFactory.create(getResources(), resource);
                                                    circularBitmapDrawable.setCircular(true);
                                                    ivChild4.setImageDrawable(circularBitmapDrawable);
                                                } else {
                                                    ivChild4.setVisibility(View.GONE);
                                                }
                                            }
                                        });
                                    } else {
                                        ivChild4.setVisibility(View.GONE);
                                    }


                                    if (objMemberMaster.getLstMemberRelativeTran().size() > 5) {
                                        llChildDetail5.setVisibility(View.VISIBLE);
                                        if (objMemberMaster.getLstMemberRelativeTran().get(5).getRelativeName() != null) {
                                            txtChildName5.setText(objMemberMaster.getLstMemberRelativeTran().get(5).getRelativeName());
                                        } else {
                                            llChildDetail5.setVisibility(View.GONE);
                                        }
                                        if (objMemberMaster.getLstMemberRelativeTran().get(5).getBirthDate() != null) {
                                            txtChildDOB5.setText(objMemberMaster.getLstMemberRelativeTran().get(5).getBirthDate());
                                        } else {
                                            llChildDetail5.setVisibility(View.GONE);
                                        }
                                        if (objMemberMaster.getLstMemberRelativeTran().get(5).getGender().equals("Male")) {
                                            txtChildGender5.setText("Male");
                                        } else if (objMemberMaster.getLstMemberRelativeTran().get(5).getGender().equals("Female")) {
                                            txtChildGender5.setText("Female");
                                        }
                                        if (objMemberMaster.getLstMemberRelativeTran().get(5).getImageName() != null) {
                                            Glide.with(DetailActivity.this).load(objMemberMaster.getLstMemberRelativeTran().get(5).getImageName()).asBitmap().centerCrop()
                                                    .diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(new BitmapImageViewTarget(ivChild5) {
                                                @Override
                                                protected void setResource(Bitmap resource) {
                                                    if (resource != null) {
                                                        ivChild5.setVisibility(View.VISIBLE);
                                                        RoundedBitmapDrawable circularBitmapDrawable =
                                                                RoundedBitmapDrawableFactory.create(getResources(), resource);
                                                        circularBitmapDrawable.setCircular(true);
                                                        ivChild5.setImageDrawable(circularBitmapDrawable);
                                                    } else {
                                                        ivChild5.setVisibility(View.GONE);
                                                    }
                                                }
                                            });
                                        } else {
                                            ivChild5.setVisibility(View.GONE);
                                        }
                                    } else {
                                        llChildDetail5.setVisibility(View.GONE);
                                    }
                                } else {
                                    llChildDetail4.setVisibility(View.GONE);
                                }
                            } else {
                                llChildDetail3.setVisibility(View.GONE);
                            }
                        } else {
                            llChildDetail2.setVisibility(View.GONE);
                        }
                    } else {
                        llChildDetail1.setVisibility(View.GONE);
                    }

                } else {
                    llSpouseName.setVisibility(View.GONE);
                    llSpouseDOB.setVisibility(View.GONE);
                    ivSpouse.setVisibility(View.GONE);
                }
            } else {
                llAnniversaryDate.setVisibility(View.GONE);
                llSpouseName.setVisibility(View.GONE);
                llSpouseDOB.setVisibility(View.GONE);
                ivSpouse.setVisibility(View.GONE);
                llChildDetail1.setVisibility(View.GONE);
                llChildDetail2.setVisibility(View.GONE);
                llChildDetail3.setVisibility(View.GONE);
                llChildDetail4.setVisibility(View.GONE);
                llChildDetail5.setVisibility(View.GONE);
            }

            if (objMemberMaster.getOfficeNumberStreet() != null) {
                llOfficeAddress.setVisibility(View.VISIBLE);
                txtOfficeAddress.setText(objMemberMaster.getOfficeNumberStreet() + ", " + objMemberMaster.getOfficeNearBy() + ", " + objMemberMaster.getOfficeArea() + ", " + objMemberMaster.getOfficeCity()
                        + ", " + objMemberMaster.getOfficeState() + " - " + objMemberMaster.getOfficeZipCode());
            } else {
                llOfficeAddress.setVisibility(View.GONE);
            }
            if (objMemberMaster.getOfficePhone() != null) {
                llOfficeNo.setVisibility(View.VISIBLE);
                txtOfficeNo.setText(objMemberMaster.getOfficePhone());
            } else {
                llOfficeNo.setVisibility(View.GONE);
            }
            if (objMemberMaster.getHomePhone() != null) {
                llHomeNo.setVisibility(View.VISIBLE);
                txtHomeNo.setText(objMemberMaster.getHomePhone());
            } else {
                llHomeNo.setVisibility(View.GONE);
            }
        }
    }

    private void ReplaceFragment(Fragment fragment, String fragmentName) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (Build.VERSION.SDK_INT >= 21) {
            Slide slideTransition = new Slide();
            slideTransition.setSlideEdge(Gravity.RIGHT);
            slideTransition.setDuration(350);
            fragment.setEnterTransition(slideTransition);
        } else {
            fragmentTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out, 0, R.anim.right_exit);
        }
        fragmentTransaction.replace(android.R.id.content, fragment, fragmentName);
        fragmentTransaction.addToBackStack(fragmentName);
        fragmentTransaction.commit();
    }

    private void SetAdvertise() {
        if (isDetail || isNotification) {
//            ivAdvertise.setVisibility(View.VISIBLE);
            isAdvertise = true;
            int delay = 1000; // delay for 10 sec.
            int period = 15000; // repeat every 10 sec.
            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                public void run() {
                    DetailActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            RequestAdvertiseMaster();
                        }
                    });
                }
            }, delay, period);
        }
    }

    private void RequestAdvertiseMaster() {
        objAdvertiseJSONParser.SelectAllAdvertiseMasterPageWise(String.valueOf(CurrentPageAdvertise), String.valueOf(1), DetailActivity.this, null);
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

    private void UpdateUserProfileImageRequest() {
        progressDialog = new ProgressDialog();
        progressDialog.show(getSupportFragmentManager(), "");

        try {
            MemberJSONParser objMemberJSONParser = new MemberJSONParser();
            MemberMaster objMemberMaster = new MemberMaster();
            objMemberMaster.setMemberMasterId(Globals.memberMasterId);
            if (imageName != null && !imageName.equals("")) {
//                strImageName = imageName.substring(0, imageName.lastIndexOf(".")) + "_" + simpleDateFormat.format(new Date()) + imageName.substring(imageName.lastIndexOf("."), imageName.length());
                strImageName = imageName;
                objMemberMaster.setImageName(strImageName);
                if (imagePhysicalNameBytes != null || !imagePhysicalNameBytes.equals("")) {
                    objMemberMaster.setImageNameBytes(imagePhysicalNameBytes);
                }
            }
            objMemberJSONParser.UpdateMemberMasterImageByMemberMasterId(DetailActivity.this, objMemberMaster);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (marshMallowPermission.checkPermissionForExternalStorage()) {
//                            try {
//                                mediaStorageDir = new File(
//                                        Environment.getExternalStorageDirectory()
//                                                + File.separator
//                                                + getString(R.string.directory_name_corp_chat)
//                                                + File.separator
//                                                + getString(R.string.directory_name_images));
//
//                                if (!mediaStorageDir.exists()) {
//                                    mediaStorageDir.mkdirs();
//                                }
//                                File mediaFile = File.createTempFile(
//                                        "Ad_" + String.valueOf(System.currentTimeMillis()), ".jpg", mediaStorageDir);
//                                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
////                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
////                                File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
//                                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mediaFile));
//                                intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
//                                ((Activity) context).startActivityForResult(intent, requestCodeCamera);
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
                            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                            fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
//
//                            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                            startActivityForResult(cameraIntent, requestCodeCamera);
                        }
                    } else {
//                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                        try {
//                            mediaStorageDir = new File(
//                                    Environment.getExternalStorageDirectory()
//                                            + File.separator
//                                            + getString(R.string.directory_name_corp_chat)
//                                            + File.separator
//                                            + getString(R.string.directory_name_images));
//
//                            if (!mediaStorageDir.exists()) {
//                                mediaStorageDir.mkdirs();
//                            }
//                            File mediaFile = File.createTempFile(
//                                    "Ad_" + String.valueOf(System.currentTimeMillis()), ".jpg", mediaStorageDir);
//
//                            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
//                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mediaFile));
//                            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
////                        File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
////                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
//                            ((Activity) context).startActivityForResult(intent, requestCodeCamera);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, requestCodeCamera);
                    }
                } else if (items[item].equals("Choose from Gallery")) {
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (marshMallowPermission.checkPermissionForExternalStorage()) {
                            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            intent.setType("image/*");
                            ((Activity) context).startActivityForResult(Intent.createChooser(intent, "Select File"), requestCodeGallery);
                        }
                    } else {
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        ((Activity) context).startActivityForResult(Intent.createChooser(intent, "Select File"), requestCodeGallery);
                    }
                } else if (items[item].equals("Remove Image")) {
                    imageName = null;
                    imagePhysicalNameBytes = null;
                    UpdateUserProfileImageRequest();
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

//    public Uri getOutputMediaFileUri(int type) {
//        return Uri.fromFile(getOutputMediaFile(type));
//    }
//
//    private static File getOutputMediaFile(int type) {
//
//        // External sdcard location
//        File mediaStorageDir = new File(
//                Environment
//                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
//                IMAGE_DIRECTORY_NAME);
//
//        // Create the storage directory if it does not exist
//        if (!mediaStorageDir.exists()) {
//            if (!mediaStorageDir.mkdirs()) {
//                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
//                        + IMAGE_DIRECTORY_NAME + " directory");
//                return null;
//            }
//        }
//
//        // Create a media file name
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
//                Locale.getDefault()).format(new Date());
//        File mediaFile;
//        if (type == MEDIA_TYPE_IMAGE) {
//            mediaFile = new File(mediaStorageDir.getPath() + File.separator
//                    + "IMG_" + timeStamp + ".jpg");
//        } else {
//            return null;
//        }
//
//        return mediaFile;
//    }

    private void OnApprvedFromNotification() {
        try {
//            objSharePreferenceManage = new SharePreferenceManage();
//            if (objSharePreferenceManage.GetPreference("LoginPreference", "MemberName", DetailActivity.this) != null && objSharePreferenceManage.GetPreference("LoginPreference", "MemberPassword", DetailActivity.this) != null) {
//                String userName = objSharePreferenceManage.GetPreference("LoginPreference", "MemberName", DetailActivity.this);
////            String userPassword = objSharePreferenceManage.GetPreference("LoginPreference", "MemberPassword", DetailActivity.this);
////            if ((!userName.isEmpty() && !userPassword.isEmpty())) {
            Globals.memberMasterId = Integer.parseInt(objSharePreferenceManage.GetPreference("LoginPreference", "MemberMasterId", DetailActivity.this));
            Globals.memberType = objSharePreferenceManage.GetPreference("LoginPreference", "MemberType", DetailActivity.this);
            Globals.isAdmin = objSharePreferenceManage.GetPreference("LoginPreference", "MemberType", DetailActivity.this).equals("Admin");
////                        if (objSharePreferenceManage.GetPreference("LoginPreference", "startPage", SplashScreenActivity.this) != null) {
////                            Globals.startPage = Integer.parseInt(objSharePreferenceManage.GetPreference("LoginPreference", "startPage", SplashScreenActivity.this));
////                        }
////                        if (Globals.startPage == 0) {
            Intent intent = new Intent(DetailActivity.this, HomeActivity.class);
            MemberMaster objMemberMaster = new MemberMaster();
            objMemberMaster.setMemberName(objSharePreferenceManage.GetPreference("LoginPreference", "MemberName", DetailActivity.this));
            objMemberMaster.setMemberMasterId(Integer.parseInt(objSharePreferenceManage.GetPreference("LoginPreference", "MemberMasterId", DetailActivity.this)));
            objMemberMaster.setMemberType(objSharePreferenceManage.GetPreference("LoginPreference", "MemberType", DetailActivity.this));
            objMemberMaster.setEmail(objSharePreferenceManage.GetPreference("LoginPreference", "MemberEmail", DetailActivity.this));
            objMemberMaster.setImageName(objSharePreferenceManage.GetPreference("LoginPreference", "MemberImage", DetailActivity.this));
            objMemberMaster.setIsApproved(Boolean.parseBoolean(objSharePreferenceManage.GetPreference("LoginPreference", "IsApproved", DetailActivity.this)));
            intent.putExtra("memberMaster", objMemberMaster);
            startActivity(intent);
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
            finish();
//                        Globals.ChangeActivity(SplashScreenActivity.this, HomeActivity.class, true);
//                        } else {
//                            Globals.ChangeActivity(SplashScreenActivity.this, RegistartionFragmentActivity.class, true);
//                        }
//            } else {
//                Globals.ClearUserPreference(DetailActivity.this);
//                Globals.ChangeActivity(DetailActivity.this, SignInActivity.class, true);
//            }
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void RequestMemberByMemberMasterId() {
        MemberJSONParser objMemeberJSONParser = new MemberJSONParser();
        objMemeberJSONParser.SelectMemberByMemberMasterId(DetailActivity.this, memberMasterId);
    }

    private void RequestIsApproved(MemberMaster objMemberMaster, boolean isApproved) {
        if (progressDialog.isAdded()) {
            progressDialog.dismiss();
        }
        progressDialog.show(getSupportFragmentManager(), "");

        objMemberMaster.setIsApproved(isApproved);
        objMemberMaster.setlinktoMemberMasterIdUpdatedBy(Globals.memberMasterId);
        objMemberMaster.setlinktoMemberMasterIdApprovedBy(Globals.memberMasterId);
        MemberJSONParser objMemeberJSONParser = new MemberJSONParser();
        objMemeberJSONParser.UpdateMemberMasterIsApproved(DetailActivity.this, objMemberMaster);
    }


}
