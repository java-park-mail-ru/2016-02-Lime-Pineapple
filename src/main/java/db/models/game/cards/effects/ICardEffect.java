package db.models.game.cards.effects;

import db.models.User;
import db.models.game.GameRoom;
import db.models.game.PlayingUser;
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

    void ActivateCardEffect(BaseCard linkedCard, PlayingUser cardUser, GameRoom cardOrigin );
}
