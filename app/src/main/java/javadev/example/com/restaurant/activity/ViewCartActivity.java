package javadev.example.com.restaurant.activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javadev.example.com.restaurant.R;
import javadev.example.com.restaurant.model.Menu;
import javadev.example.com.restaurant.ui.SmartAppConstants;

public class ViewCartActivity extends AppCompatActivity {
    TextView mTotalTextView, mTotalCountTextView, mDeliveryTextView, mAddressTextView, mAddressDetailTextView, mSubtotalTextView, mSubcarttotalTextView;
    RecyclerView mRecyclerView;
    Button mPaymentButton;
    static DataBaseHandler db;
    private CartAdapter mCartAdapter;
    LinearLayoutManager mLayoutManager;
    Cursor cartCursor;
    String nickname, address, suitno, city, state, zipcode;
    String mSubtotal = "";
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_cart);
        Typeface face = Typeface.createFromAsset(getAssets(), SmartAppConstants.BOLD_FONT);
        SharedPreferences prefs = getSharedPreferences("addresscart", MODE_PRIVATE);
        nickname = prefs.getString("address_nick", null);
        address = prefs.getString("address_add", null);
        suitno = prefs.getString("address_suit", null);
        state = prefs.getString("address_state", null);
        city = prefs.getString("address_city", null);
        zipcode = prefs.getString("address_zipcode", null);
        System.out.println("nickname" + nickname);
        System.out.println("address" + address);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_cart);
        mSubtotalTextView = (TextView) findViewById(R.id.text_cartsubtotal);
        mSubcarttotalTextView = (TextView) findViewById(R.id.text_cartsubtotaltext);
        mPaymentButton = (Button) findViewById(R.id.button_payment);
        mDeliveryTextView = (TextView) findViewById(R.id.textView_deliverto);
        mAddressTextView = (TextView) findViewById(R.id.textView_cartaddresstit);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_vcart);
        mToolbar.setTitle(R.string.carttitle);
        setSupportActionBar(mToolbar);
        mToolbar.setTitleTextColor(getResources().getColor(R.color.black));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mAddressDetailTextView = (TextView) findViewById(R.id.textView_cartaddressdetail);
        mDeliveryTextView.setTypeface(face);
        mAddressTextView.setTypeface(face);
        mAddressDetailTextView.setTypeface(face);
        mSubtotalTextView.setTypeface(face);
        mSubcarttotalTextView.setTypeface(face);
        mPaymentButton.setTypeface(face);
        mAddressTextView.setText(nickname);
        mAddressDetailTextView.setText(suitno + address + "\n" + city + state + "\n" + zipcode);
        db = new DataBaseHandler(this);
        changeCursor();
        getCartTotal();
        mSubtotal = mSubtotalTextView.getText().toString().substring(2);

        // run once to disable if empty
        mCartAdapter = new CartAdapter(getApplicationContext());
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mCartAdapter);
        mRecyclerView.setNestedScrollingEnabled(false);
        mPaymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PaymentActivity.class);
                intent.putExtra("sub_total", mSubtotal);
                startActivity(intent);
            }
        });


    }

    public void changeCursor() {
        cartCursor = db.getItems();
        if (cartCursor != null) {
            if (cartCursor.getCount() == 0) {
                Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                startActivity(intent);
            }
        }
    }


    public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
        ArrayList<Menu> data = new ArrayList<>();
        Context mContext;
        float total, price;
        int counts = 0, mcount = 0;

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView mMenuNameTextView, mCountTextView, mMenupriceTextView, mTotalTextView, mCustomAddonsTextView;
            public Button mAddButton, mRemoveButton;


            public ViewHolder(View v) {
                super(v);
                Typeface face = Typeface.createFromAsset(mContext.getAssets(), SmartAppConstants.BOLD_FONT);
                mMenuNameTextView = (TextView) v.findViewById(R.id.text_cartmenuname);
                mCountTextView = (TextView) v.findViewById(R.id.text_cartcounttext);
                mMenupriceTextView = (TextView) v.findViewById(R.id.text_cartprice);
                mCustomAddonsTextView = (TextView) v.findViewById(R.id.textView_customizaddons);
                mTotalTextView = (TextView) v.findViewById(R.id.text_total);
                mAddButton = (Button) v.findViewById(R.id.button_cartadd);
                mRemoveButton = (Button) v.findViewById(R.id.button_cartsubtract);
                mMenuNameTextView.setTypeface(face);
                mCountTextView.setTypeface(face);
                mCustomAddonsTextView.setTypeface(face);
                mMenupriceTextView.setTypeface(face);
                mTotalTextView.setTypeface(face);
                mAddButton.setTypeface(face);
                mRemoveButton.setTypeface(face);

            }
        }

        public CartAdapter(Context context) {
            mContext = context;

        }


        @Override
        public CartAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
            // create a new view
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.cart_adapter, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            int menu_id = 0, selected_options = 0, count = 0, id = 0;
            String menu_name = null, menu_price = null, roundtotal;
            StringBuilder option_values = new StringBuilder();
            StringBuilder option_names = new StringBuilder();
            if (cartCursor != null) {
                System.out.println("countssssme" + cartCursor.getCount());
                if (cartCursor.getCount() > 0 && cartCursor.moveToPosition(position)) {
                    id = cartCursor.getInt(cartCursor.getColumnIndex(Menu.ID));
                    menu_id = cartCursor.getInt(cartCursor.getColumnIndex(Menu.MENU_ID));
                    menu_name = cartCursor.getString(cartCursor.getColumnIndex(Menu.MENU_NAME));
                    menu_price = cartCursor.getString(cartCursor.getColumnIndex(Menu.MENU_PRICE));
                    selected_options = cartCursor.getInt(cartCursor.getColumnIndex(Menu.SELECTED_OPTIONS));
                    count = cartCursor.getInt(cartCursor.getColumnIndex(Menu.COUNT));
                }


            }
            if (count == 1) {
                Cursor cursor = db.getCartItems(id);
                String menuOptions = cursorOption(cursor);
                holder.mCustomAddonsTextView.setVisibility(View.VISIBLE);
                holder.mCustomAddonsTextView.setText(menuOptions);


            }
            System.out.println("option_values" + option_values);
            System.out.println("option_names" + option_names);
            price = Float.parseFloat(menu_price.substring(1));
            total = price * selected_options;
            roundtotal = String.format("%.2f", total);
            holder.mTotalTextView.setText(String.valueOf(roundtotal));
            holder.mMenupriceTextView.setText("* " + price);
            holder.mCountTextView.setText(String.valueOf(selected_options));
            holder.mMenuNameTextView.setText(menu_name);
            holder.mAddButton.setOnClickListener(new MenuClickListener(mContext, position));
            holder.mRemoveButton.setOnClickListener(new MenuClickListener(mContext, position));


        }

        @Override
        public int getItemCount() {
            return cartCursor != null ? cartCursor.getCount() : 0;
        }

        public String cursorOption(Cursor cursor) {
            String option_value, option_name;
            if (cursor != null) {
                HashMap<String, ArrayList<String>> options = new HashMap<>();
                if (cursor.getCount() > 0 && cursor.moveToFirst()) {
                    System.out.println("inerrrrrrr");
                    do {
                        option_value = cursor.getString(cursor.getColumnIndex(Menu.VALUE));
                        option_name = cursor.getString(cursor.getColumnIndex(Menu.OPTION_NAME));
                        if (!options.containsKey(option_name)) {
                            ArrayList<String> option_values = new ArrayList<>();
                            System.out.println("oppp+" + option_value);
                            System.out.println("opppnam+" + option_name);
                            option_values.add(option_value);
                            options.put(option_name, option_values);
                        } else if (options.containsKey(option_name)) {
                            System.out.println("oppptttt+" + option_value);
                            System.out.println("opppnamtttttt+" + option_name);
                            options.get(option_name).add(option_value);
                        }

                    } while (cursor.moveToNext());

                }
                Iterator mIterator = options.keySet().iterator();
                String title = null, finalString = "";
                int j = 0;
                while (mIterator.hasNext()) {
                    StringBuilder values = new StringBuilder();
                    j++;
                    String key = (String) mIterator.next();
                    ArrayList<String> value = (ArrayList) options.get(key);
                    System.out.println("Key" + key + "_ value" + value);
                    for (int i = 0; i < value.size(); i++) {
                        System.out.println("valueeee+" + value.get(i));
                        values.append(value.get(i));
                        if (i != value.size() - 1) {
                            values.append(",");
                        }
                    }
                    title = key + " : " + values;
                    finalString += title;
                    finalString += "\n";
                }
                System.out.println("titllll+" + title);
                System.out.println("iiiiiii+" + j);
                System.out.println("finalstr+" + finalString);
                options.clear();
                return finalString;

            }
            return null;
        }

        public class MenuClickListener implements View.OnClickListener {
            int mposition;
            Context mContext;

            public MenuClickListener(Context context, int position) {
                mContext = context;
                mposition = position;

            }

            @Override
            public void onClick(View v) {
                int id = v.getId();
                switch (id) {
                    case R.id.button_cartadd:
                        int menu_id = 0, order_count = 0, sId = 0;
                        ContentValues mCartContentValues = new ContentValues();
                        ArrayList<ContentValues> mCartMenuList = new ArrayList<>();
                        String menu_name = null, menu_desc, menu_price = null;
                        if (v != null) {
                            if (cartCursor.getCount() > 0 && cartCursor.moveToPosition(mposition)) {
                                sId = cartCursor.getInt(cartCursor.getColumnIndex(Menu.ID));
                                menu_id = cartCursor.getInt(cartCursor.getColumnIndex(Menu.MENU_ID));
                                menu_price = cartCursor.getString(cartCursor.getColumnIndex(Menu.MENU_PRICE));
                                menu_name = cartCursor.getString(cartCursor.getColumnIndex(Menu.MENU_NAME));
                                order_count = cartCursor.getInt(cartCursor.getColumnIndex(Menu.SELECTED_OPTIONS));
                            } else {

                            }

                        }
                        mcount = order_count + 1;
                        mCartContentValues.put(Menu.MENU_ID, menu_id);
                        mCartContentValues.put(Menu.MENU_NAME, menu_name);
                        mCartContentValues.put(Menu.MENU_PRICE, menu_price);
                        mCartContentValues.put(Menu.SELECTED_OPTIONS, mcount);
                        System.out.println("priceeeee" + menu_price);
                        mCartMenuList.add(mCartContentValues);
                        db.updateCart(Menu.TABLE_CART, mCartMenuList, Menu.CART_COUNTS, Menu.ID + "=?", new String[]{String.valueOf(sId)});
                        changeCursor();
                        mCartAdapter.notifyDataSetChanged();
                        getCartTotal();


                        break;
                    case R.id.button_cartsubtract:
                        int pmenu_id = 0, pId = 0;
                        order_count = 0;
                        ContentValues pCartContentValues = new ContentValues();
                        ArrayList<ContentValues> pCartMenuList = new ArrayList<>();
                        String pmenu_name = null, pmenu_price = null;
                        int pcart_count;
                        counts = 0;
                        if (cartCursor != null) {
                            if (cartCursor.getCount() > 0 && cartCursor.moveToPosition(mposition)) {
                                pId = cartCursor.getInt(cartCursor.getColumnIndex(Menu.ID));
                                pmenu_id = cartCursor.getInt(cartCursor.getColumnIndex(Menu.MENU_ID));
                                pmenu_price = cartCursor.getString(cartCursor.getColumnIndex(Menu.MENU_PRICE));
                                pmenu_name = cartCursor.getString(cartCursor.getColumnIndex(Menu.MENU_NAME));
                                order_count = cartCursor.getInt(cartCursor.getColumnIndex(Menu.SELECTED_OPTIONS));

                            } else {

                            }

                        }
                        pcart_count = order_count - 1;
                        pCartContentValues.put(Menu.MENU_ID, pmenu_id);
                        pCartContentValues.put(Menu.MENU_NAME, pmenu_name);
                        pCartContentValues.put(Menu.MENU_PRICE, pmenu_price);
                        pCartContentValues.put(Menu.SELECTED_OPTIONS, pcart_count);
                        pCartMenuList.add(pCartContentValues);
                        db.updateCart(Menu.TABLE_CART, pCartMenuList, Menu.CART_COUNTS, Menu.ID + "=?", new String[]{String.valueOf(pId)});
                        changeCursor();
                        getCartTotal();
                        mCartAdapter.notifyDataSetChanged();
                        break;
                }
            }

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                System.out.println("nextssss");
                Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                startActivity(intent);
                setResult(2, intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void getCartTotal() {
        String roundtotal;
        int items = 0;
        int item = 0;
        String menu_price;
        float price = 0, total_price = 0, total = 0;
        db = new DataBaseHandler(this);
        Cursor mCursor = db.query(Menu.TABLE_CART, Menu.CART_COLUMNS, null, null, null);
        if (mCursor != null) {
            System.out.println("countssss" + mCursor.getCount());
            if (mCursor.getCount() > 0 && mCursor.moveToFirst()) {
                do {
                    item = mCursor.getInt(mCursor.getColumnIndex(Menu.SELECTED_OPTIONS));
                    menu_price = mCursor.getString(mCursor.getColumnIndex(Menu.MENU_PRICE));
                    price = Float.parseFloat(menu_price.substring(1));
                    total = price * item;
                    System.out.println("pricesss" + total);
                    items += item;
                    total_price += total;
                    System.out.println("totalllpricesss" + total_price);
                } while (mCursor.moveToNext());

            }


        }
        roundtotal = String.format("%.2f", total_price);
        mSubtotalTextView.setText("$ " + String.valueOf(roundtotal));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cartCursor.close();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                Intent i = new Intent(getApplicationContext(), MenuActivity.class);
                startActivity(i);
                setResult(2, i);
                finish();
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}

