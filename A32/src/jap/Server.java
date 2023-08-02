package jap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

/**
 *
 */
public class Server extends JFrame implements ActionListener {
    private JPanel serverPanel;
    private JTextField portTextField;
    private JButton start;
    private JButton result;
    private JCheckBox finalize;
    private JButton end;

    private JTextArea console;
    private JScrollPane scrollPane;
    private ServerSocket serverSocket;


    private int port;
    private boolean finalizeServer;
    private List<ClientHandler> clients;

    //these were in UML but are unused atm
    //private Map<Integer,PlayerData> playerData;
    //private List<PLayerData> rankList;
    /**
     *
     */
    public Server(int port) {

        initializeFrame();
        createPanel();
        addPanelsToMainFrame();
        //when server is instantiated, set the serverSocket given default port
        try {
            serverSocket = new ServerSocket(port);
            console.append("Server started on port " + port + "\n");
        } catch (IOException ex) {
            console.append("Error creating server socket: " + ex.getMessage() + "\n");
        }
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
        int screenHeight = screenSize.height;
        setLocation(screenWidth - getWidth(),  0);
    }

    /**
     *
     */
    protected void createPanel(){

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
        end = new JButton("End");

        JPanel buttonComponent = new JPanel(new FlowLayout());

        buttonComponent.add(portComponent);
        buttonComponent.add(start);
        buttonComponent.add(result);
        buttonComponent.add(finalize);
        buttonComponent.add(end);
        //buttonComponent.setPreferredSize(new Dimension(600, 50));

        serverPanel.add(buttonComponent, BorderLayout.CENTER);

        console = new JTextArea();
        console.setPreferredSize(new Dimension(600, 125));
        console.setEditable(false);
        scrollPane = new JScrollPane(console);

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


    public void acceptConnection() {
        try {
            // accept connectipon
            Socket clientSocket = serverSocket.accept();
        } catch (IOException ex) {
            // Handle connection errors
            console.append("Error accepting connection: " + ex.getMessage() + "\n");

        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
