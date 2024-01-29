package cz.cvut.fit.tiuridar.tjv.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class Review implements EntityWithId<Long> {
    @Id
    @GeneratedValue
    private Long id;
    private String text;
    private Integer rating;
    @ManyToOne
    private User author;
    @ManyToOne
    private Film film;

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) return false;

        Review review = (Review) obj;
        return Objects.equals(id, review.id);
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Film getFilm() {
        return film;
    }

    public void setFilm(Film film) {
        this.film = film;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        if(rating == null)
            this.rating = null;
        else if (rating > 10 || rating < 0)
            this.rating = null;
        else
            this.rating = rating;
    }

    @Override
    public Long getId() {
        return id;
    }
}
