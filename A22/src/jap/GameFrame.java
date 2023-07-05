/**
 * File name: GameFrame.java
 * Identification: Andrew Lorimer 041056170, Solomon Thangthong 041023691
 * Course: CST 8221 - JAP, Lab Section: 301
 * Professor: Paulo Sousa
 * Date: 06/04/2023
 * Compiler: Intellij IDEA 2023.1.1 (Community Edition)
 * Purpose: File that host creation of JPanels, JButtons, music and photo files.
 */
package jap;

import javax.swing.*;
import java.awt.*;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.io.File;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 * Class Name: GameFrame
 * Method List: initializeFrame, createPanels, addPanelsToMainFrame, createBoard, playBackgroundMusic, actionPerformed, createAudioClip, resizeBoard, boardButtonEvent
 * Constants List: serialVersionUID
 * The main container panel holding all instances of JPanels and JButtons
 * @author Andrew Lorimer, Solomon Thangthong
 * @version 1
 * @see JFrame
 * @see ActionListener
 * @see Serializable
 * @since 11.0.19
 */
public class GameFrame extends JFrame implements ActionListener, Serializable {
    /**
     * ensure serialized data can be deserialized correctly, if there are changes in the class structure or fields
     */
    private static final long serialVersionUID = 1L;
    /**
     * Panel for displaying user actor playing board.
     */
    private JPanel userPanel;
    /**
     * Panel for control panel related options.
     */
    private JPanel selectionPanel;
    /**
     * Panel to display where user actor will find opponent ship location.
     */
    private JPanel opponentPanel;
    /**
     * Combobox to change GUI language.
     */
    private JComboBox<String> languageButton;
    /**
     * Button to open new window to design boat placement.
     */
    private JButton designBoatPlacement;
    /**
     * Button to randomize both actor ship placement.
     */
    private JButton randBoatPlacement;
    /**
     * Label for controlPanel history box.
     */
    private JLabel controlPanelText;
    /**
     * Combobox for changing board dimension size.
     */
    private JComboBox<Integer> dimensionComboBox;
    /**
     * Button to reset the game.
     */
    private JButton reset;
    /**
     * Button to start the game.
     */
    private JButton play;
    /**
     * Array of clickable buttons on user actor board.
     */
    private JButton[][] userButtons;
    /**
     * Array of clickable buttons on opponent board.
     */
    private JButton[][] opponentButtons;
    /**
     * Button for user actor status.
     */
    private JPanel lifeUser;
    /**
     * Button for machine actor status.
     */
    private JPanel lifeMachine;

    /**
     * Creates a new instance of the GameFrame class.
     */
    public GameFrame() {
        initializeFrame();
        createPanels();
        addPanelsToMainFrame();
        //update create user board and create opponent board to take in result of DIM formula
        userButtons = createBoard(4, userPanel, lifeUser, true);
        opponentButtons = createBoard(4, opponentPanel, lifeMachine, false);
        //play background music
        String musicFile = "resources/backgroundMusic.wav";
        playBackgroundMusic(musicFile);
    }

