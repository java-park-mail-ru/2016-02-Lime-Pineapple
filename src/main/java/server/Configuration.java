package server;


import java.io.*;

public class Configuration implements Serializable{

    private int serverPort;
    public Configuration() {

    }
    public void setServerPort(int port) {
        this.serverPort=port;
    }
    public int getServerPort() {
        return serverPort;
    }

}
