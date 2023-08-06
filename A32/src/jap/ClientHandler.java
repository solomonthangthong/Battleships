package jap;

import java.io.*;
import java.net.Socket;

/**
 * Class Name: ClientHandler
 * Method List:
 * Constants List: u
 * <p>
 * ClientHandler, Handler socket communication from Client to Server
 *
 * @author Andrew Lorimer, Solomon Thangthong
 * @version 1.0
 * @see Runnable
 * @since 11.0.19
 */
public class ClientHandler implements Runnable {
    private final Server serverInstance;
    private final Socket clientSocket;

    private Integer clientId;

    private String gameConfiguration;

    private InputStream inputStream;

    private BufferedReader reader;

    private BufferedWriter writer;
    private int computerPoints;

    /**
     * Method Name: ClientHandler
     * Purpose: Default Constructor
     * Algorithm: Initialize server, socket, input/output streams, and player data
     *
     * @param clientSocket - Socket connection
     * @param server - Server object
     */
    public ClientHandler(Socket clientSocket, Server server) {
        this.clientSocket = clientSocket;
        this.serverInstance = server;

        try {
            this.inputStream = clientSocket.getInputStream();
            this.reader = new BufferedReader(new InputStreamReader(inputStream));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            OutputStream outputStream = clientSocket.getOutputStream();
            writer = new BufferedWriter(new OutputStreamWriter(outputStream));
        } catch (IOException b) {
            b.printStackTrace();
        }

    }

    /**
     * Method Name: setClientID
     * Purpose: Setter Method for ClientID
     * Algorithm: Set global variable to passed argument
     *
     * @param clientNumber - Client Number
     */
    protected void setClientId(Integer clientNumber) {
        this.clientId = clientNumber;
    }

    /**
     * Method Name: getClientID
     * Purpose: Getter method for client ID
     * Algorithm: return variable
     *
     * @return - client ID number
     */
    protected Integer getClientId() {
        return clientId;
    }

    /**
     * Method Name: processClient
     * Purpose: While loop if reader is not null
     * Algorithm: If not null send to processProtocol method
     */
    private void processClient() {
        String protocolMessage;
        try {
            while ((protocolMessage = reader.readLine()) != null) {
                processProtocol(protocolMessage);
            }
        } catch (IOException e) {
            System.out.print("Socket closed\n");
        }
    }

    /**
     * Method Name: processProtocol
     * Purpose: Parse String from client
     * Algorithm: Put string into String Array, if length is equal or greater than 3 determine if client number, protocol #, or gameConfiguration
     *
     * @param protocol - String of protocol message
     */
    private void processProtocol(String protocol) throws IOException {
        String protocolWithId = clientId + Config.PROTOCOL_SEPARATOR + protocol;
        serverInstance.addNewLine("[Debug print]: " + protocolWithId + "\n");
        String[] spliced = protocolWithId.split(Config.PROTOCOL_SEPARATOR);

        if (spliced.length >= 3) {
            String clientID = spliced[0];
            String protocolID = spliced[1];
            String data = spliced[2];

            switch (protocolID) {
                case Config.PROTOCOL_END:
                    serverInstance.addNewLine(protocolWithId + "\n");
                    handleEndConnection(protocolID);
                    break;
                case Config.PROTOCOL_SENDGAME:
                    //serverInstance.console.append(protocolWithId + "\n");
                    serverInstance.addNewLine("[Debug print protocolID for Send]: " + protocolID + "\n");
                    serverInstance.addNewLine("[Debug print GameConfiguration for Send]: " + data + "\n");
                    //serverInstance.console.append(data + "\n");
                    receiveClientGameConfig(data);
                    break;
                case Config.PROTOCOL_RECVGAME:
                    setGameConfiguration();
                    serverInstance.addNewLine(clientID + Config.PROTOCOL_SEPARATOR + protocolID + Config.PROTOCOL_SEPARATOR + gameConfiguration + "\n");
                    sendGameConfig(clientID, protocolID);
                    break;
                case Config.PROTOCOL_DATA:
                    serverInstance.addNewLine(protocolWithId + "\n");

                    // Pass the data to the playerData method to update the player-related variables
                    playerData(data);
                    break;
                default:
                    serverInstance.addNewLine("Unknown protocol\n");
            }
        }
    }

    /**
     * Method Name: receiveClientGameConfig
     * Purpose: Take client Game Configuration and store it to Server side
     * Algorithm: Call setter method from Server Instance and set
     *
     * @param gameConfig - String game configuration
     */
    protected void receiveClientGameConfig(String gameConfig) {
        // Implement logic for method
        serverInstance.setGameConfiguration(gameConfig);
    }

    /**
     * Method Name: setGameConfiguration
     * Purpose: Setter method
     * Algorithm: Global variable = server Game Configuration
     */
    protected void setGameConfiguration() {
        this.gameConfiguration = serverInstance.sendConfigurationToClients();
    }

    /**
     * Method Name: sendGameConfig
     * Purpose: Send Protocol and gameconfiguration to Client side
     * Algorithm: Create string message and put into write stream for client
     */
    protected void sendGameConfig(String clientID, String protocolID) {
        String message = clientID + Config.PROTOCOL_SEPARATOR + protocolID + Config.PROTOCOL_SEPARATOR + gameConfiguration + "\n";
        try {
            writer.write(message);
            writer.newLine();
            writer.flush();
        } catch (IOException ex) {
            System.out.print("Message did not send to client\n");
        }
    }

    /**
     * Method Name: playerData
     * Purpose: Grab player Object from MVC and pass information to Server side
     * Algorithm:
     *
     * @param data -
     */
    protected void playerData(String data) {
        // Split the data using the delimiter ","
        String[] playerData = data.split(Config.FIELD_SEPARATOR);

        // Check if the data contains all the required information
        if (playerData.length == 4) {
            // Update the player-related variables
            String userName = playerData[0];
            int userPoints = Integer.parseInt(playerData[1]);
            computerPoints = Integer.parseInt(playerData[2]);
            int time = Integer.parseInt(playerData[3]);

            Player player = new Player(userName, true);
            player.setMaxScore(userPoints);
            player.setGameLength(time);

            serverInstance.addPlayerToList(player, clientSocket);
        }
    }

    /**
     * Method Name: handleEndConnection
     * Purpose: End the connection between client and server
     * Algorithm: Close reader, stream, and socket, print message on server end.
     *
     * @param protocolID Protocol ID
     */
    protected void handleEndConnection(String protocolID) {
        try {
            reader.close();
            inputStream.close();
            clientSocket.close();

            serverInstance.disconnectClient(clientSocket);
            serverInstance.addNewLine(protocolID + "\n");
        } catch (IOException ex) {
            System.out.println("Error handling end connection: " + ex.getMessage());
        }
    }

    /**
     * Method Name: getClientSocket
     * Purpose: Getter method for clientSocket
     * Algorithm: return the clientSocket
     *
     * @return - Local instance of clientSocket
     */
    protected Socket getClientSocket() {
        return clientSocket;
    }

    /**
     * Method Name: run
     * Purpose: Override run method of Runnable interface
     * Algorithm: Start new thread to handle processClient method, which keeps the method running
     */
    @Override
    public void run() {
        // This keeps the method running
        Thread receiveProtocol = new Thread(this::processClient);
        receiveProtocol.start();
    }
}