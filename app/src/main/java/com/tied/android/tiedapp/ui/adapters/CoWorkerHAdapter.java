package com.tied.android.tiedapp.ui.adapters;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import android.widget.TextView;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.client.Client;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.ui.activities.coworker.ViewCoWorkerActivity;
import com.tied.android.tiedapp.ui.listeners.ListAdapterListener;
import com.tied.android.tiedapp.util.Logger;
import com.tied.android.tiedapp.util.MyUtils;
import com.tied.android.tiedapp.util.RoundImage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Emmanuel on 9/9/2016.
 */
public class CoWorkerHAdapter extends RecyclerView.Adapter<CoWorkerHAdapter.ViewHolder> implements ListAdapterListener {

    private Activity activity;
    public List<User> _data;

    RoundImage roundedImage;

    public CoWorkerHAdapter(List<User> clients, Activity activity) {
        this.activity = activity;
        _data = clients;
       // this.arraylist = new ArrayList<Client>();
       // this.arraylist.addAll(_data);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.coworker_h_item, viewGroup, false);
        return new ViewHolder(view);
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

        final User data = (User) _data.get(position);
        String logo = data.getAvatar().equals("") ? null  : data.getAvatar();
        MyUtils.Picasso.displayImage(logo ,viewHolder.pic);
        MyListener myListener = new MyListener(data);
        viewHolder.name.setText(data.getFirst_name());
        viewHolder.pic.setOnClickListener(myListener);


    }

    @Override
    public int getItemCount() {
        return (null != _data ? _data.size() : 0);
    }

    @Override
    public void listInit(ArrayList arrayList) {
        this._data = arrayList;
        notifyDataSetChanged();
    }

    private class MyListener implements View.OnClickListener{
        private User user;
        public MyListener(User client){
            this.user = client;
        }
        @Override
        public void onClick(View v) {
            Bundle bundle=new Bundle();
            bundle.putSerializable(Constants.USER_DATA, user);
            MyUtils.startActivity(activity, ViewCoWorkerActivity.class, bundle);
        }
    }

    /**
     * View holder to display each RecylerView item
     */
    protected class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView pic;
        private TextView name;
        public ViewHolder(View view) {
            super(view);
            pic = (ImageView) view.findViewById(R.id.pic);
            name = (TextView) view.findViewById(R.id.name);
        }
    }
}
