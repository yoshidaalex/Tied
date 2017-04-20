package com.tied.android.tiedapp.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.objects.client.Client;
import com.tied.android.tiedapp.util.MyUtils;

import java.util.ArrayList;

/**
 * Created by Emmanuel on 6/28/2016.
 */
public class ClientListAdapter extends ClientParentAdapter {
    public static final String TAG = ClientListAdapter.class
            .getSimpleName();

    ViewHolder v;

    public ClientListAdapter(ArrayList clients, Context context) {
        super(clients, context);
        this.arraylist = clients;
    }


    public View getView(int i, View convertView, ViewGroup viewGroup) {

        View view = convertView;
        if (view == null) {
            v = new ViewHolder();
            LayoutInflater li = (LayoutInflater) _c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = li.inflate(R.layout.schedule_select_client_list_item, viewGroup, false);

            Client data = (Client) _data.get(i);

            v.name = (TextView) view.findViewById(R.id.name);
            v.address = (TextView) view.findViewById(R.id.address);
            v.imageView = (ImageView) view.findViewById(R.id.pic);

            v.name.setText(data.getFull_name());
            v.address.setText(data.getAddress().getLocationAddress());
            MyUtils.Picasso.displayImage(data.getLogo(), v.imageView);

            view.setTag(v);
        } else {
            v = (ViewHolder) convertView.getTag();;
        }

        return view;
    }

    @Override
    public void listInit(ArrayList arrayList) {
        this._data = arrayList;
        notifyDataSetChanged();
    }


    static class ViewHolder {
        ImageView imageView;
        TextView name,address;
    }

}

