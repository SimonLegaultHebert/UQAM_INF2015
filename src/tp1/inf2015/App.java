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
 * @authors Simon
 *          Adriana
 *          Claudy
 */
public class App {

    
    public static void main(String[] args) throws SAXException, ParserConfigurationException, IOException, Exception 
    {
        ReclamationsDocument reclamationsDocument = new ReclamationsDocument("C:\\Users\\Leg\\Desktop\\INF2015\\test.xml");
        
        Traitement traitement = new Traitement();
  
        
        List<Reclamation> listReclamations = new ArrayList<Reclamation>();
                
            
            try
            {
               
                reclamationsDocument.validateReclationsInformationsInXMLFile();
                reclamationsDocument.creationOfReclamationsListAfterValidation(listReclamations);
                CompteReclamation compteReclamation = new CompteReclamation();
                reclamationsDocument.creationOfCompteReclamationAfterValidation(compteReclamation, listReclamations);
                List<Reclamation> remboursements = new ArrayList<Reclamation>();
                traitement.TraitementSoin(compteReclamation, remboursements);
                reclamationsDocument.addTagRemboursement(compteReclamation, remboursements, "C:\\Users\\Leg\\Desktop\\INF2015\\Sortie.xml");                      
            }
            catch(Exception e)
            {
                reclamationsDocument.addTagInvalide("C:\\Users\\Leg\\Desktop\\INF2015\\Sortie.xml", e.getMessage());
            }
                
            
        
    
    }                                                                                                                                                                                                                                                                                                         
}

