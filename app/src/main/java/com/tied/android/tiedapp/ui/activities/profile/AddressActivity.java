package com.tied.android.tiedapp.ui.activities.profile;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.customs.MyListAsyncTask;
import com.tied.android.tiedapp.objects.Coordinate;
import com.tied.android.tiedapp.objects.Location;
import com.tied.android.tiedapp.objects.responses.ServerRes;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.SignUpApi;
import com.tied.android.tiedapp.ui.dialogs.DialogUtils;
import com.tied.android.tiedapp.ui.listeners.FragmentIterationListener;
import com.tied.android.tiedapp.util.MyUtils;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Emmanuel on 6/22/2016.
 */
public class AddressActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = AddressActivity.class
            .getSimpleName();

    public FragmentIterationListener mListener;

    private EditText etOfficeZipCode, etOfficeStreetAddress, etOfficeCity, etHomeZipCode, etHomeStreetAddress, etHomeCity;
    private String office_zipName, office_streetName, office_cityName, home_zipName, home_streetName, home_cityName;

    private Bundle bundle;
    private User user;

    private Location office_location, home_location;

    int fetchType = Constants.USE_ADDRESS_NAME;

    private Button btnSaveChange;
    ImageView btnBack;
    TextView txt_save;
    Context context;

    public Retrofit retrofit;
    public SignUpApi service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_change_address);

        context = this;
        retrofit = MainApplication.getInstance().getRetrofit();
        service = retrofit.create(SignUpApi.class);
        initComponent();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_close : case R.id.back_layout:
                onBackPressed();
                break;
            case R.id.txt_save:
                confirmEdit();
                break;
        }
    }

    public void initComponent() {
        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText(getResources().getString(R.string.address));
        btnBack = (ImageView) findViewById(R.id.img_close);
        btnBack.setOnClickListener(this);

        etHomeZipCode = (EditText) findViewById(R.id.etHomeZipCode);
        etHomeStreetAddress = (EditText) findViewById(R.id.etHomeStreetAddress);
        etHomeCity = (EditText) findViewById(R.id.etHomeCity);

        etOfficeZipCode = (EditText) findViewById(R.id.etOfficeZipCode);
        etOfficeStreetAddress = (EditText) findViewById(R.id.etOfficeStreetAddress);
        etOfficeCity = (EditText) findViewById(R.id.etOfficeCity);

        bundle = getIntent().getExtras();
        if (bundle != null) {
            Log.d(TAG, "bundle not null");
            Gson gson = new Gson();
            String user_json = bundle.getString(Constants.USER_DATA);
            user = gson.fromJson(user_json, User.class);

            office_location = user.getOffice_address();
            Log.d(TAG, office_location.toString());
            if (office_location != null) {
                etOfficeZipCode.setText(office_location.getZip());
                etOfficeStreetAddress.setText(office_location.getStreet());
                etOfficeCity.setText(office_location.getCity());
            }

            home_location = user.getHome_address();
            if (home_location != null) {
                etHomeZipCode.setText(home_location.getZip());
                etHomeStreetAddress.setText(home_location.getStreet());
                etHomeCity.setText(home_location.getCity());
            }
        }
    }

    public boolean validate() {
        office_streetName = etOfficeStreetAddress.getText().toString();
        office_cityName = etOfficeCity.getText().toString();
        office_zipName = etOfficeZipCode.getText().toString();

        home_streetName = etHomeStreetAddress.getText().toString();
        home_cityName = etHomeCity.getText().toString();
        home_zipName = etHomeZipCode.getText().toString();

        return (office_streetName != null);
    }

    private void confirmEdit() {
        if (validate()) {
            DialogUtils.displayProgress(this);
            office_location.setStreet(office_streetName);
            office_location.setCity(office_cityName);
            office_location.setZip(office_zipName);

            home_location.setStreet(home_streetName);
            home_location.setCity(home_cityName);
            home_location.setZip(home_zipName);
            new GeocodeAsyncTask().execute();
        }
    }

    class GeocodeAsyncTask extends MyListAsyncTask {

        String errorMessage = "";
        JSONObject jObject;
        JSONObject places = null;
        String lat;

        @Override
        protected void onPreExecute() {
            DialogUtils.displayProgress(context);
        }

        @Override
        protected List<Address> doInBackground(Void... params) {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = new ArrayList<Address>();

            if (fetchType == Constants.USE_ADDRESS_NAME) {
                try {
                    Log.d(TAG, office_location.getLocationAddress());
                    addresses.add(geocoder.getFromLocationName(office_location.getLocationAddress(), 1).get(0));
                    addresses.add(geocoder.getFromLocationName(home_location.getLocationAddress(), 1).get(0));
                } catch (IOException e) {
                    errorMessage = "Service not available";
                    Log.e(TAG, errorMessage, e);
                }
            } else {
                errorMessage = "Unknown Type";
                Log.e(TAG, errorMessage);
            }

            if (addresses != null && addresses.size() > 0) {
                Log.d(TAG, addresses.toString());
                return addresses;
            }

            return null;
        }

        protected void onPostExecute(List<Address> addresses) {
            if (addresses != null && addresses.get(0) != null) {
                Coordinate coordinate = new Coordinate(addresses.get(0).getLatitude(), addresses.get(0).getLongitude());
                office_location.setCoordinate(coordinate);
            }

            if (addresses != null && addresses.get(1) != null) {
                Coordinate coordinate = new Coordinate(addresses.get(1).getLatitude(), addresses.get(1).getLongitude());
                home_location.setCoordinate(coordinate);
            }

            user.setOffice_address(office_location);
            user.setHome_address(home_location);

            SignUpApi signUpApi = service;
            Call<ServerRes> response = signUpApi.updateUser(user);
            response.enqueue(new Callback<ServerRes>() {
                @Override
                public void onResponse(Call<ServerRes> call, Response<ServerRes> ServerResponseResponse) {
                    ServerRes ServerRes = ServerResponseResponse.body();
                    if (ServerRes.isAuthFailed()) {
                        DialogUtils.closeProgress();
                        User.LogOut(context);
                    } else if (ServerRes.isSuccess()) {
                        Bundle bundle = new Bundle();
                        boolean saved = user.save(context.getApplicationContext());
                        if (saved) {
                            Gson gson = new Gson();
                            String json = gson.toJson(user);
                            bundle.putString(Constants.USER_DATA, json);
                            DialogUtils.closeProgress();
                            MyUtils.showMessageAlert(AddressActivity.this, "Information successfully updated");
//                            MyUtils.startActivity(AddressActivity.this, EditProfileActivity.class, bundle);
                        } else {
                            DialogUtils.closeProgress();
                            Toast.makeText(context, "user info  was not updated", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(context, ServerRes.getMessage(), Toast.LENGTH_LONG).show();
                    }
                    DialogUtils.closeProgress();
                }

                @Override
                public void onFailure(Call<ServerRes> ServerResponseCall, Throwable t) {
                    Toast.makeText(context, "On failure : error encountered", Toast.LENGTH_LONG).show();
                    Log.d(TAG + " onFailure", t.toString());
                    DialogUtils.closeProgress();
                }
            });
        }
    }
}
