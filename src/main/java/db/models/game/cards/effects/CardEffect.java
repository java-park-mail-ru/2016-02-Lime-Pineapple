package db.models.game.cards.effects;

import db.models.game.cards.BaseCard;
import game.GameRoom;
import game.PlayingUser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by igor on 29.05.16.
 */
@Entity
@Table(name="Effects")
public class CardEffect implements ICardEffect {

    @Id
    @Column(name="EffectName")
    @NotNull
    String name="effect";
    @Column(name="Gif")
    @Nullable
    String visualEffect;
    @Column(name="desciption")
    @NotNull
    String description="No description provided";
    @Column(name="effectType")
    String type;
    @Override
    public String getCardEffectName() {
        return name;
    }
    @Override
    public String getCardEffectType() {
        return type;
    }
    @Override
    public String getCardEffectDescription() {
        return description;
    }
    @Override
    public void activateCardEffect(BaseCard linkedCard, PlayingUser cardUser, GameRoom cardOrigin ) {

    }
    CardEffect() {

    }
    CardEffect(@NotNull String descriptions, @NotNull String cardName, String tip, @NotNull String gif) {
        description=descriptions;
        name=cardName;
        type=tip;
        visualEffect=gif;
    }
}
