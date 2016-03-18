package db.models.game.cards.effects;

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
        return "ROACH";
    }

    @Override
    public String getCardEffectDescription() {
        return "Increases player score by 1 on success";
    }

    @Override
    public String getCardEffectType() {
        return "Score/ROACH";
    }



    @Override
    public void activateCardEffect(BaseCard linkedCard, PlayingUser cardTarget, GameRoom cardOrigin) {
        cardTarget.incrementScore(1L);
    }
}
