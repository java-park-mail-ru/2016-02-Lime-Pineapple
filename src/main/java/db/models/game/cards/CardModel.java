package db.models.game.cards;

import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

@Entity
@Table(name="CardModel")
//TODO: add events: OnCardPlaced(), OnCardExpired()
public class CardModel {

    @Id
    @Column
    String cardName;

    @Column
    String cardDescription;

    @OneToMany()
    Collection<EffectModel> cardEffects;

    @Column(name="cardType")
    CardType cardType;

    public void setCardType(CardType type) {this.cardType=type;}

    @NotNull
    public CardType getCardType() {
        return cardType;
    }

    @NotNull
    public Collection<EffectModel> getCardEffects() {
        return cardEffects;
    }

    CardModel() {
        this.cardEffects = new Vector<>();
        this.cardType = CardType.NONE;
    }

    CardModel(CardType type, long lifetime) {
        this.cardType=type;
        this.cardEffects = new Vector<>();
    }
}