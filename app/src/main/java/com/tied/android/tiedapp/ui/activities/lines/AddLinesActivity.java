package com.tied.android.tiedapp.ui.activities.lines;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.customs.ui.MyEditText;
import com.tied.android.tiedapp.objects.Line;
import com.tied.android.tiedapp.objects._Meta;
import com.tied.android.tiedapp.objects.responses.GeneralResponse;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.LineApi;
import com.tied.android.tiedapp.ui.activities.MainActivity;
import com.tied.android.tiedapp.ui.dialogs.DialogUtils;
import com.tied.android.tiedapp.util.Logger;
import com.tied.android.tiedapp.util.MyUtils;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Femi on 7/30/2016.
 */
public class AddLinesActivity extends AppCompatActivity implements  View.OnClickListener{
    private static final String TAG = AddLinesActivity.class.getSimpleName();
    private Bundle bundle;
    private User user;
    TextView btn_add;
    LinearLayout back_layout;

    EditText descriptonET, nameET;
    Line line;
    TextView txt_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_left);
        setContentView(R.layout.activity_line_add);

        bundle = getIntent().getExtras();
        user = MyUtils.getUserFromBundle(bundle);
        line = (Line) bundle.getSerializable(Constants.LINE_DATA);
        initComponent();
    }

    private void initComponent() {

        nameET =(EditText) findViewById(R.id.name);
        descriptonET =(EditText) findViewById(R.id.description);

        txt_title = (TextView) findViewById(R.id.txt_title);
        btn_add = (TextView) findViewById(R.id.btn_add);
        btn_add.setOnClickListener(this);
        back_layout = (LinearLayout) findViewById(R.id.back_layout);
        back_layout.setOnClickListener(this);
        if(line!=null) {
            nameET.setText(line.getName());
            descriptonET.setText(line.getDescription());

            txt_title.setText("Update Line");
            btn_add.setText("Update Line");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
                saveForm();
                break;
            case R.id.back_layout:
               onBackPressed();
                break;
        }
    }

    private void saveForm() {

        String lineName = nameET.getText().toString().trim();
        Logger.write(lineName);
        if(lineName.isEmpty()) {
            MyUtils.showToast("You must provide a name for this line");
            return;
        }
        String description = descriptonET.getText().toString().trim();
        if(line==null) line=new Line();


        if(line.getId()!=null && !line.getId().isEmpty()) {
            line.setName(lineName);
            line.setDescription(description);

            updateLine(line);
        }
        else {
            final Line line=new Line();
            line.setName(lineName);
            line.setDescription(description);

            saveLine(line);
        }
    }

    private void saveLine(final Line line) {

        LineApi lineApi = MainApplication.createService(LineApi.class, user.getToken());
        DialogUtils.displayProgress(this);
        Call<ResponseBody> response = lineApi.createLine(line);
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> resResponse) {
                if (this == null) return;
                try {
                    GeneralResponse response =new GeneralResponse(resResponse.body());
                    if (response.isAuthFailed()) {
                        User.LogOut(AddLinesActivity.this);
                        return;
                    }
                    _Meta meta=response.getMeta();
                    if(meta !=null && meta.getStatus_code()==201) {

                        Line the_line = response.getData(Constants.LINE_DATA, Line.class);
                        Logger.write("the_line: "+the_line.toString());
                        final Bundle bundle = new Bundle();
                        bundle.putSerializable(Constants.LINE_DATA, the_line);
                        MainApplication.linesList.clear();
                        MyUtils.showMessageAlert(AddLinesActivity.this, "Line \""+nameET.getText().toString()+"\" created!");
                        nameET.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                DialogUtils.closeProgress();
                                MyUtils.startRequestActivity(AddLinesActivity.this, ViewLineActivity.class, Constants.ADD_SALES, bundle);
                                finish();
                            }
                        }, 2000);

                    }else{
                        MyUtils.showToast("Error encountered");
                        DialogUtils.closeProgress();
                    }

                }catch (IOException ioe) {
                    DialogUtils.closeProgress();
                    MyUtils.showToast("Error encountered. Please check your internet connection.");

                    Logger.write(ioe);
                }
                catch (Exception jo) {
                    DialogUtils.closeProgress();
                    Logger.write(jo);
                }

            }
            @Override
            public void onFailure(Call<ResponseBody> ClientResponseCall, Throwable t) {
                Logger.write("Request failed: "+t.getCause());
                MyUtils.showConnectionErrorToast(AddLinesActivity.this);
                DialogUtils.closeProgress();
            }
        });
    }

    private void updateLine(final Line line) {

        LineApi lineApi = MainApplication.createService(LineApi.class, user.getToken());
        DialogUtils.displayProgress(this);
        Call<ResponseBody> response = lineApi.updateLine(line.getId(), line);
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> resResponse) {
                if (this == null) return;
                try {
                    GeneralResponse response =new GeneralResponse(resResponse.body());
                    if (response.isAuthFailed()) {
                        User.LogOut(AddLinesActivity.this);
                        return;
                    }
                    _Meta meta=response.getMeta();
                    if(meta !=null && meta.getStatus_code()==200) {

                        MyUtils.showMessageAlert(AddLinesActivity.this, "Update Successfully");

                        Intent intent = new Intent();
                        intent.putExtra(Constants.LINE_DATA, line);
                        setResult(RESULT_OK, intent);
                        finishActivity(Constants.Lines);
                        finish();
                    }else{
                        MyUtils.showToast("Error encountered");
                        DialogUtils.closeProgress();
                    }

                }catch (IOException ioe) {
                    DialogUtils.closeProgress();
                    MyUtils.showToast("Error encountered. Please check your internet connection.");

                    Logger.write(ioe);
                }
                catch (Exception jo) {
                    DialogUtils.closeProgress();
                    Logger.write(jo);
                }

            }
            @Override
            public void onFailure(Call<ResponseBody> ClientResponseCall, Throwable t) {
                Logger.write("Request failed: "+t.getCause());
                MyUtils.showConnectionErrorToast(AddLinesActivity.this);
                DialogUtils.closeProgress();
            }
        });
    }


}
