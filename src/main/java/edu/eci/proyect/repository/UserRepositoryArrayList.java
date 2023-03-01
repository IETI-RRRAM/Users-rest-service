package edu.eci.proyect.repository;

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
        for (User userToSearch: db) {
            if(user.getId().equals(userToSearch.getId())){
                return userToSearch;
            }
        }
        return null;
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
        for (User userToSearch: db) {
            if(user.getId().equals(userToSearch.getId())){
                userToSearch.update(new UserDto(user.getId(), user.getName(), user.getLastName()));
                return userToSearch;
            }
        }
        return null;
    }

    @Override
    public void deleteById(String id) {
        for (User user : db) {
            if (user.getId().equals(id)) {
                db.remove(user);
            }
        }
    }
}
