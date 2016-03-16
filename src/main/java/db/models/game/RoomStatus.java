package db.models.game;

public enum RoomStatus {
    LOOKING_FOR_PEOPLE,
    PREPARING_FOR_GAME,
    WAITING_FOR_PLAYERS,
    INITIAL_PHASE,
    GAME_PHASE,
    PAUSE_PHASE,
    END_PHASE,
    WAITING_FOR_CLEANUP,
    FINISHED
}
