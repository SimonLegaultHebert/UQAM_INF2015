/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tp1.inf2015;

import java.util.List;

/**
 *
 * @authors Adriana
 * 
*/
public class CalculRemboursements {

    private static final double VINGT_CINQ_POUR_CENT = 0.25;
    private static final double CINQUANTE_POUR_CENT = 0.5;
    private static final double QUARANTE_POUR_CENT = 0.4;
    private static final double CENT_POUR_CENT = 1;
    private static final double SOIXANTE_DIX_POUR_CENT = 0.7;
    private static final double QUATRE_VINGT_DIX_POUR_CENT = 0.9;
    private static final double TRENTE_CINQ_POUR_CENT = 0.35;
    private static final double QUINZE_POUR_CENT = 0.15;
    private static final double DOUZE_POUR_CENT = 0.12;
    private static final double SOIXANTE_POUR_CENT = 0.60;
    private static final double VINGT_DEUX_POUR_CENT = 0.22;
    private static final double TRENTE_POUR_CENT = 0.30;
    private static final double QUATRE_VINGT_CINQ_POUR_CENT = 0.85;
    private static final double SOIXANTE_QUINZE_POUR_CENT = 0.75;
    private static final double QUATRE_VINGT_QUINZE_POUR_CENT = 0.95;

    /*Toutes les méthodes de sous calcul sont utilisées pour les soins qui ont un montant 
     * de remboursement maximum selon leur contrat. 
     */
    public CalculRemboursements() {
    }

    public void traiterSelonSoin(CompteReclamation compteReclamation, List<Reclamation> listeRemboursements) {
        for (Reclamation reclamation : compteReclamation.getlisteReclamations()) {
            Date dateDossier = compteReclamation.getDate();
            Date dateReclamation = reclamation.getDate();

            if (dateDossier.getAnnee().equals(dateReclamation.getAnnee()) && dateDossier.getMois().equals(dateReclamation.getMois())) {
                double montant = reclamation.getMontant();
                int choixSoin = reclamation.getSoin();
                montant = choisirCalculTraitement(compteReclamation, choixSoin, montant);
                Reclamation remboursement = new Reclamation(reclamation.getSoin(), reclamation.getDate(), montant);
                listeRemboursements.add(remboursement);
            }
        }

    }

    private double choisirCalculTraitement(CompteReclamation compteReclamation, int choixSoin, double montant) {
        double montantRemboursement = 0;
        switch (compteReclamation.getContrat()) {
            case 'A': montantRemboursement = calculerTraitementA(choixSoin, montant);
                break;
            case 'B': montantRemboursement = calculerTraitementB(choixSoin, montant);
                break;
            case 'C': montantRemboursement = calculerTraitementC(choixSoin, montant);
                break;
            case 'D': montantRemboursement = calculerTraitementD(choixSoin, montant);
                break;
            default: montantRemboursement = calculerTraitementE(choixSoin, montant);
        }
        return montantRemboursement;
    }

    private double calculerTraitementA(int choixSoin, double montant) {
        double montantRemboursement = 0;
        if (choixSoin == 0 || choixSoin == 200 || choixSoin == 500) {
            montantRemboursement = montant * VINGT_CINQ_POUR_CENT;
        } else if (choixSoin == 100) {
            montantRemboursement = montant * TRENTE_CINQ_POUR_CENT;
        } else if (choixSoin == 175) {
            montantRemboursement = montant * CINQUANTE_POUR_CENT;
        } else if (choixSoin == 600) {
            montantRemboursement = montant * QUARANTE_POUR_CENT;
        }
        return montantRemboursement;
    }

    private double calculerTraitementB(int choixSoin, double montant) {
        double montantRemboursement = 0;
        if (choixSoin == 0 || choixSoin == 100 || (choixSoin >= 300 && choixSoin <= 399) || choixSoin == 500) {
            montantRemboursement = montant * CINQUANTE_POUR_CENT;
            montantRemboursement = sousCalculTraitementB(choixSoin, montantRemboursement);
        } else if (choixSoin == 175) {
            montantRemboursement = montant * SOIXANTE_QUINZE_POUR_CENT;
        } else if (choixSoin == 200 || choixSoin == 600) {
            montantRemboursement = montant * CENT_POUR_CENT;
        } else if (choixSoin == 700) {
            montantRemboursement = montant * SOIXANTE_DIX_POUR_CENT;
        }
        return montantRemboursement;
    }

