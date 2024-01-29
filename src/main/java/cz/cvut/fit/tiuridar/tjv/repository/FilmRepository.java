package cz.cvut.fit.tiuridar.tjv.repository;

import cz.cvut.fit.tiuridar.tjv.domain.Film;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface FilmRepository extends CrudRepository<Film, Long> {
}
