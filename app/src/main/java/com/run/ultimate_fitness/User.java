package com.run.ultimate_fitness;

public class User {

    public String firstName;
    public String lastName;
    public String picture;
    public String workoutGoal;
    public int phoneNumber;
    public Double weight,height;

    //This model is used to send and receive user information to the fire base database
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

    public Double getWeight() { return weight; }

    public Double getHeight() {
        return height;
    }

    public String getPicture(){return picture;}

    public String getWorkoutGoal() {
        return workoutGoal;
    }

    public void setWorkoutGoal(String workoutGoal) {
        this.workoutGoal = workoutGoal;
    }


    public User(String firstName, String lastName, int phoneNumber, Double weight, Double height, String picture, String workoutGoal) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.weight = weight;
        this.height = height;
        this.picture = picture;
        this.workoutGoal = workoutGoal;
    }
}
