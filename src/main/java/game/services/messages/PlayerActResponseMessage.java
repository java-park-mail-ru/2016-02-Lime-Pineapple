package game.services.messages;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import org.jetbrains.annotations.Nullable;
import server.messaging.Message;
import server.messaging.MessageType;

/**
 * created: 6/3/2016
 * package: game.services.messages
 */

// Используется при ответе на действия сообщения самому пользователю - источнику действия.
public class PlayerActResponseMessage extends Message {
    public static final String MESSAGE_NAME = "Game.Player.Act.Response";
    private final boolean acknowledged;

    public PlayerActResponseMessage(boolean acknowledgedOrRejected) {
        super(MessageType.GAME, MESSAGE_NAME);
        this.acknowledged = acknowledgedOrRejected;
    }

    @Nullable
    @Override
    public JsonElement serializeData() {
        return new JsonPrimitive(acknowledged);
    }
}
