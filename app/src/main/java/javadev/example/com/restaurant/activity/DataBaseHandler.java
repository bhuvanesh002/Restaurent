package javadev.example.com.restaurant.activity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import javadev.example.com.restaurant.model.Menu;

public class DataBaseHandler extends SQLiteOpenHelper {

    // All Static variables
// Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "menu";

    // Contacts table name
    private static final String TABLE_USERS = "users";
    private static final String TABLE_ADDRESS = "address";
    private static final String TABLE_CATEGORY = "category";
    private static final String TABLE_MENU = "menu";
    private static final String TABLE_MENUOPTION = "menu_option";
    private static final String TABLE_OPTIONVALUE = "option_value";
    private static final String TABLE_STORECATMENU = "store_cat_menu";
    private static final String TABLE_MENUMENUOPTION = "menu_menuoption";
    private static final String TABLE_CART = "cart";
    private static final String TABLE_SUBCART = "subcart";
    private static final String TABLE_TAX = "tax";


    // Contacts Table Columns names
    private static final String KEY_ID = "_id";
    private static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_MOBILENO = "mobileno";
    private static final String KEY_ADDRESS = "addr";
    private static final String KEY_SUITENO = "suiteno";
    private static final String KEY_NICKNAME = "nickname";
    private static final String KEY_STATE = "state";
    private static final String KEY_CITY = "city";
    private static final String KEY_ZIPCODE = "zipcode";
    private static final String KEY_MENUID = "menu_id";
    private static final String KEY_MENUNAME = "menu_name";
    private static final String KEY_MENUPRICE = "menu_price";
    private static final String KEY_CATEGORYNAME = "name";


    private static final String KEY_MENUCATEGORYNAME = "category_name";
    private static final String KEY_CATEGORYID = "category_id";
    private static final String KEY_MENUCATEGORYID = "category_id";
    private static final String KEY_MENUDESCRIPTION = "menu_description";
    private static final String KEY_MINIMUMQTY = "minimum_qty";
    private static final String KEY_MENUPRIORITY = "menu_priority";
    private static final String KEY_MEALTIMENAME = "mealtime_name";
    private static final String KEY_STARTTIME = "start_time";
    private static final String KEY_ENDTIME = "end_time";
    private static final String KEY_ISMEALTIME = "is_mealtime";
    private static final String KEY_MEALTIMESTATUS = "mealtime_status";
    private static final String KEY_SPECIALSTATUS = "special_status";
    private static final String KEY_ISSPECIAL = "is_special";
    private static final String KEY_STARTDATE = "start_date";
    private static final String KEY_ENDDATE = "end_date";
    private static final String KEY_MENUIMAGE = "menu_photo";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_PRIORITY = "priority";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_MENUIOPTIONID = "menu_option_id";
    private static final String KEY_OPTIONID = "option_id";
    private static final String KEY_OPTIONNAME = "option_name";
    private static final String KEY_DISPLAYTYPE = "display_type";
    private static final String KEY_MENUOPPRIORITY = "priority";
    private static final String KEY_DEFAULTVALUEID = "default_value_id";
    private static final String KEY_OPTIONVALUEID = "option_value_id";
    private static final String KEY_MENUOPTIONID = "menu_option_id";
    private static final String KEY_VALUE = "value";
    private static final String KEY_PRICE = "price";
    private static final String KEY_STOREID = "store_id";
    private static final String KEY_CATID = "cat_id";
    private static final String KEY_STOREMENUID = "menu_id";
    private static final String KEY_MENUOPMENUID = "menu_id";
    private static final String KEY_MENUOPTIONMENIID = "menu_option_id";
    private static final String KEY_CARTSTOREID = "store_id";
    private static final String KEY_CARTMENUID = "menu_id";
    private static final String KEY_CARTSELECTOPTION = "selected_options";
    private static final String KEY_CARTMENUPRICE = "menu_price";
    private static final String KEY_TAXNAME = "name";
    private static final String KEY_TAXPERCENTAGE = "percentage";
    private static final String KEY_CARTID = "cart_id";
    private static final String KEY_CARTCOUNT = "count";


