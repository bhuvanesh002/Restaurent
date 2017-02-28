package javadev.example.com.restaurant.fragment;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javadev.example.com.restaurant.R;
import javadev.example.com.restaurant.model.OrderHistory;
import javadev.example.com.restaurant.ui.SmartAppConstants;

/**
 * Created by Support on 1/27/2017.
 */
public class DelieveryOrderFragment extends Fragment {
    String mOrder_type;
    OrderHistory mOrderHistory;
    RecyclerView mOrderRecyclerView;
    LinearLayoutManager mLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_deliveryorder, container, false);
        mOrderRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_orderdelivery);
        ArrayList<OrderHistory> mDeliveryList = new ArrayList<>();
        InputStream inputStream = getResources().openRawResource(R.raw.orderhistory);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int ctr;
        try {
            ctr = inputStream.read();
            while (ctr != -1) {
                byteArrayOutputStream.write(ctr);
                ctr = inputStream.read();
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.v("Text Data", byteArrayOutputStream.toString());
        try {
            // Parse the mMenuList into jsonobject to get original mMenuList in form of json.
            JSONObject jObject = new JSONObject(
                    byteArrayOutputStream.toString());
            JSONArray jsonArray = jObject.getJSONArray("orderhistory");
            for (int i = 0; i < jsonArray.length(); i++) {
                mOrderHistory = new OrderHistory();
                JSONObject orderhisttyObject = jsonArray.getJSONObject(i);
                mOrder_type = orderhisttyObject.getString("order_type");
                if (mOrder_type.equals("Delivery")) {
                    mOrderHistory.location = orderhisttyObject.getString("location");
                    mOrderHistory.date = orderhisttyObject.getString("date");
                    mOrderHistory.order_number = orderhisttyObject.getString("order_number");
                    mOrderHistory.order_status = orderhisttyObject.getString("order_status");
                    mOrderHistory.price = orderhisttyObject.getString("price");
                    mDeliveryList.add(mOrderHistory);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        OrderPickupAdapter mOrderPickupAdapter;
        mOrderPickupAdapter = new OrderPickupAdapter(getActivity(), mDeliveryList);
        mOrderRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mOrderRecyclerView.setLayoutManager(mLayoutManager);
        mOrderRecyclerView.setAdapter(mOrderPickupAdapter);
        return view;
    }

    static class OrderPickupAdapter extends RecyclerView.Adapter<OrderPickupAdapter.ViewHolder> {
        ArrayList<OrderHistory> data = new ArrayList<>();
        static Context mContext;
        String mOperation;

        static class ViewHolder extends RecyclerView.ViewHolder {
            public TextView mLocationNameTextView, mDateTextView, mOrdernoTextView, mOrderstatusTextView, mOrderPriceTextView;

            ViewHolder(View v) {
                super(v);
                Typeface face = Typeface.createFromAsset(mContext.getAssets(), SmartAppConstants.BOLD_FONT);
                mLocationNameTextView = (TextView) v.findViewById(R.id.textView_orderloc);
                mDateTextView = (TextView) v.findViewById(R.id.textView_orderdate);
                mOrdernoTextView = (TextView) v.findViewById(R.id.textView_orderno);
                mOrderstatusTextView = (TextView) v.findViewById(R.id.textView_orderstatus);
                mOrderPriceTextView = (TextView) v.findViewById(R.id.textView_orderprice);
                mLocationNameTextView.setTypeface(face);
                mDateTextView.setTypeface(face);
                mOrdernoTextView.setTypeface(face);
                mOrderstatusTextView.setTypeface(face);
                mOrderPriceTextView.setTypeface(face);

            }
        }


        OrderPickupAdapter(Context context, ArrayList<OrderHistory> mDataset) {
            mContext = context;
            data = mDataset;
        }

        @Override
        public OrderPickupAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                int position) {
            // create a new view
            View v = null;
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.orderdelivery_adapter, parent, false);
            return new ViewHolder(v);

        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {

            holder.mLocationNameTextView.setText(data.get(position).getLocation());
            holder.mDateTextView.setText(data.get(position).getDate());
            holder.mOrdernoTextView.setText("order no : " + data.get(position).getOrder_number());
            holder.mOrderstatusTextView.setText(data.get(position).getOrder_status());
            holder.mOrderPriceTextView.setText("order total: " + data.get(position).getPrice());


        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }
}
