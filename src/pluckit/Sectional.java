/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pluckit;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author MorganWillis
 */
public class Sectional {
    
    // Holds the name of the sectional
    String Name;
    
    // Holds the list of all the chords in the sectional
    ArrayList<Chord> chords = new ArrayList<Chord>();
    
    /**
     * Sets the list of chords
     * @param newChords 
     */
    public void setChords(ArrayList<Chord> newChords) {
        chords = newChords;
    }
    
    /**
     * returns the list of chords
     * @return 
     */
    public ArrayList<Chord> getChords() {
        return chords;
    }
    
    /**
     * add a new chord to the list
     * @param newChord 
     */
    public void addChord(Chord newChord) {
        chords.add(newChord);
    }
    
    /** 
     * set the name of the sectional
     * @param newName 
     */
    public void setName(String newName) {
        Name = newName;
    }
    
    /**
     * get the name of the sectional
     * @return 
     */
    public String getName() {
        return Name;
    }
    
    /**
     * display the name of the sectional
     */
    public void display() {
        
        // Print the name...
        System.out.println(Name);
        
        // ... followed by all the chords
        for(int i = 0; i < chords.size(); i++) {
            System.out.println("\t" + chords.get(i).display());
        }
    }
    
    /** 
     * Create a new chord to insert into the list
     * @param Name
     * @param Fingering 
     */
    public void createNewChord(String Name, String Fingering) {
        
        // Create the chord
        Chord newChord = new Chord();
        newChord.setName(Name);
        newChord.setFingers(Fingering);
        
        // Add the chord into the list
        addChord(newChord);
    }
}
