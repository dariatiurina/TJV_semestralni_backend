package cz.cvut.fit.tiuridar.tjv.service;

import cz.cvut.fit.tiuridar.tjv.domain.Film;
import cz.cvut.fit.tiuridar.tjv.domain.Review;
import cz.cvut.fit.tiuridar.tjv.repository.ReviewRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.Optional;

@Component
public class ReviewServiceImpl extends CrudServiceImpl<Review, Long> implements ReviewService{
    ReviewRepository reviewRepository;

    @Override
    @Transactional
    public Review create(Review e) {
        if(e.getId() != null && getRepository().existsById(e.getId()))
            throw new IllegalArgumentException();
        for (Review i : e.getFilm().getWrittenReview()){
            if(i.getAuthor() == e.getAuthor())
                throw new UserCannotWriteMoreThanOneReviewToAFilm();
        }
        return getRepository().save(e);
    }

    @Override
    public void update(Long id, Review data) {
        Optional<Review> updateReview = reviewRepository.findById(id);
        if (updateReview.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        data.setId(id);
        if(data.getText() == null)
            data.setText(updateReview.get().getText());
        if(data.getRating() == null)
            data.setRating(updateReview.get().getRating());
        if(data.getAuthor() == null)
            data.setAuthor(updateReview.get().getAuthor());
        if(data.getFilm() == null)
            data.setFilm(updateReview.get().getFilm());
        reviewRepository.save(data);
    }

    public ReviewServiceImpl(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }
    @Override
    protected CrudRepository<Review, Long> getRepository() {
        return reviewRepository;
    }

    @Override
    public Collection<Review> findAllByAuthor(String authorId) {
        return reviewRepository.findAllByAuthorUsername(authorId);
    }

    @Override
    public Collection<Review> findAllByFilm(Long filmId) {
        return reviewRepository.findAllByFilmId(filmId);
    }
}
