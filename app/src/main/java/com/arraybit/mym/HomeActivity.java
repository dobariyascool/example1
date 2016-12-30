package com.arraybit.mym;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.arraybit.adapter.MemberListAdapter;
import com.arraybit.global.EndlessRecyclerOnScrollListener;
import com.arraybit.global.Globals;
import com.arraybit.global.MarshMallowPermission;
import com.arraybit.global.Service;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.modal.AdvertiseMaster;
import com.arraybit.modal.MemberMaster;
import com.arraybit.parser.AdvertiseJSONParser;
import com.arraybit.parser.MemberJSONParser;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.rey.material.widget.Button;
import com.rey.material.widget.CompoundButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

//SearchView.OnQueryTextListener,
public class HomeActivity extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener,
        MemberListAdapter.OnCardClickListener, View.OnClickListener, MemberJSONParser.MembersListRequestListener, MemberJSONParser.MemberRequestListener,
        AdvertiseJSONParser.AdvretiseRequestListener, FilterFragment.SelectFilterListerner, ConfirmDialog.ConfirmationResponseListener {

    private static final long delay = 2000L;
    public static boolean isHome = true;
    ArrayList<MemberMaster> alMemberList = new ArrayList<>();
    ArrayList<MemberMaster> lstMemberList = new ArrayList<>();
    NavigationView navigationView;
    DrawerLayout drawer;
    ImageView imageView, ivAdvertise;
    TextView txtFullName, txtAdvertise, cbName;
    LinearLayout nameLayout, errorLayout, internetLayout, approvedLayout, homeLayout;
    RecyclerView rvContactList;
    MemberListAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    MenuItem searchItem;
    DisplayMetrics displayMetrics;
    String searchText, isAdmin = "";
    boolean isAdvertise = true, isValid = true, isFilter, isLogout = false, isPrint = false, isSave = false, isDelete = false;
    ProgressDialog progressDialog = new ProgressDialog();
    int CurrentPage = 1, CurrentPageAdvertise = 1, position, pagesize=25;
    MemberMaster objMemberMaster, objMember;
    Toast toast = null;
    Timer timer = new Timer();
    AdvertiseMaster objAdvertiseMaster;
    AdvertiseJSONParser objAdvertiseJSONParser = new AdvertiseJSONParser();
    SharePreferenceManage objSharePreferenceManage = new SharePreferenceManage();
    String name = "", designation = "", qualification = "", bloodGroup = "";
    private Handler mExitHandler = new Handler();
    private boolean mRecentlyBackPressed = false;
    private Runnable mExitRunnable = new Runnable() {

        @Override
        public void run() {
            mRecentlyBackPressed = false;
        }
    };
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        try {
            Toolbar app_bar = (Toolbar) findViewById(R.id.app_bar);
            setSupportActionBar(app_bar);
            if (getSupportActionBar() != null) {
                if (Build.VERSION.SDK_INT >= 21) {
                    app_bar.setElevation(getResources().getDimension(R.dimen.app_bar_elevation));
                }
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setLogo(R.drawable.mandal_toolbar);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                MarshMallowPermission objMarshMallowPermission = new MarshMallowPermission(HomeActivity.this);
                if (!objMarshMallowPermission.checkPermissionForRecord()) {
                    objMarshMallowPermission.requestPermissionForRecord();
                }
            }

            displayMetrics = getResources().getDisplayMetrics();
            toast = Toast.makeText(HomeActivity.this, "Press again to exit", Toast.LENGTH_SHORT);

            isHome = true;

            Intent intent = getIntent();
            objMemberMaster = intent.getParcelableExtra("memberMaster");

            errorLayout = (LinearLayout) findViewById(R.id.errorLayout);
            internetLayout = (LinearLayout) findViewById(R.id.internetLayout);
            approvedLayout = (LinearLayout) findViewById(R.id.approvedLayout);
            Button btnRetry = (Button) internetLayout.findViewById(R.id.btnRetry);
            CompoundButton cbSignIn = (CompoundButton) approvedLayout.findViewById(R.id.cbSignIn);
            homeLayout = (LinearLayout) findViewById(R.id.homeLayout);

            rvContactList = (RecyclerView) findViewById(R.id.rvContactList);
            ivAdvertise = (ImageView) findViewById(R.id.ivAdvertise);
            txtAdvertise = (TextView) findViewById(R.id.txtAdvertise);
            linearLayoutManager = new LinearLayoutManager(HomeActivity.this);
            ivAdvertise.setOnClickListener(this);
            txtAdvertise.setOnClickListener(this);
            btnRetry.setOnClickListener(this);
            cbSignIn.setOnClickListener(this);

            View headerView = LayoutInflater.from(HomeActivity.this).inflate(R.layout.navigation_header, null);
            cbName = (TextView) headerView.findViewById(R.id.cbName);
            imageView = (ImageView) headerView.findViewById(R.id.imageView);
            nameLayout = (LinearLayout) headerView.findViewById(R.id.nameLayout);
            txtFullName = (TextView) headerView.findViewById(R.id.txtFullName);

            navigationView = (NavigationView) findViewById(R.id.naviViewHome);
            navigationView.setNavigationItemSelectedListener(this);
            navigationView.addHeaderView(headerView);
            nameLayout.setOnClickListener(this);
//            navigationView.addHeaderView(headerView);
            drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, app_bar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            rvContactList.addOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager) {
                @Override
                public void onLoadMore(int current_page) {
                    if (current_page > CurrentPage) {
                        CurrentPage = current_page;
                        if (Service.CheckNet(HomeActivity.this)) {
                            RequestMemberMaster(pagesize);
                        } else {
                            Toast.makeText(HomeActivity.this, getResources().getString(R.string.MsgCheckConnection), Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });

            if (Service.CheckNet(this)) {
                internetLayout.setVisibility(View.GONE);
                homeLayout.setVisibility(View.VISIBLE);
                if (objMemberMaster != null) {
                    SetUserName();

                    if (objMemberMaster.getIsApproved()) {
                        isValid = true;
                        if (Service.CheckNet(HomeActivity.this)) {
                            RequestMemberMaster(pagesize);
                        } else {
                            Toast.makeText(HomeActivity.this, getResources().getString(R.string.MsgCheckConnection), Toast.LENGTH_LONG).show();
                        }
                    } else {
//                        if (objMemberMaster.getProfession() != null && !objMemberMaster.getProfession().equals("")) {
//                            if (objMemberMaster.getHomeNumberStreet() != null && !objMemberMaster.getHomeNumberStreet().equals("")) {
                        isValid = false;
                        Globals.SetErrorLayout(approvedLayout, true, getResources().getString(R.string.MsgInvalidUser), rvContactList, 0);
//                            } else {
//                                Globals.ChangeActivity(HomeActivity.this, RegistartionFragmentActivity.class, true);
//                            }
//                        } else {
//                            Globals.ChangeActivity(HomeActivity.this, RegistartionFragmentActivity.class, true);
//                        }
                    }
                } else {
                    RequestMemberByMemberMasterId();
                }
                SetAdvertise();
            } else {
                internetLayout.setVisibility(View.VISIBLE);
                Globals.SetErrorLayout(internetLayout, true, getResources().getString(R.string.MsgCheckConnection), null, R.drawable.wifi_off);
                homeLayout.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_admin_home, menu);
//        searchItem = menu.findItem(R.id.action_search);
//        final SearchView mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
//        mSearchView.setInputType(InputType.TYPE_CLASS_TEXT);
//        mSearchView.setMaxWidth(displayMetrics.widthPixels);
//        mSearchView.setOnQueryTextListener(this);
//        searchText = mSearchView.getQuery().toString();
//
//        MenuItemCompat.setOnActionExpandListener(searchItem,
//                new MenuItemCompat.OnActionExpandListener() {
//                    @Override
//                    public boolean onMenuItemActionCollapse(MenuItem item) {
//                        // Do something when collapsed
//
//                        if (lstMemberList != null && lstMemberList.size() != 0) {
//                            adapter.SetSearchFilter(lstMemberList);
//                            Globals.HideKeyBoard(HomeActivity.this, MenuItemCompat.getActionView(searchItem));
//                        }
//
//                        return true; // Return true to collapse action view
//                    }
//
//                    @Override
//                    public boolean onMenuItemActionExpand(MenuItem item) {
//                        // Do something when expanded
//                        return true; // Return true to expand action view
//                    }
//                });
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        if (Globals.memberType.equals(Globals.MemberType.Admin.getMemberType())) {
//            menu.findItem(R.id.action_search).setVisible(true);
            menu.findItem(R.id.memberRequest).setVisible(true);
            menu.findItem(R.id.notification).setVisible(true);
            menu.findItem(R.id.memberFilter).setVisible(true);
        } else {
//            menu.findItem(R.id.action_search).setVisible(true);
            menu.findItem(R.id.memberRequest).setVisible(false);
            menu.findItem(R.id.notification).setVisible(true);
            menu.findItem(R.id.memberFilter).setVisible(true);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                finish();
                overridePendingTransition(0, R.anim.right_exit);
            }
        } else if (item.getItemId() == R.id.memberRequest) {
            isHome = false;
            Intent intent = new Intent(HomeActivity.this, NewMembersActivity.class);
            startActivityForResult(intent, 130);
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
        } else if (item.getItemId() == R.id.notification) {
            Intent intent = new Intent(HomeActivity.this, NotificationActivity.class);
            startActivityForResult(intent, 120);
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
        } else if (item.getItemId() == R.id.memberFilter) {
            FilterFragment objFilterFragment = new FilterFragment();
            Bundle bundle = new Bundle();
            bundle.putString("name", name);
            bundle.putString("designation", designation);
            bundle.putString("qualification", qualification);
            bundle.putString("bloodGroup", bloodGroup);
            objFilterFragment.setArguments(bundle);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out, 0, R.anim.right_exit);
            fragmentTransaction.replace(R.id.drawer_layout, objFilterFragment, "Filter");
            fragmentTransaction.addToBackStack("Filter");
            fragmentTransaction.commit();
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.nav_logout) {
            isLogout = true;
            drawer.closeDrawer(navigationView);
            RequestMemberMasterLogout();
//            Globals.ClearUserPreference(HomeActivity.this);
//            Intent intent = new Intent(HomeActivity.this, SignInActivity.class);
//            drawer.closeDrawer(navigationView);
//            startActivity(intent);
//            overridePendingTransition(R.anim.right_in, R.anim.left_out);
//            finish();
        } else if (item.getItemId() == R.id.nav_notification) {
            Intent intent = new Intent(HomeActivity.this, NotificationActivity.class);
            drawer.closeDrawer(navigationView);
            startActivityForResult(intent, 120);
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
        } else if (item.getItemId() == R.id.nav_my_account) {
            Intent intent = new Intent(HomeActivity.this, DetailActivity.class);
            intent.putExtra("memberMasterId", Integer.parseInt(objSharePreferenceManage.GetPreference("LoginPreference", "MemberMasterId", HomeActivity.this)));
            drawer.closeDrawer(navigationView);
            startActivityForResult(intent, 110);
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
        } else if (item.getItemId() == R.id.nav_advertisement) {
            Intent intent = new Intent(HomeActivity.this, AdvertisementActivity.class);
            drawer.closeDrawer(navigationView);
            startActivityForResult(intent, 120);
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
        } else if (item.getItemId() == R.id.nav_exit) {
            System.exit(0);
//            createandDisplayPdf();
        } else if (item.getItemId() == R.id.nav_contact_list) {
            isPrint = true;
            RequestMemberMasterForPrint(9999);
        } else if (item.getItemId() == R.id.nav_rate_us) {
            Uri uri = Uri.parse("market://details?id=" + getPackageName());
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            try {
                startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
            }
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == RESULT_OK && requestCode == 130) {
                CurrentPage = 1;
                lstMemberList = new ArrayList<>();
                RequestMemberMaster(pagesize);
            } else if (resultCode == RESULT_OK && requestCode == 110) {
                CurrentPage = 1;
                lstMemberList = new ArrayList<>();
                SetUserName();
                RequestMemberMaster(pagesize);
            } else if (resultCode == RESULT_OK && requestCode == 1) {
                Toast.makeText(HomeActivity.this, "Contact saved successfully.", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.nameLayout) {
            drawer.closeDrawer(navigationView);
            Intent intent = new Intent(HomeActivity.this, DetailActivity.class);
            intent.putExtra("memberMasterId", Integer.parseInt(objSharePreferenceManage.GetPreference("LoginPreference", "MemberMasterId", HomeActivity.this)));
            startActivityForResult(intent, 110);
//            overridePendingTransition(R.anim.right_in, R.anim.left_out);
        } else if (v.getId() == R.id.ivAdvertise || v.getId() == R.id.txtAdvertise) {
            if (objAdvertiseMaster != null) {
                AdvertiseWebViewFragment objAdvertiseWebViewFragment = new AdvertiseWebViewFragment();
                Bundle bundle = new Bundle();
                bundle.putString("url", objAdvertiseMaster.getWebsiteURL());
                objAdvertiseWebViewFragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out, 0, R.anim.right_exit);
                fragmentTransaction.replace(R.id.drawer_layout, objAdvertiseWebViewFragment, "Advertisement");
                fragmentTransaction.addToBackStack("Advertisement");
                fragmentTransaction.commit();
            }
        } else if (v.getId() == R.id.btnRetry) {
            if (Service.CheckNet(HomeActivity.this)) {
                internetLayout.setVisibility(View.GONE);
                homeLayout.setVisibility(View.VISIBLE);
                if (objMemberMaster != null) {
                    SetUserName();
                    if (objMemberMaster.getIsApproved()) {
                        isValid = true;
                        if (Service.CheckNet(HomeActivity.this)) {
                            RequestMemberMaster(pagesize);
                        } else {
                            Toast.makeText(HomeActivity.this, getResources().getString(R.string.MsgCheckConnection), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        isValid = false;
                        Globals.SetErrorLayout(approvedLayout, true, getResources().getString(R.string.MsgInvalidUser), rvContactList, 0);
                    }
                } else {
                    RequestMemberByMemberMasterId();
                }
                SetAdvertise();
            }
        } else if (v.getId() == R.id.cbSignIn) {
//            isLogout= true;
//            RequestMemberMasterLogout();
            Globals.ClearUserPreference(HomeActivity.this);
            Intent intent = new Intent(HomeActivity.this, SignInActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
            finish();
        }
    }

    @Override
    public void OnCardClick(MemberMaster objMemberMaster, String event, int position) {
        this.position = position;
        this.objMember = objMemberMaster;
        isDelete = false;
        if (event == null) {
            Intent intent = new Intent(HomeActivity.this, DetailActivity.class);
            intent.putExtra("isDetail", true);
            intent.putExtra("memberMasterId", objMemberMaster.getMemberMasterId());
            intent.putExtra("memberName", objMemberMaster.getMemberName());
            startActivity(intent);
        } else if (event.equals(getResources().getString(R.string.make_admin))) {
            this.position = position;
            if (progressDialog.isAdded()) {
                progressDialog.dismiss();
            }
            progressDialog.show(getSupportFragmentManager(), "");
            MemberJSONParser objMemeberJSONParser = new MemberJSONParser();
            isAdmin = objMemberMaster.getMemberType().equals("Admin") ? "User" : "Admin";
            objMemeberJSONParser.UpdateMemberMasterAdmin(HomeActivity.this, objMemberMaster.getMemberMasterId(), isAdmin);
        } else if (event.equals(getResources().getString(R.string.call))) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + objMemberMaster.getPhone1()));
            startActivity(intent);
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
        } else if (event.equals(getResources().getString(R.string.message))) {
            Uri uri = Uri.parse("smsto:" + objMemberMaster.getPhone1());
            Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
            startActivity(intent);
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
        } else if (event.equals(getResources().getString(R.string.email))) {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_EMAIL, objMemberMaster.getEmail());
            startActivity(Intent.createChooser(intent, "Send Email"));
        } else if (event.equals(getResources().getString(R.string.detail_save_contact))) {
            isSave = true;
            ConfirmDialog confirmDialog = new ConfirmDialog(true, "Save to contacts?");
            confirmDialog.show(getSupportFragmentManager(), "");
//            Globals.ContactSave(HomeActivity.this, objMemberMaster);
        } else if (event.equals(getResources().getString(R.string.delete_member))) {
            isDelete = true;
            ConfirmDialog confirmDialog = new ConfirmDialog(true, "Delete " + objMemberMaster.getMemberName() + "?");
            confirmDialog.show(getSupportFragmentManager(), "");
//            Globals.ContactSave(HomeActivity.this, objMemberMaster);
        }
    }

    @Override
    public void MemberListResponse(ArrayList<MemberMaster> alMemberMasters, MemberMaster objMemberMaster, String errorCode) {
        progressDialog.dismiss();
        if (isPrint) {
            isPrint = false;
            createandDisplayPdf(alMemberMasters);
        } else {
            SetRecyclerView(alMemberMasters, errorCode);
        }
    }

    @Override
    public void MemberResponse(String errorCode, MemberMaster objMemberMaster) {
//        progressDialog.dismiss();
        this.objMemberMaster = objMemberMaster;
//        if (objMemberMaster != null) {
//            SetUserName();
//            if (objMemberMaster.getProfession() != null && !objMemberMaster.getProfession().equals("")) {
//                if (objMemberMaster.getHomeNumberStreet() != null && !objMemberMaster.getHomeNumberStreet().equals("")) {
//                    if (objMemberMaster.getIsApproved()) {
//                        isValid = true;
//                        if (Service.CheckNet(HomeActivity.this)) {
//                            RequestMemberMaster();
//                        } else {
//                            Toast.makeText(HomeActivity.this, getResources().getString(R.string.MsgCheckConnection), Toast.LENGTH_LONG).show();
//                        }
//                    } else {
//                        isValid = false;
//                        Globals.SetErrorLayout(approvedLayout, true, getResources().getString(R.string.MsgInvalidUser), rvContactList, 0);
//                    }
//                } else {
//                    Globals.ChangeActivity(HomeActivity.this, RegistartionFragmentActivity.class, true);
//                }
//            } else {
//                Globals.ChangeActivity(HomeActivity.this, RegistartionFragmentActivity.class, true);
//            }
//        }
        if (Service.CheckNet(this)) {
            internetLayout.setVisibility(View.GONE);
            homeLayout.setVisibility(View.VISIBLE);
            if (objMemberMaster != null) {
                SetUserName();

                if (objMemberMaster.getIsApproved()) {
                    isValid = true;
                    if (Service.CheckNet(HomeActivity.this)) {
                        RequestMemberMaster(25);
                    } else {
                        Toast.makeText(HomeActivity.this, getResources().getString(R.string.MsgCheckConnection), Toast.LENGTH_LONG).show();
                    }
                } else {
//                    if (objMemberMaster.getProfession() != null && !objMemberMaster.getProfession().equals("")) {
//                        if (objMemberMaster.getHomeNumberStreet() != null && !objMemberMaster.getHomeNumberStreet().equals("")) {
                    isValid = false;
                    Globals.SetErrorLayout(approvedLayout, true, getResources().getString(R.string.MsgInvalidUser), rvContactList, 0);
//                        } else {
//                            Globals.ChangeActivity(HomeActivity.this, RegistartionFragmentActivity.class, true);
//                        }
//                    } else {
//                        Globals.ChangeActivity(HomeActivity.this, RegistartionFragmentActivity.class, true);
//                    }
                }
            } else {
                RequestMemberByMemberMasterId();
            }
//            SetAdvertise();
        } else {
            internetLayout.setVisibility(View.VISIBLE);
            Globals.SetErrorLayout(internetLayout, true, getResources().getString(R.string.MsgCheckConnection), null, R.drawable.wifi_off);
            homeLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void MemberUpdate(String errorCode, MemberMaster objMemberMaster) {
        progressDialog.dismiss();
        if (errorCode.equals("0")) {
            if (isLogout) {
                Globals.ClearUserPreference(HomeActivity.this);
                Intent intent = new Intent(HomeActivity.this, SignInActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                finish();
            } else if (isDelete) {
                isDelete = false;
                lstMemberList.remove(position);
                adapter.MemberDataRemoved(position);
            } else {
                adapter.MemberTypechanged(position, isAdmin);
            }
        }
    }

    @Override
    public void AdvretiseResponse(ArrayList<AdvertiseMaster> alAdvertiseMasters, AdvertiseMaster objAdvertiseMaster) {
        if (alAdvertiseMasters != null && alAdvertiseMasters.size() > 0) {
            this.objAdvertiseMaster = alAdvertiseMasters.get(0);
            CurrentPageAdvertise += 1;
            if (alAdvertiseMasters.get(0).getAdvertiseImageName() != null && !alAdvertiseMasters.get(0).getAdvertiseImageName().equals("")) {
                txtAdvertise.setVisibility(View.GONE);
                ivAdvertise.setVisibility(View.VISIBLE);
                Glide.with(HomeActivity.this).load(alAdvertiseMasters.get(0).getAdvertiseImageName()).asBitmap().diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true).into(ivAdvertise);
            } else if (alAdvertiseMasters.get(0).getAdvertiseText() != null && !alAdvertiseMasters.get(0).getAdvertiseText().equals("")) {
                ivAdvertise.setVisibility(View.GONE);
                txtAdvertise.setVisibility(View.VISIBLE);
                txtAdvertise.setText(alAdvertiseMasters.get(0).getAdvertiseText());
            }
        } else {
            CurrentPageAdvertise = 1;
            ivAdvertise.setVisibility(View.GONE);
            txtAdvertise.setVisibility(View.GONE);
        }
    }

    @Override
    public void SelectFilter(String name, String designation, String qualification, String bloodGroup, boolean isClear) {
        CurrentPage = 1;
        lstMemberList = new ArrayList<>();
        if (isClear) {
            this.name = "";
            this.qualification = "";
            this.designation = "";
            this.bloodGroup = "";
            RequestMemberMaster(pagesize);
        } else {
            this.name = name;
            this.designation = designation;
            this.qualification = qualification;
            this.bloodGroup = bloodGroup;
            RequestMemberMasterFilter(name, designation, qualification, bloodGroup);
        }
    }

//    @Override
//    public boolean onQueryTextSubmit(String query) {
//        return false;
//    }
//
//    @Override
//    public boolean onQueryTextChange(String newText) {
//        if (lstMemberList != null && lstMemberList.size() != 0) {
//            searchText = newText;
//            final ArrayList<MemberMaster> filteredList = Filter(lstMemberList, newText);
//            adapter.SetSearchFilter(filteredList);
//        }
//        return false;
//    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() != 0) {
            if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                    && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals("Advertisement")) {
                getSupportFragmentManager().popBackStack("Advertisement", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            } else if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                    && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals("Filter")) {
                getSupportFragmentManager().popBackStack("Filter", FragmentManager.POP_BACK_STACK_INCLUSIVE);
//                if (!FilterFragment.isFilter) {
//                    CurrentPage = 1;
//                    name = "";
//                    qualification = "";
//                    designation = "";
//                    bloodGroup = "";
//                    lstMemberList = new ArrayList<>();
//                    RequestMemberMaster(pagesize);
//                }
            }
        } else {
            if (FilterFragment.isFilter) {
                FilterFragment.isFilter = false;
                CurrentPage = 1;
                name = "";
                qualification = "";
                designation = "";
                bloodGroup = "";
                lstMemberList = new ArrayList<>();
                RequestMemberMaster(pagesize);
            } else if (!isValid) {
                Intent intent = new Intent(HomeActivity.this, SignInActivity.class);
                Globals.ClearUserPreference(HomeActivity.this);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                finish();
            } else {
                if (mRecentlyBackPressed) {
                    toast.cancel();
                    mExitHandler.removeCallbacks(mExitRunnable);
                    mExitHandler = null;
                    finish();
                } else {
                    mRecentlyBackPressed = true;
                    toast.show();
                    mExitHandler.postDelayed(mExitRunnable, delay);
                }
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
//        try {
//            isAdvertise = false;
//            timer.wait();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    protected void onStop() {
        super.onStop();// ATTENTION: This was auto-generated to implement the App Indexing API.
// See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
//        try {
//            isAdvertise = false;
//            timer.wait();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.disconnect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isHome = true;
//        if (!isAdvertise) {
//            isAdvertise = true;
//            timer.notify();
//        }

    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void ConfirmResponse() {
        if (isSave) {
            isSave = false;
            Globals.ContactSave(HomeActivity.this, objMember);
        } else if (isDelete) {
//            isDelete= false;
            progressDialog.show(getSupportFragmentManager(), "");
            MemberJSONParser objMemeberJSONParser = new MemberJSONParser();
            objMemeberJSONParser.DeleteMemberMasterByMemberMasterId(HomeActivity.this, objMember.getMemberMasterId(), true);
        }
    }

    // region Private Methods

    private void SetUserName() {
//        Intent intent = getIntent();
        if (objSharePreferenceManage.GetPreference("LoginPreference", "MemberName", HomeActivity.this) != null) {
            cbName.setText(objSharePreferenceManage.GetPreference("LoginPreference", "MemberName", HomeActivity.this));
        }
        if (objSharePreferenceManage.GetPreference("LoginPreference", "MemberEmail", HomeActivity.this) != null) {
            txtFullName.setVisibility(View.VISIBLE);
            txtFullName.setText(objSharePreferenceManage.GetPreference("LoginPreference", "MemberEmail", HomeActivity.this));
        } else {
            txtFullName.setVisibility(View.GONE);
        }
        if (objSharePreferenceManage.GetPreference("LoginPreference", "MemberImage", HomeActivity.this) != null) {
            Log.e("image", " " + objSharePreferenceManage.GetPreference("LoginPreference", "MemberImage", HomeActivity.this));
            Glide.with(HomeActivity.this).load(objSharePreferenceManage.GetPreference("LoginPreference", "MemberImage", HomeActivity.this)).asBitmap().centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(new BitmapImageViewTarget(imageView) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    imageView.setImageDrawable(circularBitmapDrawable);
                    imageView.setPadding(8, 8, 8, 8);
                }
            });
        } else {
            imageView.setImageResource(R.drawable.account_navigation);
        }

        hideNavigationItem();
    }

    private void SetRecyclerView(ArrayList<MemberMaster> alMemberList, String errorCode) {
        if (alMemberList == null) {
            if (CurrentPage == 1) {
                rvContactList.setVisibility(View.GONE);
                ivAdvertise.setVisibility(View.GONE);
                Globals.SetErrorLayout(errorLayout, true, String.format(getResources().getString(R.string.MsgNoRecordFound), "members"), rvContactList, 0);
            }
        } else if (alMemberList.size() == 0) {
            if (CurrentPage == 1) {
                rvContactList.setVisibility(View.GONE);
                ivAdvertise.setVisibility(View.GONE);
                Globals.SetErrorLayout(errorLayout, true, String.format(getResources().getString(R.string.MsgNoRecordFound), "members"), rvContactList, 0);
            }
        } else {
            lstMemberList.addAll(alMemberList);
            rvContactList.setVisibility(View.VISIBLE);
            ivAdvertise.setVisibility(View.VISIBLE);
            Globals.SetErrorLayout(errorLayout, false, null, rvContactList, 0);
            if (CurrentPage > 1) {
                this.alMemberList.addAll(alMemberList);
                adapter.MemberDataChanged(alMemberList);
                return;
            } else if (alMemberList.size() < 10) {
//                lstMemberList.addAll(alMemberList);
                this.alMemberList = alMemberList;
                CurrentPage += 1;
            }
            adapter = new MemberListAdapter(HomeActivity.this, this, null, alMemberList);
            rvContactList.setAdapter(adapter);
            rvContactList.setLayoutManager(linearLayoutManager);
        }
    }

    private void SetAdvertise() {
        int delay = 1000; // delay for 10 sec.
        int period = 15000; // repeat every 10 sec.
        isAdvertise = true;
//        ivAdvertise.setVisibility(View.VISIBLE);
//        txtAdvertise.setVisibility(View.GONE);
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                HomeActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        RequestAdvertiseMaster();
                    }
                });
            }
        }, delay, period);

    }

    private ArrayList<MemberMaster> Filter(ArrayList<MemberMaster> lstMemberMaster, String filterName) {
        filterName = filterName.toLowerCase();
        final ArrayList<MemberMaster> filteredList = new ArrayList<>();
        for (MemberMaster objMemberMaster : lstMemberMaster) {
            isFilter = false;
            ArrayList<String> alString = new ArrayList<>(Arrays.asList(objMemberMaster.getMemberName().toLowerCase().split(" ")));
            alString.add(0, objMemberMaster.getMemberName().toLowerCase());
            for (String aStrArray : alString) {
                if (aStrArray.length() >= filterName.length()) {
                    final String strItem = aStrArray.substring(0, filterName.length()).toLowerCase();
                    if (!isFilter) {
                        if (strItem.contains(filterName)) {
                            filteredList.add(objMemberMaster);
                            isFilter = true;
                        }
                    }
                }
            }
        }
        return filteredList;
    }

    private void RequestMemberMaster(int pageSize) {
        if (progressDialog.isAdded()) {
            progressDialog.dismiss();
        }
        progressDialog.show(getSupportFragmentManager(), "");
        MemberJSONParser objMemeberJSONParser = new MemberJSONParser();
        objMemeberJSONParser.SelectAllMemberMasterPageWise(HomeActivity.this, CurrentPage, pageSize);
    }

    private void RequestMemberMasterForPrint(int pageSize) {
        if (progressDialog.isAdded()) {
            progressDialog.dismiss();
        }
        progressDialog.show(getSupportFragmentManager(), "");
        MemberJSONParser objMemeberJSONParser = new MemberJSONParser();
        objMemeberJSONParser.SelectAllMemberMasterPageWise(HomeActivity.this, 1, pageSize);
    }

    private void RequestMemberMasterLogout() {
        if (progressDialog.isAdded()) {
            progressDialog.dismiss();
        }
        progressDialog.show(getSupportFragmentManager(), "");
        MemberJSONParser objMemeberJSONParser = new MemberJSONParser();
        objMemeberJSONParser.UpdateMemberMasterLogOut(HomeActivity.this, Globals.memberMasterId);
    }

    private void RequestMemberMasterFilter(String name, String designation, String qualification, String bloodGroup) {
        if (progressDialog.isAdded()) {
            progressDialog.dismiss();
        }
        progressDialog.show(getSupportFragmentManager(), "");
        MemberJSONParser objMemeberJSONParser = new MemberJSONParser();
        objMemeberJSONParser.SelectAllMemberMasterFilterPageWise(HomeActivity.this, CurrentPage, name, designation, qualification, bloodGroup);
    }

    private void RequestMemberByMemberMasterId() {
//        if (progressDialog.isAdded()) {
//            progressDialog.dismiss();
//        }
//        progressDialog.show(getSupportFragmentManager(), "");
        MemberJSONParser objMemeberJSONParser = new MemberJSONParser();
        objMemeberJSONParser.SelectMemberByMemberMasterId(HomeActivity.this, Globals.memberMasterId);
    }

    private void RequestAdvertiseMaster() {
        objAdvertiseJSONParser.SelectAllAdvertiseMasterPageWise(String.valueOf(CurrentPageAdvertise), String.valueOf(1), HomeActivity.this, null);
    }

    private void hideNavigationItem() {
        Menu nav_Menu = navigationView.getMenu();
        if (objSharePreferenceManage.GetPreference("LoginPreference", "MemberType", HomeActivity.this) != null) {
            if (objSharePreferenceManage.GetPreference("LoginPreference", "MemberType", HomeActivity.this).equals(Globals.MemberType.Admin.getMemberType())) {
                nav_Menu.findItem(R.id.nav_advertisement).setVisible(true);
            } else {
                nav_Menu.findItem(R.id.nav_advertisement).setVisible(false);
            }
        }
        nav_Menu.findItem(R.id.nav_rate_us).setVisible(false);
    }

    public void createandDisplayPdf(ArrayList<MemberMaster> alMemberMasters) {
        try {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/SMYM";
            File pdfFolder = new File(path);
            if (!pdfFolder.exists()) {
                pdfFolder.mkdir();
                Log.i("pdf", "Pdf Directory created");
            }

            //Create time stamp
            File myFile = new File(pdfFolder, "Member_list.pdf");
            OutputStream output = new FileOutputStream(myFile);

            //Step 1
            Document document = new Document(PageSize.A4, 25, 25, 25, 25);

            //Step 2
            PdfWriter.getInstance(document, output);

            //Step 3
            document.open();

            //Step 4 Add content
//            if(lstMemberList!=null && lstMemberList.size()>0) {
//                for (MemberMaster objMemberMaster : lstMemberList) {
//                    document.add(new ListItem(objMemberMaster.getMemberName() +"   "+objMemberMaster.getPhone1()));
//                }
//            }

            Font font = new Font();
            font.setStyle(Font.BOLD);
            font.setSize(14f);
            Phrase phrase = new Phrase("Shree Mahavir Yuvak Mandal Members List", font);
            Paragraph p3 = new Paragraph(phrase);  // to enter value you have to create paragraph  and add value in it then paragraph is added into document
            p3.setAlignment(Element.ALIGN_CENTER);
            document.add(p3);

            p3 = new Paragraph();  // to enter value you have to create paragraph  and add value in it then paragraph is added into document
            p3.add("  ");
            p3.setAlignment(Element.ALIGN_CENTER);
            document.add(p3);

            PdfPTable table = new PdfPTable(7);

            // Code 1
            table.setWidthPercentage(100);
            table.setSpacingBefore(0f);
            table.setSpacingAfter(0f);
            table.setWidths(new float[]{5f, 10f, 10f, 18f, 22f, 10f, 5f});

            // Code 2
            font = new Font();
            font.setStyle(Font.BOLD);
            font.setSize(9f);
            Phrase tablePhrse;
            PdfPCell c1;

            tablePhrse = new Phrase("Index", font);
            c1 = new PdfPCell(tablePhrse);
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);

            tablePhrse = new Phrase("Name", font);
            c1 = new PdfPCell(tablePhrse);
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);

            tablePhrse = new Phrase("Mobile No", font);
            c1 = new PdfPCell(tablePhrse);
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);

            tablePhrse = new Phrase("Email Id", font);
            c1 = new PdfPCell(tablePhrse);
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);

