package com.tied.android.tiedapp.ui.activities.profile;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.responses.ServerRes;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.ProfileApi;
import com.tied.android.tiedapp.ui.dialogs.DialogUtils;
import com.tied.android.tiedapp.util.MyUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hitendra on 9/9/2016.
 */
public class ChangePasswordActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = ChangePasswordActivity.class
            .getSimpleName();

    ImageView img_close;
    TextView txt_save;
    EditText etReNewPassword, etCurrentPassword, etNewPassword;
    User user;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        etCurrentPassword = (EditText) findViewById(R.id.etCurrentPassword);
        etNewPassword = (EditText) findViewById(R.id.etNewPassword);
        etReNewPassword = (EditText) findViewById(R.id.etReNewPassword);

        bundle = getIntent().getExtras();
        if (bundle != null) {
            Gson gson = new Gson();
            String user_json = bundle.getString(Constants.USER_DATA);
            user = gson.fromJson(user_json, User.class);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_close:
                onBackPressed();
                break;
            case R.id.txt_save:
                String old_pass = etCurrentPassword.getText().toString();
                String new_pass = etNewPassword.getText().toString();
                String conf_pass = etReNewPassword.getText().toString();
                if(old_pass.length() == 0){
                    MyUtils.showErrorAlert(this, "Please enter current password");
                }
                else if(!new_pass.equals(conf_pass)){
                    MyUtils.showErrorAlert(this, "New password does not match");
                }
                else if(new_pass.length() == 0){
                    MyUtils.showErrorAlert(this, "New password cannot be empty");
                }
                else{
                    changePassword();
                }
                break;
        }
    }

    public void changePassword(){
        DialogUtils.displayProgress(ChangePasswordActivity.this);
        ProfileApi profileApi = MainApplication.getInstance().getRetrofit().create(ProfileApi.class);
        Call<ServerRes> response = profileApi.changePassword(user.getToken(), etCurrentPassword.getText().toString(), etNewPassword.getText().toString());
        response.enqueue(new Callback<ServerRes>() {
            @Override
            public void onResponse(Call<ServerRes> call, Response<ServerRes> ServerResponseResponse) {
                if (ChangePasswordActivity.this == null) return;
                ServerRes ServerRes = ServerResponseResponse.body();
                Log.d(TAG + " onFailure", ServerResponseResponse.body().toString());
                if (ServerRes.isSuccess()) {
                    DialogUtils.closeProgress();
                    Toast.makeText(ChangePasswordActivity.this, ServerRes.getMessage(), Toast.LENGTH_LONG).show();
                    onBackPressed();
                } else if(ServerRes.isAuthFailed()) {
                    DialogUtils.closeProgress();
                    User.LogOut(ChangePasswordActivity.this);
                }else{
                    Toast.makeText(ChangePasswordActivity.this, ServerRes.getMessage(), Toast.LENGTH_LONG).show();
                    DialogUtils.closeProgress();
                }
            }

            @Override
            public void onFailure(Call<ServerRes> ServerResponseCall, Throwable t) {
                Toast.makeText(ChangePasswordActivity.this, "On failure : error encountered", Toast.LENGTH_LONG).show();
                Log.d(TAG + " onFailure", t.toString());
                DialogUtils.closeProgress();
            }
        });
    }
}
