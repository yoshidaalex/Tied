package com.tied.android.tiedapp.ui.fragments.client;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.squareup.picasso.Transformation;
import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.customs.MyStringAsyncTask;
import com.tied.android.tiedapp.objects.Coordinate;
import com.tied.android.tiedapp.objects.client.Client;
import com.tied.android.tiedapp.objects.client.ClientFilter;
import com.tied.android.tiedapp.objects.client.ClientLocation;
import com.tied.android.tiedapp.objects.responses.ClientRes;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.ClientApi;
import com.tied.android.tiedapp.ui.activities.MainActivity;
import com.tied.android.tiedapp.ui.activities.client.ActivityClientProfile;
import com.tied.android.tiedapp.ui.activities.client.MapClientList;
import com.tied.android.tiedapp.ui.dialogs.DialogUtils;
import com.tied.android.tiedapp.util.CircularImageView;
import com.tied.android.tiedapp.util.Logger;
import com.tied.android.tiedapp.util.MyUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by femi on 8/10/2016.
 */
public class ClientsMapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, View.OnClickListener{

    MapFragment mMapFragment;
    String TAG = "ACTIVIY_CLINET";
    Map<String, Client> markerClientMap = new HashMap<String, Client>();
    User user;
    ArrayList<Client> clients=new ArrayList<>();
    GoogleMap googleMap;
    ImageView img_list_clients;
    Bundle bundle;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_client_map_layout, container, false);
        return view;
    }

    public static ClientsMapFragment newInstance(Bundle bundle, ArrayList<Client> clients) {
        ClientsMapFragment cmf=new ClientsMapFragment();
        cmf.setArguments(bundle);
        cmf.setClients(clients);
        return cmf;
    }

    public void setClients(ArrayList<Client> clients) {
        this.clients = clients;
    }
    public void addClients(ArrayList<Client> clients) {
        this.clients.addAll( clients);
    }
    ImageView i;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initComponent(view);
        i=new ImageView(getActivity());
    }

    public void initComponent(View view) {
        bundle=getArguments();
        user=MyUtils.getUserFromBundle(bundle);
        mMapFragment = MapFragment.newInstance();
        FragmentTransaction fragmentTransaction =
                getActivity().getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, mMapFragment);
        fragmentTransaction.commit();

        mMapFragment.getMapAsync(this);
       // user = MyUtils.getUserLoggedIn();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity()) == ConnectionResult.SUCCESS) {
            System.gc();
            googleMap.clear();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity.getInstance().refresh.setEnabled(false);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Setting a custom info window adapter for the google map
        this.googleMap = googleMap;
        googleMap.setOnMarkerClickListener(this);
        googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            // Use default InfoWindow frame
            @Override
            public View getInfoContents(Marker arg0) {
                return null;
            }

            // Defines the contents of the InfoWindow
            @Override
            public View getInfoWindow(Marker marker) {

                //Logger.write(marker.getTag().toString());
                ContextThemeWrapper cw = new ContextThemeWrapper(
                       getActivity().getApplicationContext(), R.style.Transparent);

                //Client client=markerClientMap.get((String)marker.getTag());
                Client client;
                try {
                    client = clients.get((int) marker.getTag());
                    if (client == null) return null;
                }catch (Exception e) {
                    return null;
                }

                // Getting view from the layout file info_window_layout
                LayoutInflater inflater = (LayoutInflater) cw
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View v = inflater.inflate(R.layout.map_marker_info_window, null);

                ImageView photo = (ImageView) v.findViewById(R.id.user_picture_iv);
                MyUtils.Picasso.displayImage(client.getLogo(), photo);
                // Getting the position from the marker
                //LatLng latLng = arg0.getPosition();

                // Getting reference to the TextView to set latitude
                TextView tvName = (TextView) v.findViewById(R.id.full_name_tv);

                // Getting reference to the TextView to set longitude
                TextView tvAddress = (TextView) v.findViewById(R.id.address_tv);

                // Getting reference to the TextView to set longitude
                TextView tvDistance = (TextView) v.findViewById(R.id.distance);
                TextView totalEarnings = (TextView) v.findViewById(R.id.total_earnings);
                TextView lastVisited = (TextView) v.findViewById(R.id.last_visited);

                tvName.setText(client.getFull_name());
                tvAddress.setText(client.getAddress().getStreet() + " " + client.getAddress().getCity());

                totalEarnings.setText("Total Earnings: "+MyUtils.moneyFormat(client.getTotal_revenue()));
                try {
                    lastVisited.setText("Last Visited: " + MyUtils.parseDate(client.getLast_visited()));
                }catch (Exception e) {

                }
        Logger.write(MyUtils.getCurrentLocation()+"  "+client.getAddress().getCoordinate());

                tvDistance.setText(MyUtils.getDistance(MyUtils.getCurrentLocation(), client.getAddress().getCoordinate()));

                // Returning the view containing InfoWindow contents
                return v;

            }
        });

        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                                                   @Override
                                                   public void onInfoWindowClick(Marker marker) {
                                                       try {
                                                           Client client = clients.get((int) marker.getTag());
                                                           Bundle bundle=new Bundle();
                                                           bundle.putSerializable(Constants.CLIENT_DATA, client);
                                                           bundle.putSerializable(Constants.USER_DATA, user);
                                                           MyUtils.startActivity(getActivity(), ActivityClientProfile.class, bundle);
                                                       }catch (Exception e) {

                                                       }
                                                   }
                                               });

                // Adding and showing marker while touching the GoogleMap
        /*googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng arg0) {
                // Clears any existing markers from the GoogleMap
              //  googleMap.clear();

                // Creating an instance of MarkerOptions to set position
               // MarkerOptions markerOptions = new MarkerOptions();

                // Setting position on the MarkerOptions
              //  markerOptions.position(arg0);

                // Animating to the currently touched position
               // googleMap.animateCamera(CameraUpdateFactory.newLatLng(arg0));

                // Adding marker on the GoogleMap
                //Marker marker = googleMap.addMarker(markerOptions);

                // Showing InfoWindow on the GoogleMap
               // marker.showInfoWindow();

            }
        });*/
        Coordinate currentLocation= new Coordinate(39.9001126, -75.2890745);//clientFilter.getCoordinate();
        LatLng loc=new LatLng(currentLocation.getLat(), currentLocation.getLon());
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 11.5f));
        Marker marker=googleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
                .position(loc));
        marker.setZIndex(10000.0f);
        //marker.showInfoWindow();
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(loc));
        googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            public void onMapLoaded() {
                //do stuff here
                loadClientsFilter(MapAndListFragment.getInstance().clientFilter);
            }
        });

    }

    public int pageNumber=1;
    private int numPages=1;
    public void loadClientsFilter(final ClientFilter clientFilter) {
        if(clientFilter.getDistance()==MapAndListFragment.distance) clientFilter.setDistance(0);
        ClientApi clientApi = MainApplication.createService(ClientApi.class);
        Call<ClientRes> response = clientApi.getClientsFilter(user.getId(), pageNumber, clientFilter);
        response.enqueue(new Callback<ClientRes>() {
            @Override
            public void onResponse(Call<ClientRes> call, Response<ClientRes> resResponse) {
                if (this == null) return;
                DialogUtils.closeProgress();
                ClientRes clientRes = resResponse.body();
                Logger.write(clientRes.toString());
                if (clientRes.isAuthFailed()) {
                    User.LogOut(getActivity().getApplicationContext());
                } else if (clientRes.get_meta() != null && clientRes.get_meta().getStatus_code() == 200) {
                    numPages=clientRes.get_meta().getPage_count();
                   if(pageNumber==1) {
                       clients.clear();
                       googleMap.clear();
                   }

                    final ArrayList<Client> newClients= clientRes.getClients();
                    clients.addAll(newClients);

                    if(clients.size()==0) {
                        MyUtils.showToast("No clients found");
                        return;
                    }
                   /* Coordinate currentLocation= new Coordinate(39.9001126, -75.2890745);//clientFilter.getCoordinate();
                    LatLng loc=new LatLng(currentLocation.getLat(), currentLocation.getLon());
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 10.0f));
                    googleMap.addMarker(new MarkerOptions()
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                            .position(loc));
                    googleMap.animateCamera(CameraUpdateFactory.newLatLng(loc));
*/

                    boolean centered = false;

                    new MyStringAsyncTask() {
                        @Override
                        protected String doInBackground(Void... params) {
                            int i = -1;
                            for (final Client client : newClients) {

                                Logger.write(client.getLogo());
                                try {

                                    final LatLng latLng = new LatLng(client.getAddress().getCoordinate().getLat(),
                                            client.getAddress().getCoordinate().getLon());
                                    i++;
                                    final int num=i;
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            View marker = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker_layout, null);

                                            ImageView pic = (ImageView) marker.findViewById(R.id.pic);
                                            MyUtils.Picasso.displayImage(client.getLogo(), pic);

                                            Marker nMarker= googleMap.addMarker(new MarkerOptions().anchor(0.5f, 1.5f).icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(getActivity(), marker)))
                                                    .position(latLng));
                                            nMarker.setTag(num);
                                        }
                                    });


                                }catch (NullPointerException npe) {
                                    continue;
                                }
                                try{
                                    Thread.sleep(50);
                                }catch (Exception e) {

                                }

                            }
                            if(pageNumber<numPages) {
                                pageNumber++;
                                loadClientsFilter(clientFilter);
                            }
                            return super.doInBackground(params);
                        }
                    }.execute();


                } else {
                    Logger.write(clientRes.getMessage());
                    MyUtils.showToast(getString(R.string.connection_error));
                }
                // Log.d(TAG + " onResponse", resResponse.body().toString());
            }

            @Override
            public void onFailure(Call<ClientRes> call, Throwable t) {
                Log.d(TAG + "onFailure", t.toString());
                DialogUtils.closeProgress();
            }
        });
    }
    public void loadClientsFilter(ClientFilter clientFilter, int pageNumber) {
        this.pageNumber=pageNumber;
        loadClientsFilter(clientFilter);
    }


    // Convert a view to bitmap
    public static Bitmap createDrawableFromView(Context context, View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        marker.showInfoWindow();
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_list_clients:
                MyUtils.startActivity(getActivity(), MapClientList.class);
                break;

        }
    }

    class MarkerCallback implements com.squareup.picasso.Callback {
        Marker marker=null;

        MarkerCallback(Marker marker) {
            this.marker=marker;
        }

        @Override
        public void onError() {
            marker.showInfoWindow();
            Log.e(getClass().getSimpleName(), "Error loading thumbnail!");
        }

        @Override
        public void onSuccess() {
            if (marker != null && marker.isInfoWindowShown()) {
                marker.hideInfoWindow();
                marker.showInfoWindow();
            }
        }
    }

    private GoogleMap.OnMarkerClickListener userMarkerClickListener = new GoogleMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {
            // show dialog
            marker.showInfoWindow();
            return true;
        }
    };
    class MyPicassoTarget {
        Client client; Marker marker; int tag;
        public MyPicassoTarget( final Client client, final Marker marker, int tag) {
           this.client=client; this.marker=marker; this.tag=tag;
            Target target = new Target() {
                @Override
                public void onBitmapLoaded(final  Bitmap bitmap, Picasso.LoadedFrom from) {
                    Logger.write("Image loaded " + bitmap.getWidth());
                    getView().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Marker nMarker=googleMap.addMarker(new MarkerOptions().anchor(0.5f, 1.5f).icon(BitmapDescriptorFactory.fromBitmap(bitmap))
                                    .position(MyPicassoTarget.this.marker.getPosition()));
                            nMarker.setTag(MyPicassoTarget.this.tag);
                        }
                    }, 300);
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                    Marker marker
                            =  googleMap.addMarker(new MarkerOptions().anchor(0.5f, 1.5f).icon(BitmapDescriptorFactory.fromResource(R.drawable.avatar_profile))
                            .position(MyPicassoTarget.this.marker.getPosition()));

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                }
            };

            Picasso.with(getActivity()).load(client.getLogo()).centerCrop().error(R.drawable.avatar_profile).transform(new CircleTransform()).resize(100, 100).into(target);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        MainActivity.getInstance().refresh.setEnabled(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        MainActivity.getInstance().refresh.setEnabled(true);
    }

    public static class CircleTransform implements Transformation {
        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());

            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
            if (squaredBitmap != source) {
                source.recycle();
            }

            Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            BitmapShader shader = new BitmapShader(squaredBitmap,
                    BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setAntiAlias(true);

            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);

            squaredBitmap.recycle();
            return bitmap;
        }

        @Override
        public String key() {
            return "circle";
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.ClientFilter && resultCode == Activity.RESULT_OK) {
            if (MapAndListFragment.isClientFilter) {
                //loadClientsFilter(MapAndListFragment.search_name);
            } else if (MapAndListFragment.isClear) {
                //loadClients(googleMap);
            }
        }
    }
}
