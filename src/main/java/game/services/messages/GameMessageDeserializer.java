package game.services.messages;

import org.jetbrains.annotations.NotNull;
import server.messaging.Message;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import server.messaging.MessageDeserializer;
import server.messaging.MessageType;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * created: 6/1/2016
 * package: game.services.messages
 */
public class GameMessageDeserializer implements MessageDeserializer {
    Map<String, MessageDeserializer> map = new ConcurrentHashMap<>();

    @Override
    public void registerSubDeserializer(@NotNull String messageName, @NotNull MessageDeserializer deserializer) throws RuntimeException {
        if (map.getOrDefault(messageName, null) == null)
            map.put(messageName, deserializer);
        else
            throw new RuntimeException("MessageDeserializer with this message name is already registered");
    }

    @NotNull
    @Override
    public Message deserialize(Message message) throws RuntimeException {
        // we know, that message.data is either empty string or data
        if (message.getType() != MessageType.GAME) {
            throw new RuntimeException("Message Type is not valid. Expected MessageType.GAME");
        }
        // probably throws RuntimeException
        return map.get(message.getName()).deserialize(message);
    }

}
