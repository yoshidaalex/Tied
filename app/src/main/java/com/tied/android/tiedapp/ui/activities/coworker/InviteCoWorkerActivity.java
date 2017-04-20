package com.tied.android.tiedapp.ui.activities.coworker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.CoWorker;
import com.tied.android.tiedapp.objects._Meta;
import com.tied.android.tiedapp.objects.responses.GeneralResponse;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.CoworkerApi;
import com.tied.android.tiedapp.retrofits.services.UserApi;
import com.tied.android.tiedapp.ui.dialogs.DialogUtils;
import com.tied.android.tiedapp.util.Logger;
import com.tied.android.tiedapp.util.MyUtils;
import com.tied.android.tiedapp.util.Utility;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class InviteCoWorkerActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String TAG = InviteCoWorkerActivity.class
            .getSimpleName();

    private Bundle bundle;
    private User user;
    private User coworker;

    private EditText email;
    private String emailText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_coworker);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.gradient));
        }

        bundle = getIntent().getExtras();
        user = MyUtils.getUserFromBundle(bundle);

        email = (EditText) findViewById(R.id.email);

    }

    public void findCoworker(){

        DialogUtils.displayProgress(this);
        Call<ResponseBody> response =  MainApplication.createService(UserApi.class, user.getToken()).findByEmailOrPhone(user.getToken(), coworker);
        Log.d(TAG, response.request().url().toString());
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> responseBodyResponse) {
                if (this == null) return;
                try {
                    GeneralResponse generalResponse = new GeneralResponse(responseBodyResponse.body());
                    Logger.write(generalResponse.toString());
                    User found = generalResponse.getData(Constants.USER, User.class);
                    _Meta meta = generalResponse.getMeta();
                    if(meta.getStatus_code() == 200){
                        Logger.write("Found user : "+found);
                        call_add_CoWorker(user.getToken(), user.getId(), found);
                        DialogUtils.closeProgress();
                    }else{
                        DialogUtils.closeProgress();
                       // MyUtils.showToast(meta.getUser_message());
                        AlertDialog.Builder builder=new AlertDialog.Builder(InviteCoWorkerActivity.this);
                        builder.setMessage("No user found with this contact information. Do you want to invite "+emailText+" to "+getString(R.string.app_name)+"?");
                        final AlertDialog ad=builder.create();
                        ad.setButton(DialogInterface.BUTTON_POSITIVE, "YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                share(emailText);
                            }
                        });
                        ad.setButton(DialogInterface.BUTTON_NEGATIVE, "NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ad.dismiss();
                            }
                        });
                        ad.show();
                    }
                }catch (Exception e) {
                    Logger.write(e);
                    DialogUtils.closeProgress();
                    MyUtils.showToast(InviteCoWorkerActivity.this.getString(R.string.connection_error));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> checkEmailCall, Throwable t) {
                // Toast.makeText(getActivity(), "On failure : error encountered", Toast.LENGTH_LONG).show();
                MyUtils.showToast(InviteCoWorkerActivity.this.getString(R.string.connection_error));
                Logger.write(TAG +" onFailure", t.toString());
                DialogUtils.closeProgress();
            }
        });
    }
