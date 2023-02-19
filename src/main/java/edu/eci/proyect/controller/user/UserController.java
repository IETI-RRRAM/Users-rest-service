package edu.eci.proyect.controller.user;

import edu.eci.proyect.exception.UserNotFoundException;
import edu.eci.proyect.model.user.User;
import edu.eci.proyect.model.user.UserDto;
import edu.eci.proyect.service.user.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/vi/users/")
public class UserController {

    @Autowired
    private final UsersService usersService;

    public UserController(@Autowired UsersService usersService) {
        this.usersService = usersService;
    }

    //Create
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody UserDto userDTO) {
        User userCreated = usersService.save(new User(userDTO));
        URI createdUserUri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(userCreated.getId())
                .toUri();;
        return ResponseEntity.created(createdUserUri).body(userCreated);
    }

    //Read
    @GetMapping("{id}")
    public ResponseEntity<User> findById(@PathVariable("id") String id) {
        User userFound = usersService.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        return ResponseEntity.ok(userFound);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = usersService.all();
        return ResponseEntity.ok(users);
    }

    //Update
    @PutMapping("{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") String id, @RequestBody UserDto userDTO) {
        User userToUpdate = usersService.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        User userUpdated = usersService.update(new User(userDTO), id);
        usersService.save(userToUpdate);
        return ResponseEntity.ok(userUpdated);
    }

    //Delete
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") String id) {
        usersService.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        usersService.deleteById(id);
        return ResponseEntity.ok().build();
    }



}
