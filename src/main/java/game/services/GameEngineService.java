package game.services;

import db.exceptions.DatabaseException;
import db.models.User;
import db.services.AccountService;
import game.GameRoom;
import game.PlayingUser;
import game.services.messages.EndGameMessageResponse;
import game.services.messages.GameMessageDeserializer;
import game.services.messages.PlayerActMessage;
import server.messaging.messages.SystemMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import server.messaging.Client;
import server.messaging.MessageService;

import java.io.IOException;

/**
 * created: 5/25/2016
 * package: game.services
 */
public class GameEngineService {
    static final Logger LOGGER = LogManager.getLogger();
    final MessageService messageService;
    final AccountService accountService;

    final GameMessageDeserializer deserializer;

    public GameEngineService(@NotNull GameMessageDeserializer deserializer,
                      @NotNull MessageService messageService,
                      @NotNull AccountService accountService) {
        this.messageService = messageService;
        this.accountService = accountService;
        this.deserializer = deserializer;
    }

    // Конфигурирует сервис: запускает слушатели событий на сервисе сообщений
    public void configure() {
        this.messageService.subscribe(SystemMessage.MESSAGES.CLIENT_DISCONNECTED, (client, message)->{
            this.onClientDisconnected(client);
        });
        this.messageService.subscribe(PlayerActMessage.MESSAGE_NAME, (client, message) -> {
            final PlayerActMessage gm = (PlayerActMessage) deserializer.deserialize(message);
            this.onPlayerAct(client, gm);
        });
    }


    // METHODS



    // ENGINE LOGIC GAME


    void endGame(@NotNull GameRoom room,@NotNull Client winnerClient,@NotNull EndGameReason reason) {
        final PlayingUser winner = winnerClient.getUser();
        final PlayingUser loser = room.getAnotherPlayer(winnerClient.getUser());
        final User winnerUser = winner.getLinkedUser();
        final User loserUser = loser.getLinkedUser();
        // обновим данные победителя
        try {
            this.accountService.updateUserStats(winnerUser, winner.getCurrentScore());
        } catch (DatabaseException e) {
            LOGGER.warn(String.format("[ W ] Exception during user stats update: %s", e.toString()));
        }
        // отправляем сообщения об окончании игры
        try {
            switch (reason) {
                case DISCONNECT:
                    this.messageService.sendMessage(winnerClient, new EndGameMessageResponse(winnerUser, reason));
                    break;
                case WE_HAVE_A_WINNER:
                    // обновим данные проигравшего, но только если он не отключался
                    try {
                        this.accountService.updateUserStats(loserUser, loser.getCurrentScore());
                    } catch (DatabaseException e) {
                        LOGGER.warn(String.format("[ W ] Exception during user stats update: %s", e.toString()));
                    }
                    this.messageService.sendMessage(room, new EndGameMessageResponse(winnerUser, reason));
                    break;
                case JUST_BECAUSE:
                    LOGGER.warn("[ W ] Wow, end game reason is just because?");
                    this.messageService.sendMessage(room, new EndGameMessageResponse(null, reason));
                    break;
            }
        } catch (IOException e) {
            LOGGER.warn(String.format("[ W ]Could not send message to endpoint. WTF?%n%s", e.toString()) );
        }

    }

    void playerAct(@NotNull GameRoom room, @NotNull PlayingUser actor, @NotNull PlayerActMessage.PlayerActData actData) {
        // выполняем действия до действия юзера

        // обрабатываем действия юзера
        // действия юзера - активировал карту босса, поставил карту на поле, пропустил ход.

        // выполняем действия после хода юзера

        // отсылаем сообщения
    }


    // MESSAGE HANDLERS

    private void onPlayerAct(Client client, PlayerActMessage message) {
        try {
            final GameRoom room = messageService.getRoom(client);
            assert (PlayerActMessage.PlayerActData) message.getData() != null;
            this.playerAct(room, client.getUser(), (PlayerActMessage.PlayerActData) message.getData());
        } catch (Throwable t) {
            LOGGER.warn(String.format("[ W ] Exception during event handler: onPlayerAct. Malformed message?%n%s", t.toString()));
        }

    }

    private void onClientDisconnected(Client client) {
        try {
            final GameRoom room = messageService.getRoom(client);
            this.endGame(room, client, EndGameReason.DISCONNECT);
        } catch (Throwable t) {
            LOGGER.warn(String.format("[ W ] Exception during event handler: onClientDisconnected(). Is client without room disconnected?%n%s", t.toString()));
        }

    }

    // INNER CLASSES

    public enum EndGameReason {
        DISCONNECT,
        WE_HAVE_A_WINNER,
        JUST_BECAUSE
    }

}
