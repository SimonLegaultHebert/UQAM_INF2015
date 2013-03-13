/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tp1.inf2015;

import javax.xml.parsers.*;
import org.xml.sax.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @authors Simon Adriana Claudy
 */
public class App {

    public static void main(String[] args) throws SAXException, ParserConfigurationException, IOException, Exception {
        TraitementXML traitementXML = new TraitementXML(args[0]);
        CalculRemboursements calculRemboursement = new CalculRemboursements();
       
        List<Reclamation> listeReclamations = new ArrayList<Reclamation>();
        CompteReclamation compteReclamation = new CompteReclamation();
        try {
            traitementXML.creerDonneeApresValidation(compteReclamation, listeReclamations);
            List<Reclamation> remboursements = new ArrayList<Reclamation>();
            calculRemboursement.traiterSelonSoin(compteReclamation, remboursements);
            traitementXML.creerFichierXMLSortie(compteReclamation, remboursements, args[1]);
        } catch (Exception e) {
            traitementXML.ajouterTagInvalide(args[1], e.getMessage());
        }

    }
}
