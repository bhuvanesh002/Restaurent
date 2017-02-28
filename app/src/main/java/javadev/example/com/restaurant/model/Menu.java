package javadev.example.com.restaurant.model;

/**
 * Created by Support on 1/2/2017.
 */
public class Menu {
    public static final String TABLE_USERS = "users";
    public static final String TABLE_ADDRESS = "address";
    public static final String TABLE_CATEGORY = "category";
    public static final String TABLE_MENU = "menu";
    public static final String TABLE_MENUOPTION = "menu_option";
    public static final String TABLE_OPTIONVALUE = "option_value";
    public static final String TABLE_STORECATMENU = "store_cat_menu";
    public static final String TABLE_MENUMENUOPTION = "menu_menuoption";
    public static final String TABLE_CART = "cart";
    public static final String TABLE_SUBCART = "subcart";
    public static final String MENU_ID = "menu_id";
    public static final String MENU_NAME = "menu_name";
    public static final String MENU_DESCRIPTION = "menu_description";
    public static final String MENU_PRICE = "menu_price";
    public static final String CATEGORY_NAME = "name";
    public static final String CAT_NAME = "category_name";
    public static final String MEALTIME_NAME = "mealtime_name";
    public static final String START_TIME = "start_time";
    public static final String END_TIME = "end_time";
    public static final String MENU_PHOTO = "menu_photo";
    public static final String START_DATE = "start_date";
    public static final String END_DATE = "end_date";
    public static final String CATEGORY_ID = "category_id";
    public static final String MINIMUM_QTY = "minimum_qty";
    public static final String MENU_PRIORITY = "menu_priority";
    public static final String IS_MEALTIME = "is_mealtime";
    public static final String MEALTIME_STATUS = "mealtime_status";
    public static final String SPECIAL_STATUS = "special_status";
    public static final String NAME = "name";
    public static final String IS_SPECIAL = "is_special";
    public static final String DESCRIPTION = "description";
    public static final String PRIORITY = "priority";
    public static final String IMAGE = "image";
    public static final String COUNTS = "counts";

    public static final String OPTION_ID = "option_id";
    public static final String OPTION_NAME = "option_name";
    public static final String DISPLAY_TYPE = "display_type";

    public static final String DEFAULT_VALUE_ID = "default_value_id";
    public static final String OPTION_VALUE_ID = "option_value_id";
    public static final String MENU_OPTION_ID = "menu_option_id";
    public static final String VALUE = "value";
    public static final String PRICE = "price";
    public static final String STOREID = "store_id";
    public static final String CATID = "cat_id";
    public static final String ID = "_id";
    public static final String SELECTED_OPTIONS = "selected_options";
    public static final String CART_ID = "cart_id";
    public static final String COUNT = "count";
    public static final String MENU_OPTIONS = "menu_options";
    public static final String[] categotyColomns = {
            CATEGORY_ID, CATEGORY_NAME
    };
    public static final String[] MENU_MENU_OPTION = {
            MENU_ID, MENU_OPTION_ID
    };
    public static final String[] CART_OPTIONS = {
            MENU_ID, SELECTED_OPTIONS
    };
    public static final String[] MENU_COLUMNS = {
            MENU_ID, MENU_NAME, MENU_PRICE, MENU_DESCRIPTION, CATEGORY_ID
    };
    public static final String[] CART_COLUMNS = {
            MENU_PRICE, SELECTED_OPTIONS
    };
    public static final String[] CART_COUNTS = {
            ID
    };
    public static final String[] CART_OPTION = {
            MENU_ID
    };
    public static final String[] MENU_OPTION_ITEMS = {
            MENU_ID, OPTION_VALUE_ID, MENU_OPTION_ID, SELECTED_OPTIONS, ID
    };
    public static final String[] MENU_OPTION_ITEMS_TWO = {
            MENU_ID
    };

    public static boolean isSelected() {
        return selected;
    }

    public static void setSelected(boolean selected) {
        Menu.selected = selected;
    }

    public static boolean selected;

}
