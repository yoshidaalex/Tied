package com.tied.android.tiedapp.ui.activities.client;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.soundcloud.android.crop.Crop;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.Territory;
import com.tied.android.tiedapp.objects.client.Client;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.ui.fragments.client.AddClientFragment;
import com.tied.android.tiedapp.ui.fragments.client.ViewClientFragment;
import com.tied.android.tiedapp.ui.fragments.signups.TerritoryFragment;
import com.tied.android.tiedapp.ui.listeners.FragmentIterationListener;
import com.tied.android.tiedapp.util.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Daniel on 5/3/2016.
 */
public class AddClientActivity extends FragmentActivity implements View.OnClickListener, FragmentIterationListener {

    public static final String TAG = AddClientActivity.class
            .getSimpleName();

    private Fragment fragment = null;
    public Fragment profileFragment = null;
    private int fragment_index = 0;

    private User user;
    private Bundle bundle;
    Map<Integer, Fragment> fragments = new HashMap<Integer, Fragment>();

    // Code for our image picker select action.
    public final int IMAGE_PICKER_SELECT = 999;

    // Activity result key for camera
    public final int REQUEST_TAKE_PHOTO = 11111;

    public Bitmap bitmap;
    private boolean isLaunched = false;

    public Uri imageUri = null, outputUri = null;
    int currentFragmentID=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.overridePendingTransition(R.anim.slide_in_from_bottom, R.anim.do_nothing);

        setContentView(R.layout.activity_add_client);
        bundle = getIntent().getExtras();
        if(bundle == null){
            bundle = new Bundle();
        }
        user = User.getCurrentUser(getApplicationContext());
        Client client = (Client) getIntent().getSerializableExtra(Constants.CLIENT_DATA);

        Gson gson = new Gson();
        String user_json = gson.toJson(user);
        String client_json = gson.toJson(client);
        bundle.putString(Constants.USER_DATA, user_json);
        bundle.putString(Constants.CLIENT_DATA, client_json);
        launchFragment(Constants.AddClient, bundle);
        //launchFragment(Constants.ViewClient, bundle);
    }

    private void handleCrop(Uri outputUri) {
        ImageView avatar = ((AddClientFragment) fragment).avatar;
        avatar.setImageBitmap(null);
        Logger.write("path * "+ outputUri.getPath());
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), outputUri);
            avatar.setImageBitmap(bitmap);
        } catch (IOException e) {
            Toast.makeText(this, " error : " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Photo Selection result
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logger.write("requestCodeeeeeeee "+ requestCode + "");
        if (requestCode == Crop.REQUEST_CROP && resultCode == Activity.RESULT_OK) {
            handleCrop(outputUri);
        } else if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            outputUri = Uri.fromFile(new File(getFilesDir(), "client.jpg"));
            Uri selectedImage = imageUri;
            Crop.of(selectedImage, outputUri).asSquare().start(this);
        } else if (requestCode == IMAGE_PICKER_SELECT && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImage = data.getData();
            outputUri = Uri.fromFile(new File(getFilesDir(), "client.jpg"));
            Crop.of(selectedImage, outputUri).asSquare().start(this);
        } else if (requestCode == Constants.SELECT_TERRITORY && resultCode == Activity.RESULT_OK) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    Fragment currentFragment=null;
    public void launchFragment(int pos, Bundle bundle) {
        fragment = null;
        fragment_index = pos;

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_from_bottom, R.anim.slide_out_top);
        currentFragmentID=pos;
        switch (pos) {
            case Constants.AddClient:
                if(fragments.get(pos)==null) {
                    fragments.put(pos, AddClientFragment.newInstance(bundle) );
                }
                fragment = fragments.get(pos);
                break;
            case Constants.Territory:
                if(fragments.get(pos)==null) {
                    fragments.put(pos, TerritoryFragment.newInstance(bundle));
                }
                fragment = fragments.get(pos);
                break;
            case Constants.ViewClient:
                if(fragments.get(pos)==null) {
                    fragments.put(pos,  ViewClientFragment.newInstance(bundle) );
                }
                fragment = fragments.get(pos);
                break;
            default:
                finish();
        }

        if (fragment != null) {
            Log.d(TAG, getSupportFragmentManager().getBackStackEntryCount() + "");
            Logger.write("TAGGGG: "+ fragment.getClass().getName());
            addFragment(ft, currentFragment, fragment, fragment.getClass().getName());
        }
        currentFragment=fragment;
    }

    static long backPressed=0;

    public void OnFragmentInteractionListener(int action, Bundle bundle) {
        Log.d(TAG, " OnFragmentInteractionListener " + action);
        launchFragment(action, bundle);
    }

    @Override
    public void onBackPressed() {
       // super.onBackPressed();
        finish();
        /*
        if (Constants.AddClient == fragment_index){
            finish();
        }*/
    }


    public void goBack(View v) {
        onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_done:
                onBackPressed();
                break;
        }
    }



    public void addFragment(FragmentTransaction transaction, Fragment currentFragment, Fragment targetFragment, String tag) {

        //transaction.setCustomAnimations(0,0,0,0);
        if(currentFragment!=null) transaction.hide(currentFragment);
        // use a fragment tag, so that later on we can find the currently displayed fragment
       // if(targetFragment.isAdded()) {
       //     transaction.show(targetFragment).commit();
       // }else {
            transaction.replace(R.id.fragment_place, targetFragment, tag)
                    .addToBackStack(tag)
                    .commit();
       // }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);
    }
}
