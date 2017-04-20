package com.tied.android.tiedapp.ui.fragments.client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.Coordinate;
import com.tied.android.tiedapp.objects.client.Client;
import com.tied.android.tiedapp.objects.client.ClientFilter;
import com.tied.android.tiedapp.objects.client.ClientLocation;
import com.tied.android.tiedapp.objects.responses.ClientRes;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.ClientApi;
import com.tied.android.tiedapp.ui.activities.MainActivity;
import com.tied.android.tiedapp.ui.activities.client.ActivityClientProfile;
import com.tied.android.tiedapp.ui.adapters.MapClientListAdapter;
import com.tied.android.tiedapp.ui.dialogs.DialogUtils;
import com.tied.android.tiedapp.util.Logger;
import com.tied.android.tiedapp.util.MyUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by ZuumaPC on 8/18/2016.
 */
public class ClientsListFragment extends Fragment implements AdapterView.OnItemClickListener,View.OnClickListener{

    public static final String TAG = ClientsListFragment.class
            .getSimpleName();

    protected User user;
    protected Bundle bundle;
    protected ListView listView;
    List<Client> clients = new ArrayList<Client>();
    ClientFilter clientFilter;
//    List<Client> search_data = new ArrayList<Client>();
    protected MapClientListAdapter adapter;
    int numPages=1;
    String search_name;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.client_lines_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initComponent(view);
    }
    public static ClientsListFragment newInstance(Bundle bundle, ArrayList<Client> clients) {
        ClientsListFragment cmf=new ClientsListFragment();
        cmf.setArguments(bundle);
        cmf.setClients(clients);
        return cmf;
    }

    public void setClients(ArrayList<Client> clients) {
        this.clients = clients;
        adapter.notifyDataSetChanged();
    }
    public void addClients(ArrayList<Client> clients) {
        this.clients.addAll( clients);
        adapter.notifyDataSetChanged();
    }
    private int preLast;
    public void initComponent(View view){
        bundle = getArguments();
        user = MyUtils.getUserFromBundle(bundle);
        listView = (ListView) view.findViewById(R.id.list);
        listView.setOnItemClickListener(this);

        adapter = new MapClientListAdapter(clients, getActivity());
        listView.setAdapter(adapter);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                final int lastItem = firstVisibleItem + visibleItemCount;

                if(lastItem == totalItemCount)
                {
                    if(preLast!=lastItem)
                    {
                        if(pageNumber<numPages) {
                            pageNumber++;
                            loadClientsFilter(clientFilter);
                        }

                        preLast = lastItem;
                    }
                }
            }
        });
        loadClientsFilter(MapAndListFragment.getInstance().clientFilter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d(TAG, "here---------------- listener");
        //Client client = clients.get(position);
        Bundle bundle =new Bundle();
        bundle.putSerializable(Constants.USER_DATA, user);
        bundle.putSerializable(Constants.CLIENT_DATA, clients.get(position));
        MyUtils.startRequestActivity(getActivity(), ActivityClientProfile.class, Constants.ClientDelete, bundle);
    }

    @Override
    public void onClick(View v) {

    }



    public int pageNumber=1;
    public void loadClientsFilter(final ClientFilter clientFilter) {
        if(clientFilter.getDistance()==MapAndListFragment.distance) clientFilter.setDistance(0);
        this.clientFilter=clientFilter;
        clientFilter.setCoordinate(MyUtils.getCurrentLocation());
        DialogUtils.displayProgress(getActivity());
        ClientApi clientApi = MainApplication.createService(ClientApi.class);
        Call<ClientRes> response = clientApi.getClientsFilter(user.getId(), pageNumber, clientFilter);
        response.enqueue(new Callback<ClientRes>() {
            @Override
            public void onResponse(Call<ClientRes> call, Response<ClientRes> resResponse) {
                if (getActivity()== null ) {
                    // Logger.write("null activity");
                    return;
                }
                //Logger.write("(((((((((((((((((((((((((((((999999");
                DialogUtils.closeProgress();
                ClientRes clientRes = resResponse.body();
               Logger.write(clientRes.toString());
                try {
                    if (clientRes.isAuthFailed()) {
                        User.LogOut(getActivity());
                    } else if (clientRes.get_meta() != null && clientRes.get_meta().getStatus_code() == 200) {
                        numPages=clientRes.get_meta().getPage_count();
                        if(pageNumber==1)  clients.clear();
                        clients.addAll(clientRes.getClients());

                        if(pageNumber==1 && clients.size()==0) {
                            MyUtils.showNoResults(getView(), R.id.no_results);
                        } else {
                            MyUtils.hideNoResults(getView());
                        }

                        adapter.notifyDataSetChanged();
                    } else {
                        Logger.write("Error onResponse", clientRes.getMessage());
                    }
                }catch (Exception e) {
                    Logger.write(e);
                }
            }

            @Override
            public void onFailure(Call<ClientRes> call, Throwable t) {
                Log.d(TAG + " onFailure", t.toString());
                DialogUtils.closeProgress();
            }
        });
    }
    public void loadClientsFilter(ClientFilter clientFilter, int pageNumber) {
        this.pageNumber=pageNumber;
        loadClientsFilter(clientFilter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.ClientFilter && resultCode == Activity.RESULT_OK) {
            if (MapAndListFragment.isClientFilter) {
                //loadClientsFilter(MapAndListFragment.search_name);
            } else if (MapAndListFragment.isClear) {
                //loadClients();
            }
        }
    }
}
