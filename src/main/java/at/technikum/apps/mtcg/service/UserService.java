package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.repository.UserRepository;
import at.technikum.apps.mtcg.repository.UserStats;
import at.technikum.apps.mtcg.util.PasswordHashingUtil;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User registerUser(User user) throws IllegalArgumentException {

        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Benutzername bereits vergeben.");
        }

        String hashedPassword = PasswordHashingUtil.hashPassword(user.getPassword());
        user.setPassword(hashedPassword);
        return userRepository.save(user);
    }

    public User loginUser(String username, String password) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("Benutzername nicht gefunden."));

        if (!PasswordHashingUtil.checkPassword(password, user.getPassword())) {
            throw new IllegalArgumentException("Falsches Passwort.");
        }
        return user;
    }

    public Optional<User> findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void updateUser(User user) {
        // Prüfen, ob der Benutzer existiert
        User existingUser = userRepository.findByUsername(user.getUsername()).orElseThrow(() -> new IllegalArgumentException("Benutzer nicht gefunden."));

        existingUser.setName(user.getName());
        existingUser.setBio(user.getBio());
        existingUser.setImage(user.getImage());

        userRepository.update(existingUser);
    }

    public UserStats getUserStats(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("User not found"));
        return userRepository.getUserStats(user.getId());
    }




}