/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tp1.inf2015;

import java.text.DecimalFormat;
import java.util.List;

/**
 *
 * @author Leg
 */
public class Traitement 
{
    public static final double VINGT_CINQ_POUR_CENT = 0.25;
    public static final double ZERO_POUR_CENT = 0;
    public static final double CINQUANTE_POUR_CENT = 0.5;
    public static final double QUARANTE_POUR_CENT= 0.4;
    public static final double CENT_POUR_CENT = 1;
    public static final double SOIXANTE_DIX_POUR_CENT = 0.7;
    public static final double QUATRE_VINGT_DIX_POUR_CENT = 0.9;
    
    public Traitement() 
    {
    }
    
    public void TraitementSoin(CompteReclamation leCompteReclamation, List <Reclamation> remboursementList)
    {   
        char choixContrat = leCompteReclamation.getContrat();
        switch(choixContrat)
        {                  
            case 'A':
                traitementA(leCompteReclamation,remboursementList);
                break;
            case 'B':
                traitementB(leCompteReclamation,remboursementList);
                break;
            case 'C':
                traitementC(leCompteReclamation,remboursementList);
                break;
            case 'D':
                traitementD(leCompteReclamation,remboursementList);
                break;
                        
        }
    }
    
    public void traitementA(CompteReclamation leCompteReclamation, List <Reclamation> remboursementList)
    {
       List<Reclamation> maListeReclamation = leCompteReclamation.getReclamationList();
       double montantRemboursement;
    
       for (int i = 0; i < maListeReclamation.size(); ++i)
       {
            Date dateC = leCompteReclamation.getDate();
            Date dateR = maListeReclamation.get(i).getDate();
            
            if(dateC.getAnnee().equals(dateR.getAnnee()) && dateC.getMois().equals(dateR.getMois()))
            {    
                double monMontant = maListeReclamation.get(i).getMontant();
                int choixSoin = maListeReclamation.get(i).getSoin();
          
                if(choixSoin == 0 || choixSoin == 100 || choixSoin == 200 || choixSoin == 500)
                {
                    montantRemboursement = monMontant * VINGT_CINQ_POUR_CENT;
                } 
                else if ((choixSoin >= 300 && choixSoin <= 400) || choixSoin == 700)
                {
                    montantRemboursement = 0;
                }  
                else 
                {
                    montantRemboursement = monMontant * QUARANTE_POUR_CENT;
                } 
                Reclamation leRemboursement = new Reclamation(maListeReclamation.get(i).getSoin(), maListeReclamation.get(i).getDate(), montantRemboursement);
                remboursementList.add(leRemboursement);
            }    
       }
               
    }
    
    public void traitementB(CompteReclamation leCompteReclamation, List <Reclamation> remboursementList)
    {
    
       List<Reclamation> maListeReclamation = leCompteReclamation.getReclamationList();  
       double montantRemboursement;
       
       for (int i = 0; i < maListeReclamation.size(); ++i)
       {
            Date dateC = leCompteReclamation.getDate();
            Date dateR = maListeReclamation.get(i).getDate();
            
         if(dateC.getAnnee().equals(dateR.getAnnee()) && dateC.getMois().equals(dateR.getMois()))
         {
            double monMontant = maListeReclamation.get(i).getMontant();
            int choixSoin = maListeReclamation.get(i).getSoin();            
            
            if(choixSoin == 0)
            {
                montantRemboursement = monMontant * CINQUANTE_POUR_CENT;
                if(montantRemboursement > 40)
                {
                    montantRemboursement = 40;
                }   
            } 
            else if(choixSoin == 100 || choixSoin == 500)
            {
                montantRemboursement = monMontant * CINQUANTE_POUR_CENT;
                if(montantRemboursement > 50)
                {
                    montantRemboursement = 50;
                } 
            } 
            else if(choixSoin == 200)
            {
                montantRemboursement = monMontant * CENT_POUR_CENT;
                if(montantRemboursement > 70)
                {
                    montantRemboursement = 70;
                }         
            } 
            else if (choixSoin >= 300 && choixSoin <= 399)
            {
                montantRemboursement = monMontant * CINQUANTE_POUR_CENT;
            } 
            else if (choixSoin == 400)
            {
                montantRemboursement = 0;
            } 
            else if (choixSoin == 600)
            {
                montantRemboursement = monMontant * CENT_POUR_CENT;
            } 
            else 
            {
                montantRemboursement = monMontant * SOIXANTE_DIX_POUR_CENT;
            }
            
            Reclamation leRemboursement = new Reclamation(maListeReclamation.get(i).getSoin(), maListeReclamation.get(i).getDate(), montantRemboursement);
            remboursementList.add(leRemboursement);
         }
       }   
               
    }
    
