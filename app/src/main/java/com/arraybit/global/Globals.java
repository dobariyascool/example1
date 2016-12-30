package com.arraybit.global;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.arraybit.modal.MemberMaster;
import com.arraybit.mym.R;
import com.rey.material.widget.EditText;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Avani on 08/11/2016.
 */

public class Globals {

    public static String DateFormat = "dd/MM/yyyy";
    public static int memberMasterId = 0;
    public static boolean isAdmin = false;
    public static int startPage = 0;
    public static String memberType = "";
    static int y, M, d, H, m;

    public static void HideKeyBoard(Context context, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (Exception e) {
            // Log exception
            return null;
        }
    }

    public static void SetErrorLayout(LinearLayout layout, boolean isShow, String errorMsg, RecyclerView recyclerView, int errorIcon) {
        TextView txtMsg = (TextView) layout.findViewById(R.id.txtMsg);
        ImageView ivErrorIcon = (ImageView) layout.findViewById(R.id.ivErrorIcon);
        if (errorIcon != 0) {
            ivErrorIcon.setImageResource(errorIcon);
        }
        if (isShow) {
            layout.setVisibility(View.VISIBLE);
            txtMsg.setText(errorMsg);
            if (recyclerView != null) {
                recyclerView.setVisibility(View.GONE);
            }
        } else {
            layout.setVisibility(View.GONE);
            if (recyclerView != null) {
                recyclerView.setVisibility(View.VISIBLE);
            }
        }
    }

