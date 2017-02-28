package javadev.example.com.restaurant.activity;

import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import javadev.example.com.restaurant.R;
import javadev.example.com.restaurant.fragment.DelieveryOrderFragment;
import javadev.example.com.restaurant.fragment.PickUpFragment;
import javadev.example.com.restaurant.ui.SmartAppConstants;

public class OrderHistoryActivity extends AppCompatActivity {
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_pickuporder);
        setSupportActionBar(mToolbar);
        mToolbar.setTitleTextColor(getResources().getColor(R.color.black));
        getSupportActionBar().setTitle(R.string.order_history);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Typeface face = Typeface.createFromAsset(getAssets(), SmartAppConstants.BOLD_FONT);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout_order);
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.pickups)));
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.delivery)));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabTextColors(ColorStateList.valueOf(getResources().getColor(R.color.black)));
        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager_order);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public class PagerAdapter extends FragmentStatePagerAdapter {
        int mNumOfTabs;

        public PagerAdapter(FragmentManager fm, int NumOfTabs) {
            super(fm);
            this.mNumOfTabs = NumOfTabs;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    PickUpFragment pickup_fragment = new PickUpFragment();
                    return pickup_fragment;
                case 1:
                    DelieveryOrderFragment delivery_fragment = new DelieveryOrderFragment();
                    return delivery_fragment;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return mNumOfTabs;
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
