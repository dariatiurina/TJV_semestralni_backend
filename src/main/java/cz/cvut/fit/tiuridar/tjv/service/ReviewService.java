package cz.cvut.fit.tiuridar.tjv.service;

import cz.cvut.fit.tiuridar.tjv.domain.Review;

import java.util.Collection;

public interface ReviewService extends CrudService<Review, Long>{
    Collection<Review> findAllByAuthor(String authorId);
    Collection<Review> findAllByFilm(Long filmId);
}
