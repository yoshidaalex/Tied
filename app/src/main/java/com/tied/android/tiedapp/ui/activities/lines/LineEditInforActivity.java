package com.tied.android.tiedapp.ui.activities.lines;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.customs.model.TerritoryModel;
import com.tied.android.tiedapp.objects.Line;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.ui.adapters.LineTerritoriesAdapter;
import com.tied.android.tiedapp.util.Logger;
import com.tied.android.tiedapp.util.MyUtils;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by femi on 9/4/2016.
 */
public class LineEditInforActivity extends AppCompatActivity implements  View.OnClickListener{

    private Bundle bundle;
    private User user;

    ListView territories_listview;
    LineTerritoriesAdapter territoriesAdapter;

    ImageView img_close;
    TextView txt_save, txt_delete;
    EditText txt_name, txt_description;

    private Line line;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_edit_infor);

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
            case R.id.txt_delete:

                break;
        }
    }

    public void initComponents() {

        img_close = (ImageView) findViewById(R.id.img_close);
        img_close.setOnClickListener(this);

        txt_save = (TextView) findViewById(R.id.txt_save);
        txt_save.setOnClickListener(this);

        txt_delete = (TextView) findViewById(R.id.txt_delete);
        txt_delete.setOnClickListener(this);

        txt_name = (EditText) findViewById(R.id.txt_name);
        txt_description = (EditText) findViewById(R.id.txt_description);
    }
}
