package game.services;

import game.GameField;
import game.GameFieldRow;

/**
 * created: 6/1/2016
 * package: game
 */
public class GameFieldFactoryService {

    void buildRows(GameFieldRow[] rows, int cardsInRow) {
        // implementation of buildRows
        rows[0] = new GameFieldRow(cardsInRow, GameField.FieldType.RANGED);
        rows[1] = new GameFieldRow(cardsInRow, GameField.FieldType.MELEE);
        rows[2] = new GameFieldRow(cardsInRow, GameField.FieldType.MELEE);
        rows[3] = new GameFieldRow(cardsInRow, GameField.FieldType.RANGED);
    }

    GameField makeField(int cardsInRow) {
        final GameFieldRow[] rows = new GameFieldRow[4];
        buildRows(rows, cardsInRow);
        return new GameField(rows);
    }
}

