package db.models.game.cards;

/**
 * created: 12-Mar-16
 * package: db.models.game
 */
public enum CardType {
    GENERIC("GENERIC Card", 0),
    ROACH("ROACH Card", 1),
    PIGGIE("PIGGIE Card", 2);

    private final String cardName;
    private final int cardId;
    /**
     * @param cardName Name of the card
     * @param cardId Card number (id)
     */
    CardType(final String cardName, final int cardId) {
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