    /**
     * Method Name: InitializeFrame
     * Purpose: Method to set up parameters for GUI window
     * Algorithm: Set title, size, location, default close operation
     */
    private void initializeFrame() {
        setTitle("Battleship by: Andrew Lorimer & Solomon Thangthong");
        setSize(1280, 675);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * Method Name: createPanels
     * Purpose: This method creates and configure panels for user interface and called internally for initialization.
     * Algorithm: Create selectionPanel, all control panel buttons, create both actor panel, and health bars.
     */
    private void createPanels() {
        /* Centre Panel Controls */
        selectionPanel = new JPanel();
        selectionPanel.setBackground(Color.decode("#8119FF"));
        selectionPanel.setBorder(BorderFactory.createEmptyBorder(0, 69, 0, 69));

        // Load and display the logo
        ImageIcon image = new ImageIcon("images/logo.png");
        if (new File("images/logo.png").exists()) {
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
        languageMenu.setBackground(Color.decode("#8119FF"));
        languageMenu.add(new JLabel("Languages: "));
        languageMenu.add(languageButton);
        selectionPanel.add(languageMenu);

        // Design and Randomize buttons
        designBoatPlacement = new JButton("Design");
        designBoatPlacement.setName("Design");
        designBoatPlacement.addActionListener(this);
        randBoatPlacement = new JButton("Randomize");
        randBoatPlacement.setName("Randomize");
        randBoatPlacement.addActionListener(this);
        JPanel designOptions = new JPanel();
        designOptions.setBackground(Color.decode("#8119FF"));
        designOptions.add(designBoatPlacement);
        designOptions.add(randBoatPlacement);
        selectionPanel.add(designOptions);

        // Dimensions dropdown
        Integer[] dimensions = {4, 5, 6, 7, 8, 9, 10};
        dimensionComboBox = new JComboBox<>(dimensions);
        dimensionComboBox.addActionListener(this);
        JPanel dimensionsPanel = new JPanel();
        dimensionsPanel.setBackground(Color.decode("#8119FF"));
        dimensionsPanel.add(new JLabel("Dimensions:"));
        dimensionsPanel.add(dimensionComboBox);
        selectionPanel.add(dimensionsPanel);

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

        /* Create Time window */
        //TODO: Add time duration when play button logic is created
        JLabel time = new JLabel("Time: ");
        JPanel timeDisplay = new JPanel();
        JPanel timeContainer = new JPanel();
        timeDisplay.setBorder(BorderFactory.createRaisedBevelBorder());
        timeDisplay.setPreferredSize(new Dimension(55, 30));
        timeContainer.add(time);
        timeContainer.add(timeDisplay);
        timeContainer.setBackground(Color.decode("#8119FF"));
        selectionPanel.add(timeContainer);

        /* Creates reset button */
        reset = new JButton("Reset");
        reset.setName("Reset");
        reset.addActionListener(this);
        selectionPanel.add(reset);

        /* Creates play button */
        play = new JButton("Play");
        play.setName("Play");
        play.addActionListener(this);
        selectionPanel.add(play);

        // Creates right side panel container
        opponentPanel = new JPanel(new BorderLayout());
        opponentPanel.setPreferredSize(new Dimension(520, 119));
        opponentPanel.setBackground(Color.decode("#FF990D"));

        // Creates left side panel container
        userPanel = new JPanel(new BorderLayout());
        userPanel.setPreferredSize(new Dimension(520, 119));
        userPanel.setBackground(Color.ORANGE);

        //set user actor life bar
        lifeUser = new JPanel();
        lifeUser.setBackground(Color.ORANGE);
        JButton lifeUserHealthBar = new JButton();
        lifeUserHealthBar.setName("Life 2");
        lifeUserHealthBar.setPreferredSize(new Dimension(250, 25));
        lifeUserHealthBar.addActionListener(this);
        lifeUser.add(new JLabel("Life 1"));
        lifeUser.add(lifeUserHealthBar);

        //set opponent actor life bar
        lifeMachine = new JPanel();
        lifeMachine.setBackground(Color.decode("#FF990D"));
        JButton lifeMachineHealthBar = new JButton();
        lifeMachineHealthBar.setName("Life 2");
        lifeMachineHealthBar.setPreferredSize(new Dimension(250, 25));
        lifeMachineHealthBar.addActionListener(this);
        lifeMachine.add(new JLabel("Life 2"));
        lifeMachine.add(lifeMachineHealthBar);
    }

    /**
     * Method Name: addPanelsToMainFrame
     * Purpose: Add created panels into the main frame.
     * Algorithm: Create new contentpane, set layout, add user actor, control panel, and machine actor
     */
    private void addPanelsToMainFrame() {
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(userPanel, BorderLayout.WEST);
        contentPane.add(selectionPanel, BorderLayout.CENTER);
        contentPane.add(opponentPanel, BorderLayout.EAST);
    }

    /**
     * Method Name: createBoard
     * Purpose: Creates clickable buttons based on dimension size. Player can click the button and attempt to locate enemy ship
     * Algorithm: Create button sizes, define variables, nested loop to create 2D array of JButtons, then create col and row labels.
     *
     * @param dimension  - Board dimension size, default is 4
     * @param actorPanel - User or Opponent JPanel
     * @param whichActor - True = user actor, false = machine actor
     * @return - Button Array
     */
    private JButton[][] createBoard(int dimension, JPanel actorPanel, JPanel lifeStatus, Boolean whichActor) {
        int buttonSize = Math.min(50, 200 / dimension); // Adjust the button size based on dimension
        int labelSize = Math.min(50, 200 / dimension); // Adjust the button size based on dimension

        // Multiply dimensions by two. Intended result is if board is size 4 make it 8 by 8 grid
        dimension = dimension * 2;
        int numRows = dimension;
        int numCols = dimension;
        String actor1 = "Pos ";
        String actor2 = "Opp Pos ";

        // Initialize JPanel to hold array of buttons
        JPanel actorGrid = new JPanel(new GridLayout(numRows, numCols));
        // Initialize 2D array for buttons
        JButton[][] buttonForGrid = new JButton[numRows][numCols];
        // Create nested loop, in order to create clickable buttons for each iteration of the outer loop
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                /* Create a new button for the board */
                JButton userButton = new JButton();
                if (whichActor) {
                    userButton.setName(actor1 + (i + 1) + "," + (j + 1));
                } else {
                    userButton.setName(actor2 + (i + 1) + "," + (j + 1));
                }
                //add action lister for this instance of button
                userButton.addActionListener(this);
                userButton.setPreferredSize(new Dimension(buttonSize, buttonSize));

                if (whichActor) {
                    userButton.setBackground(Color.decode("#19A7FF"));
                } else {
                    userButton.setBackground(Color.decode("#FFC800"));
                }
                actorGrid.add(userButton);
                //assign the button in the array
                buttonForGrid[i][j] = userButton;
            }
        }

        // Create column panel to hold column label
        JPanel columnLabelsPanel = new JPanel(new GridLayout(1, numCols));
        columnLabelsPanel.setBorder(BorderFactory.createEmptyBorder(0, 1000, 0, 1000)); // Add left padding
        //add column and row numbers on user Grid
        for (int k = 0; k < numCols; k++) {
            //make  new label for each col
            JLabel columnLabels = new JLabel(String.valueOf(k + 1));
            //set same dimensions as selection box
            columnLabels.setPreferredSize(new Dimension(labelSize, labelSize));
            columnLabels.setHorizontalAlignment(SwingConstants.CENTER);
            //add column labels to the panel
            columnLabelsPanel.add(columnLabels);

            if (whichActor) {
                columnLabelsPanel.setBackground(Color.ORANGE);
            } else {
                columnLabelsPanel.setBackground(Color.decode("#FF990D"));
            }
        }

        //make a new row panel to hold the labels
        JPanel rowLabelPanel = new JPanel(new GridLayout(numRows, 1));
        columnLabelsPanel.setBorder(BorderFactory.createEmptyBorder(0, 50, 0, 0));
        for (int i = 0; i < numRows; i++) {
            //make a new label for each row
            JLabel rowLabel = new JLabel(String.valueOf(i + 1));
            //same dimension as selection box
            rowLabel.setPreferredSize(new Dimension(labelSize, labelSize));
            //center text in box
            rowLabel.setHorizontalAlignment(SwingConstants.CENTER);
            //add row labels to labels panel
            rowLabelPanel.add(rowLabel);
            if (whichActor) {
                rowLabelPanel.setBackground(Color.ORANGE);
            } else {
                rowLabelPanel.setBackground(Color.decode("#FF990D"));
            }

        }
        // Arrange layout positions for each component
        actorPanel.add(rowLabelPanel, BorderLayout.WEST);
        actorPanel.add(lifeStatus, BorderLayout.NORTH);
        actorPanel.add(actorGrid, BorderLayout.CENTER);
        actorPanel.add(columnLabelsPanel, BorderLayout.SOUTH);
        return buttonForGrid;
    }

