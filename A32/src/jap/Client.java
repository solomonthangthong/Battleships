package jap;

import javax.print.Doc;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.Socket;

public class Client extends JFrame implements ActionListener {
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

    private Boolean connectionStatus;

    public Client(Server server) {
        this.server = server;
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
        sendGame = new JButton("Send Game");
        receiveGame = new JButton("Receive Game");
        sendData = new JButton("Send Data");
        play = new JButton("Play");


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

        console = new JTextArea();
        console.setPreferredSize(new Dimension(600, 125));
        console.setEditable(false);
        scrollPane = new JScrollPane(console);

        clientPanel.add(scrollPane, BorderLayout.SOUTH);

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
            ClientHandler clientHandler = new ClientHandler(socket);
            Thread clientHandlerThread = new Thread(clientHandler);
            clientHandlerThread.start();

        } catch (IOException ex) {
            // Handle connection errors
            console.append("Connection failed: " + ex.getMessage() + "\n");
        }
    }

    public void endConnection() {
        try {
            if (socket != null && !socket.isClosed()) {
                //close the socket
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

    protected Boolean getConnectionStatus() {
        return connectionStatus;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == connect) {
            // Get the server address and port number from the text fields
            String serverAddressStr = serverAddress.getText();
            int portNumberInt = Integer.parseInt(portNumber.getText());
            // Call the connectToServer method to establish the connection
            connectToServer(serverAddressStr, portNumberInt);
        } else if (e.getSource() == end) {
            //  close the connection
            endConnection();
        }
    }
}

