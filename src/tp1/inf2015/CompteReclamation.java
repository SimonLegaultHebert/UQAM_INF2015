/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tp1.inf2015;

import java.util.Date;
import java.util.List;

/**
 *
 * @author Leg
 */
public class CompteReclamation 
{
    private int client;
    private char contrat;
    private Date mois;
    List<Reclamation> reclamationList;
    
    public CompteReclamation() 
    {
        
    }
    
    public CompteReclamation(int client, char contrat, Date mois, List<Reclamation> reclamationList) 
    {
        this.client = client;
        this.contrat = contrat;
        this.mois = mois;
        this.reclamationList = reclamationList;
    }

    public int getClient() 
    {
        return client;
    }

    public void setClient(int client) 
    {
        this.client = client;
    }

    public char getContrat() 
    {
        return contrat;
    }

    public void setContrat(char contrat) 
    {
        this.contrat = contrat;
    }

    public Date getMois() 
    {
        return mois;
    }

    public void setMois(Date mois) 
    {
        this.mois = mois;
    }

    public List<Reclamation> getReclamationList() 
    {
        return reclamationList;
    }

    public void setReclamationList(List<Reclamation> reclamationList) 
    {
        this.reclamationList = reclamationList;
    }
    
    
    
    
}
