package com.tied.android.tiedapp.ui.activities.lines;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.objects.Line;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.ui.adapters.LineTerritoriesAdapter;

/**
 * Created by femi on 9/4/2016.
 */
public class LineRelevantInforActivity extends AppCompatActivity implements  View.OnClickListener{

    private Bundle bundle;
    private User user;

    ImageView img_close;
    TextView txt_save;
    EditText website, special_request, openings;
    Spinner reorders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relevant_info);

        bundle = getIntent().getExtras();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.blue_status_bar));
        }

        initComponents();
    }

    @Override
    public void onClick(View view) {
        int id=view.getId();
        switch (id) {
            case R.id.img_close:
                finish();
                break;
            case R.id.txt_save:

                break;
        }
    }

    public void initComponents() {

        img_close = (ImageView) findViewById(R.id.img_close);
        img_close.setOnClickListener(this);

        txt_save = (TextView) findViewById(R.id.txt_save);
        txt_save.setOnClickListener(this);

        website = (EditText) findViewById(R.id.website);
        special_request = (EditText) findViewById(R.id.special_request);
        openings = (EditText) findViewById(R.id.openings);
        reorders = (Spinner) findViewById(R.id.reorders);
    }
}
