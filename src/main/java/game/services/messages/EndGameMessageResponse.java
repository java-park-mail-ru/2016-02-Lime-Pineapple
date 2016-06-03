package game.services.messages;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import db.models.User;
import game.services.GameEngineService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import server.messaging.Message;
import server.messaging.MessageType;


/**
 * created: 6/3/2016
 * package: game.services.messages
 */
public class EndGameMessageResponse extends Message {
    public static final String MESSAGE_NAME = "Game.EndGame";
    private final Long winner;
    private final GameEngineService.EndGameReason reason;

    public EndGameMessageResponse(@Nullable User winner, @NotNull GameEngineService.EndGameReason reason) {
        super(MessageType.GAME, MESSAGE_NAME);
        if (winner != null) {
            this.winner = winner.getId();
        } else {
            this.winner = null;
        }
        this.reason = reason;
    }

    @Nullable
    @Override
    public Object getData() {
        return super.getData();
    }

    @Nullable
    @Override
    public JsonElement serializeData() {
        final JsonObject obj = new JsonObject();
        obj.addProperty("winner", winner);
        obj.addProperty("reason", reason.ordinal());
        return obj;
    }
}
