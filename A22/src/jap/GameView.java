package jap;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.Serializable;

/**
 * Class Name: GameView
 * Method List:
 * Constants List:
 * View model, following MVC design pattern. Updates the view to user
 *
 * @author Andrew Lorimer, Solomon Thangthong
 * @version 1.1
 * @see JFrame
 * @see ActionListener
 * @see Serializable
 * @since 11.0.19
 */
public class GameView extends JFrame implements ActionListener {
    private GameController gameController;
    private JFrame designWindow;
    private JButton battleshipAbout;
    /**
     * Combobox to change GUI language.
     */
    private JComboBox<String> languageButton;
    /**
     * Combobox for changing board dimension size.
     */
    private JComboBox<Integer> dimensionComboBox;
    /**
     * Button to open new window to design boat placement.
     */
    private JButton designBoatPlacement;
    /**
     * Button to randomize both actor ship placement.
     */
    private JButton randBoatPlacement;
    private JPanel controlPanel;
    /**
     * Label for controlPanel history box.
     */
    private JLabel controlPanelText;
    private JPanel timeContainer;
    /**
     * Button to start the game.
     */
    private JButton play;
    /**
     * Button to reset the game.
     */
    private JButton reset;
    /**
     * Array of clickable buttons on user actor board.
     */
    private JButton[][] userButtons;
    /**
     * Array of clickable buttons on opponent board.
     */
    private JButton[][] opponentButtons;

    private JPanel progressPlayer1Panel;
    private JProgressBar player1Progress;
    private JProgressBar player2Progress;

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

    private JPanel designPanel;
    private JComboBox boatJComboBox;
    private JRadioButton boatVertical;
    private JRadioButton boatHorizontal;
    private JButton resetLayout;
    private JButton saveLayout;

    public GameView(GameController gameController, GameModel gameModel) {
        // Create instance Controller
        this.gameController = gameController;
        gameController.setGameView(this);

        initializeFrame();
        createPanels();
        addPanelsToMainFrame();

        // Create 2D arrays of buttons default dimension 4
        userButtons = gameController.getButtons(true);
        opponentButtons = gameController.getButtons(false);

        createPanelView(gameModel.getBoardSize(), userPanel, true, player1Progress);
        createPanelView(gameModel.getBoardSize(), opponentPanel, false, player2Progress);

        designWindow = new JFrame();

        //play background music
        //String musicFile = "resources/backgroundMusic.wav";
        //playBackgroundMusic(musicFile);
    }

