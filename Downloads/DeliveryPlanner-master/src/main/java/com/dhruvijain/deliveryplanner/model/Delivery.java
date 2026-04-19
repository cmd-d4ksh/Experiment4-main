package com.dhruvijain.deliveryplanner.model;

public class Delivery {
    private int id;
    private String address;
    private String status;
    private int driverId;

    public Delivery(int id, String address, String status, int driverId) {
        this.id = id;
        this.address = address;
        this.status = status;
        this.driverId = driverId;
    }

    public Delivery(String address, String status, int driverId) {
        this.address = address;
        this.status = status;
        this.driverId = driverId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getDriverId() {
        return driverId;
    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }

    @Override
    public String toString() {
        return "Delivery{" +
                "id=" + id +
                ", address='" + address + '\'' +
                ", status='" + status + '\'' +
                ", driverId=" + driverId +
                '}';
    }
}
