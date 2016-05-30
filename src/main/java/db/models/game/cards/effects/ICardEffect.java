package db.models.game.cards.effects;

import game.GameRoom;
import game.PlayingUser;
import db.models.game.cards.BaseCard;

import javax.validation.constraints.NotNull;

/**
 * created: 12-Mar-16
 * package: db.models.game
 */
public interface ICardEffect {

    @NotNull
    String getCardEffectName();

    @NotNull
    String getCardEffectDescription();

    @NotNull
    String getCardEffectType();

    void activateCardEffect(BaseCard linkedCard, PlayingUser cardUser, GameRoom cardOrigin );


}
