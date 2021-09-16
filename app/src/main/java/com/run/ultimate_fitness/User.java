package com.run.ultimate_fitness;

public class User {

    public String firstName,lastName;
    public int phoneNumber;
    public Double weight,height;

    public User() {
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public Double getWeight() {
        return weight;
    }

    public Double getHeight() {
        return height;
    }

    public User(String firstName, String lastName, int phoneNumber, Double weight, Double height) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.weight = weight;
        this.height = height;


    }
}
