package javadev.example.com.restaurant.model;

/**
 * Created by Support on 1/3/2017.
 */
public class User {
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String username;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public String email;
    public String password;
    public String mobileno;

    public User() {

    }

    public User(String username, String email, String password, String mobileno) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.mobileno = mobileno;
    }
}
