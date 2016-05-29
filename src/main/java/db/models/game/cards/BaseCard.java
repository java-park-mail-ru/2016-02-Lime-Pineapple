package db.models.game.cards;

import db.models.game.cards.effects.ICardEffect;
import org.jetbrains.annotations.NotNull;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.smartcardio.Card;
import java.util.List;
import java.util.Vector;

@Entity
@Table(name="Cards")
//TODO: add events: OnCardPlaced(), OnCardExpired()
public class BaseCard {

    @OneToMany(mappedBy="Effects")
    protected Vector<ICardEffect> cardEffects;
    @Column(name="cardType")
    protected CardType cardType;
    @Column(name="lifetime")
    protected Long timeToLive; //time interval, after which card will expire. In milliseconds

    public void setCardType(CardType type) {this.cardType=type;}
    @NotNull
    public CardType getCardType() {
        return cardType;
    }
    public void setTimeToLive(long time) {this.timeToLive=time;}
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
        this.cardType = CardType.NONE;
        this.timeToLive = 0L;
    }
    BaseCard(CardType type, long lifetime) {
        this.cardType=type;
        this.timeToLive=lifetime;
        this.cardEffects = new Vector<>();
    }
}