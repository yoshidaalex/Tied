package com.tied.android.tiedapp.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tied.android.tiedapp.R;

/**
 * Created by hitendra on 7/27/2016.
 */
public class SalesPrivacyAdapter extends RecyclerView.Adapter<SalesPrivacyAdapter.ViewHolder> {
    private OnItemClickListener mOnItemClickListener;
    private Context mContext;

    public SalesPrivacyAdapter(Context context) {
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.row_sales_privacy, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Button btnSelect;

        public ViewHolder(View itemView) {
            super(itemView);
            btnSelect = (Button) itemView.findViewById(R.id.btnSelect);
            btnSelect.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mOnItemClickListener != null)
                mOnItemClickListener.onItemClick(view, getPosition());
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }


    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }
}