//            tablePhrse = new Phrase("Birthdate", font);
//            c1 = new PdfPCell(tablePhrse);
//            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
//            table.addCell(c1);

//            tablePhrse = new Phrase("Marital Status", font);
//            c1 = new PdfPCell(tablePhrse);
//            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
//            table.addCell(c1);

//            tablePhrse = new Phrase("Qualification", font);
//            c1 = new PdfPCell(tablePhrse);
//            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
//            table.addCell(c1);

            tablePhrse = new Phrase("Residential Address", font);
            c1 = new PdfPCell(tablePhrse);
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);

            tablePhrse = new Phrase("Profession", font);
            c1 = new PdfPCell(tablePhrse);
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);

            tablePhrse = new Phrase("Blood Group", font);
            c1 = new PdfPCell(tablePhrse);
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);

            // now fetch data from database and display it in pdf

            if (alMemberMasters != null && alMemberMasters.size() > 0) {
                font = new Font();
                font.setSize(9f);
                String no = "";
                String address = "";
                for (int i = 0; i < alMemberMasters.size(); i++) {
                    tablePhrse = new Phrase(String.valueOf(i + 1), font);
                    c1 = new PdfPCell(tablePhrse);
                    c1.setColspan(1);
                    table.addCell(c1);

                    tablePhrse = new Phrase(alMemberMasters.get(i).getMemberName(), font);
                    c1 = new PdfPCell(tablePhrse);
                    c1.setColspan(1);
                    table.addCell(c1);

                    no = alMemberMasters.get(i).getPhone1();
                    if(alMemberMasters.get(i).getHomePhone()!= null &&!alMemberMasters.get(i).getHomePhone().equals(""))
                    {
                        no = no+"\n"+alMemberMasters.get(i).getHomePhone()+"(H)";
                    }
                    tablePhrse = new Phrase(no, font);
                    c1 = new PdfPCell(tablePhrse);
                    c1.setColspan(1);
                    table.addCell(c1);

                    tablePhrse = new Phrase(alMemberMasters.get(i).getEmail(), font);
                    c1 = new PdfPCell(tablePhrse);
                    c1.setColspan(1);
                    table.addCell(c1);

//                    tablePhrse = new Phrase(alMemberMasters.get(i).getBirthDate(), font);
//                    c1 = new PdfPCell(tablePhrse);
//                    c1.setColspan(1);
//                    table.addCell(c1);

//                    if (alMemberMasters.get(i).getAnniversaryDate() != null && !alMemberMasters.get(i).getAnniversaryDate().equals("")) {
//                        tablePhrse = new Phrase("Married", font);
//                    } else {
//                        tablePhrse = new Phrase("Unmarried", font);
//                    }
//                    c1 = new PdfPCell(tablePhrse);
//                    c1.setColspan(1);
//                    table.addCell(c1);

//                    tablePhrse = new Phrase(alMemberMasters.get(i).getQualification(), font);
//                    c1 = new PdfPCell(tablePhrse);
//                    c1.setColspan(1);
//                    table.addCell(c1);

                    address= alMemberMasters.get(i).getHomeNumberStreet()+", "+alMemberMasters.get(i).getHomeNearBy()+", "+alMemberMasters.get(i).getHomeArea()
                            +", "+alMemberMasters.get(i).getHomeCity()+", "+alMemberMasters.get(i).getHomeState()+" - "+alMemberMasters.get(i).getHomeZipCode();
                    tablePhrse = new Phrase(address, font);
                    c1 = new PdfPCell(tablePhrse);
                    c1.setColspan(1);
                    table.addCell(c1);

                    tablePhrse = new Phrase(alMemberMasters.get(i).getProfession(), font);
                    c1 = new PdfPCell(tablePhrse);
                    c1.setColspan(1);
                    table.addCell(c1);

                    tablePhrse = new Phrase(alMemberMasters.get(i).getBloodGroup(), font);
                    c1 = new PdfPCell(tablePhrse);
                    c1.setColspan(1);
                    table.addCell(c1);

                }
            }
            document.add(table);
            document.addCreationDate();

            //Step 5: Close the document
            document.close();

            viewPdf("Member_list.pdf", "SMYM");

        } catch (Exception e) {
            Log.e("PDFCreator", "ioException:" + e);
        }

    }

    // Method for opening a pdf file
    private void viewPdf(String file, String directory) {
        try {
            File pdfFile = new File(Environment.getExternalStorageDirectory() + "/" + directory + "/" + file);
            Uri path = Uri.fromFile(pdfFile);

            // Setting the intent for pdf reader
            Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
            pdfIntent.setDataAndType(path, "application/pdf");
            pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(pdfIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(HomeActivity.this, "Can't read pdf file", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Home Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    //endregion
}
