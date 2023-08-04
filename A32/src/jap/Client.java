package jap;

import javax.print.Doc;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

public class Client extends JFrame implements ActionListener {
    private GameController gameController;
    private Server server;
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

    private Boolean connectionStatus;

    private Integer clientID;

    private OutputStream outputStream;
    private BufferedWriter writer;

    public Client() {
        initializeFrame();
        createPanel();
        addPanelsToMainFrame();
    }

    public void initializeFrame() {
        setTitle("Battleship Game Client");
        setSize(600, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocation(0, 0);
    }

    /**
     *
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

    protected void setGameController(GameController controller){
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

    public void connectToServer(String serverAddress, int portNumber) {
        try {
            // Create a new socket
            socket = new Socket(serverAddress, portNumber);

            // Connection is successful
            console.append("Connected to server at " + serverAddress + ":" + portNumber + "\n");
            // Disable the "Connect" button
            connect.setEnabled(false);
            // Enable the "End" button to allow disconnection
            end.setEnabled(true);

            connectionStatus = true;

            //create instance of client handler and pass socket for connection
            ClientHandler clientHandler = new ClientHandler(socket, server);
            Thread clientHandlerThread = new Thread(clientHandler);
            clientHandlerThread.start();

        } catch (IOException ex) {
            // Handle connection errors
            console.append("Connection failed: " + ex.getMessage() + "\n");
        }
    }

    public void setClientID(Integer id){
        this.clientID = id;
    }

    public Server getServer() {
        return this.server;
    }

    protected void endConnection() {
        try {
            if (socket != null && !socket.isClosed()) {
                // need to figure out way to get instance of server so that I can call this
                // server.disconnectClient(socket);
                // Close the socket
                socket.close();
                console.append("Connection ended.\n");
                // Re-enable the "Connect" button
                connect.setEnabled(true);
                // Disable the "End" button since the connection is closed
                end.setEnabled(false);
            } else {
                console.append("No active connection to end.\n");
            }
        } catch (IOException ex) {
            // Handle connection closing errors
            console.append("Error ending connection: " + ex.getMessage() + "\n");
        }
    }


    protected String updatePlayerNameController() {
        String playerName = user.getText();
        return playerName;
    }

    protected void setGameConfiguration(String gameConfig){
        this.gameConfiguration = gameConfig;
    }

    protected void setDimensionSize(Integer size){
        this.dimensionSize = size;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == connect) {
            // Get the server address and port number from the text fields
            String serverAddressStr = serverAddress.getText();
            int portNumberInt = Integer.parseInt(portNumber.getText());
            // Call the connectToServer method to establish the connection
            connectToServer(serverAddressStr, portNumberInt);

            // Open stream when connection is connected
            try{
                outputStream = socket.getOutputStream();
                writer = new BufferedWriter(new OutputStreamWriter(outputStream));
            } catch(IOException b){
                b.printStackTrace();
            }

            newGame.setEnabled(true);
            sendGame.setEnabled(true);
            receiveGame.setEnabled(true);
            sendData.setEnabled(true);
            play.setEnabled(true);

        } else if (e.getSource() == end) {
            //sendProtocolEnd();

            String message = Config.PROTOCOL_END + Config.PROTOCOL_SEPARATOR + 0;
            try {
                writer.write(message);
                writer.newLine();
                writer.flush();
                endConnection();
            } catch (IOException ex) {
                System.out.print("wow");
            }
            //endConnection();

        } else if (e.getSource() == newGame){
            console.append("Creating new MVC game\n");
            gameController.sendGameConfiguration();
            // Add dimensions and game Config

        } else if (e.getSource() == sendGame){
            gameController.getDimensionSize();
            console.append("Sending Game Configuration to Server\n");
            String message = Config.PROTOCOL_SENDGAME + Config.PROTOCOL_SEPARATOR + dimensionSize + Config.FIELD_SEPARATOR + gameConfiguration;
            console.append(message + "\n");

            try {
                writer.write(message);
                writer.newLine();
                writer.flush();
            }catch(IOException ex){
                throw new RuntimeException(ex);
            }
        }
    }
}

