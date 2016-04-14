package db.models.game.cards;

import db.models.game.cards.effects.ICardEffect;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Vector;

/**
 * created: 12-Mar-16
 * package: db.models.game
 */


public class BaseCard {
    protected final Vector<ICardEffect> cardEffects;
    protected final CardType cardType;
    protected final Long timeToLive;

    @NotNull
    public CardType getCardType() {
        return cardType;
    }

    @NotNull
    public List<ICardEffect> getCardEffects() {
        return cardEffects;
    }

    @NotNull
    public Long getTimeToLive() {
        return timeToLive;
    }

    BaseCard() {
        this.cardEffects = new Vector<>();
        this.cardType = CardType.GENERIC;
        this.timeToLive = 0L;
    }
}
