package cz.cvut.fit.tiuridar.tjv.service;

import cz.cvut.fit.tiuridar.tjv.domain.Film;
import cz.cvut.fit.tiuridar.tjv.domain.Review;
import cz.cvut.fit.tiuridar.tjv.domain.User;
import cz.cvut.fit.tiuridar.tjv.repository.FilmRepository;
import cz.cvut.fit.tiuridar.tjv.repository.ReviewRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

@SpringBootTest
public class ReviewServiceImplTest {
    @Autowired
    ReviewService reviewService;
    @MockBean
    ReviewRepository reviewRepository;
    @MockBean
    FilmRepository filmRepository;

    @Test
    void userWritesTwoReviews() {
        User author = new User();
        Film film = new Film();
        Review review1 = new Review();
        Review review2 = new Review();

        author.setUsername("username");
        film.setId((long) 1);
        review1.setId((long) 1);
        review1.setAuthor(author);
        review1.setFilm(film);
        review2.setAuthor(author);
        review2.setFilm(film);
        review2.setId((long) 2);
        film.getWrittenReview().add(review1);


        Mockito.when(filmRepository.findById(film.getId())).thenReturn(Optional.of(film));
        Mockito.when(reviewRepository.findById(review1.getId())).thenReturn(Optional.of(review1));
        Mockito.when(reviewRepository.findById(review2.getId())).thenReturn(Optional.of(review2));

        Assertions.assertThrows(UserCannotWriteMoreThanOneReviewToAFilm.class, () -> reviewService.create(review2));
    }
}
