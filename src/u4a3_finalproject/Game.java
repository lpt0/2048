package u4a3_finalproject;

// Import ArrayList for utilisation
import java.util.ArrayList;
// JOptionPane for popup dialog
import javax.swing.JOptionPane;
// Date for getting the date
import java.util.Date;
// IO package for file writing
import java.io.*;
// Import the JDOM package so XML files can be worked with
import org.jdom.*;
// Import SAXBuilder for another way to work with XML files, with JDOM this time
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

/**
 * Title: Game.java
 * Programmer: Haran
 * Date: August 20th 2018
 * Description: The JFrame game portion of the 2048 application.
 */
public class Game extends javax.swing.JFrame {
    
    // Create a blank reference for the game board
    private Board brdGame;
    
    // Define a variable for the move counter
    int intMoveCounter = 1;
    
    /**
     * Creates new form Game
     */
    public Game() {
        initComponents();
        
        // First, create a 2D array with references to tile JLabels
        javax.swing.JLabel[][] lblTiles = {
            {this.lblTile1, this.lblTile2, this.lblTile3, this.lblTile4},
            {this.lblTile5, this.lblTile6, this.lblTile7, this.lblTile8},
            {this.lblTile9, this.lblTile10, this.lblTile11, this.lblTile12},
            {this.lblTile13, this.lblTile14, this.lblTile15, this.lblTile16}
        };
        
        // Instantiate a new instance of the board class with the tiles
        brdGame = new Board(lblTiles);
    }
    
    /**
     * Method that will create and execute a game action, and output the logs.
     * Will also check whether the game has been won, and execute those 
     * instructions as necessary.
     * Written to cut down the amount of copy pasted code.
     * @param strDirection The direction of board movement.
     */
    private void executeAction(String strDirection) {
        /**
         * Create and execute a new action with the direction, assign the return
         * value to a variable.
         */
        ArrayList<String> strLogs = new Action(brdGame, strDirection).execute();
        
        // Start the new block with the move number (implicit int-string cast)
        this.txtActionLog.append("Move " + intMoveCounter + ":\n");
        
        // Iterate through the ArrayList (element 1) and output those logs
        for (int i = 0; i < strLogs.size(); i++) {
            // Append the log text area with the data
            this.txtActionLog.append(strLogs.get(i) + "\n");
            // Check whether the log contains Game Over
            if (strLogs.get(i).equals("Game over!")) {
                // Call the game end function with false, for loss
                doGameEnd(false);
            }
        }
        
        // Append an extra new line, to give each round a space between logs
        this.txtActionLog.append("\n");
        
        // Update the game score
        this.txtScore.setText(Integer.toString(brdGame.getScore()));
        
        // Update the moves text field
        this.txtMoves.setText(Integer.toString(intMoveCounter));
        
        // Check if the game has been won (there is a tile with 2048?)
        if (brdGame.isGameWon()) {
            /**
             * Call the method that will do game end procedures, with true to 
             * signify a win.
             */
            doGameEnd(true);
        }
        
        // Increment the move counter
        intMoveCounter++;
    }
    
