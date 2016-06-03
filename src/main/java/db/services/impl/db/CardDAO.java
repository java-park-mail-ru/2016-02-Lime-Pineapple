package db.services.impl.db;

import db.models.User;
import db.models.game.cards.CardModel;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;


/**
 * created: 6/1/2016
 * package: db.services.impl.db
 */
public class CardDAO {
    @SuppressWarnings("unchecked")
    List<CardModel> getCardModels(@NotNull Session session) {
        return session.createCriteria(CardModel.class).list();
    }

    CardModel getCardModel(@NotNull Session session,@NotNull Long id) {
        return session.get(CardModel.class, id);
    }

    @Nullable
    CardModel getCardModel(@NotNull Session session,@NotNull String cardName) {
        final Criteria criteria = session.createCriteria(CardModel.class);
        final Object card = criteria.add(Restrictions.eq("cardName", cardName))
                .uniqueResult();
        return (CardModel)card;
    }

    void clear(@NotNull Session session) {
        final String hql = String.format("delete from %s","Cards");
        final Query query = session.createQuery(hql);
        query.executeUpdate();
    }

    void addCardModel(@NotNull Session session,@NotNull CardModel card) {
        session.save(card);
    }

    boolean removeCardModel(@NotNull Session session,@NotNull String cardName) {
        return DBUtilities.deleteById(session, CardModel.class, cardName);
    }


    Long getCount(@NotNull Session session) {
        return (Long)session.createCriteria(CardModel.class).setProjection(Projections.rowCount()).uniqueResult();
    }

    boolean changeCardModel(@NotNull Session session, @NotNull CardModel card) {
        session.update(card);
        return true;
    }
}


