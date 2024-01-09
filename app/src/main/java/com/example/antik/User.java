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
    @SerializedName("avatar")  // Add this line for the image URL field
    private String avatar;
    @SerializedName("phone")
    private String phone;

    public User(int id, String name, String email, String password, String avatar) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.avatar = avatar;
    }

    public User(String name, String email, String password, String avatar) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.avatar = avatar;
    }
    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }
   /* public User(String name, String email, String password,String phone) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
    }
*/
    public User() {
    }

    // Add getter and setter methods for 'id', 'name', 'email', and 'password'
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



    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String imageUrl) {
        this.avatar = imageUrl;
    }
}
