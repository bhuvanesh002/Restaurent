package javadev.example.com.restaurant.fragment;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import javadev.example.com.restaurant.activity.DataBaseHandler;
import javadev.example.com.restaurant.activity.MenuActivity;
import javadev.example.com.restaurant.model.Store;
import javadev.example.com.restaurant.model.Tax;
import javadev.example.com.restaurant.ui.SmartAppConstants;

/**
 * Created by Support on 12/28/2016.
 */
public class StoresFragment extends Fragment {
    Store store;
    Tax tax;
    RecyclerView mRecyclerView;
    Button mAddMemberButton, mSubmitButton;
    private StoreAdapter mStoreAdapter;
    LinearLayoutManager mLayoutManager;
    static String mStoreName = null;
    int mLoyaltypoints;
    DataBaseHandler db;
    TextView mStoresTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_stores, container, false);
        Typeface face = Typeface.createFromAsset(getActivity().getAssets(), SmartAppConstants.BOLD_FONT);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_stores);
        mStoresTextView = (TextView) view.findViewById(R.id.textView_storenear);
        mStoresTextView.setTypeface(face);
        ArrayList<Store> data = new ArrayList<>();
        ArrayList<ContentValues> mTaxList = new ArrayList<>();
        db = new DataBaseHandler(getActivity());
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
            mLoyaltypoints = jObject.getInt("loyalty");
            JSONArray jsonTaxArray = jObject.getJSONArray("tax");
            JSONArray jsonLocationArray = jObject.getJSONArray("locations");
            for (int i = 0; i < jsonTaxArray.length(); i++) {
                tax = new Tax();
                JSONObject taxObject = jsonTaxArray.getJSONObject(i);
                ContentValues taxContentValues = new ContentValues();
                taxContentValues.put(Tax.TAXNAME, taxObject.getString(Tax.TAXNAME));
                taxContentValues.put(Tax.TAXPERCENTAGE, taxObject.getString(Tax.TAXPERCENTAGE));
                mTaxList.add(taxContentValues);
            }
            for (int i = 0; i < jsonLocationArray.length(); i++) {
                store = new Store();
                JSONObject storeObject = jsonLocationArray.getJSONObject(i);
                store.store_id = storeObject.getInt("location_id");
                store.nickname = storeObject.getString("location_name");
                store.address = storeObject.getJSONObject("address").getString("addr");
                store.suiteno = storeObject.getJSONObject("address").getString("suite");
                store.city = storeObject.getJSONObject("address").getString("city");
                store.state = storeObject.getJSONObject("address").getString("state");
                store.zipcode = storeObject.getJSONObject("address").getString("zipcode");
                store.delivery_status = storeObject.getString("delivery_status");
                store.opentime = storeObject.getString("opening_time");
                store.closetime = storeObject.getString("closing_time");
                store.closereason = storeObject.getString("delivery_close_reason");
                data.add(store);
            }
            SharedPreferences prefs = PreferenceManager.
                    getDefaultSharedPreferences(getActivity());
            if (!prefs.contains("insertedIntaxDB")) {
                db.insert(Tax.TABLE_TAX, mTaxList);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("insertedIntaxDB", true);
                editor.commit();
            } else {
                // no need to insert in db
            }
        } catch (Exception e) {//
            e.printStackTrace();
        }
        SharedPreferences.Editor editor_stores = getActivity().getSharedPreferences("loyalty", getActivity().MODE_PRIVATE).edit();
        editor_stores.putInt("loyalty_points", mLoyaltypoints);
        editor_stores.commit();
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
        String delivery_status;

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
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            String mCheck = null;
            if (data.get(position).getDelivery_status().equals("open")) {
                mCheck = "Available";
            } else if (data.get(position).getDelivery_status().equals("close")) {
                mCheck = "Not Available";
            }
            holder.mNickNameTextView.setText(data.get(position).getNickname());
            holder.mAddressTextView.setText(data.get(position).getSuiteno() + "," + data.get(position).getAddress() + "\n" + data.get(position).getCity() + "," + data.get(position).getState() + "\n" + data.get(position).getZipcode() + "\n" + data.get(position).getOpentime() + "-" + data.get(position).getClosetime() + "\n" + mCheck);
            try {
                holder.mErrorTextView.setText(data.get(position).getClosereason());
            } catch (Exception e) {
            }
            holder.mOrderButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mStoreName = data.get(position).getNickname();
                    delivery_status = data.get(position).getDelivery_status();
                    Intent n = new Intent(mContext, MenuActivity.class);
                    n.putExtra("store_name", mStoreName);
                    SharedPreferences.Editor editor_stores = mContext.getSharedPreferences("stores", mContext.MODE_PRIVATE).edit();
                    editor_stores.putString("delivery_status", delivery_status);
                    editor_stores.commit();
                    n.putExtra("flag", 1);
                    mContext.startActivity(n);
                }
            });

        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }


}


