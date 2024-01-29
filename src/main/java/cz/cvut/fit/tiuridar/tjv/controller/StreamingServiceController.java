package cz.cvut.fit.tiuridar.tjv.controller;

import cz.cvut.fit.tiuridar.tjv.controller.dto.FilmDto;
import cz.cvut.fit.tiuridar.tjv.controller.dto.StreamingServiceDto;
import cz.cvut.fit.tiuridar.tjv.domain.Film;
import cz.cvut.fit.tiuridar.tjv.domain.StreamingService;
import cz.cvut.fit.tiuridar.tjv.service.StreamingServiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/streaming_service")
@CrossOrigin(origins = "http://localhost:3000")
public class StreamingServiceController {
    private final StreamingServiceService streamingService;
    private final ModelMapper modelMapper;

    public StreamingServiceController(StreamingServiceService streamingService, ModelMapper modelMapper) {
        this.streamingService = streamingService;
        this.modelMapper = modelMapper;
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletes a streaming service",
            description = "Deletes a streaming service with a given ID")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses({
            @ApiResponse(responseCode = "404", description = "invalid ID", content = @Content)
    })
    void deleteById(@PathVariable Long id) {
        try {
            streamingService.deleteById(id);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    @Operation(summary = "Create a streaming service",
            description = "Creates a new streaming service")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "409", description = "CONFLICT", content = @Content)
    })
    public StreamingServiceDto create(@RequestBody StreamingServiceDto data) {
        try {
            return convertToDto(streamingService.create(convertToEntity(data)));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a streaming service",
            description = "Updates a streaming service with a given ID")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses({
            @ApiResponse(responseCode = "409", description = "CONFLICT", content = @Content)
    })
    public void update(@PathVariable Long id, @RequestBody StreamingServiceDto dataDto) {
        try {
            streamingService.update(id, convertToEntity(dataDto));
        }
        catch(Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{streaming_id}/film/{film_id}/")
    @Operation(summary = "Add a new film to a streaming service",
            description = "Adds a new film with a FILM_ID to a streaming service with a given STREAMING_ID")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses({
            @ApiResponse(responseCode = "404", description = "NOT FOUND", content = @Content)
    })
    public void addToANewStreamingService(@PathVariable Long film_id, @PathVariable Long streaming_id) {
        try {
            streamingService.addToANewStreamingService(film_id, streaming_id);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{streaming_id}/film")
    @Operation(summary = "Get films that are on a streaming service",
            description = "Gets films that are on a streaming service with a given ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK")
    })
    public Iterable<FilmDto> readAllFilms(@PathVariable Long streaming_id) {
        var streamingServiceFind = streamingService.readById(streaming_id);
        if (streamingServiceFind.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return convertManyToDTOFilm(streamingServiceFind.get().getHasFilms());
    }

    @GetMapping
    @Operation(summary = "Get all streaming services",
            description = "Gets all streaming services")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK")
    })
    public Iterable<StreamingServiceDto> readAll() {
        return convertManyToDTO(streamingService.readAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get streaming services",
            description = "Gets streaming service with a given ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "invalid ID", content = @Content)
    })
    public StreamingServiceDto readByID(@PathVariable Long id) {
        var res = streamingService.readById(id);
        if (res.isPresent())
            return convertToDto(res.get());
        else
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/best")
    @Operation(summary = "Get a streaming services sorted by rating of their film",
            description = "Gets a streaming services sorted by rating of their film")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK")
    })
    public Iterable<StreamingServiceDto> bestStreaming() {
        return convertManyToDTO(streamingService.returnBest());
    }

    @DeleteMapping("/delete/{streaming_id}/film/{film_id}")
    @Operation(summary = "Deletes a film from a streaming service",
            description = "Deletes a film with a given FILM_ID from a streaming service with a given STREAMING_ID")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses({
            @ApiResponse(responseCode = "404", description = "invalid ID", content = @Content)
    })
    void deleteStreamingService(@PathVariable Long film_id, @PathVariable Long streaming_id) {
        streamingService.deleteFromAStreamingService(film_id, streaming_id);
    }

    private StreamingService convertToEntity(StreamingServiceDto streamingServiceDto) {
        return modelMapper.map(streamingServiceDto, StreamingService.class);
    }

    private StreamingServiceDto convertToDto(StreamingService streamingService) {
        return modelMapper.map(streamingService, StreamingServiceDto.class);
    }

    private FilmDto convertToDto(Film film) {
        return modelMapper.map(film, FilmDto.class);
    }

    private List<StreamingServiceDto> convertManyToDTO(Iterable<StreamingService> streamingServices) {
        return StreamSupport.stream(streamingServices.spliterator(), false).toList()
                .stream()
                .map(this::convertToDto)
                .collect(toList());
    }

    private List<FilmDto> convertManyToDTOFilm(Iterable<Film> films) {
        return StreamSupport.stream(films.spliterator(), false).toList()
                .stream()
                .map(this::convertToDto)
                .collect(toList());
    }
}
