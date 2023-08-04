package jap;

import java.io.*;
import java.net.Socket;
import java.util.Objects;

public class ClientHandler implements Runnable {
    private Server serverInstance;
    private Socket clientSocket;
    private String playerName;
    private String gameConfig;
    private String playerData;
    private Integer clientId;

    private InputStream inputStream;
    private BufferedReader reader;

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

    protected void setClientId(Integer clientNumber) {
        this.clientId = clientNumber;
    }

    protected Integer getClientId() {
        return clientId;
    }


    private void processClient() throws IOException {
        String protocolMessage;
        try {
            while((protocolMessage = reader.readLine()) != null){
                processProtocol(protocolMessage);
            }
        } catch(IOException e){
            e.printStackTrace();
        }

    }

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
                    sendGameConfig(protocolID);
                    break;
                case Config.PROTOCOL_RECVGAME:
                    serverInstance.console.append(protocolWithId + "\n");
                    receiveGameConfig(protocolID);
                case Config.PROTOCOL_DATA:
                    serverInstance.console.append(protocolWithId + "\n");
                    playerData(protocolID);

                default:
                    serverInstance.console.append("Unknown protocol\n");
            }
        }
    }

    protected void sendGameConfig(String protocolID) {
        // Implement logic for method
    }

    protected void receiveGameConfig(String data) {
        // Implement logic for method
    }

    protected void playerData(String data) {
        // Implement logic for method
    }

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

    protected Socket getClientSocket() {
        return clientSocket;
    }

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