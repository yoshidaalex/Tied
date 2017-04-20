package com.tied.android.tiedapp.ui.fragments.client.tab;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.Gson;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.client.Client;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.ui.activities.client.AddClientActivity;
import com.tied.android.tiedapp.ui.adapters.ClientListAdapter;
import com.tied.android.tiedapp.ui.adapters.ClientParentAdapter;
import com.tied.android.tiedapp.ui.listeners.FragmentIterationListener;
import com.tied.android.tiedapp.util.MyUtils;

import java.util.ArrayList;

/**
 * Created by Emmanuel on 8/14/2016.
 */
public class ClientList extends Fragment implements AdapterView.OnItemClickListener {

    public static final String TAG = ClientList.class
            .getSimpleName();

    protected FragmentIterationListener mListener;

    protected User user;
    protected Bundle bundle;
    protected ListView listView;

    protected ClientParentAdapter adapter;

    public ArrayList clientsList;

    // Pop up
    protected EditText search;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.schedule_select_client_list, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initComponent(view);
    }

    public void initComponent(View view){
        clientsList = new ArrayList();
        listView = (ListView) view.findViewById(R.id.list);
        listView.setOnItemClickListener(this);

        adapter = getAdapter();

        listView.setAdapter(adapter);
        listView.setFastScrollEnabled(true);

        search = (EditText) view.findViewById(R.id.search);
        bundle = getArguments();
        if (bundle != null) {
            Gson gson = new Gson();
            String user_json = bundle.getString(Constants.USER_DATA);
            user = gson.fromJson(user_json, User.class);
            initClient();
        }
    }

    protected ClientParentAdapter getAdapter(){
        adapter = new ClientListAdapter(clientsList, getActivity());
        return adapter;
    }

    protected void initClient(){
        if (clientsList.size() == 0){
            MyUtils.initClient(getActivity(), user, adapter);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d("search", "here---------------- listener");
        ArrayList arrayList = (ArrayList) adapter.getList();
        if(arrayList.get(position) instanceof Client){
            Client data = (Client) arrayList.get(position);

//            Intent intent = new Intent(getActivity(), CreateAppointmentActivity.class);
            Intent intent = new Intent(getActivity(), AddClientActivity.class);

            intent.putExtra(Constants.CLIENT_DATA, data);
            startActivity(intent);
        }
    }

    protected void nextAction(int action,Bundle bundle) {
        if (mListener != null) {
            mListener.OnFragmentInteractionListener(action, bundle);
        }
    }
}
