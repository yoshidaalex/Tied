package com.tied.android.tiedapp.ui.adapters;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.client.Client;
import com.tied.android.tiedapp.ui.activities.schedule.CreateAppointmentActivity;
import com.tied.android.tiedapp.util.RoundImage;
import com.tied.android.tiedapp.ui.dialogs.ScheduleDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Emmanuel on 6/29/2016.
 */
public class ClientScheduleHorizontalAdapter extends RecyclerView.Adapter<ClientScheduleHorizontalAdapter.ViewHolder> {

    private Activity activity;
    public List<Client> _data;
    private ArrayList<Client> arraylist;

    RoundImage roundedImage;

    public ClientScheduleHorizontalAdapter(List<Client> clients,Activity activity) {
        this.activity = activity;
        _data = clients;
        this.arraylist = new ArrayList<Client>();
        this.arraylist.addAll(_data);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.schedule_clients_cardview_item, viewGroup, false);

        return new ViewHolder(view);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

        final Client data = (Client) _data.get(position);
        viewHolder.name.setText(data.getFull_name());

        String logo = data.getLogo().equals("") ? null  : data.getLogo();
        Picasso.with(activity).
                load(logo)
                .into(new Target() {
                    @Override public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        if (bitmap != null){
                            viewHolder.pic.setImageBitmap(bitmap);
                        }else{
                            viewHolder.pic.setImageResource(R.mipmap.default_avatar);
                        }
                    }
                    @Override public void onBitmapFailed(Drawable errorDrawable) { }
                    @Override public void onPrepareLoad(Drawable placeHolderDrawable) { }
                });

        MyListener myListener = new MyListener(data);
        viewHolder.schedule.setOnClickListener(myListener);
        viewHolder.menue_icon.setOnClickListener(myListener);
    }

    @Override
    public int getItemCount() {
        return (null != _data ? _data.size() : 0);
    }


    private class MyListener implements View.OnClickListener{
        private Client client;
        public MyListener(Client client){
            this.client = client;
        }
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.schedule:
                    Intent intent = new Intent(activity, CreateAppointmentActivity.class);
                    intent.putExtra(Constants.CLIENT_DATA, client);
                    activity.startActivity(intent);
                    break;
                case R.id.menu_icon:
                    ScheduleDialog alert = new ScheduleDialog();
                    alert.showDialog(client, activity);
                    break;
            }
        }
    }



    /**
     * View holder to display each RecylerView item
     */
    protected class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView menue_icon;
        private ImageView pic;
        private TextView name;
        private TextView schedule;

        public ViewHolder(View view) {
            super(view);
            menue_icon = (ImageView) view.findViewById(R.id.menu_icon);
            pic = (ImageView) view.findViewById(R.id.pic);
            name = (TextView) view.findViewById(R.id.name);
            schedule = (TextView) view.findViewById(R.id.schedule);
        }
    }
}
