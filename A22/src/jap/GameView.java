package jap;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

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
    private final Menu menuBar;
    private JButton battleshipAbout;
    /**
     * Combobox to change GUI language.
     */
    private JComboBox<String> languageButton;
    private JLabel languageLabel;

    private ResourceBundle resourceBundle;

    /**
     * Combobox for changing board dimension size.
     */
    private JComboBox<Integer> dimensionComboBox;

    private JLabel dimensionsLabel;
    /**
     * Button to open new window to design boat placement.
     */
    private JButton designBoatPlacement;
    // Maybe use getter and setter?
    protected JFrame designWindow;

    /**
     * Button to randomize both actor ship placement.
     */
    private JButton randBoatPlacement;

    /**
     * Label for controlPanel history box.
     */
    protected JLabel controlPanelText;
    private int remainingBoats;
    private int count;
    private Timer timer;
    JLabel remainingBoat;
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
    private JPanel progressPlayer2Panel;
    private JProgressBar player1Progress;
    private JLabel player1Life;
    private JProgressBar player2Progress;
    private JLabel player2Life;
    protected static int boatSizeSelectorValue;

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

    private Boolean designSaved;
    protected Boolean randomizedClick;
    // Stores the items
    private DefaultComboBoxModel<Integer> comboBoxModel;
    private JComboBox<Integer> boatSizeSelector;
    // List to hold boats, inner list # of JButtons per size
    private List<List<Boat>> designBoatList;

    private JLabel timeLabel;

    private JRadioButton boatVertical;
    private JRadioButton boatHorizontal;
    private JButton resetLayout;
    private JButton saveLayout;

    private JLabel boatLabel;
    private JLabel directionLabel;

    private Color globalColour;
    private ColorChooser colorPanel;
    protected boolean playClicked;

    public GameView() {
        Splash s = new Splash();
        s.show(0);
        menuBar = new Menu();

        UIManager.put("ProgressBar.foreground", Color.decode("#9de47c"));
        UIManager.put("ProgressBar.background", Color.lightGray);
        UIManager.put("ProgressBar.selectionForeground", Color.decode("#f56a4d"));

        playClicked = false;
        randomizedClick = false;
        designSaved = false;


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
        setJMenuBar(menuBar.getTheMenuBar());
        setSize(1280, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


    }

    protected void registerGameController(GameController controller) {
        this.gameController = controller;
    }

    protected void resetGame(int selectedDimension) {
        // reset user actor
        gameController.resetGame(true, userPanel, opponentPanel);
        // reset user opponent
        gameController.resetGame(false, userPanel, opponentPanel);
        // Autofill Machine boats after new state
        gameController.randomBoatPlacement(false);
        createPanelView(selectedDimension, userPanel, true, progressPlayer1Panel);
        createPanelView(selectedDimension, opponentPanel, false, progressPlayer2Panel);
        gameController.disableUserButtons(true);
        updateProgressBar();
    }

    private void showAboutDialog() {
        String description =
                "Battleship is a strategic warfare game played on a grid-based board.\n\n"
                        + "The player can randomize ships or manually place them before starting the game.\n\n"
                        + "The player and Robot take turns guessing the coordinates to target and sink their opponent's ships.\n\n"
                        + "The goal of battleship is to destroy all of the opponent's ships before they destroy yours.\n\n"
                        + "Have fun";
//add image from images folder
        ImageIcon icon = new ImageIcon("images/game_about.jpg");
//display message and icon
        JOptionPane.showMessageDialog(this, description, "About Battleship", JOptionPane.INFORMATION_MESSAGE, icon);
    }


    /**
     *
     */
    protected static class Splash {
        public void show(int flag) {
            JWindow window = new JWindow();
            if (flag == 0) {
                window.getContentPane().add(new JLabel("", new ImageIcon("images/game_about.jpg"), SwingConstants.CENTER));
            }
            //win splash image
            if (flag == 1) {
                window.getContentPane().add(new JLabel("", new ImageIcon("images/game_winner.jpg"), SwingConstants.CENTER));
            }
            //lose splash image
            if (flag == 2) {
                window.getContentPane().add(new JLabel("", new ImageIcon("images/game_lost.jpg"), SwingConstants.CENTER));
            }

            window.setBounds(500, 150, 300, 200);
            window.setLocationRelativeTo(null);
            window.setVisible(true);
            window.setAlwaysOnTop(true);
            int delay = 1500;
            Timer timer = new Timer(delay, c -> {
                window.setVisible(false);
                window.dispose();
                System.out.println("here");
            });
            timer.setRepeats(false);
            timer.start();
        }
    }

    /**
     *
     */
    protected class Menu extends JFrame implements ActionListener, MenuListener {
        private final JMenuBar menu;

        /**
         *
         */
        public Menu() {
            menu = new JMenuBar();

            // Example found in Week 5 JavaSwing labs from Professor
            menu.add(buildMenu("Game",
                    new Object[]{new JMenuItem("New", new ImageIcon("images/iconload.gif")),
                            new JMenuItem("Solution", new ImageIcon("images/iconsol.gif")),
                            new JMenuItem("Exit", new ImageIcon("images/iconext.gif"))
                    }, this));

            menu.add(buildMenu("Help",
                    new Object[]{new JMenuItem("Colours", new ImageIcon("images/iconcol.gif")),
                            new JMenuItem("About", new ImageIcon("images/iconabt.gif")),
                    }, this));
        }

        /**
         * Creates a menu with menu items.
         *
         * @param parent       - if the parent is an instance of JMenu it adds items to the menu. if the parent is a string it creates the menu and then adds items to the menu.
         * @param items        - list of references to menu items names (strings). If the references null, a separator is added.
         * @param eventHandler - event handler for the menu items.
         * @return - a reference to JMenu with optional menu items. null if parent is not an instance of String or JMenu, or items is null
         */
        private JMenu buildMenu(Object parent, Object[] items, Object eventHandler) {
            JMenu m;
            if (parent instanceof JMenu)
                m = (JMenu) parent;
            else if (parent instanceof String)
                m = new JMenu((String) parent);
            else
                return null;
            if (items == null)
                return null;
            for (Object item : items) {
                if (item == null) {
                    m.addSeparator();
                } else {
                    m.add(buildMenuItem(item, eventHandler));
                }
            }
            return m;
        }

        /**
         * Creates a menu item.
         * handler. if the parent is a string it creates the menu and then adds an event handler.
         *
         * @param item         - Object from Menu bar
         * @param eventHandler - event handler for the menu items. Must be of type ActionListener
         * @return a reference to JMenuItem. null if parent is not an instance of
         * String or JMenuItem, or the event handler is an instance of
         */
        private JMenuItem buildMenuItem(Object item, Object eventHandler) {
            JMenuItem r;
            if (item instanceof String)
                r = new JMenuItem((String) item);
            else if (item instanceof JMenuItem)
                r = (JMenuItem) item;
            else
                return null;

            if (eventHandler instanceof ActionListener)
                r.addActionListener((ActionListener) eventHandler);
            else
                return null;
            return r;
        }

        public JMenuBar getTheMenuBar() {
            return menu;
        }


        @Override
        public void actionPerformed(ActionEvent e) {
            String arg = e.getActionCommand();
            Object eventSource = e.getSource();
            int selectedDimension = (int) dimensionComboBox.getSelectedItem();

            switch (arg) {
                case "New":
                    resetGame(selectedDimension);
                    enableControlPanelButtons();
                    break;
                case "Solution":
                    // TODO set opponent board to visible
                    gameController.historyLog(eventSource, controlPanelText);
                    gameController.setBoatVisible();
                    break;
                case "Colours":
                    gameController.historyLog(eventSource, controlPanelText);
                    colorPanel = new ColorChooser();
                    colorPanel.colorGUI();
                    languageChanger();
                    break;
                case "About":
                    gameController.historyLog(eventSource, controlPanelText);
                    showAboutDialog();
                    //the call is just  in action performed above menus
                    break;
            }
        }

        @Override
        public void menuSelected(MenuEvent e) {
            // Empty implementation
        }

        @Override
        public void menuDeselected(MenuEvent e) {
            // Empty implementation
        }

        @Override
        public void menuCanceled(MenuEvent e) {
            // Empty implementation
        }
    }

    /**
     * ColorChooser Class
     */
    protected class ColorChooser extends JPanel implements ActionListener, ChangeListener {
        private JFrame frame;
        private final JColorChooser tcc;
        protected JButton unselectedColourButton;
        protected JButton waterColourButton;
        protected JButton boatColourButton;
        protected JPanel unselectedColour;
        protected JPanel waterColour;
        protected JPanel boatColour;
        private JButton lastClickedButton;
        private final JButton saveColour;
        private final JButton cancelColour;
        private final JButton resetColour;
        private Color unselectedColorValue;
        private Color waterColorValue;
        private Color shipColorValue;
        private Color selectedColor;

        /**
         * Constructor for ColorChooser Class
         */
        protected ColorChooser() {
            super(new BorderLayout());
            LineBorder lineBorder = new LineBorder(Color.BLACK, 2);

            // Button options for Color Chooser
            unselectedColourButton = new JButton();
            waterColourButton = new JButton();
            boatColourButton = new JButton();
            saveColour = new JButton();
            cancelColour = new JButton();
            resetColour = new JButton();


            // Panels
            unselectedColour = new JPanel();
            waterColour = new JPanel();
            boatColour = new JPanel();

            // Setting Background Colour
            unselectedColour.setBackground(Color.decode("#f56a4d"));
            waterColour.setBackground(Color.decode("#008fa2"));
            boatColour.setBackground(Color.decode("#db9c59"));

            // Setting border
            unselectedColour.setBorder(lineBorder);
            waterColour.setBorder(lineBorder);
            boatColour.setBorder(lineBorder);

            // Set size
            unselectedColour.setPreferredSize(unselectedColourButton.getPreferredSize());
            waterColour.setPreferredSize(waterColourButton.getPreferredSize());
            boatColour.setPreferredSize(boatColourButton.getPreferredSize());

            // Contain items into a Panel
            JPanel unselectedColourPanel = new JPanel(new BorderLayout());
            unselectedColourPanel.add(unselectedColour, BorderLayout.CENTER);
            unselectedColourPanel.add(unselectedColourButton, BorderLayout.SOUTH);

            // Contain items into a Panel
            JPanel waterColourPanel = new JPanel(new BorderLayout());
            waterColourPanel.add(waterColour, BorderLayout.CENTER);
            waterColourPanel.add(waterColourButton, BorderLayout.SOUTH);

            // Contain items into a Panel
            JPanel boatColourPanel = new JPanel(new BorderLayout());
            boatColourPanel.add(boatColour, BorderLayout.CENTER);
            boatColourPanel.add(boatColourButton, BorderLayout.SOUTH);

            // Contain items into a Panel
            JPanel colourModel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
            colourModel.add(unselectedColourPanel);
            colourModel.add(waterColourPanel);
            colourModel.add(boatColourPanel);
            colourModel.setBorder(BorderFactory.createTitledBorder("Colour Model"));

            JPanel userAction = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
            userAction.add(saveColour);
            userAction.add(cancelColour);
            userAction.add(resetColour);

            // Add ActionListeners
            unselectedColourButton.addActionListener(this);
            waterColourButton.addActionListener(this);
            boatColourButton.addActionListener(this);
            saveColour.addActionListener(this);
            cancelColour.addActionListener(this);
            resetColour.addActionListener(this);

            tcc = new JColorChooser();
            tcc.getSelectionModel().addChangeListener(this);

            add(colourModel, BorderLayout.NORTH);
            add(tcc, BorderLayout.CENTER);
            add(userAction, BorderLayout.SOUTH);

            unselectedColourButton.doClick();

        }

        /**
         * Generic method to setText for JButton/JLabels from language.properties
         *
         * @param component - Control Panel JButtons or Labels
         * @param name      - Setting Name in English or French
         */
        protected void setTextForComponent(AbstractButton component, String name) {
            component.setText(name);
        }

        protected void colorGUI() {
            //Create and set up the window.
            frame = new JFrame("Choose Colour");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            //Create and set up the content pane.
            frame.setContentPane(this);
            setOpaque(true); //content panes must be opaque
            frame.setLocationRelativeTo(null);

            //Display the window.
            frame.pack();
            frame.setVisible(true);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton source = (JButton) e.getSource();

            if (source == unselectedColourButton || source == waterColourButton || source == boatColourButton) {
                // NOT REDUNDANT ENSURES JBUTTON IS NOT SAVE,CANCEL, RESET OPTIONS
                JButton clickedButton = source;

                if (lastClickedButton == clickedButton) {
                    // If the same button is clicked again, do nothing
                    return;
                }
                lastClickedButton = clickedButton;

            } else if (source == saveColour) {
                int selectedDimension = (int) dimensionComboBox.getSelectedItem();

                if (unselectedColorValue != null && waterColorValue != null && shipColorValue != null){
                    userButtons = gameController.getButtons(true);
                    gameController.setColorVariables(unselectedColorValue, waterColorValue, shipColorValue);
                    gameController.changeBoatColor(userButtons, userPanel, true, unselectedColorValue);
                    gameController.changeBoatColor(opponentButtons, opponentPanel, false, unselectedColorValue);

                    createPanelView(selectedDimension, userPanel, true, progressPlayer1Panel);
                    createPanelView(selectedDimension, opponentPanel, false, progressPlayer2Panel);

                    if (frame != null) {
                        frame.dispose();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Please select all three options before saving.", "Warning", JOptionPane.WARNING_MESSAGE);
                }



            } else if (source == cancelColour) {
                if (frame != null) {
                    frame.dispose();
                }
            } else if (source == resetColour) {
                //TODO reset selection?
                unselectedColour.setBackground(Color.decode("#f56a4d"));
                waterColour.setBackground(Color.decode("#008fa2"));
                boatColour.setBackground(Color.decode("#db9c59"));
                tcc.setColor(Color.decode("#f56a4d"));
                gameController.setColorVariables(unselectedColour.getBackground(), waterColour.getBackground(), boatColour.getBackground());
            }
        }

        @Override
        public void stateChanged(ChangeEvent e) {
            // Update the color of the selected panel
            selectedColor = tcc.getSelectionModel().getSelectedColor();

            if (lastClickedButton == unselectedColourButton) {
                unselectedColour.setBackground(selectedColor);
                unselectedColorValue = selectedColor;
            } else if (lastClickedButton == waterColourButton) {
                waterColour.setBackground(selectedColor);
                waterColorValue = selectedColor;
            } else if (lastClickedButton == boatColourButton) {
                boatColour.setBackground(selectedColor);
                shipColorValue = selectedColor;
            }
        }
    }

    protected JLabel getControlPanelText() {
        return controlPanelText;
    }

    protected JProgressBar getPlayer1Progress() {
        return player1Progress;
    }

    protected JProgressBar getPlayer2Progress() {
        return player2Progress;
    }

    protected JComboBox getDimensionComboBox() {
        return dimensionComboBox;
    }

    protected Boolean getRandomizedClick() {
        return randomizedClick;
    }

    protected Boolean getDesignSaved() {
        return designSaved;
    }

    protected void setDesignSaved(Boolean saved) {
        this.designSaved = saved;
    }

    protected Boolean getPlayClicked() {
        return playClicked;
    }

    protected void setPlayClicked(Boolean play) {
        this.playClicked = play;
    }

    protected void setRandomizedClick(Boolean random) {
        this.randomizedClick = random;
    }


    protected void setPlayRandomDesignBooleans(Boolean play, Boolean randomize, Boolean design) {
        this.playClicked = play;
        this.randomizedClick = randomize;
        this.designSaved = design;
    }

    /**
     * Pass GameModel passes and set this grid instances to GameModel
     *
     * @param actor - User player if true, Machine if false
     * @param board - 2D array JButton grid
     */
    protected void setBoardButtons(Boolean actor, JButton[][] board) {
        if (actor) {
            userButtons = board;
        } else {
            opponentButtons = board;
        }
    }

    protected JPanel getUserPanel() {
        return userPanel;
    }

    protected JPanel getOpponentPanel() {
        return opponentPanel;
    }

    protected JPanel getProgressPlayer1Panel() {
        return progressPlayer1Panel;
    }

    protected JPanel getProgressPlayer2Panel() {
        return progressPlayer2Panel;
    }

    protected JButton[][] getOpponentButtons() {
        return opponentButtons;
    }

    protected JButton[][] getUserButtons() {
        return userButtons;
    }

    protected JComboBox getBoatSizeSelector() {
        return boatSizeSelector;
    }

    protected void setBoatSizeSelectorValue(Integer selected) {
        this.boatSizeSelectorValue = selected;
    }


    protected JProgressBar getProgressBar(Boolean actor) {
        if (actor) {
            return player1Progress;
        } else {
            return player2Progress;
        }
    }

    private void startTimer(JLabel timeDisplay) {
        count = 0; // Initialize count to zero
        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                count++;
                timeDisplay.setText(String.valueOf(count));
            }
        });
        timer.start();
    }

    private void resetTimer() {
        count = 0;
        if (timer != null) {
            timer.stop();
            timer.restart();
        }
    }

    /**
     * Method Name: createPanels
     * Purpose: This method creates and configure panels for user interface and called internally for initialization.
     * Algorithm: Create selectionPanel, all control panel buttons, create both actor panel, and health bars.
     */
    protected void createPanels() {

        // Control Panel
        selectionPanel = new JPanel();
        selectionPanel.setBackground(Color.decode("#feefec"));
        selectionPanel.setBorder(BorderFactory.createEmptyBorder(0, 69, 0, 69));

        // Load and display the logo
        ImageIcon image = new ImageIcon("images/logo.png");
        if (new File("images/logo.png").exists()) {
            JButton imageLogo = new JButton(image);
            imageLogo.setBackground(Color.white);
            selectionPanel.add(imageLogo);
        } else {
            System.out.println("Image not found");
        }

        // Language dropdown
        String[] languages = {"English", "French"};
        languageButton = new JComboBox<>(languages);
        languageButton.setSelectedItem("English");
        languageButton.addActionListener(e -> {
            Object eventSource = e.getSource();
            gameController.handleLanguageButton(eventSource, controlPanelText);
        });
        languageLabel = new JLabel();
        JPanel languageMenu = new JPanel();
        languageMenu.setBackground(Color.decode("#feefec"));
        languageMenu.add(languageLabel);
        languageMenu.add(languageButton);
        selectionPanel.add(languageMenu);

        // Design and Randomize buttons
        designBoatPlacement = new JButton();
        designBoatPlacement.setName("Design");
        designBoatPlacement.addActionListener(e -> {
            Object eventSource = e.getSource();
            gameController.handleDesignBoatPlacement(eventSource, controlPanelText);
        });
        designBoatPlacement.setBackground(Color.white);
        randBoatPlacement = new JButton();
        randBoatPlacement.setName("Randomize");
        randBoatPlacement.addActionListener(e -> {
            Object eventSource = e.getSource();
            gameController.handleRandBoatPlacement(eventSource, controlPanelText);
        });
        randBoatPlacement.setBackground(Color.white);
        JPanel designOptions = new JPanel();
        designOptions.setBackground(Color.decode("#feefec"));
        designOptions.add(designBoatPlacement);
        designOptions.add(randBoatPlacement);
        selectionPanel.add(designOptions);

        // Dimensions dropdown
        Integer[] dimensions = {4, 5, 6, 7, 8, 9, 10};
        dimensionComboBox = new JComboBox(dimensions);
        dimensionComboBox.setSelectedItem(0);
        dimensionComboBox.addActionListener(e -> {
            Object eventSource = e.getSource();
            gameController.handleDimensionComboBox(eventSource, controlPanelText);
        });
        JPanel dimensionsPanel = new JPanel();
        dimensionsPanel.setBackground(Color.decode("#feefec"));
        dimensionsLabel = new JLabel();
        dimensionsPanel.add(dimensionsLabel);
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
        timeLabel = new JLabel();
        JLabel timeDisplay = new JLabel();
        JPanel timeContainer = new JPanel();
        timeDisplay.setBorder(BorderFactory.createRaisedBevelBorder());
        timeDisplay.setPreferredSize(new Dimension(55, 30));
        timeDisplay.setAlignmentX(Component.CENTER_ALIGNMENT);
        timeContainer.add(timeLabel);
        timeContainer.add(timeDisplay);
        timeContainer.setAlignmentX(Component.CENTER_ALIGNMENT);
        timeContainer.setBackground(Color.decode("#feefec"));
        selectionPanel.add(timeContainer);
//start timer

        startTimer(timeDisplay);

        /* Creates reset button */
        reset = new JButton();
        reset.setName("Reset");
        reset.addActionListener(e -> {
            Object eventSource = e.getSource();
            gameController.handleResetButton(eventSource, controlPanelText);
            //reset clock
            resetTimer();


        });
        reset.setBackground(Color.white);
        selectionPanel.add(reset);

        /* Creates play button */
        play = new JButton();
        play.setBackground(Color.white);
        play.setName("Play");
        play.addActionListener(e -> {
            Object eventSource = e.getSource();
            gameController.handlePlayButton(eventSource, controlPanelText);
        });
        selectionPanel.add(play);

        // Creates right side panel container
        opponentPanel = new JPanel(new BorderLayout());
        opponentPanel.setPreferredSize(new Dimension(520, 119));
        opponentPanel.setBackground(Color.decode("#feefec"));

        // Creates left side panel container
        userPanel = new JPanel(new BorderLayout());
        userPanel.setPreferredSize(new Dimension(520, 119));
        userPanel.setBackground(Color.decode("#feefec"));

        //set user actor life bar
        progressPlayer1Panel = new JPanel();
        player1Progress = new JProgressBar();
        player1Progress.setPreferredSize(new Dimension(350, 35));
        player1Progress.setStringPainted(true);
        //TODO Decrement colour size instead of StringPainted value
        progressPlayer1Panel.setBackground(Color.decode("#feefec"));
        progressPlayer1Panel.setPreferredSize(new Dimension(500, 50));
        player1Life = new JLabel();
        progressPlayer1Panel.add(player1Life);
        progressPlayer1Panel.add(player1Progress);

        //set opponent actor life bar
        progressPlayer2Panel = new JPanel();
        player2Progress = new JProgressBar();
        player2Progress.setPreferredSize(new Dimension(350, 35));
        player2Progress.setStringPainted(true);
        progressPlayer2Panel.setBackground(Color.decode("#feefec"));
        progressPlayer2Panel.setPreferredSize(new Dimension(500, 50));
        player2Life = new JLabel();
        progressPlayer2Panel.add(new JLabel("Life 2"));
        progressPlayer2Panel.add(player2Progress);

        // Environment variables from properties to switch Button language text
        updateLanguage(Locale.getDefault());
        updateProgressBar();
    }

    /**
     * @param locale - Property file for environment variables
     */
    private void updateLanguage(Locale locale) {
        // Try to load .properties file
        try {
            resourceBundle = ResourceBundle.getBundle("jap.language", locale);
        } catch (MissingResourceException e) {
            // Fallback to the default locale (e.g., English)
            resourceBundle = ResourceBundle.getBundle("jap.language", Locale.ENGLISH);
        }
        // Call method to set text for buttons
        updateText();
    }

    /**
     * Set text for JLabels and Buttons for language change
     */
    protected void updateText() {

        String[] keys = {"languageLabel", "designBoatPlacement", "randBoatPlacement", "dimensionsLabel", "timeLabel", "reset", "play", "player1Life", "player2Life", "unselectedColourButton", "waterColourButton", "boatColourButton", "saveColour", "cancelColour", "resetColour", "boatLabel", "directionLabel", "resetLayout", "saveLayout", "remainderString"};
        String[] buttonName = new String[keys.length];

        for (int i = 0; i < keys.length; i++) {
            buttonName[i] = resourceBundle.getString(keys[i]);
        }
        languageLabel.setText(buttonName[0]);
        designBoatPlacement.setText(buttonName[1]);
        randBoatPlacement.setText(buttonName[2]);
        dimensionsLabel.setText(buttonName[3]);
        timeLabel.setText(buttonName[4]);
        reset.setText(buttonName[5]);
        play.setText(buttonName[6]);
        player1Life.setText(buttonName[7]);
        player2Life.setText(buttonName[8]);

        if (colorPanel != null) {
            colorPanel.setTextForComponent(colorPanel.unselectedColourButton, buttonName[9]);
            colorPanel.setTextForComponent(colorPanel.waterColourButton, buttonName[10]);
            colorPanel.setTextForComponent(colorPanel.boatColourButton, buttonName[11]);
            colorPanel.setTextForComponent(colorPanel.saveColour, buttonName[12]);
            colorPanel.setTextForComponent(colorPanel.cancelColour, buttonName[13]);
            colorPanel.setTextForComponent(colorPanel.resetColour, buttonName[14]);
        }

        if (designWindow != null) {
            boatLabel.setText(buttonName[15]);
            directionLabel.setText(buttonName[16]);
            resetLayout.setText(buttonName[17]);
            saveLayout.setText(buttonName[18]);
            remainingBoat.setText(buttonName[19] + remainingBoats);
        }


    }

    protected void updateProgressBar() {
        int selectedDimensions = (int) dimensionComboBox.getSelectedItem();

        // (D*(D+1)*(D+2))/6 Formula
        int sum = 0;
        for (int i = 1; i <= selectedDimensions; i++) {
            sum += (selectedDimensions - i + 1) * i;
        }

        player1Progress.setMaximum(sum);
        player1Progress.setValue(sum);
        player1Progress.setMinimum(0);
        player1Progress.revalidate();
        player1Progress.repaint();

        player2Progress.setMaximum(sum);
        player2Progress.setValue(sum);
        player2Progress.setMinimum(0);
        player2Progress.revalidate();
        player2Progress.repaint();
    }

    /**
     * Method Name: createBoard
     * Purpose: Creates clickable buttons based on dimension size. Player can click the button and attempt to locate enemy ship
     * Algorithm: Create button sizes, define variables, nested loop to create 2D array of JButtons, then create col and row labels.
     *
     * @param dimension  - Board dimension size, default is 4
     * @param actorPanel - User or Opponent JPanel
     * @param whichActor - True = user actor, false = machine actor
     */
    protected void createPanelView(int dimension, JPanel actorPanel, Boolean whichActor, JPanel lifeStatus) {
        int buttonSize = Math.min(50, 200 / dimension); // Adjust the button size based on dimension
        int labelSize = Math.min(50, 200 / dimension); // Adjust the button size based on dimension

        // Multiply dimensions by two. Intended result is if board is size 4 make it 8 by 8 grid
        dimension = dimension * 2;
        int numRows = dimension;
        int numCols = dimension;

        // Initialize JPanel to hold array of buttons
        JPanel actorGrid = new JPanel(new GridLayout(numRows, numCols));
        // Initialize 2D array for buttons
        JButton[][] buttonForGrid;

        if (whichActor) {
            buttonForGrid = userButtons;
        } else {
            buttonForGrid = opponentButtons;
        }
        // Only loop through instance of Buttons from GameModel to assign action listener
        for (JButton[] row : buttonForGrid) {
            for (JButton button : row) {
                button.addActionListener(e -> {
                    Object eventSource = e.getSource();
                    gameController.handleJButtonClicks(eventSource, controlPanelText);
                });
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

            columnLabelsPanel.setBackground(Color.decode("#feefec"));

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
            rowLabelPanel.setBackground(Color.decode("#feefec"));


        }

        // Arrange layout positions for each component
        actorPanel.add(rowLabelPanel, BorderLayout.WEST);
        actorPanel.add(lifeStatus, BorderLayout.SOUTH);
        actorPanel.add(actorGrid, BorderLayout.CENTER);
        actorPanel.add(columnLabelsPanel, BorderLayout.NORTH);
    }

    /**
     * Method Name: addPanelsToMainFrame
     * Purpose: Add created panels into the main frame.
     * Algorithm: Create new contentpane, set layout, add user actor, control panel, and machine actor
     */
    protected void addPanelsToMainFrame() {
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(userPanel, BorderLayout.WEST);
        contentPane.add(selectionPanel, BorderLayout.CENTER);
        contentPane.add(opponentPanel, BorderLayout.EAST);
    }

    /**
     * Method Name: updateBoard
     * Purpose: Is called in GameController, When dimension JComboBox is changed, this method clears both actor board panels, and creates new board based on the size of the dimension selected.
     * Algorithm: remove both actor panels, create new board, revalidate, repaint
     *
     * @param buttons         - 2D array JButton grid
     * @param actorBoardPanel - User Panel or Opponent Panel
     */
    protected void updateBoard(JButton[][] buttons, JPanel actorBoardPanel) {

        actorBoardPanel.removeAll();

        // Clear previous actionListeners
        // TODO CHECK IF THIS WORKS WITH NEW REFACTOR
        for (JButton[] buttonRow : buttons) {
            for (JButton button : buttonRow) {
                ActionListener[] listeners = button.getActionListeners();
                for (ActionListener listener : listeners) {
                    button.removeActionListener(listener);
                }
            }
        }

        // Call Controller to grab GameModel JButtons
        userButtons = gameController.getButtons(true);
        opponentButtons = gameController.getButtons(false);

        // Revalidate and Repaint
        actorBoardPanel.revalidate();
        actorBoardPanel.repaint();
    }

    /**
     * Purpose popup window method for design boat
     */
    protected void designBoatWindow() {
        /* New JFrame for pop-up window to design board */
        designWindow.setSize(550, 550);
        //TODO find a way to set designWindow to null on close for gameController if (eventSource == button && designWindow != null)

        // Window listener if closed when not expecting reset Remaining boat count
        designWindow.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                JFrame sourceFrame = (JFrame) e.getSource();
                gameController.resetRemainingBoat();
                designWindow = null;
                sourceFrame.dispose();
            }
        });

        designWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        designWindow.setLocationRelativeTo(null);
        designWindow.setVisible(true);
    }

    /**
     * Create JFrame and panels hosting items
     *
     * @param size - Boat size
     */
    protected void designBoatPlacement(Integer size) {
        JPanel designPanel = new JPanel(new BorderLayout());
        JPanel bottomPanel = new JPanel();
        userButtons = gameController.getButtons(true);
        boatLabel = new JLabel();
        directionLabel = new JLabel();
        JLabel horizontalLabel = new JLabel("H");
        JLabel verticalLabel = new JLabel("V");
        setRemainingBoats();

        remainingBoat = new JLabel();
        remainingBoat.setForeground(Color.lightGray);
        resetLayout = new JButton();
        saveLayout = new JButton();

        resetLayout.addActionListener(e -> {
            Object eventSource = e.getSource();
            gameController.handleResetLayout(eventSource, controlPanelText);
        });
        saveLayout.addActionListener(e -> {
            Object eventSource = e.getSource();
            gameController.handleSaveLayout(eventSource, controlPanelText);
        });

        int buttonSize = Math.min(50, 200 / size); // Adjust the button size based on dimension
        JPanel designGrid = new JPanel(new GridLayout(size * 2, size * 2));


        // Only loop through instance of Buttons from GameModel to assign action listener
        for (JButton[] row : userButtons) {
            for (JButton button : row) {
                button.addActionListener(e -> {
                    Object eventSource = e.getSource();
                    gameController.handleJButtonClicks(eventSource, controlPanelText);
                });
                button.setPreferredSize(new Dimension(buttonSize, buttonSize));
                designGrid.add(button);
            }
        }
        boatSizeSelector = new JComboBox<>(comboBoxModel);
        boatVertical = new JRadioButton();
        boatHorizontal = new JRadioButton();

        // ButtonGroup where only one can be selected at a time (horizontal vertical)
        ButtonGroup orientationGroup = new ButtonGroup();

        boatSizeSelector.setSelectedIndex(0);
        setBoatSizeSelectorValue((int) boatSizeSelector.getSelectedItem());
        boatHorizontal.setActionCommand("false");
        boatVertical.setSelected(true);
        boatVertical.setActionCommand("true");

        boatSizeSelector.addActionListener(e -> {
            Object eventSource = e.getSource();
            gameController.handleBoatSizeSelector(eventSource, controlPanelText);
        });
        boatVertical.addActionListener(e -> {
            Object eventSource = e.getSource();
            gameController.handleJRadioOrientation(eventSource, controlPanelText);
        });
        boatHorizontal.addActionListener(e -> {
            Object eventSource = e.getSource();
            gameController.handleJRadioOrientation(eventSource, controlPanelText);
        });

        orientationGroup.add(boatVertical);
        orientationGroup.add(boatHorizontal);
        designPanel.add(designGrid);
        bottomPanel.add(boatLabel);
        bottomPanel.add(boatSizeSelector);
        bottomPanel.add(remainingBoat);
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

    protected void setRemainingBoats() {
        this.remainingBoats = gameController.getRemainingBoats();
    }

    protected void updateRemainingBoats() {
        setRemainingBoats();
        remainingBoat.setText("Remaining: " + remainingBoats);
    }

    protected JFrame getDesignWindow() {
        return designWindow;
    }

    protected void setDesignWindow(JFrame setter) {
        this.designWindow = setter;
    }

    /**
     * Set list of Boat Objects from GameModel
     *
     * @param designBoatList - ArrayList of User Player board
     */
    protected void setDesignBoatList(List<List<Boat>> designBoatList) {
        this.designBoatList = designBoatList;
    }

    /**
     * Extract the boat size and put it into the ComboBoxModel
     */
    protected void extractDesignBoatList() {
        comboBoxModel = new DefaultComboBoxModel<>();
        for (List<Boat> boatList : designBoatList) {
            for (Boat boat : boatList) {
                comboBoxModel.addElement(boat.getBoatLength());
            }
        }
    }

    /**
     * Set the updated String for control panel log
     *
     * @param text - Control panel Concat string
     */
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
    protected void playBackgroundMusic(String musicFile) {
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

    protected void languageChanger() {
        String selectedLanguage = (String) languageButton.getSelectedItem();
        Locale locale;

        if (selectedLanguage.equals("English")) {
            locale = Locale.ENGLISH;
        } else {
            locale = new Locale("fr", "FR");
        }
        updateLanguage(locale);
    }

    protected void disableControlPanelButtons() {
        randBoatPlacement.setEnabled(false);
        designBoatPlacement.setEnabled(false);
        play.setEnabled(false);
        dimensionComboBox.setEnabled(false);
    }

    protected void enableControlPanelButtons() {
        randBoatPlacement.setEnabled(true);
        designBoatPlacement.setEnabled(true);
        play.setEnabled(true);
        dimensionComboBox.setEnabled(true);
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

    }
}
