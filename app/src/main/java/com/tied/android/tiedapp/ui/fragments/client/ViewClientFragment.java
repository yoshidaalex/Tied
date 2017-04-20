package com.tied.android.tiedapp.ui.fragments.client;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.customs.model.ScheduleDataModel;
import com.tied.android.tiedapp.objects.Line;
import com.tied.android.tiedapp.objects.RevenueFilter;
import com.tied.android.tiedapp.objects._Meta;
import com.tied.android.tiedapp.objects.client.Client;
import com.tied.android.tiedapp.objects.responses.GeneralResponse;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.ClientApi;
import com.tied.android.tiedapp.retrofits.services.RevenueApi;
import com.tied.android.tiedapp.ui.activities.client.AddClientActivity;
import com.tied.android.tiedapp.ui.activities.client.ClientInfo;
import com.tied.android.tiedapp.ui.activities.goal.LineGoalActivity;
import com.tied.android.tiedapp.ui.activities.lines.LineClientVisitsActivity;
import com.tied.android.tiedapp.ui.activities.lines.LinesListActivity;
import com.tied.android.tiedapp.ui.activities.sales.ActivityLineClientSales;
import com.tied.android.tiedapp.ui.activities.schedule.ClientSchedulesActivity;
import com.tied.android.tiedapp.ui.activities.visits.ActivityVisitDetails;
import com.tied.android.tiedapp.ui.dialogs.DialogClientOptions;
import com.tied.android.tiedapp.ui.dialogs.DialogUtils;
import com.tied.android.tiedapp.ui.dialogs.DialogYesNo;
import com.tied.android.tiedapp.ui.listeners.FragmentIterationListener;
import com.tied.android.tiedapp.util.HelperMethods;
import com.tied.android.tiedapp.util.Logger;
import com.tied.android.tiedapp.util.MyUtils;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ViewClientFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = ViewClientFragment.class
            .getSimpleName();

    public ImageView avatar,img_edit;
    private LinearLayout icon_plus, icon_call, icon_mail;
    private TextView btn_delete, client_name, totalSales;
    RelativeLayout important_info,lines_territory;
    TextView lastVisitedTV, addressTV;

    private Bundle bundle;
    private User user;

    private Client client;
    private String client_id;

    int source;
    RevenueFilter filter=MyUtils.initializeFilter();

    FragmentIterationListener mListener;
    public static Fragment newInstance(Bundle bundle) {
        Fragment fragment=new ViewClientFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_client, container, false);
        //filter.setStart_d("2000-01-01");

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bundle = getArguments();
        user = MyUtils.getUserFromBundle(bundle);
        client=(Client) bundle.getSerializable(Constants.CLIENT_DATA);
        client_id = (String) bundle.getSerializable("client_id");

        initComponent(view);
    }

    private void initComponent(View view) {
        btn_delete = (TextView) view.findViewById(R.id.txt_delete);
        icon_plus = (LinearLayout) view.findViewById(R.id.icon_plus);
        icon_call = (LinearLayout) view.findViewById(R.id.icon_call);
        icon_mail =(LinearLayout) view.findViewById(R.id.icon_mail);
        client_name = (TextView) view.findViewById(R.id.client_name);
        totalSales=(TextView) view.findViewById(R.id.total_sales);
        img_edit = (ImageView) view.findViewById(R.id.img_edit);
        view.findViewById(R.id.sales_layout).setOnClickListener(this);
        view.findViewById(R.id.schedule_layout).setOnClickListener(this);
        view.findViewById(R.id.visits_layout).setOnClickListener(this);
        view.findViewById(R.id.line_layout).setOnClickListener(this);
        lastVisitedTV=(TextView)view.findViewById(R.id.last_visited) ;
        addressTV=(TextView)view.findViewById(R.id.address) ;

        avatar = (ImageView) view.findViewById(R.id.avatar);

        btn_delete.setOnClickListener(this);
        icon_plus.setOnClickListener(this);
        icon_call.setOnClickListener(this);
        icon_mail.setOnClickListener(this);
        img_edit.setOnClickListener(this);
        try {
            source = bundle.getInt(Constants.SOURCE);
        }catch (Exception e) {
            source = Constants.SALES_SOURCE;
        }

     //  important_info.setOnClickListener(this);
       // lines_territory.setOnClickListener(this);

        if (bundle != null && client != null) {
            Log.d(TAG, "bundle not null");
            Gson gson = new Gson();

            user = MyUtils.getUserFromBundle(bundle);
            client = (Client)bundle.getSerializable(Constants.CLIENT_DATA);

            String logo;
            try{
                logo=client.getLogo().equals("") ? null  : client.getLogo();
            } catch (NullPointerException npe) {
                logo=null;
            }
            MyUtils.Picasso.displayImage(logo, avatar);
            totalSales.setText(MyUtils.moneyFormat(client.getTotal_revenue()));
            String lastVisited=client.getLast_visited();

            long epoch=0;
            try {
                epoch = MyUtils.parseDate(lastVisited).getTime();
            }catch (Exception e) {

            }

            String timePassedString = ""+ DateUtils.getRelativeTimeSpanString(epoch, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
            lastVisitedTV.setText("Last Visited: "+ (lastVisited==null || lastVisited.isEmpty()?"Never":timePassedString));

            addressTV.setText(client.getAddress().getLocationAddress());
            //setTotalRevenue();
            setVisibile();
        }
        else if (client == null && !client_id.isEmpty()) {
            getClient();
        }

    }


    @Override
    public void onClick(View v) {
        int color = this.getResources().getColor(R.color.schedule_title_bg_color);
        Bundle bundle=new Bundle();
        bundle.putSerializable(Constants.USER_DATA, user);
        bundle.putSerializable(Constants.CLIENT_DATA, client);
        bundle.putInt(Constants.SOURCE, source);
        switch (v.getId()){

            case R.id.txt_delete:
                color = this.getResources().getColor(R.color.alert_bg_color);
                DialogYesNo alert_delete = new DialogYesNo(getActivity(),client, "DELETE CLIENT","All sales, schedule and visits related to this client with be deleted. Are you sure want to continue?","YES DELETE!",color,0);
                alert_delete.showDialog();
                break;
            case R.id.icon_plus:
                DialogClientOptions alert_client = new DialogClientOptions(client,getActivity(),bundle);
                alert_client.showDialog();
                break;
            case R.id.icon_mail:
                color = this.getResources().getColor(R.color.green_color);
                //DialogYesNo alert_call = new DialogYesNo(getActivity(),"CALL CLIENT","Are you sure want to call this client?. Call charges may apply","YES, CALL!",color,1);
               // alert_call.showDialog();
                try {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("message/rfc822");
                    i.putExtra(Intent.EXTRA_EMAIL, new String[]{client.getEmail()});
                    i.putExtra(Intent.EXTRA_SUBJECT, "");
                    i.putExtra(Intent.EXTRA_TEXT, "");

                    try {
                        startActivity(Intent.createChooser(i, "Send mail..."));
                    } catch (android.content.ActivityNotFoundException ex) {
                        MyUtils.showToast("There are no email clients installed.");
                    }
                }catch(Exception e) {
                    MyUtils.showErrorAlert(getActivity(), "Client's email not provided");
                }
                break;
            case R.id.icon_call:
                color = this.getResources().getColor(R.color.green_color);
                try {
                    Logger.write("caling...................");
                    String number = "tel:" + client.getPhone().trim();
                    Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(number));
                    startActivity(callIntent);
                }catch (Exception e) {
                    MyUtils.showErrorAlert(getActivity(), "Client's phone number not provided");
                }
                break;
            case R.id.img_edit:
                MyUtils.startRequestActivity(getActivity(), AddClientActivity.class, Constants.EDIT_CLIENT, bundle);
                break;
            case R.id.goal:
                MyUtils.startActivity(getActivity(), ClientInfo.class, bundle);
                break;
            case R.id.territory:
//                MyUtils.startActivity(getActivity(), LinesListActivity.class, bundle);
                break;
            case R.id.sales_layout:
              //  MyUtils.startRequestActivity(getActivity(), ActivityLineClientSales.class, Constants.SALES_SOURCE, bundle);
                bundle.putSerializable(Constants.CLIENT_DATA, client);
                bundle.putInt(Constants.SOURCE, source);
                bundle.putSerializable(Constants.FILTER, filter);
                MyUtils.startRequestActivity(getActivity(), ActivityLineClientSales.class, Constants.REVENUE_LIST, bundle);
                break;
            case R.id.schedule_layout:
                MyUtils.startRequestActivity(getActivity(), ClientSchedulesActivity.class, Constants.SELECT_CLIENT, bundle);
                break;
            case R.id.line_layout:
                MyUtils.startRequestActivity(getActivity(), LinesListActivity.class, Constants.SELECT_CLIENT, bundle);
                break;
            case R.id.visits_layout:
                MyUtils.startRequestActivity(getActivity(), LineClientVisitsActivity.class, Constants.SELECT_CLIENT, bundle);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.EDIT_CLIENT && resultCode == Activity.RESULT_OK) {
            Bundle bundle = data.getExtras();
            client_id = (String) bundle.getSerializable("client_id");
            getClient();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentIterationListener) {
            mListener = (FragmentIterationListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public void nextAction(Bundle bundle) {
        if (mListener != null) {
            mListener.OnFragmentInteractionListener(Constants.CreateSchedule, bundle);
        }
    }

    public void setTotalRevenue() {
        RevenueApi revenueApi = MainApplication.createService(RevenueApi.class);

        final Call<ResponseBody> response2 = revenueApi.getTotalRevenues("client", client.getId(),filter);
        response2.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> resResponse) {
                if (this == null) return;
                DialogUtils.closeProgress();
                try {
                    //Logger.write(resResponse.body().string());
                    //  JSONObject response = new JSONObject(resResponse.body().string());
                    GeneralResponse response=new GeneralResponse(resResponse.body());
                    Logger.write("RESPONSSSSSSSSSSSSSSSSSSSS "+response.toString());
                    if (response != null && response.isAuthFailed()) {
                        User.LogOut(getActivity());
                        return;
                    }

                    _Meta meta=response.getMeta();
                    if(meta !=null && meta.getStatus_code()==200) {
                        // revenueList.addAll(response.getDataAsList("revenues", Revenue.class));
                        // adapter.notifyDataSetChanged();

                           // client.setTotal_revenue(response.getData("line", Client.class).getTotal_revenue());
                            totalSales.setText("All Time: "+MyUtils.moneyFormat(client.getTotal_revenue()));

                        //totalRevenueBodyTV.setText(MyUtils.moneyFormat(line.getTotal_revenue()));

                    } else {
                        // MyUtils.showToast(getString(R.string.connection_error));
                    }
                }catch (Exception e) {
                    // MyUtils.showConnectionErrorToast(LineRevenueActivity.this);
                    //Logger.write(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //Log.d(TAG + " onFailure", t.toString());
                Logger.write(t.getMessage());
                MyUtils.showConnectionErrorToast(getActivity());
                DialogUtils.closeProgress();
            }
        });
    }

    private void getClient() {
        final ClientApi clientApi =  MainApplication.createService(ClientApi.class);
        Call<ResponseBody> response = clientApi.getClient(client_id);
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

                    _Meta meta = response.getMeta();
                    if (meta != null && meta.getStatus_code() == 200) {

                        client = ( (Client) response.getData("client", Client.class));
                        Picasso.with(getActivity()).load(client.getLogo())
                                .memoryPolicy(MemoryPolicy.NO_CACHE)
                                .networkPolicy(NetworkPolicy.NO_CACHE).into(avatar);
                       // MyUtils.Picasso.displayImage(client.getLogo(), avatar);
                        String lastVisited=client.getLast_visited();
                        lastVisitedTV.setText("Last Visited: "+ (lastVisited==null || lastVisited.isEmpty()?"Never":HelperMethods.getDateDifferenceWithToday(lastVisited)));
                        totalSales.setText(MyUtils.moneyFormat(client.getTotal_revenue()));

                        //setTotalRevenue();
                        setVisibile();

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
    }

    private void setVisibile() {
        if (user.getId().equals(client.getUser_id())) {
            btn_delete.setVisibility(View.VISIBLE);
        }
    }
}