    /**
     * Method Name: InitializeFrame
     * Purpose: Method to set up parameters for GUI window
     * Algorithm: Set title, size, location, default close operation
     */
    public void initializeFrame() {
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
        controlPanel = new JPanel();
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
        progressPlayer1Panel = new JPanel();
        player1Progress = new JProgressBar();
        progressPlayer1Panel.setBackground(Color.ORANGE);
        progressPlayer1Panel.setPreferredSize(new Dimension(250, 25));
        progressPlayer1Panel.add(new JLabel("Life 1"));
        progressPlayer1Panel.add(player1Progress);

        //set opponent actor life bar
        player2Progress = new JProgressBar();
        player2Progress.setBackground(Color.decode("#FF990D"));
        JButton lifeMachineHealthBar = new JButton();
        lifeMachineHealthBar.setName("Life 2");
        lifeMachineHealthBar.setPreferredSize(new Dimension(250, 25));
        lifeMachineHealthBar.addActionListener(this);
        player2Progress.add(new JLabel("Life 2"));
        player2Progress.add(lifeMachineHealthBar);
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
    private void createPanelView(int dimension, JPanel actorPanel, Boolean whichActor, JProgressBar lifeStatus) {
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
        JButton[][] buttonForGrid;

        if (whichActor) {
            buttonForGrid = userButtons;
        } else {
            buttonForGrid = opponentButtons;
        }
        // Only loop through instance of Buttons from GameModel to assign action listner
        for (int i = 0; i < buttonForGrid.length; i++) {
            for (int j = 0; j < buttonForGrid[i].length; j++) {
                JButton button = buttonForGrid[i][j];
                button.addActionListener(this);
                button.setPreferredSize(new Dimension(buttonSize, buttonSize));
                actorGrid.add(button);
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
    }

    /**
     * Method Name: updateBoard
     * Purpose: Is called in GameController, When dimension JComboBox is changed, this method clears both actor board panels, and creates new board based on the size of the dimension selected.
     * Algorithm: remove both actor panels, create new board, revalidate, repaint
     *
     * @param selectedDimension  - user determined size of board.
     * @param userBoardPanel     - user actor panel.
     * @param opponentBoardPanel - machine actor panel.
     */
    protected void updateBoard(Integer selectedDimension, JPanel userBoardPanel, JPanel opponentBoardPanel) {

        userBoardPanel.removeAll();
        opponentBoardPanel.removeAll();

        // Call Controller to grab GameModel JButtons
        userButtons = gameController.getButtons(true);
        opponentButtons = gameController.getButtons(false);

        // Create Panels
        createPanelView(selectedDimension, userPanel, true, player1Progress);
        createPanelView(selectedDimension, opponentPanel, false, player2Progress);

        // Revalidate and Repaint
        opponentBoardPanel.revalidate();
        userBoardPanel.revalidate();
        opponentBoardPanel.repaint();
        userBoardPanel.repaint();
    }
    protected void designBoatWindow() {
        /* New JFrame for pop-up window to design board */
        designWindow.setSize(550, 550);
        designWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        designWindow.setLocationRelativeTo(null);
        designWindow.setVisible(true);
    }

    protected void designBoatPlacement(Integer size){
        designPanel = new JPanel(new BorderLayout());
        JPanel bottomPanel = new JPanel();
        userButtons = gameController.getButtons(true);
        JLabel boatLabel = new JLabel("Boat:");
        JLabel directionLabel = new JLabel("Direction:");
        JLabel horizontalLabel = new JLabel("H");
        JLabel verticalLabel = new JLabel("V");

        resetLayout = new JButton("Reset Layout");
        saveLayout = new JButton("Save");

        int buttonSize = Math.min(50, 200 / size); // Adjust the button size based on dimension
        JPanel actorGrid = new JPanel(new GridLayout(size * 2, size * 2));

        // Only loop through instance of Buttons from GameModel to assign action listner
        for (int i = 0; i < userButtons.length; i++) {
            for (int j = 0; j < userButtons[i].length; j++) {
                JButton button = userButtons[i][j];
                button.addActionListener(this);
                button.setPreferredSize(new Dimension(buttonSize, buttonSize));
                actorGrid.add(button);
            }
        }
        boatJComboBox = new JComboBox();
        boatJComboBox.setSize(200,100);
        boatVertical = new JRadioButton();
        boatHorizontal = new JRadioButton();
        designPanel.add(actorGrid);
        bottomPanel.add(boatLabel);
        bottomPanel.add(boatJComboBox);
        bottomPanel.add(directionLabel);
        bottomPanel.add(boatHorizontal);
        bottomPanel.add(horizontalLabel);
        bottomPanel.add(boatVertical);
        bottomPanel.add(verticalLabel);
        bottomPanel.add(resetLayout);
        bottomPanel.add(saveLayout);

        designWindow.add(designPanel, BorderLayout.CENTER);
        designWindow.add(bottomPanel, BorderLayout.SOUTH);
    }

    protected void updateControlPanelText(String text) {
        controlPanelText.setText(text);
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
     *
     * @param e the event represented as user action
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        /* Initialize GameController (Goal is to pass input to controller, then controller passes to Model) */
        Object eventSource = e.getSource();
        Clip clickClip = createAudioClip();
        int selectedDimension = (int) dimensionComboBox.getSelectedItem();

        /* If else tree, checking eventSource and if matches execute correct action */
        if (eventSource == languageButton) {
            clickClip.start();
            gameController.historyLog(eventSource, controlPanelText);
        } else if (eventSource == designBoatPlacement) {
            clickClip.start();
            gameController.historyLog(eventSource, controlPanelText);
            // Open design window
            gameController.designBoatPlacement();
        } else if (eventSource == randBoatPlacement) {
            //clickClip.start();
            gameController.historyLog(eventSource, controlPanelText);

            gameController.updateModelViewBoard(selectedDimension, userPanel, opponentPanel, userButtons, opponentButtons);

            // Need to see if GameModel Buttons are updated with Boat
            gameController.randomBoatPlacement(opponentPanel, false);
            gameController.randomBoatPlacement(userPanel, true);

            createPanelView(selectedDimension, userPanel, true, player1Progress);
            createPanelView(selectedDimension, opponentPanel, false, player2Progress);

        } else if (eventSource == dimensionComboBox) {
            clickClip.start();
            // updateModelViewBoard was changeDimensions
            // resizeBoard and change dimension were essentially the same in previous iteration
            gameController.updateModelViewBoard(selectedDimension, userPanel, opponentPanel, userButtons, opponentButtons);
            gameController.historyLog(eventSource, controlPanelText);
        } else if (eventSource == reset) {
            clickClip.start();
            gameController.historyLog(eventSource, controlPanelText);
        } else if (eventSource == play) {
            clickClip.start();
            gameController.historyLog(eventSource, controlPanelText);
        } else {
            clickClip.start();
            gameController.boardButtonEvent(userButtons, eventSource, controlPanelText);
            gameController.boardButtonEvent(opponentButtons, eventSource, controlPanelText);
        }
    }
}
