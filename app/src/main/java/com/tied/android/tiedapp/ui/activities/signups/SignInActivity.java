package com.tied.android.tiedapp.ui.activities.signups;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.view.View;
import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.retrofits.services.SignUpApi;
import com.tied.android.tiedapp.ui.activities.MainActivity;
import com.tied.android.tiedapp.ui.fragments.signins.DoneResetFragment;
import com.tied.android.tiedapp.ui.fragments.signins.ResetFragment;
import com.tied.android.tiedapp.ui.fragments.signins.SignInFragment;
import com.tied.android.tiedapp.ui.listeners.FragmentIterationListener;

import com.tied.android.tiedapp.util.MyUtils;
import retrofit2.Retrofit;

public class SignInActivity extends AppCompatActivity implements FragmentIterationListener {

    public static final String TAG = SignInActivity.class
            .getSimpleName();

    private Fragment fragment = null;
    private int fragment_index = 0;

    public Bitmap bitmap;

    public Retrofit retrofit;
    public SignUpApi service;

    public Uri imageUri = null, outputUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.overridePendingTransition(R.anim.slide_in_from_bottom, R.anim.slide_out_top);
        setContentView(R.layout.activity_sign_in);
        launchFragment(Constants.SignInUser, null);
        MainApplication.clearToken();
        retrofit = MainApplication.getInstance().initRetrofit();


        service = retrofit.create(SignUpApi.class);

    }

    public void launchFragment(int pos, Bundle bundle) {
        fragment_index = pos;
        fragment = null;

        switch (pos) {
            case Constants.SignInUser:
                fragment = new SignInFragment();
                fragment.setArguments(bundle);
                break;
            case Constants.Reset:
                fragment = new ResetFragment();
                fragment.setArguments(bundle);
                break;
            case Constants.DoneReset:
                fragment = new DoneResetFragment();
                fragment.setArguments(bundle);
                break;
        }

        if (fragment != null) {
            Log.d(TAG, getSupportFragmentManager().getBackStackEntryCount() + "");
            while (getSupportFragmentManager().getBackStackEntryCount() > 1) {
                getSupportFragmentManager().popBackStackImmediate();
            }

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(fragment.getClass().getSimpleName())
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "fragment_index " + fragment_index);
        if (fragment_index == Constants.Reset) {
            launchFragment(Constants.SignInUser, null);
        }
        else if (fragment_index == Constants.SignInUser) {
            Log.d(TAG, "am in fragment_index " + fragment_index);
            Intent intent = new Intent(this, WalkThroughActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else {
            finish();
        }
    }

    @Override
    public void OnFragmentInteractionListener(int action, Bundle bundle) {
        Log.d(TAG, " OnFragmentInteractionListener " + action);
        launchFragment(action, bundle);

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.profile_layout:
                MyUtils.startActivity(this, MainActivity.class);
                break;
        }
    }

    public void goBack(View v) {
        onBackPressed();
    }
}
