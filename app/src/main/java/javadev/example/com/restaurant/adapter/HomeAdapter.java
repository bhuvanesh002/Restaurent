package javadev.example.com.restaurant.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import javadev.example.com.restaurant.R;
import javadev.example.com.restaurant.ui.FontAwesomeTextView;
import javadev.example.com.restaurant.ui.SmartAppConstants;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {
    private String[] mDataset;
    static Context mContext;
    private static OnItemClickListener mOnitemclicklistner;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mMenuTextView;
        FontAwesomeTextView mImage;

        public ViewHolder(View v) {
            super(v);
            Typeface face = Typeface.createFromAsset(mContext.getAssets(), SmartAppConstants.BOLD_FONT);
            mMenuTextView = (TextView) v.findViewById(R.id.text_menus);
            mMenuTextView.setTypeface(face);
            mImage = (FontAwesomeTextView) v.findViewById(R.id.image_arrow);
            v.setOnClickListener((View.OnClickListener) this);

        }

        @Override
        public void onClick(View view) {
            mOnitemclicklistner.onItemClick(view, getPosition());
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);

    }

    public void SetOnItemClickListener(OnItemClickListener monitemclicklistner) {
        System.out.println("possssdhndj");
        this.mOnitemclicklistner = monitemclicklistner;

    }

    public HomeAdapter(Context context, String[] myDataset, OnItemClickListener onitemclicklistner) {
        mContext = context;
        mDataset = myDataset;
        this.mOnitemclicklistner = onitemclicklistner;
    }

    @Override
    public HomeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_adapter, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mMenuTextView.setText(mDataset[position]);

    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}

