package jap;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class GameAction {
    /**
     * Generic method to print control panel history log in GUI.
     * @param eventSource - Generic parameter, could be JButton, JComboBox.
     * @param controlPanelText - JLabel use to extract .getName().
     * @param <T> - Type of elements passed from GameFrame.
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
    protected JButton[][] randBoatPlacement(int dimension) {
        //length of rows and columns on the board
        int boardSize = 2 * dimension;
        //Total amount of tiles on board
        int totalTiles = boardSize * boardSize;
        //formula to determine the amount of boats given the dimension of the board - N=(Dim/2) *(1+D)
        int totalBoats = (dimension / 2) * (1 + dimension);
        // Given formula to determine the amount of tiles the boats will occupy given dimension  S = (Dim * (Dim + 1) * (Dim + 2)) / 6
        int totalBoatTiles = (dimension * (dimension + 1) * (dimension + 2)) / 6;
        JButton[][] newBoard = new JButton[boardSize][boardSize];
                Random random = new Random();
                //loop the amount of total boats, each loop create a boat and uppdate if board is occupied or not
        for(int i = 0 ; i<totalBoats; i++ ){
            int boatSize = i+1;
            //get random true of false to determine if this boat is verticle (true) or horizontal(false)
            boolean boatIsVerticle = random.nextBoolean();

            //init vars for row and col
            int row;
            int col;

            do{
                col= random.nextInt(boardSize);
                row = random.nextInt(boardSize);

                //keep trying to create new buttons until a spot is made that is not occupied
        }while(isOccupiedOnBoard(newBoard,row,col));
            for (int k = 0;  k< boatSize;k++ ){
                if(boatIsVerticle){
                    newBoard[row+k][col].setText("Boat");
                    newBoard[row+k][col].setBackground(Color.red);
                }else{
                    newBoard[row][col+k].setText("Boat");
                    newBoard[row][col+k].setBackground(Color.red);
                }
            }


    }
        return newBoard;
}

private boolean isOccupiedOnBoard(JButton[][] board, int row, int col){
       return board[row][col].getText().equals("Boat");
    }
}
