package cz.cvut.fit.tiuridar.tjv.controller.dto;

import lombok.Data;

@Data
public class ReviewDto {
    private Long Id;
    private String text;
    private Integer rating;
    private UserDto author;
    private FilmDto film;

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
        this.rating = rating;
    }

    public UserDto getAuthor() {
        return author;
    }

    public void setAuthor(UserDto author) {
        this.author = author;
    }

    public FilmDto getFilm() {
        return film;
    }

    public void setFilm(FilmDto film) {
        this.film = film;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }
}
