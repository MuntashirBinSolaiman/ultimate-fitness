package com.run.ultimate_fitness.ui.workouts;

public class WorkoutsModel {

    private String workout_name;
    private String workout_zone;
    private String workout_description;
    private int workout_image;




    public int getWorkout_image() {
        return workout_image;
    }

    public void setWorkout_image(int workout_image) {
        this.workout_image = workout_image;
    }



    public String getWorkout_zone() {
        return workout_zone;
    }

    public void setWorkout_zone(String workout_zone) {
        this.workout_zone = workout_zone;
    }



    public String getWorkout_name() {
        return workout_name;
    }

    public void setWorkout_name(String workout_name) {
        this.workout_name = workout_name;
    }



    public String getWorkout_description() {
        return workout_description;
    }

    public void setWorkout_description(String workout_description) {
        this.workout_description = workout_description;
    }



    public WorkoutsModel(String workout_name, String workout_zone, int workout_image, String workout_description) {
        this.workout_image = workout_image;

        this.workout_name = workout_name;
        this.workout_zone = workout_zone;
        this.workout_description = workout_description;



    }


}
