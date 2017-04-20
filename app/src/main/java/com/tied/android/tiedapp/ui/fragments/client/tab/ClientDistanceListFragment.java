package com.tied.android.tiedapp.ui.fragments.client.tab;

import android.view.View;
import android.widget.TextView;

import com.tied.android.tiedapp.ui.adapters.ClientDistantAdapter;
import com.tied.android.tiedapp.ui.adapters.ClientParentAdapter;
import com.tied.android.tiedapp.util.MyUtils;

/**
 * Created by Emmanuel on 6/30/2016.
 */
public class ClientDistanceListFragment extends ClientList{

    public static final String TAG = ClientDistanceListFragment.class
            .getSimpleName();

    private TextView txt_continue;

    public void initComponent(View view) {
        super.initComponent(view);
//        txt_continue = (TextView) view.findViewById(R.id.txt_continue);
    }

    protected void initClient(){
        if (clientsList.size() == 0){
            MyUtils.initClient(getActivity(), user, adapter);
        }
    }

    protected ClientParentAdapter getAdapter(){
        adapter = new ClientDistantAdapter(clientsList, getActivity());
        return adapter;
    }

}
