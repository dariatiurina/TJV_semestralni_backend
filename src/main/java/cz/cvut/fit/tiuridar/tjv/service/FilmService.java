package cz.cvut.fit.tiuridar.tjv.service;


import cz.cvut.fit.tiuridar.tjv.domain.Film;

public interface FilmService extends CrudService<Film, Long> {
    void addToANewStreamingService(Long filmId, Long streamingServiceId);

    void deleteFromAStreamingService(Long filmId, Long streamingServiceId);
}
