/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pluckit;

/**
 *
 * @author MorganWillis
 */
class GuitarTab {
    
    Pluck tabPluck; // Holds the plucking arrangement for the song
    Chord tabChord; // Holds the chord for the tab
    
    /**
     * Returns the chord for the tab
     * @return 
     */
    public Chord getChord() {
        return tabChord;
    }
    
    /**
     * Sets the chords for the tab
     * @param newChord 
     */
    public void setChord(Chord newChord) {
        tabChord = newChord;
    }
    
    /**
     * Gets the plucking arangment for the tab
     * @return 
     */
    public Pluck getPluck() {
        return tabPluck;
    }
    
    /**
     * Set the plucking arrangment for the tab
     * @param newPluck 
     */
    public void setPluck(Pluck newPluck) {
        tabPluck = newPluck;
    }
    
    // Create a blank GuitarTab
    public GuitarTab createBlank() {
    
        return null;
    }
    
}
