package u4a3_finalproject;

// Import any needed classes
import java.util.ArrayList;

/**
 * Title: Action.java
 * Programmer: Haran
 * Date: August 20th 2018
 * Description: A class that represents an action on the game board - moving a 
 * tile right, left, and so on - this is where the logic takes place, 
 * essentially. Created to make use of switch case statement.
 */
public class Action {
    /**
     * Define a string variable for the direction the tiles will be moving - 
     * enum is not being used because it hasn't been discussed in the course.
     */
    private String strDirection;
    
    // Variable that will hold a working copy of the game board, for movements
    private Board brdGame;
    
    // ArrayList that will hold the actions taken this round
    private ArrayList<String> strActionLogs = new ArrayList<String>();
    
    /**
     * Check the current tile against the next tile, and determine whether the 
     * current tile can be moved to the next tile or whether they can be merged.
     * @param tileCurrent The current tile that will be moved.
     * @param tileNext The tile that the current tile will be moved onto.
     */
    private void checkAndMerge(Tile tileCurrent, Tile tileNext) {
        // Wrap the whole thing in a try/catch
        /**
         * Check whether the tile that the current one will be moved to, is 
         * blank (getValue == 0 means the tile is blank).
         * Also check whether the current tile is blank or not - I don't want to
         * move a nonexistent tile.
         */
        if (tileNext.getValue() == 0 && tileCurrent.getValue() != 0) {
            /**
             * Shift the current tile - the current value - to the next tile.
             * This is done by setting the next tile value to the current 
             * tile value (this tile is 4, the next tile is blank, so the next 
             * tile becomes 4 and this tile becomes blank).
             */
            tileNext.setValue(tileCurrent.getValue());
            // Set the current tile to be blank - I moved the value up
            tileCurrent.setValue(0);
            
            // Add the relevant entry to the text log
            strActionLogs.add("Shifted " + tileCurrent.intX + ", " + 
                    tileCurrent.intY + " to " + tileNext.intX + ", " + 
                    tileNext.intY + ".");
        } else if (tileNext.getValue() != 0 && tileCurrent.getValue() != 0 ) {
            /**
             * Neither this tile or the next tile are blank - can they merge?
             * They can merge only if their values are the same.
             */
            if (tileNext.getValue() == tileCurrent.getValue()) {
                /**
                 * They matched - merge the tiles.
                 * Add the current tile value onto the next tile value 
                 * (so 2 + 2 = 4, value for the next tile as an example).
                 * As all numbers are multiples of 2, multiplying the current
                 * value by 2 also works.
                 */
                tileNext.setValue(tileCurrent.getValue() + tileNext.getValue());
                // Set the current tile to be blanked - value was shifted
                tileCurrent.setValue(0);
                
                // Add the text log, also saying the new value
                strActionLogs.add("Shifted and merged " + tileCurrent.intX + 
                        ", " + tileCurrent.intY + " to " + tileNext.intX + 
                        ", " + tileNext.intY + " to make " + 
                        tileNext.getValue() + ".");
            }
            
            
        }
    }
    
    /**
     * Shift the board tiles up.
     */
    private void shiftUp() {
        // Iterate through the bottom 3 rows, bottom to top
        for (int x = this.brdGame.tileBoard.length - 1; x > 0; x--) {
            // Loop through columns 
            for (int y = 0; y < this.brdGame.tileBoard[x].length; y++) {
                // Create a variable reference to this tile for ease of use
                Tile tileCurrent = this.brdGame.tileBoard[x][y];
                // and the above tile
                Tile tileAbove = this.brdGame.tileBoard[x - 1][y];
                /** 
                 * Check whether the above tile is blank or not (x-1) and if 
                 * this tile is not blank. 
                 * This will be done by calling the custom method, so less code 
                 * can be written, saving file size.
                 */
                checkAndMerge(tileCurrent, tileAbove);
            }
        }
    }
    
    /**
     * Shift the board tiles down.
     */
    private void shiftDown() {
        // Go through the top 3 rows, and do the same as the above method
        for (int x = 0; x <= this.brdGame.tileBoard.length - 2; x++) {
            // Loop through columns - ERROR TILE ON BOTTOM
            for (int y = 0; y < this.brdGame.tileBoard[x].length; y++) {
                Tile tileCurrent = this.brdGame.tileBoard[x][y];
                Tile tileBelow = this.brdGame.tileBoard[x + 1][y];
                checkAndMerge(tileCurrent, tileBelow);
            }
        }
    }
    
    /**
     * Shift the board tiles left.
     */
    private void shiftLeft() {
        // Go through all rows
        for (int x = 0; x < this.brdGame.tileBoard.length; x++) {
            // Go through the 3 right columns - right to left
            for (int y = this.brdGame.tileBoard[x].length - 1; y > 0; y--) {
                Tile tileCurrent = this.brdGame.tileBoard[x][y];
                Tile tileLeft = this.brdGame.tileBoard[x][y - 1];
                checkAndMerge(tileCurrent, tileLeft);
            }
        }
    }
    
    /**
     * Shift the board tiles right.
     */
    private void shiftRight() {
        // Go through all rows
        for (int x = 0; x < this.brdGame.tileBoard.length; x++) {
            // Go through the 3 left columns - left to right
            for (int y = 0; y < this.brdGame.tileBoard[x].length - 1; y++) {
                Tile tileCurrent = this.brdGame.tileBoard[x][y];
                Tile tileLeft = this.brdGame.tileBoard[x][y + 1];
                checkAndMerge(tileCurrent, tileLeft);
            }
        }
    }
    
    /**
     * Execute the changes to the game board - move the game board in the 
     * direction specified.
     * @return An array of ArrayList of strings, that will hold the action log
     * (what moves have been taken for this round) and the second element being
     * where the game has been won (a tile of 2048 has been achieved), neutral 
     * (neither a win nor loss), or a loss (which is determined by try/catch - 
     * if there is a StackOverflowError, then it's a loss. This is done to make
     * use of try/catch within this program).
     */
    public ArrayList<String> execute() {
        // Begin adding the logs to the ArrayList - first with move direction
        strActionLogs.add("Moved tiles " + strDirection.toLowerCase() + ".");
        
        // Make use of while loop, to do 2 passes of board shifts
        int intLoopCounter = 0;
        while (intLoopCounter < 2) {
        // Switch/case the direction to move the board in
            switch (this.strDirection) {
                case "Up":
                    // Execute the function that will move the board up
                    shiftUp();
                    // Break out of the switch statement so it doesn't fall through
                    break;
                case "Down":
                    shiftDown();
                    break;
                case "Left": 
                    shiftLeft();
                    break;
                case "Right":
                    shiftRight();
                    break;
            }
            // Increment the loop counter
            intLoopCounter++;
        }
         
        // Get a new tile at a random location
        Tile tileNew = this.brdGame.getRandomTile();
        // Check whether the tile is -1 (no more tiles left) or not
        if (tileNew.getValue() == -1) {
            // Add a game over log.
            strActionLogs.add("Game over!");
        } else {
            // Continue as normal
            // Set the value to 2 - spawning a new tile, essentially
            tileNew.setValue(2);
            // Append the log
            strActionLogs.add("Generated tile at " + tileNew.intX + ", " + 
                    tileNew.intY + ".");
        }
        
        
        // Return the ArrayList of logs
        return strActionLogs;
    }
    
    /**
     * Create a new game action.
     * @param brdGame_ The game board, with tiles.
     * @param strDirection_ The direction in which to move tiles.
     */
    public Action(Board brdGame_, String strDirection_) {
        this.brdGame = brdGame_;
        this.strDirection = strDirection_;
    }
}
