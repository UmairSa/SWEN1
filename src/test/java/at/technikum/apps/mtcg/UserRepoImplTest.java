package at.technikum.apps.mtcg;

import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.repository.UserRepositoryImpl;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class UserRepoImplTest {
    @Test
    void save_CreateUser() {
        UserRepositoryImpl userRepository = new UserRepositoryImpl();
        User newUser = new User();
        newUser.setUsername("testUser6");
        newUser.setPassword("testPasswd");
        newUser.setCoins(100);
        newUser.setElo(100);

        User savedUser = userRepository.save(newUser);
        assertNotNull(savedUser, "Der Benutzer sollte nicht null & muss unique sein.");
        System.out.println("USER ERSTELLT: " + savedUser);
    }

    @Test
    void findAll_AndPrintUsers() {
        UserRepositoryImpl userRepository = new UserRepositoryImpl();
        userRepository.findAll().forEach(System.out::println);
    }

    @Test
    void findById_AndPrintUser() {
        UserRepositoryImpl userRepository = new UserRepositoryImpl();
        int userId = 1;

        Optional<User> foundUser = userRepository.findById(userId);
        assertTrue(foundUser.isPresent(), "Ein Benutzer mit der angegebenen ID sollte gefunden werden.");
        foundUser.ifPresent(System.out::println);
    }

    @Test
    void update_AndPrintUser() {
        UserRepositoryImpl userRepository = new UserRepositoryImpl();

        //User mit ID identifizieren
        int userId = 1;
        Optional<User> optionalUser = userRepository.findById(userId);
        assertTrue(optionalUser.isPresent(), "Benutzer sollte existieren");

        User userToUpdate = optionalUser.get();

        //alten user ausgeben
        System.out.println("ALTE USERDATEN: " + userToUpdate);

        //Daten ändern
        userToUpdate.setUsername("updatedUsername");
        userToUpdate.setElo(1000);

        //Aktualisieren
        User updatedUser = userRepository.update(userToUpdate);

        assertNotNull(updatedUser, "Der aktualisierte Benutzer sollte nicht null sein.");
        assertEquals("updatedUsername", updatedUser.getUsername(), "Der User-Name sollte aktualisiert worden sein.");
        assertEquals(1000, updatedUser.getElo(), "Der ELO-Wert sollte aktualisiert worden sein.");

        //Print neuen User
        System.out.println("NEUE USERDATEN: " + updatedUser);
    }

    @Test
    void delete_ShouldRemoveUser() {
        UserRepositoryImpl userRepository = new UserRepositoryImpl();

        int userIdToDelete = 1;

        // Überprüfen, ob Benutzer vor Löschen existiert
        assertTrue(userRepository.findById(userIdToDelete).isPresent(), "Der Benutzer sollte vor dem Löschen existieren.");

        // Löschen des Benutzers
        userRepository.deleteById(userIdToDelete);

        // Überprüfen, ob Benutzer nach Löschen nicht mehr existiert
        assertFalse(userRepository.findById(userIdToDelete).isPresent(), "Der Benutzer sollte nach dem Löschen nicht mehr vorhanden sein.");

    }
}