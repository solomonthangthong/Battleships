package jap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class Client extends JFrame implements ActionListener {
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

    public Client(){
        initializeFrame();
        createPanel();
        addPanelsToMainFrame();
    }

    public void initializeFrame() {
        setTitle("Battleship Game Client");
        setSize(600, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     *
     */
    protected void createPanel(){

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


        connect = new JButton("Connect");
        end = new JButton("End");
        newGame = new JButton("New Game");
        sendGame = new JButton("Send Game");
        receiveGame = new JButton("Receive Game");
        sendData = new JButton("Send Data");
        play = new JButton("Play");

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
    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
