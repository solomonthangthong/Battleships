package jap.protocol;

import jap.Config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ProtocolStreamReader {
    private BufferedReader reader;

    public ProtocolStreamReader(InputStream inputStream) {
        this.reader = new BufferedReader(new InputStreamReader(inputStream));
    }

    public ProtocolMessage readMessage() throws IOException {
        String receivedData = reader.readLine();

        if (receivedData == null){
            throw new IOException("End of stream reached");
        }
        String[] splice = receivedData.split(Config.FIELD_SEPARATOR);

        if (splice.length < 2){
            throw new IOException("Invalid message format");
        }

        String protocolID = splice[0];
        String data = splice[1];

        return new ProtocolMessage(protocolID, data);
    }
}
