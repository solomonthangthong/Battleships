package jap;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {
    private JFrame frame;
    private JPanel mainPanel;
    private JPanel userBoardPanel;
    private JPanel selectionPanel;
    private JPanel opponentPanel;
    private ImageIcon logo;

    public GameFrame() {
        mainFrame();
    }

    private void mainFrame() {
        frame = new JFrame("Battleshits");
        frame.setSize(1280, 720);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainPanel = new JPanel(new GridLayout(1,3));

        userBoardPanel = new JPanel();
        userBoardPanel.setBackground(Color.orange);
        mainPanel.add(userBoardPanel);

        selectionPanel = new JPanel();
        selectionPanel.setBackground(Color.decode("#19A7FF"));
        mainPanel.add(selectionPanel);

        opponentPanel = new JPanel();
        opponentPanel.setBackground(Color.decode("#FF990D"));
        mainPanel.add(opponentPanel);

        frame.getContentPane().add(mainPanel);

        frame.setVisible(true);
    }

}
