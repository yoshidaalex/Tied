package com.tied.android.tiedapp.ui.activities.client;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.client.Client;
import com.tied.android.tiedapp.objects.user.User;

public class ClientInfo extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = ClientInfo.class
            .getSimpleName();
    private User user;
    private Bundle bundle;

    LinearLayout back_layout;
    TextView fax,email,phone,street,city_state;
    String address;
    Client client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_info);

        fax = (TextView) findViewById(R.id.fax);
        email = (TextView) findViewById(R.id.email);
        phone = (TextView) findViewById(R.id.phone);
        street = (TextView) findViewById(R.id.street);
        city_state = (TextView) findViewById(R.id.city_state);
        back_layout = (LinearLayout) findViewById(R.id.back_layout);
        back_layout.setOnClickListener(this);

        bundle = getIntent().getExtras();
        if(bundle != null) {

            Log.d(TAG, "bundle not null");
            Gson gson = new Gson();
            String user_json = bundle.getString(Constants.USER_DATA);
            String client_json = bundle.getString(Constants.CLIENT_DATA);
            user = gson.fromJson(user_json, User.class);
            client = gson.fromJson(client_json, Client.class);


////            user = User.getCurrentUser(getApplicationContext());
//            String user_json = gson.toJson(user);
//            String client_json = gson.toJson(client);
//            bundle.putString(Constants.USER_DATA, user_json);
//            bundle.putString(Constants.CLIENT_DATA, client_json);

            if (client == null) return;
            address = client.getAddress().getCity() +" " + client.getAddress().getState();
            fax.setText(client.getFax());
            email.setText(client.getEmail());
            phone.setText(client.getPhone());
            street.setText(client.getAddress().getStreet());
            city_state.setText(address);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_layout:
                onBackPressed();
                break;

        }
    }

    public void goBack(View v) {
        onBackPressed();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
