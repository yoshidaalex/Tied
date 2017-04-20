package com.tied.android.tiedapp.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.Line;
import com.tied.android.tiedapp.objects.Location;
import com.tied.android.tiedapp.objects._Meta;
import com.tied.android.tiedapp.objects.client.Client;
import com.tied.android.tiedapp.objects.responses.GeneralResponse;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.ClientApi;
import com.tied.android.tiedapp.retrofits.services.LineApi;
import com.tied.android.tiedapp.ui.activities.MainActivity;
import com.tied.android.tiedapp.ui.activities.lines.AddLinesActivity;
import com.tied.android.tiedapp.ui.activities.lines.LinesSelect;
import com.tied.android.tiedapp.ui.activities.lines.ViewLineActivity;
import com.tied.android.tiedapp.ui.activities.lines.ViewNewLineActivity;
import com.tied.android.tiedapp.ui.adapters.LinesAdapter;
import com.tied.android.tiedapp.ui.dialogs.DialogUtils;
import com.tied.android.tiedapp.ui.listeners.ListAdapterListener;
import com.tied.android.tiedapp.util.Logger;
import com.tied.android.tiedapp.util.MyUtils;

import junit.framework.Test;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by ZuumaPC on 8/18/2016.
 */
public class LinesFragment extends Fragment implements AdapterView.OnItemClickListener {

    public static final String TAG = LinesFragment.class
            .getSimpleName();

    protected User user;
    protected Client client;
    protected Bundle bundle;
    protected ListView listView;
    public List<Line> lines =new ArrayList<>();

    public LinesAdapter adapter;
    ImageView img_plus;
    TextView no_results;

    int numPages=1;
    private int preLast;
    public int pageNumber=1;

    public static Fragment newInstance(Bundle bundle) {
        Fragment fragment = new LinesFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lines, container, false);

        initComponent(view);
        return view;
    }

    public void initComponent(View view){
        bundle = getArguments();
        user = MyUtils.getUserFromBundle(bundle);
        client = (Client) bundle.getSerializable(Constants.CLIENT_DATA);

        MyUtils.setColorTheme(getActivity(), bundle.getInt(Constants.SOURCE), view.findViewById(R.id.main_layout));

        img_plus = (ImageView) view.findViewById(R.id.img_plus);

        img_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  MyUtils.showNewLineDialog(getActivity(), "Create New Line", new MyUtils.MyDialogClickListener() {
                    @Override
                    public void onClick(Object response) {

                    }
                });*/
                MyUtils.startRequestActivity(getActivity(), LinesSelect.class, Constants.ADD_LINE, bundle);
            }
        });

        no_results = (TextView) view.findViewById(R.id.no_results);
        no_results.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyUtils.startRequestActivity(getActivity(), LinesSelect.class, Constants.ADD_LINE, bundle);
            }
        });

        listView = (ListView) view.findViewById(R.id.lines_listview);
        if(!MyUtils.currentUserIs(user)) {
            img_plus.setVisibility(View.GONE);
        }

        adapter = new LinesAdapter(lines, getActivity(), bundle);
        listView.setAdapter(adapter);
        Logger.write(user.toString());

        initLines();
        listView.setOnItemClickListener(this);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                final int lastItem = firstVisibleItem + visibleItemCount;

                if(lastItem == totalItemCount)
                {
                    if(preLast!=lastItem)
                    {
                        if(pageNumber<numPages) {
                            pageNumber++;
                            initLines();
                        }

                        preLast = lastItem;
                    }
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int
            position, long id) {
        Log.d(TAG, "here---------------- listener");
        Line line = lines.get(position);
        bundle.putSerializable(Constants.LINE_DATA, line);
        MyUtils.startRequestActivity(getActivity(), ViewLineActivity.class, Constants.LineDelete, bundle);
    }

    public void initLines(){
        Call<ResponseBody> response;

        if (client == null) {
            final LineApi lineApi = MainApplication.createService(LineApi.class);
            response = lineApi.getUserLines(user.getId(), pageNumber);
        } else {
            final ClientApi clientApi =  MainApplication.createService(ClientApi.class);
            response = clientApi.getClientLine(client.getId(), pageNumber);
        }

        response.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> resResponse) {
                try {
                    GeneralResponse response = new GeneralResponse(resResponse.body());
                    Logger.write(response.toString());
                    if (response.isAuthFailed()) {
                        User.LogOut(getActivity());
                        return;
                    }
                    _Meta meta=response.getMeta();
                    if(meta !=null && meta.getStatus_code() == 200) {
                        numPages=meta.getPage_count();
                        if(pageNumber==1)  lines.clear();
                        lines.addAll( (ArrayList) response.getDataAsList(Constants.LINES_lIST, Line.class));

                        if(pageNumber==1 && lines.size()==0) {
                            MyUtils.showNoResults(getView(), R.id.no_results);
                        }

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.ADD_LINE) {
                Bundle bundle = data.getExtras();
                client = (Client) bundle.getSerializable(Constants.CLIENT_DATA);
            }
            initLines();
        }
    }
}