    private static final String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
            + KEY_ID + " INTEGER PRIMARY KEY," + KEY_USERNAME + " TEXT," + KEY_MOBILENO + " INTEGER,"
            + KEY_EMAIL + " TEXT," + KEY_PASSWORD + " TEXT " + ")";
    String vegi = "veg";
    String non = "nonveg";
    private static final String CREATE_ADDRESS_TABLE = "CREATE TABLE " + TABLE_ADDRESS + "("
            + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NICKNAME + " TEXT," + KEY_ADDRESS + " TEXT," + KEY_SUITENO + " TEXT," + KEY_STATE + " TEXT," + KEY_CITY + " TEXT," + KEY_ZIPCODE + " INTEGER " + ")";
    private static final String CREATE_CATEGORY_TABLE = "CREATE TABLE " + TABLE_CATEGORY + "("
            + KEY_ID + " INTEGER PRIMARY KEY," + KEY_CATEGORYID + " INTEGER," + KEY_CATEGORYNAME + " TEXT," + KEY_DESCRIPTION + " TEXT,"
            + KEY_PRIORITY + " TEXT," + KEY_IMAGE + " TEXT " + ")";
    private static final String CREATE_MENU_TABLE = "CREATE TABLE " + TABLE_MENU + "("
            + KEY_ID + " INTEGER PRIMARY KEY," + KEY_MENUID + " INTEGER," + KEY_MENUNAME + " TEXT," + KEY_MENUDESCRIPTION + " TEXT,"
            + KEY_MENUCATEGORYID + " INTEGER," + KEY_MENUCATEGORYNAME + " TEXT," + KEY_MINIMUMQTY + " INTEGER," + KEY_MENUPRIORITY + " INTEGER," + KEY_MEALTIMENAME + " TEXT," + KEY_STARTTIME + " TEXT," + KEY_ENDTIME + " TEXT," + KEY_ISMEALTIME + " INTEGER," + KEY_MEALTIMESTATUS + " INTEGER," + KEY_SPECIALSTATUS + " INTEGER," + KEY_ISSPECIAL + " INTEGER," + KEY_MENUPRICE + " TEXT," + KEY_MENUIMAGE + " TEXT," + KEY_STARTDATE + " Date," + KEY_ENDDATE + " Date " + ")";

    private static final String CREATE_OPTIONVALUE_TABLE = "CREATE TABLE " + TABLE_MENUOPTION + "("
            + KEY_ID + " INTEGER PRIMARY KEY," + KEY_MENUIOPTIONID + " INTEGER," + KEY_OPTIONID + " INTEGER," + KEY_OPTIONNAME + " TEXT,"
            + KEY_DISPLAYTYPE + " TEXT," + KEY_MENUOPPRIORITY + " INTEGER," + KEY_DEFAULTVALUEID + " INTEGER " + ")";

    private static final String CREATE_MENUOPTION_TABLE = "CREATE TABLE " + TABLE_OPTIONVALUE + "("
            + KEY_ID + " INTEGER PRIMARY KEY," + KEY_OPTIONVALUEID + " INTEGER," + KEY_MENUOPTIONID + " INTEGER," + KEY_VALUE + " TEXT,"
            + KEY_PRICE + " TEXT " + ")";
    private static final String CREATE_STORECATMENU_TABLE = "CREATE TABLE " + TABLE_STORECATMENU + "("
            + KEY_ID + " INTEGER PRIMARY KEY," + KEY_STOREID + " INTEGER," + KEY_CATID + " INTEGER," + KEY_STOREMENUID + " INTEGER " + ")";
    private static final String CREATE_MENUMENUOPTION_TABLE = "CREATE TABLE " + TABLE_MENUMENUOPTION + "("
            + KEY_ID + " INTEGER PRIMARY KEY," + KEY_MENUOPMENUID + " INTEGER," + KEY_MENUOPTIONMENIID + " INTEGER " + ")";
    private static final String CREATE_CART_TABLE = "CREATE TABLE " + TABLE_CART + "("
            + KEY_ID + " INTEGER PRIMARY KEY," + KEY_CARTSTOREID + " INTEGER," + KEY_CARTMENUID + " INTEGER," + KEY_CARTMENUPRICE + " TEXT," + KEY_MENUNAME + " TEXT," + KEY_OPTIONNAME + " TEXT," + KEY_CARTCOUNT + " INTEGER," + KEY_CARTSELECTOPTION + " INTEGER DEFAULT 0);" + ")";
    private static final String CREATE_SUBCART_TABLE = "CREATE TABLE " + TABLE_SUBCART + "("
            + KEY_ID + " INTEGER PRIMARY KEY," + KEY_CARTID + " INTEGER," + KEY_OPTIONVALUEID + " INTEGER," + KEY_MENUIOPTIONID + " INTEGER" + ")";
    private static final String CREATE_TAX_TABLE = "CREATE TABLE " + TABLE_TAX + "("
            + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TAXNAME + " TEXT," + KEY_TAXPERCENTAGE + " INTEGER" + ")";

