package cz.cvut.fit.tiuridar.tjv.repository;

import cz.cvut.fit.tiuridar.tjv.domain.Review;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface ReviewRepository extends CrudRepository<Review, Long> {
    Collection<Review> findAllByAuthorUsername(String authorId);

    Collection<Review> findAllByFilmId(Long filmId);
}
