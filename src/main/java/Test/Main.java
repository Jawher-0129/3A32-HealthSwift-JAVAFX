package Test;

import Service.ActualiteService;
import entite.Actualite;
import util.DataSource;

public class Main {
    public static void main(String[] args) {
        Actualite a1=new Actualite("testttt","descr","type","theme");
        ActualiteService as= new ActualiteService();
        //as.add(a1);
        //as.getAll().forEach(System.out::println);
        //as.delete(37);
        //System.out.println("Actualite selected : " + as.getById(36)); //test getbyid
        /*int idToUpdate = 36;
        Actualite actualiteToUpdate = as.getById(idToUpdate);

        if (actualiteToUpdate != null) {
            actualiteToUpdate.setTitre("malika");
            actualiteToUpdate.setDescription("malika description");
            actualiteToUpdate.setType_pub_cible("malika type de public cible");
            actualiteToUpdate.setTheme("malika thème");

            as.update(actualiteToUpdate, idToUpdate);
            System.out.println("Actualité mise à jour avec succès !");
        } else {
            System.out.println("Aucune actualité trouvée avec l'identifiant " + idToUpdate);
        }*/
    }
}