    public DataBaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        System.out.println("createdb");
        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_ADDRESS_TABLE);
        db.execSQL(CREATE_CATEGORY_TABLE);
        db.execSQL(CREATE_MENU_TABLE);
        db.execSQL(CREATE_MENUOPTION_TABLE);
        db.execSQL(CREATE_OPTIONVALUE_TABLE);
        db.execSQL(CREATE_STORECATMENU_TABLE);
        db.execSQL(CREATE_MENUMENUOPTION_TABLE);
        db.execSQL(CREATE_CART_TABLE);
        db.execSQL(CREATE_SUBCART_TABLE);
        db.execSQL(CREATE_TAX_TABLE);


    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
// Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ADDRESS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MENU);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MENUOPTION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OPTIONVALUE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STORECATMENU);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MENUMENUOPTION);

        System.out.println("Inddddd");

//eate tables again
        onCreate(db);
        System.out.println("Inddddd1");
    }

    public void insert(String tableName, ArrayList<ContentValues> valueList) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (ContentValues value : valueList) {
            db.insert(tableName, null, value);
        }
    }

    public void dropTable(String tablename, String table_two) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + tablename);
        db.execSQL("delete from " + table_two);

    }

    public void updateCart(String tableName, ArrayList<ContentValues> valueList, String[] columns, String selections, String[] selectionArgs) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (ContentValues value : valueList) {
            db.update(TABLE_CART, value, selections, new String[]{selectionArgs[0]});
        }
    }

    public void insertORupdate(String tableName, ArrayList<ContentValues> valueList, String[] columns, String selections, String[] selectionArgs) {

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = query(tableName, columns, selections, selectionArgs, null);
        System.out.println("upd" + cursor.getCount());
        if (cursor.getCount() == 0) {
            for (ContentValues value : valueList) {
                db.insert(tableName, null, value);
            }
        } else {
            for (ContentValues value : valueList) {
                db.update(TABLE_CART, value, selections, new String[]{selectionArgs[0]});

            }
        }
    }

    public Cursor query(String tableName, String[] columns, String selections, String[] selectionArgs, String orderBy) {

// Select All Query

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(tableName, columns, selections, selectionArgs, null, null, orderBy);
// looping through all rows and adding to list
        System.out.println("ghajacun" + cursor);


// close inserting data from database

// return contact list
        return cursor;

    }

    public Cursor queryCount(int menu_id) {

// Select All Query
        System.out.println("menuuuuuuuuuuuuuuu" + menu_id);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(selected_options) AS counts FROM cart WHERE menu_id='" + menu_id + "' group by menu_id", null);

// looping through all rows and adding to list


// close inserting data from database

// return contact list
        return cursor;

    }

    public Cursor getCart() {

// Select All Query

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM cart", null);

// looping through all rows and adding to list
        System.out.println("ghajacun" + cursor);


// close inserting data from database

// return contact list
        return cursor;

    }

    public Cursor getMenuOptions(ArrayList<Integer> menu_optionids) {

// Select All Query
        StringBuilder ids = new StringBuilder();

        //Looping through the list
        for (int i = 0; i < menu_optionids.size(); i++) {
            ids.append("'");
            ids.append(menu_optionids.get(i));
            ids.append("'");
            if (i != menu_optionids.size() - 1) {
                ids.append(",");
            }
        }
        System.out.println("idddddddddddd" + ids.toString());


        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT menu_option.menu_option_id,menu_option.option_name,menu_option.display_type,option_value.option_value_id,option_value.value,option_value.price FROM menu_option INNER JOIN option_value ON menu_option.menu_option_id=option_value.menu_option_id where menu_option.menu_option_id IN(" + ids + ")", null);

// looping through all rows and adding to list

// return contact list
        return cursor;

    }

    public Cursor getItems() {

// Select All Query

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT _id,menu_id,selected_options,menu_name,menu_price,option_name,count FROM cart where selected_options > 0", null);
// looping through all rows and adding to list
        System.out.println("ghaja");


        return cursor;

    }

    public Cursor getCartItems(int id) {

// Select All Query

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT DISTINCT option_value.value,menu_option.option_name FROM subcart INNER JOIN option_value on subcart.option_value_id=option_value.option_value_id INNER JOIN menu_option on subcart.menu_option_id=menu_option.menu_option_id where subcart.cart_id='" + id + "'", null);
// looping through all rows and adding to list
        System.out.println("ghaja");


        return cursor;

    }

    public void addcustomize(String tableName, ArrayList<ContentValues> menuList, ArrayList<ContentValues> menu_optionsList, String[] columns, String[] columns_two, String selections, String[] selectionArgs) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = query(tableName, columns_two, selections, selectionArgs, null);
