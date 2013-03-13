/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tp1.inf2015;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

/**
 *
 * @authors Simon Adriana Claudy
 */
public class TraitementXML {

    private Document document;
    private Element elementRacine;

    public TraitementXML(String cheminFichier) throws ParserConfigurationException, SAXException, IOException {
        analyserDocumentXML(cheminFichier);
    }

    private void analyserDocumentXML(String cheminFichier) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory documentFactory = initialiserDocumentFactory();
        DocumentBuilder parser = documentFactory.newDocumentBuilder();
        document = parser.parse(cheminFichier);

    }

    private DocumentBuilderFactory initialiserDocumentFactory() {
        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        documentFactory.setIgnoringComments(true);
        documentFactory.setCoalescing(true);
        documentFactory.setNamespaceAware(true);
        return documentFactory;
    }

    public void creerFichierXMLSortie(CompteReclamation compteReclamation, List<Reclamation> listeRemboursements, String filePath) throws Exception {
        ajouterTagCompte(compteReclamation);
        ajouterTagRemboursement(listeRemboursements);
        ajouterTagTotalRemboursements(listeRemboursements);
        sauvegardeFichier(filePath);
    }

    public void ajouterTagCompte(CompteReclamation compteReclamation) throws Exception {
        reinitialiserFichierXML();
        elementRacine = document.createElement("remboursements");
        document.appendChild(elementRacine);

        ajouterTagDossier(compteReclamation);
        ajouterTagMois(compteReclamation);
    }

    public void ajouterTagDossier(CompteReclamation compteReclamation) {
        Element dossier = document.createElement("dossier");
        elementRacine.appendChild(dossier);
        dossier.setTextContent(compteReclamation.getDossier());
    }

    public void ajouterTagMois(CompteReclamation compteReclamation) {
        Element mois = document.createElement("mois");
        elementRacine.appendChild(mois);
        Date dateCompte = compteReclamation.getDate();
        mois.setTextContent(dateCompte.getAnnee() + "-" + dateCompte.getMois());
    }

    public void ajouterTagRemboursement(List<Reclamation> listeRemboursements) throws Exception {
        for (Reclamation remboursement : listeRemboursements) {
            Element nodeRemboursement = document.createElement("remboursement");
            elementRacine.appendChild(nodeRemboursement);
            ajouterTagSoin(nodeRemboursement, remboursement);
            ajouterTagDate(nodeRemboursement, remboursement);
            ajouterTagMontant(nodeRemboursement, remboursement);
        }
    }

    public void ajouterTagSoin(Element noeudRemboursement, Reclamation remboursement) {
        Element soin = document.createElement("soin");
        noeudRemboursement.appendChild(soin);
        soin.setTextContent(String.valueOf(remboursement.getSoin()));
    }

    public void ajouterTagDate(Element noeudRemboursement, Reclamation remboursement) {
        Element date = document.createElement("date");
        noeudRemboursement.appendChild(date);
        Date dateReclamation = remboursement.getDate();
        date.setTextContent(dateReclamation.getAnnee() + "-" + dateReclamation.getMois() + "-" + dateReclamation.getJour());
    }

    public void ajouterTagMontant(Element noeudRemboursement, Reclamation remboursement) {
        Element montant = document.createElement("montant");
        noeudRemboursement.appendChild(montant);
        NumberFormat decim = DecimalFormat.getInstance();
        decim.setMinimumFractionDigits(2);
        montant.setTextContent((decim.format(remboursement.getMontant()).replace(',', '.')) + "$");
    }

    public void ajouterTagTotalRemboursements(List<Reclamation> listeRemboursements) {
        Element noeudTotalRemboursements = document.createElement("total");
        elementRacine.appendChild(noeudTotalRemboursements);
        NumberFormat decim = DecimalFormat.getInstance();
        decim.setMinimumFractionDigits(2);
        noeudTotalRemboursements.setTextContent(decim.format(calculerTotalRemboursements(listeRemboursements)).replace(',', '.') + "$");
    }

    public double calculerTotalRemboursements(List<Reclamation> listeRemboursements) {
        double total = 0;
        for (Reclamation remboursement : listeRemboursements) {
            total = total + remboursement.getMontant();
        }
        return total;
    }

    public void ajouterTagInvalide(String cheminFichier, String msg) throws Exception {
        reinitialiserFichierXML();
        elementRacine = document.createElement("remboursements");
        document.appendChild(elementRacine);
        Element message = document.createElement("message");
        elementRacine.appendChild(message);
        message.setTextContent(msg);
        sauvegardeFichier(cheminFichier);
    }

    public void sauvegardeFichier(String cheminFichier) throws Exception {
        Source domSource = new DOMSource(document);
        File xmlFile = new File(cheminFichier);
        Result serializationResult = new StreamResult(xmlFile);
        Transformer xmlTransformer = TransformerFactory.newInstance().newTransformer();
        xmlTransformer.setOutputProperty(OutputKeys.INDENT, "yes");
        xmlTransformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
        xmlTransformer.transform(domSource, serializationResult);
    }

    public void reinitialiserFichierXML() {
        NodeList listeNoeud = document.getChildNodes();
        for (int i = 0; i < listeNoeud.getLength(); ++i) {
            document.removeChild(listeNoeud.item(i));
        }
    }

    public void creerDonneeApresValidation(CompteReclamation compteReclamation, List<Reclamation> reclamations) throws Exception {
        validerDocumentXML();
        creerListeReclamationsApresValidation(reclamations);
        creerCompteReclamationApresValidation(compteReclamation, reclamations);

    }

    private void creerListeReclamationsApresValidation(List<Reclamation> reclamations) throws ExceptionValidation {
        validerElementsListeReclamations(reclamations);
        creerListeReclamations(reclamations);
    }

    private void creerListeReclamations(List<Reclamation> reclamations) {

        for (int i = 0; i < getListeTag("reclamation").size(); ++i) {

            int soinInt = Integer.parseInt(getListeTag("soin").get(i));
            Date dateDate = analyserDate(getListeTag("date").get(i));
            double montantDouble = Double.parseDouble(getListeTag("montant").get(i).substring(0, getListeTag("montant").get(i).length() - 1).replace(',', '.'));
            Reclamation reclamation = new Reclamation(soinInt, dateDate, montantDouble);
            reclamations.add(reclamation);

        }
    }

    public void creerCompteReclamationApresValidation(CompteReclamation compteReclamation, List<Reclamation> reclamations) throws Exception {
        validerElementsCompteReclamation();
        creerCompteReclamation(compteReclamation, reclamations);
    }

    public void creerCompteReclamation(CompteReclamation compteReclamation, List<Reclamation> reclamations) {
        compteReclamation.setListeReclamations(reclamations);
        String dossier = getTag("dossier");
        compteReclamation.setDossier(dossier);
        Date dateDate = analyserDate(getTag("mois"));
        compteReclamation.setDate(dateDate);
    }

    public Date analyserDate(String dateString) {
        String[] dateTab = dateString.split("-");
        Date date = null;
        if (dateTab.length == 2) {
            date = new Date(dateTab[0], dateTab[1]);
        } else {
            date = new Date(dateTab[0], dateTab[1], dateTab[2]);
        }
        return date;

    }

    public void validerDocumentXML() throws ExceptionValidation {
        validerInformationsClient();
        validerInformationsReclamation();
    }

    private void validerInformationsClient() throws ExceptionValidation {
        String tableauChaine[] = {"dossier", "mois"};
        for (String chaine : tableauChaine) {
            try {
                getTag(chaine);
            } catch (NullPointerException e) {
                throw new ExceptionValidation("Il manque l'élément " + chaine + " dans le fichier");
            }
        }
    }

    private void validerInformationsReclamation() throws ExceptionValidation {
        String tableauChaine[] = {"soin", "date", "montant"};
        int nombreReclamations = getListeTag("reclamation").size();
        if (nombreReclamations == 0) {
            throw new ExceptionValidation("Il n'y a pas de réclamation dans le document");
        }

        for (String chaine : tableauChaine) {
            if (getListeTag(chaine).size() != nombreReclamations) {
                throw new ExceptionValidation("Il manque l'élément " + chaine + " dans une réclamation");
            }
        }
    }

    public void validerElementsListeReclamations(List<Reclamation> reclamations) throws ExceptionValidation {
        SimpleRegex regexSoin = new SimpleRegex("0|100|150|175|200|400|500|600|700|(3[0-9][0-9])");
        SimpleRegex regexDate = new SimpleRegex("[0-9]{4}-(0[0-9]|1[0-2])-([0-2][1-9]|3[0-1])");
        SimpleRegex regexMontant = new SimpleRegex("[0-9]*(,||.)[0-9]{2}\\$");
        for (int i = 0; i < getListeTag("reclamation").size(); ++i) {
            if (!regexSoin.matches(getListeTag("soin").get(i))) {
                throw new ExceptionValidation("Le numéro de soin d'une réclamation est invalide");
            }else if(!regexDate.matches(getListeTag("date").get(i))) {
                throw new ExceptionValidation("La date d'une réclamation est invalide");
            }else if(!regexMontant.matches(getListeTag("montant").get(i))) {
                 throw new ExceptionValidation("Le montant d'une réclamation est invalide");
            }
        }
    }

    public void validerElementsCompteReclamation() throws ExceptionValidation {
        SimpleRegex regexDossier = new SimpleRegex("(A|B|C|D|E)[0-9]{6}");
        SimpleRegex regexDate = new SimpleRegex("[0-9]{4}-(0[0-9]|1[0-2])");
        if (!regexDossier.matches(getTag("dossier"))) {
            throw new ExceptionValidation("Le numéro de dossier est invalide");
        }else if(!regexDate.matches(getTag("mois"))) {
            throw new ExceptionValidation("La date de la demande est invalide");
        }

    }

    public List<String> getListeTag(String tag) {
        List<String> liste = new ArrayList();
        NodeList listeNoeud = document.getElementsByTagName(tag);
        for (int i = 0; i < listeNoeud.getLength(); i++) {
            Element tagName = (Element) listeNoeud.item(i);
            liste.add(tagName.getTextContent());
        }
        return liste;
    }

    public String getTag(String tag) {
        NodeList listeNoeud = document.getElementsByTagName(tag);
        Element tagName = (Element) listeNoeud.item(0);
        return tagName.getTextContent();
    }
}
