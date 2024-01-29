package cz.cvut.fit.tiuridar.tjv.service;

import cz.cvut.fit.tiuridar.tjv.domain.Film;
import cz.cvut.fit.tiuridar.tjv.domain.StreamingService;
import cz.cvut.fit.tiuridar.tjv.repository.FilmRepository;
import cz.cvut.fit.tiuridar.tjv.repository.StreamingServiceRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

@SpringBootTest
public class FilmServiceImplTest {
    @Autowired
    FilmService filmService;
    @MockBean
    FilmRepository filmRepository;
    @MockBean
    StreamingServiceRepository streamingServiceRepository;

    @Test
    void addToANonExistingStreamingService(){
        Long id = (long) 1;
        Film film = new Film();
        film.setId(id);

        Mockito.when(streamingServiceRepository.findById(id)).thenReturn(Optional.empty());
        Mockito.when(filmRepository.findById(id)).thenReturn(Optional.of(film));
        Assertions.assertThrows(IllegalArgumentException.class, () -> filmService.addToANewStreamingService(id, id));
    }

    @Test
    void addToACorrectStreamingService(){
        Long id = (long) 1;
        Film film = new Film();
        film.setId(id);
        StreamingService streamingService = new StreamingService();
        streamingService.setId(id);

        Mockito.when(streamingServiceRepository.findById(id)).thenReturn(Optional.of(streamingService));
        Mockito.when(filmRepository.findById(id)).thenReturn(Optional.of(film));

        filmService.addToANewStreamingService(id, id);
        Assertions.assertTrue(film.getStreamingServicesIsOn().contains(streamingService));
        Assertions.assertTrue(streamingService.getHasFilms().contains(film));
        Mockito.verify(filmRepository, Mockito.atLeastOnce()).save(film);
        Mockito.verify(streamingServiceRepository, Mockito.atLeastOnce()).save(streamingService);
    }
}
