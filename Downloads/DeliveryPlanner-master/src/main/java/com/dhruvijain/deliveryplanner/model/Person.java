package com.dhruvijain.deliveryplanner.model;

public abstract class Person {
    private int id;
    private String name;
    private String contact;

    public Person(int id, String name, String contact) {
        this.id = id;
        this.name = name;
        this.contact = contact;
    }

    public Person(String name, String contact) {
        this.name = name;
        this.contact = contact;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    // Abstract method to demonstrate abstraction
    public abstract String getRoleDetails();

    @Override
    public String toString() {
        return "Person{id=" + id + ", name='" + name + "', contact='" + contact + "'}";
    }
}