    private double sousCalculTraitementB(int choixSoin, double montantRemboursement) {
        if (choixSoin == 0 && montantRemboursement > 40) {
            montantRemboursement = 40;
        } else if ((choixSoin == 100 || choixSoin == 500) && montantRemboursement > 50) {
            montantRemboursement = 50;
        }
        return montantRemboursement;
    }

    private double calculerTraitementC(int choixSoin, double montant) {
        double montantRemboursement = 0;
        if (choixSoin == 150) {
            montantRemboursement = montant * QUATRE_VINGT_CINQ_POUR_CENT;
        } else if ((choixSoin == 600)) {
            montantRemboursement = montant * SOIXANTE_QUINZE_POUR_CENT;
        } else if (choixSoin == 100) {
            montantRemboursement = montant * QUATRE_VINGT_QUINZE_POUR_CENT;
        } else {
            montantRemboursement = montant * QUATRE_VINGT_DIX_POUR_CENT;
        }
        return montantRemboursement;
    }

    private double calculerTraitementD(int choixSoin, double montant) {

        double montantRemboursement = 0;
        if (choixSoin == 0 || choixSoin == 100 || choixSoin == 150 || choixSoin == 500 || choixSoin == 200 || choixSoin == 600
                || (choixSoin >= 300 && choixSoin <= 400) || choixSoin == 700) {
            montantRemboursement = montant * CENT_POUR_CENT;
            montantRemboursement = sousCalculTraitementD(choixSoin, montantRemboursement);

        } else {
            montantRemboursement = montant * QUATRE_VINGT_QUINZE_POUR_CENT;
        }

        return montantRemboursement;
    }

    private double sousCalculTraitementD(int choixSoin, double montantRemboursement) {
        if (choixSoin == 0 && montantRemboursement > 85) {
            montantRemboursement = 85;
        } else if (choixSoin == 100 && montantRemboursement > 75) {
            montantRemboursement = 75;
        } else if (choixSoin == 150 && montantRemboursement > 150) {
            montantRemboursement = 150;
        } else if ((choixSoin == 200 || choixSoin == 600) && montantRemboursement > 100) {
            montantRemboursement = 100;
        } else if (choixSoin == 400 && montantRemboursement > 65) {
            montantRemboursement = 65;
        } else if (choixSoin == 700 && montantRemboursement > 90) {
            montantRemboursement = 90;
        }
        return montantRemboursement;
    }

    private double calculerTraitementE(int choixSoin, double montant) {
        double montantRemboursement = 0;
        if (choixSoin == 0 || choixSoin == 600 || choixSoin == 150) {
            montantRemboursement = montant * QUINZE_POUR_CENT;
        } else if (choixSoin == 100 || choixSoin == 400 || choixSoin == 175 || choixSoin == 500) {
            montantRemboursement = sousCalculTraitementE(choixSoin, montant);
        } else if (choixSoin == 200) {
            montantRemboursement = montant * DOUZE_POUR_CENT;
        } else if (choixSoin >= 300 && choixSoin <= 399) {
            montantRemboursement = montant * SOIXANTE_POUR_CENT;
        } else {
            montantRemboursement = montant * VINGT_DEUX_POUR_CENT;
        }
        return montantRemboursement;
    }

    private double sousCalculTraitementE(int choixSoin, double montantRemboursement) {
        if (choixSoin == 175 || choixSoin == 400 || choixSoin == 100) {
            montantRemboursement = montantRemboursement * VINGT_CINQ_POUR_CENT;
            if (choixSoin == 175 && montantRemboursement > 20) {
                montantRemboursement = 20;
            } else if (choixSoin == 400 && montantRemboursement > 15) {
                montantRemboursement = 15;
            }
        } else {
            montantRemboursement = montantRemboursement * TRENTE_POUR_CENT;
            if (choixSoin == 500 && montantRemboursement > 20) {
                montantRemboursement = 20;
            }
        }
        return montantRemboursement;
    }
}