package cz.cvut.fit.tiuridar.tjv.service;

import cz.cvut.fit.tiuridar.tjv.domain.User;

import java.util.Collection;

public interface UserService extends CrudService<User, String> {
    Collection<User> findUsersWhoWrittenMoreThanReviews(Integer sizeReviews);
}
