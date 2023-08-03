package jap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private String playerName;
    private String gameConfig;
    private String playerData;
    private Integer clientId;
private BufferedReader input;
private PrintWriter output;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
        playerName = "";
        gameConfig = "";
        playerData = "";
        // Initialize input and output streams
        try {
            input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            output = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException ex) {
            System.out.println("Error creating input/output streams: " + ex.getMessage());
        }
    }

    protected void setClientId(Integer clientNumber) {
        this.clientId = clientNumber;
    }


    void handleEndConnection() {
        try {
            // Close input and output streams
            input.close();
            output.close();
            // Close the client socket
            clientSocket.close();

            System.out.println("Client " + clientId + " disconnected.");
        } catch (IOException ex) {
            System.out.println("Error handling end connection: " + ex.getMessage());
        }
    }
    public Socket getClientSocket() {
        return clientSocket;
    }


    @Override
    public void run() {

    }
}