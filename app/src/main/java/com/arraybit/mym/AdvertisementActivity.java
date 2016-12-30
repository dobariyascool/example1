package com.arraybit.mym;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.arraybit.modal.AdvertiseMaster;

public class AdvertisementActivity extends AppCompatActivity implements AdvertisementAddFragment.BroadCastListener, ConfirmDialog.ConfirmationResponseListener {

    public AdvertisementAddFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertisement);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out, 0, R.anim.right_exit);
        fragmentTransaction.replace(R.id.llAdvertisementFragment, new AdvertisementListFragment(), getResources().getString(R.string.advertisement_list_fragment));
        fragmentTransaction.addToBackStack(getResources().getString(R.string.advertisement_list_fragment));
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_notification_advertise, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.add).setVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == RESULT_OK) {
                AdvertisementAddFragment advertisementAddFragment = (AdvertisementAddFragment) getSupportFragmentManager().findFragmentByTag(getResources().getString(R.string.advertisement_add_fragment));
                advertisementAddFragment.SelectImage(requestCode, data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() != 0) {
            if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getResources().getString(R.string.advertisement_list_fragment))) {
                setResult(RESULT_OK);
                finish();
                overridePendingTransition(0, R.anim.right_exit);
            } else if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getResources().getString(R.string.advertisement_add_fragment))) {
                getSupportFragmentManager().popBackStack(getResources().getString(R.string.advertisement_add_fragment), FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        } else {
            setResult(RESULT_OK);
            finish();
            overridePendingTransition(0, R.anim.right_exit);
        }
    }

    public void ReplaceAddFragment() {
        fragment = new AdvertisementAddFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out, 0, R.anim.right_exit);
        fragmentTransaction.replace(R.id.llAdvertisementFragment, fragment, getResources().getString(R.string.advertisement_add_fragment));
        fragmentTransaction.addToBackStack(getResources().getString(R.string.advertisement_add_fragment));
        fragmentTransaction.commit();
    }

    public void ReplaceAddFragment(AdvertiseMaster objAdvertiseMaster) {
        fragment = new AdvertisementAddFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("AdvertiseMaster", objAdvertiseMaster);
        bundle.putBoolean("isUpdate", true);
        fragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out, 0, R.anim.right_exit);
        fragmentTransaction.replace(R.id.llAdvertisementFragment, fragment, getResources().getString(R.string.advertisement_add_fragment));
        fragmentTransaction.addToBackStack(getResources().getString(R.string.advertisement_add_fragment));
        fragmentTransaction.commit();
    }

    @Override
    public void ConfirmResponse() {
        fragment.AddAdvertise();
    }

    @Override
    public void BroadCastOnclick() {
        ConfirmDialog confirmDialog = new ConfirmDialog(true, "Add Advertise?");
        confirmDialog.show(getSupportFragmentManager(), "");
    }
}