    public static String UrlEncode(String url) {
        try {
            url = url.replace("%", "~PERCENT~").replace("^", "~CARET~").replace(" ", "~SPACE~").replace("&", "~AMPERSAND~").replace("*", "~ASTERISK~")
                    .replace(":", "~COLON~").replace("<", "~LESSTHAN~").replace(",", "~COMMA~").replace(">", "~GREATERTHAN~").replace(".", "~DOT~")
                    .replace("?", "~QUESTION~").replace("\\", "~BACKSLASH~").replace("+", "~PLUS~");
            return URLEncoder.encode(url, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void ShowSnackBar(View view, String message, Context context, int duration) {
        Snackbar snackbar = Snackbar.make(view, message, duration);
        View snackView = snackbar.getView();
        if (Build.VERSION.SDK_INT >= 21) {
            snackView.setElevation(context.getResources().getDimension(R.dimen.snackbar_elevation));
        }
        TextView txt = (TextView) snackView.findViewById(android.support.design.R.id.snackbar_text);
        txt.setGravity(Gravity.CENTER);
        txt.setTextColor(ContextCompat.getColor(context, android.R.color.white));
        txt.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        snackView.setBackgroundColor(ContextCompat.getColor(context, R.color.blue_grey));
        snackbar.show();
    }

    public static void SetToolBarBackground(final Context context, final Toolbar app_bar, final int backgroundColor, final int tintColor) {
        try {
            app_bar.setBackground(new ColorDrawable(backgroundColor));
            Drawable drawable = app_bar.getOverflowIcon();
            DrawableCompat.setTint(drawable.mutate(), tintColor);
            app_bar.setOverflowIcon(drawable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean IsValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static void ClearUserPreference(final Context context) {
        SharePreferenceManage objSharePreferenceManage = new SharePreferenceManage();

        objSharePreferenceManage.RemovePreference("LoginPreference", "MemberMasterId", context);
        objSharePreferenceManage.RemovePreference("LoginPreference", "MemberEmail", context);
        objSharePreferenceManage.RemovePreference("LoginPreference", "MemberPassword", context);
        objSharePreferenceManage.RemovePreference("LoginPreference", "MemberName", context);
        objSharePreferenceManage.RemovePreference("LoginPreference", "MemberType", context);
        objSharePreferenceManage.RemovePreference("LoginPreference", "startPage", context);
        objSharePreferenceManage.RemovePreference("LoginPreference", "Phone", context);
        objSharePreferenceManage.RemovePreference("LoginPreference", "Gender", context);
        objSharePreferenceManage.RemovePreference("LoginPreference", "IsApproved", context);
        objSharePreferenceManage.RemovePreference("LoginPreference", "MemberImage", context);
        objSharePreferenceManage.ClearPreference("LoginPreference", context);

        memberMasterId = 0;
        startPage = 0;
        memberType = "";
        isAdmin = false;
    }

    public static void ChangeActivity(Activity fromActivity, Class toActivity, boolean finish) {
        Intent intent = new Intent(fromActivity, toActivity);
        fromActivity.startActivity(intent);
        fromActivity.overridePendingTransition(R.anim.right_in, R.anim.left_out);
        if (finish) {
            fromActivity.finish();
        }
    }

    public static void SetItemAnimator(RecyclerView.ViewHolder holder) {
        //slide from bottom
        ObjectAnimator animatorTranslateY = ObjectAnimator.ofFloat(holder.itemView, "translationY", 200, 0);
        animatorTranslateY.setDuration(500);
        animatorTranslateY.start();
    }

    public static void SelectImage(final Context context, final int requestCodeCamera, final int requestCodeGallery) {
        final CharSequence[] items = {"Take Photo", "Choose from Gallery", "Remove Image"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialog);
        builder.setTitle("ADD PHOTO");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
//                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    File f = new File(android.os.Environment
//                            .getExternalStorageDirectory(), "temp.jpg");
//                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
//                    ((Activity) context).startActivityForResult(intent, requestCodeCamera);
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        MarshMallowPermission marshMallowPermission = new MarshMallowPermission((Activity) context);
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
                            ((Activity) context).startActivityForResult(cameraIntent, requestCodeCamera);
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
                        ((Activity) context).startActivityForResult(cameraIntent, requestCodeCamera);
                    }
                } else if (items[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    ((Activity) context).startActivityForResult(Intent.createChooser(intent, "Select File"), requestCodeGallery);
                } else if (items[item].equals("Remove Image")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public static void ShowDatePickerDialog(final EditText txtView, Context context, final boolean IsPreventPreviousDateRequest) {
        final Calendar c = Calendar.getInstance();

        if (!txtView.getText().toString().equals("")) {
            SimpleDateFormat sdfControl = new SimpleDateFormat(DateFormat, Locale.US);
            try {
                Date dt = sdfControl.parse(String.valueOf(txtView.getText()));
                c.setTime(dt);
            } catch (ParseException ignored) {
                ignored.printStackTrace();
                Log.e("dateexception", " " + ignored.getMessage());
            }
        }

        y = c.get(Calendar.YEAR);
        M = c.get(Calendar.MONTH);
        d = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dp = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        Calendar calendar = Calendar.getInstance();

                        Date date = new Date(view.getMinDate());
                        calendar.setTime(date);

                        y = year;
                        M = monthOfYear;
                        d = dayOfMonth;

                        Calendar cal = Calendar.getInstance();
                        cal.set(Calendar.YEAR, year);
                        cal.set(Calendar.MONTH, monthOfYear);
                        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        cal.set(Calendar.HOUR_OF_DAY, 0);
                        cal.set(Calendar.MINUTE, 0);
                        cal.set(Calendar.SECOND, 0);
                        cal.set(Calendar.MILLISECOND, 0);

                        SimpleDateFormat sdfControl = new SimpleDateFormat(DateFormat, Locale.US);
                        if (IsPreventPreviousDateRequest) {
                            if (d >= calendar.get(Calendar.DAY_OF_MONTH) || M >= calendar.get(Calendar.MONTH) || y >= calendar.get(Calendar.YEAR)) {
                                txtView.setText(sdfControl.format(cal.getTime()));
                            } else {
                                txtView.setText(sdfControl.format(new Date()));
                            }
                        } else {
                            txtView.setText(sdfControl.format(cal.getTime()));
                        }
                    }

                }, y, M, d);
        if (IsPreventPreviousDateRequest) {
            dp.getDatePicker().setMinDate(System.currentTimeMillis() - 10000);
        }
        dp.hide();
        dp.show();
    }

    public static void ContactSave(Activity context, MemberMaster objMemberMaster) {
//        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
//
//        ops.add(ContentProviderOperation.newInsert(
//                ContactsContract.RawContacts.CONTENT_URI)
//                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
//                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
//                .build()
//        );
//
//        //------------------------------------------------------ Names
//        if (objMemberMaster.getMemberName() != null && !objMemberMaster.getMemberName().equals("")) {
//            ops.add(ContentProviderOperation.newInsert(
//                    ContactsContract.Data.CONTENT_URI)
//                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
//                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
//                    .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, objMemberMaster.getMemberName()).build()
//            );
//        }
//
//        //------------------------------------------------------ Mobile Number
//        if (objMemberMaster.getPhone1() != null && !objMemberMaster.getPhone1().equals("")) {
//            ops.add(ContentProviderOperation.
//                    newInsert(ContactsContract.Data.CONTENT_URI)
//                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
//                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
//                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, "+91" + objMemberMaster.getPhone1())
//                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
//                    .build()
//            );
//        }
//        if (objMemberMaster.getPhone2() != null && !objMemberMaster.getPhone2().equals("")) {
//            ops.add(ContentProviderOperation.
//                    newInsert(ContactsContract.Data.CONTENT_URI)
//                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
//                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
//                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, "+91" + objMemberMaster.getPhone2())
//                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
//                    .build()
//            );
//        }
//
//        //------------------------------------------------------ Home Numbers
//        if (objMemberMaster.getHomePhone() != null && !objMemberMaster.getHomePhone().equals("")) {
//            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
//                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
//                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
//                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, "+91" + objMemberMaster.getHomePhone())
//                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_HOME)
//                    .build());
//        }
//
//        //------------------------------------------------------ Work Numbers
//        if (objMemberMaster.getOfficePhone() != null && !objMemberMaster.getOfficePhone().equals("")) {
//            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
//                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
//                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
//                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, "+91" + objMemberMaster.getOfficePhone())
//                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_WORK)
//                    .build());
//        }
//
//        //------------------------------------------------------ Email
//        if (objMemberMaster.getEmail() != null && !objMemberMaster.getEmail().equals("")) {
//            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
//                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
//                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
//                    .withValue(ContactsContract.CommonDataKinds.Email.DATA, objMemberMaster.getEmail())
//                    .withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
//                    .build());
//        }
//
        //------------------------------------------------------ Organization
//        if(objMemberMaster.getProfession()!=null && !objMemberMaster.getProfession().equals(""))
//        {
//            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
//                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
//                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE)
////                        .withValue(ContactsContract.CommonDataKinds.Organization.COMPANY, company)
////                        .withValue(ContactsContract.CommonDataKinds.Organization.TYPE, ContactsContract.CommonDataKinds.Organization.TYPE_WORK)
//                    .withValue(ContactsContract.CommonDataKinds.Organization.TITLE, objMemberMaster.getProfession())
//                    .withValue(ContactsContract.CommonDataKinds.Organization.TYPE, ContactsContract.CommonDataKinds.Organization.TYPE_WORK)
//                    .build());
//        }
        try {
            Intent contactIntent = new Intent(ContactsContract.Intents.Insert.ACTION);
            contactIntent.setType(ContactsContract.RawContacts.CONTENT_TYPE);

            if (objMemberMaster.getMemberName() != null && !objMemberMaster.getMemberName().equals("")) {
                contactIntent.putExtra(ContactsContract.Intents.Insert.NAME, objMemberMaster.getMemberName());
            }
            if (objMemberMaster.getPhone1() != null && !objMemberMaster.getPhone1().equals("")) {
                contactIntent.putExtra(ContactsContract.Intents.Insert.PHONE,"+91"+ objMemberMaster.getPhone1());
            }
            if (objMemberMaster.getHomePhone() != null && !objMemberMaster.getPhone2().equals("")) {
                contactIntent.putExtra(ContactsContract.Intents.Insert.SECONDARY_PHONE,"+91"+ objMemberMaster.getPhone2());
                contactIntent.putExtra(ContactsContract.Intents.Insert.SECONDARY_PHONE_TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_HOME);
            }
            if (objMemberMaster.getOfficePhone() != null && !objMemberMaster.getOfficePhone().equals("")) {
                contactIntent.putExtra(ContactsContract.Intents.Insert.TERTIARY_PHONE,"+91"+ objMemberMaster.getOfficePhone());
                contactIntent.putExtra(ContactsContract.Intents.Insert.TERTIARY_PHONE_TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_WORK);
            }
            if (objMemberMaster.getEmail() != null && !objMemberMaster.getEmail().equals("")) {
                contactIntent.putExtra(ContactsContract.Intents.Insert.EMAIL, objMemberMaster.getEmail());
                contactIntent.putExtra(ContactsContract.Intents.Insert.EMAIL_TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK);
            }
           context.startActivityForResult(contactIntent, 1);
//            context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
//            context.getContentResolver().applyBatch(ContactsContract., ops);
//            Log.e("contact saved", " ");
//            Toast.makeText(context, "Contact saved successfully.", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            //  Toast.makeText(myContext, "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    //region Enum

    public enum MemberType {
        Admin(1, "Admin"),
        User(2, "User");

        private int value;
        private String memberType;

        MemberType(int value, String memberType) {
            this.value = value;
            this.memberType = memberType;
        }

        public int getValue() {
            return value;
        }

        public String getMemberType() {
            return memberType;
        }

    }

    public enum BloodGroup {
        AB1(0, "AB+"),
        AB2(1, "AB-"),
        A1(2, "A+"),
        A2(3, "A-"),
        B1(4, "B+"),
        B2(5, "B-"),
        O1(6, "O+"),
        O2(7, "O-");

        private int value;
        private String bloodGroup;

        BloodGroup(int value, String bloodGroup) {
            this.value = value;
            this.bloodGroup = bloodGroup;
        }

        public static String getBlood(int id) {
            for (BloodGroup bloodGroup : BloodGroup.values()) {
                if (bloodGroup.getValue() == id) {
                    return bloodGroup.getBloodGroup();
                }
            }
            return null;
        }

        public static int getBloodPosition(String blood) {
            for (BloodGroup bloodGroup : BloodGroup.values()) {
                if (bloodGroup.getBloodGroup().equals(blood)) {
                    return bloodGroup.getValue();
                }
            }
            return 0;
        }

        public int getValue() {
            return value;
        }

        public String getBloodGroup() {
            return bloodGroup;
        }

    }

    //endregion

}
