package u4a3_finalproject;

// Import JLabel for use within this class
import javax.swing.JLabel;

/**
 * Title: Board.java
 * Programmer: Haran
 * Date: August 20th 2018
 * Description: A class to represent a game board of 2048, methods, properties 
 * and all.
 */
public class Board {
    // Define an array of arrays of Tile objects for the game tiles
    public Tile[][] tileBoard = new Tile[4][4];
    
    /**
     * Define a function that will calculate the score of the game board.
     * This is done by summing the values of all the tiles.
     */
    public int getScore() {
        // First, set the score to 0 - clearing the score
        int intScore = 0;
        // Loop through the board's rows
        for (int x = 0; x < tileBoard.length; x++) {
            // Loop through the columns - y
            for (int y = 0; y < tileBoard[x].length; y++) {
                /* Get the value for the tile at this position, add it onto the
                score
                */
                intScore += tileBoard[x][y].getValue();
            }
        }
        return intScore;
    }
    
    /**
     * A recursive function that can get a random, blank tile.
     * Throws StackOverflowError if there are no tiles left. 
     * @return A blank tile. Returns a tile without value -1 if no tiles are 
     * left.
     */
    public Tile getRandomTile() {
        /**
         * Assign 2 random numbers between 0 and 3 (length of row/column) to 
         * variables. This will be x and y coordinates for a tile.
         */
        int intRandom1 = (int) (Math.random() * 4);
        int intRandom2 = (int) (Math.random() * 4);
        
        // Try/catch 
        try {
            // Check whether the tile at the position is blank (0) or not
            if (this.tileBoard[intRandom1][intRandom2].getValue() == 0) {
                // Return the tile at this position
                return this.tileBoard[intRandom1][intRandom2];
            } else {
                // Recurse until a tile is found
                return getRandomTile();
            }
        } catch (StackOverflowError error) {
            // There are no more tiles left, return a tile with -1.
            return new Tile();
        }
    }
    
    /**
     * Determine whether the game has been won or not.
     * In other words, linear search and find whether there is a tile with the 
     * value 2048.
     * (No binary search because the array is not sorted).
     * @return A boolean value - true if the game is won, false if it isn't.
     */
    public boolean isGameWon() {
        // Linear search through the 2D array to see if any value is 2048
        for (int x = 0; x < this.tileBoard.length; x++) {
            for (int y = 0; y < this.tileBoard[x].length; y++) {
                // Does the tile at this coordinate have value 2048?
                if (tileBoard[x][y].getValue() == 2048) {
                    // Return true - game is won
                    return true;
                }
            }
        }
        // Return false - game is not won
        return false;
    }
    
    /**
     * Create a new instance of the Board class, for the game board.
     * @param lblTiles A 2D array of JLabels, for the 2048 game tiles.
     */
    public Board(JLabel[][] lblTiles) {
        // Iterate through the 2D tile board array
        for (int x = 0; x < tileBoard.length; x++) {
            for (int y = 0; y < tileBoard[x].length; y++) {
                /**
                 * Create a blank tile at this position, with the relevant label
                 * and coordinate (x, y) values starting at 1 instead of 0.
                 */
                this.tileBoard[x][y] = new Tile(0, lblTiles[x][y], x+1 , y+1);
            }
        }
        
        // Pick 2 random tiles to begin with and set their value to 2
        getRandomTile().setValue(2);
        getRandomTile().setValue(2);
    }
}
