package jap;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Server serverInstance;
    private Socket clientSocket;
    private String playerName;
    private String gameConfig;
    private String playerData;
    private Integer clientId;

    private String gameConfiguration;

    private InputStream inputStream;
    private BufferedReader reader;

    /**
     *
     * @param clientSocket
     * @param server
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
        playerName = "";
        gameConfig = "";
        playerData = "";
    }

    /**
     *
     * @param clientNumber
     */
    protected void setClientId(Integer clientNumber) {
        this.clientId = clientNumber;
    }

    /**
     *
     * @return
     */
    protected Integer getClientId() {
        return clientId;
    }

    /**
     *
     * @throws IOException
     */
    private void processClient() throws IOException {
        String protocolMessage;
        try {
            while((protocolMessage = reader.readLine()) != null){
                processProtocol(protocolMessage);
            }
        } catch(IOException e){
            System.out.print("Socket closed\n");
        }

    }

    /**
     *
     * @param protocol
     */
    private void processProtocol(String protocol){
        String protocolWithId = clientId + Config.PROTOCOL_SEPARATOR + protocol;
        String[] spliced = protocolWithId.split(Config.PROTOCOL_SEPARATOR);

        if (spliced.length >= 3){
            String clientID = spliced[0];
            String protocolID = spliced[1];
            String data = spliced [2];

            switch(protocolID){
                case Config.PROTOCOL_END:
                    serverInstance.console.append(protocolWithId + "\n");
                    handleEndConnection(protocolID);
                    break;
                case Config.PROTOCOL_SENDGAME:
                    serverInstance.console.append(protocolWithId + "\n");
                    sendGameConfig(data);
                    break;
                case Config.PROTOCOL_RECVGAME:
                    serverInstance.console.append(protocolWithId + "\n");
                    receiveGameConfig();
                case Config.PROTOCOL_DATA:
                    serverInstance.console.append(protocolWithId + "\n");
                    playerData(protocolID);

                default:
                    serverInstance.console.append("Unknown protocol\n");
            }
        }
    }

    /**
     *
     * @param gameConfig
     */
    protected void sendGameConfig(String gameConfig) {
        // Implement logic for method
        serverInstance.setGameConfiguration(gameConfig);
    }

    /**
     *
     */
    protected void receiveGameConfig() {
        // Implement logic for method
        this.gameConfiguration = serverInstance.sendConfigurationToClients();
    }

    /**
     *
     * @return
     */
    protected String getGameConfig(){
        return gameConfig;
    }

    /**
     *
     * @param data
     */
    protected void playerData(String data) {
        // Implement logic for method
    }

    /**
     *
     * @param protocolID
     */
    protected void handleEndConnection(String protocolID) {
        try {
            reader.close();
            inputStream.close();
            clientSocket.close();

            serverInstance.disconnectClient(clientSocket);
            serverInstance.console.append(protocolID + "\n");
        } catch (IOException ex) {
            System.out.println("Error handling end connection: " + ex.getMessage());
        }
    }

    /**
     *
     * @return
     */
    protected Socket getClientSocket() {
        return clientSocket;
    }

    /**
     *
     */
    @Override
    public void run() {
        // This keeps the method running
        try {
            processClient();
        } catch (IOException e) {
            System.out.print("Socket closed\n");
        }
    }
}