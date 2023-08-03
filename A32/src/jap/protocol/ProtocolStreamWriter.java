package jap.protocol;

import jap.Config;

import java.io.IOException;
import java.io.OutputStream;

public class ProtocolStreamWriter {
    private OutputStream outputStream;

    public ProtocolStreamWriter(OutputStream outputStream){
        this.outputStream = outputStream;
    }

    public void writeMessage(String protocolId, String data) throws IOException{
        String message = protocolId + Config.FIELD_SEPARATOR + data + Config.PROTOCOL_SEPARATOR;
        outputStream.write(message.getBytes());
    }
}
