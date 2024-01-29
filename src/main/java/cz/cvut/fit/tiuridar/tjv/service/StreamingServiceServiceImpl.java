package cz.cvut.fit.tiuridar.tjv.service;

import cz.cvut.fit.tiuridar.tjv.domain.Film;
import cz.cvut.fit.tiuridar.tjv.domain.Review;
import cz.cvut.fit.tiuridar.tjv.domain.SortStreaming;
import cz.cvut.fit.tiuridar.tjv.domain.StreamingService;
import cz.cvut.fit.tiuridar.tjv.repository.FilmRepository;
import cz.cvut.fit.tiuridar.tjv.repository.StreamingServiceRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Component
public class StreamingServiceServiceImpl extends CrudServiceImpl<StreamingService, Long> implements StreamingServiceService {
    StreamingServiceRepository streamingServiceRepository;
    FilmRepository filmRepository;

    public StreamingServiceServiceImpl(StreamingServiceRepository streamingServiceRepository, FilmRepository filmRepository) {
        this.streamingServiceRepository = streamingServiceRepository;
        this.filmRepository = filmRepository;
    }

    @Override
    public Collection<StreamingService> returnBest() {
        List<StreamingService> ret = (List<StreamingService>) streamingServiceRepository.findAll();
        ret.sort(new SortStreaming());
        return ret;
    }

    @Override
    public void update(Long id, StreamingService data) {
        Optional<StreamingService> updateService = streamingServiceRepository.findById(id);
        if (updateService.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        data.setId(id);
        if (data.getName() == null)
            data.setName(updateService.get().getName());
        data.setHasFilms(updateService.get().getHasFilms());
        streamingServiceRepository.save(data);
    }

    @Override
    public void deleteById(Long id) {
        Optional<StreamingService> streamingService = streamingServiceRepository.findById(id);
        if(streamingService.isEmpty()) {
            throw new IllegalArgumentException();
        }
        for(Film i : streamingService.get().getHasFilms())
            i.getStreamingServicesIsOn().remove(streamingService.get());
        streamingServiceRepository.deleteById(id);
    }

    @Override
    public void addToANewStreamingService(Long filmId, Long streamingServiceId) {
        Optional<Film> optionalFilm = filmRepository.findById(filmId);
        Optional<StreamingService> optionalStreamingService = streamingServiceRepository.findById(streamingServiceId);

        if (optionalFilm.isEmpty() || optionalStreamingService.isEmpty())
            throw new IllegalArgumentException();

        Film film = optionalFilm.get();
        StreamingService streamingService = optionalStreamingService.get();

        if (!film.getStreamingServicesIsOn().contains(streamingService))
            film.getStreamingServicesIsOn().add(streamingService);
        if (!streamingService.getHasFilms().contains(film))
            streamingService.getHasFilms().add(film);

        filmRepository.save(film);
        streamingServiceRepository.save(streamingService);
    }

    @Override
    public void deleteFromAStreamingService(Long filmId, Long streamingServiceId) {
        Optional<Film> optionalFilm = filmRepository.findById(filmId);
        Optional<StreamingService> optionalStreamingService = streamingServiceRepository.findById(streamingServiceId);

        if (optionalFilm.isEmpty() || optionalStreamingService.isEmpty())
            throw new IllegalArgumentException();

        Film film = optionalFilm.get();
        StreamingService streamingService = optionalStreamingService.get();

        film.getStreamingServicesIsOn().remove(streamingService);
        streamingService.getHasFilms().remove(film);

        filmRepository.save(film);
        streamingServiceRepository.save(streamingService);
    }

    @Override
    protected CrudRepository<StreamingService, Long> getRepository() {
        return streamingServiceRepository;
    }
}
