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

        //Set size of entire frame
        frame.setSize(1280, 720);
        //center the frame
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //set colors of panels
        opponentPanel.setBackground(Color.decode("#FF990D"));
        userBoardPanel.setBackground(Color.orange);
        selectionPanel.setBackground(Color.decode("#19A7FF"));



        //Code to add Battle Ship Image
        try {
            image = new ImageIcon("src/images/logo.png");
            JLabel imageLogo = new JLabel(image);
            selectionPanel.add(imageLogo);
            selectionPanel.add(Box.createVerticalStrut(100));
        } catch (Exception e) {
            System.out.print("Image cannot be found");
        }

        // Create the label for Dimensions
        JLabel label = new JLabel("Dimensions:");
        selectionPanel.add(label);

        // Create the dropdown for dimensions
        Integer[] dimensions = {4, 5, 6, 7, 8, 9, 10};
        JComboBox<Integer> dimensionComboBox = new JComboBox<>(dimensions);
        selectionPanel.add(dimensionComboBox);

        //need to add action listener to get value from this formula
        //int dimensionSelected = (int) dimensionComboBox.getSelectedItem();
        //formula from
        //int result = (selectedDimension * (selectedDimension + 1) * (selectedDimension + 2)) / 6;

        //Code for user panel Grid



        selectionPanel.add(languageLabel);
        selectionPanel.add(languageButton);

       //Add panels to the main panel
        mainPanel.add(userBoardPanel);
        mainPanel.add(selectionPanel);
        mainPanel.add(opponentPanel);

        //get content and make frame visible
        frame.getContentPane().add(mainPanel);
        frame.setVisible(true);
    }

}
