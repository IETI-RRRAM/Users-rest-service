package edu.eci.proyect.service.user;

import edu.eci.proyect.model.user.User;
import edu.eci.proyect.model.user.UserDto;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UsersServiceMap implements UsersService {

    Map<String, User> userDB = new HashMap<>();

    @Override
    public User save(User user) {
        if(findById(user.getId()).isEmpty()){
            user.setId(String.valueOf(userDB.size()));
        }
        userDB.put(user.getId(), user);
        return userDB.get(user.getId());
    }

    @Override
    public Optional<User> findById(String id) {
        return Optional.ofNullable(userDB.get(id));
    }

    @Override
    public List<User> all() {
        return new ArrayList<>(userDB.values());
    }

    @Override
    public void deleteById(String id) {
        userDB.remove(id);
    }

    @Override
    public User update(User user, String userId) {
        User userToUpdate = userDB.get(userId);
        userToUpdate.update(new UserDto(user.getName(), user.getLastName(), user.getEmail()));
        return userToUpdate;
    }
}
