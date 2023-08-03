package jap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import jap.protocol.*;


public class ClientHandler implements Runnable {
    private Server serverInstance;
    private Socket clientSocket;
    private String playerName;
    private String gameConfig;
    private String playerData;
    private Integer clientId;

    private ProtocolStreamReader reader;
    private BufferedReader input;
    private PrintWriter output;

    public ClientHandler(Socket clientSocket, Server server) {
        this.clientSocket = clientSocket;
        this.serverInstance = server;
        playerName = "";
        gameConfig = "";
        playerData = "";
/*        // Initialize input and output streams
        try {
            input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            output = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException ex) {
            System.out.println("Error creating input/output streams: " + ex.getMessage());
        }*/
    }

    protected void setClientId(Integer clientNumber) {
        this.clientId = clientNumber;
    }


    protected void handleEndConnection() {
        try {
            // Close input and output streams
            input.close();
            output.close();
            // Close the client socket
            clientSocket.close();
            System.out.println("Client " + clientId + " disconnected.");
        } catch (IOException ex) {
            serverInstance.disconnectClient(clientSocket);
            System.out.println("Error handling end connection: " + ex.getMessage());
        }
    }

    protected Socket getClientSocket() {
        return clientSocket;
    }


    private void processClient() throws IOException{
        while(true){
            ProtocolMessage receivedProtocol = reader.readMessage();

            String protocolID = receivedProtocol.getProtocolID();
            String data = receivedProtocol.getData();
            // Determine the protocol and execute the corresponding action

            switch (protocolID) {
                case Config.PROTOCOL_END:
                    handleP0Protocol(data);
                    break;
                case Config.PROTOCOL_SENDGAME:
                    handleP1Protocol(data);
                    break;
                case Config.PROTOCOL_RECVGAME:
                    handleP2Protocol(data);
                    break;
                case Config.PROTOCOL_DATA:
                    handleP3Protocol(data);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown protocol ID: " + protocolID);
            }

        }
    }

    protected void handleP0Protocol(String data){

    }

    protected void handleP1Protocol(String data){

    }

    protected void handleP2Protocol(String data){

    }
    protected void handleP3Protocol(String data){

    }

    @Override
    public void run() {
        try{
            processClient();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}