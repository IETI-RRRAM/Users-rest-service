package edu.eci.proyect.service.user;

import edu.eci.proyect.exception.IllegalArgumentOfUserException;
import edu.eci.proyect.exception.InvalidUserProvidedException;
import edu.eci.proyect.exception.UserAlreadyExistException;
import edu.eci.proyect.model.user.User;
import edu.eci.proyect.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
class UsersServiceImplTest {

    @MockBean
    private UserRepository userRepository;
    @Autowired
    private UsersService userService;


    /**
     * Test the save method that allows to persist a user
     * Case: The user is saved correctly
     */
    @Test
    void saveShouldStoreUser() {
        User userToStore = new User("1","user", "test",
                "userTest@mail.com", "somePassword");
        Mockito.when(userRepository.save(userToStore)).thenReturn(userToStore);
        User userStored = userService.save(userToStore);
        Assertions.assertEquals(userToStore, userStored);
    }

    /**
     * Test the save method that allows to persist a user
     * Case: The user is not saved because he already exists
     */
    @Test
    void saveShouldNotStoreUser() {
        User userToStore = new User("1","user", "test",
                "userTest@mail.com", "somePassword");
        User userToStoreCopy = new User("1","user", "test",
                "userTest@mail.com", "somePassword");
        Mockito.when(userRepository.save(userToStore)).thenReturn(userToStore);
        User userStored = userService.save(userToStore);
        Mockito.when(userRepository.findByEmail("userTest@mail.com")).thenReturn(Optional.of(userToStore));
        Assertions.assertThrows(UserAlreadyExistException.class, () -> userService.save(userToStoreCopy));
        Assertions.assertEquals(userToStore, userStored);
    }

    /**
     * Test the save method that allows to persist a user
     * Case: The user is null and for this is not saved
     */
    @Test
    void saveShouldNotStoreUserNull() {
        Assertions.assertThrows(InvalidUserProvidedException.class, () -> userService.save(null));
    }

    /**
     * Test the save method that allows to persist a user
     * Case: The user is not saved because all of his attributes are null
     */
    @Test
    void saveShouldNotStoreUserWithNullData() {
        User userToStore = new User(null,null, null,
                null, "");
        Assertions.assertThrows(InvalidUserProvidedException.class, () -> userService.save(userToStore));
    }

    /**
     * Test the save method that allows to persist a user
     * Case: The user is not saved
     */
    @Test
    void saveShouldNotStoreUserWithExistingEmail() {
        User userToStore = new User("1","user", "test",
                "userTest@mail.com", "somePassword");
        User userToStoreCopy = new User("2","user2", "test2",
                "userTest@mail.com", "somePassword");
        Mockito.when(userRepository.save(userToStore)).thenReturn(userToStore);
        User userStored = userService.save(userToStore);
        Mockito.when(userRepository.findByEmail("userTest@mail.com")).thenReturn(Optional.of(userToStore));
        Assertions.assertThrows(UserAlreadyExistException.class, () -> userService.save(userToStoreCopy));
    }



    /**
     * Test the findById method that allows to find an existing user by his id
     * Case: The user is founded
     */
    @Test
    public void findByIdShouldReturnsExistingUser() {
        User user = new User();
        user.setId("1234");
        Mockito.when(userRepository.findById("1234")).thenReturn(Optional.of(user));
        Optional<User> userFounded = userService.findById("1234");
        Assertions.assertEquals(user, userFounded.get());
    }

    /**
     * Test the findById method that allows to find an existing user by his id
     * Case: The user is not founded
     */
    @Test
    public void findByIdShouldReturnsEmpty() {
        Mockito.when(userRepository.findById("5678")).thenReturn(Optional.empty());
        Optional<User> result = userService.findById("5678");
        Assertions.assertTrue(result.isEmpty());
    }

    /**
     * Test the findById method that allows to find an existing user by his id
     * Case: The params are incorrect
     */
    @Test
    public void findByIdShouldThrowsExceptionForEmptyId() {
        Assertions.assertThrows(IllegalArgumentOfUserException.class, () -> {
            userService.findById("");
        });
    }

    /**
     * Test the findById method that allows to find an existing user by his id
     * Case: The params are null
     */
    @Test
    public void findByIdShouldThrowsExceptionForNullId() {
        Assertions.assertThrows(IllegalArgumentOfUserException.class, () -> {
            userService.findById(null);
        });
    }

