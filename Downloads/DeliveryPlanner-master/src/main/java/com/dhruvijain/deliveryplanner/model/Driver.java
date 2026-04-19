package com.dhruvijain.deliveryplanner.model;

public class Driver extends Person {
    private String licenseNumber;

    public Driver(int id, String name, String contact, String licenseNumber) {
        super(id, name, contact);
        this.licenseNumber = licenseNumber;
    }

    public Driver(String name, String contact, String licenseNumber) {
        super(name, contact);
        this.licenseNumber = licenseNumber;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    @Override
    public String getRoleDetails() {
        return "Driver License: " + licenseNumber;
    }

    @Override
    public String toString() {
        return "Driver{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", contact='" + getContact() + '\'' +
                ", licenseNumber='" + licenseNumber + '\'' +
                '}';
    }
}
