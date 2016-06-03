package server.messaging;

import org.junit.Test;

/**
 * created: 6/3/2016
 * package: server.messaging
 */
public class MessageTest {

    @Test
    public void testMessageSerialization() {

    }

    @Test
    public void testMessageDeserialization() {
        final int  type = MessageType.SYSTEM.ordinal();
        final String name = "Lalka";
        final String dataString = "{ \"id\" : 0}";
        final String testString = String.format("{ \"type\": %d, \"name\":\"%s\", \"data\": %s}", type, name, dataString);
        final Message msg = Message.deserializeToGenericMessage(testString);
        final Message model = new Message(MessageType.values()[type], name);
        model.setDataString(dataString);


    }

}
