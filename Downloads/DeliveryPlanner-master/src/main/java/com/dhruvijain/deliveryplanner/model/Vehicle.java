package com.dhruvijain.deliveryplanner.model;

public class Vehicle {
    private int id;
    private String model;
    private String registrationNumber;

    public Vehicle(int id, String model, String registrationNumber) {
        this.id = id;
        this.model = model;
        this.registrationNumber = registrationNumber;
    }

    public int getId() {
        return id;
    }

    public String getModel() {
        return model;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "id=" + id +
                ", model='" + model + '\'' +
                ", registrationNumber='" + registrationNumber + '\'' +
                '}';
    }
}
