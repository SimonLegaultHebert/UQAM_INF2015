/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tp1.inf2015;

import com.sun.org.apache.xerces.internal.dom.CoreDocumentImpl;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.Node;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import sun.java2d.pipe.SpanShapeRenderer;

/**
 *
 * @authors Simon
 *          Adriana
 *          Claudy
 */

public class ReclamationsDocument
{
    private Document document;
    
    public ReclamationsDocument(String documentFilePath)throws ParserConfigurationException, SAXException, IOException
    {
        parseXmlDocument(documentFilePath);
    }

    private void parseXmlDocument(String documentFilePath)throws ParserConfigurationException, SAXException, IOException 
    {
        DocumentBuilderFactory documentFactory = initializeDocumentFactory();
        DocumentBuilder parser = documentFactory.newDocumentBuilder();
        document = parser.parse(documentFilePath);
        
    }

    private DocumentBuilderFactory initializeDocumentFactory() 
    {
        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        documentFactory.setIgnoringComments(true);
        documentFactory.setCoalescing(true);
        documentFactory.setNamespaceAware(true);
        return documentFactory;
    }
    
    public List<String> getTagList(String tag) 
    {
        List<String> list = new ArrayList();
        NodeList nodeList = document.getElementsByTagName(tag);
        for (int i = 0; i < nodeList.getLength(); i++)
        {
            Element tagName = (Element) nodeList.item(i);
            list.add(tagName.getTextContent());         
        }
        return list;
    }
    
    public String getTag(String tag)
    {
        NodeList nodeList = document.getElementsByTagName(tag);
        Element tagName = (Element) nodeList.item(0);
        return tagName.getTextContent();
    }
    
    public Date parsingDate(String dateString)
    {
        String[] dateTab = dateString.split("-");
        Date date = null;
        if(dateTab.length == 2)
        {
            date = new Date(dateTab[0], dateTab[1]);
        }
        else
        {
            date = new Date(dateTab[0], dateTab[1], dateTab[2]);
        }
        
        return date;
    
    }
    public void validateClientInformationsInXMLFile() throws ValidationException
    {
        String tableauChaine[] = {"client", "contrat", "mois"};
        for(String chaine : tableauChaine)
        {
            try
            {
                getTag(chaine);
            }
            catch(NullPointerException e)
            {
                throw new ValidationException("Il manque l'élément " + chaine + " dans le fichier");
            }
        }     
    }
    
    public void validateReclationsInformationsInXMLFile() throws ValidationException
    {
            String tableauChaine[] = {"soin", "date", "montant"};
            int numberOfReclamations = getTagList("reclamation").size();
            if(numberOfReclamations == 0)
            {
            throw new ValidationException("Il n'y a pas de reclamation dans le document");
            }
            
            for(String chaine : tableauChaine)
            {
                if(getTagList(chaine).size() != numberOfReclamations)
                {
                    throw new ValidationException("Il manque l'element " + chaine + " dans une reclamation");
                }
            }
    }
    
    public void creationOfReclamationsListAfterValidation(List<Reclamation> reclamations) throws ValidationException
    {
        validateReclamationsListElements(reclamations);
        createReclamationsList(reclamations);
    }
    
    
    public void createReclamationsList(List<Reclamation> reclamations)
    {
            
            for(int i = 0; i < getTagList("reclamation").size(); ++i)
            {
                
                    int soinInt = Integer.parseInt(getTagList("soin").get(i));
                    Date dateDate = parsingDate(getTagList("date").get(i));
                    double montantDouble = Double.parseDouble(getTagList("montant").get(i).substring(0, getTagList("montant").get(i).length() - 1).replace(',', '.'));
                    Reclamation reclamation = new Reclamation(soinInt, dateDate, montantDouble);
                    reclamations.add(reclamation);
                
            }             
    }
    
    public void validateReclamationsListElements(List<Reclamation> reclamations) throws ValidationException
    {
        SimpleRegex regexSoin = new SimpleRegex("0|100|200|400|500|600|700|(3[0-9][0-9])");
        SimpleRegex regexDate = new SimpleRegex("[0-9]{4}-(0[0-9]|1[0-2])-([0-2][1-9]|3[0-1])");
        SimpleRegex regexMontant = new SimpleRegex("[0-9]*(,||.)[0-9]{2}\\$");
        
            
            
            for(int i = 0; i < getTagList("reclamation").size(); ++i)
            {
                if(!regexSoin.matches(getTagList("soin").get(i)))
                {
                    throw new ValidationException("Le numéro de soin est invalide");
                }
                if(!regexDate.matches(getTagList("date").get(i)))
                {
                    throw new ValidationException("La date est invalide");
                }
                if(!regexMontant.matches(getTagList("montant").get(i)))
                {
                    throw new ValidationException("Le montant est invalide");
                }
            
            }
    
    }
    
    
    
    
    
