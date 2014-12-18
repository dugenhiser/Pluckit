/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pluckit;

import java.util.ArrayList;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;

/**
 *
 * @author Douglas
 */
public class TestTab {
    
    // Holds all the chords
    FlowPane tab;
    ArrayList<WritableImage> images; // Holds all the images in the chord
    
    /** 
     * returns the flow plane
     * @return 
     */
    public FlowPane getTab(){
        tab = new FlowPane();
        tab.setTranslateX(10);
        tab.setTranslateY(10);
        return tab;
    }
    
    /**
     * Adds a new chord to the display
     * @param chord 
     */
    public void getChild(int chord[]){
        
        // Make sure we have a list of images
        if (images == null)
            images = new ArrayList<WritableImage>();
        
        // Create the image view to take a picture of the tab when it is created
        ImageView view = new ImageView(new Image("/tab.png"));
        
        // Create the stack pane with everything positioned correctly
        StackPane newPane = new StackPane(view,getChord(-100,chord[0]),
                getChord(-60,chord[1]),getChord(-20,chord[2]),
                getChord(20,chord[3]),getChord(60,chord[4]),
                getChord(100,chord[5]));  
        
        // Add the pane to the display
        tab.getChildren().addAll(newPane);
        tab.setHgap(20);
        tab.setVgap(20);
        
        // Add the picture of the tab to the list of images
        images.add(newPane.snapshot(null, null));

    }
    
    /**
     * Creates a chord image
     * @param posX
     * @param posY
     * @return 
     */    
    public ImageView getChord(int posX, int posY){
      
        // Will hold the image that is created
        ImageView image = new ImageView(getNote());
        
        int pos = 0;
        
        // Put the fingering in the correct place
        switch(posY){
            case 0: image = new ImageView();
                break;
            case 1: pos = -180;
                 break;
            case 2: pos = -140;
                 break;
            case 3: pos = -100;
                 break;
            case 4: pos = -60;
                 break;
            case 5: pos = -20;
                 break;
            case 6: pos = 20;    
                 break;
                
                
        }
        
        // Position everything correctly
        image.setTranslateX(posX);
        image.setTranslateY(pos);
                
        // Return the image
        return image;
        
    }
    
    /**
     * gets the correct image for a note
     * @return 
     */
    public Image getNote(){
        Image note = new Image("/circle.png");
        
        return note;
    }
    
    /**
     * gets the number of images in the list
     * @return 
     */
    public int getImageCount() {
        return images.size();
    }
            
    /**
     * gets all the images in the list
     * @return 
     */
    public ArrayList<WritableImage> getImages()
    {
        return images;
    }
}
