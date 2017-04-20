package com.tied.android.tiedapp.ui.fragments.coworker;

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
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.ui.activities.goal.LineViewGoalActivity;
import com.tied.android.tiedapp.ui.adapters.GoalsAdapter;
import com.tied.android.tiedapp.util.MyUtils;

public class ActivityFragment extends Fragment implements AdapterView.OnItemClickListener,View.OnClickListener {

    public static final String TAG = ActivityFragment.class
            .getSimpleName();

    protected User user;
    protected Bundle bundle;
    protected ListView listView;

    protected GoalsAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.line_active_goals_fragment,null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        initComponent(view);
    }

    public void initComponent(View view){
        bundle = getArguments();
        user = MyUtils.getUserFromBundle(bundle);
        listView = (ListView) view.findViewById(R.id.list);
        listView.setOnItemClickListener(this);

        adapter = new GoalsAdapter(MainApplication.goals, getActivity());
        listView.setAdapter(adapter);
        if (MainApplication.goals.size() == 0){
            MyUtils.initGoals(getActivity(), user, adapter);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d(TAG, "here---------------- listener");
        Goal goal = (Goal) MainApplication.goals.get(position);
        Log.d(TAG, "here----------------"+goal.toString());
        bundle.putSerializable(Constants.GOAL_DATA, goal);
        MyUtils.startActivity(getActivity(), LineViewGoalActivity.class, bundle);
    }

    @Override
    public void onClick(View v) {

    }
}
