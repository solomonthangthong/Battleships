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

    protected JButton[][] getButtons(Boolean actor){
        if (actor){
            return gameModel.getUserPlayerButtons();
        }
        else {
            return gameModel.getOpponentButtons();
        }
    }

    protected void designBoatPlacement(){
        gameModel.designBoatPlacement();
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

    protected <T> void historyLog(T eventSource, JLabel controlPanelText){
        gameModel.historyLog(eventSource, controlPanelText);
        String updatedLog = gameModel.getCurrentGameLog();
        gameView.updateControlPanelText(updatedLog);
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
    protected void boardButtonEvent(JButton[][] buttons, Object eventSource, JLabel controlPanelText) {
        for (JButton[] row : buttons) {
            for (JButton button : row) {
                if (eventSource == button) {
                    gameModel.historyLog(eventSource, controlPanelText);
                    String updatedLog = gameModel.getCurrentGameLog();
                    gameView.updateControlPanelText(updatedLog);
                }
            }
        }
    }
}
