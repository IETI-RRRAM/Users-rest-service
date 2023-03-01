package edu.eci.proyect.repository;

import edu.eci.proyect.exception.UserNotFoundException;
import edu.eci.proyect.model.user.User;
import edu.eci.proyect.model.user.UserDto;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.events.Event;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepositoryArrayList implements UserRepository{

    ArrayList<User> db = new ArrayList<>();  
    @Override
    public User save(User user) {
        db.add(user);
        return user;
    }

    @Override
    public Optional<User> findById(String id) {
        for (User user: db) {
            if(user.getId().equals(id)){
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<User> findAll() {
        return db;
    }

    @Override
    public User update(User user, String id) {
        for (int i = 0; i < db.size(); i++) {
            User u = db.get(i);
            if (u.getId().equals(id)) {
                db.set(i, user);
                return user;
            }
        }
        throw new UserNotFoundException("User not found");
    }

    @Override
    public void deleteById(String id) {
        db.removeIf(user -> user.getId().equals(id));
    }
}
