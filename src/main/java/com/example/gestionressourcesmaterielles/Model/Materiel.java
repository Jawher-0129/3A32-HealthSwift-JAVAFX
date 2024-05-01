package com.example.gestionressourcesmaterielles.Model;

import java.time.LocalDateTime;
import java.util.Date;

public class Materiel {
    private int id;

    private String LibelleMateriel;

    private String Description;

    private int Disponibilite;

    private String ImageMateriel;

    private double Prix;
    private int id_categorie;

    private LocalDateTime date_expiration;

    public Materiel()
    {

    }

    public void setDate_expiration(LocalDateTime date_expiration) {
        this.date_expiration = date_expiration;
    }

    public Materiel(int id, String libelleMateriel, String description, int disponibilite, String imageMateriel, double prix, int id_categorie, LocalDateTime date_expiration) {
        this.id = id;
        LibelleMateriel = libelleMateriel;
        Description = description;
        Disponibilite = disponibilite;
        ImageMateriel = imageMateriel;
        Prix = prix;
        this.id_categorie = id_categorie;
        this.date_expiration = date_expiration;
    }

    public Materiel(String libelleMateriel, String description, int disponibilite, String imageMateriel, double prix, int id_categorie, LocalDateTime date_expiration) {
        this.id = id;
        LibelleMateriel = libelleMateriel;
        Description = description;
        Disponibilite = disponibilite;
        ImageMateriel = imageMateriel;
        Prix = prix;
        this.id_categorie = id_categorie;
        this.date_expiration = date_expiration;
    }

    public Materiel(int id, String libelleMateriel, String description, int disponibilite, String imageMateriel, double prix, int id_categorie) {
        this.id = id;
        LibelleMateriel = libelleMateriel;
        Description = description;
        Disponibilite = disponibilite;
        ImageMateriel = imageMateriel;
        Prix = prix;
        this.id_categorie = id_categorie;
    }


    public Materiel(int id, String libelleMateriel, String description, int disponibilite, String imageMateriel, double prix) {
        this.id = id;
        LibelleMateriel = libelleMateriel;
        Description = description;
        Disponibilite = disponibilite;
        ImageMateriel = imageMateriel;
        Prix = prix;
    }


    public Materiel(String libelleMateriel, String description, int disponibilite, String imageMateriel, double prix, int id_categorie) {
        LibelleMateriel = libelleMateriel;
        Description = description;
        Disponibilite = disponibilite;
        ImageMateriel = imageMateriel;
        Prix = prix;
        this.id_categorie = id_categorie;
    }

    public Materiel(String libelleMateriel, String description, int disponibilite, String imageMateriel, double prix) {
        LibelleMateriel = libelleMateriel;
        Description = description;
        Disponibilite = disponibilite;
        ImageMateriel = imageMateriel;
        Prix = prix;
    }

    public LocalDateTime getDate_expiration() {
        return date_expiration;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLibelleMateriel() {
        return LibelleMateriel;
    }

    public void setLibelleMateriel(String libelleMateriel) {
        LibelleMateriel = libelleMateriel;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getDisponibilite() {
        return Disponibilite;
    }

    public void setDisponibilite(int disponibilite) {
        Disponibilite = disponibilite;
    }

    public String getImageMateriel() {
        return ImageMateriel;
    }

    public void setImageMateriel(String imageMateriel) {
        ImageMateriel = imageMateriel;
    }

    public double getPrix() {
        return Prix;
    }

    public void setPrix(double prix) {
        Prix = prix;
    }

    public int getId_categorie() {
        return id_categorie;
    }

    public void setId_categorie(int id_categorie) {
        this.id_categorie = id_categorie;
    }

    @Override
    public String toString() {
        return "Materiel{" +
                "id=" + id +
                ", LibelleMateriel='" + LibelleMateriel + '\'' +
                ", Description='" + Description + '\'' +
                ", Disponibilite=" + Disponibilite +
                ", ImageMateriel='" + ImageMateriel + '\'' +
                ", Prix=" + Prix +
                ", id_categorie=" + id_categorie +
                '}';
    }
}

