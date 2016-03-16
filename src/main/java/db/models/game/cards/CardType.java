package db.models.game.cards;

import org.jetbrains.annotations.NotNull;

/**
 * created: 12-Mar-16
 * package: db.models.game
 */
public enum CardType {
    Generic("Generic Card", 0),
    Roach("Roach Card", 1),
    Piggie("Piggie Card", 2);

    private final String cardName;
    private final int cardId;
    /**
     * @param cardName
     */
    private CardType(final String cardName, final int cardId) {
        this.cardName = cardName;
        this.cardId = cardId;
    }

    /* (non-Javadoc)
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return cardName;
    }


    public int getCardId()
    {
        return this.cardId;
    }
}
