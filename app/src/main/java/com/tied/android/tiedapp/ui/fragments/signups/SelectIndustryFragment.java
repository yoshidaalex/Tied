package com.tied.android.tiedapp.ui.fragments.signups;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.ui.adapters.SelectedIndustryAdapter;

/**
 * Created by hitendra on 9/13/2016.
 */
public class SelectIndustryFragment extends Fragment {
    private RecyclerView rvSelectedIndustries;
    private GridLayoutManager gridLayoutManager;
    private SelectedIndustryAdapter selectedIndustryAdapter;
    private TextView tvTitle;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_selected_industry, container, false);
        init(rootView);
        return rootView;
    }

    private void init(View rootView) {
        tvTitle=(TextView)rootView.findViewById(R.id.tvTitle);
        tvTitle.setText("Industry");
        rvSelectedIndustries = (RecyclerView)rootView.findViewById(R.id.rvSelectedIndustries);
        gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        rvSelectedIndustries.setLayoutManager(gridLayoutManager);
        selectedIndustryAdapter = new SelectedIndustryAdapter(getActivity());
        rvSelectedIndustries.setAdapter(selectedIndustryAdapter);
    }
}
