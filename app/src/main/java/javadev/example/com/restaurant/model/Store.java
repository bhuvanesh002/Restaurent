package javadev.example.com.restaurant.model;

/**
 * Created by Support on 12/28/2016.
 */
public class Store {

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String nickname;
    public String address;

    public int getStore_id() {
        return store_id;
    }

    public void setStore_id(int store_id) {
        this.store_id = store_id;
    }

    public String getDelivery_status() {
        return delivery_status;
    }

    public void setDelivery_status(String delivery_status) {
        this.delivery_status = delivery_status;
    }

    public int store_id;

    public String getSuiteno() {
        return suiteno;
    }

    public void setSuiteno(String suiteno) {
        this.suiteno = suiteno;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String suiteno;
    public String city;
    public String state;
    public String zipcode;

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public String getOpentime() {
        return opentime;
    }

    public void setOpentime(String opentime) {
        this.opentime = opentime;
    }

    public String getClosetime() {
        return closetime;
    }

    public void setClosetime(String closetime) {
        this.closetime = closetime;
    }

    public String getClosereason() {
        return closereason;
    }

    public void setClosereason(String closereason) {
        this.closereason = closereason;
    }

    public String open;
    public String opentime;
    public String closetime;
    public String closereason;
    public String delivery_status;

    public Store() {

    }

    public Store(int store_id, String nickname, String address, String suiteno, String city, String state, String zipcode, String open, String opentime, String closetime, String closereason, String delivery_status) {
        this.store_id = store_id;
        this.nickname = nickname;
        this.address = address;
        this.suiteno = suiteno;
        this.city = city;
        this.state = state;
        this.zipcode = zipcode;
        this.open = open;
        this.opentime = opentime;
        this.closetime = closetime;
        this.closereason = closereason;
        this.delivery_status = delivery_status;


    }


}
