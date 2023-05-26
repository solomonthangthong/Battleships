package jap;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {
    public GameFrame() {
        mainFrame();
    }

    private void mainFrame() {

        JFrame frame = new JFrame("Battle shits");
        JPanel mainPanel = new JPanel(new GridLayout(1,3));
        JPanel userBoardPanel = new JPanel();
        JPanel selectionPanel = new JPanel();
        JPanel opponentPanel = new JPanel();

        frame.setSize(1280, 720);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        userBoardPanel.setBackground(Color.orange);
        mainPanel.add(userBoardPanel);

        selectionPanel.setBackground(Color.decode("#19A7FF"));
        mainPanel.add(selectionPanel);

        opponentPanel.setBackground(Color.decode("#FF990D"));
        mainPanel.add(opponentPanel);

        frame.getContentPane().add(mainPanel);

        frame.setVisible(true);
    }

}
