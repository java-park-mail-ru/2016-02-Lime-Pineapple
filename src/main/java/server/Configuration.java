package server;

public class Configuration {

    protected static final int DEFAULTPORT=9991;
    private int serverPort;
    private String dataBaseName;
    private String userTableName;
    private String userName;
    private String userPassword;
    private String baseType;
    private String dbServer;
    public Configuration(String fileName){

        readParams("./cfg/"+fileName);
    }
    public void readParams(String filename) {
        dataBaseName="WHACKAMOLEUSERS";
        userTableName="users";
        userName="root";
        userPassword="yyvt9z3e";
        baseType="mysql";
        dbServer="127.0.0.1:3306";
        serverPort=9995;
    }
    public String getDataBaseName() {
        return dataBaseName;
    }
    public String getUserTableName() {
        return userTableName;
    }
    public String getUserName() {
        return userName;
    }
    public String getUserPassword() {
        return userPassword;
    }
    public int getServerPort() {
        return serverPort;
    }
    public static int getDefaultport() {
        return DEFAULTPORT;
    }
    public String getBaseType() {
        return baseType;
    }
    public String getDbServer() {
        return dbServer;
    }
}
