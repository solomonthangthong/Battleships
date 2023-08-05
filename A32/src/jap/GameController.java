package jap;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.Serializable;

/**
 * Class Name: GameController
 * Method List: handleLanguageButton, handleDimensionComboBox, handleDesignBoatPlacement, handleRandBoatPlacement, handlePlayButton,
 * handleResetButton, handleBoatSizeSelector, HandleJRadioOrientation, handleResetLayout, handleSaveLayout, handleJButtonClicks,
 * historyLog, updateModelViewBoard, configurationString, changeBoatColor, transferDesignToUserPanel, getButtons, getBoatSize, openDesignBoat,
 * resetDesignBoatArrayList, checkOrientation, randomBoatPlacement, getRemainingBoats, resetRemainingBoat, startGame, resetGame, setColorVariables,
 * setBoatVisible, boardButtonEvent, disableUserButtons, isValid, randomSelection, performHitMissLogic, HiddenTextButtonUI, actionPerformed
 *
 * Constants List: gameModel, gameView
 * Controller model, following MVC design pattern, to handle User actions from View
 *
 * @author Andrew Lorimer, Solomon Thangthong
 * @version 2.0
 * @see JFrame
 * @see ActionListener
 * @see Serializable
 * @since 11.0.19
 */
public class GameController implements ActionListener {
    private final GameModel gameModel;
    private final GameView gameView;
    private final Client client;

    private String playerName;
    /**
     * Method Name: GameController
     * Purpose:Public constructor for GameController
     *
     * @param model contains the game logic
     * @param view contains the game display
     */
    public GameController(GameModel model, GameView view, Client client) {
        this.gameModel = model;
        this.gameView = view;
        this.client = client;

        view.registerGameController(this);
        model.setGameController(this);
        client.setGameController(this);

        view.setResizable(false);
        view.setVisible(false);


        // Set player 1 name to what is entered
        model.updatePlayerName(true, playerName);

        model.setUserPlayerButtons(model.createButtonBoard(model.getPlayer(true)));
        model.setOpponentButtons(model.createButtonBoard(model.getPlayer(false)));

        view.setBoardButtons(true, getButtons(true));
        view.setBoardButtons(false, getButtons(false));

        view.initializeFrame();
        view.createPanels();
        view.addPanelsToMainFrame();


        view.createPanelView(gameModel.getBoardSize(), view.getUserPanel(), true, view.getProgressPlayer1Panel());
        randomBoatPlacement(false);
        view.createPanelView(gameModel.getBoardSize(), view.getOpponentPanel(), false, view.getProgressPlayer2Panel());
        configurationString(false, gameModel.getOpponentButtons());
        disableUserButtons(true);

        //client.registerConnectionChangeListener(this::getConnectionStatus);
    }

    protected void playerName(String name){
        playerName = name;
    }
    /**
     * Method Name:handleLanguageButton
     * Purpose: Activate language Changer and log action in history log
     * Algorithm: call history log and language changer in GameView
     *
     * @param eventSource the language button
     * @param controlPanelText the text to be displayed in history log
     */
    protected void handleLanguageButton(Object eventSource, JLabel controlPanelText) {
        historyLog(eventSource, controlPanelText);
        gameView.languageChanger();
    }

    /**
     * Method Name: handleDimensionComboBox
     * Purpose: Take input from user to change board dimensions
     * Algorithm: Call history log, setBoardSize in the gameModel. Create Panels and update progressBar.
     *
     * @param eventSource the object from which the actionPerformed event originated.
     * @param controlPanelText the message to be displayed
     */
    protected void handleDimensionComboBox(Object eventSource, JLabel controlPanelText) {
        // updateModelViewBoard was changeDimensions
        // resizeBoard and change dimension were essentially the same in previous iteration
        historyLog(eventSource, controlPanelText);
        gameModel.setBoardSize((int) gameView.getDimensionComboBox().getSelectedItem());
        updateModelViewBoard(gameModel.getBoardSize(), gameView.getUserPanel(), gameView.getOpponentPanel());

        // Remove actionListeners and Update Panels
        gameView.createPanelView(gameModel.getBoardSize(), gameView.getUserPanel(), true, gameView.getProgressPlayer1Panel());

        // Instant create new Boats for Machine
        randomBoatPlacement(false);
        // Remove actionListeners and Update Panels
        gameView.createPanelView(gameModel.getBoardSize(), gameView.getOpponentPanel(), false, gameView.getProgressPlayer2Panel());
        configurationString(false, gameView.getOpponentButtons());
        disableUserButtons(true);
        gameView.updateProgressBar();
    }

