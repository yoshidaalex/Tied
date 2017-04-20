package com.tied.android.tiedapp.ui.fragments.signups;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.customs.MyAddressAsyncTask;
import com.tied.android.tiedapp.customs.ui.MyEditText;
import com.tied.android.tiedapp.objects.Coordinate;
import com.tied.android.tiedapp.objects.Location;
import com.tied.android.tiedapp.objects.responses.ServerRes;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.SignUpApi;
import com.tied.android.tiedapp.ui.activities.signups.SignUpActivity;
import com.tied.android.tiedapp.ui.dialogs.DialogUtils;
import com.tied.android.tiedapp.ui.listeners.FragmentIterationListener;
import com.tied.android.tiedapp.util.Logger;
import com.tied.android.tiedapp.util.MyUtils;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class HomeAddressFragment extends Fragment implements View.OnClickListener{

    public static final String TAG = HomeAddressFragment.class
            .getSimpleName();

    private FragmentIterationListener mListener;



    Bundle bundle;

    //    private Button continue_btn;
    private RelativeLayout continue_btn;

    // Reference to our image view we will use
    public ImageView img_user_picture;

    private MyEditText street, city, state, zip;
    private String cityText, stateText, streetText, zipText;
    private Location location;
    private View addButton, addressSection;


    int fetchType = Constants.USE_ADDRESS_NAME;

    public static Fragment newInstance(Bundle bundle) {
        Fragment fragment=new HomeAddressFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public HomeAddressFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_up_home_address, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initComponent(view);
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

    public void nextAction(int action, Bundle bundle) {
        if (mListener != null) {
            mListener.OnFragmentInteractionListener(action, bundle);
        }
    }

    public void initComponent(View view) {

        SignUpActivity.setStage(view, 7);
        street = (MyEditText) view.findViewById(R.id.street);
        street.setIsEditable(false);
        city = (MyEditText) view.findViewById(R.id.city);
        city.setIsEditable(false);
        state = (MyEditText) view.findViewById(R.id.state);
        state.setIsEditable(false);
        zip = (MyEditText) view.findViewById(R.id.zip);
        zip.setIsEditable(false);
        continue_btn = (RelativeLayout) view.findViewById(R.id.continue_btn);
        continue_btn.setOnClickListener(this);

        img_user_picture = (ImageView) view.findViewById(R.id.img_user_picture);
        addButton=view.findViewById(R.id.add_button);
        addButton.setOnClickListener(this);
        addressSection=view.findViewById(R.id.address_section);
        addressSection.setVisibility(View.GONE);
        addressSection.setOnClickListener(this);



        bundle = getArguments();
        MyUtils.initAvatar(bundle, img_user_picture);

    }

    public void continue_action() {
        if (validated()) {
            new GeocodeAsyncTask().execute();
        }else{
            MyUtils.showErrorAlert(getActivity(), "You must enter a valid address");
        }
    }

    private boolean validated() {
        streetText = street.getText().toString();
        cityText = city.getText().toString();
        zipText = zip.getText().toString();
        stateText = state.getText().toString();
        location = new Location(cityText, zipText, stateText, streetText);
        return (!streetText.equals("") && !cityText.isEmpty() && !stateText.isEmpty() && !zipText.isEmpty());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.continue_btn:
                continue_action();
                break;
            case R.id.address_section:
            case R.id.add_button:
                MyUtils.showAddressDialog(getActivity(), "Office Address", location, new MyUtils.MyDialogClickListener() {
                    @Override
                    public void onClick(Object response) {
                        HomeAddressFragment.this.location=(Location)response;
                        Logger.write(location.getStreet());
                        addressSection.setVisibility(View.VISIBLE);

                        street.setText(location.getStreet());
                        city.setText(location.getCity());
                        state.setText(location.getState());
                        zip.setText(location.getZip());

                        addressSection.setVisibility(View.VISIBLE);


                    }
                });
                break;
        }
    }

    class GeocodeAsyncTask extends MyAddressAsyncTask {

        String errorMessage = "";
        JSONObject jObject;
        JSONObject places = null;
        String lat;

        @Override
        protected void onPreExecute() {
            DialogUtils.displayProgress(getActivity());
        }

        @Override
        protected Address doInBackground(Void... params) {
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
            if (getActivity() == null) return;
            if (address != null) {
                Coordinate coordinate = new Coordinate(address.getLatitude(), address.getLongitude());
                location.setCoordinate(coordinate);
            }

            Bundle bundle = getArguments();

            Gson gson = new Gson();
            String user_json = bundle.getString(Constants.USER_DATA);
            final User user = gson.fromJson(user_json, User.class);
            user.setHome_address(location);

            Call<ServerRes> response = MainApplication.createService(SignUpApi.class).updateUser(user);
            response.enqueue(new Callback<ServerRes>() {
                @Override
                public void onResponse(Call<ServerRes> call, Response<ServerRes> ServerResponseResponse) {
                    if (getActivity() == null) return;
                    try {
                        ServerRes ServerRes = ServerResponseResponse.body();
                        Log.d(TAG + " onFailure", ServerResponseResponse.body().toString());
                        if (ServerRes.isSuccess()) {
                            Bundle bundle = new Bundle();
                            boolean saved = user.save(getActivity().getApplicationContext());
                            if (saved) {
                                Gson gson = new Gson();
                                String json = gson.toJson(user);
                                bundle.putString(Constants.USER_DATA, json);
                                DialogUtils.closeProgress();
                                nextAction(Constants.Picture, bundle);

                                Log.d(TAG, "location: " + json);
                            } else {
                                DialogUtils.closeProgress();
                               // MyUtils.showToast("user info  was not updated");
                            }
                        } else {
                            DialogUtils.closeProgress();
                            MyUtils.showErrorAlert(getActivity(), ServerRes.getMessage());
                        }
                    }catch (Exception e) {
                        MyUtils.showToast(getString(R.string.connection_error));
                    }
                }


                @Override
                public void onFailure(Call<ServerRes> ServerResponseCall, Throwable t) {
                    MyUtils.showToast("On failure : error encountered");
                    Log.d(TAG +" onFailure", t.toString());
                    DialogUtils.closeProgress();
                }
            });
        }
    }
}
