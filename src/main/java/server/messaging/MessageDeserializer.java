package server.messaging;

import org.jetbrains.annotations.NotNull;

/**
 * created: 6/1/2016
 * package: server.messaging
 */
public interface MessageDeserializer {
    // returns deserialized message
    void registerSubDeserializer(@NotNull String messageName,@NotNull MessageDeserializer deserializer) throws RuntimeException;

    @NotNull
    Message deserialize(Message message);
}
