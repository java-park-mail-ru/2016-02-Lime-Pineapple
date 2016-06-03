package game;

import db.models.game.cards.CardModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * created: 6/1/2016
 * package: game
 */
public class GameFieldRow {
    final Card[] row;
    final GameField.FieldType type;

    public GameFieldRow(int size, GameField.FieldType type) {
        this.row = new Card[size];
        this.type = type;
    }

    void putCardAt(int position, @NotNull Card card) {
        this.row[position] = card;
    }

    @Nullable
    Card getCardAt(int position) {
        return row[position];
    }

    boolean hasCardAt(int position) {
        return row[position] != null;
    }

    @NotNull
    Card[] getRow() {
        return this.row;
    }

    GameField.FieldType getType() {
        return this.type;
    }
}
