package com.tied.android.tiedapp.ui.fragments.signups;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.ui.adapters.IndustryAdapter;

import java.util.ArrayList;

/**
 * Created by hitendra on 9/9/2016.
 */
public class IndustryFragmentNew extends Fragment implements View.OnClickListener {
    private EditText etIndustry;
    private ListView lvIndustry;
    ArrayList<String> industryList = new ArrayList<String>();
    IndustryAdapter adapter;
    private Button btnDone, btnClear;
    private TextView tvTitle;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_industry, container, false);
        init(rootView);
        return rootView;
    }

    private void init(View rootView) {
        industryList.add("Apple");
        industryList.add("Banana");
        industryList.add("United States");
        industryList.add("United Kingdom");
        industryList.add("Germany");
        industryList.add("Tied");

        etIndustry = (EditText) rootView.findViewById(R.id.etIndustry);
        lvIndustry = (ListView) rootView.findViewById(R.id.lvIndustry);
        adapter = new IndustryAdapter(getActivity(), industryList);
        lvIndustry.setAdapter(adapter);
        btnDone = (Button) rootView.findViewById(R.id.btnDone);
        btnDone.setOnClickListener(this);

        btnClear = (Button) rootView.findViewById(R.id.btnClear);
        btnClear.setOnClickListener(this);

        tvTitle=(TextView)rootView.findViewById(R.id.tvTitle);
        tvTitle.setText(getResources().getString(R.string.industries));

        etIndustry.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                adapter.getFilter().filter(arg0);
                if (arg0.length() > 0) {
                    btnClear.setVisibility(View.VISIBLE);
                } else {
                    btnClear.setVisibility(View.GONE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnDone:
                break;
            case R.id.btnClear:
                etIndustry.setText("");
                break;
            default:
                break;
        }
    }
}
