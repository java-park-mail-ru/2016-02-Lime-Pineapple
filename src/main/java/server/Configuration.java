package server;


import java.io.*;


public class Configuration implements Serializable{

    protected static final int DEFAULTPORT=9991;
    private int serverPort;
    private String dataBaseName;
    private String userTableName;
    private String userName;
    private String userPassword;
    private String baseType;
    private String dbServer;
    public Configuration() {

    }
    public void setDataBaseName(String name) {
        this.dataBaseName=name;
    }
    public String getDataBaseName() {
        return dataBaseName;
    }
    public void setUserTableName(String name) {
        this.userTableName=name;
    }
    public String getUserTableName() {
        return userTableName;
    }
    public void setUserName(String name) {
        this.userName=name;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserPassword(String password) {
        this.userPassword=password;
    }
    public String getUserPassword() {
        return userPassword;
    }
    public void setServerPort(int port) {
        this.serverPort=port;
    }
    public int getServerPort() {
        return serverPort;
    }
    public static int getDefaultport() {
        return DEFAULTPORT;
    }
    public void setBaseType(String type) {
        this.baseType=type;
    }
    public String getBaseType() {
        return baseType;
    }
    public void setDbServer(String address) {
        this.dbServer=address;
    }
    public String getDbServer() {
        return dbServer;
    }

}
