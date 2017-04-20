package com.tied.android.tiedapp.ui.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.objects.Distance;
import com.tied.android.tiedapp.objects.client.Client;
import com.tied.android.tiedapp.util.MyUtils;

import java.util.ArrayList;

/**
 * Created by Emmanuel on 6/28/2016.
 */
public class ClientDistantAdapter extends ClientParentAdapter{
    public static final String TAG = ClientDistantAdapter.class
            .getSimpleName();

    private ArrayList arraylist;
    ViewHolder v;

    public ClientDistantAdapter(ArrayList clients, Context context) {
        super(clients, context);
        this.arraylist = clients;
    }

    public View getView(int i, View convertView, ViewGroup viewGroup) {

        View view = convertView;
        LayoutInflater li = (LayoutInflater) _c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (_data.get(i) instanceof Client) {
            final Client data = (Client) _data.get(i);
            view = li.inflate(R.layout.schedule_select_client_list_item, viewGroup, false);
            final ImageView imageView = (ImageView) view.findViewById(R.id.pic);
            TextView name = (TextView) view.findViewById(R.id.name);
            TextView address = (TextView) view.findViewById(R.id.address);

            name.setText(data.getFull_name());

            address.setText(data.getAddress().getLocationAddress());
            MyUtils.Picasso.displayImage(data.getLogo(), imageView);

        } else {
            final Distance distance = (Distance) _data.get(i);
            view = li.inflate(R.layout.schedule_select_client_text_distance, viewGroup, false);
            TextView range = (TextView) view.findViewById(R.id.txt_distance);
            String range_displayed = distance.getLower_bound() + " - " + distance.getUpper_bound() + " " + distance.getMeasurement();
            range.setText(range_displayed);
        }
        return view;
    }

    @Override
    public void listInit(ArrayList clients) {
        initAdded();

        ArrayList data = new ArrayList();
        int rangeIndex = 0;
        int minIndex = range[0];
        for (int i = 0; i < range.length - 1; i++) {
            for (int j = 0; j < clients.size(); j++) {
                Client this_client = (Client) clients.get(j);
                if (this_client.getDis_from() >= range[rangeIndex] && this_client.getDis_from() <= range[rangeIndex + 1]) {
                    if (!added[rangeIndex]) {
                        String lower = minIndex + "";
                        String upper = range[rangeIndex + 1] + "";
                        Distance distance = new Distance(lower, upper, "Miles");
                        data.add(distance);
                        added[rangeIndex] = true;
                        Log.d(TAG, "DISTANCE IS RANGE: " + distance.toString() + " j = " + j);
                        minIndex = range[rangeIndex + 1];
                    }
                    Log.d(TAG, "this_client DISTANCE IS : " + this_client.getDis_from() + " name " + this_client.getFull_name() + " j = " + j);
                    data.add(this_client);
                    clients.remove(j);
                    j--;
                }
            }
            rangeIndex++;
        }

        if (clients.size() > 0) {
            String lower = range[rangeIndex] + "";
            String upper = "n";
            Distance distance = new Distance(lower, upper, "Miles");
            data.add(distance);
            data.addAll(clients);
        }
        _data = data;
        notifyDataSetChanged();
        Log.d(TAG, "distanceList "+_data.toString());
    }

    static class ViewHolder {
        ImageView imageView;
        TextView name, address;
    }

    public void initAdded() {
        added = new boolean[range.length - 1];
    }
}

