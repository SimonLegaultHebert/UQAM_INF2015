/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tp1.inf2015;

import javax.xml.parsers.*; 
import org.w3c.dom.*; 
import org.xml.sax.*; 
import java.io.*; 
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.omg.CosNaming.NamingContextPackage.NotEmpty;

/**
 *
 * @author Leg
 */
public class App {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SAXException, ParserConfigurationException, IOException 
    {
        
        ReclamationsDocument reclamationsDocument = new ReclamationsDocument("C:\\Users\\Leg\\Desktop\\INF2015\\test.xml");
        boolean validation;
        
        List<Reclamation> listReclamations = new ArrayList<Reclamation>();
        validation = reclamationsDocument.createReclamationsList(listReclamations);
        
        if(validation)
        {
            CompteReclamation compteReclamation = new CompteReclamation();
            validation = reclamationsDocument.createCompteReclamation(compteReclamation, listReclamations);
            
            if(validation)
            {
                //TRAITEMENT DES DONNÉES
                //CRÉATION DU NOUVEAU FICHIER XML
                
                /*
                System.out.println(compteReclamation.getClient());
                System.out.println(compteReclamation.getContrat());
                System.out.println(compteReclamation.getMois() + "\n");
                for (Reclamation reclamation : listReclamations) 
                {
                   System.out.println(reclamation.getSoin());
                   System.out.println(reclamation.getDate().toString());
                   System.out.println(reclamation.getMontant() + "\n");               
                }
                */
            }
            else
            {
                System.out.println("INVALIDE");
                //UTILISATION DE LA MÉTHODE POUR CRÉER LE FICHIER INVALIDE
            }
        }
        else
        {
            System.out.println("INVALIDE");
            //UTILISATION DE LA MÉTHODE POUR CRÉER LE FICHIER INVALIDE
        }
    
    }                                                                                                                                                                                                                                                                                                         
}

