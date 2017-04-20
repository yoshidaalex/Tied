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
import com.tied.android.tiedapp.objects.Line;
import com.tied.android.tiedapp.objects.client.Client;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.util.MyUtils;
import com.tied.android.tiedapp.util.RoundImage;
import de.hdodenhof.circleimageview.CircleImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Femi on 7/22/2016.
 */
public class MyClientLineAdapter extends BaseAdapter {
    public static final String TAG = ClientScheduleAdapter.class
            .getSimpleName();

    public List<Object> _data;
    private List<String> selected=null;
    Context _c;
    ViewHolder viewHolder;
    boolean isMultiple=false;
   // RoundImage roundedImage;

   /* public MyClientLineAdapter(List<Object> data, Context context) {
        _data = data;
        _c = context;

    }*/
    public MyClientLineAdapter(List<Object> data, List<String> selected, Context context, boolean isMultiple) {
        _data = data;
        _c = context;
        this.selected=(selected==null?new ArrayList<String>():selected);
        this.isMultiple=isMultiple;
    }
    public MyClientLineAdapter(ArrayList data, Context context, boolean isMultiple) {
        _data = data;
        _c = context;
        this.selected=new ArrayList<String>();
        this.isMultiple=isMultiple;
    }

    public void setSelected(List<String> selected) {
        this.selected=(selected==null?new ArrayList<String>():selected);
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
            viewHolder=new ViewHolder();
            LayoutInflater li = (LayoutInflater) _c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = li.inflate(R.layout.line_clients_list_item, viewGroup,false);
            viewHolder.roundedImage=(CircleImageView)view.findViewById(R.id.pic);
            viewHolder.name=(TextView)view.findViewById(R.id.name);
            viewHolder.description=(TextView)view.findViewById(R.id.description);
            viewHolder.selector=(ImageView)view.findViewById(R.id.selector);
            viewHolder.pic=(ImageView)view.findViewById(R.id.pic);

            view.setTag(viewHolder);
        } else {
           viewHolder= (ViewHolder) convertView.getTag();;
        }

     if( _data.get(i) instanceof Client){
            final Client client = (Client) _data.get(i);
            viewHolder.name.setText( MyUtils.getClientName(client));
             MyUtils.Picasso.displayImage(client.getLogo(), viewHolder.roundedImage);
             viewHolder.description.setText(client.getAddress().getCity()+", "+client.getAddress().getState());
         if(isMultiple) {
             if (client.getCheckStatus()) {
                 viewHolder.selector.setImageResource(R.drawable.selected_bg);
             } else {
                 viewHolder.selector.setImageResource(R.drawable.unselectd_bg);
             }
         }else{
             viewHolder.selector.setVisibility(View.GONE);
         }
            //viewHolder.name.setText(data.getFull_name());

        }
        if( _data.get(i) instanceof Line){
            final Line line = (Line) _data.get(i);
            viewHolder.name.setText(line.getName());
           // MyUtils.Picasso.displayImage(client.getLogo(), viewHolder.roundedImage);
            viewHolder.roundedImage.setVisibility(View.VISIBLE);
            viewHolder.roundedImage.setImageResource(R.drawable.ic_layer_gray);
            try{
                viewHolder.description.setText(line.getDescription().substring(0,20)+"...");
            }catch (Exception e) {
                viewHolder.description.setText(line.getDescription());
            }

            if(isMultiple) {
                if (selected.contains(line.getId())) {
                    viewHolder.selector.setImageResource(R.drawable.selected_bg);
                } else {
                    viewHolder.selector.setImageResource(R.drawable.unselectd_bg);
                }
            }else{
                viewHolder.selector.setVisibility(View.GONE);
            }
            //viewHolder.name.setText(data.getFull_name());

        }

        if( _data.get(i) instanceof User){
            final User user = (User) _data.get(i);
            viewHolder.name.setText(user.getFullName());
            // MyUtils.Picasso.displayImage(client.getLogo(), viewHolder.roundedImage);
            viewHolder.roundedImage.setVisibility(View.GONE);
            try{
                viewHolder.description.setText("");
            }catch (Exception e) {
                //viewHolder.description.setText(line.getDescription());
            }
            if(viewHolder.pic.getTag() == null || !viewHolder.pic.getTag().equals(user.getAvatar())) {
                MyUtils.Picasso.displayImage(user.getAvatar(), viewHolder.pic);
            }
            viewHolder.pic.setTag(user.getAvatar());
            viewHolder.pic.setVisibility(View.VISIBLE);

            if(isMultiple) {
                if (selected.contains(user.getId())) {
                    viewHolder.selector.setImageResource(R.drawable.selected_bg);
                } else {
                    viewHolder.selector.setImageResource(R.drawable.unselectd_bg);
                }
            }else{
                viewHolder.selector.setVisibility(View.GONE);
            }
            //viewHolder.name.setText(data.getFull_name());

        }
        return view;
    }



    static class ViewHolder {
        ImageView selector, pic;
        TextView name, description;
        CircleImageView roundedImage;
    }
}