    /**
     * Method Name: playBackgroundMusic
     * Purpose: Enables the background music to be played when the jar file is opened.
     * Algorithm: Try catch, locate new wav File, open audio stream, loop
     *
     * @param musicFile - wav file for the background music
     */
    private void playBackgroundMusic(String musicFile) {
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

    /**
     * Method name: actionPerformed
     * Purpose: Executed when action event occurs
     * Algorithm: If else tree, determine which JButton has been clicked, call historyLog method and execute desired outcome
     * @param e the event represented as user action
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        /* Initialize GameAction (where the logic exists for test cases) */
        GameAction gameAction = new GameAction();
        Object eventSource = e.getSource();
        Clip clickClip = createAudioClip();
        int selectedDimension = (int) dimensionComboBox.getSelectedItem();

        /* If else tree, checking eventSource and if matches execute correct action */
        if (eventSource == languageButton) {
            clickClip.start();
            gameAction.historyLog(eventSource, controlPanelText);
        } else if (eventSource == designBoatPlacement) {
            clickClip.start();
            gameAction.historyLog(eventSource, controlPanelText);
            // Open design window
            gameAction.designBoatPlacement();
        } else if (eventSource == randBoatPlacement) {
            clickClip.start();
            gameAction.historyLog(eventSource, controlPanelText);
            resizeBoard(selectedDimension, userPanel, opponentPanel);
            opponentButtons = gameAction.generateBoatSize(selectedDimension, opponentButtons);
            userButtons = gameAction.generateBoatSize(selectedDimension, userButtons);
        } else if (eventSource == dimensionComboBox) {
            clickClip.start();
            resizeBoard(selectedDimension, userPanel, opponentPanel);
            gameAction.historyLog(eventSource, controlPanelText);
        } else if (eventSource == reset) {
            clickClip.start();
            gameAction.historyLog(eventSource, controlPanelText);
        } else if (eventSource == play) {
            clickClip.start();
            gameAction.historyLog(eventSource, controlPanelText);
        } else {
            clickClip.start();
            boardButtonEvent(userButtons, eventSource, controlPanelText);
            boardButtonEvent(opponentButtons, eventSource, controlPanelText);
        }
    }

