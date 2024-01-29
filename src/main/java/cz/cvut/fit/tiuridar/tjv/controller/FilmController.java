package cz.cvut.fit.tiuridar.tjv.controller;

import cz.cvut.fit.tiuridar.tjv.controller.dto.FilmDto;
import cz.cvut.fit.tiuridar.tjv.controller.dto.StreamingServiceDto;
import cz.cvut.fit.tiuridar.tjv.domain.Film;
import cz.cvut.fit.tiuridar.tjv.domain.StreamingService;
import cz.cvut.fit.tiuridar.tjv.service.FilmService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.modelmapper.ModelMapper;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping(value = "/film")
@CrossOrigin(origins = "http://localhost:3000")
public class FilmController {
    private final FilmService filmService;
    private final ModelMapper modelMapper;

    public FilmController(FilmService filmService, ModelMapper modelMapper) {
        this.filmService = filmService;
        this.modelMapper = modelMapper;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete film",
            description = "Deletes film based on an ID")
    @ApiResponses({
            @ApiResponse(responseCode = "404", description = "invalid ID", content = @Content)
    })
    void deleteById(@PathVariable Long id) {
        try {
            filmService.deleteById(id);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    @Operation(summary = "Create film",
            description = "Creates a new film")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "409", description = "CONFLICT", content = @Content)
    })
    public FilmDto create(@RequestBody FilmDto data) {
        try {
            return convertToDto(filmService.create(convertToEntity(data)));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Update film",
            description = "Updates film based on an ID")
    @ApiResponses({
            @ApiResponse(responseCode = "409", description = "CONFLICT", content = @Content)
    })
    public void update(@PathVariable Long id, @RequestBody FilmDto dataDto) {
        try {
            filmService.update(id, convertToEntity(dataDto));
        }
        catch(Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    @Operation(summary = "Get films",
            description = "Gets list of all films")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK")
    })
    public List<FilmDto> readAll() {
        return convertManyToDTO(filmService.readAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get film with ID",
            description = "Returns a film with a given ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "invalid ID", content = @Content)
    })
    public FilmDto readByID(@PathVariable Long id) {
        var res = filmService.readById(id);
        if (res.isPresent())
            return convertToDto(res.get());
        else
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "FILM NOT FOUND");
    }

    @PutMapping("/{film_id}/streaming_service/{streaming_id}")
    @Operation(summary = "Add film to a streaming service",
            description = "Adds film with a given FILM_ID to a streaming service with a given STREAMING_ID")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses({
            @ApiResponse(responseCode = "404", description = "NOT FOUND", content = @Content)
    })
    public void addToANewStreamingService(@PathVariable Long film_id, @PathVariable Long streaming_id) {
        try {
            filmService.addToANewStreamingService(film_id, streaming_id);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "FILM AND/OR STREAMING SERVICE NOT FOUND");
        }
    }

    @GetMapping("/{film_id}/streaming_service")
    @Operation(summary = "Returns streaming services",
            description = "Returns streaming services on which film with a given FILM_ID is")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND", content = @Content)
    })
    public Iterable<StreamingServiceDto> readAllStreamingServices(@PathVariable Long film_id) {
        var streamingServiceFind = filmService.readById(film_id);
        if (streamingServiceFind.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return convertManyToDTOStreamingService(streamingServiceFind.get().getStreamingServicesIsOn());
    }

    private Film convertToEntity(FilmDto filmDto) {
        return modelMapper.map(filmDto, Film.class);
    }

    private FilmDto convertToDto(Film film) {
        return modelMapper.map(film, FilmDto.class);
    }

    private List<FilmDto> convertManyToDTO(Iterable<Film> films) {
        return StreamSupport.stream(films.spliterator(), false).toList()
                .stream()
                .map(this::convertToDto)
                .collect(toList());
    }

    private List<StreamingServiceDto> convertManyToDTOStreamingService(Iterable<StreamingService> streamingServices) {
        return StreamSupport.stream(streamingServices.spliterator(), false).toList()
                .stream()
                .map(this::convertToDtoStreamingService)
                .collect(toList());
    }

    private StreamingServiceDto convertToDtoStreamingService(StreamingService streamingService) {
        return modelMapper.map(streamingService, StreamingServiceDto.class);
    }

}

