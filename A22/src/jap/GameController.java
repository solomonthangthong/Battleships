package jap;

import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;

/**
 * Class Name: GameController
 * Method List: handleLanguageButton, handleDimensionComboBox, handleDesignBoatPlacement, handleRandBoatPlacement, handlePlayButton,
 * handleResetButton, handleBoatSizeSelector, HandleJRadioOrientation, handleResetLayout, handleSaveLayout, handleJButtonClicks,
 * historyLog, updateModelViewBoard, configurationString, changeBoatColor, transferDesignToUserPanel, getButtons, getBoatSize, openDesignBoat,
 * resetDesignBoatArrayList, checkOrientation, randomBoatPlacement, getRemainingBoats, resetRemainingBoat, startGame, resetGame, setColorVariables,
 * setBoatVisible, boardButtonEvent, disableUserButtons, isValid, randomSelection, performHitMissLogic, HiddenTextButtonUI, actionPerformed
 *
 * Constants List:
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

    public GameController(GameModel model, GameView view) {
        this.gameModel = model;
        this.gameView = view;
        view.registerGameController(this);

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

    }

    /**
     * Method Name:
     * Purpose:
     * Algorithm:
     *
     * @param eventSource
     * @param controlPanelText
     */
    protected void handleLanguageButton(Object eventSource, JLabel controlPanelText) {
        historyLog(eventSource, controlPanelText);
        gameView.languageChanger();
    }

    /**
     * Method Name:
     * Purpose:
     * Algorithm:
     *
     * @param eventSource
     * @param controlPanelText
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
     * Method Name:
     * Purpose:
     * Algorithm:
     *
     * @param eventSource
     * @param controlPanelText
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
     * Method Name:
     * Purpose:
     * Algorithm:
     *
     * @param eventSource
     * @param controlPanelText
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
     * Method Name:
     * Purpose:
     * Algorithm:
     *
     * @param eventSource
     * @param controlPanelText
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
     * Method Name:
     * Purpose:
     * Algorithm:
     *
     * @param eventSource
     * @param controlPanelText
     */
    protected void handleResetButton(Object eventSource, JLabel controlPanelText) {
        historyLog(eventSource, controlPanelText);
        gameView.resetGame(gameModel.getBoardSize());
        gameView.enableControlPanelButtons();
    }

    /**
     * Method Name:
     * Purpose:
     * Algorithm:
     *
     * @param eventSource
     * @param controlPanelText
     */
    protected void handleBoatSizeSelector(Object eventSource, JLabel controlPanelText) {
        historyLog(eventSource, controlPanelText);
        System.out.print((int) gameView.getBoatSizeSelector().getSelectedItem());
        gameView.setBoatSizeSelectorValue((int) gameView.getBoatSizeSelector().getSelectedItem());
    }

    /**
     * Method Name:
     * Purpose:
     * Algorithm:
     *
     * @param eventSource
     * @param controlPanelText
     */
    protected void handleJRadioOrientation(Object eventSource, JLabel controlPanelText) {
        historyLog(eventSource, controlPanelText);
        checkOrientation(eventSource);
    }

    /**
     * Method Name:
     * Purpose:
     * Algorithm:
     *
     * @param eventSource
     * @param controlPanelText
     */
    protected void handleResetLayout(Object eventSource, JLabel controlPanelText) {
        historyLog(eventSource, controlPanelText);
        resetRemainingBoat();
        resetDesignBoatArrayList();
        gameView.updateRemainingBoats();
    }

    /**
     * Method Name:
     * Purpose:
     * Algorithm:
     *
     * @param eventSource
     * @param controlPanelText
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
     * Method Name:
     * Purpose:
     * Algorithm:
     *
     * @param eventSource
     * @param controlPanelText
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
     * Method Name:
     * Purpose:
     * Algorithm:
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
     * Method Name:
     * Purpose:
     * Algorithm:
     *
     * @param whichActor
     * @param actorBoard
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

    /**
     * Method Name:
     * Purpose:
     * Algorithm:
     *
     * @param buttons
     * @param actorPanel
     * @param actor
     * @param unselected
     */
    protected void changeBoatColor(JButton[][] buttons, JPanel actorPanel, Boolean actor, Color unselected) {

        for (JButton[] buttonRow : buttons) {
            for (JButton button : buttonRow) {

                if (button instanceof Boat) {
                    if (!actor){
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
     * Method Name:
     * Purpose:
     * Algorithm:
     *
     * @param selectedDimension
     * @param replace
     * @param userBoardPanel
     * @param opponentBoardPanel
     */
    protected void transferDesignToUserPanel(Integer selectedDimension, JButton[][] replace, JPanel userBoardPanel, JPanel opponentBoardPanel) {
        gameModel.convertDesignJButtonsToBoat(true);
        gameModel.setBoardSize(selectedDimension);
        gameModel.setUserBoardPanel(userBoardPanel);
        gameModel.setOpponentBoardPanel(opponentBoardPanel);
        gameView.updateBoard(replace, gameModel.getUserBoardPanel());
    }

    /**
     * Method Name:
     * Purpose:
     * Algorithm:
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
     * Method Name:
     * Purpose:
     * Algorithm:
     *
     * @return
     */
    protected static int getBoatSize() {
        return GameView.boatSizeSelectorValue;
    }

    /**
     * Method Name:
     * Purpose:
     * Algorithm:
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
     * Method Name:
     * Purpose:
     * Algorithm:
     */
    protected void resetDesignBoatArrayList() {
        gameModel.clearDesignBoatList();
        gameModel.convertDesignJButtonsToBoat(false);
        gameModel.populateDesignBoat();
    }

    /**
     * Method Name:
     * Purpose:
     * Algorithm:
     *
     * @param eventSource
     */
    protected void checkOrientation(Object eventSource) {
        gameModel.setBoatOrientation(eventSource);
    }

    /**
     * Method Name:
     * Purpose: sets gameModel user/opponent 2D array button grid to have random boats
     * Algorithm:
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
     * Method Name:
     * Purpose:
     * Algorithm:
     *
     * @return
     */
    protected Integer getRemainingBoats() {
        return gameModel.getNumberOfBoatsForDesign();
    }

    /**
     * Method Name:
     * Purpose:
     * Algorithm:
     */
    protected void resetRemainingBoat() {
        int reset = 0;
        gameModel.setNumberOfBoatsForDesign(reset);
    }

    /**
     * Method Name:
     * Purpose:
     * Algorithm:
     */
    protected void startGame() {
        gameView.playClicked = true;
    }

    /**
     * Method Name:
     * Purpose:
     * Algorithm:
     *
     * @param actor
     * @param userBoardPanel
     * @param opponentBoardPanel
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
                    buttons[i][j] = gameModel.updateButtonState(buttons[i][j], null, true);
                    buttons[i][j].setName((i + 1) + "," + (j + 1));
                    buttons[i][j].setUI(new HiddenTextButtonUI());
                }
                buttons[i][j] = gameModel.updateButtonState(buttons[i][j], null, true);
                buttons[i][j].setBackground(Color.decode("#f56a4d"));
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
     * Method Name:
     * Purpose:
     * Algorithm:
     *
     * @param unselected
     * @param water
     * @param hitBoat
     */
    protected void setColorVariables(Color unselected, Color water, Color hitBoat) {
        gameModel.setSelectedColour(unselected, water, hitBoat);
    }

    /**
     * Method Name:
     * Purpose: For Solution method to reveal Opponent boat, and where the boats are placed
     * Algorithm:
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
     * Method Name:
     * Purpose:
     * Algorithm:
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
     * Method Name:
     * Purpose:
     * Algorithm:
     *
     * @param selectedButton
     * @return
     */
    protected boolean isValid(JButton selectedButton) {
        return gameModel.isValidSelection(selectedButton);
    }

    /**
     * Method Name:
     * Purpose:
     * Algorithm:
     *
     * @param boardSize
     * @return
     */
    protected JButton randomSelection(int boardSize) {
        return gameModel.randomSelection(boardSize);
    }

    /**
     * Method Name:
     * Purpose:
     * Algorithm:
     *
     * @param button
     * @param controlPanelText
     * @param who
     */
    private void performHitMissLogic(JButton button, JLabel controlPanelText, Boolean who) {
        if (button instanceof Boat) {
            // Button is a boat, it's a hit
            historyLog(button, controlPanelText);
            gameModel.updateButtonState(null, (Boat) button, false);
            JProgressBar progress;
            if (who) {
                // False is opponent ProgressBar
                progress = gameView.getProgressBar(false);
                int currentValue = progress.getValue();
                int decrementValue = 1;
                int newValue = (currentValue - decrementValue);
                progress.setValue(newValue);

            } else {
                progress = gameView.getProgressBar(true);
                int currentValue = progress.getValue();
                int decrementValue = 1;
                int newValue = (currentValue - decrementValue);
                progress.setValue(newValue);
            }
        } else {
            // Button is empty, it's a miss
            historyLog(button, controlPanelText);
            gameModel.updateButtonState(button, null, false);
        }
    }

    /**
     * Method Name:
     * Purpose:
     * Algorithm:
     *
     * Custom ButtonUI implementation that hides the grayed-out text of a disabled JButton.
     */
    private static class HiddenTextButtonUI extends BasicButtonUI {
        @Override
        public void paint(Graphics g, JComponent c) {
            // Override the paint method to prevent painting the text
        }
    }

    /**
     * Method Name:
     * Purpose:
     * Algorithm:
     *
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {

    }
}