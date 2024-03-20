package jap;

/**
 * Class Name: GameBasic
 * Method List: main
 * Purpose: Host file for main method and entry
 *
 * @author Andrew Lorimer, Solomon Thangthong
 * @version 2.0
 * @since 11.0.19
 */
public class GameBasic {
    /**
     * Method Name: main
     * Purpose: The main entry point for the Battleship application.
     *
     * @param args the command-line arguments passed to the program
     */
    public static void main(String[] args) {
        GameModel gameModel = new GameModel();
        GameView gameView = new GameView();
        GameController gameController = new GameController(gameModel, gameView);
        gameView.setResizable(false);
        gameView.setVisible(true);

    }
}
