package jap;

import javax.swing.*;

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
        view.createPanelView(gameModel.getBoardSize(), view.getOpponentPanel(), false, view.getProgressPlayer2Panel());

    }

    /**
     * Purpose: Calls Model and updates string, call view and refresh
     *
     * @param eventSource - Instances of any event
     * @param controlPanelText - String of history actions from player
     * @param <T> - Generic object
     */
    protected <T> void historyLog(T eventSource, JLabel controlPanelText) {
        gameModel.historyLog(eventSource, controlPanelText);
        String updatedLog = gameModel.getCurrentGameLog();
        gameView.updateControlPanelText(updatedLog);
    }

    protected void aboutMenu() {

    }

    protected void changeLanguage() {

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
        gameView.updateBoard(gameModel.getBoardSize(), gameModel.getUserBoardPanel(), gameModel.getOpponentBoardPanel());
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
    protected void openDesignBoat(){
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

    protected void placeBoatLocation(Object eventSource) {
        boolean isBoatPlaced = gameModel.checkIfPlaced(eventSource);
        if (!isBoatPlaced) {
            //TODO DEFAULT ORIENTATION TO VERTICAL BY RADIO BUTTON AND BE ABLE TO CHANGE IT?
            gameModel.placeSelectedBoat(eventSource);
            getRemainingBoats();
        }
    }

    protected void checkOrientation(Object eventSource){
        gameModel.setBoatOrientation(eventSource);
    }

    /**
     * Purpose: sets gameModel user/opponent 2D array button grid to have random boats
     *
     * @param actorPanel - Player/Machine JPanel
     * @param actor - Player/Machine
     */
    protected void randomBoatPlacement(JPanel actorPanel, Boolean actor) {
        if (actor) {
            gameModel.setUserPlayerButtons(gameModel.generateBoatSize(true));
        } else {
            gameModel.setOpponentButtons(gameModel.generateBoatSize(false));
        }
    }

    protected Integer getRemainingBoats(){
        int remainder = gameModel.getNumberOfBoatsForDesign();
        return remainder;
    }

    protected void startGame() {

    }

    protected void resetGame(Boolean actor) {
        JButton[][] buttons;
        if (actor) {
            buttons = gameModel.getUserPlayerButtons();
        } else {
            buttons = gameModel.getOpponentButtons();
        }

        int squareBoard = gameModel.getBoardSize() * 2;
        for (int i = 0; i < squareBoard; i++) {
            for (int j = 0; j < squareBoard; j++) {
                gameModel.updateButtonState(buttons[i][j], null, true);
            }
        }
    }

    protected void hitBoard() {

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
    protected void boardButtonEvent(JButton[][] buttons, Object eventSource, JLabel controlPanelText, JFrame designWindow) {

        int rowIndex = 0;
        for (JButton[] row : buttons) {
            int columnIndex = 0;
            for (JButton button : row) {
                //TODO Reset rowIndex and columnIndex to 0 after loops are done, or redo enhanced Loop and use normal for loop
                // Checks the button clicked in grid and pop up window is opened for placing
                if (eventSource == button && designWindow != null) {
                    System.out.print("rowIndex = " + rowIndex + "\ncolumnIndex = " + columnIndex + "\n");
                    gameModel.placeSelectedBoat(eventSource);
                }
                if (eventSource == button) {
                    //TODO incorporate hit miss logic
                    //TODO incorporate hit miss logic
                    if (button instanceof Boat) {
                        historyLog(eventSource, controlPanelText);
                        gameModel.updateButtonState(null, (Boat) button, false);

                    } else if (button != null){
                        historyLog(eventSource, controlPanelText);
                        gameModel.updateButtonState(button, null, false);
                    }
                }
                columnIndex++;
            }
            rowIndex++;
        }
    }
}
