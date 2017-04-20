package com.tied.android.tiedapp.ui.fragments.signins;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.ui.listeners.FragmentIterationListener;
import com.tied.android.tiedapp.util.APIManager;
import com.tied.android.tiedapp.ui.dialogs.DialogUtils;
import com.tied.android.tiedapp.util.MyUtils;
import com.tied.android.tiedapp.util.Utility;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class ResetFragment extends Fragment implements View.OnClickListener{

    public static final String TAG = ResetFragment.class
            .getSimpleName();

    private FragmentIterationListener mListener;

    private ProgressBar progressBar;
    private EditText email;
    boolean m_bExit = false;

    private String emailText;

    LinearLayout back_layout;
    ImageView reset_password;
    LinearLayout alert_valid_email;
    Context context;

    public ResetFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_in_reset, container, false);
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

    public void nextAction(int action, Bundle bundle) {
        if (mListener != null) {
            mListener.OnFragmentInteractionListener(action,bundle);
        }
    }

    public void initComponent(View view){

        context = getActivity();

        email = (EditText) view.findViewById(R.id.email);
        email.setBackgroundResource(android.R.color.transparent);

        back_layout = (LinearLayout) view.findViewById(R.id.back_layout);
        back_layout.setOnClickListener(this);

        reset_password = (ImageView) view.findViewById(R.id.reset_password);
        reset_password.setOnClickListener(this);

      //  alert_valid_email = (LinearLayout) view.findViewById(R.id.alert_valid_email);
      //  alert_valid_email.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.reset_password:
                if (!Utility.isEmailValid(email.getText().toString())) {
                    alert_valid_email.setVisibility(View.VISIBLE);
                    //Utility.moveViewToScreenCenter( alert_valid_email, );
                    MyUtils.showErrorAlert(getActivity(), Utility.getResourceString(context, R.string.alert_valide_email));
                } else {
                    new LoadForgetPasswordTask().execute(email.getText().toString());
                }
                break;
            case R.id.back_layout:
                nextAction(Constants.SignInUser, null);
                break;
        }
    }

    class LoadForgetPasswordTask extends AsyncTask<String, Integer, String> {
        private ProgressDialog progressDialog;
        String value = "";

        protected void onPreExecute() {
            DialogUtils.displayProgress(context);
        }

        @Override
        protected void onPostExecute(String resp) {
            if(getActivity() == null) return;
            DialogUtils.closeProgress();
            try {
                JSONObject result=new JSONObject(resp);
                if (result.has("success")) {
                    m_bExit = true;
                    value = result.getString("message");
                    nextAction(Constants.DoneReset,null);

                } else {
                    m_bExit = false;
                    value = result.getString("developer_message");
                    DialogUtils.openDialog(context, value, "OK", "");
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }



        }

        @Override
        protected String doInBackground(String... param) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("email", param[0]));

            JSONObject result = null;
            result =  APIManager.getInstance().callPost(context, "auth/forgot_password", params, false);


            return result.toString();
        }
    }
}