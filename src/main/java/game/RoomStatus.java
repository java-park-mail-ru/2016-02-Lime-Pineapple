package game;

public enum RoomStatus {
    LOOKING_FOR_PEOPLE, // First state, when Room was created
    PREPARING_FOR_GAME, // Next state, when Room is populated with enough people
    WAITING_FOR_PLAYERS, // Next state, when Game Start was pressed by all players and Server is waiting for everyone to report resource loading.
    INITIAL_PHASE, // This state indicates game's start. In this phase, $Perpetrators are making their move
    GAME_PHASE, // Game started.
    PAUSE_PHASE, // Game was paused. It can be due to one of the players disconnected or one of the users explicitly paused the game
    END_PHASE, // Ending phase. In this phase, server is notifying players that game is finished
    WAITING_FOR_CLEANUP, // In this phase, players can see their results and exit the Game room. Server must hold resources until final phase
    FINISHED // Game is finished and ready to be finalized
}
