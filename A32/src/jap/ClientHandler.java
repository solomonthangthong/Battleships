package jap;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket socket;
    private Socket clientSocket;
    private String playerName;
    private String gameConfig;
    private String playerData;


    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
        playerName = "";
        gameConfig = "";
        playerData = "";
    }



    @Override
    public void run() {

    }
}