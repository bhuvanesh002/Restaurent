package javadev.example.com.restaurant.fragment;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javadev.example.com.restaurant.R;
import javadev.example.com.restaurant.activity.DataBaseHandler;
import javadev.example.com.restaurant.activity.MenuActivity;
import javadev.example.com.restaurant.activity.ViewCartActivity;
import javadev.example.com.restaurant.model.Menu;
import javadev.example.com.restaurant.ui.SmartAppConstants;


/**
 * Created by Support on 1/2/2017.
 */
public class CategoryFragment extends Fragment {
    ArrayList<ContentValues> mMenuList = new ArrayList<>();
    ArrayList<Menu> mMenuOptionList = new ArrayList<>();
    Cursor menu_cursor, customCursor;
    RecyclerView mMenuRecyclerView;
    MenuAdapter mMenuAdapter;
    LinearLayoutManager mLayoutManager;
    DataBaseHandler db;
    int mCategory_id;
    String mDeliveryStatus;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_category, container, false);
        Typeface face = Typeface.createFromAsset(getActivity().getAssets(), SmartAppConstants.BOLD_FONT);
        mMenuRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_menuone);
        Bundle b = getArguments();
        mCategory_id = b.getInt("category_id");
        System.out.println("onCreateView" + mCategory_id);
        db = new DataBaseHandler(getActivity());
        SharedPreferences prefs = PreferenceManager.
                getDefaultSharedPreferences(getActivity());
        if (!prefs.contains("insertedInDB")) {
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
                // Parse the mMenuList into jsonobject to get original mMenuList in form of json.
                JSONObject jObject = new JSONObject(
                        byteArrayOutputStream.toString());
                JSONObject menulistobject = jObject.getJSONObject("menu_list");
                JSONArray menuArray = menulistobject.getJSONArray("menus");
                JSONArray menuoptionsArray = menulistobject.getJSONArray("menu_options");
                ArrayList<ContentValues> menuoption_list = new ArrayList<>();
                ArrayList<ContentValues> optionvalueList = new ArrayList<>();
                ArrayList<ContentValues> optionsList = new ArrayList<>();
                for (int i = 0; i < menuoptionsArray.length(); i++) {
                    ContentValues contentValues = new ContentValues();
                    JSONObject menuoptionObject = menuoptionsArray.getJSONObject(i);
                    int menu_option_id = menuoptionObject.getInt(Menu.MENU_OPTION_ID);
                    contentValues.put(Menu.MENU_OPTION_ID, menu_option_id);
                    contentValues.put(Menu.OPTION_NAME, menuoptionObject.getString(Menu.OPTION_NAME));
                    contentValues.put(Menu.DISPLAY_TYPE, menuoptionObject.getString(Menu.DISPLAY_TYPE));
                    contentValues.put(Menu.PRIORITY, menuoptionObject.getString(Menu.PRIORITY));
                    contentValues.put(Menu.DEFAULT_VALUE_ID, menuoptionObject.getString(Menu.DEFAULT_VALUE_ID));
                    JSONArray option_values = menuoptionObject.getJSONArray("option_values");
                    for (int k = 0; k < option_values.length(); k++) {
                        ContentValues optioncontentvalues = new ContentValues();
                        JSONObject option_value = option_values.getJSONObject(k);
                        optioncontentvalues.put(Menu.OPTION_VALUE_ID, option_value.getInt(Menu.OPTION_VALUE_ID));
                        optioncontentvalues.put(Menu.VALUE, option_value.getString(Menu.VALUE));
                        optioncontentvalues.put(Menu.PRICE, option_value.getString(Menu.PRICE));
                        optioncontentvalues.put(Menu.MENU_OPTION_ID, menu_option_id);
                        ;
                        optionvalueList.add(optioncontentvalues);
                    }
                    menuoption_list.add(contentValues);
                }
                System.out.println("menuopsizeee" + mMenuOptionList.size());
                for (int i = 0; i < menuArray.length(); i++) {
                    ContentValues menucontentvalues = new ContentValues();
                    JSONObject menuObject = menuArray.getJSONObject(i);
                    int menuId = menuObject.getInt(Menu.MENU_ID);
                    menucontentvalues.put(Menu.MENU_ID, menuId);
                    menucontentvalues.put(Menu.MENU_NAME, menuObject.getString(Menu.MENU_NAME));
                    menucontentvalues.put(Menu.MENU_PRICE, menuObject.getString(Menu.MENU_PRICE));
                    menucontentvalues.put(Menu.MENU_DESCRIPTION, menuObject.getString(Menu.MENU_DESCRIPTION));
                    menucontentvalues.put(Menu.CATEGORY_ID, menuObject.getInt(Menu.CATEGORY_ID));
                    menucontentvalues.put(Menu.CAT_NAME, menuObject.getString(Menu.CAT_NAME));
                    menucontentvalues.put(Menu.MINIMUM_QTY, menuObject.getInt(Menu.MINIMUM_QTY));
                    menucontentvalues.put(Menu.MENU_PRIORITY, menuObject.getInt(Menu.MENU_PRIORITY));
                    menucontentvalues.put(Menu.START_TIME, menuObject.getString(Menu.START_TIME));
                    menucontentvalues.put(Menu.END_TIME, menuObject.getString(Menu.END_TIME));
                    menucontentvalues.put(Menu.IS_MEALTIME, menuObject.getInt(Menu.IS_MEALTIME));
                    menucontentvalues.put(Menu.MEALTIME_STATUS, menuObject.getInt(Menu.MEALTIME_STATUS));
                    menucontentvalues.put(Menu.IS_SPECIAL, menuObject.getInt(Menu.IS_SPECIAL));
                    menucontentvalues.put(Menu.MENU_PRICE, menuObject.getString(Menu.MENU_PRICE));
                    menucontentvalues.put(Menu.MENU_PHOTO, menuObject.getString(Menu.MENU_PHOTO));
                    JSONArray menu_options = menuObject.getJSONArray("menu_options");
                    System.out.println("menulnth" + menu_options.length());
                    if (menu_options.length() > 0) {
                        for (int j = 0; j < menu_options.length(); j++) {
                            ContentValues contentValues = new ContentValues();
                            contentValues.put(Menu.MENU_ID, menuId);
                            contentValues.put(Menu.MENU_OPTION_ID, String.valueOf(menu_options.get(j)));
                            optionsList.add(contentValues);

                        }


                    }
                    mMenuList.add(menucontentvalues);
                }
                System.out.println("menulists" + mMenuList.size());
                System.out.println("menuoption_list" + menuoption_list.size());
                System.out.println("optionvalueList" + optionvalueList.size());
                System.out.println("optionsList" + optionsList.size());
                db.insert(Menu.TABLE_MENUOPTION, menuoption_list);
                db.insert(Menu.TABLE_OPTIONVALUE, optionvalueList);
                db.insert(Menu.TABLE_MENUMENUOPTION, optionsList);
                db.insert(Menu.TABLE_MENU, mMenuList);
            } catch (Exception e) {
                e.printStackTrace();
            }
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("insertedInDB", true);
            editor.commit();
        } else {
            // no need to insert in db
        }
        // insert in DB

        // create key in prefs
        Log.d("Insert: ", "Inserting ..");
        updateMenuCursor();
        mMenuAdapter = new MenuAdapter(getActivity());
        mMenuRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mMenuRecyclerView.setLayoutManager(mLayoutManager);
        mMenuRecyclerView.setAdapter(mMenuAdapter);
        return view;

    }

    public void updateMenuCursor() {
        System.out.println("upodattttttttttttteeeeeeee" + mCategory_id);
        menu_cursor = db.query(Menu.TABLE_MENU, Menu.MENU_COLUMNS, Menu.CATEGORY_ID + "=?", new String[]{String.valueOf(mCategory_id)}, null);

    }

    public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {
        Context mContext;
        Cursor cartoptionCursor;
        int counts = 0;
        int count;
        float price;
        String mCheck, mprice;
        DataBaseHandler db;

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView mMenuNameTextView, mMenuPriceTextView, mMenuDescTextView, mCustomizeMenuTextView, mMenucountTextView;
            public Button mAddButton, mAdditonButton, mRemoveButton;
            CardView mCardView;
            RelativeLayout mRelativeLayout;

            public ViewHolder(View v) {
                super(v);
                Typeface face = Typeface.createFromAsset(mContext.getAssets(), SmartAppConstants.BOLD_FONT);
                mCardView = (CardView) v.findViewById(R.id.card_menu);
                mMenuNameTextView = (TextView) v.findViewById(R.id.textView_menutitle);
                mMenuPriceTextView = (TextView) v.findViewById(R.id.textView_menuprice);
                mMenuDescTextView = (TextView) v.findViewById(R.id.textView_menudesc);
                mCustomizeMenuTextView = (TextView) v.findViewById(R.id.textView_customize);
                mRelativeLayout = (RelativeLayout) v.findViewById(R.id.relative_numpick);
                mMenucountTextView = (TextView) v.findViewById(R.id.text_counts);
                mAddButton = (Button) v.findViewById(R.id.button_addmenu);
                mAdditonButton = (Button) v.findViewById(R.id.button_add);
                mRemoveButton = (Button) v.findViewById(R.id.button_subtract);
                mMenuNameTextView.setTypeface(face);
                mMenuPriceTextView.setTypeface(face);
                mMenuDescTextView.setTypeface(face);
                mCustomizeMenuTextView.setTypeface(face);
                mMenucountTextView.setTypeface(face);
                mAddButton.setTypeface(face);
                mAdditonButton.setTypeface(face);
                mRemoveButton.setTypeface(face);


            }
        }

        public MenuAdapter(Context context) {
            mContext = context;

        }

        @Override
        public MenuAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
            // create a new view
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.menu_adapter, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {

            SharedPreferences prefs_store = getActivity().getSharedPreferences("stores", getActivity().MODE_PRIVATE);
            mDeliveryStatus = prefs_store.getString("delivery_status", null);
            System.out.println("deliii" + mDeliveryStatus);
            if (mDeliveryStatus.equals("close")) {
                holder.mAddButton.setVisibility(View.GONE);
                holder.mRelativeLayout.setVisibility(View.GONE);
            } else {
                holder.mAddButton.setVisibility(View.VISIBLE);
            }
            int menu_id = 0;
            String menu_name = null, menu_desc = null, menu_price = null;
            if (menu_cursor != null) {
                if (menu_cursor.getCount() > 0 && menu_cursor.moveToPosition(position)) {
                    menu_id = menu_cursor.getInt(menu_cursor.getColumnIndex(Menu.MENU_ID));
                    System.out.println("menuissdddsssssssssssssss" + menu_id);
                    menu_name = menu_cursor.getString(menu_cursor.getColumnIndex(Menu.MENU_NAME));
                    menu_desc = menu_cursor.getString(menu_cursor.getColumnIndex(Menu.MENU_DESCRIPTION));
                    menu_price = menu_cursor.getString(menu_cursor.getColumnIndex(Menu.MENU_PRICE));
                }


            }
            holder.mMenuNameTextView.setText(menu_name);
            holder.mMenuDescTextView.setText(menu_desc);
            holder.mMenuPriceTextView.setText(menu_price);
            db = new DataBaseHandler(mContext);
            Cursor menuoptionCursor = db.query(Menu.TABLE_MENUMENUOPTION, Menu.MENU_MENU_OPTION, Menu.MENU_ID + "=?", new String[]{String.valueOf(menu_id)}, null);
            if (menuoptionCursor != null && menuoptionCursor.moveToPosition(position)) {
                if (menuoptionCursor.getCount() != 0 && menuoptionCursor.moveToPosition(position)) {
                    holder.mCustomizeMenuTextView.setVisibility(View.VISIBLE);
                } else {
                    holder.mCustomizeMenuTextView.setVisibility(View.GONE);
                    menuoptionCursor = null;
                    menuoptionCursor.close();
                }

            }

            System.out.println("menuissddd" + menu_id);
            cartoptionCursor = db.queryCount(menu_id);
            int selectedCount = 0;
            if (cartoptionCursor != null) {
                System.out.println("cartoptionCursor" + cartoptionCursor.getCount());
                if (cartoptionCursor.getCount() > 0 && cartoptionCursor.moveToFirst()) {
                    selectedCount = cartoptionCursor.getInt(cartoptionCursor.getColumnIndex(Menu.COUNTS));
                    System.out.println("cartcusorcount" + selectedCount + "_" + cartoptionCursor.getCount());
                }
                cartoptionCursor.close();
            }

            holder.mMenucountTextView.setText(String.valueOf(selectedCount));
            count = selectedCount;
            System.out.println("twoooes" + count);
            System.out.println("delivryyyyyyyyyyyyy" + mDeliveryStatus);

            if (count > 0) {
                System.out.println("innnnnnnnnnnnnnn");
                holder.mAddButton.setVisibility(View.GONE);
                holder.mRelativeLayout.setVisibility(View.VISIBLE);

            } else if (count == 0 && mDeliveryStatus.equals("open")) {
                holder.mAddButton.setVisibility(View.VISIBLE);
                holder.mRelativeLayout.setVisibility(View.GONE);
            }
            mCheck = menu_price.substring(1);
            System.out.println("pric" + mCheck);
            try {
                price = Float.parseFloat(mprice);
            } catch (Exception e) {
                System.out.println("priceee" + price);
            }
            holder.mAddButton.setOnClickListener(new MenuClickListener(mContext, position, menuoptionCursor, count));
            holder.mAdditonButton.setOnClickListener(new MenuClickListener(mContext, position, menuoptionCursor, count));
            holder.mRemoveButton.setOnClickListener(new MenuClickListener(mContext, position, menuoptionCursor, count));

        }

        @Override
        public int getItemCount() {
            return menu_cursor != null ? menu_cursor.getCount() : 0;
        }

        public class MenuClickListener implements View.OnClickListener {
            int mposition;
            Context mContext;
            int order_count;
            boolean checkOption = false;
            Cursor menuoptioncursor;
            boolean mcustomize = false;

            public MenuClickListener(Context context, int position, Cursor menuoptionCursor, int mcount) {
                mContext = context;
                mposition = position;
                menuoptioncursor = menuoptionCursor;
                order_count = mcount;


            }

            @Override
            public void onClick(View v) {
                int id = v.getId();
                switch (id) {
                    case R.id.button_addmenu:
                        int menu_id = 0, count = 1;
                        ContentValues mCartContentValues = new ContentValues();
                        ArrayList<ContentValues> mCartMenuList = new ArrayList<>();
                        String menu_name = null, menu_desc, menu_price = null;
                        ArrayList<Integer> mMenuoptionids = new ArrayList<>();
                        int menu_option_id = 0;
                        if (menuoptioncursor != null) {
                            if (menuoptioncursor.getCount() > 0 && menuoptioncursor.moveToPosition(mposition)) {
                                System.out.println("menuoppp" + menuoptioncursor.getCount());
                                do {
                                    menu_option_id = menuoptioncursor.getInt(menuoptioncursor.getColumnIndex(Menu.MENU_OPTION_ID));
                                    mMenuoptionids.add(menu_option_id);
                                } while (menuoptioncursor.moveToNext());
                                mcustomize = true;
                            } else {
                                mcustomize = false;

                            }

                        }
                        if (mMenuoptionids.size() > 1) {
                            checkOption = true;
                        }
                        if (mcustomize) {
                            counts = 1;
                            db = new DataBaseHandler(mContext);
                            customCursor = db.getMenuOptions(mMenuoptionids);
                            System.out.println("checkop" + checkOption);
                            CustomDialog customDialog = new CustomDialog(mContext, customCursor, mposition, checkOption);
                        } else

                        {
                            counts = 0;
                            System.out.println("menuplusssss");
                            if (menu_cursor != null) {
                                if (menu_cursor.getCount() != 0 && menu_cursor.moveToPosition(mposition)) {
                                    menu_id = menu_cursor.getInt(menu_cursor.getColumnIndex(Menu.MENU_ID));
                                    menu_price = menu_cursor.getString(menu_cursor.getColumnIndex(Menu.MENU_PRICE));
                                    menu_name = menu_cursor.getString(menu_cursor.getColumnIndex(Menu.MENU_NAME));

                                } else {

                                }
                            }
                            System.out.println("menuidddd" + menu_id);
                            System.out.println("menu__inam" + menu_name);
                            System.out.println("menu__prc" + menu_price);
                            mCartContentValues.put(Menu.MENU_ID, menu_id);
                            mCartContentValues.put(Menu.MENU_NAME, menu_name);
                            mCartContentValues.put(Menu.MENU_PRICE, menu_price);
                            mCartContentValues.put(Menu.SELECTED_OPTIONS, count);
                            mCartContentValues.put(Menu.COUNT, counts);
                            mCartMenuList.add(mCartContentValues);
                            System.out.println("menu__idsss" + menu_id);
                            db.insertORupdate(Menu.TABLE_CART, mCartMenuList, Menu.CART_OPTION, Menu.MENU_ID + "=?", new String[]{String.valueOf(menu_id)});
                            System.out.println("menindssdfr");
                            mMenuAdapter.notifyDataSetChanged();
                            ((MenuActivity) getActivity()).getMenu();

                        }
                        break;
                    case R.id.button_subtract:
                        int pmenu_id = 0;
                        ContentValues pCartContentValues = new ContentValues();
                        ArrayList<ContentValues> pCartMenuList = new ArrayList<>();
                        String pmenu_name = null, pmenu_price = null;
                        mMenuoptionids = new ArrayList<>();
                        if (menu_cursor != null) {
                            if (menu_cursor.getCount() != 0 && menu_cursor.moveToPosition(mposition)) {
                                pmenu_id = menu_cursor.getInt(menu_cursor.getColumnIndex(Menu.MENU_ID));
                                pmenu_price = menu_cursor.getString(menu_cursor.getColumnIndex(Menu.MENU_PRICE));
                                pmenu_name = menu_cursor.getString(menu_cursor.getColumnIndex(Menu.MENU_NAME));

                            } else {

                            }

                        }

                        if (menuoptioncursor != null) {
                            if (menuoptioncursor.getCount() > 0 && menuoptioncursor.moveToPosition(mposition)) {
                                menu_option_id = menuoptioncursor.getInt(menuoptioncursor.getColumnIndex("menu_option_id"));
                                mMenuoptionids.add(menu_option_id);
                                mcustomize = true;
                            } else {
                                mcustomize = false;
                            }

                        }
                        if (mMenuoptionids.size() > 1) {
                            checkOption = true;
                        }
                        if (mcustomize) {
                            counts = 1;
                            cartoptionCursor = db.queryCount(pmenu_id);
                            int selectedCount = 0;
                            if (cartoptionCursor != null) {
                                if (cartoptionCursor.getCount() > 0 && cartoptionCursor.moveToPosition(mposition)) {
                                    selectedCount = cartoptionCursor.getInt(cartoptionCursor.getColumnIndex(Menu.COUNTS));
                                    System.out.println("cartcusorcount" + selectedCount + "_" + cartoptionCursor.getCount());
                                }
                                cartoptionCursor.close();
                            }
                            if (selectedCount == 1) {
                                int cartCount = 0;
                                pCartContentValues.put(Menu.SELECTED_OPTIONS, cartCount);
                                pCartMenuList.add(pCartContentValues);
                                System.out.println("countttttttttttt" + selectedCount);
                                db.insertORupdate(Menu.TABLE_CART, pCartMenuList, Menu.CART_OPTION, Menu.MENU_ID + "=?", new String[]{String.valueOf(pmenu_id)});
                                mMenuAdapter.notifyDataSetChanged();
                                ((MenuActivity) getActivity()).getMenu();

                            } else if (selectedCount > 1) {
                                showAlert();

                            }

                        } else

                        {
                            int pcart_count;
                            pcart_count = order_count - 1;
                            counts = 0;
                            System.out.println("menuidddd" + pmenu_id);
                            pCartContentValues.put(Menu.MENU_ID, pmenu_id);
                            pCartContentValues.put(Menu.MENU_NAME, pmenu_name);
                            pCartContentValues.put(Menu.MENU_PRICE, pmenu_price);
                            pCartContentValues.put(Menu.SELECTED_OPTIONS, pcart_count);
                            pCartMenuList.add(pCartContentValues);
                            db.insertORupdate(Menu.TABLE_CART, pCartMenuList, Menu.CART_OPTION, Menu.MENU_ID + "=?", new String[]{String.valueOf(pmenu_id)});
                            mMenuAdapter.notifyDataSetChanged();
                            if (pcart_count == 1) {
                                SharedPreferences.Editor editor = getActivity().getSharedPreferences("count_update", getActivity().MODE_PRIVATE).edit();
                                editor.putInt("count", 2);
                                editor.commit();
                            }
                            ((MenuActivity) getActivity()).getMenu();
                        }
                        break;
                    case R.id.button_add:
                        int smenu_id = 0;
                        ContentValues sCartContentValues = new ContentValues();
                        ArrayList<ContentValues> sCartMenuList = new ArrayList<>();
                        String smenu_name = null, smenu_price = null;
                        mMenuoptionids = new ArrayList<>();
                        if (menuoptioncursor != null) {
                            if (menuoptioncursor.getCount() > 0 && menuoptioncursor.moveToPosition(mposition)) {
                                System.out.println("menuoppp" + menuoptioncursor.getCount());
                                do {
                                    menu_option_id = menuoptioncursor.getInt(menuoptioncursor.getColumnIndex(Menu.MENU_OPTION_ID));
                                    mMenuoptionids.add(menu_option_id);
                                } while (menuoptioncursor.moveToNext());
                                mcustomize = true;
                            } else {
                                mcustomize = false;
                            }

                        }
                        if (mMenuoptionids.size() > 1) {
                            checkOption = true;
                        }
                        if (mcustomize) {
                            counts = 1;
                            db = new DataBaseHandler(mContext);
                            customCursor = db.getMenuOptions(mMenuoptionids);
                            CustomDialog customDialog = new CustomDialog(mContext, customCursor, mposition, checkOption);
                        } else

                        {
                            int scart_count;
                            scart_count = order_count + 1;
                            counts = 0;
                            if (menu_cursor != null) {
                                if (menu_cursor.getCount() != 0 && menu_cursor.moveToPosition(mposition)) {
                                    smenu_id = menu_cursor.getInt(menu_cursor.getColumnIndex(Menu.MENU_ID));
                                    smenu_price = menu_cursor.getString(menu_cursor.getColumnIndex(Menu.MENU_PRICE));
                                    smenu_name = menu_cursor.getString(menu_cursor.getColumnIndex(Menu.MENU_NAME));

                                } else {

                                }

                            }
                            System.out.println("menuidddd" + smenu_id);
                            System.out.println("menu__idsss" + smenu_id);
                            System.out.println("menu__inam" + smenu_name);
                            System.out.println("menu__prc" + smenu_price);
                            sCartContentValues.put(Menu.MENU_ID, smenu_id);
                            sCartContentValues.put(Menu.MENU_NAME, smenu_name);
                            sCartContentValues.put(Menu.MENU_PRICE, smenu_price);
                            sCartContentValues.put(Menu.SELECTED_OPTIONS, scart_count);
                            sCartMenuList.add(sCartContentValues);
                            db.insertORupdate(Menu.TABLE_CART, sCartMenuList, Menu.CART_OPTION, Menu.MENU_ID + "=?", new String[]{String.valueOf(smenu_id)});
                            mMenuAdapter.notifyDataSetChanged();
                            ((MenuActivity) getActivity()).getMenu();
                            break;

                        }

                }
            }
        }
    }

    public class CustomDialog extends AlertDialog.Builder {
        TextView mPopNameTextView, mPopDescTextView, mTotalTextView, mPopPriceTextView, mTotalPriceTextView;
        RecyclerView mMenuoptionRecyclerView;
        Button mCancelButton, mAddcartButton;
        HashMap<Integer, Integer> option_ids = new HashMap<>();
        HashMap<Integer, Integer> secondoption_ids = new HashMap<>();
        AlertDialog b;
        String roundedPrice;
        boolean optiontype = false;
        boolean numOptions;
        Cursor cartoptionCursor;
        Cursor mcustomCursor;
        int mposition, mflag, smenu_id;
        float mprice = 0, mtotal = 0;
        MenuOptionAdapter menuOptionAdapter;
        RecyclerView.LayoutManager nLayoutManager;
        String menu_name, menu_desc, menu_price;

        public CustomDialog(Context mContext, Cursor customcursor, int position, boolean option_type) {
            super(mContext);
            mcustomCursor = customcursor;
            mposition = position;
            optiontype = option_type;
            showChangeLangDialog();
        }

        public void showChangeLangDialog() {
            final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
            LayoutInflater inflater = LayoutInflater.from(getContext());
            final View dialogView = inflater.inflate(R.layout.popup_dialog, null);
            dialogBuilder.setView(dialogView);
            mPopNameTextView = (TextView) dialogView.findViewById(R.id.textView_custmenuname);
            mPopDescTextView = (TextView) dialogView.findViewById(R.id.textView_custmenudesc);
            mPopPriceTextView = (TextView) dialogView.findViewById(R.id.textView_custmenuprice);
            mTotalTextView = (TextView) dialogView.findViewById(R.id.textView_totalpop);
            mTotalPriceTextView = (TextView) dialogView.findViewById(R.id.textView_custtotprice);
            mMenuoptionRecyclerView = (RecyclerView) dialogView.findViewById(R.id.recycler_addons);
            mCancelButton = (Button) dialogView.findViewById(R.id.button_cancelcust);
            mAddcartButton = (Button) dialogView.findViewById(R.id.button_addcartcust);
            Typeface face = Typeface.createFromAsset(getContext().getAssets(), SmartAppConstants.BOLD_FONT);
            mPopNameTextView.setTypeface(face);
            mPopDescTextView.setTypeface(face);
            mPopPriceTextView.setTypeface(face);
            mTotalTextView.setTypeface(face);
            mTotalPriceTextView.setTypeface(face);
            mCancelButton.setTypeface(face);
            mAddcartButton.setTypeface(face);
            menuOptionAdapter = new MenuOptionAdapter(getContext(), mcustomCursor, optiontype);
            mMenuoptionRecyclerView.setHasFixedSize(true);
            nLayoutManager = new LinearLayoutManager(getContext());
            mMenuoptionRecyclerView.setLayoutManager(nLayoutManager);
            mMenuoptionRecyclerView.setAdapter(menuOptionAdapter);
            if (menu_cursor != null) {
                if (menu_cursor.getCount() != 0 && menu_cursor.moveToPosition(mposition)) {
                    smenu_id = menu_cursor.getInt(menu_cursor.getColumnIndex(Menu.MENU_ID));
                    menu_price = menu_cursor.getString(menu_cursor.getColumnIndex(Menu.MENU_PRICE));
                    menu_name = menu_cursor.getString(menu_cursor.getColumnIndex(Menu.MENU_NAME));
                    menu_desc = menu_cursor.getString(menu_cursor.getColumnIndex(Menu.MENU_DESCRIPTION));

                } else {

                }

            }
            mprice = Float.parseFloat(menu_price.substring(1));
            mPopNameTextView.setText(menu_name);
            mPopDescTextView.setText(menu_desc);
            mPopPriceTextView.setText("$ " + menu_price);
            System.out.println("saba112" + mtotal);
            mTotalPriceTextView.setText(String.valueOf(mprice));
            b = dialogBuilder.create();
            b.show();
            mCancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    dismiss();

                }
            });
            mAddcartButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ContentValues optionContentValues = new ContentValues();
                    ArrayList<ContentValues> optionsList = new ArrayList<ContentValues>();
                    ArrayList<ContentValues> menu_optionsList = new ArrayList<ContentValues>();
                    ArrayList<ContentValues> secondmenu_optionsList = new ArrayList<ContentValues>();
                    if (option_ids.size() == 0) {
                        ContentValues pCartContentValues=new ContentValues();
                        ArrayList<ContentValues>pCartMenuList=new ArrayList<ContentValues>();
                        db = new DataBaseHandler(getContext());
                        if (menu_cursor != null) {

                            if (menu_cursor.getCount() != 0 && menu_cursor.moveToPosition(mposition)) {
                                smenu_id = menu_cursor.getInt(menu_cursor.getColumnIndex(Menu.MENU_ID));
                                menu_price = menu_cursor.getString(menu_cursor.getColumnIndex(Menu.MENU_PRICE));
                                menu_name = menu_cursor.getString(menu_cursor.getColumnIndex(Menu.MENU_NAME));
                                menu_desc = menu_cursor.getString(menu_cursor.getColumnIndex(Menu.MENU_DESCRIPTION));
                                cartoptionCursor = db.queryCount(smenu_id);
                                int selectedCount = 0;
                                if (cartoptionCursor != null) {
                                    System.out.println("cartoptionCursor" + cartoptionCursor.getCount());
                                    if (cartoptionCursor.getCount() > 0 && cartoptionCursor.moveToFirst()) {
                                        selectedCount = cartoptionCursor.getInt(cartoptionCursor.getColumnIndex(Menu.COUNTS));
                                        System.out.println("cartcusorcount" + selectedCount + "_" + cartoptionCursor.getCount());
                                    }
                                    cartoptionCursor.close();
                                }
                                int pcart_count=selectedCount+1;
                                pCartContentValues.put(Menu.MENU_ID, smenu_id);
                                pCartContentValues.put(Menu.MENU_NAME, menu_name);
                                pCartContentValues.put(Menu.MENU_PRICE, menu_price);
                                pCartContentValues.put(Menu.SELECTED_OPTIONS, pcart_count);
                                pCartMenuList.add(pCartContentValues);
                            } else {

                            }

                        }
                        db.insertORupdate(Menu.TABLE_CART, pCartMenuList, Menu.CART_OPTION, Menu.MENU_ID + "=?", new String[]{String.valueOf(smenu_id)});
                        updateMenuCursor();
                        mMenuAdapter.notifyDataSetChanged();
                        ((MenuActivity) getActivity()).getMenu();
                        dismiss();


                    } else if (!numOptions){
                        roundedPrice = "$" + mTotalPriceTextView.getText().toString();
                        db = new DataBaseHandler(getContext());
                        if (menu_cursor != null) {
                            if (menu_cursor.getCount() != 0 && menu_cursor.moveToPosition(mposition)) {
                                smenu_id = menu_cursor.getInt(menu_cursor.getColumnIndex(Menu.MENU_ID));
                                menu_price = menu_cursor.getString(menu_cursor.getColumnIndex(Menu.MENU_PRICE));
                                menu_name = menu_cursor.getString(menu_cursor.getColumnIndex(Menu.MENU_NAME));
                                menu_desc = menu_cursor.getString(menu_cursor.getColumnIndex(Menu.MENU_DESCRIPTION));

                            } else {

                            }

                        }
                        optionContentValues.put(Menu.MENU_ID, smenu_id);
                        optionContentValues.put(Menu.MENU_PRICE, roundedPrice);
                        optionContentValues.put(Menu.MENU_NAME, menu_name);
                        optionsList.add(optionContentValues);
                        Iterator mIterator = option_ids.keySet().iterator();
                        while (mIterator.hasNext()) {
                            ContentValues menu_optionContentValues = new ContentValues();
                            Integer key = (Integer) mIterator.next();
                            Integer value = (Integer) option_ids.get(key);
                            System.out.println("Key" + key + "_ value" + value);
                            menu_optionContentValues.put(Menu.OPTION_VALUE_ID, key);
                            menu_optionContentValues.put(Menu.MENU_OPTION_ID, value);
                            menu_optionsList.add(menu_optionContentValues);
                        }
                        for (ContentValues values : menu_optionsList) {
                            System.out.println("menussizeee" + values.toString());
                        }
                        db.addcustomize(Menu.TABLE_CART, optionsList, menu_optionsList, Menu.MENU_OPTION_ITEMS, Menu.MENU_OPTION_ITEMS_TWO, Menu.MENU_ID + "=?", new String[]{String.valueOf(smenu_id)});
                        updateMenuCursor();
                        mMenuAdapter.notifyDataSetChanged();
                        ((MenuActivity) getActivity()).getMenu();
                        dismiss();
                    }else if (numOptions){
                        roundedPrice = "$" + mTotalPriceTextView.getText().toString();
                        db = new DataBaseHandler(getContext());
                        if (menu_cursor != null) {
                            if (menu_cursor.getCount() != 0 && menu_cursor.moveToPosition(mposition)) {
                                smenu_id = menu_cursor.getInt(menu_cursor.getColumnIndex(Menu.MENU_ID));
                                menu_price = menu_cursor.getString(menu_cursor.getColumnIndex(Menu.MENU_PRICE));
                                menu_name = menu_cursor.getString(menu_cursor.getColumnIndex(Menu.MENU_NAME));
                                menu_desc = menu_cursor.getString(menu_cursor.getColumnIndex(Menu.MENU_DESCRIPTION));

                            } else {

                            }

                        }
                        optionContentValues.put(Menu.MENU_ID, smenu_id);
                        optionContentValues.put(Menu.MENU_PRICE, roundedPrice);
                        optionContentValues.put(Menu.MENU_NAME, menu_name);
                        optionsList.add(optionContentValues);
                        Iterator mIterator = option_ids.keySet().iterator();
                        while (mIterator.hasNext()) {
                            ContentValues menu_optionContentValues = new ContentValues();
                            Integer key = (Integer) mIterator.next();
                            Integer value = (Integer) option_ids.get(key);
                            System.out.println("Key" + key + "_ value" + value);
                            menu_optionContentValues.put(Menu.OPTION_VALUE_ID, key);
                            menu_optionContentValues.put(Menu.MENU_OPTION_ID, value);
                            menu_optionsList.add(menu_optionContentValues);
                        }
                        for (ContentValues values : menu_optionsList) {
                            System.out.println("menussizeee" + values.toString());
                        }
                        Iterator sIterator = secondoption_ids.keySet().iterator();
                        while (sIterator.hasNext()) {
                            ContentValues menu_secondoptionContentValues = new ContentValues();
                            Integer key = (Integer) sIterator.next();
                            Integer value = (Integer) secondoption_ids.get(key);
                            System.out.println("Key" + key + "_ value" + value);
                            menu_secondoptionContentValues.put(Menu.OPTION_VALUE_ID, key);
                            menu_secondoptionContentValues.put(Menu.MENU_OPTION_ID, value);
                            secondmenu_optionsList.add(menu_secondoptionContentValues);
                        }
                        for (ContentValues values : secondmenu_optionsList) {
                            System.out.println("menussizeee" + values.toString());
                        }
                        db.adddoublecustomiztation(Menu.TABLE_CART, optionsList, menu_optionsList, secondmenu_optionsList, Menu.MENU_OPTION_ITEMS, Menu.MENU_OPTION_ITEMS_TWO, Menu.MENU_ID + "=?", new String[]{String.valueOf(smenu_id)});
                        updateMenuCursor();
                        mMenuAdapter.notifyDataSetChanged();
                        ((MenuActivity) getActivity()).getMenu();
                        dismiss();
                    }
                }
            });
        }


        public void dismiss() {
            b.dismiss();
            mcustomCursor.close();
        }

        public void addOptions(HashMap<Integer, Integer> option_valueids, float total, int flags,boolean options) {
            option_ids = option_valueids;
            mtotal = total;
            mflag = flags;
            numOptions=options;
            String roundtotal = String.format("%.2f", mtotal);
            float totalprice = Float.parseFloat(roundtotal);
            if (mflag == 1) {
                String mtot = menu_price.substring(1);
                float mtotal = Float.parseFloat(mtot);
                mtotal += totalprice;
                String mroundtotal = String.format("%.2f", mtotal);
                mTotalPriceTextView.setText(String.valueOf(mroundtotal));
            } else if (mflag == 2) {
                String mtot = mTotalPriceTextView.getText().toString();
                float mtotal = Float.parseFloat(mtot);
                mtotal += totalprice;
                String mroundtotal = String.format("%.2f", mtotal);
                mTotalPriceTextView.setText(String.valueOf(mroundtotal));
            } else {
                String tot = mTotalPriceTextView.getText().toString();
                System.out.println("saba111" + tot);
                float stotal = Float.parseFloat(tot);
                System.out.println("saba11" + totalprice);
                stotal -= totalprice;
                String mroundtotal = String.format("%.2f", stotal);
                mTotalPriceTextView.setText(String.valueOf(mroundtotal));
            }
            System.out.println("saba113" + mprice);


        }
        public void addDoubleOptions(HashMap<Integer, Integer> option_valueids,HashMap<Integer, Integer> secondoption_valueids, float total, int flags,boolean options) {
            option_ids = option_valueids;
            secondoption_ids = secondoption_valueids;
            mtotal = total;
            numOptions=options;
            mflag = flags;
            String roundtotal = String.format("%.2f", mtotal);
            float totalprice = Float.parseFloat(roundtotal);
            if (mflag == 1) {
                String mtot = menu_price.substring(1);
                float mtotal = Float.parseFloat(mtot);
                mtotal += totalprice;
                String mroundtotal = String.format("%.2f", mtotal);
                mTotalPriceTextView.setText(String.valueOf(mroundtotal));
            } else if (mflag == 2) {
                String mtot = mTotalPriceTextView.getText().toString();
                float mtotal = Float.parseFloat(mtot);
                mtotal += totalprice;
                String mroundtotal = String.format("%.2f", mtotal);
                mTotalPriceTextView.setText(String.valueOf(mroundtotal));
            } else {
                String tot = mTotalPriceTextView.getText().toString();
                System.out.println("saba111" + tot);
                float stotal = Float.parseFloat(tot);
                System.out.println("saba11" + totalprice);
                stotal -= totalprice;
                String mroundtotal = String.format("%.2f", stotal);
                mTotalPriceTextView.setText(String.valueOf(mroundtotal));
            }
            System.out.println("saba113" + mprice);


        }


        public class MenuOptionAdapter extends RecyclerView.Adapter<MenuOptionAdapter.ViewHolder> {
            Context nContext;
            boolean mOptionType = false;
            int flags = 0;
            Cursor mcustomCursor;
            int lastCheckedPos = 0;
            String option_value = null;
            String option_price = null;
            String display_type = null;
            String option_name = null;
            final ArrayList<String> add_ons = new ArrayList<>();
            final HashMap<Integer, Integer> option_value_ids = new HashMap<>();
            final HashMap<Integer, Integer> secondoption_value_ids = new HashMap<>();
            float totalprice = 0;
            private RadioButton lastChecked = null;
            int clickedPos, option_valueid, menu_optionid;
            String checker;
            float value, values;
            String price, optionname, optionvalue, roundtotal;
            int menu_option_id = 0, option_value_id = 0;

            public class ViewHolder extends RecyclerView.ViewHolder {
                boolean check = false;
                CardView mCardView;
                RadioButton mOptionRadioButton = null;
                boolean status = false;
                CheckBox mOptionCheckBox;
                TextView mOptionTextView, mPriceTextView, mTotalTextView, mMenuTypeTextView;
                View mview;

                public ViewHolder(View v) {
                    super(v);
                    Typeface face = Typeface.createFromAsset(nContext.getAssets(), SmartAppConstants.BOLD_FONT);
                    mCardView = (CardView) v.findViewById(R.id.card_popup);
                    mOptionRadioButton = (RadioButton) v.findViewById(R.id.radioButton_pop);
                    mOptionCheckBox = (CheckBox) v.findViewById(R.id.checkBox_pop);
                    mOptionTextView = (TextView) v.findViewById(R.id.textView_radiotext);
                    mPriceTextView = (TextView) v.findViewById(R.id.textView_popprice);
                    mMenuTypeTextView = (TextView) v.findViewById(R.id.textView_menutit);
                    mview = (View) v.findViewById(R.id.view_code);
                    mOptionTextView.setTypeface(face);
                    mPriceTextView.setTypeface(face);
                    mMenuTypeTextView.setTypeface(face);


                }
            }


            public MenuOptionAdapter(Context context, Cursor customcursor, boolean optionType) {
                nContext = context;
                mcustomCursor = customcursor;
                mOptionType = optionType;
                System.out.println("optiontyp" + mOptionType);


            }

            @Override
            public MenuOptionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                   int viewType) {
                // create a new view
                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.popup_adapter, parent, false);

                return new ViewHolder(v);
            }

            @Override
            public void onBindViewHolder(final ViewHolder holder, final int position) {
                if (mcustomCursor != null) {
                    if (mcustomCursor.getCount() > 0 && mcustomCursor.moveToPosition(position)) {
                        menu_option_id = mcustomCursor.getInt(mcustomCursor.getColumnIndex(Menu.MENU_OPTION_ID));
                        option_value_id = mcustomCursor.getInt(mcustomCursor.getColumnIndex(Menu.OPTION_VALUE_ID));
                        option_value = mcustomCursor.getString(mcustomCursor.getColumnIndex(Menu.VALUE));
                        option_price = mcustomCursor.getString(mcustomCursor.getColumnIndex(Menu.PRICE));
                        display_type = mcustomCursor.getString(mcustomCursor.getColumnIndex(Menu.DISPLAY_TYPE));
                        option_name = mcustomCursor.getString(mcustomCursor.getColumnIndex(Menu.OPTION_NAME));
                        System.out.println("posirad" + display_type);
                    }


                }

                if (!mOptionType && display_type.equals("radio")) {
                    final boolean option=false;
                    totalprice = 0;
                    if (position == 0) {
                        System.out.println("posira" + position);
                        holder.mMenuTypeTextView.setVisibility(View.VISIBLE);
                        checker = option_name;
                        holder.mMenuTypeTextView.setText(option_name);
                    }
                    holder.mOptionCheckBox.setVisibility(View.GONE);
                    holder.mOptionRadioButton.setVisibility(View.VISIBLE);
                    holder.mOptionTextView.setVisibility(View.VISIBLE);
                    holder.mOptionTextView.setText(option_value);
                    holder.mPriceTextView.setText(option_price);
                    holder.mOptionRadioButton.setChecked(Menu.isSelected());
                    holder.mOptionRadioButton.setTag(new Integer(position));
                    if (position == 0 && Menu.isSelected() && holder.mOptionRadioButton.isChecked()) {
                        lastChecked = holder.mOptionRadioButton;
                        lastCheckedPos = 0;
                    }
                    holder.mOptionRadioButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mcustomCursor.getCount() > 0 && mcustomCursor.moveToPosition(position)) {
                                menu_option_id = mcustomCursor.getInt(mcustomCursor.getColumnIndex(Menu.MENU_OPTION_ID));
                                option_value_id = mcustomCursor.getInt(mcustomCursor.getColumnIndex(Menu.OPTION_VALUE_ID));
                                option_value = mcustomCursor.getString(mcustomCursor.getColumnIndex(Menu.VALUE));
                                option_price = mcustomCursor.getString(mcustomCursor.getColumnIndex(Menu.PRICE));
                                display_type = mcustomCursor.getString(mcustomCursor.getColumnIndex(Menu.DISPLAY_TYPE));
                                option_name = mcustomCursor.getString(mcustomCursor.getColumnIndex(Menu.OPTION_NAME));
                            }
                            holder.status = true;
                            int flag = 1;
                            RadioButton rb = (RadioButton) v;
                            System.out.println("ra" + rb.getTag());
                            clickedPos = ((Integer) rb.getTag()).intValue();
                            if (rb.isChecked()) {
                                option_value_ids.clear();
                                if (lastChecked != null) {
                                    lastChecked.setChecked(false);

                                }
                                lastChecked = rb;
                                lastCheckedPos = clickedPos;
                                mcustomCursor.moveToPosition(clickedPos);
                                price = mcustomCursor.getString(mcustomCursor.getColumnIndex(Menu.PRICE)).substring(1);
                                value = Float.parseFloat(price);
                                optionname = mcustomCursor.getString(mcustomCursor.getColumnIndex(Menu.OPTION_NAME));
                                optionvalue = mcustomCursor.getString(mcustomCursor.getColumnIndex(Menu.VALUE));
                                menu_optionid = mcustomCursor.getInt(mcustomCursor.getColumnIndex(Menu.MENU_OPTION_ID));
                                option_valueid = mcustomCursor.getInt(mcustomCursor.getColumnIndex(Menu.OPTION_VALUE_ID));
                                option_value_ids.put(option_valueid, menu_optionid);
                                addOptions(option_value_ids, value, flag,option);

                            } else
                                lastChecked = null;

                        }

                    });
                } else if (!mOptionType && display_type.equals("checkbox")) {
                    final boolean option=false;
                    if (position == 0 || !option_name.equals(checker)) {
                        holder.mMenuTypeTextView.setVisibility(View.VISIBLE);
                        checker = option_name;
                        holder.mMenuTypeTextView.setText(option_name);
                    }
                    System.out.println("two");
                    holder.mOptionRadioButton.setVisibility(View.GONE);
                    holder.mOptionCheckBox.setVisibility(View.VISIBLE);
                    holder.mOptionTextView.setVisibility(View.VISIBLE);
                    holder.mOptionTextView.setText(option_value);
                    holder.mPriceTextView.setText(option_price);
                    holder.mOptionCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (mcustomCursor.getCount() > 0 && mcustomCursor.moveToPosition(position)) {
                                menu_option_id = mcustomCursor.getInt(mcustomCursor.getColumnIndex(Menu.MENU_OPTION_ID));
                                option_value_id = mcustomCursor.getInt(mcustomCursor.getColumnIndex(Menu.OPTION_VALUE_ID));
                                option_value = mcustomCursor.getString(mcustomCursor.getColumnIndex(Menu.VALUE));
                                option_price = mcustomCursor.getString(mcustomCursor.getColumnIndex(Menu.PRICE));
                                display_type = mcustomCursor.getString(mcustomCursor.getColumnIndex(Menu.DISPLAY_TYPE));
                                option_name = mcustomCursor.getString(mcustomCursor.getColumnIndex(Menu.OPTION_NAME));
                            }
                            if (isChecked) {
                                int flag = 2;
                                flags += flag;
                                price = option_price.substring(1);
                                value = Float.parseFloat(price);
                                optionname = option_name;
                                optionvalue = option_value;
                                option_valueid = option_value_id;
                                menu_optionid = menu_option_id;
                                option_value_ids.put(option_valueid, menu_optionid);
                                addOptions(option_value_ids, value, flag,option);
                            } else {
                                int flag = 3;
                                flags -= flag;
                                price = option_price.substring(1);
                                value = Float.parseFloat(price);
                                totalprice -= value;
                                roundtotal = String.format("%.2f", totalprice);
                                optionname = option_name;
                                optionvalue = option_value;
                                option_valueid = option_value_id;
                                menu_optionid = menu_option_id;
                                Iterator mIterator = option_value_ids.keySet().iterator();
                                while (mIterator.hasNext()) {
                                    Integer key = (Integer) mIterator.next();
                                    Integer value = (Integer) option_value_ids.get(key);
                                    System.out.println("Key" + key + "_ value" + value);
                                    if (key == option_valueid && value == menu_optionid) {
                                        mIterator.remove();
                                    }
                                }
                                addOptions(option_value_ids, value, flag,option);
                            }

                        }
                    });
                } else if (mOptionType) {
                    final boolean option=true;
                    totalprice = 0;
                    if (display_type.equals("radio")) {
                        if (position == 0) {
                            holder.mMenuTypeTextView.setVisibility(View.VISIBLE);
                            checker = option_name;
                            holder.mMenuTypeTextView.setText(option_name);
                        }
                        holder.mOptionCheckBox.setVisibility(View.GONE);
                        holder.mOptionRadioButton.setVisibility(View.VISIBLE);
                        holder.mOptionTextView.setVisibility(View.VISIBLE);
                        holder.mOptionTextView.setText(option_value);
                        holder.mPriceTextView.setText(option_price);
                        holder.mOptionRadioButton.setChecked(Menu.isSelected());
                        holder.mOptionRadioButton.setTag(new Integer(position));
                        if (position == 0 && Menu.isSelected() && holder.mOptionRadioButton.isChecked()) {
                            lastChecked = holder.mOptionRadioButton;
                            lastCheckedPos = 0;
                        }
                        holder.mOptionRadioButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (mcustomCursor.getCount() > 0 && mcustomCursor.moveToPosition(position)) {
                                    menu_option_id = mcustomCursor.getInt(mcustomCursor.getColumnIndex(Menu.MENU_OPTION_ID));
                                    option_value_id = mcustomCursor.getInt(mcustomCursor.getColumnIndex(Menu.OPTION_VALUE_ID));
                                    option_value = mcustomCursor.getString(mcustomCursor.getColumnIndex(Menu.VALUE));
                                    option_price = mcustomCursor.getString(mcustomCursor.getColumnIndex(Menu.PRICE));
                                    display_type = mcustomCursor.getString(mcustomCursor.getColumnIndex(Menu.DISPLAY_TYPE));
                                    option_name = mcustomCursor.getString(mcustomCursor.getColumnIndex(Menu.OPTION_NAME));
                                }
                                holder.status = true;
                                int flag = 1;
                                RadioButton rb = (RadioButton) v;
                                System.out.println("ra" + rb.getTag());
                                clickedPos = ((Integer) rb.getTag()).intValue();
                                if (rb.isChecked()) {
                                    option_value_ids.clear();
                                    if (lastChecked != null) {
                                        lastChecked.setChecked(false);
                                    }
                                    lastChecked = rb;
                                    lastCheckedPos = clickedPos;
                                    mcustomCursor.moveToPosition(clickedPos);
                                    price = mcustomCursor.getString(mcustomCursor.getColumnIndex(Menu.PRICE)).substring(1);
                                    value = Float.parseFloat(price);
                                    totalprice += value;
                                    optionname = mcustomCursor.getString(mcustomCursor.getColumnIndex(Menu.OPTION_NAME));
                                    optionvalue = mcustomCursor.getString(mcustomCursor.getColumnIndex(Menu.VALUE));
                                    menu_optionid = mcustomCursor.getInt(mcustomCursor.getColumnIndex(Menu.MENU_OPTION_ID));
                                    option_valueid = mcustomCursor.getInt(mcustomCursor.getColumnIndex(Menu.OPTION_VALUE_ID));
                                    option_value_ids.put(option_valueid, menu_optionid);
                                    addOptions(option_value_ids, value, flag,option);
                                } else
                                    lastChecked = null;
                            }

                        });
                    } else if (display_type.equals("checkbox")) {
                        if (position == 0 || !option_name.equals(checker) && position != 7) {
                            holder.mview.setVisibility(View.VISIBLE);
                            holder.mMenuTypeTextView.setVisibility(View.VISIBLE);
                            checker = option_name;
                            holder.mMenuTypeTextView.setText(option_name);
                        }
                        System.out.println("two");
                        holder.mOptionRadioButton.setVisibility(View.GONE);
                        holder.mOptionCheckBox.setVisibility(View.VISIBLE);
                        holder.mOptionTextView.setVisibility(View.VISIBLE);
                        holder.mOptionTextView.setText(option_value);
                        holder.mPriceTextView.setText(option_price);
                        holder.mOptionCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if (mcustomCursor.getCount() > 0 && mcustomCursor.moveToPosition(position)) {
                                    menu_option_id = mcustomCursor.getInt(mcustomCursor.getColumnIndex(Menu.MENU_OPTION_ID));
                                    option_value_id = mcustomCursor.getInt(mcustomCursor.getColumnIndex(Menu.OPTION_VALUE_ID));
                                    option_value = mcustomCursor.getString(mcustomCursor.getColumnIndex(Menu.VALUE));
                                    option_price = mcustomCursor.getString(mcustomCursor.getColumnIndex(Menu.PRICE));
                                    display_type = mcustomCursor.getString(mcustomCursor.getColumnIndex(Menu.DISPLAY_TYPE));
                                    option_name = mcustomCursor.getString(mcustomCursor.getColumnIndex(Menu.OPTION_NAME));
                                }
                                if (isChecked) {
                                    int flag = 2;
                                    flags += flag;
                                    price = option_price.substring(1);
                                    value = Float.parseFloat(price);
                                    totalprice += value;
                                    totalprice += values;
                                    roundtotal = String.format("%.2f", totalprice);
                                    optionname = option_name;
                                    optionvalue = option_value;
                                    option_valueid = option_value_id;
                                    menu_optionid = menu_option_id;
                                    secondoption_value_ids.put(option_valueid, menu_optionid);
                                    addDoubleOptions(option_value_ids, secondoption_value_ids, value, flag, option);

                                } else {
                                    int flag = 3;
                                    flags -= flag;
                                    price = option_price.substring(1);
                                    value = Float.parseFloat(price);
                                    System.out.println("valuuu" + value);
                                    totalprice -= value;
                                    roundtotal = String.format("%.2f", totalprice);
                                    System.out.println("totlsssgt" + roundtotal);
                                    optionname = option_name;
                                    optionvalue = option_value;
                                    option_valueid = option_value_id;
                                    menu_optionid = menu_option_id;
                                    Iterator mIterator = option_value_ids.keySet().iterator();
                                    while (mIterator.hasNext()) {
                                        Integer key = (Integer) mIterator.next();
                                        Integer value = (Integer) option_ids.get(key);
                                        System.out.println("Key" + key + "_ value" + value);
                                        if (key == option_valueid && value == menu_optionid) {
                                            mIterator.remove();
                                        }
                                    }
                                    addDoubleOptions(option_value_ids, secondoption_value_ids, value, flag, option);
                                }
                            }
                        });


                    }
                }
            }

            @Override
            public int getItemCount() {
                return mcustomCursor != null ? mcustomCursor.getCount() : 0;
            }
        }
    }

    public void showAlert() {
        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(getActivity());
        // Setting Dialog Message
        alertDialog.setMessage("You have added different variations of this item. Please visit the cart to remove them separately.");
        // On pressing Settings button
        alertDialog.setPositiveButton("VIEW CART", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getActivity(), ViewCartActivity.class);
                startActivity(intent);
            }
        });
        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        // Showing Alert Message
        alertDialog.show();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        menu_cursor.close();
    }

    public void refresh() {
        updateMenuCursor();
        mMenuAdapter.notifyDataSetChanged();
    }
}
