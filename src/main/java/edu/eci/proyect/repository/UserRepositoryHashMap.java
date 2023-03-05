package edu.eci.proyect.repository;

import edu.eci.proyect.model.user.User;
import org.springframework.stereotype.Service;

import java.util.*;

public class UserRepositoryHashMap implements UserRepository{
    Map<String, User> db = new HashMap<>();
    @Override
    public User save(User user) {
        return db.put(user.getId(),user);
    }

    @Override
    public Optional<User> findById(String id) {
        return Optional.ofNullable(db.get(id));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return Optional.empty();
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<User>(db.values());
    }

    @Override
    public User update(User user, String id) {
        db.remove(id);
        db.put(user.getId(), user);
        return db.get(id);
    }

    @Override
    public void deleteById(String id) {
        db.remove(id);
    }
}
