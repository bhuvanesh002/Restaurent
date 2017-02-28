package javadev.example.com.restaurant.model;

/**
 * Created by Support on 1/27/2017.
 */
public class OrderHistory {
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOrder_number() {
        return order_number;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getOrder_type() {
        return order_type;
    }

    public void setOrder_type(String order_type) {
        this.order_type = order_type;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String location;
    public String date;
    public String order_number;
    public String order_type;
    public String order_status;
    public String price;

    public OrderHistory() {

    }

    public OrderHistory(String location, String date, String order_number, String order_type, String order_status, String price) {
        this.location = location;
        this.date = date;
        this.order_number = order_number;
        this.order_type = order_type;
        this.order_status = order_status;
        this.price = price;
    }
}
