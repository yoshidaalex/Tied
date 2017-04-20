package com.tied.android.tiedapp.ui.activities.lines;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.Line;
import com.tied.android.tiedapp.objects._Meta;
import com.tied.android.tiedapp.objects.client.Client;
import com.tied.android.tiedapp.objects.responses.GeneralResponse;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.ClientApi;
import com.tied.android.tiedapp.retrofits.services.LineApi;
import com.tied.android.tiedapp.ui.adapters.LinesAdapter;
import com.tied.android.tiedapp.ui.dialogs.DialogUtils;
import com.tied.android.tiedapp.util.Logger;
import com.tied.android.tiedapp.util.MyUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;


/**
 * Created by ZuumaPC on 8/18/2016.
 */
public class LinesSelect extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    public static final String TAG = LinesSelect.class
            .getSimpleName();

    protected User user;
    protected Client client;
    protected Bundle bundle;
    protected ListView listView;
    public List<Line> lines =new ArrayList<>();

    public LinesAdapter adapter;
    ImageView img_back, img_plus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_lines);

        initComponent();
    }

    public void initComponent(){
        bundle = getIntent().getExtras();
        user = MyUtils.getUserFromBundle(bundle);
        client = (Client) bundle.getSerializable(Constants.CLIENT_DATA);

        MyUtils.setColorTheme(this, bundle.getInt(Constants.SOURCE), findViewById(R.id.main_layout));

        img_plus = (ImageView) findViewById(R.id.img_plus);
        img_plus.setVisibility(View.GONE);

        listView = (ListView) findViewById(R.id.lines_listview);

        adapter = new LinesAdapter(lines, this, bundle);
        listView.setAdapter(adapter);
        Logger.write(user.toString());

        initLines();
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int
            position, long id) {
        Line line = lines.get(position);
        addClientLine(line.getId());
    }

    public void addClientLine(String line_id) {
        final LineApi lineApi = MainApplication.createService(LineApi.class);
        Call<ResponseBody> response = lineApi.addLineClient(line_id, client);

        response.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> resResponse) {
                try {
                    GeneralResponse response = new GeneralResponse(resResponse.body());
                    Logger.write(response.toString());
                    if (response.isAuthFailed()) {
                        User.LogOut(LinesSelect.this);
                        return;
                    }
                    _Meta meta=response.getMeta();
                    if(meta !=null && meta.getStatus_code() == 201) {
                        Intent intent = new Intent();
                        intent.putExtra(Constants.CLIENT_DATA, client);
                        setResult(RESULT_OK, intent);
                        finishActivity(Constants.ADD_LINE);
                        finish();
                    }else{
                        MyUtils.showToast("Error encountered");
                        DialogUtils.closeProgress();
                    }

                }catch (IOException ioe) {
                    Logger.write(ioe);
                }
                catch (Exception jo) {
                    Logger.write(jo);
                }
                DialogUtils.closeProgress();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(" onFailure", t.toString());
            }
        });
    }

    public void initLines(){

        final LineApi lineApi = MainApplication.createService(LineApi.class);
        Call<ResponseBody> response = lineApi.getUserLines(user.getId(), 1);

        response.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> resResponse) {
                try {
                    GeneralResponse response = new GeneralResponse(resResponse.body());
                    Logger.write(response.toString());
                    if (response.isAuthFailed()) {
                        User.LogOut(LinesSelect.this);
                        return;
                    }
                    _Meta meta=response.getMeta();
                    if(meta !=null && meta.getStatus_code() == 200) {
                        lines.clear();
                        lines .addAll( (ArrayList) response.getDataAsList(Constants.LINES_lIST, Line.class));
                        Logger.write("Lines loadeddddddddddddddddddddddddddddddddddddddddddddddd "+lines.size());
                        adapter.notifyDataSetChanged();
                    }else{
                        MyUtils.showToast("Error encountered");
                        DialogUtils.closeProgress();
                    }

                }catch (IOException ioe) {
                    Logger.write(ioe);
                }
                catch (Exception jo) {
                    Logger.write(jo);
                }
                DialogUtils.closeProgress();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(" onFailure", t.toString());
            }
        });
    }

}
