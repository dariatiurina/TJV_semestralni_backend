package cz.cvut.fit.tiuridar.tjv.controller.dto;

import lombok.Data;

@Data
public class FilmDto {
    private Long Id;
    private String name;
    private Integer duration;
    private Integer yearOfCreation;
    private Double rating;

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getYearOfCreation() {
        return yearOfCreation;
    }

    public void setYearOfCreation(Integer yearOfCreation) {
        this.yearOfCreation = yearOfCreation;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }
}
