package com.tied.android.tiedapp.ui.fragments.profile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.ui.adapters.SalesPrivacyAdapter;

public class SalesPrivacyFragment extends Fragment {
    private RecyclerView rvSalesPrivacy;
    private SalesPrivacyAdapter salesPrivacyAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sales_privacy, container, false);
        init(view);
        return view;
    }

    private void init(View rootView) {
        rvSalesPrivacy = (RecyclerView) rootView.findViewById(R.id.rvSalesPrivacy);
        salesPrivacyAdapter = new SalesPrivacyAdapter(getActivity());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvSalesPrivacy.setLayoutManager(linearLayoutManager);
        rvSalesPrivacy.setAdapter(salesPrivacyAdapter);

        salesPrivacyAdapter.setOnItemClickListener(new SalesPrivacyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                view.setSelected(!view.isSelected());
            }
        });


    }

}