// looping through all rows and adding to list

        long id = 0;
        if (cursor.getCount() == 0) {
            int counts = 1;
            for (ContentValues value : menuList) {
                value.put(Menu.SELECTED_OPTIONS, counts);
                value.put(Menu.COUNT, counts);
                id = db.insert(TABLE_CART, null, value);
            }


            for (ContentValues values : menu_optionsList) {
                values.put(Menu.CART_ID, id);

                db.insert(TABLE_SUBCART, null, values);
            }


        } else {
            int sId = 0, mcount = 0;
            Cursor mcursor = db.rawQuery("SELECT _id FROM cart where menu_id='" + selectionArgs[0] + "'", null);
            mcursor.moveToFirst();
            sId = mcursor.getInt(mcursor.getColumnIndex("_id"));
            StringBuilder mid = new StringBuilder();
            String query, select_query;

            mid.append("select cart._id, cart.menu_id, count(cart.menu_id) as count from cart inner join subcart on cart._id=subcart.cart_id where cart.menu_id=");
            mid.append(selectionArgs[0]);
            mid.append(" and (");
            for (ContentValues values : menu_optionsList) {

                mid.append("(");
                mid.append("menu_option_id=");
                Integer menu_optionid = values.getAsInteger(Menu.MENU_OPTION_ID);
                mid.append(menu_optionid);
                mid.append(" AND ");
                mid.append("option_value_id=");
                Integer option_valueid = values.getAsInteger(Menu.OPTION_VALUE_ID);
                mid.append(option_valueid);
                mid.append(")");
                mid.append(" OR ");


            }
            mcount = menu_optionsList.size();
            query = mid.substring(0, mid.length() - 4);
            select_query = query + ") group by cart._id";


            Cursor cursors = db.rawQuery(select_query, null);

            if (cursors != null) {
                if (cursors.getCount() > 0 && cursors.moveToFirst()) {

                    int count = cursors.getInt(cursors.getColumnIndex("count"));

                    if (mcount != count) {

                        int counts = 1;

                        for (ContentValues value : menuList) {
                            value.put(Menu.SELECTED_OPTIONS, counts);
                            value.put(Menu.COUNT, counts);
                            id = db.insert(TABLE_CART, null, value);
                        }

                        for (ContentValues values : menu_optionsList) {
                            values.put(Menu.CART_ID, id);

                            db.insert(TABLE_SUBCART, null, values);
                        }

                    } else {
                        int cart_id = cursors.getInt(cursors.getColumnIndex("_id"));

                        int select_count = 0, cartcount = 0;
                        Cursor count_cursor = db.rawQuery("SELECT selected_options FROM cart where _id='" + cart_id + "'", null);
                        if (count_cursor != null) {
                            if (count_cursor.getCount() > 0 && count_cursor.moveToFirst()) {
                                cartcount = count_cursor.getInt(count_cursor.getColumnIndex(Menu.SELECTED_OPTIONS));
                            }

                        }
                        select_count = cartcount + 1;
                        ContentValues count_values = new ContentValues();
                        count_values.put(Menu.SELECTED_OPTIONS, select_count);
                        System.out.println("updatee");


                        db.update(TABLE_CART, count_values, "_id=" + cart_id, null);

                    }
                } else {

                    int counts = 1;
                    for (ContentValues value : menuList) {
                        value.put(Menu.SELECTED_OPTIONS, counts);
                        value.put(Menu.COUNT, counts);
                        id = db.insert(TABLE_CART, null, value);
                    }

                    for (ContentValues values : menu_optionsList) {
                        values.put(Menu.CART_ID, id);

                        db.insert(TABLE_SUBCART, null, values);
                    }
                }
            }

        }
        db.close(); // Closing database connection
    }
    public void adddoublecustomiztation(String tableName, ArrayList<ContentValues> menuList, ArrayList<ContentValues> menu_optionsList,ArrayList<ContentValues> menu_optionssecondList, String[] columns, String[] columns_two, String selections, String[] selectionArgs) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = query(tableName, columns_two, selections, selectionArgs, null);