    public void creationOfCompteReclamationAfterValidation(CompteReclamation reclamation, List<Reclamation> reclamations) throws Exception
    {      
        validateCompteReclamationElements();
        createCompteReclamation(reclamation, reclamations);   
    }
    
    
    
    public void createCompteReclamation(CompteReclamation reclamation, List<Reclamation> reclamations)
    {
            reclamation.setReclamationList(reclamations);          
            int clientInt = Integer.parseInt(getTag("client"));
            char contratChar = getTag("contrat").charAt(0);
            Date dateDate = parsingDate(getTag("mois"));
            reclamation.setClient(clientInt);
            reclamation.setContrat(contratChar);
            reclamation.setDate(dateDate);              
    }
    
    public void validateCompteReclamationElements() throws ValidationException
    {
        SimpleRegex regexClient = new SimpleRegex("[0-9]{6}");
        SimpleRegex regexContrat = new SimpleRegex("A|B|C|D");
        SimpleRegex regexDate = new SimpleRegex("[0-9]{4}-(0[0-9]|1[0-2])");
       
        if(!regexClient.matches(getTag("client")))
        {
         throw new ValidationException("Le numéro de Client est invalide");
        }
        
        if(!regexContrat.matches(getTag("contrat")))
        {
         throw new ValidationException("Le contrat est invalide");
        }
        
        if(!regexDate.matches(getTag("mois")))
        {
         throw new ValidationException("La date est invalide");
        }
        
    }
    
    public void clean()
    {
        NodeList nodeList = document.getChildNodes(); 
        for(int i = 0; i < nodeList.getLength(); ++i)
        {
            document.removeChild(nodeList.item(i));
        }      
    }
    
     
    public void addTagRemboursement(CompteReclamation compteReclamation, List<Reclamation> listRemboursements, String filePath) throws Exception 
    {
        clean();
        Element rootElement = document.createElement("remboursements");
        document.appendChild(rootElement);
                 
        Element client = document.createElement("client");
        rootElement.appendChild(client);
        client.setTextContent(String.valueOf(compteReclamation.getClient()));
                    
        Element mois = document.createElement("mois");
        rootElement.appendChild(mois);
        Date dateC = compteReclamation.getDate();
        mois.setTextContent(dateC.getAnnee() + "-" + dateC.getMois());   
        
        for(Reclamation remboursement : listRemboursements)
        {
            Element nodeRemboursement = document.createElement("remboursement");
            rootElement.appendChild(nodeRemboursement);
            
            Element soin = document.createElement("soin");
            nodeRemboursement.appendChild(soin);
            soin.setTextContent(String.valueOf(remboursement.getSoin()));
            
            Element date = document.createElement("date");
            nodeRemboursement.appendChild(date);
            Date dateR = remboursement.getDate();
            date.setTextContent(dateR.getAnnee() + "-" + dateR.getMois() + "-" + dateR.getJour());
            
            Element montant = document.createElement("montant");
            nodeRemboursement.appendChild(montant);
            NumberFormat decim = DecimalFormat.getInstance();
            decim.setMinimumFractionDigits(2);
            montant.setTextContent((decim.format(remboursement.getMontant()).replace(',', '.')) + "$");
            
        }
        saveToFile(filePath);
    }
    
    public void addTagInvalide(String filePath, String msg) throws Exception
    {
        clean();
        Element rootElement = document.createElement("remboursements");
        document.appendChild(rootElement);
        
        Element message = document.createElement("message");
        rootElement.appendChild(message);
        message.setTextContent(msg);
        
        saveToFile(filePath);
    }
    
    public void saveToFile(String filePath) throws Exception 
    {
        Source domSource = new DOMSource(document);
        File xmlFile = new File(filePath);
        Result serializationResult = new StreamResult(xmlFile);
        Transformer xmlTransformer = TransformerFactory.newInstance().newTransformer();
        xmlTransformer.setOutputProperty(OutputKeys.INDENT, "yes");
        xmlTransformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
        xmlTransformer.transform(domSource, serializationResult);
    }    
}
