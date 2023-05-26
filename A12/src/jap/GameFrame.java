package jap;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {
    private JFrame frame;
    private JPanel panel;
    private ImageIcon logo;

    public GameFrame() {
        mainFrame();
    }

    private void mainFrame() {
        frame = new JFrame("Battleshits");
        frame.setSize(1280, 720);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);



        panel = new JPanel();
        panel.setBackground(Color.orange);

        frame.add(panel, BorderLayout.CENTER);

        frame.setVisible(true);
    }

}
