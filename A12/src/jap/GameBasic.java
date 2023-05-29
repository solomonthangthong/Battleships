package jap;

/**
 *
 */
public class GameBasic {
    /**
     * The main entry point for the Battleship application.
     * @param args the command-line arguments passed to the program
     */
    public static void main(String[] args) {
        GameFrame frame = new GameFrame();
        frame.setResizable(false);
        frame.setVisible(true);
    }
}
