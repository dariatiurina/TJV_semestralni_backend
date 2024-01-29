package cz.cvut.fit.tiuridar.tjv.controller;

import cz.cvut.fit.tiuridar.tjv.controller.dto.UserDto;
import cz.cvut.fit.tiuridar.tjv.domain.User;
import cz.cvut.fit.tiuridar.tjv.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {
    private final UserService userService;
    private final ModelMapper modelMapper;

    public UserController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/reviewsMore/{size}")
    @Operation(summary = "Get users who have written at least _ reviews",
            description = "Gets users who have written at least SIZE reviews")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK")
    })
    Iterable<UserDto> findAllWhoWrittenMore(@PathVariable Integer size){
        return convertManyToDTO(userService.findUsersWhoWrittenMoreThanReviews(size));
    }

    @DeleteMapping("/{username}")
    @Operation(summary = "Delete user",
            description = "Deletes user with a given USERNAME")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses({
            @ApiResponse(responseCode = "404", description = "invalid ID")
    })
    void deleteById(@PathVariable String username) {
        try {
            userService.deleteById(username);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    @Operation(summary = "Create user",
            description = "Creates a new user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "409", description = "CONFLICT", content = @Content)
    })
    public UserDto create(@RequestBody UserDto data) {
        try {
            return convertToDto(userService.create(convertToEntity(data)));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
    }

    @PutMapping("/{username}")
    @Operation(summary = "Update user",
            description = "Updates user with a given USERNAME")
    @ApiResponses({
            @ApiResponse(responseCode = "404", description = "invalid ID", content = @Content)
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable String username, @RequestBody UserDto dataDto) {
        try {
            userService.update(username, convertToEntity(dataDto));
        }
        catch(Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    @Operation(summary = "Get all users",
            description = "Gets all users")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK")
    })
    public Iterable<UserDto> readAll() {
        return convertManyToDTO(userService.readAll());
    }

    @GetMapping("/{username}")
    @Operation(summary = "Get user",
            description = "Gets user with a given USERNAME")
    @ApiResponses({
            @ApiResponse(responseCode = "404", description = "invalid ID", content = @Content)
    })
    public UserDto readByID(@PathVariable String username) {
        var res = userService.readById(username);
        if (res.isPresent())
            return convertToDto(res.get());
        else
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    private User convertToEntity(UserDto userDto) {
        return modelMapper.map(userDto, User.class);
    }

    private UserDto convertToDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }

    private Iterable<UserDto> convertManyToDTO(Iterable<User> users) {
        return StreamSupport.stream(users.spliterator(), false).toList()
                .stream()
                .map(this::convertToDto)
                .collect(toList());
    }

}
