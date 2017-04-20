package com.tied.android.tiedapp.ui.fragments.signups;

import android.content.Context;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.customs.MyAddressAsyncTask;
import com.tied.android.tiedapp.objects.Coordinate;
import com.tied.android.tiedapp.objects.Location;
import com.tied.android.tiedapp.objects.user.Boss;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.ui.dialogs.DialogUtils;
import com.tied.android.tiedapp.ui.listeners.FragmentIterationListener;
import com.tied.android.tiedapp.util.MyUtils;
import com.tied.android.tiedapp.util.Utility;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * A placeholder fragment containing a simple view.
 */
public class AddBossFragment extends Fragment implements View.OnClickListener{

    public static final String TAG = AddBossFragment.class
            .getSimpleName();

    private EditText email,phone;
    private RelativeLayout continue_btn;

    private ImageView img_sms, img_email, img_no_invite;
    int type_index;

    private EditText street, city, state, zip, txt_territory;
    private String cityText, stateText, streetText, zipText;
    private String emailText, phoneText, territoryText;

    int fetchType = Constants.USE_ADDRESS_NAME;
    private FragmentIterationListener mListener;

    LinearLayout sms_layout, email_layout, no_invite_layout;
    TextView txt_sms, txt_email1, txt_no_invite;

    private Boss boss;
    private Location location;

    Bundle bundle;
    // Reference to our image view we will use
    public ImageView img_user_picture;


    public static Fragment newInstance(Bundle bundle) {
        Fragment fragment=new AddBossFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    public AddBossFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_up_add_boss, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initComponent(view);
    }


    public void initComponent(View view){

        txt_territory = (EditText) view.findViewById(R.id.txt_territory);
        street = (EditText) view.findViewById(R.id.street);
        city = (EditText) view.findViewById(R.id.city);
        state = (EditText) view.findViewById(R.id.state);
        zip = (EditText) view.findViewById(R.id.zip);

        email = (EditText) view.findViewById(R.id.email);
        phone = (EditText) view.findViewById(R.id.phone);

        img_sms = (ImageView) view.findViewById(R.id.img_sms);
        img_email = (ImageView) view.findViewById(R.id.img_email);
        img_no_invite = (ImageView) view.findViewById(R.id.img_no_invite);

        txt_sms = (TextView) view.findViewById(R.id.txt_sms);
        txt_email1 = (TextView) view.findViewById(R.id.txt_email1);
        txt_no_invite = (TextView) view.findViewById(R.id.txt_no_invite);

        continue_btn = (RelativeLayout) view.findViewById(R.id.continue_btn);
        continue_btn.setOnClickListener(this);

        img_user_picture = (ImageView) view.findViewById(R.id.img_user_picture);

        bundle = getArguments();
        MyUtils.initAvatar(bundle, img_user_picture);

        sms_layout = (LinearLayout) view.findViewById(R.id.sms_layout);
        sms_layout.setOnClickListener(this);

        email_layout = (LinearLayout) view.findViewById(R.id.email_layout);
        email_layout.setOnClickListener(this);

        no_invite_layout = (LinearLayout) view.findViewById(R.id.no_invite_layout);
        no_invite_layout.setOnClickListener(this);

        type_index = 0;
        setSelectType(0);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentIterationListener) {
            mListener = (FragmentIterationListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }


    public void nextAction(Bundle bundle) {
        if (mListener != null) {
            mListener.OnFragmentInteractionListener(Constants.AddBossNow, bundle);
        }
    }


    public boolean validated(){
        streetText = street.getText().toString();
        cityText = city.getText().toString();
        zipText = zip.getText().toString();
        stateText = state.getText().toString();
        location = new Location(cityText, zipText, stateText, streetText);

        emailText = email.getText().toString();
        phoneText = phone.getText().toString();
        territoryText = txt_territory.getText().toString();

        boolean validated = false;
        if(!emailText.equals("")){
            if (!Utility.isEmailValid(emailText)) {
                MyUtils.showToast("Not a valid email");
            } else {
                validated = MyUtils.locationValidation(location);
            }
        }

        return validated;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.continue_btn:
                continue_action();
                break;
            case R.id.sms_layout:
                setSelectType(0);
                break;
            case R.id.email_layout:
                setSelectType(1);
                break;
            case R.id.no_invite_layout:
                setSelectType(2);
                break;
        }
    }

    class GeocodeAsyncTask extends MyAddressAsyncTask {

        String errorMessage = "";

        @Override
        protected void onPreExecute() {
            DialogUtils.displayProgress(getActivity());
        }

        @Override
        protected Address doInBackground(Void... none) {
            Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
            List<Address> addresses = null;

            if (fetchType == Constants.USE_ADDRESS_NAME) {
                try {
                    Log.d(TAG, location.getLocationAddress());
                    addresses = geocoder.getFromLocationName(location.getLocationAddress(), 1);
                } catch (IOException e) {
                    errorMessage = "Service not available";
                    Log.e(TAG, errorMessage, e);
                }
            } else {
                errorMessage = "Unknown Type";
                Log.e(TAG, errorMessage);
            }

            if (addresses != null && addresses.size() > 0)
                return addresses.get(0);

            return null;
        }

        protected void onPostExecute(Address address) {
            if(getActivity() == null) return;
            if (address != null) {
                Coordinate coordinate = new Coordinate(address.getLatitude(), address.getLongitude());
                location.setCoordinate(coordinate);
            }

            boss = new Boss(emailText, phoneText, territoryText, location);

            Bundle bundle = getArguments();

            Gson gson = new Gson();
            String user_json = bundle.getString(Constants.USER_DATA);
            final User user = gson.fromJson(user_json, User.class);
            user.setBoss(boss);
            user.setSign_up_stage(Constants.AddBossNow);
            user.save(getActivity().getApplicationContext());
            String json = gson.toJson(user);
            bundle.putString(Constants.USER_DATA, json);
            DialogUtils.closeProgress();
            nextAction(bundle);
        }
    }

    public void continue_action() {
        if (validated()) {
            new GeocodeAsyncTask().execute();
        }
    }

    private void setSelectType(int index) {
        if(index == 0) {
            img_sms.setBackgroundResource(R.mipmap.dot_checked_icon);
            img_email.setBackgroundResource(R.mipmap.dot_unchecked_icon);
            img_no_invite.setBackgroundResource(R.mipmap.dot_unchecked_icon);

            txt_sms.setTextColor(Color.WHITE);
            txt_email1.setTextColor(getResources().getColor(R.color.text_disable_color));
            txt_no_invite.setTextColor(getResources().getColor(R.color.text_disable_color));
        } else if (index == 1){
            img_sms.setBackgroundResource(R.mipmap.dot_unchecked_icon);
            img_email.setBackgroundResource(R.mipmap.dot_checked_icon);
            img_no_invite.setBackgroundResource(R.mipmap.dot_unchecked_icon);

            txt_sms.setTextColor(getResources().getColor(R.color.text_disable_color));
            txt_email1.setTextColor(Color.WHITE);
            txt_no_invite.setTextColor(getResources().getColor(R.color.text_disable_color));
        } else {
            img_sms.setBackgroundResource(R.mipmap.dot_unchecked_icon);
            img_email.setBackgroundResource(R.mipmap.dot_unchecked_icon);
            img_no_invite.setBackgroundResource(R.mipmap.dot_checked_icon);

            txt_sms.setTextColor(getResources().getColor(R.color.text_disable_color));
            txt_email1.setTextColor(getResources().getColor(R.color.text_disable_color));
            txt_no_invite.setTextColor(Color.WHITE);
        }
    }
}
