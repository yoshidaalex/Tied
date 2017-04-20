package com.tied.android.tiedapp.ui.adapters;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.Line;
import com.tied.android.tiedapp.objects.client.Client;
import com.tied.android.tiedapp.ui.listeners.ListAdapterListener;
import com.tied.android.tiedapp.util.MyUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZuumaPC on 8/18/2016.
 */
public class LinesAdapter extends BaseAdapter implements ListAdapterListener {

    private static class ViewHolder {
        TextView txt_line_name,txt_line_sales;
    }

    public static final String TAG = LinesAdapter.class
            .getSimpleName();

    public List<Line> _data;
    Context _c;
    ViewHolder viewHolder;
    private Bundle bundle;
    private Client client;

    public LinesAdapter(List<Line> line_list, Context context, Bundle bundle) {
        _data = line_list;
        _c = context;
        this.bundle = bundle;
//        client = (Client) bundle.getSerializable(Constants.CLIENT_DATA);
//        if (client != null){
//            _data = client.getLines();
//        }
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
            view = li.inflate(R.layout.more_line_territoy_list_item, viewGroup,false);
        } else {
            view = convertView;
        }
        viewHolder = new ViewHolder();

        viewHolder.txt_line_name = (TextView) view.findViewById(R.id.line);
        viewHolder.txt_line_sales = (TextView) view.findViewById(R.id.sales);

        final Line data = (Line) _data.get(i);

        viewHolder.txt_line_name.setText(data.getName());
        //  String sales = data.getSales() + Constants.TOTAL_SALES;
        viewHolder.txt_line_sales.setText(MyUtils.moneyFormat(data.getTotal_revenue()));
        view.setTag(data);
        return view;
    }
//
//    public void filterLines(){
//        ArrayList<Line> arrayList = new ArrayList<>();
//        for(Line line: _data){
//            ArrayList<Client> clients = line.getClients();
//            for(int i = 0; i < clients.size(); i++){
//                Client c = clients.get(i);
//                if(c.getId().equals(client.getId())){
//                    arrayList.add(line);
//                }
//            }
//        }
//        _data = arrayList;
//    }

    @Override
    public void listInit(ArrayList arrayList) {
        if (client == null){
            this._data = arrayList;
            notifyDataSetChanged();
        }
    }

}
