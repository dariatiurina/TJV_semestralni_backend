package cz.cvut.fit.tiuridar.tjv.domain;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

@Entity
public class StreamingService implements EntityWithId<Long> {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    @ManyToMany(mappedBy = "streamingServicesIsOn")
    private Collection<Film> hasFilms = new ArrayList<>();

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) return false;

        StreamingService streamingService = (StreamingService) obj;
        return Objects.equals(id, streamingService.id);
    }

    @Override
    public Long getId() {
        return id;
    }

    public Collection<Film> getHasFilms() {
        return hasFilms;
    }

    public void setHasFilms(Collection<Film> hasFilms) {
        this.hasFilms = hasFilms;
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
}
