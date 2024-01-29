package cz.cvut.fit.tiuridar.tjv.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

@Entity
public class Film implements EntityWithId<Long> {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private Integer duration;
    private Integer yearOfCreation;
    private Double rating;

    @OneToMany(mappedBy = "film")
    @JsonIgnore
    private Collection<Review> writtenReview = new ArrayList<>();
    @ManyToMany
    private Collection<StreamingService> streamingServicesIsOn = new ArrayList<>();

    public Double getRating() {
        Double sum = (double) 0;
        int count = 0;
        for (Review i : writtenReview) {
            if (i.getRating() != null) {
                sum += i.getRating();
                count++;
            }
        }
        if (count > 0)
            this.rating = sum / count;
        else
            this.rating = null;
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Collection<Review> getWrittenReview() {
        return writtenReview;
    }

    public void setWrittenReview(Collection<Review> writtenReview) {
        this.writtenReview = writtenReview;
    }

    public Collection<StreamingService> getStreamingServicesIsOn() {
        return streamingServicesIsOn;
    }

    public void setStreamingServicesIsOn(Collection<StreamingService> streamingServicesIsOn) {
        this.streamingServicesIsOn = streamingServicesIsOn;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) return false;

        Film film = (Film) obj;
        return Objects.equals(id, film.id);
    }

    public void setId(Long id) {
        this.id = id;
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

    @Override
    public Long getId() {
        return id;
    }
}
