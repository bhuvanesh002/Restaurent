package javadev.example.com.restaurant.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;

import javadev.example.com.restaurant.R;
import javadev.example.com.restaurant.model.Addresses;
import javadev.example.com.restaurant.ui.SmartAppConstants;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {
    ArrayList<Addresses> data = new ArrayList<>();
    static Context mContext;
    public static boolean mstatus=false;
    private static int lastCheckedPos = 0;
    private static RadioButton lastChecked = null;
    int clickedPos;
    private static OnItemsClickListener mOnitemclicklistner;
    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView mNickNameTextView, mAddressTextView;
        RadioButton mRadioButton = null;
         boolean status = false;

        ViewHolder(View v) {
            super(v);
            Typeface face = Typeface.createFromAsset(mContext.getAssets(), SmartAppConstants.BOLD_FONT);
            mNickNameTextView = (TextView) v.findViewById(R.id.text_nickname);
            mAddressTextView = (TextView) v.findViewById(R.id.address);
            mRadioButton = (RadioButton) v.findViewById(R.id.radioButton);
            mNickNameTextView.setTypeface(face);
            mAddressTextView.setTypeface(face);

        }

        @Override
        public void onClick(View v) {
            mOnitemclicklistner.onItemClick(v, getPosition());

        }
    }
    public interface OnItemsClickListener {
        public void onItemClick(View view, int position);

    }

    public void SetOnItemClickListener(OnItemsClickListener monitemclicklistner) {
        System.out.println("possssdhndj");
        this.mOnitemclicklistner = monitemclicklistner;

    }

    public AddressAdapter(Context context, ArrayList<Addresses> myDataset, OnItemsClickListener onitemclicklistner) {
        mContext = context;
        data = myDataset;
        this.mOnitemclicklistner = onitemclicklistner;
    }

    @Override
    public AddressAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int position) {
        // create a new view
        View v = null;
        v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.address_adapter, parent, false);

        return new ViewHolder(v);

    }



    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.mNickNameTextView.setText(data.get(position).getNickname());
        holder.mRadioButton.setChecked(data.get(position).isSelected());
        holder.mRadioButton.setTag(new Integer(position));
        holder.mAddressTextView.setText(data.get(position).getSuiteno() + "," + data.get(position).getAddress() + "\n" + data.get(position).getCity() + "," + data.get(position).getState() + "\n" + data.get(position).getZipcode());
        if (position == 0 && data.get(0).isSelected() && holder.mRadioButton.isChecked()) {
            lastChecked = holder.mRadioButton;
            lastCheckedPos = 0;
        }
        holder.mRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.status = true;
                mstatus=true;
                RadioButton rb = (RadioButton) v;
                clickedPos = ((Integer) rb.getTag()).intValue();
                System.out.println("posi" + clickedPos);
                if (rb.isChecked()) {
                    if (lastChecked != null) {
                        lastChecked.setChecked(false);
                        data.get(lastCheckedPos).setSelected(false);
                    }
                    lastChecked = rb;
                    lastCheckedPos = clickedPos;
                } else
                    lastChecked = null;
                data.get(clickedPos).setSelected(rb.isChecked());
            }
        });
        SharedPreferences.Editor editor = mContext.getSharedPreferences("addresscart", mContext.MODE_PRIVATE).edit();
        editor.putString("address_nick", data.get(clickedPos).getNickname());
        editor.putString("address_add", data.get(clickedPos).getAddress());
        editor.putString("address_suit", data.get(clickedPos).getSuiteno());
        editor.putString("address_state", data.get(clickedPos).getState());
        editor.putString("address_city", data.get(clickedPos).getCity());
        editor.putString("address_zipcode", data.get(clickedPos).getZipcode());
        editor.commit();


    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}