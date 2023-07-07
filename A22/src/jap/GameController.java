package jap;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Random;

public class GameController {
    private GameModel gameModel;
    private GameView gameView;

    public GameController(GameModel model, GameView view) {
        this.gameModel = model;
        this.gameView = view;

        view.setGameController(this);
        view.setBoardButtons(true, getButtons(true));
        view.setBoardButtons(false, getButtons(false));
        view.createPanelView(gameModel.getBoardSize(), view.getUserPanel(), true, view.getProgressPlayer1Panel());
        randomBoatPlacement(view.getOpponentPanel(), false);
        view.createPanelView(gameModel.getBoardSize(), view.getOpponentPanel(), false, view.getProgressPlayer2Panel());
        configurationString(false, gameModel.getOpponentButtons());
        disableUserButtons(true);

    }

    /**
     * Purpose: Calls Model and updates string, call view and refresh
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

    protected void aboutMenu() {

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

    protected void changeBoatColor(JButton[][] buttons, JPanel actorPanel, Boolean actor) {
        gameModel.changeBoatColor(buttons);

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

    protected void transferDesignToUserPanel(Integer selectedDimension, JButton[][] replace, JPanel userBoardPanel, JPanel opponentBoardPanel) {
        gameModel.convertDesignJButtonsToBoat(true);
        gameModel.setBoardSize(selectedDimension);
        gameModel.setUserBoardPanel(userBoardPanel);
        gameModel.setOpponentBoardPanel(opponentBoardPanel);
        gameView.updateBoard(replace, gameModel.getUserBoardPanel());
    }

    /**
     * Get instance of 2D array buttons from Game Model and return to View
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

    protected static int getBoatSize() {
        return GameView.boatSizeSelectorValue;
    }

    protected void openDesignBoat() {
        //gameView.designBoatWindow();
        //TODO add logic to check if randomize has been used
        gameModel.setUserPlayerButtons(gameModel.createButtonBoard(gameModel.getPlayer(true)));
        // Logic for loop to get # of boats, orientation, etc
        gameModel.populateDesignBoat();
        gameView.setDesignBoatList(gameModel.getDesignBoatList());
        gameView.extractDesignBoatList();
        //TODO clear layout, or save, and then refresh view
        gameView.designBoatPlacement(gameModel.getBoardSize());
    }

    protected void getDesignBoatList(){
        gameView.setDesignBoatList(gameModel.getDesignBoatList());
    }

    protected void resetDesignBoatArrayList() {
        gameModel.clearDesignBoatList();
        gameModel.convertDesignJButtonsToBoat(false);
        gameModel.populateDesignBoat();
    }

    protected void placeBoatLocation(Object eventSource) {
        boolean isBoatPlaced = gameModel.checkIfPlaced(eventSource);
        if (!isBoatPlaced) {
            //TODO DEFAULT ORIENTATION TO VERTICAL BY RADIO BUTTON AND BE ABLE TO CHANGE IT?
            gameModel.placeSelectedBoat(eventSource);
            getRemainingBoats();
        }
    }

    protected void checkOrientation(Object eventSource) {
        gameModel.setBoatOrientation(eventSource);
    }

    /**
     * Purpose: sets gameModel user/opponent 2D array button grid to have random boats
     *
     * @param actorPanel - Player/Machine JPanel
     * @param actor      - Player/Machine
     */
    protected void randomBoatPlacement(JPanel actorPanel, Boolean actor) {
        if (actor) {
            gameModel.setUserPlayerButtons(gameModel.generateBoatSize(true));
        } else {
            gameModel.setOpponentButtons(gameModel.generateBoatSize(false));
        }
    }

    protected Integer getRemainingBoats() {
        int remainder = gameModel.getNumberOfBoatsForDesign();
        return remainder;
    }

    protected void resetRemainingBoat() {
        int reset = 0;
        gameModel.setNumberOfBoatsForDesign(reset);
    }

    protected void startGame() {
        gameView.playClicked = true;
    }

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
                    Boat boat = (Boat) buttons[i][j];
                    buttons[i][j] = gameModel.updateButtonState(null, boat, true);
                    buttons[i][j].setName((i + 1) + "," + (j + 1));
                    boat.setUI(new HiddenTextButtonUI());
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

    }

    protected void setBoatColor(Color color) {
        gameModel.setSelectedColour(color);
    }

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
                    System.out.print("rowIndex = " + i + "\ncolumnIndex = " + j + "\n");
                    gameModel.placeSelectedBoat(eventSource);
                } else if (eventSource == buttons[i][j] && buttons[i][j].isEnabled()) {
                    performHitMissLogic(buttons[i][j], controlPanelText, who);
                }
            }
        }
    }

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

    protected boolean isValid(JButton selectedButton) {
        return gameModel.isValidSelection(selectedButton);
    }

    protected JButton randomSelection(int boardSize) {
        return gameModel.randomSelection(boardSize);
    }

    private void performHitMissLogic(JButton button, JLabel controlPanelText, Boolean who) {
        if (button instanceof Boat) {
            // Button is a boat, it's a hit
            System.out.print("Hit");
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
            System.out.print("Miss");
            historyLog(button, controlPanelText);
            gameModel.updateButtonState(button, null, false);
        }
    }

    /**
     * Custom ButtonUI implementation that hides the grayed-out text of a disabled JButton.
     */
    private static class HiddenTextButtonUI extends BasicButtonUI {
        @Override
        public void paint(Graphics g, JComponent c) {
            // Override the paint method to prevent painting the text
        }
    }

}