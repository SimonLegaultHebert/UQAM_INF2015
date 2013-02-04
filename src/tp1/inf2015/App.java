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
    public static void main(String[] args) throws SAXException, ParserConfigurationException, IOException, Exception 
    {
        ReclamationsDocument reclamationsDocument = new ReclamationsDocument("xml/test.xml");
        
        Traitement traitement = new Traitement();
        boolean validation;
        
        List<Reclamation> listReclamations = new ArrayList<Reclamation>();
        validation = reclamationsDocument.createReclamationsList(listReclamations);
        
        if(validation)
        {
            CompteReclamation compteReclamation = new CompteReclamation();
            validation = reclamationsDocument.createCompteReclamation(compteReclamation, listReclamations);
            
            if(validation)
            {
                List<Reclamation> remboursements = new ArrayList<Reclamation>();
                traitement.TraitementSoin(compteReclamation, remboursements);
                reclamationsDocument.addTagRemboursement(compteReclamation, remboursements, "xml/NEW.xml");                      
            }
            else
            {
                reclamationsDocument.addTagInvalide("xml/NEW.xml");
            }
        }
        else
        {
            reclamationsDocument.addTagInvalide("xml/NEW.xml");
        }
    
    }                                                                                                                                                                                                                                                                                                         
}

