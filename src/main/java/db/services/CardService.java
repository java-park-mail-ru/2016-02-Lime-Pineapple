package db.services;

import db.models.User;
import db.models.UserScore;
import db.models.game.cards.CardModel;
import org.jetbrains.annotations.NotNull;

import java.rmi.AccessException;
import java.util.Collection;

/**
 * created: 6/1/2016
 * package: db.services
 */
public interface CardService {
    Collection<CardModel> getUsers() throws AccessException;

    boolean hasCard(@NotNull String cardName) throws AccessException;

    long addCard(@NotNull CardModel card) throws AccessException;

    boolean removeCard(long id) throws AccessException;
    boolean removeCard(@NotNull String cardName) throws AccessException;

    User getCard(long id) throws AccessException;
    User getCard(@NotNull String cardName) throws AccessException;

    int getCount() throws AccessException;
    boolean changeCard(@NotNull CardModel card) throws AccessException;

    void clear() throws AccessException;
}
