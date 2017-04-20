package com.tied.android.tiedapp.ui.fragments.schedule;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.johnhiott.darkskyandroidlib.RequestBuilder;
import com.johnhiott.darkskyandroidlib.models.Request;
import com.johnhiott.darkskyandroidlib.models.WeatherResponse;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.Location;
import com.tied.android.tiedapp.objects.client.Client;
import com.tied.android.tiedapp.objects.schedule.Schedule;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.ui.activities.MainActivity;
import com.tied.android.tiedapp.util.HelperMethods;
import com.tied.android.tiedapp.util.MyUtils;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Emmanuel on 7/15/2016.
 */
public class ViewScheduleFragment extends Fragment implements View.OnClickListener,OnMapReadyCallback {

    public static final String TAG = ViewScheduleFragment.class
            .getSimpleName();

    GoogleMap myMap;
    private Location mLocation;
    private User user;
    private Bundle bundle;
    private Client client;
    private Schedule schedule;

    private LinearLayout back_layout;

    private TextView description, temperature, title, schedule_title;
    private View line;


    public static Fragment newInstance(Bundle bundle) {
        Fragment fragment=new ViewScheduleFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule_view, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initComponent(view);
    }

    public void initComponent(View view) {

        back_layout = (LinearLayout) view.findViewById(R.id.back_layout);
        back_layout.setOnClickListener(this);

        bundle = getArguments();
        if (bundle != null) {
            Log.d(TAG, "bundle not null");
            Gson gson = new Gson();
            String user_json = bundle.getString(Constants.USER_DATA);
            String client_json = bundle.getString(Constants.CLIENT_DATA);
            String schedule_json = bundle.getString(Constants.SCHEDULE_DATA);
            user = gson.fromJson(user_json, User.class);
            client = gson.fromJson(client_json, Client.class);
            schedule = gson.fromJson(schedule_json, Schedule.class);
        }


        MapFragment mapFragment = (MapFragment) getActivity().getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        schedule_title = (TextView) view.findViewById(R.id.schedule_title);
        description = (TextView) view.findViewById(R.id.description);
        //title = (TextView) view.findViewById(R.id.title);
        temperature = (TextView) view.findViewById(R.id.weather);
        line = (View) view.findViewById(R.id.line);

        //title.setText(schedule.getTitle());
        schedule_title.setText(schedule.getTitle());
        description.setText(schedule.getDescription());

        final RequestBuilder weather = new RequestBuilder();
        Request request = new Request();
        request.setLat("32.00");
        request.setLng("-81.00");
        request.setUnits(Request.Units.US);
        request.setLanguage(Request.Language.ENGLISH);
        request.addExcludeBlock(Request.Block.CURRENTLY);

        weather.getWeather(request, new Callback<WeatherResponse>() {
            @Override
            public void success(WeatherResponse weatherResponse, Response response) {
                Log.d(TAG, "temperature : "+weatherResponse.getDaily().getData().get(0).getApparentTemperatureMax()+"");
                int temp_max = (int) weatherResponse.getDaily().getData().get(0).getApparentTemperatureMax();
                temp_max = (int) HelperMethods.convertFahrenheitToCelcius(temp_max);
                temperature.setText(temp_max+"°");
            }
            @Override
            public void failure(RetrofitError retrofitError) {
                Log.d(TAG, "Error while calling: " + retrofitError.getUrl());
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.avatar:

                break;
            case R.id.back_layout:
                MyUtils.startActivity(getActivity(), MainActivity.class);
                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        myMap = googleMap;

        myMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

        LatLng MELBOURNE = new LatLng(-37.81319, 144.96298);
        Marker melbourne = myMap.addMarker(new MarkerOptions()
                .position(MELBOURNE)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(MELBOURNE,15));

        myMap.setInfoWindowAdapter(new MyInfoWindowAdapter());
        melbourne.showInfoWindow();
    }


    class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private final View myContentsView;
        boolean not_first_time_showing_info_window;

        MyInfoWindowAdapter(){
            myContentsView = getActivity().getLayoutInflater().inflate(R.layout.schedule_map_info_window, null);
        }

        @Override
        public View getInfoContents(Marker marker) {


            TextView line = ((TextView)myContentsView.findViewById(R.id.line));
            TextView address = ((TextView)myContentsView.findViewById(R.id.address));
            TextView distance = ((TextView)myContentsView.findViewById(R.id.distance));
            final ImageView image = ((ImageView) myContentsView.findViewById(R.id.image));

            line.setText(client.getCompany());
            address.setText(client.getAddress().getStreet());
            distance.setText("0.2 miles");

            Picasso.with(getActivity()).
                    load(Constants.GET_AVATAR_ENDPOINT+"avatar_"+user.getId()+".jpg")
                    .resize(35,35)
                    .into(new Target() {
                        @Override public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            if (bitmap != null){
                                image.setImageBitmap(bitmap);
                            }else{
                                image.setImageResource(R.mipmap.default_avatar);
                            }
                        }
                        @Override public void onBitmapFailed(Drawable errorDrawable) { }
                        @Override public void onPrepareLoad(Drawable placeHolderDrawable) { }
                    });

            return myContentsView;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            // TODO Auto-generated method stub
            return null;
        }

    }
}
