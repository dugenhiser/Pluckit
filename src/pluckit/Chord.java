/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pluckit;

import java.awt.AWTException;
import java.awt.Desktop;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.docx4j.dml.wordprocessingDrawing.Inline;
import org.docx4j.jaxb.Context;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.exceptions.InvalidFormatException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.BinaryPartAbstractImage;
import org.docx4j.wml.Drawing;
import org.docx4j.wml.ObjectFactory;
import org.docx4j.wml.P;
import org.docx4j.wml.R;
import org.docx4j.wml.Tbl;
import org.docx4j.wml.Tc;
import org.docx4j.wml.Tr;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author MorganWillis
 */
public class PrepArea {
    String Name; // hold the name of the song
    ArrayList<Chord> song; // Hold all the chords in the song
    private static WordprocessingMLPackage wordMLPackage; // allow the user to export
    private static ObjectFactory factory; // used to create objects

    /**
     * Set the name of the song
     * @param newName 
     */
    public void setName(String newName) {
        Name = newName;
    }

    /**
     * Return the name of the song
     * @return 
     */
    public String getName() {
        return Name;
    }

    /**
     * Set the song list
     * @param newSong 
     */
    public void setSong(ArrayList<Chord> newSong) {
        song = newSong;
    }

    /**
     * Return the song list
     * @return 
     */
    public ArrayList<Chord> getSong() {
        return song;
    }

    /**
     * Add a chord to the song
     * @param newChord 
     */
    public void addChord(Chord newChord) {
        song.add(newChord);
    }

    /** 
     * Remove a chord from the song
     * @param i 
     */
    public void removeChord(int i) {
        song.remove(i);
    }

    /**
     * export the song to word to allow for lyric editing and printing
     * @param address
     * @param pic
     * @throws AWTException
     * @throws IOException
     * @throws InvalidFormatException
     * @throws Docx4JException
     * @throws Exception 
     */
    public void exportToWord(String address, ArrayList<WritableImage> pic) 
            throws AWTException, IOException, InvalidFormatException, 
            Docx4JException, Exception {

        // Create the package to allow for exporting
        wordMLPackage = WordprocessingMLPackage.createPackage();
        
        // Create the factory
        factory = Context.getWmlObjectFactory();
        
        // Create the table and its elements
        Tbl table = factory.createTbl();
        Tr tr = factory.createTr();
        Tr trU = factory.createTr();
        
        // Create folder for the pictures to be put in
        boolean success = new File("C:\\PluckIt").mkdir();
        String picAddress = "C:\\PluckIt\\pic.png";
        success = new File("C:\\PluckIt").exists();
        
        System.out.println(pic.size());
        
        // Make sure the folder exists
        if(success) {
            
            // Copy all the pictures and insert them into the word document
            for (int i = 0; i < pic.size(); i++) {
                
                // Create the file for the pictures
                File file = new File(picAddress);
                
                // Create a temp picture too allow for adjusted settings
                WritableImage temp = pic.get(i);
                ImageView newView = new ImageView(temp);
                
                // Adjust the size
                newView.setFitHeight(200);
                newView.setFitWidth(100);
                
                // Save the image
                RenderedImage renderedImage 
                        = SwingFXUtils.fromFXImage(newView
                                .snapshot(null, null), null);
                ImageIO.write(renderedImage, "png", file);
                System.out.println("Image created and saved");

                // Create cell contents
                P paragraphWithImage 
                        = addInlineImageToParagraph(createInlineImage(file));
                P paragraphOfText 
                        = wordMLPackage.getMainDocumentPart().createParagraphOfText("");

                // Add contents to the table
                addTableCell(tr, paragraphWithImage);
                addTableCell(trU, paragraphOfText);

                // Create a new row after we reach the end of the document
                if (i % 5 == 4 || (i + 1) == pic.size()) {
                    table.getContent().add(tr);
                    table.getContent().add(trU);
                    
                    // Reset the rows in order to add more data
                    tr = factory.createTr();
                    trU = factory.createTr();

                }
                
                // Delete current picture to save space 
                file.delete();
            }
        }

        // Create the word document and save it
        wordMLPackage.getMainDocumentPart().addObject(table);
        wordMLPackage.save(new java.io.File(address));
        
        // Open the file for the users use
        Desktop.getDesktop().open(new File(address));
        System.out.println("Successful Save");
    }

