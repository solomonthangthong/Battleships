package jap;

import javax.swing.JFrame;

public class GameBasic extends JFrame {
    public static void main(String[] args) {
        JFrame myFrame = new JFrame();
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        /* Example found in Hybrid Documents page 18 */
        GamePanel panel = new GamePanel();
        myFrame.getContentPane().add(panel);
        myFrame.pack();
        myFrame.setVisible(true);
    }
}