    /**
     * Method Name: createAudioClip
     * Purpose: Enables button click sound effects
     * Algorithm: try catch new wav file, create new audio stream, return sound
     *
     * @return - If file is found, return click sound effect, else return null
     */
    private Clip createAudioClip() {
        //TODO: If else tree for if boat as been hit or miss
        try {
            // Create 2nd audio input stream so 2 sounds can occur at same time
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("resources/click.wav").getAbsoluteFile());
            // Get clip from audio file and open it
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            // Return audio file to be executed
            return clip;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Method Name: resizeBoard
     * Purpose:When dimension JComboBox is changed, this method clears both actor board panels, and creates new board based on the size of the dimension selected.
     * Algorithm: remove both actor panels, create new board, revalidate, repaint
     *
     * @param selectedDimension  - user determined size of board.
     * @param userBoardPanel     - user actor panel.
     * @param opponentBoardPanel - machine actor panel.
     */
    private void resizeBoard(Integer selectedDimension, JPanel userBoardPanel, JPanel opponentBoardPanel) {
        userBoardPanel.removeAll();
        opponentBoardPanel.removeAll();
        userButtons = createBoard(selectedDimension, userPanel, lifeUser, true);
        opponentButtons = createBoard(selectedDimension, opponentPanel, lifeMachine, false);
        opponentBoardPanel.revalidate();
        userBoardPanel.revalidate();
        opponentBoardPanel.repaint();
        userBoardPanel.repaint();
    }

    /**
     * Method Name: boardButtonEvent
     * Purpose: Loop through array of buttons to find user action event
     * Algorithm: For each loop, if eventSource equals instance of JButton call historyLog
     *
     * @param buttons          - either actor button array
     * @param eventSource      - Object event action
     * @param controlPanelText - JLabel passed, and then later uses .getName() to extract information
     */
    private void boardButtonEvent(JButton[][] buttons, Object eventSource, JLabel controlPanelText) {
        GameAction gameAction = new GameAction();
        for (JButton[] row : buttons) {
            for (JButton button : row) {
                if (eventSource == button) {
                    gameAction.historyLog(eventSource, controlPanelText);
                }
            }
        }
    }
}