// looping through all rows and adding to list

        long id = 0;
        if (cursor.getCount() == 0) {
            int counts = 1;
            for (ContentValues value : menuList) {
                value.put(Menu.SELECTED_OPTIONS, counts);
                value.put(Menu.COUNT, counts);
                id = db.insert(TABLE_CART, null, value);
            }


            for (ContentValues values : menu_optionsList) {
                values.put(Menu.CART_ID, id);

                db.insert(TABLE_SUBCART, null, values);
            }
            for (ContentValues values : menu_optionssecondList) {
                values.put(Menu.CART_ID, id);

                db.insert(TABLE_SUBCART, null, values);
            }


        } else {
            int sId = 0, mcount = 0;
            Cursor mcursor = db.rawQuery("SELECT _id FROM cart where menu_id='" + selectionArgs[0] + "'", null);
            mcursor.moveToFirst();
            sId = mcursor.getInt(mcursor.getColumnIndex("_id"));
            StringBuilder mid = new StringBuilder();
            String query, select_query;

            mid.append("select cart._id, cart.menu_id, count(cart.menu_id) as count from cart inner join subcart on cart._id=subcart.cart_id where cart.menu_id=");
            mid.append(selectionArgs[0]);
            mid.append(" and (");
            for (ContentValues values : menu_optionsList) {

                mid.append("(");
                mid.append("menu_option_id=");
                Integer menu_optionid = values.getAsInteger(Menu.MENU_OPTION_ID);
                mid.append(menu_optionid);
                mid.append(" AND ");
                mid.append("option_value_id=");
                Integer option_valueid = values.getAsInteger(Menu.OPTION_VALUE_ID);
                mid.append(option_valueid);
                mid.append(")");
                mid.append(" OR ");


            }
            for (ContentValues values : menu_optionssecondList) {

                mid.append("(");
                mid.append("menu_option_id=");
                Integer menu_optionid = values.getAsInteger(Menu.MENU_OPTION_ID);
                mid.append(menu_optionid);
                mid.append(" AND ");
                mid.append("option_value_id=");
                Integer option_valueid = values.getAsInteger(Menu.OPTION_VALUE_ID);
                mid.append(option_valueid);
                mid.append(")");
                mid.append(" OR ");


            }
            mcount = menu_optionsList.size()+menu_optionssecondList.size();
            query = mid.substring(0, mid.length() - 4);
            select_query = query + ") group by cart._id";


            Cursor cursors = db.rawQuery(select_query, null);

            if (cursors != null) {
                if (cursors.getCount() > 0 && cursors.moveToFirst()) {

                    int count = cursors.getInt(cursors.getColumnIndex("count"));

                    if (mcount != count) {

                        int counts = 1;

                        for (ContentValues value : menuList) {
                            value.put(Menu.SELECTED_OPTIONS, counts);
                            value.put(Menu.COUNT, counts);
                            id = db.insert(TABLE_CART, null, value);
                        }

                        for (ContentValues values : menu_optionsList) {
                            values.put(Menu.CART_ID, id);

                            db.insert(TABLE_SUBCART, null, values);
                        }
                        for (ContentValues values : menu_optionssecondList) {
                            values.put(Menu.CART_ID, id);

                            db.insert(TABLE_SUBCART, null, values);
                        }

                    } else {
                        int cart_id = cursors.getInt(cursors.getColumnIndex("_id"));

                        int select_count = 0, cartcount = 0;
                        Cursor count_cursor = db.rawQuery("SELECT selected_options FROM cart where _id='" + cart_id + "'", null);
                        if (count_cursor != null) {
                            if (count_cursor.getCount() > 0 && count_cursor.moveToFirst()) {
                                cartcount = count_cursor.getInt(count_cursor.getColumnIndex(Menu.SELECTED_OPTIONS));
                            }

                        }
                        select_count = cartcount + 1;
                        ContentValues count_values = new ContentValues();
                        count_values.put(Menu.SELECTED_OPTIONS, select_count);
                        System.out.println("updatee");


                        db.update(TABLE_CART, count_values, "_id=" + cart_id, null);

                    }
                } else {

                    int counts = 1;
                    for (ContentValues value : menuList) {
                        value.put(Menu.SELECTED_OPTIONS, counts);
                        value.put(Menu.COUNT, counts);
                        id = db.insert(TABLE_CART, null, value);
                    }

                    for (ContentValues values : menu_optionsList) {
                        values.put(Menu.CART_ID, id);

                        db.insert(TABLE_SUBCART, null, values);
                    }
                    for (ContentValues values : menu_optionssecondList) {
                        values.put(Menu.CART_ID, id);

                        db.insert(TABLE_SUBCART, null, values);
                    }
                }
            }

        }
        db.close(); // Closing database connection
    }

}



