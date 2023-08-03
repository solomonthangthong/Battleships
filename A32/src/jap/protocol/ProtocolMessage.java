package jap.protocol;

public class ProtocolMessage {
    private String protocolID;
    private String data;

    public ProtocolMessage(String protocolID, String data){
        this.protocolID = protocolID;
        this.data = data;
    }

    public String getProtocolID(){
        return protocolID;
    }

    public String getData(){
        return data;
    }
}
