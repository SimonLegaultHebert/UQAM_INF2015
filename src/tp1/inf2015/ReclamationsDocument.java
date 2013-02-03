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
import java.util.ArrayList;
import java.util.Date;
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
 * @author Leg
 */
public class ReclamationsDocument
{
    private Document document;
    private Document document2;
    
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
                    Date dateDate = new Date(date.get(i).replace('-', '/'));
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
            Date dateDate = new Date(date.replace('-', '/') + "/01"); //jour bidon pour utiliser le constructeur
            reclamation.setClient(clientInt);
            reclamation.setContrat(contratChar);
            reclamation.setMois(dateDate);
        }                                                            
        else
        {
            return false;
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
    
     
    public void addTagRemboursement(CompteReclamation compteReclamation, List<Reclamation> listRemboursements) 
    {
        clean();
        Element rootElement = document.createElement("remboursements");
        document.appendChild(rootElement);
                 
        Element client = document.createElement("client");
        rootElement.appendChild(client);
        client.setTextContent(String.valueOf(compteReclamation.getClient()));
                    
        Element mois = document.createElement("mois");
        rootElement.appendChild(mois);
        mois.setTextContent(String.valueOf(compteReclamation.getMois()));   
        
        for(Reclamation remboursement : listRemboursements)
        {
            Element nodeRemboursement = document.createElement("remboursement");
            rootElement.appendChild(nodeRemboursement);
            
            Element soin = document.createElement("soin");
            nodeRemboursement.appendChild(soin);
            soin.setTextContent(String.valueOf(remboursement.getSoin()));
            
            Element date = document.createElement("mois");
            nodeRemboursement.appendChild(date);
            date.setTextContent(String.valueOf(remboursement.getDate()));
            
            Element montant = document.createElement("montant");
            nodeRemboursement.appendChild(montant);
            montant.setTextContent(String.valueOf(remboursement.getMontant()));
            
        }
    }
    
    public void saveToFile(String filePath) throws Exception 
    {
        Source domSource = new DOMSource(document);
        File xmlFile = new File(filePath);
        Result serializationResult = new StreamResult(xmlFile);
        Transformer xmlTransformer = TransformerFactory.newInstance().newTransformer();
        xmlTransformer.setOutputProperty(OutputKeys.INDENT, "yes");
        xmlTransformer.transform(domSource, serializationResult);
    }    
}
