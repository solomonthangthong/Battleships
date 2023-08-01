/**
 * File name: GameBasic.java
 * Identification: Andrew Lorimer 041056170, Solomon Thangthong 041023691
 * Course: CST 8221 - JAP, Lab Section: 301
 * Professor: Paulo Sousa
 * Date: 06/04/2023
 * Compiler: Intellij IDEA 2023.1.1 (Community Edition)
 * Purpose: Main entry point for Batteship
 */
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
        Server server = new Server();
        GameModel gameModel = new GameModel();
        GameView gameView = new GameView();
        GameController gameController = new GameController(gameModel, gameView);

        gameView.setResizable(false);
        gameView.setVisible(true);

        server.setResizable(false);
        server.setVisible(true);

    }
}
