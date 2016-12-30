package com.arraybit.mym;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public class NotificationActivity extends AppCompatActivity
        implements NotificationAddFragment.BroadCastListener, ConfirmDialog.ConfirmationResponseListener {

    public static boolean isStart = false;
    public NotificationAddFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        isStart = getIntent().getBooleanExtra("isStart", false);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out, 0, R.anim.right_exit);
        fragmentTransaction.replace(R.id.llNotificationFragment, new NotificationListFragment(), getResources().getString(R.string.notification_list_fragment));
        fragmentTransaction.addToBackStack(getResources().getString(R.string.notification_list_fragment));
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
                NotificationAddFragment notificationAddFragment = (NotificationAddFragment) getSupportFragmentManager().findFragmentByTag(getResources().getString(R.string.notification_add_fragment));
                notificationAddFragment.SelectImage(requestCode, data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() != 0) {
            if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getResources().getString(R.string.notification_list_fragment))) {
                if (isStart) {
                    Intent intent = new Intent(NotificationActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                } else {
                    setResult(RESULT_OK);
                    finish();
                    overridePendingTransition(0, R.anim.right_exit);
                }
            } else if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getResources().getString(R.string.notification_add_fragment))) {
                getSupportFragmentManager().popBackStack(getResources().getString(R.string.notification_add_fragment), FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        } else {
            if (isStart) {
                Intent intent = new Intent(NotificationActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            } else {
                setResult(RESULT_OK);
                finish();
                overridePendingTransition(0, R.anim.right_exit);
            }
        }
    }

    public void ReplaceAddFragment() {
        fragment = new NotificationAddFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out, 0, R.anim.right_exit);
        fragmentTransaction.replace(R.id.llNotificationFragment, fragment, getResources().getString(R.string.notification_add_fragment));
        fragmentTransaction.addToBackStack(getResources().getString(R.string.notification_add_fragment));
        fragmentTransaction.commit();
    }

    @Override
    public void ConfirmResponse() {
        fragment.AddNotification();
    }

    @Override
    public void BroadCastOnclick() {
        ConfirmDialog confirmDialog = new ConfirmDialog(true, "Broadcast notification?");
        confirmDialog.show(getSupportFragmentManager(), "");
    }
}
