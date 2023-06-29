package jap;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class GameModel {

    /**
     * Array of clickable buttons on user actor board.
     */
    private JButton[][] userButtons;

    private JPanel userBoardPanel;
    /**
     * Array of clickable buttons on opponent board.
     */
    private JButton[][] opponentButtons;

    private JPanel opponentBoardPanel;


    private Player[] players;

    private int timer;

    private JComboBox<String> language;

    private int boardSize;

    // controlPanelText.getText(); for historyLog method
    private String currentAction;

    private int player1Points;

    private int player2Points;

    private ButtonState state;

    public GameModel() {
        boardSize = 4;
        players = new Player[2];

        players[0] = new Player("Player 1", true);
        players[1] = new Player("Player 2", false);

        userButtons = createButtonBoard(players[0]);
        opponentButtons = createButtonBoard(players[1]);
    }

    protected Player getPlayer(Boolean actor) {
        if (actor) {
            return players[0];
        } else {
            return players[1];
        }
    }

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
        String actionEvent = controlPanelText.getText();
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
                actionEvent = actionEvent + "Language set to " + selectedLanguage + "<br>";
                setCurrentAction(actionEvent);
            } else if (selectedItem instanceof Integer) {
                selectedDimension = (Integer) comboBox.getSelectedItem();
                //output the old game log text + the new text
                actionEvent = actionEvent + "Dimensions set to " + selectedDimension + "<br>";
                setCurrentAction(actionEvent);
            }
            //when design is clicked,print btn
        } else if (eventSource instanceof Boat) {
            Boat button = (Boat) eventSource;
            actionEvent = actionEvent + button.getBoatLength() + " clicked " + "<br>";
            setCurrentAction(actionEvent);
        } else if (eventSource instanceof JButton) {
            JButton button = (JButton) eventSource;
            actionEvent = actionEvent + button.getName() + " clicked " + "<br>";
            setCurrentAction(actionEvent);
        }
    }

    /**
     * Purpose: set currentAction String
     *
     * @param actionEvent
     */
    private void setCurrentAction(String actionEvent) {
        currentAction = actionEvent;
    }

    /**
     * Purpose: get currentAction string for historyLog in GameController
     *
     * @return
     */
    protected String getCurrentGameLog() {
        return currentAction;
    }

    protected Integer getBoardSize() {
        return boardSize;
    }

    protected void setBoardSize(Integer size) {
        this.boardSize = size;
    }

    protected JButton[][] getUserPlayerButtons() {
        return userButtons;
    }

    protected void setUserPlayerButtons(JButton[][] userPlayerButtons) {
        this.userButtons = userPlayerButtons;
    }

    protected void setOpponentButtons(JButton[][] opponentPlayerButtons) {
        this.opponentButtons = opponentPlayerButtons;
    }

    protected JButton[][] getOpponentButtons() {
        return opponentButtons;
    }

    protected void setUserBoardPanel(JPanel actorPanel) {
        this.userBoardPanel = actorPanel;
    }

    protected JPanel getUserBoardPanel() {
        return userBoardPanel;
    }

    protected void setOpponentBoardPanel(JPanel actorPanel) {
        this.opponentBoardPanel = actorPanel;
    }

    protected JPanel getOpponentBoardPanel() {
        return opponentBoardPanel;
    }

    protected void updateButtonState(JButton button, Boat boat) {
        // Init ButtonState
        ButtonState state;

        // Check if Button passed
        if (button != null) {
            state = new ButtonState(button);
        } else {
            state = new ButtonState(boat);
        }

        // If Button not EMPTY, and user clicked, state becomes missed
        if (button != null) {
            if (state.getState() != State.DEFAULT) {
                state.setState(State.MISS);
            }
            // if Boat is passed, and state is NOT HIT, state becomes hit
        } else if (boat.getState() != State.HIT) {
            state.setState(State.HIT);
        }

    }

    /**
     * Purpose: Creates initial 2D array for Buttons (Used in Controller updateModelViewBoard)
     *
     * @param player - actor
     * @return - 2D array of JButtons
     */
    protected JButton[][] createButtonBoard(Player player) {
        // Multiply dimensions by two. Intended result is if board is size 4 make it 8 by 8 grid
        int dimensions = boardSize * 2;
        int numRows = dimensions;
        int numCols = dimensions;
        String actor1 = "Pos ";
        String actor2 = "Opp Pos ";

        // Initialize 2D array for buttons
        JButton[][] buttons = new JButton[numRows][numCols];
        // Create nested loop, in order to create clickable buttons for each iteration of the outer loop
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                /* Create a new button for the board */
                JButton userButton = new JButton();
                if (player.isActor()) {
                    userButton.setName(actor1 + (i + 1) + "," + (j + 1));
                } else {
                    userButton.setName(actor2 + (i + 1) + "," + (j + 1));
                }

                if (player.isActor()) {
                    userButton.setBackground(Color.lightGray);
                } else {
                    userButton.setBackground(Color.lightGray);
                }
                // Set Default to 0
                ButtonState state = new ButtonState(userButton);
                userButton.setForeground(Color.BLACK);
                state.setState(State.DEFAULT);
                //assign the button in the array
                buttons[i][j] = userButton;
            }
        }
        return buttons;
    }

    /**
     * Method Name: generateBoardSize
     * Purpose: Nested loop to create boat size, and number of boats
     * Algorithm: Decrement outer and inner loop, call createRandomBoat and pass variables
     *
     * @return - 2D array of JButtons
     */
    protected JButton[][] generateBoatSize(Boolean actor) {
        JButton[][] newBoard;
        Player player;

        if (actor) {
            newBoard = userButtons;
            player = players[0];
        } else {
            newBoard = opponentButtons;
            player = players[1];
        }

        Random random = new Random();
        // Decrement Outer loop for Boat Sizes
        for (int boatSize = boardSize; boatSize >= 1; boatSize--) {
            // Decrement inner loop for number of boats
            for (int boatCount = boardSize - boatSize + 1; boatCount >= 1; boatCount--) {
                createRandomBoat(newBoard, boatSize, boardSize, random, player);
            }
        }
        return newBoard;
    }

    /**
     * Method Name: designBoatPlacement
     * Purpose: Pop-up window to design ship placement for user actor.
     * Algorithm: new intance of JFrame, set location, default close operation
     */
    protected void designBoatPlacement() {
    }

    /**
     * Method Name: createRandomBoat
     * Purpose: Loop until valid spot on 2D array is found, change colour of button to the new boat
     * Algorithm: While loop, new row col randomly generated, rand boolean if boat will be vertical/horizontal, once valid place is found set new values to JButton location
     *
     * @param board     - 2D JButton of actor
     * @param boatSize  - Outer nested loop index - boatSize from generateBoatSize
     * @param dimension - selected dimension size
     * @param random    - instance of random
     */
    protected void createRandomBoat(JButton[][] board, int boatSize, int dimension, Random random, Player player) {
        boolean boatPlaced = false;

        int red = random.nextInt(255);
        int green = random.nextInt(50);
        int blue = random.nextInt(256);

        // Loop until boats are placed
        while (!boatPlaced) {

            int randRow = random.nextInt(2 * dimension);
            int randCol = random.nextInt(2 * dimension);
            // Create colour  with the random RGB values, to distinguish between # of boats
            Color backgroundColor = new Color(red, green, blue);
            // Determine if vertical or horizontal
            boolean vertical = random.nextBoolean();

            // If vertical placement
            if (vertical) {
                if (isOccupiedOnBoard(board, randCol, randRow, boatSize, dimension, true)) {
                    // Skip iteration and move to next, restart loop to get new values
                    continue;
                }
                // Set name and background colour of JButton going down vertically for boat size
                for (int position = 0; position < boatSize; position++) {
                    Boat boat = new Boat(boatSize, true);
                    board[randRow + position][randCol] = boat;
                    boat.setBackground(backgroundColor);
                    //Can be removed later, only here to visually see if adhering to numerical representation
                    boat.setText(boatSize);
                    boat.setForeground(Color.WHITE);
                    player.addBoat(boat);
                }
            } else {
                if (isOccupiedOnBoard(board, randCol, randRow, boatSize, dimension, false)) {
                    // Skip iteration and move to next, restart loop to get new values
                    continue;
                }
                // Set name and background colour of JButton going horizontally for boat size
                for (int position = 0; position < boatSize; position++) {
                    Boat boat = new Boat(boatSize, false);
                    board[randRow][randCol + position] = boat;
                    boat.setBackground(backgroundColor);
                    //Can be removed later, only here to visually see if adhering to numerical representation
                    boat.setText(boatSize);
                    boat.setForeground(Color.WHITE);
                    player.addBoat(boat);
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
     * @param board    the game board
     * @param col      the row of the first point
     * @param row      the random col of the first point
     * @param boatSize the size of the boat being made
     * @param vertical is the boat growing in x or y dimension
     * @return boolean to determine if spot is occupied by another boat
     */
    private boolean isOccupiedOnBoard(JButton[][] board, int col, int row, int boatSize, int dimension, boolean vertical) {

        if (vertical) {
            // Check if ship goes out of bounds vertically
            if (row + boatSize > 2 * dimension) {
                return true;
            }
            for (int position = 0; position < boatSize; position++) {
                // create button to check if its instance of Boat
                JButton instanceButton = board[row + position][col];
                if (instanceButton instanceof Boat) {
                    return true;
                }
            }
        } else {
            // Check if ship goes out of bounds horizontally
            if (col + boatSize > 2 * dimension) {
                return true;
            }
            for (int position = 0; position < boatSize; position++) {
                JButton instanceButton = board[row][col + position];
                if (instanceButton instanceof Boat) {
                    return true;
                }
            }
        }
        return false;
    }
}
