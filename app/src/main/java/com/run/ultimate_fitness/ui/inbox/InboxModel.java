package com.run.ultimate_fitness.ui.inbox;

import android.graphics.Bitmap;

public class InboxModel {
    private String uid;
    private String name;
    private Bitmap image;
    private String message;

    public InboxModel(String uid, String name, String message, Bitmap image) {
        this.uid = uid;
        this.name = name;
        this.message = message;
        this.image = image;

    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
