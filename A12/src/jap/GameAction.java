package jap;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class GameAction {
    /**
     * Generic method to print control panel history log in GUI.
     *
     * @param eventSource      - Generic parameter, could be JButton, JComboBox.
     * @param controlPanelText - JLabel use to extract .getName().
     * @param <T>              - Type of elements passed from GameFrame.
     */
    protected <T> void historyLog(T eventSource, JLabel controlPanelText) {
        //cast selected item to string
        String currentGameLog = controlPanelText.getText();
        String selectedLanguage;
        Integer selectedDimension;
        //more buttons to be added defined here

        if (eventSource instanceof JComboBox) {
            JComboBox<?> comboBox = (JComboBox<?>) eventSource;
            Object selectedItem = comboBox.getSelectedItem();
            // Check whether we pass String or Int JComboBox
            if (selectedItem instanceof String) {
                selectedLanguage = (String) comboBox.getSelectedItem();
                //output the old game log text + the new text
                controlPanelText.setText(currentGameLog + "Language set to " + selectedLanguage + "<br>");
            } else if (selectedItem instanceof Integer) {
                selectedDimension = (Integer) comboBox.getSelectedItem();
                //output the old game log text + the new text
                controlPanelText.setText(currentGameLog + "Dimensions set to " + selectedDimension + "<br>");
            }
            //when design is clicked,print btn
        } else if (eventSource instanceof JButton) {
            JButton button = (JButton) eventSource;
            controlPanelText.setText(currentGameLog + button.getName() + " clicked " + "<br>");
        }
    }

    /**
     * Pop-up window to design ship placement for user actor.
     */
    protected void designBoatPlacement() {
        /* New JFrame for pop-up window to design board */
        JFrame designFrame = new JFrame();
        designFrame.setLocationRelativeTo(null);
        designFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        designFrame.setVisible(true);
    }

    /**
     * Rnadomize ship placement for machine, and if user desires to randomize.
     */
    protected JButton[][] randBoatPlacement(int dimension, JButton[][] newBoard) {
        //length of rows and columns on the board
        int boardSize = 2 * dimension;
        //Total amount of tiles on board
        int totalTiles = boardSize * boardSize;
        //formula to determine the amount of boats given the dimension of the board - N=(Dim/2) *(1+D)
        int totalBoats = dimension;

        //JButton[][] newBoard = new JButton[boardSize][boardSize];
        Random random = new Random();

        //loop the amount of total boats, each loop create a boat and update if board is occupied or not
        for (int i = 0; i < totalBoats; i++) {
            //iterator starts at zero
            int boatSize = i + 1;
            //get random true of false to determine if this boat is vertical (true) or horizontal(false)
            boolean boatIsVertical = random.nextBoolean();

            //init vars for row and col
            int row =0;
            int col =0;
             //wrap code in do while, continue geting random number until it is not occupied
            do {
                col = random.nextInt(boardSize);
                row = random.nextInt(boardSize);
                //keep trying to create new buttons until a spot is made that is not occupied - Ensure not larger than board with OR Statements
            } while (isOccupiedOnBoard(newBoard, row, col,boatSize,boatIsVertical,dimension)   || (boatIsVertical && (row + boatSize) > boardSize) || (!boatIsVertical && (col + boatSize) > boardSize)) ;
              //loop and add the boat tag for the next buttons
            for (int k = 0; k < boatSize; k++) {
                //if the boat is verticle extend the boat in the row direction
                if (boatIsVertical) {
                    int newRow = row +k;
                    if (newRow < boardSize) {
                        //set name for boat and assign color
                    newBoard[newRow][col].setName("Boat");
                    newBoard[newRow][col].setBackground(Color.blue);}
                } else {
                    //if boat is horizontal extend boat columnwise
                    int newCol = col+k;
                    //check to make sure
                    if (newCol < boardSize) {
                    newBoard[row][newCol].setName("Boat");
                    newBoard[row][newCol].setBackground(Color.red);}
                }
            }

        }
        return newBoard;
    }

    /**
     *
     * @param board the game board
     * @param row the row of the first point
     * @param col the random col of the first point
     * @param boatSize the size of the boat being made
     * @param isVertical is the boat growing in x or y dimension
     * @param dimension determines the boundary of the board
     * @return boolean to determine if spot is occupied by another boat
     */
    private boolean isOccupiedOnBoard(JButton[][] board, int row, int col, int boatSize, boolean isVertical, int dimension) {
        boolean isOccupied = false;
//if verticle grow row-wise and check each location for name "Boat"
        if (isVertical) {
            for (int i = 0; i < boatSize; i++) {
                if (row + i >= board.length || board[row + i][col].getName().equals("Boat")) {
                    //if any condition is true break out and change boolean value
                    isOccupied = true;
                    break;
                }
            }
            //if horizontal grow column wise and check name of boat
        } else {
            for (int i = 0; i < boatSize; i++) {
                if (col + i >= board[row].length || board[row][col + i].getName().equals("Boat")) {
                  //if any condition is true break out and change boolean value
                    isOccupied = true;
                    break;
                }
            }
        }

        return isOccupied;
    }
}
