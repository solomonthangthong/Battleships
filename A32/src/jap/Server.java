package jap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class Name: Server
 * Method List:
 * Constants List: u
 * <p>
 * Server, host server on IP address and accept incoming connections, store user data and game results
 *
 * @author Andrew Lorimer, Solomon Thangthong
 * @version 1.0
 * @see JFrame
 * @see ActionListener
 * @since 11.0.19
 */
public class Server extends JFrame implements ActionListener {
    private JPanel serverPanel;
    private JTextField portTextField;
    private JButton start;
    private JButton result;
    private JCheckBox finalize;
    private JButton end;

    private Boolean acceptClient;

    protected JTextArea console;
    private ServerSocket serverSocket;

    private Thread serverThread;
    private Boolean atLeastOneClient;

    private List<ClientHandler> clients;

    private String gameConfiguration;

    private final Map<Integer, Player> playerMap;

    /**
     * Method Name: Server
     * Purpose: Default constructor
     * Algorithm: Initialize required components to launch Server GUI
     */
    public Server() {
        initializeFrame();
        createPanel();
        addPanelsToMainFrame();
        playerMap = new HashMap<>();
    }


    /**
     * Method Name: main
     * Purpose: Entry point of the Server application
     * Algorithm: Use default port upon launch, create new server instance, and set visibility to true.
     *
     * @param args - The command-line arguments provided to the application.
     */
    public static void main(String[] args) {
        Server server = new Server();
        server.setResizable(false);
        server.setVisible(true);
    }

    /**
     * Method Name: InitializeFrame
     * Purpose: Method to set up parameters for GUI window
     * Algorithm: Set title, size, location, default close operation
     */
    public void initializeFrame() {
        setTitle("Battleship Game Server");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        setLocation(screenWidth - getWidth(), 0);
    }

    /**
     * Method Name:createPanels
     * Purpose:create the server panels containing the buttons for the application
     * Algorithm:create multiple JPanels representing each division of the application
     */
    protected void createPanel() {

        serverPanel = new JPanel(new BorderLayout());

        // Load and display the logo
        ImageIcon image = new ImageIcon("images/server.png");
        if (new File("images/server.png").exists()) {
            JButton imageLogo = new JButton(image);
            imageLogo.setBackground(Color.white);
            serverPanel.add(imageLogo, BorderLayout.NORTH);
        } else {
            System.out.println("Image not found");
        }

        JPanel portComponent = new JPanel();
        JLabel portLabel = new JLabel("Port: ");
        portTextField = new JTextField(5);
        portTextField.setText(String.valueOf(Config.DEFAULT_PORT));

        portComponent.add(portLabel);
        portComponent.add(portTextField);

        start = new JButton("Start");
        // If case if there are no games in result dont allow click
        result = new JButton("Results");
        finalize = new JCheckBox("Finalize");
        finalize.addActionListener(this);
        end = new JButton("End");
        start.addActionListener(this);
        end.addActionListener(this);
        result.addActionListener(this);

        JPanel buttonComponent = new JPanel(new FlowLayout());

        buttonComponent.add(portComponent);
        buttonComponent.add(start);
        buttonComponent.add(result);
        buttonComponent.add(finalize);
        buttonComponent.add(end);
        //buttonComponent.setPreferredSize(new Dimension(600, 50));

        serverPanel.add(buttonComponent, BorderLayout.CENTER);

        console = new JTextArea(7, 1);
        console.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(console);

        serverPanel.add(scrollPane, BorderLayout.SOUTH);


    }

    /**
     * Method Name: addPanelsToMainFrame
     * Purpose: Add created panels into the main frame.
     * Algorithm: Create new contentpane, set layout, add user actor, control panel, and machine actor
     */
    protected void addPanelsToMainFrame() {
        Container contentPane = getContentPane();
        contentPane.add(serverPanel);
    }

