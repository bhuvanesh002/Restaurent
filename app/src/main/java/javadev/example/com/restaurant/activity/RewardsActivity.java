package javadev.example.com.restaurant.activity;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
import javadev.example.com.restaurant.model.Rewards;
import javadev.example.com.restaurant.ui.SmartAppConstants;

public class RewardsActivity extends AppCompatActivity {
    Rewards mRewards;
    String mTotalpoints;
    Toolbar mToolbar;
    RecyclerView mRewardsRecyclerView;
    TextView mOneTextView, mTwoTextView, mPointsTextView;
    LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewards);
        Typeface face = Typeface.createFromAsset(getAssets(), SmartAppConstants.BOLD_FONT);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_rewards);
        setSupportActionBar(mToolbar);
        mToolbar.setTitleTextColor(getResources().getColor(R.color.black));
        getSupportActionBar().setTitle(R.string.rewards);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mRewardsRecyclerView = (RecyclerView) findViewById(R.id.recycler_rewards);
        mOneTextView = (TextView) findViewById(R.id.textView_youhave);
        mTwoTextView = (TextView) findViewById(R.id.textView_pointsleft);
        mPointsTextView = (TextView) findViewById(R.id.textView_points);
        mOneTextView.setTypeface(face);
        mTwoTextView.setTypeface(face);
        mPointsTextView.setTypeface(face);
        ArrayList<Rewards> mRewardsList = new ArrayList<>();
        InputStream inputStream = getResources().openRawResource(R.raw.rewards);
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
            // Parse the data into jsonobject to get original data in form of json.
            JSONObject jObject = new JSONObject(
                    byteArrayOutputStream.toString());
            mTotalpoints = jObject.getString("total_points");
            JSONArray jsonArray = jObject.getJSONArray("history");
            for (int i = 0; i < jsonArray.length(); i++) {
                mRewards = new Rewards();
                JSONObject rewardsObject = jsonArray.getJSONObject(i);
                mRewards.location = rewardsObject.getString("location");
                mRewards.date = rewardsObject.getString("date");
                mRewards.points = rewardsObject.getInt("points");
                mRewards.operation = rewardsObject.getString("operation");
                mRewardsList.add(mRewards);
            }
        } catch (Exception e) {

        }
        mPointsTextView.setText(mTotalpoints);
        RewardsAdapter mRewardsAdapter;
        mRewardsAdapter = new RewardsAdapter(getApplicationContext(), mRewardsList);
        mRewardsRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRewardsRecyclerView.setLayoutManager(mLayoutManager);
        mRewardsRecyclerView.setAdapter(mRewardsAdapter);
    }

    static class RewardsAdapter extends RecyclerView.Adapter<RewardsAdapter.ViewHolder> {
        ArrayList<Rewards> data = new ArrayList<>();
        static Context mContext;
        String mOperation;

        static class ViewHolder extends RecyclerView.ViewHolder {
            public TextView mLocationNameTextView, mRewardsDateTextView, mRewardsPointsTextView, mPointsTextView;

            ViewHolder(View v) {
                super(v);
                Typeface face = Typeface.createFromAsset(mContext.getAssets(), SmartAppConstants.BOLD_FONT);
                mLocationNameTextView = (TextView) v.findViewById(R.id.text_locationName);
                mRewardsDateTextView = (TextView) v.findViewById(R.id.text_rewardsdate);
                mRewardsPointsTextView = (TextView) v.findViewById(R.id.text_rewardspoints);
                mPointsTextView = (TextView) v.findViewById(R.id.text_point);
                mLocationNameTextView.setTypeface(face);
                mRewardsDateTextView.setTypeface(face);
                mRewardsPointsTextView.setTypeface(face);
                mPointsTextView.setTypeface(face);

            }
        }


        RewardsAdapter(Context context, ArrayList<Rewards> mDataset) {
            mContext = context;
            data = mDataset;
        }

        @Override
        public RewardsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int position) {
            // create a new view
            View v = null;
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.rewards_adapter, parent, false);
            return new ViewHolder(v);

        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            mOperation = data.get(position).getOperation();
            holder.mLocationNameTextView.setText(data.get(position).getLocation());
            holder.mRewardsDateTextView.setText(data.get(position).getDate());
            if (mOperation.equals("add")) {
                holder.mRewardsPointsTextView.setTextColor(mContext.getResources().getColor(R.color.pointcolor));
                holder.mRewardsPointsTextView.setText("+" + data.get(position).getPoints());
            } else if (mOperation.equals("redeem")) {
                holder.mRewardsPointsTextView.setText("-" + data.get(position).getPoints());
            }
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
