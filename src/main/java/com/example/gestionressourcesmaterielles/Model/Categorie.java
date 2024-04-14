package com.example.gestionressourcesmaterielles.Model;

public class Categorie {
    private int id;
    private String libelle;

    public Categorie(int id, String libelle) {
        this.id = id;
        this.libelle = libelle;
    }
    public Categorie(String libelle) {
        this.libelle = libelle;
    }

    public int getId() {
        return this.id;
    }

        public void setId(int id) {
        this.id = id;
    }

    public String getLibelle() {
        return this.libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String toString() {
        return "Categorie{id=" + this.id + ", libelle='" + this.libelle + "'}";
    }
}

