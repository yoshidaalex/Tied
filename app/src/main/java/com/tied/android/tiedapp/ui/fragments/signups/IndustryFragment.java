package com.tied.android.tiedapp.ui.fragments.signups;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.customs.model.DataModel;
import com.tied.android.tiedapp.objects.responses.ServerRes;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.SignUpApi;
import com.tied.android.tiedapp.ui.dialogs.DialogUtils;
import com.tied.android.tiedapp.ui.listeners.FragmentIterationListener;
import com.tied.android.tiedapp.util.MyUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class IndustryFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = IndustryFragment.class
            .getSimpleName();

    private RelativeLayout continue_btn;

    ArrayList<DataModel> industry_data = new ArrayList<DataModel>();
    ListView industry_listview;
    SearchAdapter industry_adapter;

    Context context;
    private SignUpApi signUpApi;
    private boolean editProfile;
    private TextView txt_hint_string, txt_signup_label;

    private FragmentIterationListener mListener;

    ArrayList<String> industries = new ArrayList<String>();
    private RelativeLayout top_layout;
    private LinearLayout bottom_layout;

    private Bundle bundle;
    // Reference to our image view we will use
    public ImageView img_user_picture;

    public static Fragment newInstance (Bundle bundle) {
        Fragment fragment=new IndustryFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_up_industry, container, false);
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
            mListener = (FragmentIterationListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public void nextAction(Bundle bundle) {
        if (mListener != null) {
            int action = Constants.AddBoss;
            if(editProfile){
                action = Constants.EditProfile;
            }
            mListener.OnFragmentInteractionListener(action, bundle);
        }
    }

    public void iniEditProfileComponent(View view){
        top_layout = (RelativeLayout) view.findViewById(R.id.top_layout);
        txt_signup_label = (TextView) view.findViewById(R.id.txt_signup_label);
        txt_signup_label.setText("Edit Industry");
        txt_hint_string = (TextView) view.findViewById(R.id.txt_hint_string);
    }

    public void initComponent(View view) {
        bundle = getArguments();
        editProfile = bundle.getBoolean(Constants.EditingProfile);
        if(editProfile){
            iniEditProfileComponent(view);
        }
        continue_btn = (RelativeLayout) view.findViewById(R.id.continue_btn);
        continue_btn.setOnClickListener(this);

        img_user_picture = (ImageView) view.findViewById(R.id.img_user_picture);
        MyUtils.initAvatar(bundle, img_user_picture);

        industry_listview = (ListView) view.findViewById(R.id.industry_listview);
        industry_adapter = new SearchAdapter(industry_data, getActivity());
        industry_listview.setAdapter(industry_adapter);
        industry_listview.setDividerHeight(0);

        industry_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DataModel item = industry_data.get(position);
                if (item.isCheck_status()) {
                    item.setCheck_status(false);
                } else {
                    item.setCheck_status(true);
                }
                industry_adapter.notifyDataSetChanged();
            }
        });
        DialogUtils.displayProgress(getActivity());
        Call<List<DataModel>> response = MainApplication.createService(SignUpApi.class).getIndustries();
        response.enqueue(new Callback<List<DataModel>>() {
            @Override
            public void onResponse(Call<List<DataModel>> call, Response<List<DataModel>> listResponse) {
                if (getActivity() == null) return;
                DialogUtils.closeProgress();
                List<DataModel> dataModelList = listResponse.body();
                industry_data.addAll(dataModelList);
                industry_adapter.notifyDataSetChanged();
                Log.d(TAG + " onResponse", dataModelList.toString());
            }

            @Override
            public void onFailure(Call<List<DataModel>> call, Throwable t) {
                Log.d(TAG + " onFailure", t.toString());
                DialogUtils.closeProgress();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.continue_btn:
                for (int i = 0 ; i < industry_data.size() ; i++) {
                    DataModel item = industry_data.get(i);
                    if (item.isCheck_status()) {
                       industries.add(item.getName());
                    }
                }
                continue_action();
                break;
        }
    }

    class SearchAdapter extends ArrayAdapter<DataModel> {

        private ArrayList<DataModel> itemList;
        private Context context;


        public SearchAdapter(ArrayList<DataModel> itemList, Context ctx) {
            super(ctx, android.R.layout.simple_list_item_1, itemList);
            this.itemList = itemList;
            this.context = ctx;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            View v = convertView;
            if (v == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                v = inflater.inflate(R.layout.industry_list_item, null);
            }

            final DataModel item = industry_data.get(position);

            ImageView img_status = (ImageView) v.findViewById(R.id.img_status);

            TextView txt_industry_item_label= (TextView) v.findViewById(R.id.txt_industry_item_label);
            txt_industry_item_label.setText(item.getName());

            if (item.isCheck_status()) {
                img_status.setBackgroundResource(R.mipmap.checked_icon);
                txt_industry_item_label.setTextColor(getResources().getColor(R.color.text_disable_color));
            } else {
                img_status.setBackgroundResource(R.mipmap.empty_unchecked_icon);
                txt_industry_item_label.setTextColor(Color.WHITE);
            }
            return v;
        }
    }

    public void continue_action() {
        if(industries.size() > 0){
            DialogUtils.displayProgress(getActivity());

            bundle = getArguments();

            Gson gson = new Gson();
            String user_json = bundle.getString(Constants.USER_DATA);
            final User user = gson.fromJson(user_json, User.class);
            user.setIndustries(industries);
            user.setSign_up_stage(Constants.AddBoss);
            Call<ServerRes> response = MainApplication.createService(SignUpApi.class).updateUser(user);
            response.enqueue(new Callback<ServerRes>() {
                @Override
                public void onResponse(Call<ServerRes> call, Response<ServerRes> ServerResponseResponse) {
                    ServerRes ServerRes = ServerResponseResponse.body();
                    Log.d(TAG + " onResponse", ServerResponseResponse.body().toString());
                    if (ServerRes.isAuthFailed()){
                        DialogUtils.closeProgress();
                        User.LogOut(getActivity());
                    }
                    if (ServerRes.isSuccess()) {
                        Bundle bundle = new Bundle();
                        boolean saved = user.save(getActivity().getApplicationContext());
                        if (saved) {
                            Gson gson = new Gson();
                            String json = gson.toJson(user);
                            bundle.putString(Constants.USER_DATA, json);
                            DialogUtils.closeProgress();
                            nextAction(bundle);
                        } else {
                            DialogUtils.closeProgress();
                            MyUtils.showToast("user info  was not updated");
                        }
                    } else {
                        MyUtils.showToast(ServerRes.getMessage());
                    }
                    DialogUtils.closeProgress();
                }

                @Override
                public void onFailure(Call<ServerRes> ServerResponseCall, Throwable t) {
                    MyUtils.showToast("On failure : error encountered");
                    DialogUtils.closeProgress();
                }
            });
        }else{
            MyUtils.showToast("No industry selected");
        }
    }
}
