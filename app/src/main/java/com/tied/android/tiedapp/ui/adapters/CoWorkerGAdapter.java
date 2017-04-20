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
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.ui.listeners.ListAdapterListener;
import com.tied.android.tiedapp.util.Logger;
import com.tied.android.tiedapp.util.MyUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Emmanuel on 9/9/2016.
 */
public class CoWorkerGAdapter extends BaseAdapter implements ListAdapterListener {

    private static class ViewHolder {
        ImageView pic;
        TextView name,summary;
    }

    public static final String TAG = LinesAdapter.class
            .getSimpleName();

    public List<User> _data;
    Context _c;
    ViewHolder viewHolder;

    public CoWorkerGAdapter(List<User> line_list, Context context) {
        _data = line_list;
        _c = context;
    }

    @Override
    public int getCount() {
        return _data.size();
    }

    @Override
    public Object getItem(int position) {
        return _data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        View view = convertView;
        if (view == null) {
            LayoutInflater li = (LayoutInflater) _c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = li.inflate(R.layout.coworker_g_item, viewGroup,false);
        } else {
            view = convertView;
        }
        viewHolder = new ViewHolder();

        viewHolder.pic = (ImageView) view.findViewById(R.id.pic);
        viewHolder.name = (TextView) view.findViewById(R.id.name);
       // viewHolder.summary = (TextView) view.findViewById(R.id.summary);

        final User data = (User) _data.get(i);

        Logger.write("Client Name "+data.getFirst_name());

        String logo = data.getAvatar().equals("") ? null  : data.getAvatar();
        MyUtils.Picasso.displayImage(logo ,viewHolder.pic);
        viewHolder.name.setText(data.getFirst_name()+" "+data.getLast_name());
        view.setTag(data);
        return view;
    }

    @Override
    public void listInit(ArrayList arrayList) {
        this._data = arrayList;
        notifyDataSetChanged();
    }
}