    /**
     * Perform the game end operations, such as saving the high score and file
     * and asking the user for their name to add to the high score file.
     * @param isGameWon Whether the game has been won or not. True for win, 
     * false for loss.
     */
    private void doGameEnd(boolean isGameWon) {
        // Initialize a variable for the message to be displayed to user
        String strMessage;
        // and a variable for the player's name
        String strName;
        
        // Disable the movement buttons
        this.btnUp.setEnabled(false);
        this.btnDown.setEnabled(false);
        this.btnLeft.setEnabled(false);
        this.btnRight.setEnabled(false);
        
        // Is the game a win?
        if (isGameWon) {
            // Set the output string to congratulate the winner (user)
            strMessage = "Congratulations, you win! ";
        } else {
            // It's a loss, tell the user
            strMessage = "You have lost. ";
        }
        // Append a message to tell the user to enter their name for the score
        strMessage += "Please enter your name to be displayed on the score list"
                + ".";
        
        // Spawn a JOptionPane that will take the player name as input
        strName = JOptionPane.showInputDialog(this, strMessage);
        // Check whether the name is blank to give a default name
        if (strName.equals("")) {
            // Set the name to a default name, Player
            strName = "Player";
        }
        
        // Create the score root element
        Element elemScoreRoot = new Element("Score");
        
        // Create elements for name, score, date, and number of moves.
        Element elemName = new Element("Name");
        Element elemScore = new Element("Score");
        Element elemDate = new Element("Date");
        Element elemMoves = new Element("Moves");
        
        // Set the values for each elements
        elemName.setText(strName);
        elemScore.setText(Integer.toString(brdGame.getScore()));
        /**
         * For Date, use the Date object from System class, and toString that 
         * date
         */
        elemDate.setText(new Date().toString());
        elemMoves.setText(Integer.toString(intMoveCounter));
        
        // Add those elements to the root score element
        elemScoreRoot.addContent(elemName);
        elemScoreRoot.addContent(elemScore);
        elemScoreRoot.addContent(elemDate);
        elemScoreRoot.addContent(elemMoves);
        
        // Initialize a blank document 
        Document docHighScores;
        // Try/catch to attempt to read the highscores file if it exists
        try {
            /**
             * Create a SAX builder that will build the XML file.
             * Assign that to a temporary document holder.
             * Looking over the documentation, this looks to be the right way to 
             * build a XML document with JDOM instead of W3C.
             */
            Document docTemp = new SAXBuilder().build("highscores.xml");
            // Check whether this document has the valid root element
            if (docTemp.hasRootElement()) {
                // Assign this document to the docHighScores variable to append
                docHighScores = docTemp;
            } else {
                // Create a new root element
                Element elemRoot = new Element("HighScores");
                // Create a new Document with that root element
                docHighScores = new Document(elemRoot);
            }
        } catch (JDOMException | IOException error) {
            // The multi-catch recommended by NetBeans has been used 
            // Output the error
            System.err.println(error);
            
            // Create a new root element
            Element elemRoot = new Element("HighScores");
            // Create a new Document with that root element anyways
            docHighScores = new Document(elemRoot);
        }
        
        // Add the score element to the root element of the doc
        docHighScores.getRootElement().addContent(elemScoreRoot);
        
        /**
         * Dump the high scores document to the file - no worries of overwrite 
         * since the whole old file is in memory, it's just been appended to (in
         * memory).
         */
        try {
            // Create the output stream
            FileOutputStream outFile = new FileOutputStream("highscores.xml");
            // Create the XMLOutputter, as done in the JDOMExample assignment
            XMLOutputter xmlOut = new XMLOutputter();
            // Output the XML data into the file
            xmlOut.output(docHighScores, outFile);
            
            // Flush and close the file
            outFile.flush();
            outFile.close();
        } catch (IOException error) {
            /**
             * Only catch IOException because FileNotFoundException inherits 
             * IOException - as such, only IOException needs to be caught, the
             * rest will also be caught here just by defining the superclass as
             * a catch
             */
            // Output the error, if there is one (unlikely, if not impossible)
            System.err.println(error);
        }
        // Display the high score frame
        new HighScores().setVisible(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblTitle = new javax.swing.JLabel();
        pnlBoardHolder = new javax.swing.JPanel();
        lblTile1 = new javax.swing.JLabel();
        lblTile4 = new javax.swing.JLabel();
        lblTile2 = new javax.swing.JLabel();
        lblTile3 = new javax.swing.JLabel();
        lblTile5 = new javax.swing.JLabel();
        lblTile6 = new javax.swing.JLabel();
        lblTile7 = new javax.swing.JLabel();
        lblTile8 = new javax.swing.JLabel();
        lblTile9 = new javax.swing.JLabel();
        lblTile10 = new javax.swing.JLabel();
        lblTile11 = new javax.swing.JLabel();
        lblTile12 = new javax.swing.JLabel();
        lblTile16 = new javax.swing.JLabel();
        lblTile14 = new javax.swing.JLabel();
        lblTile13 = new javax.swing.JLabel();
        lblTile15 = new javax.swing.JLabel();
        btnUp = new javax.swing.JButton();
        btnLeft = new javax.swing.JButton();
        btnRight = new javax.swing.JButton();
        btnDown = new javax.swing.JButton();
        lblScore = new javax.swing.JLabel();
        txtScore = new javax.swing.JTextField();
        pnlLogHolder = new javax.swing.JPanel();
        lblActionLog = new javax.swing.JLabel();
        pnlHolder = new javax.swing.JScrollPane();
        txtActionLog = new javax.swing.JTextArea();
        txtMoves = new javax.swing.JTextField();
        lblMove = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        lblTitle.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitle.setText("2048");

        pnlBoardHolder.setBackground(new java.awt.Color(204, 204, 204));

        lblTile1.setBackground(new java.awt.Color(255, 255, 255));
        lblTile1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        lblTile1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTile1.setIcon(new javax.swing.ImageIcon("C:\\Users\\Haran\\Documents\\NetBeansProjects\\ICS4U\\2048\\img\\blank.png")); // NOI18N

        lblTile4.setBackground(new java.awt.Color(255, 255, 255));
        lblTile4.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        lblTile4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTile4.setIcon(new javax.swing.ImageIcon("C:\\Users\\Haran\\Documents\\NetBeansProjects\\ICS4U\\2048\\img\\blank.png")); // NOI18N

        lblTile2.setBackground(new java.awt.Color(255, 255, 255));
        lblTile2.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        lblTile2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTile2.setIcon(new javax.swing.ImageIcon("C:\\Users\\Haran\\Documents\\NetBeansProjects\\ICS4U\\2048\\img\\blank.png")); // NOI18N

        lblTile3.setBackground(new java.awt.Color(255, 255, 255));
        lblTile3.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        lblTile3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTile3.setIcon(new javax.swing.ImageIcon("C:\\Users\\Haran\\Documents\\NetBeansProjects\\ICS4U\\2048\\img\\blank.png")); // NOI18N

        lblTile5.setBackground(new java.awt.Color(255, 255, 255));
        lblTile5.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        lblTile5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTile5.setIcon(new javax.swing.ImageIcon("C:\\Users\\Haran\\Documents\\NetBeansProjects\\ICS4U\\2048\\img\\blank.png")); // NOI18N

        lblTile6.setBackground(new java.awt.Color(255, 255, 255));
        lblTile6.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        lblTile6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTile6.setIcon(new javax.swing.ImageIcon("C:\\Users\\Haran\\Documents\\NetBeansProjects\\ICS4U\\2048\\img\\blank.png")); // NOI18N

        lblTile7.setBackground(new java.awt.Color(255, 255, 255));
        lblTile7.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        lblTile7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTile7.setIcon(new javax.swing.ImageIcon("C:\\Users\\Haran\\Documents\\NetBeansProjects\\ICS4U\\2048\\img\\blank.png")); // NOI18N

        lblTile8.setBackground(new java.awt.Color(255, 255, 255));
        lblTile8.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        lblTile8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTile8.setIcon(new javax.swing.ImageIcon("C:\\Users\\Haran\\Documents\\NetBeansProjects\\ICS4U\\2048\\img\\blank.png")); // NOI18N

        lblTile9.setBackground(new java.awt.Color(255, 255, 255));
        lblTile9.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        lblTile9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTile9.setIcon(new javax.swing.ImageIcon("C:\\Users\\Haran\\Documents\\NetBeansProjects\\ICS4U\\2048\\img\\blank.png")); // NOI18N

        lblTile10.setBackground(new java.awt.Color(255, 255, 255));
        lblTile10.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        lblTile10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTile10.setIcon(new javax.swing.ImageIcon("C:\\Users\\Haran\\Documents\\NetBeansProjects\\ICS4U\\2048\\img\\blank.png")); // NOI18N

        lblTile11.setBackground(new java.awt.Color(255, 255, 255));
        lblTile11.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        lblTile11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTile11.setIcon(new javax.swing.ImageIcon("C:\\Users\\Haran\\Documents\\NetBeansProjects\\ICS4U\\2048\\img\\blank.png")); // NOI18N

        lblTile12.setBackground(new java.awt.Color(255, 255, 255));
        lblTile12.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        lblTile12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTile12.setIcon(new javax.swing.ImageIcon("C:\\Users\\Haran\\Documents\\NetBeansProjects\\ICS4U\\2048\\img\\blank.png")); // NOI18N

        lblTile16.setBackground(new java.awt.Color(255, 255, 255));
        lblTile16.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        lblTile16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTile16.setIcon(new javax.swing.ImageIcon("C:\\Users\\Haran\\Documents\\NetBeansProjects\\ICS4U\\2048\\img\\blank.png")); // NOI18N

        lblTile14.setBackground(new java.awt.Color(255, 255, 255));
        lblTile14.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        lblTile14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTile14.setIcon(new javax.swing.ImageIcon("C:\\Users\\Haran\\Documents\\NetBeansProjects\\ICS4U\\2048\\img\\blank.png")); // NOI18N

        lblTile13.setBackground(new java.awt.Color(255, 255, 255));
        lblTile13.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        lblTile13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTile13.setIcon(new javax.swing.ImageIcon("C:\\Users\\Haran\\Documents\\NetBeansProjects\\ICS4U\\2048\\img\\blank.png")); // NOI18N

        lblTile15.setBackground(new java.awt.Color(255, 255, 255));
        lblTile15.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        lblTile15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTile15.setIcon(new javax.swing.ImageIcon("C:\\Users\\Haran\\Documents\\NetBeansProjects\\ICS4U\\2048\\img\\blank.png")); // NOI18N

        javax.swing.GroupLayout pnlBoardHolderLayout = new javax.swing.GroupLayout(pnlBoardHolder);
        pnlBoardHolder.setLayout(pnlBoardHolderLayout);
        pnlBoardHolderLayout.setHorizontalGroup(
            pnlBoardHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBoardHolderLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlBoardHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlBoardHolderLayout.createSequentialGroup()
                        .addComponent(lblTile1, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblTile2, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblTile3, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblTile4, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlBoardHolderLayout.createSequentialGroup()
                        .addComponent(lblTile5, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblTile6, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblTile7, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblTile8, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlBoardHolderLayout.createSequentialGroup()
                        .addComponent(lblTile9, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblTile10, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblTile11, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblTile12, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlBoardHolderLayout.createSequentialGroup()
                        .addComponent(lblTile13, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblTile14, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblTile15, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblTile16, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlBoardHolderLayout.setVerticalGroup(
            pnlBoardHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBoardHolderLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlBoardHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTile3, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTile2, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTile4, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTile1, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlBoardHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTile7, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTile6, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTile8, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTile5, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlBoardHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTile11, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTile10, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTile12, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTile9, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlBoardHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTile15, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTile14, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTile16, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTile13, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnUp.setText("Up");
        btnUp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpActionPerformed(evt);
            }
        });

        btnLeft.setText("Left");
        btnLeft.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLeftActionPerformed(evt);
            }
        });

        btnRight.setText("Right");
        btnRight.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRightActionPerformed(evt);
            }
        });

        btnDown.setText("Down");
        btnDown.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDownActionPerformed(evt);
            }
        });

        lblScore.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblScore.setText("Score");

        txtScore.setEditable(false);

        pnlLogHolder.setBackground(new java.awt.Color(204, 204, 204));

        lblActionLog.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblActionLog.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblActionLog.setText("Action Log");

        txtActionLog.setEditable(false);
        txtActionLog.setColumns(20);
        txtActionLog.setRows(5);
        pnlHolder.setViewportView(txtActionLog);

        javax.swing.GroupLayout pnlLogHolderLayout = new javax.swing.GroupLayout(pnlLogHolder);
        pnlLogHolder.setLayout(pnlLogHolderLayout);
        pnlLogHolderLayout.setHorizontalGroup(
            pnlLogHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlLogHolderLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlLogHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(pnlHolder, javax.swing.GroupLayout.DEFAULT_SIZE, 339, Short.MAX_VALUE)
                    .addComponent(lblActionLog, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        pnlLogHolderLayout.setVerticalGroup(
            pnlLogHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlLogHolderLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblActionLog, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlHolder)
                .addContainerGap())
        );

        txtMoves.setEditable(false);

        lblMove.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblMove.setText("Moves");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(22, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(pnlBoardHolder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(399, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtScore)
                            .addComponent(lblScore, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnLeft, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnDown, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnRight))
                            .addComponent(btnUp, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(12, 12, 12)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblMove, javax.swing.GroupLayout.DEFAULT_SIZE, 54, Short.MAX_VALUE)
                            .addComponent(txtMoves, javax.swing.GroupLayout.DEFAULT_SIZE, 54, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addComponent(pnlLogHolder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
            .addGroup(layout.createSequentialGroup()
                .addComponent(lblTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 344, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(13, 13, 13)
                .addComponent(pnlBoardHolder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblMove, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblScore)
                        .addComponent(btnUp)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtScore, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLeft)
                    .addComponent(btnDown)
                    .addComponent(btnRight)
                    .addComponent(txtMoves, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14))
            .addComponent(pnlLogHolder, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnUpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpActionPerformed
        // Call the custom method to execute the board moves
        executeAction("Up");
    }//GEN-LAST:event_btnUpActionPerformed

    private void btnDownActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDownActionPerformed
        executeAction("Down");
    }//GEN-LAST:event_btnDownActionPerformed

    private void btnRightActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRightActionPerformed
        executeAction("Right");
    }//GEN-LAST:event_btnRightActionPerformed

    private void btnLeftActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLeftActionPerformed
        executeAction("Left");
    }//GEN-LAST:event_btnLeftActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Game.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Game.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Game.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Game.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Game().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDown;
    private javax.swing.JButton btnLeft;
    private javax.swing.JButton btnRight;
    private javax.swing.JButton btnUp;
    private javax.swing.JLabel lblActionLog;
    private javax.swing.JLabel lblMove;
    private javax.swing.JLabel lblScore;
    private javax.swing.JLabel lblTile1;
    private javax.swing.JLabel lblTile10;
    private javax.swing.JLabel lblTile11;
    private javax.swing.JLabel lblTile12;
    private javax.swing.JLabel lblTile13;
    private javax.swing.JLabel lblTile14;
    private javax.swing.JLabel lblTile15;
    private javax.swing.JLabel lblTile16;
    private javax.swing.JLabel lblTile2;
    private javax.swing.JLabel lblTile3;
    private javax.swing.JLabel lblTile4;
    private javax.swing.JLabel lblTile5;
    private javax.swing.JLabel lblTile6;
    private javax.swing.JLabel lblTile7;
    private javax.swing.JLabel lblTile8;
    private javax.swing.JLabel lblTile9;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JPanel pnlBoardHolder;
    private javax.swing.JScrollPane pnlHolder;
    private javax.swing.JPanel pnlLogHolder;
    private javax.swing.JTextArea txtActionLog;
    private javax.swing.JTextField txtMoves;
    private javax.swing.JTextField txtScore;
    // End of variables declaration//GEN-END:variables
}
