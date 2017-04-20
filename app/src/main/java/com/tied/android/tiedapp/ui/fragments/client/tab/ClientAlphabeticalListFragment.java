package com.tied.android.tiedapp.ui.fragments.client.tab;

import android.view.View;

import com.tied.android.tiedapp.objects.client.Client;
import com.tied.android.tiedapp.ui.adapters.ClientListAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Emmanuel on 6/30/2016.
 */
public class ClientAlphabeticalListFragment extends ClientList{
    public static final String TAG = ClientAlphabeticalListFragment.class
            .getSimpleName();


    public void initComponent(View view) {
        super.initComponent(view);
//
//        search.addTextChangedListener(new TextWatcher() {
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                // TODO Auto-generated method stub
//
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count,
//                                          int after) {
//                // TODO Auto-generated method stub
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                // TODO Auto-generated method stub
//                String searchData = search.getText().toString().trim().toLowerCase();
//                if(adapter != null){
//                    ((ClientDistantAdapter)adapter).filter(searchData);
//                }
//            }
//
//        });
    }

    public void initFormattedClient(ArrayList clients){
        Collections.sort(clients, new Comparator() {
            @Override
            public int compare(Object lhs, Object rhs) {
                Client client = (Client) lhs;
                Client client1 = (Client) lhs;
                return client.getFull_name().compareTo(client1.getFull_name());
            }
        });

        adapter = new ClientListAdapter(clients, getActivity());
        listView.setAdapter(adapter);
        listView.setFastScrollEnabled(true);
    }

}
