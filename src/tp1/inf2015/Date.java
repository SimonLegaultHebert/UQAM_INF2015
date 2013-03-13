package tp1.inf2015;

/**
 *
 * @authors Simon
 *
 */
public class Date {

    private String Annee;
    private String Mois;
    private String Jour;

    public Date(String Annee, String Mois, String Jour) {
        this.Annee = Annee;
        this.Mois = Mois;
        this.Jour = Jour;
    }

    public Date(String Annee, String Mois) {
        this.Annee = Annee;
        this.Mois = Mois;
    }

    public String getAnnee() {
        return Annee;
    }

    public String getJour() {
        return Jour;
    }

    public String getMois() {
        return Mois;
    }

    public void setAnnee(String Annee) {
        this.Annee = Annee;
    }

    public void setJour(String Jour) {
        this.Jour = Jour;
    }

    public void setMois(String Mois) {
        this.Mois = Mois;
    }
}
