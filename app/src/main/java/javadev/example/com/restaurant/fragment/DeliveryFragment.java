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
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javadev.example.com.restaurant.R;
import javadev.example.com.restaurant.model.Store;
import javadev.example.com.restaurant.ui.SmartAppConstants;

/**
 * Created by Support on 12/30/2016.
 */
public class DeliveryFragment extends Fragment {
    ArrayList<Store> data = new ArrayList<>();
    Store store;
    RecyclerView mRecyclerView;
    TextView mStoreTextView;
    Button mAddMemberButton, mSubmitButton;
    private StoreAdapter mStoreAdapter;
    LinearLayoutManager mLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_stores, container, false);
        Typeface face = Typeface.createFromAsset(getActivity().getAssets(), SmartAppConstants.BOLD_FONT);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_stores);
        mStoreTextView = (TextView) view.findViewById(R.id.textView_storenear);
        mStoreTextView.setTypeface(face);
        InputStream inputStream = getResources().openRawResource(R.raw.stores);
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
            JSONArray jArray = jObject.getJSONArray("stores");
            for (int i = 0; i < jArray.length(); i++) {
                store = new Store();
                store.nickname = jArray.getJSONObject(i).getJSONObject("address").getString("name");
                store.address = jArray.getJSONObject(i).getJSONObject("address").getString("addr");
                store.suiteno = jArray.getJSONObject(i).getJSONObject("address").getString("suite");
                store.city = jArray.getJSONObject(i).getJSONObject("address").getString("city");
                store.state = jArray.getJSONObject(i).getJSONObject("address").getString("state");
                store.zipcode = jArray.getJSONObject(i).getJSONObject("address").getString("zipcode");
                store.open = jArray.getJSONObject(i).getString("open");
                store.opentime = jArray.getJSONObject(i).getString("opentime");
                store.closetime = jArray.getJSONObject(i).getString("closetime");
                store.closereason = jArray.getJSONObject(i).getString("closereason");
                data.add(store);
            }
        } catch (Exception e) {//
            e.printStackTrace();
        }
        mStoreAdapter = new StoreAdapter(getActivity(), data);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mStoreAdapter);
        return view;
    }

    public static class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.ViewHolder> {
        ArrayList<Store> data = new ArrayList<>();
        static Context mContext;

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public TextView mNickNameTextView, mAddressTextView, mErrorTextView;
            public Button mOrderButton;

            public ViewHolder(View v) {
                super(v);
                Typeface face = Typeface.createFromAsset(mContext.getAssets(), SmartAppConstants.BOLD_FONT);
                mNickNameTextView = (TextView) v.findViewById(R.id.text_nickname);
                mAddressTextView = (TextView) v.findViewById(R.id.address);
                mErrorTextView = (TextView) v.findViewById(R.id.textView_error);
                mOrderButton = (Button) v.findViewById(R.id.button_order);
                mNickNameTextView.setTypeface(face);
                mAddressTextView.setTypeface(face);
                mErrorTextView.setTypeface(face);
            }
        }

        public StoreAdapter(Context context, ArrayList<Store> myDataset) {
            mContext = context;
            data = myDataset;
        }

        @Override
        public StoreAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
            // create a new view
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.store_adapter, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            String mCheck = null;
            if (data.get(position).getOpen().equals("yes")) {
                mCheck = "Available";
            } else if (data.get(position).getOpen().equals("no")) {
                mCheck = "Not Available";
            }
            holder.mNickNameTextView.setText(data.get(position).getNickname());
            holder.mAddressTextView.setText(data.get(position).getSuiteno() + "," + data.get(position).getAddress() + "\n" + data.get(position).getCity() + "," + data.get(position).getState() + "\n" + data.get(position).getZipcode() + "\n" + data.get(position).getOpentime() + "-" + data.get(position).getClosetime() + "\n" + mCheck);
            try {
                holder.mErrorTextView.setText(data.get(position).getClosereason());
            } catch (Exception e) {
            }

        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }


}


