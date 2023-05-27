package jap;

import javax.swing.*;
import java.awt.*;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.io.File;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameFrame extends JFrame implements ActionListener{
    private JPanel userBoardPanel;
    private JPanel selectionPanel;
    private JPanel opponentPanel;
    private JPanel controlPanel;
    private JComboBox<String> languageButton;
    private JLabel controlPanelText;


    public GameFrame() {
        initializeFrame();
        createPanels();
        addPanelsToMainFrame();
    }

    private void initializeFrame() {
        //TODO rename Battle shits to battleship lol
        setTitle("Battle shits");
        setSize(1280, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void createPanels() {
        selectionPanel = new JPanel();
        selectionPanel.setBackground(Color.decode("#19A7FF"));
        selectionPanel.setBorder(BorderFactory.createEmptyBorder(0,69,0,69));

        // Load and display the logo
        ImageIcon image = new ImageIcon("src/images/logo.png");
        if (new File("src/images/logo.png").exists()) {
            JButton imageLogo = new JButton(image);
            selectionPanel.add(imageLogo);
        } else {
            System.out.println("Image not found");
        }

        // Language dropdown
        String[] languages = {"English", "French"};
        languageButton = new JComboBox<>(languages);
        languageButton.addActionListener(this);
        JPanel languageMenu = new JPanel();
        languageMenu.setBackground(Color.decode("#19A7FF"));
        languageMenu.add(new JLabel("Languages: "));
        languageMenu.add(languageButton);
        selectionPanel.add(languageMenu);

        // Design and Randomize buttons
        JButton designBoard = new JButton("Design");
        JButton randBoard = new JButton("Randomize");
        JPanel designOptions = new JPanel();
        designOptions.setBackground(Color.decode("#19A7FF"));
        designOptions.add(designBoard);
        designOptions.add(randBoard);
        selectionPanel.add(designOptions);

        // Dimensions dropdown
        Integer[] dimensions = {4, 5, 6, 7, 8, 9, 10};
        JComboBox<Integer> dimensionComboBox = new JComboBox<>(dimensions);
        JPanel dimensionsPanel = new JPanel();
        dimensionsPanel.setBackground(Color.decode("#19A7FF"));
        dimensionsPanel.add(new JLabel("Dimensions:"));
        dimensionsPanel.add(dimensionComboBox);
        selectionPanel.add(dimensionsPanel);

        //need to add action listener to get value from this formula
        //int dimensionSelected = (int) dimensionComboBox.getSelectedItem();
        //formula from
        //int result = (selectedDimension * (selectedDimension + 1) * (selectedDimension + 2)) / 6;

        controlPanel = new JPanel();
        controlPanel.setPreferredSize(new Dimension(200,350));
        //controlPanel.setBorder(BorderFactory.createEmptyBorder(265,185,0,0));
        controlPanel.setBorder(BorderFactory.createRaisedBevelBorder());
        selectionPanel.add(controlPanel);

        // Set the colors of remaining panels
        opponentPanel = new JPanel();
        opponentPanel.setPreferredSize(new Dimension(520,119));
        opponentPanel.setBackground(Color.decode("#FF990D"));

        userBoardPanel = new JPanel();
        userBoardPanel.setPreferredSize(new Dimension(520,119));
        userBoardPanel.setBackground(Color.ORANGE);
    }

    private void addPanelsToMainFrame() {
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(userBoardPanel, BorderLayout.WEST);
        contentPane.add(selectionPanel, BorderLayout.CENTER);
        contentPane.add(opponentPanel, BorderLayout.EAST);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object eventSource = e.getSource();
        if (eventSource == languageButton){
            System.out.println("Language button toggled");

        }
    }
}
