package com.arraybit.mym;


import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.arraybit.global.Globals;
import com.arraybit.global.MarshMallowPermission;
import com.arraybit.global.Service;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.modal.NotificationMaster;
import com.arraybit.parser.NotificationJSONParser;
import com.rey.material.widget.Button;
import com.rey.material.widget.EditText;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationAddFragment extends Fragment implements View.OnClickListener, NotificationJSONParser.NotificationAddListener {

    final int PIC_CROP = 1;
    EditText etTitle, etMessage;
    ImageView ivImage;
    Button btnAddNotification;
    LinearLayout llNotificationAdd;
    RelativeLayout rladdImage;
    String imagePhysicalNameBytes, imageName, strImageName;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss.SSS", Locale.US);
    ProgressDialog progressDialog = new ProgressDialog();
    int notificationMasterId, customerType;
    BroadCastListener broadCastListener;
    Context context;
    String picturePath = "";

    public NotificationAddFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_notification_add, container, false);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.app_bar);
        if (toolbar != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setTitle(getActivity().getResources().getString(R.string.notification_add_title));
        }
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            MarshMallowPermission marshMallowPermission = new MarshMallowPermission(getActivity());
            if (!marshMallowPermission.checkPermissionForExternalStorage()) {
                marshMallowPermission.requestPermissionForExternalStorage();
            }
        }
        setHasOptionsMenu(true);
        etTitle = (EditText) view.findViewById(R.id.etTitle);
        etMessage = (EditText) view.findViewById(R.id.etMessage);
        ivImage = (ImageView) view.findViewById(R.id.ivImage);
        llNotificationAdd = (LinearLayout) view.findViewById(R.id.llNotificationAdd);
        rladdImage = (RelativeLayout) view.findViewById(R.id.rladdImage);
        btnAddNotification = (Button) view.findViewById(R.id.btnAddNotification);

        btnAddNotification.setOnClickListener(this);
        ivImage.setOnClickListener(this);

        etMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etMessage.getText().toString().length() > 150) {
                    etMessage.setText(etMessage.getText().toString().substring(0, etMessage.getText().toString().length() - 1));
                }
            }
        });

        etTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etTitle.getText().toString().length() > 35) {
                    etTitle.setText(etTitle.getText().toString().substring(0, etTitle.getText().toString().length() - 1));
                }
            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.add).setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Globals.HideKeyBoard(getActivity(), getView());
            getActivity().getSupportFragmentManager().popBackStack(getResources().getString(R.string.notification_add_fragment), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnAddNotification) {
            Globals.HideKeyBoard(getActivity(), v);
            if (!ValidateControls()) {
                Globals.ShowSnackBar(v, getResources().getString(R.string.MsgValidation), getActivity(), 1000);
            } else {
                broadCastListener = (BroadCastListener) getActivity();
                broadCastListener.BroadCastOnclick();
            }
        } else if (v.getId() == R.id.ivImage) {
            Globals.HideKeyBoard(getActivity(), v);
            SelectImageNotification(getActivity(),101);
//            Globals.SelectImage(getActivity(), 100, 101);
//        }else if (v.getId() == R.id.ivCancleImage) {
//            Globals.HideKeyBoard(getActivity(), v);
////            Globals.SelectImage(getActivity(), this, 100, 101);
//            ivImage.setImageBitmap(null);
//            txtImageName.setText("Image Name");
//            rladdImage.setVisibility(View.VISIBLE);
////            rlCancleImage.setVisibility(View.GONE);
        }
    }

    @Override
    public void NotificationAddResponse(String errorCode, NotificationMaster notificationMaster) {
        progressDialog.dismiss();
        if (!errorCode.equals("-1")) {
            Toast.makeText(getActivity(), "Notification sent.", Toast.LENGTH_SHORT).show();
            getActivity().getSupportFragmentManager().popBackStack();
            if (notificationMaster != null) {
                notificationMasterId = notificationMaster.getNotificationMasterId();
                new NotificationSendTask().execute();
            }
        } else {
            Toast.makeText(getActivity(), "Notification not send.", Toast.LENGTH_SHORT).show();
        }
    }

    public void AddNotification() {
        if (Service.CheckNet(getActivity())) {
            AddNotificationRequest();
        } else {
            Globals.ShowSnackBar(llNotificationAdd, getResources().getString(R.string.MsgCheckConnection), getActivity(), 1000);
        }
    }

    public void SelectImage(int requestCode, Intent data) {
//        if (requestCode == 100) {
//            strImageName = String.valueOf(System.currentTimeMillis()) +"_"+Globals.memberMasterId + ".jpg";
//            File file = new File(android.os.Environment.getExternalStorageDirectory(), strImageName);
//            picturePath = file.getAbsolutePath();
//            File f1 = new File(picturePath);
//            Uri contentUri = Uri.fromFile(f1);
//            performCrop(contentUri);
//        } else
        if (requestCode == 101 && data != null && data.getData() != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            cursor.close();
            File f = new File(picturePath);
            Uri contentUri = Uri.fromFile(f);
            performCrop(contentUri);
        } else if (requestCode == PIC_CROP) {
            if (data != null) {
                // get the returned data
                Bundle extras = data.getExtras();
                // get the cropped bitmap
                Bitmap selectedBitmap = extras.getParcelable("data");

                rladdImage.setVisibility(View.GONE);
//                ivImage.setVisibility(View.VISIBLE);
                long millis = System.currentTimeMillis();
                imageName = "Notification_"+String.valueOf(millis) + ".jpg";
//                imageName = "Notification_"+String.valueOf(millis) + ".jpg" + MimeTypeMap.getFileExtensionFromUrl(picturePath);
                ivImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                ivImage.setImageBitmap(selectedBitmap);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
                byte[] bytedata = bos.toByteArray();
                imagePhysicalNameBytes = Base64.encodeToString(bytedata, Base64.DEFAULT);
                return;
            }
        }
    }

    //region Private Methods and Interface
    private void AddNotificationRequest() {
        progressDialog.show(getActivity().getSupportFragmentManager(), "");

        try {
            NotificationJSONParser objNotificationJSONParser = new NotificationJSONParser();
            NotificationMaster objNotificationMaster = new NotificationMaster();
            objNotificationMaster.setNotificationTitle(etTitle.getText().toString());
            objNotificationMaster.setNotificationText(etMessage.getText().toString());
            objNotificationMaster.setIsDeleted(false);
            objNotificationMaster.setlinktoMemberMasterIdCreatedBy(Globals.memberMasterId);

            if (imageName != null && !imageName.equals("")) {
//                strImageName = imageName.substring(0, imageName.lastIndexOf(".")) + "_" + simpleDateFormat.format(new Date()) + imageName.substring(imageName.lastIndexOf("."), imageName.length());
                strImageName = imageName;
                objNotificationMaster.setNotificationImageName(strImageName);
                objNotificationMaster.setNotificationImageNameBytes(imagePhysicalNameBytes);
            }
            objNotificationJSONParser.InsertNotificationMaster(objNotificationMaster, getActivity(), this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean ValidateControls() {
        boolean IsValid;

        if (etTitle.getText().toString().equals("") && etTitle.getText().toString().equals("")) {
            etTitle.setError("Enter Title");
            etMessage.setError("Enter Message");
            IsValid = false;
        } else if (etMessage.getText().toString().equals("") && !etTitle.getText().toString().equals("")) {
            etMessage.setError("Enter Message");
            etTitle.clearError();
            IsValid = false;
        } else if (etTitle.getText().toString().equals("") && !etMessage.getText().toString().equals("")) {
            etTitle.setError("Enter Title");
            etMessage.clearError();
            IsValid = false;
        } else {
            etMessage.clearError();
            etTitle.clearError();
            IsValid = true;
        }

        return IsValid;
    }

    private void SelectImageNotification(final Context context, final int requestCodeGallery) {
        final CharSequence[] items = {"Choose from Gallery", "Remove Image"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialog);
        builder.setTitle("ADD PHOTO");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    ((Activity) context).startActivityForResult(Intent.createChooser(intent, "Select File"), requestCodeGallery);
                } else if (items[item].equals("Remove Image")) {
                    ivImage.setImageDrawable(null);
                    rladdImage.setVisibility(View.VISIBLE);
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void performCrop(Uri picUri) {
        try {

            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
            // set crop properties
            cropIntent.putExtra("crop", "true");
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 2);
            cropIntent.putExtra("aspectY", 1);
            // indicate output X and Y
            cropIntent.putExtra("outputX", 420);
            cropIntent.putExtra("outputY", 210);
            // retrieve data on return
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            getActivity().startActivityForResult(cropIntent, PIC_CROP);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            // display an error message
            String errorMessage = "your device doesn't support the crop action!";
            Toast toast = Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public interface BroadCastListener {
        void BroadCastOnclick();
    }
    //endregion

    //region LoadingTask
    class NotificationSendTask extends AsyncTask {
        public String SendNotificationsToAll = "SendNotificationsToAll";

        @Override
        protected Object doInBackground(Object[] objects) {
            Service.HttpGetService(Service.Url + this.SendNotificationsToAll + "/" + notificationMasterId);
            return null;
        }

    }

    //endregion

}