    /**
     * Method Name:handleDesignBoatPlacement
     * Purpose: open the design window to allow user to make changes to their board.
     * Algorithm: Make new JFrame and use gameView methods to alter the view
     *
     * @param eventSource the object from which the actionPerformed event originated.
     * @param controlPanelText the message to be displayed
     */
    protected void handleDesignBoatPlacement(Object eventSource, JLabel controlPanelText) {
        historyLog(eventSource, controlPanelText);
        // Open design window
        gameView.designWindow = new JFrame();
        gameView.designBoatWindow();
        openDesignBoat();
        gameView.languageChanger();
    }

    /**
     * Method Name:handleRandBoatPlacement
     * Purpose:get user and opponent panels and randomly populate boats on both boards.
     * Algorithm: using both user and opponent panels, update the model view board and call randBoatPLacement.
     *
     * @param eventSource the object from which the actionPerformed event originated.
     * @param controlPanelText the message to be displayed
     */
    protected void handleRandBoatPlacement(Object eventSource, JLabel controlPanelText) {
        historyLog(eventSource, controlPanelText);
        updateModelViewBoard(gameModel.getBoardSize(), gameView.getUserPanel(), gameView.getOpponentPanel());

        // Need to see if GameModel Buttons are updated with Boat
        randomBoatPlacement(false);
        randomBoatPlacement(true);

        // Refresh View
        gameView.createPanelView(gameModel.getBoardSize(), gameView.getUserPanel(), true, gameView.getProgressPlayer1Panel());
        gameView.createPanelView(gameModel.getBoardSize(), gameView.getOpponentPanel(), false, gameView.getProgressPlayer2Panel());

        // Create String Representation
        configurationString(true, gameModel.getUserPlayerButtons()); // before just userButtons
        configurationString(false, gameModel.getOpponentButtons()); //before just opponentButtons? unsure if from GameView or GameModel

        // Set Boolean that checks if randomize happened to true
        gameView.setRandomizedClick(true);

    }

    /**
     * Method Name:handlePlayButton
     * Purpose:handle click event on the play button, disable all other buttons except reset.
     * Algorithm: if else logic for getting the method for board set up, then calling startGame()
     *
     * @param eventSource the object from which the actionPerformed event originated.
     * @param controlPanelText the message to be displayed
     */
    protected void handlePlayButton(Object eventSource, JLabel controlPanelText) {
        historyLog(eventSource, controlPanelText);
        if (gameView.getRandomizedClick() || gameView.getDesignSaved()) {
            startGame();
            gameView.setPlayClicked(true);
            gameView.disableControlPanelButtons();
        } else {
            JOptionPane.showMessageDialog(null, "Place ships with Design button or use Randomize to start the game.", "Warning", JOptionPane.WARNING_MESSAGE);
            gameView.setPlayClicked(false);
        }
    }

    /**
     * Method Name:handleResetButton
     * Purpose: handle user click on reset button to reset the game boards and timer.
     * Algorithm: call reset gme method in game model and re enable the control panel buttons
     *
     * @param eventSource the object from which the actionPerformed event originated.
     * @param controlPanelText the message to be displayed
     */
    protected void handleResetButton(Object eventSource, JLabel controlPanelText) {
        historyLog(eventSource, controlPanelText);
        gameView.resetGame(gameModel.getBoardSize());
        gameView.enableControlPanelButtons();

    }

    /**
     * Method Name:handleBoardSizeSelector
     * Purpose: Action method for chaging the size of a boat when in design mode
     * Algorithm:call method in gameview to update the boat size selected.
     *
     * @param eventSource the object from which the actionPerformed event originated.
     * @param controlPanelText the message to be displayed
     */
    protected void handleBoatSizeSelector(Object eventSource, JLabel controlPanelText) {
        historyLog(eventSource, controlPanelText);
        System.out.print((int) gameView.getBoatSizeSelector().getSelectedItem());
        gameView.setBoatSizeSelectorValue((int) gameView.getBoatSizeSelector().getSelectedItem());
    }