    public void traitementC(CompteReclamation leCompteReclamation, List <Reclamation> remboursementList)
    {
       List<Reclamation> maListeReclamation = leCompteReclamation.getReclamationList();
       double montantRemboursement;
       
       for (int i = 0; i < maListeReclamation.size(); ++i)
       {
            Date dateC = leCompteReclamation.getDate();
            Date dateR = maListeReclamation.get(i).getDate();
            
            if(dateC.getAnnee().equals(dateR.getAnnee()) && dateC.getMois().equals(dateR.getMois()))
            {
                double monMontant = maListeReclamation.get(i).getMontant();
                montantRemboursement = monMontant * QUATRE_VINGT_DIX_POUR_CENT;
            
                Reclamation leRemboursement = new Reclamation(maListeReclamation.get(i).getSoin(), maListeReclamation.get(i).getDate(), montantRemboursement);
                remboursementList.add(leRemboursement);
            }    
       }
               
    }
    
    public void traitementD(CompteReclamation leCompteReclamation, List <Reclamation> remboursementList)
    {
       List<Reclamation> maListeReclamation = leCompteReclamation.getReclamationList();
       double montantRemboursement;
       
       for (int i = 0; i < maListeReclamation.size(); ++i)
       {
            Date dateC = leCompteReclamation.getDate();
            Date dateR = maListeReclamation.get(i).getDate();
            
          if(dateC.getAnnee().equals(dateR.getAnnee()) && dateC.getMois().equals(dateR.getMois()))
          {
           
            double monMontant = maListeReclamation.get(i).getMontant();
            int choixSoin = maListeReclamation.get(i).getSoin();            
            
            if(choixSoin == 0)
            {
                montantRemboursement = monMontant * CENT_POUR_CENT;
                if(montantRemboursement > 85)
                {
                    montantRemboursement = 85;
                }           
                    
            } 
            else if(choixSoin == 100 || choixSoin == 500)
            {
                montantRemboursement = monMontant * CENT_POUR_CENT;
                if(montantRemboursement > 75)
                {
                    montantRemboursement = 75;
                }
                                      
            } 
            else if(choixSoin == 200 || choixSoin == 600)
            {
                montantRemboursement = monMontant * CENT_POUR_CENT;
                if(montantRemboursement > 100)
                {
                    montantRemboursement = 100;
                }
                
            } 
            else if (choixSoin >= 300 && choixSoin <= 399)
            {
                montantRemboursement = monMontant * CENT_POUR_CENT;
            } 
            else if (choixSoin == 400)
            {
                montantRemboursement = monMontant * CENT_POUR_CENT;
                if(montantRemboursement > 65)
                {
                    montantRemboursement = 65;
                }
                    
            }  
            else 
            {
                montantRemboursement = monMontant * SOIXANTE_DIX_POUR_CENT;
                if(montantRemboursement > 90)
                {
                    montantRemboursement = 90;
                }
                    
            }
            
            Reclamation leRemboursement = new Reclamation(maListeReclamation.get(i).getSoin(), maListeReclamation.get(i).getDate(), montantRemboursement);
            remboursementList.add(leRemboursement);
         }
       }
               
    }
    
    
}
