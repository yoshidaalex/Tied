package com.tied.android.tiedapp.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.model.TerritoryModel;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.ui.adapters.TerritoryAdapter;
import com.tied.android.tiedapp.util.MyUtils;

import java.util.ArrayList;


/**
 * Created by Ratan on 7/27/2015.
 */
public class TerritoriesFragment extends Fragment implements AdapterView.OnItemClickListener,View.OnClickListener {

    public static final String TAG = LinesFragment.class
            .getSimpleName();

    protected User user;
    protected Bundle bundle;
    protected ListView listView;
    private ArrayList<TerritoryModel> territoryModels;

    private TerritoryAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.client_territories_fragment,null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initComponent(view);
    }

    public void initComponent(View view){
        territoryModels = new ArrayList<>();
        bundle = getArguments();
        user = MyUtils.getUserFromBundle(bundle);
        listView = (ListView) view.findViewById(R.id.list);
        listView.setOnItemClickListener(this);

        ArrayList territory = user.getTerritories();
        if(territory != null){
            for(Object obj : territory){
                String name = (String) obj;
                territoryModels.add(new TerritoryModel(name));
            }
            adapter = new TerritoryAdapter(territoryModels, getActivity(), bundle);
            listView.setAdapter(adapter);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d(TAG, "here---------------- listener");
//        TerritoryModel territoryModel = territoryModels.get(position);
//        Bundle bundle = new Bundle();
//        bundle.putSerializable(Constants.TERRITORY_DATA, territoryModel);
//        MyUtils.startActivity(getActivity(), ViewLineActivity.class, bundle);
    }

    @Override
    public void onClick(View v) {

    }
}
