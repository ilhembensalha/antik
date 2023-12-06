package com.example.antik;

import android.graphics.Bitmap;

import com.google.gson.annotations.SerializedName;

public class User {
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("email")
    private String email;
    @SerializedName("password")
    private String password;





    public User(int id ,String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;

    }
    public User(String name, String email, String password) {

        this.name = name;
        this.email = email;
        this.password = password;

    }
    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }
}