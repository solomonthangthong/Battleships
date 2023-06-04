/**
 * File name: GameAction.java
 * Identification: Andrew Lorimer 041056170, Solomon Thangthong 041023691
 * Course: CST 8221 - JAP, Lab Section: 301
 * Professor: Paulo Sousa
 * Date: 06/04/2023
 * Compiler: Intellij IDEA 2023.1.1 (Community Edition)
 * Purpose: Host logic for control panel, design boat placement, createRandomBoat
 */

package jap;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

/**
 * Class Name: GameAction
 * Method List: historyLog, designBoatPlacement, generateBoatSize, createRandomBoat, isOccupiedOnBoard
 * Constants List: NA
 * Purpose: Host logic for history tracking, and placement of boats.
 *
 * @author Andrew Lorimer, Solomon Thangthong
 * @version 1
 * @since 11.0.19
 */
public class GameAction {
    /**
     * Method Name: historyLog
     * Purpose: Generic method to print control panel history log in GUI.
     * Algorithm: Check if object is JComboBox or JButton, concatenate string of user actions
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

        // Check for object JComboBox
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
     * Method Name: designBoatPlacement
     * Purpose: Pop-up window to design ship placement for user actor.
     * Algorithm: new intance of JFrame, set location, default close operation
     */
    protected void designBoatPlacement() {
        /* New JFrame for pop-up window to design board */
        JFrame designFrame = new JFrame();
        designFrame.setLocationRelativeTo(null);
        designFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        designFrame.setVisible(true);
    }

    /**
     * Method Name: generateBoardSize
     * Purpose: Nested loop to create boat size, and number of boats
     * Algorithm: Decrement outer and inner loop, call createRandomBoat and pass variables
     *
     * @param dimension - dimension of board
     * @param newBoard - 2D array of JButtons
     * @return - 2D array of JButtons
     */
    protected JButton[][] generateBoatSize(int dimension, JButton[][] newBoard) {
        Random random = new Random();
        // Decrement Outer loop for Boat Sizes
        for (int boatSize = dimension; boatSize >= 1; boatSize--){
            // Decrement inner loop for number of boats
            for (int boatCount = dimension - boatSize + 1; boatCount >= 1; boatCount--){
                createRandomBoat(newBoard, boatSize, dimension, random);
            }
        }
        return newBoard;
    }

    /**
     * Method Name: createRandomBoat
     * Purpose: Loop until valid spot on 2D array is found, change colour of button to the new boat
     * Algorithm: While loop, new row col randomly generated, rand boolean if boat will be vertical/horizontal, once valid place is found set new values to JButton location
     *
     * @param board - 2D JButton of actor
     * @param boatSize - Outer nested loop index - boatSize from generateBoatSize
     * @param dimension - selected dimension size
     * @param random - instance of random
     */
    private void createRandomBoat(JButton[][] board, int boatSize, int dimension, Random random) {
        boolean boatPlaced = false;

        int red = random.nextInt(255);
        int green = random.nextInt(50);
        int blue = random.nextInt(256);

        // Loop until boats are placed
        while(!boatPlaced){

            int randRow = random.nextInt(2 * dimension);
            int randCol = random.nextInt(2 * dimension);
            // Create colour  with the random RGB values, to distinguish between # of boats
            Color backgroundColor = new Color(red, green, blue);
            // Determine if vertical or horizontal
            boolean vertical = random.nextBoolean();

            // If vertical placement
            if (vertical){
                if (isOccupiedOnBoard(board, randCol, randRow, boatSize,dimension, true)){
                    // Skip iteration and move to next, restart loop to get new values
                    continue;
                }
                // Set name and background colour of JButton going down vertically for boat size
                for (int position = 0; position < boatSize; position++){
                    board[randRow + position][randCol].setName("Boat");
                    board[randRow + position][randCol].setBackground(backgroundColor);
                }
            }
            else {
                if (isOccupiedOnBoard(board, randCol, randRow, boatSize,dimension, false)){
                    // Skip iteration and move to next, restart loop to get new values
                    continue;
                }
                // Set name and background colour of JButton going horizontally for boat size
                for (int position = 0; position < boatSize; position++){
                    board[randRow][randCol + position].setName("Boat");
                    board[randRow][randCol + position].setBackground(backgroundColor);
                }
            }
            boatPlaced = true;
        }
    }


    /**
     * Method Name: isOccupiedOnBoard
     * Purpose: Check 2D array and see if row col is within dimensions, and then check if JButton is set as Boat
     * Algorithm: Check if vertical or horizontal, check if row + boatSize/col + boatSize is greater than 2 * dimensions,
     * then for loop and check instance of JButtons, if any are true, return true otherwise return false
     *
     * @param board      the game board
     * @param col        the row of the first point
     * @param row        the random col of the first point
     * @param boatSize   the size of the boat being made
     * @param vertical is the boat growing in x or y dimension
     * @return boolean to determine if spot is occupied by another boat
     */
    private boolean isOccupiedOnBoard(JButton[][] board, int col, int row, int boatSize, int dimension, boolean vertical) {

      if (vertical){
          // Check if ship goes out of bounds vertically
          if (row + boatSize > 2 * dimension) {
            return true;
          }
          for (int position = 0; position < boatSize; position++){
              if (board[row + position][col].getName().equals("Boat")){
                  return true;
              }
          }
      }
      else {
          // Check if ship goes out of bounds horizontally
        if (col + boatSize > 2 * dimension){
            return true;
        }
        for (int position = 0; position < boatSize; position++){
            if (board[row][col + position].getName().equals("Boat")){
                return true;
            }
        }
      }
      return false;
    }
}