/*
    public void continue_action(){

        DialogUtils.displayProgress(this);
        Call<ResponseBody> response =  MainApplication.createService(UserApi.class, user.getToken()).findByEmailOrPhone(emailText);
        Log.d(TAG, response.request().url().toString());
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> responseBodyResponse) {
                if (this == null) return;
                try {
                    GeneralResponse generalResponse = new GeneralResponse(responseBodyResponse.body());
                    User found = generalResponse.getData(Constants.USER, User.class);
                    _Meta meta = generalResponse.getMeta();
                    if(meta.getStatus_code() == 200){
                        Logger.write("Found user : "+found);
                        call_add_CoWorker(user.getId(), found.getId());
                    }else{
                        DialogUtils.closeProgress();
                        MyUtils.showToast(meta.getUser_message());
                    }
                }catch (Exception e) {
                    Logger.write(e);
                    DialogUtils.closeProgress();
                    MyUtils.showToast(InviteCoWorkerActivity.this.getString(R.string.connection_error));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> checkEmailCall, Throwable t) {
                // Toast.makeText(getActivity(), "On failure : error encountered", Toast.LENGTH_LONG).show();
                MyUtils.showToast(InviteCoWorkerActivity.this.getString(R.string.connection_error));
                Logger.write(TAG +" onFailure", t.toString());
                DialogUtils.closeProgress();
            }
        });
    } */
    public void call_add_CoWorker(String token, String user_id, User coworker_id){
        Call<ResponseBody> response =  MainApplication.createService(CoworkerApi.class, user.getToken()).addCoworker(token, coworker_id);
        Log.d(TAG, response.request().url().toString());
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> responseBodyResponse) {
                if (this == null) return;
                try {
                    GeneralResponse generalResponse = new GeneralResponse(responseBodyResponse.body());
                    Logger.write("createdCoWorker : "+generalResponse.toString());
                    if (generalResponse != null && generalResponse.isAuthFailed()) {
                        User.LogOut(InviteCoWorkerActivity.this);
                        return;
                    }
                    CoWorker createdCoWorker = generalResponse.getData(Constants.COWORKER, CoWorker.class);

                    _Meta meta = generalResponse.getMeta();
                    if(meta.getStatus_code() == 200){
                        MyUtils.startActivity(InviteCoWorkerActivity.this,CoWorkerActivity.class, bundle);
                    }else{
                        DialogUtils.closeProgress();
                        MyUtils.showToast(meta.getUser_message());
                    }
                }catch (Exception e) {
                    Logger.write(e);
                    MyUtils.showToast("Error encountered");
                }
                DialogUtils.closeProgress();
            }

            @Override
            public void onFailure(Call<ResponseBody> checkEmailCall, Throwable t) {
                // Toast.makeText(getActivity(), "On failure : error encountered", Toast.LENGTH_LONG).show();
                MyUtils.showToast(InviteCoWorkerActivity.this.getString(R.string.connection_error));
                Logger.write(TAG +" onFailure", t.toString());
                DialogUtils.closeProgress();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_layout:
                super.onBackPressed();
                break;
            case R.id.add:
                MyUtils.startActivity(this, InviteCoWorkerActivity.class, bundle);
                break;
            case R.id.invite:
                emailText = email.getText().toString().trim();
               /* if (!Utility.isEmailValid(emailText)) {
                    MyUtils.showErrorAlert(this, Utility.getResourceString(this, R.string.alert_valide_email));
                } else {
                    ..continue_action();
                }*/
                if(emailText.isEmpty() || (!isValidEmail(emailText)) && !isValidPhone(emailText)) {
                    MyUtils.showErrorAlert(this, "You must enter a valid email or phone number");
                    return;
                }

                coworker=new User();
                if(isValidEmail(emailText)) coworker.setEmail(emailText);
                if(isValidPhone(emailText)) coworker.setPhone(cleanPhone(emailText));
                findCoworker();
                break;
            case R.id.contact_list_button:
                showContactListDialog();
                break;

        }
    }
    private void showContactListDialog() {
        Uri uri = Uri.parse("content://contacts");
        Intent intent = new Intent(Intent.ACTION_PICK, uri);
        intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        startActivityForResult(intent, Constants.PICK_CONTACT);
    }
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        if (requestCode == Constants.PICK_CONTACT) {
            if (resultCode == RESULT_OK) {
                Uri uri = intent.getData();
                String[] projection = {  ContactsContract.CommonDataKinds.Email.ADDRESS, ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME };

                Cursor cursor = getContentResolver().query(uri, projection,
                        null, null, null);
                cursor.moveToFirst();

                int emailColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS);
                String emailText = cursor.getString(emailColumnIndex);

                int numberColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                String number = cursor.getString(numberColumnIndex);

                int nameColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                String name = cursor.getString(nameColumnIndex);

                Logger.write(TAG, "ZZZ number : " + number +" , name : "+name+" email : "+emailText);
               // coworker=new User();
               // if(isValidEmail(email)) coworker.setEmail(email);
               // coworker.setPhone(cleanPhone(number));
                //coworker.setFirst_name(name);
                //findCoworker();
                if(isValidEmail(emailText)) email.setText(emailText);
                else email.setText(number);




            }
        }
    };
    private String cleanPhone(String phoneText) {

        phoneText = phoneText.replaceAll("[)(-]","").replace(" ","");
        if(phoneText.charAt(0) == '0'){
            phoneText = "+1" + phoneText.substring(1);
        }
        if(phoneText.charAt(0) != '+'){
            phoneText = "+1" +phoneText;
        }
        return phoneText;

    }
    public static boolean isValidEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
        return matcher.find();
    }
    public static boolean isValidPhone(String phoneStr) {
        Logger.write(phoneStr.replaceAll("[)(-]","").replace(" ",""));
       try{
           String pn=""+Long.parseLong(phoneStr.replaceAll("[)(-]","").replace(" ",""));
           if(pn.length()<10 || pn.length()>12) return false;
           return true;
       }catch (Exception e) {
           return false;
       }
    }
    private void share(String phone) {
        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        String body ="I'm using "+getString(R.string.app_name)+" for Android and I recommend it to manage your business. Click here: "+getString(R.string.website);

        if(isValidPhone(phone)) {
           // shareIntent.putExtra(Intent.EXTRA_PHONE_NUMBER, phone);
            Intent smsIntent = new Intent(Intent.ACTION_VIEW);
            smsIntent.setType("vnd.android-dir/mms-sms");
            smsIntent.putExtra("address",phone);
            smsIntent.putExtra("sms_body",body);
            startActivity(smsIntent);
            return;
        }
        String subject="Try "+getString(R.string.app_name)+" for Android!";
        if(isValidEmail(phone)) {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto",phone, null));
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{phone });
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
            emailIntent.putExtra(Intent.EXTRA_TEXT, body);
            startActivity(Intent.createChooser(emailIntent, "Send email..."));
          return;

        }
        shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Try Tied for Android!");
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "I'm using "+getString(R.string.app_name)+" for Android and I recommend it to manage your business. Click here: "+getString(R.string.website));

        Intent chooserIntent = Intent.createChooser(shareIntent, "Share with");
        chooserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(chooserIntent);
    }
}
