package com.tied.android.tiedapp.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tied.android.tiedapp.R;

/**
 * Created by hitendra on 9/13/2016.
 */
public class SelectedIndustryAdapter extends RecyclerView.Adapter<SelectedIndustryAdapter.ViewHolder> {
    private Context mContext;

    private String[] tmpIndustries = {"food", "clothing", "cosmetics", "health"};

    public SelectedIndustryAdapter(Context context) {
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.row_industry_selected, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvIndustryName.setText(tmpIndustries[position]);
    }

    @Override
    public int getItemCount() {
        return tmpIndustries.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvIndustryName;

        public ViewHolder(View itemView) {
            super(itemView);
            tvIndustryName = (TextView) itemView.findViewById(R.id.tvIndustryName);
        }
    }
}
