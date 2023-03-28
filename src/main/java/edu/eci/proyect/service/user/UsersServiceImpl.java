package edu.eci.proyect.service.user;

import edu.eci.proyect.exception.IllegalArgumentOfUserException;
import edu.eci.proyect.exception.InvalidUserProvidedException;
import edu.eci.proyect.exception.UserAlreadyExistException;
import edu.eci.proyect.model.user.User;
import edu.eci.proyect.model.user.UserDto;
import edu.eci.proyect.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UsersServiceImpl implements UsersService {

    @Autowired
    UserRepository userRepository;

    @Override
    public User save(User user) throws UserAlreadyExistException {
        if(user == null || user.getEmail() == null || user.getName() == null){
            throw new InvalidUserProvidedException();
        }
        Optional<User> userStored = userRepository.findByEmail(user.getEmail());
        userStored.ifPresent(userFounded -> {
            throw new UserAlreadyExistException(userFounded.getEmail());
        });
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findById(String id) {
        if(id == null || id == ""){
            throw new IllegalArgumentOfUserException();
        }
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        if(email == null || email == ""){
            throw new IllegalArgumentOfUserException();
        }
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> all() {
        return userRepository.findAll();
    }

    @Override
    public boolean deleteById(String id) {
        if(findById(id).isPresent()){
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public User update(User user, String userId) {
        return userRepository.update(user, userId);
    }
}
