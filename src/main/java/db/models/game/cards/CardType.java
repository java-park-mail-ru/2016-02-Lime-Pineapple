package db.models.game.cards;

import org.jetbrains.annotations.NotNull;

/**
 * created: 12-Mar-16
 * package: db.models.game
 */
public enum CardType {
    NONE("Cards.None", -1),
    ONETIMEUSE("Cards.OneTimeUse.Name", 0),
    HUMAN("Cards.Human.Name", 1),
    CREATURE("Cards.Creature.Name", 2),
    MONSTER("Cards.Monster.Name", 3),
    SPIRIT("Cards.Spirit.Name", 4);


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

    public int getCardId()
    {
        return this.cardId;
    }
}