    /** 
     * Save a song in an xml format
     * @param address 
     */
    public void saveSong(String address) {
        try {

            // Create the document
            DocumentBuilderFactory docFactory 
                    = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // create the root element
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("song");
            doc.appendChild(rootElement);

            // Create all the entries
            for (Chord song1 : song) {
                // Create the entry element
                Element chord = doc.createElement("Chord");
                rootElement.appendChild(chord);
                // set the date attribute tofor the entry element
                Attr attr = doc.createAttribute("Name");
                attr.setValue(song1.getName());
                chord.setAttributeNode(attr);
                chord.setTextContent(song1.getFingers());
            }

            // write the content into xml file
            TransformerFactory transformerFactory 
                    = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result
                    = new StreamResult(new File(address));

            // Give the xml an indent for easier reading
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer
                    .setOutputProperty("{http://xml.apache.org/xslt}indent-amount"
                            , "2");

            transformer.transform(source, result);

            // Let user know the file has been saved
            System.out.println("File saved!");

        } catch (ParserConfigurationException | TransformerException pce) {
            pce.printStackTrace();
        }
    }

    /**
     * load a song from an xml format
     * @param address
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException 
     */
    public void loadSong(String address) throws ParserConfigurationException, 
            SAXException, IOException {

        song = new ArrayList<Chord>();

        // Show the user that the file is loading
        System.out.println("Loading Song \"" + address + "\"");

        // Build the document to hold the the xml file
        DocumentBuilderFactory Docfactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = Docfactory.newDocumentBuilder();

        // Load the input XML document, parse it and return an instance of the
        // Document class.
        Document document = builder.parse(new File(address));

        // Set the date format
        NodeList inNodes = document.getElementsByTagName("song");

        // Go through the nodes to pull out the correct informations
        for (int i = 0; i < inNodes.getLength(); i++) {

            // Get the current node
            Node node = inNodes.item(i);

            // See what the node tpe is
            if (node.getNodeType() == Node.ELEMENT_NODE) {

                // Set the node as an element
                Element elem = (Element) node;

                // Make sure we have an attribute 
                if (elem.getAttributes() != null) {
                    newSongChord(elem);
                }
            }
        }
    }

    /**
     * Create a new chord for the song.
     * @param elem 
     */
    private void newSongChord(Element elem) {

        // Get all the nodes of the described type
        NodeList chordNodes = elem.getElementsByTagName("Chord");

        // Go through each node
        for (int i = 0; i < chordNodes.getLength(); i++) {

            // Make the node an element
            Element chordElem = (Element) chordNodes.item(i);

            // Create the new scripture
            Chord newChord = new Chord();

            // Get the book attribute
            newChord.setName(chordElem.getAttribute("Name"));

            newChord.setFingers(chordElem.getTextContent());

            addChord(newChord);

        }

    }

    /**
     * Display the song to the user
     */
    public void display() {
        for (Chord song1 : song) {
            System.out.println(song1.display());
        }
    }

    /**
     * The following code was adapted from the docx4j library.
     */
    
    /**
     * Adds a table cell to the given row with the given paragraph as content.
     *
     * @param tr
     * @param paragraph
     */
    private void addTableCell(Tr tr, P paragraph) {
        Tc tc1 = factory.createTc();
        tc1.getContent().add(paragraph);
        tr.getContent().add(tc1);
    }

    /**
     * Adds the in-line image to a new paragraph and then returns the paragraph.
     * This method has not changed from the previous example.
     *
     * @param inline
     * @return
     */
    private static P addInlineImageToParagraph(Inline inline) {
        // Now add the in-line image to a paragraph
        ObjectFactory newfactory = new ObjectFactory();

        P paragraph = newfactory.createP();

        R run = newfactory.createR();

        paragraph.getContent().add(run);

        Drawing drawing = newfactory.createDrawing();
        
        run.getContent().add(drawing);

        drawing.getAnchorOrInline().add(inline);

        
        return paragraph;
    }

    /**
     * Creates an in-line image of the given file. As in the previous example,
     * we convert the file to a byte array, and then create an inline image
     * object of it.
     *
     * @param file
     * @return
     * @throws Exception
     */
    private static Inline createInlineImage(File file) throws Exception {
        byte[] bytes = convertImageToByteArray(file);
        
        BinaryPartAbstractImage imagePart = BinaryPartAbstractImage.createImagePart(wordMLPackage, bytes);
        
        int docPrId = 1;
        int cNvPrId = 2;

        return imagePart.createImageInline("Filename hint",
                "Alternative text", docPrId, cNvPrId, false);
        
    }

    /**
     * Convert the image from the file into an array of bytes.
     *
     * @param file
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    private static byte[] convertImageToByteArray(File file)
            throws FileNotFoundException, IOException {
        InputStream is = new FileInputStream(file);
        long length = file.length();
        // You cannot create an array using a long, it needs to be an int.
        if (length > Integer.MAX_VALUE) {
            System.out.println("File too large!!");
        }
        byte[] bytes = new byte[(int) length];
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }
        // Ensure all the bytes have been read
        if (offset < bytes.length) {
            System.out.println("Could not completely read file " + file.getName());
        }
        is.close();
        return bytes;
    }
}


