package com.arraybit.mym;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.arraybit.adapter.MemberListAdapter;
import com.arraybit.global.EndlessRecyclerOnScrollListener;
import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.modal.MemberMaster;
import com.arraybit.parser.MemberJSONParser;

import java.util.ArrayList;

public class NewMembersActivity extends AppCompatActivity implements MemberJSONParser.MembersListRequestListener, MemberListAdapter.OnCardClickListener,
        MemberListAdapter.OnRequestListener, MemberJSONParser.MemberRequestListener, ConfirmDialog.ConfirmationResponseListener {

    DisplayMetrics displayMetrics;
    LinearLayoutManager linearLayoutManager;
    LinearLayout errorLayout;
    RecyclerView rvContactList;
    MemberListAdapter adapter;
    ProgressDialog progressDialog = new ProgressDialog();
    int CurrentPage = 1, position, duration;
    boolean isStart = false;
    ArrayList<MemberMaster> alMemberList;
    MemberMaster objMemberMaster;
    boolean isApproved, isSnackShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_members);
        try {
            Toolbar app_bar = (Toolbar) findViewById(R.id.app_bar);
            setSupportActionBar(app_bar);
            if (getSupportActionBar() != null) {
                if (Build.VERSION.SDK_INT >= 21) {
                    app_bar.setElevation(getResources().getDimension(R.dimen.app_bar_elevation));
                }
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setTitle("Member Request");
            }
            displayMetrics = getResources().getDisplayMetrics();

            isStart = getIntent().getBooleanExtra("isStart", false);
            errorLayout = (LinearLayout) findViewById(R.id.errorLayout);
            rvContactList = (RecyclerView) findViewById(R.id.rvContactList);
            linearLayoutManager = new LinearLayoutManager(NewMembersActivity.this);

            rvContactList.addOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager) {
                @Override
                public void onLoadMore(int current_page) {
                    if (current_page > CurrentPage) {
                        CurrentPage = current_page;
                        if (Service.CheckNet(NewMembersActivity.this)) {
                            RequestMemberMaster();
                        } else {
                            Toast.makeText(NewMembersActivity.this, getResources().getString(R.string.MsgCheckConnection), Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });

            if (Service.CheckNet(NewMembersActivity.this)) {
                RequestMemberMaster();
            } else {
                Toast.makeText(NewMembersActivity.this, getResources().getString(R.string.MsgCheckConnection), Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (isStart) {
                Intent intent = new Intent(NewMembersActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            } else {
                setResult(RESULT_OK);
                finish();
                overridePendingTransition(0, R.anim.right_exit);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (isStart) {
            Intent intent = new Intent(NewMembersActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
        } else {
            setResult(RESULT_OK);
            finish();
            overridePendingTransition(0, R.anim.right_exit);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == RESULT_OK && requestCode == 120) {
                if (data != null) {
                    position = data.getIntExtra("position", -1);
                    if (position > -1) {
                        adapter.MemberDataRemoved(position);
                    }
                    isStart = data.getBooleanExtra("isStart", false);
                    isApproved = data.getBooleanExtra("isApproved", false);
                    if (isStart) {
                        ShowSnackBarWithAction(position, objMemberMaster);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void MemberListResponse(ArrayList<MemberMaster> alMemberMasters, MemberMaster objMemberMaster, String errorCode) {
        progressDialog.dismiss();
        SetRecyclerView(alMemberMasters);
    }

    @Override
    public void OnCardClick(MemberMaster objMemberMaster, String event, int position) {
        if (event == null) {
            this.objMemberMaster = objMemberMaster;
            this.position = position;
            Intent intent = new Intent(NewMembersActivity.this, DetailActivity.class);
            intent.putExtra("isNotification", true);
            intent.putExtra("memberMasterId", objMemberMaster.getMemberMasterId());
            intent.putExtra("memberName", objMemberMaster.getMemberName());
            intent.putExtra("position", position);
            startActivityForResult(intent, 120);
        } else if (event.equals(getResources().getString(R.string.call))) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:0" + objMemberMaster.getPhone1()));
            startActivity(intent);
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
        } else if (event.equals(getResources().getString(R.string.message))) {
            Uri uri = Uri.parse("smsto:0" + objMemberMaster.getPhone1());
            Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
//            intent.putExtra("sms_body", sms);
            startActivity(intent);
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
        } else if (event.equals(getResources().getString(R.string.email))) {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_EMAIL, objMemberMaster.getEmail());
            startActivity(Intent.createChooser(intent, "Send Email"));
        }
    }

    @Override
    public void ConfirmResponse() {
        if (isApproved) {
            RequestIsApproved(objMemberMaster, isApproved);
        } else {
            adapter.MemberDataRemoved(position);
            ShowSnackBarWithAction(position, objMemberMaster);
        }
    }

    @Override
    public void OnRequestStatus(MemberMaster objMemberMaster, int position, boolean isApproved) {
        this.position = position;
        this.objMemberMaster = objMemberMaster;
        this.isApproved = isApproved;
        ConfirmDialog confirmDialog;
        if (isApproved) {
            confirmDialog = new ConfirmDialog(true, "Approve " + objMemberMaster.getMemberName() + " ?");
        } else {
            confirmDialog = new ConfirmDialog(true, "Decline " + objMemberMaster.getMemberName() + " ?");
        }
        confirmDialog.show(getSupportFragmentManager(), "");
    }

    @Override
    public void MemberResponse(String errorCode, MemberMaster objMemberMaster) {

    }

    @Override
    public void MemberUpdate(String errorCode, MemberMaster objMemberMaster) {
        progressDialog.dismiss();
        if (errorCode.equals("0")) {
            if (isApproved) {
                adapter.MemberDataRemoved(position);
            }
        } else {
            Globals.ShowSnackBar(rvContactList, getResources().getString(R.string.MsgServerNotResponding), NewMembersActivity.this, 1000);
        }
    }

    //region Private Methods

    private void RequestMemberMaster() {
        if (progressDialog.isAdded()) {
            progressDialog.dismiss();
        }
        progressDialog.show(getSupportFragmentManager(), "");
        MemberJSONParser objMemeberJSONParser = new MemberJSONParser();
        objMemeberJSONParser.SelectAllNewMemberMasterPageWise(NewMembersActivity.this, CurrentPage);
    }

    private void ShowSnackBarWithAction(final int position, final MemberMaster objMemberMaster) {
        if (position == 0 && position == alMemberList.size() - 1) {
            isSnackShow = true;
            duration = 3000;
        } else {
            duration = 8000;
        }
        Snackbar snackbar = Snackbar
                .make(rvContactList, String.format(getResources().getString(R.string.MsgMemberDecline), objMemberMaster.getMemberName()), duration)
                .setCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar snackbar, int event) {
                        super.onDismissed(snackbar, event);
                        if (event == DISMISS_EVENT_TIMEOUT) {
                            //will be true if user not click on Action button (for example: manual dismiss, dismiss by swipe
                            if (alMemberList.size() == 0) {
                                isSnackShow = false;
                                RequestIsApproved(objMemberMaster, isApproved);
                            }
                        }
                    }
                })
                .setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        isSnackShow = false;
                        adapter.UndoData(position, objMemberMaster);
                        if (alMemberList.size() == 0) {
//                            RemarkDialogFragment.strRemark = "";
                            SetRecyclerView(alMemberList);
                        }
                    }
                });

        snackbar.setActionTextColor(ContextCompat.getColor(NewMembersActivity.this, R.color.snackBarActionColor));

        View snackView = snackbar.getView();
        if (Build.VERSION.SDK_INT >= 21) {
            snackView.setElevation(getResources().getDimension(R.dimen.snackbar_elevation));
        }
        android.widget.TextView txt = (android.widget.TextView) snackView.findViewById(android.support.design.R.id.snackbar_text);
        txt.setTextColor(ContextCompat.getColor(NewMembersActivity.this, android.R.color.white));
        snackView.setBackgroundColor(ContextCompat.getColor(NewMembersActivity.this, R.color.blue_grey));
        snackbar.show();
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
        objMemeberJSONParser.UpdateMemberMasterIsApproved(NewMembersActivity.this, objMemberMaster);
    }

    private void SetRecyclerView(ArrayList<MemberMaster> alMemberList) {
        this.alMemberList = alMemberList;
        if (alMemberList == null) {
            if (CurrentPage == 1) {
                rvContactList.setVisibility(View.GONE);
                Globals.SetErrorLayout(errorLayout, true, "There is no member request", rvContactList, 0);
            }
        } else if (alMemberList.size() == 0) {
            if (CurrentPage == 1) {
                rvContactList.setVisibility(View.GONE);
                Globals.SetErrorLayout(errorLayout, true, "There is no member request", rvContactList, 0);
            }
        } else {
            rvContactList.setVisibility(View.VISIBLE);
            Globals.SetErrorLayout(errorLayout, false, null, rvContactList, 0);
            if (CurrentPage > 1) {
                adapter.MemberDataChanged(alMemberList);
                return;
            } else if (alMemberList.size() < 10) {
                CurrentPage += 1;
            }
            adapter = new MemberListAdapter(NewMembersActivity.this, this, this, alMemberList);
            rvContactList.setAdapter(adapter);
            rvContactList.setLayoutManager(linearLayoutManager);
        }
    }

    //endregion
}
