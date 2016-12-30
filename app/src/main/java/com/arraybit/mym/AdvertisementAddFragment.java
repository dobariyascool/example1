package com.arraybit.mym;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.arraybit.global.Globals;
import com.arraybit.global.MarshMallowPermission;
import com.arraybit.global.Service;
import com.arraybit.modal.AdvertiseMaster;
import com.arraybit.parser.AdvertiseJSONParser;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.rey.material.widget.Button;
import com.rey.material.widget.EditText;
import com.rey.material.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class AdvertisementAddFragment extends Fragment implements View.OnClickListener, AdvertiseJSONParser.AdvertiseAddListener {

    final int PIC_CROP = 1;
    EditText etMessage, etWebSiteLink;
    ImageView ivImage;
    TextView txtImage;
    Button btnAddNotification;
    LinearLayout llAdvertiseAdd, llAdvertiseText;
    RelativeLayout rladdImage, rlAdImage;
    String imagePhysicalNameBytes, imageName, strImageName, picturePath = "", src;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss.SSS", Locale.US);
    ProgressDialog progressDialog = new ProgressDialog();
    BroadCastListener broadCastListener;
    Context context;
    RadioGroup rgMain;
    RadioButton rbText, rbImage;
    boolean isUpdate = false, isRemove = false;
    AdvertiseMaster objAdvertiseMaster;
    Bitmap bitmap;

    public AdvertisementAddFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_advertisement_add, container, false);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.app_bar);
        if (toolbar != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setTitle(getActivity().getResources().getString(R.string.advertisement_add_title));
        }
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            MarshMallowPermission marshMallowPermission = new MarshMallowPermission(getActivity());
            if (!marshMallowPermission.checkPermissionForExternalStorage()) {
                marshMallowPermission.requestPermissionForExternalStorage();
            }
        }
        setHasOptionsMenu(true);

        Bundle bundle = getArguments();
        if (bundle != null) {
            isUpdate = bundle.getBoolean("isUpdate", false);
            objAdvertiseMaster = bundle.getParcelable("AdvertiseMaster");
        }
        etMessage = (EditText) view.findViewById(R.id.etMessage);
        etWebSiteLink = (EditText) view.findViewById(R.id.etWebSiteLink);
        txtImage = (TextView) view.findViewById(R.id.txtImage);
        ivImage = (ImageView) view.findViewById(R.id.ivImage);
        llAdvertiseAdd = (LinearLayout) view.findViewById(R.id.llAdvertiseAdd);
        llAdvertiseText = (LinearLayout) view.findViewById(R.id.llAdvertiseText);
        rladdImage = (RelativeLayout) view.findViewById(R.id.rladdImage);
        rlAdImage = (RelativeLayout) view.findViewById(R.id.rlAdImage);
        btnAddNotification = (Button) view.findViewById(R.id.btnAddNotification);
        //end

        //Radiogroup
        rgMain = (RadioGroup) view.findViewById(R.id.rgMain);
        //

        //RadioButton
        rbText = (RadioButton) view.findViewById(R.id.rbText);
        rbImage = (RadioButton) view.findViewById(R.id.rbImage);
        //end

        //button
        btnAddNotification.setOnClickListener(this);
        ivImage.setOnClickListener(this);

        etWebSiteLink.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View editText, boolean hasFocus) {
                if (hasFocus) {
                    etWebSiteLink.setSelection(etWebSiteLink.getText().length());
                }
            }
        });

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

        rgMain.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbText) {
                    llAdvertiseText.setVisibility(View.VISIBLE);
                    txtImage.setVisibility(View.GONE);
                    rlAdImage.setVisibility(View.GONE);
                } else if (checkedId == R.id.rbImage) {
                    llAdvertiseText.setVisibility(View.GONE);
                    txtImage.setVisibility(View.VISIBLE);
                    rlAdImage.setVisibility(View.VISIBLE);
                }
            }
        });

        if (isUpdate && objAdvertiseMaster != null) {
            toolbar.setTitle(getActivity().getResources().getString(R.string.advertisement_update_title));
            btnAddNotification.setText("Update");
            SetData();
        }

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
            getActivity().getSupportFragmentManager().popBackStack();
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
                if (isUpdate) {
                    UpdateAdvertiseRequest();
                } else {
                    broadCastListener = (BroadCastListener) getActivity();
                    broadCastListener.BroadCastOnclick();
                }
            }
        } else if (v.getId() == R.id.ivImage) {
            Globals.HideKeyBoard(getActivity(), v);
            SelectImageAd(getActivity(), 101);
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
    public void AdvertiseAddResponse(String errorCode, AdvertiseMaster objAdvertiseMaster) {
        progressDialog.dismiss();
        if (!errorCode.equals("-1")) {
            getActivity().getSupportFragmentManager().popBackStack();
        } else {
            Toast.makeText(getActivity(), "Advertise can't add, try again later.", Toast.LENGTH_SHORT).show();
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
                imageName = "Ad_" + String.valueOf(millis) + ".jpg";
                Log.e("image", " " + imageName);
                ivImage.setImageBitmap(selectedBitmap);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 50, bos);
                byte[] bytedata = bos.toByteArray();
                imagePhysicalNameBytes = Base64.encodeToString(bytedata, Base64.DEFAULT);
                return;
            }
        }
    }

    public void AddAdvertise() {
        if (Service.CheckNet(getActivity())) {
//            if (isUpdate) {
//                UpdateAdvertiseRequest();
//            } else {
                AddAdvertiseRequest();
//            }
        } else {
            Globals.ShowSnackBar(llAdvertiseAdd, getResources().getString(R.string.MsgCheckConnection), getActivity(), 1000);
        }
    }

    //region Private Methods and Interface
    private void AddAdvertiseRequest() {
        progressDialog.show(getActivity().getSupportFragmentManager(), "");

        try {
            AdvertiseJSONParser objAdvertiseJSONParser = new AdvertiseJSONParser();
            AdvertiseMaster objAdvertiseMaster = new AdvertiseMaster();
            objAdvertiseMaster.setWebsiteURL(etWebSiteLink.getText().toString());
            objAdvertiseMaster.setIsDeleted(false);
            objAdvertiseMaster.setIsEnabled(true);
            objAdvertiseMaster.setlinktoMemberMasterIdCreatedBy(Globals.memberMasterId);
            if (rbText.isChecked()) {
                objAdvertiseMaster.setAdvertiseText(etMessage.getText().toString());
                objAdvertiseMaster.setAdvertisementType("Text");
            } else if (rbImage.isChecked()) {
                if (imageName != null && !imageName.equals("")) {
//                strImageName = imageName.substring(0, imageName.lastIndexOf(".")) + "_" + simpleDateFormat.format(new Date()) + imageName.substring(imageName.lastIndexOf("."), imageName.length());
                    strImageName = imageName;
                    objAdvertiseMaster.setAdvertiseImageName(strImageName);
                    objAdvertiseMaster.setAdvertiseImageNameBytes(imagePhysicalNameBytes);
                }
//                if (objAdvertiseMaster.getAdvertiseImageName() == null && !isRemove && bitmap != null) {
//                    if (this.objAdvertiseMaster != null && this.objAdvertiseMaster.getAdvertiseImageName() != null ) {
//                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
//                        byte[] bytedata = bos.toByteArray();
//                        objAdvertiseMaster.setAdvertiseImageNameBytes(Base64.encodeToString(bytedata, Base64.DEFAULT));
//                        long millis = System.currentTimeMillis();
//                        objAdvertiseMaster.setAdvertiseImageName("Ad_" + String.valueOf(millis) + ".jpg");
//                    }
//                }
                objAdvertiseMaster.setAdvertisementType("Image");
            }
            objAdvertiseJSONParser.InsertAdvertiseMaster(objAdvertiseMaster, getActivity(), this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void UpdateAdvertiseRequest() {
        progressDialog.show(getActivity().getSupportFragmentManager(), "");

        try {
            AdvertiseJSONParser objAdvertiseJSONParser = new AdvertiseJSONParser();
            AdvertiseMaster objAdvertiseMaster = new AdvertiseMaster();
            objAdvertiseMaster.setWebsiteURL(etWebSiteLink.getText().toString());
            objAdvertiseMaster.setAdvertiseMasterId(this.objAdvertiseMaster.getAdvertiseMasterId());
            objAdvertiseMaster.setlinktoMemberMasterIdUpdatedBy(Globals.memberMasterId);
            if (rbText.isChecked()) {
                objAdvertiseMaster.setAdvertiseText(etMessage.getText().toString());
                objAdvertiseMaster.setAdvertisementType("Text");
            } else if (rbImage.isChecked()) {
                if (imageName != null && !imageName.equals("")) {
//                strImageName = imageName.substring(0, imageName.lastIndexOf(".")) + "_" + simpleDateFormat.format(new Date()) + imageName.substring(imageName.lastIndexOf("."), imageName.length());
                    strImageName = imageName;
                    objAdvertiseMaster.setAdvertiseImageName(strImageName);
                    objAdvertiseMaster.setAdvertiseImageNameBytes(imagePhysicalNameBytes);
                }
                if (objAdvertiseMaster.getAdvertiseImageName() == null && !isRemove && bitmap != null) {
                    if (this.objAdvertiseMaster != null && this.objAdvertiseMaster.getAdvertiseImageName() != null) {
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                        byte[] bytedata = bos.toByteArray();
                        objAdvertiseMaster.setAdvertiseImageNameBytes(Base64.encodeToString(bytedata, Base64.DEFAULT));
                        long millis = System.currentTimeMillis();
                        objAdvertiseMaster.setAdvertiseImageName("Ad_" + String.valueOf(millis) + ".jpg");
                    }
                }
                objAdvertiseMaster.setAdvertisementType("Image");
            }
            objAdvertiseJSONParser.UpdateAdvertiseMaster(objAdvertiseMaster, getActivity(), this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean ValidateControls() {
        boolean IsValid, IsValidWeb;

        if (etWebSiteLink.getText().toString().equals("") || etWebSiteLink.getText().toString().equals("https://")) {
            etWebSiteLink.setError("Enter URL");
            IsValidWeb = false;
        } else {
            etWebSiteLink.clearError();
            IsValidWeb = true;
        }

        if (rbText.isChecked()) {
            if (etMessage.getText().toString().equals("")) {
                etMessage.setError("Enter Message");
                IsValid = false;
            } else {
                etMessage.clearError();
                IsValid = true;
            }
        } else if (rbImage.isChecked()) {
            if (ivImage.getDrawable() != null) {
                IsValid = true;
            } else {
                Globals.ShowSnackBar(llAdvertiseAdd, "Add image", getActivity(), 1000);
                IsValid = false;
            }
        } else {
            IsValid = false;
        }


        return (IsValid && IsValidWeb);
    }

    private void performCrop(Uri picUri) {
        try {

            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
            // set crop properties
            cropIntent.putExtra("crop", "true");
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 7);
            cropIntent.putExtra("aspectY", 1);
            // indicate output X and Y
            cropIntent.putExtra("outputY", 77);
            cropIntent.putExtra("outputX", 540);
            // retrieve data on return
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            getActivity().startActivityForResult(cropIntent, PIC_CROP);
        }
        // respond to users whose devices do not support the crop action
        catch (Exception anfe) {
            // display an error message
            String errorMessage = "your device doesn't support the crop action!";
            Toast toast = Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void SetData() {
        if (objAdvertiseMaster != null) {
            if (objAdvertiseMaster.getAdvertisementType() != null && objAdvertiseMaster.getAdvertisementType().equals("Text")) {
                rbText.setChecked(true);
                if (objAdvertiseMaster.getWebsiteURL() != null && !objAdvertiseMaster.getWebsiteURL().equals("")) {
                    etWebSiteLink.setText(objAdvertiseMaster.getWebsiteURL());
                }
                if (objAdvertiseMaster.getAdvertiseText() != null && !objAdvertiseMaster.getAdvertiseText().equals("")) {
                    etMessage.setText(objAdvertiseMaster.getAdvertiseText());
                }
            } else if (objAdvertiseMaster.getAdvertisementType() != null && objAdvertiseMaster.getAdvertisementType().equals("Image")) {
                rbImage.setChecked(true);
                if (objAdvertiseMaster.getWebsiteURL() != null && !objAdvertiseMaster.getWebsiteURL().equals("")) {
                    etWebSiteLink.setText(objAdvertiseMaster.getWebsiteURL());
                }
                if (objAdvertiseMaster.getAdvertiseImageName() != null && !objAdvertiseMaster.getAdvertiseImageName().equals("")) {
                    Glide.with(getActivity()).load(objAdvertiseMaster.getAdvertiseImageName()).asBitmap()
                            .diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(ivImage);
                    src = objAdvertiseMaster.getAdvertiseImageName();
                    rladdImage.setVisibility(View.GONE);
                    new LoadImage().execute();
                } else {
                    ivImage.setImageDrawable(null);
                    rladdImage.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private void SelectImageAd(final Context context, final int requestCodeGallery) {
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
//                    try {
//                        Intent pickImageIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                        File mediaStorageDir = new File(
//                                Environment.getExternalStorageDirectory()
//                                        + File.separator
//                                        + getString(R.string.directory_name_corp_chat)
//                                        + File.separator
//                                        + getString(R.string.directory_name_images));
//
//                        if (!mediaStorageDir.exists()) {
//                            mediaStorageDir.mkdirs();
//                        }
//
//                        pickImageIntent.setType("image/*");
//                        pickImageIntent.putExtra("crop", "true");
//                        pickImageIntent.putExtra("outputX", 900);
//                        pickImageIntent.putExtra("outputY", 120);
//                        pickImageIntent.putExtra("aspectX", 7.5);
//                        pickImageIntent.putExtra("aspectY", 1);
//                        pickImageIntent.putExtra("scale", true);
//                        File mediaFile = File.createTempFile(
//                                "Ad_" + String.valueOf(System.currentTimeMillis()), ".jpg", mediaStorageDir);
//                        pickImageIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mediaFile));
//                        pickImageIntent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
//                        ((Activity) context).startActivityForResult(pickImageIntent, PIC_CROP);
//                    }catch(Exception e)
//                    {
//                        e.printStackTrace();
//                    }
                } else if (items[item].equals("Remove Image")) {
                    isRemove = true;
                    ivImage.setImageDrawable(null);
                    rladdImage.setVisibility(View.VISIBLE);
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public interface BroadCastListener {
        void BroadCastOnclick();
    }

    private class LoadImage extends AsyncTask {


        @Override
        protected Object doInBackground(Object[] params) {
            try {
                URL url = new URL(src);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                bitmap = BitmapFactory.decodeStream(input);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

    }
    //endregion

}
