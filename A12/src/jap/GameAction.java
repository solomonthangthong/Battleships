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
     * Randomize ship placement for machine, and if user desires to randomize.
     */
    protected JButton[][] generateBoatSize(int dimension, JButton[][] newBoard) {
        Random random = new Random();

        for (int boatSize = 1; boatSize <= dimension; boatSize++) {
            for (int boatCount = 1; boatCount <= dimension - boatSize + 1; boatCount++) {
                createRandomBoat(newBoard, boatSize, dimension, random);
            }
        }

/*        //loop the amount of total boats, each loop create a boat and update if board is occupied or not
        for (int i = 0; i < totalBoats; i++) {
            //iterator starts at zero
            int boatSize = i + 1;
            //get random true of false to determine if this boat is vertical (true) or horizontal(false)
            boolean boatIsVertical = random.nextBoolean();

            //init vars for row and col
            int row = 0;
            int col = 0;

            //Do while, continue getting random number until it is not occupied
            do {
                col = random.nextInt(boardSize);
                row = random.nextInt(boardSize);
                //keep trying to create new buttons until a spot is made that is not occupied - Ensure not larger than board with OR Statements
            } while (isOccupiedOnBoard(newBoard, row, col, boatSize, boatIsVertical, dimension) || (boatIsVertical && (row + boatSize) > boardSize) || (!boatIsVertical && (col + boatSize) > boardSize));

            //loop and add the boat tag for the next buttons
            for (int k = 0; k < boatSize; k++) {
                //if the boat is verticle extend the boat in the row direction
                if (boatIsVertical) {
                    int newRow = row + k;
                    if (newRow < boardSize) {
                        //set name for boat and assign color
                        newBoard[newRow][col].setName("Boat");
                        newBoard[newRow][col].setBackground(Color.blue);
                    }
                } else {
                    //if boat is horizontal extend boat columnwise
                    int newCol = col + k;
                    //check to make sure
                    if (newCol < boardSize) {
                        newBoard[row][newCol].setName("Boat");
                        newBoard[row][newCol].setBackground(Color.red);
                    }
                }
            }
        }*/
        return newBoard;
    }

    private void createRandomBoat(JButton[][] board, int boatSize, int dimension, Random random) {
        boolean boatPlaced = false;

        int red = (int) (Math.random() * 256);
        int green = (int) (Math.random() * 256);
        int blue = (int) (Math.random() * 256);

        while(!boatPlaced){

            int randRow = random.nextInt(2 * dimension);
            int randCol = random.nextInt(2 * dimension);
            // Create colour  with the random RGB values
            Color backgroundColor = new Color(red, green, blue);
            // Determine if vertical or horizontal
            boolean vertical = random.nextBoolean();

            // If vertical
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
