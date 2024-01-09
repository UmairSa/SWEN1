package at.technikum.apps.mtcg;

import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.repository.UserRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class UserRepoTest {
    //User for tests
    String userName = "UnitTest";

    @Test
    void save_CreateUser() {
        UserRepository userRepository = new UserRepository();
        User newUser = new User();
        newUser.setUsername(userName);
        newUser.setPassword("testPasswd");
        newUser.setCoins(100);
        newUser.setElo(100);

        User savedUser = userRepository.save(newUser);
        assertNotNull(savedUser, "Der Benutzer sollte nicht null & muss unique sein.");
        System.out.println("User created: " + savedUser);
    }

    @Test
    void findByUsername_AndPrintUser() {
        UserRepository userRepository = new UserRepository();
        Optional<User> foundUser = userRepository.findByUsername(userName);
        assertTrue(foundUser.isPresent(), "Ein Benutzer mit der angegebenen Benutzernamen sollte gefunden werden.");
        foundUser.ifPresent(System.out::println);
    }

    @Test
    void update_AndPrintUser() {
        UserRepository userRepository = new UserRepository();

        Optional<User> optionalUser = userRepository.findByUsername(userName);
        assertTrue(optionalUser.isPresent(), "Benutzer sollte existieren");
        User userToUpdate = optionalUser.get();
        System.out.println("ALTE USERDATEN: " + userToUpdate);

        //Daten ändern
        userToUpdate.setElo(101);

        User updatedUser = userRepository.update(userToUpdate);

        assertNotNull(updatedUser, "Der aktualisierte Benutzer sollte nicht null sein.");
        //assertEquals("updatedUsername", updatedUser.getUsername(), "Der User-Name sollte aktualisiert worden sein.");
        assertEquals(101, updatedUser.getElo(), "Der ELO-Wert sollte aktualisiert worden sein.");

        System.out.println("NEUE USERDATEN: " + updatedUser);
    }

    @Test
    void delete_ShouldRemoveUser() {
        UserRepository userRepository = new UserRepository();
        assertTrue(userRepository.findByUsername(userName).isPresent(), "Der Benutzer sollte vor dem Löschen existieren.");
        userRepository.deleteByUsername(userName);
        assertFalse(userRepository.findByUsername(userName).isPresent(), "Der Benutzer sollte nach dem Löschen nicht mehr vorhanden sein.");
    }
}
/*
    @Test
    void findAll_AndPrintUsers() {
        UserRepository userRepository = new UserRepository();
        userRepository.findAll().forEach(System.out::println);
    }
*/