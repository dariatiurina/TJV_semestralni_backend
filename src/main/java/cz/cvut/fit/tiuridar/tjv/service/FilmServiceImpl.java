package cz.cvut.fit.tiuridar.tjv.service;

import cz.cvut.fit.tiuridar.tjv.domain.Film;
import cz.cvut.fit.tiuridar.tjv.domain.Review;
import cz.cvut.fit.tiuridar.tjv.domain.StreamingService;
import cz.cvut.fit.tiuridar.tjv.repository.FilmRepository;
import cz.cvut.fit.tiuridar.tjv.repository.ReviewRepository;
import cz.cvut.fit.tiuridar.tjv.repository.StreamingServiceRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Component
public class FilmServiceImpl extends CrudServiceImpl<Film, Long> implements FilmService {
    private final FilmRepository filmRepository;
    private final StreamingServiceRepository streamingServiceRepository;

    private final ReviewRepository reviewRepository;

    public FilmServiceImpl(FilmRepository filmRepository, StreamingServiceRepository streamingServiceRepository, ReviewRepository reviewRepository) {
        this.filmRepository = filmRepository;
        this.streamingServiceRepository = streamingServiceRepository;
        this.reviewRepository = reviewRepository;
    }

    @Override
    protected CrudRepository<Film, Long> getRepository() {
        return filmRepository;
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
    public void update(Long id, Film data) {
        Optional<Film> updateFilm = filmRepository.findById(id);
        if (updateFilm.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        data.setId(id);
        if (data.getName() == null)
            data.setName(updateFilm.get().getName());
        if (data.getDuration() == null)
            data.setDuration(updateFilm.get().getDuration());
        if (data.getWrittenReview() == null)
            data.setWrittenReview(updateFilm.get().getWrittenReview());
        if (data.getYearOfCreation() == null)
            data.setYearOfCreation(updateFilm.get().getYearOfCreation());
        data.setStreamingServicesIsOn(updateFilm.get().getStreamingServicesIsOn());
        filmRepository.save(data);
    }

    @Override
    public void deleteById(Long id) {
        Optional<Film> film = filmRepository.findById(id);
        if(film.isEmpty()) {
            throw new IllegalArgumentException();
        }
        for(Review i : film.get().getWrittenReview())
            reviewRepository.deleteById(i.getId());
        for(StreamingService i : film.get().getStreamingServicesIsOn())
            i.getHasFilms().remove(film.get());
        filmRepository.deleteById(id);
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
}
