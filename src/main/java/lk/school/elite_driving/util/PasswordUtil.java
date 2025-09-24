package lk.school.elite_driving.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {
    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static boolean checkPassword(String password, String hashedPassword) {
        if (hashedPassword == null) {
            throw new IllegalArgumentException("Hashed password is null");
        }
        return BCrypt.checkpw(password, hashedPassword);
    }
}
