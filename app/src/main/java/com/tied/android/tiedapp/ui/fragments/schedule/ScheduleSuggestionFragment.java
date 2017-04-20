package com.tied.android.tiedapp.ui.fragments.schedule;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.Coordinate;
import com.tied.android.tiedapp.objects.Location;
import com.tied.android.tiedapp.objects.client.Client;
import com.tied.android.tiedapp.objects.client.ClientLocation;
import com.tied.android.tiedapp.objects.responses.ClientRes;
import com.tied.android.tiedapp.objects.schedule.Schedule;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.ClientApi;
import com.tied.android.tiedapp.ui.activities.MainActivity;
import com.tied.android.tiedapp.ui.adapters.ClientScheduleAdapter;
import com.tied.android.tiedapp.ui.adapters.ClientScheduleHorizontalAdapter;
import com.tied.android.tiedapp.ui.dialogs.DialogUtils;
import com.tied.android.tiedapp.ui.listeners.FragmentIterationListener;
import com.tied.android.tiedapp.util.MyUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScheduleSuggestionFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = ScheduleSuggestionFragment.class
            .getSimpleName();

    private User user;

    private Bundle bundle;

    private TextView view_schedule, company_name;
   // private ImageView img_activity;
    private ArrayList<Client> clients;
    private ListView listView;
    private Client client;
    private Schedule schedule;
    private ImageView pic_client;
    private LinearLayout should_visit;

    private RecyclerView horizontalList;
    LinearLayoutManager horizontalManager;
    private ClientScheduleHorizontalAdapter horizontalAdapter;

    private ClientScheduleAdapter adapter;

    int[] IMAGE = {R.mipmap.avatar_profile, R.mipmap.avatar_schedule, R.mipmap.default_avatar};
    String[] NAME = {"Emily Emmanuel","Johnson Good","Nonso Lagos"};
    Location[] ADDRESS = {new Location("Ikeja","123","Lagos","Ikunna"),new Location("Old Town","546","NY","Mile street"),new Location("LA","567","Carlifornia","Myles Strt")};

    private FragmentIterationListener fragmentIterationListener;

    public static Fragment newInstance (Bundle bundle) {
        Fragment fragment=new ScheduleSuggestionFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_schedule_clients_suggestions, container, false);
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
            fragmentIterationListener = (FragmentIterationListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public void nextAction(int action, Bundle bundle) {
        if (fragmentIterationListener != null) {
            fragmentIterationListener.OnFragmentInteractionListener(action,bundle);
        }
    }

    public void initComponent(View view) {

       // img_activity = (ImageView) view.findViewById(R.id.img_activity);

        should_visit = (LinearLayout) view.findViewById(R.id.should_visit);

        clients = new ArrayList<Client>();
        listView = (ListView) view.findViewById(R.id.list);

        pic_client = (ImageView) view.findViewById(R.id.pic_client);
        company_name = (TextView) view.findViewById(R.id.company_name);

        horizontalList = (RecyclerView)view.findViewById(R.id.horizontal_list);

        //set horizontal LinearLayout as layout manager to creating horizontal list view
        horizontalManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        horizontalList.setLayoutManager(horizontalManager);

        view_schedule = (TextView) view.findViewById(R.id.view_schedule);
        view_schedule.setOnClickListener(this);
       // img_activity.setOnClickListener(this);

        bundle = getArguments();
        if (bundle != null) {
            Log.d(TAG, "bundle not null");
            Gson gson = new Gson();
            String user_json = bundle.getString(Constants.USER_DATA);
            String client_json = bundle.getString(Constants.CLIENT_DATA);
            String schedule_json = bundle.getString(Constants.SCHEDULE_DATA);
            user = gson.fromJson(user_json, User.class);
            if(user==null) {
                user=MyUtils.getUserLoggedIn();
            }
            client = gson.fromJson(client_json, Client.class);
            schedule = gson.fromJson(schedule_json, Schedule.class);
            if(client !=null) {
                try {
                    company_name.setText(client.getCompany());
                    Log.d(TAG + " schedule", schedule.getLocation().getCoordinate().toString());
                    String logo = client.getLogo().equals("") ? null : client.getLogo();
                    MyUtils.Picasso.displayImage(logo, pic_client);
                }catch (Exception e) {
                    company_name.setText(schedule.getTitle());
                    pic_client.setVisibility(View.GONE);
                }
            }

            initClient();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_schedule:
                nextAction(Constants.ViewSchedule,bundle);
                break;

        }
    }

    private void initClient(){

        Coordinate coordinate = new Coordinate(0.0, 0.0);
        if(schedule.getLocation() != null){
            coordinate = schedule.getLocation().getCoordinate();
        }
        ClientLocation clientLocation = new ClientLocation("10km", coordinate);
        ClientApi clientApi =  MainApplication.createService(ClientApi.class, user.getToken());
        Call<ClientRes> response = clientApi.getClientsByLocation(user.getId(), 1, clientLocation);
        response.enqueue(new Callback<ClientRes>() {
            @Override
            public void onResponse(Call<ClientRes> call, Response<ClientRes> resResponse) {
                if ( getActivity() == null ) return;
                DialogUtils.closeProgress();
                try {
                    ClientRes clientRes = resResponse.body();
                    if (clientRes.isAuthFailed()) {
                        User.LogOut(getActivity());
                    } else if (clientRes.get_meta() != null && clientRes.get_meta().getStatus_code() == 200) {
                        clients = clientRes.getClients();
                        horizontalAdapter = new ClientScheduleHorizontalAdapter(clients, getActivity());
                        should_visit.removeAllViews();
                        for (Client client : clients) {
                            View schedule_view = LayoutInflater.from(getActivity()).inflate(R.layout.schedule_clients_suggestion_list_item, null);
                            LinearLayout linearLayout = (LinearLayout) schedule_view.findViewById(R.id.should_visit_list_item);
                            TextView name = (TextView) linearLayout.findViewById(R.id.name);
                            name.setText(client.getFull_name());
                            ImageView imageView = (ImageView) linearLayout.findViewById(R.id.pic);
                            MyUtils.Picasso.displayImage(client.getLogo(), imageView);
                            should_visit.addView(linearLayout);
                        }

                        horizontalList.setAdapter(horizontalAdapter);
                    } else {
                        MyUtils.showToast(clientRes.getMessage());
                    }
                }catch (Exception e) {
                    MyUtils.showConnectionErrorToast(getActivity());
                }
                Log.d(TAG + " onResponse", resResponse.body().toString());
            }

            @Override
            public void onFailure(Call<ClientRes> call, Throwable t) {
                Log.d(TAG + " onFailure", t.toString());
                DialogUtils.closeProgress();
            }
        });
    }
}
