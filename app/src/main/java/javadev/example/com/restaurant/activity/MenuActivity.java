package javadev.example.com.restaurant.activity;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javadev.example.com.restaurant.R;
import javadev.example.com.restaurant.fragment.CategoryFragment;
import javadev.example.com.restaurant.model.Menu;
import javadev.example.com.restaurant.ui.SmartAppConstants;

public class MenuActivity extends AppCompatActivity {
    
    CategoryFragment fragment;
    Menu menu;
    Cursor mCursor;
    int currentPosition = 0;
    Toolbar mToolbar;
    static Animation slide_down, slide_up;
    String mStorename;
    DataBaseHandler db;
    int flag;
    static RelativeLayout sRelativeLayout;
    static TextView sMenuItemTextView, sViewCartTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        System.out.println("menuoncrateeee");
        Typeface face = Typeface.createFromAsset(getAssets(), SmartAppConstants.BOLD_FONT);
        Intent l = getIntent();
        mStorename = l.getStringExtra("store_name");
        flag = l.getIntExtra("flag", 0);
        db = new DataBaseHandler(this);
        if (flag == 1) {
            db.dropTable(Menu.TABLE_CART, Menu.TABLE_SUBCART);
        }

        mToolbar = (Toolbar) findViewById(R.id.toolbar_menu);
        mToolbar.setTitle(mStorename);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setTitleTextColor(getResources().getColor(R.color.black));
        sRelativeLayout = (RelativeLayout) findViewById(R.id.relative_cart);
        sMenuItemTextView = (TextView) findViewById(R.id.textView_ordercount);
        sViewCartTextView = (TextView) findViewById(R.id.textView_viewcart);
        sMenuItemTextView.setTypeface(face);
        sViewCartTextView.setTypeface(face);
        slide_down = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_down);
        slide_up = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_up);
        sViewCartTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent n = new Intent(getApplicationContext(), ViewCartActivity.class);
                startActivityForResult(n, 2);
                finish();
            }
        });

        db = new DataBaseHandler(this);
        SharedPreferences prefs = PreferenceManager.
                getDefaultSharedPreferences(getApplicationContext());
        if (!prefs.contains("insertedIncatDB")) {
            ArrayList<ContentValues> mCategorylist = new ArrayList<ContentValues>();
            InputStream inputStream = getResources().openRawResource(R.raw.menu);
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
                JSONObject jobj = jObject.getJSONObject("menu_list");
                JSONArray jsonArray = jobj.getJSONArray("categories");
                for (int i = 0; i < jsonArray.length(); i++) {
                    ContentValues categorycontentvalues = new ContentValues();
                    JSONObject categoryobject = jsonArray.getJSONObject(i);
                    categorycontentvalues.put(Menu.CATEGORY_ID, categoryobject.getInt(Menu.CATEGORY_ID));
                    categorycontentvalues.put(Menu.CATEGORY_NAME, categoryobject.getString(Menu.CATEGORY_NAME));
                    categorycontentvalues.put(Menu.IMAGE, categoryobject.getString(Menu.IMAGE));
                    mCategorylist.add(categorycontentvalues);
                }
                db.insert(Menu.TABLE_CATEGORY, mCategorylist);

            } catch (Exception e) {
                e.printStackTrace();
            }

            // insert in DB

            // create key in prefs
            Log.d("Insert: ", "Inserting ..");
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("insertedIncatDB", true);
            editor.commit();
        } else {
            // no need to insert in db
        }
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout_menu);
        mCursor = db.query(Menu.TABLE_CATEGORY, Menu.categotyColomns, null, null, null);
        int category_id;
        String category_name;
        if (mCursor != null) {
            System.out.println("countssss" + mCursor.getCount());
            if (mCursor.getCount() > 0 && mCursor.moveToFirst()) {
                do {
                    category_id = mCursor.getInt(mCursor.getColumnIndex(Menu.CATEGORY_ID));
                    category_name = mCursor.getString(mCursor.getColumnIndex(Menu.CATEGORY_NAME));
                    System.out.println("ghajaarferff");
                    tabLayout.addTab(tabLayout.newTab().setText(category_name));
                } while (mCursor.moveToNext());

            }


        }

        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
        tabLayout.setTabTextColors(ColorStateList.valueOf(getResources().getColor(R.color.black)));
        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager_menu);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager());
        viewPager.setOffscreenPageLimit(1);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                currentPosition = tab.getPosition();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        getMenu();
    }

    public void getMenu() {
        String roundtotal;
        int items = 0;
        int item = 0;
        String menu_price;
        float price = 0, total_price = 0, total = 0;
        db = new DataBaseHandler(this);
        SharedPreferences prefs = getSharedPreferences("count_update", MODE_PRIVATE);
        int flag_count = prefs.getInt("count", 0);
        Cursor mCursor = db.query(Menu.TABLE_CART, Menu.CART_COLUMNS, null, null, null);
        if (mCursor != null) {
            System.out.println("countsssscun" + mCursor.getCount());
            if (mCursor.getCount() > 0 && mCursor.moveToFirst()) {
                for (int i = 0; i < mCursor.getCount(); i++) {
                    mCursor.moveToPosition(i);
                    item = mCursor.getInt(mCursor.getColumnIndex(Menu.SELECTED_OPTIONS));
                    menu_price = mCursor.getString(mCursor.getColumnIndex(Menu.MENU_PRICE));
                    price = Float.parseFloat(menu_price.substring(1));
                    items += item;
                    total = price * item;
                    total_price += total;
                }

            }
            mCursor.close();

        }

        if (items == 0) {
            sRelativeLayout.startAnimation(slide_down);
            sRelativeLayout.setVisibility(View.GONE);
        } else if (items == 1 && flag_count != 2) {
            sRelativeLayout.setVisibility(View.VISIBLE);
            sRelativeLayout.startAnimation(slide_up);
        } else if (items >= 2 || flag_count == 2) {
            sRelativeLayout.setVisibility(View.VISIBLE);

        }

        roundtotal = String.format("%.2f", total_price);
        sMenuItemTextView.setText(items + " items" + "\n" + "$ " + roundtotal);

    }

    public class PagerAdapter extends FragmentStatePagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);

        }

        @Override
        public Fragment getItem(int position) {
            mCursor.moveToPosition(position);
            fragment = new CategoryFragment();
            Bundle args = new Bundle();
            args.putInt("category_id", mCursor.getInt(mCursor.getColumnIndex(Menu.CATEGORY_ID)));
            fragment.setArguments(args);
            return fragment;


//

        }

        @Override
        public int getCount() {
            return mCursor != null ? mCursor.getCount() : 0;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCursor.close();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                Cursor cursor = db.getCart();
                if (cursor != null) {
                    if (cursor.getCount() != 0) {
                        showAlert();
                    } else if (cursor.getCount() == 0) {
                        finish();
                    }
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if (requestCode == 2) {
            fragment.refresh();
            getMenu();

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                Cursor cursor = db.getCart();
                if (cursor != null) {
                    if (cursor.getCount() != 0) {
                        showAlert();
                    } else if (cursor.getCount() == 0) {
                        finish();
                    }

                }
        }
        return super.onKeyDown(keyCode, event);
    }


    public void showAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MenuActivity.this);
        // Setting Dialog Message
        alertDialog.setMessage("Are you sure want to discard the store?");
        // On pressing Settings button
        alertDialog.setPositiveButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        // on pressing cancel button
        alertDialog.setNegativeButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        alertDialog.show();
        // Showing Alert Message
    }
}

