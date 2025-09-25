package lk.school.elite_driving.exception;

public class InvalidCredentialsException extends LoginException {
    public InvalidCredentialsException(String message) {
        super(message);
    }
}