    /**
     * Test the findById method that allows to find an existing user by his id
     * Case: The user is founded even with special id
     */
    @Test
    public void findByIdShouldReturnsUserWithSpecialCharacterId() {
        User user = new User();
        user.setId("1#2$3%4&");
        Mockito.when(userRepository.findById("1#2$3%4&")).thenReturn(Optional.of(user));
        Optional<User> result = userService.findById("1#2$3%4&");
        Assertions.assertEquals(user, result.get());
    }



    /**
     * Test the findByEmail method that allows to find an existing user by his email
     * Case: The user is founded
     */
    @Test
    public void findByEmailShouldReturnsUser() {
        User user = new User();
        user.setEmail("test@example.com");
        Mockito.when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        Optional<User> result = userService.findByEmail("test@example.com");
        Assertions.assertEquals(user, result.get());
    }

    /**
     * Test the findByEmail method that allows to find an existing user by his email
     * Case: The user is not founded
     */
    @Test
    public void findByEmailShouldReturnsEmpty() {
        Mockito.when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        Optional<User> result = userService.findByEmail("test@example.com");
        Assertions.assertTrue(result.isEmpty());
    }

    /**
     * Test the findByEmail method that allows to find an existing user by his email
     * Case: The params are incorrect
     */
    @Test
    public void findByEmailShouldThrowsExceptionForEmptyEmail() {
        Assertions.assertThrows(IllegalArgumentOfUserException.class, () -> {
            userService.findByEmail("");
        });
    }

    /**
     * Test the findByEmail method that allows to find an existing user by his email
     * Case: The params are null
     */
    @Test
    public void findByEmailShouldThrowsExceptionForNullEmail() {
        Assertions.assertThrows(IllegalArgumentOfUserException.class, () -> {
            userService.findByEmail(null);
        });
    }

    /**
     * Test the findById method that allows to find an existing user by his id
     * Case: The user is founded even with special id
     */
    @Test
    public void findByEmailShouldReturnsUserWithSpecialCharacterEmail() {
        User user = new User();
        user.setEmail("1#2$3%4&@example.com");
        Mockito.when(userRepository.findByEmail("1#2$3%4&@example.com")).thenReturn(Optional.of(user));
        Assertions.assertEquals(Optional.of(user), userService.findByEmail("1#2$3%4&@example.com"));
    }



    /**
     * Test the all method that allows to get all the existing users
     * Case: there are no users registered. the answer is empty
     */
    @Test
    void allShouldReturnEmptyList() {
        Mockito.when(userRepository.findAll()).thenReturn(new ArrayList<User>());
        List<User> users = userService.all();
        Assertions.assertEquals(users, new ArrayList<User>());
    }
    /**
     * Test the all method that allows to get all the existing users
     * Case: there are users registered. the answer is all the users in a list
     */
    @Test
    void allShouldReturnUsersList() {
        User user1 = new User();
        User user2 = new User();
        User user3 = new User();
        user1.setName("Juan");
        user2.setName("Pablo");
        user2.setName("Daniel");
        List<User> users = new ArrayList<User>();
        users.add(user1);
        users.add(user2);
        users.add(user3);
        Mockito.when(userRepository.findAll()).thenReturn(users);
        List<User> usersList = userService.all();
        Assertions.assertEquals(usersList, users);
    }

    /**
     * Test the deleteById method that allows to delete an existing users
     * Case: Delete successfully an existing user
     */
    @Test
    void deleteByIdShouldDelete() {
        User user = new User();
        user.setId("1");
        Mockito.when(userRepository.findById("1")).thenReturn(Optional.of(user));
        Assertions.assertTrue(userService.deleteById("1"));

    }

    /**
     * Test the deleteById method that allows to delete an existing users
     * Case: Delete is not completed because the user doesn't exist
     */
    @Test
    void deleteById() {
        Mockito.when(userRepository.findById("1")).thenReturn(Optional.empty());
        Assertions.assertFalse(userService.deleteById("1"));
    }



    /**
     * Test the update method that allows to update an existing users
     * Case: Update is  completed successfully
     */
    @Test
    void update() {
        User userForUpdate = new User();
        userForUpdate.setId("1");
        userForUpdate.setName("James");
        Mockito.when(userRepository.update(userForUpdate,"1")).thenReturn(userForUpdate);
        User userUpdated = userService.update(userForUpdate, "1");
        Assertions.assertEquals(userUpdated, userForUpdate);
    }
}