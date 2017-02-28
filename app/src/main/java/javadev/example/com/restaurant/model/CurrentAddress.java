package javadev.example.com.restaurant.model;

/**
 * Created by Support on 1/30/2017.
 */
public class CurrentAddress {
    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String locality;
    public String country_name;
    public String country_code;
    public String postal_code;

}