    /**
     * Method Name:handleJRadioOrientation
     * Purpose: Handles when radiobutoons on board are clicked, checking their orientation on the board
     * Algorithm: update history log and check the orientation of the boat represented by radio buttons
     *
     * @param eventSource the object from which the actionPerformed event originated.
     * @param controlPanelText the message to be displayed
     */
    protected void handleJRadioOrientation(Object eventSource, JLabel controlPanelText) {
        historyLog(eventSource, controlPanelText);
        checkOrientation(eventSource);
    }

    /**
     * Method Name:handleResetLayout
     * Purpose: handle when the user presses reset button in design mode
     * Algorithm: call reset on the boats and remove all boats from the design mode instance
     *
     * @param eventSource the object from which the actionPerformed event originated.
     * @param controlPanelText the message to be displayed
     */
    protected void handleResetLayout(Object eventSource, JLabel controlPanelText) {
        historyLog(eventSource, controlPanelText);
        resetRemainingBoat();
        resetDesignBoatArrayList();
        gameView.updateRemainingBoats();
    }

    /**
     * Method Name:handleSaveLayout
     * Purpose: handle when the user presses save in the design mode
     * Algorithm: using if -else logic to ensure all boats are placed before saving, after that, set the buttons on the design board to the ones on the main board
     *
     * @param eventSource the object from which the actionPerformed event originated.
     * @param controlPanelText the message to be displayed
     */
    protected void handleSaveLayout(Object eventSource, JLabel controlPanelText) {
        historyLog(eventSource, controlPanelText);
        if (getRemainingBoats() == 0) {
            gameView.setBoardButtons(true, getButtons(true));
            transferDesignToUserPanel(gameModel.getBoardSize(), gameView.getUserButtons(), gameView.getUserPanel(), gameView.getOpponentPanel());
            resetRemainingBoat();
            configurationString(true, gameView.getUserButtons());
            gameView.designWindow.dispose();
            gameView.setDesignWindow(null);
            gameView.createPanelView(gameModel.getBoardSize(), gameView.getUserPanel(), true, gameView.getProgressPlayer1Panel());
            disableUserButtons(true);
            gameView.setDesignSaved(true);
        } else if (getRemainingBoats() != 0) {
            JOptionPane.showMessageDialog(null, "Place remaining boats in order to save", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Method Name:handleJButtonClicks
     * Purpose: handle button clicks in main window, contains logic for handlling clicks in design mode and logic for when play button has been clicked
     * Algorithm: check if play button has been clicked, detect when user or robot has clicked all of the boats
     *
     * @param eventSource the object from which the actionPerformed event originated.
     * @param controlPanelText the message to be displayed
     */
    protected void handleJButtonClicks(Object eventSource, JLabel controlPanelText) {

        int selectedDimension = (int) gameView.getDimensionComboBox().getSelectedItem();

        if (gameView.getPlayClicked()) {
            //clickClip.start();
            // User play - can only click opponent board.
            do {
                boardButtonEvent(gameView.getOpponentButtons(), eventSource, gameView.getControlPanelText(), gameView.getDesignWindow(), true);
            } while (isValid((JButton) eventSource));
            //computer selects a square
            JButton selectedButton = randomSelection(selectedDimension * 2);
            if (selectedButton != null) {
                disableUserButtons(false);
                boardButtonEvent(gameView.getUserButtons(), selectedButton, gameView.getControlPanelText(), gameView.getDesignWindow(), false);
                disableUserButtons(true);
            }
            //ADD METHOD HERE TO CHECK BOTH PROGRESS BARS THAT SOLOMON IS DOING   if progress bar = 0 . Display win or loss
            if (gameView.getPlayer1Progress().getValue() == 0) {
                GameView.Splash s = new GameView.Splash();
                s.show(2);
                gameView.resetGame(selectedDimension);

            } else if (gameView.getPlayer2Progress().getValue() == 0) {
                GameView.Splash s = new GameView.Splash();
                s.show(1);
                gameView.resetGame(selectedDimension);
            }
        } else if (gameView.getDesignWindow() != null) {
            boardButtonEvent(gameView.getUserButtons(), eventSource, gameView.getControlPanelText(), gameView.getDesignWindow(), true);
            gameView.updateRemainingBoats();
            //gameView.setBoardButtons(true, getButtons(true));
            gameView.getDesignWindow().revalidate();
            gameView.getDesignWindow().repaint();
        }
    }

    /**
     * Method Name:historyLog
     * Purpose:Handle all calls to history log and update the game log in the control panel
     * Algorithm: update the history log in game model and update the view for the user to see actions
     *
     * @param eventSource      - Instances of any event
     * @param controlPanelText - String of history actions from player
     * @param <T>              - Generic object
     */
    protected <T> void historyLog(T eventSource, JLabel controlPanelText) {
        gameModel.historyLog(eventSource, controlPanelText);
        String updatedLog = gameModel.getCurrentGameLog();
        gameView.updateControlPanelText(updatedLog);
    }

    /**
     * Method Name: updateModelViewBoard
     * Purpose: Update gameModel variables (boardSize, 2D buttons, panels, and calls view to fresh GUI)
     *
     * @param selectedDimension  - boardSize
     * @param userBoardPanel     - Visual user Panel (left side)
     * @param opponentBoardPanel - Visual opponent panel (right side)
     */
    protected void updateModelViewBoard(Integer selectedDimension, JPanel userBoardPanel, JPanel opponentBoardPanel) {
        gameModel.setBoardSize(selectedDimension);
        gameModel.setUserPlayerButtons(gameModel.createButtonBoard(gameModel.getPlayer(true)));
        gameModel.setOpponentButtons(gameModel.createButtonBoard(gameModel.getPlayer(false)));
        gameModel.setUserBoardPanel(userBoardPanel);
        gameModel.setOpponentBoardPanel(opponentBoardPanel);
        gameView.updateBoard(gameModel.getUserPlayerButtons(), gameModel.getUserBoardPanel());
        gameView.updateBoard(gameModel.getOpponentButtons(), gameModel.getOpponentBoardPanel());
    }

    /**
     * Method Name: configurationString
     * Purpose: Mediator method to pass User/Machine 2D array grid to gameModel and retrieve String representation
     * Algorithm: if else to determine user or machine, call gameModel methods
     *
     * @param whichActor - True = User player, False = Machine
     * @param actorBoard - 2D array JButton
     */
    protected void configurationString(Boolean whichActor, JButton[][] actorBoard) {
        String hello;
        if (whichActor) {
            hello = gameModel.configurationString(true, actorBoard);
            gameModel.setPlayer1Config(hello);
        } else {
            hello = gameModel.configurationString(false, actorBoard);
            gameModel.setPlayer2Config(hello);
        }
    }
    protected void setMVCVisible(){
        gameView.setResizable(false);
        gameView.setVisible(true);
    }

    /**
     *
     */
    protected void sendGameConfiguration(){
        gameModel.setUserPlayerButtons(gameModel.generateBoatSize(true));
        String config = gameModel.configurationString(true, gameModel.getUserPlayerButtons());
        client.setGameConfiguration(config);
    }

    /**
     *
     */
    protected void getDimensionSize(){
        Integer size = gameModel.getBoardSize();
        client.setDimensionSize(size);
    }

    /**
     *
     * @param gameConfig
     */
    protected void receiveGameConfigurationClient(String gameConfig){
        // Split game arary string into individual components
        String[] digitArray = gameConfig.replaceAll("[^0-9]", "").split("");
        String[] digit = gameConfig.split(Config.FIELD_SEPARATOR);
        for (int i = 0; i < digitArray.length; i++){
            System.out.print(digitArray[i]);
        }
        // First index is board size
        gameModel.setBoardSize(Integer.valueOf(digitArray[0]));
        String withoutSize = digit[1];
        // Send into gameModel Method
        gameModel.createBoardFromString(Integer.valueOf(digitArray[0]), withoutSize);
        gameView.updateBoard(gameModel.getOpponentButtons(), gameModel.getOpponentBoardPanel());
    }


    /**
     * Method Name:changeBoatColor
     * Purpose: method for changing the color of the boats and tiles on both boards
     * Algorithm: use if else logic to check if a button on the board is a boat or default tile, use parameters from color changing gui to update these
     *
     * @param buttons 2D JButton array
     * @param actorPanel User/or Machine JPanel
     * @param actor - True = User, False = Machine
     * @param unselected set Color to unselect based on User Selection
     */
    protected void changeBoatColor(JButton[][] buttons, JPanel actorPanel, Boolean actor, Color unselected) {

        for (JButton[] buttonRow : buttons) {
            for (JButton button : buttonRow) {

                if (button instanceof Boat) {
                    if (!actor) {
                        button.setBackground(unselected);
                    }
                } else {
                    button.setBackground(unselected);
                }
            }
        }

        if (actor) {
            gameModel.setUserBoardPanel(actorPanel);
            gameView.updateBoard(buttons, actorPanel);
            gameModel.setUserPlayerButtons(buttons);
        } else {
            gameModel.setOpponentBoardPanel(actorPanel);
            gameView.updateBoard(buttons, actorPanel);
            gameModel.setOpponentButtons(buttons);
        }

    }

    /**
     * Method Name:transferDesignToUserPanel
     * Purpose: transfers the board made in design mode to the user panel for play
     * Algorithm: in the game model convert the Jbuttons to boats and set both panels. call game view to update the game boards
     *
     * @param selectedDimension Selected boat size
     * @param replace JButton convert to Boat
     * @param userBoardPanel User view JPanel
     * @param opponentBoardPanel Machine view JPanel
     */
    protected void transferDesignToUserPanel(Integer selectedDimension, JButton[][] replace, JPanel userBoardPanel, JPanel opponentBoardPanel) {
        gameModel.convertDesignJButtonsToBoat(true);
        gameModel.setBoardSize(selectedDimension);
        gameModel.setUserBoardPanel(userBoardPanel);
        gameModel.setOpponentBoardPanel(opponentBoardPanel);
        gameView.updateBoard(replace, gameModel.getUserBoardPanel());
    }

    /**
     * Method Name:getButtons
     * Purpose: get the user and opponent buttons on the board
     * Algorithm: using if else logic, determine if we are getting the user or opponent buttons and retrieve them from gameModel
     *
     * @param actor - Player or Machine
     * @return - 2D board for each player
     */
    protected JButton[][] getButtons(Boolean actor) {
        if (actor) {
            return gameModel.getUserPlayerButtons();
        } else {
            return gameModel.getOpponentButtons();
        }
    }

    /**
     * Method Name:getBoatSize
     * Purpose: get the boat size from the game view
     * Algorithm: call boatSizeSelectorValue from game view to retrive boat size
     *
     * @return the current boat size
     */
    protected static int getBoatSize() {
        return GameView.boatSizeSelectorValue;
    }

    /**
     * Method Name: openDesignBoat
     * Purpose:Opens the window for design mode - manually selection positions to place boats.
     * Algorithm:clear the current user buttons if they exist, create new button board to place new boats and save them in design boat list
     */
    protected void openDesignBoat() {
        //gameView.designBoatWindow();

        gameModel.clearUserButtonListeners();
        //TODO add logic to check if randomize has been used
        gameModel.setUserPlayerButtons(gameModel.createButtonBoard(gameModel.getPlayer(true)));
        // Logic for loop to get # of boats, orientation, etc
        gameModel.populateDesignBoat();
        gameView.setDesignBoatList(gameModel.getDesignBoatList());
        gameView.extractDesignBoatList();
        //TODO clear layout, or save, and then refresh view
        gameView.designBoatPlacement(gameModel.getBoardSize());
    }

    /**
     * Method Name:resetDesignBoatArrayList
     * Purpose: clear the design boats that are saved in an arraylist
     * Algorithm: clear the boat list in game model. repopulate the design boats so they can be re -placed
     */
    protected void resetDesignBoatArrayList() {
        gameModel.clearDesignBoatList();
        //TODO NOT SURE COMMENTING THIS OUT BREAKS SOMETHING
        gameModel.convertDesignJButtonsToBoat(false);
        gameModel.populateDesignBoat();
    }

    /**
     * Method Name:checkOrientation
     * Purpose:checks the current orientation of a boat
     * Algorithm:call game model to set the boat orientation according to current orientation
     *
     * @param eventSource a Boat
     */
    protected void checkOrientation(Object eventSource) {
        gameModel.setBoatOrientation(eventSource);
    }

    /**
     * Method Name:randomBoatPlacement
     * Purpose: sets gameModel user/opponent 2D array button grid to have random boats
     * Algorithm:check if user or opponent and set the buttons in gameModel to place the randomly generated boat
     *
     * @param actor - Player/Machine
     */
    protected void randomBoatPlacement(Boolean actor) {
        if (actor) {
            gameModel.setUserPlayerButtons(gameModel.generateBoatSize(true));
        } else {
            gameModel.setOpponentButtons(gameModel.generateBoatSize(false));
        }
    }

    /**
     * Method Name:getRemainingBoats
     * Purpose: Used for counting down the amount of boats left to place in design mode
     * Algorithm: check the number of boats present in game model
     *
     * @return the amount of boats remaining
     */
    protected Integer getRemainingBoats() {
        return gameModel.getNumberOfBoatsForDesign();
    }

    /**
     * Method Name:resetRemainingBoat
     * Purpose:when reset is clicked, the amount of boats to be placed must also be reset
     * Algorithm: set the number of boats for design to zero so remaining boats is reset
     */
    protected void resetRemainingBoat() {
        int reset = 0;
        gameModel.setNumberOfBoatsForDesign(reset);
    }

    /**
     * Method Name:startGame
     * Purpose: set a flag so that the game is in play mode, and users can only select the opponent board
     * Algorithm:set flag in gameview
     */
    protected void startGame() {
        gameView.playClicked = true;
    }

    /**
     * Method Name:resetGame
     * Purpose:resets the user and opponent panels after reset is clicked. reset all boats in both panels
     * Algorithm:using if else logic, determine if we are acting on user board or opponent board, loop thru entire board array and reset all buttons
     *
     * @param actor True = User, False = Machine
     * @param userBoardPanel User JPanel
     * @param opponentBoardPanel Opponent JPanel
     */
    protected void resetGame(Boolean actor, JPanel userBoardPanel, JPanel opponentBoardPanel) {
        JButton[][] buttons;
        Border whiteBorder = BorderFactory.createLineBorder(Color.white);

        if (actor) {
            buttons = gameModel.getUserPlayerButtons();
        } else {
            buttons = gameModel.getOpponentButtons();
        }

        int squareBoard = gameModel.getBoardSize() * 2;
        for (int i = 0; i < squareBoard; i++) {
            for (int j = 0; j < squareBoard; j++) {
                if (buttons[i][j] instanceof Boat) {
                    buttons[i][j] = new JButton();
                    buttons[i][j] = gameModel.updateButtonState(buttons[i][j], null, true, false);
                    buttons[i][j].setName((i + 1) + "," + (j + 1));
                    buttons[i][j].setUI(new HiddenTextButtonUI());
                }
                buttons[i][j] = gameModel.updateButtonState(buttons[i][j], null, true, false);
                buttons[i][j].setBackground(gameModel.getSelectedColour());
                buttons[i][j].setForeground(Color.white);
                buttons[i][j].setBorderPainted(true);
                buttons[i][j].setBorder(whiteBorder);
                buttons[i][j].setUI(new HiddenTextButtonUI());
            }
        }
        if (actor) {
            gameView.updateBoard(buttons, userBoardPanel);
        } else {
            gameView.updateBoard(buttons, opponentBoardPanel);
        }
        gameView.setPlayRandomDesignBooleans(false, false, false);
    }

    /**
     * Method Name:setColorVariables
     * Purpose:set the user selected colors to the board contents - water, hitboat and unselected.
     * Algorithm: update the color of each property in the game model
     *
     * @param unselected - a color for a button that does not have a boat or water
     * @param water - a color representing the water
     * @param hitBoat - a color property representing a hitBoat
     */
    protected void setColorVariables(Color unselected, Color water, Color hitBoat) {
        gameModel.setSelectedColour(unselected, water, hitBoat);
    }

    /**
     * Method Name:setBoatVisible
     * Purpose: For Solution method to reveal Opponent boat, and where the boats are placed
     * Algorithm: set boat visibility in game model to true
     */
    protected void setBoatVisible() {
        gameModel.setBoatVisible();
    }

    /**
     * Method Name: boardButtonEvent
     * Purpose: Loop through array of buttons to find user action event
     * Algorithm: For each loop, if eventSource equals instance of JButton call historyLog
     *
     * @param buttons          - either actor button array
     * @param eventSource      - Object event action
     * @param controlPanelText - JLabel passed, and then later uses .getName() to extract information
     * @param designWindow     - JFrame for popup window for Designing ships
     * @param who              - Actor true = user, false = machine
     */
    protected void boardButtonEvent(JButton[][] buttons, Object eventSource, JLabel controlPanelText, JFrame designWindow, Boolean who) {

        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[i].length; j++) {
                if (eventSource == buttons[i][j] && designWindow != null) {
                    gameModel.placeSelectedBoat(eventSource);
                } else if (eventSource == buttons[i][j] && buttons[i][j].isEnabled()) {
                    performHitMissLogic(buttons[i][j], controlPanelText, who);
                }
            }
        }
    }

    /**
     * Method Name:disableUserButtons
     * Purpose: method to prevent the user from clicking on their on board when play is clicked.
     * Algorithm: check if opponent has played and disable all buttons for the user so that turn taking can occur
     *
     * @param didMachinePlay - Did the machine play True or False
     */
    protected void disableUserButtons(Boolean didMachinePlay) {
        JButton[][] buttons;
        buttons = gameModel.getUserPlayerButtons();
        Integer size = gameModel.getBoardSize() * 2;

        // Check if machine played TRUE TO DISABLE, FALSE TO ENABLE?
        if (didMachinePlay) {
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (buttons[i][j] != null) {
                        buttons[i][j].setEnabled(false);
                        //buttons[i][j].setUI(new HiddenTextButtonUI());
                    }
                }
            }
        } else {
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (buttons[i][j] != null) {
                        buttons[i][j].setEnabled(true);
                        //buttons[i][j].setUI(new HiddenTextButtonUI());
                    }
                }
            }
        }
        gameView.setBoardButtons(true, buttons);
    }

    /**
     * Method Name:isValid
     * Purpose:check if selected button is a valid button to click
     * Algorithm: return a boolean variable to determine if the selected button is valid in gamemodel
     *
     * @param selectedButton the button pressed by the user
     * @return true if it is a valid selection, else return false
     */
    protected boolean isValid(JButton selectedButton) {
        return gameModel.isValidSelection(selectedButton);
    }

    /**
     * Method Name:randomSelection
     * Purpose: method used for randomly picking a spot on the board for the computer to take turns firing
     * Algorithm:pass the board size to gameModel and randomly select a sqaure on the board given that board size
     *
     * @param boardSize the size of board
     * @return the randomly selected button
     */
    protected JButton randomSelection(int boardSize) {
        return gameModel.randomSelection(boardSize);
    }

    /**
     * Method Name:performHitMissLogic
     * Purpose: Method is used to determine if a randomly selected sqaure or user selected sqaure contains a boat, resulting in either a hit or miss
     * Algorithm:check if selected button is a boat, check who selected the button (user or opponent) and update the button state given that selection
     *
     * @param button the selected button by the user or opponent
     * @param controlPanelText Message for the history log
     * @param who - the actor who selected a button
     */
    private void performHitMissLogic(JButton button, JLabel controlPanelText, Boolean who) {
        if (button instanceof Boat) {
            // Button is a boat, it's a hit
            historyLog(button, controlPanelText);
            gameModel.updateButtonState(null, (Boat) button, false, who);
            JProgressBar progress;
            if (who) {
                // False is opponent ProgressBar
                GameModel.updateUserPoints();
                progress = gameView.getProgressBar(false);
                int currentValue = progress.getValue();
                int decrementValue = 1;
                int newValue = (currentValue - decrementValue);
                progress.setValue(newValue);

            } else {
                GameModel.updateComputerPoints();
                progress = gameView.getProgressBar(true);
                int currentValue = progress.getValue();
                int decrementValue = 1;
                int newValue = (currentValue - decrementValue);
                progress.setValue(newValue);
            }
        } else {
            // Button is empty, it's a miss
            historyLog(button, controlPanelText);
            gameModel.updateButtonState(button, null, false, who);
        }
    }

    /**
     * Method Name:HiddenTextButton
     * Purpose: Custom ButtonUI implementation that hides the grayed-out text of a disabled JButton.
     *
     */
    private static class HiddenTextButtonUI extends BasicButtonUI {
        @Override
        public void paint(Graphics g, JComponent c) {
            // Override the paint method to prevent painting the text
        }
    }

    /**
     * Method Name:actionPerformed
     *
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {

    }
}