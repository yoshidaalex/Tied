package com.tied.android.tiedapp.ui.adapters;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.model.SelectContact;
import com.tied.android.tiedapp.util.RoundImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Emmanuel on 6/15/2016.
 */
public class SelectContactAdapter extends BaseAdapter {

    public List<SelectContact> _data;
    private ArrayList<SelectContact> arraylist;
    Context _c;
    ViewHolder v;
    RoundImage roundedImage;

    public SelectContactAdapter(List<SelectContact> SelectContacts, Context context) {
        _data = SelectContacts;
        _c = context;
        this.arraylist = new ArrayList<SelectContact>();
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
            view = li.inflate(R.layout.invite_employee_list_item, null);
        } else {
            view = convertView;
        }

        v = new ViewHolder();
        v.title = (TextView) view.findViewById(R.id.name);
        v.check = (ImageView) view.findViewById(R.id.check);
        v.phone = (TextView) view.findViewById(R.id.no);
        v.imageView = (ImageView) view.findViewById(R.id.pic);

        final SelectContact data = (SelectContact) _data.get(i);
        v.title.setText(data.getName());
        if (data.getCheckStatus()) {
            v.check.setBackgroundResource(R.mipmap.circle_check2);
        } else {
            v.check.setBackgroundResource(R.mipmap.circle_uncheck);
        }
        v.phone.setText(data.getPhone());

        // Set image if exists
        try {

            if (data.getThumb() != null) {
//                v.imageView.setImageBitmap(data.getThumb());
                // Setting round image
                Bitmap bm = data.getThumb();
                roundedImage = new RoundImage(bm);
                v.imageView.setImageDrawable(roundedImage);
            } else {
//                v.imageView.setImageResource(R.mipmap.default_avatar);
                // Setting round image
                Bitmap bm = BitmapFactory.decodeResource(view.getResources(), R.mipmap.default_avatar); // Load default image
                roundedImage = new RoundImage(bm);
                v.imageView.setImageDrawable(roundedImage);
            }

        } catch (OutOfMemoryError e) {
            // Add default picture
            v.imageView.setImageDrawable(this._c.getDrawable(R.mipmap.default_avatar));
            e.printStackTrace();
        }
 
        // Set check box listener android
        v.check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean same = data.getCheckStatus();
                if (same) {
                    data.setCheckStatus(false);
                    v.check.setBackgroundResource(R.mipmap.circle_uncheck);
                } else {
                    data.setCheckStatus(true);
                    v.check.setBackgroundResource(R.mipmap.circle_check2);
                }
                notifyDataSetChanged();
            }
        });

        view.setTag(data);
        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        _data.clear();
        if (charText.length() == 0) {
            _data.addAll(arraylist);
        } else {
            for (SelectContact wp : arraylist) {
                if (wp.getName().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    _data.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
    static class ViewHolder {
        ImageView imageView,check;
        TextView title, phone;
    }
   
}
