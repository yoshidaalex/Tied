package com.tied.android.tiedapp.ui.fragments.notification;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.ImageView;

import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.Notification;
import com.tied.android.tiedapp.objects._Meta;
import com.tied.android.tiedapp.objects.responses.GeneralResponse;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.NotificationApi;
import com.tied.android.tiedapp.ui.activities.MainActivity;
import com.tied.android.tiedapp.ui.activities.client.ActivityClientProfile;
import com.tied.android.tiedapp.ui.activities.coworker.ViewCoWorkerActivity;
import com.tied.android.tiedapp.ui.activities.schedule.ScheduleDetailsActivitiy;
import com.tied.android.tiedapp.ui.activities.visits.ActivityVisitDetails;
import com.tied.android.tiedapp.ui.adapters.ExpendableNotificationListAdapter;
import com.tied.android.tiedapp.ui.dialogs.DialogUtils;
import com.tied.android.tiedapp.util.Logger;
import com.tied.android.tiedapp.util.MyUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;


/**
 * Created by Femi on 7/30/2016.
 */
public class NotificationFragment extends Fragment {
    private static final String TAG = NotificationFragment.class.getSimpleName();
    private Bundle bundle;
    private User user;

    ExpandableListView expandableListView;
    ImageView img_close;

    ExpendableNotificationListAdapter listAdapter;
    List<String> listDataHeader;
    HashMap<String, ArrayList<Notification>> listDataChild;
    ArrayList<Notification> temp;
View view;
    public static Fragment newInstance(Bundle bundle) {
        Fragment fragment = new NotificationFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_notification, container, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getActivity().getResources().getColor(R.color.blue_status_bar));
        }
        MainActivity.getInstance().clearNewAlertCount();


        bundle = getArguments();
        user = MyUtils.getUserFromBundle(bundle);

        initComponent(view);
        return view;
    }

    private void initComponent(View view) {

        expandableListView = (ExpandableListView) view.findViewById(R.id.expandableListView);
        prepareListData();

        listAdapter = new ExpendableNotificationListAdapter(getActivity(), listDataHeader, listDataChild);
        expandableListView.setAdapter(listAdapter);

        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Notification model = (Notification) listDataChild.get(listDataHeader.get(groupPosition))
                        .get(childPosition);

                switch (model.getObject()) {
                    case "user":
                        if (user.getId().equals(model.getUser_id())) {
                            ((MainActivity) getActivity()).launchFragment(Constants.Profile, bundle);
                        } else {
                            bundle.putString("user_id", model.getObject_id());
                            MyUtils.startActivity(getActivity(), ViewCoWorkerActivity.class, bundle);
                        }
                        break;
                    case "visit":
                        bundle.putString("visit_id", model.getId());
                        MyUtils.startActivity(getActivity(), ActivityVisitDetails.class, bundle);
                        break;
                    case "client":
                        bundle.putString("client_id", model.getId());
                        MyUtils.startActivity(getActivity(), ActivityClientProfile.class, bundle);
                        break;
                    case "schedule":
                        bundle.putString("schedule_id", model.getId());
                        MyUtils.startActivity(getActivity(), ScheduleDetailsActivitiy.class, bundle);
                        break;
                }
                return false;
            }
        });
    }
    public void refresh() {
        prepareListData();
        MainActivity.getInstance().refresh.setRefreshing(false);
    }
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, ArrayList<Notification>>();

        DialogUtils.displayProgress(getActivity());

        final NotificationApi notificationApi =  MainApplication.createService(NotificationApi.class);
        Call<ResponseBody> response = notificationApi.getNotifications(user.getId(), 1);
        response.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> resResponse) {
                if (this == null) {
                    // Logger.write("null activity");
                    return;
                }
                //Logger.write("(((((((((((((((((((((((((((((999999");
                DialogUtils.closeProgress();

                try {
                    GeneralResponse response = new GeneralResponse(resResponse.body());

                    if (response.isAuthFailed()) {
                        User.LogOut(getActivity());
                        return;
                    }

                    temp = new ArrayList<Notification>();

                    _Meta meta = response.getMeta();
                    if (meta != null && meta.getStatus_code() == 200) {
                        JSONObject jsonObject=new JSONObject(response.toString());
                        JSONArray notification_obj = jsonObject.getJSONArray("notification");
                        int len=notification_obj.length();
                        for (int i = 0 ; i < len; i++) {
                            JSONObject item = notification_obj.getJSONObject(i);

                            String date = MyUtils.getDate(item.getLong("created"));

                            Notification notificationDataModel = new Notification();
                            notificationDataModel.setId(item.getString("id"));
                            notificationDataModel.setComment(item.getString("comment"));
                            notificationDataModel.setObject(item.getString("object"));
                            notificationDataModel.setObject_id(item.getString("object_id"));
                            notificationDataModel.setType(item.getString("type"));
                            notificationDataModel.setGroup(item.getString("group"));
                            notificationDataModel.setUser_id(item.getString("user_id"));
                            notificationDataModel.setSeen(item.getBoolean("seen"));
                            notificationDataModel.setCreated(item.getLong("created"));

                            temp.add(notificationDataModel);

                            if (!listDataHeader.contains(date)) {
                                listDataHeader.add(date);
                            }
                        }

                        for (int i = 0 ; i < listDataHeader.size() ; i++) {
                            ArrayList<Notification> temp1 = new ArrayList<Notification>();
                            for (int j = 0 ; j < temp.size(); j++) {
                                Notification data = temp.get(j);

                                String date = MyUtils.getDate(data.getCreated());

                                if (listDataHeader.get(i).equals(date)) {
                                    temp1.add(data);
                                }
                            }
                            listDataChild.put(listDataHeader.get(i), temp1);
                            expandableListView.expandGroup(i);
                        }

                        if(len==0) view.findViewById(R.id.no_results).setVisibility(View.VISIBLE);
                        else view.findViewById(R.id.no_results).setVisibility(View.GONE);

                    } else {
                        MyUtils.showToast("Error encountered");
                        DialogUtils.closeProgress();
                    }

                } catch (Exception e) {
                    Logger.write(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                Logger.write(" onFailure", t.toString());
            }
        });
//
//        // Adding child data
//        listDataHeader.add("JANUARY 10TH");
//        listDataHeader.add("JANUARY 9TH");
//

//
//        for (int i = 0 ; i < 4 ; i++) {
//            NotificationDataModel notificationDataModel = new NotificationDataModel();
//
//            notificationDataModel.setTxt_time("3:57 PM");
//            if (i == 0) {   //notification status
//                notificationDataModel.setTxt_first("Added");
//                notificationDataModel.setTxt_end("as a client");
//                notificationDataModel.setbNew(true);
//            } else if (i == 1) {
//                notificationDataModel.setTxt_first("Completed new a new");
//                notificationDataModel.setTxt_end("Email");
//                notificationDataModel.setbNew(true);
//            } else if (i == 2) {
//                notificationDataModel.setTxt_first("Visited");
//                notificationDataModel.setTxt_end("site");
//                notificationDataModel.setbNew(false);
//            } else if (i == 3) {
//                notificationDataModel.setTxt_first("New invitation received from");
//                notificationDataModel.setTxt_end("");
//                notificationDataModel.setbNew(false);
//            }
//
//            notificationDataModel.setTxt_user("Emmanuel Iroko");
//
//            notificationDataModels.add(notificationDataModel);
//        }
//
//        listDataChild.put(listDataHeader.get(0), notificationDataModels); // Header, Child data
//        listDataChild.put(listDataHeader.get(1), notificationDataModels); // Header, Child data

    }

}
