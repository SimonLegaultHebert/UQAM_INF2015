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
    
    public boolean createReclamationsList(List<Reclamation> reclamations)
    {
        SimpleRegex regexSoin = new SimpleRegex("0|100|200|400|500|600|700|(3[0-9][0-9])");
        SimpleRegex regexDate = new SimpleRegex("[0-9]{4}-(0[0-9]|1[0-2])-([0-2][1-9]|3[0-1])");
        SimpleRegex regexMontant = new SimpleRegex("[0-9]*.[0-9]{2}\\$");
        List<String> soin = getTagList("soin");
        List<String> date = getTagList("date");
        List<String> montant = getTagList("montant");
        
        
            int size = soin.size();
            for(int i = 0; i < size; ++i)
            {
                if(regexSoin.matches(soin.get(i)) && regexDate.matches(date.get(i)) && regexMontant.matches(montant.get(i)))
                {
                    int soinInt = Integer.parseInt(soin.get(i));
                    Date dateDate = parsingDate(date.get(i));
                    double montantDouble = Double.parseDouble(montant.get(i).substring(0, montant.get(i).length() - 1));
                    Reclamation reclamation = new Reclamation(soinInt, dateDate, montantDouble);
                    reclamations.add(reclamation);
                }
                else
                {
                    return false; //si un champ n'est pas valide
                }
            }
       return true; 
    }
    
    public boolean createCompteReclamation(CompteReclamation reclamation, List<Reclamation> reclamations)
    {
        reclamation.setReclamationList(reclamations);
        SimpleRegex regexClient = new SimpleRegex("[0-9]{6}");
        SimpleRegex regexContrat = new SimpleRegex("A|B|C|D");
        SimpleRegex regexDate = new SimpleRegex("[0-9]{4}-(0[0-9]|1[0-2])");
        String client = getTag("client");
        String contrat = getTag("contrat");
        String date = getTag("mois");
        
        if(regexClient.matches(client) && regexContrat.matches(contrat) && regexDate.matches(date))
        {
            int clientInt = Integer.parseInt(client);
            char contratChar = contrat.charAt(0);
            Date dateDate = parsingDate(date);
            reclamation.setClient(clientInt);
            reclamation.setContrat(contratChar);
            reclamation.setDate(dateDate);
        }                                                            
        else
        {
            return false; //si le champ n'est pas valide
        }
        return true;
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
            montant.setTextContent((decim.format(remboursement.getMontant())) + "$");
            
        }
        saveToFile(filePath);
    }
    
    public void addTagInvalide(String filePath) throws Exception
    {
        clean();
        Element rootElement = document.createElement("remboursements");
        document.appendChild(rootElement);
        
        Element message = document.createElement("message");
        rootElement.appendChild(message);
        message.setTextContent("Données invalides");
        
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
