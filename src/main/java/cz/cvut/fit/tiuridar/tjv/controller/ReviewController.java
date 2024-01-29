package cz.cvut.fit.tiuridar.tjv.controller;

import cz.cvut.fit.tiuridar.tjv.controller.dto.ReviewDto;
import cz.cvut.fit.tiuridar.tjv.domain.Review;
import cz.cvut.fit.tiuridar.tjv.service.ReviewService;
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
@RequestMapping("/review")
@CrossOrigin(origins = "http://localhost:3000")
public class ReviewController {
    private final ReviewService reviewService;
    private final ModelMapper modelMapper;

    public ReviewController(ReviewService reviewService, ModelMapper modelMapper) {
        this.reviewService = reviewService;
        this.modelMapper = modelMapper;
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletes review",
            description = "Deletes review with a given ID")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses({
            @ApiResponse(responseCode = "404", description = "invalid ID", content = @Content)
    })
    void deleteById(@PathVariable Long id) {
        try {
            reviewService.deleteById(id);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    @Operation(summary = "Creates review",
            description = "Creates a new review")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "409", description = "CONFLICT", content = @Content)
    })
    public ReviewDto create(@RequestBody ReviewDto data) {
        try {
            return convertToDto(reviewService.create(convertToEntity(data)));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Update review",
            description = "Updates review with given ID")
    @ApiResponses({
            @ApiResponse(responseCode = "409", description = "CONFLICT", content = @Content),
            @ApiResponse(responseCode = "404", description = "NOT FOUND")
    })
    public void update(@PathVariable Long id, @RequestBody ReviewDto dataDto){
        try {
            reviewService.update(id, convertToEntity(dataDto));
        }
        catch(Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    @Operation(summary = "Get all reviews",
            description = "Gets all reviews")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK")
    })
    public Iterable<ReviewDto> readAll() {
        return convertManyToDTO(reviewService.readAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get review",
            description = "Gets review with a given ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "invalid ID", content = @Content)
    })
    public ReviewDto readByID(@PathVariable Long id) {
        var res = reviewService.readById(id);
        if (res.isPresent())
            return convertToDto(res.get());
        else
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/user/{username}")
    @Operation(summary = "Gets all reviews from a user",
            description = "Gets all reviews from a user with a given USERNAME")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "invalid ID", content = @Content)
    })
    public Iterable<ReviewDto> readAllByUser(@PathVariable String username){
        return convertManyToDTO(reviewService.findAllByAuthor(username));
    }

    @GetMapping("/film/{id}")
    @Operation(summary = "Gets all reviews to a film",
            description = "Gets all reviews to a film with a given ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "invalid ID", content = @Content)
    })
    public Iterable<ReviewDto> readAllByFilm(@PathVariable Long id){
        return convertManyToDTO(reviewService.findAllByFilm(id));
    }

    private Review convertToEntity(ReviewDto reviewDto){
        return modelMapper.map(reviewDto, Review.class);
    }

    private ReviewDto convertToDto(Review review){
        return modelMapper.map(review, ReviewDto.class);
    }

    private List<ReviewDto> convertManyToDTO(Iterable<Review> reviews) {
        return StreamSupport.stream(reviews.spliterator(), false).toList()
                .stream()
                .map(this::convertToDto)
                .collect(toList());
    }

}
