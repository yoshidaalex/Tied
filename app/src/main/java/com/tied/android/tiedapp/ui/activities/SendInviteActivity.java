package com.tied.android.tiedapp.ui.activities;

import android.os.Bundle;
import android.app.Activity;

import com.tied.android.tiedapp.R;

public class SendInviteActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_left);
        setContentView(R.layout.activity_send_invite);
    }
}
