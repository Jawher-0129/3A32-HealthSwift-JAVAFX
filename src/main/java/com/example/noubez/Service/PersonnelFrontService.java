package com.example.noubez.Service;

import com.example.noubez.Model.Personnel;

import java.util.ArrayList;
import java.util.List;

public class PersonnelFrontService {
    private static List<Personnel> personnelList = new ArrayList<>();


    public List<Personnel> getAll() {
        // Ici, vous pouvez récupérer les données de la base de données
        // Pour la démonstration, nous renvoyons simplement la liste statique
        return personnelList;
    }

    public void add(Personnel personnel) {
        // Ajouter le personnel à la liste
        personnelList.add(personnel);
        // Ici, vous pouvez insérer les données dans la base de données
    }

    public void update(Personnel newPersonnel, int personnelId) {
        // Recherche du personnel à mettre à jour
        for (Personnel personnel : personnelList) {
            if (personnel.getId_personnel() == personnelId) {
                // Mise à jour des informations du personnel
                personnel.setNom(newPersonnel.getNom());
                personnel.setPrenom_personnel(newPersonnel.getPrenom_personnel());
                personnel.setDisponibilite(newPersonnel.getDisponibilite());
                personnel.setRole(newPersonnel.getRole());
                personnel.setExperience(newPersonnel.getExperience());
                personnel.setImage(newPersonnel.getImage());
                personnel.setRating(newPersonnel.getRating());
                personnel.setUser_id_id(newPersonnel.getUser_id_id());
                // Ici, vous pouvez mettre à jour les données dans la base de données
                return;
            }
        }
        // Si le personnel n'est pas trouvé
        throw new IllegalArgumentException("Personnel non trouvé avec l'ID : " + personnelId);
    }

    public void delete(int personnelId) {
        // Recherche du personnel à supprimer
        for (Personnel personnel : personnelList) {
            if (personnel.getId_personnel() == personnelId) {
                // Supprimer le personnel de la liste
                personnelList.remove(personnel);
                // Ici, vous pouvez supprimer les données de la base de données
                return;
            }
        }
        // Si le personnel n'est pas trouvé
        throw new IllegalArgumentException("Personnel non trouvé avec l'ID : " + personnelId);
    }
}
