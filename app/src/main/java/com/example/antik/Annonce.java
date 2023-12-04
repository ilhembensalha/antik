package com.example.antik;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Annonce {
    @SerializedName("status")
    private int status;

    @SerializedName("annonces")
    private List<Annoncee> annonces;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<Annoncee> getAnnonces() {
        return annonces;
    }

    public void setAnnonces(List<Annoncee> annonces) {
        this.annonces = annonces;
    }

    public static class Annoncee {
        @SerializedName("id")
        private long id;

        @SerializedName("titre")
        private String titre;

        @SerializedName("description")
        private String description;

        @SerializedName("prix")
        private double prix;

        @SerializedName("location")
        private String location;

        @SerializedName("cat_id")
        private long catId;

        @SerializedName("user_id")
        private long userId;

        @SerializedName("image")
        private ImageDetails image;

        public long getId() {
            return id;
        }

        public void setId(long id) {
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

        public long getCatId() {
            return catId;
        }

        public void setCatId(long catId) {
            this.catId = catId;
        }

        public long getUserId() {
            return userId;
        }

        public void setUserId(long userId) {
            this.userId = userId;
        }

        public ImageDetails getImage() {
            return image;
        }

        public void setImage(ImageDetails image) {
            this.image = image;
        }

        public static class ImageDetails {
            @SerializedName("path")
            private String path;

            @SerializedName("url")
            private String url;

            public String getPath() {
                return path;
            }

            public void setPath(String path) {
                this.path = path;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }
    }
}
