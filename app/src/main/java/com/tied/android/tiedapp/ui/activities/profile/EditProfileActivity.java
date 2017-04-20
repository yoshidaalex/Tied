package com.tied.android.tiedapp.ui.activities.profile;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.responses.ServerRes;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.SignUpApi;
import com.tied.android.tiedapp.ui.dialogs.DialogUtils;
import com.tied.android.tiedapp.ui.listeners.FragmentIterationListener;
import com.tied.android.tiedapp.util.MyUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Emmanuel on 6/22/2016.
 */
public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText first_name, last_name, email, fax, company_name, phone;
    private String firstNameText, lastNameText, emailText, faxText, companyNameText, phoneText, homeAddressText, officeAddressText;

    private ImageView img_close;
    private Bundle bundle;
    private User user;

    private TextView office_address_text, home_address_text, txt_save;

    private LinearLayout home_address, office_address;
    public FragmentIterationListener mListener;

    private TextView tvTitle,tvChange;
    Context context;

    public Retrofit retrofit;
    public SignUpApi service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        context = this;
        retrofit = MainApplication.getInstance().getRetrofit();
        service = retrofit.create(SignUpApi.class);

        initComponent();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        firstNameText = first_name.getText().toString();
        lastNameText = last_name.getText().toString();
        emailText = email.getText().toString();
        phoneText = email.getText().toString();
        faxText = fax.getText().toString();
        companyNameText = company_name.getText().toString();

        outState.putString(Constants.FIRST_NAME, firstNameText);
        outState.putString(Constants.LAST_NAME, lastNameText);
        outState.putString(Constants.EMAIL, emailText);
        outState.putString(Constants.PHONE, emailText);
        outState.putString(Constants.FAX, faxText);
        outState.putString(Constants.COMPANY_NAME, companyNameText);

    }

    public void initComponent() {

        img_close = (ImageView) findViewById(R.id.img_close);
        img_close.setOnClickListener(this);

        txt_save = (TextView) findViewById(R.id.txt_save);
        txt_save.setOnClickListener(this);


        phone = (EditText) findViewById(R.id.etPhoneNumber);
        first_name = (EditText) findViewById(R.id.first_name);
        last_name = (EditText) findViewById(R.id.last_name);
        email = (EditText) findViewById(R.id.email);
        fax = (EditText) findViewById(R.id.fax);
        company_name = (EditText) findViewById(R.id.company_name);

        home_address = (LinearLayout) findViewById(R.id.home_address);
        home_address_text = (TextView) findViewById(R.id.home_address_text);

        office_address = (LinearLayout) findViewById(R.id.office_address);
        office_address_text = (TextView) findViewById(R.id.office_address_text);

        tvChange=(TextView) findViewById(R.id.tvChange);
        tvChange.setOnClickListener(this);

        bundle = getIntent().getExtras();
        if (bundle != null) {
            Gson gson = new Gson();
            String user_json = bundle.getString(Constants.USER_DATA);
            user = gson.fromJson(user_json, User.class);

            first_name.setText(user.getFirst_name());
            last_name.setText(user.getLast_name());
            email.setText(user.getEmail());
            phone.setText(user.getPhone());
            fax.setText(user.getFax());
            company_name.setText(user.getCompany_name());

            if (user.getOffice_address() != null) {
                office_address_text.setText(user.getOffice_address().getLocationAddress());
            }

            if (user.getHome_address() != null) {
                home_address_text.setText(user.getHome_address().getLocationAddress());
            }
/*
            if (user.getIndustries() != null && user.getIndustries().size() > 0) {
                String indust = StringUtils.join(user.getIndustries().toArray(), ", ");
                industry_list.setText(indust);
            }*/
        }
    }

    private void confirmEdit() {
        if (validate()) {
            DialogUtils.displayProgress(this);

            user.setFirst_name(firstNameText);
            user.setLast_name(lastNameText);
            user.setFax(faxText);
            user.setCo_workers(companyNameText);

            Call<ServerRes> response = service.updateUser(user);
            response.enqueue(new Callback<ServerRes>() {
                @Override
                public void onResponse(Call<ServerRes> call, Response<ServerRes> ServerResponseResponse) {
                    ServerRes ServerRes = ServerResponseResponse.body();

                    if (ServerRes.isAuthFailed()) {
                        DialogUtils.closeProgress();
                        User.LogOut(context);
                    } else if (ServerRes.isSuccess()) {
                        boolean saved = user.save(context.getApplicationContext());
                        if (saved) {
                            Gson gson = new Gson();
                            String json = gson.toJson(user);
                            bundle.putString(Constants.USER_DATA, json);
                            DialogUtils.closeProgress();
                            MyUtils.showToast("Information has been updated");
                            onBackPressed();
                        } else {
                            DialogUtils.closeProgress();
                            MyUtils.showErrorAlert(EditProfileActivity.this, "An error occurred. Your info was not updated");
                        }
                    } else {
                        Toast.makeText(context, ServerRes.getMessage(), Toast.LENGTH_LONG).show();
                    }
                    DialogUtils.closeProgress();
                }

                @Override
                public void onFailure(Call<ServerRes> ServerResponseCall, Throwable t) {
                    MyUtils.showErrorAlert(EditProfileActivity.this, "Server error. Please try again later");
                    DialogUtils.closeProgress();
                }
            });
        }
    }

    public boolean validate() {
        firstNameText = first_name.getText().toString();
        lastNameText = last_name.getText().toString();
        emailText = email.getText().toString();
        faxText = fax.getText().toString();
        companyNameText = company_name.getText().toString();

        return (firstNameText != null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_save:
                confirmEdit();
                break;
            case R.id.img_close:
                finish();
                break;
            case R.id.tvChange:
                MyUtils.startActivity(this, AddressActivity.class, bundle);
                break;
        }
    }
}
