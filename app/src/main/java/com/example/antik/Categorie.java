package com.example.antik;

import com.google.gson.annotations.SerializedName;

public class Categorie {

        @SerializedName("id")
        private int id;

        @SerializedName("nomcat")
        private String nomcat;

        // Constructors, getters, and setters

        public Categorie(int id, String nomcat) {
            this.id = id;
            this.nomcat = nomcat;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return nomcat;
        }

        // Add other getters and setters as needed
    }

