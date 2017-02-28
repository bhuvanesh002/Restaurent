package javadev.example.com.restaurant.model;

/**
 * Created by Support on 12/27/2016.
 */
public class Addresses {
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

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean selected;

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
    public boolean status;

    public Addresses() {

    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Addresses(String nickname, String address, String suiteno, String city, String state, String zipcode) {
        this.nickname = nickname;
        this.address = address;
        this.suiteno = suiteno;
        this.city = city;
        this.state = state;
        this.zipcode = zipcode;


    }


}
