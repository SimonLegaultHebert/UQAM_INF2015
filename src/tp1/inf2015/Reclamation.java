package tp1.inf2015;

/**
 *
 * @authors Simon Adriana Claudy
 */
public class Reclamation {

    private int soin;
    private Date date;
    private double montant;

    public Reclamation(int soin, Date date, double montant) {
        this.soin = soin;
        this.date = date;
        this.montant = montant;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public int getSoin() {
        return soin;
    }

    public void setSoin(int soin) {
        this.soin = soin;
    }
}
