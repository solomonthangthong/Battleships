package jap;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Class Name: Client
 * Method List:
 * Constants List: u
 * <p>
 * Client, Client GUI for user to connect to server,create new game configurations, receive them and play.
 *
 * @author Andrew Lorimer, Solomon Thangthong
 * @version 1.0
 * @see JFrame
 * @see ActionListener
 * @since 11.0.19
 */
public class Client extends JFrame implements ActionListener {
    private GameController gameController;
    private JPanel clientPanel;
    private JTextField user;
    private JTextField serverAddress;
    private JTextField portNumber;
    private JButton connect;
    private JButton end;

    private JButton newGame;
    private JButton sendGame;
    private JButton receiveGame;
    private JButton sendData;
    private JButton play;
    private JTextArea console;
    private JScrollPane scrollPane;

    private Socket socket;

    private String gameConfiguration;
    private Integer dimensionSize;
    private InputStream inputStream;

    private OutputStream outputStream;
    private BufferedWriter writer;

    private BufferedReader reader;
    private BlockingQueue<String> messageQueue;

    /**
     * Constructor for Class
     */
    public Client() {
        initializeFrame();
        createPanel();
        addPanelsToMainFrame();
    }

    /**
     * Method Name: InitializeFrame
     * Purpose: Method to set up parameters for GUI window
     * Algorithm: Set title, size, location, default close operation
     */
    public void initializeFrame() {
        setTitle("Battleship Game Client");
        setSize(600, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocation(0, 0);
    }

    /**
     * Method Name:createPanels
     * Purpose:create the client GUI and the buttons for the application
     * Algorithm:create multiple JPanels representing each division of the application, including all the buttons the user can press
     */
    protected void createPanel() {

        clientPanel = new JPanel(new BorderLayout());

        // Load and display the logo
        ImageIcon image = new ImageIcon("images/client.png");
        if (new File("images/client.png").exists()) {
            JButton imageLogo = new JButton(image);
            imageLogo.setBackground(Color.white);
            clientPanel.add(imageLogo, BorderLayout.NORTH);
        } else {
            System.out.println("Image not found");
        }

        JPanel userComponent = new JPanel();
        JLabel userLabel = new JLabel("User:");
        user = new JTextField(8);

        user.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updatePlayerNameController();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updatePlayerNameController();
            }


            @Override
            public void changedUpdate(DocumentEvent e) {
                updatePlayerNameController();
            }
        });

        userComponent.add(userLabel);
        userComponent.add(user);

        JPanel serverComponent = new JPanel();
        JLabel serverLabel = new JLabel("Server:");
        serverAddress = new JTextField(8);
        serverComponent.add(serverLabel);
        serverComponent.add(serverAddress);

        JPanel portComponent = new JPanel();
        JLabel portLabel = new JLabel("Port:");
        portNumber = new JTextField(5);
        portComponent.add(portLabel);
        portComponent.add(portNumber);
        //set default values

        serverAddress.setText(Config.DEFAULT_ADDR);
        portNumber.setText(String.valueOf(Config.DEFAULT_PORT));
        user.setText(String.valueOf(Config.DEFAULT_USER));

        connect = new JButton("Connect");
        end = new JButton("End");
        newGame = new JButton("New Game");
        newGame.setEnabled(false);
        sendGame = new JButton("Send Game");
        sendGame.setEnabled(false);
        receiveGame = new JButton("Receive Game");
        receiveGame.setEnabled(false);
        sendData = new JButton("Send Data");
        sendData.setEnabled(false);
        play = new JButton("Play");
        play.setEnabled(false);


        //action listeners to the buttons
        connect.addActionListener(this);
        end.addActionListener(this);
        newGame.addActionListener(this);
        sendGame.addActionListener(this);
        receiveGame.addActionListener(this);
        sendData.addActionListener(this);
        play.addActionListener(this);


        JPanel buttonComponent = new JPanel(new FlowLayout());

        buttonComponent.add(userComponent);
        buttonComponent.add(serverComponent);
        buttonComponent.add(portComponent);
        buttonComponent.add(connect);
        buttonComponent.add(end);
        buttonComponent.add(newGame);
        buttonComponent.add(sendGame);
        buttonComponent.add(receiveGame);
        buttonComponent.add(sendData);
        buttonComponent.add(play);


        clientPanel.add(buttonComponent, BorderLayout.CENTER);

        console = new JTextArea(7, 1);
        console.setEditable(false);
        scrollPane = new JScrollPane(console);

        clientPanel.add(scrollPane, BorderLayout.SOUTH);

    }

    /**
     * Method Name: addNewLine
     * Purpose: Add new line of text to text area in Server GUI console
     * Algorithm: append message, set position to new line
     *
     * @param line
     */
    private void addNewLine(String line){
        console.append(line);
        console.setCaretPosition(console.getDocument().getLength());
        console.scrollRectToVisible(new Rectangle(console.getPreferredSize()));
    }

    protected void setGameController(GameController controller) {
        this.gameController = controller;
    }

    /**
     * Method Name: addPanelsToMainFrame
     * Purpose: Add created panels into the main frame.
     * Algorithm: Create new contentpane, set layout, add user actor, control panel, and machine actor
     */
    protected void addPanelsToMainFrame() {
        Container contentPane = getContentPane();
        contentPane.add(clientPanel);

    }

    /**
     * Method Name: connectToServer
     * Purpose: Create socket to connect to server
     * Algorithm: Create new instance of socket and pass address and portnumber
     *
     * @param serverAddress - IP address of server
     * @param portNumber - Integer value of server port
     */
    public void connectToServer(String serverAddress, int portNumber) {
        try {
            // Create a new socket
            socket = new Socket(serverAddress, portNumber);

            // Connection is successful
            addNewLine("Connected to server at " + serverAddress + ":" + portNumber + "\n");
            // Disable the "Connect" button
            connect.setEnabled(false);
            // Enable the "End" button to allow disconnection
            end.setEnabled(true);

        } catch (IOException ex) {
            // Handle connection errors
            addNewLine("Connection failed: " + ex.getMessage() + "\n");
        }
    }

    /**
     * Method Name: endConnection
     * Purpose: End connect to server
     * Algorithm:
     */
    protected void endConnection() {
        try {
            if (socket != null && !socket.isClosed()) {
                // need to figure out way to get instance of server so that I can call this
                // server.disconnectClient(socket);
                // Close the socket
                socket.close();
                addNewLine("Connection ended.\n");
                // Re-enable the "Connect" button
                connect.setEnabled(true);
                // Disable the "End" button since the connection is closed
                end.setEnabled(false);
            } else {
                addNewLine("No active connection to end.\n");
            }
        } catch (IOException ex) {
            // Handle connection closing errors
            addNewLine("Error ending connection: " + ex.getMessage() + "\n");
        }
    }

    /**
     * Method Name: updatePlayerNameController
     * Purpose: return playerName from JTextField to update player Object in MVC
     * Algorithm: get JTextField and return that value
     *
     * @return - playerName
     */
    protected String updatePlayerNameController() {
        String playerName = user.getText();
        return playerName;
    }


    /**
     * Method Name: listenForClientHandleMessage
     * Purpose: Create thread to constantly listen for messages from ClientHandler
     * Algorithm: Create new thread and start thread
     */
    public void listenForClientHandleMessage() {
        Thread listeningThread = new Thread(this::receiveServerOutput);
        listeningThread.start();
    }

    /**
     * Method Name: receiveServerOutput
     * Purpose: Parse string information from output stream
     * Algorithm: While loop, if string is not null splice string into indexes, and use information
     */
    private void receiveServerOutput() {
        String protocolMessage;
        try {
            while ((protocolMessage = reader.readLine()) != null) {
                String[] spliced = protocolMessage.split(Config.PROTOCOL_SEPARATOR);
                if (spliced.length >= 3) {
                    String clientID = spliced[0];
                    String protocolID = spliced[1];
                    String data = spliced[2];
                    gameConfiguration = data;
                    addNewLine("Received " + protocolMessage + "\n");
                    messageQueue.offer(gameConfiguration);
                }
            }
        } catch (IOException e) {
            System.out.print("Socket closed\n");
        }
    }

    /**
     * Method Name: setGameConfiguration
     * Purpose: set Global String game configuration to passed argument
     * Algorithm: this.variable = passed variable
     *
     * @param gameConfig - String game configuration from Server
     */
    protected void setGameConfiguration(String gameConfig) {
        this.gameConfiguration = gameConfig;
    }

    /**
     * Method Name: setDimensionSize
     * Purpose: set Global Integer dimensionSize to passed argument
     * Algorithm: this.variable = passed variable
     *
     * @param size - Board dimension size
     */
    protected void setDimensionSize(Integer size) {
        this.dimensionSize = size;
    }

    /**
     * Method Name: sendProtocolToServer
     * Purpose: Pass protocol message to generic method to reduce duplicated code
     * Algorithm: Try catch use Buffer and write message to stream
     *
     * @param message - Protocol string example 1#P1#4,1000000000444401000000000333003000000030201020302000200100220000
     */
    private void sendProtocolToServer(String message){
        try {
            writer.write(message);
            writer.newLine();
            writer.flush();
        } catch (IOException ex) {
            System.out.print("wow");
        }
    }

    /**
     * Method Name: actionPerformed
     * Purpose: Invoked JButton when an action occurs
     * Algorithm: If else tree to determine specific actions
     *
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == connect) {
            // Get the server address and port number from the text fields
            String serverAddressStr = serverAddress.getText();

            messageQueue = new ArrayBlockingQueue<>(10);

            int portNumberInt = Integer.parseInt(portNumber.getText());
            // Call the connectToServer method to establish the connection
            connectToServer(serverAddressStr, portNumberInt);

            // Open input stream when connection is connected
            try {
                this.inputStream = socket.getInputStream();
                this.reader = new BufferedReader(new InputStreamReader(inputStream));
            } catch (IOException b) {
                b.printStackTrace();
            }

            // Open stream when connection is connected
            try {
                outputStream = socket.getOutputStream();
                writer = new BufferedWriter(new OutputStreamWriter(outputStream));
            } catch (IOException b) {
                b.printStackTrace();
            }

            // Thread to listen for incoming messages from Server
            listenForClientHandleMessage();

            // Grab default board size then Increment in Handler
            gameController.getDimensionSize();

            // Enable buttons once connected
            newGame.setEnabled(true);
            sendGame.setEnabled(true);
            receiveGame.setEnabled(true);
            sendData.setEnabled(true);
            play.setEnabled(true);

        } else if (e.getSource() == end) {
            //sendProtocolEnd();

            String message = Config.PROTOCOL_END + Config.PROTOCOL_SEPARATOR + 0;
            sendProtocolToServer(message);
            endConnection();

        } else if (e.getSource() == newGame) {
            //TODO If new game was pressed already must increment board size
            addNewLine("Creating new MVC game\n");
            gameController.sendGameConfiguration();
            // Add dimensions and game Config

        } else if (e.getSource() == sendGame) {
            addNewLine("Sending Game Configuration to Server\n");
            String message = Config.PROTOCOL_SENDGAME + Config.PROTOCOL_SEPARATOR + dimensionSize + Config.FIELD_SEPARATOR + gameConfiguration;
            addNewLine(message + "\n");
            sendProtocolToServer(message);

        } else if (e.getSource() == receiveGame) {

            String message = Config.PROTOCOL_RECVGAME + Config.PROTOCOL_SEPARATOR + 0;
            addNewLine("Receiving Game Configuration from Server\n");
            addNewLine(message + "\n");
            sendProtocolToServer(message);

            // Send String config to MVC
            try {
                String config = messageQueue.take();
                addNewLine(config + "\n");
                gameController.receiveGameConfigurationClient(config);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }


        } else if (e.getSource() == sendData) {
            ///get the points and time from the MVC
            int userPoints = GameModel.getUserPoints();
            int computerPoints = GameModel.getComputerPoints();
            String userName = user.getText();

            int time = GameView.getTime();
            String message = Config.PROTOCOL_DATA + Config.PROTOCOL_SEPARATOR + userName + "," + userPoints + "," + computerPoints + "," + time + "\n";
        //clientID # protcolID (P3) # playerData
            sendProtocolToServer(message);

        } else if (e.getSource() == play) {
            gameController.setMVCVisible();
        }
    }
}

