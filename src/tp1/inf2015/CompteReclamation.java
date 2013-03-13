/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tp1.inf2015;

import java.util.List;

/**
 *
 * @authors Simon Adriana Claudy
 */
public class CompteReclamation {

    private String dossier;
    private Date date;
    private List<Reclamation> listeReclamations;

    public CompteReclamation() {
    }

    public CompteReclamation(String dossier, Date date, List<Reclamation> listeReclamations) {

        this.dossier = dossier;
        this.date = date;
        this.listeReclamations = listeReclamations;
    }

    public String getDossier() {
        return dossier;
    }

    public void setDossier(String dossier) {
        this.dossier = dossier;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<Reclamation> getlisteReclamations() {
        return listeReclamations;
    }

    public void setListeReclamations(List<Reclamation> listeReclamations) {
        this.listeReclamations = listeReclamations;
    }

    public char getContrat() {
        return dossier.charAt(0);
    }

    public int getClient() {
        return Integer.parseInt(dossier.substring(1, 6));
    }
}
