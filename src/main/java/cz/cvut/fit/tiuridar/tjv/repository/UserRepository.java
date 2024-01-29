package cz.cvut.fit.tiuridar.tjv.repository;

import cz.cvut.fit.tiuridar.tjv.domain.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Collection;

@Repository
public interface UserRepository extends CrudRepository<User, String> {
    @Query("SELECT u from User u WHERE size(u.writtenByUser) >= :sizeReviews")
    Collection<User> findUsersWhoWrittenMoreThanReviews(@Param("sizeReviews") Integer sizeReviews);

}
