package com.tied.android.tiedapp.ui.fragments.signups;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.LoggingBehavior;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.gson.Gson;
import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.responses.CheckEmail;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.SignUpApi;
import com.tied.android.tiedapp.ui.activities.signups.WalkThroughActivity;
import com.tied.android.tiedapp.ui.dialogs.AppDialog;
import com.tied.android.tiedapp.ui.dialogs.DialogUtils;
import com.tied.android.tiedapp.ui.listeners.FragmentIterationListener;
import com.tied.android.tiedapp.util.Logger;
import com.tied.android.tiedapp.util.MyUtils;
import com.tied.android.tiedapp.util.Utility;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class EmailSignUpFragment extends Fragment implements View.OnClickListener{

    public static final String TAG = EmailSignUpFragment.class
            .getSimpleName();

    public CallbackManager callbackManager;
    private AccessToken accessToken;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;

    private FragmentIterationListener mListener;

    private RelativeLayout continue_btn;
    //LinearLayout alert_valid_email;
    private ImageView btn_close, img_facebook, img_twitter, img_help;
    public TwitterAuthClient authClient = new TwitterAuthClient();
    private TwitterSession session;
    private TwitterAuthToken authToken;

    boolean m_bExit = false;
    AppDialog alertDialog;

    private EditText email;

    String facebookId, firstName = "", lastName="", emailText="", avatar="";

    public static Fragment newInstance (Bundle bundle) {
        Fragment fragment=new EmailSignUpFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public EmailSignUpFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_up_email, container, false);
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
            mListener.OnFragmentInteractionListener(Constants.Password,bundle);
        }
    }

    public void initComponent(View view){

        email = (EditText) view.findViewById(R.id.email);

        btn_close = (ImageView) view.findViewById(R.id.img_close);
        img_help = (ImageView) view.findViewById(R.id.img_help);
        img_facebook = (ImageView) view.findViewById(R.id.img_facebook);
        img_twitter = (ImageView) view.findViewById(R.id.img_twitter);
        continue_btn = (RelativeLayout) view.findViewById(R.id.continue_btn);
        continue_btn.setOnClickListener(this);
        btn_close.setOnClickListener(this);

        initializeFaceBook();
        img_facebook.setOnClickListener(this);
        img_twitter.setOnClickListener(this);

        Bundle bundle = getArguments();
        if(bundle != null){
            Gson gson = new Gson();
            String user_json = bundle.getString(Constants.USER_DATA);
            Log.d(TAG, user_json);
            User user = gson.fromJson(user_json, User.class);
            email.setText(user.getEmail());
        }

       //alert_valid_email = (LinearLayout) view.findViewById(R.id.alert_valid);
       // alert_valid_email.setVisibility(View.GONE);
    }

    public void continue_action(){
        DialogUtils.displayProgress(getActivity());
        Call<CheckEmail> response =  MainApplication.createService(SignUpApi.class).checkEmail(emailText);
        Log.d(TAG, response.request().url().toString());
        response.enqueue(new Callback<CheckEmail>() {
            @Override
            public void onResponse(Call<CheckEmail> call, Response<CheckEmail> checkEmailResponse) {
                if (getActivity() == null) return;
                try {
                    CheckEmail checkEmail = checkEmailResponse.body();

                    if (checkEmail.isSuccess()) {
                        Bundle bundle = new Bundle();
                        User user = new User();
                        user.setEmail(emailText);
                        user.setFirst_name(firstName);
                        user.setLast_name(lastName);
                        user.setAvatar(avatar);
                        Gson gson = new Gson();
                        String user_json = gson.toJson(user);
                        bundle.putString(Constants.USER_DATA, user_json);
                        nextAction(bundle);
                    } else {
                        MyUtils.showErrorAlert(getActivity(), checkEmail.getMessage());
                    }
                }catch (Exception e) {
                    Logger.write(e);
                    MyUtils.showToast(getActivity().getString(R.string.connection_error));
                }
                DialogUtils.closeProgress();
            }

            @Override
            public void onFailure(Call<CheckEmail> checkEmailCall, Throwable t) {
               // Toast.makeText(getActivity(), "On failure : error encountered", Toast.LENGTH_LONG).show();
                MyUtils.showToast(getActivity().getString(R.string.connection_error));
                Logger.write(TAG +" onFailure", t.toString());
                DialogUtils.closeProgress();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.continue_btn:
                emailText = email.getText().toString();
                if (!Utility.isEmailValid(emailText)) {
                  //  alert_valid_email.setVisibility(View.VISIBLE);
                 //  Utility.moveViewToScreenCenter( alert_valid_email, Utility.getResourceString(getActivity(), R.string.alert_valide_email));
                    MyUtils.showErrorAlert(getActivity(), Utility.getResourceString(getActivity(), R.string.alert_valide_email));
                } else {
                    continue_action();
                }
                break;
            case R.id.img_close:
                Intent intent = new Intent(getActivity(), WalkThroughActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
            case R.id.img_facebook:

                LoginManager.getInstance().logInWithReadPermissions(getActivity(), Arrays.asList("public_profile", "email"));
                break;
            case R.id.img_twitter:
                loginTwitter();
                break;
        }
    }

    private void loginTwitter(){

        authClient.authorize(getActivity(), new com.twitter.sdk.android.core.Callback<TwitterSession>(){
            @Override
            public void success(Result<TwitterSession> twitterSessionResult) {
                session = twitterSessionResult.data;
                authToken = session.getAuthToken();

                authClient.requestEmail(session, new com.twitter.sdk.android.core.Callback<String>() {
                    @Override
                    public void success(Result<String> result) {
                        Toast.makeText(getActivity(), result.data, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void failure(TwitterException e) {
                        Log.d(TAG, e.getMessage());
                        Toast.makeText(getActivity(),e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void failure(TwitterException e) {
                Toast.makeText(getActivity(),e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    private void initializeFaceBook() {
        FacebookSdk.sdkInitialize(getActivity());
        FacebookSdk.addLoggingBehavior(LoggingBehavior.REQUESTS);
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().logOut();
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        accessToken = loginResult.getAccessToken();
                        GetUserProfile();
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(getActivity(), "Login Cancel", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(getActivity(), exception.getMessage(),
                                Toast.LENGTH_SHORT).show();
                        Log.d(TAG, exception.getMessage());
                    }
                });

        setAccessTokenTracker();
        setProfileTracker();
    }

    private void GetUserProfile() {
        DialogUtils.displayProgress(getActivity());
        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {

                    @Override
                    public void onCompleted(JSONObject object,
                                            GraphResponse response) {

                        DialogUtils.closeProgress();

                        Log.d("response ", response.toString()+"");

                        // TODO Auto-generated method stub
                        JSONObject obj = response.getJSONObject();
                        try {

                            facebookId = obj.getString("id");
                            avatar = "https://graph.facebook.com/" + facebookId + "/picture?type=large";
                            firstName = obj.getString("first_name");
                            lastName = obj.getString("last_name");
                            emailText = obj.getString("email");
                            email.setText(emailText);
                            continue_action();

                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,first_name,last_name,email");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void setAccessTokenTracker(){
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {
                // Set the access token using
                // currentAccessToken when it's loaded or set.
                accessToken = currentAccessToken;
            }
        };
    }

    private void setProfileTracker(){
        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                LoginManager.getInstance().logOut();
            }
        };
    }
}