package edu.eci.proyect.controller.user;

import edu.eci.proyect.exception.UserNotFoundException;
import edu.eci.proyect.model.user.User;
import edu.eci.proyect.model.user.UserDto;
import edu.eci.proyect.service.user.UsersService;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/v1/users")
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
    @GetMapping("/id/{id}")
    public ResponseEntity<User> findById(@PathVariable("id") String id) {
        User userFound = usersService.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        return ResponseEntity.ok(userFound);
    }

    @GetMapping("/email/{email}")
    public  ResponseEntity<User> findByEmail(@PathVariable("email") String email){
        User userFound = usersService.findByEmail(email).orElseThrow(()-> new UserNotFoundException(email));
        return ResponseEntity.ok(userFound);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = usersService.all();
        return ResponseEntity.ok(users);
    }

    //Update
    @RolesAllowed("ADMIN")
    @PutMapping("{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") String id, @RequestBody UserDto userDTO) {
        User userUpdated = usersService.update(new User(userDTO), id);
        return ResponseEntity.ok(userUpdated);
    }

    //Delete
    @RolesAllowed("ADMIN")
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") String id) {
        usersService.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        usersService.deleteById(id);
        return ResponseEntity.ok().build();
    }



}
