package edu.eci.proyect.repository;

import edu.eci.proyect.model.user.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User save(User user);
    Optional<User> findById(String id);

    List<User> findAll();

    User update(User user, String id);

    void deleteById(String id);




}
