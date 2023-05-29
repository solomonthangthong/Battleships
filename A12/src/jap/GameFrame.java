package jap;

import javax.swing.*;
import java.awt.*;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.io.File;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//imports for sound effects
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 * The main frame of the Battleship application.
 */
public class GameFrame extends JFrame implements ActionListener {
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
     * Button for life status.
     */
    private JButton lifeUser;

    /**
     * Creates a new instance of the GameFrame class.
     */
    public GameFrame() {
        initializeFrame();
        createPanels();
        addPanelsToMainFrame();
        //update create user board and create opponent board to take in result of DIM formula
        userButtons = createBoard(4, userPanel, true);
        opponentButtons = createBoard(4, opponentPanel, false);
        //play background music
        String musicFile = "resources/backgroundMusic.wav";
        playBackgroundMusic(musicFile);
    }

    /**
     * Method to set up parameters for GUI window
     */
    private void initializeFrame() {
        setTitle("Battleship");
        setSize(1280, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * Creates and configure panels for user interface.
     * Called internally for initialization.
     */
    private void createPanels() {
        selectionPanel = new JPanel();
        selectionPanel.setBackground(Color.decode("#19A7FF"));
        /* Potentially do not need this border, if left and right sides use Preferred Size */
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
        languageMenu.setBackground(Color.decode("#19A7FF"));
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
        designOptions.setBackground(Color.decode("#19A7FF"));
        designOptions.add(designBoatPlacement);
        designOptions.add(randBoatPlacement);
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

        //set opponent life bar
        lifeUser = new JButton("Life");
        lifeUser.setName("Life");
        lifeUser.addActionListener(this);

        userPanel = new JPanel();
        userPanel.setPreferredSize(new Dimension(520, 119));
        userPanel.setBackground(Color.ORANGE);

    }

    /**
     * Add created panels into the main frame.
     */
    private void addPanelsToMainFrame() {
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(userPanel, BorderLayout.WEST);
        contentPane.add(selectionPanel, BorderLayout.CENTER);
        contentPane.add(opponentPanel, BorderLayout.EAST);
    }

    /**
     * Creates clickable buttons based on dimension size. Player can click the button and attempt to locate enemy ship
     * @param dimension - Board dimension size, default is 4
     * @param actorPanel - User or Opponent JPanel
     * @param whichActor - True = user actor, false = machine actor
     * @return - Button Array
     */
    public JButton[][] createBoard(int dimension, JPanel actorPanel, Boolean whichActor) {
        int buttonSize = Math.min(50, 200 / dimension); // Adjust the button size based on dimension
        int labelSize = Math.min(50, 200 / dimension); // Adjust the button size based on dimension

        //manually manipulate dimensions for now - update input param dimension with formula result
        dimension = dimension * 2;
        int numRows = dimension;
        int numCols = dimension;
        String actor1 = "Pos ";
        String actor2 = "Opp Pos ";
        JPanel actorGrid = new JPanel(new GridLayout(numRows, numCols));
        JButton[][] buttonForGrid = new JButton[numRows][numCols];
        //create the buttons in a for loop
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                /* Find a better function to use aside from this name, because this names the buttons but the history track almost there */
                JButton userButton = new JButton();
                if (whichActor) {
                    userButton.setName(actor1 + (i + 1) + "," + (j + 1));
                } else {
                    userButton.setName(actor2 + (i + 1) + "," + (j + 1));
                }
                //add action lister for every button
                userButton.addActionListener(this);
                userButton.setPreferredSize(new Dimension(buttonSize, buttonSize));
                if (whichActor) {
                    userButton.setBackground(Color.blue);
                } else {
                    userButton.setBackground(Color.red);
                }
                actorGrid.add(userButton);
                //assign the button in the array
                buttonForGrid[i][j] = userButton;
            }
        }

        //make a new column panel to hold column label
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
        }
        //add row and column label panels and position west(left) and south(bottom)
        actorPanel.add(rowLabelPanel);
        //add the user selection hit box and center it
        actorPanel.add(actorGrid, BorderLayout.CENTER);
        actorPanel.add(columnLabelsPanel, BorderLayout.CENTER);
        //userBoardPanel.add(lifeUser,BorderLayout.CENTER);
        return buttonForGrid;
    }

    /**
     * @param musicFile
     */
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

    /**
     * Executed when action event occurs
     * @param e the event represented as user action
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        /*Initialize GameAction (where the logic exists for test cases) */
        GameAction gameAction = new GameAction();
        Object eventSource = e.getSource();
        Clip clickClip = createAudioClip();

        if (eventSource == languageButton) {
            clickClip.start();
            gameAction.historyLog(eventSource, controlPanelText);
        } else if (eventSource == designBoatPlacement) {
            clickClip.start();
            gameAction.historyLog(eventSource, controlPanelText);
            //pop up box
            gameAction.designBoatPlacement();
        } else if (eventSource == randBoatPlacement) {
            clickClip.start();
            gameAction.historyLog(eventSource, controlPanelText);
            gameAction.randBoatPlacement();
        } else if (eventSource == dimensionComboBox) {
            int selectedDimension = (int) dimensionComboBox.getSelectedItem();
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
     * @return
     */
    //later we can pass parameter to specify click sound or blast sound from a strike on selection hit box,
    private Clip createAudioClip() {
        try {
            //create 2nd audio input stream so 2 sounds can occur at same time
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("resources/click.wav").getAbsoluteFile());
            //get clip from audio file and open it
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            //return the click sound
            return clip;
        } catch (Exception e) {
            e.printStackTrace();
        }
        //return nothing if there is error
        return null;
    }

    /**
     * When dimension JComboBox is changed, this method clears both actor board panels, and creates new board
     * based on the size of the dimension selected.
     * @param selectedDimension - user determined size of board.
     * @param userBoardPanel - user actor panel.
     * @param opponentBoardPanel - machine actor panel.
     */
    private void resizeBoard(Integer selectedDimension, JPanel userBoardPanel, JPanel opponentBoardPanel) {
        userBoardPanel.removeAll();
        opponentBoardPanel.removeAll();
        userButtons = createBoard(selectedDimension, userPanel, true);
        opponentButtons = createBoard(selectedDimension, opponentPanel, false);
        opponentBoardPanel.revalidate();
        userBoardPanel.revalidate();
        opponentBoardPanel.repaint();
        userBoardPanel.repaint();
    }

    /**
     * Loop through array of buttons to find user action event
     * Created method to reduce for loop duplication
     * @param buttons - either actor button array
     * @param eventSource - Object event action
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
