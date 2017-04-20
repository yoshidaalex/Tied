package com.tied.android.tiedapp.ui.adapters;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.objects.client.Client;
import com.tied.android.tiedapp.util.MyUtils;
import com.tied.android.tiedapp.util.RoundImage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Emmanuel on 6/29/2016.
 */
public class ClientScheduleAdapter extends BaseAdapter {
    public static final String TAG = ClientScheduleAdapter.class
            .getSimpleName();

    public List<Client> _data;
    private ArrayList<Client> arraylist;
    Context _c;
    ViewHolder v;
    RoundImage roundedImage;

    public ClientScheduleAdapter(List<Client> clients, Context context) {
        _data = clients;
        _c = context;
        this.arraylist = new ArrayList<Client>();
        this.arraylist.addAll(_data);
    }

    @Override
    public int getCount() {
        return _data.size();
    }

    @Override
    public Object getItem(int i) {
        return _data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view = convertView;
        if (view == null) {
            LayoutInflater li = (LayoutInflater) _c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = li.inflate(R.layout.schedule_clients_suggestion_list_item, viewGroup,false);
        } else {
            view = convertView;
        }

        v = new ViewHolder();
        v.name = (TextView) view.findViewById(R.id.name);
        v.imageView = (ImageView) view.findViewById(R.id.pic);

        final Client data = (Client) _data.get(i);
        v.name.setText(data.getFull_name());

        String logo = data.getLogo().equals("") ? null  : data.getLogo();
        MyUtils.Picasso.displayImage(logo, v.imageView);
        view.setTag(data);
        return view;
    }


    static class ViewHolder {
        ImageView imageView;
        TextView name;
    }

}
