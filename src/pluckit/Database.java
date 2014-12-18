/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pluckit;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author MorganWillis
 */
public class Database {
    
    // Hold all of the addresses needed
    String saveAddress;
    String loadAddress;
    String sectionalAddress;
    
    /**
     * returns the save address
     * @return 
     */
    public String getSaveAddress() {
        return saveAddress;
    }
    
    /** 
     * saves the save address
     */
    public void setSaveAddress(String newSave) {
        saveAddress = newSave;
    }
    
    /**
     * Gets the load address
     * @return 
     */
    public String getLoadAddress() {
        return loadAddress;
    }
    
    /**
     * sets the sectional address
     * @param newAddress 
     */
    public void setSectionalAddress(String newAddress) {
        sectionalAddress = newAddress;
    }
    
    /**
     * Gets the sectional address
     * @return 
     */
    public String getSectionalAddress() {
        return sectionalAddress;
    }
    
    /**
     * Sets the load address
     * @param newLoad 
     */
    public void setLoadAddress(String newLoad) {
        loadAddress = newLoad;
    }
    
    /**
     * Loads a sectional from the xml file
     * @return
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException 
     */
    public ArrayList<Sectional> loadSectional() 
            throws ParserConfigurationException, SAXException, IOException {
        
        // Hold all of our sectionals that are read in
        ArrayList<Sectional> newSectional;
        newSectional = new ArrayList<Sectional>();
        
         // Show the user that the file is loading
        System.out.println("Loading file \"" + sectionalAddress + "\"");
        
        // Build the document to hold the the xml file
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        
        // Load the input XML document, parse it and return an instance of the
        // Document class.
        Document document = builder.parse(new File(sectionalAddress));
        
        // Set the date format
        NodeList inNodes = document.getElementsByTagName("Sectional");
        
        // Go through the nodes to pull out the correct informations
        for (int i = 0; i < inNodes.getLength(); i++) {
            
            // Get the current node
            Node node = inNodes.item(i);
            
            // See what the node tpe is
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                
                // Set the node as an element
                Element elem = (Element) node;
                
                // Make sure we have an attribute 
                if(elem.getAttributes() != null) {
                    newSectional.add(createSectional(elem));
                }
            }
        }
        return newSectional;
    }
    
    /**
     * Creates a new sectional
     * @param elem
     * @return 
     */
    private Sectional createSectional(Element elem) {
        
        // Will hold the new sectional
        Sectional newSectional = new Sectional();
        
        // Get the value of the ID attribute.
        newSectional.setName(elem.getAttribute("Name"));
                        
                        
        // Get all the nodes of the described type
        NodeList chordNodes = elem.getElementsByTagName("Chord");
                       
        // Go through each node
        for(int i = 0; i < chordNodes.getLength(); i++) {
             
            // Make the node an element
            Element chordElem = (Element)chordNodes.item(i);
                        
            // Create the new scripture
            Chord newChord = new Chord();
                        
            // Get the book attribute
            newChord.setName(chordElem.getAttribute("Name"));
                            
            newChord.setFingers(chordElem.getTextContent());
            
            newSectional.addChord(newChord);
        }
        
        return newSectional;
    }
}
    
