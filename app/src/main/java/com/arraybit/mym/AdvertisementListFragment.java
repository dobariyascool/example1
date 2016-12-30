package com.arraybit.mym;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.arraybit.adapter.AdvertiseAdapter;
import com.arraybit.global.EndlessRecyclerOnScrollListener;
import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.modal.AdvertiseMaster;
import com.arraybit.parser.AdvertiseJSONParser;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;


public class AdvertisementListFragment extends Fragment implements AdvertiseJSONParser.AdvretiseRequestListener,
        AdvertiseAdapter.AdvertiseListener, AdvertiseJSONParser.AdvertiseUpdateListener {

    RecyclerView rvAdvertisements;
    LinearLayoutManager linearLayoutManager;
    int CurrentPage = 1;
    ProgressDialog progressDialog = new ProgressDialog();
    LinearLayout errorLayout;
    AdvertiseAdapter adapter;
    int position;
    boolean isEnable = false;
    AdvertiseMaster objAdvertiseMaster;

    public AdvertisementListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_advertisement_list, container, false);
        try {
            Toolbar toolbar = (Toolbar) view.findViewById(R.id.app_bar);
            if (toolbar != null) {
                ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
                ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getActivity().getResources().getString(R.string.advertisement_title));
            }
            setHasOptionsMenu(true);

            rvAdvertisements = (RecyclerView) view.findViewById(R.id.rvAdvertisements);
            linearLayoutManager = new LinearLayoutManager(getActivity());
            errorLayout = (LinearLayout) view.findViewById(R.id.errorLayout);

            rvAdvertisements.addOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager) {
                @Override
                public void onLoadMore(int current_page) {
                    if (current_page > CurrentPage) {
                        CurrentPage = current_page;
                        if (Service.CheckNet(getActivity())) {
                            RequestAdvertiseMaster();
                        } else {
                            Toast.makeText(getActivity(), getResources().getString(R.string.MsgCheckConnection), Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });

            if (Service.CheckNet(getActivity())) {
                RequestAdvertiseMaster();
            } else {
                Globals.SetErrorLayout(errorLayout, true, getResources().getString(R.string.MsgCheckConnection), null, R.drawable.wifi_off);
                Globals.ShowSnackBar(errorLayout, getResources().getString(R.string.MsgCheckConnection), getActivity(), 1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.add).setVisible(true);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getActivity().setResult(RESULT_OK);
            getActivity().finish();
            getActivity().overridePendingTransition(0, R.anim.right_exit);
        } else if (item.getItemId() == R.id.add) {
            ((AdvertisementActivity) getActivity()).ReplaceAddFragment();
            CurrentPage = 1;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void AdvretiseResponse(ArrayList<AdvertiseMaster> alAdvertiseMasters, AdvertiseMaster objAdvertiseMaster) {
        progressDialog.dismiss();
        SetRecyclerView(alAdvertiseMasters);
    }

    @Override
    public void OnCardClick(AdvertiseMaster objAdvertiseMaster, String event, int position) {
        this.objAdvertiseMaster = objAdvertiseMaster;
        this.position = position;
        if (event.equals(getResources().getString(R.string.action_delete))) {
            DeleteAdvertiseMaster(objAdvertiseMaster);
        } else if (event.equals(getResources().getString(R.string.action_disable)) || event.equals(getResources().getString(R.string.action_enable))) {
            this.isEnable = objAdvertiseMaster.getIsEnabled() ? false : true;
            UpdateAdvertiseMaster(objAdvertiseMaster, isEnable);
        } else if (event.equals(getResources().getString(R.string.action_edit))) {
            ((AdvertisementActivity) getActivity()).ReplaceAddFragment(objAdvertiseMaster);
            CurrentPage = 1;
        }
    }

    @Override
    public void AdvertiseDisableResponse(String errorCode) {
        progressDialog.dismiss();
        if (errorCode.equals("0")) {
            adapter.AdvretiseDisable(position, isEnable);
        }
    }

    @Override
    public void AdvertiseDeleteResponse(String errorCode) {
        progressDialog.dismiss();
        if (errorCode.equals("0")) {
            adapter.AdvertiseDataRemove(position);
        }
    }


    //region Private Methods
    private void RequestAdvertiseMaster() {
        if (progressDialog.isAdded()) {
            progressDialog.dismiss();
        }
        progressDialog.show(getActivity().getSupportFragmentManager(), "");
        AdvertiseJSONParser objAdvertiseJSONParser = new AdvertiseJSONParser();
        objAdvertiseJSONParser.SelectAllAdvertiseMasterPageWise(String.valueOf(CurrentPage), String.valueOf(25), getActivity(), this);
    }

    private void UpdateAdvertiseMaster(AdvertiseMaster objAdvertiseMaster, boolean isEnable) {
        if (progressDialog.isAdded()) {
            progressDialog.dismiss();
        }
        progressDialog.show(getActivity().getSupportFragmentManager(), "");

        objAdvertiseMaster.setIsEnabled(isEnable);
        objAdvertiseMaster.setlinktoMemberMasterIdUpdatedBy(Globals.memberMasterId);
        AdvertiseJSONParser objAdvertiseJSONParser = new AdvertiseJSONParser();
        objAdvertiseJSONParser.UpdateAdvertiseMasterDisable(objAdvertiseMaster, getActivity(), this);
    }

    private void DeleteAdvertiseMaster(AdvertiseMaster objAdvertiseMaster) {
        if (progressDialog.isAdded()) {
            progressDialog.dismiss();
        }
        progressDialog.show(getActivity().getSupportFragmentManager(), "");
        objAdvertiseMaster.setIsDeleted(true);
        objAdvertiseMaster.setlinktoMemberMasterIdUpdatedBy(Globals.memberMasterId);
        AdvertiseJSONParser objAdvertiseJSONParser = new AdvertiseJSONParser();
        objAdvertiseJSONParser.UpdateAdvertiseMasterDelete(objAdvertiseMaster, getActivity(), this);
    }

    private void SetRecyclerView(ArrayList<AdvertiseMaster> lstAdvertiseMasters) {
        if (lstAdvertiseMasters == null) {
            if (CurrentPage == 1) {
                Globals.SetErrorLayout(errorLayout, true, String.format(getResources().getString(R.string.MsgNoRecordFound), "advertisements"), rvAdvertisements, 0);
            }
        } else if (lstAdvertiseMasters.size() == 0) {
            if (CurrentPage == 1) {
                Globals.SetErrorLayout(errorLayout, true, String.format(getResources().getString(R.string.MsgNoRecordFound), "advertisements"), rvAdvertisements, 0);
            }
        } else {
            Globals.SetErrorLayout(errorLayout, false, null, rvAdvertisements, 0);
            if (CurrentPage > 1) {
                adapter.AdvertiseDataChanged(lstAdvertiseMasters);
                return;
            } else if (lstAdvertiseMasters.size() < 10) {
                CurrentPage += 1;
            }
            adapter = new AdvertiseAdapter(getActivity(), lstAdvertiseMasters, this);
            rvAdvertisements.setAdapter(adapter);
            rvAdvertisements.setLayoutManager(linearLayoutManager);
        }
    }

    //endregion

}
