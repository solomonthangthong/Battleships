package jap;

import javax.swing.*;
import java.awt.*;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.io.File;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Objects;
//imports for sound effects
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class GameFrame extends JFrame implements ActionListener {
    private JPanel userBoardPanel;
    private JPanel selectionPanel;
    private JPanel opponentPanel;
    private JComboBox<String> languageButton;
    private JButton designBoard;
    private JButton randBoard;
    private JLabel controlPanelText;
    private JComboBox<Integer> dimensionComboBox;
    private JButton reset;
    private JButton play;
    private JButton[][] userButtons;
    private JButton[][] opponentGridButtons;

    public GameFrame() {
        initializeFrame();
        createPanels();
        addPanelsToMainFrame();
        //update create user board and create opponent board to take in result of DIM formula
        createUserBoard(4);
        createOpponentBoard(4);
        //play background music
        String musicFile = "resources/backgroundMusic.wav";
        playBackgroundMusic(musicFile);
    }

    private void initializeFrame() {
        setTitle("Battleship");
        setSize(1280, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void createPanels() {
        selectionPanel = new JPanel();
        selectionPanel.setBackground(Color.decode("#19A7FF"));
        /* Potentially do not need this border, if left and right sides use Preferred Size */
        selectionPanel.setBorder(BorderFactory.createEmptyBorder(0, 69, 0, 69));

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
        languageButton.setName("Languages");
        languageButton.addActionListener(this);
        JPanel languageMenu = new JPanel();
        languageMenu.setBackground(Color.decode("#19A7FF"));
        languageMenu.add(new JLabel("Languages: "));
        languageMenu.add(languageButton);
        selectionPanel.add(languageMenu);

        // Design and Randomize buttons
        designBoard = new JButton("Design");
        designBoard.setName("Design");
        designBoard.addActionListener(this);
        randBoard = new JButton("Randomize");
        randBoard.setName("Randomize");
        randBoard.addActionListener(this);
        JPanel designOptions = new JPanel();
        designOptions.setBackground(Color.decode("#19A7FF"));
        designOptions.add(designBoard);
        designOptions.add(randBoard);
        selectionPanel.add(designOptions);

        // Dimensions dropdown
        Integer[] dimensions = {4, 5, 6, 7, 8, 9, 10};
        dimensionComboBox = new JComboBox<>(dimensions);
        dimensionComboBox.addActionListener(this);
        JPanel dimensionsPanel = new JPanel();
        dimensionsPanel.setBackground(Color.decode("#19A7FF"));
        dimensionsPanel.add(new JLabel("Dimensions:"));
        dimensionsPanel.add(dimensionComboBox);
        selectionPanel.add(dimensionsPanel);

        //need to add action listener to get value from this formula
        //int dimensionSelected = (int) dimensionComboBox.getSelectedItem();
        //formula from
        //int result = (selectedDimension * (selectedDimension + 1) * (selectedDimension + 2)) / 6;

        //Control panel code
        JPanel controlPanel = new JPanel();
        controlPanel.setPreferredSize(new Dimension(200, 350));
        controlPanel.setBorder(BorderFactory.createRaisedBevelBorder());

        //add scrollable panel within the control panel
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(180, 320));

        // control panel text code
        controlPanelText = new JLabel("<html>");
        // set the text to appear at the top and left
        controlPanelText.setVerticalAlignment(JLabel.TOP);
        controlPanelText.setHorizontalAlignment(JLabel.LEFT);
        //Add the control panel text and scroll pane to the control panel
        controlPanel.add(controlPanelText);
        controlPanel.add(scrollPane);
        //set view port so that text appears inside scrollPane
        scrollPane.setViewportView(controlPanelText);
        selectionPanel.add(controlPanel);

        JLabel time = new JLabel("Time: ");
        JPanel timeDisplay = new JPanel();
        JPanel timeContainer = new JPanel();
        timeDisplay.setBorder(BorderFactory.createRaisedBevelBorder());
        timeDisplay.setPreferredSize(new Dimension(55, 30));
        timeContainer.add(time);
        timeContainer.add(timeDisplay);
        timeContainer.setBackground(Color.decode("#19A7FF"));
        selectionPanel.add(timeContainer);

        reset = new JButton("Reset");
        reset.setName("Reset");
        reset.addActionListener(this);
        selectionPanel.add(reset);

        play = new JButton("Play");
        play.setName("Play");
        play.addActionListener(this);
        selectionPanel.add(play);

        // Set the colors of remaining panels
        opponentPanel = new JPanel();
        opponentPanel.setPreferredSize(new Dimension(520, 119));
        opponentPanel.setBackground(Color.decode("#FF990D"));

        userBoardPanel = new JPanel();
        userBoardPanel.setPreferredSize(new Dimension(520, 119));
        userBoardPanel.setBackground(Color.ORANGE);
    }

    private void addPanelsToMainFrame() {
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(userBoardPanel, BorderLayout.WEST);
        contentPane.add(selectionPanel, BorderLayout.CENTER);
        contentPane.add(opponentPanel, BorderLayout.EAST);
    }

    public void createUserBoard(int dimension) {
        //manually manipulate dimensions for now - update input param dimension with formula result
        dimension = dimension * 2;
        int numRows = dimension;
        int numCols = dimension;
        JPanel userGrid = new JPanel(new GridLayout(numRows, numCols));
        userButtons = new JButton[numRows][numCols];
        //create the buttons in a for loop
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                /* Find a better function to use aside from this name, because this names the buttons but the history track almost there */
                JButton userButton = new JButton();
                userButton.setName("Pos " + (i + 1) + "," + (j + 1));
                //add action lister for every button
                userButton.addActionListener(this);
                userButton.setPreferredSize(new Dimension(50, 50));
                userButton.setBackground(Color.blue);

                userGrid.add(userButton);
                //assign the button in the array
                userButtons[i][j] = userButton;
            }
        }

        //make a new column panel to hold column label
        JPanel columnLabelsPanel = new JPanel(new GridLayout(1, numCols));
        //add column and row numbers on user Grid
        for (int k = 0; k < numCols; k++) {
            //make  new label for each col
            JLabel columnLabels = new JLabel(String.valueOf(k + 1));
            //set same dimensions as selection box
            columnLabels.setPreferredSize(new Dimension(50, 50));
            columnLabels.setHorizontalAlignment(SwingConstants.CENTER);
            //add column labels to the panel
            columnLabelsPanel.add(columnLabels);


        }
        //make a new row panel to hold the labels
        JPanel rowLabelPanel = new JPanel(new GridLayout(numRows, 1));
        for (int i = 0; i < numRows; i++) {
            //make a new label for each row
            JLabel rowLabel = new JLabel(String.valueOf(i + 1));
            //same dimension as selection box
            rowLabel.setPreferredSize(new Dimension(50, 50));
            //center text in box
            rowLabel.setHorizontalAlignment(SwingConstants.CENTER);
            //add row labels to labels panel
            rowLabelPanel.add(rowLabel);
        }
        //add row and column label panels and position west(left) and south(bottom)
        userBoardPanel.add(rowLabelPanel);
        //add the user selection hit box and center it
        userBoardPanel.add(userGrid, BorderLayout.CENTER);
        userBoardPanel.add(columnLabelsPanel);

    }

    public void createOpponentBoard(int dimension) {
        //manually manipulate dimensions for now - update input param dimension with formula result
        dimension = dimension * 2;
        int numRows = dimension;
        int numCols = dimension;
        JPanel opponentGrid = new JPanel(new GridLayout(numRows, numCols));
        opponentGridButtons = new JButton[numRows][numCols];
        //create the buttons in a for loop
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                JButton opponentButton = new JButton();
                opponentButton.setName(("Opp Pos " + (i + 1) + "," + (j + 1)));
                opponentButton.addActionListener(this);
                //set size of each button
                opponentButton.setPreferredSize(new Dimension(50, 50));
                opponentButton.setBackground(Color.red);
                //add buttons to grid
                opponentGrid.add(opponentButton);
                opponentGridButtons[i][j] = opponentButton;
            }
        }

        //make a new column panel to hold column label
        JPanel columnLabelsPanel = new JPanel(new GridLayout(1, numCols));
        //add column and row numbers on user Grid
        for (int k = 0; k < numCols; k++) {
            //make  new label for each col
            JLabel columnLabels = new JLabel(String.valueOf(k + 1));
            //set same dimensions as selection box
            columnLabels.setPreferredSize(new Dimension(50, 50));
            columnLabels.setHorizontalAlignment(SwingConstants.CENTER);
            //add column labels to the panel
            columnLabelsPanel.add(columnLabels);


        }
        //make a new row panel to hold the labels
        JPanel rowLabelPanel = new JPanel(new GridLayout(numRows, 1));
        for (int i = 0; i < numRows; i++) {
            //make a new label for each row
            JLabel rowLabel = new JLabel(String.valueOf(i + 1));
            //same dimension as selection box
            rowLabel.setPreferredSize(new Dimension(50, 50));
            //center text in box
            rowLabel.setHorizontalAlignment(SwingConstants.CENTER);
            //add row labels to labels panel
            rowLabelPanel.add(rowLabel);
        }

        //add row and column label panels and position west(left) and south(bottom)
        opponentPanel.add(rowLabelPanel);
        //add the user selection hit box and center it
        opponentPanel.add(opponentGrid, BorderLayout.CENTER);
        opponentPanel.add(columnLabelsPanel);
    }


    public void playBackgroundMusic(String musicFile) {
        try {
            //state the path where audio file is found
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(musicFile).getAbsoluteFile());
            //get the audio clip defined in AudioSystem
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            //open and loop the clip
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        /*Initialize GameAction (where the logic exists for test cases) */
        GameAction gameAction = new GameAction();
        Object eventSource = e.getSource();
        if (eventSource == languageButton) {
            gameAction.historyLog(eventSource, controlPanelText);
        } else if (eventSource == designBoard) {
            gameAction.historyLog(eventSource, controlPanelText);
            //pop up box
            gameAction.designBoard();
        } else if (eventSource == randBoard) {
            gameAction.historyLog(eventSource, controlPanelText);
            gameAction.randBoard();
        } else if (eventSource == dimensionComboBox) {
            gameAction.historyLog(eventSource, controlPanelText);
        } else if (eventSource == reset) {
            gameAction.historyLog(eventSource, controlPanelText);
        } else if (eventSource == play) {
            gameAction.historyLog(eventSource, controlPanelText);
        } else {
            for (JButton[] row : userButtons) {
                for (JButton button : row) {
                    if (eventSource == button) {
                        gameAction.historyLog(eventSource, controlPanelText);
                    }
                }
            }

            for (JButton[] row : opponentGridButtons) {
                for (JButton button : row) {
                    if (eventSource == button) {
                        gameAction.historyLog(eventSource, controlPanelText);
                    }
                }
            }
        }
    }
}
