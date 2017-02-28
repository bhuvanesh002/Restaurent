package javadev.example.com.restaurant.model;

/**
 * Created by Support on 1/28/2017.
 */
public class Tax {
    public static final String TAXNAME = "name";
    public static final String TAXPERCENTAGE = "percentage";
    public static final String TABLE_TAX = "tax";

    public String getTax_type() {
        return tax_type;
    }

    public void setTax_type(String tax_type) {
        this.tax_type = tax_type;
    }

    public String getTax_value() {
        return tax_value;
    }

    public void setTax_value(String tax_value) {
        this.tax_value = tax_value;
    }

    public String tax_type;
    public String tax_value;

    public Tax() {

    }

    public Tax(String tax_type, String tax_value) {
        this.tax_type = tax_type;
        this.tax_value = tax_value;

    }

    public static final String[] TAX_COLUMNS = {
            TAXNAME, TAXPERCENTAGE
    };
}
