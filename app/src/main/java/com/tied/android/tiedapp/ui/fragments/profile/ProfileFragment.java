package com.tied.android.tiedapp.ui.fragments.profile;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.SignUpApi;
import com.tied.android.tiedapp.ui.activities.MainActivity;
import com.tied.android.tiedapp.ui.activities.profile.ChangePasswordActivity;
import com.tied.android.tiedapp.ui.activities.profile.EditProfileActivity;
import com.tied.android.tiedapp.ui.activities.profile.NotificationProfileActivity;
import com.tied.android.tiedapp.ui.activities.profile.PrivacyActivity;
import com.tied.android.tiedapp.ui.listeners.ImageReadyForUploadListener;
import com.tied.android.tiedapp.util.DemoData;
import com.tied.android.tiedapp.util.MyUtils;

import java.util.HashMap;
import java.util.Map;

import me.relex.circleindicator.CircleIndicator;
import retrofit2.Retrofit;

/**
 * Created by Daniel on 5/3/2016.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = ProfileFragment.class
            .getSimpleName();

    public ImageView img_user_picture, add, drawerUserPicture;
    private ImageReadyForUploadListener imageReadyForUploadListener;

    private Fragment fragment = null;
    private int fragment_index = 0;
    public Fragment profileFragment = null;
    public Bitmap bitmap;

    public Uri imageUri = null, outputUri = null;
    Map<Integer, Fragment> fragments = new HashMap<Integer, Fragment>();

    User user;
    public Bundle bundle;

    // Code for our image picker select action.
    public final int IMAGE_PICKER_SELECT = 999;

    // Activity result key for camera
    public final int REQUEST_TAKE_PHOTO = 11111;

    public Retrofit retrofit;
    public SignUpApi service;

    public static ViewPager vpProfile;
    public static PagerAdapter mPagerAdapter;
    private CircleIndicator circleIndicator;

    private RelativeLayout atvPersonalInfo, atvNotifications, atvChangePassword, atvPrivacy, atvHelp, atvLogout;

    public static Fragment newInstance(Bundle bundle) {
        Fragment fragment = new ProfileFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_new, container, false);
        initComponent(view);
        Log.d(TAG, "AM HERE AGAIN");
        return view;
    }

    public void initComponent(View view) {

        bundle = getArguments();
        user = User.getCurrentUser(getActivity().getApplicationContext());
        Gson gson = new Gson();
        String json = gson.toJson(user);
        bundle.putString(Constants.USER_DATA, json);

        vpProfile = (ViewPager) view.findViewById(R.id.vpProfile);
        circleIndicator = (CircleIndicator) view.findViewById(R.id.circleIndicator);
        mPagerAdapter = new PagerAdapter(getFragmentManager());
        vpProfile.setAdapter(mPagerAdapter);
        circleIndicator.setViewPager(vpProfile);

        atvNotifications = (RelativeLayout) view.findViewById(R.id.rlNotifications);
        atvNotifications.setOnClickListener(this);

        atvPersonalInfo = (RelativeLayout) view.findViewById(R.id.rlEditInfo);
        atvPersonalInfo.setOnClickListener(this);

        atvChangePassword = (RelativeLayout) view.findViewById(R.id.rlChangePassword);
        atvChangePassword.setOnClickListener(this);

        atvPrivacy = (RelativeLayout) view.findViewById(R.id.rlPrivacy);
        atvPrivacy.setOnClickListener(this);

        atvHelp = (RelativeLayout) view.findViewById(R.id.rlHelp);
        atvHelp.setOnClickListener(this);

        atvLogout = (RelativeLayout) view.findViewById(R.id.rlLogout);
        atvLogout.setOnClickListener(this);
    }
    public void refresh() {
        vpProfile.setAdapter(null);
        vpProfile.setAdapter(mPagerAdapter);
        mPagerAdapter.notifyDataSetChanged();
        MainActivity.getInstance().refresh.setRefreshing(false);
    }
    @Override
    public void onResume() {
        super.onResume();
        user = User.getCurrentUser(getActivity().getApplicationContext());
        Gson gson = new Gson();
        String json = gson.toJson(user);
        bundle.putString(Constants.USER_DATA, json);
        mPagerAdapter.notifyDataSetChanged();
        Log.d(TAG, "user : " + user.toString());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlEditInfo:
                MyUtils.startActivity(getActivity(), EditProfileActivity.class, bundle);
                break;
            case R.id.rlNotifications:
                MyUtils.startActivity(getActivity(), NotificationProfileActivity.class, bundle);
                break;
            case R.id.rlChangePassword:
                MyUtils.startActivity(getActivity(), ChangePasswordActivity.class, bundle);
                break;
            case R.id.rlPrivacy:
                MyUtils.startActivity(getActivity(), PrivacyActivity.class, bundle);
                break;
            case R.id.rlHelp:
                break;
            case R.id.rlLogout:
                User.LogOut(getActivity().getApplicationContext());
                break;
        }
    }

    public class PagerAdapter extends FragmentStatePagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            Log.d(TAG, "position : " + position);
            switch (position) {
                case 0:
                    fragment = new AvatarProfileFragment();
                    ((MainActivity) getActivity()).profileFragment = fragment;
                    break;
                case 1:
                    fragment = new GoalProfileFragment();
                    break;
                case 2:
                    fragment = new SalesProfileFragment();
                    break;
            }
            assert fragment != null;
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getCount() {
            return DemoData.profile_layout.length;
        }
    }

}
