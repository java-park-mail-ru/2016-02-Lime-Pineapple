package db.models.game.cards.effects;

import db.models.User;
import db.models.game.GameRoom;
import db.models.game.PlayingUser;
import db.models.game.cards.BaseCard;

/**
 * created: 12-Mar-16
 * package: db.models.game.cards.effects
 */


public class RoachEffect implements ICardEffect {
    @Override
    public String getCardEffectName() {
        return "Roach";
    }

    @Override
    public String getCardEffectDescription() {
        return "Increases player score by 1 on success";
    }

    @Override
    public String getCardEffectType() {
        return "Score/Roach";
    }



    @Override
    public void ActivateCardEffect(BaseCard linkedCard, PlayingUser cardTarget, GameRoom cardOrigin) {
        cardTarget.incrementScore(1L);
    }
}
