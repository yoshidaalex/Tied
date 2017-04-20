package com.tied.android.tiedapp.ui.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.ui.fragments.DailyStatsFragment;
import org.w3c.dom.Text;

/**
 * Created by femi on 11/21/2016.
 */
public class ActivityMaintenance extends FragmentActivity implements View.OnClickListener {
    public static String MESSAGE ="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // this.overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_left);
        setContentView(R.layout.activity_maintenance_mode);
        if(MESSAGE.isEmpty()) MESSAGE=getString(R.string.app_name)+" is undergoing maintenance. Please check back later";
        ((TextView)findViewById(R.id.message)).setText(MESSAGE);


    }



    @Override
    public void onClick(View view) {
        finish();
    }
}
