package com.tied.android.tiedapp.ui.fragments.sales;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.ui.fragments.FirstFragment;
import com.tied.android.tiedapp.ui.fragments.schedule.CreateScheduleFragment;

/**
 * Created by femi on 8/4/2016.
 */
public class AddSalesFragment extends Fragment {


    public AddSalesFragment() {
        // Required empty public constructor
        super();
    }

    public static AddSalesFragment newInstance (Bundle bundle) {
        AddSalesFragment fragment=new AddSalesFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_sales, container, false);
    }

    public void onClick(View view) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
