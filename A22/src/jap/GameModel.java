package jap;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.Serializable;
import java.util.*;
import java.util.List;

/**
 * Class Name: GameModel
 * <p>
 * Method List: getPlayer, historyLog, setCurrentAction, getCurrentGameLog, getBoardSize, setBoardSize, getUserPlayerButtons, setUserPlayerButtons,
 * setOpponentButtons, getOpponentButtons, setUserBoardPanel, getUserBoardPanel, setOpponentBoardPanel, getOpponentBoardPanel, getNumberOfBoatsForDesign,
 * setNumberOfBoatsForDesign, setSelectedColour, setPlayer1Config, setPlayer2Config, updateButtonState, CreateButtonBoard, HiddenTextButtonUI,
 * configurationString, generateBoatSize, populateDesignBoat, randomSelection, isValidSelection, clearDesignLight, getDesignBoatList, setBoatOrientation,
 * clearUserButtonListeners, placeSelectedBoat, setBoatVisible, convertDesignJButtonToBoat, createRandomBoat, isOccupiedOnBoard
 * <p>
 * Constants List: userButtons, opponentButtons, opponentBoardPanel, players, player1Config, player2Config, boardSize, currentAction, designBoatList, boatSizeSearch, numberOfBoatsForDesign
 * whiteBorder, selectedColour, waterColor, hitBoatColor
 * <p>
 * Model model, following MVC design pattern, to store data from Controller
 *
 * @author Andrew Lorimer, Solomon Thangthong
 * @version 2.0
 * @see JFrame
 * @see ActionListener
 * @see Serializable
 * @since 11.0.19
 */
public class GameModel {

    GameController gameController;
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

    private final Player[] players;
    private String player1Config;
    private String player2Config;

    private int boardSize;

    // controlPanelText.getText(); for historyLog method
    private String currentAction;

    // Nested list, inner list represent size of boat
    private List<List<Boat>> designBoatList;
    private Integer boatSizeSearch;
    private Integer numberOfBoatsForDesign;

    private Border whiteBorder;
    private Color selectedColour;
    private Color waterColour;
    private Color hitBoatColor;

    private HiddenTextButtonUI hiddenText;

    private SetDisableJButtonWhiteText setDisabledWhite;

