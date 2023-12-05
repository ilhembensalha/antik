package com.example.antik;

import com.google.gson.annotations.SerializedName;

public class AnnonceDetails {
    @SerializedName("id")
    private int id;
    @SerializedName("titre")
    private String titre;
    @SerializedName("description")
    private String description;
    @SerializedName("prix")
    private double prix;
    @SerializedName("location")
    private String location;
    @SerializedName("livraison")
    private String livraison;
    @SerializedName("cat_id")
    private int categoryId;
    @SerializedName("user_id")
    private int userId;
    @SerializedName("image")
    private ImageDetails image;

    // Constructor, getters, and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLivraison() {
        return livraison;
    }

    public void setLivraison(String livraison) {
        this.livraison = livraison;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public ImageDetails getImage() {
        return image;
    }

    public void setImage(ImageDetails image) {
        this.image = image;
    }

    public class ImageDetails {

        @SerializedName("path")
        private String path;

        @SerializedName("url")
        private final String url;

        public ImageDetails(String path, String url) {
            this.path = path;
            this.url = url;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getUrl() {
            return url;
        }



    }
}
