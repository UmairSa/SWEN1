package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.repository.UserRepositoryImpl;
import at.technikum.apps.mtcg.util.PasswordHashingUtil;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class UserService {
    private final UserRepositoryImpl userRepository;

    public User registerUser(User user) throws IllegalArgumentException{

        // Prüfen, ob ein Benutzer mit demselben Benutzernamen bereits existiert
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Benutzername bereits vergeben.");
        }

        // Passwort hashen
        String hashedPassword = PasswordHashingUtil.hashPassword(user.getPassword());
        user.setPassword(hashedPassword);

        // Benutzer erstellen
        return userRepository.save(user);
    }

    public User loginUser(String username, String password) {

        // Benutzer anhand des Benutzernamens finden
        User user = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("Benutzername nicht gefunden."));

        // Passwort überprüfen
        if (!PasswordHashingUtil.checkPassword(password, user.getPassword())) {
            throw new IllegalArgumentException("Falsches Passwort.");
        }

        // Authentifizierung erfolgreich
        return user;
    }

    public User updateUser(User user) {

        // Prüfen, ob der Benutzer existiert
        User existingUser = userRepository.findById(user.getId()).orElseThrow(() -> new IllegalArgumentException("Benutzer nicht gefunden."));

        // Daten aktualisieren, z.B. Passwort, falls geändert
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            String hashedPassword = PasswordHashingUtil.hashPassword(user.getPassword());
            existingUser.setPassword(hashedPassword);
        }
        // Weitere Felder entsprechend aktualisieren
        return userRepository.update(existingUser);
    }
}
