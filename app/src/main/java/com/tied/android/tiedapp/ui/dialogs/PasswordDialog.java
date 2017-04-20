package com.tied.android.tiedapp.ui.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.retrofits.services.ProfileApi;
import com.tied.android.tiedapp.objects.responses.ServerRes;
import com.tied.android.tiedapp.objects.user.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Emmanuel on 6/23/2016.
 */
public class PasswordDialog implements View.OnClickListener{

    public static final String TAG = PasswordDialog.class
            .getSimpleName();

    private EditText old_password, new_password, confirm_password;
    private TextView cancel, done;
    private Dialog dialog;
    private Context context;
    private User user;

    public void showDialog(Activity activity, User user){
        dialog = new Dialog(activity);
        context = activity;
        this.user = user;
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_change_password);

        old_password = (EditText) dialog.findViewById(R.id.old_password);
        new_password = (EditText) dialog.findViewById(R.id.new_password);
        confirm_password = (EditText) dialog.findViewById(R.id.confirm_password);

        cancel = (TextView) dialog.findViewById(R.id.cancel);
        done = (TextView) dialog.findViewById(R.id.yes);
        done.setOnClickListener(this);
        cancel.setOnClickListener(this);

        dialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancel:
                dialog.dismiss();
                break;
            case R.id.yes:
                String old_pass = old_password.getText().toString();
                String new_pass = new_password.getText().toString();
                String conf_pass = confirm_password.getText().toString();
                if(new_pass.equals(conf_pass) && conf_pass.length() > 0 && old_pass.length() > 0){
                    changePassword();
                }else{
                    Toast.makeText(context, "password input mismatch", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    public void changePassword(){
        DialogUtils.displayProgress(context);
        ProfileApi profileApi = MainApplication.getInstance().getRetrofit().create(ProfileApi.class);
        Call<ServerRes> response = profileApi.changePassword(user.getToken(), old_password.getText().toString(), new_password.getText().toString());
        response.enqueue(new Callback<ServerRes>() {
            @Override
            public void onResponse(Call<ServerRes> call, Response<ServerRes> ServerResponseResponse) {
                if (context == null) return;
                ServerRes ServerRes = ServerResponseResponse.body();
                Log.d(TAG + " onFailure", ServerResponseResponse.body().toString());
                if (ServerRes.isSuccess()) {
                    DialogUtils.closeProgress();
                    dialog.dismiss();
                    Toast.makeText(context, ServerRes.getMessage(), Toast.LENGTH_LONG).show();
                } else if(ServerRes.isAuthFailed()) {
                    DialogUtils.closeProgress();
                    User.LogOut(context);
                }else{
                    Toast.makeText(context, ServerRes.getMessage(), Toast.LENGTH_LONG).show();
                    DialogUtils.closeProgress();
                }
            }

            @Override
            public void onFailure(Call<ServerRes> ServerResponseCall, Throwable t) {
                Toast.makeText(context, "On failure : error encountered", Toast.LENGTH_LONG).show();
                Log.d(TAG + " onFailure", t.toString());
                DialogUtils.closeProgress();
            }
        });
    }
}