    /**
     * Method Name: acceptConnection
     * Purpose: Accept the socket from client side
     * Algorithm: Accept socket and create new instane of ClientHandler, add new Client Handler to List of clients, and start new thread
     */
    public void acceptConnection() {
        while (!Thread.interrupted()) {
            try {
                // accept connection
                Socket clientSocket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(clientSocket, this);
                clients.add(clientHandler);
                int clientNumber = clients.indexOf(clientHandler);
                clientHandler.setClientId(clientNumber);

                Thread clientThread = new Thread(clientHandler);
                clientThread.start();
                atLeastOneClient = true;
                addNewLine("Client " + clientNumber + " connected: " + clientSocket.getInetAddress().getHostAddress() + "\n");
            } catch (IOException ex) {
                // Handle connection errors
                JOptionPane.showMessageDialog(null, "Error accepting connection: " + ex.getMessage() + "\n", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    /**
     * Method Name: startServer
     * Purpose:
     * Algorithm:
     *
     * @param port - Integer port value
     */
    public void startServer(int port) {
        // Check if the server is already running
        if (serverSocket != null && !serverSocket.isClosed()) {
            addNewLine("Server is already running on port " + port + "\n");
            return;
        }

        try {
            serverSocket = new ServerSocket(port);
            addNewLine("Server started on port " + port + "\n");
            end.setEnabled(true);
            start.setEnabled(false);

        } catch (IOException ex) {
            addNewLine("Error creating server socket: " + ex.getMessage() + "\n");
        }
    }

    /**
     * Method Name: disconnectClient
     * Purpose: When a client disconnect server will acknowledge
     * Algorithm: Method is called from ClientHandler and  If client matches from List of clients, remove the client from list.
     *
     * @param clientSocket - Socket created in Client class to communicate to server
     */
    public void disconnectClient(Socket clientSocket) {
        // Find the corresponding ClientHandler in the list
        for (ClientHandler clientHandler : clients) {
            if (clientHandler.getClientSocket() == clientSocket) {
                clients.remove(clientHandler);
                addNewLine("Client " + clientHandler.getClientId() + " has been disconnected\n");
                break;
            }
        }
    }

    /**
     * Method Name: endConnection
     * Purpose:
     * Algorithm:
     */
    public void endConnection() {
        if (serverSocket != null && !serverSocket.isClosed()) {
            try {
                serverThread.stop();
                int portNumberInt = Integer.parseInt(portTextField.getText());
                serverSocket.close();
                addNewLine("Server closed on port " + portNumberInt + "\n");
                //re enable start button when connection is ended
                start.setEnabled(true);
                //disable the end button
                end.setEnabled(false);

                atLeastOneClient = false;
                finalize.setSelected(false);

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error closing server socket: " + ex.getMessage() + "\n", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Server is not running or already closed.\n", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Method Name: setGameConfiguration
     * Purpose: Setter method for game configuration
     * Algorithm: Set global variable to passed argument
     *
     * @param config - Game Configuration example 1#P1#4,1000000000444401000000000333003000000030201020302000200100220000
     */
    protected void setGameConfiguration(String config) {
        if (!config.equals(gameConfiguration)) {
            this.gameConfiguration = config;
        } else {
            JOptionPane.showMessageDialog(null, "This configuration has already been received from a client instance.", "Warning", JOptionPane.WARNING_MESSAGE);
        }

    }

    /**
     * Method Name: sendConfigurationToClients
     * Purpose: Getter method for game configuration
     * Algorithm: Return String for game config
     *
     * @return - Game Configuration example 1#P1#4,1000000000444401000000000333003000000030201020302000200100220000
     */
    protected String sendConfigurationToClients() {
        return gameConfiguration;
    }

    /**
     * Method Name: addNewLine
     * Purpose: Add new line of text to text area in Server GUI console
     * Algorithm: append message, set position to new line
     *
     * @param line - String message to append to console
     */
    protected void addNewLine(String line) {
        console.append(line);
        console.setCaretPosition(console.getDocument().getLength());
        console.scrollRectToVisible(new Rectangle(console.getPreferredSize()));
    }

    /**
     * Method Name: addPlayerToList
     * Purpose: add Player Object to Map
     * Algorithm: For loop if list of ClientHandlers match socket, add player Object to Map
     *
     * @param player - Player object
     * @param clientSocket - Client socket connection to server
     */
    protected void addPlayerToList(Player player, Socket clientSocket) {
        // index to increment for enhanced for loop
        int index = 0;
        for (ClientHandler clientHandler : clients) {
            if (clientHandler.getClientSocket() == clientSocket) {
                playerMap.put(index, player);
            }
            index++;
        }
    }

    /**
     * Method Name:
     * Purpose:
     * Algorithm:
     */
    private void showPlayerDataPopup() {
        // Prepare the data to show in the pop-up box
        StringBuilder playerData = new StringBuilder();

        // Loop through playerMap to grab information
        for (Map.Entry<Integer, Player> entry : playerMap.entrySet()) {
            int playerId = entry.getKey();
            Player player = entry.getValue();
            String playerName = player.getPlayerName();
            int points = player.getPoints();
            int time = player.getTime();
            playerData.append("Game Results:\nPlayer[" + playerId + "]: " + playerName + ", Points: " + points + ", Time: " + time + "\n");

        }

        // Show the pop-up box with the player data
        JOptionPane.showMessageDialog(this, playerData.toString(), "Player Data", JOptionPane.INFORMATION_MESSAGE);
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
        if (e.getSource() == start) {
            int portNumberInt = Integer.parseInt(portTextField.getText());
            if (Integer.toString(portNumberInt).length() == 5) {
                startServer(portNumberInt);
                serverThread = new Thread(this::acceptConnection);
                serverThread.start();
                acceptClient = true;
                clients = new ArrayList<>();
            } else {
                JOptionPane.showMessageDialog(null, "Port enter is out of range. Please enter a 5 digit port number.\n", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        } else if (e.getSource() == end) {
            if (serverThread != null) {
                serverThread.interrupt();
            }
            for (ClientHandler client: clients){
                client.sendServerEnd(String.valueOf(client.getClientId()), Config.PROTOCOL_END);
            }

            endConnection();
        } else if (e.getSource() == result) {
            if (!playerMap.isEmpty()) {
                showPlayerDataPopup();
            } else {
                JOptionPane.showMessageDialog(null, "No player has been recorded yet. Please allow clients to connect, play games, and store their data.\n", "Warning", JOptionPane.WARNING_MESSAGE);
            }

        }
        if (finalize.isSelected()) {
            if (atLeastOneClient != null && true){
                Thread checkThread = new Thread(() -> {
                    while (clients.size() > 0) {
                        try {
                            Thread.sleep(1000); // Adjust the sleep duration as needed
                        } catch (InterruptedException ignored) {
                        }
                    }
                    endConnection();
                    System.out.println("Server has been finalized and shut down.");
                });
                checkThread.start();
            } else {
                JOptionPane.showMessageDialog(null, "No client instance has connected yet.\n", "Warning", JOptionPane.WARNING_MESSAGE);
                finalize.setSelected(false);
            }
        }
    }
}

