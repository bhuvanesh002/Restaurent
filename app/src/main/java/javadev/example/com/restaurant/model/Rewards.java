package javadev.example.com.restaurant.model;

/**
 * Created by Support on 1/27/2017.
 */
public class Rewards {
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

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public String location;
    public String date;
    public String operation;
    public Integer points;

    public Rewards() {

    }

    public Rewards(String location, String date, String operation, Integer points) {
        this.location = location;
        this.date = date;
        this.operation = operation;
        this.points = points;
    }
}
