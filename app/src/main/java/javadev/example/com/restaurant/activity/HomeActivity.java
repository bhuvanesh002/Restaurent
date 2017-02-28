package javadev.example.com.restaurant.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.viewpagerindicator.CirclePageIndicator;

import java.util.Timer;
import java.util.TimerTask;

import javadev.example.com.restaurant.R;
import javadev.example.com.restaurant.adapter.HomeAdapter;
import javadev.example.com.restaurant.ui.SmartAppConstants;

public class HomeActivity extends AppCompatActivity implements HomeAdapter.OnItemClickListener {
    RecyclerView mRecyclerView;
    private HomeAdapter mHomeAdapter;
    LinearLayoutManager mLayoutManager;
    Button mDeliveryButton, mTakeawayButton;
    CirclePageIndicator mIndicator;
    private String[] mDataset = {"REWARDS", "TRACK ORDER"};
    ViewPager mViewPager;
    int[] mResources = {
            R.drawable.burger,
            R.drawable.burger,
            R.drawable.burger,
            R.drawable.burger,

    };

    int currentPage;
    CustomPagerAdapter mCustomPagerAdapter;
    Timer swipeTimer;
    private int NUM_PAGES;
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("SMART MENU");
        setSupportActionBar(mToolbar);
        mToolbar.setTitleTextColor(getResources().getColor(R.color.black));
        Typeface face = Typeface.createFromAsset(getAssets(), SmartAppConstants.BOLD_FONT);
        mDeliveryButton = (Button) findViewById(R.id.button);
        mTakeawayButton = (Button) findViewById(R.id.button2);
        mDeliveryButton.setTypeface(face);
        mTakeawayButton.setTypeface(face);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_home);
        mDeliveryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent l = new Intent(HomeActivity.this, AddressActivity.class);
                startActivity(l);
            }
        });
        mTakeawayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent l = new Intent(HomeActivity.this, PickUpActivity.class);
                startActivity(l);
            }
        });
        mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
        mCustomPagerAdapter = new CustomPagerAdapter(this, mResources);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mCustomPagerAdapter);
        mIndicator.setViewPager(mViewPager);
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mViewPager.setCurrentItem(currentPage++, true);
            }
        };

        swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {

            @Override
            public void run() {
                handler.post(Update);
            }
        }, 500, 3000);
        mHomeAdapter = new HomeAdapter(HomeActivity.this, mDataset, this);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(HomeActivity.this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mHomeAdapter);
        mHomeAdapter.SetOnItemClickListener(new HomeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                System.out.println("posss" + position);
                if (position == 0) {
                    Intent i = new Intent(getApplicationContext(), RewardsActivity.class);
                    startActivity(i);
                } else if (position == 1) {
                    Intent i = new Intent(getApplicationContext(), OrderHistoryActivity.class);
                    startActivity(i);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.user:
                Intent i = new Intent(HomeActivity.this, UserActivity.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onItemClick(View view, int position) {

    }

    public class CustomPagerAdapter extends PagerAdapter {
        Context mContext;
        LayoutInflater mLayoutInflater;
        int[] mResources;

        public CustomPagerAdapter(Context context, int[] mResources) {
            mContext = context;
            this.mResources = mResources;
            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }


        @Override
        public int getCount() {
            return mResources.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((LinearLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);
            ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
            imageView.setImageResource(mResources[position]);
            container.addView(itemView);
            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }

    }


}