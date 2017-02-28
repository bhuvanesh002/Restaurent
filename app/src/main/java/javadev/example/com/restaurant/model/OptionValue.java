package javadev.example.com.restaurant.model;

/**
 * Created by Support on 1/11/2017.
 */
public class OptionValue {
    public int getOption_valueid() {
        return option_valueid;
    }

    public void setOption_valueid(int option_valueid) {
        this.option_valueid = option_valueid;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int option_valueid;
    public int menu_optionid;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String value;
    public String price;

    public OptionValue(int option_valueid, int menu_optionid, String value, String price) {
        this.option_valueid = option_valueid;
        this.menu_optionid = menu_optionid;
        this.value = value;
        this.price = price;

    }

    public OptionValue() {


    }
}
