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


    private void processClient() throws IOException {
        String protocolID = "";
        String data = "";
        try {
            String protocolMessage;

            while ((protocolMessage = reader.readLine()) != null) {
                String[] splice = protocolMessage.split(Config.PROTOCOL_SEPARATOR);
                if (splice.length < 2) {
                    serverInstance.console.append("Invalid protocol\n");
                    continue;
                }
                protocolID = splice[0];
                data = splice[1];
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Determine the protocol and execute the corresponding action
        switch (protocolID) {
            case Config.PROTOCOL_END:
                handleEndConnection(protocolID, data);
                return;
            case Config.PROTOCOL_SENDGAME:
                sendGameConfig(data);
                break;
            case Config.PROTOCOL_RECVGAME:
                receiveGameConfig(data);
                break;
            case Config.PROTOCOL_DATA:
                playerData(data);
                break;
            default:
                throw new IllegalArgumentException("Unknown protocol ID: " + protocolID);
        }

    }


    protected void sendGameConfig(String data) {

    }

    protected void receiveGameConfig(String data) {

    }

    protected void playerData(String data) {

    }

    protected void handleEndConnection(String protocolId, String data) {
        try {
            reader.close();
            inputStream.close();

            clientSocket.close();

            serverInstance.console.append("Client " + clientId + " disconnected.");
            serverInstance.console.append(protocolId + data);
        } catch (IOException ex) {
            serverInstance.disconnectClient(clientSocket);
            System.out.println("Error handling end connection: " + ex.getMessage());
        }
    }

    protected Socket getClientSocket() {
        return clientSocket;
    }

    @Override
    public void run() {
        try {
            processClient();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}