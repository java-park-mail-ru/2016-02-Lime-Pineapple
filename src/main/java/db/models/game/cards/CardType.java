package db.models.game.cards;

import org.jetbrains.annotations.NotNull;

/**
 * created: 12-Mar-16
 * package: db.models.game
 */
public enum CardType {
    None("Cards.None", -1),
    OneTimeUse("Cards.OneTimeUse.Name", 0),
    Human("Cards.Human.Name", 1),
    Creature("Cards.Creature.Name", 2),
    Monster("Cards.Monster.Name", 3),
    Spirit("Cards.Spirit.Name", 4);


    private final String cardName;
    private final int cardId;
    /**
     * @param cardNameId Used to bind card name localized in desired language
     */
    CardType(final String cardNameId, final int cardId) {
        this.cardName = cardNameId;
        this.cardId = cardId;
    }

    /* (non-Javadoc)
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return cardName;
    }

    @NotNull
    public int getCardId()
    {
        return this.cardId;
    }
}
