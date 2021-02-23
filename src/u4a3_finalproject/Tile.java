package u4a3_finalproject;

// Import classes for referencing within this class
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * Title: Tile.java
 * Programmer: Haran
 * Date: August 20th 2018
 * Description: A class to represent a game tile for 2048.
 */
public class Tile {
    // Int to represent what number is on the tile - default 2
    private int intValue = 2;
    
    // Reference to what JLabel this tile corresponds with
    public JLabel lblTile;
    
    // Private variable for what icon is attached to this tile
    private ImageIcon iconTile;
    
    // Integers to represent the tile position, X and Y
    public int intX, intY;
    
    /**
     * Define an internal function that will, based on the current tile, 
     * determine what icon will go along with that value and set the relevant 
     * variable and label icon to the correct icon.
     */
    private void setIcon() {
        // Check whether the value is within the valid range
        if (this.intValue >= 2 && this.intValue <= 2048) {
            /**
             * The icon can be determined by using the value name directly.
             * (No need to convert to string with Integer.toString - 
             * concatenating it like this implicitly casts the integer to a 
             * string)
             */
            this.iconTile = new ImageIcon("img/tile_" + this.intValue + ".png");
        } else {
            // It's a blank tile if it isn't within range (such as value == 0)
            this.iconTile = new ImageIcon("img/blank.png");
        }
        
        // Set the JLabel icon
        this.lblTile.setIcon(this.iconTile);
    }
    
    /**
     * A getter function to retrieve the tile value.
     * @return The tile value.
     */
    public int getValue() {
        // Return the value for this tile
        return this.intValue;
    }
    
    /**
     * Set the tile value to the provided parameter.
     * From that, the icon will automatically be set.
     * @param intValue_ The tile value, between 2 to 2048 (inclusive) for valid
     * tiles that count towards score, or 0 if it is a blank tile.
     */
    public void setValue(int intValue_) {
        // Set the instance value to the specified value
        this.intValue = intValue_;
        
        // Determine and set the icon based on the new value
        this.setIcon();
    }
    
    /**
     * Create a new instance of tile object.
     * @param intValue_ The value of the tile (2, 4, 8, 16, ...).
     * @param lblTile_  The JLabel attached to this tile.
     * @param intX_ The X coordinate of the tile.
     * @param intY_ The Y coordinate of the tile.
     */
    public Tile(int intValue_, JLabel lblTile_, int intX_, int intY_) {
        // Assign the parameters to the object properties
        this.intValue = intValue_;
        this.lblTile = lblTile_;
        this.intX = intX_;
        this.intY = intY_;
        
        // Determine and set the label icon
        this.setIcon();
    }
    
    /**
     * Default class constructor, with no arguments.
     * If this is created, a tile will be created with the value -1. This is to
     * represent that it is a tile without a label, and is used by Board.java to
     * signify there are no tiles left.
     */
    public Tile() {
        this.intValue = -1;
    }
}
