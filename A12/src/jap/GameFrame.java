package jap;

import javax.swing.*;
import java.awt.*;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class GameFrame extends JFrame {
    public GameFrame() {
        mainFrame();
    }

    private void mainFrame() {
        //TODO rename Battle shits to battleship lol
        JFrame frame = new JFrame("Battle shits");
        JPanel mainPanel = new JPanel(new GridLayout(1, 3));
        JPanel userBoardPanel = new JPanel();
        JPanel selectionPanel = new JPanel();
        JPanel opponentPanel = new JPanel();
        String[] languages = {"English", "French"};
        JLabel languageLabel = new JLabel("Languages: ");
        JComboBox languageButton = new JComboBox(languages);
        ImageIcon image;

        frame.setSize(1280, 720);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        userBoardPanel.setBackground(Color.orange);
        mainPanel.add(userBoardPanel);

        selectionPanel.setBackground(Color.decode("#19A7FF"));
        try {
            image = new ImageIcon("src/images/logo.png");
            JLabel imageLogo = new JLabel(image);
            selectionPanel.add(imageLogo);
            selectionPanel.add(Box.createVerticalStrut(100));
        } catch (Exception e) {
            System.out.print("Image cannot be found");
        }
        selectionPanel.add(languageLabel);
        selectionPanel.add(languageButton);

        mainPanel.add(selectionPanel);

        opponentPanel.setBackground(Color.decode("#FF990D"));
        mainPanel.add(opponentPanel);

        frame.getContentPane().add(mainPanel);
        frame.setVisible(true);
    }

}
