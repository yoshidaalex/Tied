package com.tied.android.tiedapp.ui.fragments.signups;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.ui.activities.MainActivity;
import com.tied.android.tiedapp.ui.activities.signups.InviteContactActivity;
import com.tied.android.tiedapp.ui.activities.signups.SignUpActivity;
import com.tied.android.tiedapp.ui.listeners.FragmentIterationListener;

/**
 * A placeholder fragment containing a simple view.
 */
public class AddInviteFragment extends Fragment implements View.OnClickListener{

    public static final String TAG = AddInviteFragment.class
            .getSimpleName();

    private FragmentIterationListener mListener;

    private RelativeLayout continue_btn;
    private TextView txt_add_later;
    LinearLayout via_email_layout, via_sms_layout, via_add_app, import_excel;

    Bundle bundle;
    // Reference to our image view we will use
    public ImageView img_user_picture;


    public static Fragment newInstance(Bundle bundle) {
        Fragment fragment=new AddInviteFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public AddInviteFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_up_add_invite, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initComponent(view);
    }


    public void initComponent(View view){

        txt_add_later = (TextView) view.findViewById(R.id.txt_add_later);
        continue_btn = (RelativeLayout) view.findViewById(R.id.continue_btn);
        continue_btn.setOnClickListener(this);
        txt_add_later.setOnClickListener(this);

        via_email_layout = (LinearLayout) view.findViewById(R.id.via_email_layout);
        via_email_layout.setOnClickListener(this);

        via_sms_layout = (LinearLayout) view.findViewById(R.id.via_sms_layout);
        via_sms_layout.setOnClickListener(this);

        via_add_app = (LinearLayout) view.findViewById(R.id.via_add_app);
        via_add_app.setOnClickListener(this);

        import_excel = (LinearLayout) view.findViewById(R.id.import_excel);
        import_excel.setOnClickListener(this);


        img_user_picture = (ImageView) view.findViewById(R.id.img_user_picture);

        bundle = getArguments();
        if (bundle != null) {
            Gson gson = new Gson();
            String user_json = bundle.getString(Constants.USER_DATA);
            User user = gson.fromJson(user_json, User.class);
            ((SignUpActivity) getActivity()).loadAvatar(user, img_user_picture);
        }

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

    public void nextAction(int action, Bundle bundle) {
        if (mListener != null) {
            mListener.OnFragmentInteractionListener(action,bundle);
        }
    }

    public void continue_action() {

    }

    public boolean validated(){
        return true;
    }

    @Override
    public void onClick(View v) {
        Bundle bundle = getArguments();
        Gson gson = new Gson();
        String user_json = bundle.getString(Constants.USER_DATA);
        User user = gson.fromJson(user_json, User.class);
        switch (v.getId()){
            case R.id.continue_btn:
                continue_action();
                break;
            case R.id.via_add_app:
                user.setSign_up_stage(Constants.CoWorker);
                boolean saved = user.save(getActivity().getApplicationContext());
                if(saved){
                    String json = gson.toJson(user);
                    bundle.putString(Constants.USER_DATA, json);
                    nextAction(Constants.CoWorker, bundle);
                }else{
                    Toast.makeText(getActivity(), "user info  was not updated", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.via_email_layout:
                Intent invite_intent = new Intent(getActivity(), InviteContactActivity.class);
                startActivity(invite_intent);
                break;
            case R.id.via_sms_layout:
                Intent sms_intent = new Intent(getActivity(), InviteContactActivity.class);
                startActivity(sms_intent);
                break;
            case R.id.txt_add_later:
                user.setSign_up_stage(Constants.Completed);
                saved = user.save(getActivity().getApplicationContext());
                if(saved){
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.putExtra(Constants.USER_DATA, user);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }else{
                    Toast.makeText(getActivity(), "user info  was not updated", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}