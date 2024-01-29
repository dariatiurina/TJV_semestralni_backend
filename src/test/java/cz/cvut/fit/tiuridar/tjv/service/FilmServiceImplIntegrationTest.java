package cz.cvut.fit.tiuridar.tjv.service;

import cz.cvut.fit.tiuridar.tjv.domain.Film;
import cz.cvut.fit.tiuridar.tjv.domain.StreamingService;
import cz.cvut.fit.tiuridar.tjv.repository.FilmRepository;
import cz.cvut.fit.tiuridar.tjv.repository.StreamingServiceRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;

@SpringBootTest
public class FilmServiceImplIntegrationTest {
    @Autowired
    private FilmServiceImpl filmService;
    @Autowired
    private StreamingServiceRepository streamingServiceRepository;
    @Autowired
    private FilmRepository filmRepository;

    @Test
    @Transactional
    void addToACorrectStreamingService() {
        filmRepository.deleteAll();
        streamingServiceRepository.deleteAll();

        Long id = 1L;

        Film film = new Film();
        film.setId(id);
        film.setStreamingServicesIsOn(new HashSet<>());

        StreamingService streamingService = new StreamingService();
        streamingService.setId(id);
        streamingService.setHasFilms(new HashSet<>());

        filmRepository.save(film);
        streamingServiceRepository.save(streamingService);

        filmService.addToANewStreamingService(film.getId(), streamingService.getId());
        Film persistedFilm = filmRepository.findById(id).orElse(null);
        StreamingService persistedStreamingService = streamingServiceRepository.findById(id).orElse(null);

        Assertions.assertNotNull(persistedFilm);
        Assertions.assertNotNull(persistedStreamingService);

        Assertions.assertTrue(persistedFilm.getStreamingServicesIsOn().contains(persistedStreamingService));
        Assertions.assertTrue(persistedStreamingService.getHasFilms().contains(persistedFilm));
    }
}
