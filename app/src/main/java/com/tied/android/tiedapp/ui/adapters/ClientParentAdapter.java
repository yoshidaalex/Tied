package com.tied.android.tiedapp.ui.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.tied.android.tiedapp.objects.client.Client;
import com.tied.android.tiedapp.ui.listeners.ListAdapterListener;
import com.tied.android.tiedapp.util.RoundImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Emmanuel on 8/21/2016.
 */
public class ClientParentAdapter extends BaseAdapter implements ListAdapterListener {

    protected List _data;
    protected int[] range = {0, 500, 1000, 2000, 5000};
    protected boolean[] added;

    protected ArrayList arraylist;

    RoundImage roundedImage;
    Context _c;

    public ClientParentAdapter(List clients, Context context) {
        _data = clients;
        _c = context;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public void listInit(ArrayList arrayList) {

    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        _data.clear();
        if (charText.length() == 0) {
            _data.addAll(arraylist);
        } else {
            for (Object wp : arraylist) {
                if(wp instanceof Client){
                    if (((Client) wp).getFull_name().toLowerCase(Locale.getDefault())
                            .contains(charText)) {
                        _data.add(wp);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }

    public List getList(){
        return _data;
    }

}
