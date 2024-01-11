package at.technikum.apps.mtcg.util;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class PasswordHashingUtil {

    public static String hashPassword(String plainTextPassword) {
        return BCrypt.withDefaults().hashToString(12, plainTextPassword.toCharArray());
    }

    public static boolean checkPassword(String plainTextPassword, String hashedPassword) {
        BCrypt.Result result = BCrypt.verifyer().verify(plainTextPassword.toCharArray(), hashedPassword);
        return result.verified;
    }
}