    /**
     * Constructor for GameModel class
     */
    public GameModel() {
        boardSize = 4;
        players = new Player[2];

        players[0] = new Player("Player 1", true);
        players[1] = new Player("Player 2", false);

        selectedColour = Color.decode("#f56a4d");
        waterColour = Color.decode("#008fa2");
        hitBoatColor = Color.decode("#db9c59");

/*        userButtons = createButtonBoard(players[0]);
        opponentButtons = createButtonBoard(players[1]);*/

        numberOfBoatsForDesign = 0;
        hiddenText = new HiddenTextButtonUI();
        setDisabledWhite = new SetDisableJButtonWhiteText();

    }
    protected void setGameController(GameController controller){
        this.gameController = controller;
    }
    /**
     * Method Name: getPlayer
     * Purpose: Get player from List
     * Algorithm: If else statement, return player
     *
     * @param actor - User Player or Machine
     * @return - Player instance
     */
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
        } else if (eventSource instanceof JRadioButton) {
            JRadioButton selectedRadioButton = (JRadioButton) eventSource;
            String actionCommand = selectedRadioButton.getActionCommand();
            boolean selectedValue = Boolean.parseBoolean(actionCommand);
            if (selectedValue) {
                actionEvent = actionEvent + "Orientation set to " + "vertical" + "<br>";
            } else {
                actionEvent = actionEvent + "Orientation set to " + "horizontal" + "<br>";
            }
            setCurrentAction(actionEvent);
        } else if (eventSource instanceof JMenuItem) {
            JMenuItem menuItem = (JMenuItem) eventSource;
            String actionCommand = menuItem.getActionCommand();

            switch (actionCommand) {
                case "New":
                    actionEvent = actionEvent + "New game state implemented" + "<br>";
                    break;
                case "Solution":
                    actionEvent = actionEvent + "Opponent ship has been shown" + "<br>";
                    break;
                case "Exit":
                    actionEvent = actionEvent + "Player has chosen to exit" + "<br>";
                    break;

            }
            setCurrentAction(actionEvent);
        }
    }

    /**
     * Method Name: setCurrentAction
     * Purpose: set currentAction String
     * Algorithm: set global variable to passed argument
     *
     * @param actionEvent - Updated action string
     */
    private void setCurrentAction(String actionEvent) {
        currentAction = actionEvent;
    }

    /**
     * Method Name: getCurrentGameLog
     * Purpose: get currentAction string for historyLog in GameController
     * Algorithm: return currentAction
     *
     * @return - Control panel concatenate string
     */
    protected String getCurrentGameLog() {
        return currentAction;
    }

    /**
     * Method Name: getBoardSize
     * Purpose: Getter method for Board Size
     * Algorithm: return variable
     *
     * @return - int Board size
     */
    protected Integer getBoardSize() {
        return boardSize;
    }

    /**
     * Method Name: setBoardSize
     * Purpose: Setter for Board Size
     * Algorithm: Set global variable to passed argument
     *
     * @param size - Passed board Size from Controller
     */
    protected void setBoardSize(Integer size) {
        this.boardSize = size;
    }

    /**
     * Method Name: getUserPlayerButtons
     * Purpose: Getter method for user Buttons
     * Algorithm: return userButtons
     *
     * @return - User Button grid
     */
    protected JButton[][] getUserPlayerButtons() {
        return userButtons;
    }

    /**
     * Method Name: setUserPlayerButtons
     * Purpose: Setter for User Buttons
     * Algorithm: Set global variable for userButtons to passed argument
     *
     * @param userPlayerButtons - User Button grid from controller
     */
    protected void setUserPlayerButtons(JButton[][] userPlayerButtons) {
        this.userButtons = userPlayerButtons;
    }

    /**
     * Method Name: setOpponentButtons
     * Purpose: Setter method for Opponent Buttons
     * Algorithm: Set global variable to passed argument
     *
     * @param opponentPlayerButtons - Opponent Button grid from Controller
     */
    protected void setOpponentButtons(JButton[][] opponentPlayerButtons) {
        this.opponentButtons = opponentPlayerButtons;
    }

    /**
     * Method Name: getOpponentButtons
     * Purpose: Getter method for Opponent Buttons
     * Algorithm: return variable
     *
     * @return - Opponent Button grid
     */
    protected JButton[][] getOpponentButtons() {
        return opponentButtons;
    }

    /**
     * Method Name: setUserBoardPanel
     * Purpose: Setter method for UserBoard Panel
     * Algorithm: Set global variable to passed argument
     *
     * @param actorPanel - User JPanel from Controller
     */
    protected void setUserBoardPanel(JPanel actorPanel) {
        this.userBoardPanel = actorPanel;
    }

    /**
     * Method Name: getUserBoardPanel
     * Purpose: Getter method for UserBoard Panel
     * Algorithm: return variable
     *
     * @return - User JPanel
     */
    protected JPanel getUserBoardPanel() {
        return userBoardPanel;
    }

    /**
     * Method Name: setOpponentBoardPanel
     * Purpose: Setter method for Opponent Board Panel
     * Algorithm: Set global variable to passed argument
     *
     * @param actorPanel - Opponent JPanel
     */
    protected void setOpponentBoardPanel(JPanel actorPanel) {
        this.opponentBoardPanel = actorPanel;
    }

    /**
     * Method Name: getOpponentBoardPanel
     * Purpose: Getter method for Opponent Board Panel
     * Algorithm: return variable
     *
     * @return - Opponent JPanel
     */
    protected JPanel getOpponentBoardPanel() {
        return opponentBoardPanel;
    }

    /**
     * Method Name: getNumberOfBoatsForDesign
     * Purpose: Getter method for number of boats (remaining boat count)
     * Algorithm: return variable
     *
     * @return - Int for RemainingBoats variable
     */
    protected Integer getNumberOfBoatsForDesign() {
        return numberOfBoatsForDesign;
    }

    /**
     * Method Name: setNumberOfBoatsForDesign
     * Purpose: Setter number of boats for Design (remaining boat count)
     * Algorithm: Set global variable to passed argument
     *
     * @param reset - Reset numberOfBoatsForDesign to 0
     */
    protected void setNumberOfBoatsForDesign(Integer reset) {
        this.numberOfBoatsForDesign = reset;
    }

    protected Color getSelectedColour(){
        return selectedColour;
    }
    /**
     * Method Name: setSelectedColour
     * Purpose: Take color values from selection and set to global variables here
     * Algorithm: Set variables
     *
     * @param unselected - Grid Button Color
     * @param water      - When User/Machine misses
     * @param hitBoat    - When ship/boat is hit
     */
    protected void setSelectedColour(Color unselected, Color water, Color hitBoat) {
        this.selectedColour = unselected;
        this.waterColour = water;
        this.hitBoatColor = hitBoat;
    }

    /**
     * Method Name: setPlayer1Config
     * Purpose: set Player1 board layout in String representation
     * Algorithm: Set global variable to passed argument
     *
     * @param config - Player board arrangement string presentation
     */
    protected void setPlayer1Config(String config) {
        this.player1Config = config;
        System.out.print(player1Config + "\n");
    }

    /**
     * Method Name: setPlayer2Config
     * Purpose: set Player2 board layout in String representation
     * Algorithm: Set global variable to passed argument
     *
     * @param config - Player board arrangement string presentation
     */
    protected void setPlayer2Config(String config) {
        this.player2Config = config;
        System.out.print(player2Config + "\n");
    }

    /**
     * Method Name: updateButtonState
     * Purpose: When User/Machine select respective grid, update the State of JButton (Hit, Miss)
     * Algorithm: Check if button is null, if not null and is RESET, restore original JButton state.
     * If not reset determine if actor has hit ship or not, update state
     *
     * @param button - Passed JButton from board
     * @param boat   - Passed Boat from board
     * @param reset  - Boolean If reset button click
     * @param who    - Boolean for actor
     * @return button - Return JButton instance
     */
    protected JButton updateButtonState(JButton button, Boat boat, Boolean reset, Boolean who) {
        // Init ButtonState
        ButtonState state;
        Clip hitMissSound;

        if (button != null) {
            state = new ButtonState(button);
        } else {
            state = new ButtonState(boat);
        }

        // If Button not EMPTY, and user clicked, state becomes missed
        if (button != null && reset) {
            state.setState(State.DEFAULT);
            button.setBackground(Color.lightGray);
            button.setForeground(button.getBackground());
            return button;
        } else if (button != null) {
            if (state.getState() == State.DEFAULT) {
                state.setState(State.MISS);
                button.setBackground(waterColour);
                button.setForeground(Color.white);
                button.setEnabled(false);
                button.setUI(setDisabledWhite);
/*                hitMissSound = boardClipPlay(false);
                if (who) {
                    hitMissSound.start();
                }*/
            }
            // if Boat is passed, and state is NOT HIT, state becomes hit
        } else if (boat != null && reset) {
            JButton replaceButton = new JButton();
            state = new ButtonState(replaceButton);
            state.setState(State.DEFAULT);
            replaceButton.setBackground(Color.lightGray);
            replaceButton.setForeground(Color.black);

            return replaceButton;
        } else if (boat != null) {
            state.setState(State.HIT);
            boat.setBackground(hitBoatColor);
            boat.setForeground(Color.white);
            boat.setEnabled(false);
            boat.setUI(setDisabledWhite);
/*            hitMissSound = boardClipPlay(true);
            if (who) {
                hitMissSound.start();
            }*/
        }

        return button;
    }

    /**
     * Method Name: createButtonBoard
     * Purpose: Creates initial 2D array for Buttons (Used in Controller updateModelViewBoard)
     * Algorithm: Nested for loop to JButtons. Pass player object to determine who the buttons belong to
     *
     * @param player - actor
     * @return - 2D array of JButtons
     */
    protected JButton[][] createButtonBoard(Player player) {
        // Multiply dimensions by two. Intended result is if board is size 4 make it 8 by 8 grid
        whiteBorder = BorderFactory.createLineBorder(Color.white);
        int dimensions = boardSize;

        // Initialize 2D array for buttons
        JButton[][] buttons = new JButton[dimensions][dimensions];
        // Create nested loop, in order to create clickable buttons for each iteration of the outer loop
        for (int i = 0; i < dimensions; i++) {
            for (int j = 0; j < dimensions; j++) {
                /* Create a new button for the board */
                JButton userButton = new JButton();
                userButton.setBackground(selectedColour);
                if (player.getActor()) {
                    userButton.setName("[" + (i + 1) + "] [" +  (j + 1) + "]");
                } else {
                    userButton.setName("[" + (i + 1)  + "]" + "[" + (j + 1)+ "]");
                }
                // Set Default to 0
                ButtonState state = new ButtonState(userButton);
                userButton.setForeground(Color.white);
                userButton.setBorderPainted(true);
                userButton.setBorder(whiteBorder);
                state.setState(State.DEFAULT);
                userButton.setUI(hiddenText);
                //assign the button in the array
                buttons[i][j] = userButton;
            }
        }
        return buttons;
    }

    /**
     * Method Name: HiddenTextButtonUI
     * Purpose: Custom ButtonUI implementation that hides the grayed-out text of a disabled JButton.
     * Algorithm: Override the method to not show grey text when disabled
     */
    private static class HiddenTextButtonUI extends BasicButtonUI {
        @Override
        public void paint(Graphics g, JComponent c) {
            // Override the paint method to prevent painting the text
        }
    }

    /**
     * Method Name: configurationString
     * Purpose: String configuration for board layout
     * Algorithm: if else statement to determine whose buttons, StringBuilder and concatenate
     * the Text from JButton for configuration
     *
     * @param actor        - Player or Machine
     * @param actorButtons - Player or Machine 2D array JButton grid
     * @return - String representation of grid
     */
    protected String configurationString(Boolean actor, JButton[][] actorButtons) {
        JButton[][] buttons;

        if (actor) {
            userButtons = actorButtons;
            buttons = userButtons;

        } else {
            opponentButtons = actorButtons;
            buttons = opponentButtons;
        }

        StringBuilder configStringBuilder = new StringBuilder();

        for (JButton[] buttonRow : buttons) {
            for (JButton button : buttonRow) {
                if (button instanceof Boat) {
                    Boat boat = (Boat) button;
                    configStringBuilder.append(boat.getBoatLength());
                } else if (button != null) {
                    configStringBuilder.append(button.getText());
                }
            }
        }
        return configStringBuilder.toString();
    }

    /**
     * Method Name: generateBoardSize
     * Purpose: Nested loop to create boat size, and number of boats
     * Algorithm: Decrement outer and inner loop, call createRandomBoat and pass variables
     *
     * @param actor - True = user, false = machine
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
            //for (int boatCount = boardSize - boatSize + 1; boatCount >= 1; boatCount--) {
                createRandomBoat(newBoard, boatSize, boardSize, random, player);
            //}
        }
        return newBoard;
    }

    /**
     * Method Name: designBoatPlacement
     * Purpose: Pop-up window to design ship placement for user actor.
     * Algorithm: new instance of JFrame, set location, default close operation
     */
    protected void populateDesignBoat() {
        designBoatList = new ArrayList<>();
        //
        for (int boatSize = boardSize; boatSize >= 1; boatSize--) {
            // List for current boat size
            List<Boat> boatSizeList = new ArrayList<>();

            for (int boatCount = boardSize - boatSize + 1; boatCount >= 1; boatCount--) {
                Boat boat = new Boat(boatSize, true);
                boat.setText(boatSize);
                boat.setForeground(Color.WHITE);
                boatSizeList.add(boat);
                // Increment numberOfBoats for remaining number
                numberOfBoatsForDesign++;
            }
            designBoatList.add(boatSizeList);
        }

    }

    /**
     * Method Name: randomSelection
     * Purpose: Machine method to select user grid with random variable
     * Algorithm: Do while loop until Machine selects JButton that equals to hit or miss
     *
     * @param boardSize - 2D grid size
     * @return - Selected JButton
     */
    protected JButton randomSelection(int boardSize) {
        Random random = new Random();
        int row;
        int column;
        JButton selectedButton;

        do {
            row = random.nextInt(boardSize);
            column = random.nextInt(boardSize);
            selectedButton = userButtons[row][column];
        } while (selectedButton.getText().equals("HIT") || selectedButton.getText().equals("MISS"));

        return selectedButton;
    }

    /**
     * Method Name: isValidSelection
     * Purpose: Check if selection is valid
     * Algorithm: If else tree, if equals to HIT or MISS return boolean
     *
     * @param selectedButton - Passed JButton that was clicked by Player or Machine
     * @return - Boolean value
     */
    protected boolean isValidSelection(JButton selectedButton) {
        if (selectedButton.getText().equals("HIT") || selectedButton.getText().equals("MISS")) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Method Name: clearDesignBoatList
     * Purpose: Clear the list when reset game/ or new state
     * Algorithm: clear() List
     */
    protected void clearDesignBoatList() {
        designBoatList.clear();
    }

    /**
     * Method Name: getDesignBoatList
     * Purpose: Getter method for DesignBoatList array
     * Algorithm: return designBoatList
     *
     * @return - return Nested List of Boats
     */
    protected List<List<Boat>> getDesignBoatList() {
        return designBoatList;
    }

    /**
     * Method Name: setBoatOrientation
     * Purpose: set Boat Object Orientation
     * Algorithm: Enhanced for loop and set the boat Orientation
     *
     * @param eventSource - JRadioButton H or V
     */
    protected void setBoatOrientation(Object eventSource) {

        JRadioButton selectedRadioButton = (JRadioButton) eventSource;
        String actionCommand = selectedRadioButton.getActionCommand();
        boolean selectedValue = Boolean.parseBoolean(actionCommand);

        for (List<Boat> innerList : designBoatList) {
            // Iterate boat object in innerList
            for (Boat boat : innerList) {
                // Getter and check if same value from JComboBox
                boat.setBoatOrientation(selectedValue);
            }
        }
    }

    /**
     * Method Name: clearUserButtonListeners
     * Purpose: Clear actionListeners to avoid overlapping JButtons when transferring
     * from Main JFrame to Design Window
     * Algorithm: Enhanced For loop to clear each listener from JButton
     */
    protected void clearUserButtonListeners() {
        for (JButton[] row : userButtons) {
            for (JButton button : row) {
                ActionListener[] actionListeners = button.getActionListeners();
                for (ActionListener listener : actionListeners) {
                    button.removeActionListener((listener));
                }
            }
        }
    }

    /**
     * Method Name: placeSelectedBoat
     * Purpose: Place boat on 2D array
     * Algorithm: Enhanced For Loop, check condition to see if boat is NOT NULL AND equals selected Boat size AND not placed
     * Determine array location of user selection, if overlaps break, if not create boat within nested loop
     *
     * @param eventSource - JButton selection from Board
     */
    protected void placeSelectedBoat(Object eventSource) {
        int boatSizeSearch = GameController.getBoatSize();

        // Iterate nested List
        for (List<Boat> innerList : designBoatList) {
            // Iterate boat object in innerList
            for (Boat boat : innerList) {
                if (boat.getBoatOrientation() != null && boat.getBoatLength() == boatSizeSearch && !boat.getPlaced()) {
                    // Get the selected button
                    JButton clickedButton = (JButton) eventSource;

                    // Get the position of the clicked button
                    int clickedRow = -1;
                    int clickedCol = -1;
                    for (int row = 0; row < userButtons.length; row++) {
                        for (int col = 0; col < userButtons[row].length; col++) {
                            if (userButtons[row][col] == clickedButton) {
                                clickedRow = row;
                                clickedCol = col;
                                break;
                            }
                        }
                        if (clickedRow != -1 && clickedCol != -1) {
                            break;
                        }
                    }

                    // Check if boat position is within bounds
                    if (boat.getBoatOrientation()) {
                        if (clickedRow + boatSizeSearch <= userButtons.length) {
                            // Check for overlap
                            boolean overlap = false;
                            for (int i = 0; i < boatSizeSearch; i++) {
                                if (userButtons[clickedRow + i][clickedCol].getBackground() == Color.red || userButtons[clickedRow + i][clickedCol].getBackground() == Color.blue) {
                                    overlap = true;
                                    break;
                                }
                            }

                            if (!overlap) {
                                JButton[][] boatPosition = new JButton[boatSizeSearch][1];
                                //Boat[][] boatPosition = new Boat[boatSizeSearch][1];
                                for (int i = 0; i < boatSizeSearch; i++) {
                                    boatPosition[i][0] = userButtons[clickedRow + i][clickedCol];
                                    boatPosition[i][0].setBackground(Color.BLUE);
                                    boatPosition[i][0].setForeground(Color.white);
                                    boatPosition[i][0].setText(String.valueOf(boatSizeSearch));
                                    boatPosition[i][0].setName("Convert");
                                }
                                boat.setPlaced(true);
                                boat.setBoatPosition(boatPosition);
                                numberOfBoatsForDesign--;
                            } else {
                                JOptionPane.showMessageDialog(null, "Ship overlaps with another boat.\nPlease try again.", "Warning", JOptionPane.WARNING_MESSAGE);
                            }
                            break;
                        } else {
                            JOptionPane.showMessageDialog(null, "Ship does not fit within the board dimensions.\nPlease try again.", "Warning", JOptionPane.WARNING_MESSAGE);
                            break;
                        }
                    } else {
                        if (clickedCol + boatSizeSearch <= userButtons[clickedRow].length) {
                            // Check for overlap
                            boolean overlap = false;
                            for (int i = 0; i < boatSizeSearch; i++) {
                                if (userButtons[clickedRow][clickedCol + i].getBackground() == Color.red || userButtons[clickedRow][clickedCol + i].getBackground() == Color.blue) {
                                    overlap = true;
                                    break;
                                }
                            }

                            if (!overlap) {
                                JButton[][] boatPosition = new JButton[1][boatSizeSearch];
                                for (int i = 0; i < boatSizeSearch; i++) {
                                    boatPosition[0][i] = userButtons[clickedRow][clickedCol + i];
                                    boatPosition[0][i].setBackground(Color.RED);
                                    boatPosition[0][i].setForeground(Color.white);
                                    boatPosition[0][i].setText(String.valueOf(boatSizeSearch));
                                    boatPosition[0][i].setName("Convert");
                                }
                                boat.setPlaced(true);
                                boat.setBoatPosition(boatPosition);
                                numberOfBoatsForDesign--;
                            } else {
                                JOptionPane.showMessageDialog(null, "Ship overlaps with another boat.\nPlease try again.", "Warning", JOptionPane.WARNING_MESSAGE);
                            }
                            break;
                        } else {
                            JOptionPane.showMessageDialog(null, "Ship does not fit within the board dimensions.\nPlease try again.", "Warning", JOptionPane.WARNING_MESSAGE);
                            break;
                        }
                    }
                }
            }
        }
    }


    /**
     * Method Name: setBoatVisible
     * Purpose: For Solution method to reveal Opponent boat, and where the boats are placed
     * Algorithm: Enhanced For loop, and if instance of Boatm setVisibility to true, change boat Color
     */
    protected void setBoatVisible() {
        Map<Integer, Color> sizeColorMap = new HashMap<>();
        Random random = new Random();

        for (JButton[] boatRow : opponentButtons) {
            for (JButton button : boatRow) {
                if (button instanceof Boat) {
                    Boat boat = (Boat) button;
                    if (!sizeColorMap.containsKey(boat.getBoatLength())) {
                        Color randomColour = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
                        sizeColorMap.put(boat.getBoatLength(), randomColour);
                    }
                    if (sizeColorMap.containsKey(boat.getBoatLength()) && !boat.getVisibility()) {
                        boat.setVisibility(true);
                        boat.setBackground(sizeColorMap.get(boat.getBoatLength()));
                    }
                }
            }
        }
    }

    /**
     * Method Name: convertDesignJButtonsToBoat
     * Purpose: Convert JButtons from Design mode into boats and place into user grid
     * Algorithm: Nested For loop, if JButton contains "Convert" store size and new color in Map.
     * If matches are found during For loop iteration, ensure same size boat are same colour.
     *
     * @param update - Boolean if NOT true reset JButton for DesignWindow
     */
    protected void convertDesignJButtonsToBoat(Boolean update) {
        Map<Integer, Color> sizeColorMap = new HashMap<>();
        Random random = new Random();

        for (int row = 0; row < userButtons.length; row++) {
            for (int col = 0; col < userButtons.length; col++) {

                JButton button = userButtons[row][col];
                Integer size = Integer.parseInt(button.getText());
                String name = button.getName();

                // If update is not true, reset JButton for DesignWindow
                if (update) {

                    Boolean orientation;
                    // Check if button name contains "Convert"
                    if (name != null && name.contains("Convert")) {
                        // Generate random colour for each unique size
                        if (!sizeColorMap.containsKey(size)) {
                            Color randomColour = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
                            sizeColorMap.put(size, randomColour);
                        }

                        // Determine Orientation
                        if (button.getBackground().equals(Color.BLUE)) {
                            orientation = true;
                        } else {
                            orientation = false;
                        }
                        // Create new Boat
                        Boat boat = new Boat(size, orientation);

                        // If size is found in map use the same colour
                        if (sizeColorMap.containsKey(size)) {
                            boat.setBackground(sizeColorMap.get(size));
                        }
                        boat.setForeground(Color.white);
                        boat.setBoatLength(size);
                        boat.setText(size);
                        boat.setUI(hiddenText);
                        userButtons[row][col] = boat;
                    }
                } else {
                    if (button.getName().equals("Convert")) {
                        button.setText("0");
                        button.setForeground(Color.black);
                        button.setBackground(Color.decode("#f56a4d"));
                        button.setUI(hiddenText);
                    }
                }
            }
        }
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
     * @param player    - Player object instance
     */
    protected void createRandomBoat(JButton[][] board, int boatSize, int dimension, Random random, Player player) {
        boolean boatPlaced = false;
        designBoatList = new ArrayList<>();

        int red = random.nextInt(255);
        int green = random.nextInt(50);
        int blue = random.nextInt(256);

        // Loop until boats are placed
        while (!boatPlaced) {
            List<Boat> boatSizeList = new ArrayList<>();
            int randRow = random.nextInt(dimension);
            int randCol = random.nextInt(dimension);
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
                    if (player.getActor()) {
                        boat.setBackground(backgroundColor);
                        boat.setBorder(whiteBorder);
                    } else {
                        boat.setBackground(selectedColour);
                        boat.setForeground(Color.white);
                        boat.setBorder(whiteBorder);
                    }
                    boat.setPlaced(true);
                    //Can be removed later, only here to visually see if adhering to numerical representation
                    boat.setText(boatSize);
                    //boat.setForeground(Color.WHITE);
                    boat.setUI(hiddenText);
                    player.addBoat(boat);
                    boatSizeList.add(boat);
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
                    if (player.getActor()) {
                        boat.setBackground(backgroundColor);
                        boat.setBorder(whiteBorder);
                    } else {
                        boat.setBackground(selectedColour);
                        boat.setForeground(Color.white);
                        boat.setBorder(whiteBorder);
                    }
                    boat.setPlaced(true);
                    //Can be removed later, only here to visually see if adhering to numerical representation
                    boat.setText(boatSize);
                    //boat.setForeground(Color.WHITE);
                    boat.setUI(hiddenText);
                    player.addBoat(boat);
                    boatSizeList.add(boat);
                }
            }
            designBoatList.add(boatSizeList);
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
            if (row + boatSize > dimension) {
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
            if (col + boatSize > dimension) {
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

    /**
     * MethodName: SetDisableJButtonWhiteText
     * Purpose: CustomButtonUI is a custom ButtonUI implementation that allows setting the
     * disabled text color to white while properly centering the text on disabled buttons.
     *
     */
    public class SetDisableJButtonWhiteText extends BasicButtonUI {

        private final Color DISABLED_TEXT_COLOR = Color.WHITE;

        /**
         * MethodName: paint
         * Purpose: Overrides the paint method to customize the text appearance on disabled buttons.
         *
         * @param g the Graphics object to paint on
         * @param c the component being painted
         */
        @Override
        public void paint(Graphics g, JComponent c) {
            AbstractButton button = (AbstractButton) c;
            ButtonModel model = button.getModel();

            // Check if the button is disabled
            if (!model.isEnabled()) {
                g.setColor(DISABLED_TEXT_COLOR);

                // Get the text to be displayed on the button
                String text = button.getText();
                // Get the font metrics to calculate the position of the text
                FontMetrics fm = button.getFontMetrics(button.getFont());

                // Calculate the x and y coordinates to center the text
                int x = button.getWidth() / 2 - fm.stringWidth(text) / 2;
                int y = button.getHeight() / 2 + fm.getAscent() / 2 - 1;

                // Draw the text with the disabled text color
                g.drawString(text, x, y);
            } else {
                // Call the superclass paint method for enabled buttons
                super.paint(g, button);
            }
        }
    }

    /**
     * Method Name: boardClipPlay f
     * Purpose: Enables button click sound effects
     * Algorithm: try catch new wav file, create new audio stream, return sound
     *
     * @return - If file is found, return click sound effect, else return null
     */
    private Clip boardClipPlay(Boolean hit) {
        AudioInputStream audioInputStream;
        try {
            // Create 2nd audio input stream so 2 sounds can occur at same time
            if (hit) {
                audioInputStream = AudioSystem.getAudioInputStream(new File("resources/hit.wav").getAbsoluteFile());
            } else {
                audioInputStream = AudioSystem.getAudioInputStream(new File("resources/miss.wav").getAbsoluteFile());
            }

            // Get clip from audio file and open it
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            // Return audio file to be executed
            return clip;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
