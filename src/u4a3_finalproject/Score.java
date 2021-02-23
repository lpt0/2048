package u4a3_finalproject;

/**
 * Title: Score.java
 * Programmer: Haran
 * Date: August 21st 2018
 * Description: An object that will represent a user score, under the HighScores
 * frame.
 */
public class Score {
    // Create variables for name, score, and date
    private String strName;
    private int intScore;
    private String strScoreDate;
    private String strMoves;
    
    /**
     * Get the player's score value for this score object.
     * @return The player score, as an integer.
     */
    public int getScore() {
        return this.intScore;
    }
    
    public String toFormattedString() {
        // Define a variable that will hold the return value, and be appended to
        String strReturn = "";
        
        // Format the string how it should appear on the score list
        strReturn += this.strName + "\n";
        strReturn += "Score: " + Integer.toString(intScore) + "\n";
        strReturn += "Date: " + strScoreDate + "\n";
        
        return strReturn;
    }
    
    /**
     * Create a new score object.
     * @param strName_ The name of the player who achieved this score.
     * @param strScore The score, as a string
     * @param strScoreDate_ The date of which this score was achieved
     * @param strMoves_ The amount of moves for the game, as a string.
     */
    public Score(String strName_, String strScore, String strScoreDate_, 
            String strMoves_) {
        // Set the object properties with the provided parameters
        this.strName = strName_;
        // Convert the string score to an integer
        this.intScore = Integer.parseInt(strScore);
        /**
         * The date is being stored as a string because either way, it will need
         * to be converted to a string.
         */
        this.strScoreDate = strScoreDate_;
        this.strMoves = strMoves_;
    }
}
