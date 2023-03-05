package edu.eci.proyect.service.user;


import edu.eci.proyect.model.user.User;

import java.util.List;
import java.util.Optional;

public interface UsersService {

    User save(User user);

    Optional<User> findById(String id);

    Optional<User> findByEmail(String email);

    List<User> all();

    void deleteById(String id);

    User update(User user, String userId);
}
