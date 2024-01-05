package at.technikum.apps.mtcg;

import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.repository.UserRepositoryImpl;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserRepoImplTest {
    @Test
    void save_ShouldCreateUser() {
        UserRepositoryImpl userRepository = new UserRepositoryImpl();
        User newUser = new User();
        newUser.setUsername("testUser3");
        newUser.setPassword("testPasswd");
        newUser.setCoins(999);
        newUser.setElo(100);

        User savedUser = userRepository.save(newUser);
        assertNotNull(savedUser, "Der gespeicherte Benutzer sollte nicht null sein.");
    }

    @Test
    void findAll_AndPrintUsers() {
        UserRepositoryImpl userRepository = new UserRepositoryImpl();
        userRepository.findAll().forEach(System.out::println);
    }
}