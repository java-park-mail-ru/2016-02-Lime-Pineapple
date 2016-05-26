package server.messaging;

/**
 * created: 5/26/2016
 * package: server.messaging
 */
public class Message {
    MessageType type;
    String name;
    String data;

    public MessageType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getData() {
        return data;
    }
}
