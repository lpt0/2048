package u4a3_finalproject;

// Import the Exception superclass for catching all exceptions
import java.lang.Exception;

// Import the following for XML modification usage
import javax.xml.parsers.*;
import org.w3c.dom.*;


/**
 * Title: HighScores.java
 * Programmer: Haran
 * Date: August 21st 2018
 * Description: A JFrame to show all the high scores for all runs of the game.
 * Notes:
 * - since the button in the Introduction screen will only be enabled if the 
 * high score file is present, assume that the file is valid (meaning, do not 
 * assume that the user will attempt to launch to this screen directly).
 */
public class HighScores extends javax.swing.JFrame {
    /**
     * Creates new form HighScores
     */
    public HighScores() {
        initComponents();
        
        // Initialize a variable for the current index of the score array
        int intCurrentIndex = 0;
        
        // Read the XML file, parse it and create an array for the scores
        try {
            // Build the existing XML file
            Document docXml = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder().parse("highscores.xml");
            
            /** 
             * Search the XML for the score elements, assign the result to a 
             * variable.
             * The score elements are the child nodes of the high score element
             */
            NodeList nodeScores = docXml.getElementsByTagName("HighScores")
                    .item(0).getChildNodes();
            
            // Create a new array of the nodelist size, for the scores
            scrHighScores = new Score[nodeScores.getLength()];
            
            // Iterate through the score list
            for (int i = 0; i < nodeScores.getLength(); i++) {
                // Cast and assign the current score node to an Element variable
                Element elemCurrent = (Element) nodeScores.item(i);
                // Create variables for name, score, and date
                String strName, strScore, strDate, strMoves;
                
                /**
                 * Find the needed data - name, score, date. 
                 * There's one of each, so it can be assumed that the first item
                 * for each search will be what I'm looking for.
                 */
                strName = elemCurrent.getElementsByTagName("Name").item(0)
                        .getTextContent();
                strScore = elemCurrent.getElementsByTagName("Score").item(0)
                        .getTextContent();
                strDate = elemCurrent.getElementsByTagName("Date").item(0)
                        .getTextContent();
                strMoves = elemCurrent.getElementsByTagName("Moves").item(0)
                        .getTextContent();
                
                // Create a new score object 
                Score scrCurrent = new Score(strName, strScore, strDate, 
                        strMoves);
                // Add that score to the current index in the score array
                scrHighScores[intCurrentIndex] = scrCurrent;
                
                // Increment the current index counter
                intCurrentIndex++;
            }
            
            // Attempt to selection sort the array in descending order, by score
            selectionSort();
            

            // Loop through the score array, output the score data
            for (int i = 0; i < scrHighScores.length; i++) {
                /**
                 * Append the output text area with the player's position on the 
                 * scoreboard, and then the formatted string of data.
                 * The player position is i + 1, to make it clear for a user.
                 * Use the custom method within the score class to get the 
                 * formatted data.
                 * Separate each score by a blank line.
                 */
                this.txtHighScores.append((i + 1) + ". " + scrHighScores[i]
                        .toFormattedString() + "\n");
            }
            
        } catch (Exception error) {
            // Since all exceptions inherit this class, just catch that instead
            // Output any error to STDERR
            System.err.println(error);
            /**
             * If the file is modified by the user and not the program, this is 
             * more likely to error.
             */
        }
    }

    /**
     * Create a variable that will hold the 1D array of score objects.
     * The initial length does not matter; this variable will be overwritten 
     * with a new array, of a different length.
     */
    Score[] scrHighScores = new Score[1];
    
    /**
     * Selection sort the high score array in descending order, in place.
     */
    private void selectionSort() {
        // Loop through the array
        for (int i = 0; i < scrHighScores.length; i++) {
            // Loop through the array, offset from the i position + 1
            for (int j = i + 1; j < scrHighScores.length; j++) {
                // Is the the element at i less than the element at j?
                if (scrHighScores[i].getScore() < scrHighScores[j].getScore()) {
                    // Create a temp variable to hold the outer element
                    Score scrTemp = scrHighScores[i];
                    // Swap the elements to be in descending order
                    scrHighScores[i] = scrHighScores[j];
                    scrHighScores[j] = scrTemp;
                }
            }
        }
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
        pnlHolder = new javax.swing.JScrollPane();
        txtHighScores = new javax.swing.JTextArea();
        btnClose = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        lblTitle.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitle.setText("High Scores");

        txtHighScores.setEditable(false);
        txtHighScores.setColumns(20);
        txtHighScores.setLineWrap(true);
        txtHighScores.setRows(5);
        txtHighScores.setWrapStyleWord(true);
        pnlHolder.setViewportView(txtHighScores);

        btnClose.setText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(pnlHolder)
                    .addComponent(lblTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(155, Short.MAX_VALUE)
                .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(155, 155, 155))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnlHolder, javax.swing.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnClose)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // Close this specific HighScore window - set it to invisible
        this.setVisible(false);
    }//GEN-LAST:event_btnCloseActionPerformed

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
            java.util.logging.Logger.getLogger(HighScores.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(HighScores.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(HighScores.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(HighScores.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new HighScores().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JScrollPane pnlHolder;
    private javax.swing.JTextArea txtHighScores;
    // End of variables declaration//GEN-END:variables
}
