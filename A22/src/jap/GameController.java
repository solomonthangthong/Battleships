package jap;

import javax.swing.*;

public class GameController {
    private GameModel gameModel;
    private GameView gameView;

    public GameController (GameModel gameModel){
        this.gameModel = gameModel;

    }

    protected void setGameView(GameView gameView){
        this.gameView = gameView;
    }

    /**
     * Purpose: Calls Model and updates string, call view and refresh
     * @param eventSource
     * @param controlPanelText
     * @param <T>
     */
    protected <T> void historyLog(T eventSource, JLabel controlPanelText){
        gameModel.historyLog(eventSource, controlPanelText);
        String updatedLog = gameModel.getCurrentGameLog();
        gameView.updateControlPanelText(updatedLog);
    }

    protected void aboutMenu(){

    }

    protected void changeLanguage(){

    }

    /**
     * Method Name: updateModelViewBoard
     * Purpose: Update gameModel variables (boardSize, 2D buttons, panels, and calls view to fresh GUI)
     *
     * @param selectedDimension - boardSize
     * @param userBoardPanel - Visual user Panel (left side)
     * @param opponentBoardPanel - Visual opponent panel (right side)
     * @param userButtons - user 2D array button grid (left side)
     * @param opponentButtons - opponent 2D array button grid (right side)
     */
    protected void updateModelViewBoard(Integer selectedDimension, JPanel userBoardPanel, JPanel opponentBoardPanel, JButton[][] userButtons, JButton[][] opponentButtons){
        gameModel.setBoardSize(selectedDimension);
        gameModel.setUserPlayerButtons(gameModel.createButtonBoard(gameModel.getPlayer(true)));
        gameModel.setOpponentButtons(gameModel.createButtonBoard(gameModel.getPlayer(false)));
        gameModel.setUserBoardPanel(userBoardPanel);
        gameModel.setOpponentBoardPanel(opponentBoardPanel);
        gameView.updateBoard(gameModel.getBoardSize(), gameModel.getUserBoardPanel(),gameModel.getOpponentBoardPanel());
    }

    /**
     * Get instance of 2D array buttons from Game Model and return to View
     * @param actor
     * @return
     */
    protected JButton[][] getButtons(Boolean actor){
        if (actor){
            return gameModel.getUserPlayerButtons();
        }
        else {
            return gameModel.getOpponentButtons();
        }
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

    protected void placeBoatLocation(Object eventSource){
        boolean isBoatPlaced = gameModel.checkIfPlaced(eventSource);
        if (!isBoatPlaced){
            //TODO DEFAULT ORIENTATION TO VERTICAL BY RADIO BUTTON AND BE ABLE TO CHANGE IT?
            gameModel.placeSelectedBoat(eventSource);

        }
    }

    protected void checkOrientation(Object eventSource){
        gameModel.setBoatOrientation(eventSource);
    }

    /**
     * Purpose: sets gameModel user/opponent 2D array button grid to have random boats
     * @param actorPanel
     * @param actor
     */
    protected void randomBoatPlacement(JPanel actorPanel, Boolean actor){
        if (actor){
            gameModel.setUserPlayerButtons(gameModel.generateBoatSize(true));
        }
        else {
            gameModel.setOpponentButtons(gameModel.generateBoatSize(false));
        }
    }

    protected void startGame(){

    }
    protected void resetGame(){

    }
    protected void hitBoard(){

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
                // Checks the button clicked in grid and pop up window is opened for placing
                if (eventSource == button && designWindow != null){
                    System.out.print("rowIndex = " + rowIndex + "\ncolumnIndex = " + columnIndex + "\n");
                    gameModel.placeSelectedBoat(eventSource);
                }
                if (eventSource == button) {
                    //TODO incorporate hit miss logic
                    //TODO incorporate hit miss logic
                    if (button instanceof Boat){
                        gameModel.historyLog(eventSource, controlPanelText);
                        gameModel.updateButtonState(null, (Boat) button);
                        String updatedLog = gameModel.getCurrentGameLog();
                        gameView.updateControlPanelText(updatedLog);
                    }else {
                        gameModel.historyLog(eventSource, controlPanelText);
                        gameModel.updateButtonState(button, null);
                        String updatedLog = gameModel.getCurrentGameLog();
                        gameView.updateControlPanelText(updatedLog);
                    }
                }
                columnIndex++;
            }
            rowIndex++;
        }
    }
}
