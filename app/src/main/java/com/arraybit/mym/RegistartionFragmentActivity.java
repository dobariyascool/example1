package com.arraybit.mym;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.arraybit.global.Globals;

public class RegistartionFragmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registartion_fragment);

        if (Globals.startPage == 1) {
            RegistrationDetailFragment objRegistrationDetailFragment = new RegistrationDetailFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable("MemberMaster", null);
            objRegistrationDetailFragment.setArguments(bundle);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out, 0, R.anim.right_exit);
            fragmentTransaction.replace(android.R.id.content, objRegistrationDetailFragment, "PersonalDetail");
            fragmentTransaction.addToBackStack("PersonalDetail");
            fragmentTransaction.commit();
        } else if (Globals.startPage == 2) {
            RegistrationContactFragment objRegistrationContactFragment = new RegistrationContactFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable("MemberMaster", null);
            objRegistrationContactFragment.setArguments(bundle);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out, 0, R.anim.right_exit);
            fragmentTransaction.replace(android.R.id.content, objRegistrationContactFragment, "ContactDetail");
            fragmentTransaction.addToBackStack("ContactDetail");
            fragmentTransaction.commit();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == RESULT_OK) {
                if (getSupportFragmentManager().getBackStackEntryCount() != 0) {
                    if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals("PersonalDetail")) {
                        RegistrationDetailFragment registrationDetailFragment = (RegistrationDetailFragment) getSupportFragmentManager().findFragmentByTag("PersonalDetail");
                        registrationDetailFragment.SelectImage(requestCode, data);
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() != 0) {
            if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
                if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals("ContactDetail")) {
                    finish();
                } else if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals("PersonalDetail")) {
                    finish();
                }
            }
        }
    }
}
