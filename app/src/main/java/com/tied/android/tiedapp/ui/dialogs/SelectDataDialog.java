package com.tied.android.tiedapp.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.model.DataModel;
import com.tied.android.tiedapp.ui.adapters.DataSelectAdapter;
import com.tied.android.tiedapp.ui.fragments.client.AddClientFragment;

import java.util.ArrayList;

/**
 * Created by ZuumaPC on 7/14/2016.
 */
public class SelectDataDialog implements View.OnClickListener{


    public static final String TAG = SelectDataDialog.class
            .getSimpleName();

    private SelectedListener selectedListener;

    private TextView close;
    private Dialog dialog;
    private Context context;
    private DataModel dataModel;
//    private User user;

    Fragment fragment;
    TextView txt;


    ArrayList<DataModel> listData;
    DataSelectAdapter adapter;
    ListView listView;

    public SelectDataDialog(ArrayList<DataModel> listData, TextView txt,Fragment fragment){
        this.listData = listData;
        this.fragment = fragment;
        this.txt = txt;
    }

    public void showDialog(){
        dialog = new Dialog(fragment.getActivity());
        context = fragment.getActivity();

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_select_data);

        listView = (ListView) dialog.findViewById(R.id.list);

        close = (TextView) dialog.findViewById(R.id.close);
        close.setOnClickListener(this);


        adapter = new DataSelectAdapter(listData,context);
        listView.setAdapter(adapter);

        this.dataModel = listData.get(0);

        dialog.show();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                adapter.setSelectedIndex(position);
                selectedListener = (AddClientFragment) fragment;
                dataModel = listData.get(position);
                selectedListener.selectedNow(dataModel, txt);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.close:
                dialog.dismiss();
                break;
        }
    }

    public interface SelectedListener{
        void selectedNow(DataModel dataModel, TextView txt);
    }
}
