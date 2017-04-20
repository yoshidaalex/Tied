package com.tied.android.tiedapp.ui.fragments.client;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.Coordinate;
import com.tied.android.tiedapp.objects.Line;
import com.tied.android.tiedapp.objects.Territory;
import com.tied.android.tiedapp.objects.client.Client;
import com.tied.android.tiedapp.objects.client.ClientFilter;
import com.tied.android.tiedapp.objects.client.ClientLocation;
import com.tied.android.tiedapp.objects.responses.ClientRes;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.ClientApi;
import com.tied.android.tiedapp.ui.activities.MainActivity;
import com.tied.android.tiedapp.ui.activities.client.ClientFilterActivity;
import com.tied.android.tiedapp.ui.dialogs.DialogUtils;
import com.tied.android.tiedapp.util.Logger;
import com.tied.android.tiedapp.util.MyUtils;
import retrofit2.Call;
import retrofit2.Response;

import java.util.ArrayList;

/**
 * Created by Emmanuel on 7/1/2016.
 */
public class MapAndListFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = MapAndListFragment.class
            .getSimpleName();

    public ViewPager mViewPager;
    public PagerAdapter mPagerAdapter;
    private Bundle bundle;
    private ClientsMapFragment clientsMapFragment;
    public  ClientFilter clientFilter = new ClientFilter();


    public static ArrayList<String> selectedLines = new ArrayList<String>();
    public static ArrayList<Territory> selectedTerritories = new ArrayList<Territory>();
    ClientsListFragment clientsListFragment;
    LinearLayout parent, back_layout;
    ImageView img_segment, img_filter, app_icon;
    User user;
    public static String search_name = "";
    public static int distance = 2000;
    public static String group = "me";
    public static int last_visited = 0;
    public static String orderby = "distance";
    public static String order = "asc";
    public static boolean isClientFilter = false;
    public static boolean isClear = false;
    private static MapAndListFragment mapAndListFragment;
    ArrayList<Client> clients;

    boolean bMap = true;
    EditText search;
    TextView search_button;
    Fragment fragment = null;

    public static Fragment newInstance(Bundle bundle) {
        Fragment fragment = new MapAndListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
    public static MapAndListFragment getInstance() {
        return mapAndListFragment;
    }
    public static int getDefaultDistance() {
        return distance;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeFilter();
        mapAndListFragment=this;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_client_list_layout, container, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getActivity().getResources().getColor(R.color.blue_status_bar));
        }

        initComponent(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void initComponent(View view) {

        bundle = getArguments();

        app_icon = (ImageView) view.findViewById(R.id.app_icon);
        app_icon.setVisibility(View.VISIBLE);

        parent = (LinearLayout) view.findViewById(R.id.parent);
        parent.setBackgroundResource(R.drawable.background_blue);

        back_layout = (LinearLayout) view.findViewById(R.id.back_layout);
        back_layout.setVisibility(View.GONE);

        img_segment = (ImageView) view.findViewById(R.id.img_segment);
        img_segment.setOnClickListener(this);

        img_filter = (ImageView) view.findViewById(R.id.img_filter);
        img_filter.setOnClickListener(this);

        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);

        mPagerAdapter = new PagerAdapter(getChildFragmentManager());
        if (mViewPager != null) {
            mViewPager.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mViewPager.setAdapter(mPagerAdapter);
                    if (bundle.getBoolean(Constants.CLIENT_LIST)){
                        mViewPager.setCurrentItem(1);
                        selectTab(1);
                    }else{
                        mViewPager.setCurrentItem(0);
                        selectTab(0);
                    }
                }
            }, 500);


        }
        onCustomSelected(mViewPager);
        img_segment.setBackgroundResource(R.drawable.map_active);

        search = (EditText) view.findViewById(R.id.search);
        search_button = (TextView) view.findViewById(R.id.search_button);
        search_button.setOnClickListener(this);
        triggerReloadOnSearchFieldEmpty();
    }

    private void initializeFilter() {
        clientFilter=new ClientFilter();
        //clientFilter.setName(search.getText().toString());
        clientFilter.setDistance(0);
        clientFilter.setUnit("mi");
        clientFilter.setGroup(MapAndListFragment.group);
        clientFilter.setLast_visited(MapAndListFragment.last_visited);
        clientFilter.setOrder_by(MapAndListFragment.orderby);
        clientFilter.setOrder(MapAndListFragment.order);
        if (MapAndListFragment.selectedTerritories.size() == 0)
            clientFilter.setTerritories(null);
        else
            clientFilter.setTerritories(MapAndListFragment.selectedTerritories);

        if (MapAndListFragment.selectedLines.size() == 0)
            clientFilter.setLines(null);
        else
            clientFilter.setLines(MapAndListFragment.selectedLines);

        Coordinate coordinate =MyUtils.getCurrentLocation();
        if (coordinate == null) {
            coordinate = user.getOffice_address().getCoordinate();
        }
        clientFilter.setCoordinate(coordinate);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_segment:
                bMap = !bMap;
                if (bMap) {
                    mViewPager.setCurrentItem(0);

                    img_segment.setBackgroundResource(R.drawable.map_active);
                } else {
                    mViewPager.setCurrentItem(1);

                    img_segment.setBackgroundResource(R.drawable.list_active);
                }
                break;
            case R.id.img_filter:
                search_name = search.getText().toString();
                MyUtils.startRequestActivity(getActivity(), ClientFilterActivity.class, Constants.ClientFilter, bundle);
                break;
            case R.id.search_button:
                if (search.getText().toString().length() > 3) {
//                    if (bMap) {
                    clientFilter.setName(search.getText().toString());
                    doSearch();
                }
                break;
        }
    }
    void triggerReloadOnSearchFieldEmpty() {
        search.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if(search.getText().toString().isEmpty()) {
                    clientFilter.setName(null);
                    doSearch();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });
    }

    public void selectTab(int position){
        switch (position){
            case 0:
                bMap = true;
                img_segment.setBackgroundResource(R.drawable.map_active);
                break;
            case 1:
                bMap = false;
                img_segment.setBackgroundResource(R.drawable.list_active);
                break;
            default:;
        }
    }

    public void onCustomSelected(ViewPager vpPager){
        // Attaching the page change listener inside the activity
        vpPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            // This method will be invoked when a new page becomes selected.
            @Override
            public void onPageSelected(int position) {
//                Toast.makeText(LinesListActivity.this,"Selected page position: " + position, Toast.LENGTH_SHORT).show();
              //  if(position>0) MainActivity.getInstance().refresh.setEnabled(true);
                selectTab(position);
            }

            // This method will be invoked when the current page is scrolled
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Code goes here
            }

            // Called when the scroll state changes:
            // SCROLL_STATE_IDLE, SCROLL_STATE_DRAGGING, SCROLL_STATE_SETTLING
            @Override
            public void onPageScrollStateChanged(int state) {
                // Code goes here
            }
        });
    }
    public void refresh() {
        final int current_item=mViewPager.getCurrentItem();
        mViewPager.setAdapter(null);
        mViewPager.setAdapter(mPagerAdapter);
        mPagerAdapter.notifyDataSetChanged();
        mViewPager.postDelayed(new Runnable() {
            @Override
            public void run() {
                mViewPager.setCurrentItem(current_item);
            }
        }, 300);

        MainActivity.getInstance().refresh.setRefreshing(false);
    }
    public class PagerAdapter extends FragmentStatePagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position){
                case 0:
                    if(clientsMapFragment==null) {
                        clientsMapFragment=new ClientsMapFragment();
                    }
                    fragment = clientsMapFragment;
                    break;
                case 1:
                    if(clientsListFragment==null) {
                        clientsListFragment=new ClientsListFragment();
                    }
                    fragment = clientsListFragment;
                    break;
            }
            assert fragment != null;
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }



    @Override
    public void onPause() {
        super.onPause();
        MainActivity.getInstance().refresh.setEnabled(true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        clientFilter.setName(search.getText().toString());
        clientFilter.setDistance(MapAndListFragment.distance);
        clientFilter.setUnit("mi");
        clientFilter.setGroup(MapAndListFragment.group);
        clientFilter.setLast_visited(MapAndListFragment.last_visited);
        clientFilter.setOrder_by(MapAndListFragment.orderby);
        clientFilter.setOrder(MapAndListFragment.order);
        if (MapAndListFragment.selectedTerritories.size() == 0)
            clientFilter.setTerritories(null);
        else
            clientFilter.setTerritories(MapAndListFragment.selectedTerritories);

        if (MapAndListFragment.selectedLines.size() == 0)
            clientFilter.setLines(null);
        else
            clientFilter.setLines(MapAndListFragment.selectedLines);


        Coordinate coordinate = new Coordinate(39.9001126, -75.2890745);//MyUtils.getCurrentLocation();
        if (coordinate == null) {
            coordinate = user.getOffice_address().getCoordinate();
        }
        clientFilter.setCoordinate(coordinate);

        doSearch();

//        fragment.onActivityResult(requestCode, resultCode, data);
    }
    void doSearch() {

        clientsMapFragment.loadClientsFilter(clientFilter, 1);
        clientsListFragment.loadClientsFilter(clientFilter, 1);
    }
}
