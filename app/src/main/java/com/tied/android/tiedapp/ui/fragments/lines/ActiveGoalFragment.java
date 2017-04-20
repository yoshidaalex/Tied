package com.tied.android.tiedapp.ui.fragments.lines;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.Goal;
import com.tied.android.tiedapp.objects.Line;
import com.tied.android.tiedapp.objects._Meta;
import com.tied.android.tiedapp.objects.client.Client;
import com.tied.android.tiedapp.objects.responses.GeneralResponse;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.GoalApi;
import com.tied.android.tiedapp.ui.activities.goal.LineViewGoalActivity;
import com.tied.android.tiedapp.ui.adapters.GoalsAdapter;
import com.tied.android.tiedapp.ui.dialogs.DialogUtils;
import com.tied.android.tiedapp.util.Logger;
import com.tied.android.tiedapp.util.MyUtils;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ActiveGoalFragment extends Fragment implements AdapterView.OnItemClickListener,View.OnClickListener {

    public static final String TAG = ActiveGoalFragment.class
            .getSimpleName();

    protected User user;
    private Client client;
    Line line;
    protected Bundle bundle;
    protected ListView listView;

    protected GoalsAdapter adapter;

    List<Goal> goals=new ArrayList<Goal>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.line_active_goals_fragment,null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initComponent(view);
    }

    public void initComponent(View view){
        bundle = getArguments();
        user = MyUtils.getUserFromBundle(bundle);
        try {
            line = (Line) bundle.getSerializable(Constants.LINE_DATA);
            client = (Client) bundle.getSerializable(Constants.CLIENT_DATA);
        }catch(Exception e) {

        }

        listView = (ListView) view.findViewById(R.id.list);
        listView.setOnItemClickListener(this);

       // Logger.write("Lineeeeeeeeeeeeeeeeeeeeeeeeee "+line.toString());

        if(line!=null || client!=null) view.findViewById(R.id.progress_layout).setVisibility(View.GONE);
        adapter = new GoalsAdapter(goals, getActivity());
        listView.setAdapter(adapter);
        if (goals.size() == 0){
            loadActiveGoals(getActivity());
        }
    }
    private void loadAllActive() {
        if (MainApplication.goals.size() == 0){
            MyUtils.initGoals(getActivity(), user, adapter);
        }
    }

    private void loadActiveGoals(final Context context) {
        final GoalApi goalApi =  MainApplication.createService(GoalApi.class, user.getToken());
        Call<ResponseBody> response;
        if(line!=null)
           response= goalApi.getLineGoals(user.getToken(),line.getId(),  GoalApi.STATUS_INCOMPLETE, 1 );
        else
            response= goalApi.getUserGoals( );
        response.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> resResponse) {
                try {
                    ResponseBody responseBody=resResponse.body();

                    GeneralResponse response = new GeneralResponse(responseBody);
                    if (response.isAuthFailed()) {
                        User.LogOut(context);
                        return;
                    }
                    _Meta meta=response.getMeta();
                    if(meta !=null && meta.getStatus_code() == 200) {
                        ArrayList goals = (ArrayList) response.getDataAsList(Constants.GOAL_lIST, Goal.class);
                        if(goals.size() > 0){
                            ActiveGoalFragment.this.goals = goals;
                            if (adapter != null){
                                adapter.listInit(goals);
                            }
                        }
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d(TAG, "here---------------- listener");
//        Goal goal = (Goal) MainApplication.goals.get(position);
//        Log.d(TAG, "here----------------"+goal.toString());
//        bundle.putSerializable(Constants.GOAL_DATA, goal);
        MyUtils.startActivity(getActivity(), LineViewGoalActivity.class);
    }

    @Override
    public void onClick(View v) {

    }
}
